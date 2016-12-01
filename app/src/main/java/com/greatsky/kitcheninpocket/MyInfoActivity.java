package com.greatsky.kitcheninpocket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greatsky.kitcheninpocket.object.Follow;

import java.util.ArrayList;

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView changepass = null;
    private TextView username = null;
    private RelativeLayout followers_layout = null;
    private RelativeLayout followings_layout = null;
    private Fragment follower = null;
    private Fragment following = null;
    private Fragment currentFragment = null;

    private ListView followinglv = null;
    SharedPreferences shared;
    SharedPreferences.Editor editor;
    String str_username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        shared = getSharedPreferences("login", MODE_PRIVATE);
        str_username = shared.getString("user","");


        followers_layout = (RelativeLayout)findViewById(R.id.get_my_follower_layout) ;
        followings_layout = (RelativeLayout)findViewById(R.id.get_my_following_layout) ;
        followings_layout.setOnClickListener(this);
        followers_layout.setOnClickListener(this);
        changepass = (TextView) findViewById(R.id.my_info_changepassword);
        username = (TextView)findViewById(R.id.my_info_username);


        username.setText(str_username);
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyInfoActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        initTab();


    }


    private void initTab() {
        if (follower == null) {
            follower = new FollowerFragment();
        }

        if (!follower.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_layout, follower).commit();

            currentFragment = follower;
            followers_layout.setBackgroundResource(R.color.light_gray);
        }

    }

    private void addOrShowFragment(FragmentTransaction transaction,
                                   Fragment fragment) {
        if (currentFragment == fragment)
            return;

        if (!fragment.isAdded()) {
            transaction.hide(currentFragment)
                    .add(R.id.content_layout, fragment).commit();
        } else {
            transaction.hide(currentFragment).show(fragment).commit();
        }

        currentFragment = fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_my_follower_layout:
                clickFollowerLayout();
                break;
            case R.id.get_my_following_layout:
                clickFollowingLayout();
                break;
            default:
                break;
        }
    }

    private void clickFollowerLayout() {
        if (follower == null) {
            follower = new FollowerFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), follower);
        followers_layout.setBackgroundResource(R.color.light_gray);
        followings_layout.setBackgroundResource(R.color.white);

    }

    private void clickFollowingLayout() {
        if (following == null) {
            following = new FollowingFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), following);

        followings_layout.setBackgroundResource(R.color.light_gray);
        followers_layout.setBackgroundResource(R.color.white);
    }
}
