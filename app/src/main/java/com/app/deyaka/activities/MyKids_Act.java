package com.app.deyaka.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.deyaka.R;
import com.app.deyaka.adapters.GridSpacingItemDecoration;
import com.app.deyaka.adapters.Utils;
import com.app.deyaka.models.MyKidList_Model;
import com.app.deyaka.retrofit.APIClient;
import com.app.deyaka.retrofit.APIInterface;
import com.app.deyaka.retrofit.Logout_RetroModel;
import com.app.deyaka.retrofit.MyKids_RetroModel;
import com.app.deyaka.session_management.SessionManager;
import com.mindorks.placeholderview.PlaceHolderView;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyKids_Act extends AppCompatActivity {

    Toolbar toolbar_my_kids;
    APIInterface apiInterface;
    ProgressDialog progressDialog;
    SessionManager session;
    PlaceHolderView phv_kids;
    LinearLayout llNoData;
    private TextView btnTryAgain;
    private LinearLayout llTryAgain, llMyKids;

    String[] str_bg_color = {"#2FD498", "#089ADA", "#FF7878", "#F993C3", "#9BC4E2", "#F89827"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_kids_act);

        session = new SessionManager(MyKids_Act.this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = new ProgressDialog(MyKids_Act.this);
        progressDialog.setMessage("Please wait....");

        toolbar_my_kids = findViewById(R.id.toolbar_my_kids);
        llNoData = findViewById(R.id.llNoData);
        btnTryAgain = findViewById(R.id.btnTryAgain);
        llTryAgain = findViewById(R.id.llTryAgain);
        llMyKids = findViewById(R.id.llMyKids);


        setSupportActionBar(toolbar_my_kids);//manadatory for menu items
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        toolbar_my_kids.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        phv_kids = findViewById(R.id.phv_kids);
        phv_kids.getBuilder()
                .setHasFixedSize(false)
                .setItemViewCacheSize(10)
                .setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        //space between columns
        int spanCount = 2; // 2 columns
        int spacing = 30; // 10px
        boolean includeEdge = true;
        phv_kids.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


        if (Utils.CheckInternetConnection(getApplicationContext())) {
            getKidsListAPI();
        } else {
            Toast.makeText(getApplicationContext(), "Please Check Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void createKidsList(int n) {
        if (n >= 0) {
            Random random = new Random();
            int num = random.nextInt(str_bg_color.length - 0) + 0;
            // phv_kids.addView(new MyKidsModel(getApplicationContext(), str_bg_color[num], n));
            createKidsList(n - 1);
            Log.e("---kids array length--", String.valueOf(n - 1));

            Log.e("---Random number--", String.valueOf(num));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.logout_menu,menu);
        //for menu icon and text
        menu.add(0, 1, 1, menuIconWithText(getResources()
                .getDrawable(R.drawable.logout_mipmap), getResources()
                .getString(R.string.logout)));
        toolbar_my_kids.setTitle("My Kids");//displaying toolbar title after adding menu title is getting disappear so settitle here
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case 1:
                // Toast.makeText(getApplicationContext(), "Test Logout!", Toast.LENGTH_LONG).show();
                logoutUsr();
                break;
        }
        return true;
    }

    public void logoutUsr() {

        Logout_RetroModel logout_retroModel = new Logout_RetroModel(session.getUserId(), session.getOtpDeviceId());
        Call<Logout_RetroModel> call_logout = apiInterface.logout(logout_retroModel);
        call_logout.enqueue(new Callback<Logout_RetroModel>() {
            @Override
            public void onResponse(Call<Logout_RetroModel> call, Response<Logout_RetroModel> response) {
                Logout_RetroModel logout_resources = response.body();
                if (response.isSuccessful()) {
                    if (logout_resources.status.equals("success")) {
                        session.logoutUser();
                        Toast.makeText(getApplicationContext(), logout_resources.message, Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        Intent intentLogin = new Intent(MyKids_Act.this, SignInActivity.class);
                        startActivity(intentLogin);
                        finish();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), logout_resources.message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(MyKids_Act.this)
                            .setMessage("Network Connection error! Please try again later")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }

            @Override
            public void onFailure(Call<Logout_RetroModel> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
                new AlertDialog.Builder(MyKids_Act.this)
                        .setMessage("Network Connection error! Please try again later")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    public void getKidsListAPI() {

        MyKids_RetroModel myKids_retroModel = new MyKids_RetroModel(session.getUserId());
        Call<MyKids_RetroModel> call_myKidsRetroModel = apiInterface.getKidsList(myKids_retroModel);
        call_myKidsRetroModel.enqueue(new Callback<MyKids_RetroModel>() {
            @Override
            public void onResponse(Call<MyKids_RetroModel> call, Response<MyKids_RetroModel> response) {
                MyKids_RetroModel kids_resources = response.body();
                if (response.isSuccessful()) {

                    llMyKids.setVisibility(View.VISIBLE);
                    llTryAgain.setVisibility(View.GONE);

                    if (kids_resources.status.equals("success")) {

                        phv_kids.setVisibility(View.VISIBLE);
                        llNoData.setVisibility(View.GONE);
                        List<MyKids_RetroModel.KidsListDatum> kids_result = kids_resources.result;
                        if (kids_result.size() <= 0) {
                            phv_kids.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);

                        } else {
                            for (MyKids_RetroModel.KidsListDatum kids_data : kids_result) {

                                phv_kids.addView(new MyKidList_Model(MyKids_Act.this, kids_data.kid_name,
                                        kids_data.kid_class, kids_data.kid_image, kids_data.kid_id, str_bg_color));

                                if (!kids_data.school_name.equals("null") && !kids_data.school_name.equals(null)
                                        && !kids_data.school_name.equals("NA") && !kids_data.school_name.isEmpty()) {
                                    session.putSchoolName(kids_data.school_name);
                                } else {
                                    session.putSchoolName("NA");
                                }
                            }
                        }
                    } else {
                        phv_kids.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);

                    }
                } else {
                    llMyKids.setVisibility(View.GONE);
                    llTryAgain.setVisibility(View.VISIBLE);
                    phv_kids.setVisibility(View.GONE);
                    llNoData.setVisibility(View.GONE);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MyKids_RetroModel> call, Throwable t) {
                call.cancel();
                llMyKids.setVisibility(View.GONE);
                llTryAgain.setVisibility(View.VISIBLE);
                phv_kids.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);
                progressDialog.dismiss();
            }
        });
    }

    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
