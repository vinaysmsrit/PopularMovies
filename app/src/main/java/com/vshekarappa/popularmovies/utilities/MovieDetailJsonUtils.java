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

    public static List<MovieDetail> getMovieDetailFromJson(Context context, String movieDataJsonStr) throws JSONException {

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
             String title =  movieDetailJson.getString(MOV_TITLE);
             MovieDetail movieDetail = new MovieDetail(title,R.drawable.cupcake);
             parsedMovieData.add(movieDetail);
        }

        return parsedMovieData;
    }
}
