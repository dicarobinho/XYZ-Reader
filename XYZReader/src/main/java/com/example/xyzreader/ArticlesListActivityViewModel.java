package com.example.xyzreader;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.xyzreader.data.AppDataBase;
import com.example.xyzreader.data.AppExecutors;
import com.example.xyzreader.data.ArticlesDao;
import com.example.xyzreader.data.Repository;
import com.example.xyzreader.model.Article;
import com.example.xyzreader.utils.ResultsDisplay;

import java.util.List;

public class ArticlesListActivityViewModel extends AndroidViewModel {
    private final Repository repository;
    private LiveData<List<Article>> articles;
    private final ArticlesDao articlesDao;

    public ArticlesListActivityViewModel(@NonNull Application application) {
        super(application);
        AppDataBase database = AppDataBase.getInstance(this.getApplication());
        articles = database.articlesDao().loadArticles();
        articlesDao = database.articlesDao();
        repository = Repository.getInstance(application);
    }

    public LiveData<ResultsDisplay<List<Article>>> getArticlesFromServer() {
        return repository.getArticlesFromServer();
    }

    public LiveData<List<Article>> getArticles() {
        return articles;
    }

    public void addArticles(List<Article> articles) {
            AppExecutors.getInstance().diskIO().execute(() -> articlesDao.insertArticles(articles));
    }
}