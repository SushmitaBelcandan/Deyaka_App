package com.app.deyaka.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.deyaka.R;
import com.app.deyaka.models.Payment_History_Child_Model;
import com.app.deyaka.models.Payment_history_Model;
import com.app.deyaka.retrofit.APIClient;
import com.app.deyaka.retrofit.APIInterface;
import com.app.deyaka.retrofit.PayHistory_RetroModel;
import com.app.deyaka.retrofit.YearList_retroModel;
import com.app.deyaka.session_management.SessionManager;
import com.mindorks.placeholderview.ExpandablePlaceHolderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentHistory_Act extends AppCompatActivity {

    public ExpandablePlaceHolderView phvPaymentHistory;
    Spinner spnrPaymentHistory;
    APIInterface apiInterface;
    SessionManager session;

    ProgressDialog progressDialog;
    Toolbar toolbar_payment_history;
    LinearLayout llNoData, llView;
    private TextView btnTryAgain;
    private LinearLayout llTryAgain, llPayHistory;

    ArrayList<String> arrList_year;
    String[] strArr_year = {"2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013", "2012", "2011"};
    String strYear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_history_act);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        session = new SessionManager(PaymentHistory_Act.this);
        progressDialog = new ProgressDialog(PaymentHistory_Act.this);
        progressDialog.setMessage("Please Wait...");

        phvPaymentHistory = findViewById(R.id.phvPaymentHistory);
        llNoData = findViewById(R.id.llNoData);
        llView = findViewById(R.id.llView);

        btnTryAgain = findViewById(R.id.btnTryAgain);
        llTryAgain = findViewById(R.id.llTryAgain);
        llPayHistory = findViewById(R.id.llPayHistory);


        spnrPaymentHistory = findViewById(R.id.spnrPaymentHistory);
        toolbar_payment_history = findViewById(R.id.toolbar_payment_history);
        //Tool bar backarrow navigation
        setSupportActionBar(toolbar_payment_history);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar_payment_history.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //***************************************************************************************Spinner item List****************************//
        getYearList();
    }

    public void getYearList() {
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final YearList_retroModel year_list_model = new YearList_retroModel(session.getKidId());
        Call<YearList_retroModel> call_year_list = apiInterface.getYearList(year_list_model);
        call_year_list.enqueue(new Callback<YearList_retroModel>() {
            @Override
            public void onResponse(Call<YearList_retroModel> call, Response<YearList_retroModel> response) {
                YearList_retroModel year_list_resources = response.body();
                if (response.isSuccessful()) {
                    if (year_list_resources.status.equals("success")) {

                        List<YearList_retroModel.YearList_Datum> arrayList_year = year_list_resources.response;
                        arrList_year = new ArrayList<String>();
                        if (arrayList_year.size() <= 0) {
                            progressDialog.dismiss();
                            llPayHistory.setVisibility(View.VISIBLE);
                            llTryAgain.setVisibility(View.GONE);
                            llView.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);

                        } else {
                            llPayHistory.setVisibility(View.VISIBLE);
                            llTryAgain.setVisibility(View.GONE);
                            llView.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                            for (YearList_retroModel.YearList_Datum year_list_result : arrayList_year) {

                                arrList_year.add(year_list_result.year); //add each year into an arraylist

                            }
                            ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrList_year);
                            yearAdapter.setDropDownViewResource(R.layout.year_list);
                            spnrPaymentHistory.setAdapter(yearAdapter);
                            progressDialog.dismiss();
                            spnrPaymentHistory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    ((TextView) view).setTextColor(getResources().getColor(R.color.purle)); //Change selected text color
                                    ((TextView) view).setTextAppearance(getApplicationContext(), R.style.year_drop_down);
                                    strYear = spnrPaymentHistory.getSelectedItem().toString(); //selected string
                                    showHistoryData(strYear);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    } else {
                        //empty list
                        progressDialog.dismiss();
                        llPayHistory.setVisibility(View.VISIBLE);
                        llTryAgain.setVisibility(View.GONE);
                        llView.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);

                    }
                } else {
                    progressDialog.dismiss();
                    llPayHistory.setVisibility(View.GONE);
                    llTryAgain.setVisibility(View.VISIBLE);
                    llView.setVisibility(View.GONE);
                    llNoData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<YearList_retroModel> call, Throwable t) {
                call.cancel();
                llPayHistory.setVisibility(View.GONE);
                llTryAgain.setVisibility(View.VISIBLE);
                llView.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);
                progressDialog.dismiss();
            }
        });
    }

    public void showHistoryData(String str_year) {
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final PayHistory_RetroModel pay_hist_model = new PayHistory_RetroModel(session.getKidId(), session.getUserId(), str_year);
        Call<PayHistory_RetroModel> call_pay_hist = apiInterface.showPayHistory(pay_hist_model);
        call_pay_hist.enqueue(new Callback<PayHistory_RetroModel>() {
            @Override
            public void onResponse(Call<PayHistory_RetroModel> call, Response<PayHistory_RetroModel> response) {
                PayHistory_RetroModel pay_hist_resources = response.body();
                if (response.isSuccessful()) {
                    if (!pay_hist_resources.result.equals("null")) {

                        llView.setVisibility(View.VISIBLE);
                        llNoData.setVisibility(View.GONE);
                        llPayHistory.setVisibility(View.VISIBLE);
                        llTryAgain.setVisibility(View.GONE);

                        List<PayHistory_RetroModel.PayHistory_Datum> arrayList_ward = pay_hist_resources.result;
                        for (PayHistory_RetroModel.PayHistory_Datum pay_hist_result : arrayList_ward) {
                            phvPaymentHistory.addView(new Payment_history_Model(PaymentHistory_Act.this,
                                    pay_hist_result.paid_date, pay_hist_result.Fee_title, pay_hist_result.Amount));
                            phvPaymentHistory.addView(new Payment_History_Child_Model(PaymentHistory_Act.this,
                                    pay_hist_result.Amount, pay_hist_result.paid_date, pay_hist_result.invoice_name,
                                    pay_hist_result.txn_id, pay_hist_result.receipt_number, pay_hist_result.payment_mode,
                                    pay_hist_result.invoice_number));
                        }

                    } else {
                        llPayHistory.setVisibility(View.VISIBLE);
                        llTryAgain.setVisibility(View.GONE);
                        llView.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);


                    }

                } else {
                    llPayHistory.setVisibility(View.GONE);
                    llTryAgain.setVisibility(View.VISIBLE);
                    llView.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PayHistory_RetroModel> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
                llPayHistory.setVisibility(View.GONE);
                llTryAgain.setVisibility(View.VISIBLE);
                llView.setVisibility(View.GONE);
                llNoData.setVisibility(View.VISIBLE);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
