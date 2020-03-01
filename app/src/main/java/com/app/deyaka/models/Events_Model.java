package com.app.deyaka.models;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.webkit.WebView;
import android.widget.TextView;

import com.app.deyaka.R;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.expand.Parent;
import com.mindorks.placeholderview.annotations.expand.SingleTop;

import static android.view.View.GONE;

@NonReusable
@Layout(R.layout.events_model)
public class Events_Model {

    @View(R.id.wvLink)
    public WebView wvLink;

    @View(R.id.tvTitleEvents)
    public TextView tvTitleEvents;

    @View(R.id.tvDescEvents)
    public TextView tvDescEvents;

    public Context mContext;
    public String strDesc, strTitle, strLink;

    public Events_Model(Context context, String str_desc, String str_title, String str_link) {
        this.mContext = context;
        this.strDesc = str_desc;
        this.strTitle = str_title;
        this.strLink = str_link;
    }

    @Resolve
    public void onResolved() {
        if (!strTitle.trim().equals("null") && !strTitle.trim().equals(null) && !strTitle.trim().equals("NA") && !strTitle.trim().isEmpty()) {
            tvTitleEvents.setText(strTitle);
        } else {
            tvTitleEvents.setText("");
        }

        if (!strDesc.trim().equals("null") && !strDesc.trim().equals(null) && !strDesc.trim().equals("NA") && !strDesc.trim().isEmpty()) {
            if (strDesc.length() > 75) {
                Spannable wordtoSpan = new SpannableString(strDesc.substring(0, 75) + "... Read More");
                wordtoSpan.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.sky_blue)),
                        wordtoSpan.length() - 9, wordtoSpan.length() - 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvDescEvents.setText(wordtoSpan);
            } else {
                tvDescEvents.setText(strDesc);
            }
        } else {
            tvDescEvents.setText("");
            if (!strLink.trim().equals("null") && !strLink.trim().equals(null) && !strLink.trim().equals("NA")
                    && !strLink.trim().isEmpty()) {
                wvLink.loadData(strLink, "text/html", null);
                wvLink.setVisibility(android.view.View.VISIBLE);
            } else {
                wvLink.setVisibility(GONE);
            }

        }
    }

    @Click(R.id.tvDescEvents)
    public void onClick() {

        if (!strLink.trim().equals("null") && !strLink.trim().equals(null) && !strLink.trim().equals("NA")
                && !strLink.trim().isEmpty()) {
            wvLink.loadData(strLink, "text/html", null);
            wvLink.setVisibility(android.view.View.VISIBLE);
        } else {
            wvLink.setVisibility(GONE);
        }
        tvDescEvents.setText(strDesc);
    }

}
