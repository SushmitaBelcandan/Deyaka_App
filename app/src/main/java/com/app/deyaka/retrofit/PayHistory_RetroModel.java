package com.app.deyaka.retrofit;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PayHistory_RetroModel {

    @SerializedName("kids_ids")
    public String kids_ids;

    @SerializedName("user_ids")
    public String user_ids;

    @SerializedName("years")
    public String years;

    public PayHistory_RetroModel(String kidId, String usrId, String year) {
        this.kids_ids = kidId;
        this.user_ids = usrId;
        this.years = year;
    }

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("result")
    public List<PayHistory_Datum> result = null;

    public class PayHistory_Datum {

        @SerializedName("Fee Id")
        public String Fee_id;

        @SerializedName("Fee Title")
        public String Fee_title;

        @SerializedName("Amount")
        public String Amount;

        @SerializedName("Paid Date")
        public String paid_date;

        @SerializedName("Invoice Name")
        public String invoice_name;

        @SerializedName("Transaction ID")
        public String txn_id;

        @SerializedName("Receipt Number")
        public String receipt_number;

        @SerializedName("Payment Mode")
        public String payment_mode;

        @SerializedName("Invoice Number")
        public String invoice_number;

        @SerializedName("Payment ID")
        public String payment_id;

    }
}
