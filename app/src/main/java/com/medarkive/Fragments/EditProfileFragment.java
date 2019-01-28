package com.medarkive.Fragments;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.medarkive.R;
import com.medarkive.Main.DisplayDataActivity;

public class EditProfileFragment extends Fragment {


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
		View v= inflater.inflate(R.layout.fragment_edit_profile, container , false);
		
		RelativeLayout rv = (RelativeLayout) v.findViewById(R.id.rv);
		new ASSL(getActivity(), rv, 1134, 720, false);
//		((DisplayDataActivity)getActivity()).view.setVisibility(8);
//		((DisplayDataActivity)getActivity()).changeTopBar("Edit Profile" , ((DisplayDataActivity)getActivity()).homeTBar);
		
		 v.findViewById(R.id.edit_name).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((DisplayDataActivity)getActivity()).editName("Edit Name");
			}
		});
		 v.findViewById(R.id.change_password).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((DisplayDataActivity)getActivity()).editName("Change Password");
				}
			});
		 v.findViewById(R.id.cpd_choice).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((DisplayDataActivity)getActivity()).editName("Select CPD");
				}
			});
		 
//		 v.findViewById(R.id.edit_name_frwd).setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					((DisplayDataActivity)getActivity()).editName("Edit Name");
//				}
//			});
//		 v.findViewById(R.id.change_password_frwd).setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					((DisplayDataActivity)getActivity()).editName("Change Password");
//				}
//			});
//		 v.findViewById(R.id.cpd_choice_frwd).setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					((DisplayDataActivity)getActivity()).editName("Select CPD");
//				}
//			});
		 
		 return v;
	}

}
