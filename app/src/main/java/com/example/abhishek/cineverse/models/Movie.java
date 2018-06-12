package com.example.abhishek.cineverse.models;

import com.google.gson.annotations.SerializedName;

public class Movie {
    private String title;
    private float votes;

    @SerializedName("original_title")
    private String originalTitle;

    private String overview;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;


    /**
     * An instance of Movie to store it's details
     *
     * @param title:         Title of the movie
     * @param originalTitle: Title of the movie in native language
     * @param overview:      A synopsis of the movie
     * @param releaseDate:   Release date of the movie
     * @param posterPath:    Relative path for a Poster
     * @param backdropPath:  Relative path for a Backdrop
     * @param votes:         Votes for the movie out of 10
     */
    public Movie(String title, String originalTitle, String overview, String releaseDate, String posterPath, String backdropPath, float votes) {
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.votes = votes;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public float getVotes() {
        return votes;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", votes=" + votes +
                '}';
    }
}
