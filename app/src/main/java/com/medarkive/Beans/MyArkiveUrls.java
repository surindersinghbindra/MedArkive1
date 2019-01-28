package com.medarkive.Beans;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class MyArkiveUrls implements Parcelable{
    private String linkName;
    private String link;
    private Drawable icon;

    public MyArkiveUrls(){}
    public MyArkiveUrls(String text, String position) {

        this.linkName = text;
        this.link = position;
    }
    
    public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getlink() {

        return link;
    }

    public void setlink(String link) {

        this.link = link;
    }

    public String getlinkName() {

        return linkName;
    }

    public void setlinkName(String linkName) {

        this.linkName = linkName;
    }

    @Override
    public String toString() {

        return this.linkName;
    }

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}


}
