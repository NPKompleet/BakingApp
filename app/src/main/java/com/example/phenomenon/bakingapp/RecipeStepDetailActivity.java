package com.example.phenomenon.bakingapp;


import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import com.example.phenomenon.bakingapp.pojo.Step;

import java.util.ArrayList;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

public class RecipeStepDetailActivity extends AppCompatActivity implements RecipeStepNavFragment.OnNavButtonClickListener {
    Step step;
    ArrayList<Step> steps;
    static int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        step= getIntent().getParcelableExtra(getString(R.string.step_detail_intent_key));
        steps= getIntent().getParcelableArrayListExtra("steps");
        index= getIntent().getIntExtra("index", 0);

        //Step step= steps.get(index);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(this, FullScreenVideoActivity.class);
            intent.putExtra(getString(R.string.step_detail_intent_key), step);
            intent.putParcelableArrayListExtra("steps", steps);
            intent.putExtra("index", index);
            startActivity(intent);
            onDestroy();
        }




        // Create the detail fragment and add it to the activity
        // using a fragment transaction.
        Bundle arguments = new Bundle();
        arguments.putParcelable(RecipeStepVideoFragment.VIDEO_URL,
                getIntent().getParcelableExtra(getString(R.string.step_detail_intent_key)));
        RecipeStepVideoFragment fragment = new RecipeStepVideoFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.step_detail_video_container, fragment)
                .commit();


        Bundle instArguments = new Bundle();
        instArguments.putString(RecipeStepInstructionFragment.INSTRUCTION, step.getDescription());
        RecipeStepInstructionFragment iFragment = new RecipeStepInstructionFragment();
        iFragment.setArguments(instArguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.step_detail_instruction_container, iFragment)
                .commit();

        Bundle navArguments = new Bundle();
        navArguments.putInt(RecipeStepNavFragment.INDEX_PARAM, index);
        navArguments.putInt(RecipeStepNavFragment.MAX_INDEX_PARAM, steps.size()-1);
        RecipeStepNavFragment navFragment = new RecipeStepNavFragment();
        navFragment.setArguments(navArguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.nav_fragment, navFragment)
                .commit();



    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //navigateUpTo(new Intent(this, RecipeStepActivity.class));
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onNavClicked(View view) {
        switch (view.getId()) {
            case R.id.left_nav:
                index--;
                navigateTo(index);
                break;

            case R.id.right_nav:
                index++;
                navigateTo(index);
                break;
        }
    }

    public void navigateTo(int index){
        Step step= steps.get(index);
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
