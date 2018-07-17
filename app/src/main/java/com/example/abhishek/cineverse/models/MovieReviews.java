package com.example.abhishek.cineverse.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReviews {
    @SerializedName("results")
    public List<ReviewResult> reviews;

    @SerializedName("id")
    public int id;

    public class ReviewResult {
        // The name of the video
        @SerializedName("author")
        String authorName;

        @SerializedName("content")
        String review;

        public String getAuthorName() {
            return authorName;
        }

        public String getReview() {
            return review;
        }
    }
}
