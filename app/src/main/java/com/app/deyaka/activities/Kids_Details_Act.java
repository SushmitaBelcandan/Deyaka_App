package com.app.deyaka.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.deyaka.R;
import com.app.deyaka.adapters.Utils;
import com.app.deyaka.retrofit.APIClient;
import com.app.deyaka.retrofit.APIInterface;
import com.app.deyaka.retrofit.WardDetails_RetroModel;
import com.app.deyaka.session_management.SessionManager;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Kids_Details_Act extends AppCompatActivity {

    APIInterface apiInterface;
    ProgressDialog progressdialog;
    Toolbar toolbarKidsDetails, toolbarKidsDetails1;
    SessionManager sessionManager;

    private ImageView iv_kid_image, iv_school_logo;
    private TextView btnTryAgain;
    private LinearLayout llTryAgain, llkidsLayout;
    private TextView tv_school_name, tv_kid_name;
    private TextView tv_org_name, tv_term, tv_section, tv_dob, tv_house_color, tv_father_name, tv_mother_name, tv_mobile_number, tv_email_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kids_details);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressdialog = new ProgressDialog(Kids_Details_Act.this);
        progressdialog.setMessage("Please Wait...");
        sessionManager = new SessionManager(Kids_Details_Act.this);

        iv_school_logo = findViewById(R.id.iv_school_logo);
        iv_kid_image = findViewById(R.id.iv_kid_image);
        tv_school_name = findViewById(R.id.tv_school_name);
        tv_kid_name = findViewById(R.id.tv_kid_name);

        tv_org_name = findViewById(R.id.tv_org_name);
        tv_term = findViewById(R.id.tv_term);
        tv_section = findViewById(R.id.tv_section);
        tv_dob = findViewById(R.id.tv_dob);
        tv_house_color = findViewById(R.id.tv_house_color);
        tv_father_name = findViewById(R.id.tv_father_name);
        tv_mother_name = findViewById(R.id.tv_mother_name);
        tv_mobile_number = findViewById(R.id.tv_mobile_number);
        tv_email_id = findViewById(R.id.tv_email_id);

        btnTryAgain = findViewById(R.id.btnTryAgain);
        llTryAgain = findViewById(R.id.llTryAgain);
        llkidsLayout = findViewById(R.id.llkidsLayout);

        toolbarKidsDetails = findViewById(R.id.toolbarKidsDetails);
        setSupportActionBar(toolbarKidsDetails);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarKidsDetails.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbarKidsDetails1 = findViewById(R.id.toolbarKidsDetails1);
        setSupportActionBar(toolbarKidsDetails1);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        //slow network connection page toolbar
        toolbarKidsDetails1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Utils.CheckInternetConnection(getApplicationContext())) {
            getWardDetailsAPI();
        } else {
            Toast.makeText(getApplicationContext(), "Please Check Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getWardDetailsAPI() {
        try {
            progressdialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WardDetails_RetroModel ward_model = new WardDetails_RetroModel(sessionManager.getKidId());
        Call<WardDetails_RetroModel> call_ward_details = apiInterface.getWardDetails(ward_model);
        call_ward_details.enqueue(new Callback<WardDetails_RetroModel>() {
            @Override
            public void onResponse(Call<WardDetails_RetroModel> call, Response<WardDetails_RetroModel> response) {
                WardDetails_RetroModel ward_resources = response.body();
                if (response.isSuccessful()) {
                    if (ward_resources.status.equals("success")) {
                        llkidsLayout.setVisibility(View.VISIBLE);
                        llTryAgain.setVisibility(View.GONE);
                        List<WardDetails_RetroModel.WardDetailsDatum> arrayList_ward = ward_resources.result;
                        for (WardDetails_RetroModel.WardDetailsDatum ward_data_result : arrayList_ward) {
                            //***************************************Kid Photo*******************************************
                            if (!ward_data_result.kid_photo.equals("NA") && !ward_data_result.kid_photo.equals("null") &&
                                    !ward_data_result.kid_photo.equals(null) && !ward_data_result.kid_photo.isEmpty()) {
                                Glide.with(getApplicationContext()).load(ward_data_result.kid_photo).error(R.drawable.person).into(iv_kid_image);
                            } else {
                                Glide.with(getApplicationContext()).load(R.drawable.person).into(iv_kid_image);
                            }
                            //***************************************Kid Name*******************************************
                            if (!ward_data_result.kid_name.equals("NA") && !ward_data_result.kid_name.equals("null") &&
                                    !ward_data_result.kid_name.equals(null) && !ward_data_result.kid_name.isEmpty()) {
                                tv_kid_name.setText(ward_data_result.kid_name);
                            } else {
                                tv_kid_name.setText("");
                            }
                            //***************************************School logo*******************************************
                            if (!ward_data_result.school_logo.equals("NA") && !ward_data_result.school_logo.equals("null") &&
                                    !ward_data_result.school_logo.equals(null) && !ward_data_result.school_logo.isEmpty()) {
                                Glide.with(getApplicationContext()).load(ward_data_result.school_logo).error(R.drawable.error_image).into(iv_school_logo);
                            } else {
                                Glide.with(getApplicationContext()).load(R.drawable.error_image).into(iv_school_logo);
                            }
                            //**********************************school name**********************************************
                            if (!ward_data_result.school_name.equals("NA") && !ward_data_result.school_name.equals("null") &&
                                    !ward_data_result.school_name.equals(null) && !ward_data_result.school_name.isEmpty()) {
                                tv_org_name.setText(ward_data_result.school_name);
                                tv_school_name.setText(ward_data_result.school_name);
                            } else {
                                tv_org_name.setText("-");
                                tv_school_name.setText("");
                            }
                            //****************************************standard*******************************************
                            if (!ward_data_result.standard.equals("NA") && !ward_data_result.standard.equals("null") &&
                                    !ward_data_result.standard.equals(null) && !ward_data_result.standard.isEmpty()) {
                                tv_term.setText(ward_data_result.standard);
                            } else {
                                tv_term.setText("-");
                            }
                            //*************************************section**********************************************
                            if (!ward_data_result.section.equals("NA") && !ward_data_result.section.equals("null") &&
                                    !ward_data_result.section.equals(null) && !ward_data_result.section.isEmpty()) {
                                tv_section.setText(ward_data_result.section);
                            } else {
                                tv_section.setText("-");
                            }
                            //***********************************Date of Birth*****************************************
                            if (!ward_data_result.dob.equals("NA") && !ward_data_result.dob.equals("null") &&
                                    !ward_data_result.dob.equals(null) && !ward_data_result.dob.isEmpty()) {

                                Date localTime = null;
                                try {
                                    localTime = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(ward_data_result.dob);
                                } catch (java.text.ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

                                String dob = sdf.format(new Date(localTime.getTime()));
                                Log.e("-----date-------------", dob);
                                tv_dob.setText(dob);

                            } else {
                                tv_dob.setText("-");
                            }
                            //*********************************House Color********************************************
                            if (!ward_data_result.house_color.equals("NA") && !ward_data_result.house_color.equals("null") &&
                                    !ward_data_result.house_color.equals(null) && !ward_data_result.house_color.isEmpty()) {
                                tv_house_color.setText(ward_data_result.house_color);
                            } else {
                                tv_house_color.setText("-");
                            }
                            //**********************************Father Name*******************************************
                            if (!ward_data_result.parent_father_name.equals("NA") && !ward_data_result.parent_father_name.equals("null") &&
                                    !ward_data_result.parent_father_name.equals(null) && !ward_data_result.parent_father_name.isEmpty()) {
                                tv_father_name.setText(ward_data_result.parent_father_name);
                                sessionManager.payDetailsParentName(ward_data_result.parent_father_name);
                            } else {
                                tv_father_name.setText("-");
                                sessionManager.payDetailsParentName("NA");
                            }
                            //********************************Mother Name**********************************************
                            if (!ward_data_result.parent_mother_name.equals("NA") && !ward_data_result.parent_mother_name.equals("null") &&
                                    !ward_data_result.parent_mother_name.equals(null) && !ward_data_result.parent_mother_name.isEmpty()) {
                                tv_mother_name.setText(ward_data_result.parent_mother_name);
                            } else {
                                tv_mother_name.setText("-");
                            }
                            //***********************************Parent Mobile Number**********************************
                            if (!ward_data_result.parent_mobileno.equals("NA") && !ward_data_result.parent_mobileno.equals("null") &&
                                    !ward_data_result.parent_mobileno.equals(null) && !ward_data_result.parent_mobileno.isEmpty()) {
                                tv_mobile_number.setText(ward_data_result.parent_mobileno);
                                sessionManager.payDetailsMobile(ward_data_result.parent_mobileno);
                            } else {
                                tv_mobile_number.setText("-");
                                sessionManager.payDetailsMobile("NA");
                            }
                            //*************************************Email Id**********************************************
                            if (!ward_data_result.parent_email.equals("NA") && !ward_data_result.parent_email.equals("null") &&
                                    !ward_data_result.parent_email.equals(null) && !ward_data_result.parent_email.isEmpty()) {
                                tv_email_id.setText(ward_data_result.parent_email);
                                sessionManager.payDetails(ward_data_result.parent_email);
                            } else {
                                tv_email_id.setText("-");
                                sessionManager.payDetails("NA");
                            }
                        }
                    } else {
                        llkidsLayout.setVisibility(View.GONE);
                        llTryAgain.setVisibility(View.VISIBLE);
                    }
                } else {
                    llkidsLayout.setVisibility(View.GONE);
                    llTryAgain.setVisibility(View.VISIBLE);
                }

                progressdialog.dismiss();
            }

            @Override
            public void onFailure(Call<WardDetails_RetroModel> call, Throwable t) {
                call.cancel();
                llkidsLayout.setVisibility(View.GONE);
                llTryAgain.setVisibility(View.VISIBLE);
                progressdialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
