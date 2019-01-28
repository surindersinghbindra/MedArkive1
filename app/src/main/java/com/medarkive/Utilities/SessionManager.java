package com.medarkive.Utilities;

import java.util.HashMap;

import com.medarkive.Main.DisplayDataActivity;
import com.medarkive.Main.LoginActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	private static final String PREF_NAME = "MedPref";

	private static final String IS_LOGIN = "IsLoggedIn";

	public static final String KEY_NAME = "name";

	public static final String KEY_EMAIL = "email";

	public static final String KEY_USER_ID = "user_id";
	
	public static final String KEY_TOKEN = "token";

	public static final String CPD_USER_CHOICE = "cpd_choice";

	// Constructor
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void createLoginSession(String name, String email, String userId , String token) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		editor.putString(KEY_NAME, name);

		editor.putString(KEY_EMAIL, email);

		editor.putString(KEY_USER_ID, userId);
		
		editor.putString(KEY_TOKEN, token);

		// commit changes
		editor.commit();
	}
	
	public void changeName(String name){
		editor.putString(KEY_NAME, name);
		editor.commit();
	}

	public void checkLogin() {
		// Check login status
		if (!this.isLoggedIn()) {
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, LoginActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			_context.startActivity(i);
		} else {
			// user logged in redirect him to Display page
			Intent i = new Intent(_context, DisplayDataActivity.class);
			_context.startActivity(i);
		}
	}

	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));

		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

		user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
		
		user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
		// return user
		return user;
	}

	public void logoutUser() {
		// Clearing all data from Shared Preferences
		editor.clear();
		System.out.println("session -------->>>" + editor.clear().toString());
		editor.commit();
		System.out.println("editor =---===>" + editor.toString());

		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, LoginActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		// Staring Login Activity
		_context.startActivity(i);
		((Activity) _context).finish();
	}

	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}
	
	public void setCPDChoice(String str){
		editor.putString(CPD_USER_CHOICE, str);
		editor.commit();
	}
	
	public String getCPDChoice(){
		// cpd choice either 1 ,0
		return pref.getString(CPD_USER_CHOICE, "1");
	}
	
}
