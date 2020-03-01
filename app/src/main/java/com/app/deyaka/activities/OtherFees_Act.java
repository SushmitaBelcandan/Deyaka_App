package com.app.deyaka.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.deyaka.R;
import com.app.deyaka.adapters.Utils;
import com.app.deyaka.models.OtherFees_Model;
import com.app.deyaka.retrofit.APIClient;
import com.app.deyaka.retrofit.APIInterface;
import com.app.deyaka.retrofit.OtherFees_RetroModel;
import com.app.deyaka.retrofit.WardDetails_RetroModel;
import com.app.deyaka.session_management.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mindorks.placeholderview.PlaceHolderView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherFees_Act extends AppCompatActivity {

    APIInterface apiInterface;
    SessionManager session;
    ProgressDialog progressDialog;

    PlaceHolderView phvOtherFees;
    Toolbar mToolbar;
    private TextView tvEmptyList;
    private TextView btnTryAgain;
    private LinearLayout llTryAgain, llOtherFee;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_fee_act);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        session = new SessionManager(OtherFees_Act.this);
        progressDialog = new ProgressDialog(OtherFees_Act.this);
        progressDialog.setMessage("Please Wait...");

        btnTryAgain = findViewById(R.id.btnTryAgain);
        llTryAgain = findViewById(R.id.llTryAgain);
        llOtherFee = findViewById(R.id.llOtherFee);

        mToolbar = findViewById(R.id.toolbar_other_fee);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvEmptyList = findViewById(R.id.tvEmptyList);
        phvOtherFees = findViewById(R.id.phvOtherFees);

        if (Utils.CheckInternetConnection(getApplicationContext())) {
            getOtherFeesDetails();
        } else {
            Toast.makeText(getApplicationContext(), "Please Check Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getOtherFeesDetails() {
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        OtherFees_RetroModel other_fees_model = new OtherFees_RetroModel(session.getKidId());
        Call<OtherFees_RetroModel> call_other_fees = apiInterface.getOtherFeesList(other_fees_model);
        call_other_fees.enqueue(new Callback<OtherFees_RetroModel>() {
            @Override
            public void onResponse(Call<OtherFees_RetroModel> call, Response<OtherFees_RetroModel> response) {
                OtherFees_RetroModel other_fees_resources = response.body();
                if (response.isSuccessful()) {
                    if (other_fees_resources.status.equals("success")) {

                        llOtherFee.setVisibility(View.VISIBLE);
                        llTryAgain.setVisibility(View.GONE);
                        if (!other_fees_resources.result.equals("null")) {

                            phvOtherFees.setVisibility(View.VISIBLE);
                            tvEmptyList.setVisibility(View.GONE);

                            JsonArray jsonElements = (JsonArray) new Gson().toJsonTree(other_fees_resources.result);
                            for (int j = 0; j < jsonElements.size(); j++) {

                                String str_installment_id = String.valueOf(jsonElements.get(j).getAsJsonObject()
                                        .get("installement_id")).replace("\"", "");

                                String str_fee_title = String.valueOf(jsonElements.get(j).getAsJsonObject()
                                        .get("fee_title")).replace("\"", "");

                                String str_fee_amount = String.valueOf(jsonElements.get(j).getAsJsonObject()
                                        .get("fee_amount")).replace("\"", "");

                                String str_payment_status = String.valueOf(jsonElements.get(j).getAsJsonObject()
                                        .get("payment_status")).replace("\"", "");

                                String str_paid_date = String.valueOf(jsonElements.get(j).getAsJsonObject()
                                        .get("paid_date")).replace("\"", "");

                                String str_payment_id = String.valueOf(jsonElements.get(j).getAsJsonObject()
                                        .get("payment_id")).replace("\"", "");

                                phvOtherFees.addView(new OtherFees_Model(OtherFees_Act.this, str_fee_title, str_fee_amount,
                                        str_paid_date, str_payment_status, str_payment_id, str_installment_id));

                            }

                        } else {
                            phvOtherFees.setVisibility(View.GONE);
                            llOtherFee.setVisibility(View.VISIBLE);
                            tvEmptyList.setVisibility(View.VISIBLE);//empty list text visible and hide list view for null data
                            llTryAgain.setVisibility(View.GONE);
                        }
                    } else {
                        phvOtherFees.setVisibility(View.GONE);
                        llOtherFee.setVisibility(View.VISIBLE);
                        tvEmptyList.setVisibility(View.VISIBLE);
                        llTryAgain.setVisibility(View.GONE);
                    }
                } else {
                    llOtherFee.setVisibility(View.GONE);
                    llTryAgain.setVisibility(View.VISIBLE);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<OtherFees_RetroModel> call, Throwable t) {
                call.cancel();
                llOtherFee.setVisibility(View.GONE);
                llTryAgain.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
