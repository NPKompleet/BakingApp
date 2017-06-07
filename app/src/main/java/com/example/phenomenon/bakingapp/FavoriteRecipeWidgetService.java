package com.example.phenomenon.bakingapp;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by PHENOMENON on 6/6/2017.
 */

public class FavoriteRecipeWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FavoriteRecipeRemoteViewFactory(this.getApplicationContext(), intent);
    }
}
