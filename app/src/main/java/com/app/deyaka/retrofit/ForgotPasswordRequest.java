package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForgotPasswordRequest {

    @SerializedName("mobiles")
    public String mobiles;

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    public ForgotPasswordRequest(String mobiles) {
        this.mobiles = mobiles;
    }

    @SerializedName("result")
    public List<ForgotPassword> response = null;

    public class ForgotPassword {

        @SerializedName("mobile")
        public String mobile;

        @SerializedName("country_code")
        public String country_code;

        @SerializedName("otp")
        public String otp;
    }
}
