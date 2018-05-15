package com.wateron.smartrhomes.realtime;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.wateron.smartrhomes.R;

import com.wateron.smartrhomes.activities.RealTimeActivity;
import com.wateron.smartrhomes.component.TouchBar;
import com.wateron.smartrhomes.component.TouchBarCommunicator;
import com.wateron.smartrhomes.models.Apartment;
import com.wateron.smartrhomes.models.DashDataDates;
import com.wateron.smartrhomes.models.DashboardData;
import com.wateron.smartrhomes.models.Meter;
import com.wateron.smartrhomes.models.Slabs;
import com.wateron.smartrhomes.util.DashboardHandlerInterface;
import com.wateron.smartrhomes.util.DashboardHelper;
import com.wateron.smartrhomes.util.DataHelper;
import com.wateron.smartrhomes.util.LoginHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Paranjay on 27-12-2017.
 */

public class RealTimeDash extends Fragment implements RealTimeProvider, TouchBarCommunicator {


    List<DashboardData> dashboardData = new ArrayList<>();
    DataHelper dataHelper;
    List<Apartment> apartments;
    int selectedAptId = -1;
    boolean currencySelected = false;
    int currentSpanSelected=0;
    TextView homeBigText,homeSmallText;
    RelativeLayout main_animation_holder;
    ImageView alert_icon_active;
    LinearLayout addhere;
    Button month1,month2,month3;
    Spinner dashboardpicker;
    Button rupeefliptext,litresfliptext;
    LinearLayout flipswitch,alert_view;
    ImageButton flipswitchbg,flipswitchbutton;
    ImageView nextmeter, prevmeter;
    TouchBar touchBar;
    View linetoremove;
    TextView totalTag,monthtag,alerttext,billAmountCurrency,billAmount,billStatus,billDate;
    ImageView bigdot,smalldot;
    LinearLayout bartag1,bartag2;
    LinearLayout ddown;
    LinearLayout homebutton;
    RelativeLayout bighisclick;
    LinearLayout weekViewTop,weekViewBot,monthview,hourviewtop,hourViewBot;
    TextView hourtime1,hourtime2,hourtime3,hourtime4,hourtime5,hourtime6,hourtime7,hourtime8,hourtime9,hourtime10,hourtime11,hourtime12;
    TextView hourtimebottom1,hourtimebottom2,hourtimebottom3,hourtimebottom4,hourtimebottom5,hourtimebottom6,hourtimebottom7,hourtimebottom8,hourtimebottom9,hourtimebottom10,hourtimebottom11,hourtimebottom12;
    TextView hourtype1,hourtype2,hourtype3,hourtype4,hourtype5,hourtype6,hourtype7,hourtype8,hourtype9,hourtype10,hourtype11,hourtype12;
    TextView hourtypebottom1,hourtypebottom2,hourtypebottom3,hourtypebottom4,hourtypebottom5,hourtypebottom6,hourtypebottom7,hourtypebottom8,hourtypebottom9,hourtypebottom10,hourtypebottom11,hourtypebottom12;
    Button weekTopText1,weekTopText2,weekTopText3,weekTopText4,weekTopText5,weekTopText6,weekTopText7;
    Button weekbottomText1,weekbottomText2,weekbottomText3,weekbottomText4,weekbottomText5,weekbottomText6,weekbottomText7;
    List<String> weekdates=new ArrayList<String>();
    List<String> monthdates=new ArrayList<String>();
    int currentSelectedHourSlot=0;
    int currentSelectedWeekSlot=0;
    int currentSelectedMonthSlot=0;
    int maxweekslot=0;
    int maxhourlot=0;
    int maxmonthslot=2;
    double hourlydatalitres[]=new double[12];
    double hourlydatalitresyesterday[]=new double[12];
    double weekldatalitres[]=new double[7];
    double weekldatalitrespast[]=new double[7];
    double monthdatalitres[]=new double[3];
    double hourlytotal=0;
    double hourlytotalyesterday=0;
    double weeklytotal=0;
    double weeklypasttotal=0;
    double monthlytotal=0;
//    List<Meter> meterList=new ArrayList<>();
//    List<Meter> meterList1=new ArrayList<>();
    int meterstyleselected=-1;
    Apartment selectedApartment;
    List<String>    mids=new ArrayList<>();
    List<String>    activealarmids=new ArrayList<>();
    boolean dataLoaded =false;
    List<Slabs> slabsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment,container,false);
        dataHelper = new DataHelper(getContext());

        initView(view);
        loadFlipSwitch();
        loadDialSwitcher();
        loadTouchBar();
        loadSpinner();
        loadHomeButton();
        loadAlertButton();
        loadBigCircleCLick();
        loadDefaultValues();
        loadMonthSlots();
        return view;
    }

    private void initView(View view) {
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_light.ttf");
        homeBigText=(TextView)view.findViewById(R.id.home_big_text);
        homeBigText.setSelected(true);
        homeBigText.setTypeface(type);
        homeSmallText=(TextView)view.findViewById(R.id.small_text);
        homeSmallText.setSelected(true);
        homeSmallText.setTypeface(type);
        billAmountCurrency=(TextView)view.findViewById(R.id.billrupeetext);
        main_animation_holder=(RelativeLayout)view.findViewById(R.id.main_animation_holder);
        billAmountCurrency.setTypeface(type);
        billAmount=(TextView)view.findViewById(R.id.billamounttext);
        billAmount.setTypeface(type);
        billStatus=(TextView)view.findViewById(R.id.billstatus);
        billStatus .setTypeface(type);
        billDate=(TextView)view.findViewById(R.id.billdate);
        billDate .setTypeface(type);
        addhere=(LinearLayout)view.findViewById(R.id.bottomdots);
        alerttext=(TextView)view.findViewById(R.id.alertText);
        alerttext.setTypeface(type);
        alert_icon_active=(ImageView)view.findViewById(R.id.alert_icon_active);
        //currencyToggleViews
        rupeefliptext=(Button)view.findViewById(R.id.flipswitchrupee);
        rupeefliptext.setTypeface(type);
        litresfliptext=(Button)view.findViewById(R.id.flipswitchlitres);
        litresfliptext .setTypeface(type);
        flipswitchbg=(ImageButton)view.findViewById(R.id.flipswitchbg);
        flipswitch=(LinearLayout)view.findViewById(R.id.flipswitchbuttonview);
        flipswitchbutton=(ImageButton)view.findViewById(R.id.flipswitchbutton);
        //DialToggleViews
        nextmeter=(ImageView) view.findViewById(R.id.nextmeter);
        prevmeter=(ImageView)view.findViewById(R.id.prevmeter);
        //TouchBarViews
        touchBar=(TouchBar)view.findViewById(R.id.touch_bar);
        //AlertViews
        alert_view=view.findViewById(R.id.alert_view);
        //timepicker
        dashboardpicker=view.findViewById(R.id.dashboardpicker);
        ddown=(LinearLayout)view.findViewById(R.id.ddown);

        linetoremove=view.findViewById(R.id.linetoremove);

        totalTag=(TextView)view.findViewById(R.id.totaltag);
        totalTag.setTypeface(type);
        monthtag=(TextView)view.findViewById(R.id.currentselectedmonthdisplayview);
        monthtag.setTypeface(type);

        bigdot=(ImageView)view.findViewById(R.id.big_dial_dot);
        smalldot=(ImageView)view.findViewById(R.id.small_dial_dot);
        weekViewBot=(LinearLayout)view.findViewById(R.id.weeklyspanBottomview) ;
        weekViewTop=(LinearLayout)view.findViewById(R.id.weeklyspanview) ;
        homebutton=(LinearLayout) view.findViewById(R.id.homebutton);
        hourviewtop=(LinearLayout)view.findViewById(R.id.hourlyspanview) ;
        hourViewBot=(LinearLayout)view.findViewById(R.id.hourlyspanbottomview) ;
        bartag2=(LinearLayout)view.findViewById(R.id.bartag2);
        bartag1=(LinearLayout)view.findViewById(R.id.bartag1);
        monthview=(LinearLayout)view.findViewById(R.id.monthlyspanview) ;

        bighisclick=(RelativeLayout)view.findViewById(R.id.bighisclick);

        month1=(Button)view.findViewById(R.id.month1);
        month1.setTypeface(type);
        month2=(Button)view.findViewById(R.id.month2);
        month2.setTypeface(type);
        month3=(Button)view.findViewById(R.id.month3);
        month3.setTypeface(type);

        hourtime1=(TextView)view.findViewById(R.id.hourtime1);
        hourtime1 .setTypeface(type);
        hourtime2=(TextView)view.findViewById(R.id.hourtime2);
        hourtime2 .setTypeface(type);
        hourtime3=(TextView)view.findViewById(R.id.hourtime3);
        hourtime3.setTypeface(type);
        hourtime4=(TextView)view.findViewById(R.id.hourtime4);
        hourtime4.setTypeface(type);
        hourtime5=(TextView)view.findViewById(R.id.hourtime5);
        hourtime5.setTypeface(type);
        hourtime6=(TextView)view.findViewById(R.id.hourtime6);
        hourtime6.setTypeface(type);
        hourtime7=(TextView)view.findViewById(R.id.hourtime7);
        hourtime7.setTypeface(type);
        hourtime8=(TextView)view.findViewById(R.id.hourtime8);
        hourtime8.setTypeface(type);
        hourtime9=(TextView)view.findViewById(R.id.hourtime9);
        hourtime9.setTypeface(type);
        hourtime10=(TextView)view.findViewById(R.id.hourtime10);
        hourtime10.setTypeface(type);
        hourtime11=(TextView)view.findViewById(R.id.hourtime11);
        hourtime11.setTypeface(type);
        hourtime12=(TextView)view.findViewById(R.id.hourtime12);
        hourtime12.setTypeface(type);
        hourtimebottom1=(TextView)view.findViewById(R.id.hourtimebottom1);
        hourtimebottom1.setTypeface(type);
        hourtimebottom2=(TextView)view.findViewById(R.id.hourtimebottom2);
        hourtimebottom2.setTypeface(type);
        hourtimebottom3=(TextView)view.findViewById(R.id.hourtimebottom3);
        hourtimebottom3 .setTypeface(type);
        hourtimebottom4=(TextView)view.findViewById(R.id.hourtimebottom4);
        hourtimebottom4 .setTypeface(type);
        hourtimebottom5=(TextView)view.findViewById(R.id.hourtimebottom5);
        hourtimebottom5.setTypeface(type);
        hourtimebottom6=(TextView)view.findViewById(R.id.hourtimebottom6);
        hourtimebottom6 .setTypeface(type);
        hourtimebottom7=(TextView)view.findViewById(R.id.hourtimebottom7);
        hourtimebottom7.setTypeface(type);
        hourtimebottom8=(TextView)view.findViewById(R.id.hourtimebottom8);
        hourtimebottom8.setTypeface(type);
        hourtimebottom9=(TextView)view.findViewById(R.id.hourtimebottom9);
        hourtimebottom9.setTypeface(type);
        hourtimebottom10=(TextView)view.findViewById(R.id.hourtimebottom10);
        hourtimebottom10 .setTypeface(type);
        hourtimebottom11=(TextView)view.findViewById(R.id.hourtimebottom11);
        hourtimebottom11.setTypeface(type);
        hourtimebottom12=(TextView)view.findViewById(R.id.hourtimebottom12);
        hourtimebottom12 .setTypeface(type);

        hourtype1=(TextView)view.findViewById(R.id.hourtype1);
        hourtype1.setTypeface(type);
        hourtype2=(TextView)view.findViewById(R.id.hourtype2);
        hourtype2.setTypeface(type);
        hourtype3=(TextView)view.findViewById(R.id.hourtype3);
        hourtype3.setTypeface(type);
        hourtype4=(TextView)view.findViewById(R.id.hourtype4);
        hourtype4.setTypeface(type);
        hourtype5=(TextView)view.findViewById(R.id.hourtype5);
        hourtype5.setTypeface(type);
        hourtype6=(TextView)view.findViewById(R.id.hourtype6);
        hourtype6.setTypeface(type);
        hourtype7=(TextView)view.findViewById(R.id.hourtype7);
        hourtype7.setTypeface(type);
        hourtype8=(TextView)view.findViewById(R.id.hourtype8);
        hourtype8 .setTypeface(type);
        hourtype9=(TextView)view.findViewById(R.id.hourtype9);
        hourtype9 .setTypeface(type);
        hourtype10=(TextView)view.findViewById(R.id.hourtype10);
        hourtype10.setTypeface(type);
        hourtype11=(TextView)view.findViewById(R.id.hourtype11);
        hourtype11.setTypeface(type);
        hourtype12=(TextView)view.findViewById(R.id.hourtype12);
        hourtype12 .setTypeface(type);

        hourtypebottom1=(TextView)view.findViewById(R.id.hourtypebottom1);
        hourtypebottom1.setTypeface(type);
        hourtypebottom2=(TextView)view.findViewById(R.id.hourtypebottom2);
        hourtypebottom2 .setTypeface(type);
        hourtypebottom3=(TextView)view.findViewById(R.id.hourtypebottom3);
        hourtypebottom3.setTypeface(type);
        hourtypebottom4=(TextView)view.findViewById(R.id.hourtypebottom4);
        hourtypebottom4.setTypeface(type);
        hourtypebottom5=(TextView)view.findViewById(R.id.hourtypebottom5);
        hourtypebottom5 .setTypeface(type);
        hourtypebottom6=(TextView)view.findViewById(R.id.hourtypebottom6);
        hourtypebottom6.setTypeface(type);
        hourtypebottom7=(TextView)view.findViewById(R.id.hourtypebottom7);
        hourtypebottom7.setTypeface(type);
        hourtypebottom8=(TextView)view.findViewById(R.id.hourtypebottom8);
        hourtypebottom8.setTypeface(type);
        hourtypebottom9=(TextView)view.findViewById(R.id.hourtypebottom9);
        hourtypebottom9.setTypeface(type);
        hourtypebottom10=(TextView)view.findViewById(R.id.hourtypebottom10);
        hourtypebottom10.setTypeface(type);
        hourtypebottom11=(TextView)view.findViewById(R.id.hourtypebottom11);
        hourtypebottom11.setTypeface(type);
        hourtypebottom12=(TextView)view.findViewById(R.id.hourtypebottom12);
        hourtypebottom12.setTypeface(type);
//
        weekTopText1=(Button)view.findViewById(R.id.topweektext1);
        weekTopText1.setTypeface(type);
        weekTopText2=(Button)view.findViewById(R.id.topweektext2);
        weekTopText2 .setTypeface(type);
        weekTopText3=(Button)view.findViewById(R.id.topweektext3);
        weekTopText3 .setTypeface(type);
        weekTopText4=(Button)view.findViewById(R.id.topweektext4);
        weekTopText4.setTypeface(type);
        weekTopText5=(Button)view.findViewById(R.id.topweektext5);
        weekTopText5.setTypeface(type);
        weekTopText6=(Button)view.findViewById(R.id.topweektext6);
        weekTopText6.setTypeface(type);
        weekTopText7=(Button)view.findViewById(R.id.topweektext7);
        weekTopText7 .setTypeface(type);
        weekbottomText1=(Button)view.findViewById(R.id.bottomweektext1);
        weekbottomText1 .setTypeface(type);
        weekbottomText2=(Button)view.findViewById(R.id.bottomweektext2);
        weekbottomText2 .setTypeface(type);
        weekbottomText3=(Button)view.findViewById(R.id.bottomweektext3);
        weekbottomText3 .setTypeface(type);
        weekbottomText4=(Button)view.findViewById(R.id.bottomweektext4);
        weekbottomText4 .setTypeface(type);
        weekbottomText5=(Button)view.findViewById(R.id.bottomweektext5);
        weekbottomText5 .setTypeface(type);
        weekbottomText6=(Button)view.findViewById(R.id.bottomweektext6);
        weekbottomText6.setTypeface(type);
        weekbottomText7=(Button)view.findViewById(R.id.bottomweektext7);
        weekbottomText7 .setTypeface(type);
    }

    //User Interaction
    private void loadHomeButton() {
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RealTimeActivity)getActivity()).opendr();

            }
        });
    }

    private void loadBigCircleCLick(){
        bighisclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RealTimeActivity)getActivity()).logevent("Dashboard_BigCircle",totalTag.getText().toString(),"Touch Event");;
                ((RealTimeActivity)getActivity()).loadHistory();
            }
        });
    }

    private void loadSpinner() {
        List<String> titles=new ArrayList<String>();
        titles.add("Today");
        titles.add("This week");
        titles.add("This month");
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(getActivity(),R.layout.spinner_default,titles);
        dataAdapter.setDropDownViewResource(R.layout.spinner_text);
        dashboardpicker.setAdapter(dataAdapter);
        dashboardpicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                currentSpanSelected=position;
                if(currentSpanSelected==2){
                    linetoremove.setVisibility(View.INVISIBLE);
                }else{{
                    linetoremove.setVisibility(View.VISIBLE);
                }}
                switch (currentSpanSelected){
                    case 0:
                        ((RealTimeActivity)getActivity()).logevent("Dashboard_TimePeriod","Today","Touch Event");
                        showHour();
                        setTitleTag();
                        break;
                    case 1:
                        ((RealTimeActivity)getActivity()).logevent("Dashboard_TimePeriod","This week","Touch Event");
                        showWeek();
                        setTitleTag();
                        break;
                    case 2:
                        ((RealTimeActivity)getActivity()).logevent("Dashboard_TimePeriod","This Month","Touch Event");
                        showMonth();
                        break;
                }
                showValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ddown.setClickable(true);
        ddown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardpicker.performClick();
            }
        });
    }

    private void setTitleTag(){
        if(meterMapList==null){
            return;
        }
        if(meterMapList.size()==1){
            if(meterstyleselected==-1){
                meterMapList1=meterMapList;
                totalTag.setText(meterMapList.get(0).get("location").toString());
//                        Toast.makeText(getActivity(),"Showing all meters",Toast.LENGTH_SHORT).show();
            }else{
                meterMapList1.add(meterMapList.get(meterstyleselected));
                totalTag.setText(meterMapList.get(meterstyleselected).get("location").toString());
//                        Toast.makeText(getActivity(),"Showing "+meterList.get(meterstyleselected).getLocation()+" meter",Toast.LENGTH_SHORT).show();
            }
        }else {
            if(meterstyleselected==-1){
                meterMapList1=meterMapList;
//                        String sss=getActivity().getSharedPreferences("userdaetails",Context.MODE_PRIVATE).getString("mobile","8971281004");
                totalTag.setText("Total");
//                        Toast.makeText(getActivity(),"Showing all meters",Toast.LENGTH_SHORT).show();
            }else{
                meterMapList1.add(meterMapList.get(meterstyleselected));
                totalTag.setText(meterMapList.get(meterstyleselected).get("location").toString());
//                        Toast.makeText(getActivity(),"Showing "+meterList.get(meterstyleselected).getLocation()+" meter",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadAlertButton(){
        alert_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RealTimeActivity)getActivity()).logevent("Dashboard_ActiveAlert", String.valueOf(activealarmids.size()),"Touch Event");;
                ((RealTimeActivity)getActivity()).loadAlert();
            }
        });
    }

    private void loadTouchBar(){
        touchBar.setTouchBarCommunicator(this);
    }

    private void currencyToggle(){
        Typeface typeface1=Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_regular.ttf");
        Typeface typeface2=Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_light.ttf");
        if(currencySelected){
            ((RealTimeActivity)getActivity()).logevent("Dashboard_Toggle",rupeefliptext.getText().toString(),"Touch Event");
            rupeefliptext.setTypeface(typeface1);
            litresfliptext.setTypeface(typeface2);
            flipswitch.setGravity(Gravity.TOP);
        }else{
            ((RealTimeActivity)getActivity()).logevent("Dashboard_Toggle",litresfliptext.getText().toString(),"Touch Event");
            rupeefliptext.setTypeface(typeface2);
            litresfliptext.setTypeface(typeface1);
            flipswitch.setGravity(Gravity.BOTTOM);
        }
        showValue();
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("defaults_pref", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("currencySelected",currencySelected).apply();
    }

    private void loadDialSwitcher() {
        prevmeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rightSwipe();
            }
        });

        nextmeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                leftWipe();
            }
        });
    }

    public void leftWipe(){
        Log.d("left","clicked");
        if(meterMapList.size()>1){

            if(meterstyleselected<meterMapList.size()-1){
                meterstyleselected++;
                Log.d("meterselecter", String.valueOf(meterstyleselected));
                ((RealTimeActivity)getActivity()).logevent("Dashboard_H_Swipe",meterMapList.get(meterstyleselected).get("location").toString(),"Touch Event");
                main_animation_holder.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_out_left));
                loadvalues();
                showValue();
                main_animation_holder.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_right));
            }
        }

    }

    public void rightSwipe(){
        Log.d("right","clicked");
        if(meterMapList.size()>1){

            if(meterstyleselected>=0){

                meterstyleselected--;
                Log.d("meterselecter", String.valueOf(meterstyleselected));
                if(meterstyleselected==-1){
                    ((RealTimeActivity)getActivity()).logevent("Dashboard_H_Swipe","Total","Touch Event");
                }else{
                    ((RealTimeActivity)getActivity()).logevent("Dashboard_H_Swipe",meterMapList.get(meterstyleselected).get("location").toString(),"Touch Event");
                }

                main_animation_holder.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_out_right));
                loadvalues();
                showValue();
                main_animation_holder.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_left));
            }
        }

    }

    private void loadFlipSwitch() {
        flipswitchbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currencySelected=!currencySelected;
                currencyToggle();

            }
        });
        flipswitchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currencySelected=!currencySelected;
                currencyToggle();

            }
        });
        rupeefliptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currencySelected=true;
                currencyToggle();

            }
        });
        litresfliptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currencySelected=false;
                currencyToggle();

            }
        });
    }

    private void showMonth() {
        monthtag.setVisibility(View.VISIBLE);
        smalldot.setVisibility(View.GONE);
        bigdot.setVisibility(View.GONE);
        touchBar.setVisibility(View.GONE);
        weekViewTop.setVisibility(View.GONE);
        weekViewBot.setVisibility(View.GONE);
        monthview.setVisibility(View.VISIBLE);
        hourviewtop.setVisibility(View.GONE);
        hourViewBot.setVisibility(View.GONE);
        bartag2.setVisibility(View.GONE);
    }

    private void showHour() {
        monthtag.setVisibility(View.GONE);
        touchBar.setVisibility(View.VISIBLE);
        touchBar.changeStyle(0);
        smalldot.setVisibility(View.VISIBLE);
        bigdot.setVisibility(View.VISIBLE);
        weekViewTop.setVisibility(View.GONE);
        weekViewBot.setVisibility(View.GONE);
        monthview.setVisibility(View.GONE);
        hourviewtop.setVisibility(View.VISIBLE);
        hourViewBot.setVisibility(View.VISIBLE);
        bartag2.setVisibility(View.VISIBLE);
    }

    private void showWeek() {
        monthtag.setVisibility(View.GONE);
        smalldot.setVisibility(View.VISIBLE);
        bigdot.setVisibility(View.VISIBLE);
        touchBar.setVisibility(View.VISIBLE);
        touchBar.changeStyle(1);
        weekViewTop.setVisibility(View.VISIBLE);
        weekViewBot.setVisibility(View.VISIBLE);
        monthview.setVisibility(View.GONE);
        hourviewtop.setVisibility(View.GONE);
        hourViewBot.setVisibility(View.GONE);
        bartag2.setVisibility(View.VISIBLE);
    }
    RealTimeLoader loader;
    boolean loaderNotInit = true;
    boolean isListneing = false;
    AlertDialog alertDialog;
    @Override
    public void onResume() {
        super.onResume();
//        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
//        builder.setMessage("Loading");
//        builder.setCancelable(false);
//        alertDialog = builder.create();
//        alertDialog.show();
        String[] mobile = LoginHandler.getUserMobile(getContext());
        if(loaderNotInit){
            loader = new RealTimeLoader(this,mobile[0],mobile[1]);
        }
        if(!isListneing){
            loader.loadUser();
        }
    }


    public void loadData() {
        if(alertDialog!=null){
            if(alertDialog.isShowing()){
                alertDialog.dismiss();
            }
        }
//        apartments = dataHelper.loadApartments();
//        if(apartments != null && apartments.size()!=0){
//            loadDefaults();
//            loadMenuSpinner();
            loadvalues();
            loadslabs();
            setApartmentValues();
            showValue();
//        }else{
//            // malformed apartment data
//        }
    }

    private void loadslabs() {
        slabsList = new ArrayList<>();
        try {
            JSONArray slab= new JSONArray(selectedApt.get("slabs").toString());
            JSONArray slabrate= new JSONArray(selectedApt.get("slab_rates").toString());

            for(int i=0;i<slab.length();i++){
                Slabs slabs=new Slabs();
                slabs.setSlabKl(slab.getDouble(i));
                slabs.setSlabPrice(slabrate.getDouble(i));
                slabs.setSlabLevel(i);
                slabs.setApTid(selectedAptId);
                slabsList.add(slabs);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setApartmentValues() {
        String billtext = selectedApt.get("bill").toString();
        String paid = "";
        String billam = "";
        String dateb = "";
        try {
            JSONObject bi = new JSONObject(billtext);
            if(bi.has("paid"))
                paid = bi.getString("paid");
            if(bi.has("amount"))
                billam = String.valueOf(bi.getInt("amount"));
            if(bi.has("date"))
                dateb = bi.getString("date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        billAmount.setText(billam);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat sdf2=new SimpleDateFormat("dd MMM yyyy",Locale.ENGLISH);
        String billdate="-";
        try {
            billdate=sdf2.format(sdf.parse(dateb));

        } catch (ParseException e) {

            e.printStackTrace();
        }
        rupeefliptext.setText(userData.get("currency_text").toString());
        litresfliptext.setText(userData.get("unit_text").toString());
        billDate.setText(billdate);
        if(paid.equals("Yes")){
            billStatus.setText("Paid");
            billAmountCurrency.setTextColor(getResources().getColor(R.color.billpaid));
            billAmount.setTextColor(getResources().getColor(R.color.billpaid));
            billDate.setTextColor(getResources().getColor(R.color.billpaid));
            billStatus.setBackgroundResource(R.drawable.bill_status_paid);
        }else{
            billStatus.setText("Due date");
            billAmountCurrency.setTextColor(getResources().getColor(R.color.billunpaid));
            billAmount.setTextColor(getResources().getColor(R.color.billunpaid));
            billDate.setTextColor(getResources().getColor(R.color.billunpaid));
            billStatus.setBackgroundResource(R.drawable.bill_status);
        }
        billAmountCurrency.setText(String.valueOf(Character.toChars(Integer.parseInt(String.valueOf(userData.get("currency_symbol"))))));
//        Log.d("csy",String.valueOf(selectedApartment.getCurrencySymbol()));
    }

    private void setValues() {

    }

    private void loadMenuSpinner() {
        List<String> aptnames=new ArrayList<>();
        List<Integer> aptids=new ArrayList<>();
        int select =0;
        int c =0;
        if(apartmentMapList!=null &&apartmentMapList.size()!=0){
            for(Map<String,Object> a:apartmentMapList){
                aptids.add(Integer.parseInt( a.get("id").toString()));
                aptnames.add((String) a.get("name"));
                if(selectedAptId == Integer.parseInt( a.get("id").toString())){
                    select=c;
                }
                c++;
            }
            ((RealTimeActivity)getActivity()).loadSpinnerView(aptnames,select,aptids);
        }
    }

    private void loadDefaults() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("defaults_pref",Context.MODE_PRIVATE);
        selectedAptId = sharedPreferences.getInt("apartmentIdSelected",-1);
        if(selectedAptId == -1){
            sharedPreferences.edit()
                    .putInt("apartmentIdSelected",apartments.get(0).getId())
                    .apply();
            selectedAptId = apartments.get(0).getId();
        }
        currencySelected = sharedPreferences.getBoolean("currencySelected",false);
        currencyToggle();
        loadDataValue();
    }

    private void loadDataValue() {
        meterMapList=new ArrayList<>();
        selectedApartment = dataHelper.getApartment(selectedAptId);
//        meterList=dataHelper.getMeterForApartment(selectedAptId);
    }

    private void loadMonthSlots() {

        month1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maxmonthslot>=0){
                    removeMonthSlotBackground(currentSelectedMonthSlot);

                    currentSelectedMonthSlot=0;
                    addMonthSlotBackground(currentSelectedMonthSlot);

                    showValue();
                }
            }
        });
        month2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maxmonthslot>=1){
                    removeMonthSlotBackground(currentSelectedMonthSlot);

                    currentSelectedMonthSlot=1;
                    addMonthSlotBackground(currentSelectedMonthSlot);

                    showValue();
                }
            }
        });
        month3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maxmonthslot>=2){
                    removeMonthSlotBackground(currentSelectedMonthSlot);

                    currentSelectedMonthSlot=2;
                    addMonthSlotBackground(currentSelectedMonthSlot);

                    showValue();
                }
            }
        });

    }



    @Override
    public void barTouched(int style, int slot) {
        Log.d("Bar","barTouched "+String.valueOf(style)+"-"+String.valueOf(slot));
        switch (style){
            case 0:
                currentSelectedHourSlot=slot;
                showValue();
                showActivatedSlotHour();

                break;
            case 1:
                currentSelectedWeekSlot=slot;
                showValue();
                showActivatedSlotWeek();
                break;
        }
    }

    @Override
    public void barEntered(int style, int slot) {
        Log.d("Bar","barEntered "+String.valueOf(style)+"-"+String.valueOf(slot));
        switch (style){
            case 0:
                currentSelectedHourSlot=slot;
                showValue();
                break;
            case 1:
                currentSelectedWeekSlot=slot;
                showValue();
                break;
        }
    }

    @Override
    public void barExcitedd(int style, int slot) {
        Log.d("Bar","barExcitedd "+String.valueOf(style)+"-"+String.valueOf(slot));
        switch (style){
            case 0:
                currentSelectedHourSlot=slot;
                showValue();
                ((RealTimeActivity)getActivity()).logevent("Dashboard_Slider","Hour"+String.valueOf(currentSelectedHourSlot),"Touch Event");
                showActivatedSlotHour();
                showselectedhourslot(currentSelectedHourSlot);
                break;
            case 1:
                currentSelectedWeekSlot=slot;
                ((RealTimeActivity)getActivity()).logevent("Dashboard_Slider","Week"+String.valueOf(currentSelectedWeekSlot),"Touch Event");
                showValue();
                showActivatedSlotWeek();
                showSelectedSlotWeek(currentSelectedWeekSlot);
                break;
        }
    }

    private void showSelectedSlotWeek(int slot){
        Typeface typeface1=Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_regular.ttf");
        switch (slot){
            case 0:
                weekTopText1.setTypeface(typeface1);
                weekbottomText1 .setTypeface(typeface1);
                weekbottomText1.setTextColor(getResources().getColor(R.color.white));
                weekTopText1.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                weekbottomText2 .setTypeface(typeface1);
                weekTopText2 .setTypeface(typeface1);
                weekbottomText2.setTextColor(getResources().getColor(R.color.white));
                weekTopText2.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                weekbottomText3.setTypeface(typeface1);
                weekTopText3 .setTypeface(typeface1);
                weekbottomText3.setTextColor(getResources().getColor(R.color.white));
                weekTopText3.setTextColor(getResources().getColor(R.color.white));
                break;
            case 3:
                weekTopText4 .setTypeface(typeface1);
                weekbottomText4 .setTypeface(typeface1);
                weekbottomText4.setTextColor(getResources().getColor(R.color.white));
                weekTopText4.setTextColor(getResources().getColor(R.color.white));
                break;
            case 4:
                weekbottomText5 .setTypeface(typeface1);
                weekTopText5.setTypeface(typeface1);
                weekbottomText5.setTextColor(getResources().getColor(R.color.white));
                weekTopText5.setTextColor(getResources().getColor(R.color.white));
                break;
            case 5:
                weekTopText6.setTypeface(typeface1);
                weekbottomText6 .setTypeface(typeface1);
                weekbottomText6.setTextColor(getResources().getColor(R.color.white));
                weekTopText6.setTextColor(getResources().getColor(R.color.white));
                break;
            case 6:
                weekbottomText7 .setTypeface(typeface1);
                weekTopText7  .setTypeface(typeface1);
                weekbottomText7.setTextColor(getResources().getColor(R.color.white));
                weekTopText7.setTextColor(getResources().getColor(R.color.white));
                break;

        }
    }

    private void showActivatedSlotWeek(){
        for(int i=0;i<=maxweekslot;i++){
            Typeface typeface1=Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_light.ttf");
            switch (i){
                case 0:
                    weekTopText1.setTypeface(typeface1);
                    weekbottomText1.setTypeface(typeface1);

                    weekbottomText1.setTextColor(getResources().getColor(R.color.headercolor));
                    weekTopText1.setTextColor(getResources().getColor(R.color.white));
                    break;
                case 1:
                    weekbottomText2.setTypeface(typeface1);
                    weekTopText2.setTypeface(typeface1);
                    weekbottomText2.setTextColor(getResources().getColor(R.color.headercolor));
                    weekTopText2.setTextColor(getResources().getColor(R.color.white));
                    break;
                case 2:
                    weekbottomText3 .setTypeface(typeface1);
                    weekTopText3 .setTypeface(typeface1);
                    weekbottomText3.setTextColor(getResources().getColor(R.color.headercolor));
                    weekTopText3.setTextColor(getResources().getColor(R.color.white));
                    break;
                case 3:
                    weekbottomText4  .setTypeface(typeface1);
                    weekTopText4 .setTypeface(typeface1);

                    weekbottomText4.setTextColor(getResources().getColor(R.color.headercolor));
                    weekTopText4.setTextColor(getResources().getColor(R.color.white));
                    break;
                case 4:
                    weekbottomText5 .setTypeface(typeface1);
                    weekTopText5 .setTypeface(typeface1);
                    weekbottomText5.setTextColor(getResources().getColor(R.color.headercolor));
                    weekTopText5.setTextColor(getResources().getColor(R.color.white));
                    break;
                case 5:
                    weekbottomText6.setTypeface(typeface1);
                    weekTopText6.setTypeface(typeface1);
                    weekbottomText6.setTextColor(getResources().getColor(R.color.headercolor));
                    weekTopText6.setTextColor(getResources().getColor(R.color.white));
                    break;
                case 6:
                    weekTopText7.setTypeface(typeface1);
                    weekbottomText7.setTypeface(typeface1);
                    weekbottomText7.setTextColor(getResources().getColor(R.color.headercolor));
                    weekTopText7.setTextColor(getResources().getColor(R.color.white));
                    break;

            }
        }
        for(int i=maxweekslot+1;i<7;i++){
            switch (i){
                case 0:
                    weekbottomText1.setTextColor(getResources().getColor(R.color.greytext));
                    weekTopText1.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                case 1:
                    weekbottomText2.setTextColor(getResources().getColor(R.color.greytext));
                    weekTopText2.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                case 2:
                    weekbottomText3.setTextColor(getResources().getColor(R.color.greytext));
                    weekTopText3.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                case 3:
                    weekbottomText4.setTextColor(getResources().getColor(R.color.greytext));
                    weekTopText4.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                case 4:
                    weekbottomText5.setTextColor(getResources().getColor(R.color.greytext));
                    weekTopText5.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                case 5:
                    weekbottomText6.setTextColor(getResources().getColor(R.color.greytext));
                    weekTopText6.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                case 6:
                    weekbottomText7.setTextColor(getResources().getColor(R.color.greytext));
                    weekTopText7.setTextColor(getResources().getColor(R.color.greytext));
                    break;

            }
        }
    }

    private void showselectedhourslot(int slot){

        Typeface typeface1=Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_regular.ttf");
        switch (slot){
            case 0:
                hourtime1.setTypeface(typeface1);
                hourtime1.setTextColor(getResources().getColor(R.color.white));
                hourtype1.setTextColor(getResources().getColor(R.color.headercolor));
                hourtimebottom1.setTextColor(getResources().getColor(R.color.white));
                hourtimebottom1.setTypeface(typeface1);
                hourtypebottom1.setTextColor(getResources().getColor(R.color.headercolor));
                break;
            case 1:
                hourtime2.setTextColor(getResources().getColor(R.color.white));
                hourtime2.setTypeface(typeface1);
                hourtype2.setTextColor(getResources().getColor(R.color.headercolor));
                hourtimebottom2.setTextColor(getResources().getColor(R.color.white));
                hourtimebottom2.setTypeface(typeface1);
                hourtypebottom2.setTextColor(getResources().getColor(R.color.headercolor));
                break;
            case 2:
                hourtime3.setTextColor(getResources().getColor(R.color.white));
                hourtime3.setTypeface(typeface1);
                hourtype3.setTextColor(getResources().getColor(R.color.headercolor));
                hourtimebottom3.setTextColor(getResources().getColor(R.color.white));
                hourtimebottom3.setTypeface(typeface1);
                hourtypebottom3.setTextColor(getResources().getColor(R.color.headercolor));
                break;
            case 3:
                hourtime4.setTextColor(getResources().getColor(R.color.white));
                hourtime4.setTypeface(typeface1);
                hourtype4.setTextColor(getResources().getColor(R.color.headercolor));
                hourtimebottom4.setTextColor(getResources().getColor(R.color.white));
                hourtimebottom4.setTypeface(typeface1);
                hourtypebottom4.setTextColor(getResources().getColor(R.color.headercolor));
                break;
            case 4:
                hourtime5.setTextColor(getResources().getColor(R.color.white));
                hourtime5.setTypeface(typeface1);
                hourtype5.setTextColor(getResources().getColor(R.color.headercolor));
                hourtimebottom5.setTextColor(getResources().getColor(R.color.white));
                hourtimebottom5.setTypeface(typeface1);
                hourtypebottom5.setTextColor(getResources().getColor(R.color.headercolor));
                break;
            case 5:
                hourtime6.setTextColor(getResources().getColor(R.color.white));
                hourtime6.setTypeface(typeface1);
                hourtype6.setTextColor(getResources().getColor(R.color.headercolor));
                hourtimebottom6.setTextColor(getResources().getColor(R.color.white));
                hourtimebottom6.setTypeface(typeface1);
                hourtypebottom6.setTextColor(getResources().getColor(R.color.headercolor));
                break;
            case 6:
                hourtime7.setTextColor(getResources().getColor(R.color.white));
                hourtime7.setTypeface(typeface1);
                hourtype7.setTextColor(getResources().getColor(R.color.headercolor));
                hourtimebottom7.setTextColor(getResources().getColor(R.color.white));
                hourtimebottom7.setTypeface(typeface1);
                hourtypebottom7.setTextColor(getResources().getColor(R.color.headercolor));
                break;
            case 7:
                hourtime8.setTextColor(getResources().getColor(R.color.white));
                hourtime8.setTypeface(typeface1);
                hourtype8.setTextColor(getResources().getColor(R.color.headercolor));
                hourtimebottom8.setTextColor(getResources().getColor(R.color.white));
                hourtimebottom8.setTypeface(typeface1);
                hourtypebottom8.setTextColor(getResources().getColor(R.color.headercolor));
                break;
            case 8:
                hourtime9.setTextColor(getResources().getColor(R.color.white));
                hourtime9.setTypeface(typeface1);
                hourtype9.setTextColor(getResources().getColor(R.color.headercolor));
                hourtimebottom9.setTextColor(getResources().getColor(R.color.white));
                hourtimebottom9.setTypeface(typeface1);
                hourtypebottom9.setTextColor(getResources().getColor(R.color.headercolor));
                break;
            case 9:
                hourtime10.setTextColor(getResources().getColor(R.color.white));
                hourtime10.setTypeface(typeface1);
                hourtype10.setTextColor(getResources().getColor(R.color.headercolor));
                hourtimebottom10.setTextColor(getResources().getColor(R.color.white));
                hourtimebottom10.setTypeface(typeface1);
                hourtypebottom10.setTextColor(getResources().getColor(R.color.headercolor));
                break;
            case 10:
                hourtime11.setTextColor(getResources().getColor(R.color.white));
                hourtime11.setTypeface(typeface1);
                hourtype11.setTextColor(getResources().getColor(R.color.headercolor));
                hourtimebottom11.setTextColor(getResources().getColor(R.color.white));
                hourtimebottom11.setTypeface(typeface1);
                hourtypebottom11.setTextColor(getResources().getColor(R.color.headercolor));
                break;
            case 11:
                hourtime12.setTextColor(getResources().getColor(R.color.white));
                hourtime12 .setTypeface(typeface1);
                hourtype12.setTextColor(getResources().getColor(R.color.headercolor));
                hourtimebottom12.setTextColor(getResources().getColor(R.color.white));
                hourtimebottom12.setTypeface(typeface1);
                hourtypebottom12.setTextColor(getResources().getColor(R.color.headercolor));
                break;

        }
    }

    private void showActivatedSlotHour(){
        for(int i=maxhourlot+1;i<12;i++){
            switch (i){
                case 0:
                    hourtime1.setTextColor(getResources().getColor(R.color.greytext));
                    hourtype1.setTextColor(getResources().getColor(R.color.greytext));
                    hourtimebottom1.setTextColor(getResources().getColor(R.color.greytext));
                    hourtypebottom1.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                case 1:
                    hourtime2.setTextColor(getResources().getColor(R.color.greytext));
                    hourtype2.setTextColor(getResources().getColor(R.color.greytext));
                    hourtimebottom2.setTextColor(getResources().getColor(R.color.greytext));
                    hourtypebottom2.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                case 2:
                    hourtime3.setTextColor(getResources().getColor(R.color.greytext));
                    hourtype3.setTextColor(getResources().getColor(R.color.greytext));
                    hourtimebottom3.setTextColor(getResources().getColor(R.color.greytext));
                    hourtypebottom3.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                case 3:
                    hourtime4.setTextColor(getResources().getColor(R.color.greytext));
                    hourtype4.setTextColor(getResources().getColor(R.color.greytext));
                    hourtimebottom4.setTextColor(getResources().getColor(R.color.greytext));
                    hourtypebottom4.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                case 4:
                    hourtime5.setTextColor(getResources().getColor(R.color.greytext));
                    hourtype5.setTextColor(getResources().getColor(R.color.greytext));
                    hourtimebottom5.setTextColor(getResources().getColor(R.color.greytext));
                    hourtypebottom5.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                case 5:
                    hourtime6.setTextColor(getResources().getColor(R.color.greytext));
                    hourtype6.setTextColor(getResources().getColor(R.color.greytext));
                    hourtimebottom6.setTextColor(getResources().getColor(R.color.greytext));
                    hourtypebottom6.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                case 6:
                    hourtime7.setTextColor(getResources().getColor(R.color.greytext));
                    hourtype7.setTextColor(getResources().getColor(R.color.greytext));
                    hourtimebottom7.setTextColor(getResources().getColor(R.color.greytext));
                    hourtypebottom7.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                case 7:
                    hourtime8.setTextColor(getResources().getColor(R.color.greytext));
                    hourtype8.setTextColor(getResources().getColor(R.color.greytext));
                    hourtimebottom8.setTextColor(getResources().getColor(R.color.greytext));
                    hourtypebottom8.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                case 8:
                    hourtime9.setTextColor(getResources().getColor(R.color.greytext));
                    hourtype9.setTextColor(getResources().getColor(R.color.greytext));
                    hourtimebottom9.setTextColor(getResources().getColor(R.color.greytext));
                    hourtypebottom9.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                case 9:
                    hourtime10.setTextColor(getResources().getColor(R.color.greytext));
                    hourtype10.setTextColor(getResources().getColor(R.color.greytext));
                    hourtimebottom10.setTextColor(getResources().getColor(R.color.greytext));
                    hourtypebottom10.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                case 10:
                    hourtime11.setTextColor(getResources().getColor(R.color.greytext));
                    hourtype11.setTextColor(getResources().getColor(R.color.greytext));
                    hourtimebottom11.setTextColor(getResources().getColor(R.color.greytext));
                    hourtypebottom11.setTextColor(getResources().getColor(R.color.greytext));
                    break;
                case 11:
                    hourtime12.setTextColor(getResources().getColor(R.color.greytext));
                    hourtype12.setTextColor(getResources().getColor(R.color.greytext));
                    hourtimebottom12.setTextColor(getResources().getColor(R.color.greytext));
                    hourtypebottom12.setTextColor(getResources().getColor(R.color.greytext));
                    break;

            }
        }
        for(int i=0;i<=maxhourlot;i++){
            Typeface typeface1=Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_light.ttf");
            switch (i){
                case 0:
                    hourtime1.setTypeface(typeface1);
                    hourtimebottom1.setTypeface(typeface1);
                    hourtime1.setTextColor(getResources().getColor(R.color.white));
                    hourtype1.setTextColor(getResources().getColor(R.color.white));
                    hourtimebottom1.setTextColor(getResources().getColor(R.color.headercolor));
                    hourtypebottom1.setTextColor(getResources().getColor(R.color.headercolor));
                    break;
                case 1:
                    hourtime2.setTypeface(typeface1);
                    hourtimebottom2.setTypeface(typeface1);
                    hourtime2.setTextColor(getResources().getColor(R.color.white));
                    hourtype2.setTextColor(getResources().getColor(R.color.white));
                    hourtimebottom2.setTextColor(getResources().getColor(R.color.headercolor));
                    hourtypebottom2.setTextColor(getResources().getColor(R.color.headercolor));
                    break;
                case 2:
                    hourtime3 .setTypeface(typeface1);
                    hourtimebottom3.setTypeface(typeface1);
                    hourtime3.setTextColor(getResources().getColor(R.color.white));
                    hourtype3.setTextColor(getResources().getColor(R.color.white));
                    hourtimebottom3.setTextColor(getResources().getColor(R.color.headercolor));
                    hourtypebottom3.setTextColor(getResources().getColor(R.color.headercolor));
                    break;
                case 3:
                    hourtime4.setTypeface(typeface1);
                    hourtimebottom4.setTypeface(typeface1);
                    hourtime4.setTextColor(getResources().getColor(R.color.white));
                    hourtype4.setTextColor(getResources().getColor(R.color.white));
                    hourtimebottom4.setTextColor(getResources().getColor(R.color.headercolor));
                    hourtypebottom4.setTextColor(getResources().getColor(R.color.headercolor));
                    break;
                case 4:
                    hourtime5 .setTypeface(typeface1);
                    hourtimebottom5 .setTypeface(typeface1);
                    hourtime5.setTextColor(getResources().getColor(R.color.white));
                    hourtype5.setTextColor(getResources().getColor(R.color.white));
                    hourtimebottom5.setTextColor(getResources().getColor(R.color.headercolor));
                    hourtypebottom5.setTextColor(getResources().getColor(R.color.headercolor));
                    break;
                case 5:
                    hourtime6.setTypeface(typeface1);
                    hourtimebottom6.setTypeface(typeface1);
                    hourtime6.setTextColor(getResources().getColor(R.color.white));
                    hourtype6.setTextColor(getResources().getColor(R.color.white));
                    hourtimebottom6.setTextColor(getResources().getColor(R.color.headercolor));
                    hourtypebottom6.setTextColor(getResources().getColor(R.color.headercolor));
                    break;
                case 6:
                    hourtime7 .setTypeface(typeface1);
                    hourtimebottom7 .setTypeface(typeface1);
                    hourtime7.setTextColor(getResources().getColor(R.color.white));
                    hourtype7.setTextColor(getResources().getColor(R.color.white));
                    hourtimebottom7.setTextColor(getResources().getColor(R.color.headercolor));
                    hourtypebottom7.setTextColor(getResources().getColor(R.color.headercolor));
                    break;
                case 7:
                    hourtime8.setTypeface(typeface1);
                    hourtimebottom8.setTypeface(typeface1);
                    hourtime8.setTextColor(getResources().getColor(R.color.white));
                    hourtype8.setTextColor(getResources().getColor(R.color.white));
                    hourtimebottom8.setTextColor(getResources().getColor(R.color.headercolor));
                    hourtypebottom8.setTextColor(getResources().getColor(R.color.headercolor));
                    break;
                case 8:
                    hourtime9.setTypeface(typeface1);
                    hourtimebottom9.setTypeface(typeface1);
                    hourtime9.setTextColor(getResources().getColor(R.color.white));
                    hourtype9.setTextColor(getResources().getColor(R.color.white));
                    hourtimebottom9.setTextColor(getResources().getColor(R.color.headercolor));
                    hourtypebottom9.setTextColor(getResources().getColor(R.color.headercolor));
                    break;
                case 9:
                    hourtime10.setTypeface(typeface1);
                    hourtimebottom10.setTypeface(typeface1);
                    hourtime10.setTextColor(getResources().getColor(R.color.white));
                    hourtype10.setTextColor(getResources().getColor(R.color.white));
                    hourtimebottom10.setTextColor(getResources().getColor(R.color.headercolor));
                    hourtypebottom10.setTextColor(getResources().getColor(R.color.headercolor));
                    break;
                case 10:
                    hourtime11.setTypeface(typeface1);
                    hourtimebottom11.setTypeface(typeface1);
                    hourtime11.setTextColor(getResources().getColor(R.color.white));
                    hourtype11.setTextColor(getResources().getColor(R.color.white));
                    hourtimebottom11.setTextColor(getResources().getColor(R.color.headercolor));
                    hourtypebottom11.setTextColor(getResources().getColor(R.color.headercolor));
                    break;
                case 11:
                    hourtime12.setTypeface(typeface1);
                    hourtimebottom12.setTypeface(typeface1);
                    hourtime12.setTextColor(getResources().getColor(R.color.white));
                    hourtype12.setTextColor(getResources().getColor(R.color.white));
                    hourtimebottom12.setTextColor(getResources().getColor(R.color.headercolor));
                    hourtypebottom12.setTextColor(getResources().getColor(R.color.headercolor));
                    break;

            }
        }
    }

    private void loadDefaultValues() {
        // Log.d("print","values");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MMM",Locale.ENGLISH);
        SimpleDateFormat sdf3=new SimpleDateFormat("EEE",Locale.ENGLISH);
        SimpleDateFormat sdf4=new SimpleDateFormat("MMM",Locale.ENGLISH);

        monthdates=new ArrayList<String>();
        String thismonth=sdf2.format(new Date());
        int year=Integer.parseInt(thismonth.split("-")[0]);
        switch (sdf4.format(new Date())){
            case "Jan":
                try {
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year-1)+"-"+"Nov" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year-1)+"-"+"Dec" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Jan" ) ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case "Feb":
                try {
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year-1)+"-"+"Dec" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Jan" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Feb" ) ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case "Mar":
                try {
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Jan" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Feb" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Mar" ) ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case "Apr":
                try {
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Feb" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Mar" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Apr" ) ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case "May":
                try {
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Mar" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Apr" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"May" ) ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case "Jun":
                try {
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Apr" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"May" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Jun" ) ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case "Jul":
                try {
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"May" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Jun" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Jul" ) ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case "Aug":
                try {
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Jun" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Jul" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Aug" ) ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case "Sep":
                try {
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Jul" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Aug" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Sep" ) ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case "Oct":
                try {
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Aug" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Sep" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Oct" ) ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case "Nov":
                try {
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Sep" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Oct" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Nov" ) ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case "Dec":
                try {
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Oct" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Nov" ) ));
                    monthdates.add(sdf.format( sdf2.parse(String.valueOf(year)+"-"+"Dec" ) ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;

        }

        int today=0;
        switch (sdf3.format(new Date())){
            case "Sun":
                today=0;
                break;
            case "Mon":
                today=1;
                break;
            case "Tue":
                today=2;
                break;
            case "Wed":
                today=3;
                break;
            case "Thu":
                today=4;
                break;
            case "Fri":
                today=5;
                break;
            case "Sat":
                today=6;
                break;
        }
        long currenttime=new Date().getTime();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH-mm", Locale.ENGLISH);
        try {
            String todaydate=sdf.format(new Date());

            if(currenttime>new Date(simpleDateFormat.parse(todaydate+" "+"02-00").getTime()).getTime()){
                currentSelectedHourSlot=0;
                maxhourlot=0;
                ((RealTimeActivity)getActivity()).timecreated=new Date(simpleDateFormat.parse(todaydate+" "+"02-00").getTime()).getTime();
            }

            if(currenttime>new Date(simpleDateFormat.parse(todaydate+" "+"04-00").getTime()).getTime()){
                currentSelectedHourSlot=1;
                maxhourlot=1;
                ((RealTimeActivity)getActivity()).timecreated=new Date(simpleDateFormat.parse(todaydate+" "+"04-00").getTime()).getTime();
            }
            if(currenttime>new Date(simpleDateFormat.parse(todaydate+" "+"06-00").getTime()).getTime()){
                currentSelectedHourSlot=2;
                maxhourlot=2;
                ((RealTimeActivity)getActivity()).timecreated=new Date(simpleDateFormat.parse(todaydate+" "+"06-00").getTime()).getTime();
            }
            if(currenttime>new Date(simpleDateFormat.parse(todaydate+" "+"08-00").getTime()).getTime()){
                currentSelectedHourSlot=3;
                maxhourlot=3;
                ((RealTimeActivity)getActivity()).timecreated=new Date(simpleDateFormat.parse(todaydate+" "+"08-00").getTime()).getTime();
            }
            if(currenttime>new Date(simpleDateFormat.parse(todaydate+" "+"10-00").getTime()).getTime()){
                currentSelectedHourSlot=4;
                maxhourlot=4;
                ((RealTimeActivity)getActivity()).timecreated=new Date(simpleDateFormat.parse(todaydate+" "+"10-00").getTime()).getTime();
            }
            if(currenttime>new Date(simpleDateFormat.parse(todaydate+" "+"12-00").getTime()).getTime()){
                currentSelectedHourSlot=5;
                maxhourlot=5;
                ((RealTimeActivity)getActivity()).timecreated=new Date(simpleDateFormat.parse(todaydate+" "+"12-00").getTime()).getTime();
            }
            if(currenttime>new Date(simpleDateFormat.parse(todaydate+" "+"14-00").getTime()).getTime()){
                currentSelectedHourSlot=6;
                maxhourlot=6;
                ((RealTimeActivity)getActivity()).timecreated=new Date(simpleDateFormat.parse(todaydate+" "+"14-00").getTime()).getTime();
            }
            if(currenttime>new Date(simpleDateFormat.parse(todaydate+" "+"16-00").getTime()).getTime()){
                currentSelectedHourSlot=7;
                maxhourlot=7;
                ((RealTimeActivity)getActivity()).timecreated=new Date(simpleDateFormat.parse(todaydate+" "+"16-00").getTime()).getTime();
            }
            if(currenttime>new Date(simpleDateFormat.parse(todaydate+" "+"18-00").getTime()).getTime()){
                currentSelectedHourSlot=8;
                maxhourlot=8;
                ((RealTimeActivity)getActivity()).timecreated=new Date(simpleDateFormat.parse(todaydate+" "+"18-00").getTime()).getTime();
            }
            if(currenttime>new Date(simpleDateFormat.parse(todaydate+" "+"20-00").getTime()).getTime()){
                currentSelectedHourSlot=9;
                maxhourlot=9;
                ((RealTimeActivity)getActivity()).timecreated=new Date(simpleDateFormat.parse(todaydate+" "+"20-00").getTime()).getTime();
            }
            if(currenttime>new Date(simpleDateFormat.parse(todaydate+" "+"22-00").getTime()).getTime()){
                currentSelectedHourSlot=10;
                maxhourlot=10;
                ((RealTimeActivity)getActivity()).timecreated=new Date(simpleDateFormat.parse(todaydate+" "+"22-00").getTime()).getTime();
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Exception 1", Toast.LENGTH_SHORT).show();
        }
        showActivatedSlotHour();

        showselectedhourslot(currentSelectedHourSlot);
        maxmonthslot=2;
        maxweekslot=today;
        showActivatedSlotWeek();
        showSelectedSlotWeek(today);
        touchBar.setMaxSlotAvailable(new int[]{maxhourlot,maxweekslot});
        currentSelectedWeekSlot=today;
        currentSelectedMonthSlot=2;
        addMonthSlotBackground(currentSelectedMonthSlot);
        long time=new Date().getTime();
        long day=86400000;

        weekdates=new ArrayList<String>();
        for(int i=0-today;i<7-today;i++){
            weekdates.add(sdf.format(new Date(time+(day*i))));
        }
        showDefaultValues();

    }

    private void showDefaultValues() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        SimpleDateFormat sdf2=new SimpleDateFormat("EEE",Locale.ENGLISH);
        SimpleDateFormat sdf3=new SimpleDateFormat("MMM",Locale.ENGLISH);
        SimpleDateFormat sdf4=new SimpleDateFormat("dd",Locale.ENGLISH);
        try {
            weekTopText1.setText(sdf4.format(sdf.parse(weekdates.get(0))));
            weekTopText2.setText(sdf4.format(sdf.parse(weekdates.get(1))));
            weekTopText3.setText(sdf4.format(sdf.parse(weekdates.get(2))));
            weekTopText4.setText(sdf4.format(sdf.parse(weekdates.get(3))));
            weekTopText5.setText(sdf4.format(sdf.parse(weekdates.get(4))));
            weekTopText6.setText(sdf4.format(sdf.parse(weekdates.get(5))));
            weekTopText7.setText(sdf4.format(sdf.parse(weekdates.get(6))));
            weekbottomText1.setText(sdf2.format(sdf.parse(weekdates.get(0))));
            weekbottomText2.setText(sdf2.format(sdf.parse(weekdates.get(1))));
            weekbottomText3.setText(sdf2.format(sdf.parse(weekdates.get(2))));
            weekbottomText4.setText(sdf2.format(sdf.parse(weekdates.get(3))));
            weekbottomText5.setText(sdf2.format(sdf.parse(weekdates.get(4))));
            weekbottomText6.setText(sdf2.format(sdf.parse(weekdates.get(5))));
            weekbottomText7.setText(sdf2.format(sdf.parse(weekdates.get(6))));

            month1.setText(sdf3.format(sdf.parse(monthdates.get(0))));
            month2.setText(sdf3.format(sdf.parse(monthdates.get(1))));
            month3.setText(sdf3.format(sdf.parse(monthdates.get(2))));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void addMonthSlotBackground(int slot){

        switch (slot){
            case 0:
                month1.setBackgroundResource((R.drawable.month_range_slider));
                break;
            case 1:
                month2.setBackgroundResource((R.drawable.month_range_slider));
                break;
            case 2:
                month3.setBackgroundResource((R.drawable.month_range_slider));
                break;
        }
    }

    private void removeMonthSlotBackground(int slot){
        switch (slot){
            case 0:
                month1.setBackgroundColor(getResources().getColor(R.color.transparent));
                break;
            case 1:
                month2.setBackgroundColor(getResources().getColor(R.color.transparent));
                break;
            case 2:
                month3.setBackgroundColor(getResources().getColor(R.color.transparent));
                break;
        }
    }

    private void enableMeterDot(int m) {

        addhere.removeAllViews();
        if(meterMapList.size()==1){
            prevmeter.setVisibility(View.INVISIBLE);
            nextmeter.setVisibility(View.INVISIBLE);
            addhere.setVisibility(View.INVISIBLE);
        }else {
            if(meterstyleselected==-1){
                prevmeter.setVisibility(View.INVISIBLE);
                nextmeter.setVisibility(View.VISIBLE);
            }else if(meterstyleselected==meterMapList.size()-1){
                prevmeter.setVisibility(View.VISIBLE);
                nextmeter.setVisibility(View.INVISIBLE);
            }else{
                prevmeter.setVisibility(View.VISIBLE);
                nextmeter.setVisibility(View.VISIBLE);
            }

            for(int i=0;i<meterMapList.size()+1;i++){
                ImageView vi=new ImageView(getContext());
                DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
                float dp = 10f;
                float fpixels = metrics.density * dp;
                int pixels = (int) (fpixels + 0.5f);
                vi.setLayoutParams(new ViewGroup.LayoutParams(pixels, pixels-2));
                if(i-1==meterstyleselected){
                    vi.setImageResource(R.drawable.meterdot2);
                    vi.setPadding(2,2,2,2);
                }else{
                    vi.setImageResource(R.drawable.meterdot);
                    vi.setPadding(5,5,5,5);
                }

                vi.setScaleType(ImageView.ScaleType.FIT_CENTER);
                addhere.addView(vi);
            }
        }

    }
    DashDataDates dashDataDates=new DashDataDates();

    private void loadvalues() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-d",Locale.ENGLISH);
        hourlydatalitres=new double[]{0,0,0,0,0,0,0,0,0,0,0,0};
        hourlydatalitresyesterday=new double[]{0,0,0,0,0,0,0,0,0,0,0,0};
        weekldatalitres=new double[]{0,0,0,0,0,0,0};
        weekldatalitrespast=new double[]{0,0,0,0,0,0,0};
        monthdatalitres=new double[]{0,0,0};
        hourlytotal=0;
        hourlytotalyesterday=0;
        weeklytotal=0;
        weeklypasttotal=0;
        monthlytotal=0;
        if(selectedAptId!=-1){
            if(meterMapList!=null){
                meterMapList1=new ArrayList<>();
                enableMeterDot(meterstyleselected);
                if(meterMapList.size()==1){
                    addhere.setVisibility(View.GONE);
                    if(meterstyleselected==-1){
                        meterMapList1=meterMapList;
                        totalTag.setText(meterMapList.get(0).get("location").toString());
                    }else{
                        meterMapList1.add(meterMapList.get(meterstyleselected));
                        totalTag.setText(meterMapList.get(meterstyleselected).get("location").toString());
                    }
                }else {
                    if(meterstyleselected==-1){
                        meterMapList1=meterMapList;
                        totalTag.setText("Total");
                    }else{
                        meterMapList1.add(meterMapList.get(meterstyleselected));
                        totalTag.setText(meterMapList.get(meterstyleselected).get("location").toString());
                    }
                }
                activealarmids.clear();
                mids.clear();


                dashDataDates=new DashDataDates();
                for(int i=0;i<7;i++){
                    dashDataDates.weekldatalitresdates.add(weekdates.get(i));
                }
                for(int i=0;i<7;i++){
                    try {
                        String date=sdf.format(new Date((sdf.parse(weekdates.get(i))).getTime()-(86400000*7)));
                        dashDataDates.weekldatalitrespastdates.add(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                for(int i=0;i<3;i++){
                    String monthstart=monthdates.get(i).split("-")[0]+"-"+monthdates.get(i).split("-")[1];
                    for(int j=0;j<getDayinMonth(monthdates.get(i));j++){
                        try {
                            switch (i){
                                case 0:
                                    dashDataDates.monthdatalitresdates1.add(sdf.format(sdf2.parse(monthstart+"-"+(String.valueOf(j+1)))));
                                    break;
                                case 1:
                                    dashDataDates.monthdatalitresdates2.add(sdf.format(sdf2.parse(monthstart+"-"+(String.valueOf(j+1)))));
                                    break;
                                case  2:
                                    dashDataDates.monthdatalitresdates3.add(sdf.format(sdf2.parse(monthstart+"-"+(String.valueOf(j+1)))));
                                    break;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                dashboardData = new ArrayList<>();
                loader.stopListening();
                for(Map<String,Object> m:meterMapList1){
                    try {
                        JSONArray array=new JSONArray(m.get("alarm_quantity").toString());
                        if(array.length()!=0 && array.getInt(0)!=0){
                            activealarmids.add(m.get("id").toString());
                            mids.add(m.get("location").toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dashboardData.add(new DashboardData());

                    loader.startListeningForMeters(selectedAptId,Integer.parseInt(m.get("id").toString()));
                    Log.d("data","listening"+String.valueOf(m.get("id")));
                }

                if(activealarmids!=null){
                    if(!activealarmids.isEmpty()){
                        if(activealarmids.size()==1){
                            alert_icon_active.setImageResource(R.drawable.alert);
                            alerttext.setTextColor(getResources().getColor(R.color.billunpaid));
                            alerttext.setText(mids.get(0));

                        }else{
                            alert_icon_active.setImageResource(R.drawable.alert);
                            alerttext.setTextColor(getResources().getColor(R.color.billunpaid));
                            alerttext.setText(String.valueOf(activealarmids.size())+" active\nalerts");
                            alerttext.setEnabled(true);
                        }
                    }else{
                        alert_icon_active.setImageResource(R.drawable.no_alert);
                        alerttext.setTextColor(getResources().getColor(R.color.blacktext));
                        alerttext.setText("No active\nalerts");
                        alerttext.setEnabled(false);
                    }
                }else{
                    alert_icon_active.setImageResource(R.drawable.no_alert);
                    alerttext.setTextColor(getResources().getColor(R.color.blacktext));
                    alerttext.setText("No active\nalerts");
                    alerttext.setEnabled(false);
                }
                dataLoaded = true;
            }

        }

    }

    public int getDayinMonth(String s1){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-M-d",Locale.ENGLISH);
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        int daysInMonth=30;
        String s= null;
        try {
            s = sdf.format(sdf2.parse(s1));
            int iYear = Integer.parseInt(s.split("-")[0]);
            int iMonth = Integer.parseInt(s.split("-")[1]);
            int iDay = Integer.parseInt(s.split("-")[2]);
            Calendar mycal = new GregorianCalendar(iYear, iMonth-1, iDay);
            daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  daysInMonth;
    }

    private void showValue(){
        if(!dataLoaded){
            return;
        }
        switch (currentSpanSelected){
            case 0:
                showHourValue();
                break;
            case 1:
                showWeekValue();
                break;
            case 2:
                showMonthValue();
                break;
        }
    }

    private void showMonthValue() {
        switch (currentSelectedMonthSlot){
            case 0:
                monthtag.setText(month1.getText());
                totalTag.setText(month3.getText());
                break;
            case 1:
                monthtag.setText(month2.getText());
                totalTag.setText(month3.getText());
                break;
            case 2:
                monthtag.setText(month3.getText());
                totalTag.setText(month3.getText());
                break;
        }

        if(currencySelected){
            double val=0;
            for(int i=0;i<dashboardData.size();i++){
                val += dashboardData.get(i).monthdatalitres[currentSelectedMonthSlot];
            }
            val =getRupee(val);
            String valtd=String.valueOf((int)val);
            if(val>1000){
                valtd=String.valueOf((int)val);
            }else{
                valtd=String.format("%.2f",val);
            }
            homeSmallText.setText(valtd);
            double val1=0;
            for(int i=0;i<dashboardData.size();i++){
                val1 += dashboardData.get(i).monthdatalitres[2];
            }
            val1 = getRupee(val1);
            String valtd1=String.valueOf((int)val1);
            if(val1>1000){
                valtd1=String.valueOf((int)val1);
            }else{
                valtd1=String.format("%.2f",val1);
            }
            homeBigText.setText(valtd1);

        }else{
            double val=0;
            for(int i=0;i<dashboardData.size();i++){
                val += dashboardData.get(i).monthdatalitres[currentSelectedMonthSlot];
            }
            String valtd="0";
            if(val>1000){
                valtd=String.valueOf((int)val);
            }else{
                valtd=String.format("%.1f",val);
            }
            homeSmallText.setText(valtd);
            double val1=0;
            for(int i=0;i<dashboardData.size();i++){
                val1 += dashboardData.get(i).monthdatalitres[2];
            }
            String valtd1="0";
            if(val1>1000){
                valtd1=String.valueOf((int)val1);
            }else{
                valtd1=String.format("%.1f",val1);
            }
            homeBigText.setText(valtd1);
        }


    }

    private void showWeekValue() {
        double val1=0;
        double val=0;
        double past=0;
        double pastTotal=0;

        if(currencySelected){

            for(int i=0; i<dashboardData.size();i++){
                val1 += dashboardData.get(i).weekldatalitres[currentSelectedWeekSlot];
                past += dashboardData.get(i).weekldatalitrespast[currentSelectedWeekSlot];
                val += dashboardData.get(i).weeklytotal;
                pastTotal += dashboardData.get(i).weeklypasttotal;

            }
            val = getRupee(val);
            val1 = getRupee(val1);
            String valtd1=String.valueOf((int)val1);
            if(val1>1000){
                valtd1=String.valueOf((int)val1);
            }else{
                valtd1=String.format("%.2f",val1);
            }

            String valtd=String.valueOf((int)val);
            if(val>1000){
                valtd=String.valueOf((int)val);
            }else{
                valtd=String.format("%.2f",val);
            }

            homeSmallText.setText(valtd1);
            homeBigText.setText(valtd);
        }else{
            for(int i=0; i<dashboardData.size();i++){
                val1 += dashboardData.get(i).weekldatalitres[currentSelectedWeekSlot];
                past += dashboardData.get(i).weekldatalitrespast[currentSelectedWeekSlot];
                val += dashboardData.get(i).weeklytotal;
                pastTotal += dashboardData.get(i).weeklypasttotal;

            }
            String valtd1="0";
            if(val1>1000){
                valtd1=String.valueOf((int)val1);
            }else{
                valtd1=String.format("%.1f",val1);
            }

            String valtd="0";
            if(val>1000){
                valtd=String.valueOf((int)val);
            }else{
                valtd=String.format("%.1f",val);
            }

            homeSmallText.setText(valtd1);
            homeBigText.setText(valtd);
//            homeSmallText.setText(String.valueOf((int)weekldatalitres[currentSelectedWeekSlot]));
//            homeBigText.setText(String.valueOf((int)weeklytotal));
        }

        if(val1>past){
            smalldot.setImageResource(R.drawable.red_dot);
        }else{
            smalldot.setImageResource(R.drawable.green_dot);
        }
        if(val>pastTotal){
            bigdot.setImageResource(R.drawable.red_dot);
        }else{
            bigdot.setImageResource(R.drawable.green_dot);
        }

    }

    private void showHourValue() {
        double val1=0;
        double val=0;
        double past = 0;
        double pastTotal =0;
        if(currencySelected){

            for(int i=0; i<dashboardData.size();i++){
                val1 += dashboardData.get(i).hourlydatalitres[currentSelectedHourSlot];
                past += dashboardData.get(i).hourlydatalitresyesterday[currentSelectedHourSlot];
                val += dashboardData.get(i).hourlytotal;
                pastTotal += dashboardData.get(i).hourlytotalyesterday;

            }
            val1 = getRupee(val1);
            val = getRupee(val);
            String valtd1=String.valueOf((int)val1);
            if(val1>1000){
                valtd1=String.valueOf((int)val1);
            }else{
                valtd1=String.format("%.2f",val1);
            }

            String valtd=String.valueOf((int)val);
            if(val>1000){
                valtd=String.valueOf((int)val);
            }else{
                valtd=String.format("%.2f",val);
            }
            homeSmallText.setText(valtd1);
            homeBigText.setText(valtd);
        }else{

            String valtd1="0";
            for(int i=0; i<dashboardData.size();i++){
                val1 += dashboardData.get(i).hourlydatalitres[currentSelectedHourSlot];
                past += dashboardData.get(i).hourlydatalitresyesterday[currentSelectedHourSlot];
                val += dashboardData.get(i).hourlytotal;
                pastTotal += dashboardData.get(i).hourlytotalyesterday;
            }
            if(val1>1000){
                valtd1=String.valueOf((int)val1);
            }else{
                valtd1=String.format("%.1f",val1);
            }
            String valtd="0";
            if(val>1000){
                valtd=String.valueOf((int)val);
            }else{
                valtd=String.format("%.1f",val);
            }
            homeSmallText.setText(valtd1);
            homeBigText.setText(valtd);
        }

        if(val1>past){
            smalldot.setImageResource(R.drawable.red_dot);
        }else{
            smalldot.setImageResource(R.drawable.green_dot);
        }
        if(val>pastTotal){
            bigdot.setImageResource(R.drawable.red_dot);
        }else{
            bigdot.setImageResource(R.drawable.green_dot);
        }

    }

    private double getRupee(double amount) {
        double ans=0;
        if(slabsList!=null){
            double a=amount/1000;
            for(int i=0;i<slabsList.size();i++){
                if(i==slabsList.size()-1){
                    ans+=(a)*slabsList.get(i).getSlabPrice();
                    Log.d("fo0r"+String.valueOf(amount),String.valueOf(ans));
                }else{
                    if(a>slabsList.get(i+1).getSlabKl()){
                        ans+=(slabsList.get(i+1).getSlabKl())*slabsList.get(i).getSlabPrice();
                        a = a -slabsList.get(i+1).getSlabKl();
                        Log.d("fo1r"+String.valueOf(amount),String.valueOf(ans));
                    }else{
                        ans+=(a)*slabsList.get(i).getSlabPrice();
                        a=0;
                        Log.d("fo2r"+String.valueOf(amount),String.valueOf(ans));
                    }
                }
            }
        }
        return  ans;
    }


    Map<String,Object> userData;
    int waitingForApartmentDetails =0;
    @Override
    public void setUserValue(Map<String, Object> data) {
        Log.d("apts",data.toString());
        userData =data;
        try {
            waitingForApartmentDetails =  new JSONArray(data.get("aptIds").toString()).length();
            apartmentMapList = new ArrayList<>();
            loader.loadApartmentsData(data.get("aptIds"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apartmentUpdateEvent(Map<String, Object> map) {
        Log.d("apts",map.toString());
    }


    List<Map<String,Object>> apartmentMapList;
    List<Map<String,Object>> meterMapList;
    List<Map<String,Object>> meterMapList1;

    Map<String,Object> selectedApt;
    @Override
    public void apartmentsDetail(Map<String, Object> map) {
        Log.d("apts",map.toString());
        if(waitingForApartmentDetails!=0){
            waitingForApartmentDetails--;
            apartmentMapList.add(map);
            Log.d("data", String.valueOf(waitingForApartmentDetails));
            if(waitingForApartmentDetails==0){
                selectedAptId = getSelectedAptId();
                selectedApt = getApt(selectedAptId);
                Log.d("sele",selectedApt.toString());
                currencySelected = getCurruncySelection();
//                loadFlipSwitch();
                currencyToggle();
                meterMapList = new ArrayList<>();
                loader.loadMeterForSelectedApt(selectedAptId);
            }
        }
    }

    private Map<String, Object> getApt(int selectedAptId) {
        Map <String ,Object> d = null;
        for(int i=0;i<apartmentMapList.size();i++){
//            Log.d("apt", String.valueOf(Integer.parseInt(apartmentMapList.get(i).get("id"))));
            if(Integer.parseInt(apartmentMapList.get(i).get("id").toString()) == selectedAptId){
                d = apartmentMapList.get(i);
            }
        }
        return d;
    }

    private boolean getCurruncySelection() {
        return (boolean) userData.get("flipswitchCurrecySelected");
    }

    @Override
    public void metersLoaded(List<Map<String, Object>> meters) {
        meterMapList.addAll(meters);
        Log.d("meters",meters.toString());
        loadData();
    }

    @Override
    public void dailyDataForMeterLoaded(int id, Map<String, Object> map) {
        Log.d("daily",map.toString());
        int index =0;
        for(int i=0;i<dashboardData.size();i++){
            if(Integer.parseInt(meterMapList1.get(i).get("id").toString()) == id){
                index = i;
            }
        }
        dashboardData.get(index).weeklytotal = 0;
        for(int i=0;i<dashDataDates.weekldatalitresdates.size();i++){
            Object o =map.get(dashDataDates.weekldatalitresdates.get(i));
            String oo="0";
            if(o!=null){
                oo=o.toString();
            }
            dashboardData.get(index).weekldatalitres[i] = Double.valueOf(oo );
            dashboardData.get(index).weeklytotal += dashboardData.get(index).weekldatalitres[i];
        }
        dashboardData.get(index).weeklypasttotal = 0;
        for(int i=0;i<dashDataDates.weekldatalitrespastdates.size();i++){
            Object o =map.get(dashDataDates.weekldatalitrespastdates.get(i));
            String oo="0";
            if(o!=null){
                oo=o.toString();
            }
            dashboardData.get(index).weekldatalitrespast[i] = Double.valueOf(oo);
            dashboardData.get(index).weeklypasttotal += dashboardData.get(index).weekldatalitrespast[i];
        }

        for(int i=0;i<dashDataDates.monthdatalitresdates1.size();i++){
            Object o =map.get(dashDataDates.monthdatalitresdates1.get(i));
            String oo="0";
            if(o!=null){
                oo=o.toString();
            }
            dashboardData.get(index).monthdatalitres[0] += Double.valueOf( oo);
        }
        for(int i=0;i<dashDataDates.monthdatalitresdates2.size();i++){
            Object o =map.get(dashDataDates.monthdatalitresdates2.get(i));
            String oo="0";
            if(o!=null){
                oo=o.toString();
            }
            dashboardData.get(index).monthdatalitres[1] += Double.valueOf( oo);
        }
        for(int i=0;i<dashDataDates.monthdatalitresdates3.size();i++){
            Object o =map.get(dashDataDates.monthdatalitresdates3.get(i));
            String oo="0";
            if(o!=null){
                oo=o.toString();
            }
            dashboardData.get(index).monthdatalitres[2] += Double.valueOf( oo);
        }

        dashboardData.get(index).monthlytotal = dashboardData.get(index).monthdatalitres[0]+dashboardData.get(index).monthdatalitres[1]+dashboardData.get(index).monthdatalitres[2];
        showValue();
    }

    @Override
    public void twoHourDataTodayUpdate(int id, Map<String, Object> map) {
        int index =0;
        Log.d("twoHourDataTodayUpdate",map.toString());
        for(int i=0;i<dashboardData.size();i++){
            if(Integer.parseInt(meterMapList1.get(i).get("id").toString()) == id){
                index = i;
            }
        }
        dashboardData.get(index).hourlytotal =0;
        for(int i=0;i<12;i++){
            dashboardData.get(index).hourlydatalitres[i]= Double.valueOf( map.get(i+"").toString());
            dashboardData.get(index).hourlytotal += Double.valueOf( map.get(i+"").toString());
        }
        showValue();
    }

    @Override
    public void twoHourDataYesterdayUpdate(int id, Map<String, Object> map) {
        int index =0;
        Log.d("twoHourDataYesterdayUpe",map.toString());
        for(int i=0;i<dashboardData.size();i++){
            if(Integer.parseInt(meterMapList1.get(i).get("id").toString()) == id){
                index = i;
            }
        }
        dashboardData.get(index).hourlytotalyesterday = 0;
        for(int i=0;i<12;i++){
            dashboardData.get(index).hourlydatalitresyesterday[i]= Double.valueOf( map.get(i+"").toString());
            dashboardData.get(index).hourlytotalyesterday += Double.valueOf( map.get(i+"").toString());
        }
        showValue();
    }


    private int getSelectedAptId() {
        String s = userData.get("selectedAptId").toString();
        return  Integer.parseInt(s);
    }




}
