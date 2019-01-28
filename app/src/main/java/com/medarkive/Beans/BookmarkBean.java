package com.medarkive.Beans;

import android.os.Parcel;
import android.os.Parcelable;

public class BookmarkBean implements Parcelable {

	private String bookmark_id;

	private String bookmark_title;

	private String bookmark_link;

	private String timestamp;
	
	public BookmarkBean(){
		
	}

	public String getBookmark_id() {
		return bookmark_id;
	}

	public void setBookmark_id(String bookmark_id) {
		this.bookmark_id = bookmark_id;
	}

	public String getBookmark_title() {
		return bookmark_title;
	}

	public void setBookmark_title(String bookmark_title) {
		this.bookmark_title = bookmark_title;
	}

	public String getBookmark_link() {
		return bookmark_link;
	}

	public void setBookmark_link(String bookmark_link) {
		this.bookmark_link = bookmark_link;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	private BookmarkBean(Parcel in) {
		// This order must match the order in writeToParcel()
		bookmark_id = in.readString();
		bookmark_link = in.readString();
		bookmark_title = in.readString();
		timestamp = in.readString();
		// Continue doing this for the rest of your member data
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(bookmark_id);
		dest.writeString(bookmark_link);
		dest.writeString(bookmark_title);
		dest.writeString(timestamp);

	}

	public static final Parcelable.Creator<BookmarkBean> CREATOR = new Parcelable.Creator<BookmarkBean>() {
		public BookmarkBean createFromParcel(Parcel in) {
			return new BookmarkBean(in);
		}

		public BookmarkBean[] newArray(int size) {
			return new BookmarkBean[size];
		}
	};
}
