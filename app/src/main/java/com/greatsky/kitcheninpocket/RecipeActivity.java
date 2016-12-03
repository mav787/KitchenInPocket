package com.greatsky.kitcheninpocket;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by fangwenli on 29/11/2016.
 */

public class RecipeActivity extends Activity{

    TextView recipeName, ingredientContent,step1Content,step2Content,step3Content,authorUsername;
    ImageView coverPicture, step1Picture, step2Picture, step3Picture;
    ImageButton authorImage;
    FloatingActionButton likeIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recipeName = (TextView) findViewById(R.id.recipe_name);
        ingredientContent = (TextView) findViewById(R.id.ingredient_content);
        step1Content = (TextView) findViewById(R.id.step1_content);
        step2Content = (TextView) findViewById(R.id.step2_content);
        step3Content = (TextView) findViewById(R.id.step3_content);
        authorUsername = (TextView) findViewById(R.id.author_username);

        coverPicture = (ImageView) findViewById(R.id.cover_picture);
        step1Picture = (ImageView) findViewById(R.id.step1_picture);
        step2Picture = (ImageView) findViewById(R.id.step2_picture);
        step3Picture = (ImageView) findViewById(R.id.step3_picture);

        authorImage = (ImageButton) findViewById(R.id.author_image);
        likeIcon = (FloatingActionButton) findViewById(R.id.like_icon);
    }



}
