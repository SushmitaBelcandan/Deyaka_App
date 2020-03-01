package com.app.deyaka.models;


import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.deyaka.R;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.expand.Collapse;
import com.mindorks.placeholderview.annotations.expand.Expand;
import com.mindorks.placeholderview.annotations.expand.Parent;
import com.mindorks.placeholderview.annotations.expand.SingleTop;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Parent
@SingleTop
@Layout(R.layout.payment_history_model)
public class Payment_history_Model {


    @View(R.id.tvFeeTitle)
    public TextView tvFeeTitle;

    @View(R.id.tvAmount)
    public TextView tvAmount;

    @View(R.id.tvYear)
    public TextView tvYear;

    @View(R.id.llPayHistHeader)
    public LinearLayout llPayHistHeader;

    @View(R.id.ivToggle)
    public ImageView ivToggle;

    public Context mContext;
    public String strYear, strFeeTitle, strAmount;
    public boolean toggle_status = true;

    public Payment_history_Model(Context context, String year, String feeTitle, String amount) {
        this.mContext = context;
        this.strYear = year;
        this.strFeeTitle = feeTitle;
        this.strAmount = amount;
    }

    @Resolve
    public void onResolved() {

        if (!strYear.equals("null") && !strYear.equals(null) && !strYear.isEmpty() && !strYear.equals("NA")) {

            Date localTime = null;
            try {
                localTime = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(strYear);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String paidDate = sdf.format(new Date(localTime.getTime()));
            tvYear.setText(paidDate);
        } else {
            tvYear.setText("-");
        }

        if (!strFeeTitle.equals("null") && !strFeeTitle.equals(null) && !strFeeTitle.isEmpty() && !strFeeTitle.equals("NA")) {
            tvFeeTitle.setText(strFeeTitle);
        } else {
            tvFeeTitle.setText("-");
        }

        if (!strAmount.equals("null") && !strAmount.equals(null) && !strAmount.isEmpty() && !strAmount.equals("NA")) {

            double fee_double = Double.parseDouble(strAmount);
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            String moneyString = formatter.format(fee_double);
            tvAmount.setText(moneyString);
        } else {
            tvAmount.setText("-");
        }
    }

    @Expand
    public void onExpand() {
        llPayHistHeader.setBackgroundResource(R.drawable.upr_round_corner);
        ivToggle.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow_drop_up));
    }

    @Collapse
    public void onCollapse() {
        llPayHistHeader.setBackgroundResource(R.drawable.pay_hist_gray_bg);
        ivToggle.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow_drop_down));
    }

}
