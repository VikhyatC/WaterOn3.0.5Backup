package com.wateron.smartrhomes.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wateron.smartrhomes.R;
import com.wateron.smartrhomes.activities.MainActivity;
import com.wateron.smartrhomes.component.AppConstants;
import com.wateron.smartrhomes.component.MeterAdapter;
import com.wateron.smartrhomes.models.Apartment;
import com.wateron.smartrhomes.models.Meter;
import com.wateron.smartrhomes.models.MeterSet;
import com.wateron.smartrhomes.util.CrashHelper;
import com.wateron.smartrhomes.util.DataHelper;
import com.wateron.smartrhomes.util.LoginHandler;
import com.wateron.smartrhomes.util.MeterHandlerInterface;
import com.wateron.smartrhomes.util.MeterRenameHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Paranjay on 14-12-2017.
 */

public class SettingFragment extends Fragment implements MeterHandlerInterface {
    public AlertDialog alertDialog;
    public int changedPosition;
    public String changedName;
    ListView listView;
    LinearLayout homemenubutton;
    TextView spinnertime;
    int curselected=-1;
    DataHelper dataHandler;
    Apartment apartment;
    MeterSet[] list=new MeterSet[5];
    List<Meter> meter=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.settings_fragment,container,false);
        dataHandler = new DataHelper(getContext());
        listView=(ListView)view.findViewById(R.id.setting_list);

        spinnertime=(TextView)view.findViewById(R.id.spinner_time);
        Typeface type1= Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_regular.ttf");
        spinnertime.setTypeface(type1);
        homemenubutton=(LinearLayout)view.findViewById(R.id.homemenubutton);
        homemenubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });
        loadApartment();
        return view;
    }

    private void loadApartment() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("defaults_pref", Context.MODE_PRIVATE);
        int selectedAptId = sharedPreferences.getInt("apartmentIdSelected",-1);
        apartment = dataHandler.getApartment(selectedAptId);
        meter = dataHandler.getMeterForApartment(selectedAptId);
        Log.d("MeterSet", String.valueOf(meter));
        if(meter!=null){
            if(!meter.isEmpty()){
                list=new MeterSet[meter.size()];
                int count=0;
                for(Meter m:meter){
                    list[count]=new MeterSet(m,false);
                    count++;
                }
                ListAdapter adapter=new MeterAdapter(getActivity(),list,this);
                listView.setAdapter(adapter);
                Log.d("adapter","done"+String.valueOf(list.length));
            }
        }
    }

    public void editValues(String meterid,String newName,int positiotn){
        String[] mobile = LoginHandler.getUserMobile(getContext());
        changedPosition=positiotn;
        changedName=newName;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_details",Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken",null);
        if (authToken!=null){
            Log.d("TokenReceived",authToken);
            MeterRenameHelper.changeValveName(mobile[0],mobile[1],authToken,this,meterid,newName);
        }

//        MeterRenameHelper.changeValveName(mobile[0],mobile[1], FirebaseInstanceId.getInstance().getToken(),this,meterid,newName);

    }
    public boolean isCurrent() {
        return curselected == -1;
    }
    public void hideIt(){
        if(getActivity().getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
        list[curselected].setEditOn(false);
        curselected=-1;
        ListAdapter adapter=new MeterAdapter(getActivity(),list,this);
        listView.setAdapter(adapter);
    }
    public void listupdate(int position, boolean state) {
        if(curselected==-1){
            ((InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            list[position].setEditOn(state);
            curselected=position;
            ListAdapter adapter=new MeterAdapter(getActivity(),list,this);
            listView.setAdapter(adapter);
        }else if(curselected==position){
            list[position].setEditOn(false);
            curselected=-1;
            ListAdapter adapter=new MeterAdapter(getActivity(),list,this);
            listView.setAdapter(adapter);
        }else{
            ((InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            list[position].setEditOn(state);
            list[curselected].setEditOn(false);
            curselected=position;
            ListAdapter adapter=new MeterAdapter(getActivity(),list,this);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void nameChanged() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(alertDialog!=null){
                    if(alertDialog.isShowing()){
                        alertDialog.dismiss();
                    }
                }

                    Toast.makeText(getActivity(), "Meter location name updated.", Toast.LENGTH_SHORT).show();
                    Meter meter1=meter.get(changedPosition);
                    ((MainActivity)getActivity()).logevent("Setting_WaterMeter_MeterName_change","Meter name change","Touch Event");
                    meter1.setLocationUser(changedName);
                    dataHandler.updateMeter(meter1);
                    refresh();


            }
        });
    }

    private void refresh() {
        loadApartment();
    }

    @Override
    public void errorChangingName(final String response, final int httpResult, final String url, final String xmsin, final String token, final String meter_id, final String newName) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(alertDialog!=null){
                    if(alertDialog.isShowing()){
                        alertDialog.dismiss();
                    }
                }

                    list[curselected].setEditOn(false);
                    curselected=-1;
                    ListAdapter adapter=new MeterAdapter(getActivity(),list,SettingFragment.this);
                    listView.setAdapter(adapter);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
                String dateTime = sdf.format(new Date());
                String[] mobile = LoginHandler.getUserMobile(getContext());
                String versionDetails = System.getProperty("os.version");
                Log.d("VersionDetails",versionDetails);
                CrashHelper.SendCrashMailer("("+mobile[1]+")"+mobile[0], AppConstants.APPVERSION, String.valueOf(httpResult),response+"\n"+"REQUEST_URL:"+url+"\n"+"X_MSIN:"+xmsin+"\n"+"TOKEN:"+token+"METER_ID:"+meter_id+"RENAME_TO:"+newName,dateTime,"android");
                    ((MainActivity)getActivity()).logevent("Setting_WaterMeter_MeterName_change","Error updating name","Error non fatal");
                    Toast.makeText(getActivity(),"Error Updating location name",Toast.LENGTH_LONG).show();


            }
        });
    }
}
