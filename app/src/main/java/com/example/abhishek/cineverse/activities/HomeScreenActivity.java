package com.example.abhishek.cineverse.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.transition.ChangeBounds;
import android.support.transition.ChangeTransform;
import android.support.transition.TransitionSet;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.support.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishek.cineverse.R;
import com.example.abhishek.cineverse.adapters.MovieAdapter;
import com.example.abhishek.cineverse.fragments.DetailFragment;
import com.example.abhishek.cineverse.fragments.SortDialogFragment;
import com.example.abhishek.cineverse.models.MovieViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeScreenActivity extends AppCompatActivity implements SortDialogFragment.SortDialogListener, MovieAdapter.MovieAdapterOnClickHandler {

    private static final long MOVE_DEFAULT_TIME = 1000;
    private static final long FADE_DEFAULT_TIME = 300;
    @BindView(R.id.rv_movies_list)
    RecyclerView rvMoviesList;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    @BindView(R.id.app_bar)
    AppBarLayout app_bar;

    private MovieViewModel movieViewModel;
    private SortDialogFragment sortDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Typeface font = Typeface.createFromAsset(getAssets(), "pacifico_regular.ttf");
        ((TextView) toolbar.getChildAt(0)).setTypeface(font);

        int orientation = getResources().getConfiguration().orientation;
        GridLayoutManager layoutManager =
                new GridLayoutManager(this, orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 4);
        MovieAdapter adapter = new MovieAdapter(this, this);
        rvMoviesList.setAdapter(adapter);
        rvMoviesList.setLayoutManager(layoutManager);

        movieViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MovieViewModel.class);

        movieViewModel.getMoviesLiveData(this).observe(this, movies -> {
            Log.v("Debug App", "Fetched Data: " + movies.toString());
            adapter.setMovieData(movies);
        });

        sortDialog = new SortDialogFragment();
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
                // TODO: 13/6/18 Change TAG and add more properties if link{needed https://developer.android.com/guide/topics/ui/dialogs}
                sortDialog.show(getSupportFragmentManager(), "TAG");
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
    public void onClick(int index, ImageView v) {
        DetailFragment fragment = DetailFragment.newInstance(
                movieViewModel
                        .getMoviesLiveData(this)
                        .getValue()
                        .get(index)
        );

        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();

        TransitionSet set = new TransitionSet();
        set.addTransition(new ChangeTransform());
        set.addTransition(new ChangeBounds());
        set.setDuration(1000*MOVE_DEFAULT_TIME);
        fragment.setSharedElementEnterTransition(set);
//
//
//        // 3. Enter Transition for New Fragment
//        Fade enterFade = new Fade();
//        enterFade.setStartDelay(MOVE_DEFAULT_TIME);
//        enterFade.setDuration(FADE_DEFAULT_TIME);
//        fragment.setEnterTransition(enterFade);


        fragmentManager.addToBackStack("TAG");
        fragmentManager.replace(R.id.fragment_container, fragment, "TAG");
        fragmentManager.addSharedElement(v, movieViewModel.getMoviesLiveData(this).getValue().get(index).getTitle());
        fragmentManager.commitAllowingStateLoss();
    }
}
