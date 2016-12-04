package com.greatsky.kitcheninpocket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class FavorActivity extends AppCompatActivity {


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
                Intent intent = new Intent(FavorActivity.this, RecipeActivity.class);
                intent.putExtra("is_favor", "true");
                String[] split = result.split("\\}|\\{",5);
                String temp = split[3].replaceAll("\"","");
                String[] msg = temp.split(":|,");
                for(int i = 0; i < msg.length; i= i + 2)
                    intent.putExtra(msg[i], msg[i+1]);
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
                Toast.makeText(FavorActivity.this, msg[7], Toast.LENGTH_SHORT).show();
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
            Call<ResponseBody> call = restAPI.getfavors(access_token);
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
                    url = info[11].substring(1, info[11].length()-1);
                    asynchronousImageRequest(info);
                }
                update();
            }
            else
            if(result.contains("error"))
            {
                String[] msg = result.split("\"");
                Toast.makeText(FavorActivity.this, msg[7], Toast.LENGTH_SHORT).show();
            }


        }

        public void update(){
            mAdapter.notifyDataSetChanged();
        }



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        str_username = intent.getStringExtra("username");
        shared = getSharedPreferences("login", Context.MODE_PRIVATE);
        access_token = shared.getString("access_token", "");
        mAdapter = new MenuAdapter();
        setTitle("Menu(" + str_username +")");
        lv = (ListView)findViewById(R.id.menu_listview);

        lv.setAdapter(mAdapter);

    }




}
