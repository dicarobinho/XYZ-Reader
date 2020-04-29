package com.example.xyzreader.data;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xyzreader.model.Article;
import com.example.xyzreader.retrofit.ApiManager;
import com.example.xyzreader.utils.Constants;
import com.example.xyzreader.utils.ResultsDisplay;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static volatile Repository sInstance;
    private final ArticlesDao articlesDao;

    private MutableLiveData<ResultsDisplay<List<Article>>> mArticlesObservable;

    private Repository(final Application application) {
        AppDataBase appDataBase = AppDataBase.getInstance(application);
        articlesDao = appDataBase.articlesDao();
    }

    public static Repository getInstance(final Application application) {
        if (sInstance == null) {
            synchronized (Repository.class) {
                if (sInstance == null) {
                    sInstance = new Repository(application);
                }
            }
        }
        return sInstance;
    }

    public LiveData<ResultsDisplay<List<Article>>> getArticlesFromServer() {
        mArticlesObservable = new MutableLiveData<>();
        mArticlesObservable.setValue(ResultsDisplay.loading(null));

        ApiManager.getInstance().getArticles(new Callback<List<Article>>() {
            @Override
            public void onResponse(@NonNull Call<List<Article>> call, @NonNull Response<List<Article>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Article> articles = response.body();
                        mArticlesObservable.setValue(ResultsDisplay.success(articles));
                    }
                } else
                mArticlesObservable.setValue(ResultsDisplay.error(String.valueOf(response.code()), null));
            }

            @Override
            public void onFailure(@NonNull Call<List<Article>> call, @NonNull Throwable t) {
                Log.v(Constants.ERROR_TAG, Constants.HTTP_ARTICLES_ERROR);
                mArticlesObservable.setValue(ResultsDisplay.error(t.getMessage(), null));
            }
        });

        return mArticlesObservable;
    }

    public LiveData<Article> getArticle(int id) {
        return articlesDao.loadArticle(id);
    }
}
