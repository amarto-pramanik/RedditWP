package net.amarto.redditwp;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.aware.SubscribeConfig;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;

import java.util.List;

public class SubmissionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private Listing<Submission> submissions;
	private static final String TAG = SubmissionAdapter.class.getSimpleName();

	public static class MyViewHolder extends RecyclerView.ViewHolder {
		public TextView submissionTitle;
		public ProgressBar progressbar;

		public MyViewHolder(View v) {
			super(v);
			submissionTitle = v.findViewById(R.id.submissionTitle);
		}
	}

	public SubmissionAdapter(Listing<Submission> submissions) {
		this.submissions = submissions;
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
	                                                  int viewType) {
		View v = (View) LayoutInflater.from(parent.getContext())
				.inflate(R.layout.submissions_list_item, parent, false);
		return new MyViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
		MyViewHolder holder = (MyViewHolder) viewHolder;
		holder.submissionTitle.setText(submissions.get(i).getTitle());
		viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "onClick: Clicked on submissions: " + submissions.get(i).getTitle());
				Intent intent = new Intent(v.getContext(), CommentActivity.class);
				intent.putExtra("SUBMISSION", submissions.get(i).getId());
				v.getContext().startActivity(intent);
			}
		});
	}
//
//	@Override
//	public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
//		viewHolder.submissionTitle.setText(submissions.get(i).getTitle());
//	}

	@Override
	public int getItemCount() {
		return submissions.getChildren().size();
	}

	public void add(Listing<Submission> newSubmissions) {
		submissions.addAll(newSubmissions);
		Log.d(TAG, "add: Added new submissions. New submission list size: " + submissions.size());
		notifyDataSetChanged();
	}
}