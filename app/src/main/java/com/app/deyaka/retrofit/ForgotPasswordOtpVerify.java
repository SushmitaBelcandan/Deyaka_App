package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForgotPasswordOtpVerify {

    @SerializedName("mobiles")
    public String mobiles;

    @SerializedName("otps")
    public String otps;

    @SerializedName("country_codes")
    public String country_codes;

    public ForgotPasswordOtpVerify(String mobiles, String otps, String country_codes) {
        this.mobiles = mobiles;
        this.otps = otps;
        this.country_codes = country_codes;
    }

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("response")
    public List<FPOtpVerify> response = null;

    public class FPOtpVerify {

        @SerializedName("mobile")
        public String mobile;

        @SerializedName("country_code")
        public String country_code;
    }
}
