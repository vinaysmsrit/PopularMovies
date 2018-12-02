package com.vshekarappa.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.vshekarappa.popularmovies.database.FavoriteDatabase;
import com.vshekarappa.popularmovies.model.MovieDetail;
import com.vshekarappa.popularmovies.model.MovieTrailer;
import com.vshekarappa.popularmovies.model.UserReview;
import com.vshekarappa.popularmovies.utilities.AppExecutors;
import com.vshekarappa.popularmovies.utilities.MovieConstants;
import com.vshekarappa.popularmovies.utilities.MovieDetailJsonUtils;
import com.vshekarappa.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.LinearLayout.HORIZONTAL;

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

    @BindView(R.id.lbl_trailers)
    TextView mTrailerView;

    @BindView(R.id.tv_reviews)
    TextView mReviewsView;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private FavoriteDatabase mDb;
    private MovieTrailerAdapter mAdapter;
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

        setupRecyclerView();

        loadMovieTrailerReviews(movieDetail);


    }

    private void setupRecyclerView() {
        mAdapter = new MovieTrailerAdapter(this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        mRecyclerView.addItemDecoration(itemDecor);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadMovieTrailerReviews(MovieDetail movieDetail) {
        int movId = movieDetail.getMovieId();
        new FetchTrailersTask().execute(movId);
        new FetchReviewsTask().execute(movId);
    }

    private void updateFavIcon(final MovieDetail movieDetail) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                final MovieDetail favMovie =
                        mDb.favoriteDao().loadFavoritesByMovieId(movieDetail.getMovieId());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (favMovie != null) {
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
                        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                mDb.favoriteDao().addFavorite(movieDetail);
                            }
                        });
                    } else {
                        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                final MovieDetail favMovie =
                                        mDb.favoriteDao().loadFavoritesByMovieId(movieDetail.getMovieId());

                                if (favMovie != null ) {
                                    Log.d(TAG,"Deleting Favorite ="+favMovie.getMovieId());
                                    mDb.favoriteDao().deleteFavorite(favMovie);
                                }
                            }
                        });
                        Toast.makeText(DetailActivity.this,"Favorite Removed ",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private class FetchTrailersTask extends AsyncTask<Integer,Void, List<MovieTrailer>> {

        @Override
        protected List<MovieTrailer> doInBackground(Integer... params) {
            Integer movId = params[0];
            String category = movId.toString()+"/videos";

            URL movieTrailersUrl =  NetworkUtils.buildUrl(category);
            Log.d(TAG,"FetchTrailersTask URL = "+movieTrailersUrl.toString());
            String movieTrailerDetails = null;
            try {
                movieTrailerDetails = NetworkUtils.getResponseFromHttpUrl(movieTrailersUrl);
                Log.d(TAG,"FetchTrailersTask  JSON Response Received "+movieTrailerDetails);
                return MovieDetailJsonUtils.getTrailersFromJson(DetailActivity.this,movieTrailerDetails);
            } catch (Exception e) {
                Log.d(TAG,"FetchTrailersTask Exception in getting TRAILER !!");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<MovieTrailer> trailerList) {
            if (trailerList.size() > 0) {
                mTrailerView.setVisibility(View.VISIBLE);
            }
            mAdapter.setData(trailerList);
        }
    }

    private class FetchReviewsTask extends AsyncTask<Integer,Void, List<UserReview>> {

        @Override
        protected List<UserReview> doInBackground(Integer... params) {
            Integer movId = params[0];
            String category = movId.toString()+"/reviews";

            URL movieReviewsUrl =  NetworkUtils.buildUrl(category);
            Log.d(TAG,"FetchReviewsTask URL = "+movieReviewsUrl.toString());
            String movieReviewDetails = null;
            try {
                movieReviewDetails = NetworkUtils.getResponseFromHttpUrl(movieReviewsUrl);
                Log.d(TAG,"FetchReviewsTask  JSON Response Received "+movieReviewDetails);
                return MovieDetailJsonUtils.getReviewsFromJson(DetailActivity.this,movieReviewDetails);
            } catch (Exception e) {
                Log.d(TAG,"FetchReviewsTask Exception in getting REVIEWS !!");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<UserReview> userReviewList) {
            mReviewsView.setText("\n User Reviews : \n\n");
            for(int i = 0; i< userReviewList.size(); i++) {
                UserReview userReview = userReviewList.get(i);
                mReviewsView.append(userReview.getAuthor() +"\n");
                mReviewsView.append(userReview.getContent() +"\n\n");
            }
        }
    }

}
