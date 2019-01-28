package com.medarkive.Fragments;

import java.util.ArrayList;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medarkive.R;
import com.medarkive.Adapter.CustomArrayListAdapterForBookmark;
import com.medarkive.Beans.BookmarkBean;
import com.medarkive.Main.DisplayDataActivity;
import com.medarkive.Main.LoginActivity;

public class BookmarkListFragment extends ListFragment {
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		((DisplayDataActivity)getActivity()).setCustomActionBarBackButton("Bookmarks History");
		View view = inflater.inflate(R.layout.activity_bookmark_list, container,
				false);
		
		RelativeLayout rv = (RelativeLayout) view.findViewById(R.id.rv);
		new ASSL(getActivity(), rv, 1134, 720, false);
		
		ArrayList <BookmarkBean> arr= getArguments().getParcelableArrayList(LoginActivity.Extra_Value);
		
		CustomArrayListAdapterForBookmark adapter = new CustomArrayListAdapterForBookmark(getActivity(), arr);
		setListAdapter(adapter);
		
		return view;
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
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		TextView txt =(TextView) v.findViewById(R.id.bookmark_link_txt);
		((DisplayDataActivity)getActivity()).clickOnBookmarkUrl(txt.getText().toString());
//		FragmentTransaction ftt = getActivity().getSupportFragmentManager()
//				.beginTransaction();
//		Fragment browser = new SimpleWebViewFragment();
//		Bundle bundle = new Bundle();
//		bundle.putString(SimpleWebViewFragment.EXTRA_URL,
//				txt.getText().toString());
//		browser.setArguments(bundle);
//		// }
//		ftt.replace(R.id.content, browser);
//		ftt.addToBackStack(null);
//		ftt.commit();
	}

	
	
	
}
