package com.example.phenomenon.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;

import com.example.phenomenon.bakingapp.pojo.Recipe;
import com.example.phenomenon.bakingapp.utils.NetworkUtil;

import java.util.ArrayList;

/**
 * Created by PHENOMENON on 5/26/2017.
 */

public class RecipeListLoader extends AsyncTaskLoader<ArrayList<Recipe>> {

    public RecipeListLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Recipe> loadInBackground() {
        return NetworkUtil.fetchData();
    }
}


