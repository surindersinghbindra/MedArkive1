package com.medarkive.Adapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medarkive.R;
import com.medarkive.Beans.CPDBean;
import com.medarkive.Main.DisplayDataActivity;
import com.medarkive.Main.LoginActivity;
import com.medarkive.Utilities.Functions;
import com.medarkive.Utilities.SessionManager;

public class CustomListAdapterForEvents extends BaseAdapter {
	private ArrayList<CPDBean> mData;
	private Context ctx;
	private String CPDCoice;
	private HashMap<String, String> user;
	// LinearLayout imageview ;
	CPDBean beanCPd, cpdDelEvent;

	final JSONObject eventDescriptionToSave = new JSONObject();

	ViewHolder holder;

	public CustomListAdapterForEvents(ArrayList<CPDBean> arrlst, Context ctx) {
		mData = new ArrayList<CPDBean>();
		mData = arrlst;
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		return mData.size();
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

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.row_event_detail, parent, false);
			holder = new ViewHolder();
			holder.eventDetail = (TextView) convertView
					.findViewById(R.id.event_description);
			holder.deleteEvent = (LinearLayout) convertView
					.findViewById(R.id.delete_row);
			holder.rv = (RelativeLayout) convertView.findViewById(R.id.rv);

			holder.rv.setLayoutParams(new ListView.LayoutParams(720, 150));
			ASSL.DoMagic(holder.rv);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SessionManager sm = new SessionManager(ctx);
		CPDCoice = sm.getCPDChoice();
		user = sm.getUserDetails();
		final String u_id = user.get(SessionManager.KEY_USER_ID);
		beanCPd = getItem(position);

		// TODO replace findViewById by ViewHolder
		if (beanCPd.getEvent_description() != null
				&& beanCPd.getCredit() != null) {
			Date newdate = null;
			SimpleDateFormat smdf = new SimpleDateFormat("yyyy-dd-mm");
			String created = "";
			// TextView evenDec = null;
			if (beanCPd.getCreated() != null) {
				try {
					newdate = smdf.parse(beanCPd.getCreated());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SimpleDateFormat formatedDate = new SimpleDateFormat("mm-dd");
				created = formatedDate.format(newdate);
				AssetManager assetManagerlight = ctx.getAssets();
				Typeface tfGillSansLight = Typeface.createFromAsset(
						assetManagerlight, "gill-sans-light.ttf");

				// evenDec = (TextView)
				// result.findViewById(R.id.event_description);
				holder.eventDetail.setTypeface(tfGillSansLight);
			}
			if (beanCPd.getType_name() != null) {
				holder.eventDetail.setText(created + " "
						+ beanCPd.getName().toString().substring(0, 3) + ". "
						+ beanCPd.getType_name() + ": "
						+ beanCPd.getEvent_description() + ", "
						+ beanCPd.getCredit() + " Credit");
			} else {
				holder.eventDetail.setText(created + " "
						+ beanCPd.getName().toString().substring(0, 3) + ". "
						+ beanCPd.getEvent_description() + ", "
						+ beanCPd.getCredit() + " Credit");
			}
			// imageview = (LinearLayout) result.findViewById(R.id.delete_row);
			holder.deleteEvent.setTag(beanCPd.getCpd_event_id());

		}
		final String event_id = beanCPd.getCpd_event_id();
		// final String category_id = beanCPd.getCpd_id();
		holder.deleteEvent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder adb = new AlertDialog.Builder(ctx);

				adb.setTitle("Would you like to Delete this Event");

				adb.setIcon(android.R.drawable.ic_dialog_alert);

				adb.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								try {
									eventDescriptionToSave.put("method",
											"delete_cpd_event");
									eventDescriptionToSave.put("user_id", u_id);
									eventDescriptionToSave.put("cpd_user_type",
											CPDCoice);
									eventDescriptionToSave.put("cpd_event_id",
											event_id);
									// System.out.println("json to snd ====="
									// + eventDescriptionToSave);
									cpdDelEvent = beanCPd;
									DeleteEvent deleteEvent = new DeleteEvent();
									deleteEvent.execute("");
									// deleteEvent.deleteEvent(eventDescriptionToSave,
									// category_id, ctx , beanCPd.getName());
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								// Toast.makeText(ctx,"Event Deleted",
								// Toast.LENGTH_SHORT).show();
							}
						});

				adb.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				adb.show();
			}
		});
		return convertView;
	}

	static class ViewHolder {
		public TextView eventDetail;
		public LinearLayout deleteEvent;
		public RelativeLayout rv;
	}

	public class DeleteEvent extends AsyncTask<String, Void, JSONObject> {

		// final CountDownLatch latch = new CountDownLatch(1);

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			Functions.showLoadingDialog(ctx, "Please Wait...");

		}

		@Override
		protected JSONObject doInBackground(String... params) {
			// latch.countDown();
			JSONObject result = null;
			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams
					.setConnectionTimeout(client.getParams(), 10000);
			HttpResponse response;
			JSONObject json = new JSONObject();
			try {
				HttpPost post = new HttpPost(LoginActivity.WEBSERVICEURL);
				// json.put("cpd_user_type", cpdUserType);
				// json.put("user_id", uid);
				// json.put("method", method);
				StringEntity se = new StringEntity(
						eventDescriptionToSave.toString());
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
						Log.v("event -->", "," + json.toString());
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
			// System.out.println("json o "+jsonResponse);
			// category_id, ctx ,
			try {
				if (page.getJSONObject("delete_cpd_event").getString("success").equals("true")) {
					if (cpdDelEvent != null) {
						mData.remove(cpdDelEvent);
						notifyDataSetChanged();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			 ((DisplayDataActivity)ctx).forwardToEventDetail(page ,
//			 beanCPd.getCpd_id() , beanCPd.getName());
			// object = jsonResponse;
			// setObject(jsonResponse);

		}
	}

}
