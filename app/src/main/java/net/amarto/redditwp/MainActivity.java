package net.amarto.redditwp;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.dean.jraw.RedditClient;
import net.dean.jraw.android.AndroidHelper;
import net.dean.jraw.android.AppInfoProvider;
import net.dean.jraw.android.ManifestAppInfoProvider;
import net.dean.jraw.android.SharedPreferencesTokenStore;
import net.dean.jraw.http.NetworkAdapter;
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

import java.util.List;
import java.util.UUID;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {

    private static RedditClient mainclient;
    boolean accountCreated = false;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AuthenticateUserlessClientAsync().execute();
    }

    private class AuthenticateUserlessClientAsync extends AsyncTask<Void, Void, Listing<Submission>> {
        private static final String TAG = "AuthenticateUserlessCli";
        private UUID deviceUUID;
        private SharedPreferencesTokenStore tokenStore;

        @Override
        protected Listing<Submission> doInBackground(Void... voids) {
            try {
                deviceUUID = UUID.randomUUID();
                UserAgent userAgent = new UserAgent("android", "net.amarto", "0.1", "meatspin6969");
                NetworkAdapter networkAdapter = new OkHttpNetworkAdapter(userAgent);

                tokenStore = new SharedPreferencesTokenStore(getApplicationContext());
                // Load stored tokens into memory
                tokenStore.load();
                // Automatically save new tokens as they arrive
                tokenStore.setAutoPersist(true);

                AppInfoProvider provider = new ManifestAppInfoProvider(getApplicationContext());
                Credentials oauthCreds = Credentials.userlessApp(ManifestAppInfoProvider.KEY_CLIENT_ID, deviceUUID);
                AccountHelper helper = AndroidHelper.accountHelper(provider, deviceUUID, tokenStore);

                mainclient = helper.switchToUserless();
                DefaultPaginator<Submission> frontPage = mainclient.subreddit("Purdue")
                        .posts()
                        .limit(30)
                        .build();

               return frontPage.next();
            } catch (Exception e) {
                Log.e(TAG, "yiufkrctjhterdxrtjxfcthedrfx" + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Listing<Submission> submissions) {
            for (Submission s : submissions) {
                System.out.println(s.getTitle());
                Log.e(TAG, s.getTitle());
            }
        }
    }
}

