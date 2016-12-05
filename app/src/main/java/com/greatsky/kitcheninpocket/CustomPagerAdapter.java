package com.greatsky.kitcheninpocket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.greatsky.kitcheninpocket.object.Menu;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.R.color.black;

/**
 * Created by fangwenli on 13/11/2016.
 */

//the adapter to inflate the images on the homepage "Weekly Recommended"
class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    List<Menu> recommends;
    LayoutInflater mLayoutInflater;
    String access_token;
    String result;
    int login;
//    int[] mResources = {
//            R.drawable.first,
//            R.drawable.second,
//            R.drawable.third
//    };

    public CustomPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        recommends = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return recommends.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        View dot0 = (View) itemView.findViewById(R.id.v_dot0);
        View dot1 = (View) itemView.findViewById(R.id.v_dot1);
        View dot2 = (View) itemView.findViewById(R.id.v_dot2);


        imageView.setImageBitmap(recommends.get(position).getImage()); //set the image resource
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login==0)
                {
                    Toast.makeText(mContext, R.string.login_first, Toast.LENGTH_SHORT).show();
                }
                else {
                    getRecipe(recommends.get(position).getId());
                }

            }
        });

        switch (position){
            case 0: dot0.setBackgroundResource(R.drawable.dot_focused);
                dot1.setBackgroundResource(R.drawable.dot_normal);
                dot2.setBackgroundResource(R.drawable.dot_normal);
                break;
            case 1: dot1.setBackgroundResource(R.drawable.dot_focused);
                dot0.setBackgroundResource(R.drawable.dot_normal);
                dot2.setBackgroundResource(R.drawable.dot_normal);
                break;
            case 2: dot2.setBackgroundResource(R.drawable.dot_focused);
                dot0.setBackgroundResource(R.drawable.dot_normal);
                dot1.setBackgroundResource(R.drawable.dot_normal);
                break;
        }

        TextView bottomTextItem = (TextView) itemView.findViewById(R.id.bottomText);
//        CustomObject customObject = items.get(position);
        bottomTextItem.setText(recommends.get(position).getName());

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
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

    public void afterGetRecipe(String result)
    {
        if(result.contains("success"))
        {
            Intent intent = new Intent(mContext, RecipeActivity.class);
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
            mContext.startActivity(intent);
        }
        else if(result.contains("error"))
        {
            String[] msg = result.split("\"");
            Toast.makeText(mContext, msg[7], Toast.LENGTH_SHORT).show();
        }
    }


}

