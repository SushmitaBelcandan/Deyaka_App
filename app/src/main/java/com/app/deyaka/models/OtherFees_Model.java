package com.app.deyaka.models;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.app.deyaka.R;
import com.app.deyaka.activities.PayNow_act;
import com.app.deyaka.activities.PaymentDetails_Act;
import com.app.deyaka.activities.Payment_Failure_Act;
import com.app.deyaka.activities.Payment_Success_Act;
import com.app.deyaka.adapters.Utils;
import com.app.deyaka.retrofit.APIClient;
import com.app.deyaka.retrofit.APIInterface;
import com.app.deyaka.retrofit.PayStatusTerms_RetroModel;
import com.app.deyaka.retrofit.PaymentOption_RetroModel;
import com.app.deyaka.session_management.SessionManager;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

@NonReusable
@Layout(R.layout.other_fee_list_items)
public class OtherFees_Model {

    @View(R.id.other_fee_type)
    public TextView other_fee_type;

    @View(R.id.tv_othr_fee_amount_title)
    public TextView tv_othr_fee_amount_title;

    @View(R.id.tv_othr_fee_amnt_val)
    public TextView tv_othr_fee_amnt_val;

    @View(R.id.tv_pending_fee_date_title)
    public TextView tv_pending_fee_date_title;

    @View(R.id.tvPaidDateTitle_othr_fee)
    public TextView tvPaidDateTitle_othr_fee;

    @View(R.id.tv_othr_fee_date_val)
    public TextView tv_othr_fee_date_val;

    @View(R.id.tv_othr_fee_pstatus_title)
    public TextView tv_othr_fee_pstatus_title;

    @View(R.id.tv_othr_fee_pay_status_val)
    public TextView tv_othr_fee_pay_status_val;

    @View(R.id.tvPaymentStatusPending)
    public TextView tvPaymentStatusPending;

    @View(R.id.tvMsg)
    public TextView tvMsg;

    @View(R.id.btnViewDetails)
    public Button btnViewDetails;

    @View(R.id.btnPayNow)
    public Button btnPayNow;

    public Context mContext;
    APIInterface apiInterface;
    SessionManager session;

    public boolean flag_btn_cash = true;
    public boolean flag_btn_chq = true;
    public int pay_option_flag = 0;
    public String str_chq_num, str_name_chq, str_bank_chq, str_branch_chq, str_date_chq;
    public String str_fee_title, str_fee_amount, str_paid_date, str_payment_status, str_payment_id, str_installment_id;
    public String strDate, strDate_1;
    public String str_date_cash, str_amount_cash;

    public Calendar myCalendar = Calendar.getInstance();

    public OtherFees_Model(Context context, String fee_title, String fee_amount, String paid_date,
                           String pay_status, String pay_id, String str_installment_id) {
        this.mContext = context;
        this.str_fee_title = fee_title;
        this.str_fee_amount = fee_amount;
        this.str_paid_date = paid_date;
        this.str_payment_status = pay_status;
        this.str_payment_id = pay_id;
        this.str_installment_id = str_installment_id;
    }

    @Resolve
    public void onResolved() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        session = new SessionManager(mContext);

        if (!str_fee_title.equals("null") && !str_fee_title.equals(null) && !str_fee_title.equals("NA") && !str_fee_title.isEmpty()) {
            other_fee_type.setText(str_fee_title);
            tv_othr_fee_amount_title.setText(str_fee_title);
        } else {
            other_fee_type.setText("-");
            tv_othr_fee_amount_title.setText("-");
        }

        if (!str_fee_amount.equals("null") && !str_fee_amount.equals(null) && !str_fee_amount.equals("NA") && !str_fee_amount.isEmpty()) {
            double fee_double = Double.parseDouble(str_fee_amount);
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            String moneyString = formatter.format(fee_double);
            tv_othr_fee_amnt_val.setText(moneyString);
        } else {
            tv_othr_fee_amnt_val.setText("Rs." + " " + "-");
        }

        if (!str_paid_date.equals("null") && !str_paid_date.equals(null) && !str_paid_date.equals("NA") && !str_paid_date.isEmpty()) {

            Date localTime = null;
            try {
                localTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).parse(str_paid_date);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

            String paidDate = sdf.format(new Date(localTime.getTime()));
            Log.e("-----date-------------", paidDate);
            tv_othr_fee_date_val.setText(paidDate);
        } else {
            tv_othr_fee_date_val.setText("-");
        }

        if (!str_payment_status.equals("null") && !str_payment_status.equals(null) && !str_payment_status.equals("NA") && !str_payment_status.isEmpty()) {
            if (str_payment_status.equals("0")) {
                //pending status
                tvPaymentStatusPending.setVisibility(android.view.View.VISIBLE);
                tv_othr_fee_pay_status_val.setVisibility(android.view.View.GONE);

                tvPaidDateTitle_othr_fee.setVisibility(android.view.View.GONE);
                tv_pending_fee_date_title.setVisibility(android.view.View.VISIBLE);

                btnViewDetails.setVisibility(android.view.View.GONE);
                btnPayNow.setVisibility(android.view.View.VISIBLE);
                tvMsg.setVisibility(android.view.View.GONE);
            } else if (str_payment_status.equals("1")) {
                //paid status
                tv_othr_fee_pay_status_val.setVisibility(android.view.View.VISIBLE);
                tvPaymentStatusPending.setVisibility(android.view.View.GONE);

                tvPaidDateTitle_othr_fee.setVisibility(android.view.View.VISIBLE);
                tv_pending_fee_date_title.setVisibility(android.view.View.GONE);

                btnViewDetails.setVisibility(android.view.View.VISIBLE);
                btnPayNow.setVisibility(android.view.View.GONE);
                tvMsg.setVisibility(android.view.View.GONE);
            } else if (str_payment_status.equals("2")) {
                //admin needs to approve
                tvPaymentStatusPending.setVisibility(android.view.View.VISIBLE);
                tv_othr_fee_pay_status_val.setVisibility(android.view.View.GONE);

                tvPaidDateTitle_othr_fee.setVisibility(android.view.View.GONE);
                tv_pending_fee_date_title.setVisibility(android.view.View.VISIBLE);

                btnViewDetails.setVisibility(android.view.View.GONE);
                //make button pay-now disable
                btnPayNow.setVisibility(android.view.View.VISIBLE);
                btnPayNow.setEnabled(false);
                btnPayNow.setClickable(false);
                btnPayNow.setBackgroundResource(R.drawable.disable_pay_now);

                tvMsg.setVisibility(android.view.View.VISIBLE);
                tvMsg.setText(R.string.admin_needs_to_approve);
            } else {
                //please contact school admin
                tvPaymentStatusPending.setVisibility(android.view.View.VISIBLE);
                tv_othr_fee_pay_status_val.setVisibility(android.view.View.GONE);

                tvPaidDateTitle_othr_fee.setVisibility(android.view.View.GONE);
                tv_pending_fee_date_title.setVisibility(android.view.View.VISIBLE);

                btnViewDetails.setVisibility(android.view.View.GONE);
                //make button pay-now disable
                btnPayNow.setVisibility(android.view.View.VISIBLE);
                btnPayNow.setEnabled(false);
                btnPayNow.setClickable(false);
                btnPayNow.setBackgroundResource(R.drawable.disable_pay_now);

                tvMsg.setVisibility(android.view.View.VISIBLE);
                tvMsg.setText(R.string.please_contact_school_admin);
            }
        } else {
            tvPaymentStatusPending.setVisibility(android.view.View.VISIBLE);
            tv_othr_fee_pay_status_val.setVisibility(android.view.View.GONE);

            tvPaidDateTitle_othr_fee.setVisibility(android.view.View.GONE);
            tv_pending_fee_date_title.setVisibility(android.view.View.VISIBLE);

            btnViewDetails.setVisibility(android.view.View.GONE);
            btnPayNow.setVisibility(android.view.View.VISIBLE);
            tvMsg.setVisibility(android.view.View.GONE);
        }


    }

    @Click(R.id.btnViewDetails)
    public void onClick() {
        Intent intent = new Intent(mContext, PaymentDetails_Act.class);
        intent.putExtra("PAYMENT_ID", str_payment_id);
        //pass viewdetails id for payment details which will be commen field for other fees as well as fees
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Click(R.id.btnPayNow)
    public void onClickPayNow() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        android.view.View promptView = layoutInflater.inflate(R.layout.pay_now_popup, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
        alertDialogBuilder.setView(promptView);

        final CardView cardv_cash = promptView.findViewById(R.id.cardv_cash);
        cardv_cash.setVisibility(android.view.View.GONE);
        CardView cardv_cheque = promptView.findViewById(R.id.cardv_cheque);
        cardv_cheque.setVisibility(android.view.View.GONE);
        CardView cardv_online = promptView.findViewById(R.id.cardv_online);

        final LinearLayout llCash = promptView.findViewById(R.id.llCash);
        final LinearLayout llCheque = promptView.findViewById(R.id.llCheque);

        final LinearLayout llCashDetail = promptView.findViewById(R.id.llCashDetail);
        final LinearLayout llChequeDetails = promptView.findViewById(R.id.llChequeDetails);

        final EditText etDate = promptView.findViewById(R.id.etDate);
        final EditText etAmountCash = promptView.findViewById(R.id.etAmountCash);
        final EditText etChqNo = promptView.findViewById(R.id.etChqNo);
        final EditText etNameChq = promptView.findViewById(R.id.etNameChq);
        final EditText etBankChq = promptView.findViewById(R.id.etBankChq);
        final EditText etBranchChq = promptView.findViewById(R.id.etBranchChq);
        final EditText etDateCheque = promptView.findViewById(R.id.etDateCheque);

        etDate.setHintTextColor(mContext.getResources().getColor(R.color.gray_b));
        etAmountCash.setHintTextColor(mContext.getResources().getColor(R.color.gray_b));
        etChqNo.setHintTextColor(mContext.getResources().getColor(R.color.gray_b));
        etNameChq.setHintTextColor(mContext.getResources().getColor(R.color.gray_b));
        etBankChq.setHintTextColor(mContext.getResources().getColor(R.color.gray_b));
        etBranchChq.setHintTextColor(mContext.getResources().getColor(R.color.gray_b));
        etDateCheque.setHintTextColor(mContext.getResources().getColor(R.color.gray_b));


        final Button btn_cancel = promptView.findViewById(R.id.btn_cancel);
        final Button btn_continue = promptView.findViewById(R.id.btn_continue);
        //calender select--------------------------------------------------------------------
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //set selcted date
                String myFormat = "ddMMyyyy";
                String dateFormat = "yyyy-MM-dd";//In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                SimpleDateFormat sdfDate = new SimpleDateFormat(dateFormat, Locale.US);
                if (!sdf.equals("null")) {
                    strDate = sdf.format(myCalendar.getTime());
                    strDate_1 = sdfDate.format(myCalendar.getTime());
                    if (!strDate_1.equals("null") && !strDate_1.equals(null)) {
                        etDate.setText(strDate_1);
                    } else {
                        etDate.setText("");
                    }
                } else {
                    strDate = "null";
                }
                // setUserId();
            }

        };

        etDate.setFocusable(false);
        etDate.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(android.view.View v) {
                // TODO Auto-generated method stub
                DatePickerDialog dpd = new DatePickerDialog(mContext, R.style.MyThemeOverlay, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis()); //make future date disable
                dpd.show();
                dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // do something onCancel
                        strDate_1 = "null";
                        etDate.setText("");
                    }
                });

            }
        });
        //set date from calender

//----------------------------------------------------------------------------------------------
        cardv_online.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                Intent intent = new Intent(mContext, PayNow_act.class);
                session.saveOnlinePayDetails(str_installment_id, str_fee_title,
                        session.getKidId(), str_fee_amount, "1");
                mContext.startActivity(intent);
            }
        });

        cardv_cash.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (flag_btn_cash == true) {
                    pay_option_flag = 0; //0 for cash
                    llCash.setBackgroundResource(R.drawable.upr_round_white_bg_gray_border);
                    llCashDetail.setVisibility(android.view.View.VISIBLE);
                    llChequeDetails.setVisibility(android.view.View.GONE);
                    llCheque.setBackgroundResource(R.drawable.gray_border_round_edge);
                    flag_btn_cash = false;
                } else {
                    etDate.setText("");
                    etAmountCash.setText("");
                    etChqNo.setText("");
                    etNameChq.setText("");
                    etBankChq.setText("");
                    etBranchChq.setText("");
                    etDateCheque.setText("");

                    llCashDetail.setVisibility(android.view.View.GONE);
                    llCash.setBackgroundResource(R.drawable.gray_border_round_edge);
                    flag_btn_cash = true;
                }
            }
        });

        cardv_cheque.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (flag_btn_chq == true) {
                    pay_option_flag = 1;//1 for cheque
                    llCashDetail.setVisibility(android.view.View.GONE);
                    llChequeDetails.setVisibility(android.view.View.VISIBLE);
                    llCheque.setBackgroundResource(R.drawable.upr_round_white_bg_gray_border);
                    llCash.setBackgroundResource(R.drawable.gray_border_round_edge);

                    flag_btn_chq = false;
                } else {
                    //clear text when tiles getting closed
                    etChqNo.setText("");
                    etNameChq.setText("");
                    etBankChq.setText("");
                    etBranchChq.setText("");
                    etDateCheque.setText("");
                    etDate.setText("");
                    etAmountCash.setText("");

                    llChequeDetails.setVisibility(android.view.View.GONE);
                    llCheque.setBackgroundResource(R.drawable.gray_border_round_edge);
                    flag_btn_chq = true;
                }
            }
        });

        btn_continue.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                String DATE_PATTERN = "^(1[0-9]|0[1-9]|3[0-1]|2[1-9])/(0[1-9]|1[0-2])/[0-9]{4}$";
                Matcher matcher;
                if (pay_option_flag == 0) {
                    //=========================================If it is cash=====================================================
                    if ((etDate.getText().toString().isEmpty() || etDate == null)) {
                        etDate.setError("Enter the date");
                    } else if (etAmountCash.getText().toString().isEmpty() || etAmountCash.getText().toString() == null) {
                        etAmountCash.setError("Enter the amount");
                    } else if (((etDate.getText().toString().isEmpty()) && (etAmountCash.getText().toString().isEmpty()))) {
                        etAmountCash.setError("Enter the amount");
                        etDate.setError("Enter the date");
                    } else {
                        etAmountCash.setError(null);
                        etDate.setError(null);//remove error icon once the text has filled

                        str_date_cash = etDate.getText().toString().trim();
                        str_amount_cash = etAmountCash.getText().toString().trim();
                        if (Utils.CheckInternetConnection(mContext)) {
                            callPaymentAPI(session.getUserId(), str_installment_id, str_fee_title, session.getKidId(), str_amount_cash,
                                    "2", "NA", "NA", "NA", "NA",
                                    "NA", "NA", "NA", "NA",
                                    "1", str_date_cash);
                        } else {
                            Toast.makeText(mContext, "Please Check Internet Connection!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    matcher = Pattern.compile(DATE_PATTERN).matcher(etDateCheque.getText().toString());
                    if (etChqNo.getText().toString().isEmpty() || etChqNo.getText().toString() == null) {
                        etChqNo.setError("Enter the Cheque Number");
                    } else if (etNameChq.getText().toString().isEmpty() || etNameChq.getText().toString() == null) {
                        etNameChq.setError("Enter the Name on Cheque");
                    } else if (etBankChq.getText().toString().isEmpty() || etBankChq.getText().toString() == null) {
                        etBankChq.setError("Enter the Bank Name");
                    } else if (etBranchChq.getText().toString().isEmpty() || etBranchChq.getText().toString() == null) {
                        etBranchChq.setError("Enter the branch");
                    } else if (etDateCheque.getText().toString().isEmpty() || etDateCheque.getText().toString() == null) {
                        etDateCheque.setError("Enter the Date");
                    } else if (!matcher.matches()) {
                        etDateCheque.setError("Invalid Date Format!");
                    } else {
                        Date localTime = null;
                        try {
                            localTime = new SimpleDateFormat("dd/MM/yyyy",
                                    Locale.getDefault()).parse(etDateCheque.getText().toString());
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String paidDateChq = sdf.format(new Date(localTime.getTime()));

                        str_chq_num = etChqNo.getText().toString().trim();
                        str_name_chq = etNameChq.getText().toString().trim();
                        str_bank_chq = etBankChq.getText().toString().trim();
                        str_branch_chq = etBranchChq.getText().toString().trim();
                        str_date_chq = paidDateChq;

                        if (Utils.CheckInternetConnection(mContext)) {
                            callPaymentAPI(session.getUserId(), str_installment_id, str_fee_title, session.getKidId(), str_fee_amount,
                                    "3", str_name_chq, str_bank_chq, str_branch_chq, "NA",
                                    "NA", str_chq_num, "NA", "NA",
                                    "1", str_date_chq);
                        } else {
                            Toast.makeText(mContext, "Please Check Internet Connection!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

        });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //mae alert bg transparent for custom rounded corner
        btn_cancel.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                alertDialog.dismiss();
                llCashDetail.setVisibility(android.view.View.GONE);
                llCash.setBackgroundResource(R.drawable.gray_border_round_edge);
                llChequeDetails.setVisibility(android.view.View.GONE);
                llCheque.setBackgroundResource(R.drawable.gray_border_round_edge);
            }
        });
    }

    public void callPaymentAPI(String usrId, String feeInstallmentDetailId, String feeTitle, String studentId, String amount,
                               final String paymentMode, String userName, String bankName, String branchName, String invoiceName,
                               String transactionId, String receiptNumber, String invoiceNumber, String paymentId,
                               String paymentStatus, String paymentDate) {

        PaymentOption_RetroModel paymentOption_retroModel = new PaymentOption_RetroModel(usrId, feeInstallmentDetailId, feeTitle,
                studentId, amount, paymentMode, userName, bankName, branchName, invoiceName, transactionId, receiptNumber, invoiceNumber,
                paymentId, paymentStatus, paymentDate);

        Call<PaymentOption_RetroModel> call_payment_option = apiInterface.makePayment(paymentOption_retroModel);
        call_payment_option.enqueue(new Callback<PaymentOption_RetroModel>() {
            @Override
            public void onResponse(Call<PaymentOption_RetroModel> call, Response<PaymentOption_RetroModel> response) {
                PaymentOption_RetroModel pay_resource = response.body();
                if (response.isSuccessful()) {
                    if (pay_resource.status.equals("success")) {
                        if (paymentMode.equals("2")) {
                            new AlertDialog.Builder(mContext)
                                    .setMessage("* Payment Conditioned to Admin's Approval")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent intentPaymentSuccess = new Intent(mContext, Payment_Success_Act.class);
                                            intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            mContext.startActivity(intentPaymentSuccess);
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else if (paymentMode.equals("3")) {
                            new AlertDialog.Builder(mContext)
                                    .setMessage("* Payment Conditioned to Admin's Approval")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent intentPaymentSuccess = new Intent(mContext, Payment_Success_Act.class);
                                            intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            mContext.startActivity(intentPaymentSuccess);
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
                            Intent intentPaymentSuccess = new Intent(mContext, Payment_Success_Act.class);
                            intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intentPaymentSuccess);
                        }
                    } else {
                        Intent intentPaymentSuccess = new Intent(mContext, Payment_Failure_Act.class);
                        intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intentPaymentSuccess);
                        Toast.makeText(mContext, pay_resource.message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Intent intentPaymentSuccess = new Intent(mContext, Payment_Failure_Act.class);
                    intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intentPaymentSuccess);
                }
            }

            @Override
            public void onFailure(Call<PaymentOption_RetroModel> call, Throwable t) {
                call.cancel();
                Intent intentPaymentSuccess = new Intent(mContext, Payment_Failure_Act.class);
                intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentPaymentSuccess.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intentPaymentSuccess);
            }
        });
    }

}
