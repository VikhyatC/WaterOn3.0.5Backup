package com.wateron.smartrhomes.util;

import android.content.Context;

import java.util.List;

/**
 * Created by Paranjay on 03-12-2017.
 */

public class LoginHandler {

    public static void loginUserToApp(String mobile, String isd, boolean demo, Context context){
        context.getSharedPreferences("login_details",Context.MODE_PRIVATE)
        .edit()
                .putBoolean("loggedIn",true)
                .putString("mobile",mobile)
                .putString("isd",isd)
                .putBoolean("demo",demo)
                .apply();
    }

    public static boolean isUserInDemoMode(Context context){
        return  context.getSharedPreferences("login_details",Context.MODE_PRIVATE).getBoolean("demo",false);
    }

    public static boolean isUserLoggedIn(Context context){
        return  context.getSharedPreferences("login_details",Context.MODE_PRIVATE).getBoolean("loggedIn",false);
    }

    public static void logUserOut(Context context){
        context.getSharedPreferences("login_details",Context.MODE_PRIVATE)
                .edit()
                .putBoolean("loggedIn",false)
                .putString("mobile","")
                .putString("isd","")
                .apply();
    }

    public static String[] getUserMobile(Context context){
        String[] ans = {context.getSharedPreferences("login_details",Context.MODE_PRIVATE).getString("mobile",""),context.getSharedPreferences("login_details",Context.MODE_PRIVATE).getString("isd","")};
        return ans;
    }
}
