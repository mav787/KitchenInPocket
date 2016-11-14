package com.greatsky.kitcheninpocket;

/**
 * Created by lshbritta on 16/11/13.
 */

public class Login_Response {
    String state;
    String message;

    public Login_Response(String message, String state) {
        this.message = message;
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public String getState() {
        return state;
    }
}
