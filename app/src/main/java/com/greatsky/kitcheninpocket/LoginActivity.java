package com.greatsky.kitcheninpocket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

    private EditText user,pass =null;
    private CheckBox checkpass,auto_login =null;
    private Button login =null;
    private LinearLayout main =null;
    private TextView auto =null;

    private String str_user,str_pass =null;

    private SharedPreferences shared =null;
    private SharedPreferences.Editor editor =null;

    private int isCheck =0;
    private int isAuto =0;
    private int isLogIn = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitView();
        //
        shared =this.getSharedPreferences("login", Context.MODE_PRIVATE);
        editor =shared.edit();

        isAuto =shared.getInt("auto", 0);
        isCheck =shared.getInt("check",0);
        isLogIn =shared.getInt("login", 0);

        if(isAuto ==1){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        main.setVisibility(View.GONE);
                        auto.setVisibility(View.VISIBLE);
                        Thread.sleep(2000);
                        editor.putInt("login",1);
                        Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        if(isCheck==1){

            str_user =shared.getString("user", "");
            str_pass =shared.getString("pass", "");

            user.setText(str_user);
            pass.setText(str_pass);

            checkpass.setChecked(true);
        }else {

            checkpass.setChecked(false);
        }


        auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean bool) {
                // TODO Auto-generated method stub
                if(bool){
                    checkpass.setChecked(true);
                }
            }
        });

        checkpass.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean bool) {
                // TODO Auto-generated method stub
                if(!bool){
                    auto_login.setChecked(false);

                    editor.putInt("check",0);
                    editor.putInt("auto", 0);
                    editor.commit();
                }
            }
        });
        //��¼�¼�
        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(isLogin()){
                    if(str_user.equals("123") && str_pass.equals("123")){

                        if(auto_login.isChecked()){
                            editor.putInt("check",1);
                            editor.putInt("auto", 1);
                            editor.putString("user", str_user);
                            editor.putString("pass",str_pass);
                        }else if(checkpass.isChecked()){
                            editor.putInt("check",1);
                            editor.putString("user", str_user);
                            editor.putString("pass",str_pass);
                        }else {
                            editor.putInt("check",0);
                            editor.putInt("auto", 0);
                            editor.putString("user", str_user);
                            editor.putString("pass",str_pass);
                        }
                        editor.putInt("login",1);
                        editor.commit();
                        Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.invalid_user), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.failed_login) , Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private Boolean isLogin(){
        str_user =user.getText().toString();
        str_pass =pass.getText().toString();
        if(str_user.equals("") || str_pass.equals(""))
            return false;
        return true;
    }

    private void InitView() {
        // TODO Auto-generated method stub
        user =(EditText)findViewById(R.id.user);
        pass =(EditText)findViewById(R.id.pass);
        login =(Button)findViewById(R.id.login);
        checkpass =(CheckBox)findViewById(R.id.checkpass);
        auto_login =(CheckBox)findViewById(R.id.auto_login);
        auto =(TextView)findViewById(R.id.auto);
        main =(LinearLayout)findViewById(R.id.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        isAuto =shared.getInt("auto", 0);
        main.setVisibility(View.VISIBLE);
        auto.setVisibility(View.GONE);
        if(isAuto==1){
            auto_login.setChecked(true);
        }else {
            auto_login.setChecked(false);
        }
    }


}



