package com.greatsky.kitcheninpocket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.greatsky.kitcheninpocket.object.Authorization;
import com.greatsky.kitcheninpocket.object.Follow;
import com.greatsky.kitcheninpocket.object.User;

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


public class FollowerFragment extends Fragment {

    private ListView followerlv = null;
    private SharedPreferences shared = null;
    Intent intent;
    String access_token = "";
    String result = "";
    String userid = "";

    UserAdapter mAdapter;

    class UserAdapter extends BaseAdapter
    {
        private ArrayList<Follow> list;
        private LayoutInflater userInflater;

        UserAdapter()
        {

            list = new ArrayList<Follow>();

            asynchronousRequest();

        }

        public void add(Follow x)
        {
            list.add(x);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Follow getItem(int position) {
            return list.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            userInflater = LayoutInflater.from(getContext());
            if(convertView == null)
                convertView = (RelativeLayout)userInflater.inflate(R.layout.user_info_listview,null);
            TextView username = (TextView) convertView.findViewById(R.id.user_info_username);
            TextView createtime = (TextView)convertView.findViewById(R.id.user_info_createtime);
            //TextView intro = (TextView)convertView.findViewById(R.id.user_info_intro) ;
            username.setText(mAdapter.getItem(position).getName());
            createtime.setText(mAdapter.getItem(position).getCreatetime());
            //intro.setText(mAdapter.getItem(position).getIntro());
            return convertView;
        }

        public void asynchronousRequest()  {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://kitchen-in-pocket.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            HerokuService restAPI = retrofit.create(HerokuService.class);
            Call<ResponseBody> call = restAPI.getfollower(userid,access_token);
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
                    Follow f = new Follow(info[1], info[3].substring(1,info[3].length()-1),info[5], info[7]);
                    list.add(f);
                }
                update();
            }
            else
            if(result.contains("error"))
            {
                String[] msg = result.split("\"");
                Toast.makeText(getActivity(), msg[7], Toast.LENGTH_SHORT).show();
            }


        }

    }

    public void update(){
        mAdapter.notifyDataSetChanged();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_follower, container, false);
        shared = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        access_token = shared.getString("access_token", "");
        userid = shared.getString("userid", "");
        mAdapter = new UserAdapter();
        followerlv = (ListView)view.findViewById(R.id.my_follower_listview);
        followerlv.setAdapter(mAdapter);
        followerlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("username", mAdapter.getItem(position).getName());
                intent.putExtra("userid", mAdapter.getItem(position).getId());
                FollowRequest(mAdapter.getItem(position).getId());

            }
        });
        return view;
    }

    protected void afterFollowRequest(String result)
    {
        if(result.contains("true"))
            intent.putExtra("isfollowed", 1);
        else
            intent.putExtra("isfollowed", 2);
        getActivity().startActivity(intent);
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


}
