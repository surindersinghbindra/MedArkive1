package com.medarkive.Main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;

import com.medarkive.R;
import com.medarkive.Utilities.ConnectionDetector;
import com.medarkive.Utilities.Functions;

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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

public class SignUp extends Activity {

    private String URL = "http://medarkive.com/WebServices/index";
    // private ProgressDialog mProgressBar;
    EditText user, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TelephonyManager tm = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = ""
                + android.provider.Settings.Secure.getString(
                getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        final String deviceId = deviceUuid.toString();

        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //  super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);

        // EditText password = (EditText) findViewById(R.id.email);
        // EditText username = (EditText) findViewById(R.id.name);
        // EditText type = (EditText) findViewById(R.id.type);
        final Context ctx = getApplicationContext();

        findViewById(R.id.submit_register).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        user = (EditText) findViewById(R.id.name);
                        email = (EditText) findViewById(R.id.email);

                        if (user.getText().toString().length() > 0
                                && email.getText().toString().length() > 0) {
                            ConnectionDetector cd = new ConnectionDetector(
                                    SignUp.this);
                            if (cd.isConnectedToInternet()) {
                                registerNewUser(user.getText().toString()
                                        .trim(), email.getText().toString()
                                        .trim(), deviceId, ctx);
                                Functions.showLoadingDialog(
                                        SignUp.this, "Please Wait...");
                                // mProgressBar = new ProgressDialog(
                                // RegisterNewUser.this);
                                // mProgressBar.setIndeterminate(true);
                                // mProgressBar
                                // .setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                // mProgressBar.show();
                            } else {
                                // new AlertDialog.Builder(RegisterNewUser.this)
                                // .setTitle("Data Connection Unavailable")
                                // .setMessage(
                                // "Please connect to a Network in order to complete Registration")
                                // .setCancelable(false)
                                // .setPositiveButton("ok",
                                // new OnClickListener() {
                                // @Override
                                // public void onClick(
                                // DialogInterface dialog,
                                // int which) {
                                // // whatever...
                                // }
                                // }).create().show();
                                //cd.showAlertDialog(cd.isConnectingToInternet());
                            }
                        } else {
                            Builder builder = new AlertDialog.Builder(
                                    SignUp.this);
                            builder.setTitle("Information missing");
                            builder.setMessage("All fields are mandatory. Please  fill in all the fields.");
                            builder.setCancelable(true);
                            builder.setPositiveButton("Ok",
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // TODO Auto-generated method stub

                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            // Toast.makeText(RegisterNewUser.this,
                            // "Please fill required fields.",
                            // Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        findViewById(R.id.back_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
    }

    public void registerNewUser(final String usernam, final String email,
                                final String deviId, final Context ctx) {

        Thread t = new Thread() {
            String method = "registration";

            public void run() {
                Looper.prepare(); // For Preparing Message Pool for the child
                // Thread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(),
                        10000); // Timeout Limit
                HttpResponse response;
                JSONObject json = new JSONObject();
                try {
                    HttpPost post = new HttpPost(URL);
                    json.put("name", usernam);
                    json.put("emailid", email);
                    json.put("device_id", deviId);
                    json.put("method", method);
                    System.out.println("json ==" + json.toString());
                    StringEntity se = new StringEntity(json.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
                            "application/json"));
                    post.setEntity(se);
                    // System.out.println("check 1");
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
                        System.out.println("json:----- " + json.toString());
                        if (json.length() > 0) {
                            final String msg = new JSONObject(
                                    json.getString("registration")).get(
                                    "status").toString();
                            System.out.println("check.....>>>" + msg);
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    // mProgressBar.dismiss();
                                    Functions.dismissLoadingDialog();
                                    if (!isFinishing()) {
                                        if (msg.equalsIgnoreCase("Email address already exists.")) {
                                            new AlertDialog.Builder(
                                                    SignUp.this)
                                                    // .setTitle("Registration")
                                                    .setMessage("This email ID already exist. Please try with a different email.")
                                                    .setCancelable(false)
                                                    .setPositiveButton(
                                                            "ok",
                                                            new OnClickListener() {
                                                                @Override
                                                                public void onClick(
                                                                        DialogInterface dialog,
                                                                        int which) {
                                                                    // whatever...
                                                                    // finish();
                                                                }
                                                            }).create().show();
                                        } else {
                                            new AlertDialog.Builder(
                                                    SignUp.this)
//													.setTitle("Registration")
                                                    .setMessage("Congratulations, you have successfully registered for a MedArkive account. Please check your email for credentials.")
                                                    .setCancelable(false)
                                                    .setPositiveButton(
                                                            "ok",
                                                            new OnClickListener() {
                                                                @Override
                                                                public void onClick(
                                                                        DialogInterface dialog,
                                                                        int which) {
                                                                    // whatever...
                                                                    // finish();
                                                                }
                                                            }).create().show();
                                        }
                                    }
                                }
                            });
                            // finish();
                        } else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    // mProgressBar.dismiss();
                                    Functions.dismissLoadingDialog();
                                    if (!isFinishing()) {
                                        new AlertDialog.Builder(
                                                SignUp.this)
//												.setTitle("Registration Error")
                                                .setMessage("Something went wrong")
                                                .setCancelable(false)
                                                .setPositiveButton("ok",
                                                        new OnClickListener() {
                                                            @Override
                                                            public void onClick(
                                                                    DialogInterface dialog,
                                                                    int which) {
                                                                // finish();
                                                                // whatever...
                                                            }
                                                        }).create().show();
                                    }
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Functions.dismissLoadingDialog();
                    new AlertDialog.Builder(SignUp.this)
//							.setTitle("Registration Error")
                            .setMessage("Oops…server error")
                            .setCancelable(false)
                            .setPositiveButton("ok", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            }).create().show();
                }
                Looper.loop(); // Loop in the message queue
            }
        };
        t.start();
    }
}
