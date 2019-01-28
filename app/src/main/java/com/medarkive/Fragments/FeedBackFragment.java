package com.medarkive.Fragments;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.medarkive.R;
import com.medarkive.Main.DisplayDataActivity;
import com.medarkive.Main.LoginActivity;
import com.medarkive.Utilities.Functions;
import com.medarkive.Utilities.SessionManager;

public class FeedBackFragment extends Fragment {

	private JSONObject feedbackJson;

	EditText feedbackTxt;

	public JSONObject getObj() {
		return feedbackJson;
	}

	public void setObj(JSONObject obj) {
		this.feedbackJson = obj;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_feed_back);
		if (savedInstanceState == null) {
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    if(isVisibleToUser) {
	        Activity a = getActivity();
	        if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
	}

	private void setContentView(int activityFeddBack) {
		// TODO Auto-generated method stub

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.fragment_feed_back, container, false);
//		((DisplayDataActivity) getActivity())
//				.customActionBarEditProfile("MedArkive Feedback");
		feedbackTxt = (EditText) v.findViewById(R.id.feedback);
		SessionManager sm = new SessionManager(getActivity());

		HashMap<String, String> user = sm.getUserDetails();
		final String uid = user.get(SessionManager.KEY_USER_ID);
		final String emailid = user.get(SessionManager.KEY_EMAIL);
		v.findViewById(R.id.send_feedback).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						final String feedbackString = feedbackTxt.getText()
								.toString();
						// TODO Auto-generated method stub
						if (feedbackString.length() == 0) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									getActivity());
							builder.setTitle("Text Missing.");
							AlertDialog alert = builder.create();
							alert.setMessage("Please enter some text. In order to submit a feedback.");
							alert.setButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									});
							alert.show();
						} else {
							try {
								feedbackJson = new JSONObject();
								feedbackJson.put("method", "cpd_feedback");
								feedbackJson.put("user_id", uid);
								feedbackJson.put("emailid", emailid);
								feedbackJson.put("feedback_content",
										feedbackString);

								SendFeedBack sfb = new SendFeedBack();
								// String
								sfb.execute();
								JSONObject obj = sfb.getOobj();
								System.out.println("responsde feedback --"
										+ obj);
								// if (obj.getJSONObject("cpd_feedback")
								// .getString("success").equals("true")) {
									
							} catch (Exception e) {
								e.printStackTrace();
								new AlertDialog.Builder(getActivity())
								.setTitle("Please try again.")
								.setMessage("Sorry something’s gone wrong.")
								.setCancelable(false)
								.setPositiveButton("ok", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										
									}
								}).create().show();
							}
						}
					}
				});
		
		RelativeLayout rv = (RelativeLayout) v.findViewById(R.id.feedback_layout);
		new ASSL(getActivity(), rv, 1134, 720, false);

		return v;
	}

	public class SendFeedBack extends AsyncTask<String, Void, String> {
//		ProgressDialog mProgressDialog;

		JSONObject oobj;
		final CountDownLatch latch = new CountDownLatch(1);

		public JSONObject getOobj() {
			return oobj;
		}

		public void setOobj(JSONObject oobj) {
			this.oobj = oobj;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Functions.showLoadingDialog(getActivity(), "Please wait...");
//			mProgressDialog = new ProgressDialog(getActivity());
//			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			latch.countDown();
			JSONObject json = new JSONObject();
			String result = "fail";
			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams
					.setConnectionTimeout(client.getParams(), 10000);
			HttpResponse response;
			// JSONObject json = new JSONObject();
			try {
				HttpPost post = new HttpPost(LoginActivity.WEBSERVICEURL);
				StringEntity se = new StringEntity(feedbackJson.toString());
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
						setOobj(json);
						result = "pass";
					}
				}
			} catch (Exception e) {
				Functions.dismissLoadingDialog();
				e.printStackTrace();
				new AlertDialog.Builder(getActivity())
				.setTitle("Oops…server error")
				.setMessage("Please try again.")
				.setCancelable(false)
				.setPositiveButton("ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).create().show();
			}
			return result;
		}

		@SuppressWarnings("deprecation")
		protected void onPostExecute(String page) {
			Functions.dismissLoadingDialog();
			if(page.equals("pass")){
			AlertDialog.Builder builder = new AlertDialog.Builder(
					getActivity());
			builder.setTitle("Feedback Sent ✓");
			AlertDialog alert = builder.create();
			alert.setMessage("Thankyou for sending feedback about the MedArkive app. We are constantly striving to improve the quality and usefulness of the app and all comments will be used to enhance future releases.");
			alert.setButton(
					"OK",
					new DialogInterface.OnClickListener() {
						public void onClick(
								DialogInterface dialog,
								int which) {
							((DisplayDataActivity)getActivity()).showbottomBar();
							((DisplayDataActivity)getActivity()).hideKeyboard();
							((DisplayDataActivity)getActivity()).view.setVisibility(8);
							((DisplayDataActivity)getActivity()).changeTopBar(new  PDFTabsFragment() ,"home", ((DisplayDataActivity)getActivity()).homeTBar);
							getActivity().getSupportFragmentManager().popBackStack();
						}
					});
			alert.show();
		} else {
			Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Please try again.");
			builder.setMessage("Sorry something’s gone wrong.");
			builder.setCancelable(true);
			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
//			mProgressDialog.dismiss();
		}
	}
}
