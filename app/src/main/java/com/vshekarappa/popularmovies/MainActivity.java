package com.vshekarappa.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vshekarappa.popularmovies.utilities.MovieDetailJsonUtils;
import com.vshekarappa.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MovieDetailAdapter movieAdapter;

    private static final String API_KEY = "5c25eaa5f74d80e33ae9df32e7b1ff55";

    MovieDetail[] movieDetails1 = {
            new MovieDetail("Cupcake", R.drawable.cupcake),
            new MovieDetail("Donut", R.drawable.donut),
            new MovieDetail("Eclair", R.drawable.eclair),
            new MovieDetail("Froyo", R.drawable.froyo),
            new MovieDetail("GingerBread", R.drawable.gingerbread),
            new MovieDetail("Honeycomb", R.drawable.honeycomb),
            new MovieDetail("Ice Cream Sandwich", R.drawable.icecream),
			new MovieDetail("Jelly Bean",  R.drawable.jellybean),
			new MovieDetail("KitKat",  R.drawable.kitkat),
			new MovieDetail("Lollipop",  R.drawable.lollipop)
    };

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private GridView mPosterGridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mPosterGridView = findViewById(R.id.posters_grid);

        loadMovieData();
    }

    private void loadMovieData() {
        new FetchMovieDataTask().execute(API_KEY);
    }

    private void showPosterView(List<MovieDetail> movieDetails) {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the Poster data is visible */
        mPosterGridView.setVisibility(View.VISIBLE);

        movieAdapter = new MovieDetailAdapter(this, movieDetails);
        mPosterGridView.setAdapter(movieAdapter);

    }

    /**
     * This method will make the error message visible and hide the poster
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mPosterGridView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }



    public class FetchMovieDataTask extends AsyncTask<String,Void, List<MovieDetail>>  {

        @Override
        protected List<MovieDetail> doInBackground(String... params) {

            String apiKey = params[0];

            URL movieRequestUrl =  NetworkUtils.buildUrl(apiKey);

            try {
                String movieDetailsData = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                List<MovieDetail> movieDetailList =
                        MovieDetailJsonUtils.getMovieDetailFromJson(MainActivity.this, movieDetailsData);

                return movieDetailList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<MovieDetail> movieDetailList) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieDetailList != null) {
                showPosterView(movieDetailList);
            } else {
                showErrorMessage();
            }
        }
    }
}
