package com.wateron.smartrhomes.util;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Paranjay on 14-12-2017.
 */

public class MeterRenameHelper {

    private static MeterHandlerInterface handlerInterface;

    public static void changeValveName(String mobile,String isd,String token,MeterHandlerInterface handlerInterface, String meterid, String newName) {
        MeterRenameHelper.handlerInterface=handlerInterface;
        NameChanger nameChanger=new NameChanger();
        Log.d("ValvaChangeToken",token);
        nameChanger.execute("("+isd+")"+mobile,token,meterid,newName);
    }

    private static class NameChanger extends AsyncTask<String,Void,Void>{

        String response = "";
        int HttpResult;
        String url;
        private String token;
        private String xmsin;
        private String meter_id;
        private String newName;

        @Override
        protected Void doInBackground(String... strings) {
            url="http://appapi.wateron.in/v2.0/meter/"+strings[2]+"/renameto/"+strings[3];
            Log.d("data getting",url);
            try {
                URL object=new URL(url);
                Truster.trustEveryone();
                token = strings[1];
                xmsin = strings[0];
                meter_id = strings[2];
                newName = strings[3];
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setRequestProperty("X-AUTH-TOKEN","Bearer "+strings[1] );
                con.setRequestProperty("X-MSIN",strings[0] );
                con.setRequestMethod("POST");

                StringBuilder sb = new StringBuilder();
               HttpResult = con.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    response = sb.toString();
                    System.out.println("data NameChanger" + strings[0]);
                    System.out.println("data" + sb.toString());
                } else {

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getErrorStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    response = sb.toString();
                    System.out.println(response);
                    System.out.println("data NameChanger" + strings[0]);
                    response = "";
                    Log.d("res",String.valueOf(HttpResult));
                    System.out.println(con.getResponseMessage());
                    Log.e("error"+String.valueOf(con.getResponseCode()),con.getResponseMessage());
                }


            }catch ( IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(response!= null && !response.isEmpty()){
                try {
                    JSONObject object=new JSONObject(response);
                    if(object.has("response")){
                        handlerInterface.nameChanged();
                    }else {
                        handlerInterface.errorChangingName(response, HttpResult,url,xmsin,token,meter_id,newName);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    handlerInterface.errorChangingName(response,HttpResult, url, xmsin, url, meter_id, newName);
                }

            }else {
                handlerInterface.errorChangingName(response, HttpResult, url, xmsin, url, meter_id, newName);
            }
        }

    }
}
