package com.example.phenomenon.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import static com.example.phenomenon.bakingapp.RecipeListActivity.favoriteRecipe;
import static com.example.phenomenon.bakingapp.RecipeStepActivity.FAVORITE_RECIPE;
import static com.example.phenomenon.bakingapp.RecipeStepActivity.RECIPE_PREF;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteRecipeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_recipe_widget);

        //retrieve favorite from Shared Preferences
        SharedPreferences sharedPref = context.getSharedPreferences(RECIPE_PREF, Context.MODE_PRIVATE);

        //set the name of the recipe in the widget to the favorite recipe in The main activity
        views.setTextViewText(R.id.widget_title, sharedPref.getString(FAVORITE_RECIPE, ""));
        views.setRemoteAdapter(R.id.listview, new Intent(context, FavoriteRecipeWidgetService.class));
        views.setEmptyView(R.id.listview, R.id.appwidget_text);

        //creating an intent
        Intent intent= new Intent(context, RecipeListActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(context, 0, intent, 0);

        //set
        views.setOnClickPendingIntent(R.id.widget_title_bar, pendingIntent);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        //int[] appWidgetIds= appWidgetManager.getAppWidgetIds(new)

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listview);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, FavoriteRecipeWidget.class));
        context.sendBroadcast(intent);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, FavoriteRecipeWidget.class);
            //mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.listview);
            int[] appWidgetIds= mgr.getAppWidgetIds(cn);
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, mgr, appWidgetId);
            }
        }

        super.onReceive(context, intent);
    }

}

