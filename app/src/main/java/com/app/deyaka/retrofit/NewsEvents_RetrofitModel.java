package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

public class NewsEvents_RetrofitModel {

    @SerializedName("kids_ids")
    public String kids_ids;

    @SerializedName("notify_ids")
    public String notify_ids;

    public NewsEvents_RetrofitModel(String kid_id, String notify_id) {
        this.kids_ids = kid_id;
        this.notify_ids = notify_id;
    }

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("result")
    public Object result;
}
