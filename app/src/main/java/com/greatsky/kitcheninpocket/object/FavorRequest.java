package com.greatsky.kitcheninpocket.object;

/**
 * Created by lshbritta on 16/12/3.
 */

public class FavorRequest {

    private String access_token;
    private String recipe_id;

    public FavorRequest(String access_token, String recipe_id) {
        this.access_token = access_token;
        this.recipe_id = recipe_id;
    }
}
