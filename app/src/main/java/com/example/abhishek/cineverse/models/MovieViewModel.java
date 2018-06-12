package com.example.abhishek.cineverse.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.net.Uri;

import com.example.abhishek.cineverse.BuildConfig;
import com.example.abhishek.cineverse.R;
import com.example.abhishek.cineverse.data.UrlContract;
import com.example.abhishek.cineverse.network.GsonRequest;
import com.example.abhishek.cineverse.network.NetworkRequestQueue;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieViewModel extends ViewModel {

    @SerializedName("results")
    public List<Movie> mMovies;
    private MutableLiveData<List<Movie>> mMutableMovieList;

    @Override
    public String toString() {
        return "MovieViewModel{" +
                "mMovies=" + mMovies +
                '}';
    }

    public LiveData<List<Movie>> getMoviesLiveData(Context context) {
        if (mMutableMovieList == null) {
            mMutableMovieList = new MutableLiveData<>();
            getMovies(context, context.getString(R.string.sort_by_popularity));
        }

        return mMutableMovieList;
    }

    private void getMovies(Context context, String sort) {
        String url = Uri.parse(UrlContract.BASE_URL_MOVIE).buildUpon()
                .appendPath(
                        sort.equalsIgnoreCase(context.getString(R.string.sort_by_popularity)) ?
                                UrlContract.ENDPOINT_MOVIE_POPULAR
                                : UrlContract.ENDPOINT_MOVIE_TOP_RATED
                )
                .appendQueryParameter("language", "en")
                .appendQueryParameter("page", "1")
                .appendQueryParameter("api_key", BuildConfig.ApiKey)
                .toString();

        GsonRequest<MovieJsonContainer> movieGsonRequest = new GsonRequest<>(
                url,
                MovieJsonContainer.class,
                null,
                response -> mMutableMovieList.setValue(response.movies),
                error -> {
                    // TODO: 12/6/18 Handle error
                }
        );
        NetworkRequestQueue.getInstance(context).addToRequestQueue(movieGsonRequest);
    }

    public void sortMoviesBy(Context context, String sort) {
        if (sort.equalsIgnoreCase(context.getString(R.string.sort_by_popularity)) ||
                sort.equalsIgnoreCase(context.getString(R.string.sort_by_rating)))
            getMovies(context, sort);
        else
            getMovies(context, context.getString(R.string.sort_by_popularity));
    }
}