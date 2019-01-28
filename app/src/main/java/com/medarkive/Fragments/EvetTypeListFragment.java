/*
 * 
 * sam
 * class for display List of CPD UK event type
 * 
 */
package com.medarkive.Fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import rmn.androidscreenlibrary.ASSL;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.medarkive.R;
import com.medarkive.Adapter.CustomEventTypeListAdapter;
import com.medarkive.Beans.CPDBean;
import com.medarkive.Main.DisplayDataActivity;
import com.medarkive.Main.LoginActivity;
import com.medarkive.Utilities.Item;
import com.medarkive.Utilities.SessionManager;

@SuppressLint("UseSparseArrays")
public class EvetTypeListFragment extends ListFragment {

	public static final String TYPE_ID = "type_id";

	public static final String ARRAY_LIST_DATA = "Array_List";

	private ArrayList<CPDBean> aaa;

	private List<Item> dataForTheAdapter = new ArrayList<Item>();
//	private ArrayAdapter<Item> mAdapter;
	private CustomEventTypeListAdapter adapter;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    if(isVisibleToUser) {
	        Activity a = getActivity();
	        if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_cpd_type_list,
				container, false);
		SessionManager sm = new SessionManager(getActivity());
		String str = sm.getCPDChoice();
		if (str.equals("1")) {
			((DisplayDataActivity) getActivity()).editName("CPD Log (UK)");
		} else {
			((DisplayDataActivity) getActivity()).editName("CPD Log (International)");
		}
		
		RelativeLayout rv = (RelativeLayout) rootView.findViewById(R.id.rv);
		new ASSL(getActivity(), rv, 1134, 720, false);
//		mAdapter = new ArrayAdapter<Item>(
//				getActivity().getApplicationContext(),
//				android.R.layout.simple_list_item_1, dataForTheAdapter);
		adapter=new CustomEventTypeListAdapter(dataForTheAdapter, getActivity());
		setListAdapter(adapter);
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		HashMap<Integer, String> map = new HashMap<Integer, String>();

		aaa = getArguments().getParcelableArrayList(LoginActivity.Extra_Value);
		for (CPDBean bean : aaa) {
			map.put(Integer.parseInt(bean.getCpd_type_id()),
					bean.getType_name());
		}
		Iterator<Entry<Integer, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			System.out.println(pairs.getKey());
			int tagId = (Integer) pairs.getKey();
			dataForTheAdapter.add(new Item(pairs.getValue().toString(), tagId));
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String tag = adapter.getItem(position).getPosition() + "";
		// System.out.println("tag ======"
		// + mAdapter.getItem(position).getPosition());

		Item item = dataForTheAdapter.get(position);

//		FragmentTransaction ft = getActivity().getSupportFragmentManager()
//				.beginTransaction();
//		EventDescriptionFragment event = new EventDescriptionFragment();
		Bundle bdl = new Bundle();
		bdl.putParcelable(TYPE_ID, item);
		bdl.putParcelableArrayList(ARRAY_LIST_DATA, aaa);
		((DisplayDataActivity)getActivity()).evetTypeFragmentCall(bdl);
//		event.setArguments(bdl);
//		ft.replace(R.id.content, event);
//		ft.addToBackStack(null);
//		ft.commit();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
//		SessionManager sm = new SessionManager(getActivity());
//		String str = sm.getCPDChoice();
//		if (str.equals("1")) {
//			((DisplayDataActivity) getActivity()).editName("CPD Log (UK)");
//		} else {
//			((DisplayDataActivity) getActivity()).editName("CPD Log (International)");
//		}
		super.onResume();
	}
}
