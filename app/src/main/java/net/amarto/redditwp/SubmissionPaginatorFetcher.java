package net.amarto.redditwp;

import android.content.Context;
import android.os.AsyncTask;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.pagination.Paginator;

public final class SubmissionPaginatorFetcher extends AsyncTask<Void, Void, DefaultPaginator<Submission>>{
	private RedditClient client;
	private Listing<Submission> submissions;
	private DefaultPaginator<Submission> paginator;

	public SubmissionPaginatorFetcher(RedditClient client) {
		this.client = client;
	}

	@Override
	protected DefaultPaginator<Submission> doInBackground(Void... voids) {
		paginator = client.subreddit("WritingPrompts")
				.posts().limit(2).build();

		Listing<Submission> submissions= paginator.next();

		return paginator;
	}

	public Listing<Submission> getSubmissions() {
		return this.submissions;
	}
}
