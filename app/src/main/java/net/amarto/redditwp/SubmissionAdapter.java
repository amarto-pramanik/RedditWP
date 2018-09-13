package net.amarto.redditwp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;

import java.util.List;

public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.MyViewHolder> {
	private Listing<Submission> submissions;

	public static class MyViewHolder extends RecyclerView.ViewHolder {
		public TextView submissionTitle;

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
	public MyViewHolder onCreateViewHolder(ViewGroup parent,
										 int viewType) {
		View v = (View) LayoutInflater.from(parent.getContext())
				.inflate(R.layout.submissions_list_item, parent, false);

		MyViewHolder vh = new MyViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
		viewHolder.submissionTitle.setText(submissions.get(i).getTitle());
	}

	@Override
	public int getItemCount() {
		return submissions.getChildren().size();
	}
}