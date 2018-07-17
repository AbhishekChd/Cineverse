package com.example.abhishek.cineverse.ui.detail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.abhishek.cineverse.data.MovieRepository;
import com.example.abhishek.cineverse.models.MovieReviews;
import com.example.abhishek.cineverse.models.MovieVideos;

public class DetailViewModel extends AndroidViewModel {
    private static final String LOG_TAG = DetailViewModel.class.getSimpleName();
    private final MovieRepository movieRepository;
    private LiveData<MovieVideos> mVideosLiveData;
    private LiveData<MovieReviews> mReviewsLiveData;

    public DetailViewModel(@NonNull Application application, MovieRepository repository, int movieId) {
        super(application);
        this.movieRepository = repository;
        this.mVideosLiveData = new MutableLiveData<>();
        this.mReviewsLiveData = new MutableLiveData<>();
        fetchMovieVideos(movieId);
        fetchMovieReviews(movieId);
    }

    public LiveData<MovieVideos> getVideosLiveData() {
        return mVideosLiveData;
    }

    public LiveData<MovieReviews> getReviewsLiveData() {
        return mReviewsLiveData;
    }


    private void fetchMovieVideos(int movieId) {
        mVideosLiveData = movieRepository.fetchMovieVideos(movieId);
    }

    private void fetchMovieReviews(int movieId) {
        mReviewsLiveData = movieRepository.fetchMovieReviews(movieId);
    }

    public void addOrRemoveFavourites(int movieId) {
        movieRepository.addOrRemoveFavourites(movieId);
    }

    public LiveData<Boolean> isFavourite(int movieId){
        return movieRepository.isFavourite(movieId);
    }
}
