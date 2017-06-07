package com.example.phenomenon.bakingapp.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by PHENOMENON on 6/4/2017.
 */


@ContentProvider(
        authority = RecipeProvider.AUTHORITY,
        database = RecipeDatabase.class)
public class RecipeProvider {

    public static final String AUTHORITY= "com.example.phenomenon.bakingapp.provider.authority";

    @TableEndpoint(table = RecipeDatabase.RECIPE_TABLE)
    public static class Ingredients{

        @ContentUri(
                path = "ingredients",
                type = "vnd.android.cursor.dir/ingredients",
                defaultSort = RecipeContract._ID)
        public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/ingredients");
    }
}
