package com.example.phenomenon.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.example.phenomenon.bakingapp.provider.RecipeContract;
import com.example.phenomenon.bakingapp.provider.RecipeProvider;

import java.util.HashMap;

/**
 * Created by PHENOMENON on 6/6/2017.
 */

public class FavoriteRecipeRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory{
    private Context mContext;
    private Cursor mCursor;

    public FavoriteRecipeRemoteViewFactory(Context context, Intent intent){
        mContext = context;
    }


    public void initCursor(){
        if (mCursor != null) {
            mCursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();
        /**This is done because the widget runs as a separate thread
         when compared to the current app and hence the app's data won't be accessible to it
         because I'm using a content provided **/

        //fetch data from database ordered by the id column
        mCursor = mContext.getContentResolver().query(RecipeProvider.Ingredients.CONTENT_URI,
                new String[]{RecipeContract.COLUMN_INGREDIENT, RecipeContract.COLUMN_QUANTITY,
                        RecipeContract.COLUMN_MEASURE}, null, null, RecipeContract._ID);
        Binder.restoreCallingIdentity(identityToken);

    }

    @Override
    public void onCreate() {
        initCursor();
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

    }

    @Override
    public void onDataSetChanged() {
        initCursor();
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor==null ? 0 : mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

        rv.setTextViewText(R.id.widget_ingredient, mCursor.getString(0));
        rv.setTextViewText(R.id.widget_quantity, String.valueOf(mCursor.getInt(1)));

        rv.setInt(R.id.widget_measure, "setImageResource", getImage(mCursor.getString(2)));



        /*Intent fillInIntent= new Intent();
        fillInIntent.putExtra(mContext.getString(R.string.symbol_intent_key),  mCursor.getString(0));
        rv.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);*/

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public static int getImage(String measure){
        HashMap<String, Integer> ImageMap= new HashMap<>();
        ImageMap.put("CUP", R.drawable.cup);
        ImageMap.put("TBLSP", R.drawable.tblsp);
        ImageMap.put("TSP", R.drawable.tsp);
        ImageMap.put("K", R.drawable.k);
        ImageMap.put("UNIT", R.drawable.unit);
        ImageMap.put("OZ", R.drawable.oz);
        ImageMap.put("G", R.drawable.g);
        ImageMap.put("OTHER", R.drawable.measure);

        if (!ImageMap.containsKey(measure)) return ImageMap.get("OTHER");
        return ImageMap.get(measure);
    }
}
