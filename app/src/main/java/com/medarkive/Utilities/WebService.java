package com.medarkive.Utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

import android.content.Context;
import android.os.Looper;
import android.util.Log;

public class WebService {
	
	private String URL = "http://medarkive.com/WebServices/index";
	boolean  status = false;
	public boolean postJsonWebApi(final JSONObject obj,
			final Context ctx) {
		Thread t = new Thread() {
			public void run() {
				Looper.prepare(); // For Preparing Message Pool for the child
									// Thread
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						10000); // Timeout Limit
				HttpResponse response;
				JSONObject json = obj;
				try {
					HttpPost post = new HttpPost(URL);
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
						System.out.println("result :----- " + sb.toString());
						if (json.length() > 0) {
								status= true;
						}else{
							status =false;
						}

					}

				} catch (Exception e) {
					status =false;
					e.printStackTrace();
				}

				Looper.loop(); // Loop in the message queue
			}
		};

		t.start();
		return status;
	}
	
}
