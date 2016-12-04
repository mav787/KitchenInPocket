package com.greatsky.kitcheninpocket.object;

/**
 * Created by fangwenli on 03/12/2016.
 */

public class Ingredients {

    String name;
    String amount;

    public Ingredients(){

    }

    public Ingredients(String name, String amount){
        this.name = name;
        this.amount = amount;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAmount(String amount){
        this.amount = amount;
    }

    public String getName(){
        return this.name;
    }

    public String getAmount(){
        return this.amount;
    }
}
