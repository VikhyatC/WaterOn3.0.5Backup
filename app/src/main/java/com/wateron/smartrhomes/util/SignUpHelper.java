package com.wateron.smartrhomes.util;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.wateron.smartrhomes.activities.PreLoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Paranjay on 05-12-2017.
 */

public class SignUpHelper {
    public static PreLoginActivity activity;
    public static void sendOTP (PreLoginActivity activity, String mobile){
        SignUpHelper.activity = activity;
        SendOTPTask sendOTPTask=new SendOTPTask();
        sendOTPTask.execute(mobile);
    }

    public static void verifyOTP(PreLoginActivity activity, String mobileno, String otp) {
        SignUpHelper.activity=activity;
        VerifyOtpTask getDataTask=new VerifyOtpTask();
        getDataTask.execute(mobileno,otp);
    }

    public static void storePassword(PreLoginActivity activity,String mobileNo, String pw) {
        SignUpHelper.activity= activity;
        SavePasswordTask getDataTask=new SavePasswordTask();
//        String passwordtoStore= URLEncoder.encode(pw);
        Log.d("Mobile Number",mobileNo);
        getDataTask.execute(mobileNo,pw);
    }

    public static void signInUser(PreLoginActivity activity, String mobile, String pw) {
        SignUpHelper.activity=activity;
            SignInTask getDataTask=new SignInTask();
//        String password= URLEncoder.encode(pw);
        getDataTask.execute(mobile,pw);
    }
    public static class SignInTask extends AsyncTask<String,Void,Void> {
        private String response;
        private int HttpResult;
        String url;
        String base64;
        @Override
        protected void onPostExecute(Void aVoid) {

        }

        @Override
        protected Void doInBackground(String... params) {

             url="https://appapi.wateron.in/v2.0/login/";
            Log.d("data getting",url);

            try {
                URL object=new URL(url);
                Truster.trustEveryone();
                HttpsURLConnection con = (HttpsURLConnection) object.openConnection();
                String t=FirebaseInstanceId.getInstance().getToken();
                Log.d("token",t);
                con.setRequestProperty("X-ANDROID-TOKEN",t );
                Log.d("data getting",t);
                String text=params[0]+":"+params[1];
                byte[] data = text.getBytes(Charset.defaultCharset());
                base64 = Base64.encodeToString(data, Base64.DEFAULT);
                Log.d("data",text);
                Log.d("data getting",base64);
                con.setRequestProperty("Authorization","Basic "+ base64);

                con.setRequestMethod("GET");

                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();
                String message = con.getResponseMessage();
                Log.d("HTTP_RESULT", String.valueOf(HttpResult));
                Log.d("MessageLogin",message);
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    response = sb.toString();
                    parseData(sb.toString());
                    Log.d("MessageLogin",message);
                } else {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getErrorStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    response = sb.toString();
                    System.out.println(con.getResponseMessage());
                    Log.e("error"+String.valueOf(con.getResponseCode()),con.getResponseMessage());
                    try{
                        activity.onSignInFailed(HttpResult,response,"URL:"+url,"FCM_ID:"+FirebaseInstanceId.getInstance().getToken(),"AUTHORIZATION:"+base64);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


            }catch ( IOException e) {
                e.printStackTrace();
                try{
                    activity.onSignInFailed(HttpResult, response, "URL:" + url, "FCM_ID:" + FirebaseInstanceId.getInstance().getToken(),"AUTHORIZATION:"+base64 );
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }


            return null;
        }

        private void parseData(String s) {
            try {
                Log.d("tag",s);
                JSONObject jsonObject=new JSONObject(s);
                String ans=jsonObject.getString("message");
                String authToken = jsonObject.getString("authToken");
                Log.d("ans",ans);
                ans=ans.trim();
                if(ans.equals("Bad Request")){
                    activity.onSignInFailed(HttpResult, response, "URL:" + url, "FCM_ID:" + FirebaseInstanceId.getInstance().getToken(), "AUTHORIZATION:"+base64);
                }else if(ans.equals("Login success")){
                    if(jsonObject.getBoolean("demo")){
                        activity.onSignInSuccesswithDemonumber();
                    }else{
                        activity.onSignInSuccess(authToken);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                activity.onSignInFailed(HttpResult, response, "URL:" + url, "FCM_ID:" + FirebaseInstanceId.getInstance().getToken(), "AUTHORIZATION:"+base64);
            }

        }
    }
    public static class SavePasswordTask extends AsyncTask<String,Void,Void> {
        String url;
        @Override
        protected Void doInBackground(String... params) {
            String response = null;
            int HttpResult = 0;
            url="http://appapi.wateron.in/v2.0/reset/password/"+params[0];
            Log.d("data getting",url);
            try {
                URL object=new URL(url);
                Truster.trustEveryone();
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json");

                String t= FirebaseInstanceId.getInstance().getToken();
                con.setRequestProperty("X-ANDROID-TOKEN",t );
                con.setRequestMethod("POST");

                JSONObject da=new JSONObject();
                try {
                    da.put("pwd",params[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(da.toString());
                wr.flush();

                StringBuilder sb = new StringBuilder();
                HttpResult = con.getResponseCode();
                if (HttpResult == HttpsURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    response = sb.toString();
                    //                    parseData(sb.toString());
                    activity.onStorePasswordSuccess();
                    System.out.println("data" + sb.toString());
                } else {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getErrorStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    response = sb.toString();
                    System.out.println(con.getResponseMessage());
                    try{
                        activity.onStorePasswordFailed(HttpResult,response,"URL:"+url,"FCM_ID:"+FirebaseInstanceId.getInstance().getToken(),"body_param:"+params[1]);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


            }catch ( IOException e) {
                e.printStackTrace();
                try{
                    activity.onStorePasswordFailed(HttpResult, response,"URL:"+url, "FCM_ID:"+FirebaseInstanceId.getInstance().getToken(), "body_param:"+params[1]);
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }


            return null;
        }

//        private void parseData(String s) {
//            try {
//                Log.d("response",s);
//                if(s.trim().equals("[\"SUCCESS\"]")){
//                    activity.onStorePasswordSuccess();
//                }else{
//                    activity.onStorePasswordFailed(HttpResult, response);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                activity.onStorePasswordFailed(HttpResult, response);
//            }
//
//        }
    }

    public static class VerifyOtpTask extends AsyncTask<String,Void,Void> {
        int HttpResult;
        String response;
        String url;
        @Override
        protected Void doInBackground(String... params) {
            url="http://appapi.wateron.in/v2.0/otp/validate/"+params[0]+"/"+params[1];
            Log.d("data getting",url);
            try {
                URL object=new URL(url);
                Truster.trustEveryone();
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                String t= FirebaseInstanceId.getInstance().getToken();
                con.setRequestProperty("X-ANDROID-TOKEN",t );
                con.setRequestMethod("GET");
                Log.d("t",t);

                StringBuilder sb = new StringBuilder();
                HttpResult = con.getResponseCode();
                Log.d("HTTP_RES", String.valueOf(HttpResult));
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    response = sb.toString();
                    parseData(sb.toString());
                    //activity.onOTPVerificationSuccess();
                    System.out.println("data" + sb.toString());
                } else {
                    System.out.println(con.getResponseMessage());
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getErrorStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    response = sb.toString();
                    try{
                        activity.onOTPVerificationFailed(HttpResult, response,"URL:"+url,"FCM_ID:"+FirebaseInstanceId.getInstance().getToken());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


            }catch ( IOException e) {
                e.printStackTrace();
                try{
                    activity.onOTPVerificationFailed(HttpResult, response, "URL :"+url, "FCM_ID:"+FirebaseInstanceId.getInstance().getToken());
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }


            return null;
        }

        private void parseData(String s) {
            try {

                JSONObject jsonObject = new JSONObject(s);
                Log.d("response_code", String.valueOf(jsonObject.getInt("code")));
                if(jsonObject.getInt("code")==200){
                    activity.onOTPVerificationSuccess();
                }else{
                    activity.onOTPVerificationFailed(jsonObject.getInt("code"), response, "URL:"+url, "FCM_ID:"+FirebaseInstanceId.getInstance().getToken());
                }

            } catch (Exception e) {
                e.printStackTrace();
                activity.onOTPVerificationFailed(HttpResult, response, "URL"+url, "FCM_ID:"+FirebaseInstanceId.getInstance().getToken());
            }

        }
    }

    public static class SendOTPTask extends AsyncTask<String,Void,Void> {
        int HttpResult;
        String response;
        String url;
        @Override
        protected Void doInBackground(String... params) {
             url="http://appapi.wateron.in/v2.0/register/mobilenum/"+params[0];
            Log.d("data getting",url);
            try {
                URL object=new URL(url);
                Truster.trustEveryone();
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                String t= FirebaseInstanceId.getInstance().getToken();
                con.setRequestProperty("X-ANDROID-TOKEN",t );
                con.setRequestMethod("GET");
                StringBuilder sb = new StringBuilder();
                HttpResult = con.getResponseCode();
                Log.i("asd",String.valueOf(HttpResult));
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    response = sb.toString();
                    parseData(sb.toString());
                    System.out.println("data" + sb.toString());
                } else {
                    System.out.println(con.getResponseMessage());
                    try{
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(con.getErrorStream(), "utf-8"));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        response = sb.toString();
                        br.close();
                        parseData(sb.toString());
                        activity.onOTPSendFailed(HttpResult,response,"URL:"+url,"FCM_ID:"+FirebaseInstanceId.getInstance().getToken());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


            }catch ( IOException e) {
                e.printStackTrace();
                try{
                    activity.onOTPSendFailed(HttpResult, response, "URL:" + url, "FCM_ID:"+FirebaseInstanceId.getInstance().getToken());
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }


            return null;
        }

        private void parseData(String s){
            try {
                Log.d("anssss",s);
                JSONObject jsonObject=new JSONObject(s);
                if(jsonObject.getInt("code")==200){
                    activity.onOTPSendSuccess();
                }else{
                    activity.onOTPSendFailed(jsonObject.getInt("code"), s, "URL:" + url,"FCM_ID:"+FirebaseInstanceId.getInstance().getId());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
