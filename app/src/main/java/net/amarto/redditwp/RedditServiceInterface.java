package net.amarto.redditwp;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Submission;
import net.dean.jraw.oauth.Credentials;

import java.util.List;
import java.util.UUID;

public interface RedditServiceInterface {
	public static final String VERSION_NAME = "0.1";
	public static final String PACKAGE_NAME = "net.amarto.redditWP";
	public static final String REDDIT_USERNAME = "meatspin6969";
	public static final UserAgent REDDITME_USERAGENT = new UserAgent("android", PACKAGE_NAME, VERSION_NAME, REDDIT_USERNAME);
	public static final Credentials USERLESS = Credentials.userlessApp("hsro81jAMJgXhw", UUID.randomUUID());

	public void authenticateUserless() throws NetworkException;

	public RedditClient getRedditClient();
}
