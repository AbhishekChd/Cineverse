package com.example.abhishek.cineverse.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishek.cineverse.R;
import com.example.abhishek.cineverse.data.UrlContract;
import com.example.abhishek.cineverse.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private MovieAdapterOnClickHandler mClickHandler;
    private Context Ctx;

    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        Ctx = context;
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
        holder.tvMovieTitle.setText(movies.get(position).getTitle());
        holder.tvRating.setText(Ctx.getString(R.string.rating_format, movies.get(position).getVotes()));

        Picasso.with(Ctx)
                .load(UrlContract.BASE_POSTER_LARGE_URL + movies.get(position).getPosterPath())
                .into(holder.ivMoviePoster);

        ViewCompat.setTransitionName(holder.ivMoviePoster, movies.get(position).getTitle());

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
        void onClick(int index, ImageView v);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder   implements View.OnClickListener{

        ImageView ivMoviePoster;
        TextView tvMovieTitle;
        TextView tvRating;
        String genre;

        int image;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ivMoviePoster = itemView.findViewById(R.id.iv_movie_poster);
            tvMovieTitle = itemView.findViewById(R.id.tv_movie_title);
            tvRating = itemView.findViewById(R.id.tv_movie_rating);
            genre = itemView.getContext().getString(R.string.sample_genre);
            image = R.drawable.poster_w185_thor;
            ivMoviePoster.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition(),ivMoviePoster);
        }
    }
}
