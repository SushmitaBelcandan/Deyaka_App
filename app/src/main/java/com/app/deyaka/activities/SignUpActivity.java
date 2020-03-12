package com.app.deyaka.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.app.deyaka.retrofit.SignUpRequest;
import com.app.deyaka.session_management.SessionManager;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import java.lang.ref.WeakReference;
import java.util.List;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_signup;
    CountryCodePicker ccp;
    EditText et_countrycodesignup, etphonenumber;
    APIInterface apiInterface;
    String sMobile, sCountryCode;
    SessionManager sessionManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(SignUpActivity.this);

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Please Wait...");

        btn_signup = (Button) findViewById(R.id.signup);
        et_countrycodesignup = (EditText) findViewById(R.id.countrycodesignup);
        etphonenumber = (EditText) findViewById(R.id.phonenumber);

        btn_signup.setOnClickListener(this);
        ccp = (CountryCodePicker) findViewById(R.id.ccp_sign_up);
        et_countrycodesignup.setText(ccp.getSelectedCountryNameCode());

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country country) {
                et_countrycodesignup.setText(ccp.getSelectedCountryNameCode());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup:
                sMobile = etphonenumber.getText().toString();
                sCountryCode = et_countrycodesignup.getText().toString().replace("+", "");

                if (Utils.CheckInternetConnection(getApplicationContext()) && !sMobile.isEmpty() && sMobile.length() == 10) {
                    verifysuccess();
                } else if (sMobile.isEmpty() || sMobile == null || sMobile.length() != 10) {

                    etphonenumber.setText("");
                    etphonenumber.setError("Enter correct mobile number");
                } else {

                    Toast.makeText(getApplicationContext(), "Please check internet connection", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void verifysuccess() {
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final SignUpRequest user = new SignUpRequest(sMobile, sCountryCode);
        Call<SignUpRequest> call = apiInterface.checkMobileNumber(user);
        call.enqueue(new Callback<SignUpRequest>() {
            @Override
            public void onResponse(Call<SignUpRequest> call, Response<SignUpRequest> response) {
                SignUpRequest signUpRequest = response.body();
                if (response.isSuccessful()) {
                    if (signUpRequest.status.equals("success")) {
                        List<SignUpRequest.SignUp> datumList = signUpRequest.result;
                        for (SignUpRequest.SignUp datum : datumList) {
                            sessionManager.createPref(datum.mobile, datum.country_code);
                        }
                        startSMSListener();//start sms retriver service
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, signUpRequest.message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, SignUpWithOtp.class));
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, signUpRequest.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(SignUpActivity.this)
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
            public void onFailure(Call<SignUpRequest> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
                new AlertDialog.Builder(SignUpActivity.this)
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
                Toast.makeText(SignUpActivity.this, "SMS Retriever starts", Toast.LENGTH_LONG).show();
            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
