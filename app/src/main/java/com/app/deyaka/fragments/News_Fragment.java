package com.app.deyaka.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.deyaka.R;
import com.app.deyaka.models.News_Model;
import com.app.deyaka.retrofit.APIClient;
import com.app.deyaka.retrofit.APIInterface;
import com.app.deyaka.retrofit.NewsEvents_RetrofitModel;
import com.app.deyaka.session_management.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mindorks.placeholderview.PlaceHolderView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class News_Fragment extends Fragment {

    APIInterface apiInterface;
    SessionManager session;
    ProgressDialog progressDialog;

    private PlaceHolderView phvNews;
    private LinearLayout llNoData;
    private TextView btnTryAgain;
    private LinearLayout llTryAgain;
    private RelativeLayout rlNews;

    public News_Fragment() {
        //empty constructor
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.news_fragment, container, false);

        session = new SessionManager(getActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait...");

        phvNews = view.findViewById(R.id.phvNews);
        llNoData = view.findViewById(R.id.llNoData);

        btnTryAgain = view.findViewById(R.id.btnTryAgain);
        llTryAgain = view.findViewById(R.id.llTryAgain);
        rlNews = view.findViewById(R.id.rlNews);

        getNewsList();
        return view;
    }

    public void getNewsList() {
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        NewsEvents_RetrofitModel newsEvents_model = new NewsEvents_RetrofitModel(session.getKidId(), "1");
        Call<NewsEvents_RetrofitModel> call_newsEvents_model = apiInterface.getNewsEvents(newsEvents_model);
        call_newsEvents_model.enqueue(new Callback<NewsEvents_RetrofitModel>() {
            @Override
            public void onResponse(Call<NewsEvents_RetrofitModel> call, Response<NewsEvents_RetrofitModel> response) {

                NewsEvents_RetrofitModel newsEvents_resources = response.body();
                if (response.isSuccessful()) {
                    if (newsEvents_resources.status.equals("success")) {

                        rlNews.setVisibility(View.VISIBLE);
                        llTryAgain.setVisibility(View.GONE);

                        JsonArray jsonElements = (JsonArray) new Gson().toJsonTree(newsEvents_resources.result);
                        if (jsonElements.size() > 0) {
                            llNoData.setVisibility(View.GONE);
                            phvNews.setVisibility(View.VISIBLE);
                            for (int j = 0; j < jsonElements.size(); j++) {

                                String newsEvents_title = String.valueOf(jsonElements.get(j).getAsJsonObject()
                                        .get("title")).replace("\"", "");

                                String newsEvents_subtitle = String.valueOf(jsonElements.get(j).getAsJsonObject()
                                        .get("subtitle")).replace("\"", "");

                                String newsEvents_url = String.valueOf(jsonElements.get(j).getAsJsonObject()
                                        .get("url")).replace("\"", "");

                                phvNews.addView(new News_Model(getActivity(), newsEvents_subtitle, newsEvents_title, newsEvents_url));
                            }
                        } else {
                            llNoData.setVisibility(View.VISIBLE);
                            phvNews.setVisibility(View.GONE);
                        }
                    } else {
                        rlNews.setVisibility(View.VISIBLE);
                        llTryAgain.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                        phvNews.setVisibility(View.GONE);
                    }
                } else {
                    rlNews.setVisibility(View.GONE);
                    llTryAgain.setVisibility(View.VISIBLE);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<NewsEvents_RetrofitModel> call, Throwable t) {
                call.cancel();
                rlNews.setVisibility(View.GONE);
                llTryAgain.setVisibility(View.VISIBLE);
            }
        });
    }
}
