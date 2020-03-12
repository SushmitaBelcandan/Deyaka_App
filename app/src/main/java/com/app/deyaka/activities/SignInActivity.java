package com.app.deyaka.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.deyaka.R;
import com.app.deyaka.adapters.Utils;
import com.app.deyaka.retrofit.APIClient;
import com.app.deyaka.retrofit.APIInterface;
import com.app.deyaka.retrofit.LoginRequest;
import com.app.deyaka.session_management.SessionManager;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_sendotp;
    TextView tv_forgotpassword, tv_signuptext;
    CountryCodePicker ccp;
    EditText et_phonenumber, et_countrycode, et_password;
    APIInterface apiInterface;
    SessionManager saveInPref;
    ProgressDialog progressDialog;

    String smobile_no, spassword, scountry_code;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btn_sendotp = (Button) findViewById(R.id.send_otp);
        tv_forgotpassword = (TextView) findViewById(R.id.forgotpassword);
        tv_signuptext = (TextView) findViewById(R.id.signuptext);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        et_phonenumber = (EditText) findViewById(R.id.phonenumber);
        et_countrycode = (EditText) findViewById(R.id.countrycode_sign_in);
        et_password = (EditText) findViewById(R.id.password);

        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setMessage("Please Wait...");

        btn_sendotp.setOnClickListener(this);
        tv_forgotpassword.setOnClickListener(this);
        tv_signuptext.setOnClickListener(this);


        apiInterface = APIClient.getClient().create(APIInterface.class);
        saveInPref = new SessionManager(SignInActivity.this);

        String text = "<font color=#29275F>Don't have Account?</font> <font color=#428DFF>Sign Up</font>";
        tv_signuptext.setText(Html.fromHtml(text));

        checkAndRequestPermissions();


        et_countrycode.setText(ccp.getSelectedCountryCodeWithPlus());

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
                                           @Override
                                           public void onCountrySelected(Country country) {
                                               et_countrycode.setText(ccp.getSelectedCountryCodeWithPlus());
                                           }
                                       }
        );

        et_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (et_password.getRight() - et_password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (et_password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            et_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide, 0);
                            et_password.setSelection(et_password.getText().length());
                        } else {
                            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            et_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show, 0);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_otp:
                smobile_no = et_phonenumber.getText().toString();
                spassword = et_password.getText().toString();
                scountry_code = et_countrycode.getText().toString().replace("+", "");

                if (Utils.CheckInternetConnection(getApplicationContext()) && !smobile_no.isEmpty() && !spassword.isEmpty()) {
                    loginsuccess();
                } else if (smobile_no.isEmpty() || smobile_no == null || smobile_no.length() != 10) {
                    et_phonenumber.setText("");
                    et_phonenumber.setError("Enter correct mobile number");
                } else if (spassword.isEmpty() || spassword == null) {
                    et_password.setText("");
                    et_password.setError("Enter correct password");
                } else if (smobile_no.isEmpty() && spassword.isEmpty()) {
                    et_phonenumber.setText("");
                    et_phonenumber.setError("Enter correct mobile number");
                    et_password.setText("");
                    et_password.setError("Enter correct password");
                } else {
                    Toast.makeText(getApplicationContext(), "Please check internet connection", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.forgotpassword:
                startActivity(new Intent(SignInActivity.this, RecoverPassword.class));
                break;

            case R.id.signuptext:
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                break;

            default:
                break;
        }
    }

    public void loginsuccess() {
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final LoginRequest user = new LoginRequest(smobile_no, spassword, scountry_code);
        Call<LoginRequest> call = apiInterface.getLoginResult(user);
        call.enqueue(new Callback<LoginRequest>() {
            @Override
            public void onResponse(Call<LoginRequest> call, Response<LoginRequest> response) {
                LoginRequest loginRequest = response.body();
                if (response.isSuccessful()) {
                    if (loginRequest.status.equals("success")) {
                        List<LoginRequest.Login> datumList = loginRequest.response;
                        for (LoginRequest.Login datum : datumList) {
                            saveInPref.saveDataResendOTP(smobile_no, spassword, scountry_code);
                            saveInPref.createPref(datum.mobile, datum.country_code);
                        }
                        progressDialog.dismiss();
                        startSMSListener();
                        Toast.makeText(SignInActivity.this, loginRequest.message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignInActivity.this, SingInWithOtp.class));
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignInActivity.this, loginRequest.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(SignInActivity.this)
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
            public void onFailure(Call<LoginRequest> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
                new AlertDialog.Builder(SignInActivity.this)
                        .setMessage("Internet Connection is slow Please try again later")
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean checkAndRequestPermissions() {
        int readsms = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int receivesms = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int callphone = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int sendSms = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (readsms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_SMS);
        }
        if (receivesms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECEIVE_SMS);
        }
        if (callphone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CALL_PHONE);
        }
        if (sendSms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.SEND_SMS);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SignInActivity.this, "SMS Retriever starts", Toast.LENGTH_LONG).show();
            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignInActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
