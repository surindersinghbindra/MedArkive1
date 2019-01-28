package com.medarkive.Fragments;

import java.util.List;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.medarkive.R;
import com.medarkive.Adapter.CollectionPagerAdapter;
import com.medarkive.Beans.PDFBean;
import com.medarkive.Main.DisplayDataActivity;
import com.medarkive.Utilities.ConnectionDetector;
import com.medarkive.Utilities.DatabaseHandler;

public class PDFTabsFragment extends Fragment {

	// private String pdf_url = "";
	private ViewPager mViewPager;
	private CollectionPagerAdapter collectionPagerAdapter;
	private static DatabaseHandler db;
	private List<PDFBean> pdfList;
	ConnectionDetector cd;

	// static DownloadTask download;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		db = new DatabaseHandler(getActivity().getApplicationContext());
		pdfList = db.getAllPDF();
		// collectionPagerAdapter = new CollectionPagerAdapter(
		// getChildFragmentManager(), pdfList);
		cd = new ConnectionDetector(getActivity());
		// download = new DownloadTask(getActivity());
	}
	
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
		// collectionPagerAdapter = new CollectionPagerAdapter(
		// getChildFragmentManager(), pdfList);
		// ((DisplayDataActivity)getActivity()).setCustomActionBarHome();
		((DisplayDataActivity) getActivity()).showbottomBar();
		View view = inflater.inflate(R.layout.fragment_pdftabs, container, false);

		new ASSL(getActivity(), (LinearLayout) view.findViewById(R.id.content), 1134, 720, false);
		mViewPager = (ViewPager) view.findViewById(R.id.pager);
		collectionPagerAdapter = new CollectionPagerAdapter(getChildFragmentManager(), pdfList);

		mViewPager.setAdapter(collectionPagerAdapter);

		return view;
	}

}
