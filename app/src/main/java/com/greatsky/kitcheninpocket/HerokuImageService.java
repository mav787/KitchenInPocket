package com.greatsky.kitcheninpocket;

import com.greatsky.kitcheninpocket.object.Authorization;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by fangwenli on 03/12/2016.
 */

public interface HerokuImageService {
//    @Headers({
//            "Accept: application/json",
//            "Content-type: multipart/form-data"
//    })

    @Multipart
    @POST("/api/v2/recipe_picture")
    Call<ResponseBody> upload_recipe_picture(@Part("file\"; filename=\"image.png\"") RequestBody imageFile);
}
