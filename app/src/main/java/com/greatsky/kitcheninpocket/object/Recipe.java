package com.greatsky.kitcheninpocket.object;

import android.graphics.Bitmap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by fangwenli on 03/12/2016.
 */

public class Recipe {

    String access_token;
    String recipe_name;
    String recipe_url;
    ArrayList<Ingredients> ingredients;
    ArrayList<String> steps;


    public Recipe(String access_token, String recipe_name, String recipe_url, ArrayList<Ingredients> ingredients, ArrayList<String> steps) {
        this.access_token = access_token;
        this.recipe_name = recipe_name;
        this.recipe_url = recipe_url;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public void setRecipe_name(String recipe_name){
        this.recipe_name = recipe_name;
    }

    public void setRecipe_url(String recipe_url){
        this.recipe_url = recipe_url;
    }

    public void setIngredients(ArrayList<Ingredients> ingredients){
        this.ingredients = ingredients;
    }

    public void setSteps(ArrayList<String> steps){
        this.steps = steps;
    }

    public String getRecipe_name(){
        return this.recipe_name;
    }

    public String getRecipe_url(){
        return this.recipe_url;
    }

    public ArrayList<Ingredients> getIngredients(){
        return this.ingredients;
    }

    public ArrayList<String> getSteps(){
        return this.steps;
    }
}
