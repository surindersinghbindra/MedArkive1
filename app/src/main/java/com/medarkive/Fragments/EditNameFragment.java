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
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.medarkive.R;
import com.medarkive.Main.DisplayDataActivity;
import com.medarkive.Main.LoginActivity;
import com.medarkive.Utilities.Functions;
import com.medarkive.Utilities.SessionManager;

public class EditNameFragment extends android.support.v4.app.Fragment {
	private JSONObject editNameJson;
	private EditText nametxt;
	private Button changeNameBtn;


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
		final View view = inflater.inflate(R.layout.fragment_edit_name,
				container, false);
		final SessionManager sm = new SessionManager(getActivity());
		RelativeLayout rv = (RelativeLayout) view.findViewById(R.id.rv);
		new ASSL(getActivity(), rv, 1134, 720, false);

		HashMap<String, String> userDetail = sm.getUserDetails();
		final String uid = userDetail.get(SessionManager.KEY_USER_ID);
		nametxt = (EditText) view.findViewById(R.id.edit_name);
		nametxt.setText(userDetail.get(SessionManager.KEY_NAME));
		changeNameBtn = (Button) view.findViewById(R.id.change_name_btn);
		changeNameBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (nametxt.getText().length() == 0) {
					nametxt.setError("Please enter a Name which you want to change");
				} else {
					try {
						editNameJson = new JSONObject();
						editNameJson.put("method", "edit_profile");
						editNameJson.put("name", nametxt.getText().toString());
						editNameJson.put("user_id", uid);

						ChangeNameReq cnr = new ChangeNameReq();
						String[] str = new String[1];
						cnr.execute(str);
						// String msg =
						// cnr.getOobj().getJSONObject("edit_profile").get("status").toString();
						// Toast.makeText(getActivity(), msg ,
						// Toast.LENGTH_SHORT).show();
						if (cnr.getOobj().getJSONObject("edit_profile")
								.get("success").toString().equals("true")) {
							sm.changeName(nametxt.getText().toString());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		return view;
	}


	/**
	 * A placeholder fragment containing a simple view.
	 */

	public class ChangeNameReq extends AsyncTask<String, Void, String> {
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
			// mProgressDialog = new ProgressDialog(getActivity());
			// mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// mProgressDialog.show();
			Functions.showLoadingDialog(getActivity(), "Please Wait...");
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
				StringEntity se = new StringEntity(editNameJson.toString());
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
				Functions.dismissLoadingDialog();
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
//				Toast.makeText(getActivity(), "Oops…server error",
//						Toast.LENGTH_SHORT);
			}
			return result;
		}

		protected void onPostExecute(String page) {
			Functions.dismissLoadingDialog();
			if (page.equals("pass")) {
				// Toast.makeText(getActivity(),
				// "Thanks, your account profile name was changed successfully.",
				// Toast.LENGTH_SHORT);
				Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Profile name changed ✓");
				builder.setMessage("Thanks, your account profile name was changed successfully.");
				builder.setCancelable(true);
				builder.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								getActivity().getSupportFragmentManager()
										.popBackStack();
								((DisplayDataActivity) getActivity())
										.changeTopBar(
												new EditProfileFragment(),
												"Edit Profile",
												((DisplayDataActivity) getActivity()).homeTBar);
							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();
				SessionManager sm = new SessionManager(getActivity());
				sm.changeName(nametxt.getText().toString());
			} else {
				// Toast.makeText(getActivity(),
				// "Sorry something’s gone wrong.", Toast.LENGTH_SHORT);
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
			// mProgressDialog.dismiss();
		}
	}
}
