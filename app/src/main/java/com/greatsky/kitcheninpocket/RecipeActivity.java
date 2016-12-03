package com.greatsky.kitcheninpocket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.greatsky.kitcheninpocket.object.FavorRequest;
import com.greatsky.kitcheninpocket.object.Menu;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by fangwenli on 29/11/2016.
 */

public class RecipeActivity extends Activity{

    TextView recipeName, ingredientContent,step1Content,step2Content,step3Content,authorUsername;
    ImageView coverPicture, step1Picture, step2Picture, step3Picture;
    ImageButton authorImage;
    FloatingActionButton likeIcon;
    String isFavored = "";
    SharedPreferences shared;
    String access_token = "";
    String recipeId = "";
    String result = "";
    String ingredient = "";
    String step = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        isFavored = intent.getStringExtra("is_favor");
        recipeId = intent.getStringExtra("id");
        ingredient = intent.getStringExtra("ingredient");
        step = intent.getStringExtra("step");
        shared = getSharedPreferences("login", Context.MODE_PRIVATE);
        access_token = shared.getString("access_token", "");

        recipeName = (TextView) findViewById(R.id.recipe_name);
        recipeName.setText(intent.getStringExtra("name"));
        ingredientContent = (TextView) findViewById(R.id.ingredient_content);
        step1Content = (TextView) findViewById(R.id.step1_content);
        step2Content = (TextView) findViewById(R.id.step2_content);
        step3Content = (TextView) findViewById(R.id.step3_content);
        authorUsername = (TextView) findViewById(R.id.author_username);
        authorUsername.setText(intent.getStringExtra("user_name"));

        coverPicture = (ImageView) findViewById(R.id.cover_picture);
        asynchronousImageRequest(intent.getStringExtra("picture"));

        step1Picture = (ImageView) findViewById(R.id.step1_picture);
        step2Picture = (ImageView) findViewById(R.id.step2_picture);
        step3Picture = (ImageView) findViewById(R.id.step3_picture);

        authorImage = (ImageButton) findViewById(R.id.author_image);
        likeIcon = (FloatingActionButton) findViewById(R.id.like_icon);
        if (isFavored.equals("true"))
            likeIcon.setImageResource(R.drawable.heart1);
        else
            likeIcon.setImageResource(R.drawable.heart2);
        likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFavored.equals("false")) {
                    FavorRequest();
                } else {
                    DeleteFavorRequest();
                }

            }
        });
    }

    public void asynchronousImageRequest(String url)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://kitchen-in-pocket.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        HerokuService restAPI = retrofit.create(HerokuService.class);
        Call<ResponseBody> call = restAPI.loadimage(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Log.e("===","return:" + response.body().toString());
                Bitmap source = BitmapFactory.decodeStream(response.body().byteStream());
                coverPicture.setImageBitmap(source);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("===","failed");
            }
        });
    }


    protected void afterFavorRequest(String result)
    {
        if(result.contains("success"))
        {
            likeIcon.setImageResource(R.drawable.heart1);
            isFavored = "true";
            Toast.makeText(RecipeActivity.this, "LIKE THIS", Toast.LENGTH_SHORT).show();
        }
        else
        if(result.contains("error"))
        {
            String[] msg = result.split("\"");
            Toast.makeText(RecipeActivity.this, msg[7], Toast.LENGTH_SHORT).show();
        }
    }

    protected void afterDeleteFavorRequest(String result)
    {
        if(result.contains("success"))
        {
            likeIcon.setImageResource(R.drawable.heart2);
            isFavored = "false";
            Toast.makeText(RecipeActivity.this, "DON'T LIKE THIS ANY MORE", Toast.LENGTH_SHORT).show();
        }
        else
        if(result.contains("error"))
        {
            String[] msg = result.split("\"");
            Toast.makeText(RecipeActivity.this, msg[7], Toast.LENGTH_SHORT).show();
        }
    }



    protected void FavorRequest()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://kitchen-in-pocket.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        HerokuService restAPI = retrofit.create(HerokuService.class);
        FavorRequest fr = new FavorRequest(access_token, recipeId);
        Call<ResponseBody> call = restAPI.favorrequest(fr);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Log.e("===","return:" + response.body().toString());
                BufferedSource source = response.body().source();
                try {
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Buffer buffer = source.buffer();
                result = buffer.clone().readString(Charset.forName("UTF-8"));
                afterFavorRequest(result);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("===","failed");
            }
        });

    }


    protected void DeleteFavorRequest()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://kitchen-in-pocket.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        HerokuService restAPI = retrofit.create(HerokuService.class);
        FavorRequest fr = new FavorRequest(access_token, recipeId);
        Call<ResponseBody> call = restAPI.deletefavorrequest(fr);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Log.e("===","return:" + response.body().toString());
                BufferedSource source = response.body().source();
                try {
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Buffer buffer = source.buffer();
                result = buffer.clone().readString(Charset.forName("UTF-8"));
                afterDeleteFavorRequest(result);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("===","failed");
            }
        });

    }


}
