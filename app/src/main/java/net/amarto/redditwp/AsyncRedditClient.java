package net.amarto.redditwp;

import android.content.Context;
import android.os.AsyncTask;

import net.dean.jraw.RedditClient;
import net.dean.jraw.android.AndroidHelper;
import net.dean.jraw.android.AppInfoProvider;
import net.dean.jraw.android.ManifestAppInfoProvider;
import net.dean.jraw.android.SharedPreferencesTokenStore;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.oauth.AccountHelper;
import net.dean.jraw.oauth.Credentials;

import java.lang.ref.WeakReference;
import java.util.UUID;

public class AsyncRedditClient extends AsyncTask<Context, Void, Void> implements RedditServiceInterface{
	private static final String TAG = AsyncRedditClient.class.getSimpleName();
	private UUID deviceUUID = UUID.randomUUID();
	private UserAgent userAgent = new UserAgent("android", "net.amarto", "0.1", "meatspin6969");
	private WeakReference<Context> weakContext;
	private AppInfoProvider provider;
	private SharedPreferencesTokenStore tokenStore;
	private static RedditClient client;
	NetworkAdapter networkAdapter = new OkHttpNetworkAdapter(userAgent);

	public AsyncRedditClient(Context context) {
		this.weakContext = new WeakReference<>(context);
	}

	@Override
	public void authenticateUserless() throws NetworkException {

	}

	@Override
	public RedditClient getRedditClient() {
		return client;
	}


	@Override
	protected Void doInBackground(Context... contexts) {
		if (contexts[0] != null) {
			tokenStore = new SharedPreferencesTokenStore(contexts[0]);
			provider = new ManifestAppInfoProvider(contexts[0]);
		}
		// Load stored tokens into memory
		tokenStore.load();
		// Automatically save new tokens as they arrive
		tokenStore.setAutoPersist(true);

		Credentials oauthCreds = Credentials.userlessApp(ManifestAppInfoProvider.KEY_CLIENT_ID, deviceUUID);
		AccountHelper helper = AndroidHelper.accountHelper(provider, deviceUUID, tokenStore);

		client = helper.switchToUserless();

		return null;
	}
}
