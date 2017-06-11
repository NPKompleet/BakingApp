package com.example.phenomenon.bakingapp.IdlingResource;


import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import com.example.phenomenon.bakingapp.RecipeListActivity;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by PHENOMENON on 6/11/2017.
 *
 */

public class RecipeIdlingResource implements IdlingResource {

    @Nullable
    private volatile ResourceCallback mCallback;
    private AtomicBoolean mIsIdleNow= new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback=callback;
    }

    /**
     * Sets the new idle state, if isIdleNow is true, it pings the {@link ResourceCallback}.
     * @param isIdleNow false if there are pending operations, true if idle.
     */
    public void setIdleState(boolean isIdleNow) {
        mIsIdleNow.set(isIdleNow);
        if (isIdleNow && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }
}
