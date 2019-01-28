package com.medarkive.Fragments;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.medarkive.R;
import com.medarkive.Main.DisplayDataActivity;
import com.medarkive.Utilities.SessionManager;

public class CPDOptionFragment extends Fragment {

	String strCPDChioce;

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
		View view = inflater.inflate(R.layout.fragment_cpdoption, container,
				false);

		RelativeLayout rv = (RelativeLayout) view.findViewById(R.id.rv);
		new ASSL(getActivity(), rv, 1134, 720, false);

		final ImageView cpdUk = (ImageView) view
				.findViewById(R.id.cpd_uk_chck_box);
		final ImageView cpdInternational = (ImageView) view
				.findViewById(R.id.cpd_inter_check_box);
		RelativeLayout cpdUKLay = (RelativeLayout) view
				.findViewById(R.id.cpd_uk);
		RelativeLayout cpdInternationalLay = (RelativeLayout) view
				.findViewById(R.id.cpd_international);
		Button submit = (Button) view.findViewById(R.id.submit_cpd_choice);
		cpdUk.setBackgroundResource(R.drawable.ic_blue_circle);
		cpdInternational.setBackgroundResource(R.drawable.ic_blue_circle);
		final SessionManager sm = new SessionManager(getActivity());
		String cpdChoice = sm.getCPDChoice();
		if (cpdChoice != null) {
			strCPDChioce = cpdChoice;
			if (cpdChoice.contains("1")) {
				view.findViewById(R.id.cpd_uk_chck_box).setBackgroundResource(
						R.drawable.ic_blue_circle_checked);
			}
			if (cpdChoice.contains("2")) {
				view.findViewById(R.id.cpd_inter_check_box)
						.setBackgroundResource(
								R.drawable.ic_blue_circle_checked);
			}
		}
		cpdUKLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				strCPDChioce = "1";
				cpdUk.setBackgroundResource(R.drawable.ic_blue_circle_checked);
				cpdInternational
						.setBackgroundResource(R.drawable.ic_blue_circle);
			}
		});

		cpdInternationalLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				strCPDChioce = "2";
				cpdUk.setBackgroundResource(R.drawable.ic_blue_circle);
				cpdInternational
						.setBackgroundResource(R.drawable.ic_blue_circle_checked);
			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (strCPDChioce != null) {
					if (strCPDChioce.contains("1")) {
						Builder builder = new AlertDialog.Builder(getActivity());
						builder.setTitle("CPD Option Changed ✓");
						builder.setMessage("CPD LOG (UK) Selected");
						builder.setCancelable(true);
						builder.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										getActivity().getSupportFragmentManager().popBackStack();
										((DisplayDataActivity)getActivity()).changeTopBar(new EditProfileFragment(), "Edit Profile", ((DisplayDataActivity)getActivity()).homeTBar);
									}
								});
						AlertDialog dialog = builder.create();
						dialog.show();
//						Toast.makeText(getActivity(), "CPD LOG (UK) Selected",
//								Toast.LENGTH_SHORT).show();
					} else {
						Builder builder = new AlertDialog.Builder(getActivity());
						builder.setTitle("CPD Option Changed ✓");
						builder.setMessage("CPD LOG (International) Selected");
						builder.setCancelable(true);
						builder.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										getActivity().getSupportFragmentManager().popBackStack();
										((DisplayDataActivity)getActivity()).changeTopBar(new EditProfileFragment(), "Edit Profile", ((DisplayDataActivity)getActivity()).homeTBar);
									}
								});
						AlertDialog dialog = builder.create();
						dialog.show();
//						Toast.makeText(getActivity(),
//								"CPD LOG (International) Selected",
//								Toast.LENGTH_SHORT).show();
					}
					sm.setCPDChoice(strCPDChioce);
				}
			}
		});

		return view;
	}

}
