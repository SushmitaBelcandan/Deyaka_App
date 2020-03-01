package com.app.deyaka.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.deyaka.R;
import com.app.deyaka.adapters.Utils;
import com.app.deyaka.retrofit.APIClient;
import com.app.deyaka.retrofit.APIInterface;
import com.app.deyaka.retrofit.SignUpNewPasswordRequest;
import com.app.deyaka.session_management.SessionManager;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetPassword extends AppCompatActivity implements View.OnClickListener {

    LinearLayout btn_updatepassword;
    EditText et_newpassword, et_confirmpassword;
    String sPassword, sConPassword, sMobileNo, sCountryCode, device_id, token;

    SessionManager saveInPref;
    APIInterface apiInterface;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = new ProgressDialog(SetPassword.this);
        progressDialog.setMessage("Please Wait...");

        saveInPref = new SessionManager(SetPassword.this);

        device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        token = FirebaseInstanceId.getInstance().getToken();
        //  Log.e("------set password token refresh-----",""+token);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        et_newpassword = (EditText) findViewById(R.id.newpassword);
        et_confirmpassword = (EditText) findViewById(R.id.confirmpassword);

        sMobileNo = saveInPref.getMobileNumber();
        sCountryCode = saveInPref.getCountryCode();

        btn_updatepassword = (LinearLayout) findViewById(R.id.updatepassword);
        btn_updatepassword.setOnClickListener(this);

        et_newpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (et_newpassword.getRight() - et_newpassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (et_newpassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            et_newpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            et_newpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide, 0);
                            et_newpassword.setSelection(et_newpassword.getText().length());
                        } else {
                            et_newpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            et_newpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show, 0);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        et_confirmpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (et_confirmpassword.getRight() - et_confirmpassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (et_confirmpassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            et_confirmpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            et_confirmpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide, 0);
                            et_confirmpassword.setSelection(et_confirmpassword.getText().length());
                        } else {
                            et_confirmpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            et_confirmpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show, 0);
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
            case R.id.updatepassword:
                sPassword = et_newpassword.getText().toString();
                sConPassword = et_confirmpassword.getText().toString();

                if (Utils.CheckInternetConnection(getApplicationContext())) {
                    if (sPassword.equals(sConPassword) && !sPassword.isEmpty() && !sConPassword.isEmpty())
                        updatePassword();
                    else {
                        Toast.makeText(SetPassword.this, "The passwords entered do not match", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check internet connection", Toast.LENGTH_SHORT).show();
                }
            default:
                break;

        }
    }

    public void updatePassword() {
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final SignUpNewPasswordRequest signUpNewPasswordRequest = new SignUpNewPasswordRequest(sMobileNo, sCountryCode,
                sConPassword, device_id, token);
        Call<SignUpNewPasswordRequest> call = apiInterface.setNewPassword(signUpNewPasswordRequest);
        call.enqueue(new Callback<SignUpNewPasswordRequest>() {
            @Override
            public void onResponse(Call<SignUpNewPasswordRequest> call, Response<SignUpNewPasswordRequest> response) {
                SignUpNewPasswordRequest signUpNewPasswordRequest = response.body();
                if (response.isSuccessful()) {
                    if (signUpNewPasswordRequest.status.equals("success")) {
                        List<SignUpNewPasswordRequest.SignUpNewPassword> datumList = signUpNewPasswordRequest.result;
                        for (SignUpNewPasswordRequest.SignUpNewPassword datum : datumList) {
                            saveInPref.createOtpPref(datum.mobile, datum.country_code, datum.user_id, datum.device_id, datum.token);
                        }
                        progressDialog.dismiss();
                        Toast.makeText(SetPassword.this, signUpNewPasswordRequest.message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SetPassword.this, MyKids_Act.class));
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SetPassword.this, signUpNewPasswordRequest.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(SetPassword.this)
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
            public void onFailure(Call<SignUpNewPasswordRequest> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
                new AlertDialog.Builder(SetPassword.this)
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
}



