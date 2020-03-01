package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YearList_retroModel {

    @SerializedName("kids_ids")
    public String kids_ids;

    public YearList_retroModel(String kidId) {
        this.kids_ids = kidId;
    }

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("response")
    public List<YearList_Datum> response = null;

    public class YearList_Datum {

        @SerializedName("year")
        public String year;

    }
}
