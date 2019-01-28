/*
 * Auther sam
 * class for display data in list of CPD events. 
 */

package com.medarkive.Fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.medarkive.R;
import com.medarkive.Adapter.CustomListAdapterForEvents;
import com.medarkive.Beans.CPDBean;
import com.medarkive.Main.DisplayDataActivity;
import com.medarkive.Main.LoginActivity;

public class EventDetailsListFragment extends ListFragment {

	private ArrayList<CPDBean> arrList = null;
	private ArrayList<CPDBean> arrListforAdapter = null;
	private CustomListAdapterForEvents adapter;
	public static final String EVENT_DESCRIPTION = "event_description";

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    if(isVisibleToUser) {
	        Activity a = getActivity();
	        if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		arrList = getArguments().getParcelableArrayList(
				LoginActivity.Extra_Value);
		arrListforAdapter = new ArrayList<CPDBean>();
		arrListforAdapter.addAll(arrList);
		Collections.sort(arrListforAdapter, new Comparator<CPDBean>() {

			@Override
			public int compare(CPDBean lhs, CPDBean rhs) {
				// TODO Auto-generated method stub
				if (lhs.getCpd_event_id() == null
						|| rhs.getCpd_event_id() == null) {
					return 0;
				}
				int lhsId = Integer.parseInt(lhs.getCpd_event_id());
				int rhsId = Integer.parseInt(rhs.getCpd_event_id());
//				boolean result = lhsId < rhsId;
				return (lhsId > rhsId ? -1 : (lhsId == rhsId ? 0 : 1));
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.event_deatil_list_fragment,
				container, false);

		RelativeLayout rv = (RelativeLayout) view.findViewById(R.id.rv);
		new ASSL(getActivity(), rv, 1134, 720, false);
		
//		((DisplayDataActivity) getActivity())
//				.setCustomActionBarBackButton("Event Detail List");
		Iterator<CPDBean> iitr = arrListforAdapter.iterator();
		while (iitr.hasNext()) {
			CPDBean bean = iitr.next();
			if (bean.getEvent_description() == null && bean.getCredit() == null) {
				iitr.remove();
			}
			if (bean.getName().equals("Summary")) {
				view.findViewById(R.id.add_event).setVisibility(Button.GONE);
				view.findViewById(R.id.imageView1)
						.setVisibility(ImageView.GONE);
			} else {
				view.findViewById(R.id.add_event).setVisibility(Button.VISIBLE);
				view.findViewById(R.id.imageView1).setVisibility(
						ImageView.VISIBLE);
			}
		}
		if (arrListforAdapter != null) {
			adapter = new CustomListAdapterForEvents(arrListforAdapter,
					getActivity());
			setListAdapter(adapter);
		}
//		SessionManager sm = new SessionManager(getActivity());
//		final String cpdChoice = sm.getCPDChoice();
		view.findViewById(R.id.add_event).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						((DisplayDataActivity)getActivity()).eventDetailListFragmentCall(arrList);
						
//						if (cpdChoice.contains("2")) {
//							FragmentTransaction ft = getActivity()
//									.getSupportFragmentManager()
//									.beginTransaction();
//							EventDescriptionFragment event = new EventDescriptionFragment();
//							Bundle bdl = new Bundle();
//							// System.out
//							// .println("array list size     iiuiiiui----"
//							// + arrList.size());
//							bdl.putParcelableArrayList(
//									LoginActivity.Extra_Value, arrList);
//							event.setArguments(bdl);
//							ft.replace(R.id.content, event);
//							ft.addToBackStack(null);
//							ft.commit();
//						} else if (cpdChoice.contains("1")) {
//							FragmentTransaction ft = getActivity()
//									.getSupportFragmentManager()
//									.beginTransaction();
//							if(arrList.size() >0){
//							if(arrList.get(0).getName().equals("Other Activity")){
//								EventDescriptionFragment event = new EventDescriptionFragment();
//								Bundle bdl = new Bundle();
//								// System.out
//								// .println("array list size     iiuiiiui----"
//								// + arrList.size());
//								bdl.putParcelableArrayList(
//										LoginActivity.Extra_Value, arrList);
//								event.setArguments(bdl);
//								ft.replace(R.id.content, event);
//								ft.addToBackStack(null);
//								ft.commit();
//							}else{
//								
//								EvetTypeListFragment evetType = new EvetTypeListFragment();
//								Bundle bdl = new Bundle();
//								bdl.putParcelableArrayList(
//										LoginActivity.Extra_Value, arrList);
//								evetType.setArguments(bdl);
//								ft.replace(R.id.content, evetType);
//								ft.addToBackStack(null);
//								ft.commit();
//							}
//							}
//						}
						
//						((DisplayDataActivity)getActivity()).view.setVisibility(8);
//						((DisplayDataActivity)getActivity()).editName("Event Detail");
//						ActionBar mActionBar = getActivity().getActionBar();
//						LayoutInflater mInflater = LayoutInflater
//								.from(getActivity());
//
//						View mCustomView = mInflater.inflate(
//								R.layout.custom_actionbar_back_button, null);
//						mActionBar.setCustomView(mCustomView);
//						mActionBar.setDisplayShowCustomEnabled(true);
					}
				});
		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		CPDBean obj = adapter.getItem(position);
		((DisplayDataActivity) getActivity())
		.setCustomActionBarBackButton("Event Description");
//		FragmentTransaction ft = getActivity().getSupportFragmentManager()
//				.beginTransaction();
//		EventDescriptionFragment event = new EventDescriptionFragment();
		Bundle bdl = new Bundle();
		bdl.putParcelable(EVENT_DESCRIPTION, obj);
//		event.setArguments(bdl);
//		ft.replace(R.id.content, event);
//		ft.addToBackStack(null);
//		ft.commit();
		((DisplayDataActivity) getActivity()).eventDecriptionFragmentCall(bdl);
	}
}
