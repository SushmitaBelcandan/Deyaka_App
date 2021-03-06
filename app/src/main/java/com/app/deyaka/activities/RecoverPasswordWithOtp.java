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
import com.app.deyaka.retrofit.ForgotPasswordOtpVerify;
import com.app.deyaka.retrofit.ForgotPasswordRequest;
import com.app.deyaka.retrofit.SignUpOtpVerifyRequest;
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

public class RecoverPasswordWithOtp extends AppCompatActivity implements View.OnClickListener, OtpReceivedInterface {

    ProgressDialog progressDialog;
    LinearLayout btn_verifyandcontinue;
    TextView tv_timer, tv_dr_otp, tv_resendotp, tv_pw, tv_enter_otp;
    APIInterface apiInterface;
    EditText et_otp_one, et_otp_two, et_otp_three, et_otp_four;
    String mobile, countrycode, otp;
    SessionManager saveInPref;
    LinearLayout lltimer;
    RecoverPassword recoverPassword;
    SmsReceiverOtp mSmsBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_otp);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        recoverPassword = new RecoverPassword();

        AppSignatureHashHelper appSignatureHelper = new AppSignatureHashHelper(this);
        appSignatureHelper.getAppSignatures();

        mSmsBroadcastReceiver = new SmsReceiverOtp();

        mSmsBroadcastReceiver.setOnOtpListeners(RecoverPasswordWithOtp.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        saveInPref = new SessionManager(RecoverPasswordWithOtp.this);

        progressDialog = new ProgressDialog(RecoverPasswordWithOtp.this);
        progressDialog.setMessage("Please Wait...");

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
        lltimer = (LinearLayout) findViewById(R.id.ll_timer);

        tv_resendotp.setOnClickListener(this);

        btn_verifyandcontinue = (LinearLayout) findViewById(R.id.verify_and_continue);
        btn_verifyandcontinue.setOnClickListener(this);


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

    public void setForgotPassword(String sMobileNo) {
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
                            saveInPref.createPref(datum.mobile, datum.country_code);
                        }
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), forgotPasswordRequest.message, Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), forgotPasswordRequest.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(RecoverPasswordWithOtp.this)
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
                new AlertDialog.Builder(RecoverPasswordWithOtp.this)
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
                    verifyandcontinue();
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
                String mobileNum = saveInPref.getMobileNumber();
                setForgotPassword(mobileNum);

                break;

            default:
                break;
        }
    }

    public void verifyandcontinue() {
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ForgotPasswordOtpVerify user = new ForgotPasswordOtpVerify(mobile, otp, countrycode);
        Call<ForgotPasswordOtpVerify> call = apiInterface.forgotPasswordOtpVerify(user);
        call.enqueue(new Callback<ForgotPasswordOtpVerify>() {
            @Override
            public void onResponse(Call<ForgotPasswordOtpVerify> call, Response<ForgotPasswordOtpVerify> response) {
                ForgotPasswordOtpVerify forgotPasswordOtpVerify = response.body();
                if (response.isSuccessful()) {
                    if (forgotPasswordOtpVerify.status.equals("success")) {
                        progressDialog.dismiss();
                        Toast.makeText(RecoverPasswordWithOtp.this, forgotPasswordOtpVerify.message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RecoverPasswordWithOtp.this, CreateNewPassword.class));
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RecoverPasswordWithOtp.this, forgotPasswordOtpVerify.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(RecoverPasswordWithOtp.this)
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
            public void onFailure(Call<ForgotPasswordOtpVerify> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
                new AlertDialog.Builder(RecoverPasswordWithOtp.this)
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

  /*  private BroadcastReceiver receiver = new BroadcastReceiver() {
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
    };*/


    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RecoverPasswordWithOtp.this, "SMS Retriever starts", Toast.LENGTH_LONG).show();

            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RecoverPasswordWithOtp.this, "Error", Toast.LENGTH_LONG).show();
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
