package com.ahmed_beheiri.kitcheninphone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Recipes> {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imagerecipe)
    ImageView imagerecipe;
    @BindView(R.id.recipeimageview)
    ImageView recimeimageviewtoolbar;
    @BindView(R.id.publishername)
    TextView publishername;
    @BindView(R.id.rate)
    TextView rate;
    @BindView(R.id.showmore)
    Button showmorebutton;
    @BindView(R.id.ingredintetext)
    TextView ingredintetext;
    @BindView(R.id.linearlayout)
    LinearLayout linearLayout;
    @BindView(R.id.favouritefab)
    FloatingActionButton favouritefab;
    private String id;
    private ArrayList<String> ingredinates;
    private String sourceurl;
    private Recipes recipe;
    public static final int LOADER_ID = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailes);
        ButterKnife.bind(this);
        ingredinates = new ArrayList<>();

        Intent i = getIntent();

        if (i != null) {
            recipe = i.getParcelableExtra("recipe");
            setupToolbar(recipe.getTitle());
            Picasso.with(this).load(recipe.getImageurl()).into(imagerecipe);
            Picasso.with(this).load(recipe.getImageurl()).into(recimeimageviewtoolbar);
            publishername.setText(recipe.getPublisher());
            rate.setText(String.valueOf(recipe.getRate()));

            id = recipe.getID();


            ingredinates = new ArrayList<>();


            if (recipe.isFavourited(this)) {
                favouritefab.setImageResource(R.drawable.ic_heart);
            } else {
                favouritefab.setImageResource(R.drawable.ic_heart_border);
            }


        }


        getSupportLoaderManager().initLoader(LOADER_ID, savedInstanceState, this);

    }


    private void setupToolbar(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
    }


    @Override
    public Loader<Recipes> onCreateLoader(int ID, Bundle args) {
        return new AsyncTaskLoader<Recipes>(this) {
            Recipes recipes;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (recipes != null) {
                    deliverResult(recipes);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Recipes loadInBackground() {
                URL url = Network.getUrlBuilder(id);
                try {
                    String result = Network.getResponseFromHttpUrl(url);
                    Recipes ing = Utils.getIngredients(DetailesActivity.this, result);
                    return ing;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(Recipes data) {
                this.recipes = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Recipes> loader, Recipes data) {
        if (data == null) {
            showErrorMessage();
        } else {
            ArrayList<String> ingre = Utils.convertStringToArray(data.getIngredients());
            for (int i = 0; i < ingre.size(); i++) {
                ingredintetext.append(ingre.get(i) + "\n\n");
            }
            sourceurl = data.getSourceurl();
            recipe.setSourceurl(sourceurl);
            recipe.setIngredients(data.getIngredients());


            showmorebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailesActivity.this, RecipeWay.class);
                    i.putExtra("sourceurl", recipe.getSourceurl());
                    startActivity(i);
                }
            });

            favouritefab.setOnClickListener(new View.OnClickListener() {
                Context context = getApplicationContext();

                @Override
                public void onClick(View v) {
                    if (!recipe.isFavourited(context)) {
                        if (recipe.saveToFavourits(context)) {
                            favouritefab.setImageResource(R.drawable.ic_heart);
                        }
                    } else {
                        if (recipe.removeFromFavourite(context)) {
                            favouritefab.setImageResource(R.drawable.ic_heart_border);
                        }
                    }

                }
            });


        }
    }

    @Override
    public void onLoaderReset(Loader<Recipes> loader) {

    }

    private void showErrorMessage() {
        ingredintetext.setText(getString(R.string.Error_ing_text));

    }
}
