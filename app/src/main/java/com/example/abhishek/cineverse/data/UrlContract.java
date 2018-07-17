package com.example.abhishek.cineverse.data;

public final class UrlContract {
    // https://api.themoviedb.org/3/movie/
    public static final String BASE_API_URL = "https://api.themoviedb.org/3/";

    // Youtube video and thumbnail base url
    /**
     * Use {@code String.format()} to replace {@code %s} with your YouTube video {@code id}
     * <p>
     * <p>
     * <em>Example usage:</em> {@code String.format(YOUTUBE_VIDEO_URL, youtube_video_id);}
     * <p>
     * </p>
     */
    public static final String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v=%s";
    /**
     * Use {@code String.format()} to replace {@code %s} with your YouTube video {@code id}
     * <p>
     * <p>
     * <em>Example usage:</em> {@code String.format(YOUTUBE_THUMBNAIL_URL, youtube_video_id);}
     * <p>
     * </p>
     */
    public static final String YOUTUBE_THUMBNAIL_URL =
            "https://img.youtube.com/vi/%s/mqdefault.jpg";
    // Image base urls
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p";
    public static final String BASE_POSTER_LARGE_URL = BASE_IMAGE_URL + "/w342";
    public static final String BASE_BACKDROP_SMALL_URL = BASE_IMAGE_URL + "/w780";
}
