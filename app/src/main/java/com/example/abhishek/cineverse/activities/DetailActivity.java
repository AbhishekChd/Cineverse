package com.example.abhishek.cineverse.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.abhishek.cineverse.R;
import com.example.abhishek.cineverse.data.UrlContract;
import com.example.abhishek.cineverse.helpers.DateUtils;
import com.example.abhishek.cineverse.models.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    //    @BindView(R.id.tv_movie_genre)
//    TextView tvGenre;
    @BindView(R.id.tv_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.tv_overview)
    TextView tvOverview;
    @BindView(R.id.tv_rating)
    TextView tvRating;
    @BindView(R.id.iv_backdrop)
    ImageView ivBackdrop;
    @BindView(R.id.iv_movie_poster)
    ImageView ivMoviePoster;
    @BindView(R.id.pb_rating)
    ProgressBar pbRating;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        Movie movie = getIntent().getParcelableExtra("movie");
        Log.v("Debug App", movie.toString());
        setupUiElements(movie);
    }

    private void setupUiElements(Movie movie) {
        tvTitle.setText(movie.getTitle());
        tvTitle.setSelected(true);
//        tvGenre.setText(getString(R.string.sample_genre));
        tvOverview.setText(movie.getOverview());

        tvReleaseDate.setText(
                DateUtils.getFullDateFromShortDate(
                        movie.getReleaseDate()
                )
        );

        tvRating.setText(getString(R.string.rating_format, movie.getVotes()));
        pbRating.setProgress((int) movie.getVotes() * 10);

        Picasso.with(this)
                .load(UrlContract.BASE_POSTER_LARGE_URL + movie.getPosterPath())
                .into(ivMoviePoster);

        Picasso.with(this)
                .load(UrlContract.BASE_BACKDROP_SMALL_URL + movie.getBackdropPath())
                .into(ivBackdrop);
    }
}
