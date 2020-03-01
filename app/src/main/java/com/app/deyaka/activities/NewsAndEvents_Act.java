package com.app.deyaka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.app.deyaka.R;
import com.app.deyaka.adapters.CustomViewPager;
import com.app.deyaka.adapters.NewsEvents_Adapter;
import com.google.android.material.tabs.TabLayout;

public class NewsAndEvents_Act extends AppCompatActivity {

    private Toolbar toolbar_news_events;
    private TabLayout tabNewsEvents;
    private CustomViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_and_events_act);

        toolbar_news_events = findViewById(R.id.toolbar_news_events);
        viewPager = findViewById(R.id.viewPager);
        tabNewsEvents = findViewById(R.id.tabNewsEvents);
        //toolbar
        setSupportActionBar(toolbar_news_events);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar_news_events.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //tab layout
        tabNewsEvents.addTab(tabNewsEvents.newTab().setText("News"));
        tabNewsEvents.addTab(tabNewsEvents.newTab().setText("Events"));
        tabNewsEvents.setTabGravity(TabLayout.GRAVITY_FILL);
        // tabNewsEvents.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager.setPagingEnabled(false);
        NewsEvents_Adapter newsEvents_adapter = new NewsEvents_Adapter(getSupportFragmentManager(),
                NewsAndEvents_Act.this, tabNewsEvents.getTabCount());
        viewPager.setAdapter(newsEvents_adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabNewsEvents));
        tabNewsEvents.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
