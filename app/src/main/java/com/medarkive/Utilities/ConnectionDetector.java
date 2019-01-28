package com.medarkive.Utilities;

import com.medarkive.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

	private Context context;
	private String alertTitle="Oops…connection error";
	private String alertMessage="Please check your Internet connection and try downloading again.";

	public ConnectionDetector(Context context) {
		this.context = context;
	}

	public boolean isConnectedToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
			{
				for (int i = 0; i < info.length; i++)
				{
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public void showAlertDialog(Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(alertTitle);

		// Setting Dialog Message
		alertDialog.setMessage(alertMessage);

		// Setting alert dialog icon
		alertDialog.setIcon((status) ? R.drawable.ic_check
				: R.drawable.ic_cancel);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
}
