package com.vshekarappa.popularmovies.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.vshekarappa.popularmovies.model.MovieDetail;
import com.vshekarappa.popularmovies.utilities.MovieConstants;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<MovieDetail>> favoriteList;
    private static final String TAG = MovieConstants.APP_LOG_TAG;

    public MainViewModel(@NonNull Application application) {
        super(application);
        FavoriteDatabase database = FavoriteDatabase.getInstance(this.getApplication());
        Log.d(TAG,"Loading Favorites from ViewModel Database");
        favoriteList = database.favoriteDao().loadAllFavorites();
    }

    public LiveData<List<MovieDetail>> getFavoriteList() {
        return favoriteList;
    }
}
