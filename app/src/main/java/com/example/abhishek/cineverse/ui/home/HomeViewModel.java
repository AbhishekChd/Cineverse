package com.example.abhishek.cineverse.ui.home;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.abhishek.cineverse.data.MovieRepository;
import com.example.abhishek.cineverse.helpers.InjectorUtils;
import com.example.abhishek.cineverse.models.Movie;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private static final String LOG_TAG = HomeViewModel.class.getSimpleName();
    private final MovieRepository movieRepository;
    private LiveData<List<Movie>> mMoviesLiveData;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        movieRepository = InjectorUtils.provideRepository(application.getBaseContext());
    }


    public LiveData<List<Movie>> getMoviesLiveData(int criteria) {
        if (mMoviesLiveData == null) {
            fetchMoviesByFilter(criteria);
        }
        return mMoviesLiveData;
    }

    /**
     * Sort movies based on selection
     *
     * @param sort Sort criteria and Defaults when passed null
     */
    public void fetchMoviesByFilter(int sort) {
        mMoviesLiveData = movieRepository.fetchMovieBy(sort);
    }


    private List<Movie> setFavoriteMovies(List<Movie> movies) {
//        MovieDatabase database = MovieDatabase.getInstance(getApplication());
//        AppExecutors.getInstance().getDiskIO().execute(() -> {
//            int[] favIds = database.movieDao().getFavoriteMovieIds();
//            Arrays.sort(favIds);
//
//            int n = movies.size();
//            for (int i = 0; i < n; i++) {
//                if (Arrays.binarySearch(favIds, movies.get(i).getId()) >= 0) {
//                    movies.get(i).setFavorite(true);
//                }
//            }
//        });
        return movies;
    }
}
