package com.example.abhishek.cineverse.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.abhishek.cineverse.R;
import com.example.abhishek.cineverse.models.MovieViewModel;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        MovieViewModel movieViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MovieViewModel.class);

        movieViewModel.getMoviesLiveData(this).observe(this, movies -> {
            Log.v("Debug App", movies.toString());
        });
    }
}
