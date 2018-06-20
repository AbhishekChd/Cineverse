package com.example.abhishek.cineverse.helpers;

import com.example.abhishek.cineverse.data.UrlContract;

/**
 * An Image Util class to load images from {@link UrlContract}
 * of different sizes.
 */
public final class ImageUrlUtils {

    /**
     * Load large poster from posterPath
     * @param posterPath relative path of Poster
     * @return Complete poster url
     */
    public static String getLargePosterUrl(String posterPath) {
        return UrlContract.BASE_POSTER_LARGE_URL + posterPath;
    }

    public static String getSmallBackdropUrl(String backdropPath){
        return UrlContract.BASE_BACKDROP_SMALL_URL + backdropPath;
    }
}
