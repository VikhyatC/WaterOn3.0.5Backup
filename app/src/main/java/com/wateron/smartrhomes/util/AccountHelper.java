package com.wateron.smartrhomes.util;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Paranjay on 15-12-2017.
 */

public class AccountHelper {
    private static List<String> numbers;
    private static AccountHandlerInterface handlerInterface;
    public static void deleteFamily(String mobile, String isd, String token, String member_number, AccountHandlerInterface handlerInterface, String member_isd, int aptID, int pos) {
        AccountHelper.handlerInterface = handlerInterface;
        FamilyDestroyer familyDestroyer = new FamilyDestroyer();
        familyDestroyer.execute("("+isd+")"+mobile,token,member_number,member_isd, String.valueOf(aptID), String.valueOf(pos));
    }

    public static void addFamilyMember(String mobile, String isd, String token, AccountHandlerInterface handlerInterface, String member_mobile, String member_isd) {
        AccountHelper.handlerInterface = handlerInterface;
        FamilyAdder familyAdder = new FamilyAdder();
        Log.d("Mobile/ISD?",mobile);
       // int member_mobile_number=AccountHelper.numbers.get(0);
       // Log.d("Mem_mob_no", String.valueOf(member_mobile_number));
        familyAdder.execute(mobile,isd,token,member_mobile,member_isd);
    }

    public static void changeFamilyMember(String mobile,String isd,String token,String main, String s, AccountHandlerInterface handlerInterface) {
        AccountHelper.handlerInterface = handlerInterface;
    }

    public static void loadFamily(String s, String s1, String token, AccountHandlerInterface handlerInterface, int selectedAptId) {
        AccountHelper.handlerInterface = handlerInterface;
        FamilyFetcher fetcher=new FamilyFetcher();
        fetcher.execute("("+s1+")"+s,token,String.valueOf(selectedAptId));
    }

    private static class FamilyFetcher extends AsyncTask<String,Void,Void>{
        int aptid;
        String response;
        int HttpResult;
        String url;
        String xmsin;
        String token;
        @Override
        protected Void doInBackground(String... strings) {
            url="http://appapi.wateron.in/v2.0/account/member";
            Log.d("data getting",url);
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
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getErrorStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    response = sb.toString();
                    System.out.println(response);
                    System.out.println(strings[0]);
                }


            }catch ( IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(response!=null && !response.isEmpty()){
                List<String> numbers = new ArrayList<>();
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray membs= object.getJSONArray("members");
                    for(int i=0;i<membs.length();i++){
                        if(membs.getJSONObject(i).getInt("apartment_id")==aptid){

                            JSONArray nums=membs.getJSONObject(i).getJSONArray("member_mobile_num");
                            for(int j=0;j<nums.length();j++){
                                numbers.add(nums.getString(j));
                                AccountHelper.numbers = numbers;
                                Log.d("numbers", String.valueOf(numbers));
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(numbers.size()==0){
                    handlerInterface.errorLoadingMembers(response,HttpResult,url,xmsin,token);
                }else {
                    handlerInterface.loadData(numbers);
                }
            }else {
                handlerInterface.errorLoadingMembers(response, HttpResult,url,xmsin,token);
            }
        }
    }

    private static class FamilyAdder extends AsyncTask<String,Void,Void> {

        private String response;
        String url;
        int HttpResult;
        String xmsin;
        String token;
        @Override
        protected void onPostExecute(Void aVoid) {
//            handlerInterface.loadAddedData();
            try {
                Log.d("AddMemberResponse",response);
                JSONObject response_json = new JSONObject(response);
                String new_member = response_json.getString("memberMobileNumber");
                handlerInterface.loadAddedData(new_member);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            url = "http://appapi.wateron.in/v2.0/account/member";
//             object= null;
            URL object= null;
            try {
                xmsin = "("+strings[1]+")"+strings[0];
                token = strings[2];
                object = new URL(url);
                Truster.trustEveryone();
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                String t= FirebaseInstanceId.getInstance().getToken();
                con.setRequestProperty("X-AUTH-TOKEN","Bearer "+strings[2]);
                con.setRequestProperty("X-MSIN","("+strings[1]+")"+strings[0] );
                con.setRequestProperty("Content-type","application/json");
                con.setRequestMethod("POST");
                JSONObject da=new JSONObject();
                da.put("member_country_code",Integer.parseInt(strings[4]));
                da.put("member_mobile_num", Long.parseLong(strings[3]));
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(da.toString());
                wr.flush();
                StringBuilder sb = new StringBuilder();
                HttpResult = con.getResponseCode();
                Log.d("HttpResultFamilyAdd", String.valueOf(HttpResult));
                if (HttpResult == HttpsURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
//                    parseData(sb.toString());
                    response = sb.toString();
                    System.out.println("data" + sb.toString());
                } else {
                    response = "";
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getErrorStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    response = sb.toString();
                    handlerInterface.errorLoadingMembers(response,HttpResult,url,xmsin,token);

                }

            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (ProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (NumberFormatException e){
                e.printStackTrace();
            }

            return null;
        }

    }


    private static class FamilyDestroyer extends AsyncTask<String,Void,Void> {
        private String response;
        private String url;
        private int HttpResult;
        private String xmsin;
        private String token;
        private int member_ccode;
        private String member_mobile;
        private long apt_id;

        @Override
        protected Void doInBackground(String... strings) {
            url = "http://appapi.wateron.in/v2.0/account/member";
            URL object= null;
            try {
                object = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Truster.trustEveryone();
            HttpURLConnection con = null;
            try {
                if (object != null) {
                    con = (HttpURLConnection) object.openConnection();
                    con.setReadTimeout(15000);
                    con.setConnectTimeout(15000 /* milliseconds */);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            xmsin = strings[0];
            token = strings[1];
            member_ccode = Integer.parseInt(strings[3]);
            member_mobile = strings[2];
            apt_id = Long.parseLong(strings[4]);
            Log.d("MSIN",strings[0]);
            if (con != null) {
                con.setRequestProperty("X-AUTH-TOKEN","Bearer "+strings[1] );
                con.setRequestProperty("X-MSIN",strings[0] );
            }
            try {
                if (con != null) {
                    con.setRequestMethod("DELETE");
//                    con.setDoInput(true);
//                    con.setDoOutput(true);
                }
                StringBuilder sb = new StringBuilder();
                JSONObject data = new JSONObject();
                try {
                    try{
                        data.put("member_country_code", Integer.parseInt(strings[3]));
                        data.put("member_mobile_num",strings[2]);
                        data.put("apartment_id", Long.parseLong(strings[4]));
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("JsonBody"+data);
                String body_params = String.valueOf(data);
                if (con != null) {
                    con.setChunkedStreamingMode(0);
                }
                OutputStream os = null;
                if (con != null) {
                    os = con.getOutputStream();
                }
                BufferedWriter writer = null;
                if (os != null) {
                    writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                }
                if (writer != null) {
                    writer.write(body_params);
                }
                if (writer != null) {
                    writer.flush();
                }
                if (writer != null) {
                    writer.close();
                }
                if (os != null) {
                    os.close();
                }

                HttpResult = 0;
                if (con != null) {
                    HttpResult = con.getResponseCode();
                }
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();
                    response = sb.toString();
                    System.out.println("AccountHelperdata" + sb.toString());
                } else {
                    response = "";
                    BufferedReader br = null;
                    if (con != null) {
                        br = new BufferedReader(
                                new InputStreamReader(con.getErrorStream(), "utf-8"));
                    }
                    String line = null;
                    if (br != null) {
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                    }
                    if (br != null) {
                        br.close();
                    }
                    response = sb.toString();
                    handlerInterface.errorLoadingDeletedMembers(response,HttpResult,"REQUEST_URL:"+url,"X_MSIN:"+xmsin,"Token :"+token,member_ccode,member_mobile,apt_id,strings[5]);
                    System.out.println(response);
                    response = "";
                    System.out.println(strings[0]);
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if (HttpResult==200){
                    JSONObject data = new JSONObject(response);
                    Long member_number = data.getLong("member_mobile_num");
                    handlerInterface.loadData(member_number);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
