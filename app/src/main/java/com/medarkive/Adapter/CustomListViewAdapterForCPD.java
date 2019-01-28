package com.medarkive.Adapter;

import java.util.ArrayList;

import rmn.androidscreenlibrary.ASSL;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medarkive.R;
import com.medarkive.Beans.CPDBean;
import com.medarkive.Main.CircleChartView;
import com.medarkive.Main.DisplayDataActivity;

public class CustomListViewAdapterForCPD extends BaseAdapter {
	private ArrayList<CPDBean> mData;
	private Context ctx;
	ViewHolder holder;

	public CustomListViewAdapterForCPD(ArrayList<CPDBean> arrlst, Context ctx) {
		mData = new ArrayList<CPDBean>();
		mData = arrlst;
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		return mData.size() + 1;
	}

	@Override
	public CPDBean getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO implement you own logic with ID
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.row_credit_detail, parent, false);

			holder = new ViewHolder();
			holder.summaryCreditDetail = (TextView) convertView
					.findViewById(R.id.summary_credit_detail);
			holder.circleChatView = (CircleChartView) convertView
					.findViewById(R.id.summary_progress);
			holder.summaryDeatil = (TextView) convertView
					.findViewById(R.id.summary);
			holder.frwBtn = (ImageView) convertView
					.findViewById(R.id.summary_frwd_bt);
			holder.rv = (RelativeLayout) convertView.findViewById(R.id.rv);

			convertView.setTag(holder);
			holder.rv.setLayoutParams(new ListView.LayoutParams(720, 200));
			ASSL.DoMagic(holder.rv);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == mData.size()) {
			holder.summaryDeatil.setText("Reading Tracker");
			holder.summaryCreditDetail.setVisibility(8);
			holder.circleChatView.setVisibility(8);
			holder.rv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((DisplayDataActivity)ctx).trackerFragmentCall();
				}
			});
		} else {
			CPDBean item = getItem(position);
			AssetManager assetManager = ctx.getAssets();
			Typeface tf = Typeface.createFromAsset(assetManager,
					"gill-sans.ttf");

			// TODO replace findViewById by ViewHolder
			// TextView summary = (TextView) result.findViewById(R.id.summary);
			holder.summaryDeatil.setText(item.getName());
			holder.summaryDeatil.setTypeface(tf);

			AssetManager assetManager1 = ctx.getAssets();
			Typeface tfLight = Typeface.createFromAsset(assetManager1,
					"gill-sans-light.ttf");

			// TextView creditDetail =
			// (TextView)result.findViewById(R.id.summary_credit_detail);
//			Log.v("check val ", item.getCredit()+",");
//			Log.v("check val credit detail ", item.getCredit_detail()+",");
			holder.summaryCreditDetail.setText(item.getCredit_detail());
			holder.summaryCreditDetail.setTypeface(tfLight);

			holder.circleChatView.setPercentage(item.getCredit_percentage());
			holder.frwBtn.setTag(item.getName());
		}
		return convertView;
	}

	static class ViewHolder {
		public TextView summaryCreditDetail, summaryDeatil;
		public CircleChartView circleChatView;
		public ImageView frwBtn;
		public RelativeLayout rv;
	}
}
