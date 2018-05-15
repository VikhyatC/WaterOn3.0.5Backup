package com.wateron.smartrhomes.util;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikhyat on 14/5/18.
 */

public class CrashHelper {

    public static void SendCrashMailer(String mobile,String version,String errorType,String errorLog,String time,String device){
        CrashDetector detector = new CrashDetector();
        detector.execute(mobile,version,errorType,errorLog+"\n",time,device);
    }

    private static class CrashDetector extends AsyncTask<String,Void,Void> {
        private String url = "http://nuclious.com/app/ErrorReport.php";
        @Override
        protected Void doInBackground(String... strings) {
            URL object = null;
            try {
                object = new URL(url);
                Truster.trustEveryone();
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("mobileno", strings[0]));
                params.add(new BasicNameValuePair("version", strings[1]));
                params.add(new BasicNameValuePair("errorType", "API -"+strings[2]));
                params.add(new BasicNameValuePair("errorLog",strings[3]));
                params.add(new BasicNameValuePair("time",strings[4]));
                params.add(new BasicNameValuePair("device",strings[5]));


                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));
                writer.flush();
                writer.close();
                os.close();
                con.connect();
                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();
                Log.d("Crash Result", String.valueOf(HttpResult));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private String getQuery(List<NameValuePair> params) {
            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (NameValuePair pair : params)
            {
                if (first)
                    first = false;
                else
                    result.append("&");

                try {
                    result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            return result.toString();
        }
    }
}


