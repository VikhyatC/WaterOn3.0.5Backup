package com.wateron.smartrhomes.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.wateron.smartrhomes.R;
import com.wateron.smartrhomes.activities.MainActivity;
import com.wateron.smartrhomes.models.Apartment;
import com.wateron.smartrhomes.models.Meter;
import com.wateron.smartrhomes.util.CrashHelper;
import com.wateron.smartrhomes.util.DataHelper;
import com.wateron.smartrhomes.util.DataValidator;
import com.wateron.smartrhomes.util.LoginHandler;
import com.wateron.smartrhomes.util.ValveHandlerInterface;
import com.wateron.smartrhomes.util.ValveHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Paranjay on 12-12-2017.
 */

public class ValveFragment extends Fragment implements ValveHandlerInterface {

    LinearLayout homemenubutton;
    FrameLayout previousBtn,nextBtn;
    TextView spinner_time,roomType,valve_text,valve_date,valve_time,valve_action_type,valve_bottom_info,coun_down_text;
    ImageView valve_icon_img,valve_control_alert_img;
    ImageButton valve_bg_img;
    DataHelper dataHandler;
    List<Meter> meterList=new ArrayList<>();
    Apartment apartment=new Apartment();
    int selected=0;
    boolean valvepresent=false;
    boolean currentValveStatus=true;
    boolean clickednote=false;
    String clickedaptmeter="";
    AlertDialog alertDialog;
    boolean connecting=false;
    int currenttime=5;
    private Handler handler;
    private Runnable timer=new Runnable() {
        @Override
        public void run() {
            currenttime--;
            Log.d("running","Handler");
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(100);
            Log.d("TouchedRunnable"," catching started"+String.valueOf(currenttime));
            coun_down_text.setText(String.valueOf(currenttime));
            if(currenttime==0){
                valve_bg_img.setImageResource(R.drawable.black_valve_operation);
                coun_down_text.setVisibility(View.GONE);
                valve_icon_img.setVisibility(View.VISIBLE);
                if(connecting){
                    Log.d("current Time is 0","connecting");
                    connecting=false;
                    processended();
                }

            }else{
                Log.d("current Time is not 0","Handler");
                handler.postDelayed(this,1000);
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.valve_fragment,container,false);
        Log.d("ValveView","Created");
        initView(view);
        dataHandler = new DataHelper(getContext());
        initListners();
        loadData();
        return view;

    }
    private void loadData() {
        selected=0;
        loadApartment();
        loadValve();
    }

    private void loadValve() {
        if(meterList.isEmpty()||meterList==null){
            Log.d("LoadingValve...","EmptyorNull");
            ((MainActivity)getActivity()).loadHome(1);
        }else{
            Log.d("LoadingValve...","NotNull");
            displayValue();
        }
    }

    private void loadApartment() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("defaults_pref", Context.MODE_PRIVATE);
        int selectedAptId = sharedPreferences.getInt("apartmentIdSelected",-1);
        apartment = dataHandler.getApartment(selectedAptId);
        meterList = dataHandler.getMeterForApartment(selectedAptId);
//        Log.d("")
    }

    private void displayValue() {
        Log.d("Selected", String.valueOf(selected));
        Meter m=meterList.get(selected);
        Log.d("MeterDetails", String.valueOf(m));
        if(m.getHasValve()==1){
            Log.d("Meter","HasValve");
            valvepresent=true;
        }else {
            Log.d("Meter","DoesNotHaveValve");
            valvepresent=false;
        }
//        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("toast_state",Context.MODE_PRIVATE);
//        waitingForCNF=sharedPreferences.getBoolean(meterList.get(selected).getId(),false);
//        if(waitingForCNF){
//            if(sharedPreferences.getBoolean(meterList.get(selected).getId()+"reset",false)){
//                waitingForCNF=false;
//                sharedPreferences.edit().putBoolean(meterList.get(selected).getId(),false).apply();
//            }
//        }
//        long timee= Long.parseLong(sharedPreferences.getString(meterList.get(selected).getId()+"time","0"));
//        if(new Date().getTime()-timee<3000 && timee!=0){
//            sharedPreferences.edit().putString(meterList.get(selected).getId()+"time","0").putBoolean(meterList.get(selected).getId()+"reset",true).apply();
//            ((HomeActivity)getActivity()).onRefresh();
//        }
        roomType.setText(m.getLocationUser());
        if(m.getValveCurrentStatus().equals("Open")){
            currentValveStatus=true;
            valve_text.setText("Valve is open since");
            valve_control_alert_img.setImageResource(R.drawable.valve_control_flag_unlock);
            valve_icon_img.setImageResource(R.drawable.red_lock);
            valve_action_type.setText("Stop water supply");
            valve_bottom_info.setText("Press & hold for 5 sec");
        }
        if(m.getValveCurrentStatus().equals("Close")){
            currentValveStatus=false;
            valve_text.setText("Valve is closed since");
            valve_control_alert_img.setImageResource(R.drawable.valve_control_flag);
            valve_icon_img.setImageResource(R.drawable.unlock);
            valve_action_type.setText("Resume water supply");
            valve_bottom_info.setText("Press & hold for 5 sec");
        }
        if(m.getValveCurrentStatus().equals("NA")){
            valve_text.setText("Valve is NA since");
            valve_control_alert_img.setImageResource(R.drawable.valve_control_flag_unlock);
        }

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat sdf1=new SimpleDateFormat("EEE d MMM",Locale.ENGLISH);
        SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm",Locale.ENGLISH);
        SimpleDateFormat sdf3=new SimpleDateFormat("hh:mm aa",Locale.ENGLISH);
        String date=m.getValveLastOperated();
        valve_time.setVisibility(View.VISIBLE);
        String time="";
        try {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("valve_manager", Context.MODE_PRIVATE);
            Log.d("Last Operated",m.getValveLastOperated().split(" ")[1]);
            time=m.getValveLastOperated().split(" ")[1];
            date=sdf1.format(sdf.parse(m.getValveLastOperated().split(" ")[0] ));
            time=sdf3.format(sdf2.parse(time));
            time=time.replace(".","");
            valve_time.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        valve_time.setText(time);
        valve_date.setText(date);

        if(selected==meterList.size()-1){
            nextBtn.setEnabled(false);
            nextBtn.setVisibility(View.INVISIBLE);
            Log.d("next","off");
        }else{
            nextBtn.setEnabled(true);
            nextBtn.setVisibility(View.VISIBLE);
            Log.d("next","on");
        }

        if(selected==0){
            previousBtn.setEnabled(false);
            previousBtn.setVisibility(View.INVISIBLE);
            Log.d("prev","off");
        }else{
            previousBtn.setEnabled(true);
            previousBtn.setVisibility(View.VISIBLE);
            Log.d("prev","on");
        }
        if(!valvepresent){
            valve_date.setText("Installation");
            valve_bottom_info.setText("");
            valve_text.setText("Valve is open since");
            valve_action_type.setText("No valve");
        }

    }

    private void initListners() {
        homemenubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected--;
                displayValue();
                ((MainActivity)getActivity()).logevent("Valve_Meter_point_change",meterList.get(selected).getLocationDefault(),"Touch Event");
                Log.d("click",">>prev");
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected++;
                displayValue();
                ((MainActivity)getActivity()).logevent("Valve_Meter_point_change",meterList.get(selected).getLocationDefault(),"Touch Event");
                Log.d("click",">>next");
            }
        });

//        valve_bg_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        roomType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        valve_bg_img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    Log.d("Touched","catching ACTION_BUTTON_PRESS");
                    if(!connecting  && valvepresent){
                        connecting=true;
                        Log.d("Touched"," catching started");
                        currenttime=5;
                        valve_bg_img.setImageResource(R.drawable.black_depressed);
                        coun_down_text.setText(String.valueOf(currenttime));
                        valve_icon_img.setVisibility(View.GONE);
                        coun_down_text.setVisibility(View.VISIBLE);
                        handler=new Handler();
                        handler.postDelayed(timer,1000);

                    }
                    return true;
                }
                if(event.getAction()==MotionEvent.ACTION_UP ||event.getAction()==MotionEvent.ACTION_CANCEL){
                    Log.d("Touched"," catching ACTION_BUTTON_RELEASE");
                    if(connecting){
                        Log.d("Touched"," catching started");
                        handler.removeCallbacks(timer);
                        connecting=false;
                        valve_bg_img.setImageResource(R.drawable.black_valve_operation);
                        coun_down_text.setVisibility(View.GONE);
                        valve_icon_img.setVisibility(View.VISIBLE);
                    }
                    return  true;
                }
                Log.d("Touched","didnt catch anything");
                return false;
            }
        });


    }

    private void processended() {
        if(valvepresent){
            Log.d("Meter doing work",meterList.get(selected).getValveCurrentStatus());
            if(meterList.get(selected).getValveCurrentStatus().equals("Open")){
                Log.d("CurrentStatus","open");
                String[] mobile = LoginHandler.getUserMobile(getContext());
//        ((HomeActivity)getActivity()).logevent("Valve_Close",dataHandler.getMeter(alarms.get(selected).getMeter_id()).getLocation(),"Touch Event");
                ValveHelper.closeValve(mobile[0],mobile[1], FirebaseInstanceId.getInstance().getToken(),this,meterList.get(selected).getId());
                Toast.makeText(getActivity(),"Requesting to Close Valve",Toast.LENGTH_SHORT).show();

            }
            if(meterList.get(selected).getValveCurrentStatus().equals("Close")){
                Log.d("CurrentStatus","close");
                String[] mobile = LoginHandler.getUserMobile(getContext());
//        ((HomeActivity)getActivity()).logevent("Valve_Close",dataHandler.getMeter(alarms.get(selected).getMeter_id()).getLocation(),"Touch Event");
                ValveHelper.openValve(mobile[0],mobile[1], FirebaseInstanceId.getInstance().getToken(),this,meterList.get(selected).getId());
                Toast.makeText(getActivity(),"Requesting to Open Valve",Toast.LENGTH_SHORT).show();
            }
            if(meterList.get(selected).getValveCurrentStatus().equals("NA")){
                Toast.makeText(getActivity(),"The status of the valve is not supported for operation.",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(),"The meter installation doesnt have valve.",Toast.LENGTH_SHORT).show();
        }


    }

    private void initView(View view) {
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_light.ttf");
        homemenubutton=(LinearLayout) view.findViewById(R.id.homemenubutton);
        previousBtn=(FrameLayout) view.findViewById(R.id.previousBtn);
        nextBtn=(FrameLayout)view.findViewById(R.id.nextBtn);
        Typeface type1= Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_regular.ttf");
        spinner_time=(TextView)view.findViewById(R.id.spinner_time);
        spinner_time.setTypeface(type1);
        roomType=(TextView)view.findViewById(R.id.roomType);
        roomType.setTypeface(type);
        valve_text=(TextView) view.findViewById(R.id.valve_text);
        valve_text.setTypeface(type);
        valve_date=(TextView)view.findViewById(R.id.valve_date);
        valve_date.setTypeface(type);
        valve_time=(TextView)view.findViewById(R.id.valve_time);
        valve_time.setTypeface(type);
        valve_action_type=(TextView)view.findViewById(R.id.valve_action_type);
        valve_action_type.setTypeface(type);
        valve_bottom_info=(TextView)view.findViewById(R.id.valve_bottom_info);
        valve_bottom_info.setTypeface(type);
        coun_down_text=(TextView)view.findViewById(R.id.coun_down_text);
        coun_down_text.setTypeface(type);
        valve_bg_img=(ImageButton) view.findViewById(R.id.valve_bg_img);
        valve_icon_img=(ImageView)view.findViewById(R.id.valve_icon_img);
        valve_control_alert_img=(ImageView)view.findViewById(R.id.valve_control_alert_img);

    }

    @Override
    public void actionSuccess(int state, final String response) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                DataValidator.setHardReload(getContext(),true);
                if(meterList.get(selected).getValveCurrentStatus().equals("Open")){
//                        meterList.get(selected).setCurrent_status("Close");
//                        dataHandler.updateMeter(meterList.get(selected));
                    Log.d("Valve CLose/Ignore","Detected");
                    ((MainActivity)getActivity()).logevent("Valve_Close",meterList.get(selected).getLocationDefault(),"Touch Event");

                    Toast.makeText(getActivity(),response +" at "+meterList.get(selected).getLocationUser(),Toast.LENGTH_LONG).show();
                }else{
                    Log.d("Valve open","Detected");
//                        meterList.get(selected).setCurrent_status("Open");
//                        dataHandler.updateMeter(meterList.get(selected));
                    ((MainActivity)getActivity()).logevent("Valve_Open",meterList.get(selected).getLocationDefault(),"Touch Event");
                    Toast.makeText(getActivity(),response+" at "+meterList.get(selected).getLocationUser(),Toast.LENGTH_LONG).show();
                }
//                    SharedPreferences sharedPreferences=getActivity().getSharedPreferences("toast_state",Context.MODE_PRIVATE);
//                    sharedPreferences.edit().putString(meterList.get(selected).getId()+"time",String.valueOf(new Date().getTime())).putBoolean(meterList.get(selected).getId(),true).apply();
//                    waitingForCNF=true;
//                    displayValue();


            }
        });
    }

    @Override
    public void actionFailed(int state, final String response, final int code, final String url, final String xmsin, final String token, final String meter_id, final String action) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
                String dateTime = sdf.format(new Date());
                String[] mobile = LoginHandler.getUserMobile(getContext());
                CrashHelper.SendCrashMailer(mobile[0],"3.0.5", String.valueOf(code),response+"REQUEST_URL:"+url+"XMSIN:"+xmsin+"TOKEN:"+token+"METER_ID"+meter_id+"ACTION:"+action,dateTime,"android");
                Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();
            }
        });
    }
}
