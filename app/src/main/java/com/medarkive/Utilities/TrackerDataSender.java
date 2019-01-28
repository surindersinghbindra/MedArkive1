package com.medarkive.Utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.json.JSONObject;

import com.medarkive.Beans.TrackerDataBean;
import com.medarkive.Main.LoginActivity;

import android.content.Context;
import android.os.Looper;

public class TrackerDataSender {

//	public void getTrackerData(final Context ctx) {
//		final String method = "cpd_tracking";
//		final ArrayList<TrackerDataBean> arrw = new ArrayList<TrackerDataBean>() ;
//		SessionManager sm = new SessionManager(ctx);
//		HashMap<String, String> userdata = sm.getUserDetails();
//		final String userId = userdata.get(SessionManager.KEY_USER_ID);
//		Thread t = new Thread() {
//			public void run() {
//				Looper.prepare();
//				HttpClient client = new DefaultHttpClient();
//				HttpConnectionParams.setConnectionTimeout(client.getParams(),
//						10000); // Timeout Limit
//				HttpResponse response;
//				JSONObject json = new JSONObject();
//				try {
//					HttpPost post = new HttpPost(LoginActivity.WEBSERVICEURL);
//					json.put("method", method);
//					json.put("user_id", userId);
//					StringEntity se = new StringEntity(json.toString());
//					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
//							"application/json"));
//					post.setEntity(se);
//
//					response = client.execute(post);
//					System.out.println(response == null);
//					if (response != null) {
//						// reading response
//						InputStream ips = response.getEntity().getContent();
//						BufferedReader buf = new BufferedReader(
//								new InputStreamReader(ips, "UTF-8"));
//						if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//							throw new Exception(response.getStatusLine()
//									.getReasonPhrase());
//						}
//						StringBuilder sb = new StringBuilder();
//						String s;
//						while (true) {
//							s = buf.readLine();
//							if (s == null || s.length() == 0)
//								break;
//							sb.append(s);
//						}
//						buf.close();
//						ips.close();
//						// Gettig Json
//						json = new JSONObject(sb.toString());
//						System.out.println(json.toString());
//						JSONArray arr = json
//								.getJSONArray("BrowserArchiveTimeTracking");
//						List<TrackerDataBean> datalist = new ArrayList<TrackerDataBean>();
//						for (int i = 0; i < arr.length(); i++) {
//							TrackerDataBean trackerDataBean = new TrackerDataBean();
//							JSONObject row = arr.getJSONObject(i);
//							trackerDataBean
//									.setTitle(row.getString("pdf_title"));
//							trackerDataBean.setLink(row.getString("link_url"));
//							trackerDataBean.setSpentTime(row
//									.getString("total_time_spent"));
//							trackerDataBean.setId(row.getString("pdf_id"));
//							trackerDataBean.setTrackType(row.getString("type"));
//							datalist.add(trackerDataBean);
//						}
//						DatabaseHandler db = new DatabaseHandler(ctx);
//						for (TrackerDataBean bean : datalist) {
//							db.addTrackedData(bean);
//						}
//
//					}
//
//				} catch (Exception exception) {
//					exception.printStackTrace();
//				}
//				Looper.loop(); // Loop in the message queue
//			}
//		};
//		t.start();
//	}
	public void updateTracker(ArrayList<TrackerDataBean> arr , final Context ctx) {
		final String method = "BrowserArchiveTimeTracking";
		final ArrayList<TrackerDataBean> arrw = arr;
		SessionManager sm = new SessionManager(ctx);
		HashMap<String, String> userdata = sm.getUserDetails();
		final String userId = userdata.get(SessionManager.KEY_USER_ID);
		Thread t = new Thread() {
			public void run() {
				Looper.prepare();
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						10000); // Timeout Limit
				HttpResponse response;
				JSONObject json = new JSONObject();
				try {
					HttpPost post = new HttpPost(LoginActivity.WEBSERVICEURL);
					json.put("method", method);
					json.put("user_id", userId);
					JSONArray arrjson = new JSONArray();
					for (TrackerDataBean bean : arrw) {
						JSONObject data = new JSONObject();
						data.put("type", "browser");
						data.put("link_url", bean.getLink());
						data.put("time_track", bean.getSpentTime());
						data.put("title", bean.getTitle());
						arrjson.put(data);
					}
					json.put("data", arrjson);

					StringEntity se = new StringEntity(json.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
							"application/json"));
					post.setEntity(se);

					response = client.execute(post);
					System.out.println(response == null);
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
						// Gettig Json
						json = new JSONObject(sb.toString());
						System.out.println(json.toString());
						JSONArray arr = json
								.getJSONArray("BrowserArchiveTimeTracking");
						List<TrackerDataBean> datalist = new ArrayList<TrackerDataBean>();
						for (int i = 0; i < arr.length(); i++) {
							TrackerDataBean trackerDataBean = new TrackerDataBean();
							JSONObject row = arr.getJSONObject(i);
							trackerDataBean
									.setTitle(row.getString("pdf_title"));
							trackerDataBean.setLink(row.getString("link_url"));
							trackerDataBean.setSpentTime(row
									.getString("total_time_spent"));
							trackerDataBean.setId(row.getString("pdf_id"));
							trackerDataBean.setTrackType(row.getString("type"));
							datalist.add(trackerDataBean);
						}
						DatabaseHandler db = new DatabaseHandler(ctx);
						for (TrackerDataBean bean : datalist) {
							db.addTrackedData(bean);
						}

					}

				} catch (Exception exception) {
					exception.printStackTrace();
				}
				Looper.loop(); // Loop in the message queue
			}
		};
		t.start();
	}
}
