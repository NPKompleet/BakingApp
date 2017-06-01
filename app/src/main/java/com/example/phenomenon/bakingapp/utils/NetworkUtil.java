package com.example.phenomenon.bakingapp.utils;



import com.example.phenomenon.bakingapp.pojo.Ingredient;
import com.example.phenomenon.bakingapp.pojo.Recipe;
import com.example.phenomenon.bakingapp.pojo.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by PHENOMENON on 5/26/2017.
 */

public  class NetworkUtil {

    //private static  String path= "http://go.udacity.com/android-baking-app-json";

    private static OkHttpClient client = new OkHttpClient();


    private static String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static ArrayList<Recipe> fetchData()
    {
        String path= "http://go.udacity.com/android-baking-app-json";
        String data= null;
        ArrayList<Recipe> Recipes= new ArrayList<>();

        try{
            data= run(path);
        } catch (IOException e){
            e.printStackTrace();
        }

        //Use a JSON array since expected response is the form of a JSON Array
        JSONArray jData= null;
        try {
            jData = new JSONArray(data);

            for (int i=0; i<jData.length(); i++){
                JSONObject obj= jData.getJSONObject(i);

                int id = obj.getInt("id");
                String name= obj.getString("name");
                JSONArray ingredients= obj.getJSONArray("ingredients");
                JSONArray steps= obj.getJSONArray("steps");
                int servings = obj.getInt("servings");
                String image= obj.getString("image");

                ArrayList<Ingredient> ingredientList= new ArrayList<>();
                for (int j=0; j<ingredients.length(); j++){
                    //get each ingredient
                    JSONObject ingObj= ingredients.getJSONObject(j);
                    int quantity = ingObj.getInt("quantity");
                    String measure= ingObj.getString("measure");
                    String ingredientName= ingObj.getString("ingredient");

                    Ingredient ingredient= new Ingredient(quantity, measure, ingredientName);
                    //add each ingredient to the list of ingredients for each recipe
                    ingredientList.add(ingredient);
                }


                ArrayList<Step> stepList= new ArrayList<>();
                for (int k=0; k<steps.length(); k++){
                    //get each step
                    JSONObject stepObj= steps.getJSONObject(k);
                    int stepId = stepObj.getInt("id");
                    String shortDescription= stepObj.getString("shortDescription");
                    String description= stepObj.getString("description");
                    String videoURL= stepObj.getString("videoURL");
                    String thumbnailURL= stepObj.getString("thumbnailURL");

                    Step step= new Step(stepId, shortDescription, description, videoURL, thumbnailURL);
                    //add each successive steps to the list of steps for each recipe
                    stepList.add(step);
                }

                Recipe recipe= new Recipe(id, name, ingredientList, stepList, servings, image);
                //add each recipe to the list of recipes
                Recipes.add(recipe);

            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return Recipes;
    }
}
