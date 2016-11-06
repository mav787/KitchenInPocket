package com.greatsky.kitcheninpocket;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button=(Button)findViewById(R.id.isAuto);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                SharedPreferences.Editor editor=getSharedPreferences("login", Context.MODE_PRIVATE).edit();
                editor.putInt("auto", 0);
                editor.commit();
                Toast.makeText(MainActivity.this, getResources().getString(R.string.cancel_auto_login), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
