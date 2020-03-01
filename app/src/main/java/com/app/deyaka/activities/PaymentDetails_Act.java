package com.app.deyaka.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.deyaka.R;
import com.app.deyaka.adapters.NumberToWordsConverter;
import com.app.deyaka.adapters.Utils;
import com.app.deyaka.retrofit.APIClient;
import com.app.deyaka.retrofit.APIInterface;
import com.app.deyaka.retrofit.FeeDetails_RetroModel;
import com.app.deyaka.session_management.SessionManager;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetails_Act extends AppCompatActivity {

    APIInterface apiInterface;
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    Toolbar toolbar_view_details;

    private String str_pay_id, str_fee_title;

    private LinearLayout llTryAgain, llPayDetails, llNoData;
    private TextView btnTryAgain;

    private TextView tvInstallmentAmountHeader, tvPayStatusHeader, tvInstallmentTitleHeader;
    private TextView tvAmountNumBody, tvAmountWordsBody, tvPaidDateBody, tvInvoiceNameBody, tvTxnIdBody, tvReceiptNumBody, tvPayModeBody, tvInvoiceNumBody;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_details);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = new ProgressDialog(PaymentDetails_Act.this);
        sessionManager = new SessionManager(PaymentDetails_Act.this);

        Intent oIntent = getIntent();//for payment id
        str_pay_id = oIntent.getExtras().getString("PAYMENT_ID");

        tvInstallmentTitleHeader = findViewById(R.id.tvInstallmentTitleHeader);
        tvPayStatusHeader = findViewById(R.id.tvPayStatusHeader);
        tvInstallmentAmountHeader = findViewById(R.id.tvInstallmentAmountHeader);

        tvAmountNumBody = findViewById(R.id.tvAmountNumBody);
        tvAmountWordsBody = findViewById(R.id.tvAmountWordsBody);
        tvPaidDateBody = findViewById(R.id.tvPaidDateBody);
        tvInvoiceNameBody = findViewById(R.id.tvInvoiceNameBody);
        tvTxnIdBody = findViewById(R.id.tvTxnIdBody);
        tvReceiptNumBody = findViewById(R.id.tvReceiptNumBody);
        tvPayModeBody = findViewById(R.id.tvPayModeBody);
        tvInvoiceNumBody = findViewById(R.id.tvInvoiceNumBody);

        llNoData = findViewById(R.id.llNoData);
        btnTryAgain = findViewById(R.id.btnTryAgain);
        llTryAgain = findViewById(R.id.llTryAgain);
        llPayDetails = findViewById(R.id.llPayDetails);

        /********toolbar action going back and finish the activity************************************/
        toolbar_view_details = findViewById(R.id.toolbar_view_details);
        setSupportActionBar(toolbar_view_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar_view_details.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        /*********************************************************************************************/
        if (Utils.CheckInternetConnection(getApplicationContext())) {
            getWardDetailsAPI();
        } else {
            Toast.makeText(getApplicationContext(), "Please Check Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getWardDetailsAPI() {
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FeeDetails_RetroModel feeDetails = new FeeDetails_RetroModel(sessionManager.getKidId(), str_pay_id);
        Call<FeeDetails_RetroModel> call_feeDetails = apiInterface.getFeeDetails(feeDetails);
        call_feeDetails.enqueue(new Callback<FeeDetails_RetroModel>() {
            @Override
            public void onResponse(Call<FeeDetails_RetroModel> call, Response<FeeDetails_RetroModel> response) {
                FeeDetails_RetroModel feedetais_resources = response.body();
                if (response.isSuccessful()) {
                    if (feedetais_resources.status.equals("success")) {

                        List<FeeDetails_RetroModel.FeesDetails_Datum> arrayList_feeDetails = feedetais_resources.result;
                        if (arrayList_feeDetails.size() <= 0) {
                            llPayDetails.setVisibility(View.GONE);
                            llTryAgain.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        } else {
                            llPayDetails.setVisibility(View.VISIBLE);
                            llTryAgain.setVisibility(View.GONE);
                            llNoData.setVisibility(View.GONE);
                            for (FeeDetails_RetroModel.FeesDetails_Datum fee_details_data_result : arrayList_feeDetails) {
                                //*************************************Installment Name*********************************************
                                if (!fee_details_data_result.installement_name.equals("NA") && !fee_details_data_result.installement_name.equals("null") &&
                                        !fee_details_data_result.installement_name.equals(null) && !fee_details_data_result.installement_name.isEmpty()) {
                                    tvInstallmentTitleHeader.setText(fee_details_data_result.installement_name); //setting from intent value
                                } else {
                                    tvInstallmentTitleHeader.setText(fee_details_data_result.installement_name);//setting from intent value
                                }
                                //*************************************Installment  Amount*********************************************
                                if (!fee_details_data_result.installement_fee.equals("NA") && !fee_details_data_result.installement_fee.equals("null") &&
                                        !fee_details_data_result.installement_fee.equals(null) && !fee_details_data_result.installement_fee.isEmpty()) {

                                    double fee_double = Double.parseDouble(fee_details_data_result.installement_fee);
                                    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                                    String moneyString = formatter.format(fee_double);

                                    tvInstallmentAmountHeader.setText(moneyString);
                                    tvAmountNumBody.setText(moneyString);
                                    tvAmountWordsBody.setText(numToWords(Integer.parseInt(fee_details_data_result.installement_fee)) + " Only");
                                } else {
                                    tvInstallmentAmountHeader.setText("Rs." + " " + "-");
                                    tvAmountNumBody.setText("Rs." + " " + "-");
                                    tvAmountWordsBody.setText("-");
                                }
                                //*************************************Installment  Amount in Words*********************************************
                          /*  if (!fee_details_data_result.amount_in_word.equals("NA") && !fee_details_data_result.amount_in_word.equals("null") &&
                                    !fee_details_data_result.amount_in_word.equals(null) && !fee_details_data_result.amount_in_word.isEmpty()) {
                                tvAmountWordsBody.setText(fee_details_data_result.amount_in_word);
                            } else {
                                tvAmountWordsBody.setText("-");
                            }*/
                                //*************************************Installment  Payment Status*********************************************
                                if (!fee_details_data_result.payment_status.equals("NA") && !fee_details_data_result.payment_status.equals("null") &&
                                        !fee_details_data_result.payment_status.equals(null) && !fee_details_data_result.payment_status.isEmpty()) {
                                    tvPayStatusHeader.setText("Paid");
                                } else {
                                    tvPayStatusHeader.setText("Paid");
                                }
                                //*************************************Installment  Payment Date*********************************************
                                if (!fee_details_data_result.paid_date.equals("NA") && !fee_details_data_result.paid_date.equals("null") &&
                                        !fee_details_data_result.paid_date.equals(null) && !fee_details_data_result.paid_date.isEmpty()) {

                                    Date localTime = null;
                                    try {
                                        localTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).parse(fee_details_data_result.paid_date);
                                    } catch (java.text.ParseException e) {
                                        e.printStackTrace();
                                    }
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

                                    String paidDate = sdf.format(new Date(localTime.getTime()));
                                    Log.e("-----date-------------", paidDate);
                                    tvPaidDateBody.setText(paidDate);
                                } else {
                                    tvPaidDateBody.setText("-");
                                }
                                //*************************************Installment  Invoice Number*********************************************
                                if (!fee_details_data_result.invoice_num.equals("NA") && !fee_details_data_result.invoice_num.equals("null") &&
                                        !fee_details_data_result.invoice_num.equals(null) && !fee_details_data_result.invoice_num.isEmpty()) {
                                    tvInvoiceNumBody.setText(fee_details_data_result.invoice_num);
                                } else {
                                    tvInvoiceNumBody.setText("-");
                                }
                                //*************************************Installment  Invoice Name*********************************************
                                if (!fee_details_data_result.invoice_name.equals("NA") && !fee_details_data_result.invoice_name.equals("null") &&
                                        !fee_details_data_result.invoice_name.equals(null) && !fee_details_data_result.invoice_name.isEmpty()) {
                                    tvInvoiceNameBody.setText(fee_details_data_result.invoice_name);
                                } else {
                                    tvInvoiceNameBody.setText("-");
                                }
                                //*************************************Installment Transaction Id*********************************************
                                if (!fee_details_data_result.txn_id.equals("NA") && !fee_details_data_result.txn_id.equals("null") &&
                                        !fee_details_data_result.txn_id.equals(null) && !fee_details_data_result.txn_id.isEmpty()) {
                                    tvTxnIdBody.setText(fee_details_data_result.txn_id);
                                } else {
                                    tvTxnIdBody.setText("-");
                                }
                                //*************************************Installment Receipt Number*********************************************
                                if (!fee_details_data_result.receipt_num.equals("NA") && !fee_details_data_result.receipt_num.equals("null") &&
                                        !fee_details_data_result.receipt_num.equals(null) && !fee_details_data_result.receipt_num.isEmpty()) {
                                    tvReceiptNumBody.setText(fee_details_data_result.receipt_num);
                                } else {
                                    tvReceiptNumBody.setText("-");
                                }
                                //*************************************Installment Receipt Number*********************************************
                                if (!fee_details_data_result.payment_mode.equals("NA") && !fee_details_data_result.payment_mode.equals("null") &&
                                        !fee_details_data_result.payment_mode.equals(null) && !fee_details_data_result.payment_mode.isEmpty()) {
                                    //  tvPayModeBody.setText(fee_details_data_result.payment_mode);
                                    if (fee_details_data_result.payment_mode.equals("1")) {
                                        tvPayModeBody.setText("Online");
                                    } else if (fee_details_data_result.payment_mode.equals("2")) {
                                        tvPayModeBody.setText("Cash");
                                    } else {
                                        tvPayModeBody.setText("Cheque");
                                    }
                                } else {
                                    tvPayModeBody.setText("-");
                                }
                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        llNoData.setVisibility(View.VISIBLE);
                        llPayDetails.setVisibility(View.GONE);
                        llTryAgain.setVisibility(View.GONE);
                    }
                } else {
                    progressDialog.dismiss();
                    llPayDetails.setVisibility(View.GONE);
                    llTryAgain.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<FeeDetails_RetroModel> call, Throwable t) {
                call.cancel();
                llPayDetails.setVisibility(View.GONE);
                llTryAgain.setVisibility(View.VISIBLE);
                llNoData.setVisibility(View.GONE);
                progressDialog.dismiss();
            }
        });
    }

    private String numToWords(int n) { //optional
        NumberToWordsConverter ntw = new NumberToWordsConverter(); // directly implement this
        return ntw.convert(n);
    } //optional

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
