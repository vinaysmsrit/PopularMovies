package com.vshekarappa.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.vshekarappa.popularmovies.utilities.MovieConstants;

@Database(entities = {FavoriteEntity.class}, version = 1, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase {

    private static FavoriteDatabase sInstance;
    private static final String FAV_DATABASE_NAME = "favorites.db";
    private static final Object LOCK = new Object();

    private static final String TAG = MovieConstants.APP_LOG_TAG;

    public static FavoriteDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, " Creating Favorite Database" );
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavoriteDatabase.class,FAV_DATABASE_NAME)
                        .build();
            }
        }
        Log.d(TAG, " Getting Favorite Database Instance" );
        return sInstance;
    }

    public abstract FavoriteDao favoriteDao();
}
