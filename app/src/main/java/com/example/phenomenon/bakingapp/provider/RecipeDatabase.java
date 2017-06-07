package com.example.phenomenon.bakingapp.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by PHENOMENON on 6/4/2017.
 */


@Database(version= RecipeDatabase.VERSION)
public class RecipeDatabase {

    public static final int VERSION=1;

    @Table(RecipeContract.class)
    public static final String RECIPE_TABLE= "ingredients";
}
