package com.vshekarappa.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vshekarappa.popularmovies.utilities.MovieConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_detail_mov_title)
    TextView mMovieTitleView;

    @BindView(R.id.tv_detail_mov_overview)
    TextView mOverviewView;

    @BindView(R.id.tv_released)
    TextView mReleaseView;

    @BindView(R.id.tv_rating)
    TextView mRatingView;

    @BindView(R.id.img_movie_detail)
    ImageView mMovieImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        MovieDetail movieDetail = (MovieDetail) getIntent().getParcelableExtra(MovieConstants.MOVIE_DETAIL_EXTRA);

        setupUI(movieDetail);
    }

    private void setupUI(MovieDetail movieDetail) {
        if (movieDetail != null) {
            Picasso.with(DetailActivity.this)
                    .load(movieDetail.getPosterPath())
                    .into(mMovieImageView);

            mMovieTitleView.setText(movieDetail.getTitle());
            mReleaseView.setText(movieDetail.getReleaseDate());
            mRatingView.setText(movieDetail.getRating()+"/10");
            mOverviewView.setText(movieDetail.getOverview());
        }
    }
}
