package com.example.abhishek.cineverse.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.abhishek.cineverse.BuildConfig;
import com.example.abhishek.cineverse.data.UrlContract;
import com.example.abhishek.cineverse.network.MovieClient;
import com.example.abhishek.cineverse.network.MovieService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieViewModel extends AndroidViewModel {

    private static final String LOG_TAG = MovieViewModel.class.getSimpleName();
    private final Callback<MovieJsonContainer> retrofitCallback;
    private MutableLiveData<List<Movie>> mMutableMovieList;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        retrofitCallback = new Callback<MovieJsonContainer>() {
            @Override
            public void onResponse(Call<MovieJsonContainer> call, Response<MovieJsonContainer> moviesResponse) {
                if (moviesResponse.raw().cacheResponse() != null) {
                    Log.d(LOG_TAG, "Response from cache");
                }
                if (moviesResponse.raw().networkResponse() != null) {
                    Log.d(LOG_TAG, "Response from network");
                }

                MovieJsonContainer container = moviesResponse.body();
                if (container != null) {
                    Log.d(LOG_TAG, String.valueOf(moviesResponse.headers()));
                    mMutableMovieList.setValue(container.movies);
                }
            }

            @Override
            public void onFailure(Call<MovieJsonContainer> call, Throwable t) {
                Log.e(getClass().getSimpleName(), "Error parsing JSON: " + t.getMessage());
            }
        };
    }

    public LiveData<List<Movie>> getMoviesLiveData() {
        if (mMutableMovieList == null) {
            mMutableMovieList = new MutableLiveData<>();
        }

        return mMutableMovieList;
    }

    /**
     * Sort movies based on selection
     *
     * @param sort Sort criteria and Defaults when passed null
     */
    public void fetchMoviesByFilter(int sort) {
        Context context = getApplication().getBaseContext();

        MovieClient movieService = MovieService.getInstance(context);
        Call<MovieJsonContainer> call;

        switch (sort) {
            case UrlContract.POPULAR:
                call = movieService.getPopularMovies(BuildConfig.ApiKey);
                break;
            case UrlContract.TOP_RATED:
                call = movieService.getTopRatedMovies(BuildConfig.ApiKey);
                break;
            default:
                call = movieService.getPopularMovies(BuildConfig.ApiKey);
                break;
        }

        call.enqueue(retrofitCallback);
    }
}
