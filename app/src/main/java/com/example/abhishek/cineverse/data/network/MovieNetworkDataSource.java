package com.example.abhishek.cineverse.data.network;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.abhishek.cineverse.BuildConfig;
import com.example.abhishek.cineverse.models.Movie;
import com.example.abhishek.cineverse.models.MovieJsonContainer;
import com.example.abhishek.cineverse.models.MovieReviews;
import com.example.abhishek.cineverse.models.MovieVideos;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieNetworkDataSource {

    private static final String LOG_TAG = MovieNetworkDataSource.class.getSimpleName();

    // For static initialisation
    private static final Object LOCK = new Object();
    private static MovieNetworkDataSource sInstance;
    private final MutableLiveData<List<Movie>> mDownloadedMovies;
    private final MutableLiveData<MovieVideos> mMovieVideos;
    private final MutableLiveData<MovieReviews> mMovieReviews;
    private final MovieClient movieClient;

    private MovieNetworkDataSource(MovieClient movieClient) {
        mDownloadedMovies = new MutableLiveData<>();
        mMovieVideos = new MutableLiveData<>();
        mMovieReviews = new MutableLiveData<>();
        this.movieClient = movieClient;
    }

    public synchronized static MovieNetworkDataSource getInstance(MovieClient movieClient) {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieNetworkDataSource(movieClient);
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    public void fetchMoviesByCriteria(String criteria) {
        Call<MovieJsonContainer> moviesNetworkCall = movieClient.getMoviesByCriteria(
                criteria, BuildConfig.ApiKey);

        moviesNetworkCall.enqueue(new Callback<MovieJsonContainer>() {
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
                    mDownloadedMovies.postValue(container.movies);
                }
            }

            @Override
            public void onFailure(Call<MovieJsonContainer> call, Throwable t) {

            }
        });
    }

    public LiveData<MovieVideos> fetchMovieVideos(int id) {
        Call<MovieVideos> movieVideosCall = movieClient.getMovieTrailers(
                id,
                BuildConfig.ApiKey
        );

        movieVideosCall.enqueue(new Callback<MovieVideos>() {
            @Override
            public void onResponse(Call<MovieVideos> call, Response<MovieVideos> response) {
                MovieVideos movieVideos = response.body();
                if (movieVideos != null) {
                    mMovieVideos.postValue(movieVideos);
                    Log.d(LOG_TAG, "Got video data from network : "
                            + movieVideos.videos.size()
                    );
                }
            }

            @Override
            public void onFailure(Call<MovieVideos> call, Throwable t) {
                @SuppressLint("DefaultLocale")
                String errorMessage = String.format(
                        "Could not fetch movie video for id %d due to error %s",
                        id,
                        t.getMessage()
                );
                Log.e(LOG_TAG, errorMessage);
            }
        });
        return mMovieVideos;
    }

    public LiveData<MovieReviews> fetchMovieReviews(int id) {
        Call<MovieReviews> movieReviewsCall = movieClient.getMovieReviews(
                id,
                BuildConfig.ApiKey
        );

        movieReviewsCall.enqueue(new Callback<MovieReviews>() {
            @Override
            public void onResponse(Call<MovieReviews> call, Response<MovieReviews> response) {
                MovieReviews movieReviews = response.body();
                if (movieReviews != null) {
                    mMovieReviews.postValue(movieReviews);
                    Log.d(LOG_TAG, "Got review data from network : "
                            + movieReviews.reviews.size()
                    );
                }
            }

            @Override
            public void onFailure(Call<MovieReviews> call, Throwable t) {
                @SuppressLint("DefaultLocale")
                String errorMessage = String.format(
                        "Could not fetch movie review for id %d due to error %s",
                        id,
                        t.getMessage()
                );
                Log.e(LOG_TAG, errorMessage);
            }
        });
        return mMovieReviews;
    }

    public LiveData<List<Movie>> getDownloadedMovies() {
        return mDownloadedMovies;
    }
}
