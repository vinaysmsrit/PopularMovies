package com.vshekarappa.popularmovies.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorites ORDER BY movieId" )
    List<FavoriteEntity> loadAllFavorites();

    @Insert
    void addFavorite(FavoriteEntity favoriteEntity);

    @Delete
    void deleteFavorite(FavoriteEntity favoriteEntity);

    @Query("SELECT * FROM favorites WHERE favId = :favId")
    FavoriteEntity loadFavoriteById(int favId);

}
