package com.example.phenomenon.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phenomenon.bakingapp.IdlingResource.RecipeIdlingResource;
import com.example.phenomenon.bakingapp.pojo.Recipe;
import com.example.phenomenon.bakingapp.provider.RecipeContract;
import com.example.phenomenon.bakingapp.provider.RecipeProvider;
import com.example.phenomenon.bakingapp.utils.NetworkBroadcastReceiver;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.phenomenon.bakingapp.FavoriteRecipeWidget.sendRefreshBroadcast;
import static com.example.phenomenon.bakingapp.RecipeStepActivity.FAVORITE_RECIPE;
import static com.example.phenomenon.bakingapp.RecipeStepActivity.RECIPE_PREF;

public class RecipeListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>>, RecipeListAdapter.RecipeClickHandler,
        NetworkBroadcastReceiver.ConnectivityListener, SwipeRefreshLayout.OnRefreshListener{
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.rv_recipe_list)
    RecyclerView mRecipeRecyclerView;

    @BindView(R.id.error_message)
    TextView errorTV;

    @BindView(R.id.recipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    NetworkBroadcastReceiver mReceiver= null;

    static int RECIPE_LIST_LOADER = 2;
    RecipeListAdapter mAdapter;
    ArrayList<Recipe> recipes= new ArrayList<>();
    private boolean receiver_registered= false;
    private boolean stateSaved= false;

    public static String favoriteRecipe= "";


    @Nullable
    private RecipeIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link RecipeIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new RecipeIdlingResource();
        }
        return mIdlingResource;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        if(getResources().getBoolean(R.bool.two_pane)){
            mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }else {
            mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        }
        mAdapter= new RecipeListAdapter(this, this, recipes);
        mRecipeRecyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        if (savedInstanceState!= null){
            stateSaved= true;
            recipes= savedInstanceState.getParcelableArrayList("list");
            mAdapter.swapData(recipes);}
       /* }else {
            getFavoriteRecipe();
            onRefresh();
        }*/
        getIdlingResource();

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!stateSaved){
            getFavoriteRecipe();
            onRefresh();
        }
    }

    private void getFavoriteRecipe(){
       /* String[] projection= {RecipeContract.COLUMN_RECIPE};
        Cursor c=  getContentResolver().query(RecipeProvider.Ingredients.CONTENT_URI, projection, null, null, null);
        if (c != null && c.moveToFirst()) {
            favoriteRecipe = c.getString(c.getColumnIndex(RecipeContract.COLUMN_RECIPE));
            c.close();
        }*/

        SharedPreferences sharedPref = getSharedPreferences(RECIPE_PREF, Context.MODE_PRIVATE);
        favoriteRecipe = sharedPref.getString(FAVORITE_RECIPE, "");

        Toast.makeText(this, "fav: "+favoriteRecipe, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList("list", recipes);
    }


    @Override
    /**
     * Same method implemented by two interfaces and so can be called by either
     * @link NetworkBroadcastReceiver.ConnectivityListener : The first interface
     * SwipeRefreshLayout: second interface
     */
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        if (networkActive()) {
            if (mIdlingResource != null) {
                mIdlingResource.setIdleState(false);
            }
            initLoader();
            errorTV.setText(getString(R.string.toast_connecting));
        }else{
            Toast.makeText(this, getString(R.string.toast_no_network), Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            errorTV.setVisibility(View.VISIBLE);
            mReceiver= new NetworkBroadcastReceiver(this);
            this.registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            receiver_registered=true;
        }

    }

    public boolean networkActive() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void initLoader(){
        getSupportLoaderManager().initLoader(RECIPE_LIST_LOADER, null, this).forceLoad();
    }




    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
        return new RecipeListLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
        if (data==null) {
            Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, getString(R.string.load_finished), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, data.get(0).getName(), Toast.LENGTH_SHORT).show();
        mAdapter.swapData(data);
        swipeRefreshLayout.setRefreshing(false);
        errorTV.setVisibility(View.GONE);
        if (receiver_registered) this.unregisterReceiver(mReceiver);
        receiver_registered=false;

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {

    }


    @Override
    public void onClickRecipe(Recipe recipe) {
        Toast.makeText(this, recipe.getName()+ getString(R.string.recipe_selected), Toast.LENGTH_SHORT).show();
        Intent recipeStepIntent= new Intent(this, RecipeStepActivity.class);
        recipeStepIntent.putExtra(getString(R.string.step_intent_key), recipe);
        startActivity(recipeStepIntent);
    }

    @Override
    protected void onDestroy() {
        if (receiver_registered) this.unregisterReceiver(mReceiver);
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }



    //added a refresh option in the menu for a11y reasons
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh) {
            onRefresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
