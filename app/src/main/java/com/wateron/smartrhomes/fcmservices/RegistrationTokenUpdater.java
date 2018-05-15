package com.wateron.smartrhomes.fcmservices;

import android.content.Context;

import android.os.AsyncTask;
import android.util.Log;

import com.wateron.smartrhomes.util.LoginHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by paran on 9/29/2016.
 */

public class RegistrationTokenUpdater {

    public static Context context;

    public static void updateToken(Context context,String token){
        RegistrationTokenUpdater.context=context;
        UpdateTask updateTask=new UpdateTask();
        String number[]= LoginHandler.getUserMobile(context);
        if(!number.equals("null")){
            updateTask.execute(token,number[1]+number[0]);
        }
    }

    public static class UpdateTask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {

            String url="https://www.nuclious.com/app/GCMreg.php?gcmID="+params[0]+"&mobileNo="+params[1];
            Log.d("FCM Token url",url);
            try {
                URL object=new URL(url);

                HttpsURLConnection con = (HttpsURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestMethod("POST");

                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();
                Log.d("response",String.valueOf(HttpResult));
                if (HttpResult == HttpsURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    parseData(sb.toString());
                    System.out.println("" + sb.toString());
                }

            }catch ( IOException e) {
                e.printStackTrace();
                try{
//                    ((HomeActivity)context).onDownLOadError();
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
            return null;
        }

        private void parseData(String s) {
            Log.d("FCM JSON Recieved",s);
        }
    }

}
