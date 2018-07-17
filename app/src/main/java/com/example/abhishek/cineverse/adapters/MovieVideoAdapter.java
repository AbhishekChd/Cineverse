package com.example.abhishek.cineverse.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.abhishek.cineverse.R;
import com.example.abhishek.cineverse.models.MovieVideos;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoAdapter.MovieVideoViewHolder> {
    private static final String LOG_TAG = MovieVideoAdapter.class.getSimpleName();
    private final List<MovieVideos.VideoResult> videos;
    private final MovieVideoOnClickListener mOnClickListener;

    public MovieVideoAdapter(List<MovieVideos.VideoResult> videos, MovieVideoOnClickListener listener) {
        this.videos = videos;
        this.mOnClickListener = listener;
    }

    @NonNull
    @Override
    public MovieVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.movie_trailer_card, parent, false);
        return new MovieVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVideoViewHolder holder, int position) {
        String videoUrl = videos.get(position).getVideoThumbnailUrl();
        if (videoUrl != null) {
            if (!videoUrl.isEmpty()) {
                Picasso.with(holder.ivThumbnail.getContext())
                        .load(videoUrl)
                        .into(holder.ivThumbnail);
            }
        }
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public interface MovieVideoOnClickListener {
        void onClick(String videoUrl);
    }

    class MovieVideoViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivThumbnail;
        final ImageView ivPlayButton;

        public MovieVideoViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            ivPlayButton = itemView.findViewById(R.id.iv_play_button);

            ivPlayButton.setOnClickListener(v -> mOnClickListener.onClick(videos.get(getAdapterPosition()).getVideoUrl()));
        }
    }
}
