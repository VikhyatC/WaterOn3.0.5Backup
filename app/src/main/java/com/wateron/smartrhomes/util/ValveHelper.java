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

public class ValveHelper {

    private static ValveHandlerInterface handlerInterface;
    private static int type;
    public static void ignoreAlert(String mobile,String isd,String token,ValveHandlerInterface handlerInterface, int meterId) {
        type=2;
        ValveHelper.handlerInterface=handlerInterface;
        controllValve("("+isd+")"+mobile,token,meterId,"ignore");
    }

    private static void controllValve(String s, String token, int meterId, String type) {
        ValveController controller=new ValveController();
        controller.execute(s,token, String.valueOf(meterId),type);
    }

    public static void closeValve(String mobile,String isd,String token,ValveHandlerInterface handlerInterface, int meterId) {
        type=1;
        ValveHelper.handlerInterface=handlerInterface;
        controllValve("("+isd+")"+mobile,token,meterId,"close");
    }

    public static void openValve(String mobile,String isd,String token,ValveHandlerInterface handlerInterface, int meterId) {
        type=0;
        ValveHelper.handlerInterface=handlerInterface;
        controllValve("("+isd+")"+mobile,token,meterId,"open");
    }

    public static class ValveController extends AsyncTask<String,Void,Void>{
        int code;
        String response="";
        String url;
        private String token;
        private String xmsin;
        private String meter_id;
        private String action;

        @Override
        protected Void doInBackground(String... strings) {
            url="http://appapi.wateron.in/v2.0/meter/"+strings[2]+"/valve/action/"+strings[3];
            Log.d("data getting",url);
            try {
                URL object=new URL(url);
                Truster.trustEveryone();
                token= strings[1];
                xmsin = strings[0];
                meter_id = strings[2];
                action = strings[3];
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setRequestProperty("X-AUTH-TOKEN","Bearer "+strings[1] );
                con.setRequestProperty("X-MSIN",strings[0] );
                con.setRequestMethod("POST");

                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();
                Log.d("HTTP_result0", String.valueOf(HttpResult));
                code = HttpResult;
                if (HttpResult == HttpURLConnection.HTTP_OK) {

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    response = sb.toString();
                    System.out.println("res" + sb.toString());
                } else {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getErrorStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    response = sb.toString();

                    System.out.println("HTTP_Not_OK"+response);
//                    response = "";
                    Log.d("id",strings[0]);
                    Log.d("res",String.valueOf(HttpResult));
//                    System.out.println(con.getResponseMessage());
//                    Log.e("error"+String.valueOf(con.getResponseCode()),con.getResponseMessage());
                }


            }catch ( IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(response.isEmpty()||response==null){
                handlerInterface.actionFailed(type,"Server is Unreacheable", code, url, xmsin, token, meter_id, action);
            }else{
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if(code==400){
                        String response = jsonObject.getString("reason");
                        handlerInterface.actionFailed(type,response,code, url, xmsin, token, meter_id, action);
                    }else if ((code>199)&&(code<300)){
                        try {
                            String response = jsonObject.getString("response");
                            if(jsonObject.has("valveActionTime")){
                                handlerInterface.actionSuccess(type,response);
                            }else{
                                handlerInterface.actionFailed(type,response, code,url,xmsin,token,meter_id,action);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        handlerInterface.actionFailed(type,"Server Under maintaineance", code, url, xmsin, token, meter_id, action);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
