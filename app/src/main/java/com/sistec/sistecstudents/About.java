package com.sistec.sistecstudents;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sistec.helperClasses.MyHelperClass;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView versionNameTV = findViewById(R.id.version_textview);
        versionNameTV.setText("Version : " + MyHelperClass.getVersionName(About.this));
    }
}
