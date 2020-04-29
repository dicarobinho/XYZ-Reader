package com.example.xyzreader;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.xyzreader.data.Repository;
import com.example.xyzreader.model.Article;

public class ArticleDetailActivityViewModel extends AndroidViewModel {
    private final Repository repository;

    public ArticleDetailActivityViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(application);
    }

    public LiveData<Article> getSpecificArticleFromDb(int id) {
        return repository.getArticle(id);
    }
}