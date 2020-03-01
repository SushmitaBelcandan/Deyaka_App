package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VerifyRequest {

    @SerializedName("mobiles")
    public String mobiles;

    @SerializedName("otps")
    public String otps;

    @SerializedName("country_codes")
    public String country_codes;

    @SerializedName("device_ids")
    public String device_ids;

    @SerializedName("tokens")
    public String tokens;


    public VerifyRequest(String mobiles, String otps, String country_codes, String device_ids, String tokens) {
        this.mobiles = mobiles;
        this.otps = otps;
        this.country_codes = country_codes;
        this.device_ids = device_ids;
        this.tokens = tokens;
    }

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("result")
    public List<Verify> response = null;

    public class Verify {

        @SerializedName("mobile")
        public String mobile;

        @SerializedName("country_code")
        public String country_code;

        @SerializedName("user_id")
        public String user_id;

        @SerializedName("status")
        public String status;

        @SerializedName("device_id")
        public String device_id;

        @SerializedName("token")
        public String token;
    }
}
