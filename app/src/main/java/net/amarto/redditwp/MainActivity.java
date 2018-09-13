package net.amarto.redditwp;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import net.dean.jraw.RedditClient;
import net.dean.jraw.android.AndroidHelper;
import net.dean.jraw.android.AppInfoProvider;
import net.dean.jraw.android.ManifestAppInfoProvider;
import net.dean.jraw.android.SharedPreferencesTokenStore;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.oauth.AccountHelper;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.pagination.Paginator;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {

	/* TODO: Get a simple list of submission titles
	* TODO: Be able to click on a submission and move to different fragment
	* TODO: Show a list of at least top-level comments
	* TODO: Show all child comments when click on top-level comment
	* TODO: Think of more things later */

	private static final String TAG = "MainActivity";
    private Listing<Submission> submissions;

	private RedditClient client;

	private RecyclerView submissionsRecyclerView;
	private RecyclerView.Adapter submissionsAdapter;
	private RecyclerView.LayoutManager submissionsLayoutManager;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		AsyncRedditClient asyncRedditClient = new AsyncRedditClient();
		final RedditClient mainclient;
		try {
			mainclient = asyncRedditClient.execute().get();
			SubmissionFetcher fetcher = new SubmissionFetcher(mainclient);
			submissions = fetcher.execute().get();
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}

		submissionsRecyclerView = findViewById(R.id.recyclerview_submissions);

        submissionsLayoutManager = new LinearLayoutManager(this);
        submissionsRecyclerView.setLayoutManager(submissionsLayoutManager);

        submissionsAdapter = new SubmissionAdapter(submissions);
        submissionsRecyclerView.setAdapter(submissionsAdapter);

    }
	public class AsyncRedditClient extends AsyncTask<Void, Void, RedditClient> {
		private final String TAG = net.amarto.redditwp.AsyncRedditClient.class.getSimpleName();
		private UUID deviceUUID = UUID.randomUUID();
		private UserAgent userAgent = new UserAgent("android", "net.amarto", "0.1", "meatspin6969");
		private AppInfoProvider provider;
		private SharedPreferencesTokenStore tokenStore;

		NetworkAdapter networkAdapter = new OkHttpNetworkAdapter(userAgent);

		@Override
		protected RedditClient doInBackground(Void... voids) {
			tokenStore = new SharedPreferencesTokenStore(getApplicationContext());
			provider = new ManifestAppInfoProvider(getApplicationContext());
			// Load stored tokens into memory
			tokenStore.load();
			// Automatically save new tokens as they arrive
			tokenStore.setAutoPersist(true);

			Credentials oauthCreds = Credentials.userlessApp(ManifestAppInfoProvider.KEY_CLIENT_ID, deviceUUID);
			AccountHelper helper = AndroidHelper.accountHelper(provider, deviceUUID, tokenStore);

			client = helper.switchToUserless();
			Log.e(TAG, "NAJIDIADBASFBIAF" + client.toString());

			return client;
		}
	}
}

