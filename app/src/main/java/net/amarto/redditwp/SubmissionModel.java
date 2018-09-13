package net.amarto.redditwp;

public class SubmissionModel {
	private String title;

	public SubmissionModel(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return title;
	}

}
