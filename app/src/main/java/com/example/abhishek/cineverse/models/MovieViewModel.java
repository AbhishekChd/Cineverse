package com.example.abhishek.cineverse.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.abhishek.cineverse.AppExecutors;
import com.example.abhishek.cineverse.BuildConfig;
import com.example.abhishek.cineverse.data.MovieDatabase;
import com.example.abhishek.cineverse.data.UrlContract;
import com.example.abhishek.cineverse.network.MovieClient;
import com.example.abhishek.cineverse.network.MovieService;

import java.util.Arrays;
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
            public void onResponse(Call<MovieJsonContainer> call
                    , Response<MovieJsonContainer> moviesResponse) {
                if (moviesResponse.raw().cacheResponse() != null) {
                    Log.d(LOG_TAG, "Response from cache");
                }
                if (moviesResponse.raw().networkResponse() != null) {
                    Log.d(LOG_TAG, "Response from network");
                }

                MovieJsonContainer container = moviesResponse.body();
                if (container != null) {
                    Log.d(LOG_TAG, String.valueOf(moviesResponse.headers()));
                    mMutableMovieList.setValue(setFavoriteMovies(container.movies));
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
        Call<MovieJsonContainer> call = null;

        switch (sort) {
            case UrlContract.POPULAR:
                call = movieService.getPopularMovies(BuildConfig.ApiKey);
                break;
            case UrlContract.TOP_RATED:
                call = movieService.getTopRatedMovies(BuildConfig.ApiKey);
                break;
            case UrlContract.FAVOURITES:
                fetchFavouriteMovies();
                break;
            default:
                call = movieService.getPopularMovies(BuildConfig.ApiKey);
                break;
        }

        if (call != null) {
            call.enqueue(retrofitCallback);
        }
    }


    private List<Movie> setFavoriteMovies(List<Movie> movies) {
        MovieDatabase database = MovieDatabase.getInstance(getApplication());
        AppExecutors.getInstance().getDiskIO().execute(() -> {
            int[] favIds = database.movieDao().getFavoriteMovieIds();
            Arrays.sort(favIds);

            int n = movies.size();
            for (int i = 0; i < n; i++) {
                if (Arrays.binarySearch(favIds, movies.get(i).getId()) >= 0) {
                    movies.get(i).setFavorite(true);
                }
            }
        });
        return movies;
    }

    private void fetchFavouriteMovies() {
        MovieDatabase database = MovieDatabase.getInstance(getApplication());
        AppExecutors.getInstance().getDiskIO().execute(() -> {
            List<Movie> movies = database.movieDao().getFavouriteMovies();
            mMutableMovieList.postValue(movies);
        });
    }
}
