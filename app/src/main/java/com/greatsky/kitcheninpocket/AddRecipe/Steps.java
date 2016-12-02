package com.greatsky.kitcheninpocket.AddRecipe;

/**
 * Created by fangwenli on 01/12/2016.
 */

public class Steps {

    private String step_num;
    private String description;
    private int image_source;

    public Steps(){}

    public Steps(String step_num, int image_source){
        this.step_num = step_num;
//        this.description = description;
        this.image_source = image_source;
    }

    public void setStep_num(String step_num){
        this.step_num = step_num;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setImage_source(int image_source){
        this.image_source = image_source;
    }

    public String getStep_num(){
        return this.step_num;
    }

    public String getDescription(){
        return this.description;
    }

    public int getImage_source(){
        return this.image_source;
    }
}
