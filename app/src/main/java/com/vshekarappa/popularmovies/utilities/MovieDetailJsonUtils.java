package com.vshekarappa.popularmovies.utilities;

import android.content.Context;

import com.vshekarappa.popularmovies.MovieDetail;
import com.vshekarappa.popularmovies.model.MovieTrailer;
import com.vshekarappa.popularmovies.model.UserReview;

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
    private static final String MOV_RELEASE_DATE = "release_date";
    private static final String MOV_OVERVIEW = "overview";
    private static final String MOV_RATING = "vote_average";

    private static final String TRAILER_RESULTS = "results";
    private static final String TRAILER_ID = "id";
    private static final String TRAILER_KEY = "key";
    private static final String TRAILER_NAME = "name";
    private static final String TRAILER_TYPE = "type";

    private static final String REVIEW_RESULTS = "results";
    private static final String REVIEW_AUTHOR = "author";
    private static final String REVIEW_CONTENT = "content";
    private static final String REVIEW_URL = "url";


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
             String overview = movieDetailJson.getString(MOV_OVERVIEW);
             String releaseDate = movieDetailJson.getString(MOV_RELEASE_DATE);
             Double rating = movieDetailJson.getDouble(MOV_RATING);


             MovieDetail movieDetail = new MovieDetail();

             // Set Movie Posters & title
             movieDetail.setPosterPath(posterPath);
             movieDetail.setTitle(title);
             movieDetail.setMovieId(movieId);
             movieDetail.setOverview(overview);
             movieDetail.setReleaseDate(releaseDate);
             movieDetail.setRating(rating);

             parsedMovieData.add(movieDetail);
        }
        return parsedMovieData;
    }

    public static List<MovieTrailer> getTrailersFromJson(Context context, String movieTrailersJsonStr ) throws JSONException {
        final String MOV_MESSAGE_CODE = "cod";

        JSONObject trailersJson = new JSONObject(movieTrailersJsonStr);

        /* Is there an error? */
        if (trailersJson.has(MOV_MESSAGE_CODE)) {
            int errorCode = trailersJson.getInt(MOV_MESSAGE_CODE);
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

        List<MovieTrailer> trailersList = new ArrayList<>();
        JSONArray trailersArray = trailersJson.getJSONArray(TRAILER_RESULTS);

        for (int i=0; i< trailersArray.length(); i++) {
            JSONObject trailerJson = trailersArray.getJSONObject(i);
            MovieTrailer trailer = new MovieTrailer();
            trailer.setKey(trailerJson.getString(TRAILER_KEY));
            trailer.setName(trailerJson.getString(TRAILER_NAME));
            trailer.setTrailerId(trailerJson.getString(TRAILER_ID));
            trailer.setType(trailerJson.getString(TRAILER_TYPE));
            trailersList.add(trailer);
        }

        return trailersList;
    }

    public static List<UserReview> getReviewsFromJson(Context context, String movieReviewsJsonStr ) throws JSONException {
        final String MOV_MESSAGE_CODE = "cod";

        JSONObject reviewsJsonList = new JSONObject(movieReviewsJsonStr);

        /* Is there an error? */
        if (reviewsJsonList.has(MOV_MESSAGE_CODE)) {
            int errorCode = reviewsJsonList.getInt(MOV_MESSAGE_CODE);
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

        List<UserReview> userReviewList = new ArrayList<>();
        JSONArray reviewsArray = reviewsJsonList.getJSONArray(REVIEW_RESULTS);

        for (int i=0; i< reviewsArray.length(); i++) {
            JSONObject reviewJson = reviewsArray.getJSONObject(i);
            UserReview userReview = new UserReview();
            userReview.setAuthor(reviewJson.getString(REVIEW_AUTHOR));
            userReview.setContent(reviewJson.getString(REVIEW_CONTENT));
            userReview.setUrl(reviewJson.getString(REVIEW_URL));
            userReviewList.add(userReview);
        }

        return userReviewList;
    }



}
