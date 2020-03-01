package com.app.deyaka.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.deyaka.fragments.Events_Fragment;
import com.app.deyaka.fragments.News_Fragment;

public class NewsEvents_Adapter extends FragmentPagerAdapter {
    private Context mContext;
    int mTotalTab;

    public NewsEvents_Adapter(@NonNull FragmentManager fm, Context contxt, int countTab) {
        super(fm);
        this.mContext = contxt;
        this.mTotalTab = countTab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                News_Fragment news_fragment = new News_Fragment();
                return news_fragment;
            case 1:
                Events_Fragment events_fragment = new Events_Fragment();
                return events_fragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return mTotalTab;
    }
}
