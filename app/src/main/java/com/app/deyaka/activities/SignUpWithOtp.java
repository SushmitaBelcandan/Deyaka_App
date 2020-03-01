package com.app.deyaka.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.app.deyaka.R;
import com.app.deyaka.adapters.Utils;
import com.app.deyaka.retrofit.APIClient;
import com.app.deyaka.retrofit.APIInterface;
import com.app.deyaka.retrofit.SignUpOtpVerifyRequest;
import com.app.deyaka.retrofit.SignUpRequest;
import com.app.deyaka.session_management.SessionManager;
import com.app.deyaka.sms.AppSignatureHashHelper;
import com.app.deyaka.sms.OtpReceivedInterface;
import com.app.deyaka.sms.SmsReceiverOtp;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpWithOtp extends AppCompatActivity implements View.OnClickListener, OtpReceivedInterface {

    ProgressDialog progressDialog;
    LinearLayout btn_verifyandcontinue;
    TextView tv_timer, tv_dr_otp, tv_resendotp, tv_pw, tv_enter_otp;

    APIInterface apiInterface;
    EditText et_otp_one, et_otp_two, et_otp_three, et_otp_four;
    String mobile, countrycode, otp;
    SessionManager saveInPref;
    LinearLayout lltimer;
    SmsReceiverOtp mSmsBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_sign_up);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        saveInPref = new SessionManager(SignUpWithOtp.this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        AppSignatureHashHelper appSignatureHelper = new AppSignatureHashHelper(this);
        appSignatureHelper.getAppSignatures();

        mSmsBroadcastReceiver = new SmsReceiverOtp();

        mSmsBroadcastReceiver.setOnOtpListeners(SignUpWithOtp.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);

        et_otp_one = (EditText) findViewById(R.id.editTextone);
        et_otp_two = (EditText) findViewById(R.id.editTexttwo);
        et_otp_three = (EditText) findViewById(R.id.editTextthree);
        et_otp_four = (EditText) findViewById(R.id.editTextfour);
        tv_enter_otp = (TextView) findViewById(R.id.tv_enter_otp);
        tv_enter_otp.setText("Enter the OTP sent to " + saveInPref.getMobileNumber());

        tv_timer = (TextView) findViewById(R.id.tv_timer);
        tv_dr_otp = (TextView) findViewById(R.id.dr_otp);
        tv_resendotp = (TextView) findViewById(R.id.resendotp);
        tv_pw = (TextView) findViewById(R.id.pw);

        tv_resendotp.setOnClickListener(this);

        lltimer = (LinearLayout) findViewById(R.id.ll_timer);

        btn_verifyandcontinue = (LinearLayout) findViewById(R.id.verify_and_continue);
        btn_verifyandcontinue.setOnClickListener(this);

        progressDialog = new ProgressDialog(SignUpWithOtp.this);
        progressDialog.setMessage("Please Wait...");
        et_otp_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_otp_one.getText().toString().length() == 1) {
                    et_otp_two.requestFocus();
                }
            }
        });

        et_otp_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_otp_two.getText().toString().length() == 0) {
                    et_otp_one.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_otp_two.getText().toString().length() == 1) {
                    et_otp_three.requestFocus();
                }
            }
        });

        et_otp_three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_otp_three.getText().toString().length() == 0) {
                    et_otp_two.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_otp_three.getText().toString().length() == 1) {
                    et_otp_four.requestFocus();
                }
            }
        });

        et_otp_four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_otp_four.getText().toString().length() == 0) {
                    et_otp_three.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // We can call api to verify the OTP here or on an explicit button click
            }
        });


        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

                tv_timer.setText("00:" + millisUntilFinished / 1000 + " ");
            }

            public void onFinish() {
                tv_pw.setVisibility(View.GONE);
                lltimer.setVisibility(View.GONE);
                tv_dr_otp.setVisibility(View.VISIBLE);
                tv_resendotp.setVisibility(View.VISIBLE);
                btn_verifyandcontinue.setEnabled(false);
                btn_verifyandcontinue.setClickable(false);
                btn_verifyandcontinue.setBackgroundResource(R.drawable.disable_otp_verify_button);

            }
        };
        countDownTimer.start();

    }

    public void resendOTP(String sMobile, String sCountryCode) {
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
                            saveInPref.createPref(datum.mobile, datum.country_code);
                        }
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), signUpRequest.message, Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), signUpRequest.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(SignUpWithOtp.this)
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
                new AlertDialog.Builder(SignUpWithOtp.this)
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_and_continue:

                mobile = saveInPref.getMobileNumber();
                countrycode = saveInPref.getCountryCode();

                String otp_one = et_otp_one.getText().toString();
                String otp_two = et_otp_two.getText().toString();
                String otp_three = et_otp_three.getText().toString();
                String otp_four = et_otp_four.getText().toString();

                otp = otp_one + "" + otp_two + "" + otp_three + "" + otp_four;

                if (Utils.CheckInternetConnection(getApplicationContext())) {
                    verifysignupsuccess();
                } else {
                    Toast.makeText(getApplicationContext(), "Please check internet connection", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.resendotp:
                btn_verifyandcontinue.setEnabled(true);
                btn_verifyandcontinue.setClickable(true);
                btn_verifyandcontinue.setBackgroundResource(R.drawable.button_shape);
                tv_dr_otp.setVisibility(View.GONE);
                tv_resendotp.setVisibility(View.GONE);
                CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        tv_pw.setVisibility(View.VISIBLE);
                        lltimer.setVisibility(View.VISIBLE);
                        tv_timer.setText("00:" + millisUntilFinished / 1000 + " ");
                    }

                    public void onFinish() {
                        tv_dr_otp.setVisibility(View.VISIBLE);
                        tv_resendotp.setVisibility(View.VISIBLE);
                        tv_pw.setVisibility(View.GONE);
                        lltimer.setVisibility(View.GONE);
                        btn_verifyandcontinue.setEnabled(false);
                        btn_verifyandcontinue.setClickable(false);
                        btn_verifyandcontinue.setBackgroundResource(R.drawable.disable_otp_verify_button);


                    }
                };
                countDownTimer.start();

                startSMSListener();
                String otp_mobile = saveInPref.getMobileNumber();
                String otp_countrycode = saveInPref.getCountryCode();

                resendOTP(otp_mobile, otp_countrycode);


                break;


        }
    }

    public void verifysignupsuccess() {
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final SignUpOtpVerifyRequest user = new SignUpOtpVerifyRequest(mobile, countrycode, otp);
        Call<SignUpOtpVerifyRequest> call = apiInterface.verifyOtp(user);
        call.enqueue(new Callback<SignUpOtpVerifyRequest>() {
            @Override
            public void onResponse(Call<SignUpOtpVerifyRequest> call, Response<SignUpOtpVerifyRequest> response) {
                SignUpOtpVerifyRequest signUpOtpVerifyRequest = response.body();
                if (response.isSuccessful()) {
                    if (signUpOtpVerifyRequest.status.equals("success")) {
                        List<SignUpOtpVerifyRequest.SignUpOtp> datumList = signUpOtpVerifyRequest.response;
                        for (SignUpOtpVerifyRequest.SignUpOtp datum : datumList) {
                            saveInPref.createPref(datum.mobile, datum.country_code);
                        }
                        progressDialog.dismiss();
                        Toast.makeText(SignUpWithOtp.this, signUpOtpVerifyRequest.message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpWithOtp.this, SetPassword.class));
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpWithOtp.this, signUpOtpVerifyRequest.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(SignUpWithOtp.this)
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
            public void onFailure(Call<SignUpOtpVerifyRequest> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
                new AlertDialog.Builder(SignUpWithOtp.this)
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

    /* private BroadcastReceiver receiver = new BroadcastReceiver() {
         @Override
         public void onReceive(Context context, Intent intent) {
             if (intent.getAction().equalsIgnoreCase("otp")) {
                 final String message = intent.getStringExtra("message");
                 int l = message.length();
                 String msg1 = message.substring(l - 4, l - 3);
                 String msg2 = message.substring(l - 3, l - 2);
                 String msg3 = message.substring(l - 2, l - 1);
                 String msg4 = message.substring(l - 1, l);
                 Log.d("Message", message + "");
                 et_otp_one.setText(msg1);
                 et_otp_two.setText(msg2);
                 et_otp_three.setText(msg3);
                 et_otp_four.setText(msg4);

                 //Do whatever you want with the code here
             }
         }
     };
 */
    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SignUpWithOtp.this, "SMS Retriever starts", Toast.LENGTH_LONG).show();

            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpWithOtp.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onOtpReceived(String otp) {
        Toast.makeText(this, "Otp Received " + otp, Toast.LENGTH_LONG).show();
        int l = otp.length();
        String msg1 = otp.substring(l - 4, l - 3);
        String msg2 = otp.substring(l - 3, l - 2);
        String msg3 = otp.substring(l - 2, l - 1);
        String msg4 = otp.substring(l - 1, l);

        et_otp_one.setText(msg1);
        et_otp_two.setText(msg2);
        et_otp_three.setText(msg3);
        et_otp_four.setText(msg4);
    }

    @Override
    public void onOtpTimeout() {
        Toast.makeText(this, "Time out, please resend", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        // LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mSmsBroadcastReceiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mSmsBroadcastReceiver);
    }


}
