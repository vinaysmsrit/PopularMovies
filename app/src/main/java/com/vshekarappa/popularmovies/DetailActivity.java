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

    @BindView(R.id.tv_detail_rel_rating)
    TextView mReleaseRatingView;

    @BindView(R.id.img_movie_detail)
    ImageView mMovieImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        MovieDetail movieDetail = (MovieDetail) getIntent().getParcelableExtra(MovieConstants.MOVIE_DETAIL_EXTRA);

        Picasso.with(DetailActivity.this)
                .load(movieDetail.getPosterPath())
                .into(mMovieImageView);

        mMovieTitleView.setText(movieDetail.getTitle());
        mReleaseRatingView.setText(" Released on : "+movieDetail.getReleaseDate()+"\n\n"
                +" Rating : "+movieDetail.getRating()+"/10");
        mOverviewView.setText("Overview : \n"+movieDetail.getOverview());
    }
}
