package com.greatsky.kitcheninpocket.object;

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

    public String getName() {
        return name;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getIntro() {
        return intro;
    }
}
