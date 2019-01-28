package com.medarkive.Fragments;

import java.util.ArrayList;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.medarkive.R;
import com.medarkive.Adapter.MyArkiveListAdapter;
import com.medarkive.Beans.MyArkiveUrls;

public class MyArkiveFragment extends android.support.v4.app.ListFragment {

	private final String dropbox_link = "https://www.dropbox.com/login";
	private final String googleDrive_link = "https://drive.google.com";
	private final String myDrive_link = "https://onedrive.live.com/about/en-us/";
	ArrayList<MyArkiveUrls> urlList;

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
		View view = inflater.inflate(R.layout.fragment_my_arkive, container,
				false);
		dataIn();
		
		RelativeLayout rv = (RelativeLayout) view.findViewById(R.id.rv);
		
		new ASSL(getActivity(), rv, 1134, 720, false);
		
		MyArkiveListAdapter adapter = new MyArkiveListAdapter(getActivity() , urlList);
		setListAdapter(adapter);
		
//		((DisplayDataActivity)getActivity()).customActionBarEditProfile("MyArkive");
		
		
//		view.findViewById(R.id.dropbox).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				((DisplayDataActivity)getActivity()).dropboxClicked();
//			}
//		});
//		view.findViewById(R.id.google_drive).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				((DisplayDataActivity)getActivity()).googleDriveClicked();
//			}
//		});
//		view.findViewById(R.id.my_drive).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				((DisplayDataActivity)getActivity()).myDriveClicked();
//			}
//		});
//		view.findViewById(R.id.dropbox_frwd).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				((DisplayDataActivity)getActivity()).dropboxClicked();
//			}
//		});
//		view.findViewById(R.id.google_drive_frwd).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				((DisplayDataActivity)getActivity()).googleDriveClicked();
//			}
//		});
//		view.findViewById(R.id.my_drive_frwd).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				((DisplayDataActivity)getActivity()).myDriveClicked();
//			}
//		});
		
		return view;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_my_arkive,
					container, false);
			return rootView;
		}
	}
	
	
	public void dataIn(){
	    urlList = new ArrayList<MyArkiveUrls>();
		MyArkiveUrls urls = new MyArkiveUrls();
		urls.setlinkName("Dropbox");
		urls.setlink(dropbox_link);
		urls.setIcon(getActivity().getResources().getDrawable(R.drawable.ic_dropbox));
		MyArkiveUrls urls1 = new MyArkiveUrls();
		urls1.setlinkName("Google Drive");
		urls1.setlink(googleDrive_link);
		urls1.setIcon(getActivity().getResources().getDrawable(R.drawable.ic_google));
		MyArkiveUrls urls2 = new MyArkiveUrls();
		urls2.setlinkName("OneDrive");
		urls2.setlink(myDrive_link);
		urls2.setIcon(getActivity().getResources().getDrawable(R.drawable.ic_onedrive));
		MyArkiveUrls urls3 = new MyArkiveUrls();
		urls3.setlinkName("MedScape");
		urls3.setlink("http://Medscape.com");
		urls3.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls4 = new MyArkiveUrls();
		urls4.setlinkName("BMC");
		urls4.setlink("http://www.biomedcentral.com/");
		urls4.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		
		MyArkiveUrls urls23= new MyArkiveUrls();
		urls23.setlinkName("Virtually free");
		urls23.setlink("http://virtually-free.com/");
		urls23.setIcon(getActivity().getResources().getDrawable(R.drawable.ic_virtually));
		
		MyArkiveUrls urls5 = new MyArkiveUrls();
		urls5.setlinkName("BMJ Journals");
		urls5.setlink("http://journals.bmj.com/");
		urls5.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls6 = new MyArkiveUrls();
		urls6.setlinkName("BMJ News");
		urls6.setlink("http://www.bmj.com/uk/news");
		urls6.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls7 = new MyArkiveUrls();
		urls7.setlinkName("NEJM");
		urls7.setlink("http://www.nejm.org/");
		urls7.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls8 = new MyArkiveUrls();
		urls8.setlinkName("Lancet");
		urls8.setlink("http://www.thelancet.com/home");
		urls8.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls9 = new MyArkiveUrls();
		urls9.setlinkName("Evidence Search");
		urls9.setlink("https://www.evidence.nhs.uk/");
		urls9.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls10 = new MyArkiveUrls();
		urls10.setlinkName("PubMed");
		urls10.setlink("http://www.ncbi.nlm.nih.gov/pubmed");
		urls10.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls11 = new MyArkiveUrls();
		urls11.setlinkName("PLOS");
		urls11.setlink("http://www.plosone.org/");
		urls11.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls12 = new MyArkiveUrls();
		urls12.setlinkName("Wiley OA");
		urls12.setlink("http://www.wileyopenaccess.com/view/index.html");
		urls12.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls14 = new MyArkiveUrls();
		urls14.setlinkName("Elsevier OA");
		urls14.setlink("http://www.elsevier.com/about/open-access/open-access-journa" +
				"ls");
		urls14.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls13 = new MyArkiveUrls();
		urls13.setlinkName("Cochrane");
		urls13.setlink("http://www.cochrane.org/cochrane-reviews");
		urls13.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls15 = new MyArkiveUrls();
		urls15.setlinkName("Academia");
		urls15.setlink("https://www.academia.edu/");
		urls15.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls16 = new MyArkiveUrls();
		urls16.setlinkName("Merck Manuals");
		urls16.setlink("http://www.merckmanuals.com/professional/index.html");
		urls16.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls17 = new MyArkiveUrls();
		urls17.setlinkName("Dove");
		urls17.setlink("http://www.dovepress.com/");
		urls17.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls18 = new MyArkiveUrls();
		urls18.setlinkName("Bentham Open");
		urls18.setlink("http://benthamopen.com/");
		urls18.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls19 = new MyArkiveUrls();
		urls19.setlinkName("Medknow");
		urls19.setlink("http://www.medknow.com/");
		urls19.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls20 = new MyArkiveUrls();
		urls20.setlinkName("Hindawi");
		urls20.setlink("http://www.hindawi.com/");
		urls20.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls21 = new MyArkiveUrls();
		urls21.setlinkName("Libertas Academica");
		urls21.setlink("http://www.la-press.com/");
		urls21.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		MyArkiveUrls urls22 = new MyArkiveUrls();
		urls22.setlinkName("Science Based Medicine");
		urls22.setlink("http://www.sciencebasedmedicine.org/");
		urls22.setIcon(getActivity().getResources().getDrawable(R.drawable.browser));
		
		urlList.add(urls);
		urlList.add(urls1);
		urlList.add(urls2);
		urlList.add(urls3);
		urlList.add(urls4);
		urlList.add(urls23);
		urlList.add(urls5);
		urlList.add(urls6);
		urlList.add(urls7);
		urlList.add(urls8);
		urlList.add(urls9);
		urlList.add(urls10);
		urlList.add(urls11);
		urlList.add(urls12);
		urlList.add(urls13);
		urlList.add(urls14);
		urlList.add(urls15);
		urlList.add(urls16);
		urlList.add(urls17);
		urlList.add(urls18);
		urlList.add(urls19);
		urlList.add(urls20);
		urlList.add(urls21);
		urlList.add(urls22);
	}
}
