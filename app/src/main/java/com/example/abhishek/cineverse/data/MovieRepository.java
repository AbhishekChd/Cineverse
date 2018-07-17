package com.example.abhishek.cineverse.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.abhishek.cineverse.AppExecutors;
import com.example.abhishek.cineverse.data.database.MovieDao;
import com.example.abhishek.cineverse.data.network.MovieNetworkDataSource;
import com.example.abhishek.cineverse.models.FavouriteMovieEntry;
import com.example.abhishek.cineverse.models.Movie;
import com.example.abhishek.cineverse.models.MovieReviews;
import com.example.abhishek.cineverse.models.MovieVideos;

import java.util.List;

public class MovieRepository {
    private static final String LOG_TAG = MovieRepository.class.getSimpleName();

    // For singleton instantiation
    private static final Object LOCK = new Object();
    private static MovieRepository sInstance;
    private static String criteria;
    private static MutableLiveData<List<Movie>> mLatestDatabaseData;
    private final MovieDao movieDao;
    private final MovieNetworkDataSource mMovieNetworkDataSource;
    private final AppExecutors mExecutor;

    private MovieRepository(MovieDao movieDao, AppExecutors executor, MovieNetworkDataSource movieNetworkDataSource) {
        this.movieDao = movieDao;
        this.mMovieNetworkDataSource = movieNetworkDataSource;
        this.mExecutor = executor;
        mLatestDatabaseData = new MutableLiveData<>();
        LiveData<List<Movie>> networkData = movieNetworkDataSource.getDownloadedMovies();
        networkData.observeForever(latestMoviesData -> {
            if (latestMoviesData != null) {
                for (Movie movie : latestMoviesData) {
                    movie.setCriteria(criteria);
                }
                mLatestDatabaseData.setValue(latestMoviesData);
                executor.getDiskIO().execute(() -> movieDao.bulkMovieInsert(
                        latestMoviesData.toArray(new Movie[latestMoviesData.size()])
                ));
                Log.d(LOG_TAG, "Added new movies for :" + criteria);
            }
        });
    }

    public synchronized static MovieRepository getInstance(
            MovieDao movieDao,
            AppExecutors executor,
            MovieNetworkDataSource movieNetworkDataSource) {
        Log.d(LOG_TAG, "Getting Movie Repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieRepository(movieDao, executor, movieNetworkDataSource);
                Log.d(LOG_TAG, "Made new Movie Repository");
            }
        }
        return sInstance;
    }

    public LiveData<List<Movie>> fetchMovieBy(int sortBy) {
        if (sortBy == AppConstants.POPULAR) {
            MovieRepository.criteria = AppConstants.CRITERIA_POPULAR;
        } else if (sortBy == AppConstants.TOP_RATED) {
            MovieRepository.criteria = AppConstants.CRITERIA_TOP_RATED;
        } else {
            return getFavouriteMovies();
        }
        initializeData(MovieRepository.criteria);
        return mLatestDatabaseData;
    }

    private void initializeData(String criteria) {
        mMovieNetworkDataSource.fetchMoviesByCriteria(criteria);
    }

    public LiveData<MovieVideos> fetchMovieVideos(int movieId) {
        return mMovieNetworkDataSource.fetchMovieVideos(movieId);
    }

    public LiveData<MovieReviews> fetchMovieReviews(int movieId) {
        return mMovieNetworkDataSource.fetchMovieReviews(movieId);
    }

    private LiveData<List<Movie>> getFavouriteMovies() {
        mExecutor.getDiskIO().execute(() -> {
            LiveData<List<Movie>> favouriteMovies =
                    movieDao.getAllFavouriteMovies();
            Observer<List<Movie>> observer = new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    if (movies != null) {
                        mLatestDatabaseData.postValue(movies);
                        favouriteMovies.removeObserver(this);
                    }
                }
            };
            favouriteMovies.observeForever(observer);
        });
        return mLatestDatabaseData;
    }

    public void addOrRemoveFavourites(int id) {
        mExecutor.getDiskIO().execute(() -> {
            int count = movieDao.getFavouriteCountById(id);

            if (count > 0) {
                movieDao.deleteFromFavourites(id);
            } else {
                movieDao.insertFavouriteMovie(new FavouriteMovieEntry(id));
            }
        });
    }

    public LiveData<Boolean> isFavourite(int id) {
        return movieDao.isFavourite(id);
    }
}
