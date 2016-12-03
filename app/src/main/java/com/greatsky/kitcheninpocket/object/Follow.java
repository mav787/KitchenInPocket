package com.greatsky.kitcheninpocket.object;

import java.text.SimpleDateFormat;

/**
 * Created by lshbritta on 16/11/30.
 */

public class Follow {
    String id;
    String name;
    String createtime;
    String intro;

    public Follow(String id, String name, String createtime,  String intro) {
        this.createtime = createtime;
        this.id = id;
        this.intro = intro;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreatetime() {
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long time = new Long(createtime);
        time = time * 1000;
        String d = format.format(time);
        return d;
    }

    public String getIntro() {
        return intro;
    }
}
