package com.example.uts.SharedPreference.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.uts.entity.User;

public class UserPreferences {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public static final String IS_LOGIN = "isLogin";
    public static final String KEY_ID = "id";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAMA_AKUN = "nama_akun";
    public static final String KEY_IMAGE = "image";
    public static final String ACCESS_TOKEN = "access_token";

    public UserPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userPreferences",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setUser(Long id, String email, String password, String nama_akun, String image, String access_token){
        editor.putBoolean(IS_LOGIN, true);
        editor.putLong(KEY_ID,id);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PASSWORD,password);
        editor.putString(KEY_NAMA_AKUN,nama_akun);
        editor.putString(KEY_IMAGE,image);
        editor.putString(ACCESS_TOKEN,access_token);
        editor.commit();
    }

    public User getUserLogin(){
        String email,password,nama_akun,image;
        Long id;

        id = sharedPreferences.getLong(KEY_ID,0);
        password = sharedPreferences.getString(KEY_PASSWORD,null);
        email = sharedPreferences.getString(KEY_EMAIL,null);
        nama_akun = sharedPreferences.getString(KEY_NAMA_AKUN,null);
        image = sharedPreferences.getString(KEY_IMAGE,null);
        User user = new User(email,password,nama_akun,image);
        user.setId(id);
        return user;
    }

    public void setKeyEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public void setKeyPassword(String password) {
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public void setKeyNamaAkun(String nama_akun) {
        editor.putString(KEY_NAMA_AKUN, nama_akun);
        editor.commit();
    }

    public void setKeyImage(String image){
        editor.putString(KEY_IMAGE, image);
        editor.commit();
    }

    public String getAccessToken(){
        return sharedPreferences.getString(ACCESS_TOKEN, null);
    }

    public boolean checkLogin(){
        return sharedPreferences.getBoolean(IS_LOGIN,false);
    }

    public void logout(){
        editor.clear();
        editor.commit();
    }
}
