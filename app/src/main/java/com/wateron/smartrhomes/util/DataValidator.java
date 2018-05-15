package com.wateron.smartrhomes.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Paranjay on 06-12-2017.
 */

public class DataValidator {

    static boolean checkIfDataPresent(Context context, String view) {
        return context.getSharedPreferences("data_status", MODE_PRIVATE).getBoolean(view+"Loaded",false);
    }

    static boolean checkIfHardReloadNeeded(Context context){
        return context.getSharedPreferences("data_status",MODE_PRIVATE).getBoolean("hardReload",false);
    }

    public static void setHardReload(Context context,boolean hardReloadVal){
        SharedPreferences sharedPreferences = context.getSharedPreferences("data_status",MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("hardReload",hardReloadVal).apply();
    }

    public static void updateDataFlag(Context context,String view,long time) {
        SharedPreferences sharedPreferences  = context.getSharedPreferences("data_status",MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(view+"Loaded",true)
                .putLong(view+"LoadTime",time)
                .apply();
    }

    static boolean checkIfInWaitTime(Context context, String view) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("data_status", MODE_PRIVATE);
        long timeLoaded = sharedPreferences.getLong(view + "LoadTime", 0);
        System.out.println("LoadTime========= :"+timeLoaded);
        if(timeLoaded == 0){
            return false;
        }
        return timeLoaded != 0 && checkTwoHourMark(timeLoaded);
    }

    private static boolean checkTwoHourMark(long timeLoaded) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH", Locale.getDefault());
        String date = sdf.format(new Date(timeLoaded));
        boolean ans = false;
        try {
            long time = sdf.parse(date).getTime();
            if(time + (2*60*60*1000) > new Date().getTime()){
                ans = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("ans",ans?"yes":"no");
        return ans;
    }
}
