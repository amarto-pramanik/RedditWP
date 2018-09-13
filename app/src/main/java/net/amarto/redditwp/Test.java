package net.amarto.redditwp;

import android.util.Log;

import net.dean.jraw.RedditClient;
import net.dean.jraw.android.ManifestAppInfoProvider;
import net.dean.jraw.android.SharedPreferencesTokenStore;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;

import java.util.UUID;

public class Test {

    public static void main(String[] ae) {
            UUID deviceUUID = UUID.randomUUID();
            UserAgent userAgent = new UserAgent("android", "net.amarto.redditwp", "0.1", "meatspin6969");
            NetworkAdapter networkAdapter = new OkHttpNetworkAdapter(userAgent);
            Credentials oauthCreds = Credentials.userlessApp("17wSyG_36msVyA", deviceUUID);
            RedditClient mainclient = OAuthHelper.automatic(networkAdapter, oauthCreds);

            Log.e("kidfgo", "wow done");

//        try {
//            UUID deviceUUID = UUID.randomUUID();
//            UserAgent userAgent = new UserAgent("android", "net.amarto", "0.1", "meatspin6969");
//            NetworkAdapter networkAdapter = new OkHttpNetworkAdapter(userAgent);
//            Credentials oauthCreds = Credentials.userlessApp(ManifestAppInfoProvider.KEY_CLIENT_ID, deviceUUID);
//            RedditClient mainclient = OAuthHelper.automatic(networkAdapter, oauthCreds);
//            Log.e("kidfgo", "wow done");
//        } catch (Exception e) {
//            Log.e("kuig", "yiufkrctjhterdxrtjxfcthedrfx" + e.getMessage());
//        }
    }
}
