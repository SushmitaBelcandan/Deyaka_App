package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

public class Logout_RetroModel {

    @SerializedName("user_ids")
    public String user_ids;

    @SerializedName("device_ids")
    public String device_ids;

    public Logout_RetroModel(String usr_id, String device_id)
    {
        this.user_ids = usr_id;
        this.device_ids = device_id;
    }

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;



}
