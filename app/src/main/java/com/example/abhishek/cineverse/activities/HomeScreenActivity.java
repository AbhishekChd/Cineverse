package com.example.abhishek.cineverse.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.abhishek.cineverse.AppExecutors;
import com.example.abhishek.cineverse.R;
import com.example.abhishek.cineverse.adapters.MovieAdapter;
import com.example.abhishek.cineverse.data.MovieDatabase;
import com.example.abhishek.cineverse.data.UrlContract;
import com.example.abhishek.cineverse.fragments.SortDialogFragment;
import com.example.abhishek.cineverse.models.Movie;
import com.example.abhishek.cineverse.models.MovieViewModel;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeScreenActivity extends AppCompatActivity implements SortDialogFragment.SortDialogListener, MovieAdapter.MovieAdapterOnClickHandler {

    private static final String sortDialogFragmentTag = "sort-dialog";
    private static final String LOG_TAG = HomeScreenActivity.class.getSimpleName();
    @BindView(R.id.rv_movies_list)
    RecyclerView rvMoviesList;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.app_bar)
    AppBarLayout app_bar;

    private MovieViewModel movieViewModel;
    private SortDialogFragment sortDialog;
    private Snackbar noInternetSnack;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ButterKnife.bind(this);

        // Setup toolbar
        setSupportActionBar(toolbar);

        sortDialog = new SortDialogFragment();

        // Set grid layout and Adapter
        setupRecyclerView();

        // Setup ViewModel and LiveData
        setupMoviesViewModel();

        // Display data on recycler view or error if nt internet
        showDataOrError(UrlContract.POPULAR);
    }

    private void setupMoviesViewModel() {
        movieViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MovieViewModel.class);
        movieViewModel.getMoviesLiveData().observe(this, movies -> {
            if (movies != null) {
                adapter.setMovieData(movies);
            }
        });
    }

    private void setupRecyclerView() {
        int orientation = getResources().getConfiguration().orientation;
        GridLayoutManager layoutManager =
                new GridLayoutManager(this, orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 4);
        adapter = new MovieAdapter(this, this);
        rvMoviesList.setAdapter(adapter);
        rvMoviesList.setLayoutManager(layoutManager);
    }

    /**
     * Show data or error based on connectivity
     *
     * @param choice Selected option for sort
     */
    private void showDataOrError(int choice) {
        if (!isConnected()) {
            noInternetSnack =
                    Snackbar.make(findViewById(R.id.coordinator), "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Retry", view -> showDataOrError(choice))
                            .setActionTextColor(getResources().getColor(R.color.colorAccent));
            noInternetSnack.show();
        } else {
            noInternetSnack = null;
            movieViewModel.fetchMoviesByFilter(choice);
        }
    }

    /**
     * Check internet connection
     *
     * @return returns true if connected
     */
    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(int index, View view) {
        List<Movie> movies = movieViewModel
                .getMoviesLiveData()
                .getValue();
        Movie movie = null;
        if (movies != null) {
            movie = movies.get(index);
        }
        Intent detailIntent = new Intent(this, DetailActivty.class);
        detailIntent.putExtra(DetailActivty.ARG_PARAM, movie);
        detailIntent.putExtra(DetailActivty.IMAGE_TRANSITION_NAME,
                ViewCompat.getTransitionName(view));

        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this,
                        view,
                        ViewCompat.getTransitionName(view)
                );

        startActivity(detailIntent, optionsCompat.toBundle());
    }

    @Override
    public void onFavButtonClick(int index) {
        Log.d(LOG_TAG, "Entering fav onClick");
        MovieDatabase database = MovieDatabase.getInstance(this);
        AppExecutors.getInstance().getDiskIO().execute(() -> {
            List<Movie> movieList = movieViewModel
                    .getMoviesLiveData()
                    .getValue();

            if (movieList != null) {
                Movie movie = movieList.get(index);
                if (movie.isFavorite()) {
                    database.movieDao().deleteMovie(movie);
                    movie.setFavorite(false);
                } else {
                    movie.setFavorite(true);
                    movie.setDateAdded(new Date());
                    database.movieDao().insertMovie(movieList.get(index));
                }
            }
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogItemClick(int which) {
        switch (which) {
            case 0:
                showDataOrError(UrlContract.POPULAR);
                break;
            case 1:
                showDataOrError(UrlContract.TOP_RATED);
                break;
            default:
                showDataOrError(UrlContract.POPULAR);
                break;
        }
        sortDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                // Start sort dialog
                sortDialog.show(getSupportFragmentManager(), sortDialogFragmentTag);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        noInternetSnack = null;
        super.onDestroy();
    }
}
