package com.medarkive.Adapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medarkive.R;
import com.medarkive.Beans.BookmarkBean;
import com.medarkive.Main.LoginActivity;
import com.medarkive.Utilities.BookmarkManager;
import com.medarkive.Utilities.Functions;

public class CustomArrayListAdapterForBookmark extends
		ArrayAdapter<BookmarkBean> {

	private Context context;
	private ArrayList<BookmarkBean> arr;
	ViewHolder holder;
	String bookmarkId ;
	int position1;

	public CustomArrayListAdapterForBookmark(Context context,
			ArrayList<BookmarkBean> arr) {
		super(context, R.layout.activity_tracker, arr);

		this.context = context;
		this.arr = arr;
	}

	static class ViewHolder {
		public TextView bookmsrkTitle, bookmarkLink;
		public RelativeLayout rv;
		public LinearLayout deleteBookmark;
		public String id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.bookmark_list_row, parent,
					false);
			holder = new ViewHolder();
			holder.bookmsrkTitle = (TextView) convertView.findViewById(R.id.bookmark_title_txt);
			holder.bookmarkLink = (TextView) convertView.findViewById(R.id.bookmark_link_txt);
			holder.rv = (RelativeLayout) convertView.findViewById(R.id.rv);
			holder.deleteBookmark = (LinearLayout)convertView.findViewById(R.id.delete_bookmark);
			
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.bookmsrkTitle.setText(arr.get(position).getBookmark_title());

		holder.bookmarkLink.setText(arr.get(position).getBookmark_link());
		final int pos = position;

		holder.deleteBookmark.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
//						System.out.println("Size of array before delete ==0"
//								+ arr.size());
						// set title
						alertDialogBuilder.setTitle("Bookmarks");

						// set dialog message
						alertDialogBuilder
								.setMessage("Click yes to remove!")
								.setCancelable(true)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {

											@SuppressLint("ShowToast")
											@Override
											public void onClick(
													DialogInterface dialog,
													int id) {
												// if this button is clicked,
												// close
												// current activity
												final JSONObject obj = new JSONObject();
												try {
													obj.put("method",
															"delete_bookmark");
													obj.put("bookmark_id", arr
															.get(pos)
															.getBookmark_id());
													
													bookmarkId = arr.get(pos).getBookmark_id();
													position1 = pos;
													GetBookmarkes gb = new GetBookmarkes();
													gb.execute();

//													BookmarkManager bookMgr = new BookmarkManager(
//															obj, context);
//													String str = bookMgr
//															.deletBookmark();
//													// System.out.println("test 111");
//													JSONObject oo = bookMgr
//															.getJsonObj();

													

												} catch (Exception e) {
													e.printStackTrace();
												}
											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int id) {
												// if this button is clicked,
												// just close
												// the dialog box and do nothing
												dialog.cancel();

											}
										});
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
						// show it
						alertDialog.show();
					}
				});
//		holder.id =arr.get(position).getBookmark_id();
//		convertView.setTag(arr.get(position).getBookmark_id());
		return convertView;
	}

	public class GetBookmarkes extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String result = "fail";
			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams
					.setConnectionTimeout(client.getParams(), 10000);
			HttpResponse response;
			JSONObject json = null;
			try {
//				SessionManager sm = new SessionManager(DisplayDataActivity.this);
//				HashMap<String, String> user = sm.getUserDetails();
//				String uid = user.get(SessionManager.KEY_USER_ID);
				final JSONObject obj = new JSONObject();
				
				obj.put("method",
						"delete_bookmark");
				obj.put("bookmark_id", bookmarkId);
//				// TODO Auto-generated catch block
				HttpPost post = new HttpPost(LoginActivity.WEBSERVICEURL);
				StringEntity se = new StringEntity(obj.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				post.setEntity(se);
				response = client.execute(post);
				/* Checking response */
				if (response != null) {
					// reading response
					InputStream ips = response.getEntity().getContent();
					BufferedReader buf = new BufferedReader(
							new InputStreamReader(ips, "UTF-8"));
					if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
						throw new Exception(response.getStatusLine()
								.getReasonPhrase());
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
//						System.out.println("response ----" + json);
//						 setJsonObj(json);
						result = "pass";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			Functions.dismissLoadingDialog();
			
			if (result.equals("pass")) {
				System.out
						.println("removed items");
				arr.remove(position1);
				notifyDataSetChanged();
//				System.out.println("Size of array after delete ==0"
//						+ arr.size());
				Toast.makeText(
						context,
						"Bookmark Deleted",
						Toast.LENGTH_SHORT);
			} else {
				Toast.makeText(
						context,
						"Error in Deletion",
						Toast.LENGTH_SHORT);
			}
//			JsonManipulator jm = new JsonManipulator();
//			ArrayList<BookmarkBean> arr = jm.bookmarkJsonToMap(result);
//
//			FragmentTransaction traFragment = DisplayDataActivity.this
//					.getSupportFragmentManager().beginTransaction();
//			Bundle bdl = new Bundle();
//
//			BookmarkListFragment bookmark = new BookmarkListFragment();
//			bdl.putParcelableArrayList(LoginActivity.Extra_Value, arr);
//			bookmark.setArguments(bdl);
//			traFragment.replace(R.id.content, bookmark);
//			traFragment.addToBackStack(null);
//			traFragment.commit();

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
//			Functions.showLoadingDialog(DisplayDataActivity.this, "Loading...");
			super.onPreExecute();
		}

	}
}
