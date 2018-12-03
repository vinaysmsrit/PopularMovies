package com.vshekarappa.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.vshekarappa.popularmovies.model.MovieDetail;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorites ORDER BY movieId" )
    LiveData<List<MovieDetail>> loadAllFavorites();

    @Insert
    void addFavorite(MovieDetail favoriteEntity);

    @Delete
    void deleteFavorite(MovieDetail favoriteEntity);

    @Query("SELECT * FROM favorites WHERE movieId = :movieId")
    MovieDetail loadFavoritesByMovieId(int movieId);
}
