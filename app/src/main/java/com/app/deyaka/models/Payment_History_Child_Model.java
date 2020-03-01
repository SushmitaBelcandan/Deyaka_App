package com.app.deyaka.models;


import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.app.deyaka.R;
import com.app.deyaka.adapters.NumberToWordsConverter;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Layout(R.layout.pay_hist_child_model)
public class Payment_History_Child_Model {

    @View(R.id.tvInvoiceNum)
    public TextView tvInvoiceNum;

    @View(R.id.tvPaymentMode)
    public TextView tvPaymentMode;

    @View(R.id.tvReceiptNum)
    public TextView tvReceiptNum;

    @View(R.id.tvTxnId)
    public TextView tvTxnId;

    @View(R.id.tvInvoiceName)
    public TextView tvInvoiceName;

    @View(R.id.tvDate)
    public TextView tvDate;

    @View(R.id.tvAmntWords)
    public TextView tvAmntWords;

    @View(R.id.tvAmountInNum)
    public TextView tvAmountInNum;

    public Context mContext;
    public String strAmount, strPaidDate, strInvName, strTxnId, strReceiptNum, strPayMode, strInvNum;

    public Payment_History_Child_Model(Context context, String str_amount, String paid_date, String inv_name,
                                       String txn_id, String receipt_num, String pay_mode, String inv_num) {
        this.mContext = context;
        this.strAmount = str_amount;
        this.strPaidDate = paid_date;
        this.strInvName = inv_name;
        this.strTxnId = txn_id;
        this.strReceiptNum = receipt_num;
        this.strPayMode = pay_mode;
        this.strInvNum = inv_num;
    }

    @Resolve
    public void onResolved() {
        //**********************Amount in Number*************************************************************
        if (!strAmount.equals("null") && !strAmount.equals(null) && !strAmount.isEmpty() && !strAmount.equals("NA")) {
            double fee_double = Double.parseDouble(strAmount);
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            String moneyString = formatter.format(fee_double);
            tvAmountInNum.setText(moneyString);
            tvAmntWords.setText(numToWords(Integer.parseInt(strAmount)) + " Only");
        } else {
            tvAmountInNum.setText("-");
            tvAmntWords.setText("-");
        }
        //*******************Paid Date****************************************************************
        if (!strPaidDate.equals("null") && !strPaidDate.equals(null) && !strPaidDate.isEmpty() && !strPaidDate.equals("NA")) {

            Date localTime = null;
            try {
                localTime = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(strPaidDate);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            String paidDate = sdf.format(new Date(localTime.getTime()));
            tvDate.setText(paidDate);
        } else {
            tvDate.setText("-");
        }
        //**********************Invoice Name*************************************************************
        if (!strInvName.equals("null") && !strInvName.equals(null) && !strInvName.isEmpty() && !strInvName.equals("NA")) {
            tvInvoiceName.setText(strInvName);
        } else {
            tvInvoiceName.setText("-");
        }
        //**********************Transaction ID*************************************************************
        if (!strTxnId.equals("null") && !strTxnId.equals(null) && !strTxnId.isEmpty() && !strTxnId.equals("NA")) {
            tvTxnId.setText(strTxnId);
        } else {
            tvTxnId.setText("-");
        }
        //**********************Receipt Number*************************************************************
        if (!strReceiptNum.equals("null") && !strReceiptNum.equals(null) && !strReceiptNum.isEmpty() && !strReceiptNum.equals("NA")) {
            tvReceiptNum.setText(strReceiptNum);
        } else {
            tvReceiptNum.setText("-");
        }
        //**********************Payment Mode*************************************************************
        if (!strPayMode.equals("null") && !strPayMode.equals(null) && !strPayMode.isEmpty() && !strPayMode.equals("NA")) {
            if (strPayMode.equals("1")) {
                tvPaymentMode.setText("Online");
            } else if (strPayMode.equals("2")) {
                tvPaymentMode.setText("Cash");
            } else {
                tvPaymentMode.setText("Cheque");
            }

        } else {
            tvPaymentMode.setText("-");
        }
        //**********************Payment Mode*************************************************************
        if (!strInvNum.equals("null") && !strInvNum.equals(null) && !strInvNum.isEmpty() && !strInvNum.equals("NA")) {
            tvInvoiceNum.setText(strInvNum);
        } else {
            tvInvoiceNum.setText("-");
        }
    }

    private String numToWords(int n) { //optional
        NumberToWordsConverter ntw = new NumberToWordsConverter(); // directly implement this
        return ntw.convert(n);
    } //optional

}
