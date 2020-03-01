package com.app.deyaka.models;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.app.deyaka.R;
import com.app.deyaka.adapters.MySpannable;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.expand.Parent;
import com.mindorks.placeholderview.annotations.expand.SingleTop;


import static android.view.View.GONE;

@NonReusable
@Layout(R.layout.news_model)
public class News_Model {

    @View(R.id.wvLink)
    public WebView wvLink;

    @View(R.id.tvTitleNews)
    public TextView tvTitleNews;

    @View(R.id.tvDescNews)
    public TextView tvDescNews;

    public Context mContext;
    public String strDesc, strTitleNews, strLink;

    public News_Model(Context context, String str_desc, String str_title, String str_link) {
        this.mContext = context;
        this.strDesc = str_desc;
        this.strTitleNews = str_title;
        this.strLink = str_link;
    }

    @Resolve
    public void onResolved() {
        if (!strTitleNews.trim().equals("null") && !strTitleNews.trim().equals(null) && !strTitleNews.trim().equals("NA")
                && !strTitleNews.trim().isEmpty()) {
            tvTitleNews.setText(strTitleNews);
        } else {
            tvTitleNews.setText("");
        }


        if (!strDesc.trim().equals("null") && !strDesc.trim().equals(null) && !strDesc.trim().equals("NA")
                && !strDesc.trim().isEmpty()) {
            if (strDesc.length() > 75) {
                Spannable wordtoSpan = new SpannableString(strDesc.substring(0, 75) + "... Read More");
                wordtoSpan.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.sky_blue)),
                        wordtoSpan.length() - 9, wordtoSpan.length() - 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvDescNews.setText(wordtoSpan);
            } else {
                tvDescNews.setText(strDesc);
            }
        } else {
            tvDescNews.setText("");
            if (!strLink.trim().equals("null") && !strLink.trim().equals(null) && !strLink.trim().equals("NA")
                    && !strLink.trim().isEmpty()) {
                //load data into webview
                wvLink.loadData(strLink, "text/html", null);
                wvLink.setVisibility(android.view.View.VISIBLE);

                wvLink.setVisibility(GONE);
            }

        }
    }

    @Click(R.id.tvDescNews)
    public void onClick() {
        if (!strLink.trim().equals("null") && !strLink.trim().equals(null) && !strLink.trim().equals("NA")
                && !strLink.trim().isEmpty()) {
            wvLink.loadData(strLink, "text/html", null);
            wvLink.setVisibility(android.view.View.VISIBLE);
        } else {
            wvLink.setVisibility(GONE);
        }
        tvDescNews.setText(strDesc);
    }

}
