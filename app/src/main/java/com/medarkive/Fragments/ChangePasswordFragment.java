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
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class ChangePasswordFragment extends Fragment {

	private EditText oldPasswordTxt;
	private EditText newPasswordTxt;
	private EditText confirmPasswordTxt;
	private String oldPassword;
	private JSONObject passwordjson;

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
		View view = inflater.inflate(R.layout.fragment_change_password,
				container, false);

		RelativeLayout rv = (RelativeLayout) view.findViewById(R.id.rv);
		new ASSL(getActivity(), rv, 1134, 720, false);

		oldPasswordTxt = (EditText) view.findViewById(R.id.old_password);
		newPasswordTxt = (EditText) view.findViewById(R.id.new_password);
		confirmPasswordTxt = (EditText) view
				.findViewById(R.id.confirm_password);

		SessionManager sm = new SessionManager(getActivity());
		HashMap<String, String> map = sm.getUserDetails();
		final String uid = map.get(SessionManager.KEY_USER_ID);
		final String name = map.get(SessionManager.KEY_NAME);
		view.findViewById(R.id.change_password_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String newPass = newPasswordTxt.getText().toString();
						String confirmpass = confirmPasswordTxt.getText()
								.toString();
						oldPassword = oldPasswordTxt.getText().toString();
						if (!newPass.equals(confirmpass)) {
							newPasswordTxt
									.setError("Please enter the same password in both fields");
							confirmPasswordTxt
									.setError("Please enter the same password in both fields");
						} else if (oldPasswordTxt.getText().toString().length() == 0) {
							oldPasswordTxt
									.setError("Please provide your Old Password");
						} else {
							try {
								passwordjson = new JSONObject();
								passwordjson.put("method", "ChangePassword");
								passwordjson.put("user_id", uid);
								passwordjson.put("name", name);
								passwordjson.put("old_password", oldPassword);
								passwordjson.put("new_password", newPass);
								ChangePasswordReq cpr = new ChangePasswordReq();
								cpr.execute("");
//								JSONObject response = cpr.getOobj();
//								String msg = response
//										.getJSONObject("ChangePassword")
//										.get("status").toString();
//								Toast.makeText(getActivity(), msg,
//										Toast.LENGTH_LONG).show();
							} catch (Exception e) {
								// TODO Auto-generated catch block
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
								Functions.dismissLoadingDialog();
								e.printStackTrace();
							}

						}

					}
				});

		return view;
	}


	public class ChangePasswordReq extends AsyncTask<String, Void, String> {
		// ProgressDialog mProgressDialog;

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
			Functions.showLoadingDialog(getActivity(), "Please Wait...");
			// mProgressDialog= new ProgressDialog(getActivity());
			// mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			latch.countDown();
			JSONObject json = new JSONObject();
			// user = sm.getUserDetails();
			// uid = user.get(SessionManager.KEY_USER_ID);
			// cpdUserType = sm.getCPDChoice();
			String result = "fail";
			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams
					.setConnectionTimeout(client.getParams(), 10000);
			HttpResponse response;
			// JSONObject json = new JSONObject();
			try {
				HttpPost post = new HttpPost(LoginActivity.WEBSERVICEURL);
				StringEntity se = new StringEntity(passwordjson.toString());
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
				Functions.dismissLoadingDialog();
//				Toast.makeText(getActivity(), "Oops…server error",
//						Toast.LENGTH_SHORT).show();
			}
			return result;
		}

		protected void onPostExecute(String page) {
			Functions.dismissLoadingDialog();
			if (page.equals("pass")) {
//				showAlert();
				
				new AlertDialog.Builder(getActivity())
				.setTitle("Password Changed ✓")
				.setMessage("Thanks, your account password was changed successfully")
				.setCancelable(false)
				.setPositiveButton("ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						getActivity().getSupportFragmentManager().popBackStack();
						((DisplayDataActivity)getActivity()).changeTopBar(new EditProfileFragment(), "Edit Profile", ((DisplayDataActivity)getActivity()).homeTBar);
					}
				}).create().show();
				
//				Toast.makeText(
//						getActivity(),
//						"Thanks, your account password was changed successfully",
//						Toast.LENGTH_SHORT).show();
			} else {
				
				new AlertDialog.Builder(getActivity())
				.setTitle("Please try again.")
				.setMessage("Sorry something’s gone wrong.")
				.setCancelable(false)
				.setPositiveButton("ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
//						getActivity().getSupportFragmentManager().popBackStack();
//						((DisplayDataActivity)getActivity()).changeTopBar(new EditProfileFragment(), "Edit Profile", ((DisplayDataActivity)getActivity()).homeTBar);
					}
				}).create().show();
				
//				Toast.makeText(getActivity(), "Sorry something’s gone wrong.",
//						Toast.LENGTH_SHORT).show();
			}
			// mProgressDialog.dismiss();
		}
	}
	
//	public void showAlert()
//	{
//		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//				getActivity());
// 
//			// set title
//			alertDialogBuilder.setTitle("Your Title");
// 
//			// set dialog message
//			alertDialogBuilder
//				.setMessage("Click yes to exit!")
//				.setCancelable(false)
//				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,int id) {
//						// if this button is clicked, close
//						// current activity
//					}
//				  })
//				.setNegativeButton("No",new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,int id) {
//						// if this button is clicked, just close
//						// the dialog box and do nothing
//						dialog.cancel();
//					}
//				});
// 
//				// create alert dialog
//				AlertDialog alertDialog = alertDialogBuilder.create();
// 
//				// show it
//				alertDialog.show();
//	}
}
