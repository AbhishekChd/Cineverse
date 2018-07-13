package com.example.abhishek.cineverse.activities;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.abhishek.cineverse.R;
import com.example.abhishek.cineverse.helpers.DateUtils;
import com.example.abhishek.cineverse.helpers.ImageUrlUtils;
import com.example.abhishek.cineverse.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivty extends AppCompatActivity {

    public static final String ARG_PARAM = "movie";
    public static final String IMAGE_TRANSITION_NAME = "transition-name";

    @BindView(R.id.tv_title)
    TextView tvTitle;
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
    @BindView(R.id.toolbar_detail)
    Toolbar toolbar;
    @BindView(R.id.iv_fav_movie)
    ImageView ivFavMovie;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        // setup poster image transition
        setPosterImageTransition();
        movie = getIntent().getParcelableExtra(ARG_PARAM);

        setupUiElements(movie);
    }

    private void setPosterImageTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivMoviePoster.setTransitionName(
                    Objects.requireNonNull(
                            getIntent().getExtras()).getString(IMAGE_TRANSITION_NAME)
            );
        }
    }

    private void setupUiElements(Movie movie) {
        tvTitle.setText(movie.getTitle());
        tvTitle.setSelected(true);
        tvOverview.setText(movie.getOverview());

        tvReleaseDate.setText(
                DateUtils.getFullDateFromShortDate(movie.getReleaseDate())
        );

        tvRating.setText(getString(R.string.rating_format, movie.getVotes()));
        pbRating.setProgress((int) movie.getVotes() * 10);

        setupImageViews();

        // Adding elevation for all versions
        ViewCompat.setElevation(ivMoviePoster, getResources().getDimension(R.dimen.default_poster_elevation));

        // Adding radius for supported devices
        adRadiusToPoster();
    }

    private void setupImageViews() {
        if (movie.isFavorite()) {
            ivFavMovie.setImageDrawable(
                    getResources()
                            .getDrawable(R.drawable.ic_action_is_fav)
            );
        } else {
            ivFavMovie.setImageDrawable(
                    getResources()
                            .getDrawable(R.drawable.ic_action_is_not_fav));
        }


        Picasso.with(this)
                .load(ImageUrlUtils.getLargePosterUrl(movie.getPosterPath()))
                .noFade()
                .into(ivMoviePoster);

        Picasso.with(this)
                .load(ImageUrlUtils.getSmallBackdropUrl(movie.getBackdropPath()))
                .into(ivBackdrop);
    }

    private void adRadiusToPoster() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivMoviePoster.setOutlineProvider(new ViewOutlineProvider() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), getResources().getDimension(R.dimen.default_poster_radius));
                }
            });
            ivMoviePoster.setClipToOutline(true);
        }
    }
}
