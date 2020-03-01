package com.app.deyaka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.deyaka.R;

public class Payment_Failure_Act extends AppCompatActivity {

    Button btnHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_failure_act);

        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMyKids = new Intent(Payment_Failure_Act.this, MyKids_Act.class);
                intentMyKids.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentMyKids);
                finish();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intentMyKids = new Intent(Payment_Failure_Act.this, MyKids_Act.class);
        intentMyKids.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentMyKids);
        finish();
    }
}
