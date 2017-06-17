package com.example.phenomenon.bakingapp;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.phenomenon.bakingapp.pojo.Ingredient;
import com.example.phenomenon.bakingapp.pojo.Recipe;
import com.example.phenomenon.bakingapp.pojo.Step;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by PHENOMENON on 6/11/2017.
 *
 */

@RunWith(AndroidJUnit4.class)
public class RecipeStepActivityTest {
    private  ArrayList<Step> steps= new ArrayList<>();

    private ArrayList<Ingredient> ingredients= new ArrayList<>();

    private Step step1= new Step(1, "test 1", "Testing step 1", "", "");
    private Step step2= new Step(2, "test 2", "Testing step 2", "", "");

    private Ingredient ing1= new Ingredient(1, "CUP", "ingredient 1");
    private Ingredient ing2= new Ingredient(2, "TSP", "ingredient 2");




    @Rule
    public IntentsTestRule<RecipeStepActivity> activityTestRule=
            new IntentsTestRule<>(RecipeStepActivity.class, false, false);

    @Test
    public void someTest(){
        steps.add(step1);
        steps.add(step2);

        ingredients.add(ing1);
        ingredients.add(ing2);

        Recipe testRecipe = new Recipe(1, "Test Recipe", ingredients, steps, 2, "");

        Intent intent = new Intent();

        intent.putExtra("step_intent", testRecipe);
        activityTestRule.launchActivity(intent);
        activityTestRule.getActivity();

        boolean mTwoPane= activityTestRule.getActivity().getResources().getBoolean(R.bool.two_pane);


        if (mTwoPane){
            onView(withId(R.id.rv_recipe_step))
                    .perform(RecyclerViewActions.scrollToPosition(1), click());

            onView(withId(R.id.recipe_step_instruction_detail))
                    .check(matches(withText("Testing step 2")));

        }else{
            onView(withId(R.id.rv_recipe_step))
                    .perform(RecyclerViewActions.scrollToPosition(1), click());

            /*intended(allOf(
                    hasExtraWithKey(activityTestRule.getActivity().getString(R.string.step_detail_intent_key)),
                    hasExtraWithKey("steps"),
                    hasExtraWithKey("index"),
                    isInternal()
            ));*/
        }
    }
}
