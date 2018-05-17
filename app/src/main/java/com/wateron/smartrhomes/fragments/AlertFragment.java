package com.wateron.smartrhomes.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.wateron.smartrhomes.R;
import com.wateron.smartrhomes.activities.MainActivity;
import com.wateron.smartrhomes.component.AppConstants;
import com.wateron.smartrhomes.models.Alert;
import com.wateron.smartrhomes.models.Apartment;
import com.wateron.smartrhomes.models.Meter;
import com.wateron.smartrhomes.models.Slabs;
import com.wateron.smartrhomes.util.AlertHandlerInterface;
import com.wateron.smartrhomes.util.AlertHelper;
import com.wateron.smartrhomes.util.CrashHelper;
import com.wateron.smartrhomes.util.DataHelper;
import com.wateron.smartrhomes.util.LoginHandler;
import com.wateron.smartrhomes.util.ValveHandlerInterface;
import com.wateron.smartrhomes.util.ValveHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by Paranjay on 11-12-2017.
 */

public class AlertFragment extends Fragment implements AlertHandlerInterface, ValveHandlerInterface {
    public boolean displayingImportant=false;
    LinearLayout homemenubutton;
    FrameLayout back,forward;
    Button rupeefliptext,litresfliptext;
    ImageButton flipswitchbg,flipswitchbutton;
    LinearLayout flipswitch;
    TextView spinner_time,roomType,alertTextDesc,alerttime,alertdate,alertquatity,alertcomdity,alertmainactiontext,alertsubactiontext;
    ImageView alertdescicon,alertbigiconimg,alert_switch_img,alertsmalliconimage;
    TextView bigcountdown,bigcountdown1,smallcountdown,smallcountdown1,coun_down_text1;
    DataHelper dataHandler;
    Alert alarmdetails;
    int currentAlertStatus=0;
    boolean loadwith=true;
    RelativeLayout withValve;
    FrameLayout countdown_stoper;
    ImageView valve_bg_img,valve_icon_img;
    TextView coun_down_text;
    List<Slabs> slabDataList=new ArrayList<>();
    Apartment apartment=new Apartment();
    int selected=0;
    List<Alert> alarms=new ArrayList<>();
    List<Meter> meters=new ArrayList<>();
    List<String> activealarms=new ArrayList<>();
    boolean rupeeselected=true;
    boolean connecting1=false;
    boolean connecting2=false;
    boolean connecting3=false;
    private Handler handler1,handler2,handler3;
    private int count=5;
    //2018-03-08 14:05
    private Runnable alertClose=new Runnable() {
        @Override
        public void run() {
            count--;
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(100);
            bigcountdown.setText(String.valueOf(count));
            if(count==0){
                alert_switch_img.setImageResource(R.drawable.alert_action_button);
                bigcountdown.setVisibility(View.GONE);
                alertbigiconimg.setVisibility(View.VISIBLE);
                if(connecting1){
                    connecting1=false;
                    processended1();

                }
            }else{
                handler1.postDelayed(this,1000);
            }

        }
    };
    private  Runnable alertmessageclose=new Runnable() {
        @Override
        public void run() {
            count--;
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(100);
            coun_down_text.setText(String.valueOf(count));
            if(count==0){
                valve_bg_img.setImageResource(R.drawable.red_alert);
                coun_down_text.setVisibility(View.GONE);
                valve_icon_img.setVisibility(View.VISIBLE);

                if(connecting3){
                    connecting3=false;
                    processended3();
                }

            }else{


                handler3.postDelayed(this,1000);

            }
        }
    };
    private Runnable alertEgnore=new Runnable() {
        @Override
        public void run() {
            count--;
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(100);
            smallcountdown.setText(String.valueOf(count));
            if(count==0){
                alert_switch_img.setImageResource(R.drawable.alert_action_button);
                smallcountdown.setVisibility(View.GONE);
                alertsmalliconimage.setVisibility(View.VISIBLE);

                if(connecting2){
                    connecting2=false;
                    processended2();
                }

            }else{


                handler2.postDelayed(this,1000);

            }
        }
    };
    private void processended3() {
        Toast.makeText(getActivity(),"Ignoring Alert",Toast.LENGTH_SHORT).show();
        String[] mobile = LoginHandler.getUserMobile(getContext());
        alarms=dataHandler.getActiveAlarms(selectedAptId);
//        dataHandler.deleteAlert(alarms.get(selected));
        ValveHelper.ignoreAlert(mobile[0],mobile[1],FirebaseInstanceId.getInstance().getToken(),this,alarms.get(selected).getMeterId());
    }

    private void processended1() {
        Toast.makeText(getActivity(),"Requesting to close valve",Toast.LENGTH_LONG).show();
        String[] mobile = LoginHandler.getUserMobile(getContext());
//        ((HomeActivity)getActivity()).logevent("Valve_Close",dataHandler.getMeter(alarms.get(selected).getMeter_id()).getLocation(),"Touch Event");
//        dataHandler.deleteAlert(alarms.get(selected));
        ValveHelper.closeValve(mobile[0],mobile[1],FirebaseInstanceId.getInstance().getToken(),this,alarms.get(selected).getMeterId());
        //alarms.remove(alarms.get(selected));

    }

    private void processended2() {
        String[] mobile = LoginHandler.getUserMobile(getContext());
        Toast.makeText(getActivity(),"Ignoring Alert",Toast.LENGTH_SHORT).show();
//        Alert alarm = dataHandler.getAlert();
//        dataHandler.deleteAlert(alarms.get(selected));
        ValveHelper.ignoreAlert(mobile[0],mobile[1],FirebaseInstanceId.getInstance().getToken(),this,alarms.get(selected).getMeterId());
//        alarms.remove(alarms.get(selected));
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_fragment,container,false);
        dataHandler=new DataHelper(getContext());
        initView(view);
        loadDefaults();
        initListners();
        checkNotificationToLoad();
        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        checkNotificationToLoad();
//    }

    private void checkNotificationToLoad() {
        alarms = new ArrayList<>();
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("alert_manager",Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("loadNotificationAlert",false)){
            sharedPreferences.edit().putBoolean("loadNotificationAlert",false).apply();
            String alarmtoload=sharedPreferences.getString("alertid","0");
            int selected_apt = sharedPreferences.getInt("selectedApartment",-1);
            int screen = getActivity().getSharedPreferences("alert_manager",Context.MODE_PRIVATE).getInt("apartment_index",-1);
            Log.d("AlarmAPtIndex", String.valueOf(screen));
            if(!alarmtoload.equals("0")&&(screen!=-1)){
                alarms=new ArrayList<>();

//                Log.d("IndexNotification", String.valueOf(alarms.indexOf(selected_apt)));
//                selected = selected_apt;
//                selected = alarms.lastIndexOf(selected_apt);
                selectedAptId = selected_apt;
                alarms=dataHandler.getActiveAlarms(selectedAptId);

                //                loadAlerts();

                loadValues();

                Log.d("AlarmForNotification", String.valueOf(alarms));
                displayingImportant=true;
            }else {
                Log.d("Notification","not Correct");
                loadAlerts();
            }
        }else{
            Log.d("Notification","not Occured");
            loadAlerts();
        }
    }

    private void loadAlerts() {

        String[] mobile = LoginHandler.getUserMobile(getContext());
        AlertHelper.getAlertData(mobile[0],mobile[1], FirebaseInstanceId.getInstance().getToken(),this, String.valueOf(apartment.getId()));
    }
    int selectedAptId;
    private void loadDefaults() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("defaults_pref", Context.MODE_PRIVATE);
        selectedAptId = sharedPreferences.getInt("apartmentIdSelected",-1);
        apartment = dataHandler.getApartment(selectedAptId);
        meters = dataHandler.getMeterForApartment(selectedAptId);
        slabDataList = dataHandler.getSlabs(selectedAptId);
        rupeeselected = sharedPreferences.getBoolean("currencySelected",false);
        rupeefliptext.setText(apartment.getCurrencyText());
        litresfliptext.setText(apartment.getUnitText());
        flipTheSwitch(rupeeselected);
    }
    public  boolean touchWithinBounds(MotionEvent event, View view) {
        if (event == null || view == null || view.getWidth() == 0 || view.getHeight() == 0)
            return false;

        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation);
        int viewMaxX = viewLocation[0] + view.getWidth() - 1;
        int viewMaxY = viewLocation[1] + view.getHeight() - 1;
        return (event.getRawX() <= viewMaxX && event.getRawX() >= viewLocation[0]
                && event.getRawY() <= viewMaxY && event.getRawY() >= viewLocation[1]);
    }
    private void initListners() {
        loadFlipSwitch();
        flipTheSwitch(rupeeselected);
        homemenubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });
        bigcountdown1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    Log.d("Touched","catching ACTION_BUTTON_PRESS");
                    if(!connecting1 && !connecting2){

                        if(currentAlertStatus==0){
                            connecting1=true;
                            Log.d("Touched"," catching started");
                            count=5;
                            alert_switch_img.setImageResource(R.drawable.alert_action_button_red_depressed);
                            bigcountdown.setText(String.valueOf(count));
                            alertbigiconimg.setVisibility(View.GONE);
                            bigcountdown.setVisibility(View.VISIBLE);
                            handler1=new Handler();
                            handler1.postDelayed(alertClose,1000);
                        }


                    }
                    return true;
                }

                if(event.getAction()==MotionEvent.ACTION_UP ||event.getAction()==MotionEvent.ACTION_CANCEL){
                    Log.d("Touched"," catching ACTION_BUTTON_RELEASE");
                    if(connecting1){
                        connecting1=false;
                        Log.d("Touched"," catching started");
                        handler1.removeCallbacks(alertClose);
                        alert_switch_img.setImageResource(R.drawable.alert_action_button);
                        bigcountdown.setVisibility(View.GONE);
                        alertbigiconimg.setVisibility(View.VISIBLE);
                    }
                    return  true;
                }
                return false;
            }
        });

        coun_down_text1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    if(!connecting3){
                        if(currentAlertStatus==0){
                            connecting3=true;
                            count=5;
                            valve_bg_img.setImageResource(R.drawable.black_depressed);
                            coun_down_text.setText(String.valueOf(count));
                            valve_icon_img.setVisibility(View.GONE);
                            coun_down_text.setVisibility(View.VISIBLE);
                            handler3=new Handler();
                            handler3.postDelayed(alertmessageclose,1000);
                        }
                    }
                    return  true;
                }

                if(event.getAction()==MotionEvent.ACTION_UP ||event.getAction()==MotionEvent.ACTION_CANCEL){
                    Log.d("Touched"," catching ACTION_BUTTON_RELEASE");
                    if(connecting3){
                        connecting3=false;
                        Log.d("Touched"," catching started");
                        handler3.removeCallbacks(alertmessageclose);
                        valve_bg_img.setImageResource(R.drawable.red_alert);
                        coun_down_text.setVisibility(View.GONE);
                        valve_icon_img.setVisibility(View.VISIBLE);
                    }
                    return  true;
                }

                return false;
            }
        });

        smallcountdown1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    Log.d("Touched","catching ACTION_BUTTON_PRESS");
                    if(!connecting1 && !connecting2){
                        if(currentAlertStatus==0){
                            connecting2=true;
                            Log.d("Touched"," catching started");
                            count=5;
                            alert_switch_img.setImageResource(R.drawable.alert_action_button_black_depressed);
                            smallcountdown.setText(String.valueOf(count));
                            alertsmalliconimage.setVisibility(View.GONE);
                            smallcountdown.setVisibility(View.VISIBLE);
                            handler2=new Handler();
                            handler2.postDelayed(alertEgnore,1000);
                        }


                    }
                    return true;
                }

                if(event.getAction()==MotionEvent.ACTION_UP ||event.getAction()==MotionEvent.ACTION_CANCEL){
                    Log.d("Touched"," catching ACTION_BUTTON_RELEASE");
                    if(connecting2){
                        connecting2=false;
                        Log.d("Touched"," catching started");
                        handler2.removeCallbacks(alertEgnore);
                        alert_switch_img.setImageResource(R.drawable.alert_action_button);
                        smallcountdown.setVisibility(View.GONE);
                        alertsmalliconimage.setVisibility(View.VISIBLE);
                    }
                    return  true;
                }
                return false;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected==0){

                }else{
                    selected--;
                    loadValues();
                    Log.d("click",">>prev");
                }

            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp=selected+1;
                if(temp<alarms.size()){
                    selected++;
                    loadValues();
                    Log.d("click",">>next");
                }


            }
        });
    }

    private void initView(View view) {
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_light.ttf");

        withValve=(RelativeLayout)view.findViewById(R.id.withValve);
        countdown_stoper=(FrameLayout)view.findViewById(R.id.countdown_stoper);
        valve_bg_img=(ImageView)view.findViewById(R.id.valve_bg_img);
        valve_icon_img=(ImageView)view.findViewById(R.id.valve_icon_img);
        coun_down_text=(TextView)view.findViewById(R.id.coun_down_text);
        coun_down_text1=(TextView)view.findViewById(R.id.coun_down_text3);
        rupeefliptext=(Button)view.findViewById(R.id.flipswitchrupee);
        rupeefliptext.setTypeface(type);
        litresfliptext=(Button)view.findViewById(R.id.flipswitchlitres);
        litresfliptext .setTypeface(type);
        flipswitchbg=(ImageButton)view.findViewById(R.id.flipswitchbg);
        flipswitch=(LinearLayout)view.findViewById(R.id.flipswitchbuttonview);
        flipswitchbutton=(ImageButton)view.findViewById(R.id.flipswitchbutton);
        homemenubutton=(LinearLayout) view.findViewById(R.id.homemenubutton);
        Typeface type1= Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_regular.ttf");
        spinner_time=(TextView)view.findViewById(R.id.spinner_time);
        spinner_time .setTypeface(type1);
        roomType=(TextView)view.findViewById(R.id.roomType);
        roomType .setTypeface(type);
        alertTextDesc=(TextView)view.findViewById(R.id.alert_text_desc);
        alertTextDesc .setTypeface(type);
        alerttime=(TextView)view.findViewById(R.id.alert_time_display);
        alerttime .setTypeface(type);
        alertdate=(TextView)view.findViewById(R.id.alert_date_display);
        alertdate .setTypeface(type);
        alertquatity=(TextView)view.findViewById(R.id.alert_text_value);
        alertquatity .setTypeface(type);
        alertcomdity=(TextView)view.findViewById(R.id.alert_text_value_unit);
        alertcomdity.setTypeface(type);
        alertmainactiontext=(TextView)view.findViewById(R.id.alert_action_text);
        alertmainactiontext.setTypeface(type);
        alertsubactiontext=(TextView)view.findViewById(R.id.alert_bottom_info);
        alertsubactiontext.setTypeface(type);
        bigcountdown=(TextView)view.findViewById(R.id.coun_down_text1);
        bigcountdown.setTypeface(type);
        bigcountdown1=(TextView)view.findViewById(R.id.coun_down_text11);
        bigcountdown1.setTypeface(type);
        smallcountdown=(TextView)view.findViewById(R.id.coun_down_text2);
        smallcountdown.setTypeface(type);
        smallcountdown1=(TextView)view.findViewById(R.id.coun_down_text22);
        smallcountdown1.setTypeface(type);
        back=(FrameLayout) view.findViewById(R.id.previousBtn);

        forward=(FrameLayout)view.findViewById(R.id.nextBtn);

        alertdescicon=(ImageView)view.findViewById(R.id.alert_icon);

        alertbigiconimg=(ImageView)view.findViewById(R.id.alert_big_icon_img);
        alertsmalliconimage=(ImageView)view.findViewById(R.id.alert_small_icon_img);

        alert_switch_img=(ImageView)view.findViewById(R.id.alert_switch_img);
    }

    private void loadFlipSwitch() {
        flipswitchbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rupeeselected=!rupeeselected;
                changeUnit(rupeeselected);
            }
        });
        flipswitchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rupeeselected=!rupeeselected;
                changeUnit(rupeeselected);
            }
        });
        rupeefliptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rupeeselected=true;
                changeUnit(rupeeselected);
            }
        });
        litresfliptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rupeeselected=false;
                changeUnit(rupeeselected);
            }
        });
    }

    private void flipTheSwitch(boolean rupeeselected) {
        Typeface typeface1=Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_regular.ttf");
        Typeface typeface2=Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_light.ttf");

        if(rupeeselected){

            rupeefliptext.setTypeface(typeface1);
            litresfliptext.setTypeface(typeface2);
            flipswitch.setGravity(Gravity.TOP);
        }else{
            rupeefliptext.setTypeface(typeface2);
            litresfliptext.setTypeface(typeface1);
            flipswitch.setGravity(Gravity.BOTTOM);
        }
    }

    private void changeUnit(boolean rupeeselected) {
        flipTheSwitch(rupeeselected);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("defaults_pref",Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("currencySelected",rupeeselected).apply();
//        updatepickerslot();
        updateAlarmQuantity();
    }

    @Override
    public Context getInstance() {
        return getContext();
    }

    @Override
    public void loadData(boolean latest) {
        if(latest){

        }else{

        }
        Log.d("Loading alarm data","Next");
//        alarms.clear();
        alarms = new ArrayList<>();
        alarms = dataHandler.getActiveAlarms(apartment.getId());
//        Set<Alert> alert_set = new HashSet<>();
//        alert_set.addAll(alarms);
//        alarms.clear();
//        alarms.addAll(alert_set);
        Log.d("ActiveAlarms", String.valueOf(alarms));
        if(alarms==null){
            gotoHome();
//            to take demo comment above line and uncomment this
//            alarms=new ArrayList<>();
//            Alert alert=new Alert(12,"2017-12-14 12:20",10001,1931);
//            alarms.add(alert);
//            loadValues();
        }else{
            if(alarms.size()==0){
                gotoHome();
            }else{
                loadValues();
            }

        }
    }

    private void loadValues() {
        loadButtons();
        if(alarms!=null){
            alarmdetails=alarms.get(selected);
            Log.d("Alarm Details :", String.valueOf(alarmdetails));
            int cid=alarmdetails.getMeterId();
            for(Meter m:meters){
                if(m.getId()==(cid)){
                    roomType.setText(m.getLocationUser());
                    if(m.getHasValve()==1){
                        loadwith=true;
                    }else{
                        loadwith=false;
                    }

                }
            }
            currentAlertStatus=0;
            loadAlertText();

            try {
                SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                SimpleDateFormat sdf3=new SimpleDateFormat("hh:mm a",Locale.ENGLISH);
                String time=sdf3.format(sdf2.parse(alarmdetails.getTime().split(" ")[1]));
                time=time.replace(".","");

                alerttime.setText(time);
                Log.d("TEFSGFSF",alerttime.getText().toString());
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
                SimpleDateFormat sdf1=new SimpleDateFormat("EEE d MMM",Locale.ENGLISH);
                String date=sdf1.format(sdf.parse(alarmdetails.getTime().split(" ")[0] ));
                alertdate.setText(date);
                alertdate.setVisibility(View.VISIBLE);
            }catch (Exception e){
                e.printStackTrace();
                alerttime.setText(alarmdetails.getTime());
                alertdate.setVisibility(View.GONE);
            }

            updateAlarmQuantity();
            if(selected==alarms.size()-1){
                forward.setEnabled(false);
                Log.d("next","off");
            }else{
                forward.setEnabled(true);
                Log.d("next","on");
            }

            if(selected==0){
                back.setEnabled(false);
                Log.d("prev","off");
            }else{
                back.setEnabled(true);
                Log.d("prev","on");
            }
        }

//        String loaction=meters.get().getLocation();
//        roomType.setText(meters.get(selected).getLocation());

//        currentAlertStatus=dataHandler.getAlarmstate(alarmdetails.getId());

    }

    private void loadButtons() {
        if(alarms!=null){
            if(alarms.size()==1){
                back.setVisibility(View.INVISIBLE);
                forward.setVisibility(View.INVISIBLE);
            }else{
                if(selected==alarms.size()-1){
                    back.setVisibility(View.VISIBLE);
                    forward.setVisibility(View.INVISIBLE);
                }else if(selected==0){
                    back.setVisibility(View.INVISIBLE);
                    forward.setVisibility(View.VISIBLE);
                }else{
                    back.setVisibility(View.VISIBLE);
                    forward.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    private void updateAlarmQuantity() {
        if(rupeeselected){
            alertquatity.setText(String.valueOf((int)getRupee(Double.parseDouble(String.valueOf(alarmdetails.getQty())))));
            alertcomdity.setText(apartment.getCurrencyText());
        }else{
            alertquatity.setText(String.valueOf(alarmdetails.getQty()));
            alertcomdity.setText(apartment.getUnitText());
        }

    }
    private double getRupee(double amount) {
        double ans=0;
        if(slabDataList!=null){
            double a=amount/1000;
            for(int i=0;i<slabDataList.size();i++){
                if(i==slabDataList.size()-1){
                    ans+=(a)*slabDataList.get(i).getSlabPrice();
                    Log.d("fo0r"+String.valueOf(amount),String.valueOf(ans));
                }else{
                    if(a>slabDataList.get(i+1).getSlabKl()){
                        ans+=(slabDataList.get(i+1).getSlabKl())*slabDataList.get(i).getSlabPrice();
                        a = a -slabDataList.get(i+1).getSlabKl();
                        Log.d("fo1r"+String.valueOf(amount),String.valueOf(ans));
                    }else{
                        ans+=(a)*slabDataList.get(i).getSlabPrice();
                        a=0;
                        Log.d("fo2r"+String.valueOf(amount),String.valueOf(ans));
                    }
                }
            }
        }
        return  ans;
    }
    private void loadWithoutValve() {
        if(currentAlertStatus==0){
            alertsubactiontext.setText("Press & hold for 5 secs");
            alertdescicon.setImageResource(R.drawable.alert_alert_icon);
            valve_bg_img.setImageResource(R.drawable.red_alert);
            valve_icon_img.setImageResource(R.drawable.read_envelope);
            alertTextDesc.setText("You are losing water in your home. Please check for any leaks.");

        }
        if(currentAlertStatus==2){
            alertsubactiontext.setText("This alert has been ignored");
            alertdescicon.setImageResource(R.drawable.alert_sucess_top);
            valve_bg_img.setImageResource(R.drawable.green_alert);
            valve_icon_img.setImageResource(R.drawable.unread_envelope);
            alertTextDesc.setText("This alert was ignored. WaterOn will not re-alert you for this incidence.");
        }
    }

    private void loadNormal() {
        if(currentAlertStatus==0){
            alertsubactiontext.setText("Press & hold for 5 secs");
            alertdescicon.setImageResource(R.drawable.alert_alert_icon);
            alert_switch_img.setImageResource(R.drawable.alert_action_button);
            alertTextDesc.setText("You are losing water in your home. Please check for any leaks.");
        }
        if(currentAlertStatus==1){
            alertsubactiontext.setText("You can open valve from valve control screen");
            alertdescicon.setImageResource(R.drawable.alert_sucess_top);
            alert_switch_img.setImageResource(R.drawable.alert_success_bottom);
            alertTextDesc.setText("Congratulations. Valve will be shut off in a few minutes.");

        }
        if(currentAlertStatus==2){
            alertsubactiontext.setText("This alert has been ignored");
            alertdescicon.setImageResource(R.drawable.alert_sucess_top);
            alert_switch_img.setImageResource(R.drawable.alert_ignore_sucess);
            alertTextDesc.setText("This alert has been ignored. WaterOn will not re-alert you for this incidence.");
        }
    }

    private void loadAlertText() {
        if(loadwith){
            withValve.setVisibility(View.VISIBLE);
            countdown_stoper.setVisibility(View.GONE);
            loadNormal();
        }else{
            withValve.setVisibility(View.GONE);
            countdown_stoper.setVisibility(View.VISIBLE);
            loadWithoutValve();
        }
    }
    private void gotoHome() {
        ((MainActivity)getActivity()).loadHome(0);
    }

    @Override
    public void errorGettingData(String response, int httpResult, String url, String xmsin, String token) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
        String dateTime = sdf.format(new Date());
        String[] mobile = LoginHandler.getUserMobile(getContext());
        CrashHelper.SendCrashMailer(mobile[0], AppConstants.APPVERSION, String.valueOf(httpResult),response+"\n"+"REQUEST_URL:"+url+"\n"+"XMSIN:"+xmsin+"\n"+"TOKEN:"+token,dateTime+"-"+"AlertScreen","android");
        gotoHome();
    }

    @Override
    public void actionSuccess(final int state, final String response) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(state==0){
                    currentAlertStatus=0;
                }else if(state==2){
                    currentAlertStatus = 2;
                }else{
                    currentAlertStatus =1;
                }
                dataHandler.deleteAlert(alarms.get(selected));
                Log.d("Response Success Action",response);
                loadAlertText();

//                dataHandler.updateAlarmState(alarmdetails,1);
//                alertsubactiontext.setText("You can open valve from valve control screen");
//                Toast.makeText(getActivity(),result+" "+response,Toast.LENGTH_SHORT).show();
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
                CrashHelper.SendCrashMailer("("+mobile[1]+")"+mobile[0],AppConstants.APPVERSION, String.valueOf(code),response+"REQUEST_URL:"+url+"XMSIN:"+xmsin+"TOKEN:"+token+"METER_ID"+meter_id+"ACTION:"+action,dateTime,"android");
                Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(),"Failed! Unable to make request",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
