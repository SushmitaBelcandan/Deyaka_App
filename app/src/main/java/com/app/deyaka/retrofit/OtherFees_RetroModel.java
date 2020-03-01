package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

public class OtherFees_RetroModel {

    @SerializedName("kids_ids")
    public String kids_ids;

    public OtherFees_RetroModel(String kid_id) {
        this.kids_ids = kid_id;
    }

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("result")
    public Object result;

}
