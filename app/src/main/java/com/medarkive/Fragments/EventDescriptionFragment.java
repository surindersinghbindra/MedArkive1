package com.medarkive.Fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medarkive.R;
import com.medarkive.Beans.CPDBean;
import com.medarkive.Main.DisplayDataActivity;
import com.medarkive.Main.LoginActivity;
import com.medarkive.Utilities.ConnectionDetector;
import com.medarkive.Utilities.EventManager;
import com.medarkive.Utilities.Item;
import com.medarkive.Utilities.SessionManager;

public class EventDescriptionFragment extends Fragment {
	int mYear = 0;
	int mMonth = 0;
	int mDay = 0;
	private TextView clinical;
	private TextView nonClinical;

	private String created;
	private EditText description;
	private String selectedClinical = "0";
	private TextView credit;
	private ArrayList<CPDBean> arrList;
	private String category_id;
	private String type_id = "0";
	private TextView heading;
	private Button saveEvent;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(
				R.layout.fragment_event_description, container, false);
		
		RelativeLayout rv = (RelativeLayout) rootView.findViewById(R.id.rv);
		new ASSL(getActivity(), rv, 1134, 720, false);
		
		AssetManager assetManagerlight = getActivity().getAssets();
		Typeface tfGillSansLight = Typeface.createFromAsset(assetManagerlight,"gill-sans-light.ttf");
		
		AssetManager assetManager = getActivity().getAssets();
		Typeface tfGillSans = Typeface.createFromAsset(assetManager,"gill-sans.ttf");

		credit = (TextView) rootView.findViewById(R.id.credit_text);
		description = (EditText) rootView.findViewById(R.id.description);
		heading = (TextView) rootView.findViewById(R.id.headline);
		clinical = (TextView) rootView.findViewById(R.id.clinical);
		nonClinical = (TextView) rootView.findViewById(R.id.non_clinical);
		saveEvent = (Button) rootView.findViewById(R.id.save_event);
		final TextView txtDate = (TextView) rootView
				.findViewById(R.id.date_selector);
		txtDate.setTypeface(tfGillSansLight);
		if (getArguments().containsKey(
				EventDetailsListFragment.EVENT_DESCRIPTION)) {
			CPDBean eventDescriptionObj = getArguments().getParcelable(
					EventDetailsListFragment.EVENT_DESCRIPTION);
			Log.v("Tag credit  ===", eventDescriptionObj.getCredit().toString()+",");
			Log.v("Tag   ===", eventDescriptionObj.getName().toString()+",");
			Log.v("Tag   ===", eventDescriptionObj.getCreated().toString()+",");
			Log.v("Tag   ===", eventDescriptionObj.getClinical().toString()+",");
			
			saveEvent.setVisibility(Button.GONE);
			saveEvent.setTypeface(tfGillSans);
			credit.setText(eventDescriptionObj.getCredit());
			credit.setKeyListener(null);
			credit.setTypeface(tfGillSansLight);
			description.setText(eventDescriptionObj.getEvent_description());
			description.setKeyListener(null);
			description.setTypeface(tfGillSansLight);
			heading.setTypeface(tfGillSans);
			heading.setText(eventDescriptionObj.getName());
			txtDate.setText(eventDescriptionObj.getCreated());
			if (eventDescriptionObj.getClinical().contains("false")) {
//				clinical.setBackgroundResource(R.drawable.ic_clinical_on);
				clinical.setBackgroundColor(Color.parseColor("#58C5BC"));
				clinical.setTextColor(Color.parseColor("#8688A5"));
//				clinical.setBackground(getActivity().getResources().getDrawable());
				clinical.setTag("on");
				
//				nonClinical.setBackgroundResource(R.drawable.ic_non_clinical_off);//(getActivity().getResources().getDrawable(R.drawable.ic_non_clinical_off));
				nonClinical.setBackgroundColor(Color.parseColor("#8688A5"));
				nonClinical.setTextColor(Color.parseColor("#58C5BC"));
				nonClinical.setTag("off");
			} else if (eventDescriptionObj.getClinical().contains("true")) {
				
				clinical.setTag("off");
//				clinical.setImageResource(R.drawable.ic_clinical_off);
				clinical.setBackgroundColor(Color.parseColor("#8688A5"));
				clinical.setTextColor(Color.parseColor("#58C5BC"));
				nonClinical.setTag("on");
//				nonClinical.setImageResource(R.drawable.ic_non_clinical_on);
				nonClinical.setBackgroundColor(Color.parseColor("#58C5BC"));
				nonClinical.setTextColor(Color.parseColor("#8688A5"));
				
//				clinical.setBackgroundResource(R.drawable.ic_clinical_off);
//				clinical.setTag("off");
//				nonClinical.setBackgroundResource(R.drawable.ic_non_clinical_on);
//				nonClinical.setTag("on");
			}
		} else {
			((DisplayDataActivity) getActivity()).setCustomActionBarBackButton("Add Event");

			String title = "";
			if (getArguments().containsKey(LoginActivity.Extra_Value)) {

				arrList = getArguments().getParcelableArrayList(
						LoginActivity.Extra_Value);
				for (CPDBean bean : arrList) {
					category_id = bean.getCpd_id();
					title = bean.getName();
				}
				heading.setText(title);
			} else if (getArguments().containsKey(EvetTypeListFragment.TYPE_ID)) {
				Item item = getArguments().getParcelable(
						EvetTypeListFragment.TYPE_ID);
				type_id = item.getPosition() + "";
				title = item.getText();
				heading.setText(title);
				arrList = getArguments().getParcelableArrayList(
						EvetTypeListFragment.ARRAY_LIST_DATA);
				for (CPDBean bean : arrList) {
					if (bean.getCpd_type_id().equals(type_id)) {
						category_id = bean.getCpd_id();
						System.out.println("category_id---" + bean.getCpd_id());
						title = bean.getName();
					}
				}
			}

			final Calendar c = Calendar.getInstance();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);

			txtDate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DatePickerDialog dpd = new DatePickerDialog(getActivity(),
							new DatePickerDialog.OnDateSetListener() {

								@Override
								public void onDateSet(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									txtDate.setText(year + "-"
											+ (monthOfYear + 1) + "-"
											+ dayOfMonth);
									created = year + "-" + (monthOfYear + 1)
											+ "-" + dayOfMonth;
								}
							}, mYear, mMonth, mDay);
					dpd.show();
				}
			});

			clinical.setBackgroundColor(Color.parseColor("#58C5BC"));
			clinical.setTextColor(Color.parseColor("#8688A5"));
			clinical.setTag("on");
			nonClinical.setBackgroundColor(Color.parseColor("#8688A5"));
			nonClinical.setTextColor(Color.parseColor("#58C5BC"));
			nonClinical.setTag("off");
			clinical.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					clinical.setImageResource(R.drawable.ic_clinical_on);
					clinical.setBackgroundColor(Color.parseColor("#58C5BC"));
					clinical.setTextColor(Color.parseColor("#8688A5"));
					
					clinical.setTag("on");
					
					nonClinical.setBackgroundColor(Color.parseColor("#8688A5"));
					nonClinical.setTextColor(Color.parseColor("#58C5BC"));
//					nonClinical
//							.setImageResource(R.drawable.ic_non_clinical_off);
					nonClinical.setTag("off");
					selectedClinical = "0";
				}
			});

			nonClinical.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					clinical.setTag("off");
//					clinical.setImageResource(R.drawable.ic_clinical_off);
					clinical.setBackgroundColor(Color.parseColor("#8688A5"));
					clinical.setTextColor(Color.parseColor("#58C5BC"));
					nonClinical.setTag("on");
//					nonClinical.setImageResource(R.drawable.ic_non_clinical_on);
					nonClinical.setBackgroundColor(Color.parseColor("#58C5BC"));
					nonClinical.setTextColor(Color.parseColor("#8688A5"));
					selectedClinical = "1";
				}
			});

			rootView.findViewById(R.id.save_event).setOnClickListener(
					new OnClickListener() {

						@SuppressWarnings("deprecation")
						@Override
						public void onClick(View v) {
							ConnectionDetector cd = new ConnectionDetector(getActivity());
							if(!cd.isConnectedToInternet()){
								cd.showAlertDialog(false);
							}else{
							AlertDialog alertDialog;
							String date = txtDate.getText().toString();
							String creditVal = credit.getText().toString();
							String descVal = description.getText().toString();
							if (date.equals("Select Date here") || creditVal.length() == 0
									|| descVal.length() == 0 ) {
								AlertDialog.Builder builder = new AlertDialog.Builder(
										getActivity());
								builder.setTitle("Information Missing!!!");
								alertDialog = builder.create();
								alertDialog
										.setMessage("All fields are mendatory. Please fill through all fields");
								alertDialog.setButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												// Write your code here to
												// execute after dialog closed
												// Toast.makeText(getApplicationContext(),
												// "You clicked on OK",
												// Toast.LENGTH_SHORT).show();
											}
										});
								alertDialog.show();

							} else if(creditVal.equals(".")){
								AlertDialog.Builder builder = new AlertDialog.Builder(
										getActivity());
								builder.setTitle("Wrong Information!!!");
								alertDialog = builder.create();
								alertDialog
										.setMessage("Please provide a valid value in credit feild.");
								alertDialog.setButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
											}
										});
								alertDialog.show();
							}else{
								SessionManager sm = new SessionManager(
										getActivity());
								HashMap<String, String> userDetail = sm
										.getUserDetails();
								final String uid = userDetail
										.get(SessionManager.KEY_USER_ID);
								final String cpdChoice = sm.getCPDChoice();
								// System.out.println("tag ======"
								// + mAdapter.getItem(position).getPosition());

								AlertDialog.Builder adb = new AlertDialog.Builder(
										getActivity());

								adb.setTitle("Would you like to save this Event");

								adb.setIcon(android.R.drawable.ic_dialog_alert);

								adb.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												final JSONObject eventDescriptionToSave = new JSONObject();
												try {
													System.out
															.println("category ID---"
																	+ category_id);
													eventDescriptionToSave.put(
															"method",
															"add_cpd_event");
													eventDescriptionToSave.put(
															"user_id", uid);
													eventDescriptionToSave.put(
															"cpd_user_type",
															cpdChoice);
													eventDescriptionToSave.put(
															"category_id",
															category_id);
													eventDescriptionToSave.put(
															"clinical",
															selectedClinical);
													eventDescriptionToSave.put(
															"created", created);
													eventDescriptionToSave.put(
															"credit",
															credit.getText());
													eventDescriptionToSave.put(
															"description",
															description
																	.getText());
													eventDescriptionToSave
															.put(EvetTypeListFragment.TYPE_ID,
																	type_id);
													System.out
															.println("json to snd ====="
																	+ eventDescriptionToSave);
													EventManager saveEvent = new EventManager();
													JSONObject oo =saveEvent.saveEventDetail(eventDescriptionToSave,getActivity(),category_id);
													
												} catch (JSONException e) {
													// TODO Auto-generated catch
													// block
													Toast.makeText(getActivity(),
															"Event Saving Failed Please try again",
															Toast.LENGTH_SHORT)
															.show();
													e.printStackTrace();
												}
												Toast.makeText(getActivity(),
														"Event Saved",
														Toast.LENGTH_SHORT)
														.show();
											}
										});

								adb.setNegativeButton("Cancel",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {

												// finish();
											}
										});
								adb.show();
								}
							}
						}
					});
		}
		return rootView;
	}

}
