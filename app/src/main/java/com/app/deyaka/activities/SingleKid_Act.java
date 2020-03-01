package com.app.deyaka.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.app.deyaka.R;
import com.app.deyaka.adapters.GridSpacingItemDecoration;
import com.app.deyaka.adapters.SingleKid_ImageSlider_Adapter;
import com.app.deyaka.adapters.Utils;
import com.app.deyaka.models.SingleKid_Model;
import com.app.deyaka.retrofit.APIClient;
import com.app.deyaka.retrofit.APIInterface;
import com.app.deyaka.retrofit.HomePageImg_RetroModel;
import com.app.deyaka.session_management.SessionManager;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mindorks.placeholderview.PlaceHolderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleKid_Act extends AppCompatActivity {

    SessionManager session;
    APIInterface apiInterface;
    ProgressDialog progressDialog;
    ViewPager pager_single_kid;
    TabLayout indicator_single_kid;
    PlaceHolderView phv_data;
    Toolbar toolbar_single_Kid;

    private static String[] XMEN = {"https://www.gstatic.com/webp/gallery3/2.png", "https://www.gstatic.com/webp/gallery3/1.png", "https://www.gstatic.com/webp/gallery3/3.png"};
    private ArrayList<String> XMENArray = new ArrayList<String>();
    private Integer[] arr_icons = {R.drawable.ward_details, R.drawable.news_events, R.drawable.fees, R.drawable.payment_history, R.drawable.other_fees};
    private String[] arr_list_name = {"Ward Details", "News and Events", "Fees", "Payment History", "Other Fees"};
    private String[] arr_bg_color = {"#C4A8EC", "#E5BC92", "#B5CE8D", "#EBD9BB", "#A2C7E9"};
    private Integer[] arr_flag = {1, 2, 3, 4, 5};
    public int defaultGap = 30;
    public static int currentPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_kid_act);

        session = new SessionManager(SingleKid_Act.this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = new ProgressDialog(SingleKid_Act.this);
        progressDialog.setMessage("Please Wait...");

        pager_single_kid = findViewById(R.id.pager_single_kid);
        indicator_single_kid = findViewById(R.id.indicator_single_kid);
        phv_data = findViewById(R.id.phv_data);

        toolbar_single_Kid = findViewById(R.id.toolbar_single_kid);
        setSupportActionBar(toolbar_single_Kid);//manadatory for menu items
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar_single_Kid.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (!session.getSchoolName().equals("NA")) {
            Log.e("---school name----", "" + session.getSchoolName());
            getSupportActionBar().setTitle(session.getSchoolName());
        } else {
            toolbar_single_Kid.setTitle("");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        //************************************************Image Slider************************************************************
     /*   for (int i = 0; i < XMEN.length; i++) {
            XMENArray.add(XMEN[i]);
            Log.e("---images----", String.valueOf(XMEN[i]));
        }*/
        pager_single_kid.setPadding(defaultGap, 0, defaultGap, 0);
        pager_single_kid.setClipToPadding(false);
        pager_single_kid.setPageMargin(55);
        if (Utils.CheckInternetConnection(getApplicationContext())) {
            getHomePageImages();
        } else {
            Toast.makeText(getApplicationContext(), "Please Check Internet Connection!", Toast.LENGTH_SHORT).show();
        }
        //*****************************************************************************PlaceholderView Data******************************
        phv_data.getBuilder()
                .setHasFixedSize(false)
                .setItemViewCacheSize(10)
                .setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        for (int i = 0; i < arr_list_name.length; i++) {
            phv_data.addView(new SingleKid_Model(SingleKid_Act.this, arr_icons[i], arr_list_name[i], arr_flag[i], arr_bg_color[i]));
        }
    }

    public void getHomePageImages() {
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HomePageImg_RetroModel homePage_Model = new HomePageImg_RetroModel(session.getKidId());
        Call<HomePageImg_RetroModel> call_HomePageImgModel = apiInterface.getImages(homePage_Model);
        call_HomePageImgModel.enqueue(new Callback<HomePageImg_RetroModel>() {
            @Override
            public void onResponse(Call<HomePageImg_RetroModel> call, Response<HomePageImg_RetroModel> response) {
                HomePageImg_RetroModel homePageImg_Resources = response.body();
                if (response.isSuccessful()) {
                    if (homePageImg_Resources.status.equals("success")) {
                        int i = 0;
                        List<HomePageImg_RetroModel.HomePageDatum> list_HomaPageImg = homePageImg_Resources.result;

                        for (HomePageImg_RetroModel.HomePageDatum img_data : list_HomaPageImg) {
                            if (i <= 2) {
                                XMENArray.add(img_data.image_name);
                                i++;
                            }
                        }
                    } else {
                        XMENArray.add("NA");
                    }
                } else {
                    XMENArray.add("NA");
                }
                pager_single_kid.setAdapter(new SingleKid_ImageSlider_Adapter(SingleKid_Act.this, XMENArray));
                indicator_single_kid.setupWithViewPager(pager_single_kid);
                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        if (currentPage == XMEN.length) {
                            currentPage = 0;
                        }
                        pager_single_kid.setCurrentItem(currentPage++, true);
                    }
                };

                Timer swipeTimer = new Timer();
                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 2500, 2500);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<HomePageImg_RetroModel> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
                XMENArray.add("NA");
                pager_single_kid.setAdapter(new SingleKid_ImageSlider_Adapter(SingleKid_Act.this, XMENArray));
                indicator_single_kid.setupWithViewPager(pager_single_kid);
                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        if (currentPage == XMEN.length) {
                            currentPage = 0;
                        }
                        pager_single_kid.setCurrentItem(currentPage++, true);
                    }
                };

                Timer swipeTimer = new Timer();
                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 2500, 2500);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
