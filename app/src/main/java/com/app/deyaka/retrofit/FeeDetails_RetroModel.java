package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeeDetails_RetroModel {

    @SerializedName("kids_ids")
    public String kids_ids;

    @SerializedName("payment_ids")
    public String payment_ids;

    public FeeDetails_RetroModel(String kid_id, String pay_id) {
        this.kids_ids = kid_id;
        this.payment_ids = pay_id;
    }

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("result")
    public List<FeesDetails_Datum> result = null;

    public class FeesDetails_Datum {
        @SerializedName("installement_no")
        public String installement_no;

        @SerializedName("installement_name")
        public String installement_name;

        @SerializedName("installement_fee")
        public String installement_fee;

        @SerializedName("amount_in_word")
        public String amount_in_word;

        @SerializedName("payment_status")
        public String payment_status;

        @SerializedName("paid_date")
        public String paid_date;

        @SerializedName("invoice_num")
        public String invoice_num;

        @SerializedName("invoice_name")
        public String invoice_name;

        @SerializedName("txn_id")
        public String txn_id;

        @SerializedName("receipt_num")
        public String receipt_num;

        @SerializedName("payment_mode")
        public String payment_mode;

    }
}
