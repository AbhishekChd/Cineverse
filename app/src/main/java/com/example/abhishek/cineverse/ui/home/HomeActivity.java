package com.example.abhishek.cineverse.ui.home;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.abhishek.cineverse.R;
import com.example.abhishek.cineverse.adapters.MovieAdapter;
import com.example.abhishek.cineverse.data.AppConstants;
import com.example.abhishek.cineverse.fragments.SortDialogFragment;
import com.example.abhishek.cineverse.models.Movie;
import com.example.abhishek.cineverse.ui.detail.DetailActivity;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
        implements SortDialogFragment.SortDialogListener,
        MovieAdapter.MovieAdapterOnClickHandler,
        LifecycleOwner {

    private static final String sortDialogFragmentTag = "sort-dialog";
    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    private static int sCriteria;
    @BindView(R.id.rv_movies_list)
    RecyclerView rvMoviesList;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private HomeViewModel mHomeViewModel;
    private SortDialogFragment sortDialog;
    private Snackbar noInternetSnack;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        // Setup toolbar
        setSupportActionBar(toolbar);

        sortDialog = new SortDialogFragment();

        // Set grid layout and Adapter
        setupRecyclerView();

        // Setup ViewModel and LiveData
        setupMoviesViewModel();
    }

    private void loadInitialDataByPreference() {
        SharedPreferences preferences =
                getSharedPreferences(getString(R.string.sort_preference), Context.MODE_PRIVATE);
        sCriteria = preferences.getInt(
                getString(R.string.sort_by_key),
                AppConstants.POPULAR
        );
        // Display data on recycler view or error if nt internet
        showDataOrError(sCriteria);
    }

    private void setupMoviesViewModel() {
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mHomeViewModel.getMoviesLiveData(sCriteria).observe(this, movies -> {
            if (movies != null) {
                adapter.setMovieData(movies);
            }
        });
    }

    private void setupRecyclerView() {
        int orientation = getResources().getConfiguration().orientation;
        GridLayoutManager layoutManager =
                new GridLayoutManager(this, orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 4);
        adapter = new MovieAdapter(this);
        rvMoviesList.setAdapter(adapter);
        rvMoviesList.setLayoutManager(layoutManager);
    }

    /**
     * Show data or error based on connectivity
     *
     * @param choice Selected option for sort
     */
    private void showDataOrError(int choice) {
        if (choice == AppConstants.FAVOURITES) {
            mHomeViewModel.fetchMoviesByFilter(choice);
        }
        if (!isConnected()) {
            noInternetSnack =
                    Snackbar.make(findViewById(R.id.coordinator), R.string.no_internet_error_message, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.retry_action, view -> showDataOrError(choice))
                            .setActionTextColor(getResources().getColor(R.color.colorAccent));
            noInternetSnack.show();
        } else {
            noInternetSnack = null;
            mHomeViewModel.fetchMoviesByFilter(choice);
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
        return Objects.requireNonNull(connectivityManager).getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(int index, View view) {
        List<Movie> movies = mHomeViewModel
                .getMoviesLiveData(sCriteria)
                .getValue();
        Movie movie = null;
        if (movies != null) {
            movie = movies.get(index);
        }
        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra(DetailActivity.ARG_PARAM, movie);
        detailIntent.putExtra(DetailActivity.IMAGE_TRANSITION_NAME,
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
    public void onDialogItemClick(int which) {
        SharedPreferences preferences =
                getSharedPreferences(getString(R.string.sort_preference), Context.MODE_PRIVATE);
        switch (which) {
            case 0:
                showDataOrError(AppConstants.POPULAR);
                preferences
                        .edit()
                        .putInt(getString(R.string.sort_by_key), AppConstants.POPULAR)
                        .apply();
                sCriteria = AppConstants.POPULAR;
                break;
            case 1:
                showDataOrError(AppConstants.TOP_RATED);
                preferences
                        .edit()
                        .putInt(getString(R.string.sort_by_key), AppConstants.TOP_RATED)
                        .apply();
                sCriteria = AppConstants.TOP_RATED;
                break;
            case 2:
                preferences
                        .edit()
                        .putInt(getString(R.string.sort_by_key), AppConstants.FAVOURITES)
                        .apply();
                showDataOrError(AppConstants.FAVOURITES);
                sCriteria = AppConstants.FAVOURITES;
                break;
            default:
                showDataOrError(AppConstants.POPULAR);
                preferences
                        .edit()
                        .putInt(getString(R.string.sort_by_key), AppConstants.POPULAR)
                        .apply();
                sCriteria = AppConstants.POPULAR;
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        noInternetSnack = null;
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        loadInitialDataByPreference();
        super.onStart();
    }
}
