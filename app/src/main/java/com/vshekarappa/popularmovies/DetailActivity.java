package com.vshekarappa.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vshekarappa.popularmovies.utilities.MovieDetailJsonUtils;
import com.vshekarappa.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    TextView mMovieTitleView;
    TextView mOverviewView;

    ImageView mMovieImageView;

    ProgressBar mDetailLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mMovieTitleView = (TextView) findViewById(R.id.tv_detail_mov_title);
        mOverviewView = (TextView) findViewById(R.id.tv_detail_mov_overview);
        mDetailLoadingBar = (ProgressBar) findViewById(R.id.detail_pb_loading_indicator);
        mMovieImageView = (ImageView) findViewById(R.id.img_movie_detail);

        Intent movieDetailIntent = getIntent();

        if (movieDetailIntent != null && movieDetailIntent.hasExtra(Intent.EXTRA_TEXT)) {
            mMovieTitleView.setText(movieDetailIntent.getStringExtra(Intent.EXTRA_TEXT));
            Integer movieId = movieDetailIntent.getIntExtra(Intent.EXTRA_INDEX,-1);
            mOverviewView.append("\n ID= "+movieId.toString());

            loadMovieDetail(movieId);
        }
    }

    private void loadMovieDetail(Integer movieId) {
        new FetchMovieDetailTask().execute(movieId);
    }

    private class FetchMovieDetailTask extends AsyncTask<Integer,Void,MovieDetail> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDetailLoadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieDetail doInBackground(Integer... params) {
            Integer movieId = params[0];
            URL movieRequestUrl =  NetworkUtils.buildUrl(movieId.toString());
            try {
                String movieDetailData = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                MovieDetail movieDetail = MovieDetailJsonUtils.getMovieDetailFromJson(DetailActivity.this, movieDetailData);

                return movieDetail;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieDetail movieDetail) {
            mDetailLoadingBar.setVisibility(View.INVISIBLE);
            mOverviewView.append("\n Overview :\n"+ movieDetail.overview);

            Picasso.with(DetailActivity.this)
                    .load(movieDetail.backdropPath)
                    .into(mMovieImageView);
        }
    }
}
