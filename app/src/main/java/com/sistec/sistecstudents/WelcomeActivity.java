package com.sistec.sistecstudents;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sistec.helperClasses.AppConnectivityStatus;
import com.sistec.helperClasses.RemoteServiceUrl;
import com.sistec.helperClasses.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WelcomeActivity extends AppCompatActivity {

    private static String LOGIN_URL = RemoteServiceUrl.SERVER_URL + RemoteServiceUrl.METHOD_NAME.LOGIN;
    private static String ENROLL_PREF_KEY = RemoteServiceUrl.SHARED_PREF.ENROLL_PREF_KEY;
    private static String PASSWORD_PREF_KEY = RemoteServiceUrl.SHARED_PREF.PASSWORD_PREF_KEY;
    private static String IS_LOGIN_PREF_KEY = RemoteServiceUrl.SHARED_PREF.IS_LOGIN_PREF_KEY;
    String sharedPrefUserFileName = RemoteServiceUrl.SHARED_PREF.USER_FILE_NAME;
    String sharedPrefLoginFileName = RemoteServiceUrl.SHARED_PREF.LOGIN_STATUS_FILE_NAME;
    SharedPreferences sharedPrefUser, sharedPrefLogin;
    LinearLayout connectingLayout, loginLayout;
    ImageView startLogo;
    TextView welcomeTxt, connTxt, forgotPass;
    EditText enrollNoEditText, passwordEditText;
    CheckBox rememberChkbx;
    Button loginBtn;
    Animation animationBlink, animationBounce, animationZoomOut, shiftUpZoomOut;
    String e_no = "", password = "";
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        connectingLayout = findViewById(R.id.connecting_layout);
        loginLayout = findViewById(R.id.login_layout);
        startLogo = findViewById(R.id.start_logo);
        welcomeTxt = findViewById(R.id.welcome_txt);
        connTxt = findViewById(R.id.conn);
        enrollNoEditText = findViewById(R.id.enroll_num);
        passwordEditText = findViewById(R.id.password);
        rememberChkbx = findViewById(R.id.remember);
        forgotPass = findViewById(R.id.forgot_pass);
        loginBtn = findViewById(R.id.login);

        //Auto fetch saved preferences
        sharedPrefUser = getSharedPreferences(sharedPrefUserFileName, Context.MODE_PRIVATE);
        sharedPrefLogin = getSharedPreferences(sharedPrefLoginFileName, Context.MODE_PRIVATE);
        e_no = sharedPrefUser.getString(ENROLL_PREF_KEY, "");
        enrollNoEditText.setText(e_no);
        password = sharedPrefUser.getString(PASSWORD_PREF_KEY, "");
        passwordEditText.setText(password);
        if (enrollNoEditText.getText().toString().isEmpty()) {
            enrollNoEditText.requestFocus();
            enrollNoEditText.setError(null);
        }

        //A[[lying animation on the views
        animationZoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        animationBounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        animationBlink = AnimationUtils.loadAnimation(this, R.anim.blink);
        shiftUpZoomOut = AnimationUtils.loadAnimation(this, R.anim.shift_up_zoom_out);

        //First animation
        startLogo.startAnimation(animationBounce);
        connTxt.startAnimation(animationBlink);

        if (AppConnectivityStatus.isOnline(getApplicationContext())) {
            if (sharedPrefLogin.getBoolean(IS_LOGIN_PREF_KEY, false))
                login();
            else
                animationSwitcher();
        } else {
            noInternetAlert();
        }

        enrollNoEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (enrollNoEditText.getText().toString().trim().length() != 12)
                    enrollNoEditText.setError("Invalid Enroll Num");
                else
                    enrollNoEditText.setError(null);
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 4 && enrollNoEditText.getText().toString().trim().length() == 12) {
                    loginBtn.setBackground(getResources().getDrawable(R.drawable.button_enabled));
                    loginBtn.setEnabled(true);
                } else {
                    loginBtn.setBackground(getResources().getDrawable(R.drawable.button_disabled));
                    loginBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    builder = new AlertDialog.Builder(WelcomeActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                else
                    builder = new AlertDialog.Builder(WelcomeActivity.this);
                builder.setTitle("Password Recovery");
                builder.setMessage("You will get a password reset link on your mail");
                final EditText input = new EditText(WelcomeActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                input.setLayoutParams(lp);
                input.setHint("Enrollment Number");
                input.setTextColor(getResources().getColor(R.color.colorPrimary));
                builder.setView(input);
                builder.setIcon(R.drawable.ic_recover_green_24dp);
                builder.setPositiveButton("Send Link", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userID = input.getText().toString().trim();
                        //TODO: send reset link to @userID
                        enrollNoEditText.setText(userID);
                        passwordEditText.setText("");
                        Toast.makeText(WelcomeActivity.this, "Reset link sent to your mail", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e_no = enrollNoEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();
                if (AppConnectivityStatus.isOnline(getApplicationContext())) {
                    login();
                } else
                    noInternetAlert();

            }
        });
    }

    private void noInternetAlert() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder = new AlertDialog.Builder(WelcomeActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        else
            builder = new AlertDialog.Builder(WelcomeActivity.this);
        builder.setTitle("Connectivity Error")
                .setMessage("No Internet Access!!!")
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WelcomeActivity.this.finish();
                    }
                })
                .setIcon(R.drawable.ic_error_red_24dp)
                .setCancelable(false)
                .show();
    }

    private void animationSwitcher() {
        //Toast.makeText(WelcomeActivity.this,"Connected",Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startLogo.startAnimation(animationZoomOut);
                welcomeTxt.setText(R.string.login_to_erp);
                welcomeTxt.startAnimation(shiftUpZoomOut);
                connTxt.clearAnimation();
                connTxt.setVisibility(View.GONE);
                connectingLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);

            }
        }, 2000);
    }

    private void login() {
        AppConnectivityStatus.showProgress(WelcomeActivity.this, "Login", "Please wait a moment");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            String success = root.getString("success");
                            JSONArray array = root.getJSONArray("login");
                            if (success.equals("1")) {
                                JSONObject jsonObject = array.getJSONObject(0);
                                updateSharedPref(true, jsonObject.getString("e_no"));

                                Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                                intent.putExtra("e_no", jsonObject.getString("e_no"));
                                intent.putExtra("name", jsonObject.getString("name"));
                                startActivity(intent);
                                AppConnectivityStatus.hideProgress();
                                WelcomeActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Response Error", error.toString());
                        updateSharedPref(false, "Not Available");
                        AppConnectivityStatus.hideProgress();
                        Toast.makeText(WelcomeActivity.this, "Invalid Credentials, Retry", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("e_no", e_no);
                params.put("password", password);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void updateSharedPref(boolean login_status, String en) {
        SharedPreferences.Editor userEditor = sharedPrefUser.edit();
        SharedPreferences.Editor loginEditor = sharedPrefLogin.edit();
        if (rememberChkbx.isChecked()) {
            //Save Login Details
            userEditor.putString(ENROLL_PREF_KEY, e_no);
            userEditor.putString(PASSWORD_PREF_KEY, password);
        } else {
            userEditor.putString(ENROLL_PREF_KEY, "");
            userEditor.putString(PASSWORD_PREF_KEY, "");
        }
        userEditor.apply();
        loginEditor.putString(ENROLL_PREF_KEY, en);
        loginEditor.putBoolean(IS_LOGIN_PREF_KEY, login_status);
        loginEditor.apply();

    }

}
