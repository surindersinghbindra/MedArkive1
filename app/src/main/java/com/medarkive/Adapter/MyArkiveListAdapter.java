package com.medarkive.Adapter;

import java.util.ArrayList;

import rmn.androidscreenlibrary.ASSL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medarkive.R;
import com.medarkive.Beans.MyArkiveUrls;
import com.medarkive.Main.DisplayDataActivity;

public class MyArkiveListAdapter extends BaseAdapter {

	private LayoutInflater inflator;
	Activity a1;
	String selectedPath;
	ViewHolder holder = null;
	Bitmap bitmap;

	ArrayList<MyArkiveUrls> urlList = new ArrayList<MyArkiveUrls>();

	public MyArkiveListAdapter(Activity a, ArrayList<MyArkiveUrls> urlList) {
		// TODO Auto-generated constructor stub
		super();
		inflator = (LayoutInflater) a
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.urlList = urlList;
		a1 = a;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return urlList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static class ViewHolder {
		public LinearLayout myarkieLay;
		public TextView myarkive_txt;
		public ImageView browserIc;
		public RelativeLayout rv;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {

		if (view == null) {
			view = inflator.inflate(R.layout.row_myarkive, null);
			holder = new ViewHolder();
			holder.myarkive_txt = (TextView) view.findViewById(R.id.list_item);
			holder.myarkieLay = (LinearLayout) view.findViewById(R.id.myarkiveLay);
			holder.browserIc = (ImageView) view.findViewById(R.id.browser_ic);
			holder.rv = (RelativeLayout) view.findViewById(R.id.rv);
			
			holder.rv.setLayoutParams(new ListView.LayoutParams(720	,200));
			ASSL.DoMagic(holder.rv);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.myarkive_txt.setTag(holder);
		
		if(urlList.get(position).getlinkName().equalsIgnoreCase("Dropbox")){
			
			holder.browserIc.getLayoutParams().width = (int)(336 * ASSL.Xscale());
			holder.browserIc.getLayoutParams().height = (int) (113 * ASSL.Yscale());
			holder.myarkive_txt.setVisibility(8);
		}else if(urlList.get(position).getlinkName().equalsIgnoreCase("Google Drive")){
			holder.browserIc.getLayoutParams().width = (int)(336 * ASSL.Xscale());
			holder.browserIc.getLayoutParams().height = (int) (113 * ASSL.Yscale());
			holder.myarkive_txt.setVisibility(8);
		}else if(urlList.get(position).getlinkName().equalsIgnoreCase("OneDrive")){
			holder.browserIc.getLayoutParams().width = (int)(336 * ASSL.Xscale());
			holder.browserIc.getLayoutParams().height = (int) (113 * ASSL.Yscale());
			holder.myarkive_txt.setVisibility(8);
		}
		else if(urlList.get(position).getlinkName().equalsIgnoreCase("Virtually free")){
			holder.browserIc.getLayoutParams().width = (int)(336 * ASSL.Xscale());
			holder.browserIc.getLayoutParams().height = (int) (113 * ASSL.Yscale());
			holder.myarkive_txt.setVisibility(8);
		}else{
			holder.browserIc.getLayoutParams().width = (int) (108 * ASSL.Xscale());
			holder.browserIc.getLayoutParams().height = (int) (113 * ASSL.Yscale());
			holder.myarkive_txt.setVisibility(0);
		}
		
		holder.browserIc.setBackground(urlList.get(position).getIcon());
		holder.browserIc.setTag(holder);
		holder.myarkive_txt.setText(urlList.get(position).getlinkName());
		
		holder.myarkieLay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((DisplayDataActivity)a1).myArkiveLinkClicked(urlList.get(position));
			}
		});
		return view;
	}
}
