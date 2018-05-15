package com.wateron.smartrhomes.util;

import android.os.AsyncTask;
import android.util.Log;

import com.wateron.smartrhomes.models.Alert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Paranjay on 14-12-2017.
 */

public class AlertHelper {

    private static AlertHandlerInterface handlerInterface;
    private static boolean dataPresent = false;
    private static boolean dataLatest = false;
    public static void getAlertData(String mobile,String isd,String token,AlertHandlerInterface handlerInterface,String aptid){
        AlertHelper.handlerInterface = handlerInterface;
        if(DataValidator.checkIfDataPresent(handlerInterface.getInstance(),"alert")){
            if(DataValidator.checkIfInWaitTime(handlerInterface.getInstance(),"alert")){
                dataPresent = true;
                dataLatest = true;
            }else{
                dataPresent = true;
                dataLatest = false;

            }
        }else {
            dataPresent = false;
            dataLatest = false;
        }
        loadFromServer("("+isd+")"+mobile,token,aptid);
    }

    private static void loadFromServer(String number, String token, String aptid) {
        AlertHelper.AlertFetcher fetcher = new AlertHelper.AlertFetcher();
        fetcher.execute(number,token,aptid);
    }

    public static class AlertFetcher extends AsyncTask<String,Void,Void>{
        String response;
        int aptid;
        int HttpResult;
        String url;
        private String xmsin;
        private String token;

        @Override
        protected Void doInBackground(String... strings) {
            url="http://appapi.wateron.in/v2.0/alerts/active/"+strings[2];
            Log.d("data getting",url);
            if(dataLatest){
                return null;
            }
            aptid = Integer.parseInt(strings[2]);
            try {
                URL object=new URL(url);
                xmsin = strings[0];
                token = strings[1];
                Truster.trustEveryone();
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setRequestProperty("X-AUTH-TOKEN","Bearer "+strings[1] );
                con.setRequestProperty("X-MSIN",strings[0] );
                con.setRequestMethod("GET");

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
                    handlerInterface.errorGettingData(response,HttpResult,url,xmsin,token);
                    Log.e("error"+String.valueOf(con.getResponseCode()),con.getResponseMessage());
                }


            }catch ( IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            List<Alert> alertList = new ArrayList<>();
            if(response!= null && !response.isEmpty()){
                boolean finished = false;

                try {
//                    JSONObject object = new JSONObject(response);
                    JSONArray alerts = new JSONArray(response);
                    for(int i=0;i<alerts.length();i++){
                        Alert alert=new Alert();
                        JSONObject alertobj=alerts.getJSONObject(i);
                        alert.setAptId(aptid);
                        alert.setMeterId(alertobj.getJSONObject("meter").getInt("id"));
                        alert.setQty(alertobj.getDouble("quantity"));
                        alert.setTime(alertobj.getString("time"));
                        alertList.add(alert);
                        System.out.print(alert.toString());
                    }
                    finished = true;
                    dataLatest = true;
                    dataPresent = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if(finished){
                        Log.d("AlertData","Finished");
                        new DataHelper(handlerInterface.getInstance()).storeAlerts(alertList);
                        DataValidator.updateDataFlag(handlerInterface.getInstance(),"alert",new Date().getTime());
                        handlerInterface.loadData(true);
                    }else {
                        if(dataPresent){
                            Log.d("AlertData","NotFinished");
                            handlerInterface.loadData(dataLatest);
                        }else {
                            handlerInterface.errorGettingData(response,HttpResult,url,xmsin,token);
                        }
                    }
                }
            }else{
                if(dataPresent){
                    Log.d("AlertData","offline");
                    new DataHelper(handlerInterface.getInstance()).storeAlerts(alertList);
                    handlerInterface.loadData(dataLatest);

                }else {
                    Log.d("AlertData","Error in  Response");
                    handlerInterface.errorGettingData(response, HttpResult, url, xmsin, token);
                }
            }
        }
    }
}
