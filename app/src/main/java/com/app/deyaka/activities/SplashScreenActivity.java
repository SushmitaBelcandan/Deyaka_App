package com.app.deyaka.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.app.deyaka.R;
import com.app.deyaka.session_management.SessionManager;

public class SplashScreenActivity extends AppCompatActivity {

    SessionManager session;
    public static final String KEY_TERMS_ACCEPTED = "false";
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        session = new SessionManager(SplashScreenActivity.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!session.isLoggedIn()) {
                    prefs = PreferenceManager.getDefaultSharedPreferences(SplashScreenActivity.this);
                    if (!prefs.getBoolean(KEY_TERMS_ACCEPTED, false)) {
                        showTermsConditions();
                    }
                    else {
                        Intent i = new Intent(SplashScreenActivity.this, SignInActivity.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, MyKids_Act.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }

    private void showTermsConditions() {
        LayoutInflater layoutInflater = LayoutInflater.from(SplashScreenActivity.this);
        android.view.View payPopUpView = layoutInflater.inflate(R.layout.terms_conditions_popup, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashScreenActivity.this, R.style.AlertDialogStyle);
        alertDialogBuilder.setView(payPopUpView);

        final CheckBox chkbx_agree = payPopUpView.findViewById(R.id.cbAgreement);
        //show second term conditions popup
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final Button btn_proceed = payPopUpView.findViewById(R.id.btn_proceed_agreement);
        btn_proceed.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (chkbx_agree.isChecked()) {
                    alertDialog.dismiss();
                    prefs.edit().putBoolean(KEY_TERMS_ACCEPTED, true).commit();
                    Intent i = new Intent(SplashScreenActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}
