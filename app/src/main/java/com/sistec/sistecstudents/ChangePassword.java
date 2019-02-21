package com.sistec.sistecstudents;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class ChangePassword extends AppCompatActivity {

    EditText con_passEditText, old_passEditText,new_passEditText;
    Button changepasswordBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        con_passEditText = findViewById(R.id.con_pass);
        old_passEditText = findViewById(R.id.old_pass);
        new_passEditText = findViewById(R.id.new_pass);
        changepasswordBtn = findViewById(R.id.change_pass_btn);


        con_passEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 4 && con_passEditText.getText().toString().trim().length() > 4 && old_passEditText.getText().toString().trim().length() > 4 && new_passEditText.getText().toString().trim().length() > 4) {
                activateBtn();
            } else {
                deactivateBtn();
            }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        old_passEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 4 && con_passEditText.getText().toString().trim().length() > 4 && old_passEditText.getText().toString().trim().length() > 4 && new_passEditText.getText().toString().trim().length() > 4) {
                    activateBtn();
                } else {
                    deactivateBtn();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        new_passEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 4 && con_passEditText.getText().toString().trim().length() > 4 && old_passEditText.getText().toString().trim().length() > 4 && new_passEditText.getText().toString().trim().length() > 4) {
                    activateBtn();
                } else {
                    deactivateBtn();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private void activateBtn(){
        changepasswordBtn.setBackground(getResources().getDrawable(R.drawable.button_enabled));
        changepasswordBtn.setEnabled(true);

    }
    private void deactivateBtn(){
        changepasswordBtn.setBackground(getResources().getDrawable(R.drawable.button_disabled));
        changepasswordBtn.setEnabled(false);

    }
}