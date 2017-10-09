package com.npkompleet.phenomenon.bakingapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.npkompleet.phenomenon.bakingapp.pojo.Ingredient;
import com.npkompleet.phenomenon.bakingapp.pojo.Recipe;
import com.npkompleet.phenomenon.bakingapp.pojo.Step;
import com.npkompleet.phenomenon.bakingapp.provider.RecipeContract;
import com.npkompleet.phenomenon.bakingapp.provider.RecipeProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;
import static com.npkompleet.phenomenon.bakingapp.RecipeListActivity.favoriteRecipe;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeStepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeStepActivity extends AppCompatActivity implements RecipeStepAdapter.StepClickHandler{

    @BindView(R.id.rv_recipe_step)
    RecyclerView mStepRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    Recipe recipe;

    public static final String RECIPE_PREF= "Recipe_Pref";
    public static final String FAVORITE_RECIPE= "Fav_Recipe";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (getSupportActionBar()!=null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (getIntent().hasExtra(getString(R.string.step_intent_key))){
            recipe= getIntent().getParcelableExtra(getString(R.string.step_intent_key));
        }

        RecipeStepAdapter recipeStepAdapter= new RecipeStepAdapter(this, this, recipe);

        assert mStepRecyclerView != null;
        mStepRecyclerView.setAdapter(recipeStepAdapter);


        // The detail container view will be present only in the
        // large-screen layouts (res/values-w900dp).
        // If this view is present, then the
        // activity should be in two-pane mode.
        mTwoPane = getResources().getBoolean(R.bool.two_pane);

        if (recipe.getName().equals(favoriteRecipe)){
            fab.setImageResource(R.drawable.ic_favorite_black_24dp);
        }



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentResolver resolver= getContentResolver();
                SharedPreferences sharedPreferences= getSharedPreferences(RECIPE_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();

                if (recipe.getName().equals(favoriteRecipe)){
                    fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    Toast.makeText(RecipeStepActivity.this, recipe.getName()+ " is no longer a favorite", Toast.LENGTH_SHORT).show();
                    String[] selectArgs= {recipe.getName()};
                    resolver.delete(RecipeProvider.Ingredients.CONTENT_URI, RecipeContract.COLUMN_RECIPE + "=?", selectArgs);
                    favoriteRecipe = "";

                    editor.putString(FAVORITE_RECIPE, favoriteRecipe);
                    editor.apply();

                    //refresh the widget
                    FavoriteRecipeWidget.sendRefreshBroadcast(RecipeStepActivity.this);

                } else {
                    if (!favoriteRecipe.equals("")) {
                        String[] selectArgs = {favoriteRecipe};
                        resolver.delete(RecipeProvider.Ingredients.CONTENT_URI, RecipeContract.COLUMN_RECIPE + "=?", selectArgs);
                        Toast.makeText(RecipeStepActivity.this, favoriteRecipe + " is no longer a favorite", Toast.LENGTH_SHORT).show();

                    }

                    for (Ingredient ingredient : recipe.getIngredients()) {
                        ContentValues cv = new ContentValues();
                        cv.put(RecipeContract.COLUMN_RECIPE, recipe.getName());
                        cv.put(RecipeContract.COLUMN_INGREDIENT, ingredient.getIngredientName());
                        cv.put(RecipeContract.COLUMN_QUANTITY, ingredient.getQuantity());
                        cv.put(RecipeContract.COLUMN_MEASURE, ingredient.getMeasure());

                        resolver.insert(RecipeProvider.Ingredients.CONTENT_URI, cv);
                    }

                    fab.setImageResource(R.drawable.ic_favorite_black_24dp);
                    favoriteRecipe= recipe.getName();
                    Toast.makeText(RecipeStepActivity.this, recipe.getName()+ " is new favorite", Toast.LENGTH_SHORT).show();

                    editor.putString(FAVORITE_RECIPE, favoriteRecipe);
                    editor.commit();

                    //refresh the widget
                    FavoriteRecipeWidget.sendRefreshBroadcast(RecipeStepActivity.this);
                }

            }
        });

    }



    @Override
    public void onClick(Step step, int index) {
        //Toast.makeText(this, "click, index: "+ index, Toast.LENGTH_SHORT).show();
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(RecipeStepVideoFragment.VIDEO_URL, step);
            RecipeStepVideoFragment fragment = new RecipeStepVideoFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_video_container, fragment)
                    .commit();


            Bundle instArguments = new Bundle();
            instArguments.putString(RecipeStepInstructionFragment.INSTRUCTION, step.getDescription());
            RecipeStepInstructionFragment iFragment = new RecipeStepInstructionFragment();
            iFragment.setArguments(instArguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_instruction_container, iFragment)
                    .commit();


        } else {
            Intent intent;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                intent = new Intent(this, RecipeStepDetailActivity.class);
            }else{
                intent = new Intent(this, FullScreenVideoActivity.class);
            }
            intent.putExtra(getString(R.string.step_detail_intent_key), step);

            intent.putParcelableArrayListExtra("steps", recipe.getSteps());

            intent.putExtra("index", index);

            startActivity(intent);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //navigateUpTo(new Intent(this, RecipeListActivity.class));
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
