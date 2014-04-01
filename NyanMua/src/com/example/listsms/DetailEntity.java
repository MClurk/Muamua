package com.example.listsms;

public class DetailEntity {
	private String text;
	private int layoutID;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getLayoutID() {
		return layoutID;
	}

	public void setLayoutID(int layoutID) {
		this.layoutID = layoutID;
	}

	public DetailEntity() {
	}

	public DetailEntity(String text, int layoutID) {
		super();
		this.text = text;
		this.layoutID = layoutID;
	}

}
