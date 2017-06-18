package com.example.phenomenon.bakingapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phenomenon.bakingapp.pojo.Ingredient;
import com.example.phenomenon.bakingapp.pojo.Recipe;
import com.example.phenomenon.bakingapp.pojo.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PHENOMENON on 5/28/2017.
 */

public class RecipeStepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_INGREDIENT= 0;
    private static final int TYPE_STEP= 1;
    private Context context;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;
    private StepClickHandler stepClickHandler;
    private float scale=0;

    public RecipeStepAdapter(Context context, StepClickHandler clickHandler, Recipe recipe)
    {
        this.context= context;
        ingredients= recipe.getIngredients();
        steps= recipe.getSteps();
        stepClickHandler= clickHandler;
        scale= context.getResources().getDisplayMetrics().density;
    }

    interface StepClickHandler
    {
        public void onClick(Step step, int index);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType)
        {
            case TYPE_INGREDIENT:
                View item = LayoutInflater.from(context).inflate(R.layout.recipe_ingredient_content, parent, false);
                return new RecipeIngredientHolder(item);

            default:
                View newItem = LayoutInflater.from(context).inflate(R.layout.recipe_step_content, parent, false);
                return new RecipeStepHolder(newItem);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType())
        {
            case TYPE_INGREDIENT:
                RecipeIngredientHolder iHolder= (RecipeIngredientHolder) holder;
                //iHolder.gridLayout.setElevation();
                iHolder.gridLayout.setRowCount(ingredients.size());
                for (Ingredient ingredient: ingredients){
                    TextView ingredientTV= new TextView(context);
                    ingredientTV.setText(ingredient.getIngredientName());
                    ingredientTV.setTypeface(Typeface.DEFAULT_BOLD);
                    ingredientTV.setTextColor(Color.BLACK);
                    GridLayout.LayoutParams param= new GridLayout.LayoutParams();
                    param.height= GridLayout.LayoutParams.WRAP_CONTENT;

                    //param.width= GridLayout.LayoutParams.WRAP_CONTENT;
                    param.width= (int) (150 * scale + 0.5f);
                    param.rightMargin= 5;
                    ingredientTV.setLayoutParams(param);
                    TextView quantityTV= new TextView(context);
                    quantityTV.setText(String.valueOf(ingredient.getQuantity() + ingredient.getMeasure()));
                    quantityTV.setTextColor(Color.BLACK);


                    iHolder.gridLayout.addView(ingredientTV);
                    iHolder.gridLayout.addView(quantityTV);
                    //iHolder.gridLayout.addView(measureTV);

                }
                break;

            default:
                //for the case of step, reduce the position number to
                //account for the addition of the ingredient holder
                //convert the ViewHolder to a StepViewHolder then set the text
                position--;
                RecipeStepHolder stepHolder= (RecipeStepHolder) holder;
                Step step= steps.get(position);
                String url= step.getThumbnailURL();
                if (!url.equals("")){
                    Picasso.with(context).load(url).into(stepHolder.imageView);
                }
                stepHolder.step.setText(step.getShortDescription());
                break;
        }

    }

    @Override
    public int getItemCount() {
        return steps.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position==0? TYPE_INGREDIENT : TYPE_STEP;
    }

    public class RecipeStepHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.id)
        TextView step;

        @BindView(R.id.step_image)
        ImageView imageView;

        public RecipeStepHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            stepClickHandler.onClick(steps.get(getLayoutPosition()-1), getLayoutPosition()-1);
        }
    }

    private class RecipeIngredientHolder extends RecyclerView.ViewHolder{
        GridLayout gridLayout;

        public RecipeIngredientHolder(View itemView)
        {
            super(itemView);
            gridLayout= (GridLayout) itemView.findViewById(R.id.ingredient_grid);
        }
    }
}
