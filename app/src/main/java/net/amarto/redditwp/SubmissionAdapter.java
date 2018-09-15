package net.amarto.redditwp;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;

import java.util.List;

public class SubmissionAdapter extends PagedListAdapter<Submission, SubmissionAdapter.MyViewHolder> {
	private Listing<Submission> submissions;

	public static class MyViewHolder extends RecyclerView.ViewHolder {
		public TextView submissionTitle;

		public MyViewHolder(View v) {
			super(v);
			submissionTitle = v.findViewById(R.id.submissionTitle);
		}
	}

	public SubmissionAdapter(Listing<Submission> submissions) {
		super(DIFF_CALLBACK);
		this.submissions = submissions;
	}

	public static final DiffUtil.ItemCallback<Submission> DIFF_CALLBACK =
			new DiffUtil.ItemCallback<Submission>() {
				@Override
				public boolean areItemsTheSame(Submission oldItem, Submission newItem) {
					return oldItem.getId() == newItem.getId();
				}
				@Override
				public boolean areContentsTheSame(Submission oldItem, Submission newItem) {
					return (oldItem.getTitle().equals(newItem.getTitle()));
				}
			};

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
		viewHolder.submissionTitle.setText(getItem(i).getTitle());
	}

	public void addMoreSubmissions(Listing<Submission> newSubmissions) {
		submissions.addAll(newSubmissions);
	}
}
