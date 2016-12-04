package com.greatsky.kitcheninpocket.AddRecipe;

/**
 * Created by fangwenli on 29/11/2016.
 */

public class Ingredients_list {

    private String ingredient;
    private String amount;

    public Ingredients_list(){}

    public Ingredients_list(String ingredient, String amount){
        this.ingredient = ingredient;
        this.amount = amount;

    }

    public void setIngredient(String ingredient){
        this.ingredient = ingredient;
    }

    public void setAmount(String amount){
        this.amount = amount;
    }

    public String getIngredient(){
        return this.ingredient;
    }

    public String getAmount(){
        return this.amount;
    }

}
