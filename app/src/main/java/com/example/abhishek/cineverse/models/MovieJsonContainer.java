package com.example.abhishek.cineverse.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieJsonContainer {
    public int page;

    @SerializedName("total_results")
    public int totalResults;

    @SerializedName("total_pages")
    public int totalPages;

    @SerializedName("results")
    public List<Movie> movies;

    @Override
    public String toString() {
        return "MovieJsonContainer{" +
                "page=" + page +
                ", totalResults=" + totalResults +
                ", totalPages=" + totalPages +
                ", movies=" + movies +
                '}';
    }
}
