package com.example.abhishek.cineverse.adapters;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishek.cineverse.R;
import com.example.abhishek.cineverse.helpers.ImageUrlUtils;
import com.example.abhishek.cineverse.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private List<Movie> movies;
    private final MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.tvMovieTitle.setText(movie.getTitle());
        holder.tvRating.setText(
                holder.tvMovieTitle
                        .getContext()
                        .getString(R.string.rating_format, movie.getVotes())
        );

        ViewCompat.setTransitionName(holder.ivMoviePoster, String.valueOf(movie.getId()));

        Picasso.with(holder.tvMovieTitle.getContext())
                .load(ImageUrlUtils.getLargePosterUrl(
                        movie.getPosterPath()
                ))
                .into(holder.ivMoviePoster);
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    public void setMovieData(List<Movie> movieData) {
        movies = movieData;
        notifyDataSetChanged();
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(int index, View view);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView ivMoviePoster;
        final TextView tvMovieTitle;
        final TextView tvRating;

        MovieViewHolder(View itemView) {
            super(itemView);
            ivMoviePoster = itemView.findViewById(R.id.iv_movie_poster);
            tvMovieTitle = itemView.findViewById(R.id.tv_movie_title);
            tvRating = itemView.findViewById(R.id.tv_movie_rating);
            ivMoviePoster.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition(), v);
        }
    }
}
