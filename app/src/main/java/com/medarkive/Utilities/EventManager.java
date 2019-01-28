package com.medarkive.Utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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

import android.content.Context;
import android.os.Looper;

import com.medarkive.Main.DisplayDataActivity;
import com.medarkive.Main.LoginActivity;

public class EventManager {
	private JSONObject object= null ;
	
	public JSONObject saveEventDetail(final JSONObject obj , final Context ctx , String category_id){
		final String str = category_id;
		Thread t = new Thread() {
			public void run() {
				Looper.prepare(); // For Preparing Message Pool for the child
									// Thread
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						10000); // Timeout Limit
				HttpResponse response;
				final JSONObject json =obj;
				JSONObject jsonResponse = new JSONObject() ;
				try {
					HttpPost post = new HttpPost(LoginActivity.WEBSERVICEURL);
					// System.out.println("json =="+json.toString());
					StringEntity se = new StringEntity(json.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
							"application/json"));
					post.setEntity(se);
					// System.out.println("check 1");
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

						// Gettig Json
						jsonResponse = new JSONObject(sb.toString());
						// System.out.println("json:----- " + json.toString());
						if (json.length() > 0) {
//							bdl.putCharSequence(LoginActivity.Extra_Value,
//									json.toString());
							/*Bundle bdl = new Bundle();
							Fragment cpd = new CPDLogListFragment();
							cpd.setArguments(bdl);
							ft.replace(R.id.content, cpd);
							ft.commit();
							System.out.println("json obbbb----"+jsonResponse);*/
							System.out.println("json o "+jsonResponse);
							((DisplayDataActivity)ctx).forward(jsonResponse , str);
							object = jsonResponse;
							setObject(jsonResponse);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Looper.loop(); // Loop in the message queue
			}
		};
		t.start();
		return object;
	}
	
//	public void deleteEvent(JSONObject obj , String cpd_id , final Context ctx , final String name){
//		final JSONObject json =obj;
//		final String str = cpd_id;
//		Thread t = new Thread() {
//			public void run() {
//				Looper.prepare(); // For Preparing Message Pool for the child
//									// Thread
//				HttpClient client = new DefaultHttpClient();
//				HttpConnectionParams.setConnectionTimeout(client.getParams(),
//						10000); // Timeout Limit
//				HttpResponse response;
//				
//				JSONObject jsonResponse = new JSONObject() ;
//				try {
//					HttpPost post = new HttpPost(LoginActivity.WEBSERVICEURL);
//					// System.out.println("json =="+json.toString());
//					StringEntity se = new StringEntity(json.toString());
//					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
//							"application/json"));
//					post.setEntity(se);
//					// System.out.println("check 1");
//					response = client.execute(post);
//					/* Checking response */
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
//						jsonResponse = new JSONObject(sb.toString());
//						// System.out.println("json:----- " + json.toString());
//						if (json.length() > 0) {
//							System.out.println("json o "+jsonResponse);
//							((DisplayDataActivity)ctx).forwardToEventDetail(jsonResponse , str , name);
//							object = jsonResponse;
//							setObject(jsonResponse);
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				Looper.loop(); // Loop in the message queue
//			}
//		};
//		t.start();
////		return object;
//	}

	public JSONObject getObject() {
		return object;
	}

	public void setObject(JSONObject object) {
		this.object = object;
	}

}
