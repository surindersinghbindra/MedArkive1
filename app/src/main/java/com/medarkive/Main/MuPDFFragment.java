/*
package com.medarkive.Main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.artifex.mupdf.viewer.MuPDFCore;
import com.artifex.mupdfdemo.FilePicker;
import com.artifex.mupdfdemo.MuPDFAlert;
import com.medarkive.Beans.PDFBean;
import com.medarkive.Beans.PDFTrackerBean;
import com.medarkive.R;
import com.medarkive.Utilities.ConnectionDetector;
import com.medarkive.Utilities.DatabaseHandler;
import com.medarkive.Utilities.MyLocation;
import com.medarkive.Utilities.MyLocation.LocationResult;
import com.medarkive.Utilities.SessionManager;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

import rmn.androidscreenlibrary.ASSL;

class ThreadPerTaskExecutor implements Executor {
	public void execute(Runnable r) {
		new Thread(r).start();
	}
}

@SuppressLint("SimpleDateFormat")
public class MuPDFFragment extends Fragment implements FilePicker.FilePickerSupport {
	*/
/* The core rendering instance *//*

	enum TopBarMode {
		Main, Search, Annot, Delete, More, Accept
	};

	enum AcceptMode {
		Highlight, Underline, StrikeOut, Ink
	};

	private long readingTime = 0;
	private long startTimer = 0;
	private long stopTimer = 0;
	private final int OUTLINE_REQUEST = 0;
	// private final int PRINT_REQUEST = 1;
	private final int FILEPICK_REQUEST = 2;
	private MuPDFCore core;
	private String mFileName;
	private MuPDFReaderView mDocView;
	private View mButtonsView;
	private boolean mButtonsVisible;
	private EditText mPasswordView;
	private TextView mFilenameView;
	private SeekBar mPageSlider;
	private int mPageSliderRes;
	private TextView mPageNumberView;
	private TextView mInfoView;
	private ImageButton mOutlineButton;
	private TextView mAnnotTypeText;
	private ViewAnimator mTopBarSwitcher;
	private ImageButton mLinkButton;
	private TopBarMode mTopBarMode = TopBarMode.Main;
	private AcceptMode mAcceptMode;
	private AlertDialog.Builder mAlertBuilder;
	private boolean mLinkHighlight = false;
	private final Handler mHandler = new Handler();
	private boolean mAlertsActive = false;
	private boolean mReflow = false;
	private AsyncTask<Void, Void, MuPDFAlert> mAlertTask;
	private AlertDialog mAlertDialog;
	// private FilePicker mFilePicker;
	private double latitude;
	private double longitude;
	private int pageNumber = 0;
	private PDFBean pdfBean;
	private String starttime;
	HashMap<String, String> user;
	ConnectionDetector cd;
	DatabaseHandler db;
	ViewGroup container;
	LayoutInflater inflater;

	public void createAlertWaiter() {
		mAlertsActive = true;
		// All mupdf library calls are performed on asynchronous tasks to avoid
		// stalling
		// the UI. Some calls can lead to javascript-invoked requests to display
		// an
		// alert dialog and collect a reply from the user. The task has to be
		// blocked
		// until the user's reply is received. This method creates an
		// asynchronous task,
		// the purpose of which is to wait of these requests and produce the
		// dialog
		// in response, while leaving the core blocked. When the dialog receives
		// the
		// user's response, it is sent to the core via replyToAlert, unblocking
		// it.
		// Another alert-waiting task is then created to pick up the next alert.
		if (mAlertTask != null) {
			mAlertTask.cancel(true);
			mAlertTask = null;
		}
		if (mAlertDialog != null) {
			mAlertDialog.cancel();
			mAlertDialog = null;
		}
		mAlertTask = new AsyncTask<Void, Void, MuPDFAlert>() {

			@Override
			protected MuPDFAlert doInBackground(Void... arg0) {
				if (!mAlertsActive)
					return null;
				return core.waitForAlert();
			}

			@Override
			protected void onPostExecute(final MuPDFAlert result) {
				// core.waitForAlert may return null when shutting down
				if (result == null)
					return;
				final MuPDFAlert.ButtonPressed pressed[] = new MuPDFAlert.ButtonPressed[3];
				for (int i = 0; i < 3; i++)
					pressed[i] = MuPDFAlert.ButtonPressed.None;
				DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						mAlertDialog = null;
						if (mAlertsActive) {
							int index = 0;
							switch (which) {
							case AlertDialog.BUTTON1:
								index = 0;
								break;
							case AlertDialog.BUTTON2:
								index = 1;
								break;
							case AlertDialog.BUTTON3:
								index = 2;
								break;
							}
							result.buttonPressed = pressed[index];
							// Send the user's response to the core, so that it
							// can
							// continue processing.
							core.replyToAlert(result);
							// Create another alert-waiter to pick up the next
							// alert.
							createAlertWaiter();
						}
					}
				};
				mAlertDialog = mAlertBuilder.create();
				mAlertDialog.setTitle(result.title);
				mAlertDialog.setMessage(result.message);
				switch (result.iconType) {
				case Error:
					break;
				case Warning:
					break;
				case Question:
					break;
				case Status:
					break;
				}
				switch (result.buttonGroupType) {
				case OkCancel:
					mAlertDialog.setButton(AlertDialog.BUTTON2, getString(R.string.cancel), listener);
					pressed[1] = MuPDFAlert.ButtonPressed.Cancel;
				case Ok:
					mAlertDialog.setButton(AlertDialog.BUTTON1, getString(R.string.okay), listener);
					pressed[0] = MuPDFAlert.ButtonPressed.Ok;
					break;
				case YesNoCancel:
					mAlertDialog.setButton(AlertDialog.BUTTON3, getString(R.string.cancel), listener);
					pressed[2] = MuPDFAlert.ButtonPressed.Cancel;
				case YesNo:
					mAlertDialog.setButton(AlertDialog.BUTTON1, getString(R.string.yes), listener);
					pressed[0] = MuPDFAlert.ButtonPressed.Yes;
					mAlertDialog.setButton(AlertDialog.BUTTON2, getString(R.string.no), listener);
					pressed[1] = MuPDFAlert.ButtonPressed.No;
					break;
				}
				mAlertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						mAlertDialog = null;
						if (mAlertsActive) {
							result.buttonPressed = MuPDFAlert.ButtonPressed.None;
							core.replyToAlert(result);
							createAlertWaiter();
						}
					}
				});

				mAlertDialog.show();
			}
		};

		mAlertTask.executeOnExecutor(new ThreadPerTaskExecutor());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		Log.v("PDF file ===----->>", pdfBean.getPDF_FILE());
	}

	public void gotoPage(int paramInt) {
		Log.v("goto page num", paramInt + "");
		Log.v("current page num", this.mDocView.getDisplayedViewIndex() + "");
		this.mDocView.setDisplayedViewIndex(paramInt);
	}

	public void destroyAlertWaiter() {
		mAlertsActive = false;
		if (mAlertDialog != null) {
			mAlertDialog.cancel();
			mAlertDialog = null;
		}
		if (mAlertTask != null) {
			mAlertTask.cancel(true);
			mAlertTask = null;
		}
	}

	private MuPDFCore openFile(String path) {
		int lastSlashPos = path.lastIndexOf('/');
		mFileName = new String(lastSlashPos == -1 ? path : path.substring(lastSlashPos + 1));
		System.out.println("Trying to open " + path);
		try {
			core = new MuPDFCore(getActivity(), path);
			// New file: drop the old outline data
			OutlineActivityData.set(null);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		return core;
	}

	private MuPDFCore openBuffer(byte buffer[], String magic) {
		System.out.println("Trying to open byte buffer");
		try {
			core = new MuPDFCore(getActivity(), buffer, magic);
			// New file: drop the old outline data
			OutlineActivityData.set(null);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		return core;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		// TODO Auto-generated method stub
		// return super.onCreateView(inflater, container, savedInstanceState);
		SessionManager sm = new SessionManager(getActivity());
		db = new DatabaseHandler(getActivity());
		cd = new ConnectionDetector(getActivity());
		user = sm.getUserDetails();
		View rootView = inflater.inflate(R.layout.mupdf_fragment, container, false);

		timer.schedule(timerTask, 20000);
		pdfBean = (PDFBean) getArguments().getSerializable(DisplayDataActivity.EXTRA_URL);

		LocationResult locationResult = new LocationResult() {
			@Override
			public void gotLocation(Location location) {
				// Got the location!
				if (location != null) {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
				}
			}
		};
		MyLocation myLocation = new MyLocation();
		myLocation.getLocation(getActivity(), locationResult);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));

		starttime = dateFormat.format(cal.getTime());
		startTimer = System.currentTimeMillis();

		mAlertBuilder = new AlertDialog.Builder(getActivity());

		if (core == null) {
			core = (MuPDFCore) getActivity().getLastNonConfigurationInstance();

			if (savedInstanceState != null && savedInstanceState.containsKey("FileName")) {
				mFileName = savedInstanceState.getString("FileName");
			}
		}
		if (core == null) {
			// Intent intent = getActivity().getIntent();
			byte buffer[] = null;
			// if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			// Uri uri = intent.getData();
			Uri uri = null;
			if (pdfBean != null) {
				if (pdfBean.getFILE_TYPE().equalsIgnoreCase("ebook")) {
					Log.v("Ebook", "True");
					File file = getActivity().getDir(pdfBean.getPDF_ID(), getActivity().MODE_PRIVATE);
					uri = Uri.parse(file.getAbsolutePath() + "/" + pdfBean.getPDF_FILE().substring(pdfBean.getPDF_FILE().lastIndexOf("/")));

				} else {
					Log.v("PDF", "True");
					uri = Uri.parse(LoginActivity.path + pdfBean.getPDF_FILE().substring(pdfBean.getPDF_FILE().lastIndexOf("/")));
				}

			}
			System.out.println("URI to open is: " + uri);
			if (uri.toString().startsWith("content://")) {
				String reason = null;
				try {
					InputStream is = getActivity().getContentResolver().openInputStream(uri);
					int len = is.available();
					buffer = new byte[len];
					is.read(buffer, 0, len);
					is.close();
				} catch (java.lang.OutOfMemoryError e) {
					System.out.println("Out of memory during buffer reading");
					reason = e.toString();
				} catch (Exception e) {
					System.out.println("Exception reading from stream: " + e);

					// Handle view requests from the Transformer Prime's
					// file manager
					// Hopefully other file managers will use this same
					// scheme, if not
					// using explicit paths.
					// I'm hoping that this case below is no longer
					// needed...but it's
					// hard to test as the file manager seems to have
					// changed in 4.x.
					try {
						Cursor cursor = getActivity().getContentResolver().query(uri, new String[] { "_data" }, null, null, null);
						if (cursor.moveToFirst()) {
							String str = cursor.getString(0);
							if (str == null) {
								reason = "Couldn't parse data in intent";
							} else {
								uri = Uri.parse(str);
							}
						}
					} catch (Exception e2) {
						System.out.println("Exception in Transformer Prime file manager code: " + e2);
						reason = e2.toString();
					}
				}
				if (reason != null) {
					buffer = null;
					// Resources res = getResources();
					// AlertDialog alert = mAlertBuilder.create();
					// setTitle(String.format(res.getString(R.string.cannot_open_document_Reason),
					// reason));
					// alert.setButton(AlertDialog.BUTTON_POSITIVE,
					// getString(R.string.dismiss), new
					// DialogInterface.OnClickListener() {
					// public void onClick(DialogInterface dialog, int which) {
					// finish();
					// }
					// });
					// alert.show();
					return container;
				}
			}
			if (buffer != null) {
				core = openBuffer(buffer, "pdf");
			} else {
				core = openFile(Uri.decode(uri.getEncodedPath()));
			}
			SearchTaskResult.set(null);
		}
		if (core != null && core.needsPassword()) {
			requestPassword(savedInstanceState);
			return container;
		}
		if (core != null && core.countPages() == 0) {
			core = null;
		}
		// }
		if (core == null) {
			AlertDialog alert = mAlertBuilder.create();
			alert.setTitle(R.string.cannot_open_document);
			alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dismiss), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// finish();
				}
			});
			alert.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					// finish();
				}
			});
			alert.show();
			return container;
		}
		// createUI(savedInstanceState);

		if (core == null) {
			return rootView;
		}

		// Now create the UI.
		// First create the document view
		mDocView = new MuPDFReaderView(getActivity()) {
			@Override
			protected void onMoveToChild(int i) {
				if (core == null)
					return;
				mPageNumberView.setText(String.format("%d / %d", i + 1, core.countPages()));
				mPageSlider.setMax((core.countPages() - 1) * mPageSliderRes);
				mPageSlider.setProgress(i * mPageSliderRes);
				pageNumber = i + 1;
				super.onMoveToChild(i);
			}

			@Override
			protected void onTapMainDocArea() {
				if (!mButtonsVisible) {
					showButtons();
				} else {
					if (mTopBarMode == TopBarMode.Main)
						hideButtons();
				}
			}

			@Override
			protected void onDocMotion() {
				hideButtons();
			}

			@Override
			protected void onHit(Hit item) {
				switch (mTopBarMode) {
				case Annot:
					if (item == Hit.Annotation) {
						showButtons();
						mTopBarMode = TopBarMode.Delete;
						mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
					}
					break;
				case Delete:
					mTopBarMode = TopBarMode.Annot;
					mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
					// fall through
				default:
					MuPDFView pageView = (MuPDFView) mDocView.getDisplayedView();
					if (pageView != null)
						pageView.deselectAnnotation();
					break;
				}
			}
		};
		mDocView.setAdapter(new MuPDFPageAdapter(getActivity(), this, core));
		makeButtonsView();

		// Set up the page slider
		int smax = Math.max(core.countPages() - 1, 1);
		mPageSliderRes = ((10 + smax - 1) / smax) * 2;

		// Set the file-name text
		mFilenameView.setText(mFileName);

		// Activate the seekbar
		mPageSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
				mDocView.setDisplayedViewIndex((seekBar.getProgress() + mPageSliderRes / 2) / mPageSliderRes);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				updatePageNumView((progress + mPageSliderRes / 2) / mPageSliderRes);
			}
		});

		mLinkButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setLinkHighlight(!mLinkHighlight);
			}
		});

		if (core.hasOutline()) {
			mOutlineButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					OutlineItem outline[] = core.getOutline();
					if (outline != null) {
						OutlineActivityData.get().items = outline;
						Intent intent = new Intent(getActivity(), OutlineActivity.class);
						startActivityForResult(intent, OUTLINE_REQUEST);
					}
				}
			});
		} else {
			mOutlineButton.setVisibility(View.GONE);
		}

		// Reenstate last state if it was recorded
		SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
		mDocView.setDisplayedViewIndex(prefs.getInt("page" + mFileName, 0));

		if (savedInstanceState == null || !savedInstanceState.getBoolean("ButtonsHidden", false))
			showButtons();

		if (savedInstanceState != null && savedInstanceState.getBoolean("SearchMode", false))
			searchModeOn();
		RelativeLayout rv = (RelativeLayout) rootView.findViewById(R.id.rv);
		new ASSL(getActivity(), rv, 1134, 720, false);
		rv.addView(mDocView);
		// rv.addView(mButtonsView);
		setLinkHighlight(true);
		// LinearLayout.LayoutParams relativeParams =
		// (LinearLayout.LayoutParams) layout.getLayoutParams();
		// relativeParams.setMargins(0, 96, 0, 0);
		// layout.setLayoutParams(relativeParams);

		return rootView;

	}

	public void requestPassword(final Bundle savedInstanceState) {
		mPasswordView = new EditText(getActivity());
		mPasswordView.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
		mPasswordView.setTransformationMethod(new PasswordTransformationMethod());

		AlertDialog alert = mAlertBuilder.create();
		alert.setTitle(R.string.enter_password);
		alert.setView(mPasswordView);
		alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.okay), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (core.authenticatePassword(mPasswordView.getText().toString())) {
					createUI(savedInstanceState);
				} else {
					requestPassword(savedInstanceState);
				}
			}
		});
		alert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// finish();
			}
		});
		alert.show();
	}

	public void createUI(Bundle savedInstanceState) {
		if (core == null)
			return;
		// Now create the UI.
		// First create the document view
		mDocView = new MuPDFReaderView(getActivity()) {
			@Override
			protected void onMoveToChild(int i) {
				if (core == null)
					return;
				mPageNumberView.setText(String.format("%d / %d", i + 1, core.countPages()));
				mPageSlider.setMax((core.countPages() - 1) * mPageSliderRes);
				mPageSlider.setProgress(i * mPageSliderRes);
				pageNumber = i + 1;
				super.onMoveToChild(i);
			}

			@Override
			protected void onTapMainDocArea() {
				if (!mButtonsVisible) {
					showButtons();
				} else {
					if (mTopBarMode == TopBarMode.Main)
						hideButtons();
				}
			}

			@Override
			protected void onDocMotion() {
				hideButtons();
			}

			@Override
			protected void onHit(Hit item) {
				switch (mTopBarMode) {
				case Annot:
					if (item == Hit.Annotation) {
						showButtons();
						mTopBarMode = TopBarMode.Delete;
						mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
					}
					break;
				case Delete:
					mTopBarMode = TopBarMode.Annot;
					mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
					// fall through
				default:
					// Not in annotation editing mode, but the pageview will
					// still select and highlight hit annotations, so
					// deselect just in case.
					MuPDFView pageView = (MuPDFView) mDocView.getDisplayedView();
					if (pageView != null)
						pageView.deselectAnnotation();
					break;
				}
			}
		};
		mDocView.setAdapter(new MuPDFPageAdapter(getActivity(), this, core));
		makeButtonsView();

		// Set up the page slider
		int smax = Math.max(core.countPages() - 1, 1);
		mPageSliderRes = ((10 + smax - 1) / smax) * 2;

		// Set the file-name text
		mFilenameView.setText(mFileName);

		// Activate the seekbar
		mPageSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
				mDocView.setDisplayedViewIndex((seekBar.getProgress() + mPageSliderRes / 2) / mPageSliderRes);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				updatePageNumView((progress + mPageSliderRes / 2) / mPageSliderRes);
			}
		});

		mLinkButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setLinkHighlight(!mLinkHighlight);
			}
		});

		if (core.hasOutline()) {
			mOutlineButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					OutlineItem outline[] = core.getOutline();
					if (outline != null) {
						OutlineActivityData.get().items = outline;
						Intent intent = new Intent(getActivity(), OutlineActivity.class);
						startActivityForResult(intent, OUTLINE_REQUEST);
					}
				}
			});
		} else {
			mOutlineButton.setVisibility(View.GONE);
		}

		// Reenstate last state if it was recorded
		SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
		mDocView.setDisplayedViewIndex(prefs.getInt("page" + mFileName, 0));

		if (savedInstanceState == null || !savedInstanceState.getBoolean("ButtonsHidden", false))
			showButtons();

		if (savedInstanceState != null && savedInstanceState.getBoolean("SearchMode", false))
			searchModeOn();

		RelativeLayout layout = new RelativeLayout(getActivity());
		layout.addView(mDocView);
		layout.addView(mButtonsView);
		getActivity().setContentView(layout);
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// switch (requestCode) {
	// case OUTLINE_REQUEST:
	// if (resultCode >= 0)
	// mDocView.setDisplayedViewIndex(resultCode);
	// break;
	// case PRINT_REQUEST:
	// if (resultCode == RESULT_CANCELED)
	// showInfo(getString(R.string.print_failed));
	// break;
	// case FILEPICK_REQUEST:
	// if (mFilePicker != null && resultCode == RESULT_OK)
	// mFilePicker.onPick(data.getData());
	// }
	// super.onActivityResult(requestCode, resultCode, data);
	// }

	public Object onRetainNonConfigurationInstance() {
		MuPDFCore mycore = core;
		core = null;
		return mycore;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (mFileName != null && mDocView != null) {
			outState.putString("FileName", mFileName);

			// Store current page in the prefs against the file name,
			// so that we can pick it up each time the file is loaded
			// Other info is needed only for screen-orientation change,
			// so it can go in the bundle
			SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putInt("page" + mFileName, mDocView.getDisplayedViewIndex());
			edit.commit();
		}

		if (!mButtonsVisible)
			outState.putBoolean("ButtonsHidden", true);

		if (mTopBarMode == TopBarMode.Search)
			outState.putBoolean("SearchMode", true);

		if (mReflow)
			outState.putBoolean("ReflowMode", true);
	}

	@Override
	public void onPause() {
		super.onPause();
		timer.cancel();
		// sendTrackerData();
		// if (mSearchTask != null)
		// mSearchTask.stop();

		if (mFileName != null && mDocView != null) {
			SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putInt("page" + mFileName, mDocView.getDisplayedViewIndex());
			edit.commit();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		timer.schedule(timerTask, 20000);
	}

	public void onDestroy() {
		timer.cancel();
		// sendTrackerData();
		if (mDocView != null) {
			mDocView.applyToChildren(new ReaderView.ViewMapper() {
				public void applyToView(View view) {
					((MuPDFView) view).releaseBitmaps();
				}
			});
		}
		if (core != null)
			core.onDestroy();
		if (mAlertTask != null) {
			mAlertTask.cancel(true);
			mAlertTask = null;
		}
		core = null;
		super.onDestroy();
	}

	private void setLinkHighlight(boolean highlight) {
		mLinkHighlight = highlight;
		// LINK_COLOR tint
		mLinkButton.setColorFilter(highlight ? Color.argb(0xFF, 172, 114, 37) : Color.argb(0xFF, 255, 255, 255));
		// Inform pages of the change.
		mDocView.setLinksEnabled(highlight);
	}

	private void showButtons() {
		if (core == null)
			return;
		if (!mButtonsVisible) {
			mButtonsVisible = true;
			// Update page number text and slider
			int index = mDocView.getDisplayedViewIndex();
			updatePageNumView(index);
			mPageSlider.setMax((core.countPages() - 1) * mPageSliderRes);
			mPageSlider.setProgress(index * mPageSliderRes);
			if (mTopBarMode == TopBarMode.Search) {
				// mSearchText.requestFocus();
				showKeyboard();
			}

			Animation anim = new TranslateAnimation(0, 0, -mTopBarSwitcher.getHeight(), 0);
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {
					mTopBarSwitcher.setVisibility(View.VISIBLE);
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
				}
			});
			mTopBarSwitcher.startAnimation(anim);

			anim = new TranslateAnimation(0, 0, mPageSlider.getHeight(), 0);
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {
					mPageSlider.setVisibility(View.VISIBLE);
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					mPageNumberView.setVisibility(View.VISIBLE);
				}
			});
			mPageSlider.startAnimation(anim);
		}
	}

	private void hideButtons() {
		if (mButtonsVisible) {
			mButtonsVisible = false;
			hideKeyboard();

			Animation anim = new TranslateAnimation(0, 0, 0, -mTopBarSwitcher.getHeight());
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					mTopBarSwitcher.setVisibility(View.INVISIBLE);
				}
			});
			mTopBarSwitcher.startAnimation(anim);

			anim = new TranslateAnimation(0, 0, 0, mPageSlider.getHeight());
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {
					mPageNumberView.setVisibility(View.INVISIBLE);
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					mPageSlider.setVisibility(View.INVISIBLE);
				}
			});
			mPageSlider.startAnimation(anim);
		}
	}

	private void searchModeOn() {
		if (mTopBarMode != TopBarMode.Search) {
			mTopBarMode = TopBarMode.Search;
			// Focus on EditTextWidget
			// mSearchText.requestFocus();
			showKeyboard();
			mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
		}
	}

	private void searchModeOff() {
		if (mTopBarMode == TopBarMode.Search) {
			mTopBarMode = TopBarMode.Main;
			hideKeyboard();
			mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
			SearchTaskResult.set(null);
			// Make the ReaderView act on the change to mSearchTaskResult
			// via overridden onChildSetup method.
			mDocView.resetupChildren();
		}
	}

	private void updatePageNumView(int index) {
		if (core == null)
			return;
		mPageNumberView.setText(String.format("%d / %d", index + 1, core.countPages()));
	}

	private void showInfo(String message) {
		mInfoView.setText(message);

		int currentApiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentApiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			// SafeAnimatorInflater safe = new SafeAnimatorInflater((Activity)
			// getActivity(), R.animator.info, (View) mInfoView);
		} else {
			mInfoView.setVisibility(View.VISIBLE);
			mHandler.postDelayed(new Runnable() {
				public void run() {
					mInfoView.setVisibility(View.INVISIBLE);
				}
			}, 500);
		}
	}

	private void makeButtonsView() {
		mButtonsView = getActivity().getLayoutInflater().inflate(R.layout.buttons, null);
		mFilenameView = (TextView) mButtonsView.findViewById(R.id.docNameText);
		mPageSlider = (SeekBar) mButtonsView.findViewById(R.id.pageSlider);
		mPageNumberView = (TextView) mButtonsView.findViewById(R.id.pageNumber);
		mInfoView = (TextView) mButtonsView.findViewById(R.id.info);
		mOutlineButton = (ImageButton) mButtonsView.findViewById(R.id.outlineButton);
		mAnnotTypeText = (TextView) mButtonsView.findViewById(R.id.annotType);
		mTopBarSwitcher = (ViewAnimator) mButtonsView.findViewById(R.id.switcher);
		mLinkButton = (ImageButton) mButtonsView.findViewById(R.id.linkButton);
		mTopBarSwitcher.setVisibility(View.INVISIBLE);
		mPageNumberView.setVisibility(View.INVISIBLE);
		mInfoView.setVisibility(View.INVISIBLE);
		mPageSlider.setVisibility(View.INVISIBLE);
	}

	public void OnMoreButtonClick(View v) {
		mTopBarMode = TopBarMode.More;
		mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
	}

	public void OnCancelMoreButtonClick(View v) {
		mTopBarMode = TopBarMode.Main;
		mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
	}

	public void OnEditAnnotButtonClick(View v) {
		mTopBarMode = TopBarMode.Annot;
		mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
	}

	public void OnCancelAnnotButtonClick(View v) {
		mTopBarMode = TopBarMode.More;
		mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
	}

	public void OnHighlightButtonClick(View v) {
		mTopBarMode = TopBarMode.Accept;
		mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
		mAcceptMode = AcceptMode.Highlight;
		mDocView.setMode(MuPDFReaderView.Mode.Selecting);
		mAnnotTypeText.setText(R.string.highlight);
		showInfo(getString(R.string.select_text));
	}

	public void OnUnderlineButtonClick(View v) {
		mTopBarMode = TopBarMode.Accept;
		mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
		mAcceptMode = AcceptMode.Underline;
		mDocView.setMode(MuPDFReaderView.Mode.Selecting);
		mAnnotTypeText.setText(R.string.underline);
		showInfo(getString(R.string.select_text));
	}

	public void OnInkButtonClick(View v) {
		mTopBarMode = TopBarMode.Accept;
		mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
		mAcceptMode = AcceptMode.Ink;
		mDocView.setMode(MuPDFReaderView.Mode.Drawing);
		mAnnotTypeText.setText(R.string.ink);
		showInfo(getString(R.string.draw_annotation));
	}

	public void OnCancelAcceptButtonClick(View v) {
		MuPDFView pageView = (MuPDFView) mDocView.getDisplayedView();
		if (pageView != null) {
			pageView.deselectText();
			pageView.cancelDraw();
		}
		mDocView.setMode(MuPDFReaderView.Mode.Viewing);
		switch (mAcceptMode) {
		// case CopyText:
		// mTopBarMode = TopBarMode.More;
		// break;
		default:
			mTopBarMode = TopBarMode.Annot;
			break;
		}
		mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
	}

	public void OnAcceptButtonClick(View v) {
		MuPDFView pageView = (MuPDFView) mDocView.getDisplayedView();
		boolean success = false;
		switch (mAcceptMode) {
		// case CopyText:
		// if (pageView != null)
		// success = pageView.copySelection();
		// mTopBarMode = TopBarMode.More;
		// showInfo(success?getString(R.string.copied_to_clipboard):getString(R.string.no_text_selected));
		// break;

		case Highlight:
			if (pageView != null)
				success = pageView.markupSelection(Annotation.Type.HIGHLIGHT);
			mTopBarMode = TopBarMode.Annot;
			if (!success)
				showInfo(getString(R.string.no_text_selected));
			break;

		case Underline:
			if (pageView != null)
				success = pageView.markupSelection(Annotation.Type.UNDERLINE);
			mTopBarMode = TopBarMode.Annot;
			if (!success)
				showInfo(getString(R.string.no_text_selected));
			break;

		case StrikeOut:
			if (pageView != null)
				success = pageView.markupSelection(Annotation.Type.STRIKEOUT);
			mTopBarMode = TopBarMode.Annot;
			if (!success)
				showInfo(getString(R.string.no_text_selected));
			break;

		case Ink:
			if (pageView != null)
				success = pageView.saveDraw();
			mTopBarMode = TopBarMode.Annot;
			if (!success)
				showInfo(getString(R.string.nothing_to_save));
			break;
		}
		mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
		mDocView.setMode(MuPDFReaderView.Mode.Viewing);
	}

	public void OnCancelSearchButtonClick(View v) {
		searchModeOff();
	}

	public void OnDeleteButtonClick(View v) {
		MuPDFView pageView = (MuPDFView) mDocView.getDisplayedView();
		if (pageView != null)
			pageView.deleteSelectedAnnotation();
		mTopBarMode = TopBarMode.Annot;
		mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
	}

	public void OnCancelDeleteButtonClick(View v) {
		MuPDFView pageView = (MuPDFView) mDocView.getDisplayedView();
		if (pageView != null)
			pageView.deselectAnnotation();
		mTopBarMode = TopBarMode.Annot;
		mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
	}

	private void showKeyboard() {
		// InputMethodManager imm = (InputMethodManager)
		// getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		// if (imm != null)
		// imm.showSoftInput(mSearchText, 0);
	}

	private void hideKeyboard() {
		// InputMethodManager imm = (InputMethodManager)
		// getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		// if (imm != null)
		// imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);
	}

	// public boolean onSearchRequested() {
	// if (mButtonsVisible && mTopBarMode == TopBarMode.Search) {
	// hideButtons();
	// } else {
	// showButtons();
	// searchModeOn();
	// }
	// return super.onSearchRequested();
	// }

	// @Override
	// public boolean onPrepareOptionsMenu(Menu menu) {
	// if (mButtonsVisible && mTopBarMode != TopBarMode.Search) {
	// hideButtons();
	// } else {
	// showButtons();
	// searchModeOff();
	// }
	// return super.onPrepareOptionsMenu(menu);
	// }

	@Override
	public void onStart() {
		if (core != null) {
			// core.startAlerts();
			createAlertWaiter();
		}

		super.onStart();
	}

	@Override
	public void onStop() {
		if (core != null) {
			destroyAlertWaiter();
			// core.stopAlerts();
		}

		super.onStop();
	}

	protected void sendTrackerData() {
		if (startTimer > 0) {
			System.out.println("latitue -------->>" + latitude);
			System.out.println("longitute -------->>" + longitude);
			stopTimer = System.currentTimeMillis();
			readingTime = (stopTimer - startTimer) / 1000;
			PDFTrackerBean trakingBean = new PDFTrackerBean();
			trakingBean.setPdf_id(pdfBean.getPDF_ID());
			if (pageNumber > 0) {
				trakingBean.setPage(pageNumber + "");
			}

			final String uid = user.get(SessionManager.KEY_USER_ID);
			final String token = user.get(SessionManager.KEY_TOKEN);
			trakingBean.setStarttime(starttime);

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			trakingBean.setEndtime(dateFormat.format(cal.getTime()));
			trakingBean.setInterval_sec(readingTime + "");
			trakingBean.setIp_address(latitude + "#" + longitude);
			if (pdfBean.getFILE_TYPE().equals("ebook")) {
				trakingBean.setFile_id(pdfBean.getPDF_LOGO());
			} else {
				trakingBean.setFile_id("0");
			}
			Log.e("Connection detector ------>", cd.isConnectedToInternet()+"");
			if (cd.isConnectedToInternet()) {

				Thread t = new Thread() {
					public void run() {
						// Looper.prepare();
						Geocoder geocoder = new Geocoder(getActivity());
						// Get the current location from the input parameter
						// list
						// Create a list to contain the result address
						List<Address> addresses = null;
						try {
							*/
/*
							 * Return 1 address.
							 *//*

							addresses = geocoder.getFromLocation(latitude, longitude, 1);
						} catch (IOException e1) {
							Log.e("LocationSampleActivity", "IO Exception in getFromLocation()");
							e1.printStackTrace();
						} catch (IllegalArgumentException e2) {
							// Error message to post in the log
							String errorString = "Illegal arguments " + Double.toString(latitude) + " , " + Double.toString(longitude) + " passed to address service";
							Log.e("LocationSampleActivity", errorString);
							e2.printStackTrace();
						}
						// If the reverse geocode returned an address
						if (addresses != null && addresses.size() > 0) {
							// Get the first address
							Address address = addresses.get(0);
							*/
/*
							 * Format the first line of address (if available),
							 * city, and country name.
							 *//*

							// setAdd(address);
							String addressText = String.format("%s, %s, %s",
							// If there's a street address, add it
									address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
									// Locality is usually a city
									address.getLocality(),
									// The country of the address
									address.getCountryName());
							// Return the text

							// String uid =
							// user.get(SessionManager.KEY_USER_ID);
							// String token =
							// user.get(SessionManager.KEY_TOKEN);
							PDFTrackerBean trakingBean = new PDFTrackerBean();
							trakingBean.setPdf_id(pdfBean.getPDF_ID());
							if (pageNumber > 0) {
								trakingBean.setPage(pageNumber + "");
							}
							trakingBean.setStarttime(starttime);
							DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Calendar cal = Calendar.getInstance();
							trakingBean.setEndtime(dateFormat.format(cal.getTime()));
							trakingBean.setInterval_sec(readingTime + "");
							trakingBean.setIp_address(latitude + "#" + longitude);
							if (pdfBean.getFILE_TYPE().equals("ebook")) {
								trakingBean.setFile_id(pdfBean.getPDF_LOGO());
							} else {
								trakingBean.setFile_id("0");
							}
							trakingBean.setAddress(address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "");
							trakingBean.setCountry(address.getCountryName());
							trakingBean.setCity(address.getLocality());
							trakingBean.setOffline(0);

							try {
								
								
								JSONObject obj = new JSONObject();
								obj.put("method", "TimeTrackOfReaders");
								obj.put("pdf_id", trakingBean.getPdf_id());
								obj.put("user_id", uid);
								obj.put("ip_address", latitude + "#" + longitude);
								obj.put("starttime", trakingBean.getStarttime());
								obj.put("country", trakingBean.getCountry()+"");
								obj.put("city", trakingBean.getCity()+"");
								obj.put("address", trakingBean.getAddress()+"");
								obj.put("page", trakingBean.getPage());
								obj.put("endtime", trakingBean.getEndtime());
								obj.put("interval_sec", trakingBean.getInterval_sec());
								obj.put("token", token);
								obj.put("file_id", trakingBean.getFile_id());
								System.out.println("sending obj ---->" + obj);
								WebService webapi = new WebService();
								webapi.postJsonWebApi(obj, getActivity());

								HttpClient client = new DefaultHttpClient();
								HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
																										// Limit
								HttpResponse response;
								JSONObject json = obj;
								try {
									HttpPost post = new HttpPost("http://medarkive.com/WebServices/index");
									// System.out.println("json =="+json.toString());
									StringEntity se = new StringEntity(json.toString());
									se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
									post.setEntity(se);
									// System.out.println("check 1");
									response = client.execute(post);
									*/
/* Checking response *//*

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
										System.out.println("result :----- " + sb.toString());
										if (json.length() > 0) {
											// status= true;
										} else {
											// status =false;
										}
									}
								} catch (Exception e) {
									// status =false;
									e.printStackTrace();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
						// Looper.loop(); // Loop in the message queue
					}
				};
				t.start();

			} else {
				
				trakingBean.setStarttime(starttime);
				DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar cal1 = Calendar.getInstance();
				trakingBean.setEndtime(dateFormat1.format(cal1.getTime()));
				trakingBean.setInterval_sec(readingTime + "");
				trakingBean.setIp_address(latitude + "#" + longitude);
				if (pdfBean.getFILE_TYPE().equals("ebook")) {
					trakingBean.setFile_id(pdfBean.getPDF_LOGO());
				} else {
					trakingBean.setFile_id("0");
				}
				trakingBean.setAddress("");
				trakingBean.setCountry("");
				trakingBean.setCity("");
				trakingBean.setOffline(1);
			}
			db.addPDFTrackedData(trakingBean);
			List<PDFTrackerBean> lstTrackerbeanOffline = db.getAllOFFLINEPDFTrackedData();
			List<PDFTrackerBean> lstTrackerbeanOnline = db.getAllonlinePDFTrackedData();
			for (PDFTrackerBean bean : lstTrackerbeanOffline) {
				System.out.println(" tracking id  ------->>" + bean.getTrack_id());
				System.out.println(" get city  ------->>" + bean.getCity());
				System.out.println(" country  ------->>" + bean.getCountry());
				System.out.println(" address ------->>" + bean.getAddress());
				System.out.println(" offline ------->>" + bean.getOffline());
				System.out.println(" get ip ------->>" + bean.getIp_address());
			}
			for (PDFTrackerBean bean : lstTrackerbeanOnline) {
				System.out.println(" tracking id  ------->>" + bean.getTrack_id());
				System.out.println(" get city  ------->>" + bean.getCity());
				System.out.println(" country  ------->>" + bean.getCountry());
				System.out.println(" address ------->>" + bean.getAddress());
				System.out.println(" offline ------->>" + bean.getOffline());
				System.out.println(" get ip ------->>" + bean.getIp_address());
			}
		}
	}

	//tracking data sending thread 
	final Handler handler = new Handler();
	Timer timer = new Timer(false);
	TimerTask timerTask = new TimerTask() {
		@Override
		public void run() {
			handler.post(new Runnable() {
				@Override
				public void run() {
					// Do whatever you want
					if (timerTask.scheduledExecutionTime() >= 20000) {
						sendTrackerData();
					}
				}
			});
		}
	};

	@Override
	public void performPickFor(FilePicker picker) {
		// mFilePicker = picker;
		Intent intent = new Intent(getActivity(), ChoosePDFActivity.class);
		intent.setAction(ChoosePDFActivity.PICK_KEY_FILE);
		startActivityForResult(intent, FILEPICK_REQUEST);
	}

	private class GetAddressTask extends AsyncTask<Address, Void, Address> {

		*/
/**
		 * Get a Geocoder instance, get the latitude and longitude look up the
		 * address, and return it
		 * 
		 * @params params One or more Location objects
		 * @return A string containing the address of the current location, or
		 *         an empty string if no address can be found, or an error
		 *         message
		 *//*

		@Override
		protected Address doInBackground(Address... params) {
			Geocoder geocoder = new Geocoder(getActivity());
			// Get the current location from the input parameter list
			Address loc = params[0];
			// Create a list to contain the result address
			List<Address> addresses = null;
			try {
				*/
/*
				 * Return 1 address.
				 *//*

				addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
			} catch (IOException e1) {
				Log.e("LocationSampleActivity", "IO Exception in getFromLocation()");
				e1.printStackTrace();
				return null;
			} catch (IllegalArgumentException e2) {
				// Error message to post in the log
				String errorString = "Illegal arguments " + Double.toString(loc.getLatitude()) + " , " + Double.toString(loc.getLongitude()) + " passed to address service";
				Log.e("LocationSampleActivity", errorString);
				e2.printStackTrace();
				return null;
			}
			// If the reverse geocode returned an address
			if (addresses != null && addresses.size() > 0) {
				// Get the first address
				Address address = addresses.get(0);
				*/
/*
				 * Format the first line of address (if available), city, and
				 * country name.
				 *//*

				// setAdd(address);
				String addressText = String.format("%s, %s, %s",
				// If there's a street address, add it
						address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
						// Locality is usually a city
						address.getLocality(),
						// The country of the address
						address.getCountryName());
				// Return the text
				return address;
			} else {
				return null;
			}
		}

		@Override
		protected void onPostExecute(Address result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// GetLocationAddressAsyncTask getAdd = new
			// GetLocationAddressAsyncTask();
			// Address address = new Address(null);
			// address.setLatitude(latitude);
			// address.setLongitude(longitude);
			// String addressstr = getAdd.getAddress(getActivity(), address);
			// System.out.println("Address ------->>" + addressstr);
			// Address add = getAdd.getAdd();
			String uid = user.get(SessionManager.KEY_USER_ID);
			String token = user.get(SessionManager.KEY_TOKEN);
			PDFTrackerBean trakingBean = new PDFTrackerBean();
			if (result != null) {
				trakingBean.setPdf_id(pdfBean.getPDF_ID());
				if (pageNumber > 0) {
					trakingBean.setPage(pageNumber + "");
				}
				trakingBean.setStarttime(starttime);
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				trakingBean.setEndtime(dateFormat.format(cal.getTime()));
				trakingBean.setInterval_sec(readingTime + "");
				trakingBean.setIp_address(latitude + "#" + longitude);
				if (pdfBean.getFILE_TYPE().equals("ebook")) {
					trakingBean.setFile_id(pdfBean.getPDF_LOGO());
				} else {
					trakingBean.setFile_id("0");
				}
				trakingBean.setAddress(result.getMaxAddressLineIndex() > 0 ? result.getAddressLine(0) : "");
				trakingBean.setCountry(result.getCountryName());
				trakingBean.setCity(result.getLocality());
				trakingBean.setOffline(0);

				try {
					JSONObject obj = new JSONObject();
					obj.put("method", "TimeTrackOfReaders");
					obj.put("pdf_id", trakingBean.getPdf_id());
					obj.put("user_id", uid);
					obj.put("ip_address", latitude + "#" + longitude);
					obj.put("starttime", trakingBean.getStarttime());
					obj.put("country", trakingBean.getCountry());
					obj.put("city", trakingBean.getCity());
					obj.put("address", trakingBean.getAddress());
					obj.put("page", trakingBean.getPage());
					obj.put("endtime", trakingBean.getEndtime());
					obj.put("interval_sec", trakingBean.getInterval_sec());
					obj.put("token", token);
					obj.put("file_id", trakingBean.getFile_id());
					System.out.println("sending obj ---->" + obj);
					WebService webapi = new WebService();
					webapi.postJsonWebApi(obj, getActivity());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
*/
