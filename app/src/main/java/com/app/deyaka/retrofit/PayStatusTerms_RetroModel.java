package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

public class PayStatusTerms_RetroModel {

    @SerializedName("kids_ids")
    public String kids_ids;

    @SerializedName("user_ids")
    public String user_ids;

    public PayStatusTerms_RetroModel(String kidId,String usrId)
    {
        this.kids_ids = kidId;
        this.user_ids = usrId;
    }
    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("result")
    public String result;



}
