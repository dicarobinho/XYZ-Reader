package com.example.xyzreader.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.xyzreader.model.Article;
import com.example.xyzreader.utils.Constants;

@Database(entities = {Article.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    private static final String LOG_TAG = AppDataBase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = Constants.DATABASE_NAME;
    private static AppDataBase sInstance;

    public static AppDataBase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, Constants.CREATING_NEW_DATABASE_INSTANCE);
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDataBase.class, AppDataBase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, Constants.GETTING_DATABASE_INSTANCE);
        return sInstance;
    }

    public abstract ArticlesDao articlesDao();
}
