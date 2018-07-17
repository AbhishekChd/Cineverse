package com.example.abhishek.cineverse.ui.detail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.abhishek.cineverse.data.MovieRepository;

public class DetailViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
    private final int movieId;
    private final MovieRepository mRepository;
    private final Application application;

    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link AndroidViewModel}
     */
    public DetailViewModelFactory(@NonNull Application application, MovieRepository repository, int movieId) {
        super(application);
        this.application = application;
        this.mRepository = repository;
        this.movieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailViewModel(application, mRepository, movieId);
    }
}
