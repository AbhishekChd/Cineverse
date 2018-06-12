package com.example.abhishek.cineverse.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.abhishek.cineverse.R;
import com.example.abhishek.cineverse.adapters.MovieAdapter;
import com.example.abhishek.cineverse.fragments.SortDialogFragment;
import com.example.abhishek.cineverse.models.MovieViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeScreenActivity extends AppCompatActivity implements SortDialogFragment.SortDialogListener {

    @BindView(R.id.rv_movies_list)
    RecyclerView rvMoviesList;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        int orientation = getResources().getConfiguration().orientation;
        GridLayoutManager layoutManager =
                new GridLayoutManager(this, orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 3);
        MovieAdapter adapter = new MovieAdapter(this, null);
        rvMoviesList.setAdapter(adapter);
        rvMoviesList.setLayoutManager(layoutManager);

        movieViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MovieViewModel.class);

        movieViewModel.getMoviesLiveData(this).observe(this, movies -> {
            Log.v("Debug App", "Fetched Data: " + movies.toString());
            adapter.movies = movies;
            adapter.notifyDataSetChanged();
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
                SortDialogFragment sortDialog = new SortDialogFragment();
                // TODO: 13/6/18 Change TAG and add more properties if link{needed https://developer.android.com/guide/topics/ui/dialogs}
                sortDialog.show(getSupportFragmentManager(), "TAG");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDialogItemClick(int which) {
        String[] options = getResources().getStringArray(R.array.filter_sort);
        movieViewModel.sortMoviesBy(this, options[which]);
    }
}
