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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.greatsky.kitcheninpocket.object.FavorRequest;
import com.greatsky.kitcheninpocket.object.Menu;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

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

    TextView recipeName, ingredientContent,authorUsername;
    ListView steplist;
    ImageView coverPicture;
    FloatingActionButton likeIcon;
    String isFavored = "";
    SharedPreferences shared;
    String access_token = "";
    String recipeId = "";
    String result = "";
    String ingredient = "";
    String step = "";
    Intent intent;
    Intent i;
    RecipeAdapter adapter;
    String[] steps;

    class RecipeAdapter extends BaseAdapter
    {
        private ArrayList<String> list;
        private LayoutInflater menuInflater;

        RecipeAdapter()
        {
            list = new ArrayList<String>();
            for(String x : steps)
            {
                list.add(x);
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            menuInflater = LayoutInflater.from(getApplicationContext());
            if(convertView == null)
                convertView = (RelativeLayout)menuInflater.inflate(R.layout.show_step_item,null);

            TextView id = (TextView) convertView.findViewById(R.id.step_item_id);
            TextView stepcontent = (TextView)convertView.findViewById(R.id.step_item_content);

            id.setText("STEP " + Integer.toString(position + 1) + ":");
            stepcontent.setText(adapter.getItem(position));


            //image.setImageResource(mAdapter.getItem(position).getImagepath());
            //owner.setText(mAdapter.getItem(position).getOwnername());
            return convertView;
        }

    }

    public void setHeight(){
        int listViewHeight = 0;
        int adaptCount = adapter.getCount();
        for(int i=0;i<adaptCount;i++){
            View temp = adapter.getView(i,null,steplist);
            temp.measure(0,0);
            listViewHeight += temp.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = this.steplist.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.FILL_PARENT;
        layoutParams.height = listViewHeight;
        steplist.setLayoutParams(layoutParams);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        intent = getIntent();
        isFavored = intent.getStringExtra("is_favor");
        recipeId = intent.getStringExtra("id");
        ingredient = intent.getStringExtra("ingredient");
        step = intent.getStringExtra("step");
        shared = getSharedPreferences("login", Context.MODE_PRIVATE);
        access_token = shared.getString("access_token", "");

        recipeName = (TextView) findViewById(R.id.recipe_name);
        recipeName.setText(intent.getStringExtra("name"));


        ingredientContent = (TextView) findViewById(R.id.ingredient_content);

        ingredient = ingredient.replaceAll("\\{|\\}", "");
        String[] ingredients = ingredient.split(":|,");
        ingredient = "";
        int count = 1;
        for(int i = 0; i < ingredients.length; i = i + 4)
        {
            ingredient = ingredient + count + ": " + ingredients[i+1];
            int j = ingredients[i+1].length();
            while(j < 25) {
                ingredient += " ";
                j++;
            }
            ingredient+= ingredients[i+3] + "\n";
            count ++;
        }
        ingredientContent.setText(ingredient);


        steplist = (ListView) findViewById(R.id.step_listview);
        steps = step.split(",");
        adapter = new RecipeAdapter();
        setHeight();
        steplist.setAdapter(adapter);

        authorUsername = (TextView) findViewById(R.id.author_username);
        authorUsername.setText(intent.getStringExtra("user_name"));
        authorUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(RecipeActivity.this, MenuActivity.class);
                i.putExtra("username", intent.getStringExtra("user_name"));
                i.putExtra("userid", intent.getStringExtra("user_id"));
                FollowRequest(intent.getStringExtra("user_id"));
            }
        });

        coverPicture = (ImageView) findViewById(R.id.cover_picture);
        asynchronousImageRequest(intent.getStringExtra("picture"));


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

    protected void afterFollowRequest(String result)
    {
        if(result.contains("true"))
            i.putExtra("isfollowed", 1);
        else
            i.putExtra("isfollowed", 2);
        startActivity(i);
    }

    protected void FollowRequest(String Userid)
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://kitchen-in-pocket.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        HerokuService restAPI = retrofit.create(HerokuService.class);
        Call<ResponseBody> call = restAPI.userinfo(Userid, access_token);

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
                afterFollowRequest(result);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("===","failed");
            }
        });

    }


    public void asynchronousImageRequest(String url)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://res.cloudinary.com/hsayf1nxm/image/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        HerokuService restAPI = retrofit.create(HerokuService.class);
        String[] temp = url.split("image");
        Call<ResponseBody> call = restAPI.loadimage(temp[1].substring(1, temp[1].length()));
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
