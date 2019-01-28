package com.medarkive.Main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.medarkive.R;
import com.medarkive.Adapter.MySimpleArrayAdapter;
import com.medarkive.Beans.TrackerDataBean;
import com.medarkive.Utilities.ConnectionDetector;
import com.medarkive.Utilities.DatabaseHandler;
import com.medarkive.Utilities.Functions;
import com.medarkive.Utilities.SessionManager;

public class TrackerFragment extends ListFragment {

	SimpleCursorAdapter mAdapter;

	// private SessionManager sm;
	// private String uid;
	// private String cpdUserType;
	// private String method = "cpd_data";
	private ConnectionDetector cd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		cd = new ConnectionDetector(getActivity());
		if (cd.isConnectedToInternet()) {
			GetTrakerData tracker = new GetTrakerData();
			tracker.execute("");
		} else {
			cd.showAlertDialog(false);
			final DatabaseHandler db = new DatabaseHandler(getActivity());

			ArrayList<TrackerDataBean> Items = new ArrayList<TrackerDataBean>();

			System.out.println("Arraylist hashmap declared");

			// Reading all values
			Log.d("Reading: ", "Reading all contacts..");
			List<TrackerDataBean> data = db.getAllTrackedData();
			TrackerDataBean demodata = new TrackerDataBean();
			demodata.setTitle("ARTICLE");
			demodata.setSpentTime("Total Spent Time (hh:mm:ss)");
			Items.add(demodata);
			for (TrackerDataBean val : data) {
				Items.add(val);
			}

			// Adding Items to ListView
			MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(getActivity(), Items);

			setListAdapter(adapter);
		}

		// ListView lv = getListView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(R.layout.activity_tracker, container, false);
		RelativeLayout rv = (RelativeLayout) rootView.findViewById(R.id.rv);
		new ASSL(getActivity(), rv, 1134, 720, false);

		return rootView;

	}

	public class GetTrakerData extends AsyncTask<String, Void, JSONObject> {

		final CountDownLatch latch = new CountDownLatch(1);

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			Functions.showLoadingDialog(getActivity(), "Please Wait...");

		}

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject result = null;
			final String method = "cpd_tracking";
			final ArrayList<TrackerDataBean> arrw = new ArrayList<TrackerDataBean>();
			SessionManager sm = new SessionManager(getActivity());
			HashMap<String, String> userdata = sm.getUserDetails();
			final String userId = userdata.get(SessionManager.KEY_USER_ID);
			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
																					// Limit
			HttpResponse response;
			JSONObject json = new JSONObject();
			try {
				HttpPost post = new HttpPost(LoginActivity.WEBSERVICEURL);
				json.put("method", method);
				json.put("user_id", userId);
				StringEntity se = new StringEntity(json.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
				post.setEntity(se);

				response = client.execute(post);
				System.out.println(response == null);
				if (response != null) {
					// reading response
					InputStream ips = response.getEntity().getContent();
					BufferedReader buf = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
					if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
						throw new Exception(response.getStatusLine().getReasonPhrase());
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
					// Gettig Json
					result = new JSONObject(sb.toString());

				}

			} catch (Exception exception) {
				exception.printStackTrace();
			}

			return result;
		}

		protected void onPostExecute(JSONObject page) {
			// dialog.dismiss();
			Functions.dismissLoadingDialog();
			try {
				System.out.println(page.toString());
				JSONArray arr;

				arr = page.getJSONObject("cpd_tracking").getJSONArray("cdp_tracks");

				List<TrackerDataBean> datalist = new ArrayList<TrackerDataBean>();
				for (int i = 0; i < arr.length(); i++) {
					TrackerDataBean trackerDataBean = new TrackerDataBean();
					JSONObject row = arr.getJSONObject(i);
					if (row.has("pdf_title")) {
						trackerDataBean.setTitle(row.getString("pdf_title"));
					}
					if (row.has("link_url")) {
						trackerDataBean.setLink(row.getString("link_url"));
					}
					if (row.has("total_time_spent")) {
						trackerDataBean.setSpentTime(row.getString("total_time_spent"));
					}
					if (row.has("pdf_id")) {
						trackerDataBean.setId(row.getString("pdf_id"));
					}
					if (row.has("type")) {
						trackerDataBean.setTrackType(row.getString("type"));
					}
					datalist.add(trackerDataBean);
				}
				DatabaseHandler db = new DatabaseHandler(getActivity());
				for (TrackerDataBean bean : datalist) {
					db.addTrackedData(bean);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			final DatabaseHandler db = new DatabaseHandler(getActivity());

			ArrayList<TrackerDataBean> Items = new ArrayList<TrackerDataBean>();

			System.out.println("Arraylist hashmap declared");

			// Reading all values
			Log.d("Reading: ", "Reading all contacts..");
			List<TrackerDataBean> data = db.getAllTrackedData();
			TrackerDataBean demodata = new TrackerDataBean();
			demodata.setTitle("ARTICLE");
			demodata.setSpentTime("Total Spent Time (hh:mm:ss)");
			Items.add(demodata);
			for (TrackerDataBean val : data) {
				Items.add(val);
			}

			// Adding Items to ListView
			MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(getActivity(), Items);

			setListAdapter(adapter);

		}
	}

}
