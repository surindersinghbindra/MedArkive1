package com.medarkive.Adapter;

import java.io.Serializable;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.medarkive.Beans.PDFBean;
import com.medarkive.Fragments.UserDetail;

@SuppressWarnings("serial")
public class CollectionPagerAdapter extends FragmentStatePagerAdapter implements Serializable {
	List<PDFBean> list;

	public CollectionPagerAdapter(FragmentManager fm, List<PDFBean> pdfList) {
		super(fm);
		list = pdfList;
	}

	@Override
	public float getPageWidth(int position) {

		return 0.75f;
	}

	@Override
	public Fragment getItem(int i) {
		PDFBean pdf = list.get(i);
		Fragment fragment = new UserDetail();
		Bundle args = new Bundle();
		args.putSerializable("userDetail",pdf);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
}