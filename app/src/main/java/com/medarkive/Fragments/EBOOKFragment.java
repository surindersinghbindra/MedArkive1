package com.medarkive.Fragments;

import java.util.ArrayList;
import java.util.Iterator;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.medarkive.R;
import com.medarkive.Adapter.CustomArrayAdapterForEbook;
import com.medarkive.Adapter.EbookPagerAdapter;
import com.medarkive.Beans.EbookBean;
import com.medarkive.Beans.EbookChapterBean;
import com.medarkive.Main.CustomViewPagerForEbook;
import com.medarkive.Main.DisplayDataActivity;
import com.medarkive.Main.LoginActivity;


public class EBOOKFragment extends Fragment {

	public static EbookPagerAdapter adapter;
	public static CustomViewPagerForEbook mPager;
	Fragment fr;
	CustomArrayAdapterForEbook chapterAdapter;
	private static int currentPage;
	private ImageView ebookChapter, backBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

		View rootView = inflater.inflate(R.layout.fragment_ebooklist, container, false);
		LinearLayout rv = (LinearLayout) rootView.findViewById(R.id.rv);
		new ASSL(getActivity(), rv, 1134, 720, false);

//		FrameLayout.LayoutParams relativeParams = (FrameLayout.LayoutParams) rv.getLayoutParams();
//		relativeParams.setMargins(0, (int) (96 * ASSL.Yscale()), 0, 0);
//		rv.setLayoutParams(relativeParams);
//
//		RelativeLayout topBar = (RelativeLayout) ((DisplayDataActivity) getActivity()).findViewById(R.id.ebook_top_bar);
//
//		RelativeLayout.LayoutParams relativeParams1 = (RelativeLayout.LayoutParams) topBar.getLayoutParams();
//		relativeParams1.setMargins(0, (int) (96 * ASSL.Yscale()), 0, 0);
//		topBar.setLayoutParams(relativeParams1);

		mPager = (CustomViewPagerForEbook) rootView.findViewById(R.id.pager);
		ebookChapter = (ImageView) getActivity().findViewById(R.id.chapter_img);
		backBtn = (ImageView) getActivity().findViewById(R.id.back_to_home_btn);

		final ArrayList<EbookBean> lst = getArguments().getParcelableArrayList(LoginActivity.Extra_Value);
		Log.v("List Size : -", lst.size() + "");
		adapter = new EbookPagerAdapter(getActivity().getSupportFragmentManager(), lst, getActivity());
		mPager.setAdapter(adapter);
		PageListener ebookPageListener = new PageListener();
		mPager.setOnPageChangeListener(ebookPageListener);
		ebookChapter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int[] location = new int[2];
				v.getLocationOnScreen(location);
				// showStatusPopup(HomePage.this, location);
				itemPopUp(getActivity(), location);
			}
		});
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((DisplayDataActivity) getActivity()).backBtnEbook();
			}
		});
		getActivity().findViewById(R.id.download_vid).setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				((DisplayDataActivity) getActivity()).downloadvideo(lst.get(0).getPdf_id() + "");

			}
		});

		getActivity().findViewById(R.id.home_img).setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				//((MuPDFFragment) ((EbookPagerAdapter) mPager.getAdapter()).instantiateItem(mPager, currentPage)).gotoPage(0);
			}
		});
		ArrayList<EbookChapterBean> localArrayList2 = new ArrayList<EbookChapterBean>();
		Iterator<EbookBean> localIterator = lst.iterator();

		while (true) {
			if (!localIterator.hasNext()) {
				this.chapterAdapter = new CustomArrayAdapterForEbook(getActivity(), android.R.layout.simple_list_item_1, localArrayList2, getResources());
				break;
			}
			EbookBean localEbookBean = (EbookBean) localIterator.next();
			EbookChapterBean localEbookChapterBean = new EbookChapterBean();
			localEbookChapterBean.setChapterTitle(localEbookBean.getChapter());
			localEbookChapterBean.setChapterName(localEbookBean.getChapter_title());
			localEbookChapterBean.setChapterId(localEbookBean.getId());
			localArrayList2.add(localEbookChapterBean);
		}
		return rootView;
	}

	// class CustomTouchListner implements OnTouchListener{
	//
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// if (getScale() > 1f) {
	// getParent().requestDisallowInterceptTouchEvent(true);
	// } else {
	// getParent().requestDisallowInterceptTouchEvent(false);
	// }
	// return false;
	// }
	//
	// }
	public void itemPopUp(Activity activity, int p[]) {
		final Dialog chapterListDialog = new Dialog(activity);
		chapterListDialog.requestWindowFeature(1);
		WindowManager.LayoutParams localLayoutParams = chapterListDialog.getWindow().getAttributes();
		localLayoutParams.gravity = 51;
		localLayoutParams.x = (int) (p[0] * ASSL.Xscale());
		localLayoutParams.y = (int) (p[1] + 100.0F * ASSL.Yscale());
		localLayoutParams.height = (int) (500.0F * ASSL.Xscale());
		localLayoutParams.width = (int) (400.0F * ASSL.Yscale());
		chapterListDialog.getWindow().getAttributes().dimAmount = 0.0F;
		chapterListDialog.getWindow().addFlags(2);
		chapterListDialog.setCancelable(false);
		chapterListDialog.setCanceledOnTouchOutside(true);
		chapterListDialog.setContentView(R.layout.custom_chapter_list_dialog);
		new ASSL(activity, (LinearLayout) chapterListDialog.findViewById(R.id.rv), 1134, 720, false);
		chapterListDialog.show();
		ListView localListView = (ListView) chapterListDialog.findViewById(R.id.chapter_list);
		localListView.setAdapter(chapterAdapter);
		localListView.setOnItemClickListener((new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				EBOOKFragment.mPager.setCurrentItem(position);
				chapterListDialog.cancel();
			}
		}));
	}

	private class PageListener extends ViewPager.SimpleOnPageChangeListener {
		private PageListener() {
		}

		public void onPageSelected(int position) {
			Log.i("Ebook Pager:", "page selected " + position);
			currentPage = position;
		}
	}
}
