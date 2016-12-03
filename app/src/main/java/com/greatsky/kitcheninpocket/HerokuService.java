package com.greatsky.kitcheninpocket;

import android.database.Observable;

import com.greatsky.kitcheninpocket.object.Authorization;
import com.greatsky.kitcheninpocket.object.ChangePassword;
import com.greatsky.kitcheninpocket.object.Follow;
import com.greatsky.kitcheninpocket.object.FollowRequest;
import com.greatsky.kitcheninpocket.object.Registration;
import com.greatsky.kitcheninpocket.object.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by lshbritta on 16/11/13.
 */

public interface HerokuService {
    @Headers({
            "Accept: application/json",
            "Content-type: application/json;charset=utf-8"
    })

    @POST("/api/v1/users/login")
    Call<ResponseBody> login(@Body User user);
    @POST("/api/v1/users")
    Call<ResponseBody> register(@Body Registration registration);
    @PUT("/api/v1/users/changepassword")
    Call<ResponseBody> changepassword(@Body ChangePassword changePassword);

    @GET("/api/v1/users/{id}/followers")
    Call<ResponseBody> getfollower(@Path("id") String usesrid, @Query("access_token") String authorization);
    @GET("/api/v1/users/{id}/followings")
    Call<ResponseBody> getfollowing(@Path("id") String usesrid, @Query("access_token") String authorization);
    @GET("/api/v1/users/{id}/recipes")
    Call<ResponseBody> getrecipes(@Path("id") String usesrid, @Query("access_token") String authorization);
    @GET("/{url}")
    Call<ResponseBody> loadimage(@Path("url") String fileUrl);
    @GET("/api/v1/users/{id}")
    Call<ResponseBody> userinfo(@Path("id") String userid, @Query("access_token") String authorization);
    @POST("/api/v1/follows")
    Call<ResponseBody> followrequest(@Body FollowRequest fr);
    @POST("/api/v1/follows")
    Call<ResponseBody> deletefollowrequest(@Body FollowRequest fr);
}
