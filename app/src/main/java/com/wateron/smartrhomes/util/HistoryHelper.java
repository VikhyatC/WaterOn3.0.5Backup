package com.wateron.smartrhomes.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.wateron.smartrhomes.models.DailyData;
import com.wateron.smartrhomes.models.HAlert;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by Paranjay on 12-12-2017.
 */

public class HistoryHelper {

    private static HistoryHandlerInterface handlerInterface;
    private static boolean dataPresent = false;
    public static boolean dataLatest = false;
    public static void getHistoryData(String mobile, String isd, String token, HistoryHandlerInterface handlerInterface, String aptid){

        HistoryHelper.handlerInterface = handlerInterface;

        if(DataValidator.checkIfDataPresent(handlerInterface.getInstance(),"history")){
            if(DataValidator.checkIfHardReloadNeeded(handlerInterface.getInstance())){
                dataPresent = true;
                dataLatest = false;
                Log.d("HardReload","detected");
            }else if(DataValidator.checkIfInWaitTime(handlerInterface.getInstance(),"history")){
                Log.d("Detected ","In Wait Time");
                    dataPresent = true;
            }else{
                Log.d("Detected ","Not In Wait Time");
                dataPresent = true;
                dataLatest = false;
            }

        }else {
            Log.d("Detected ","Data Not Present");
            dataPresent = false;
            dataLatest = false;
        }
        loadFromServer("("+isd+")"+mobile,token,aptid);
    }

    private static void loadFromServer(String number, String token,String aptid) {
        HistoryHelper.HistoryFetcher fetcher = new HistoryFetcher();
        fetcher.execute(number,token,aptid);
    }

    public static class HistoryFetcher extends AsyncTask<String,Void,Void>{
        String response;
        int aptid;
        int HttpResult;
        String url;
        private String xmsin;
        private String token;

        @Override
        protected Void doInBackground(String... strings) {
            url="http://appapi.wateron.in/v2.0/cons/history/"+strings[2];
            Log.d("data getting",url);
            if(dataLatest){
                return null;
            }
            aptid = Integer.parseInt(strings[2]);
            try {
                URL object=new URL(url);
                Truster.trustEveryone();
                xmsin = strings[0];
                token = strings[1];
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setRequestProperty("X-AUTH-TOKEN","Bearer "+strings[1] );
                con.setRequestProperty("X-MSIN",strings[0] );
                con.setRequestMethod("GET");

                StringBuilder sb = new StringBuilder();
                HttpResult = con.getResponseCode();
                Log.d("HistoryHttp_result", String.valueOf(HttpResult));
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
                    handlerInterface.errorGettingData(response, HttpResult,url,xmsin,token);
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
//                Log.d("Response","NotNull");
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                boolean finished = false;
                List<DailyData> dailyDataList = new ArrayList<>();
                List<HAlert> alertList = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject dailyConsumption = object.getJSONObject("consumption_history");
                    JSONObject alerts = object.getJSONObject("alerts_history");
                    long timec=new Date().getTime();
                    long day=86400000;
                    for(int k=0;k<dailyConsumption.length();k++){
                        DailyData dailyData=new DailyData();
                        HAlert alert = new HAlert();
                        long timetoadd=timec;
                        for(int l=0;l<k;l++){
                            timetoadd=timetoadd-day;
                        }
                        dailyData.setDate((sdf.format(new Date(timetoadd))));
                        alert.setDate(dailyData.getDate());
                        dailyData.setAptId(aptid);
                        alert.setAptId(aptid);
                        try {
                            dailyData.setValue(dailyConsumption.getDouble(sdf.format(new Date(timetoadd))));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        try{
                            alert.setValue(alerts.getInt(alert.getDate()));
                        }catch (Exception e){
                            e.printStackTrace();
                            alert.setValue(0);
                        }
                        alertList.add(alert);
                        dailyDataList.add(dailyData);
                    }
                    finished = true;
                    dataLatest = true;
                    dataPresent = true;
                    new DataHelper(handlerInterface.getInstance()).storeHistory(dailyDataList,alertList);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if(finished){
                        Context context = handlerInterface.getInstance();
                        Log.d("Response","NotNullFinished");
                        new DataHelper(handlerInterface.getInstance());
                        DataValidator.updateDataFlag(context,"history",new Date().getTime());
                        Log.d("Latest Data?", String.valueOf(dataLatest));
                        handlerInterface.loadData(false);
                        handlerInterface.RecheckData();
                    }else {
                        if(dataPresent){
                            Log.d("Response","NotNullNOTFINISHED");
                            Log.d("Latest Data?", String.valueOf(dataLatest));
                            handlerInterface.loadData(dataLatest);
                        }else {
                            Log.d("Latest Data?", String.valueOf(dataLatest));
                            Log.d("Response","NotNullERROR");
                            handlerInterface.errorGettingData(response,HttpResult, url, xmsin, token);
                        }
                    }
                }
            }else{
                if(dataPresent){
                    Log.d("Latest Data?", String.valueOf(dataLatest));
                    Log.d("Response","NullLoad");
                    handlerInterface.loadData(dataLatest);

                }else {
                    Log.d("Latest Data?", String.valueOf(dataLatest));
                    handlerInterface.errorGettingData(response, HttpResult, url, xmsin, token);
                }
            }
        }
    }
}
