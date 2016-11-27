package com.greatsky.kitcheninpocket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.greatsky.kitcheninpocket.object.Authorization;
import com.greatsky.kitcheninpocket.object.ChangePassword;

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

public class ChangePasswordActivity extends AppCompatActivity {

    EditText old_password = null;
    EditText new_password = null;
    EditText renew_password = null;
    Button cancel = null;
    Button submit = null;
    String str_old = "";
    String str_new = "";
    String str_renew = "";
    String result = "";
    SharedPreferences shared = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        shared = getSharedPreferences("login", MODE_PRIVATE);
        old_password = (EditText)findViewById(R.id.change_old_pass);
        new_password = (EditText)findViewById(R.id.change_new_pass);
        renew_password = (EditText)findViewById(R.id.change_renew_pass);
        cancel = (Button)findViewById(R.id.change_cancel);
        submit = (Button)findViewById(R.id.change_submit);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                old_password.setText("");
                new_password.setText("");
                renew_password.setText("");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_old = old_password.getText().toString();
                str_new = new_password.getText().toString();
                str_renew = renew_password.getText().toString();
                ChangePasswordRequest();
            }
        });
    }

    public void ChangePasswordRequest()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://kitchen-in-pocket.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        HerokuService restAPI = retrofit.create(HerokuService.class);
        ChangePassword changePassword = new ChangePassword(str_old, str_new, str_renew);
        String access_token = shared.getString("access_token","");
        //Authorization authorization = new Authorization(access_token);
        Call<ResponseBody> call = restAPI.changepassword(access_token, changePassword);

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
                afterChangePassword();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("===","failed");
            }
        });
    }

    public void afterChangePassword()
    {
        if(result.contains("success")) {
            Intent intent = new Intent(ChangePasswordActivity.this, MyInfoActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.failed_change_password), Toast.LENGTH_SHORT).show();
    }
}
