package com.medarkive.Adapter;

import java.util.ArrayList;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.medarkive.R;
import com.medarkive.Main.DisplayDataActivity;
import com.medarkive.Utilities.DatabaseHandler;
import com.medarkive.Utilities.SessionManager;

@SuppressWarnings("unchecked")
public class CustomAdapterForNavDrawer extends BaseExpandableListAdapter {

	public ArrayList<String> groupItem, tempChild;
	public ArrayList<Object> childtem = new ArrayList<Object>();
	public LayoutInflater minflater;
	public Activity activity;
	private final Context context;
	ViewHolder viewholder;
	private LayoutInflater inflator;

	public CustomAdapterForNavDrawer(Context context, ArrayList<String> grList,
			ArrayList<Object> childItem) {
		this.context = context;
		this.groupItem = grList;
		this.childtem = childItem;
		inflator = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setInflater(LayoutInflater mInflater, Activity act) {
		this.minflater = mInflater;
		activity = act;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		AssetManager assetManagerlight = context.getAssets();
		Typeface tfGillSansLight = Typeface.createFromAsset(assetManagerlight,
				"gill-sans-light.ttf");

		tempChild = (ArrayList<String>) childtem.get(groupPosition);

		if (convertView == null) {
			convertView = inflator
					.inflate(R.layout.nav_drawer_child_item, null);
			viewholder = new ViewHolder();
			viewholder.childTxtView = (TextView) convertView
					.findViewById(R.id.child_item);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		// TextView text = null;
		viewholder.childTxtView.setTag(viewholder);
		// viewholder.container.setTag(viewholder);

		viewholder.childTxtView.setText(tempChild.get(childPosition));
		// viewholder.childTxtView.setPadding(70, 16, 0, 0);
		// viewholder.childTxtView.setTextSize(18);
		viewholder.childTxtView.setTextColor(Color.parseColor("#FEFEFF"));
		viewholder.childTxtView.setClickable(true);
		viewholder.childTxtView.setTypeface(tfGillSansLight);
		// System.out.println("Child------>>>>>>>" + text.getText());
		viewholder.childTxtView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (childPosition) {
				case 0:
					// DisplayDataActivity aa = new DisplayDataActivity();
					((DisplayDataActivity) context).edit_profile();

					break;

				case 1:
					SessionManager sm = new SessionManager(v.getContext());
					sm.logoutUser();
					DatabaseHandler db = new DatabaseHandler(v.getContext());
					db.deleteAllTable();
					break;

				}
			}
		});
		
		viewholder.rv.setLayoutParams(new ListView.LayoutParams(720, 150));
		ASSL.DoMagic(viewholder.rv);
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<String>) childtem.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return groupItem.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View view,
			ViewGroup parent) {

		if (view == null) {
			view = inflator.inflate(R.layout.nav_drawer_list_item, null);
			viewholder = new ViewHolder();
			viewholder.rv = (LinearLayout) view.findViewById(R.id.rv);
			viewholder.txtview = (TextView) view
					.findViewById(R.id.nav_drawer_txt);
			viewholder.icon = (ImageView) view.findViewById(R.id.myarkiveIcon);
			view.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) view.getTag();
		}

		viewholder.txtview.setTag(viewholder);
		viewholder.rv.setTag(viewholder);

		viewholder.txtview.setText(groupItem.get(groupPosition));

		// TextView viewholder.txtview;
		// viewholder.txtview = (TextView) viewholder.txtview;
		// int padding =50;
		// int left =20;
		// int right =10;
		// int top =20;
		// viewholder.txtview.setCompoundDrawablePadding(padding);
		viewholder.txtview.setTextColor(Color.parseColor("#FEFEFF"));
		AssetManager assetManager = context.getAssets();
		Typeface tf = Typeface.createFromAsset(assetManager, "gill-sans.ttf");
		viewholder.txtview.setTypeface(tf);
		viewholder.txtview.setText(groupItem.get(groupPosition));
		switch (groupPosition) {

		case 0:
			viewholder.icon.setBackgroundResource(R.drawable.ic_nav_home);
			// viewholder.txtview.setCompoundDrawablesWithIntrinsicBounds(0
			// ,R.drawable.ic_nav_home, 0, 0);
			break;

		case 1:
			viewholder.icon.setBackgroundResource(R.drawable.ic_nav_cpd);
			// viewholder.txtview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_nav_cpd,0,
			// 0, 0);
			break;

		case 2:
			viewholder.icon.setBackgroundResource(R.drawable.ic_nav_www);
			// viewholder.txtview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_nav_www,0,
			// 0, 0);
			break;

		case 3:
			viewholder.icon.setBackgroundResource(R.drawable.ic_nav_myarkive);
			// viewholder.txtview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_nav_myarkive,0,
			// 0, 0);
			break;

		case 4:
			viewholder.icon.setBackgroundResource(R.drawable.ic_nav_dashboard);
			// viewholder.txtview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_nav_dashboard,0,
			// 0, 0);
			break;

		case 5:
			viewholder.icon.setBackgroundResource(R.drawable.ic_nav_setting);
			// viewholder.txtview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_nav_setting,0,
			// 0, 0);
			break;

		case 6:
			viewholder.icon.setBackgroundResource(R.drawable.ic_nav_feedback);
			// viewholder.txtview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_nav_feedback,0,
			// 0, 0);
			break;
		}

		viewholder.rv.setLayoutParams(new ListView.LayoutParams(720, 150));
		ASSL.DoMagic(viewholder.rv);
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	public static class ViewHolder {
		public LinearLayout rv;
		public TextView txtview;
		public TextView childTxtView;
		public ImageView icon;
	}
}
