package com.example.ongraph.socialapp;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPref {

    private Context context;
    private SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String APP_PREF = "Social_Preference";
    public static final String APP_IS_LOGIN = "isLogin";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String IMAGE = "image";
    public static final String NAME = "Akash";

    public AppSharedPref(Context context){
        this.context = context;
        sharedpreferences = context.getSharedPreferences(APP_PREF, 0);
    }

    public void setAppIsLogin(boolean value){
        editor = sharedpreferences.edit();
        editor.putBoolean(APP_IS_LOGIN, value);
        editor.commit();
    }

    public void setUsername(String username) {
        editor = sharedpreferences.edit();
        editor.putString(USERNAME, username);
        editor.commit();
    }

    public void setEmail(String email){
        editor = sharedpreferences.edit();
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public void setImage(String url){
        editor = sharedpreferences.edit();
        editor.putString(IMAGE, url);

        editor.commit();
    }

    public void setName(String name) {
        editor = sharedpreferences.edit();
        editor.putString(NAME, name);
        editor.commit();
    }

    public String getName() {
        return sharedpreferences.getString(NAME  , null);
    }

    public String getUsername() {
        return sharedpreferences.getString(USERNAME  , null);
    }

    public String getEmail() {
        return sharedpreferences.getString(EMAIL  , null);
    }

    public String getImage() {
        return sharedpreferences.getString(IMAGE  , null);
    }

    public boolean isAppLogin(){
        return sharedpreferences.getBoolean(APP_IS_LOGIN  , false);
    }
}
