package com.greatsky.kitcheninpocket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.greatsky.kitcheninpocket.object.Follow;
import com.greatsky.kitcheninpocket.object.FollowRequest;
import com.greatsky.kitcheninpocket.object.Menu;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

public class MenuActivity extends AppCompatActivity {


    FloatingActionButton fab = null;
    int isFollowed = 0;
    SharedPreferences shared;
    String str_username;
    MenuAdapter mAdapter;
    ListView lv = null;
    String access_token = "";
    String result = "";
    String userid = "";
    String baseURL = "http://kitchen-in-pocket.herokuapp.com/";
    String url = "";


    class MenuAdapter extends BaseAdapter
    {
        private ArrayList<Menu> list;
        private LayoutInflater menuInflater;

        MenuAdapter()
        {
            list = new ArrayList<Menu>();
            asynchronousRequest();
        }

        public void add(Menu x)
        {
            list.add(x);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Menu getItem(int position) {
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
                convertView = (LinearLayout)menuInflater.inflate(R.layout.menu_listview_entry,null);

            final ImageView image = (ImageView) convertView.findViewById(R.id.menu_listview_image);
            TextView name = (TextView)convertView.findViewById(R.id.menu_listview_name);

            name.setText(mAdapter.getItem(position).getName());

            image.setImageBitmap(mAdapter.getItem(position).getImage());
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        getRecipe(mAdapter.getItem(position).getId());

                }
            });

            //image.setImageResource(mAdapter.getItem(position).getImagepath());
            //owner.setText(mAdapter.getItem(position).getOwnername());
            return convertView;
        }

        public void afterGetRecipe(String result)
        {
            if(result.contains("success"))
            {
                Intent intent = new Intent(MenuActivity.this, RecipeActivity.class);
                if(result.contains("true"))
                    intent.putExtra("is_favor", "true");
                else
                    intent.putExtra("is_favor", "false");
                String[] split = result.split("\\}|\\{",5);
                String temp = split[3].replaceAll("\"","");
                String[] msg = temp.split(":|,");
                for(int i = 0; i < msg.length; i= i + 2) {
                    if(msg[i].equals("picture")) {
                        intent.putExtra(msg[i], msg[i + 1] + ":" + msg[i + 2]);
                        i++;
                    }
                    else intent.putExtra(msg[i], msg[i + 1]);

                }
                String[]detail = split[4].split("\\[|\\]");
                String ingredient = detail[1].replaceAll("\"","");
                String step = detail[3].replaceAll("\"","");
                intent.putExtra("ingredient", ingredient);
                intent.putExtra("step", step);
                startActivity(intent);
            }
            else if(result.contains("error"))
            {
                String[] msg = result.split("\"");
                Toast.makeText(MenuActivity.this, msg[7], Toast.LENGTH_SHORT).show();
            }
        }



        public void getRecipe(String id)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://kitchen-in-pocket.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            HerokuService restAPI = retrofit.create(HerokuService.class);
            Call<ResponseBody> call = restAPI.getrecipe(id, access_token);
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
                    afterGetRecipe(result);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("===","failed");
                }
            });
        }


        public void asynchronousImageRequest(final String[] info)
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
                    Menu f = new Menu(info[1],info[3],info[5],info[7],info[9],info[11]);
                    f.setImage(source);
                    list.add(f);
                    update();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("===","failed");
                }
            });
        }

        public void asynchronousRequest()  {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://kitchen-in-pocket.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            HerokuService restAPI = retrofit.create(HerokuService.class);
            Call<ResponseBody> call = restAPI.getrecipes(userid,access_token);
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
                    handleresponse();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("===","failed");
                }
            });
        }

        public void handleresponse()
        {
            if(result.contains("success")) {
                String[] split = result.split("\\}|\\{");
                int i = 3;
                for(; i < split.length; i+= 2)
                {
                    String[] info = split[i].split(":|,");
                    url = info[11].substring(1, info[11].length()) + ":" + info[12].substring(0,info[12].length()-1);
                    asynchronousImageRequest(info);
                }
                update();
            }
            else
            if(result.contains("error"))
            {
                String[] msg = result.split("\"");
                Toast.makeText(MenuActivity.this, msg[7], Toast.LENGTH_SHORT).show();
            }


        }

        public void update(){
            mAdapter.notifyDataSetChanged();
        }



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        str_username = intent.getStringExtra("username");
        isFollowed = intent.getIntExtra("isfollowed", -1);
        //0 - user itself
        //1 - followed
        //2 - unfollow
        fab = (FloatingActionButton) findViewById(R.id.user_follow);
        if (isFollowed == 1)
            fab.setImageResource(R.drawable.heart1);
        else
            fab.setImageResource(R.drawable.heart2);
        shared = getSharedPreferences("login", Context.MODE_PRIVATE);
        access_token = shared.getString("access_token", "");
        mAdapter = new MenuAdapter();
        setTitle("Menu(" + str_username +")");
        lv = (ListView)findViewById(R.id.menu_listview);

        lv.setAdapter(mAdapter);

        if(isFollowed == 0)
            fab.setVisibility(View.INVISIBLE);
        else {

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFollowed == 2) {
                        FollowRequest();
                    } else {
                        DeleteFollowRequest();
                    }

                }
            });
        }
    }

    protected void afterFollowRequest(String result)
    {
        if(result.contains("success"))
        {
            fab.setImageResource(R.drawable.heart1);
            isFollowed = 1;
            Toast.makeText(MenuActivity.this, "LIKE THIS", Toast.LENGTH_SHORT).show();
        }
        else
        if(result.contains("error"))
        {
            String[] msg = result.split("\"");
            Toast.makeText(MenuActivity.this, msg[7], Toast.LENGTH_SHORT).show();
        }
    }

    protected void afterDeleteFollowRequest(String result)
    {
        if(result.contains("success"))
        {
            fab.setImageResource(R.drawable.heart2);
            isFollowed = 2;
            Toast.makeText(MenuActivity.this, "DON'T LIKE THIS ANY MORE", Toast.LENGTH_SHORT).show();
        }
        else
        if(result.contains("error"))
        {
            String[] msg = result.split("\"");
            Toast.makeText(MenuActivity.this, msg[7], Toast.LENGTH_SHORT).show();
        }
    }



    protected void FollowRequest()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://kitchen-in-pocket.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        HerokuService restAPI = retrofit.create(HerokuService.class);
        FollowRequest fr = new FollowRequest(access_token, userid);
        Call<ResponseBody> call = restAPI.followrequest(fr);

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


    protected void DeleteFollowRequest()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://kitchen-in-pocket.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        HerokuService restAPI = retrofit.create(HerokuService.class);
        FollowRequest fr = new FollowRequest(access_token, userid);
        Call<ResponseBody> call = restAPI.deletefollowrequest(fr);

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
                afterDeleteFollowRequest(result);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("===","failed");
            }
        });

    }



}
