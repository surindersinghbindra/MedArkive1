package com.medarkive.Main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.medarkive.Adapter.CustomAdapterForNavDrawer;
import com.medarkive.Beans.BookmarkBean;
import com.medarkive.Beans.CPDBean;
import com.medarkive.Beans.EbookBean;
import com.medarkive.Beans.MyArkiveUrls;
import com.medarkive.Beans.PDFBean;
import com.medarkive.Beans.PDFTrackerBean;
import com.medarkive.Fragments.BookmarkListFragment;
import com.medarkive.Fragments.CPDLogListFragment;
import com.medarkive.Fragments.CPDOptionFragment;
import com.medarkive.Fragments.ChangePasswordFragment;
import com.medarkive.Fragments.EBOOKFragment;
import com.medarkive.Fragments.EditNameFragment;
import com.medarkive.Fragments.EditProfileFragment;
import com.medarkive.Fragments.EventDescriptionFragment;
import com.medarkive.Fragments.EventDetailsListFragment;
import com.medarkive.Fragments.EvetTypeListFragment;
import com.medarkive.Fragments.FeedBackFragment;
import com.medarkive.Fragments.MyArkiveFragment;
import com.medarkive.Fragments.PDFTabsFragment;
import com.medarkive.Fragments.SimpleWebViewFragment;
import com.medarkive.R;
import com.medarkive.Utilities.ConnectionDetector;
import com.medarkive.Utilities.Constants;
import com.medarkive.Utilities.DatabaseHandler;
import com.medarkive.Utilities.Functions;
import com.medarkive.Utilities.JsonManipulator;
import com.medarkive.Utilities.OAuthRequestTokenTask;
import com.medarkive.Utilities.SessionManager;
import com.medarkive.Utilities.TwitterUtils;
import com.medarkive.Utilities.WebService;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import rmn.androidscreenlibrary.ASSL;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

@SuppressLint("NewApi")
public class DisplayDataActivity extends FragmentActivity implements OnChildClickListener {

	public static String EXTRA_URL = "pdf_url";
	public DrawerLayout mDrawerLayout;
	// private LinearLay mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	public RelativeLayout mRelativeDrawer, homeTBar, webVewTBar, myarkiveTBar, ebookTopBar;
	private final String cpdURL = "http://medarkive.com/WebServices/cpd_graph/";
	private boolean doubleBackToExitPressedOnce;
	// private ProgressDialog mProgressDialog;
	String filename;
	private String URL = "http://medarkive.com/WebServices/index";
	private boolean enabled = true, isEbook = false;
	private final String BROWSER = "browser";
	private final String HOME = "home";
	private final String DASHBOARD = "dashboard";
	private final String FEEDBACK = "feedback";
	private ConnectionDetector cd;
	private static String tag;
	private ProgressBar pbar;
	private String cpdChoicestr = null;
	ArrayList<String> groupItem = new ArrayList<String>();
	ArrayList<Object> childItem = new ArrayList<Object>();
	// private android.app.ActionBar mActionBar;
	private float lastTranslate = 0.0f;
	int id = 0, bookNo = 0;
	private View lastview = null;
	private SessionManager sm;
	private HashMap<String, String> user;
	TextView statusTxt, logOutTxt, editProfilrTxt;
	public ImageView logo, backBtn;
	LinearLayout refreshLay, drawerLy, mysettingChild, mysettingLay, homeLay, cpdLay, myarkiveLay, wwwLay, dashboardLay, feedbackLay;
	ScrollView mDrawerList;
	public Fragment fr;
	public View view;
	String shareURL;
	int currentPdfId;

	//private UiLifecycleHelper uiHelper;
	private static String APP_ID = "1561393870774418"; // Replace your App
	// IDAPP_ID = "";
	//private Facebook facebook;
	//private AsyncFacebookRunner mAsyncRunner;

	private static final String[] PERMISSIONS = new String[] { "publish_actions" };

	private static final String TOKEN = "access_token";
	private static final String EXPIRES = "expires_in";
	private static final String KEY = "facebook-credentials";
	private String messageToPost;

	String authentcated = "";
	private static String var;
	private String temp = "";
	static Context cctx;
	private static DefaultHttpClient client;
	public static String secret;
	public static String tok;
	static User usert;
	private SharedPreferences prefs;
	boolean same_msg_chk_twt = false;
	long enqueue;
	DownloadManager dm;
	int i = 0;
	String[] str;
	ConnectionDetector mConnectionDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_data);
		mConnectionDetector= new ConnectionDetector(this);

		sm = new SessionManager(DisplayDataActivity.this);
		cd = new ConnectionDetector(DisplayDataActivity.this);
		user = sm.getUserDetails();
		statusTxt = (TextView) findViewById(R.id.status_txt);
		refreshLay = (LinearLayout) findViewById(R.id.refresh);
		logo = (ImageView) findViewById(R.id.logo);
		homeTBar = (RelativeLayout) findViewById(R.id.home_menu_bar);
		webVewTBar = (RelativeLayout) findViewById(R.id.web_view_bar);
		myarkiveTBar = (RelativeLayout) findViewById(R.id.myarkive_top_bar);
		ebookTopBar = (RelativeLayout) findViewById(R.id.ebook_top_bar);
		backBtn = (ImageView) findViewById(R.id.back);
		drawerLy = (LinearLayout) findViewById(R.id.drawer_icon);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mysettingChild = (LinearLayout) findViewById(R.id.mysetting_child_nav);
		mysettingLay = (LinearLayout) findViewById(R.id.mysetting_nav);
		wwwLay = (LinearLayout) findViewById(R.id.www_nav);
		homeLay = (LinearLayout) findViewById(R.id.home_nav);
		cpdLay = (LinearLayout) findViewById(R.id.cpd_nav);
		feedbackLay = (LinearLayout) findViewById(R.id.feedback_nav);
		myarkiveLay = (LinearLayout) findViewById(R.id.myarkive_nav);
		dashboardLay = (LinearLayout) findViewById(R.id.dashboard_nav);
		logOutTxt = (TextView) findViewById(R.id.logout_txt);
		editProfilrTxt = (TextView) findViewById(R.id.edit_profile_txt);
		new ASSL(DisplayDataActivity.this, mDrawerLayout, 1134, 720, false);

		if (cd.isConnectedToInternet()) {

			DatabaseHandler db = new DatabaseHandler(DisplayDataActivity.this);
			List<PDFTrackerBean> listtrackerPdf = db.getAllOFFLINEPDFTrackedData();
			Log.e("Offline data size", listtrackerPdf.size()+"");
			if (listtrackerPdf.size() > 0) {
				// mProgressDialog = new
				// ProgressDialog(DisplayDataActivity.this);
				// mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				// mProgressDialog.show();
				try {
					JSONArray pdfTrackerArr = new JSONArray();
					for (PDFTrackerBean bean : listtrackerPdf) {
						JSONObject pdfTrackingBean = new JSONObject();
						pdfTrackingBean.put("pdf_id", bean.getPdf_id());
						pdfTrackingBean.put("ip_address", bean.getIp_address());
						pdfTrackingBean.put("starttime", bean.getStarttime());
						pdfTrackingBean.put("country", bean.getCountry());
						pdfTrackingBean.put("city", bean.getCity());
						pdfTrackingBean.put("address", bean.getAddress());
						pdfTrackingBean.put("page", bean.getPage());
						pdfTrackingBean.put("endtime", bean.getEndtime());
						pdfTrackingBean.put("interval_sec", bean.getInterval_sec());
						pdfTrackingBean.put("file_id", bean.getFile_id());
						pdfTrackerArr.put(pdfTrackingBean);

						db.updatePDFTrackerData(bean.getTrack_id());
					}
					JSONObject sendingData = new JSONObject();
					sendingData.put("method", "OfflineTimeTrackOfReaders");
					sendingData.put("data", pdfTrackerArr);
					sendingData.put("user_id", user.get(SessionManager.KEY_USER_ID));
					sendingData.put("token", user.get(SessionManager.KEY_TOKEN));
					WebService webser = new WebService();
					webser.postJsonWebApi(sendingData, DisplayDataActivity.this);
					System.out.println("send online tracked data " + sendingData);
				} catch (Exception E) {
					E.printStackTrace();
				}
			}
			// mProgressDialog.dismiss();
		}

		fr = new PDFTabsFragment();
		final FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
		ft1.replace(R.id.content, fr);
		ft1.addToBackStack(null);
		ft1.commit();
		view = homeTBar;
		changeTopBar(fr, "Home", view);
		homeLay.setBackgroundColor(Color.parseColor("#8589A3"));
		changeNavbarColor(homeLay);
		// } else
		// setCustomActionBarHome();

		// System.out.println("fragment size ======" + ff.size());
		LoginActivity.path = getFilesDir() + "/";
		// mTitle = mDrawerTitle = getTitle();

		// CPD
		ImageView cpdMenu = (ImageView) findViewById(R.id.cpd_log);
		// www
		ImageView www = (ImageView) findViewById(R.id.www_button);
		// dashboard
		ImageView dashboard = (ImageView) findViewById(R.id.dashboard_button);
		// myarkive
		ImageView myarkive = (ImageView) findViewById(R.id.myarkive_button);

		cpdMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final SessionManager sm = new SessionManager(DisplayDataActivity.this);
				String cpdchicesString = sm.getCPDChoice();
				if (cpdchicesString == null) {
					final CharSequence[] items = { "CPD UK", "CPD International" };
					AlertDialog.Builder builder = new AlertDialog.Builder(DisplayDataActivity.this);
					builder.setTitle("CPD Log Choice");
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (cpdChoicestr != null) {
								if (cpdChoicestr.contains("1") || cpdChoicestr.contains("2")) {
									sm.setCPDChoice(cpdChoicestr);
								}
							}
						}
					});

					builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							cpdLay.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub

								}
							});
							if ("CPD UK".equals(items[which])) {
								cpdChoicestr = "1";
							} else if ("CPD International".equals(items[which])) {
								cpdChoicestr = "2";
							}
						}
					});
					builder.show();
				} else {

					if (!cd.isConnectedToInternet()) {
						cd.showAlertDialog(cd.isConnectedToInternet());
					} else {
						FragmentTransaction ffCPD = getSupportFragmentManager().beginTransaction();

						fr = new CPDLogListFragment();
						changeTopBar(fr, "CPD", homeTBar);
						ffCPD.replace(R.id.content, fr);
						ffCPD.commit();
						// final LinearLayout cpdTextView = (LinearLayout)
						// getViewByPosition(
						// 1, mDrawerList);
						cpdLay.setBackgroundColor(Color.parseColor("#58C5BC"));
						// if (lastview != null) {
						// lastview.setBackgroundColor(Color
						// .parseColor("#4B566D"));
						// }
						changeNavbarColor(cpdLay);
					}
				}
			}
		});

		www.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!cd.isConnectedToInternet()) {
					cd.showAlertDialog(cd.isConnectedToInternet());
				} else {
					// setCustomActionBarforBrowser();
					// Fragment browser = fm.findFragmentByTag(BROWSER);
					// if (browser == null) {

					FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
					fr = new SimpleWebViewFragment();
					Bundle bundle = new Bundle();
					bundle.putString(SimpleWebViewFragment.EXTRA_URL, "https://www.google.com");
					fr.setArguments(bundle);
					// }
					ftt.replace(R.id.content, fr, BROWSER);
					ftt.addToBackStack(null);
					ftt.commit();
					changeTopBar(fr, "WWW", webVewTBar);

					hideBottomBar();
					// final LinearLayout wwwTextView = (LinearLayout)
					// getViewByPosition(
					// 2, mDrawerList);
					wwwLay.setBackgroundColor(Color.parseColor("#F45851"));
					// if (lastview != null) {
					// lastview.setBackgroundColor(Color.parseColor("#4B566D"));
					// }
					changeNavbarColor(wwwLay);
				}
			}
		});

		dashboard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!cd.isConnectedToInternet()) {
					cd.showAlertDialog(cd.isConnectedToInternet());
				} else {
					// TODO Auto-generated method stub
					view.setVisibility(8);

					FragmentTransaction ff2 = getSupportFragmentManager().beginTransaction();
					fr = new SimpleWebViewFragment();
					Bundle bde = new Bundle();
					SessionManager sm = new SessionManager(DisplayDataActivity.this);
					String cpd = sm.getCPDChoice();
					if (cpd.equals("1")) {
						bde.putString(SimpleWebViewFragment.EXTRA_URL, cpdURL + "1");
					} else if (cpd.equals("2")) {
						bde.putString(SimpleWebViewFragment.EXTRA_URL, cpdURL + "2");
					}
					bde.putBoolean(SimpleWebViewFragment.dashboard_check, true);
					fr.setArguments(bde);
					// }

					ff2.replace(R.id.content, fr, DASHBOARD);
					ff2.addToBackStack(null);
					ff2.commit();
					changeTopBar(fr, "Dashboard", homeTBar);
					hideBottomBar();

					// final LinearLayout dashboardTxtView = (LinearLayout)
					// getViewByPosition(
					// 4, mDrawerList);
					dashboardLay.setBackgroundColor(Color.parseColor("#FFB459"));
					// if (lastview != null) {
					// lastview.setBackgroundColor(Color.parseColor("#4B566D"));
					// }
					changeNavbarColor(dashboardLay);
				}
			}
		});

		myarkive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!cd.isConnectedToInternet()) {
					cd.showAlertDialog(cd.isConnectedToInternet());
				} else {
					view.setVisibility(8);

					FragmentTransaction ftMyArkive = getSupportFragmentManager().beginTransaction();
					fr = new MyArkiveFragment();
					ftMyArkive.replace(R.id.content, fr, DASHBOARD);
					ftMyArkive.addToBackStack(null);
					ftMyArkive.commit();
					hideBottomBar();
					changeTopBar(fr, "Myarkive", homeTBar);
					// final LinearLayout myArkiveTxtview = (LinearLayout)
					// getViewByPosition(
					// 3, mDrawerList);
					myarkiveLay.setBackgroundColor(Color.parseColor("#9885C3"));
					// if (lastview != null) {
					// lastview.setBackgroundColor(Color.parseColor("#4B566D"));
					// }
					changeNavbarColor(myarkiveLay);
				}
			}
		});

		findViewById(R.id.refresh).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// refresh();
			}
		});

		findViewById(R.id.drawer_icon).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean check = mDrawerLayout.isDrawerOpen(mRelativeDrawer);
				if (check) {
					mDrawerLayout.closeDrawer(mRelativeDrawer);
				} else {
					mDrawerLayout.openDrawer(mRelativeDrawer);
				}
			}
		});

		homeLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				view.setVisibility(8);
				mysettingChild.setVisibility(8);
				showbottomBar();
				FragmentTransaction ff = getSupportFragmentManager().beginTransaction();
				// Fragment home = fm.findFragmentByTag(HOME);
				// if (home == null) {

				fr = new PDFTabsFragment();
				// }
				ff.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left);
				ff.replace(R.id.content, fr, HOME);
				// ft1.addToBackStack(null);
				ff.commit();
				changeTopBar(fr, "Home", homeTBar);
				// mDrawerList.setItemChecked(groupPosition, true);
				v.setBackgroundColor(Color.parseColor("#8589A3"));
				changeNavbarColor(v);
				mDrawerLayout.closeDrawer(mRelativeDrawer);
			}
		});

		wwwLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mysettingChild.setVisibility(8);
				if (!cd.isConnectedToInternet()) {
					cd.showAlertDialog(cd.isConnectedToInternet());
				} else {
					// Browser
					// setCustomActionBarforBrowser();
					view.setVisibility(8);

					// Fragment browser = fm.findFragmentByTag(BROWSER);
					// if (browser == null) {
					FragmentTransaction ff1 = getSupportFragmentManager().beginTransaction();
					fr = new SimpleWebViewFragment();
					Bundle bundle = new Bundle();
					bundle.putString(SimpleWebViewFragment.EXTRA_URL, "https://www.google.com");
					fr.setArguments(bundle);
					// }
					ff1.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
					ff1.replace(R.id.content, fr, BROWSER);
					ff1.addToBackStack(null);
					ff1.commit();

					changeTopBar(fr, "WWW", webVewTBar);
					// mDrawerList.setItemChecked(groupPosition, true);
					v.setBackgroundColor(Color.parseColor("#F45851"));
					changeNavbarColor(v);
					hideBottomBar();

					mDrawerLayout.closeDrawer(mRelativeDrawer);
				}
			}
		});
		cpdLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mysettingChild.setVisibility(8);
				final SessionManager sm = new SessionManager(DisplayDataActivity.this);
				String cpdchicesString = sm.getCPDChoice();
				if (cpdchicesString == null) {
					final CharSequence[] items = { "CPD UK", "CPD International" };
					AlertDialog.Builder builder = new AlertDialog.Builder(DisplayDataActivity.this);
					builder.setTitle("CPD Log Choice");
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (cpdChoicestr != null) {
								if (cpdChoicestr.contains("1") || cpdChoicestr.contains("2")) {
									sm.setCPDChoice(cpdChoicestr);
								}
							}
						}
					});

					builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							if ("CPD UK".equals(items[which])) {
								cpdChoicestr = "1";
							} else if ("CPD International".equals(items[which])) {
								cpdChoicestr = "2";
							}
						}
					});
					builder.show();
				} else {

					if (!cd.isConnectedToInternet()) {
						cd.showAlertDialog(cd.isConnectedToInternet());
					} else {
						view.setVisibility(8);

						FragmentTransaction ffCPD = getSupportFragmentManager().beginTransaction();
						fr = new CPDLogListFragment();
						ffCPD.replace(R.id.content, fr);
						ffCPD.commit();
						// final LinearLayout cpdTextView = (LinearLayout)
						// getViewByPosition(
						// 1, mDrawerList);
						cpdLay.setBackgroundColor(Color.parseColor("#58C5BC"));
						// if (lastview != null) {
						// lastview.setBackgroundColor(Color
						// .parseColor("#4B566D"));
						// }
						changeTopBar(fr, "CPD", homeTBar);
						changeNavbarColor(cpdLay);
					}
				}
				// mDrawerList.setItemChecked(groupPosition, true);
				// v.setBackgroundColor(Color.parseColor("#58C5BC"));
				// changeNavbarColor(v);
				mDrawerLayout.closeDrawer(mRelativeDrawer);
			}
		});

		dashboardLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mysettingChild.setVisibility(8);
				if (!cd.isConnectedToInternet()) {
					cd.showAlertDialog(cd.isConnectedToInternet());

				} else {
					// Fragment dashboard = fm.findFragmentByTag(DASHBOARD);
					// if (dashboard == null) {\
					view.setVisibility(8);
					FragmentTransaction ff2 = getSupportFragmentManager().beginTransaction();
					fr = new SimpleWebViewFragment();

					Bundle bde = new Bundle();
					// bde.putString(SimpleWebViewFragment.EXTRA_URL,
					// cpdURL);
					SessionManager sm = new SessionManager(DisplayDataActivity.this);
					String cpd = sm.getCPDChoice();
					String userid = user.get(SessionManager.KEY_USER_ID);
					if (cpd.equals("1")) {
						bde.putString(SimpleWebViewFragment.EXTRA_URL, cpdURL + "/" + userid + "/1");
					} else if (cpd.equals("2")) {
						bde.putString(SimpleWebViewFragment.EXTRA_URL, cpdURL + "/" + userid + "/2");
					}
					bde.putBoolean(SimpleWebViewFragment.dashboard_check, true);
					fr.setArguments(bde);
					// }
					ff2.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
					ff2.replace(R.id.content, fr, DASHBOARD);
					ff2.addToBackStack(null);
					ff2.commit();
					changeTopBar(fr, "Dashboard", homeTBar);
					// mDrawerList.setItemChecked(groupPosition, true);
					v.setBackgroundColor(Color.parseColor("#FFB459"));
					changeNavbarColor(v);
					hideBottomBar();
					mDrawerLayout.closeDrawer(mRelativeDrawer);
				}
			}
		});

		mysettingLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundColor(Color.parseColor("#3469AF"));
				changeNavbarColor(v);
				// TODO Auto-generated method stub
				if (mysettingChild.getVisibility() == LinearLayout.GONE)
					mysettingChild.setVisibility(0);
				else
					mysettingChild.setVisibility(8);
			}
		});

		myarkiveLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				view.setVisibility(8);
				mysettingChild.setVisibility(8);
				FragmentTransaction ftMyArkive = getSupportFragmentManager().beginTransaction();
				fr = new MyArkiveFragment();
				ftMyArkive.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
				ftMyArkive.replace(R.id.content, fr);
				ftMyArkive.addToBackStack(null);
				ftMyArkive.commit();
				hideBottomBar();
				changeTopBar(fr, "Myarkive", homeTBar);
				// mDrawerList.setItemChecked(groupPosition, true);
				v.setBackgroundColor(Color.parseColor("#9885C3"));
				changeNavbarColor(v);
				mDrawerLayout.closeDrawer(mRelativeDrawer);
			}
		});

		feedbackLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				view.setVisibility(8);
				mysettingChild.setVisibility(8);
				FragmentTransaction ff3 = getSupportFragmentManager().beginTransaction();
				// Fragment feedback = fm.findFragmentByTag(FEEDBACK);
				// if (feedback == null) {
				fr = new FeedBackFragment();
				// }
				ff3.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left);
				ff3.replace(R.id.content, fr, FEEDBACK);
				ff3.addToBackStack(null);
				ff3.commit();

				hideBottomBar();
				changeTopBar(fr, "Feedback", homeTBar);
				// mDrawerList.setItemChecked(groupPosition, true);
				v.setBackgroundColor(Color.parseColor("#ED6979"));
				changeNavbarColor(v);
				mDrawerLayout.closeDrawer(mRelativeDrawer);
			}
		});

		logOutTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SessionManager sm = new SessionManager(v.getContext());
				sm.logoutUser();
				DatabaseHandler db = new DatabaseHandler(v.getContext());
				db.deleteAllTable();
			}
		});

		editProfilrTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edit_profile();
			}
		});

		refreshLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mConnectionDetector.isConnectedToInternet()){
					new RefreshData().execute("");
				}
				else{
					Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
				}
				
			}
		});

		HashMap<String, String> userDetail = sm.getUserDetails();

		id = Integer.parseInt(userDetail.get(SessionManager.KEY_USER_ID));
		// setCustomActionBarHome();
		setGroupData();
		setChildGroupData();
		initDrawer();

		/*uiHelper = new UiLifecycleHelper(DisplayDataActivity.this, null);
		uiHelper.onCreate(savedInstanceState);
		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);*/
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("on pause");
		//uiHelper.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("on Resume");
		// uiHelper.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("On Destroy");
		//uiHelper.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

/*		uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
			@Override
			public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
				Log.e("Activity", String.format("Error: %s", error.toString()));
			}

			@Override
			public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
				Log.i("Activity", "Success!");
			}
		});*/
	}

	public void changeNavbarColor(View view) {
		if (lastview != null && !view.equals(lastview))
			lastview.setBackgroundColor(Color.parseColor("#49546E"));

		lastview = view;
	}

	public void forward(JSONObject ob, String category_id) {
		ArrayList<CPDBean> arr = null;
		arr = new JsonManipulator().convertJsonToArrayListforSavedEventDetail(ob, DisplayDataActivity.this, Integer.parseInt(category_id));
		// System.out.println("arr list size  <<<<<<<<<--------->>>>>>>>"
		// + arr.size());
		Bundle bdl = new Bundle();
		bdl.putParcelableArrayList(LoginActivity.Extra_Value, arr);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		EventDetailsListFragment listEvent = new EventDetailsListFragment();
		listEvent.setArguments(bdl);
		ft.replace(R.id.content, listEvent);
		hideKeyboard();
		ft.addToBackStack(null);
		ft.commit();
	}

	// public void forwardToEventDetail(JSONObject ob, String category_id,
	// String name) {
	// // new AlertDialog.Builder(DisplayDataActivity.this)
	// // // .setTitle("CPD event Deleted Succesfully")
	// // .setMessage("CPD event deleted successfully.")
	// // .setCancelable(false)
	// // .setPositiveButton("ok", new DialogInterface.OnClickListener() {
	// // @Override
	// // public void onClick(DialogInterface dialog, int which) {
	// // // TODO Auto-generated method stub
	// // }
	// // }).create().show();
	// ArrayList<CPDBean> arr = null;
	// HashMap<String, ArrayList<CPDBean>> map = new JsonManipulator()
	// .jsonToMapDeleEven(ob, DisplayDataActivity.this);
	// arr = map.get(name);
	// Bundle bdl = new Bundle();
	// bdl.putParcelableArrayList(LoginActivity.Extra_Value, arr);
	// FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	// EventDetailsListFragment listEvent = new EventDetailsListFragment();
	// listEvent.setArguments(bdl);
	// ft.replace(R.id.content, listEvent);
	// ft.addToBackStack(null);
	// ft.commit();
	// }

	public void enable(boolean b) {
		enabled = b;
	}

	// @Override
	// public boolean dispatchTouchEvent(MotionEvent ev) {
	// if (enabled)
	// return super.dispatchTouchEvent(ev);
	// return true;
	// }

	public View getViewByPosition(int position, ExpandableListView listView) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
		if (position < firstListItemPosition || position > lastListItemPosition) {
			return listView.getAdapter().getView(position, null, listView);
		} else {
			final int childIndex = position - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}

	// @SuppressLint("InflateParams")
	// public void setCustomActionBarHome() {
	// android.app.ActionBar mActionBar = getActionBar();
	// mActionBar.setDisplayShowHomeEnabled(false);
	// mActionBar.setDisplayShowTitleEnabled(false);
	// LayoutInflater mInflater = LayoutInflater.from(this);
	//
	// View mCustomView = mInflater.inflate(R.layout.custom_actionbar_menu,
	// null);
	// mActionBar.setCustomView(mCustomView);
	// mActionBar.setDisplayShowCustomEnabled(true);
	//
	// mCustomView.findViewById(R.id.refresh).setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// refresh();
	// }
	// });
	// mCustomView.findViewById(R.id.drawer_icon).setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// boolean check = mDrawerLayout
	// .isDrawerOpen(mRelativeDrawer);
	// if (check) {
	// mDrawerLayout.closeDrawer(mRelativeDrawer);
	// } else {
	// mDrawerLayout.openDrawer(mRelativeDrawer);
	// }
	// }
	// });
	// }

	public void changeTopBar(Fragment fr, String frName, View v) {
		// view.setVisibility(8);
		view = v;
		if (fr.toString().startsWith("PDFTabsFragment")) {
			homeTBar.setVisibility(0);
			ebookTopBar.setVisibility(8);
			statusTxt.setVisibility(8);
			logo.setVisibility(0);
			refreshLay.setVisibility(0);
			backBtn.setVisibility(8);
			drawerLy.setVisibility(0);
		} else if (fr.toString().startsWith("SimpleWebViewFragment")) {
			if (frName.contains("Dashboard")) {
				drawerLy.setVisibility(0);
				homeTBar.setVisibility(0);
				ebookTopBar.setVisibility(8);
				logo.setVisibility(8);
				refreshLay.setVisibility(8);
				backBtn.setVisibility(8);
				statusTxt.setVisibility(0);

				if (sm.getCPDChoice() != null) {
					String cpd = sm.getCPDChoice();
					if (cpd.equals("1")) {
						statusTxt.setText("UK Physician Dashboard");
					} else {
						statusTxt.setText("International Dashboard");
					}
				} else {
					statusTxt.setText("UK Physician Dashboard");
				}
			} else if (frName.contains("MyArkiveView")) {
				myarkiveTBar.setVisibility(0);
			} else if (frName.contains("WWW")) {
				drawerLy.setVisibility(0);
				webVewTBar.setVisibility(0);
				new SimpleWebViewFragment().setCustomActionBarForBrowser(DisplayDataActivity.this);
			}
		} else if (fr.toString().startsWith("FeedBackFragment")) {
			drawerLy.setVisibility(0);
			homeTBar.setVisibility(0);
			ebookTopBar.setVisibility(8);
			logo.setVisibility(8);
			refreshLay.setVisibility(8);
			backBtn.setVisibility(8);
			statusTxt.setVisibility(0);
			statusTxt.setText("MedArkive Feedback");
		} else if (fr.toString().startsWith("EditProfileFragment")) {
			drawerLy.setVisibility(0);
			homeTBar.setVisibility(0);
			logo.setVisibility(8);
			ebookTopBar.setVisibility(8);
			backBtn.setVisibility(8);
			refreshLay.setVisibility(8);
			statusTxt.setVisibility(0);
			statusTxt.setText("Edit Profile");
		} else if (fr.toString().startsWith("CPDLogListFragment")) {
			drawerLy.setVisibility(0);
			sm = new SessionManager(DisplayDataActivity.this);
			homeTBar.setVisibility(0);
			logo.setVisibility(8);
			ebookTopBar.setVisibility(8);
			refreshLay.setVisibility(8);
			backBtn.setVisibility(8);
			statusTxt.setVisibility(0);
			String str = sm.getCPDChoice();
			if (str.equals("1")) {
				statusTxt.setText("CPD Log (UK)");
			} else if (str.equals("2")) {
				statusTxt.setText("CPD Log (International)");
			}
		} else if (fr.toString().startsWith("MyArkiveFragment")) {
			drawerLy.setVisibility(0);
			homeTBar.setVisibility(0);
			ebookTopBar.setVisibility(8);
			logo.setVisibility(8);
			refreshLay.setVisibility(8);
			statusTxt.setVisibility(0);
			backBtn.setVisibility(8);
			statusTxt.setText("MyArkive");
		} else if (fr.toString().startsWith("EBOOKFragment")) {
			drawerLy.setVisibility(0);
			homeTBar.setVisibility(8);
			logo.setVisibility(8);
			refreshLay.setVisibility(8);
			statusTxt.setVisibility(0);
			backBtn.setVisibility(8);
			ebookTopBar.setVisibility(0);
		}

	}

	// public void setCustomActionBarforBrowser() {
	// // android.app.ActionBar mActionBar = getActionBar();
	// // mActionBar.setDisplayShowHomeEnabled(false);
	// // mActionBar.setDisplayShowTitleEnabled(false);
	// // LayoutInflater mInflater = LayoutInflater
	// // .from(DisplayDataActivity.this);
	//
	// // View mCustomView = mInflater.inflate(
	// // R.layout.custom_action_bar_webview, null);
	// // mActionBar.setCustomView(mCustomView);
	// // mActionBar.setDisplayShowCustomEnabled(true);
	//
	// View forward = findViewById(R.id.forward);
	// forward.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// SimpleWebViewFragment.goForward();
	// }
	// });
	//
	// View back = findViewById(R.id.back);
	// back.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// SimpleWebViewFragment.goBack();
	// }
	// });
	// }

	private void initDrawer() {

		if (mDrawerLayout == null) {
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		}

		if (mDrawerList == null) {
			mDrawerList = (ScrollView) findViewById(R.id.userDrawerList);
			CustomAdapterForNavDrawer adapter = new CustomAdapterForNavDrawer(this, groupItem, childItem);
			// mDrawerLayout.setDrawerShadow(R.drawable.nav_drawer_shadow,
			// GravityCompat.START);
			mDrawerLayout.setScrimColor(Color.parseColor("#00FFFFFF"));
			// mDrawerList.setAdapter(adapter);
		}
		if (mRelativeDrawer == null) {
			mRelativeDrawer = (RelativeLayout) findViewById(R.id.relativeDrawer);
		}
		// System.out.println("child Count-" + mDrawerList.getChildCount());
		// mDrawerList.setOnChildClickListener(this);
		// final LinearLayout bottom = (LinearLayout)
		// findViewById(R.id.bottom_bar);
		final FrameLayout frame = (FrameLayout) findViewById(R.id.content);

		// mDrawerList.setOnGroupClickListener(new OnGroupClickListener() {
		// @Override
		// public boolean onGroupClick(ExpandableListView parent, View v,
		// int groupPosition, long id) {
		//
		// if (lastview != null) {
		// lastview.setBackgroundColor(Color.parseColor("#4B566D"));
		// }
		// lastview = v;
		// switch (groupPosition) {
		//
		// case 0: // Home
		// // setCustomActionBarHome();
		// view.setVisibility(8);
		// changeTopBar("Home", homeTBar);
		// showbottomBar();
		// FragmentTransaction ff = getSupportFragmentManager()
		// .beginTransaction();
		// // Fragment home = fm.findFragmentByTag(HOME);
		// // if (home == null) {
		//
		// fr = new PDFTabsFragment();
		// // }
		// ff.setCustomAnimations(R.anim.slide_in_left,
		// R.anim.slide_in_left);
		// ff.replace(R.id.content, fr, HOME);
		// // ft1.addToBackStack(null);
		// ff.commit();
		// mDrawerList.setItemChecked(groupPosition, true);
		// v.setBackgroundColor(Color.parseColor("#8589A3"));
		// mDrawerLayout.closeDrawer(mRelativeDrawer);
		//
		// break;
		// case 1: // CPD
		//
		// // TODO Auto-generated method stub
		// final SessionManager sm = new SessionManager(
		// DisplayDataActivity.this);
		// String cpdchicesString = sm.getCPDChoice();
		// if (cpdchicesString == null) {
		// final CharSequence[] items = { "CPD UK",
		// "CPD International" };
		// AlertDialog.Builder builder = new AlertDialog.Builder(
		// DisplayDataActivity.this);
		// builder.setTitle("CPD Log Choice");
		// builder.setPositiveButton("OK",
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// if (cpdChoicestr != null) {
		// if (cpdChoicestr.contains("1")
		// || cpdChoicestr
		// .contains("2")) {
		// sm.setCPDChoice(cpdChoicestr);
		// }
		// }
		// }
		// });
		//
		// builder.setSingleChoiceItems(items, -1,
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// // TODO Auto-generated method stub
		//
		// if ("CPD UK".equals(items[which])) {
		// cpdChoicestr = "1";
		// } else if ("CPD International"
		// .equals(items[which])) {
		// cpdChoicestr = "2";
		// }
		// }
		// });frName.contains
		// builder.show();
		// } else {
		//
		// if (!cd.isConnectedToInternet()) {
		// cd.showAlertDialog(cd.isConnectedToInternet());
		// } else {
		// view.setVisibility(8);
		// changeTopBar("CPD", homeTBar);
		// FragmentTransaction ffCPD = getSupportFragmentManager()
		// .beginTransaction();
		// fr = new CPDLogListFragment();
		// ffCPD.replace(R.id.content, fr);
		// ffCPD.commit();
		// final LinearLayout cpdTextView = (LinearLayout) getViewByPosition(
		// 1, mDrawerList);
		// cpdTextView.setBackgroundColor(Color
		// .parseColor("#58C5BC"));
		// if (lastview != null) {
		// lastview.setBackgroundColor(Color
		// .parseColor("#4B566D"));
		// }
		// lastview = cpdTextView;
		// }
		// }
		//
		// mDrawerList.setItemChecked(groupPosition, true);
		// v.setBackgroundColor(Color.parseColor("#58C5BC"));
		// mDrawerLayout.closeDrawer(mRelativeDrawer);
		// break;
		//
		// case 2:
		// if (!cd.isConnectedToInternet()) {
		// cd.showAlertDialog(cd.isConnectedToInternet());
		// } else {
		// // Browser
		// // setCustomActionBarforBrowser();
		// view.setVisibility(8);
		// changeTopBar("WWW", webVewTBar);
		// // Fragment browser = fm.findFragmentByTag(BROWSER);
		// // if (browser == null) {
		// FragmentTransaction ff1 = getSupportFragmentManager()
		// .beginTransaction();
		// fr = new SimpleWebViewFragment();
		// Bundle bundle = new Bundle();
		// bundle.putString(SimpleWebViewFragment.EXTRA_URL,
		// "https://www.google.com");
		// fr.setArguments(bundle);
		// // }
		// ff1.setCustomAnimations(R.anim.slide_in_left,
		// R.anim.slide_out_right);
		// ff1.replace(R.id.content, fr, BROWSER);
		// ff1.addToBackStack(null);
		// ff1.commit();
		//
		// mDrawerList.setItemChecked(groupPosition, true);
		// v.setBackgroundColor(Color.parseColor("#F45851"));
		// hideBottomBar();
		//
		// mDrawerLayout.closeDrawer(mRelativeDrawer);
		// }
		// break;
		//
		// case 3: // My Arkive
		// view.setVisibility(8);
		// changeTopBar("Myarkive", homeTBar);
		// FragmentTransaction ftMyArkive = getSupportFragmentManager()
		// .beginTransaction();
		// ListFragment myArkive = new MyArkiveFragment();
		// ftMyArkive.setCustomAnimations(R.anim.slide_in_left,
		// R.anim.slide_out_right);
		// ftMyArkive.replace(R.id.content, myArkive);
		// ftMyArkive.addToBackStack(null);
		// ftMyArkive.commit();
		// hideBottomBar();
		//
		// mDrawerList.setItemChecked(groupPosition, true);
		// v.setBackgroundColor(Color.parseColor("#9885C3"));
		//
		// mDrawerLayout.closeDrawer(mRelativeDrawer);
		// break;
		//
		// case 4: // DashBoard
		// // setCustomActionBarHome();
		// if (!cd.isConnectedToInternet()) {
		// cd.showAlertDialog(cd.isConnectedToInternet());
		//
		// } else {
		// // Fragment dashboard = fm.findFragmentByTag(DASHBOARD);
		// // if (dashboard == null) {\
		//
		// view.setVisibility(8);
		// changeTopBar("Dashboard", homeTBar);
		// FragmentTransaction ff2 = getSupportFragmentManager()
		// .beginTransaction();
		// fr = new SimpleWebViewFragment();
		// Bundle bde = new Bundle();
		// bde.putString(SimpleWebViewFragment.EXTRA_URL, cpdURL);
		// bde.putBoolean(SimpleWebViewFragment.dashboard_check,
		// true);
		// fr.setArguments(bde);
		// // }
		// ff2.setCustomAnimations(R.anim.slide_in_left,
		// R.anim.slide_out_right);
		// ff2.replace(R.id.content, fr, DASHBOARD);
		// ff2.addToBackStack(null);
		// ff2.commit();
		//
		// mDrawerList.setItemChecked(groupPosition, true);
		// v.setBackgroundColor(Color.parseColor("#FFB459"));
		// hideBottomBar();
		// mDrawerLayout.closeDrawer(mRelativeDrawer);
		// }
		// break;
		//
		// case 5: // My Settings
		//
		// mDrawerList.setItemChecked(groupPosition, true);
		// v.setBackgroundColor(Color.parseColor("#3469AF"));
		// break;
		//
		// case 6: // FeedBack
		//
		// view.setVisibility(8);
		// changeTopBar("Feedback", homeTBar);
		// FragmentTransaction ff3 = getSupportFragmentManager()
		// .beginTransaction();
		// // Fragment feedback = fm.findFragmentByTag(FEEDBACK);
		// // if (feedback == null) {
		// fr = new FeedBackFragment();
		// // }
		// ff3.setCustomAnimations(R.anim.slide_in_left,
		// R.anim.slide_in_left);
		// ff3.replace(R.id.content, fr, FEEDBACK);
		// ff3.addToBackStack(null);
		// ff3.commit();
		//
		// hideBottomBar();
		// mDrawerList.setItemChecked(groupPosition, true);
		// v.setBackgroundColor(Color.parseColor("#ED6979"));
		// mDrawerLayout.closeDrawer(mRelativeDrawer);
		// break;
		// }
		//
		// // aa.getGroupView(1, true, v , (ViewGroup) v.getRootView());
		//
		// ViewHolder vh = (ViewHolder) v.getTag();
		// TextView vie = vh.txtview;
		//
		// String tag1 = (String) vie.getText();
		// // String tag1 = (String) ((TextView) v).getText();
		// Log.v("Tag ", "tag ->" + tag1);
		// if (tag1.contains("Home")) {
		// v.setBackgroundColor(Color.parseColor("#8589A3"));
		// }
		// if (tag1.contains("CPD Log")) {
		// v.setBackgroundColor(Color.parseColor("#58C5BC"));
		// }
		// if (tag1.contains("Browser")) {
		// v.setBackgroundColor(Color.parseColor("#F45851"));
		// }
		// if (tag1.contains("My Arkive")) {
		// v.setBackgroundColor(Color.parseColor("#9885C3"));
		// }
		// if (tag1.contains("Dashboard")) {
		// v.setBackgroundColor(Color.parseColor("#FFB459"));
		// }
		// if (tag1.contains("My Setting")) {
		// v.setBackgroundColor(Color.parseColor("#3469AF"));
		// }
		// if (tag1.contains("Feedback")) {
		// v.setBackgroundColor(Color.parseColor("#ED6979"));
		// }
		//
		// return false;
		// }
		//
		// });

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
			@SuppressLint("NewApi")
			public void onDrawerSlide(View drawerView, float slideOffset) {
				// if (lastview == null) {
				// final LinearLayout home = (LinearLayout) getViewByPosition(
				// 0, mDrawerList);
				// home.setBackgroundColor(Color.parseColor("#8589A3"));
				// lastview = home;
				// System.out.println("check ");
				// System.out.println(home == null);
				// }
				float moveFactor = (mDrawerList.getWidth() * slideOffset);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					frame.setTranslationX(moveFactor);
					// bottom.setTranslationX(moveFactor);
				} else {
					TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
					anim.setDuration(0);
					anim.setFillAfter(true);
					frame.startAnimation(anim);
					// bottom.startAnimation(anim);
					lastTranslate = moveFactor;
				}
				if (drawerView != null) {
					super.onDrawerSlide(drawerView, 0);
				} else {
					super.onDrawerSlide(drawerView, slideOffset);
				}
			}

			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				// getActionBar().setTitle("");
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				// getActionBar().setTitle("");
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	public void myArkiveLinkClicked(MyArkiveUrls urls) {
		view.setVisibility(8);

		FragmentTransaction ff2 = getSupportFragmentManager().beginTransaction();
		fr = new SimpleWebViewFragment();
		Bundle bde = new Bundle();
		bde.putString(SimpleWebViewFragment.EXTRA_URL, urls.getlink());
		bde.putBoolean(SimpleWebViewFragment.dashboard_check, false);
		bde.putBoolean(SimpleWebViewFragment.MYARKIVE_CHECK, true);
		fr.setArguments(bde);
		changeTopBar(fr, "MyArkiveView", myarkiveTBar);
		hideBottomBar();
		ff2.replace(R.id.content, fr);
		ff2.addToBackStack(null);
		ff2.commit();
	}

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

	public void setGroupData() {
		for (String str : getResources().getStringArray(R.array.side_menu_array)) {
			groupItem.add(str);
		}
	}

	public void setChildGroupData() {

		ArrayList<String> child = new ArrayList<String>();
		// home
		child = new ArrayList<String>();
		childItem.add(child);
		// CPD
		child = new ArrayList<String>();
		childItem.add(child);
		// browser
		child = new ArrayList<String>();
		childItem.add(child);
		// myarkive
		child = new ArrayList<String>();
		childItem.add(child);
		// dashBoard
		child = new ArrayList<String>();
		childItem.add(child);
		// settings
		child = new ArrayList<String>();
		for (String str : getResources().getStringArray(R.array.settings_submenu)) {
			child.add(str);
		}
		childItem.add(child);
		// feedback
		child = new ArrayList<String>();
		childItem.add(child);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	protected void refresh() {
		if (!cd.isConnectedToInternet()) {
			cd.showAlertDialog(cd.isConnectedToInternet());

		} else {
			final DatabaseHandler db = new DatabaseHandler(getApplicationContext());

			final List<PDFBean> lst = new ArrayList<PDFBean>();
			user = sm.getUserDetails();
			final String userid = user.get(SessionManager.KEY_USER_ID);
			Thread t = new Thread() {
				String method = "refresh_data";

				public void run() {
					Looper.prepare(); // For Preparing Message Pool for the
										// child
										// mProgressDialog.show();
					HttpClient client = new DefaultHttpClient();
					HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
																							// Limit
					HttpResponse response;
					JSONObject json = new JSONObject();
					try {
						HttpPost post = new HttpPost(URL);
						json.put("method", "refresh_data");
						json.put("user_id", userid);
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
							json = new JSONObject(sb.toString());
							System.out.println("json:----- " + json.toString());
							if (json.length() > 0) {
								String str = new JSONObject(json.getString(method)).get("success").toString();
								// System.out.printlwrap_contentn("check.....>>>"+str);
								// System.out.println("method ------>"+new
								// JSONObject(json.getString(method)).get("pdf_data").toString());
								if (str.compareTo("true") == 0) {

									JSONArray jsonArr = new JSONObject(json.getString("refresh_data")).getJSONArray("pdf_data");

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
										lst.add(pdf);
									}
									// Inserting Contacts
									Log.d("Insert: ", "Inserting ..");
									for (PDFBean data : lst) {
										db.addPDF(data);
									}
									// Reading all contacts
									Log.d("Reading: ", "Reading all contacts..");
									List<PDFBean> pdfs = db.getAllPDF();

									for (PDFBean pd : pdfs) {
										String log = "Id: " + pd.getPDF_ID() + " ,Name: " + pd.getPDF_TITLE() + " ,Phone: " + pd.getPDF_SUB_TITLE();
										// Writing Contacts to log
										Log.d("Name: ", log);
									}

								} else {
									// mProgressDialog.dismiss();
									// createDialog(error);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						// mProgressDialog.dismiss();
						// createDialog(error);
					}
					Looper.loop(); // Loop in the message queue

				}

			};

			t.start();
			// mProgressDialog.dismiss();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
		// if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
		// Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
		// } else if (newConfig.orientation ==
		// Configuration.ORIENTATION_PORTRAIT){
		// Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		// }
	}

	@Override
	public void setTitle(CharSequence title) {
		// getActionBar().setTitle("");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		// getActionBar().setTitle("");
		return super.onCreateOptionsMenu(menu);
	}

	public void hideBottomBar() {
		findViewById(R.id.bottom_bar).setVisibility(FrameLayout.GONE);
	}

	public void showbottomBar() {
		findViewById(R.id.bottom_bar).setVisibility(FrameLayout.VISIBLE);
	}

	public void downloadPDF(View view) {
		cd = new ConnectionDetector(DisplayDataActivity.this);

		System.out.println(cd.isConnectedToInternet());
		DatabaseHandler db = new DatabaseHandler(DisplayDataActivity.this);
		currentPdfId = Integer.parseInt(view.getTag().toString().trim());
		PDFBean pdf = db.getPDF(currentPdfId);

		tag = view.getTag() + "pb";
		if (pdf.getFILE_TYPE().contains("pdf")) {
			isEbook = false;
			String filename = pdf.getPDF_FILE().toString().substring(pdf.getPDF_FILE().toString().lastIndexOf('/') + 1, pdf.getPDF_FILE().toString().length());
			File file = new File(LoginActivity.path + filename);

			if (file.exists()) {
				// commented by SMIT
				fr = new MuPDFFragment();
				editName("pdf" + "-" + pdf.getPDF_TITLE());
				FragmentTransaction ff2 = getSupportFragmentManager().beginTransaction();
				Bundle bde = new Bundle();
				bde.putSerializable(EXTRA_URL, pdf);
				fr.setArguments(bde);
				hideBottomBar();
				ff2.replace(R.id.content, fr);
				ff2.addToBackStack(null);
				ff2.commit();

				// Uri uri = Uri.parse(LoginActivity.path + filename);
				//
				// Intent intent = new Intent(view.getContext(),
				// MuPDFActivity.class);
				//
				// intent.setAction(Intent.ACTION_VIEW);
				//
				// intent.setData(uri);
				//
				// intent.putExtra(LoginActivity.Extra_Value, pdf);
				//
				// view.getContext().startActivity(intent);

			} else {
				if (!cd.isConnectedToInternet()) {
					cd.showAlertDialog(cd.isConnectedToInternet());
				} else {
					Functions.showLoadingWithoutDialog(view.getContext(), "");
					initAnimation(view);
					enable(false);
					DownloadTask download1 = new DownloadTask(view.getContext());
					download1.execute(pdf.getPDF_FILE());

				}
			}
		} else if (pdf.getFILE_TYPE().contains("video")) {
			isEbook = false;
			final RelativeLayout videoView = (RelativeLayout) findViewById(R.id.video_view);

			final VideoView video = (VideoView) findViewById(R.id.medarkive_video);
			final Button close = (Button) findViewById(R.id.close);

			String filename = pdf.getPDF_FILE().toString().substring(pdf.getPDF_FILE().toString().lastIndexOf('/') + 1, pdf.getPDF_FILE().toString().length());
			// filename= filename.replaceAll("\\s+","%20");
			File file = new File(LoginActivity.path + filename);
			if (file.exists()) {
				Uri uri = Uri.parse(LoginActivity.path + filename);
				MediaController videoMediaController = new MediaController(DisplayDataActivity.this);
				video.setVideoURI(uri);
				videoView.setVisibility(RelativeLayout.VISIBLE);
				close.setVisibility(Button.VISIBLE);
				video.setVisibility(VideoView.VISIBLE);
				videoMediaController.setMediaPlayer(video);
				video.setMediaController(videoMediaController);
				video.requestFocus();
				video.start();
				close.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						video.setVisibility(VideoView.GONE);
						close.setVisibility(Button.GONE);
						videoView.setVisibility(RelativeLayout.GONE);
					}
				});
			} else {
				if (!cd.isConnectedToInternet()) {
					cd.showAlertDialog(cd.isConnectedToInternet());
				} else {
					Functions.showLoadingWithoutDialog(view.getContext(), "");
					initAnimation(view);
					enable(false);
					DownloadTask download = new DownloadTask(DisplayDataActivity.this);
					download.execute(pdf.getPDF_FILE());
				}
			}
		} else if (pdf.getFILE_TYPE().contains("ebook")) {
			isEbook = true;
			File file = new File(DisplayDataActivity.this.getDir(pdf.getPDF_ID(), Context.MODE_PRIVATE).getAbsolutePath());
			ArrayList<EbookBean> ebookList;
			ebookList = db.getEbookData(pdf.getPDF_ID());
			if (file.listFiles().length > 0) {
				if (fr != null) {
					getSupportFragmentManager().beginTransaction().remove(fr).commit();
				}
				FragmentTransaction tf = getSupportFragmentManager().beginTransaction();
				fr = new EBOOKFragment();
				Bundle bdl = new Bundle();
				// bookNo = 0;

				// EbookBean beanObj = ebookList.get(0);

				// Log.v("PDF File name :", beanObj.getFile_name());
				// Log.v("PDF PDF name :", beanObj.getPdf_name());

				// PDFBean pdf= new PDFBean();
				// pdf.setPDF_ID(bean.getPdf_id());
				// pdf.setPDF_TITLE(bean.getPdf_name());
				// pdf.setFILE_TYPE("ebook");
				// pdf.setPDF_LOGO(bean.getId());

				// PDFBean pdfbean = new PDFBean();
				// pdfbean.setPDF_FILE(beanObj.getFile_name());
				// pdfbean.setPDF_ID(beanObj.getPdf_id());
				// pdfbean.setPDF_TITLE(beanObj.getPdf_name());
				// pdfbean.setPDF_LOGO(beanObj.getId());
				// pdfbean.setFILE_TYPE("ebook");

				bdl.putParcelableArrayList(LoginActivity.Extra_Value, ebookList);

				fr.setArguments(bdl);
				tf.replace(R.id.content, fr);
				tf.addToBackStack(null);
				tf.commit();
				hideBottomBar();
				view = ebookTopBar;
				changeTopBar(fr, "Ebook", view);
			} else {
				if (!cd.isConnectedToInternet()) {
					cd.showAlertDialog(cd.isConnectedToInternet());
				} else {
					Functions.showLoadingWithoutDialog(view.getContext(), "");
					EbookDownlaoderAsyncTask downloader = new EbookDownlaoderAsyncTask();

					String[] urls = new String[ebookList.size()];
					int i = 0;
					for (EbookBean bean : ebookList) {
						urls[i] = bean.getFile_name();
						i++;
					}
					enable(false);
					initAnimation(view);
					downloader.startDownload(urls, DisplayDataActivity.this, pdf.getPDF_ID());
				}
			}
		}
	}

	public void ebookLoadPDF(PDFBean pdf) {
		if (fr != null) {
			getSupportFragmentManager().beginTransaction().remove(fr).commit();
		}
		fr = new MuPDFFragment();
		FragmentTransaction ff2 = getSupportFragmentManager().beginTransaction();
		Bundle bde = new Bundle();
		bde.putSerializable(DisplayDataActivity.EXTRA_URL, pdf);
		fr.setArguments(bde);
		hideBottomBar();
		ff2.replace(R.id.content, fr);
		ff2.addToBackStack(null);
		ff2.commit();
	}

	public void finishProgress() {
		AlertDialog alertDialog = new AlertDialog.Builder(DisplayDataActivity.this).create();
		alertDialog.setTitle("MedArkive Service");
		alertDialog.setMessage("The content is now available any time you need it");
		alertDialog.setIcon(R.drawable.ic_check);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// here you can add functions
			}
		});
		alertDialog.show();
		// mProgressDialog.dismiss();

		pbar.clearAnimation();
		pbar.destroyDrawingCache();
		pbar.setVisibility(ProgressBar.INVISIBLE);
	}

	// public void rigtGesture(Context ctx) {
	// Toast.makeText(DisplayDataActivity.this, "Right ", 200).show();
	// if(bookNo <= ebookList.size() && ebookList.size() != 0){
	// if (fr != null) {
	// getSupportFragmentManager().beginTransaction().remove(fr).commit();
	// }
	// FragmentTransaction tf = getSupportFragmentManager().beginTransaction();
	// fr = new MuPDFFragment();
	// Bundle bdl = new Bundle();
	// bookNo--;
	// EbookBean beanObj = ebookList.get(bookNo);
	// PDFBean pdfbean = new PDFBean();
	// pdfbean.setPDF_FILE(beanObj.getFile_name());
	// pdfbean.setPDF_ID(beanObj.getPdf_id());
	// pdfbean.setPDF_TITLE(beanObj.getChapter_title());
	// pdfbean.setFILE_TYPE("ebook");
	//
	// bdl.putSerializable(EXTRA_URL, pdfbean);
	//
	// fr.setArguments(bdl);
	// tf.replace(R.id.content, fr);
	// tf.addToBackStack(null);
	// tf.commit();
	// hideBottomBar();
	// tf.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
	// }
	// }

	// public void leftGesture(Context ctx) {
	// if(bookNo < ebookList.size() && ebookList.size() != 0){
	// if (fr != null) {
	// getSupportFragmentManager().beginTransaction().remove(fr).commit();
	// }
	// FragmentTransaction tf = getSupportFragmentManager().beginTransaction();
	// fr = new MuPDFFragment();
	// Bundle bdl = new Bundle();
	// bookNo++;
	// EbookBean beanObj = ebookList.get(bookNo);
	// PDFBean pdfbean = new PDFBean();
	// pdfbean.setPDF_FILE(beanObj.getFile_name());
	// pdfbean.setPDF_ID(beanObj.getPdf_id());
	// pdfbean.setPDF_TITLE(beanObj.getChapter_title());
	// pdfbean.setFILE_TYPE("ebook");
	//
	// bdl.putSerializable(EXTRA_URL, pdfbean);
	//
	// fr.setArguments(bdl);
	// tf.replace(R.id.content, fr);
	// tf.addToBackStack(null);
	// tf.commit();
	// hideBottomBar();
	// tf.setCustomAnimations(R.anim.slide_out_right ,R.anim.slide_in_left);
	// }
	// Toast.makeText(DisplayDataActivity.this, "Left ", 200).show();
	// }

	private void initAnimation(View view) {
		// R.drawable.tile1 is PNG
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.progressbar);
		AnimationDrawable shiftedAnimation = getAnimation(b);

		// R.id.progress is ImageView in my application
		pbar = (ProgressBar) view.getRootView().findViewWithTag(tag);
		pbar.setVisibility(ProgressBar.VISIBLE);
		pbar.setBackground(shiftedAnimation);
		shiftedAnimation.start();
	}

	private Bitmap getShiftedBitmap(Bitmap bitmap, int shiftX) {
		Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
		Canvas newBitmapCanvas = new Canvas(newBitmap);

		Rect srcRect1 = new Rect(shiftX, 0, bitmap.getWidth(), bitmap.getHeight());
		Rect destRect1 = new Rect(srcRect1);
		destRect1.offset(-shiftX, 0);
		newBitmapCanvas.drawBitmap(bitmap, srcRect1, destRect1, null);
		Rect srcRect2 = new Rect(0, 0, shiftX, bitmap.getHeight());
		Rect destRect2 = new Rect(srcRect2);
		destRect2.offset(bitmap.getWidth() - shiftX, 0);
		newBitmapCanvas.drawBitmap(bitmap, srcRect2, destRect2, null);
		return newBitmap;
	}

	private List<Bitmap> getShiftedBitmaps(Bitmap bitmap) {
		List<Bitmap> shiftedBitmaps = new ArrayList<Bitmap>();
		int fragments = 10;
		int shiftLength = bitmap.getWidth() / fragments;
		for (int i = 0; i < fragments; ++i) {
			shiftedBitmaps.add(getShiftedBitmap(bitmap, shiftLength * i));
		}
		return shiftedBitmaps;
	}

	private AnimationDrawable getAnimation(Bitmap bitmap) {
		AnimationDrawable animation = new AnimationDrawable();
		animation.setOneShot(false);
		List<Bitmap> shiftedBitmaps = getShiftedBitmaps(bitmap);
		int duration = 50;
		for (Bitmap image : shiftedBitmaps) {
			BitmapDrawable navigationBackground = new BitmapDrawable(getResources(), image);
			navigationBackground.setTileModeX(TileMode.REPEAT);
			animation.addFrame(navigationBackground, duration);
		}
		return animation;
	}

	public void hideKeyboard() {
		// showbottomBar();
		InputMethodManager inputMethodManager = (InputMethodManager) DisplayDataActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(DisplayDataActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	// public void customActionBarEditProfile(String title) {
	// mActionBar = getActionBar();
	// mActionBar.setDisplayShowHomeEnabled(false);
	// mActionBar.setDisplayShowTitleEnabled(false);
	// LayoutInflater mInflater = LayoutInflater.from(this);
	//
	// View mCustomView = mInflater.inflate(
	// R.layout.custom_actionbar_editprofile, null);
	// ((TextView) mCustomView.findViewById(R.id.title)).setText(title);
	// mActionBar.setCustomView(mCustomView);
	// mActionBar.setDisplayShowCustomEnabled(true);
	// mCustomView.findViewById(R.id.nav_drawer_icon).setOnClickListener(
	// new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// InputMethodManager inputMethodManager = (InputMethodManager)
	// DisplayDataActivity.this
	// .getSystemService(Activity.INPUT_METHOD_SERVICE);
	// inputMethodManager.hideSoftInputFromWindow(
	// DisplayDataActivity.this.getCurrentFocus()
	// .getWindowToken(),
	// InputMethodManager.HIDE_NOT_ALWAYS);
	// // TODO Auto-generated method stub
	// boolean check = mDrawerLayout
	// .isDrawerOpen(mRelativeDrawer);
	// if (check) {
	// mDrawerLayout.closeDrawer(mRelativeDrawer);
	// } else {
	// mDrawerLayout.openDrawer(mRelativeDrawer);
	// }
	// }
	//
	// });
	// }

	public void edit_profile() {
		view.setVisibility(8);

		FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
		fr = new EditProfileFragment();
		// }
		ftt.replace(R.id.content, fr);
		ftt.commit();
		hideBottomBar();
		changeTopBar(fr, "Edit Profile", homeTBar);
		mDrawerLayout.closeDrawer(mRelativeDrawer);
	}

	@SuppressLint("InflateParams")
	public void editName(String title) {
		FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
		if (title.contains("Edit Name")) {
			fr = new EditNameFragment();
			ftt.replace(R.id.content, fr);
			ftt.addToBackStack(null);
			ftt.commit();
		} else if (title.contains("Change Password")) {
			fr = new ChangePasswordFragment();
			ftt.replace(R.id.content, fr);
			ftt.addToBackStack(null);
			ftt.commit();
		} else if (title.contains("Select CPD")) {
			fr = new CPDOptionFragment();
			ftt.replace(R.id.content, fr);
			ftt.addToBackStack(null);
			ftt.commit();
		} else if (title.contains("Ebook")) {
			// title = title.substring(title.indexOf("-")+1);
		} else if (title.contains("-")) {
			if (title.substring(0, title.indexOf("-")).equals("pdf")) {
				title = title.substring(title.indexOf("-") + 1);
			}
		}
		setCustomActionBarBackButton(title);
		hideBottomBar();
	}

	public void setCustomActionBarBackButton(final String title) {
		InputMethodManager inputMethodManager = (InputMethodManager) DisplayDataActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(DisplayDataActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		drawerLy.setVisibility(0);
		webVewTBar.setVisibility(8);
		logo.setVisibility(8);
		refreshLay.setVisibility(8);
		statusTxt.setVisibility(0);
		statusTxt.setText(title);
		backBtn.setVisibility(0);

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getSupportFragmentManager().popBackStack();
				// changeTopBar(fr, str, drawerLy);
				Fragment frna = getSupportFragmentManager().findFragmentById(R.id.content);
				Log.v("Fragment in frgm mgr ", frna.toString() + ",");
				// Log.v("Fragment name in fr", fr.toString() + ",");

				if (frna.toString().startsWith("EventDescriptionFragment") || frna.toString().startsWith("EvetTypeListFragment")) {
					drawerLy.setVisibility(0);
					sm = new SessionManager(DisplayDataActivity.this);
					homeTBar.setVisibility(0);
					logo.setVisibility(8);
					refreshLay.setVisibility(8);
					backBtn.setVisibility(0);
					String str = sm.getCPDChoice();
					if (str.equals("1")) {
						statusTxt.setText("CPD Log (UK)");
					} else if (str.equals("2")) {
						statusTxt.setText("CPD Log (International)");
					}
				} else if (frna.toString().startsWith("EventDetailsListFragment")) {
					drawerLy.setVisibility(0);
					sm = new SessionManager(DisplayDataActivity.this);
					homeTBar.setVisibility(0);
					logo.setVisibility(8);
					refreshLay.setVisibility(8);
					backBtn.setVisibility(8);
					statusTxt.setVisibility(0);
					String str = sm.getCPDChoice();
					if (str.equals("1")) {
						statusTxt.setText("CPD Log (UK)");
					} else if (str.equals("2")) {
						statusTxt.setText("CPD Log (International)");
					}
				} else if (frna.toString().startsWith("CPDOptionFragment") || frna.toString().startsWith("EditNameFragment") || frna.toString().startsWith("ChangePasswordFragment")) {
					drawerLy.setVisibility(0);
					// sm = new SessionManager(DisplayDataActivity.this);
					homeTBar.setVisibility(0);
					logo.setVisibility(8);
					refreshLay.setVisibility(8);
					backBtn.setVisibility(8);
					statusTxt.setVisibility(0);
					statusTxt.setText("Edit Profile");
				} else if (frna.toString().startsWith("TrackerFragment")) {
					drawerLy.setVisibility(0);
					sm = new SessionManager(DisplayDataActivity.this);
					homeTBar.setVisibility(0);
					logo.setVisibility(8);
					refreshLay.setVisibility(8);
					backBtn.setVisibility(8);
					statusTxt.setVisibility(0);
					String str = sm.getCPDChoice();
					if (str.equals("1")) {
						statusTxt.setText("CPD Log (UK)");
					} else if (str.equals("2")) {
						statusTxt.setText("CPD Log (International)");
					}
				}
				if (frna.toString().startsWith("BookmarkListFragment")) {
					drawerLy.setVisibility(0);
					homeTBar.setVisibility(8);
					logo.setVisibility(8);
					refreshLay.setVisibility(8);
					webVewTBar.setVisibility(0);
					backBtn.setVisibility(8);
				} else {
					homeTBar.setVisibility(0);
					logo.setVisibility(0);
					refreshLay.setVisibility(0);
					backBtn.setVisibility(8);
					statusTxt.setVisibility(8);
				}

				//
				// if (str.equals("Event Detail List")) {
				// getSupportFragmentManager().beginTransaction()
				// .replace(R.id.content, new CPDLogListFragment())
				// .commit();
				// view.setVisibility(8);
				// changeTopBar(fr ,"CPD", homeTBar);
				// } else if (str.equals("Ebook")) {
				// getSupportFragmentManager().popBackStack();
				// view.setVisibility(8);
				// changeTopBar(fr,"Home", homeTBar);
				// } else if (str.equals("Edit Name")) {
				// getSupportFragmentManager().popBackStack();
				// view.setVisibility(8);
				// changeTopBar(fr,"Edit Profile", homeTBar);
				// } else if (str.equals("Change Password")) {
				// getSupportFragmentManager().popBackStack();
				// view.setVisibility(8);
				// changeTopBar(fr,"Edit Profile", homeTBar);
				// } else if (str.equals("CPD Choice")) {
				// getSupportFragmentManager().popBackStack();
				// view.setVisibility(8);
				// changeTopBar(fr,"Edit Profile", homeTBar);
				// } else if (str.equals("Add Event")) {
				// SessionManager sm = new SessionManager(
				// DisplayDataActivity.this);
				// String str = sm.getCPDChoice();
				// if (str.equals("1")) {
				// ((DisplayDataActivity) DisplayDataActivity.this)
				// .editName("CPD Log (UK)");
				// } else {
				// ((DisplayDataActivity) DisplayDataActivity.this)
				// .editName("CPD Log (International)");
				// }
				// getSupportFragmentManager().popBackStack();
				// // view.setVisibility(8);
				// // changeTopBar("Event Detail List", homeTBar);
				// } else if (str.equals("Event Description")) {
				// getSupportFragmentManager().popBackStack();
				// view.setVisibility(8);
				// changeTopBar(fr,"Event Detail List", homeTBar);
				// } else if (str.equals("Bookmarks")) {
				// getSupportFragmentManager().popBackStack();
				// view.setVisibility(8);
				// changeTopBar(fr,"WWW", webVewTBar);
				// } else if (str.equals("Reading Tracker")) {
				// getSupportFragmentManager().beginTransaction()
				// .replace(R.id.content, new CPDLogListFragment())
				// .commit();
				// view.setVisibility(8);
				// changeTopBar(fr,"CPD", homeTBar);
				// } else if (str.equals("CPD Log (UK)")
				// || str.equals("CPD Log (International)")) {
				// getSupportFragmentManager().popBackStack();
				// // view.setVisibility(8);
				// // // changeTopBar("CPD", homeTBar);
				// }
			}
		});
	}

	public void trackerFragmentCall() {
		fr = new TrackerFragment();
		editName("Reading Tracker");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.content, fr);
		ft.addToBackStack(null);
		ft.commit();
	}

	public void eventDecriptionFragmentCall(Bundle bdl) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		fr = new EventDescriptionFragment();
		// Bundle bdl = new Bundle();
		// bdl.putParcelable(EVENT_DESCRIPTION, obj);
		fr.setArguments(bdl);
		ft.replace(R.id.content, fr);
		ft.addToBackStack(null);
		ft.commit();
	}

	public void eventDetailListFragmentCall(ArrayList<CPDBean> arrList) {
		SessionManager sm = new SessionManager(DisplayDataActivity.this);
		final String cpdChoice = sm.getCPDChoice();
		if (cpdChoice.contains("2")) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			fr = new EventDescriptionFragment();
			Bundle bdl = new Bundle();
			// System.out
			// .println("array list size     iiuiiiui----"
			// + arrList.size());
			bdl.putParcelableArrayList(LoginActivity.Extra_Value, arrList);
			fr.setArguments(bdl);
			ft.replace(R.id.content, fr);
			ft.addToBackStack(null);
			ft.commit();
		} else if (cpdChoice.contains("1")) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			if (arrList.size() > 0) {
				if (arrList.get(0).getName().equals("Other Activity")) {
					fr = new EventDescriptionFragment();
					Bundle bdl = new Bundle();
					// System.out
					// .println("array list size     iiuiiiui----"
					// + arrList.size());
					bdl.putParcelableArrayList(LoginActivity.Extra_Value, arrList);
					fr.setArguments(bdl);
					ft.replace(R.id.content, fr);
					ft.addToBackStack(null);
					ft.commit();
				} else {

					EvetTypeListFragment evetType = new EvetTypeListFragment();
					Bundle bdl = new Bundle();
					bdl.putParcelableArrayList(LoginActivity.Extra_Value, arrList);
					evetType.setArguments(bdl);
					ft.replace(R.id.content, evetType);
					ft.addToBackStack(null);
					ft.commit();
				}
			}
		}

		// ((DisplayDataActivity)getActivity()).view.setVisibility(8);
		// ((DisplayDataActivity)getActivity()).editName("Event Detail");
		// ActionBar mActionBar = getActivity().getActionBar();
		// LayoutInflater mInflater = LayoutInflater
		// .from(getActivity());
		//
		// View mCustomView = mInflater.inflate(
		// R.layout.custom_actionbar_back_button, null);
		// mActionBar.setCustomView(mCustomView);
		// mActionBar.setDisplayShowCustomEnabled(true);

	}

	public void evetTypeFragmentCall(Bundle bdl) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		fr = new EventDescriptionFragment();
		// Bundle bdl = new Bundle();
		// bdl.putParcelable(TYPE_ID, item);
		// bdl.putParcelableArrayList(ARRAY_LIST_DATA, aaa);
		fr.setArguments(bdl);
		ft.replace(R.id.content, fr);
		ft.addToBackStack(null);
		ft.commit();
	}

	@SuppressLint("InflateParams")
	public void changePassword() {
		hideBottomBar();

		FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
		fr = new ChangePasswordFragment();
		// }
		ftt.replace(R.id.content, fr);
		ftt.addToBackStack(null);
		ftt.commit();
		setCustomActionBarBackButton("Change Password");
	}

	@SuppressLint("InflateParams")
	public void cpdChoice() {
		hideBottomBar();

		FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
		fr = new CPDOptionFragment();
		// }
		ftt.replace(R.id.content, fr);
		ftt.addToBackStack(null);
		ftt.commit();
		// mActionBar = getActionBar();
		// mActionBar.setDisplayShowHomeEnabled(false);
		// mActionBar.setDisplayShowTitleEnabled(false);
		// LayoutInflater mInflater = LayoutInflater.from(this);
		//
		// View mCustomView = mInflater.inflate(
		// R.layout.custom_actionbar_back_button, null);
		// ((TextView)
		// mCustomView.findViewById(R.id.title)).setText("CPD Choice");
		// mActionBar.setCustomView(mCustomView);
		// mActionBar.setDisplayShowCustomEnabled(true);

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				view.setVisibility(8);
				changeTopBar(fr, "Edit Profile", homeTBar);
				// customActionBarEditProfile("Edit Profile");
				getSupportFragmentManager().popBackStack();
			}
		});
	}

	class DownloadTask extends AsyncTask<String, Integer, String> {
		private String name = "";
		private Context context;
		private PowerManager.WakeLock mWakeLock;

		public DownloadTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
		}

		@Override
		protected void onPostExecute(String result) {
			Functions.dismissLoadingDialog();
			AlertDialog alertDialog = new AlertDialog.Builder(DisplayDataActivity.this).create();
			alertDialog.setTitle("MedArkive Service");
			alertDialog.setMessage("The Content has saved on your device for futher working");
			alertDialog.setIcon(R.drawable.ic_check);
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// here you can add functions
				}
			});
			alertDialog.show();
			// mProgressDialog.dismiss();

			pbar.clearAnimation();
			pbar.destroyDrawingCache();
			pbar.setVisibility(ProgressBar.INVISIBLE);
			enable(true);
		}

		@Override
		protected String doInBackground(String... sUrl) {
			// initAnimation();
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			try {
				name = sUrl[0].toString().substring(sUrl[0].toString().lastIndexOf('/') + 1, sUrl[0].toString().length());

				sUrl[0] = sUrl[0].replaceAll("\\s+", "%20");
				URL url = new URL(sUrl[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();
				System.out.println(" url-------->>>>" + connection.getURL());
				System.out.println("error conn-------->>>>" + connection.getErrorStream());
				// System.out.println("-----------------test1------------------------");
				// expect HTTP 200 OK, so we don't mistakenly save error report
				// instead of the file
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
				}
				// this will be useful to display download percentage
				// might be -1: server did not report the length
				int fileLength = connection.getContentLength();
				System.out.println("filelength======" + fileLength);
				// download the file
				input = connection.getInputStream();
				System.out.println("file path -------->>>>>" + LoginActivity.path + name);
				output = new FileOutputStream(LoginActivity.path + name);
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
					// Uri uri = Uri.parse(LoginActivity.path + name);
					// Intent intent = new Intent(getActivity(),
					// MuPDFActivity.class);
					//
					// intent.setAction(Intent.ACTION_VIEW);
					//
					// intent.setData(uri);
					//
					// getActivity().startActivity(intent);
				}
			} catch (Exception e) {
				Functions.dismissLoadingDialog();
				e.printStackTrace();
				return e.toString();
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
				}

				if (connection != null)
					connection.disconnect();
			}
			return null;
		}
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		return false;
	}

	public void showBookmarkList() {
		GetBookmarkes gb = new GetBookmarkes();
		gb.execute("");
	}

	public class GetBookmarkes extends AsyncTask<String, Void, JSONObject> {
		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub

			String result = "fail";
			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
			HttpResponse response;
			JSONObject json = null;
			try {
				SessionManager sm = new SessionManager(DisplayDataActivity.this);
				HashMap<String, String> user = sm.getUserDetails();
				String uid = user.get(SessionManager.KEY_USER_ID);
				final JSONObject obj = new JSONObject();
				obj.put("method", "show_bookmark");
				obj.put("user_id", uid);
				// TODO Auto-generated catch block
				HttpPost post = new HttpPost(LoginActivity.WEBSERVICEURL);
				StringEntity se = new StringEntity(obj.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
				post.setEntity(se);
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
					json = new JSONObject(sb.toString());
					if (json.length() > 0) {
						System.out.println("response ----" + json);
						// setJsonObj(json);
						result = "pass";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			Functions.dismissLoadingDialog();
			JsonManipulator jm = new JsonManipulator();
			ArrayList<BookmarkBean> arr = jm.bookmarkJsonToMap(result);

			FragmentTransaction traFragment = DisplayDataActivity.this.getSupportFragmentManager().beginTransaction();
			Bundle bdl = new Bundle();

			fr = new BookmarkListFragment();
			bdl.putParcelableArrayList(LoginActivity.Extra_Value, arr);
			fr.setArguments(bdl);
			traFragment.replace(R.id.content, fr);
			traFragment.addToBackStack(null);
			traFragment.commit();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			Functions.showLoadingDialog(DisplayDataActivity.this, "Loading...");
			super.onPreExecute();
		}

	}

	public class RefreshData extends AsyncTask<String, Void, JSONObject> {
		final List<PDFBean> lst = new ArrayList<PDFBean>();

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub

			// String result = "fail";
			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
			HttpResponse response;
			JSONObject json = null;
			try {
				SessionManager sm = new SessionManager(DisplayDataActivity.this);
				HashMap<String, String> user = sm.getUserDetails();
				String uid = user.get(SessionManager.KEY_USER_ID);
				final JSONObject obj = new JSONObject();
				obj.put("method", "refresh_data");
				obj.put("user_id", uid);
				// TODO Auto-generated catch block
				HttpPost post = new HttpPost(LoginActivity.WEBSERVICEURL);
				StringEntity se = new StringEntity(obj.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
				post.setEntity(se);
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
					json = new JSONObject(sb.toString());
					if (json.length() > 0) {
						System.out.println("response ----" + json);
						// setJsonObj(json);
						// result = "pass";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			// TODO Auto-generated method stub
			Functions.dismissLoadingDialog();

			String str;
			try {
				str = new JSONObject(json.getString("refresh_data")).get("success").toString();

				// System.out.printlwrap_contentn("check.....>>>"+str);
				// System.out.println("method ------>"+new
				// JSONObject(json.getString(method)).get("pdf_data").toString());
				if (str.compareTo("true") == 0) {

					JSONArray jsonArr = new JSONObject(json.getString("refresh_data")).getJSONArray("pdf_data");

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
						lst.add(pdf);
					}
					Log.v("ArrayList size ->", "lst" + lst.size());
					// Inserting Contacts
					DatabaseHandler db = new DatabaseHandler(DisplayDataActivity.this);
					Log.d("Insert: ", "Inserting ..");
					for (PDFBean data : lst) {
						db.addPDF(data);
					}
					// Reading all contacts
					Log.d("Reading: ", "Reading all contacts..");
					List<PDFBean> pdfs = db.getAllPDF();

					for (PDFBean pd : pdfs) {
						String log = "Id: " + pd.getPDF_ID() + " ,Name: " + pd.getPDF_TITLE() + " ,Phone: " + pd.getPDF_SUB_TITLE();
						// Writing Contacts to log
						Log.d("Name: ", log);
					}

					FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
					fr = new PDFTabsFragment();
					ftt.replace(R.id.content, fr);
					ftt.addToBackStack(null);
					ftt.commit();

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Functions.dismissLoadingDialog();
				e.printStackTrace();
			}

			// } else {
			// // mProgressDialog.dismiss();
			// // createDialog(error);
			// }

			super.onPostExecute(json);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			Functions.showLoadingDialog(DisplayDataActivity.this, "Loading...");
			super.onPreExecute();
		}

	}

	@SuppressWarnings("deprecation")
	public void shareOnFB(String url) {
		// commented by SMIT
		//restoreCredentials(facebook);

		String facebookMessage = getIntent().getStringExtra("facebookMessage");
		if (facebookMessage == null) {
			facebookMessage = url;
		}
		messageToPost = facebookMessage;

		// commented by SMIT
		/*if (FacebookDialog.canPresentShareDialog(DisplayDataActivity.this, FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			uiHelper = new UiLifecycleHelper(DisplayDataActivity.this, null);
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(DisplayDataActivity.this).setLink(
					"http://programmerguru.com/android-tutorial/getting-started-with-the-facebook-sdk-for-android/").build();
			uiHelper.trackPendingDialogCall(shareDialog.present());
		} else {
			AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(DisplayDataActivity.this);
			alertDialog2.setTitle("Are you sure you want share this Link?");
			alertDialog2.setMessage(url);
			alertDialog2.setPositiveButton("Post", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (!facebook.isSessionValid()) {
						loginAndPostToWall();
					} else {
						postToWall(messageToPost);
					}
				}
			});
			alertDialog2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			alertDialog2.show();

			// Toast.makeText(act, "FB not installed ", 500).show();
		}*/
	}
	// commented by SMIT
/*	@SuppressWarnings("deprecation")
	public boolean saveCredentials(Facebook facebook) {
		Editor editor = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
		editor.putString(TOKEN, facebook.getAccessToken());
		editor.putLong(EXPIRES, facebook.getAccessExpires());
		return editor.commit();
	}

	@SuppressWarnings("deprecation")
	public boolean restoreCredentials(Facebook facebook) {
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
		facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
		facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
		return facebook.isSessionValid();
	}

	@SuppressWarnings("deprecation")
	public void loginAndPostToWall() {
		try {
			facebook.authorize(this, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	// commented by SMIT
/*	@SuppressWarnings("deprecation")
	public void postToWall(final String message) {
		final Thread t = new Thread(new Runnable() {
			public void run() {
				Bundle parameters = new Bundle();
				parameters.putString("Title", "Medarkive want to share with you.");
				parameters.putString("link", message);
				parameters.putString("description", "");
				try {
					facebook.request("me");
					String response = facebook.request("me/feed", parameters, "POST");
					Log.d("Tests", "got response: " + response);
					if (response == null || response.equals("") || response.equals("false")) {
						Log.v("fb respose", "Blank response.");
					} else {
						Log.v("fb respose", "Message posted to your facebook wall!");
					}
					// finish();
				} catch (Exception e) {
					showToast("Failed to post to wall!");
					e.printStackTrace();
					// finish();
				}
			}
		});
		t.start();
	}*/
// commented by SMIT
/*	class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			saveCredentials(facebook);
			if (messageToPost != null) {
				postToWall(messageToPost);
			}
		}

		public void onFacebookError(FacebookError error) {
			showToast("Authentication with Facebook failed!");
			// finish();
		}

		public void onError(DialogError error) {
			showToast("Authentication with Facebook failed!");
			// finish();
		}

		public void onCancel() {
			showToast("Authentication with Facebook cancelled!");
			// finish();
		}
	}*/

	private void showToast(String message) {
		// Toast.makeText(getApplicationContext(), message,
		// Toast.LENGTH_SHORT).show();
	}

	public void pdfUrlClicked(String pdfURL) {
		String codeMatcher = "";
		DatabaseHandler db = new DatabaseHandler(DisplayDataActivity.this);
		ArrayList<PDFBean> videoURL = db.getAllVideoURL();
		String pdfCode = pdfURL.substring(pdfURL.lastIndexOf("/") + 1, pdfURL.length());
		// Log.v("Code : =", "," + pdfCode);
		for (PDFBean pdf : videoURL) {
			if (pdf.getFILE_CODE().equalsIgnoreCase(pdfCode)) {
				// Log.v("Code Matched -->", "," + pdfCode);
				codeMatcher = pdf.getPDF_FILE();
			}
		}
		if (codeMatcher.length() > 0) {
			// Log.v("Path for video ->", codeMatcher);
			final Dialog dialog = new Dialog(DisplayDataActivity.this, R.style.AllDialog);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.introvid);
			dialog.setCanceledOnTouchOutside(false);
			RelativeLayout rv = (RelativeLayout) dialog.findViewById(R.id.rv);
			new ASSL(DisplayDataActivity.this, rv, 1134, 720, false);

			dialog.show();
			Functions.showLoadingDialog(DisplayDataActivity.this, "Please wait...");
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams((int) ASSL.Xscale() * 710, (int) ASSL.Xscale() * 1100);
			lp.copyFrom(dialog.getWindow().getAttributes());
			dialog.getWindow().setAttributes(lp);
			final VideoView videoview = (VideoView) dialog.findViewById(R.id.surface_view);

			dialog.findViewById(R.id.close).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			Uri uri;
			File file = new File(getFilesDir(), codeMatcher.substring(codeMatcher.lastIndexOf("/") + 1, codeMatcher.length()));
			if (file.exists()) {
				uri = Uri.parse(file.getAbsolutePath());
			} else {
				uri = Uri.parse(codeMatcher);
			}
			videoview.setVideoURI(uri);
			videoview.start();

			MediaController vidControl = new MediaController(this);
			vidControl.setAnchorView(videoview);
			videoview.setMediaController(vidControl);

			videoview.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					Functions.dismissLoadingDialog();
				}
			});

			videoview.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					Toast.makeText(getApplicationContext(), "Video completed", 500).show();
				}
			});

			codeMatcher = "";
		} else {
			FragmentTransaction ff2 = getSupportFragmentManager().beginTransaction();
			fr = new SimpleWebViewFragment();
			Bundle bde = new Bundle();
			bde.putString(SimpleWebViewFragment.EXTRA_URL, pdfURL);
			bde.putBoolean(SimpleWebViewFragment.dashboard_check, false);
			bde.putBoolean(SimpleWebViewFragment.MYARKIVE_CHECK, false);
			fr.setArguments(bde);
			changeTopBar(fr, "WWW", webVewTBar);
			hideBottomBar();
			ff2.replace(R.id.content, fr);
			ff2.addToBackStack(null);
			ff2.commit();
		}
	}

	public void backBtnEbook() {
		fr = new PDFTabsFragment();
		final FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
		ft1.replace(R.id.content, fr);
		ft1.addToBackStack(null);
		ft1.commit();
		view = homeTBar;
		changeTopBar(fr, "Home", view);
		homeLay.setBackgroundColor(Color.parseColor("#8589A3"));
		changeNavbarColor(homeLay);
	}

	public void shareOnTwitter(String url) {
		shareURL = url;
		new logout().execute();
	}

	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.i("TAG...2", "tweet111");

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DisplayDataActivity.this);

		final Uri uri = intent.getData();
		if (uri != null && uri.getScheme().equals(Constants.OAUTH_CALLBACK_SCHEME)) {
			Log.i("TAG...3", "Callback received : " + uri);
			Log.i("TAG...4", "Retrieving Access Token");
			new RetrieveAccessTokenTask(DisplayDataActivity.this, Constants.consumer, Constants.provider, prefs).execute(uri);
		} else {
			Log.i("some error", "some error");

		}
	}

	public void clickOnBookmarkUrl(String url) {
		FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
		fr = new SimpleWebViewFragment();
		Bundle bundle = new Bundle();
		bundle.putString(SimpleWebViewFragment.EXTRA_URL, url);
		fr.setArguments(bundle);
		ftt.replace(R.id.content, fr);
		ftt.addToBackStack(null);
		ftt.commit();
		changeTopBar(fr, "WWW", webVewTBar);

		hideBottomBar();
	}

	class logout extends AsyncTask<Void, Void, String> {
		// class BackgroundAsyncTask extends AsyncTask<Void, Boolean, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// dialog = ProgressDialog.show(ShareKikit.this, "", "Loading...",
			// true);

		}

		protected String doInBackground(Void... urls) {
			if (TwitterUtils.isAuthenticated(prefs)) {
				Log.i("authenticated", "authenticated");
				try {

					authentcated = "authincated";
					// ActivityContext.twitterpost=true;
					String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
					String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
					AccessToken a = new AccessToken(token, secret);
					Twitter twitter = new TwitterFactory().getInstance();
					twitter.setOAuthConsumer(com.medarkive.Utilities.Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
					twitter.setOAuthAccessToken(a);

					String temp = shareURL;
					Log.i("length", temp.length() + "");
					if (temp.length() >= 120) {
						temp = temp.substring(0, 119);
					}

					twitter.updateStatus(temp);

					same_msg_chk_twt = false;

				} catch (Exception e) {
					// TODO: handle exception
					same_msg_chk_twt = true;
					Log.i("error......same message", e.toString());
				}

			} else {
				authentcated = "not";
				var = "login";
				client = new DefaultHttpClient();
				Log.i("inside Read 22 ", "inside Read 22");
				new Read().execute(" ");
			}

			return null;
		}

		protected void onPostExecute(String text) {
			if (authentcated.equals("authincated")) {
				Log.i("inside else...", "inside twitter authenticated");
				// post();
				if (same_msg_chk_twt) {
					Toast.makeText(getApplicationContext(), "already twitted.", 2000).show();
				} else {
					Toast.makeText(getApplicationContext(), "Success.", 2000).show();
				}
			}
		}
	}

	// **************************** Here we call for twitter
	// *********************************//

	public class Read extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			try {
				if (var.equals("login")) {
					Log.i("called", "login called");
					login_tweet();
				} else {

				}
			} catch (Exception e) {
				Log.v("Error", e.toString());
			}
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			if (var.equals("not")) {
				var = "";

				new RetrieveData().execute("data");

			} else if (var.equals("login")) {
				var = "";
			}
		}
	}

	public void login_tweet() {
		try {
			Log.i("TAG", "3");
			Constants.consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
			Constants.provider = new CommonsHttpOAuthProvider(Constants.REQUEST_URL, Constants.ACCESS_URL, Constants.AUTHORIZE_URL);
		} catch (Exception e) {
			Log.e("TAG", "Error creating consumer / provider" + e);
		}
		Log.i("TAG", "Starting task to retrieve request token.");
		// new OAuthRequestTokenTask(ImagePreview.this,
		// Constants.consumer,Constants.provider).execute();
		new OAuthRequestTokenTask(DisplayDataActivity.this, Constants.consumer, Constants.provider, 1).execute();
	}

	class RetrieveData extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {
			Log.i("inside Retrieve data 33 ", "inside Retrieve data 33");
			try {
				String CONSUMER_KEY = Constants.CONSUMER_KEY;// "Doc0PxvgBj9NSI4wvgpnA";
				String CONSUMER_SECRET = Constants.CONSUMER_SECRET;// "jEDjVk2JOhSL7DJBTGihdp9wXkyEq5TmNm43TXVcCQ";
				String ACCESS_SECRET = secret;// "fmzPS4oxD8c2cTzT8zxX4lQZiXUSTSwKPc2n3r4";
				String ACCESS_TOKEN = tok;// "1102864022-1oQWImjL8Mhg4afaNuerd9DjG2SYBwC5pfXn6W5";
				Twitter twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
				AccessToken accessToken = null;
				accessToken = new AccessToken(ACCESS_TOKEN, ACCESS_SECRET);
				twitter.setOAuthAccessToken(accessToken);
				try {
					String temp = "first tweet now";
					if (temp.length() >= 120) {
						temp = temp.substring(0, 119);
					}

					Log.i("length", temp.length() + "");
					StatusUpdate status = new StatusUpdate(temp);
					twitter.updateStatus(status);
					same_msg_chk_twt = false;

				} catch (Exception e1) {
					e1.printStackTrace();
					same_msg_chk_twt = true;
					Log.i("same message error", e1.toString() + "");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String text) {
			Log.i("inside Retrieve onPostExecute 55 ", "inside Retrieve onPostExecute 55");
			Log.i("inside else...", "inside twitter not authenticated");
			if (same_msg_chk_twt) {
				Toast.makeText(getApplicationContext(), "already twitted.", 2000).show();
			} else {
				Toast.makeText(getApplicationContext(), "Success.", 2000).show();
			}
		}
	};

	public class RetrieveAccessTokenTask extends AsyncTask<Uri, Void, Void> {

		private Context context;
		private OAuthProvider provider;
		private OAuthConsumer consumer;
		private SharedPreferences prefs;

		public RetrieveAccessTokenTask(Context context, OAuthConsumer consumer, OAuthProvider provider, SharedPreferences prefs) {
			this.context = context;
			this.consumer = consumer;
			this.provider = provider;
			this.prefs = prefs;
		}

		@Override
		protected Void doInBackground(Uri... params) {
			final Uri uri = params[0];
			final String oauth_verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);

			try {
				provider.retrieveAccessToken(consumer, oauth_verifier);

				final Editor edit = prefs.edit();
				edit.putString(OAuth.OAUTH_TOKEN, consumer.getToken());
				edit.putString(OAuth.OAUTH_TOKEN_SECRET, consumer.getTokenSecret());
				edit.commit();

				String token1 = prefs.getString(OAuth.OAUTH_TOKEN, "");

				String secret1 = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

				consumer.setTokenWithSecret(token1, secret1);

				context.startActivity(new Intent(context, DisplayDataActivity.class));
				AccessToken aa = new AccessToken(token1, secret1);
				final Twitter twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
				twitter.setOAuthAccessToken(aa);
				try {
					SharedPreferences pref = getSharedPreferences("MyPref", 0);
					Editor editor = pref.edit();
					editor.putString("twitter_name", twitter.getScreenName());
					editor.commit();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (TwitterException e) {
					e.printStackTrace();
				}

				long userId = aa.getUserId();
				usert = twitter.showUser(userId);
				String username = usert.getName();

				tok = aa.getToken();
				secret = aa.getTokenSecret();
				executeAfterAccessTokenRetrieval();
				Log.i("USER NAME", "user name: " + username);
				Log.i("ACCESS TOKEN", "access token: " + tok);
				Log.i("TAG", "OAuth - Access Token Retrieved");
			} catch (Exception e) {

				Log.i("TAG....here", "OAuth - Access Token Retrieval Error" + e);

			}
			return null;
		}

		public void executeAfterAccessTokenRetrieval() {
			// TODO Auto-generated method stub
			var = "not";
			client = new DefaultHttpClient();
			Log.i("TAG", "executeAfterAccessTokenRetrieval");
			new Read().execute(" ");
		}
	}

	public void downloadvideo(final String pdfId) {
		boolean fileCheck = false;
		DatabaseHandler db = new DatabaseHandler(DisplayDataActivity.this);
		final ArrayList<PDFBean> videosList = db.getEbookVideoURL(pdfId);

		for (PDFBean obj : videosList) {
			String fileName = obj.getPDF_FILE().substring(obj.getPDF_FILE().lastIndexOf("/") + 1, obj.getPDF_FILE().length());

			File file = new File(getFilesDir(), fileName);
			if (file.exists()) {
				fileCheck = true;
			} else {
				fileCheck = false;
			}
		}
		if (!fileCheck) {
			new AlertDialog.Builder(this).setTitle("Download Videos").setMessage("Are you sure you want to Downlaod all video for offline viewing?")
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// continue with delete
							Log.e("Pdf id :-> ", pdfId);
							Set<String> arr = new HashSet<String>();
							for (PDFBean bean : videosList) {
								Log.v("Pdf video id :-> ", bean.getPDF_FILE() + ",");
								arr.add(bean.getPDF_FILE());
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
								str = new String[arr.size()];

								for (String s : arr) {
									s = s.replaceAll("\\s+", "%20");
									str[i] = s;
									i++;
								}
								if (str.length > 0) {
									i = 0;

									DownloadVideoAsynTask downloadTask = new DownloadVideoAsynTask();
									downloadTask.execute(str[i]);
								}
							}
						}
					}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// do nothing
						}
					}).setIcon(android.R.drawable.ic_dialog_alert).show();
		} else {
			new AlertDialog.Builder(this).setTitle("Delete Videos").setMessage("Are you sure you want to delete all Videos?")
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// continue with delete
							boolean deleted;
							for (PDFBean obj : videosList) {
								String fileName = obj.getPDF_FILE().substring(obj.getPDF_FILE().lastIndexOf("/") + 1, obj.getPDF_FILE().length());
								File dir = getFilesDir();
								File file = new File(dir, fileName);
								deleted = file.delete();
								Log.v(fileName + "---->", deleted + "");
							}
						}
					}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// do nothing
						}
					}).setIcon(android.R.drawable.ic_dialog_alert).show();
		}

	}

	class DownloadVideoAsynTask extends AsyncTask<String, Integer, String> {
		private String name = "";
		private Context context;
		private PowerManager.WakeLock mWakeLock;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.progressbar);
			AnimationDrawable shiftedAnimation = getAnimation(b);
			pbar = (ProgressBar) findViewById(R.id.progress_ebook);
			pbar.setVisibility(ProgressBar.VISIBLE);
			pbar.setBackground(shiftedAnimation);
			shiftedAnimation.start();
			Functions.showLoadingWithoutDialog(DisplayDataActivity.this, "");
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			Functions.dismissLoadingDialog();
			i++;
			if (i < str.length) {
				DownloadVideoAsynTask dd = new DownloadVideoAsynTask();
				dd.execute(str[i]);
			} else {
				AlertDialog alertDialog = new AlertDialog.Builder(DisplayDataActivity.this).create();
				alertDialog.setTitle("MedArkive Service");
				alertDialog.setMessage("The Videos has saved on your device for futher working");
				alertDialog.setIcon(R.drawable.ic_check);
				alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// here you can add functions
					}
				});
				alertDialog.show();
				// mProgressDialog.dismiss();

				pbar.clearAnimation();
				pbar.destroyDrawingCache();
				pbar.setVisibility(ProgressBar.INVISIBLE);
				enable(true);
			}
		}

		@Override
		protected String doInBackground(String... sUrl) {
			// initAnimation();
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			// for (int i = 0; i < sUrl.length; i++) {
			try {
				name = sUrl[0].toString().substring(sUrl[0].toString().lastIndexOf('/') + 1, sUrl[0].toString().length());
				sUrl[0] = sUrl[0].replaceAll("\\s+", "%20");
				URL url = new URL(sUrl[0]);
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
				byte data[] = new byte[40485760];
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
				return e.toString();
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
				}
			}
			if (connection != null)
				connection.disconnect();
			// }
			return null;
		}
	}
}