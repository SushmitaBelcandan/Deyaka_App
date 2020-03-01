package com.app.deyaka.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.deyaka.R;
import com.app.deyaka.adapters.Utils;
import com.app.deyaka.retrofit.APIClient;
import com.app.deyaka.retrofit.APIInterface;
import com.app.deyaka.retrofit.ForgotPasswordRequest;
import com.app.deyaka.retrofit.LoginRequest;
import com.app.deyaka.session_management.SessionManager;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecoverPassword extends AppCompatActivity implements View.OnClickListener {

    Button btn_sendotp;
    EditText etMobileNo;
    String sMobileNo;

    APIInterface apiInterface;
    SessionManager sessionManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(RecoverPassword.this);

        progressDialog = new ProgressDialog(RecoverPassword.this);
        progressDialog.setMessage("Please Wait...");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        etMobileNo = (EditText) findViewById(R.id.et_mn);
        btn_sendotp = (Button) findViewById(R.id.rcvr_send_otp);
        btn_sendotp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rcvr_send_otp:

                sMobileNo = etMobileNo.getText().toString();

                if (Utils.CheckInternetConnection(getApplicationContext()) && !sMobileNo.isEmpty() && sMobileNo.length() == 10) {
                    setForgotPassword();
                } else if (sMobileNo.isEmpty() || sMobileNo == null || sMobileNo.length() != 10) {
                    etMobileNo.setText("");
                    etMobileNo.setError("Enter correct mobile number");
                } else {
                    Toast.makeText(getApplicationContext(), "Please check internet connection", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public void setForgotPassword() {
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ForgotPasswordRequest user = new ForgotPasswordRequest(sMobileNo);
        Call<ForgotPasswordRequest> call = apiInterface.getPassword(user);
        call.enqueue(new Callback<ForgotPasswordRequest>() {
            @Override
            public void onResponse(Call<ForgotPasswordRequest> call, Response<ForgotPasswordRequest> response) {
                ForgotPasswordRequest forgotPasswordRequest = response.body();
                if (response.isSuccessful()) {
                    if (forgotPasswordRequest.status.equals("success")) {
                        List<ForgotPasswordRequest.ForgotPassword> datumList = forgotPasswordRequest.response;
                        for (ForgotPasswordRequest.ForgotPassword datum : datumList) {
                            sessionManager.createPref(datum.mobile, datum.country_code);
                        }
                        progressDialog.dismiss();
                        startSMSListener();//start sms retriver service
                        Toast.makeText(RecoverPassword.this, forgotPasswordRequest.message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RecoverPassword.this, RecoverPasswordWithOtp.class));
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RecoverPassword.this, forgotPasswordRequest.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(RecoverPassword.this)
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
            public void onFailure(Call<ForgotPasswordRequest> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
                new AlertDialog.Builder(RecoverPassword.this)
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

    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RecoverPassword.this, "SMS Retriever starts", Toast.LENGTH_LONG).show();
            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RecoverPassword.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
