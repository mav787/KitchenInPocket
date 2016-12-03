package com.greatsky.kitcheninpocket.object;

import android.graphics.Bitmap;

import java.text.SimpleDateFormat;

/**
 * Created by lshbritta on 16/12/1.
 */

public class Menu {
    String id;
    String create_time;
    String picture;
    String name;
    String user_id;
    String user_name;
    Bitmap image;

    public Menu( String id, String name, String user_id, String user_name, String create_time,  String picture) {
        this.create_time = create_time;
        this.id = id;
        this.name = name.substring(1, name.length()-1);
        this.picture = picture.substring(1, picture.length()-1);
        this.user_id = user_id;
        this.user_name = user_name.substring(1, user_name.length()-1);
    }

    public String getCreate_time() {
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long time = new Long(create_time);
        time = time * 1000;
        String d = format.format(time);
        return d;
    }

    public String getName() {
        return name;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPicture() {
        return picture;
    }

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }
}
