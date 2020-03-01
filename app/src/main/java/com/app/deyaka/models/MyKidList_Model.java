package com.app.deyaka.models;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.app.deyaka.R;
import com.app.deyaka.activities.SingleKid_Act;
import com.app.deyaka.session_management.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.zuowei.circleimageview.CircleImageView;

import java.util.Random;


@NonReusable
@Layout(R.layout.my_kids_model)
public class MyKidList_Model {

    @View(R.id.card_kid)
    public CardView card_kid;

    @View(R.id.tv_kid_name)
    public TextView tv_kid_name;

    @View(R.id.tv_class_name)
    public TextView tv_class_name;

    @View(R.id.iv_kid)
    public CircleImageView iv_kid;

    SessionManager sessionManager;
    public Context mContext;
    public String kidname, kids_class, kids_img, kids_id;
    public String[] str_bg_color;

    public MyKidList_Model(Context contxt, String kid_name, String kidClass, String kidImage, String kidId, String[] bg_color) {
        this.mContext = contxt;
        this.kidname = kid_name;
        this.kids_class = kidClass;
        this.kids_img = kidImage;
        this.kids_id = kidId;
        this.str_bg_color = bg_color;

    }

    @Resolve
    public void onResolved() {
        Random random = new Random();
        int num = random.nextInt(str_bg_color.length - 0) + 0;
        card_kid.setCardBackgroundColor(Color.parseColor(str_bg_color[num]));

        sessionManager = new SessionManager(mContext);

        if (!kidname.equals("null") && !kidname.equals(null) && !kidname.equals("NA") && !kidname.isEmpty()) {
            tv_kid_name.setText(kidname);
            sessionManager.putKidName(kidname);
        } else {
            tv_kid_name.setText("");
            sessionManager.putKidName("NA");
        }
        if (!kids_class.equals("null") && !kids_class.equals(null) && !kids_class.equals("NA") && !kids_class.isEmpty()) {

            tv_class_name.setText("Class" + " " + ":" + kids_class);
            sessionManager.putKidSchlName(kids_class);
        } else {
            tv_class_name.setText("");
            sessionManager.putKidSchlName("NA");
        }
        if (!kids_img.equals("null") && !kids_img.equals(null) && !kids_img.equals("NA") && !kids_img.isEmpty()) {
            Glide.with(mContext).applyDefaultRequestOptions(new RequestOptions()
                    .placeholder(R.drawable.default_kid).error(R.drawable.default_kid)).load(kids_img).into(iv_kid);
        } else {
            Glide.with(mContext).load(R.drawable.default_kid).into(iv_kid);
        }
    }

    @Click(R.id.card_kid)
    public void onClick() {
        Intent intentFormAct = new Intent(mContext, SingleKid_Act.class);
        sessionManager.putKidId(kids_id);
        intentFormAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intentFormAct);
    }

}
