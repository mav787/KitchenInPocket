package com.greatsky.kitcheninpocket.object;

/**
 * Created by lshbritta on 16/12/3.
 */

public class FollowRequest {
    public FollowRequest(String access_token, String following_id) {
        this.access_token = access_token;
        this.following_id = following_id;
    }

    private String access_token;
    private String following_id;


}
