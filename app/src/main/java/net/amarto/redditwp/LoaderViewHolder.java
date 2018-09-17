package net.amarto.redditwp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

public class LoaderViewHolder extends RecyclerView.ViewHolder {
	ProgressBar progressBar;

	public LoaderViewHolder(View v) {
		super(v);
		progressBar = v.findViewById(R.id.progressbar);
	}
}
