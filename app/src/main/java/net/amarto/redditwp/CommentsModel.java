package net.amarto.redditwp;

import java.util.Objects;

public class CommentsModel {
	private String topLevelCommentText;
	private int numberOfChildren;

	public CommentsModel(String topLevelCommentText, int numberOfChildren) {
		this.topLevelCommentText = topLevelCommentText;
		this.numberOfChildren = numberOfChildren;
	}

	public String getTopLevelCommentText() {
		return topLevelCommentText;
	}

	public void setTopLevelCommentText(String topLevelCommentText) {
		this.topLevelCommentText = topLevelCommentText;
	}

	public int getNumberOfChildren() {
		return numberOfChildren;
	}

	public void setNumberOfChildren(int numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}

	@Override
	public String toString() {
		return "CommentsModel{" +
				"topLevelCommentText='" + topLevelCommentText + '\'' +
				", numberOfChildren=" + numberOfChildren +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CommentsModel that = (CommentsModel) o;
		return numberOfChildren == that.numberOfChildren &&
				Objects.equals(topLevelCommentText, that.topLevelCommentText);
	}

	@Override
	public int hashCode() {

		return Objects.hash(topLevelCommentText, numberOfChildren);
	}
}
