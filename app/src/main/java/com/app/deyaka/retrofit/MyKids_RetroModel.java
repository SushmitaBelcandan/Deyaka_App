package com.app.deyaka.retrofit;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyKids_RetroModel {

    @SerializedName("user_ids")
    public String user_ids;

    public MyKids_RetroModel(String usr_id) {
        this.user_ids = usr_id;
    }

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("result")
    public List<KidsListDatum> result = null;

    public class KidsListDatum {

        @SerializedName("kid_id")
        public String kid_id;

        @SerializedName("kid_name")
        public String kid_name;

        @SerializedName("kid_image")
        public String kid_image;

        @SerializedName("kid_class")
        public String kid_class;

        @SerializedName("school_name")
        public String school_name;

        @SerializedName("school_logo")
        public String school_logo;
    }
}
