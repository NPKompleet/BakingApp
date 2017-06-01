package com.example.phenomenon.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.phenomenon.bakingapp.pojo.Recipe;
import com.example.phenomenon.bakingapp.utils.NetworkBroadcastReceiver;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>>, RecipeListAdapter.RecipeClickHandler{
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.rv_recipe_list)
    RecyclerView mRecipeRecyclerView;

    NetworkBroadcastReceiver mReceiver= null;

    static int RECIPE_LIST_LOADER = 2;
    RecipeListAdapter mAdapter;
    ArrayList<Recipe> recipes= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        if(getResources().getBoolean(R.bool.two_pane)){
            mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }else {
            mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        }
        mAdapter= new RecipeListAdapter(this, this, recipes);
        mRecipeRecyclerView.setAdapter(mAdapter);

        if (networkActive()) {
            getSupportLoaderManager().initLoader(RECIPE_LIST_LOADER, null, this).forceLoad();
        }else{
            Toast.makeText(this, getString(R.string.toast_no_network), Toast.LENGTH_SHORT).show();
            mReceiver= new NetworkBroadcastReceiver();
            this.registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

    }

    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
        return new RecipeListLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
        if (data==null) {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "load finished", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, data.get(0).getName(), Toast.LENGTH_SHORT).show();
        mAdapter.swapData(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {

    }

    public boolean networkActive() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onClickRecipe(Recipe recipe) {
        Toast.makeText(this, recipe.getName()+ " clicked", Toast.LENGTH_SHORT).show();
        Intent recipeStepIntent= new Intent(this, RecipeStepActivity.class);
        recipeStepIntent.putExtra(getString(R.string.step_intent_key), recipe);
        startActivity(recipeStepIntent);
    }
}
