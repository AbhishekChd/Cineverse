package com.example.abhishek.cineverse.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class MovieViewModel extends ViewModel {

    private MutableLiveData<List<Movie>> mMutableMovieList;

    public LiveData<List<Movie>> getMovies() {
        if (mMutableMovieList == null) {
            mMutableMovieList = new MutableLiveData<>();
        }

        // TODO: 12/6/18 Set network tasks for List of Movies

        return mMutableMovieList;
    }
}
