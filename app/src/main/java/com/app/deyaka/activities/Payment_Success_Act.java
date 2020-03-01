package com.app.deyaka.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.deyaka.R;

public class Payment_Success_Act extends AppCompatActivity {

    Button btnHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_success_act);
        btnHome = findViewById(R.id.btnHome);
    }

    @Override
    public void onResume() {
        super.onResume();
        LayoutInflater layoutInflater = LayoutInflater.from(Payment_Success_Act.this);
        android.view.View promptView = layoutInflater.inflate(R.layout.feedback_popup, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Payment_Success_Act.this, R.style.AlertDialogStyle);
        alertDialogBuilder.setView(promptView);

        ImageButton imgBtnClose = promptView.findViewById(R.id.imgBtnClose);
        Button btn_continue = promptView.findViewById(R.id.btn_continue);


        final AlertDialog alertDialog1 = alertDialogBuilder.create();
        alertDialog1.show();
        alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //mae alert bg transparent for custom rounded corner
        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id="+getApplicationContext().getPackageName());//+getApplicationContext.getPackageName()i both placescom.app.veggies
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName())));
                }
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMyKids = new Intent(Payment_Success_Act.this, MyKids_Act.class);
                intentMyKids.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentMyKids);
                finish();
            }
        });

    }
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intentMyKids = new Intent(Payment_Success_Act.this, MyKids_Act.class);
        intentMyKids.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentMyKids);
        finish();
    }
}
