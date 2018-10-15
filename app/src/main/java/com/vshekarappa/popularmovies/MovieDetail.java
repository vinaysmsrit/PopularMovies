package com.vshekarappa.popularmovies;

public class MovieDetail {
    String movieName;
    int imageUrl; // storing Image URL
    String posterPath;

    public MovieDetail(String movieName, int imageUrl, String posterPath) {
        this.movieName = movieName;
        this.imageUrl = imageUrl;
        this.posterPath = posterPath;
    }
}
