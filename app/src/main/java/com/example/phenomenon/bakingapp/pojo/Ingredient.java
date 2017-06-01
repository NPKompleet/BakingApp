package com.example.phenomenon.bakingapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PHENOMENON on 5/25/2017.
 */

public class Ingredient implements Parcelable{
    private int quantity;
    private String measure;
    private String ingredientName;

    public Ingredient(int quantity, String measure, String ingredientName){
        this.quantity= quantity;
        this.measure= measure;
        this.ingredientName= ingredientName;
    }


    protected Ingredient(Parcel in) {
        quantity = in.readInt();
        measure = in.readString();
        ingredientName = in.readString();
    }


    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }



    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quantity);
        dest.writeString(measure);
        dest.writeString(ingredientName);
    }
}
