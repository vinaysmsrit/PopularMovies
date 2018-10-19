package com.vshekarappa.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    TextView mMovieTitleView;
    TextView mOverviewView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mMovieTitleView = (TextView) findViewById(R.id.tv_detail_mov_title);
        mOverviewView = (TextView) findViewById(R.id.tv_detail_mov_overview);

        Intent movieDetailIntent = getIntent();

        if (movieDetailIntent != null && movieDetailIntent.hasExtra(Intent.EXTRA_TEXT)) {
            mMovieTitleView.setText(movieDetailIntent.getStringExtra(Intent.EXTRA_TEXT));
            Integer movieId = movieDetailIntent.getIntExtra(Intent.EXTRA_INDEX,-1);
            mOverviewView.append("\n ID= "+movieId.toString());
        }
    }
}
