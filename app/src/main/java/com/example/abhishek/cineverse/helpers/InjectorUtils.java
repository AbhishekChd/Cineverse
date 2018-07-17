package com.example.abhishek.cineverse.helpers;

import android.app.Application;
import android.content.Context;

import com.example.abhishek.cineverse.AppExecutors;
import com.example.abhishek.cineverse.data.MovieRepository;
import com.example.abhishek.cineverse.data.database.MovieDao;
import com.example.abhishek.cineverse.data.database.MovieDatabase;
import com.example.abhishek.cineverse.data.network.MovieClient;
import com.example.abhishek.cineverse.data.network.MovieNetworkDataSource;
import com.example.abhishek.cineverse.data.network.MovieService;
import com.example.abhishek.cineverse.ui.detail.DetailViewModelFactory;

public class InjectorUtils {
    public static MovieRepository provideRepository(Context context) {
        MovieClient movieClient = MovieService.getInstance(context);
        MovieDao movieDao = MovieDatabase.getInstance(context).movieDao();
        AppExecutors executors = AppExecutors.getInstance();

        return MovieRepository.getInstance(
                movieDao, executors, MovieNetworkDataSource.getInstance(movieClient)
        );
    }

    public static DetailViewModelFactory provideDetailViewModelFactory
            (Application application, int id) {
        MovieRepository repository =
                InjectorUtils.provideRepository(application.getBaseContext());
        return new DetailViewModelFactory(application, repository, id);
    }
}
