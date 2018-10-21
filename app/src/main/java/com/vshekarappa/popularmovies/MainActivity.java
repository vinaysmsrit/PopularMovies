package com.vshekarappa.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vshekarappa.popularmovies.utilities.MovieDetailJsonUtils;
import com.vshekarappa.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieDetailAdapter.IMoviePosterClickHandler {

    private static final String DETAIL_MOVIE_ID = "movie_id";
    private MovieDetailAdapter movieAdapter;

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

        loadMoviePosters();
    }

    private void loadMoviePosters() {
        new FetchMoviePostersTask().execute("popular");
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

    @Override
    public void onClick(MovieDetail movieDetail) {
        Intent intentDetail = new Intent(this,DetailActivity.class);
        intentDetail.putExtra("movie_data",movieDetail);
        startActivity(intentDetail);
    }


    private class FetchMoviePostersTask extends AsyncTask<String,Void, List<MovieDetail>>  {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<MovieDetail> doInBackground(String... params) {

            String category = params[0];

            URL movieRequestUrl =  NetworkUtils.buildUrl(category);

            try {
                String moviePosterDetails = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                List<MovieDetail> moviePostersList =
                        MovieDetailJsonUtils.getMoviePostersFromJson(MainActivity.this, moviePosterDetails);

                return moviePostersList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<MovieDetail> moviePostersList) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviePostersList != null) {
                showPosterView(moviePostersList);
            } else {
                showErrorMessage();
            }
        }
    }
}
