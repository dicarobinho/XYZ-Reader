package com.example.xyzreader.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.xyzreader.model.Article;

import java.util.List;

@Dao
public interface ArticlesDao {
    @Query("SELECT * FROM articles")
    LiveData<List<Article>> loadArticles();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArticles(List<Article> articles);

    @Query("SELECT * FROM articles WHERE id = :id")
    LiveData<Article> loadArticle(int id);
}

