package com.app.deyaka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.app.deyaka.R;

public class PasswordChanged extends AppCompatActivity implements View.OnClickListener {

    LinearLayout back_to_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_changed);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        back_to_login=(LinearLayout)findViewById(R.id.backtologin);
        back_to_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backtologin:
                startActivity(new Intent(PasswordChanged.this, SignInActivity.class));
                break;
            default:
                break;
        }
    }
}
