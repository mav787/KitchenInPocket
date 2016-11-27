package com.greatsky.kitcheninpocket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MyInfoActivity extends AppCompatActivity {

    private Button changepass = null;
    private TextView username = null;
    private ListView followers = null;
    private ListView followings = null;
    SharedPreferences shared;
    SharedPreferences.Editor editor;
    String str_username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        shared = getSharedPreferences("login", MODE_PRIVATE);
        str_username = shared.getString("user","");


        changepass = (Button)findViewById(R.id.my_info_changepassword);
        username = (TextView)findViewById(R.id.my_info_username);
        followers = (ListView)findViewById(R.id.my_info_myfollowers);
        followings = (ListView)findViewById(R.id.my_info_myfollowings);

        username.setText(str_username);
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyInfoActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });


    }
}
