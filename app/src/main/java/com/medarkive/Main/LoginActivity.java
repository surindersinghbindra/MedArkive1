package com.medarkive.Main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medarkive.Beans.EbookBean;
import com.medarkive.Beans.PDFBean;
import com.medarkive.R;
import com.medarkive.Utilities.ConnectionDetector;
import com.medarkive.Utilities.DatabaseHandler;
import com.medarkive.Utilities.Functions;
import com.medarkive.Utilities.SessionManager;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import rmn.androidscreenlibrary.ASSL;

@SuppressLint("NewApi")

public class LoginActivity extends Activity {

	public static final String WEBSERVICEURL = "http://medarkive.com/WebServices/index";
	public static final String Extra_Value = "";
	public static String path;
	private boolean doubleBackToExitPressedOnce;
	private EditText username;
	private EditText password;
	// private ProgressDialog mProgressDialog;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
//		Crittercism.initialize(getApplicationContext(),"PSxWfB8LOLCn7DGakElVY0KtUonXL48W");
		final ConnectionDetector cd = new ConnectionDetector(LoginActivity.this);

		LinearLayout rv = (LinearLayout) findViewById(R.id.rv);
		new ASSL(LoginActivity.this, rv, 1134, 720, false);

		// getActionBar().setTitle("");
		// getActionBar().setIcon(R.drawable.ic_actionbar_medarkive);
		path = getFilesDir() + "/";

		SessionManager sm = new SessionManager(getApplicationContext());
		username = (EditText) findViewById(R.id.et_usn);
		password = (EditText) findViewById(R.id.et_pass);
		// Checking login status from shared prefrences
		if (sm.isLoggedIn()) {
			intent = new Intent(getApplicationContext(), DisplayDataActivity.class);
			startActivity(intent);
			finish();
		} else {
			TextView txtView = (TextView) findViewById(R.id.forgetPassword);
			registerForContextMenu(txtView);
			txtView.setLayoutDirection(0);
			if (cd.isConnectedToInternet()) {
				findViewById(R.id.forgetPassword).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final EditText input = new EditText(LoginActivity.this);
						AlertDialog.Builder helpBuilder = new AlertDialog.Builder(LoginActivity.this);
						helpBuilder.setTitle("Forgotten Password?");
						helpBuilder.setMessage("Enter Your Email");
						input.setSingleLine();
						helpBuilder.setView(input);
						helpBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// System.out.println("Email entered by user====="+
								// input.getText().toString());
								if (input.getText().length() == 0) {
									final Dialog check = new Dialog(LoginActivity.this);
									check.setTitle("Please Enter a Email Id");
									check.show();
								} else {
									try { 
										final JSONObject obj = new JSONObject();
										obj.put("method", "frgtpassword");
										obj.put("emailid",input.getText().toString());
										sendEmailForReset(obj);
//										final Dialog check = new Dialog(LoginActivity.this);
//										check.setTitle("Please enter an email address before tapping send.");
//										check.show();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						});
						// Remember, create doesn't show the dialog
						AlertDialog helpDialog = helpBuilder.create();
						helpDialog.show();
					}
				});
				// click listener for button
				findViewById(R.id.sign_up_button).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						intent = new Intent(LoginActivity.this, SignUp.class);
						startActivity(intent);
					}
				});
				findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						/*
						 * System.out.println("username  === "+username.getText
						 * ().toString()); System.out.println("Password  ==="
						 * +password.getText().toString());
						 */
						if (cd.isConnectedToInternet()) {
							if (username.getText().length() == 0) {
								username.setError("Email can't left Blank");

							} else if (password.getText().length() == 0) {
								password.setError("Password can't left Blank");
							}
							if (password.getText().length() > 0 && username.getText().length() > 0) {
								login(username.getText().toString(), password.getText().toString(), LoginActivity.this);
							}
						} else {
							cd.showAlertDialog(cd.isConnectedToInternet());
						}
					}
				});
			} else {
				cd.showAlertDialog(cd.isConnectedToInternet());
			}
		}

	}

	private void sendEmailForReset(final JSONObject obj) {
		Thread t = new Thread() {
			public void run() {
				Looper.prepare(); // For Preparing Message Pool for the child
									// Thread
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
																						// Limit
				HttpResponse response;
				final JSONObject json = obj;
				JSONObject jsonResponse = new JSONObject();
				try {
					HttpPost post = new HttpPost(LoginActivity.WEBSERVICEURL);
					// System.out.println("json =="+json.toString());
					StringEntity se = new StringEntity(json.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
					post.setEntity(se);
					// System.out.println("check 1");
					response = client.execute(post);
					/* Checking response */
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
						jsonResponse = new JSONObject(sb.toString());
						if (json.length() > 0) {
							System.out.println("json o " + jsonResponse);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Looper.loop(); // Loop in the message queue
			}
		};
		t.start();
	}

	protected void login(final String email, final String pwd, final Context ctx) {
		// mProgressDialog.show();
		Functions.showLoadingDialog(LoginActivity.this, "Loading...");
		Thread t = new Thread() {
			String method = "login";

			public void run() {
				Looper.prepare(); // For Preparing Message Pool for the child
									// Thread
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
																						// Limit
				HttpResponse response;
				JSONObject json = new JSONObject();
				try {
					HttpPost post = new HttpPost(WEBSERVICEURL);
					json.put("username", email);
					json.put("password", pwd);
					json.put("method", method);
					// System.out.println("json =="+json.toString());
					StringEntity se = new StringEntity(json.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
					post.setEntity(se);
					// System.out.println("check 1");
					response = client.execute(post);
					/* Checking response */
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
						Log.v("response login json:----- " , sb.toString());
						// Gettig Json
						json = new JSONObject(sb.toString());
//						 Log.v("json:----- " , json.toString());
						if (json.length() > 0) {
							String str = new JSONObject(json.getString(method)).get("success").toString();
//							Log.v("Login Response :", str + "");
							// System.out.printlwrap_contentn("check.....>>>"+str);
							// System.out.println("method ------>"+new
							// JSONObject(json.getString(method)).get("pdf_data").toString());
							if (str.compareTo("true") == 0) {
								SessionManager sm = new SessionManager(ctx);
								String userID = new JSONObject(json.getString(method)).get("user_id").toString();
								String token = new JSONObject(json.getString(method)).get("token").toString();
								String name = new JSONObject(json.getString(method)).get("name").toString();
								sm.createLoginSession(name, email, userID, token);

								DatabaseHandler db = new DatabaseHandler(ctx);

								List<PDFBean> lst = new ArrayList<PDFBean>();
								List<EbookBean> ebookLst = new ArrayList<EbookBean>();
								ArrayList<PDFBean> linkedBean = new ArrayList<PDFBean>();
								JSONArray jsonArr = new JSONObject(json.getString("login")).getJSONArray("pdf_data");
//								Log.v("pdf_data", jsonArr + "");
								for (int i = 0; i < jsonArr.length(); i++) {
									PDFBean pdf = new PDFBean();
									JSONObject row = jsonArr.getJSONObject(i);
									pdf.setPDF_TITLE(row.getString("pdf_title"));
									pdf.setPDF_SUB_TITLE(row.getString("pdf_sub_title"));
									pdf.setACTIVATED_DATE(row.getString("activated_date"));
									pdf.setFILE_TYPE(row.getString("file_type"));
									pdf.setFIRST_PAGE(row.getString("first_page_bg"));
									pdf.setPDF_ID(row.getString("pdf_id"));
									pdf.setPDF_LOGO(row.getString("pdf_logo"));
									pdf.setPDF_FILE(row.getString("pdf_file"));
									pdf.setPDF_THUMB(row.getString("pdf_thumb"));
									JSONArray linkedPdfData = row.getJSONArray("linked_file_data");
									for (int j = 0; j < linkedPdfData.length(); j++) {
										PDFBean linkedPDFBean = new PDFBean();
										JSONObject llinkedPdfDatarow = linkedPdfData.getJSONObject(j);
										linkedPDFBean.setPDF_FILE(llinkedPdfDatarow.getString("file_path"));
										linkedPDFBean.setFILE_ID(llinkedPdfDatarow.getString("file_id"));
										linkedPDFBean.setPDF_TITLE(llinkedPdfDatarow.getString("file_title"));
										linkedPDFBean.setPDF_SUB_TITLE(llinkedPdfDatarow.getString("file_sub_title"));
										linkedPDFBean.setFIRST_PAGE(llinkedPdfDatarow.getString("file_name"));
										linkedPDFBean.setPDF_THUMB(llinkedPdfDatarow.getString("file_thumb"));
										linkedPDFBean.setFILE_CODE(llinkedPdfDatarow.getString("file_code"));
										linkedPDFBean.setFILE_TYPE(llinkedPdfDatarow.getString("file_type"));
										linkedPDFBean.setPDF_ID(row.getString("pdf_id"));
										linkedBean.add(linkedPDFBean);
										
									}
									lst.add(pdf);
									JSONArray jEbookArr = row.getJSONArray("ebook_data");
									Log.v("linked pdf data", linkedPdfData+",");
									for (int j = 0; j < jEbookArr.length(); j++) {
										JSONObject ebook = jEbookArr.getJSONObject(j);
										EbookBean ebookBean = new EbookBean();
										ebookBean.setId(ebook.getString("id"));
										ebookBean.setPdf_id(ebook.getString("pdf_id"));
										ebookBean.setFile_name(ebook.getString("file_name"));
										ebookBean.setPdf_name(ebook.getString("pdf_name"));
										ebookBean.setChapter(ebook.getString("chapter"));
										ebookBean.setChapter_title(ebook.getString("chapter_title"));
//										if(linkedBean.size() > 0){
//											ebookBean.setLinkedBean(linkedBean);
//										}
										ebookLst.add(ebookBean);
									}
								}
								// Inserting Contacts
								Log.d("Insert: ", "Inserting ..");
								for (PDFBean data : lst) {
									db.addPDF(data);
								}
								// Reading all contacts
								for (EbookBean data : ebookLst) {
									db.addEbook(data);
								}
								for (PDFBean data : linkedBean) {
									db.addVideo(data);
								}
								Log.d("Reading: ", "Reading all contacts..");
								loadingResources(lst);
								deletePrivateFiles();

							} else {
								LoginActivity.this.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Functions.dismissLoadingDialog();
										// mProgressDialog.cancel();
										// This code will always run on
										// the UI thread, therefore is
										// safe to modify UI elements.
										if (username == null) {
											((EditText) findViewById(R.id.et_usn)).setError("Oops, would you please check your email and password ?");
										} else
											username.setError("Oops, would you please check your email and password ?");
										if (password == null) {
											((EditText) findViewById(R.id.et_pass)).setError("Oops, would you please check your email and password ?");
										} else
											password.setError("Oops, would you please check your email and password ?");
									}
								});
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					LoginActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Functions.dismissLoadingDialog();
							// mProgressDialog.cancel();
							// This code will always run on the UI thread,
							// therefore is safe to modify UI elements.
							if (username == null) {
								((EditText) findViewById(R.id.et_usn)).setError("Username or password mismatch");
							} else
								username.setError("Username or password mismatch");
							if (password == null) {
								((EditText) findViewById(R.id.et_pass)).setError("Username or password mismatch");
							} else
								password.setError("Username or password mismatch");
						}
					});
				}
				Looper.loop(); // Loop in the message queue
			}
		};

		t.start();
	}

	private void deletePrivateFiles() {
		String[] files = fileList();
		for (String fileName : files) {
			deleteFile(fileName);
		}
	}

	// double click back exit
	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
			super.onBackPressed();
			return;
		}

		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
	}

	private void loadingResources(List<PDFBean> pdfList) {
		Set<String> arr = new HashSet<String>();
		for (int a = 0; a < pdfList.size(); a++) {
			String pdfThumb = pdfList.get(a).getPDF_THUMB();
			// pdfThumb
			// String ss
			// ="http://medarkive.com/app/webroot/files/cover/default/android_default.png";
			if (pdfThumb.contains("default/default.png")) {
				// Log.v("Tag default.png chanage url", pdfThumb);
				arr.add("http://medarkive.com/app/webroot/files/cover/default/android_default.png");
			} else {
				arr.add(pdfList.get(a).getPDF_THUMB());
			}

		}
		if (arr.size() > 0) {

			Iterator<String> itr = arr.iterator();
			while (itr.hasNext()) {
				String ss = itr.next();
				String file = ss.substring(ss.lastIndexOf('/') + 1, ss.length());
				File check = new File(getFilesDir(), file);
				if (check.exists()) {
					itr.remove();
				}
			}
			String[] str = new String[arr.size()];
			int i = 0;
			for (String s : arr) {
				s = s.replaceAll("\\s+", "%20");
				str[i] = s;
				i++;
			}
			if (str.length > 0) {
				// System.out.println("downloading url "+str[0]);
				final DownloadTask downloadTask = new DownloadTask(LoginActivity.this);
				downloadTask.execute(str);

				// intent = new Intent(ctx, DisplayDataActivity.class);
				// startActivity(intent);
				// finish();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	private class DownloadTask extends AsyncTask<String, Integer, String> {
		private String name = "";
		private Context context;
		private PowerManager.WakeLock mWakeLock;

		public DownloadTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// take CPU lock to prevent CPU from going off if the user
			// presses the power button during download
			Functions.showLoadingDialog(LoginActivity.this, "Loading...");
			// mProgressDialog.show();
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
			mWakeLock.acquire();

		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
		}

		@Override
		protected void onPostExecute(String result) {
			Functions.dismissLoadingDialog();
			// mProgressDialog.dismiss();
			if (result != null) {
				// Toast.makeText(context, "Download error: " + result,
				// Toast.LENGTH_LONG).show();
				intent = new Intent(getApplicationContext(), DisplayDataActivity.class);
				startActivity(intent);
				finish();
			} else {
				// Toast.makeText(context, "File downloaded",
				// Toast.LENGTH_SHORT)
				// .show();
				intent = new Intent(getApplicationContext(), DisplayDataActivity.class);
				startActivity(intent);
				finish();
			}

		}

		@Override
		protected String doInBackground(String... sUrl) {
			// initAnimation();
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			for (int i = 0; i < sUrl.length; i++) {
				try {

					name = sUrl[i].toString().substring(sUrl[i].toString().lastIndexOf('/') + 1, sUrl[i].toString().length());
					sUrl[i] = sUrl[i].replaceAll("\\s+", "%20");
					URL url = new URL(sUrl[i]);
					connection = (HttpURLConnection) url.openConnection();
					connection.connect();

					// expect HTTP 200 OK, so we don't mistakenly save error
					// report
					// instead of the file
					if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
						return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
					}

					// this will be useful to display download percentage
					// might be -1: server did not report the length
					int fileLength = connection.getContentLength();
					// download the file
					input = connection.getInputStream();

					output = openFileOutput(name, Context.MODE_PRIVATE);
					byte data[] = new byte[4096];
					long total = 0;
					int count;
					while ((count = input.read(data)) != -1) {
						// allow canceling with back button
						if (isCancelled()) {
							input.close();
							return null;
						}
						total += count;
						// publishing the progress....
						if (fileLength > 0) // only if total length is known
							publishProgress((int) (total * 100 / fileLength));
						output.write(data, 0, count);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Functions.dismissLoadingDialog();
					intent = new Intent(getApplicationContext(), DisplayDataActivity.class);
					startActivity(intent);
					finish();
					return e.toString();
				} finally {
					try {
						if (output != null)
							output.close();
						if (input != null)
							input.close();
					} catch (IOException ignored) {
						Functions.dismissLoadingDialog();
						intent = new Intent(getApplicationContext(), DisplayDataActivity.class);
						startActivity(intent);
						finish();
					}
				}
				if (connection != null)
					connection.disconnect();
			}
			return null;
		}
	}

	/*
	 * private class DownloadTask extends AsyncTask<String, Integer, String> {
	 * 
	 * private Context context; private PowerManager.WakeLock mWakeLock;
	 * 
	 * public DownloadTask(Context context) { this.context = context; }
	 * 
	 * 
	 * @Override protected String doInBackground(String... sUrl) { InputStream
	 * input = null; FileOutputStream output = null; HttpURLConnection
	 * connection = null; try { URL url = new URL(sUrl[0]); connection =
	 * (HttpURLConnection) url.openConnection(); connection.connect();
	 * 
	 * // expect HTTP 200 OK, so we don't mistakenly save error report //
	 * instead of the file if (connection.getResponseCode() !=
	 * HttpURLConnection.HTTP_OK) { return "Server returned HTTP " +
	 * connection.getResponseCode() + " " + connection.getResponseMessage(); }
	 * 
	 * // this will be useful to display download percentage // might be -1:
	 * server did not report the length int fileLength =
	 * connection.getContentLength();
	 * 
	 * // download the file input = connection.getInputStream(); String
	 * filename=url.toString().substring(url.toString().lastIndexOf('/')+1
	 * ,url.toString().length()); output = openFileOutput(filename,
	 * Context.MODE_PRIVATE);
	 * 
	 * byte data[] = new byte[4096]; long total = 0; int count; while ((count =
	 * input.read(data)) != -1) { // allow canceling with back button if
	 * (isCancelled()) { input.close(); return null; } total += count; //
	 * publishing the progress.... if (fileLength > 0) // only if total length
	 * is known publishProgress((int) (total * 100 / fileLength));
	 * output.write(data, 0, count); } } catch (Exception e) { return
	 * e.toString(); } finally { try { if (output != null) output.close(); if
	 * (input != null) input.close(); } catch (IOException ignored) { }
	 * 
	 * if (connection != null) connection.disconnect(); } return null; }
	 * 
	 * @Override protected void onPreExecute() { super.onPreExecute(); // take
	 * CPU lock to prevent CPU from going off if the user // presses the power
	 * button during download PowerManager pm = (PowerManager) context
	 * .getSystemService(Context.POWER_SERVICE); mWakeLock =
	 * pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
	 * mWakeLock.acquire(); mProgressDialog.show(); }
	 * 
	 * @Override protected void onProgressUpdate(Integer... progress) {
	 * super.onProgressUpdate(progress); // if we get here, length is known, now
	 * set indeterminate to false mProgressDialog.setIndeterminate(false);
	 * mProgressDialog.setMax(100); mProgressDialog.setProgress(progress[0]); }
	 * 
	 * @Override protected void onPostExecute(String result) {
	 * mWakeLock.release(); mProgressDialog.dismiss(); if (result != null)
	 * Toast.makeText(context, "Download error: " + result,
	 * Toast.LENGTH_LONG).show(); else Toast.makeText(context,
	 * "File downloaded", Toast.LENGTH_SHORT) .show(); } }
	 */
}
