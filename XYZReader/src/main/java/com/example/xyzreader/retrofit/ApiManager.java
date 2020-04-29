package com.example.xyzreader.retrofit;

import com.example.xyzreader.model.Article;
import com.example.xyzreader.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static ApiManager sApiManager;
    private static ApiInterface sApiInterfaceForced;

    private ApiManager() {
        Retrofit retrofitForced = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        sApiInterfaceForced = retrofitForced.create(ApiInterface.class);
    }

    public static ApiManager getInstance() {
        if (sApiManager == null) {
            sApiManager = new ApiManager();
        }
        return sApiManager;
    }

    public void getArticles(Callback<List<Article>> callback) {
        Call<List<Article>> response = sApiInterfaceForced.getArticles();
        response.enqueue(callback);
    }
}
