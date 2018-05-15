package com.wateron.smartrhomes.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wateron.smartrhomes.R;
import com.wateron.smartrhomes.util.TicketHttpHelper;
import com.wateron.smartrhomes.util.Truster;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by Vikhyat Chandra on 2/17/2018.
 */

@SuppressLint("ValidFragment")
class AknowledgmentFragment extends android.support.v4.app.Fragment {
    private TextView Line1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aknwoldgement_screen,container,false);
        return  view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Line1=view.findViewById(R.id.congratulations);
        Replier replier =new Replier();
        replier.execute();
    }

    private class Replier extends AsyncTask<String,Void,Void> {


        @Override
        protected Void doInBackground(String... strings) {
            TicketHttpHelper.HttpResultHelper httpResult = null;
            HttpResponse response = null;
            Truster.trustEveryone();
            String result = "";
            String inputLine;
            JSONObject data = new JSONObject();

            ArrayList<String[]> headers = new ArrayList<>();
            headers.add(new String[]{"Content-Type", "application/json"});

            String url = "https://smarterhomes.freshdesk.com/api/v2/tickets/141/reply";
            try {
                httpResult = TicketHttpHelper.httpPost(url, SupportFragment.api_token, "X", String.valueOf(data), headers, 7000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(httpResult.getResponse()));
            try {
                while ((inputLine = in.readLine()) != null) {
                    result += inputLine;
                    Log.d("RESULT:",result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
