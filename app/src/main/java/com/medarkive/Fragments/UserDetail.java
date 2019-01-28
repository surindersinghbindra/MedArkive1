package com.medarkive.Fragments;

import java.io.File;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medarkive.R;
import com.medarkive.Beans.PDFBean;
import com.medarkive.Main.LoginActivity;

public class UserDetail extends android.support.v4.app.Fragment {
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    if(isVisibleToUser) {
	        Activity a = getActivity();
	        if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		View rootView = null;
		AssetManager assetManager1 = getActivity().getAssets();
		Typeface tfLight = Typeface.createFromAsset(assetManager1, "gill-sans-light.ttf");

		AssetManager assetManager = getActivity().getAssets();
		Typeface tf = Typeface.createFromAsset(assetManager, "gill-sans.ttf");

		rootView = inflater.inflate(R.layout.userdteail, container, false);
		RelativeLayout rv = (RelativeLayout) rootView.findViewById(R.id.rv);
		new ASSL(getActivity(), rv, 1134, 720, false);
		Bundle args = getArguments();
		PDFBean pdf = (PDFBean) args.getSerializable("userDetail");
		TextView title = (TextView) rootView.findViewById(R.id.title);
		title.setText(pdf.getPDF_TITLE());
		title.setTypeface(tf);

		TextView subTitle = (TextView) rootView.findViewById(R.id.sub_title);
		subTitle.setText(pdf.getPDF_SUB_TITLE());
		subTitle.setTypeface(tfLight);

		String thumbName = pdf.getPDF_THUMB().toString().substring(pdf.getPDF_THUMB().toString().lastIndexOf('/') + 1, pdf.getPDF_THUMB().toString().length());
		thumbName = thumbName.replaceAll("\\s+", "%20");
		File thumb = new File(LoginActivity.path + thumbName);
		if (thumb.exists()) {
			Bitmap bm = BitmapFactory.decodeFile(thumb.getAbsolutePath());
			((ImageView) rootView.findViewById(R.id.thumb)).setImageBitmap(bm);
			((ImageView) rootView.findViewById(R.id.thumb)).setTag(pdf.getPDF_ID());
		} else {
			((ImageView) rootView.findViewById(R.id.thumb)).setBackgroundResource(R.drawable.ic_default);
			((ImageView) rootView.findViewById(R.id.thumb)).setTag(pdf.getPDF_ID());
		}
		((ProgressBar) rootView.findViewById(R.id.progress)).setTag(pdf.getPDF_ID() + "pb");
		return rootView;
	}
}
