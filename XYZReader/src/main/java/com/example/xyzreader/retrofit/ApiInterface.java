package com.example.xyzreader.retrofit;

import com.example.xyzreader.model.Article;
import com.example.xyzreader.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET(Constants.JSON_CONTENT_PATH)
    Call<List<Article>> getArticles();
}
