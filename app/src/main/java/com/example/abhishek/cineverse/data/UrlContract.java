package com.example.abhishek.cineverse.data;

public final class UrlContract {
    public static final String BASE_API_URL = "https://api.themoviedb.org/3/";

    // https://api.themoviedb.org/3/movie/
    public static final String ENDPOINT_MOVIE = "movie";
    public static final String BASE_URL_MOVIE = BASE_API_URL + "movie";

    public static final int POPULAR = 0;
    public static final int TOP_RATED = 1;

    // Image base urls
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p";
    public static final String BASE_ORIGINAL_IMAGE_URL = BASE_IMAGE_URL + "/original";
    public static final String BASE_POSTER_SMALL_URL = BASE_IMAGE_URL + "/w185";
    public static final String BASE_POSTER_LARGE_URL = BASE_IMAGE_URL + "/w342";
    public static final String BASE_BACKDROP_SMALL_URL = BASE_IMAGE_URL + "/w780";
    public static final String BASE_BACKDROP_LARGE_URL = BASE_IMAGE_URL + "/w1280";

}
