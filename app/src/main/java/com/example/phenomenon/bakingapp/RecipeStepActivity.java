package com.example.phenomenon.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


import com.example.phenomenon.bakingapp.pojo.Recipe;
import com.example.phenomenon.bakingapp.pojo.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    Recipe recipe;

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

    }



    @Override
    public void onClick(Step step, int index) {
        Toast.makeText(this, "click, index: "+ index, Toast.LENGTH_SHORT).show();
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(RecipeStepVideoFragment.ARG_ITEM_ID, String.valueOf(step.getId()));
            RecipeStepVideoFragment fragment = new RecipeStepVideoFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_video_container, fragment)
                    .commit();


            Bundle instArguments = new Bundle();
            instArguments.putString(RecipeStepInstructionFragment.ARG_PARAM, step.getDescription());
            RecipeStepInstructionFragment iFragment = new RecipeStepInstructionFragment();
            iFragment.setArguments(instArguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_instruction_container, iFragment)
                    .commit();


        } else {
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.putExtra(RecipeStepVideoFragment.ARG_ITEM_ID, String.valueOf(step.getId()));
            //Bundle bundle= new Bundle();
            //bundle.putParcelableArrayList("steps", recipe.getSteps());
            intent.putParcelableArrayListExtra("steps", recipe.getSteps());
            //bundle.putInt("index", index);
            intent.putExtra("index", index);
            //intent.putExtra("bundle", bundle);

            startActivity(intent);
        }
    }


}
