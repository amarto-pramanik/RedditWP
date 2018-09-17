package net.amarto.redditwp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import net.dean.jraw.RedditClient;
import net.dean.jraw.android.AndroidHelper;
import net.dean.jraw.android.ManifestAppInfoProvider;
import net.dean.jraw.android.SharedPreferencesTokenStore;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.oauth.AccountHelper;
import net.dean.jraw.pagination.DefaultPaginator;

import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("ALL")
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
	private EndlessRecyclerViewScrollListener scrollListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		submissionsRecyclerView = findViewById(R.id.recyclerview_submissions);
		layoutManager = new LinearLayoutManager(this);
		submissionsRecyclerView.setLayoutManager(layoutManager);


		Disposable d = getMainClient().flatMap(new Function<RedditClient, ObservableSource<? extends Listing<Submission>>>() {
			@Override
			public ObservableSource<? extends Listing<Submission>> apply(RedditClient redditClient) throws Exception {
				return MainActivity.this.getSubmissions();
			}
		})
				.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<Listing<Submission>>() {
					@Override
					public void accept(Listing<Submission> submissions) throws Exception {
						submissionsAdapter = new SubmissionAdapter(submissions);
						submissionsRecyclerView.setAdapter(submissionsAdapter);
					}
				}, throwable -> {
					Log.e(TAG, "onCreate: Error!:" + throwable.getMessage());
					Toast.makeText(this, "Couldn't get submissions. Please try again later.", Toast.LENGTH_LONG).show();
				});
		scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
			@Override
			public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
				Disposable d = getSubmissions()
						.subscribeOn(Schedulers.newThread())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(new Consumer<Listing<Submission>>() {
							@Override
							public void accept(Listing<Submission> submissions) throws Exception {
								submissionsAdapter.add(submissions);
								Log.d(TAG, "accept: Got new submissions of size: " + submissions.size());
							}
						}, throwable -> {
							Log.e(TAG, "onLoadMore: Couldn't get new submissions!: " + throwable.getMessage());
							Toast.makeText(MainActivity.this, "Couldn't get new submissions. Please try again later.", Toast.LENGTH_SHORT).show();

						});
			}
		};
		submissionsRecyclerView.addOnScrollListener(scrollListener);
	}


	public Observable<RedditClient> getMainClient() {
		return Observable.create(new ObservableOnSubscribe<RedditClient>() {
			@Override
			public void subscribe(ObservableEmitter<RedditClient> emitter) throws Exception {
				UUID deviceUUID = UUID.randomUUID();
				//UserAgent userAgent = new UserAgent("android", "net.amarto", "0.1", "meatspin6969");
				SharedPreferencesTokenStore tokenStore = new SharedPreferencesTokenStore(MainActivity.this.getApplicationContext());
				ManifestAppInfoProvider provider = new ManifestAppInfoProvider(MainActivity.this.getApplicationContext());
				tokenStore.load();
				tokenStore.setAutoPersist(true);

				//Credentials oauthCreds = Credentials.userlessApp(ManifestAppInfoProvider.KEY_CLIENT_ID, deviceUUID);
				AccountHelper helper = AndroidHelper.accountHelper(provider, deviceUUID, tokenStore);

				client = helper.switchToUserless();
				Log.d(TAG, "getMainClient: Got a redditClient: " + client.toString());
				paginator = client.subreddit("WritingPrompts")
						.posts().build();
				Log.d(TAG, "getMainClient: Got a paginator: " + paginator.getBaseUrl());
				emitter.onNext(client);
				emitter.onComplete();
			}
		});
	}

	public Observable<Listing<Submission>> getSubmissions() {
		return Observable.create(new ObservableOnSubscribe<Listing<Submission>>() {
			@Override
			public void subscribe(ObservableEmitter<Listing<Submission>> emitter) throws Exception {
				Listing<Submission> submissions = paginator.next();
				Log.d(TAG, "getSubmissions: Got submissions of size: " + submissions.size());
				emitter.onNext(submissions);
			}
		});
	}
}

