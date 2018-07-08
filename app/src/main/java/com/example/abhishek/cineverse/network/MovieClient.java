package com.example.abhishek.cineverse.network;

import com.example.abhishek.cineverse.models.MovieJsonContainer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieClient {
    @GET("movie/popular")
    Call<MovieJsonContainer> getPopularMovies(@Query("api_key") String apiKey);
}
