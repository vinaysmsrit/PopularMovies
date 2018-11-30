package com.vshekarappa.popularmovies.utilities;

import com.vshekarappa.popularmovies.MovieDetail;
import com.vshekarappa.popularmovies.database.FavoriteEntity;

public class FavoriteUtils {

    public static MovieDetail getMovieDetail(FavoriteEntity favoriteEntity) {
        MovieDetail movieDetail = new MovieDetail();
        movieDetail.setMovieId(favoriteEntity.getMovieId());
        movieDetail.setTitle(favoriteEntity.getTitle());
        movieDetail.setRating(favoriteEntity.getRating());
        movieDetail.setOverview(favoriteEntity.getOverview());
        movieDetail.setPosterPath(favoriteEntity.getPosterPath());
        movieDetail.setReleaseDate(favoriteEntity.getReleaseDate());
        return movieDetail;
    }
}
