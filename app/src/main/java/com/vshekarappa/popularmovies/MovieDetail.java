package com.vshekarappa.popularmovies;

public class MovieDetail {
    String movieName;
    String posterPath;
    int movieId;

    public MovieDetail(String movieName, String posterPath, int movieId) {
        this.movieName = movieName;
        this.posterPath = posterPath;
        this.movieId = movieId;
    }
}
