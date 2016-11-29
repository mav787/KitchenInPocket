package com.greatsky.kitcheninpocket.object;

/**
 * Created by lshbritta on 16/11/17.
 */

public class ChangePassword {
    private String access_token;
    private String old_password;
    private String new_password;
    private String new_password2;

    public ChangePassword(String access_token, String old_password, String new_password,String new_password2) {
        this.access_token = access_token;
        this.new_password2 = new_password2;
        this.new_password = new_password;
        this.old_password = old_password;
    }
}
