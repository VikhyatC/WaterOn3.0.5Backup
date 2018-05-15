package com.wateron.smartrhomes.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.json.JSONException;
import org.w3c.dom.Text;

import com.google.firebase.iid.FirebaseInstanceId;
import com.wateron.smartrhomes.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.io.BufferedReader;

import com.wateron.smartrhomes.util.DataHelper;
import com.wateron.smartrhomes.util.LoginHandler;
import com.wateron.smartrhomes.util.TicketHandlerInteface;
import com.wateron.smartrhomes.util.TicketHttpHelper;
import com.wateron.smartrhomes.util.Truster;
import com.wateron.smartrhomes.models.Apartment;
import java.io.IOException;

import static com.wateron.smartrhomes.fcmservices.RegistrationTokenUpdater.context;

/**
 * Created by Paranjay on 12-12-2017.
 */

public class SupportFragment extends Fragment implements TicketHandlerInteface{
    public static String api_token = "FSqh7VGTjS3LkvkpVQHL";
    private TextView text_desc;
    private TextView text_subject;
    private EditText et_desc;
    private EditText et_subject;
    private Button submit_btn;
    private static String url;
    String[] mobile_MSIN;
    int selectedAptId = -1;
    String society;
    DataHelper helper ;
    private Apartment selectedApartment;
    JSONObject User_details;
    private TextView inavlidInput;
    private String mobile;
    private String address;
    private String email;
    private String description;
    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DataHelper(getContext());

    }

    private void loadContext() {
        selectedApartment = helper.getApartment(selectedAptId);
        Log.d("SelectedApartmentID", String.valueOf(selectedAptId));
        int apt_id = selectedApartment.getId();
        selectedAptId = apt_id;
        society = selectedApartment.getSociety();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        url= getActivity().getSharedPreferences("defaults", Context.MODE_PRIVATE).getString("supporturl","https://smarterhomes.freshdesk.com/api/v2/tickets");
        mobile_MSIN = LoginHandler.getUserMobile(getContext());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("defaults_pref",Context.MODE_PRIVATE);
        selectedAptId = sharedPreferences.getInt("apartmentIdSelected",-1);
        final String mobile = mobile_MSIN[0]/*+mobile_MSIN[0]*/;
        Log.d("url & Mobile",url+" "+mobile);

       /* if(User_details!=null){
            try {
                email_iD = User_details.getString("resident_email");
                phone_number = User_details.getString("resident_phone");
                String apartment_name = User_details.getString("apartment_name");
                String floor = User_details.getString("address");
                address = this.society+apartment_name+floor;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

        et_desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                inavlidInput.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //et_desc.setText(s);

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = et_desc.getText().toString().length();
                if(length==0){
                    inavlidInput.setVisibility(View.VISIBLE);
                }
            }
        });
        helper = new DataHelper(getContext());
        if(!isOnline()){
            final AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
            builder.setTitle("No internet Connection");
            builder.setMessage("Please turn on internet connection to continue");

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // To test, Add a Toast, with a context of getBaseContext() and message such as
                    // "I can't believe you pressed me! You better sleep with one eye open tonight. Just sayin'."

                    builder.setCancelable(false);
                    SupportFragment fragment = new SupportFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentview,fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else {
            TicketHttpHelper.getUserTicketDetails(mobile_MSIN[1],mobile_MSIN[0],selectedAptId, FirebaseInstanceId.getInstance().getToken(),this);
        }


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = et_desc.getText().toString();
                String subject= et_subject.getText().toString();
                Log.d("Description:Address",description+address);
                Log.d("Email:Subject",email+subject);
                if(!isOnline()){
                    final AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
                    builder.setTitle("No internet Connection");
                    builder.setMessage("Please turn on internet connection to continue");

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // To test, Add a Toast, with a context of getBaseContext() and message such as
                            // "I can't believe you pressed me! You better sleep with one eye open tonight. Just sayin'."

                            builder.setCancelable(false);
                            SupportFragment fragment = new SupportFragment();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentview,fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }

                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    TicketFetcher ticketFetcher = new TicketFetcher();
                    Log.d("AddessOfCustomer",address);
                    ticketFetcher.execute(description, mobile, address,subject,email);
                }
            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(getContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.support_fragment,container,false);
        /*WebView view1=(WebView)view.findViewById(R.id.webView1);
        view1.setWebViewClient(new WebViewClient());*/
        Typeface type  = Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_light.ttf");
        Typeface type1  = Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_regular.ttf");
        inavlidInput = (TextView)view.findViewById(R.id.invalid_input);
        text_desc = (TextView)view.findViewById(R.id.description_text);
        text_subject= (TextView)view.findViewById(R.id.subject_text);
        et_desc = (EditText)view.findViewById(R.id.user_description);
        et_subject= (EditText)view.findViewById(R.id.user_subject);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        submit_btn = (Button)view.findViewById(R.id.submit_btn);
        text_desc.setTypeface(type);
        text_subject.setTypeface(type);
        inavlidInput.setTypeface(type);
        et_desc.setTypeface(type);
        et_subject.setTypeface(type);
        submit_btn.setTypeface(type1);
//       try {
//            TicketHttpHelper.doPostRequest(url,"{\"description\":\"Some details on the issue ...\",\"subject\":\"Support needed..\",\"email\":\"email@yourdomain.com\",\"priority\":1,\"status\":2}");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        //view1.loadUrl(url);

        return view;
    }

    @Override
    public void fetchData(String user_data) {
        try {
            Log.d("user_data:",user_data);
            if(user_data!=null){
                progressBar.setVisibility(View.GONE);
                User_details = new JSONObject(user_data);
                mobile= User_details.getString("resident_phone");
                String floor= User_details.getString("address");
                String apartment_name= User_details.getString("apartment_name");
                address = society+" "+floor+" "+apartment_name;
                email = User_details.getString("resident_email");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class TicketFetcher extends AsyncTask<String,Void,Void> {
        TicketHttpHelper.HttpResultHelper httpResult;
        TicketHttpHelper.HttpResultHelper httpReply;
        String id = null;
        String result = "";
        HttpResponse response = null;
        private JSONObject jsonParser;

        @Override
        protected void onPostExecute(Void aVoid) {

            //httpReply = TicketHttpHelper.httpPost()
            if (result!=null){
                AcknowlgmentFragment fragment = new AcknowlgmentFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentview,fragment);
                fragmentTransaction.commit();
            }

        }

        @Override
        protected Void doInBackground(String... strings) {
//            try {
//                TicketHttpHelper.doGetRequest(url);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            Truster.trustEveryone();

            String inputLine;
            // JSON data
            JSONObject customfields = new JSONObject();
            try {
                customfields.put("mobile_number",strings[1]);
                customfields.put("address",strings[2]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject data = new JSONObject();
            try {
                data.put("description", strings[0]);
                data.put("subject", strings[3]);
                data.put("email", strings[4]);
                data.put("priority", 1);
                data.put("custom_fields",customfields);
                // data.put("custom_fields", new JSONArray().;
                data.put("status",2);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Headers
            ArrayList<String[]> headers = new ArrayList<>();
            headers.add(new String[]{"Content-Type", "application/json"});
//            response = TicketHttpHelper.httpget();
            // String reply_url= "https://smarterhomes.freshdesk.com/api/v2/tickets/"+id+"/reply";
            try {
                httpResult = TicketHttpHelper.httpPost(url, api_token, "X", String.valueOf(data), headers, 7000);
                //httpReply = TicketHttpHelper.httpPost(reply_url,api_token, "X", String.valueOf(data),headers,7000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(httpResult.getResponse()));
            try {
                while ((inputLine = in.readLine()) != null) {
                    result += inputLine;
                    Log.d("RESULT",result);
                    try {
                        JSONObject jsonParser = new JSONObject(result);
                        // jsonParser.getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //reply_url = "https://smarterhomes.freshdesk.com/api/v2/tickets/"+id+"/reply"
            return null;
        }
    }
}