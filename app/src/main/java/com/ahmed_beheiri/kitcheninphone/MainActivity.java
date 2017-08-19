package com.ahmed_beheiri.kitcheninphone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ahmed_beheiri.kitcheninphone.database.RecipeContract;
import com.ahmed_beheiri.kitcheninphone.database.RecipesPrefernces;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.OnItemClickListener, LoaderManager.LoaderCallbacks<ArrayList<Recipes>> {
    @BindView(R.id.recyclerview_recipes)
    RecyclerView reciperecycleview;
    @BindView(R.id.tv_error_message_display)
    TextView ErrorMessagetext;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar pb;
    @BindView(R.id.toolbar_actionbar)
    Toolbar toolbar;
    @BindView(R.id.adView)
    AdView mAdView;
    private ArrayList<Recipes> recipeslist;
    private RecipesAdapter recipesAdapter;
    Bundle bundle = new Bundle();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String Sorting;
    private static final int ID_RECIPE_LOADER = 15;
    private RecycleViewScrollListener scrollListener;
    private int page ;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar(getString(R.string.app_name));

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        recipeslist = new ArrayList<>();

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reciperecycleview.setLayoutManager(linearLayoutManager);
        reciperecycleview.setHasFixedSize(true);
        recipesAdapter = new RecipesAdapter(recipeslist, this, this);
        reciperecycleview.setAdapter(recipesAdapter);
        scrollListener=new RecycleViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new Database needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                if (!(RecipesPrefernces.getPreferredSorting(MainActivity.this).equals("favorites")))
                    loadData();
            }
        };

        reciperecycleview.addOnScrollListener(scrollListener);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        Sorting = this.getString(R.string.pref_sorting);
        page=0;
        loadData();

    }

    private void loadData() {
        bundle.putInt("page", ++page);

        if (getSupportLoaderManager().getLoader(ID_RECIPE_LOADER) == null) {
            getSupportLoaderManager().initLoader(ID_RECIPE_LOADER, bundle, this);
        } else {
            getSupportLoaderManager().restartLoader(ID_RECIPE_LOADER, bundle, this);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent i = new Intent(this, DetailesActivity.class);
        i.putExtra("recipe", recipeslist.get(position));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition((ViewGroup) view);
        }
        startActivityForResult(i, 1);


    }

    private void showRecipeData() {
        ErrorMessagetext.setVisibility(View.INVISIBLE);
        reciperecycleview.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        reciperecycleview.setVisibility(View.INVISIBLE);
        ErrorMessagetext.setVisibility(View.VISIBLE);
    }

    private void setupToolbar(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public Loader<ArrayList<Recipes>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Recipes>>(this) {

            ArrayList<Recipes> recipes;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                pb.setVisibility(View.VISIBLE);
                if (recipes != null) {
                    deliverResult(recipes);
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<Recipes> loadInBackground() {
                Integer page = args.getInt("page");

                String sorting = RecipesPrefernces
                        .getPreferredSorting(MainActivity.this);

                if (!(sorting.equals("favorites"))) {

                    URL reciperequesturl = Network.urlBuilder(page);
                    try {
                        String jsonMovieResponse = Network
                                .getResponseFromHttpUrl(reciperequesturl);

                        return Utils.getRecipesFromJSON(MainActivity.this, jsonMovieResponse);


                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }

                } else {
                    Cursor cursor = getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI, null, null, null, null);
                    ArrayList<Recipes> arrayList = new ArrayList<>();
                    if (cursor != null) {
                        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                            arrayList.add(new Recipes(cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.ID)),
                                    cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_TITLE)),
                                    cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_PUBLISHER)),
                                    cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_IMAGE)),
                                    cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_SOURCEURL)),
                                    cursor.getDouble(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_USER_RATING)),
                                    cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGREDIENTS))));
                        }
                        return arrayList;
                    }
                    return null;
                }
            }

            @Override
            public void deliverResult(ArrayList<Recipes> data) {
                this.recipes = data;
                pb.setVisibility(View.INVISIBLE);
                super.deliverResult(data);

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Recipes>> loader, ArrayList<Recipes> data) {
        pb.setVisibility(View.INVISIBLE);
        if (null == data) {
            showErrorMessage();
        } else {
            showRecipeData();
            recipeslist.addAll(data);
            recipesAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipes>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.menuSortnew):
                editor.putString(Sorting, "new");
                editor.commit();

                recipeslist.clear();
                recipesAdapter.notifyDataSetChanged();
                loadData();
                return true;
            case (R.id.menuSortFavorites):
                editor.putString(Sorting, "favorites");
                editor.commit();
                recipeslist.clear();
                recipesAdapter.notifyDataSetChanged();
                loadData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().initLoader(ID_RECIPE_LOADER, bundle, this);
    }
}
