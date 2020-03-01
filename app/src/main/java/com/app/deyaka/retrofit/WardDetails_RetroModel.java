package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WardDetails_RetroModel {

    @SerializedName("kid_ids")
    public String kid_ids;

    public WardDetails_RetroModel(String kid_id) {
        this.kid_ids = kid_id;
    }

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("result")
    public List<WardDetailsDatum> result = null;

    public class WardDetailsDatum {

        @SerializedName("dob")
        public String dob;

        @SerializedName("house_color")
        public String house_color;

        @SerializedName("parent_father_name")
        public String parent_father_name;

        @SerializedName("parent_mother_name")
        public String parent_mother_name;

        @SerializedName("parent_mobileno")
        public String parent_mobileno;

        @SerializedName("parent_email")
        public String parent_email;

        @SerializedName("school_name")
        public String school_name;

        @SerializedName("standard")
        public String standard;

        @SerializedName("section")
        public String section;

        @SerializedName("kid_id")
        public String kid_id;

        @SerializedName("kid_name")
        public String kid_name;

        @SerializedName("school_logo")
        public String school_logo;

        @SerializedName("kid_photo")
        public String kid_photo;

    }

}
