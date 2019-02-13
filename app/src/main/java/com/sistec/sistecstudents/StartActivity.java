package com.sistec.sistecstudents;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

public class StartActivity extends AppCompatActivity {

    private static String ENROLL_PREF_KEY = "enroll_pref_key", PASSWORD_PREF_KEY = "password_pref_key";
    LinearLayout connectingLayout, loginLayout;
    ImageView startLogo;
    TextView welcomeTxt, connTxt, forgotPass;
    EditText enrollNoEditText, passwordEditText;
    CheckBox rememberChkbx;
    Button loginBtn;
    Animation animationBlink, animationBounce, animationZoomOut, shiftUpZoomOut;
    String sharedPrefFileName = "user", defaultEn = "", defaultPass = "", enroll = "", pass = "";
    SharedPreferences sharedPrefe;
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
        sharedPrefe = getSharedPreferences(sharedPrefFileName, Context.MODE_PRIVATE);
        enrollNoEditText.setText(sharedPrefe.getString(ENROLL_PREF_KEY, defaultEn));
        passwordEditText.setText(sharedPrefe.getString(PASSWORD_PREF_KEY, defaultPass));
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

        if (checkInternet()) {
            //Toast.makeText(StartActivity.this,"Connected",Toast.LENGTH_LONG).show();
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
            }, 3000);
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
                    builder = new AlertDialog.Builder(StartActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                else
                    builder = new AlertDialog.Builder(StartActivity.this);
                builder.setTitle("Password Recovery");
                builder.setMessage("You will get a password reset link on your mail");
                final EditText input = new EditText(StartActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                input.setLayoutParams(lp);
                input.setHint("Enrollment Number");
                builder.setView(input);
                builder.setIcon(R.drawable.ic_recover_green_24dp);
                builder.setPositiveButton("Send Link", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userID = input.getText().toString().trim();
                        //TODO: send reset link to @userID
                        enrollNoEditText.setText(userID);
                        passwordEditText.setText("");
                        Toast.makeText(StartActivity.this, userID, Toast.LENGTH_LONG).show();
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
                enroll = enrollNoEditText.getText().toString().trim();
                pass = passwordEditText.getText().toString().trim();
                SharedPreferences.Editor editor = sharedPrefe.edit();
                if (checkInternet()) {
                    if (rememberChkbx.isChecked()) {
                        //Save Login Details
                        editor.putString(ENROLL_PREF_KEY, enroll);
                        editor.putString(PASSWORD_PREF_KEY, pass);
                    } else {
                        editor.putString(ENROLL_PREF_KEY, "");
                        editor.putString(PASSWORD_PREF_KEY, "");
                    }
                    //TODO: Send login credentials @enroll and @pass and get response
                    //after geting response save cache and start main activity
                    //This will happen only on successful login
                    startActivity(new Intent(StartActivity.this, HomeActivity.class));
                    StartActivity.this.finish();
                } else
                    noInternetAlert();
                editor.apply();
                editor.commit();
            }
        });
    }

    private boolean checkInternet() {
        //Check Network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void noInternetAlert() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder = new AlertDialog.Builder(StartActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        else
            builder = new AlertDialog.Builder(StartActivity.this);
        builder.setTitle("Error in Connection")
                .setMessage("No Internet Access!!!")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StartActivity.this.finish();
                    }
                })
                .setIcon(R.drawable.ic_error_red_24dp)
                .setCancelable(false)
                .show();
    }

}
