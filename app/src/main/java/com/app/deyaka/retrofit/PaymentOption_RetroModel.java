package com.app.deyaka.retrofit;

import com.google.gson.annotations.SerializedName;

public class PaymentOption_RetroModel {

    @SerializedName("user_ids")
    public String user_ids;

    @SerializedName("fee_installment_details_ids")
    public String fee_installment_details_ids;

    @SerializedName("fee_titles")
    public String fee_titles;

    @SerializedName("student_ids")
    public String student_ids;

    @SerializedName("amounts")
    public String amounts;

    @SerializedName("payment_modes")
    public String payment_modes;

    @SerializedName("user_names")
    public String user_names;

    @SerializedName("bank_names")
    public String bank_names;

    @SerializedName("branch_names")
    public String branch_names;

    @SerializedName("invoice_names")
    public String invoice_names;

    @SerializedName("transaction_ids")
    public String transaction_ids;

    @SerializedName("receipt_numbers")
    public String receipt_numbers;

    @SerializedName("invoice_numbers")
    public String invoice_numbers;

    @SerializedName("payment_ids")
    public String payment_ids;

    @SerializedName("payment_statuss")
    public String payment_statuss;

    @SerializedName("payment_date")
    public String payment_date;

    public PaymentOption_RetroModel(String usrId, String feeInstallmentDetailId, String feeTitle, String studentId, String amount,
                                    String paymentMode, String userName, String bankName, String branchName, String invoiceName,
                                    String transactionId, String receiptNumber, String invoiceNumber, String paymentId,
                                    String paymentStatus,String paymentDate) {
        this.user_ids = usrId;
        this.fee_installment_details_ids = feeInstallmentDetailId;
        this.fee_titles = feeTitle;
        this.student_ids = studentId;
        this.amounts = amount;
        this.payment_modes = paymentMode;
        this.user_names = userName;
        this.bank_names = bankName;
        this.branch_names = branchName;
        this.invoice_names = invoiceName;
        this.transaction_ids = transactionId;
        this.receipt_numbers = receiptNumber;
        this.invoice_numbers = invoiceNumber;
        this.payment_ids = paymentId;
        this.payment_statuss = paymentStatus;
        this.payment_date = paymentDate;
    }


    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;
}
