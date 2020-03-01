package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForgotNewPasswordRequest {

    @SerializedName("mobiles")
    public String mobilenumber;

    @SerializedName("country_codes")
    public String countrycode;

    @SerializedName("passwords")
    public String passwords;

    public ForgotNewPasswordRequest(String mobilenumber, String countrycode, String passwords) {
        this.mobilenumber = mobilenumber;
        this.countrycode = countrycode;
        this.passwords = passwords;
    }

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("response")
    public List<ForgotNewPassword> response = null;

    public class ForgotNewPassword {

        @SerializedName("mobile")
        public String mobile;

        @SerializedName("country_code")
        public String country_code;

    }
}
