package com.vshekarappa.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vshekarappa.popularmovies.model.MovieDetail;

import java.util.List;

public class MovieDetailAdapter extends ArrayAdapter<MovieDetail> {


    private IMoviePosterClickHandler moviePosterClickHandler;

    public MovieDetailAdapter(@NonNull Context context, @NonNull List<MovieDetail> movieDetailList) {
        super(context, 0, movieDetailList);
        moviePosterClickHandler = (IMoviePosterClickHandler) context;
    }


    public interface IMoviePosterClickHandler  {
        void onClick(MovieDetail movieDetail);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MovieDetail movieDetail = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.poster_item, parent, false);
        }

        ImageView posterView = convertView.findViewById(R.id.poster_image);

        Picasso.with(getContext())
                .load(movieDetail.getPosterPath())
                .placeholder(R.drawable.movie_icon)
                .into(posterView);

        posterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieDetail movieDetailSelected = getItem(position);
                moviePosterClickHandler.onClick(movieDetailSelected);
            }
        });

        return convertView;
    }

}