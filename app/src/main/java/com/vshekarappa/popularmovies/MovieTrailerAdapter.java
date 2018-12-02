package com.vshekarappa.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vshekarappa.popularmovies.model.MovieTrailer;
import com.vshekarappa.popularmovies.utilities.MovieConstants;

import java.util.List;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder> {
    List<MovieTrailer> mTrailerList;
    private Context mContext;

    static final String TAG = MovieConstants.APP_LOG_TAG;


    public MovieTrailerAdapter(Context context) {
        mContext = context;
    }


    public void setData(List<MovieTrailer> trailers) {
        mTrailerList = trailers;
        notifyDataSetChanged();
    }

    @Override
    public MovieTrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.trailer_item,parent,false);
        return new MovieTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailerViewHolder holder, int position) {
        if (mTrailerList == null) return;
        MovieTrailer trailer = mTrailerList.get(position);
        holder.mTextView.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mTrailerList != null) count = mTrailerList.size();
        return count;
    }

    public class MovieTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTextView;
        public final ImageView mImageView;

        public MovieTrailerViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.tv_trailer_name);
            mImageView = (ImageView)itemView.findViewById(R.id.btn_play);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            MovieTrailer trailer = mTrailerList.get(position);
            String youTubeLink = MovieConstants.YOUTUBE_BASE_URI + trailer.getKey();
            mContext.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(youTubeLink)));
        }
    }
}
