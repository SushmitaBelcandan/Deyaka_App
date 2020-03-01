package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomePageImg_RetroModel {

    @SerializedName("kids_ids")
    public String kids_ids;

    public HomePageImg_RetroModel(String kid_id) {
        this.kids_ids = kid_id;
    }

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("result")
    public List<HomePageDatum> result = null;

    public class HomePageDatum {

        @SerializedName("image_name")
        public String image_name;
    }

}
