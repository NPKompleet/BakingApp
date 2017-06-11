package com.example.phenomenon.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.phenomenon.bakingapp.pojo.Ingredient;
import com.example.phenomenon.bakingapp.pojo.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.phenomenon.bakingapp.RecipeListActivity.favoriteRecipe;

/**
 * Created by PHENOMENON on 5/26/2017.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {
    private Context context;
    private ArrayList<Recipe> myRecipes;
    private RecipeClickHandler clickHandler;

    public RecipeListAdapter(Context context, RecipeClickHandler clickHandler, ArrayList<Recipe> recipes){
        this.context= context;
        this.clickHandler=clickHandler;
        myRecipes= recipes;

    }

    public interface RecipeClickHandler{
        void onClickRecipe(Recipe recipe);
    }

    public void swapData(ArrayList<Recipe> newRecipes){
        myRecipes.clear();
        myRecipes.addAll(newRecipes);
        this.notifyDataSetChanged();
    }


    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.recipe_list_content, parent, false);
        return new RecipeViewHolder(item);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        final Recipe recipe= myRecipes.get(position);
        holder.name.setText(recipe.getName());
        //set the image resource if url is provided
        if (recipe.getImage() != null && !recipe.getImage().equals(""))
        {
            Picasso.with(context).load(recipe.getImage()).into(holder.imageView);
        }

        if (recipe.getName().equals(favoriteRecipe)){
            holder.favView.setImageResource(R.drawable.ic_favorite_orange_24dp);
        }else{
            holder.favView.setImageResource(R.drawable.ic_favorite_border_orange_24dp);
        }

        holder.shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share= new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                String text= "Ingredients for "+ recipe.getName()+"\n"
                        +"______________________________________" +"\n";
                for (Ingredient ing : recipe.getIngredients()){

                }
                share.putExtra(Intent.EXTRA_TEXT, text);
                context.startActivity(share);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.recipe_list_text)
        TextView name;

        @BindView(R.id.recipe_list_image)
        ImageView imageView;

        @BindView(R.id.recipe_list_favorite)
        ImageView favView;

        @BindView(R.id.recipe_list_share)
        ImageView shareView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position= getAdapterPosition();
            clickHandler.onClickRecipe(myRecipes.get(position));
        }
    }
}
