package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SignUpOtpVerifyRequest {

    @SerializedName("mobiles")
    public String mobilenumber;

    @SerializedName("country_codes")
    public String countrycode;

    @SerializedName("otps")
    public String otp;

    public SignUpOtpVerifyRequest(String mobilenumber, String countrycode, String otp) {
        this.mobilenumber = mobilenumber;
        this.countrycode = countrycode;
        this.otp = otp;
    }

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("response")
    public List<SignUpOtp> response = null;

    public class SignUpOtp {

        @SerializedName("mobile")
        public String mobile;

        @SerializedName("country_code")
        public String country_code;

    }
}
