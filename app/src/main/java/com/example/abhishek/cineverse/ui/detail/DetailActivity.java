package com.example.abhishek.cineverse.ui.detail;

import android.annotation.TargetApi;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.abhishek.cineverse.R;
import com.example.abhishek.cineverse.adapters.MovieReviewAdapter;
import com.example.abhishek.cineverse.adapters.MovieVideoAdapter;
import com.example.abhishek.cineverse.helpers.DateUtils;
import com.example.abhishek.cineverse.helpers.ImageUrlUtils;
import com.example.abhishek.cineverse.helpers.InjectorUtils;
import com.example.abhishek.cineverse.models.Movie;
import com.example.abhishek.cineverse.models.MovieReviews;
import com.example.abhishek.cineverse.models.MovieVideos;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity
        implements MovieVideoAdapter.MovieVideoOnClickListener {

    public static final String ARG_PARAM = "movie";
    public static final String IMAGE_TRANSITION_NAME = "transition-name";
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private static String shareDetails;
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
    @BindView(R.id.tv_trailers)
    TextView tvTrailers;
    @BindView(R.id.rv_trailers)
    RecyclerView rvTrailers;
    @BindView(R.id.tv_reviews)
    TextView tvReviews;
    @BindView(R.id.rv_reviews)
    RecyclerView rvReviews;
    private Drawable isFav;
    private Drawable isNotFav;
    private Movie movie;
    private MovieVideoAdapter adapter;
    private boolean isFavourite = false;
    private DetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        isFav = getResources()
                .getDrawable(R.drawable.ic_action_is_fav);
        isNotFav = getResources()
                .getDrawable(R.drawable.ic_action_is_not_fav);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // setup poster image transition
        setPosterImageTransition();
        movie = getIntent().getParcelableExtra(ARG_PARAM);

        removeExtraDetails();

        setupUiElements(movie);

        // Setup DetailViewModel and observe Videos and Review data
        setupViewModel();

        ivFavMovie.setOnClickListener(v -> {
            viewModel.addOrRemoveFavourites(movie.getId());
            isFavourite = !isFavourite;
            setupFavIcon();
        });
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders
                .of(this,
                        InjectorUtils.provideDetailViewModelFactory(
                                getApplication(),
                                movie.getId()
                        ))
                .get(DetailViewModel.class);
        setupObservers(viewModel);
    }

    private void setupObservers(DetailViewModel viewModel) {
        viewModel.isFavourite(movie.getId()).observe(this, isFavouriteData -> {
            if (isFavouriteData != null) {
                this.isFavourite = isFavouriteData;
                setupFavIcon();
            }
        });
        viewModel.getVideosLiveData().observe(this, movieVideos -> {
            if (movieVideos != null && movieVideos.videos != null) {
                Log.d(LOG_TAG,
                        "Received update for videos : " + movieVideos.videos.size()
                );
                if (movieVideos.videos.size() > 0 && movieVideos.id == movie.getId()) {
                    setupTrailers(movieVideos.videos);
                    StringBuilder builder = new StringBuilder();
                    for (MovieVideos.VideoResult video : movieVideos.videos) {
                        builder.append(video.getVideoUrl())
                                .append('\n');
                    }
                    shareDetails = builder.toString();
                }
            }
        });
        viewModel.getReviewsLiveData().observe(this, movieReviews -> {
            if (movieReviews != null && movieReviews.reviews != null) {
                Log.d(LOG_TAG,
                        "Received update for reviews : " + movieReviews.reviews.size()
                );
                if (movieReviews.reviews.size() > 0 && movieReviews.id == movie.getId()) {
                    setupReviews(movieReviews.reviews);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                launchShareIntent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBodyText = getString(
                R.string.share_detail_format,
                movie.getTitle(),
                movie.getVotes(),
                shareDetails
        );
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(shareIntent);
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
        addRadiusToPoster();
    }

    private void setupFavIcon() {
        if (isFavourite) {
            ivFavMovie.setImageDrawable(isFav);
        } else {
            ivFavMovie.setImageDrawable(isNotFav);
        }
    }

    private void setupImageViews() {
        Picasso.with(this)
                .load(ImageUrlUtils.getLargePosterUrl(movie.getPosterPath()))
                .noFade()
                .into(ivMoviePoster);

        Picasso.with(this)
                .load(ImageUrlUtils.getSmallBackdropUrl(movie.getBackdropPath()))
                .into(ivBackdrop);
    }

    private void addRadiusToPoster() {
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

    private void setupTrailers(List<MovieVideos.VideoResult> videos) {
        adapter = new MovieVideoAdapter(videos, this);
        tvTrailers.setVisibility(View.VISIBLE);
        rvTrailers.setVisibility(View.VISIBLE);
        rvTrailers.setAdapter(adapter);
    }

    private void setupReviews(List<MovieReviews.ReviewResult> reviews) {
        MovieReviewAdapter reviewAdapter = new MovieReviewAdapter(reviews);
        tvReviews.setVisibility(View.VISIBLE);
        rvReviews.setVisibility(View.VISIBLE);
        rvReviews.setAdapter(reviewAdapter);
    }

    @Override
    public void onClick(String videoUrl) {
        if (videoUrl != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(videoUrl));
            startActivity(intent);
        } else {
            Log.w(LOG_TAG, "Video url given was null");
        }
    }

    @Override
    protected void onDestroy() {
        removeExtraDetails();
        isNotFav = null;
        isFav = null;
        viewModel = null;
        adapter = null;
        super.onDestroy();
    }

    private void removeExtraDetails() {
        rvReviews.setVisibility(View.GONE);
        tvReviews.setVisibility(View.GONE);
        rvTrailers.setVisibility(View.GONE);
        tvTrailers.setVisibility(View.GONE);
    }
}
