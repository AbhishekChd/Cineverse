package com.example.abhishek.cineverse.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.abhishek.cineverse.R;
import com.example.abhishek.cineverse.adapters.MovieAdapter;
import com.example.abhishek.cineverse.fragments.DetailFragment;
import com.example.abhishek.cineverse.fragments.SortDialogFragment;
import com.example.abhishek.cineverse.models.MovieViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeScreenActivity extends AppCompatActivity implements SortDialogFragment.SortDialogListener, MovieAdapter.MovieAdapterOnClickHandler {

    private static final String sortDialogFragmentTag = "sort-dialog";
    @BindView(R.id.rv_movies_list)
    RecyclerView rvMoviesList;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.app_bar)
    AppBarLayout app_bar;

    private MovieViewModel movieViewModel;
    private SortDialogFragment sortDialog;
    private Snackbar noInternetSnack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ButterKnife.bind(this);

        // Setup toolbar
        setSupportActionBar(toolbar);
        Typeface font = ResourcesCompat.getFont(this, R.font.pacifico_regular);
        ((TextView) toolbar.getChildAt(0)).setTypeface(font);

        sortDialog = new SortDialogFragment();

        showDataOrError();
    }

    private void showDataOrError() {
        if (!isConnected()) {
            noInternetSnack =
                    Snackbar.make(findViewById(R.id.coordinator), "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Retry", view -> {
                                showDataOrError();
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorAccent));
            noInternetSnack.show();
        } else {
            noInternetSnack = null;
            loadAndDisplayMovies();
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void loadAndDisplayMovies() {
        // Set grid layout and Adapter
        int orientation = getResources().getConfiguration().orientation;
        GridLayoutManager layoutManager =
                new GridLayoutManager(this, orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 4);
        MovieAdapter adapter = new MovieAdapter(this, this);
        rvMoviesList.setAdapter(adapter);
        rvMoviesList.setLayoutManager(layoutManager);

        // Setup ViewModel and LiveData
        movieViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MovieViewModel.class);
        movieViewModel.getMoviesLiveData(this).observe(this, movies -> {
            Log.v("Debug App", "Fetched Data: " + movies.toString());
            adapter.setMovieData(movies);
        });
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
    public void onDialogItemClick(int which) {
        sortDialog.dismiss();
        String[] options = getResources().getStringArray(R.array.filter_sort);
        movieViewModel.sortMoviesBy(this, options[which]);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(int index) {
        DetailFragment fragment = DetailFragment.newInstance(
                movieViewModel
                        .getMoviesLiveData(this)
                        .getValue()
                        .get(index)
        );

        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();

        fragmentManager.addToBackStack("TAG");
        fragmentManager.replace(R.id.fragment_container, fragment, "TAG");
//        fragmentManager.addSharedElement(v, movieViewModel.getMoviesLiveData(this).getValue().get(index).getTitle());
        fragmentManager.commit();
    }
}
