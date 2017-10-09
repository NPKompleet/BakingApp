package com.npkompleet.phenomenon.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.npkompleet.phenomenon.bakingapp.pojo.Step;

import java.util.ArrayList;

public class FullScreenVideoActivity extends AppCompatActivity {

    //public static String FSCR_VIDEO_URL= "url";
    //public static String FSCR_POSITION= "position";

    private Step step;
    ArrayList<Step> steps;
    private int index;
    //private long position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);



        step= getIntent().getParcelableExtra(getString(R.string.step_detail_intent_key));
        //get the index and steps list in case we switch to portrait orientation
        steps= getIntent().getParcelableArrayListExtra("steps");
        index= getIntent().getIntExtra("index", 0);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.putExtra(getString(R.string.step_detail_intent_key), step);
            intent.putParcelableArrayListExtra("steps", steps);
            intent.putExtra("index", index);
            startActivity(intent);
            onDestroy();
        }

        int uiOpt= View.SYSTEM_UI_FLAG_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(uiOpt);

        ActionBar actionBar= getSupportActionBar();
        if (actionBar!=null) actionBar.hide();

        Bundle arguments = new Bundle();
        arguments.putParcelable(RecipeStepVideoFragment.VIDEO_URL, step);
        RecipeStepVideoFragment fragment = new RecipeStepVideoFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_full_screen_video, fragment)
                .commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
