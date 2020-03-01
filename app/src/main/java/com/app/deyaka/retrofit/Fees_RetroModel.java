package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

public class Fees_RetroModel {

    @SerializedName("kids_ids")
    public String kids_ids;

    public Fees_RetroModel(String kid_id)
    {
        this.kids_ids =  kid_id;
    }

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("total_fee")
    public String total_fee;

    @SerializedName("total_installment")
    public String total_installment;

    @SerializedName("total_installment_fee")
    public String total_installment_fee;

    @SerializedName("kid_id")
    public String kid_id;

    @SerializedName("kid_name")
    public String kid_name;

    @SerializedName("school_logo")
    public String school_logo;

    @SerializedName("school_name")
    public String school_name;

    @SerializedName("kid_photo")
    public String kid_photo;

    @SerializedName("result")
    public Object result;

}
