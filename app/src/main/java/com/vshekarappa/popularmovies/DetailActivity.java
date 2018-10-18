package com.vshekarappa.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    TextView mMovieTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mMovieTitleView = (TextView) findViewById(R.id.tv_detail_mov_title);


        Intent movieDetailIntent = getIntent();

        if (movieDetailIntent != null && movieDetailIntent.hasExtra(Intent.EXTRA_TEXT)) {
            mMovieTitleView.setText(movieDetailIntent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }
}
