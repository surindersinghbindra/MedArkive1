package com.medarkive.Utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import org.json.JSONObject;

import com.medarkive.R;
import com.medarkive.Beans.BookmarkBean;
import com.medarkive.Fragments.BookmarkListFragment;
import com.medarkive.Main.DisplayDataActivity;
import com.medarkive.Main.LoginActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class BookmarkManager {

	private JSONObject jsonObj ;
	private Context ctx;
	public BookmarkManager(JSONObject obj , Context ctx ){
		this.ctx = ctx;
		jsonObj = obj;
	}
	public BookmarkManager(){}
	
	public JSONObject getJsonObj() {
		return jsonObj;
	}



	public void setJsonObj(JSONObject jsonObj) {
		this.jsonObj = jsonObj;
	}



	public void saveBookmark(final JSONObject obj , final Context ctx){

		Thread t = new Thread() {
			public void run() {
				Looper.prepare();
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						10000); // Timeout Limit
				HttpResponse response;
				JSONObject json = obj;
				try {
					HttpPost post = new HttpPost(LoginActivity.WEBSERVICEURL);
					StringEntity se = new StringEntity(json.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
							"application/json"));
					post.setEntity(se);

					response = client.execute(post);
					System.out.println(response == null);
					if (response != null) {
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
						if(json.length() > 0 ){
							Toast.makeText(ctx, json.getJSONObject("save_bookmark").get("status").toString() , Toast.LENGTH_SHORT);
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

	public String deletBookmark (){
		String str="fail";
		GetBookmarkes gbm = new GetBookmarkes();
		try{
			gbm.execute();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			JSONObject response = getJsonObj();
			Toast.makeText(ctx, response.getJSONObject("delete_bookmark").getString("status") , Toast.LENGTH_SHORT);
			str=response.getJSONObject("delete_bookmark").getString("success");
		}catch (Exception e){
			e.printStackTrace();
		}
//		JSONObject response ;
//		SessionManager sm = new SessionManager(ctx);
//		HashMap<String , String> user = sm.getUserDetails();
//		String uid = user.get(SessionManager.KEY_USER_ID);
//		try {
//			final JSONObject obj = new JSONObject();
//			obj.put("method", "show_bookmark");
//			obj.put("user_id", uid);
//			BookmarkManager bookmarkMgr = new BookmarkManager();
//			response = bookmarkMgr.getBookmaks(obj,ctx );
//			JsonManipulator jm = new JsonManipulator();
//			ArrayList< BookmarkBean> arr = jm.bookmarkJsonToMap(response);
//			Intent intent = new Intent(ctx, BookmarkListActivity.class);
//			intent.putParcelableArrayListExtra(LoginActivity.Extra_Value, arr);
//			ctx.startActivity(intent);
//			((BookmarkListActivity)ctx).finish();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		return str;
	}

//	public JSONObject getBookmaks(final JSONObject obj , final Context ctx){
////		GetBookmarkes gbm = new GetBookmarkes();
//		try{
//			jsonObj = obj;
////			gbm.execute().get();
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//		
//		return getJsonObj();
//	}
	
	
	public class GetBookmarkes extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String result = "fail";
			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams
					.setConnectionTimeout(client.getParams(), 10000);
			HttpResponse response;
			JSONObject json = null;
			try {
//				SessionManager sm = new SessionManager(DisplayDataActivity.this);
//				HashMap<String, String> user = sm.getUserDetails();
//				String uid = user.get(SessionManager.KEY_USER_ID);
//				final JSONObject obj = new JSONObject();
//				obj.put("method", "show_bookmark");
//				obj.put("user_id", uid);
//				// TODO Auto-generated catch block
				HttpPost post = new HttpPost(LoginActivity.WEBSERVICEURL);
				StringEntity se = new StringEntity(jsonObj.toString());
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
						System.out.println("response ----" + json);
						 setJsonObj(json);
						result = "pass";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			Functions.dismissLoadingDialog();
//			JsonManipulator jm = new JsonManipulator();
//			ArrayList<BookmarkBean> arr = jm.bookmarkJsonToMap(result);
//
//			FragmentTransaction traFragment = DisplayDataActivity.this
//					.getSupportFragmentManager().beginTransaction();
//			Bundle bdl = new Bundle();
//
//			BookmarkListFragment bookmark = new BookmarkListFragment();
//			bdl.putParcelableArrayList(LoginActivity.Extra_Value, arr);
//			bookmark.setArguments(bdl);
//			traFragment.replace(R.id.content, bookmark);
//			traFragment.addToBackStack(null);
//			traFragment.commit();

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
//			Functions.showLoadingDialog(DisplayDataActivity.this, "Loading...");
			super.onPreExecute();
		}

	}
}
