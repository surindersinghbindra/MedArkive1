package com.medarkive.Utilities;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable{
	    private String mText;
	    private int mPosition;

	    public Item(String text, int position) {

	        this.mText = text;
	        this.mPosition = position;
	    }

	    public int getPosition() {

	        return mPosition;
	    }

	    public void setPosition(int position) {

	        this.mPosition = position;
	    }

	    public String getText() {

	        return mText;
	    }

	    public void setText(String text) {

	        this.mText = text;
	    }

	    @Override
	    public String toString() {

	        return this.mText;
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
