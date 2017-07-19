package com.greatsky.kitcheninpocket;



public class Recommended {

    String title;
    int path;

    public Recommended(){

    }

    public Recommended(String title, int path){
        this.title = title;
        this.path = path;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setPath(int Path){
        this.path = path;
    }

    public String getTitle(){
        return this.title;
    }

    public int getPath(){
        return this.path;
    }
}
