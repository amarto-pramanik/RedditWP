package net.amarto.redditwp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;

import java.util.List;

public abstract class FooterLoaderAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	protected boolean showLoader;
	private static final int VIEWTYPE_ITEM = 1;
	private static final int VIEWTYPE_LOADER = 2;

	protected Listing<Submission> submissions;
	protected LayoutInflater layoutInflater;

	public FooterLoaderAdapter(Context context) {
		layoutInflater = LayoutInflater.from(context);
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		if (viewType == VIEWTYPE_LOADER) {
			View view = layoutInflater.inflate(R.layout.loader_item_layout, viewGroup, false);

			return new LoaderViewHolder(view);
		} else if (viewType == VIEWTYPE_ITEM) {
			return getYourItemViewHolder(viewGroup);
		}

		throw new IllegalArgumentException("Invalid ViewType: " + viewType);

	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof LoaderViewHolder) {
			LoaderViewHolder loaderViewHolder = (LoaderViewHolder)holder;
			if (showLoader) {
				loaderViewHolder.progressBar.setVisibility(View.VISIBLE);
			} else {
				loaderViewHolder.progressBar.setVisibility(View.GONE);
			}

			return;
		}

		bindYourViewHolder(holder, position);
	}

	@Override
	public int getItemCount() {
		if (submissions == null || submissions.size() == 0) {
			return 0;
		}

		return submissions.size() + 1;
	}

	@Override
	public long getItemId(int position) {
		if (position != 0 && position == getItemCount() - 1) {
			return -1;
		}
		return getYourItemId(position);
	}

	@Override
	public int getItemViewType(int position) {
		if (position != 0 && position == getItemCount() - 1) {
			return VIEWTYPE_LOADER;
		}
		return VIEWTYPE_ITEM;
	}

	public void showLoading(boolean status) {
		showLoader = status;
	}

	public void setItems(Listing<Submission> submissions) {
		this.submissions = submissions;
	}

	public abstract long getYourItemId(int position);
	public abstract RecyclerView.ViewHolder getYourItemViewHolder(ViewGroup parent);
	public abstract void bindYourViewHolder(RecyclerView.ViewHolder holder, int position);
}
