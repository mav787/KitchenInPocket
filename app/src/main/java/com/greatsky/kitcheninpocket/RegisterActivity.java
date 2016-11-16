package com.greatsky.kitcheninpocket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.greatsky.kitcheninpocket.object.Registration;

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

public class RegisterActivity extends AppCompatActivity {

    private Button cancel;
    private Button register;
    private EditText username;
    private EditText password;
    private EditText repassword;
    String str_username;
    String str_password;
    String str_repassword;
    String result = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        cancel = (Button)findViewById(R.id.register_cancel);
        register = (Button)findViewById(R.id.register_register);
        username = (EditText)findViewById(R.id.register_username);
        password = (EditText)findViewById(R.id.register_pass);
        repassword = (EditText)findViewById(R.id.register_repass);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username.setText("");
                password.setText("");
                repassword.setText("");
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_username = username.getText().toString();
                str_password = password.getText().toString();
                str_repassword = repassword.getText().toString();
                if(str_username.length() > 0 && str_password.length() > 0 && str_repassword.length() > 0)
                    RegisterRequest();
            }
        });
    }

    protected void RegisterRequest()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://kitchen-in-pocket.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        HerokuService restAPI = retrofit.create(HerokuService.class);
        Registration registration = new Registration(str_username,str_password,str_repassword);
        Call<ResponseBody> call = restAPI.register(registration);

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
                afterRegister();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("===","failed");
            }
        });

    }

    public void afterRegister()
    {
        if(result.contains("success")) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.invalid_user), Toast.LENGTH_SHORT).show();

    }

}
