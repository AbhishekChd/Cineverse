package com.example.abhishek.cineverse.data.network;

import com.example.abhishek.cineverse.models.MovieJsonContainer;
import com.example.abhishek.cineverse.models.MovieReviews;
import com.example.abhishek.cineverse.models.MovieVideos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieClient {
    @GET("movie/{criteria}")
    Call<MovieJsonContainer> getMoviesByCriteria(
            @Path("criteria") String criteria,
            @Query("api_key") String apiKey
    );

    @GET("movie/{id}/videos")
    Call<MovieVideos> getMovieTrailers(
            @Path("id") int movieId,
            @Query("api_key") String apiKey
    );

    @GET("movie/{id}/reviews")
    Call<MovieReviews> getMovieReviews(
            @Path("id") int movieId,
            @Query("api_key") String apiKey
    );
}
