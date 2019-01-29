package com.medarkive.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.medarkive.Beans.EbookBean;
import com.medarkive.Beans.PDFBean;
import com.medarkive.Main.DisplayDataActivity;
import com.medarkive.Main.MuPDFFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("serial")
public class EbookPagerAdapter extends FragmentStatePagerAdapter implements Serializable {
	ArrayList<EbookBean> list;
	FragmentManager fm;
	//static MuPDFFragment fr;
	Context mContext;
	private Map<Integer, String> mFragmentTags;

	public EbookPagerAdapter(FragmentManager fm, ArrayList<EbookBean> pdfList, Context paramContext) {
		super(fm);
		this.fm = fm;
		list = pdfList;
		this.mContext = paramContext;
		this.mFragmentTags = new HashMap<Integer, String>();
	}

	@Override
	public float getPageWidth(int position) {
		return 1.0f;
	}

	public Fragment getFragment(int i) {

		String str = (String) this.mFragmentTags.get(Integer.valueOf(i));
		if (str == null)
			return null;
		return this.fm.findFragmentByTag(str);
	}

	public FragmentManager getFragmentMgr() {
		return this.fm;
	}

	@Override
	public Fragment getItem(int paramInt) {
		EbookBean localEbookBean = (EbookBean) this.list.get(paramInt);
		Bundle localBundle = new Bundle();
		PDFBean localPDFBean = new PDFBean();
		localPDFBean.setPDF_FILE(localEbookBean.getFile_name());
		localPDFBean.setPDF_ID(localEbookBean.getPdf_id());
		localPDFBean.setPDF_TITLE(localEbookBean.getPdf_name());
		localPDFBean.setPDF_LOGO(localEbookBean.getId());
		localPDFBean.setFILE_TYPE("ebook");
		localBundle.putSerializable(DisplayDataActivity.EXTRA_URL, localPDFBean);
		return Fragment.instantiate(this.mContext, MuPDFFragment.class.getName(), localBundle);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object instantiateItem(ViewGroup paramViewGroup, int paramInt) {
		Object localObject = super.instantiateItem(paramViewGroup, paramInt);
		if ((localObject instanceof Fragment)) {
			// commented by SMIT
			String str = ((MuPDFFragment) localObject).getTag();
			this.mFragmentTags.put(Integer.valueOf(paramInt), str);
		}
		return localObject;
	}
}
