package net.amarto.redditwp;

import android.content.Context;
import android.os.AsyncTask;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.pagination.Paginator;

public final class SubmissionFetcher extends AsyncTask<Void, Void, Listing<Submission>>{
	private RedditClient client;
	private Listing<Submission> submissions;
	private DefaultPaginator<Submission> paginator;

	public SubmissionFetcher(RedditClient client) {
		this.client = client;
	}

	@Override
	protected Listing<Submission> doInBackground(Void... voids) {
		paginator = client.subreddit("WritingPrompts")
				.posts().build();

		this.submissions = paginator.next();
		return submissions;
	}

	public Listing<Submission> getSubmissions() {
		return this.submissions;
	}
}
