package com.app.deyaka.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.app.deyaka.models.Fees_Fragment_Model;
import com.app.deyaka.retrofit.APIClient;
import com.app.deyaka.retrofit.APIInterface;
import com.app.deyaka.retrofit.Fees_RetroModel;
import com.app.deyaka.session_management.SessionManager;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mindorks.placeholderview.PlaceHolderView;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fees_Act extends AppCompatActivity {

    APIInterface apiInterface;
    ProgressDialog progressdialog;
    SessionManager session;

    private LinearLayout llTotalFeeSection, llFeekidsLayout, llTryAgain;
    private TextView btnTryAgain;
    private TextView tv_kid_name_fee, tv_school_name_fee;
    private ImageView iv_school_logo_fee, iv_kid_image_fee;
    private TextView tvTotalFeeValueMain, tvTotalInstallmentMain;
    private TextView tvEmptyList;
    private PlaceHolderView phvFees;
    private Toolbar toolbarFeeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fee_fragment);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressdialog = new ProgressDialog(Fees_Act.this);
        progressdialog.setMessage("Please Wait...");
        session = new SessionManager(Fees_Act.this);

        tv_kid_name_fee = findViewById(R.id.tv_kid_name_fee);
        iv_school_logo_fee = findViewById(R.id.iv_school_logo_fee);
        iv_kid_image_fee = findViewById(R.id.iv_kid_image_fee);
        tv_school_name_fee = findViewById(R.id.tv_school_name_fee);

        llTotalFeeSection = findViewById(R.id.llTotalFeeSection);
        tvTotalFeeValueMain = findViewById(R.id.tvTotalFeeValueMain);
        tvTotalInstallmentMain = findViewById(R.id.tvTotalInstallmentMain);
        tvEmptyList = findViewById(R.id.tvEmptyList);

        llFeekidsLayout = findViewById(R.id.llFeekidsLayout);
        btnTryAgain = findViewById(R.id.btnTryAgain);
        llTryAgain = findViewById(R.id.llTryAgain);

        toolbarFeeList = findViewById(R.id.toolbarFeeList);
        setSupportActionBar(toolbarFeeList);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarFeeList.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        phvFees = findViewById(R.id.phvFees);

        if (Utils.CheckInternetConnection(getApplicationContext())) {
            getFeesListAPI();
        } else {
            Toast.makeText(getApplicationContext(), "Please Check Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getFeesListAPI() {
        try {
            progressdialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Fees_RetroModel fees_retroModel = new Fees_RetroModel(session.getKidId());
        Call<Fees_RetroModel> call_fees = apiInterface.getFeesList(fees_retroModel);
        call_fees.enqueue(new Callback<Fees_RetroModel>() {
            @Override
            public void onResponse(Call<Fees_RetroModel> call, Response<Fees_RetroModel> response) {
                Fees_RetroModel fees_resources = response.body();
                if (response.isSuccessful()) {
                    if (fees_resources.status.equals("success")) {
                        llTotalFeeSection.setVisibility(View.VISIBLE);
                        llFeekidsLayout.setVisibility(View.VISIBLE);
                        llTryAgain.setVisibility(View.GONE);
                        //***************************************Kid Photo*******************************************
                        if (!fees_resources.kid_photo.equals("NA") && !fees_resources.kid_photo.equals("null") &&
                                !fees_resources.kid_photo.equals(null) && !fees_resources.kid_photo.isEmpty()) {
                            Glide.with(getApplicationContext()).load(fees_resources.kid_photo).error(R.drawable.person).into(iv_kid_image_fee);
                        } else {
                            Glide.with(getApplicationContext()).load(R.drawable.person).into(iv_kid_image_fee);
                        }
                        //***************************************Kid Name*******************************************
                        if (!fees_resources.kid_name.equals("NA") && !fees_resources.kid_name.equals("null") &&
                                !fees_resources.kid_name.equals(null) && !fees_resources.kid_name.isEmpty()) {
                            tv_kid_name_fee.setText(fees_resources.kid_name);
                        } else {
                            tv_kid_name_fee.setText("");
                        }
                        //***************************************School logo*******************************************
                        if (!fees_resources.school_logo.equals("NA") && !fees_resources.school_logo.equals("null") &&
                                !fees_resources.school_logo.equals(null) && !fees_resources.school_logo.isEmpty()) {
                            Glide.with(getApplicationContext()).load(fees_resources.school_logo).error(R.drawable.error_image).into(iv_school_logo_fee);
                        } else {
                            Glide.with(getApplicationContext()).load(R.drawable.error_image).into(iv_school_logo_fee);
                        }
                        //**********************************school name**********************************************
                        if (!fees_resources.school_name.equals("NA") && !fees_resources.school_name.equals("null") &&
                                !fees_resources.school_name.equals(null) && !fees_resources.school_name.isEmpty()) {
                            tv_school_name_fee.setText(fees_resources.school_name);
                        } else {
                            tv_school_name_fee.setText("");

                        }
                        //**********************************Total Fee*********************************************
                        if (!fees_resources.total_fee.equals("NA") && !fees_resources.total_fee.equals("null") &&
                                !fees_resources.total_fee.equals(null) && !fees_resources.total_fee.isEmpty()) {

                            double fee_double = Double.parseDouble(fees_resources.total_fee);
                            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                            String moneyString = formatter.format(fee_double);
                            tvTotalFeeValueMain.setText(moneyString);

                        } else {
                            tvTotalFeeValueMain.setText("");

                        }
                        //**********************************Total Installment********************************************
                        if (!fees_resources.total_installment.equals("NA") && !fees_resources.total_installment.equals("null") &&
                                !fees_resources.total_installment.equals(null) && !fees_resources.total_installment.isEmpty()) {
                            tvTotalInstallmentMain.setText(fees_resources.total_installment);
                        } else {
                            tvTotalInstallmentMain.setText("");

                        }
                        if (!fees_resources.result.equals("null")) {

                            phvFees.setVisibility(View.VISIBLE);
                            tvEmptyList.setVisibility(View.GONE);

                            JsonArray jsonElements = (JsonArray) new Gson().toJsonTree(fees_resources.result);
                            for (int j = 0; j < jsonElements.size(); j++) {


                                String str_installment_id = String.valueOf(jsonElements.get(j).getAsJsonObject()
                                        .get("installement_id")).replace("\"", "");

                                String str_intsallment_num = String.valueOf(jsonElements.get(j).getAsJsonObject()
                                        .get("installement_no")).replace("\"", "");

                                String str_intsallment_fee = String.valueOf(jsonElements.get(j).getAsJsonObject()
                                        .get("installement_fee")).replace("\"", "");

                                String str_paid_date = String.valueOf(jsonElements.get(j).getAsJsonObject()
                                        .get("paid_date")).replace("\"", "");

                                String str_payment_id = String.valueOf(jsonElements.get(j).getAsJsonObject()
                                        .get("payment_id")).replace("\"", "");

                                String str_payment_status = String.valueOf(jsonElements.get(j).getAsJsonObject()
                                        .get("payment_status")).replace("\"", "");

                                phvFees.addView(new Fees_Fragment_Model(Fees_Act.this, str_intsallment_num, str_intsallment_fee,
                                        str_paid_date, str_payment_id, str_payment_status, str_installment_id));

                            }

                        } else {
                            llFeekidsLayout.setVisibility(View.VISIBLE);
                            llTotalFeeSection.setVisibility(View.GONE);
                            phvFees.setVisibility(View.GONE);
                            tvEmptyList.setVisibility(View.VISIBLE);//empty list text visible and hide list view for null data
                            llTryAgain.setVisibility(View.GONE);
                        }
                    } else {
                        llFeekidsLayout.setVisibility(View.VISIBLE);
                        llTotalFeeSection.setVisibility(View.GONE);
                        phvFees.setVisibility(View.GONE);
                        tvEmptyList.setVisibility(View.VISIBLE);//empty list text visible and hide list view for null data
                        llTryAgain.setVisibility(View.GONE);
                    }
                } else {
                    llFeekidsLayout.setVisibility(View.GONE);
                    llTryAgain.setVisibility(View.VISIBLE);
                }
                progressdialog.dismiss();
            }

            @Override
            public void onFailure(Call<Fees_RetroModel> call, Throwable t) {
                call.cancel();
                llFeekidsLayout.setVisibility(View.GONE);
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
