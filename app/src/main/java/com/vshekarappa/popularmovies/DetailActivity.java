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
    TextView mReleaseRatingView;
    ImageView mMovieImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mMovieTitleView = (TextView) findViewById(R.id.tv_detail_mov_title);
        mOverviewView = (TextView) findViewById(R.id.tv_detail_mov_overview);
        mMovieImageView = (ImageView) findViewById(R.id.img_movie_detail);
        mReleaseRatingView = (TextView) findViewById(R.id.tv_detail_rel_rating);


        MovieDetail movieDetail = (MovieDetail) getIntent().getParcelableExtra("movie_data");

        Picasso.with(DetailActivity.this)
                .load(movieDetail.getPosterPath())
                .into(mMovieImageView);

        mMovieTitleView.setText(movieDetail.getTitle());
        mReleaseRatingView.setText(" Released on : "+movieDetail.getReleaseDate()+"\n\n"
                +" Rating : "+movieDetail.getRating()+"/10");
        mOverviewView.setText("Overview : \n"+movieDetail.getOverview());
    }
}
