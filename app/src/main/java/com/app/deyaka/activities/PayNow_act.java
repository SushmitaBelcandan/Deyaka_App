package com.app.deyaka.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.deyaka.R;
import com.app.deyaka.retrofit.APIClient;
import com.app.deyaka.retrofit.APIInterface;
import com.app.deyaka.retrofit.PaymentOption_RetroModel;
import com.app.deyaka.session_management.SessionManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayNow_act extends AppCompatActivity implements PaymentResultListener {

    Button btnPayNow;
    SessionManager session;

    private String formattedDate, formatedDatetext;
    private String str_kid_name, str_kid_schl, str_fee_name, str_fee_amount;
    private TextView tvKidName, tvSchlName, tvFeeName, tvFeeAmount, tvPaidDate;

    APIInterface apiInterface;
    Toolbar toolbarPayNow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_now_act);
        session = new SessionManager(PayNow_act.this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        tvKidName = findViewById(R.id.tvKidName);
        tvSchlName = findViewById(R.id.tvSchlName);
        tvFeeName = findViewById(R.id.tvFeeName);
        tvFeeAmount = findViewById(R.id.tvFeeAmount);
        tvPaidDate = findViewById(R.id.tvPaidDate);

        str_kid_name = session.getKidName();
        str_kid_schl = session.getKidSchlName();
        str_fee_amount = session.getPayAmount();
        str_fee_name = session.getPayFeeTitle();

        //current date generate
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df_txt = new SimpleDateFormat("dd MMM yyyy");
        formattedDate = df.format(c);
        formatedDatetext = df_txt.format(c);


        if (!str_kid_name.equals("null") && !str_kid_name.equals(null) && !str_kid_name.equals("NA") && !str_kid_name.isEmpty()) {
            tvKidName.setText(str_kid_name);
        } else {
            tvKidName.setText("");
        }

        if (!str_kid_schl.equals("null") && !str_kid_schl.equals(null) && !str_kid_schl.equals("NA") && !str_kid_schl.isEmpty()) {
            tvSchlName.setText(str_kid_schl);
        } else {
            tvSchlName.setText("");
        }

        if (!str_fee_amount.equals("null") && !str_fee_amount.equals(null) && !str_fee_amount.equals("NA") && !str_fee_amount.isEmpty()) {

            double fee_double = Double.parseDouble(str_fee_amount);
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            String moneyString = formatter.format(fee_double);
            tvFeeAmount.setText(moneyString);

        } else {
            tvFeeAmount.setText("");
        }

        if (!str_fee_name.equals("null") && !str_fee_name.equals(null) && !str_fee_name.equals("NA") && !str_fee_name.isEmpty()) {
            tvFeeName.setText(str_fee_name);
        } else {
            tvFeeName.setText("");
        }
        tvPaidDate.setText(formatedDatetext);

        toolbarPayNow = findViewById(R.id.toolbarPayNow);
        setSupportActionBar(toolbarPayNow);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbarPayNow.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnPayNow = findViewById(R.id.btnPayNow);
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fee_details(session.getPayFeeTitle(), session.getPayAmount());
            }
        });
    }

    public void fee_details(String feeTitle, String amount) {
        Activity activity = this;
        Checkout co = new Checkout();

        double fee_amount_dbl = Double.parseDouble(amount);
        double feeAmountRs = Math.round(fee_amount_dbl * 100);
        String fee_amount_str = String.valueOf(feeAmountRs);

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", feeTitle);//fee title
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", fee_amount_str);//"fee_amount_str"

            JSONObject preFill = new JSONObject();
            preFill.put("email", session.getPayEmail());
            preFill.put("contact", session.getPayMobileNo());

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            callPaymentAPI(session.getUserId(), session.getPayInstallDetailId(), session.getPayFeeTitle(),
                    session.getPayStudentId(), session.getPayAmount(), session.getPayMode(), "NA",
                    "NA", "NA", "NA", razorpayPaymentID, "NA",
                    "NA", "NA", "1", formattedDate);
            Intent intentPaymentSuccess = new Intent(PayNow_act.this, Payment_Success_Act.class);
            intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentPaymentSuccess);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            callPaymentAPI(session.getUserId(), session.getPayInstallDetailId(), session.getPayFeeTitle(),
                    session.getPayStudentId(), session.getPayAmount(), session.getPayMode(), "NA",
                    "NA", "NA", "NA", String.valueOf(code), "NA",
                    "NA", "NA", "0", formattedDate);
            Intent intentPaymentSuccess = new Intent(PayNow_act.this, Payment_Failure_Act.class);
            intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentPaymentSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callPaymentAPI(String usrId, String feeInstallmentDetailId, String feeTitle, String studentId, String amount,
                               String paymentMode, String userName, String bankName, String branchName, String invoiceName,
                               String transactionId, String receiptNumber, String invoiceNumber, String paymentId,
                               String paymentStatus, String paymentDate) {

        PaymentOption_RetroModel paymentOption_retroModel = new PaymentOption_RetroModel(usrId, feeInstallmentDetailId, feeTitle,
                studentId, amount, paymentMode, userName, bankName, branchName, invoiceName, transactionId, receiptNumber, invoiceNumber,
                paymentId, paymentStatus, paymentDate);

        Call<PaymentOption_RetroModel> call_payment_option = apiInterface.makePayment(paymentOption_retroModel);
        call_payment_option.enqueue(new Callback<PaymentOption_RetroModel>() {
            @Override
            public void onResponse(Call<PaymentOption_RetroModel> call, Response<PaymentOption_RetroModel> response) {
                PaymentOption_RetroModel pay_resource = response.body();
                if (response.isSuccessful()) {
                    if (pay_resource.status.equals("success")) {
                        Toast.makeText(getApplicationContext(), pay_resource.message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), pay_resource.message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Intent intentPaymentSuccess = new Intent(PayNow_act.this, Payment_Failure_Act.class);
                    intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentPaymentSuccess);
                }
            }

            @Override
            public void onFailure(Call<PaymentOption_RetroModel> call, Throwable t) {
                call.cancel();
                Intent intentPaymentSuccess = new Intent(PayNow_act.this, Payment_Failure_Act.class);
                intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentPaymentSuccess);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
