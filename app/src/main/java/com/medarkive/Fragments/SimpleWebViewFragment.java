package com.medarkive.Fragments;

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

import rmn.androidscreenlibrary.ASSL;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.medarkive.R;
import com.medarkive.Beans.TrackerDataBean;
import com.medarkive.Main.DisplayDataActivity;
import com.medarkive.Main.LoginActivity;
import com.medarkive.Utilities.SessionManager;
import com.medarkive.Utilities.TrackerDataSender;

@SuppressWarnings("deprecation")
public class SimpleWebViewFragment extends Fragment {

	public static final String EXTRA_URL = "url";
	public static final String dashboard_check = "dashboard_view";
	public static final String MYARKIVE_CHECK = "myarkive";
	private String mUrl = null;

	private FrameLayout mView = null;
	private ImageView mImageView = null;
	private static WebView browser = null;
	private ProgressBar mPbar = null;

	// private Button tracker = null;
	// private ImageView forwardTracker = null;
	// private View buttonDivider = null;
	LinearLayout searchLay;
	private double browseTime = 0;
	private long start = 0;
	private long end = 0;
	private ArrayList<TrackerDataBean> browsehistory;
	private String trackURL;
	private String trackTitle;
	private SearchView searchView;
	private ImageView forward = null;
	private ImageView backward = null;
	private ImageView share = null;
	public JSONObject obj;
	public static String uid;

	// Session.StatusCallback scb;

	String medarkiveShareText = "MedArkive help HCP's keep track of their CPD and discover interesting research and educational content. Our service is free please try it now and see how we can help you to save hours on your CPD: https://itunes.apple.com/app/medarkive/id835824114?mt=8 Best wishes, MedArkive";

	@Override
	public void onStop() {
		// TODO Auto-generated method stubsearchView
		super.onStop();
		System.out.println("On Stop");
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			Activity a = getActivity();
			if (a != null)
				a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		System.out.println("onDestroyView");
		// if (browsehistory.size() > 0)
		// updateTracker(browsehistory);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUrl = getArguments().getString(EXTRA_URL);
		if (!mUrl.startsWith("http"))
			mUrl = "http://" + mUrl;
	}

	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.web_view, container, false);
		mPbar = (ProgressBar) view.findViewById(R.id.web_view_progress);

		mView = (FrameLayout) view.findViewById(R.id.web_view);

		mImageView = new ImageView(getActivity());
		mImageView.setBackgroundColor(Color.parseColor("#4B566D"));
		mImageView.setImageResource(R.drawable.big_image_loading);
		mImageView.setScaleType(ScaleType.CENTER_INSIDE);
		mImageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mImageView.post(new Runnable() {
			@Override
			public void run() {
				((AnimationDrawable) mImageView.getDrawable()).start();
			}
		});
		mView.addView(mImageView);

		browser = new WebView(getActivity());
		browser.setVisibility(View.GONE);
		searchLay = (LinearLayout) view.findViewById(R.id.searchLay);

		// browser.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT));
		if (mUrl != null) {
			browser.setWebViewClient(new MyWebViewClient());
			browser.setWebChromeClient(new MyWebChromeClient());
			browser.getSettings().setPluginState(PluginState.ON);
			browser.getSettings().setDefaultZoom(ZoomDensity.FAR);
			browser.getSettings().setBuiltInZoomControls(true);
			browser.getSettings().setSupportZoom(true);
			browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
			browser.getSettings().setAllowFileAccess(true);
			browser.getSettings().setDomStorageEnabled(true);
			browser.getSettings().setJavaScriptEnabled(true);
			browser.getSettings().setAppCacheEnabled(true);
			browser.setBackgroundColor(0x00000000);

			browser.getSettings().setLoadWithOverviewMode(true);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				browser.getSettings().setDisplayZoomControls(false);

			browser.loadUrl(mUrl);
		}

		mView.addView(browser);

		// tracker = (Button) view.findViewById(R.id.tracker);
		// forwardTracker = (ImageView) view.findViewById(R.id.forward_tracker);
		// buttonDivider = (View) view.findViewById(R.id.button_divider);
		searchView = (SearchView) view.findViewById(R.id.searchView);

		if (getArguments().getBoolean(dashboard_check)) {
			// setCustomActionBarForDashBoard();
			// tracker.setVisibility(Button.VISIBLE);
			// forwardTracker.setVisibility(ImageView.VISIBLE);
			// buttonDivider.setVisibility(View.VISIBLE);
			searchLay.setVisibility(8);
		} else {
			// setCustomActionBarForBrowser();
			// tracker.setVisibility(Button.GONE);
			// forwardTracker.setVisibility(ImageView.GONE);
			// buttonDivider.setVisibility(View.GONE);
			searchLay.setVisibility(0);
		}
		if (getArguments().getBoolean(MYARKIVE_CHECK)) {
			setCustomActionBarForMyarkive(getActivity());
			searchLay.setVisibility(8);
		}

		SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

		SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
			public boolean onQueryTextChange(String newText) {
				// this is your adapter that will be filtered
				return true;
			}

			public boolean onQueryTextSubmit(String query) {
				InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				String url = "https://www.google.com/search?q=";
				browser.loadUrl(url + query);
				return false;
			}
		};
		searchView.setOnQueryTextListener(queryTextListener);
		// tracker.setOnClickListener(new OnClickListener() {

		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent i = new Intent(getActivity(), TrackerActivity.class);
		// getActivity().startActivity(i);
		// }
		// });

		// forwardTracker.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent i = new Intent(getActivity(), TrackerActivity.class);
		// getActivity().startActivity(i);
		// }
		// });

		browsehistory = new ArrayList<TrackerDataBean>();

		RelativeLayout rv = (RelativeLayout) view.findViewById(R.id.content);
		new ASSL(getActivity(), rv, 1134, 720, false);

		SessionManager sm = new SessionManager(getActivity());
		HashMap<String, String> user = sm.getUserDetails();
		uid = user.get(SessionManager.KEY_USER_ID);

		return view;
	}

	public class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int progress) {
			if (progress < 100 && mPbar.getVisibility() == ProgressBar.GONE) {
				mPbar.setVisibility(ProgressBar.VISIBLE);
				mPbar.setProgress(progress);
			}
			if (progress == 100) {
				mPbar.setVisibility(ProgressBar.GONE);
				updateButton();
			}
		}
	}

	public class MyWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			view.setVisibility(View.VISIBLE);
			final Animation fade = new AlphaAnimation(0.0f, 1.0f);
			fade.setDuration(200);
			view.startAnimation(fade);
			view.setVisibility(View.VISIBLE);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.endsWith(".mp4")) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse(url), "video/*");
				view.getContext().startActivity(intent);
				return true;
			} else {
				return super.shouldOverrideUrlLoading(view, url);
			}
		}

	}

	public static void goForward() {

		browser.goForward();
	}

	public static void goBack() {

		browser.goBack();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (mView != null && browser != null) {
			mView.removeView(browser);
			browser.removeAllViews();
			browser.destroy();
		}
	}

	@SuppressLint("InflateParams")
	public void setCustomActionBarForBrowser(final Activity ctx) {
		// android.app.ActionBar mActionBar = ((DisplayDataActivity)
		// getActivity())
		// .getActionBar();
		// mActionBar.setDisplayShowHomeEnabled(false);
		// mActionBar.setDisplayShowTitleEnabled(false);
		// LayoutInflater mInflater = LayoutInflater.from(getActivity());

		// final View mCustomView = mInflater.inflate(
		// R.layout.custom_action_bar_webview, null);
		// mActionBar.setCustomView(mCustomView);
		// mActionBar.setDisplayShowCustomEnabled(true);

		forward = (ImageView) ctx.findViewById(R.id.forward);
		forward.setBackgroundResource(R.drawable.ic_action_next_item);
		forward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goForward();
				updateButton();
			}
		});

		backward = (ImageView) ctx.findViewById(R.id.back1);
		backward.setBackgroundResource(R.drawable.ic_bck_arrow);
		backward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goBack();
				updateButton();
			}
		});

		share = (ImageView) ctx.findViewById(R.id.web_link_share);

		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// @SuppressWarnings("static-access")
				// LayoutInflater inflater = (LayoutInflater) ctx
				// .getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
				// View layout = inflater.inflate(R.layout.alert_share_web_view,
				// null);
				// AlertDialog.Builder builder;
				final Dialog alertDialog = new Dialog(ctx);
				// builder = new AlertDialog.Builder(ctx);
				// builder.setView(layout);
				alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				alertDialog.setContentView(R.layout.alert_share_web_view);

				LinearLayout rv = (LinearLayout) alertDialog.findViewById(R.id.rv);

				new ASSL(ctx, rv, 1134, 720, false);
				SessionManager sm = new SessionManager(ctx);
				final HashMap<String, String> user = sm.getUserDetails();
				final String userMessage = user.get(SessionManager.KEY_NAME) + " thinks this will interest you: ";

				alertDialog.findViewById(R.id.facebook).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						((DisplayDataActivity) ctx).shareOnFB(browser.getUrl());
						alertDialog.dismiss();
					}
				});

				alertDialog.findViewById(R.id.twitter).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Toast.makeText(v.getContext(), "twitter clicked",
						// 500).show();

						((DisplayDataActivity) ctx).shareOnTwitter(browser.getUrl());
						alertDialog.dismiss();
					}
				});
				alertDialog.findViewById(R.id.gmail).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						Toast.makeText(v.getContext(), "gmail clicked", 500).show();
						// Intent sendIntent = new Intent();
						// sendIntent.setAction(Intent.ACTION_SEND);
						// sendIntent.putExtra(Intent.EXTRA_TITLE,
						// userMessage + browser.getUrl());
						// sendIntent.putExtra(Intent.EXTRA_TEXT,
						// userMessage + browser.getUrl() + " "
						// + medarkiveShareText);
						// sendIntent
						// .putExtra(Intent.EXTRA_SUBJECT,
						// "I found something through MedArkive I want to share with you");
						// sendIntent.setPackage("com.google.android.gm");
						// sendIntent.setType("text/plain");
						// startActivity(sendIntent);

					}
				});

				final ImageButton cancelTrackingbtn = (ImageButton) alertDialog.findViewById(R.id.cancel_share);
				cancelTrackingbtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						alertDialog.cancel();
					}
				});
				alertDialog.show();
			}
		});

		ctx.findViewById(R.id.start_tracking).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				browsehistory = new ArrayList<TrackerDataBean>();
				trackURL = browser.getUrl();
				final TextView startTracking = (TextView) ctx.findViewById(R.id.start_tracking);

				if (startTracking.getText().toString().equals("Start")) {
					final Dialog alertDialogTrackingtitle = new Dialog(ctx);

					alertDialogTrackingtitle.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alertDialogTrackingtitle.setContentView(R.layout.alert_tracking_title);

					RelativeLayout rv = (RelativeLayout) alertDialogTrackingtitle.findViewById(R.id.rv);

					new ASSL(ctx, rv, 1134, 720, false);

					alertDialogTrackingtitle.setTitle("Tracking Title");
					TextView trackURLtxt = (TextView) alertDialogTrackingtitle.findViewById(R.id.track_url);
					trackURLtxt.setText(browser.getUrl());

					final Button startTrackingbtn = (Button) alertDialogTrackingtitle.findViewById(R.id.start_tracker_btn);

					startTrackingbtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							start = System.currentTimeMillis();
							final EditText trackingtitle = (EditText) alertDialogTrackingtitle.findViewById(R.id.tracking_title);
							// TODO Auto-generated method stub
							if (trackingtitle.getText() != null) {
								if (trackingtitle.getText().length() == 0) {
									final Dialog check = new Dialog(ctx);
									check.setTitle("Please give a title for start tracking");
									check.show();
								} else {
									startTracking.setText("Stop");
									trackTitle = trackingtitle.getText().toString();
									alertDialogTrackingtitle.cancel();
								}
							} else {
								final Dialog check = new Dialog(ctx);
								check.setTitle("Please give a title for start tracking");
								check.show();
							}
						}
					});

					final Button cancelTrackingbtn = (Button) alertDialogTrackingtitle.findViewById(R.id.cancel_tracking_btn);
					cancelTrackingbtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							alertDialogTrackingtitle.cancel();
						}
					});

					alertDialogTrackingtitle.show();
				} else {
					TrackerDataBean trackerdata = new TrackerDataBean();
					System.out.println("start time ----" + start);
					end = System.currentTimeMillis();
					System.out.println("End time ----=-" + end);
					long result = end - start;
					browseTime = result / 1000;
					System.out.println("total browse time " + browseTime);
					trackerdata.setSpentTime(browseTime + "");
					trackerdata.setLink(trackURL);
					trackerdata.setTitle(trackTitle);
					browsehistory.add(trackerdata);
					TrackerDataSender tra = new TrackerDataSender();
					tra.updateTracker(browsehistory, ctx);
					startTracking.setText("Start");
				}
			}
		});

		ctx.findViewById(R.id.bookmarks).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog alertDialog = new Dialog(ctx);
				alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				alertDialog.setContentView(R.layout.alert_bookmark_view);
				LinearLayout rv = (LinearLayout) alertDialog.findViewById(R.id.rv);

				new ASSL(ctx, rv, 1134, 720, false);
				alertDialog.show();
				alertDialog.findViewById(R.id.add_bookmark).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						final Dialog alertDialogADDBookmark = new Dialog(ctx);
						alertDialogADDBookmark.requestWindowFeature(Window.FEATURE_NO_TITLE);
						alertDialogADDBookmark.setContentView(R.layout.alert_add_bookmark_view);
						LinearLayout rv = (LinearLayout) alertDialogADDBookmark.findViewById(R.id.rv);

						new ASSL(ctx, rv, 1134, 720, false);
						final EditText bookmarkTitle = (EditText) alertDialogADDBookmark.findViewById(R.id.bookmark_title);
						final TextView bookmarkURL = (TextView) alertDialogADDBookmark.findViewById(R.id.url);
						((TextView) alertDialogADDBookmark.findViewById(R.id.url)).setText(browser.getUrl());
						alertDialogADDBookmark.findViewById(R.id.add_bookmark_btn).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								try {
									obj = new JSONObject();
									obj.put("method", "save_bookmark");
									obj.put("user_id", uid);
									obj.put("bookmark_title", bookmarkTitle.getText());
									obj.put("bookmark_link", bookmarkURL.getText());
									SaveBookmarkes gbm = new SaveBookmarkes();
									gbm.execute();
								} catch (Exception e) {
									e.printStackTrace();
								}
								alertDialogADDBookmark.cancel();
							}
						});
						alertDialogADDBookmark.findViewById(R.id.cancel_add_bookmark_btn).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO
								alertDialogADDBookmark.cancel();
							}
						});
						alertDialogADDBookmark.show();
						alertDialog.dismiss();
					}
				});
				alertDialog.findViewById(R.id.show_bookmark).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// JSONObject response;
						try {
							((DisplayDataActivity) ctx).showBookmarkList();
						} catch (Exception e) {
							e.printStackTrace();
						}
						alertDialog.cancel();

					}
				});
				alertDialog.findViewById(R.id.cancel_bookmark).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialog.cancel();
					}
				});

			}
		});
	}

	private void updateButton() {
		if (backward != null && forward != null) {
			if (browser.canGoBack())
				backward.setBackgroundResource(R.drawable.ic_bck_arrow);
			else
				backward.setBackgroundResource(R.drawable.ic_bck_arrow);

			if (browser.canGoForward())
				forward.setBackgroundResource(R.drawable.ic_action_next_item);
			else
				forward.setBackgroundResource(R.drawable.ic_action_next_item);
		}

	}

	@SuppressLint("InflateParams")
	private void setCustomActionBarForMyarkive(final Activity activity) {
		View forward = activity.findViewById(R.id.forward1);
		forward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goForward();
			}
		});

		View back = activity.findViewById(R.id.backward);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goBack();
			}
		});

		activity.findViewById(R.id.back_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((DisplayDataActivity) getActivity()).view.setVisibility(8);
				Fragment fr = new MyArkiveFragment();
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, fr).commit();

				((DisplayDataActivity) getActivity()).changeTopBar(fr, "Myarkive", ((DisplayDataActivity) getActivity()).homeTBar);
			}
		});

		activity.findViewById(R.id.myarkive_share).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// @SuppressWarnings("static-access")
				// LayoutInflater inflater = (LayoutInflater) ctx
				// .getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
				// View layout = inflater.inflate(R.layout.alert_share_web_view,
				// null);
				// AlertDialog.Builder builder;
				final Dialog alertDialog = new Dialog(activity);
				// builder = new AlertDialog.Builder(ctx);
				// builder.setView(layout);
				alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				alertDialog.setContentView(R.layout.alert_share_web_view);

				LinearLayout rv = (LinearLayout) alertDialog.findViewById(R.id.rv);

				new ASSL(activity, rv, 1134, 720, false);
				SessionManager sm = new SessionManager(activity);
				final HashMap<String, String> user = sm.getUserDetails();
				final String userMessage = user.get(SessionManager.KEY_NAME) + " thinks this will interest you: ";

				alertDialog.findViewById(R.id.facebook).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						((DisplayDataActivity) activity).shareOnFB(browser.getUrl());
						alertDialog.dismiss();
					}
				});

				alertDialog.findViewById(R.id.twitter).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Toast.makeText(v.getContext(), "twitter clicked",
						// 500).show();

						((DisplayDataActivity) activity).shareOnTwitter(browser.getUrl());
						alertDialog.dismiss();
					}
				});
				alertDialog.findViewById(R.id.gmail).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						Toast.makeText(v.getContext(), "gmail clicked", 500).show();

					}
				});

				final ImageButton cancelTrackingbtn = (ImageButton) alertDialog.findViewById(R.id.cancel_share);
				cancelTrackingbtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						alertDialog.cancel();
					}
				});
				alertDialog.show();

			}

		});

		activity.findViewById(R.id.start_tracking1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				trackURL = browser.getUrl();
				final TextView startTracking = (TextView) activity.findViewById(R.id.start_tracking1);

				if (startTracking.getText().toString().equals("Start")) {
					final Dialog alertDialogTrackingtitle = new Dialog(getActivity());
					alertDialogTrackingtitle.setContentView(R.layout.alert_tracking_title);
					alertDialogTrackingtitle.setTitle("Tracking Title");
					TextView trackURLtxt = (TextView) alertDialogTrackingtitle.findViewById(R.id.track_url);
					trackURLtxt.setText(browser.getUrl());

					final Button startTrackingbtn = (Button) alertDialogTrackingtitle.findViewById(R.id.start_tracker_btn);

					startTrackingbtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							start = System.currentTimeMillis();
							final EditText trackingtitle = (EditText) alertDialogTrackingtitle.findViewById(R.id.tracking_title);
							// TODO Auto-generated method stub
							if (trackingtitle.getText() != null) {
								if (trackingtitle.getText().length() == 0) {
									final Dialog check = new Dialog(getActivity());
									check.setTitle("Please give a title for start tracking");
									check.show();
								} else {
									startTracking.setText("Stop");
									trackTitle = trackingtitle.getText().toString();
									alertDialogTrackingtitle.cancel();
								}
							} else {
								final Dialog check = new Dialog(getActivity());
								check.setTitle("Please give a title for start tracking");
								check.show();
							}
						}
					});

					final Button cancelTrackingbtn = (Button) alertDialogTrackingtitle.findViewById(R.id.cancel_tracking_btn);
					cancelTrackingbtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							alertDialogTrackingtitle.cancel();
						}
					});

					alertDialogTrackingtitle.show();
				} else {
					TrackerDataBean trackerdata = new TrackerDataBean();
					end = System.currentTimeMillis();
					// System.out.println("End time ----=-" + end);
					long result = end - start;
					browseTime = result / 1000;
					trackerdata.setSpentTime(browseTime + "");
					trackerdata.setLink(trackURL);
					trackerdata.setTitle(trackTitle);
					browsehistory.add(trackerdata);
					TrackerDataSender tra = new TrackerDataSender();
					tra.updateTracker(browsehistory, getActivity());
					startTracking.setText("Start");
				}
			}
		});
	}

	public class SaveBookmarkes extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = "fail";
			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
			HttpResponse response;
			JSONObject json;
			try {
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
			return result;
		}
	}

}
