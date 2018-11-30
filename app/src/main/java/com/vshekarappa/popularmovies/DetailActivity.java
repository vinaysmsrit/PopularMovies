package com.vshekarappa.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.vshekarappa.popularmovies.database.FavoriteDatabase;
import com.vshekarappa.popularmovies.database.FavoriteEntity;
import com.vshekarappa.popularmovies.utilities.AppExecutors;
import com.vshekarappa.popularmovies.utilities.MovieConstants;

import java.util.List;

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

    @BindView(R.id.btn_favorite)
    ToggleButton mFavoriteIcon;

    private FavoriteDatabase mDb;
    private static final String TAG = MovieConstants.APP_LOG_TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        MovieDetail movieDetail = (MovieDetail) getIntent().getParcelableExtra(MovieConstants.MOVIE_DETAIL_EXTRA);
        setupUI(movieDetail);

        mDb = FavoriteDatabase.getInstance(getApplicationContext());

        updateFavIcon(movieDetail);
    }

    private void updateFavIcon(final MovieDetail movieDetail) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<FavoriteEntity> favList =
                        mDb.favoriteDao().loadFavoritesByMovieId(movieDetail.getMovieId());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ( favList != null && favList.size() > 0) {
                            mFavoriteIcon.setChecked(true);
                        }
                    }
                });
            }
        });
    }

    private void setupUI(final MovieDetail movieDetail) {
        if (movieDetail != null) {
            Picasso.with(DetailActivity.this)
                    .load(movieDetail.getPosterPath())
                    .into(mMovieImageView);

            mMovieTitleView.setText(movieDetail.getTitle());
            mReleaseView.setText(movieDetail.getReleaseDate());
            mRatingView.setText(movieDetail.getRating()+"/10");
            mOverviewView.setText(movieDetail.getOverview());
            mFavoriteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean favAdded = false;
                    if (((ToggleButton)view).isChecked()) {
                        final FavoriteEntity favorite = new FavoriteEntity(movieDetail);
                        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                mDb.favoriteDao().addFavorite(favorite);
                            }
                        });
                    } else {
                        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                final List<FavoriteEntity> favList =
                                        mDb.favoriteDao().loadFavoritesByMovieId(movieDetail.getMovieId());

                                if (favList != null & favList.size() > 0) {
                                    Log.d(TAG,"Deleting Favorite size="+favList.size());
                                    for (int i=0;i<favList.size();i++) {
                                        mDb.favoriteDao().deleteFavorite(favList.get(i));
                                    }
                                }
                            }
                        });
                        Toast.makeText(DetailActivity.this,"Favorite Removed ",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
