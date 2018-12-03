package com.vshekarappa.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vshekarappa.popularmovies.database.MainViewModel;
import com.vshekarappa.popularmovies.model.MovieDetail;
import com.vshekarappa.popularmovies.utilities.MovieConstants;
import com.vshekarappa.popularmovies.utilities.MovieDetailJsonUtils;
import com.vshekarappa.popularmovies.utilities.NetworkUtils;


import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieDetailAdapter.IMoviePosterClickHandler {

    private static final String STATE_SORT_CATEGORY = "sort_category";
    private MovieDetailAdapter movieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private GridView mPosterGridView;

    private static final String TAG = MovieConstants.APP_LOG_TAG;

    private static final int POPULAR_MOVIES  = 100;
    private static final int TOPRATED_MOVIES = 200;
    private static final int FAVORITE_MOVIES = 300;

    private int mCategory = POPULAR_MOVIES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mPosterGridView = (GridView) findViewById(R.id.posters_grid);

        if (savedInstanceState != null) {
            mCategory = savedInstanceState.getInt(STATE_SORT_CATEGORY,POPULAR_MOVIES);
        }
        loadMoviePosters();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SORT_CATEGORY,mCategory);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {
            mCategory = POPULAR_MOVIES;
        } else if (id == R.id.action_rating) {
            mCategory = TOPRATED_MOVIES;
        } else if (id == R.id.action_favorites) {
            mCategory = FAVORITE_MOVIES;
        }
        loadMoviePosters();
        return super.onOptionsItemSelected(item);
    }

    private void loadFavoritePosters() {
        MainViewModel viewModel = ViewModelProviders.of(MainActivity.this).get(MainViewModel.class);

        viewModel.getFavoriteList().observe(MainActivity.this, new Observer<List<MovieDetail>>() {
            @Override
            public void onChanged(@Nullable List<MovieDetail> favoriteEntities) {
                Log.d(TAG,"Fav Observer onChanged mCategory="+mCategory);
                if (mCategory == FAVORITE_MOVIES) {
                    showPosterView(favoriteEntities);
                }
            }
        });
    }

    private void loadMoviePosters() {
        Log.d(TAG,"loadMoviePosters category ="+mCategory);
        if (mCategory == FAVORITE_MOVIES) {
            loadFavoritePosters();
        } else {
            String sortBy = null;
            if (mCategory == POPULAR_MOVIES) {
                sortBy = MovieConstants.SORT_POPULAR;
            } else if(mCategory == TOPRATED_MOVIES) {
                sortBy = MovieConstants.SORT_TOP_RATED;
            }
            Log.d(TAG,"loadMoviePosters sortBy ="+sortBy);
            new FetchMoviePostersTask().execute(sortBy);
        }
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
        intentDetail.putExtra(MovieConstants.MOVIE_DETAIL_EXTRA,movieDetail);
        startActivity(intentDetail);
    }


    private class FetchMoviePostersTask extends AsyncTask<String,Void, List<MovieDetail>>  {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG,"FetchMoviePostersTask onPreExecute");
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<MovieDetail> doInBackground(String... params) {

            String category = params[0];

            URL movieRequestUrl =  NetworkUtils.buildUrl(category);
            Log.d(TAG,"FetchMoviePostersTask URL = "+movieRequestUrl.toString());

            try {
                String moviePosterDetails = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                Log.d(TAG,"FetchMoviePostersTask moviePosterDetails JSON Response Received ");
                return MovieDetailJsonUtils.getMoviePostersFromJson(MainActivity.this, moviePosterDetails);
            } catch (Exception e) {
                Log.d(TAG,"FetchMoviePostersTask moviePosterDetails JSON Response ERROR ");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<MovieDetail> moviePostersList) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviePostersList != null) {
                Log.d(TAG,"FetchMoviePostersTask onPostExecute movies count="+moviePostersList.size());
                showPosterView(moviePostersList);
            } else {
                Log.d(TAG,"FetchMoviePostersTask onPostExecute ERROR ");
                showErrorMessage();
            }
        }
    }
}
