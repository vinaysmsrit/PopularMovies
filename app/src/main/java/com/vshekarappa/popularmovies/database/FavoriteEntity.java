package com.vshekarappa.popularmovies.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.vshekarappa.popularmovies.MovieDetail;

@Entity(tableName = "favorites")
public class FavoriteEntity {
    @PrimaryKey(autoGenerate = true)
    private int favId;

    private String title;
    private int movieId;
    private String releaseDate;
    private String overview;
    private String posterPath;
    private Double rating;

    @Ignore
    public FavoriteEntity(MovieDetail movieDetail) {
        this.title = movieDetail.getTitle();
        this.movieId = movieDetail.getMovieId();
        this.releaseDate = movieDetail.getReleaseDate();
        this.overview = movieDetail.getOverview();
        this.posterPath = movieDetail.getPosterPath();
        this.rating = movieDetail.getRating();
    }

    @Ignore
    public FavoriteEntity(String title, int movieId, String releaseDate,
                          String overview,String posterPath, double rating) {
        this.title = title;
        this.movieId = movieId;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.posterPath = posterPath;
        this.rating = rating;
    }

    public FavoriteEntity(int favId, String title, int movieId, String releaseDate,
                          String overview,String posterPath, double rating) {
        this.favId = favId;
        this.title = title;
        this.movieId = movieId;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.posterPath = posterPath;
        this.rating = rating;
    }

    public int getFavId() {
        return favId;
    }

    public void setFavId(int favId) {
        this.favId = favId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
