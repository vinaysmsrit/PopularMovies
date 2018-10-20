package com.vshekarappa.popularmovies.utilities;

import android.content.Context;

import com.vshekarappa.popularmovies.MovieDetail;
import com.vshekarappa.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailJsonUtils {

    private static final String MOV_RESULTS = "results";
    private static final String MOV_TITLE = "title";
    private static final String MOV_POSTER_PATH = "poster_path";
    private static final String BASE_IMAGE_PATH = "http://image.tmdb.org/t/p/w185/";
    private static final String MOV_ID = "id";
    private static final String MOV_BACKDROP_PATH = "backdrop_path";
    private static final String MOV_RELEASE_DATE = "release_date";
    private static final String MOV_OVERVIEW = "overview";

    public static List<MovieDetail> getMoviePostersFromJson(Context context, String movieDataJsonStr) throws JSONException {

        final String MOV_MESSAGE_CODE = "cod";

        JSONObject moviedataJson = new JSONObject(movieDataJsonStr);

        /* Is there an error? */
        if (moviedataJson.has(MOV_MESSAGE_CODE)) {
            int errorCode = moviedataJson.getInt(MOV_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        List<MovieDetail> parsedMovieData = new ArrayList<>();
        JSONArray moviesArray = moviedataJson.getJSONArray(MOV_RESULTS);

        for (int i=0; i< moviesArray.length(); i++) {
             JSONObject movieDetailJson = moviesArray.getJSONObject(i);

             // Parse JSON Movie Array
             String title =  movieDetailJson.getString(MOV_TITLE);
             String posterPath = BASE_IMAGE_PATH + movieDetailJson.getString(MOV_POSTER_PATH);
             Integer movieId =  movieDetailJson.getInt(MOV_ID);


             MovieDetail movieDetail = new MovieDetail();

             // Set Movie Posters & title
             movieDetail.setPosterPath(posterPath);
             movieDetail.setTitle(title);
             movieDetail.setMovieId(movieId);

             parsedMovieData.add(movieDetail);
        }
        return parsedMovieData;
    }

    public static MovieDetail getMovieDetailFromJson(Context context, String movieDetailJsonString) throws JSONException{
        final String MOV_MESSAGE_CODE = "cod";

        JSONObject moviedataJson = new JSONObject(movieDetailJsonString);

        /* Is there an error? */
        if (moviedataJson.has(MOV_MESSAGE_CODE)) {
            int errorCode = moviedataJson.getInt(MOV_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        MovieDetail movieDetail = new MovieDetail();

        String title = moviedataJson.getString(MOV_TITLE);
        Integer movieId = moviedataJson.getInt(MOV_ID);
        String posterPath = BASE_IMAGE_PATH + moviedataJson.getString(MOV_POSTER_PATH);
        String backdropPath = BASE_IMAGE_PATH + moviedataJson.getString(MOV_BACKDROP_PATH);
        String releaseDate = moviedataJson.getString(MOV_RELEASE_DATE);
        String overview = moviedataJson.getString(MOV_OVERVIEW);

        movieDetail.setMovieId(movieId);
        movieDetail.setTitle(title);
        movieDetail.setPosterPath(posterPath);
        movieDetail.setBackdropPath(backdropPath);
        movieDetail.setReleaseDate(releaseDate);
        movieDetail.setOverview(overview);

        return movieDetail;
    }


}
