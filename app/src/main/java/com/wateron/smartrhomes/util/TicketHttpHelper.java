package com.wateron.smartrhomes.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import android.util.Base64;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

// Required for JSON handling


public class TicketHttpHelper {
    private static TicketHandlerInteface ticketInterface;
    public static HttpResultHelper httpPost(String urlStr, String user, String password, String data, ArrayList<String[]> headers, int timeOut) throws IOException
    {
        // Set url
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // If secure connection
        if (urlStr.startsWith("https")) {
            try {
                SSLContext sc;
                sc = SSLContext.getInstance("TLS");
                sc.init(null, null, new java.security.SecureRandom());
                ((HttpsURLConnection)conn).setSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                Log.d("debug 1", "Failed to construct SSL object", e);
            }
        }


        // Use this if you need basic authentication
        if ((user != null) && (password != null)) {
            String userPass = user + ":" + password;
            String basicAuth = "Basic " + Base64.encodeToString(userPass.getBytes(), Base64.DEFAULT);
            conn.setRequestProperty("Authorization", basicAuth);
        }

        // Set Timeout and method
        conn.setReadTimeout(timeOut);
        conn.setConnectTimeout(timeOut);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        if (headers != null) {
            for (int i = 0; i < headers.size(); i++) {
                conn.setRequestProperty(headers.get(i)[0], headers.get(i)[1]);
            }
        }

        if (data != null) {
            conn.setFixedLengthStreamingMode(data.getBytes().length);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            os.close();
        }

        InputStream inputStream = null;
        try
        {
            inputStream = conn.getInputStream();
        }
        catch(IOException exception)
        {
            inputStream = conn.getErrorStream();
        }

        HttpResultHelper result = new HttpResultHelper();
        result.setStatusCode(conn.getResponseCode());
        Log.d("Result", String.valueOf(result));
        result.setResponse(inputStream);

        return result;
    }

    public static HttpResponse httpget() {
        HttpResponse response = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI("https://smarterhomes.freshdesk.com/api/v2/ticket_fields"));
            response = client.execute(request);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }
    public static void getUserTicketDetails(String ISD, String mobile, int selectedAptId, String token,TicketHandlerInteface inteface){
        TicketDetailsFetcher fetcher = new TicketDetailsFetcher();
        TicketHttpHelper.ticketInterface = inteface;
        Log.d("AptID", String.valueOf(selectedAptId));
        fetcher.execute("("+ISD+")"+mobile,token,String.valueOf(selectedAptId));
    }
    public static class HttpResultHelper {
            private int statusCode;
            private InputStream response;

            public HttpResultHelper() {
            }

            public int getStatusCode() {
                return statusCode;
            }

            public void setStatusCode(int statusCode) {
                this.statusCode = statusCode;
            }

            public InputStream getResponse() {
                return response;
            }

            public void setResponse(InputStream response) {
                this.response = response;
            }
        }

    private static class TicketDetailsFetcher extends AsyncTask<String,Void,Void> {
        String response;

        @Override
        protected void onPostExecute(Void aVoid) {
            ticketInterface.fetchData(response);
        }

        @Override
        protected Void doInBackground(String... strings) {
            String url="https://appapi.wateron.in/v2.0/account/"+strings[2];

            try {
                URL object = new URL(url);
                Log.d("data getting",url);
                Truster.trustEveryone();
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setRequestProperty("X-AUTH-TOKEN","Bearer "+strings[1] );
                con.setRequestProperty("X-MSIN",strings[0] );
                con.setRequestMethod("GET");
                StringBuilder sb = new StringBuilder();

                int HttpResult = con.getResponseCode();
                Log.d("HTTP_response", String.valueOf(con.getResponseCode()));
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = null;
                    br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    response = sb.toString();
                    System.out.println("Ticket data" + sb.toString());
                } else {
                    response = "";
                    System.out.println(con.getResponseMessage());
                    Log.e("error"+String.valueOf(con.getResponseCode()),con.getResponseMessage());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
