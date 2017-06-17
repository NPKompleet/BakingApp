package com.example.phenomenon.bakingapp.provider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by PHENOMENON on 6/4/2017.
 */

public class RecipeContract {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    public  static final String _ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_RECIPE= "recipe";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_INGREDIENT= "ingredient";

    @DataType(DataType.Type.REAL)
    @NotNull
    public static final String COLUMN_QUANTITY= "quantity";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_MEASURE= "measure";
}
