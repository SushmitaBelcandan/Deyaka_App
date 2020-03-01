package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginRequest {

    @SerializedName("mobiles")
    public String mobilenumber;

    @SerializedName("passwords")
    public String password;

    @SerializedName("country_codes")
    public String countrycode;

    public LoginRequest(String mobilenumber, String password, String countrycode) {
        this.mobilenumber = mobilenumber;
        this.password = password;
        this.countrycode = countrycode;

    }

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("response")
    public List<Login> response = null;

    public class Login {

        @SerializedName("mobile")
        public String mobile;

        @SerializedName("country_code")
        public String country_code;

        @SerializedName("status")
        public String status;

        @SerializedName("otp")
        public String otp;
    }
}
