package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SignUpNewPasswordRequest {

    @SerializedName("mobiles")
    public String mobilenumber;

    @SerializedName("country_codes")
    public String countrycode;

    @SerializedName("passwords")
    public String password;

    @SerializedName("device_ids")
    public String device_ids;

    @SerializedName("tokens")
    public String tokens;

    public SignUpNewPasswordRequest(String mobilenumber, String countrycode, String password,
                                    String device_id, String token) {
        this.mobilenumber = mobilenumber;
        this.countrycode = countrycode;
        this.password = password;
        this.device_ids = device_id;
        this.tokens = token;
    }

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("result")
    public List<SignUpNewPassword> result = null;

    public class SignUpNewPassword {

        @SerializedName("mobile")
        public String mobile;

        @SerializedName("country_code")
        public String country_code;

        @SerializedName("user_id")
        public String user_id;

        @SerializedName("device_id")
        public String device_id;

        @SerializedName("token")
        public String token;

    }
}
