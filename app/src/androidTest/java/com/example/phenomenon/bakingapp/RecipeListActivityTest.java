package com.example.phenomenon.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasType;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;


/**
 * Created by PHENOMENON on 6/9/2017.
 *
 */

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {


    @Rule
    public IntentsTestRule<RecipeListActivity> mActivityTestRule=
            new IntentsTestRule<>(RecipeListActivity.class);



    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void swipeToRefreshNoNetworkTest(){
        onView(withId(R.id.recipe_refresh))
                .perform(swipeDown());

        if (!networkActive()) {
            onView(withId(R.id.error_message))
                    .check(matches(isDisplayed()));
        }

    }


    @Test
    public void RecyclerViewClickTest() {
        if (networkActive()) {
            //swipeToRefreshTest();
            onView(withId(R.id.recipe_refresh))
                    .perform(swipeDown());

            onView(withId(R.id.error_message))
                    .check(matches(not(isDisplayed())));

            onView(withId(R.id.rv_recipe_list))
                    .perform(RecyclerViewActions.scrollToPosition(3), click());

            intended(allOf(
                    hasExtraWithKey(mActivityTestRule.getActivity().getString(R.string.step_intent_key)),
                    isInternal()
            ));

        }
    }

    @Test
    public void RecyclerViewShareClickTest(){
        if (networkActive()){
            //swipeToRefreshTest();
            onView(withId(R.id.recipe_refresh))
                    .perform(swipeDown());

            onView(withId(R.id.error_message))
                    .check(matches(not(isDisplayed())));


            onView(allOf(withId(R.id.recipe_list_share), hasSibling(withText(equalTo("Nutella Pie")))))
                    .perform(click());

            intended(allOf(
                    hasAction(Intent.ACTION_SEND),
                    hasExtraWithKey(Intent.EXTRA_TEXT),
                    hasType("text/plain"),
                    not(isInternal())
            ));

        }

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }




    public boolean networkActive() {
        ConnectivityManager cm = (ConnectivityManager) mActivityTestRule.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
