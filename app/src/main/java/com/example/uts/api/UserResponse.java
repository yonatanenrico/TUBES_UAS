package com.example.uts.api;

import com.example.uts.entity.User;
import com.google.gson.annotations.SerializedName;

public class UserResponse {
    private String message;
    @SerializedName("user")
    private User user;
    @SerializedName("access_token")
    private String accessToken;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}