package com.medarkive.Utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.medarkive.R;

public class Functions {
	
	private static ProgressDialog progressDial;
	
	 public static int dpToPx(int dp, Context ctx) {
		    Resources r = ctx.getResources();
		    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
		}
		//originally: http://stackoverflow.com/questions/5418510/disable-the-touch-events-for-all-the-views
		//modified for the needs here
		public static void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
		    int childCount = viewGroup.getChildCount();
		    for (int i = 0; i < childCount; i++) {
		        View view = viewGroup.getChildAt(i);
		        if(view.isFocusable())
		            view.setEnabled(enabled);
		        if (view instanceof ViewGroup) {
		            enableDisableViewGroup((ViewGroup) view, enabled);
		            } else if (view instanceof ListView) {
		                if(view.isFocusable())
		                    view.setEnabled(enabled);
		                ListView listView = (ListView) view;
		                int listChildCount = listView.getChildCount();
		                for (int j = 0; j < listChildCount; j++) {
		                    if(view.isFocusable())
		                        listView.getChildAt(j).setEnabled(false);
		                    }
		                }
		        }
		    }
		
		public static void showLoadingDialog(Context contxt, String msg) {

			progressDial = new ProgressDialog(contxt,

			android.R.style.Theme_Translucent_NoTitleBar);

			// pd_st.getWindow().getAttributes().windowAnimations =

			// R.style.Animations_LoadingDialogFade;

			progressDial.show();

			WindowManager.LayoutParams layoutParams = progressDial.getWindow()

			.getAttributes();

			layoutParams.dimAmount = 0.6f;

			progressDial.getWindow().addFlags(

			WindowManager.LayoutParams.FLAG_DIM_BEHIND);

			progressDial.setCancelable(false);

			progressDial.setContentView(R.layout.loading_box);

			FrameLayout framLay = (FrameLayout) progressDial.findViewById(R.id.rv);

			// new ASSL((Activity)c, rv, 1134, 720, false);

			TextView dialogTxt = (TextView) progressDial.findViewById(R.id.tv101);

			dialogTxt.setText(msg);

		}
		
		public static void showLoadingWithoutDialog(Context contxt, String msg) {

			progressDial = new ProgressDialog(contxt,

			android.R.style.Theme_Translucent_NoTitleBar);

			// pd_st.getWindow().getAttributes().windowAnimations =

			// R.style.Animations_LoadingDialogFade;

			progressDial.show();

			WindowManager.LayoutParams layoutParams = progressDial.getWindow()

			.getAttributes();

//			layoutParams.dimAmount = 0.6f;

//			progressDial.getWindow().addFlags(
//
//			WindowManager.LayoutParams.FLAG_DIM_BEHIND);

			progressDial.setCancelable(false);

			progressDial.setContentView(R.layout.loading_box);
			progressDial.findViewById(R.id.rlt).setVisibility(8);

			FrameLayout framLay = (FrameLayout) progressDial.findViewById(R.id.rv);

			// new ASSL((Activity)c, rv, 1134, 720, false);

			TextView dialogTxt = (TextView) progressDial.findViewById(R.id.tv101);

			dialogTxt.setText(msg);

		}
		
		public static void dismissLoadingDialog() {

			if (progressDial != null) {

				progressDial.dismiss();

				progressDial = null;

			}

		}

}
