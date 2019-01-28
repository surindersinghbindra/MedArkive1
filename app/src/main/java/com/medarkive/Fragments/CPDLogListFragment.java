/*
 * sam
 * 
 * class for Display list of CPD catagories 
 * 
 */

package com.medarkive.Fragments;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.medarkive.R;
import com.medarkive.Adapter.CustomListViewAdapterForCPD;
import com.medarkive.Beans.CPDBean;
import com.medarkive.Main.DisplayDataActivity;
import com.medarkive.Main.LoginActivity;
import com.medarkive.Utilities.ConnectionDetector;
import com.medarkive.Utilities.Functions;
import com.medarkive.Utilities.JsonManipulator;
import com.medarkive.Utilities.SessionManager;

public class CPDLogListFragment extends ListFragment {

	private ArrayList<CPDBean> cpdUKArraylist = null;
	private HashMap<String, ArrayList<CPDBean>> map;
	// private ProgressDialog dialog ;

	private SessionManager sm;
	private String uid;
	private String cpdUserType;
	private String method = "cpd_data";
	private ConnectionDetector cd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
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
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.cpd_fragment, container, false);
		LinearLayout rv = (LinearLayout) view.findViewById(R.id.rv);
		new ASSL(getActivity(), rv, 1134, 720, false);

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		GetCPD cpd = new GetCPD();
		cpd.execute("");
		((DisplayDataActivity) getActivity()).hideBottomBar();
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		super.onListItemClick(l, v, pos, id);
//		Iterator it = map.entrySet().iterator();
//		while (it.hasNext()) {
//			Map.Entry pairs = (Map.Entry) it.next();
//			System.out.println(pairs.getKey());
//			ArrayList<CPDBean> c = (ArrayList<CPDBean>) pairs.getValue();
//			for (CPDBean ve : c) {
//				System.out.println("get name " + ve.getName());
//				System.out
//						.println("get event des " + ve.getEvent_description());
//				System.out.println("get creiated " + ve.getCreated());
//				System.out.println("get credit " + ve.getCredit());
//				System.out.println("get type name " + ve.getType_name());
//			}
//		}
		CPDBean beanobj = (CPDBean) ((CustomListViewAdapterForCPD) getListAdapter())
				.getItem(pos);
		System.out.println(beanobj != null);
		ArrayList<CPDBean> arrlst = map.get(beanobj.getName());
		Bundle bdl = new Bundle();
		// ((DisplayDataActivity) getActivity())
		// .setCustomActionBarBackButton("Event Detail List");
		SessionManager sm = new SessionManager(getActivity());
		String str = sm.getCPDChoice();
		if (str.equals("1")) {
			((DisplayDataActivity) getActivity()).editName("CPD Log (UK)");
		} else {
			((DisplayDataActivity) getActivity()).editName("CPD Log (International)");
		}

		bdl.putParcelableArrayList(LoginActivity.Extra_Value, arrlst);
		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
		EventDetailsListFragment listEvent = new EventDetailsListFragment();
		listEvent.setArguments(bdl);
		ft.replace(R.id.content, listEvent);
		ft.addToBackStack(null);
		ft.commit();
	}

	public class GetCPD extends AsyncTask<String, Void, JSONObject> {

		final CountDownLatch latch = new CountDownLatch(1);

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			Functions.showLoadingDialog(getActivity(), "Please Wait...");

		}

		@Override
		protected JSONObject doInBackground(String... params) {
			latch.countDown();
			sm = new SessionManager(getActivity());
			HashMap<String, String> user = sm.getUserDetails();
			uid = user.get(SessionManager.KEY_USER_ID);
			cpdUserType = sm.getCPDChoice();
			JSONObject result = null;
			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams
					.setConnectionTimeout(client.getParams(), 10000);
			HttpResponse response;
			JSONObject json = new JSONObject();
			try {
				HttpPost post = new HttpPost(LoginActivity.WEBSERVICEURL);
				json.put("cpd_user_type", cpdUserType);
				json.put("user_id", uid);
				json.put("method", method);
				StringEntity se = new StringEntity(json.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				post.setEntity(se);
				response = client.execute(post);
				/* Checking response */
				if (response != null) {
					// reading response
					InputStream ips = response.getEntity().getContent();
					BufferedReader buf = new BufferedReader(
							new InputStreamReader(ips, "UTF-8"));
					if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
						throw new Exception(response.getStatusLine()
								.getReasonPhrase());
					}
					StringBuilder sb = new StringBuilder();
					String s;
					while (true) {
						s = buf.readLine();
						if (s == null || s.length() == 0)
							break;
						sb.append(s);
					}
					buf.close();
					ips.close();
					json = new JSONObject(sb.toString());
					if (json.length() > 0) {
						// threadMsg(json.toString());
						result = json;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		protected void onPostExecute(JSONObject page) {
			// dialog.dismiss();
			Functions.dismissLoadingDialog();

			sm = new SessionManager(getActivity());
			cd = new ConnectionDetector(getActivity());
			if (cd.isConnectedToInternet()) {
				JsonManipulator converter = new JsonManipulator();
				map = converter.jsonToMap(page, getActivity());
				cpdUKArraylist = converter.convertJsonToArrayList(page,
						getActivity());
				CustomListViewAdapterForCPD adapter = new CustomListViewAdapterForCPD(
						cpdUKArraylist, getActivity());
				setListAdapter(adapter);

				// View footer = ((LayoutInflater)
				// getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_traker_button,
				// null, false);
				// ListView listView = getListView();
				// listView.addFooterView(footer);
				//
				// footer.findViewById(R.id.tracker).setOnClickListener(new
				// OnClickListener() {
				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// Intent i = new Intent(getActivity(), TrackerActivity.class);
				// getActivity().startActivity(i);
				// }
				// });

				// RelativeLayout rv = (RelativeLayout)
				// footer.findViewById(R.id.rv);
				// new ASSL(getActivity(), rv, 1134, 720, false);

			} else {
				cd.showAlertDialog(false);
			}
		}
	}
}
