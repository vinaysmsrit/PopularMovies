package com.vshekarappa.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
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

import com.vshekarappa.popularmovies.database.FavoriteDatabase;
import com.vshekarappa.popularmovies.database.FavoriteEntity;
import com.vshekarappa.popularmovies.utilities.AppExecutors;
import com.vshekarappa.popularmovies.utilities.FavoriteUtils;
import com.vshekarappa.popularmovies.utilities.MovieConstants;
import com.vshekarappa.popularmovies.utilities.MovieDetailJsonUtils;
import com.vshekarappa.popularmovies.utilities.NetworkUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieDetailAdapter.IMoviePosterClickHandler {

    private MovieDetailAdapter movieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private GridView mPosterGridView;

    private static final String TAG = MovieConstants.APP_LOG_TAG;

    private static final int POPULAR_MOVIES  = 100;
    private static final int TOPRATED_MOVIES = 200;
    private static final int FAVORITE_MOVIES = 300;

    private int mCategory = POPULAR_MOVIES;
    private FavoriteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mPosterGridView = (GridView) findViewById(R.id.posters_grid);

        loadMoviePosters(MovieConstants.SORT_POPULAR);

        mDb = FavoriteDatabase.getInstance(getApplicationContext());
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
            loadMoviePosters(MovieConstants.SORT_POPULAR);
        } else if (id == R.id.action_rating) {
            mCategory = TOPRATED_MOVIES;
            loadMoviePosters(MovieConstants.SORT_TOP_RATED);
        } else if (id == R.id.action_favorites) {
            mCategory = FAVORITE_MOVIES;
            LiveData<List<FavoriteEntity>> favoriteList = mDb.favoriteDao().loadAllFavorites();
            final List<MovieDetail> favMovieList = new ArrayList<>();
            favoriteList.observe(MainActivity.this, new Observer<List<FavoriteEntity>>() {
                @Override
                public void onChanged(@Nullable List<FavoriteEntity> favoriteEntities) {
                    favMovieList.clear();
                    for (int i=0; i< favoriteEntities.size();i++) {
                        MovieDetail movieDetail = FavoriteUtils.getMovieDetail(favoriteEntities.get(i));
                        favMovieList.add(movieDetail);
                    }
                    Log.d(TAG,"Fav Observer onChanged mCategory="+mCategory);
                    if (mCategory == FAVORITE_MOVIES) {
                        showPosterView(favMovieList);
                    }
                }
            });
            // writeToSD();
        }

        return super.onOptionsItemSelected(item);
    }


    private void writeToSD() {
        Log.d(TAG,"writeToSD Enter");
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                File sd = Environment.getExternalStorageDirectory();
                String DB_PATH = getFilesDir().getAbsolutePath().replace("files", "databases") + File.separator;
                Log.d(TAG,"writeToSD DB_PATH="+DB_PATH);

                try {
                    if (sd.canWrite()) {
                        String currentDBPath = "favorites.db";
                        String backupDBPath = "backupname.db";
                        File currentDB = new File(DB_PATH, currentDBPath);
                        File backupDB = new File(sd, backupDBPath);
                        Log.d(TAG,"writeToSD sd.canWrite");

                        if (currentDB.exists()) {
                            Log.d(TAG,"writeToSD currentDB.exists");
                            FileChannel src = new FileInputStream(currentDB).getChannel();
                            FileChannel dst = new FileOutputStream(backupDB).getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                        }
                        Log.d(TAG,"Database Written");
                    } else {
                        Log.d(TAG,"writeToSD sd Cant Write");
                    }
                } catch (IOException e) {
                    Log.d(TAG,"Error Pulling DB !!!!");
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadMoviePosters(String sortBy) {
        Log.d(TAG,"loadMoviePosters sortBy ="+sortBy);
        new FetchMoviePostersTask().execute(sortBy);
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
