package com.medarkive.Utilities;

import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

public class Constants {

	public static final String CONSUMER_KEY = "yKKc0AUP4MZLIJWCPOBEerUUJ";
	public static final String CONSUMER_SECRET = "JmygB2hF2KWamvPv7VfqJ3bCYp7lqQvhN3CBPAqsDRKlFR2Al3";

	public static final String REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "https://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";

	public static final String OAUTH_CALLBACK_SCHEME = "tumblrdemo-kikit";
	public static final String OAUTH_CALLBACK_HOST = "callback";
	public static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME+ "://" + OAUTH_CALLBACK_HOST;
	
	public static final String OAUTH_CALLBACK_SCHEME1 = "tumblrdemo-kikit";
	public static final String OAUTH_CALLBACK_HOST1 = "callback1";
	public static final String OAUTH_CALLBACK_URL1 = OAUTH_CALLBACK_SCHEME1+ "://" + OAUTH_CALLBACK_HOST1;
	

	public static CommonsHttpOAuthConsumer consumer = null;
	public static OAuthProvider provider = null;
}