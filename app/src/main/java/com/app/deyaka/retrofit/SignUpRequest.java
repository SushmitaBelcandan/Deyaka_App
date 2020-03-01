package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SignUpRequest {

    @SerializedName("mobiles")
    public String mobilenumber;

    @SerializedName("country_codes")
    public String countrycode;

    public SignUpRequest(String mobilenumber, String countrycode) {
        this.mobilenumber = mobilenumber;
        this.countrycode = countrycode;
    }

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("result")
    public List<SignUp> result = null;

    public class SignUp {

        @SerializedName("mobile")
        public String mobile;

        @SerializedName("country_code")
        public String country_code;

        @SerializedName("otp")
        public String otp;
    }
}
