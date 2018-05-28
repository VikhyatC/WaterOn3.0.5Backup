package com.wateron.smartrhomes.util;

import android.os.AsyncTask;
import android.util.Log;


import com.wateron.smartrhomes.models.Alert;
import com.wateron.smartrhomes.models.Apartment;
import com.wateron.smartrhomes.models.DailyData;
import com.wateron.smartrhomes.models.Meter;
import com.wateron.smartrhomes.models.Slabs;
import com.wateron.smartrhomes.models.TwoHourForMeter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Paranjay on 06-12-2017.
 */

public class DashboardHelper {
    static TwoHourForMeter tm;
    private static DashboardHandlerInterface handlerInterface;
    private static TokenSessionHandler tokenSessionHandler;
    private static boolean dataPresent = false;
    public static boolean dataLatest = false;
    static List<TwoHourForMeter> twoHourForMeters= new ArrayList<>();
    static List<Alert> alertList;

    public static void getDashboardData(String mobile, String isd, String token, DashboardHandlerInterface handlerInterface) {
        DashboardHelper.handlerInterface = handlerInterface;
        Log.d("DahsboardAPItoken", token);
        if (DataValidator.checkIfDataPresent(handlerInterface.getInstance(), "dashboard")) {
            if (DataValidator.checkIfHardReloadNeeded(handlerInterface.getInstance())) {
                dataPresent = true;
                dataLatest = false;
                Log.d("HardReload", "detected");
            } else if (DataValidator.checkIfInWaitTime(handlerInterface.getInstance(), "dashboard")) {
                Log.d("WaitTime", "detected");
                dataPresent = true;
                dataLatest = true;
            } else {
                dataPresent = true;
                dataLatest = false;
                Log.d("DashboardHelper", "NonReloadWait");
            }
        } else {
            Log.d("DashboardHelper", "NoPacket");
            dataPresent = false;
            dataLatest = false;
        }
        loadFromServer("(" + isd + ")" + mobile, token);
    }

    private static void loadFromServer(String number, String token) {
        DashboardFetcher fetcher = new DashboardFetcher();
        fetcher.execute(number, token);
    }

    public static void PreloadDashboardData(String mobile, String isd, String token, DashboardHandlerInterface handlerInterface) {
        DashboardHelper.handlerInterface = handlerInterface;
        loadFromServer("(" + isd + ")" + mobile, token);
    }

    public static void RefreshDashboardData(String mobile, String isd, int apt_id, String token, DashboardHandlerInterface dashboardHandlerInterface) {
        DashboardHelper.handlerInterface = dashboardHandlerInterface;
        RefresherTask refresher = new RefresherTask();
        dataLatest = true;
        Log.d("X-MSIN","(" + isd + ")" + mobile);
        Log.d("AptIDRefresh", String.valueOf(apt_id));

        refresher.execute("(" + isd + ")" + mobile, String.valueOf(apt_id), token);
    }

    public static void getPreviouslyUsedToken(String isd, String mobile,TokenSessionHandler tokenSessionHandler) {
        DashboardHelper.tokenSessionHandler = tokenSessionHandler;
        TokenRetriever tokenRetriever = new TokenRetriever();
        tokenRetriever.execute("(" + isd + ")" + mobile);

    }

    public static class DashboardFetcher extends AsyncTask<String, Void, Void> {

        String response;
        private Meter meter;
        int HttpResult;
        private String responseError;
        String url;
        String xmsin;
        private String token;

        @Override
        protected Void doInBackground(String... strings) {

            url = "https://appapi.wateron.in/v2.0/dashboard/";
            Log.d("data getting", url);
            try {
                xmsin = strings[0];
                token = strings[1];
                URL object = new URL(url);
                Truster.trustEveryone();
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setRequestProperty("X-AUTH-TOKEN", "Bearer " + strings[1]);
                con.setRequestProperty("X-MSIN", strings[0]);
                con.setRequestMethod("GET");

                StringBuilder sb = new StringBuilder();
                HttpResult = con.getResponseCode();
                Log.d("HTTPRESULT", String.valueOf(HttpResult));
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    response = sb.toString();
//                    Log.d("Response length", String.valueOf(response.length()));
//                    longInfo(response);
                    System.out.println("data" + sb.toString());
                } else {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getErrorStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    responseError = sb.toString();
                    System.out.println(con.getResponseMessage());
                    Log.e("error" + String.valueOf(con.getResponseCode()), con.getResponseMessage());
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void longInfo(String response) {
            if (response.length() > 4000) {
                Log.i("response>4000", response.substring(0, 4000));
                longInfo(response.substring(4000));
            } else {
                Log.i("response<4000", response);
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            JSONObject error_json = null;
//            Log.d("Response Dashbpard",response);
            if (response != null && !response.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                boolean finished = false;
                List<Apartment> apartmentList = new ArrayList<>();
                List<Slabs> slabsList = new ArrayList<>();
//                DashboardHelper.twoHourForMeters =
                DashboardHelper.alertList = new ArrayList<>();
                List<DailyData> dailyDataList = new ArrayList<>();
                List<Meter> meterList = new ArrayList<>();
//
                try {
                    JSONArray apartments = new JSONArray(response);
                    Log.d("ApartmentsLength", String.valueOf(apartments.length()));
//                    JSONArray apartments = main.getJSONArray("apartments");
                    for (int i = 0; i < apartments.length(); i++) {
                        JSONObject apart = apartments.getJSONObject(i);
                        Apartment apartment = new Apartment();
                        apartment.setId(apart.getInt("id"));
                        apartment.setName(apart.getString("name"));
                        apartment.setSociety(apart.getString("society"));
                        apartment.setFixedcharge(apart.getDouble("fixed_charge"));
                        JSONObject bill = apart.getJSONObject("bill");
                        apartment.setBillAmount(bill.getDouble("amount"));
                        apartment.setBillDate(bill.getString("date"));
                        apartment.setBillPaid(bill.getString("paid"));
                        JSONObject commodity = apart.getJSONObject("commodity");
                        apartment.setUnitText(commodity.getString("unit_text"));
                        apartment.setUnitAbbrev(commodity.getString("unit_abbrev"));
                        apartment.setCurrencyText(commodity.getString("currency_text"));
                        apartment.setCurrencyAbbrev(commodity.getString("currency_abbrev"));
                        apartment.setUnitSymbol(commodity.getString("unit_symbol"));
                        apartment.setCurrencySymbol(commodity.getString("currency_symbol"));
                        JSONObject slabsdetails = apart.getJSONObject("bill_slabs");
                        JSONArray slabval = slabsdetails.getJSONArray("slabs");
                        JSONArray slabrates = slabsdetails.getJSONArray("rates");
                        apartmentList.add(apartment);
                        for (int j = 0; j < slabrates.length(); j++) {
                            Slabs slabs = new Slabs();
                            slabs.setApTid(apartment.getId());
//                            if(j+1==slabrates.length()){
//                                slabs.setSlabKl(-1);
//                            }else{
                            slabs.setSlabKl(slabval.getDouble(j));
//                            }
                            slabs.setSlabPrice(slabrates.getDouble(j));
                            slabs.setSlabLevel(j);
                            slabsList.add(slabs);
                        }
                        JSONArray meters = apart.getJSONArray("meters");
                        for (int j = 0; j < meters.length(); j++) {
                            JSONObject meterjson = meters.getJSONObject(j);
                            meter = new Meter();
                            meter.setAptId(apartment.getId());
                            meter.setApartment_index(i);
                            meter.setId(meterjson.getInt("id"));
                            meter.setLocationUser(meterjson.getString("location_user"));
                            meter.setLocationDefault(meterjson.getString("location_default"));
                            meter.setValveCurrentStatus(meterjson.getString("valve_current_status"));
                            meter.setValveLastOperated(meterjson.getString("valve_last_operated"));
                            meter.setHasValve(meterjson.getInt("has_valve"));
                            meterList.add(meter);
                            JSONObject alerts = meterjson.getJSONObject("active_alert");
                            Alert alert = new Alert();
                            alert.setAptId(apartment.getId());
                            alert.setMeterId(meter.getId());
                            alert.setQty(alerts.getDouble("quantity"));
                            alert.setTime(alerts.getString("time"));
                            alertList.add(alert);

                            JSONArray twoToday = meterjson.getJSONArray("two_hourly_consumption_today");
                            JSONArray twoyester = meterjson.getJSONArray("two_hourly_consumption_yesterday");

                            for (int k = 0; k < twoToday.length(); k++) {
                                tm = new TwoHourForMeter();
                                tm.setAptId(apartment.getId());
                                tm.setDate((sdf.format(new Date())));
                                tm.setMeterId(meter.getId());
                                tm.setSlot(k);
                                tm.setValue(twoToday.getDouble(k));
                                DashboardHelper.twoHourForMeters.add(tm);
                                TwoHourForMeter tm1 = new TwoHourForMeter();
                                tm1.setAptId(apartment.getId());
                                tm1.setDate(sdf.format((new Date(new Date().getTime() - (24 * 60 * 60 * 1000)))));
                                tm1.setMeterId(meter.getId());
                                tm1.setSlot(k);
                                tm1.setValue(twoyester.getDouble(k));
                                DashboardHelper.twoHourForMeters.add(tm1);
//                                new DataHelper(handlerInterface.getInstance()).BackupTwoHourlyData(DashboardHelper.twoHourForMeters);
                            }
                            JSONObject dailyConsumption = meterjson.getJSONObject("daily_consumption");

                            long timec = new Date().getTime();
                            long day = 86400000;
                            for (int k = 0; k < dailyConsumption.length(); k++) {
                                DailyData dailyData = new DailyData();
                                dailyData.setMeterId(meter.getId());
                                long timetoadd = timec;
                                for (int l = 0; l < k; l++) {
                                    timetoadd = timetoadd - day;
                                }
                                dailyData.setDate((sdf.format(new Date(timetoadd))));
                                dailyData.setAptId(apartment.getId());
                                try {
                                    dailyData.setValue(dailyConsumption.getDouble(sdf.format(new Date(timetoadd))));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                dailyDataList.add(dailyData);
                            }
                        }
                    }
                    finished = true;
                    dataLatest = true;
                    dataPresent = true;
//                    Log.d("Stroing Values", "Dashboard");
//                    Log.d("TwoHourMeterslist", String.valueOf(DashboardHelper.twoHourForMeters));
                    new DataHelper(handlerInterface.getInstance()).storeDashboard(apartmentList, dailyDataList, slabsList, DashboardHelper.twoHourForMeters, meterList, alertList);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (finished) {
//                        Log.d("OnPostExecute", "Anonymous");
//                        Log.d("TwoHourMeterslist", String.valueOf(DashboardHelper.twoHourForMeters));
                        new DataHelper(handlerInterface.getInstance()).storeDashboard(apartmentList, dailyDataList, slabsList, DashboardHelper.twoHourForMeters, meterList, alertList);
                        DataValidator.setHardReload(handlerInterface.getInstance(), false);
                        DataValidator.updateDataFlag(handlerInterface.getInstance(), "dashboard", new Date().getTime());
                        handlerInterface.loadData(true);
                    } else {
                        if (dataPresent) {
                            //new DataHelper(handlerInterface.getInstance()).storeDashboard(apartmentList,dailyDataList,slabsList,twoHourForMeterList,meterList,alertList);
//                            Log.d("TwoHourMeterslist", String.valueOf(DashboardHelper.twoHourForMeters));
//                            Log.d("OnPostExecute", "Data is present");
                            Log.d("lOOADING", "should not happen: ");
                            handlerInterface.loadData(dataLatest);
                        } else {
                            try {
                                error_json = new JSONObject(responseError);
                                if (HttpResult==400){
                                    String response = error_json.getString("reason");
                                    handlerInterface.errorGettingData(response,HttpResult,url,xmsin,token);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Log.d("OnPostExecute", "Data is not present");

                        }
                    }
                }
            } else {
                    if (HttpResult==400){
                        try {
                            error_json = new JSONObject(responseError);
                            String response = error_json.getString("reason");
                            handlerInterface.errorGettingData(response, HttpResult, url, xmsin, token);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }

            }
        }
    }

    private static class RefresherTask extends AsyncTask<String, Void, Void> {
        String response;
        int HttpResult;
        List<TwoHourForMeter> twoHourForMeterList = new ArrayList<>();
        List<Alert> alertList = new ArrayList<>();
        private String responseError;
        String url;
        private String xmsin;
        private String token;


        @Override
        protected Void doInBackground(String... strings) {
            url = "https://appapi.wateron.in/v2.0/cons/hourly/" + strings[1];
            Log.d("dataRefresh getting", url);
            try {
                xmsin = strings[0];
                token = strings[2];
                URL object = new URL(url);
                Truster.trustEveryone();
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setRequestProperty("X-AUTH-TOKEN", "Bearer " + strings[2]);
                con.setRequestProperty("X-MSIN", strings[0]);
                con.setRequestMethod("GET");
                Log.d("TwoHourlySize", String.valueOf(DashboardHelper.twoHourForMeters.size()));
                if(DashboardHelper.twoHourForMeters.size()<1){
                    DashboardHelper.twoHourForMeters = new DataHelper(handlerInterface.getInstance()).getTwoHourlyData(strings[1]);
                }
                StringBuilder sb = new StringBuilder();
               HttpResult = con.getResponseCode();
                Log.d("Refresh resultCode", String.valueOf(HttpResult));
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    response = sb.toString();
                    System.out.println("dataRefreshed :" + response);
                } else {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getErrorStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    responseError = sb.toString();
                    handlerInterface.errorGettingData(responseError, HttpResult,url,xmsin,token);
                    System.out.println(con.getResponseMessage());
                    Log.e("error" + String.valueOf(con.getResponseCode()), con.getResponseMessage());
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void  onPostExecute(Void aVoid) {
            HashMap<Integer, ArrayList<Double>> meter_values = new HashMap<>();
            try {
                 double total_today = 0;
                 double total_yesterday = 0;
                 if((response!=null)&&(!response.isEmpty())){


                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    JSONArray freshData = new JSONArray(response);
                    for (int i = 0; i < freshData.length(); i++) {
                        JSONObject fresh = freshData.getJSONObject(i);
                        int meter_id = fresh.getInt("id");
                        JSONObject alerts = fresh.getJSONObject("active_alert");
                        JSONArray twoToday = fresh.getJSONArray("two_hourly_consumption_today");
                        JSONArray twoyester = fresh.getJSONArray("two_hourly_consumption_yesterday");
                        Log.d("ConsumptionToday", String.valueOf(twoToday));
//                    for (int j=0 ; j < DashboardHelper.alertList.size();j++){
//                        Alert alert = new Alert();
//                        alert.setAptId(DashboardHelper.alertList.get(j).getAptId());
//                        alert.setMeterId(DashboardHelper.alertList.get(j).getMeterId());
//                        alert.setQty(alerts.getDouble("quantity"));
//                        alert.setTime(alerts.getString("time"));
//                        alertList.add(alert);
//                    }

                        int countToday = 0;
                        int countYesterday = 0;

                        for (int k = 0; k < twoToday.length() * 2; k++) {
                            TwoHourForMeter tm = new TwoHourForMeter();
                            Log.d("Index", String.valueOf(k));
                            tm.setAptId(DashboardHelper.twoHourForMeters.get(k).getAptId());
                            tm.setDate(DashboardHelper.twoHourForMeters.get(k).getDate());
                            tm.setMeterId(meter_id);
                            tm.setSlot(DashboardHelper.twoHourForMeters.get(k).getSlot());
                            if (k % 2 == 0) {
                                ArrayList<Double> hourly_value_today = new ArrayList<>();
                                tm.setValue(twoToday.getDouble(countToday));
                                hourly_value_today.add(twoToday.getDouble(countToday));
                                meter_values.put(meter_id,hourly_value_today);
                                total_today += twoToday.getDouble(countToday);
                                countToday++;
                            } else {
                                ArrayList<Double> hourly_value_yesterday = new ArrayList<>();
                                tm.setValue(twoyester.getDouble(countYesterday));
                                hourly_value_yesterday.add(twoyester.getDouble(countYesterday));
                                meter_values.put(meter_id,hourly_value_yesterday);
                                total_yesterday += twoyester.getDouble(countYesterday);
                                countYesterday++;
                            }
                            this.twoHourForMeterList.add(tm);
                        }
                    }
                    new DataHelper(handlerInterface.getInstance()).updateDashboard(this.twoHourForMeterList);

                    //                new DataHelper(handlerInterface.getInstance()).storeAlerts(alertList);
                    handlerInterface.loadData(true);
                    handlerInterface.updateDailyConsumption(total_today,total_yesterday,meter_values);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

    private static class TokenRetriever extends AsyncTask<String, Void, Void> {
        private String response;

        @Override
        protected Void doInBackground(String... strings) {
            String url = "https://appapi.wateron.in/v2.0/auth/";
            try {
                Log.d("data getting",url);
                URL object = new URL(url);
                Truster.trustEveryone();
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                Log.d("X-MSIN",strings[0]);
                con.setRequestProperty("X-MSIN", strings[0]);
                con.setRequestProperty("X-AUTH-TOKEN", "Bearer req4auth");
                con.setRequestMethod("GET");

                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();

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
                    response = "";
                    System.out.println("Message"+con.getResponseMessage());
                    Log.e("error" + String.valueOf(con.getResponseCode()), con.getResponseMessage());
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if (response!=null){
                    Log.d("Response",response);
                    JSONObject jsonObject = new JSONObject(response);
                    String token = jsonObject.getString("auth-token");
                    tokenSessionHandler.storeToken(token);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}