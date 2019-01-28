package com.medarkive.Adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.medarkive.R;
import com.medarkive.Beans.EbookChapterBean;
import java.util.ArrayList;

public class CustomArrayAdapterForEbook extends ArrayAdapter<EbookChapterBean> {
	private Activity activity;
	private ArrayList<EbookChapterBean> data;
	LayoutInflater inflater;
	public Resources res;
	EbookChapterBean ebookBeanObj = null;

	public CustomArrayAdapterForEbook(Activity paramActivity, int view, ArrayList<EbookChapterBean> paramArrayList, Resources paramResources) {
		super(paramActivity, view, paramArrayList);
		this.activity = paramActivity;
		this.data = paramArrayList;
		this.res = paramResources;
		this.inflater = ((LayoutInflater) this.activity.getSystemService("layout_inflater"));
	}

	public View getCustomView(int pos, View paramView, ViewGroup paramViewGroup) {
		View contentView = this.inflater.inflate(R.layout.chapter_item, paramViewGroup, false);
		this.ebookBeanObj = null;
		this.ebookBeanObj = ((EbookChapterBean) this.data.get(pos));
		TextView chapterTitle = (TextView) contentView.findViewById(R.id.chapter_title);
		TextView chapterName = (TextView) contentView.findViewById(R.id.chapter_name);
		chapterTitle.setText(this.ebookBeanObj.getChapterTitle());
		chapterName.setText(this.ebookBeanObj.getChapterName());
		contentView.setTag(this.ebookBeanObj.getChapterId());
		return contentView;
	}

	public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		return getCustomView(paramInt, paramView, paramViewGroup);
	}

	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		return getCustomView(paramInt, paramView, paramViewGroup);
	}
}
