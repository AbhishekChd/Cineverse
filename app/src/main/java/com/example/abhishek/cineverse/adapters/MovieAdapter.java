package com.example.abhishek.cineverse.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
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

    public List<Movie> movies;
    private Context Ctx;

    public MovieAdapter(Context context, List<Movie> movies) {
        Ctx = context;
        this.movies = movies;
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
        holder.tvMovieGenre.setText(holder.genre);

        Picasso.with(Ctx)
                .load(UrlContract.BASE_POSTER_LARGE_URL + movies.get(position).getPosterPath())
                .into(holder.ivMoviePoster);
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        //        @BindView(R.id.iv_movie_poster)
        ImageView ivMoviePoster;
        //        @BindView(R.id.tv_movie_title)
        TextView tvMovieTitle;
        //        @BindView(R.id.tv_movie_genre)
        TextView tvMovieGenre;

        //        @BindString(R.string.sample_genre)
        String genre;

        int image;

        public MovieViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(itemView);
            ivMoviePoster = itemView.findViewById(R.id.iv_movie_poster);
            tvMovieTitle = itemView.findViewById(R.id.tv_movie_title);
            tvMovieGenre = itemView.findViewById(R.id.tv_movie_genre);
            genre = itemView.getContext().getString(R.string.sample_genre);
            image = R.drawable.poster_w185_thor;
        }
    }
}
