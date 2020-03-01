package com.app.deyaka.models;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.app.deyaka.R;
import com.app.deyaka.activities.Fees_Act;
import com.app.deyaka.activities.Kids_Details_Act;
import com.app.deyaka.activities.NewsAndEvents_Act;
import com.app.deyaka.activities.OtherFees_Act;
import com.app.deyaka.activities.PaymentHistory_Act;
import com.app.deyaka.session_management.SessionManager;
import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

@NonReusable
@Layout(R.layout.single_kid_model)
public class SingleKid_Model {

    @View(R.id.cv_kid_data)
    public CardView cv_kid_data;

    @View(R.id.ivKidImg)
    public ImageView ivKidImg;

    @View(R.id.tvKidName)
    public TextView tvKidName;

    public Context mContext;
    public String mKidName;
    public Integer mKidImg;
    public Integer mFlag;
    public String strBGColor;

    public SingleKid_Model(Context context, Integer str_kid_img, String str_kid_name, int flag, String str_bg_color) {
        this.mContext = context;
        this.mKidImg = str_kid_img;
        this.mKidName = str_kid_name;
        this.mFlag = flag;
        this.strBGColor = str_bg_color;
    }

    @Resolve
    public void onResolved() {

        cv_kid_data.setCardBackgroundColor(Color.parseColor(strBGColor));


        if (!mKidName.trim().equals("null") && !mKidName.trim().equals(null) && !mKidName.trim().equals("NA")
                && !mKidName.trim().isEmpty()) {
            tvKidName.setText(mKidName);
        } else {
            tvKidName.setText("");
        }
        if (mKidImg != 0) {
            Glide.with(mContext).load(mKidImg).error(R.drawable.broken_image).into(ivKidImg);
        } else {
            Glide.with(mContext).load(R.drawable.broken_image).error(R.drawable.broken_image).into(ivKidImg);
        }
    }

    @Click(R.id.cv_kid_data)
    public void onClick() {
        if (mFlag == 1) {
            Intent intentKidsDetails = new Intent(mContext, Kids_Details_Act.class);
            intentKidsDetails.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intentKidsDetails);
        } else if (mFlag == 2) {
            Intent intentNewsEvents = new Intent(mContext, NewsAndEvents_Act.class);
            intentNewsEvents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intentNewsEvents);
        } else if (mFlag == 3) {
            Intent intentFees = new Intent(mContext, Fees_Act.class);
            intentFees.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intentFees);
        } else if (mFlag == 4) {
            Intent intentPayHist = new Intent(mContext, PaymentHistory_Act.class);
            intentPayHist.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intentPayHist);
        } else {
            Intent intentOtherFees = new Intent(mContext, OtherFees_Act.class);
            intentOtherFees.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intentOtherFees);
        }
    }

}
