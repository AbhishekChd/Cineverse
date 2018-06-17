package com.example.abhishek.cineverse.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.abhishek.cineverse.R;
import com.example.abhishek.cineverse.data.UrlContract;
import com.example.abhishek.cineverse.helpers.DateUtils;
import com.example.abhishek.cineverse.models.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    private static final String ARG_PARAM = "movie";
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
    @BindView(R.id.toolbar_detail)
    Toolbar toolbar;
    private Movie movie;


    public DetailFragment() {
    }

    /**
     * @param movie Movie Passed to show details
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(Movie movie) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        postponeEnterTransition();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
//        }
        if (getArguments() != null) {
            movie = getArguments().getParcelable(ARG_PARAM);
        }

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_detail, container, false);
        ButterKnife.bind(this, v);

        toolbar.setNavigationOnClickListener(view -> {
            getActivity().onBackPressed();
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivMoviePoster.setTransitionName(movie.getTitle());
        }
        Picasso.with(getContext())
                .load(UrlContract.BASE_POSTER_LARGE_URL + movie.getPosterPath())
                .noFade()
                .into(ivMoviePoster);
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


        Picasso.with(getContext())
                .load(UrlContract.BASE_BACKDROP_SMALL_URL + movie.getBackdropPath())
                .into(ivBackdrop);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
