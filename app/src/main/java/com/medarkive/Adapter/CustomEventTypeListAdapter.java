package com.medarkive.Adapter;

import java.util.List;

import rmn.androidscreenlibrary.ASSL;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medarkive.R;
import com.medarkive.Utilities.Item;

public class CustomEventTypeListAdapter extends BaseAdapter {
	private List<Item> mData;
	private Context ctx;
	ViewHolder holder;

	public CustomEventTypeListAdapter(List<Item> dataForTheAdapter, Context ctx) {
		mData = dataForTheAdapter;
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Item getItem(int position) {
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
					R.layout.row_event_type, parent, false);
			holder = new ViewHolder();
			holder.eventType = (TextView) convertView.findViewById(R.id.event_type_txt);
			holder.rv = (RelativeLayout) convertView.findViewById(R.id.rv);
			holder.rv.setLayoutParams(new ListView.LayoutParams(720 , 140));
			ASSL.DoMagic(holder.rv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		final Item Item = getItem(position);
		// TODO replace findViewById by ViewHolder
		if (Item.getText() != null) {
//			TextView eventType = (TextView) result.findViewById(R.id.event_type_txt);
			AssetManager assetManager = ctx.getAssets();
			Typeface tf = Typeface.createFromAsset(assetManager,"gill-sans-light.ttf");
			holder.eventType.setText(Item.getText());
			holder.eventType.setTag(Item.getPosition());
			holder.eventType.setTypeface(tf);
		}
		return convertView;
	}
	
	static class ViewHolder{
		public RelativeLayout rv;
		public TextView eventType;
		
	}

}
