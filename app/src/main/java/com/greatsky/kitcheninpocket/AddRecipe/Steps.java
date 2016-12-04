package com.greatsky.kitcheninpocket.AddRecipe;

/**
 * Created by fangwenli on 01/12/2016.
 */

public class Steps {

    private String step_num;
    private String description;

    public Steps(){}

    public Steps(String step_num, String description){
        this.step_num = step_num;
        this.description = description;
    }

    public void setStep_num(String step_num){
        this.step_num = step_num;
    }

    public void setDescription(String description){
        this.description = description;
    }


    public String getStep_num(){
        return this.step_num;
    }

    public String getDescription(){
        return this.description;
    }

}
