package net.amarto.redditwp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import net.dean.jraw.RedditClient;
import net.dean.jraw.android.AndroidHelper;
import net.dean.jraw.android.ManifestAppInfoProvider;
import net.dean.jraw.android.SharedPreferencesTokenStore;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.oauth.AccountHelper;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.pagination.DefaultPaginator;

import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "MainActivity";

	private RedditClient client;
	private DefaultPaginator<Submission> paginator;

	private boolean loading = false;
	private int pageNumber = 1;
	private final int VISIBLE_THRESHOLD = 1;
	private int lastVisibleItem, totalItemCount;

	private RecyclerView submissionsRecyclerView;
	private SubmissionAdapter submissionsAdapter;
	private LinearLayoutManager layoutManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		submissionsRecyclerView = findViewById(R.id.recyclerview_submissions);
		layoutManager = new LinearLayoutManager(this);
		submissionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

		Disposable d = getMainClient().flatMap(redditClient -> getSubmissions())
				.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(submissions -> {
					submissionsAdapter = new SubmissionAdapter(submissions);
					submissionsRecyclerView.setAdapter(submissionsAdapter);
				}, throwable -> Log.e(TAG, "onCreate: Error!:" + throwable.getMessage()));

	}


	public Observable<RedditClient> getMainClient() {
		return Observable.create(emitter -> {
			UUID deviceUUID = UUID.randomUUID();
			//UserAgent userAgent = new UserAgent("android", "net.amarto", "0.1", "meatspin6969");
			SharedPreferencesTokenStore tokenStore = new SharedPreferencesTokenStore(getApplicationContext());
			ManifestAppInfoProvider provider = new ManifestAppInfoProvider(getApplicationContext());
			tokenStore.load();
			tokenStore.setAutoPersist(true);

			//Credentials oauthCreds = Credentials.userlessApp(ManifestAppInfoProvider.KEY_CLIENT_ID, deviceUUID);
			AccountHelper helper = AndroidHelper.accountHelper(provider, deviceUUID, tokenStore);

			client = helper.switchToUserless();
			paginator = client.subreddit("WritingPrompts")
					.posts().build();
			Log.d(TAG, "getMainClient: Got mainclient and a paginator Username: " + client.me().getUsername());
			emitter.onNext(client);
		});
	}

	public Observable<Listing<Submission>> getSubmissions() {
		return Observable.create(emitter -> {
			Listing<Submission> submissions = paginator.next();
			Log.d(TAG, "getSubmissions: Got submissions of size: " + submissions.size());
			emitter.onNext(submissions);
		});
	}
}

