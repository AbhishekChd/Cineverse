package com.example.abhishek.cineverse.data;

public final class UrlContract {
    public static final String BASE_API_URL = "https://api.themoviedb.org/3/";

    // https://api.themoviedb.org/3/movie/
    public static final String ENDPOINT_MOVIE = "movie";
    public static final String BASE_URL_MOVIE = BASE_API_URL + "movie";

    public static final String ENDPOINT_MOVIE_POPULAR = "popular";
    public static final String ENDPOINT_MOVIE_TOP_RATED = "top_rated";
}
