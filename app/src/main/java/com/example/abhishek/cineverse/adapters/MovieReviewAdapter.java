package com.example.abhishek.cineverse.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abhishek.cineverse.R;
import com.example.abhishek.cineverse.models.MovieReviews;

import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder> {
    private static final String LOG_TAG = MovieReviewAdapter.class.getSimpleName();
    private final List<MovieReviews.ReviewResult> reviews;

    public MovieReviewAdapter(List<MovieReviews.ReviewResult> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public MovieReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.movie_review_card, parent, false);
        return new MovieReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewViewHolder holder, int position) {
        String authorName = reviews.get(position).getAuthorName();
        holder.tvName.setText(authorName);
        holder.tvUserInitial.setText(
                String.valueOf(authorName.toUpperCase().charAt(0))
        );
        holder.tvReview.setText(reviews.get(position).getReview());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class MovieReviewViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName;
        final TextView tvReview;
        final TextView tvUserInitial;

        MovieReviewViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvReview = itemView.findViewById(R.id.tv_review);
            tvUserInitial = itemView.findViewById(R.id.tv_user_initial);
        }
    }
}
