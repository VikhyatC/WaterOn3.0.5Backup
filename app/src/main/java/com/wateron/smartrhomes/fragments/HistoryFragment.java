package com.wateron.smartrhomes.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.wateron.smartrhomes.R;
import com.wateron.smartrhomes.activities.MainActivity;
import com.wateron.smartrhomes.component.AppConstants;
import com.wateron.smartrhomes.component.Bar;
import com.wateron.smartrhomes.models.Apartment;
import com.wateron.smartrhomes.models.GraphData;
import com.wateron.smartrhomes.models.HAlert;
import com.wateron.smartrhomes.models.Slabs;
import com.wateron.smartrhomes.util.CrashHelper;
import com.wateron.smartrhomes.util.DataHelper;
import com.wateron.smartrhomes.util.HistoryHandlerInterface;
import com.wateron.smartrhomes.util.HistoryHelper;
import com.wateron.smartrhomes.util.LoginHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Paranjay on 08-12-2017.
 */

public class HistoryFragment extends Fragment implements HistoryHandlerInterface {

    int selectedAptId = -1;
    GraphData graphData;
    Apartment apartment;
    double ccomodity1=0;
    double ccomodity2=0;
    double ccomodity3=0;
    double ccomodity4=0;
    double ccomodity5=0;
    double ccomodity6=0;
    int clickCount = 0;
    int currentseletedWeekOption=0;
    int currentselectedmonthoption=0;
    boolean weekoptionenabled=true;
    String weekoptionfrom[]=new String[4];
    String weekoptionto[]=new String[4];
    String monthoptionfrom[]=new String[2];
    String monthoptionto[]=new String[2];
    String currentFrom;
    String currentTo;
    ProgressBar spinner;
    int curselwebar=0;
    int currentselectedweekbar=0;
    int[] barids=new int[]{
            R.id.bar1,R.id.bar2,R.id.bar3,R.id.bar4,R.id.bar5,R.id.bar6,R.id.bar7,R.id.bar8,R.id.bar9,R.id.bar10,
            R.id.bar11,R.id.bar12,R.id.bar13,R.id.bar14,R.id.bar15,R.id.bar16,R.id.bar17,R.id.bar18,R.id.bar19,R.id.bar20,
            R.id.bar21,R.id.bar22,R.id.bar23,R.id.bar24,R.id.bar25,R.id.bar26,R.id.bar27,R.id.bar28,R.id.bar29,R.id.bar30,
            R.id.bar31};
    int[] weekbarids=new int[]{
            R.id.weekbar1,R.id.weekbar2,R.id.weekbar3,R.id.weekbar4,R.id.weekbar5,R.id.weekbar6,R.id.weekbar7
    };
    View vvv;
    int currentselectedbar=0;
    int curselbar=0;
    boolean rupeeselected=true;
    private boolean isloaded;
    private int noOfDays=31;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment,container,false);
        dataHandler = new DataHelper(getContext());
        vvv=view;
        loadAptId();
        loadslabs();
        initView(view);
        changeUnit();
        initListners();
        loadFlipSwitch();
        loadDefaults();
        setRoomType();
        loadPicker();
        return view;
    }
    List<HAlert> alarms = new ArrayList<>();
    private void loadValues() {
        loadGraph();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        long currenttime=new Date().getTime();
        graphData.setWeekvalue1(0);
        graphData.setWeekvalue2(0);
        graphData.setWeekvalue3(0);
        graphData.setWeekvalue4(0);
        graphData.setWeekvalue5(0);
        graphData.setWeekvalue6(0);
        graphData.setWeekvalue7(0);
        graphData.setMonthvalue1(0);
        graphData.setMonthvalue2(0);
        graphData.setMonthvalue3(0);
        graphData.setMonthvalue4(0);
        graphData.setMonthvalue5(0);
        graphData.setMonthvalue6(0);
        graphData.setMonthvalue7(0);
        graphData.setMonthvalue8(0);
        graphData.setMonthvalue9(0);
        graphData.setMonthvalue10(0);
        graphData.setMonthvalue11(0);
        graphData.setMonthvalue12(0);
        graphData.setMonthvalue13(0);
        graphData.setMonthvalue14(0);
        graphData.setMonthvalue15(0);
        graphData.setMonthvalue16(0);
        graphData.setMonthvalue17(0);
        graphData.setMonthvalue18(0);
        graphData.setMonthvalue19(0);
        graphData.setMonthvalue20(0);
        graphData.setMonthvalue21(0);
        graphData.setMonthvalue22(0);
        graphData.setMonthvalue23(0);
        graphData.setMonthvalue24(0);
        graphData.setMonthvalue25(0);
        graphData.setMonthvalue26(0);
        graphData.setMonthvalue27(0);
        graphData.setMonthvalue28(0);
        graphData.setMonthvalue29(0);
        graphData.setMonthvalue30(0);
        graphData.setMonthvalue31(0);
        graphData.setMonthtotal(0);
        graphData.setWeektotal(0);
        graphData.setWeekAvg(0);
        graphData.setWeekmax(10);
        graphData.setWeekmin(10);
        graphData.setMonthmax(10);
        graphData.setMonthmin(10);
        graphData.setMonthAvg(0);
        alarms.clear();
        if(weekoptionenabled){
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getWeekDates1()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getWeekDates2()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getWeekDates3()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getWeekDates4()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getWeekDates5()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getWeekDates6()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getWeekDates7()));
            try{
                graphData.setWeekvalue1(dataHandler.getHDailyData(graphData.getWeekDates1(),selectedAptId));
                graphData.setWeekvalue2(dataHandler.getHDailyData(graphData.getWeekDates2(),selectedAptId));
                graphData.setWeekvalue3(dataHandler.getHDailyData(graphData.getWeekDates3(),selectedAptId));
                graphData.setWeekvalue4(dataHandler.getHDailyData(graphData.getWeekDates4(),selectedAptId));
                graphData.setWeekvalue5(dataHandler.getHDailyData(graphData.getWeekDates5(),selectedAptId));
                graphData.setWeekvalue6(dataHandler.getHDailyData(graphData.getWeekDates6(),selectedAptId));
                graphData.setWeekvalue7(dataHandler.getHDailyData(graphData.getWeekDates7(),selectedAptId));
            }catch (Exception e){
                e.printStackTrace();
            }
            double weekt=graphData.getWeekvalue1()+
                    graphData.getWeekvalue2()+
                    graphData.getWeekvalue3()+
                    graphData.getWeekvalue4()+
                    graphData.getWeekvalue5()+
                    graphData.getWeekvalue6()+
                    graphData.getWeekvalue7();
            Log.d("week value",String.valueOf(graphData.getWeekvalue1())+" - "
                    +String.valueOf(graphData.getWeekvalue2())+" - "
                    +String.valueOf(graphData.getWeekvalue3())+" - "
                    +String.valueOf(graphData.getWeekvalue4())+" - "
                    +String.valueOf(graphData.getWeekvalue5())+" - "
                    +String.valueOf(graphData.getWeekvalue6())+" - "
                    +String.valueOf(graphData.getWeekvalue7()));
            Log.d("week total",String.valueOf(weekt));
            Log.d("week Average",String.valueOf(weekt/7));
            graphData.setWeektotal(weekt);
            graphData.setWeekAvg(weekt/7);
            double min=graphData.getWeekvalue1();
            double max=graphData.getWeekvalue1();
            String dat=graphData.getWeekDates1();

            if((graphData.getWeekvalue2()<min)&&(checkIfBeforeToday(graphData.getWeekDates6()))){
                min=graphData.getWeekvalue2();
                dat=graphData.getWeekDates2();
            }
            if((graphData.getWeekvalue3()<min)&&(checkIfBeforeToday(graphData.getWeekDates3()))){
                min=graphData.getWeekvalue3();
                dat=graphData.getWeekDates3();
            }
            if((graphData.getWeekvalue4()<min)&&(checkIfBeforeToday(graphData.getWeekDates4()))){
                min=graphData.getWeekvalue4();
                dat=graphData.getWeekDates4();
            }
            if((graphData.getWeekvalue5()<min)&&(checkIfBeforeToday(graphData.getWeekDates5()))){
                min=graphData.getWeekvalue5();
                dat=graphData.getWeekDates5();
            }

            if((graphData.getWeekvalue6()<min)&&(checkIfBeforeToday(graphData.getWeekDates6()))){
                min=graphData.getWeekvalue6();
                dat=graphData.getWeekDates6();
            }
            if((graphData.getWeekvalue7()<min)&&(checkIfBeforeToday(graphData.getWeekDates7()))){
                min=graphData.getWeekvalue7();
                dat=graphData.getWeekDates7();
            }
            Log.d("MinWeekDate",dat);
            graphData.setWeekmindate(dat);

            dat=graphData.getWeekDates1();
            if(graphData.getWeekvalue2()>max){
                max=graphData.getWeekvalue2();
                dat=graphData.getWeekDates2();
            }
            if(graphData.getWeekvalue3()>max){
                max=graphData.getWeekvalue3();
                dat=graphData.getWeekDates3();
            }
            if(graphData.getWeekvalue4()>max){
                max=graphData.getWeekvalue4();
                dat=graphData.getWeekDates4();
            }
            if(graphData.getWeekvalue5()>max){
                max=graphData.getWeekvalue5();
                dat=graphData.getWeekDates5();
            }
            if(graphData.getWeekvalue6()>max){
                max=graphData.getWeekvalue6();
                dat=graphData.getWeekDates6();
            }
            if(graphData.getWeekvalue7()>max){
                max=graphData.getWeekvalue7();
                dat=graphData.getWeekDates7();
            }
            graphData.setWeekmaxdate(dat);
            graphData.setWeekmin(min);
            graphData.setWeekmax(max);


        }else {
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates1()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates2()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates3()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates4()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates5()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates6()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates7()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates8()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates9()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates10()));

            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates11()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates12()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates13()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates14()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates15()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates16()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates17()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates18()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates19()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates20()));

            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates21()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates22()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates23()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates24()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates25()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates26()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates27()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates28()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates29()));
            alarms.add(dataHandler.getHAlert(selectedAptId,graphData.getMonthDates30()));

            try{
//                if (==30){
//
//                }
//                Log.d("DaysInThisMonth", String.valueOf(getDayinMonth(sdf.format(currenttime))));

                graphData.setMonthvalue1(dataHandler.getHDailyData(graphData.getMonthDates1(),selectedAptId));
                graphData.setMonthvalue2(dataHandler.getHDailyData(graphData.getMonthDates2(),selectedAptId));
                graphData.setMonthvalue3(dataHandler.getHDailyData(graphData.getMonthDates3(),selectedAptId));
                graphData.setMonthvalue4(dataHandler.getHDailyData(graphData.getMonthDates4(),selectedAptId));
                graphData.setMonthvalue5(dataHandler.getHDailyData(graphData.getMonthDates5(),selectedAptId));
                graphData.setMonthvalue6(dataHandler.getHDailyData(graphData.getMonthDates6(),selectedAptId));
                graphData.setMonthvalue7(dataHandler.getHDailyData(graphData.getMonthDates7(),selectedAptId));
                graphData.setMonthvalue8(dataHandler.getHDailyData(graphData.getMonthDates8(),selectedAptId));
                graphData.setMonthvalue9(dataHandler.getHDailyData(graphData.getMonthDates9(),selectedAptId));
                graphData.setMonthvalue10(dataHandler.getHDailyData(graphData.getMonthDates10(),selectedAptId));

                graphData.setMonthvalue11(dataHandler.getHDailyData(graphData.getMonthDates11(),selectedAptId));
                graphData.setMonthvalue12(dataHandler.getHDailyData(graphData.getMonthDates12(),selectedAptId));
                graphData.setMonthvalue13(dataHandler.getHDailyData(graphData.getMonthDates13(),selectedAptId));
                graphData.setMonthvalue14(dataHandler.getHDailyData(graphData.getMonthDates14(),selectedAptId));
                graphData.setMonthvalue15(dataHandler.getHDailyData(graphData.getMonthDates15(),selectedAptId));
                graphData.setMonthvalue16(dataHandler.getHDailyData(graphData.getMonthDates16(),selectedAptId));
                graphData.setMonthvalue17(dataHandler.getHDailyData(graphData.getMonthDates17(),selectedAptId));
                graphData.setMonthvalue18(dataHandler.getHDailyData(graphData.getMonthDates18(),selectedAptId));
                graphData.setMonthvalue19(dataHandler.getHDailyData(graphData.getMonthDates19(),selectedAptId));
                graphData.setMonthvalue20(dataHandler.getHDailyData(graphData.getMonthDates20(),selectedAptId));

                graphData.setMonthvalue21(dataHandler.getHDailyData(graphData.getMonthDates21(),selectedAptId));
                graphData.setMonthvalue22(dataHandler.getHDailyData(graphData.getMonthDates22(),selectedAptId));
                graphData.setMonthvalue23(dataHandler.getHDailyData(graphData.getMonthDates23(),selectedAptId));
                graphData.setMonthvalue24(dataHandler.getHDailyData(graphData.getMonthDates24(),selectedAptId));
                graphData.setMonthvalue25(dataHandler.getHDailyData(graphData.getMonthDates25(),selectedAptId));
                graphData.setMonthvalue26(dataHandler.getHDailyData(graphData.getMonthDates26(),selectedAptId));
                graphData.setMonthvalue27(dataHandler.getHDailyData(graphData.getMonthDates27(),selectedAptId));
                graphData.setMonthvalue28(dataHandler.getHDailyData(graphData.getMonthDates28(),selectedAptId));
                graphData.setMonthvalue29(dataHandler.getHDailyData(graphData.getMonthDates29(),selectedAptId));
                graphData.setMonthvalue30(dataHandler.getHDailyData(graphData.getMonthDates30(),selectedAptId));
                if (this.noOfDays==31){
                    graphData.setMonthvalue31(dataHandler.getHDailyData(graphData.getMonthDates31(),selectedAptId));
                }
            }catch (Exception e){
                e.printStackTrace();
            }


            double montht=graphData.getMonthvalue1()+
                    graphData.getMonthvalue2()+
                    graphData.getMonthvalue3()+
                    graphData.getMonthvalue4()+
                    graphData.getMonthvalue5()+
                    graphData.getMonthvalue6()+
                    graphData.getMonthvalue7()+
                    graphData.getMonthvalue8()+
                    graphData.getMonthvalue9()+
                    graphData.getMonthvalue10()+
                    graphData.getMonthvalue11()+
                    graphData.getMonthvalue12()+
                    graphData.getMonthvalue13()+
                    graphData.getMonthvalue14()+
                    graphData.getMonthvalue15()+
                    graphData.getMonthvalue16()+
                    graphData.getMonthvalue17()+
                    graphData.getMonthvalue18()+
                    graphData.getMonthvalue19()+
                    graphData.getMonthvalue20()+
                    graphData.getMonthvalue21()+
                    graphData.getMonthvalue22()+
                    graphData.getMonthvalue23()+
                    graphData.getMonthvalue24()+
                    graphData.getMonthvalue25()+
                    graphData.getMonthvalue26()+
                    graphData.getMonthvalue27()+
                    graphData.getMonthvalue28()+
                    graphData.getMonthvalue29()+
                    graphData.getMonthvalue30()+
                    graphData.getMonthvalue31();

            graphData.setMonthtotal(montht);
            graphData.setMonthAvg(montht/30);
            double min=graphData.getMonthvalue1();
            double max=graphData.getMonthvalue1();
            String dat=graphData.getMonthDates1();
            if(min>graphData.getMonthvalue2()&&checkIfBeforeToday(graphData.getMonthDates2())){
                min=graphData.getMonthvalue2();
                dat=graphData.getMonthDates2();
            }
            if(min>graphData.getMonthvalue3()&&checkIfBeforeToday(graphData.getMonthDates3())){
                min=graphData.getMonthvalue3();
                dat=graphData.getMonthDates3();
            }
            if(min>graphData.getMonthvalue4()&&checkIfBeforeToday(graphData.getMonthDates4())){
                min=graphData.getMonthvalue4();
                dat=graphData.getMonthDates4();
            }
            if(min>graphData.getMonthvalue5()&&checkIfBeforeToday(graphData.getMonthDates5())){
                min=graphData.getMonthvalue5();
                dat=graphData.getMonthDates5();
            }
            if(min>graphData.getMonthvalue6()&&checkIfBeforeToday(graphData.getMonthDates6())){
                min=graphData.getMonthvalue6();
                dat=graphData.getMonthDates6();
            }
            if(min>graphData.getMonthvalue7()&&checkIfBeforeToday(graphData.getMonthDates7())){
                min=graphData.getMonthvalue7();
                dat=graphData.getMonthDates7();
            }
            if(min>graphData.getMonthvalue8()&&checkIfBeforeToday(graphData.getMonthDates8())){
                min=graphData.getMonthvalue8();
                dat=graphData.getMonthDates8();
            }
            if(min>graphData.getMonthvalue9()&&checkIfBeforeToday(graphData.getMonthDates9())){
                min=graphData.getMonthvalue9();
                dat=graphData.getMonthDates9();
            }
            if(min>graphData.getMonthvalue10()&&checkIfBeforeToday(graphData.getMonthDates10())){
                min=graphData.getMonthvalue10();
                dat=graphData.getMonthDates10();
            }
            if(min>graphData.getMonthvalue11()&&checkIfBeforeToday(graphData.getMonthDates11())){
                min=graphData.getMonthvalue11();
                dat=graphData.getMonthDates11();
            }

            if(min>graphData.getMonthvalue12()&&checkIfBeforeToday(graphData.getMonthDates12())){
                min=graphData.getMonthvalue12();
                dat=graphData.getMonthDates12();
            }
            if(min>graphData.getMonthvalue13()&&checkIfBeforeToday(graphData.getMonthDates13())){
                min=graphData.getMonthvalue13();
                dat=graphData.getMonthDates13();
            }
            if(min>graphData.getMonthvalue14()&&checkIfBeforeToday(graphData.getMonthDates14())){
                min=graphData.getMonthvalue14();
                dat=graphData.getMonthDates14();
            }
            if(min>graphData.getMonthvalue15()&&checkIfBeforeToday(graphData.getMonthDates15())){
                min=graphData.getMonthvalue15();
                dat=graphData.getMonthDates15();
            }
            if(min>graphData.getMonthvalue16()&&checkIfBeforeToday(graphData.getMonthDates16())){
                min=graphData.getMonthvalue16();
                dat=graphData.getMonthDates16();
            }
            if(min>graphData.getMonthvalue17()&&checkIfBeforeToday(graphData.getMonthDates17())){
                min=graphData.getMonthvalue17();
                dat=graphData.getMonthDates17();
            }
            if(min>graphData.getMonthvalue18()&&checkIfBeforeToday(graphData.getMonthDates18())){
                min=graphData.getMonthvalue18();
                dat=graphData.getMonthDates18();
            }
            if(min>graphData.getMonthvalue19()&&checkIfBeforeToday(graphData.getMonthDates19())){
                min=graphData.getMonthvalue19();
                dat=graphData.getMonthDates19();
            }
            if(min>graphData.getMonthvalue20()&&checkIfBeforeToday(graphData.getMonthDates20())){
                min=graphData.getMonthvalue20();
                dat=graphData.getMonthDates20();
            }

            if(min>graphData.getMonthvalue21()&&checkIfBeforeToday(graphData.getMonthDates21())){
                min=graphData.getMonthvalue21();
                dat=graphData.getMonthDates21();
            }

            if(min>graphData.getMonthvalue22()&&checkIfBeforeToday(graphData.getMonthDates22())){
                min=graphData.getMonthvalue22();
                dat=graphData.getMonthDates22();
            }
            if(min>graphData.getMonthvalue23()&&checkIfBeforeToday(graphData.getMonthDates23())){
                min=graphData.getMonthvalue23();
                dat=graphData.getMonthDates23();
            }
            if(min>graphData.getMonthvalue24()&&checkIfBeforeToday(graphData.getMonthDates24())){
                min=graphData.getMonthvalue24();
                dat=graphData.getMonthDates24();
            }
            if(min>graphData.getMonthvalue25()&&checkIfBeforeToday(graphData.getMonthDates25())){
                min=graphData.getMonthvalue25();
                dat=graphData.getMonthDates25();
            }
            if(min>graphData.getMonthvalue26()&&checkIfBeforeToday(graphData.getMonthDates26())){
                min=graphData.getMonthvalue26();
                dat=graphData.getMonthDates26();
            }
            if(min>graphData.getMonthvalue27()&&checkIfBeforeToday(graphData.getMonthDates27())){
                min=graphData.getMonthvalue27();
                dat=graphData.getMonthDates27();
            }

            if(min>graphData.getMonthvalue28()&&checkIfBeforeToday(graphData.getMonthDates28())){
                Log.d("28thisBeforeToday", String.valueOf(checkIfBeforeToday(graphData.getMonthDates28())));
                min=graphData.getMonthvalue28();
                dat=graphData.getMonthDates28();
            }
            if(min>graphData.getMonthvalue29()&&checkIfBeforeToday(graphData.getMonthDates29())){
                min=graphData.getMonthvalue29();
                dat=graphData.getMonthDates29();
            }
            if(min>graphData.getMonthvalue30()&&checkIfBeforeToday(graphData.getMonthDates30())){
                min=graphData.getMonthvalue30();
                dat=graphData.getMonthDates30();
            }
            if (noOfDays==31){
                if(min>graphData.getMonthvalue31()&&checkIfBeforeToday(graphData.getMonthDates31())){
                    min=graphData.getMonthvalue31();
                    dat=graphData.getMonthDates31();
                }
            }


            graphData.setMonthmin(min);

            graphData.setMonthmindate(dat);

            //
            dat=graphData.getMonthDates1();
            if(max<graphData.getMonthvalue2()){
                max=graphData.getMonthvalue2();
                dat=graphData.getMonthDates2();
            }
            if(max<graphData.getMonthvalue3()){
                max=graphData.getMonthvalue3();
                dat=graphData.getMonthDates3();
            }
            if(max<graphData.getMonthvalue4()){
                max=graphData.getMonthvalue4();
                dat=graphData.getMonthDates4();
            }
            if(max<graphData.getMonthvalue5()){
                max=graphData.getMonthvalue5();
                dat=graphData.getMonthDates5();
            }
            if(max<graphData.getMonthvalue6()){
                max=graphData.getMonthvalue6();
                dat=graphData.getMonthDates6();
            }
            if(max<graphData.getMonthvalue7()){
                max=graphData.getMonthvalue7();
                dat=graphData.getMonthDates7();
            }
            if(max<graphData.getMonthvalue8()){
                max=graphData.getMonthvalue8();
                dat=graphData.getMonthDates8();
            }
            if(max<graphData.getMonthvalue9()){
                max=graphData.getMonthvalue9();
                dat=graphData.getMonthDates9();
            }
            if(max<graphData.getMonthvalue10()){
                max=graphData.getMonthvalue10();
                dat=graphData.getMonthDates10();
            }
            if(max<graphData.getMonthvalue11()){
                max=graphData.getMonthvalue11();
                dat=graphData.getMonthDates11();
            }

            if(max<graphData.getMonthvalue12()){
                max=graphData.getMonthvalue12();
                dat=graphData.getMonthDates12();
            }
            if(max<graphData.getMonthvalue13()){
                max=graphData.getMonthvalue13();
                dat=graphData.getMonthDates13();
            }
            if(max<graphData.getMonthvalue14()){
                max=graphData.getMonthvalue14();
                dat=graphData.getMonthDates14();
            }
            if(max<graphData.getMonthvalue15()){
                max=graphData.getMonthvalue15();
                dat=graphData.getMonthDates15();
            }
            if(max<graphData.getMonthvalue16()){
                max=graphData.getMonthvalue16();
                dat=graphData.getMonthDates16();
            }
            if(max<graphData.getMonthvalue17()){
                max=graphData.getMonthvalue17();
                dat=graphData.getMonthDates17();
            }
            if(max<graphData.getMonthvalue18()){
                max=graphData.getMonthvalue18();
                dat=graphData.getMonthDates18();
            }
            if(max<graphData.getMonthvalue19()){
                max=graphData.getMonthvalue19();
                dat=graphData.getMonthDates19();
            }
            if(max<graphData.getMonthvalue20()){
                max=graphData.getMonthvalue20();
                dat=graphData.getMonthDates20();
            }

            if(max<graphData.getMonthvalue21()){
                max=graphData.getMonthvalue21();
                dat=graphData.getMonthDates21();
            }

            if(max<graphData.getMonthvalue22()){
                max=graphData.getMonthvalue22();
                dat=graphData.getMonthDates22();
            }
            if(max<graphData.getMonthvalue23()){
                max=graphData.getMonthvalue23();
                dat=graphData.getMonthDates23();
            }
            if(max<graphData.getMonthvalue24()){
                max=graphData.getMonthvalue24();
                dat=graphData.getMonthDates24();
            }
            if(max<graphData.getMonthvalue25()){
                max=graphData.getMonthvalue25();
                dat=graphData.getMonthDates25();
            }
            if(max<graphData.getMonthvalue26()){
                max=graphData.getMonthvalue26();
                dat=graphData.getMonthDates26();
            }
            if(max<graphData.getMonthvalue27()){
                max=graphData.getMonthvalue27();
                dat=graphData.getMonthDates27();
            }
            if(max<graphData.getMonthvalue28()){
                max=graphData.getMonthvalue28();
                dat=graphData.getMonthDates28();
            }
            if(max<graphData.getMonthvalue29()){
                max=graphData.getMonthvalue29();
                dat=graphData.getMonthDates29();
            }
            if(max<graphData.getMonthvalue30()){
                max=graphData.getMonthvalue30();
                dat=graphData.getMonthDates30();
            }
            if(max<graphData.getMonthvalue31()){
                max=graphData.getMonthvalue31();
                dat=graphData.getMonthDates31();
            }

            graphData.setMonthmax(max);

            graphData.setMonthMaxdate(dat);

        }


    }

    private boolean checkIfBeforeToday(String weekDates6) {
        Log.d("WeekDates",weekDates6);
        Calendar c = Calendar.getInstance();

// set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

// and get that as a Date
        Date today = c.getTime();
        Log.d("Today Date", String.valueOf(today));
// or as a timestamp in milliseconds
        long todayInMillis = c.getTimeInMillis();

// user-specified date which you are testing
// let's say the components come from a form or something
        String[] date_details = weekDates6.split("-");
        System.out.println("date of week:"+Integer.parseInt(date_details[2]));
        int year = Integer.parseInt(date_details[0]);//
        int month = Integer.parseInt(date_details[1])-1;
        int dayOfMonth = Integer.parseInt(date_details[2]);

// reuse the calendar to set user specified date
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

// and get that as a Date
        Date dateSpecified = c.getTime();

// test your condition
        if (dateSpecified.before(today)) {
            System.err.println("Date specified [" + dateSpecified + "] is before today [" + today + "]");
        } else {
            System.err.println("Date specified [" + dateSpecified + "] is NOT before today [" + today + "]");
        }
        return dateSpecified.before(today)||(dateSpecified.equals(today));
    }

    private void loadGraph() {
        graphData=new GraphData();
        if(weekoptionenabled){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            graphData.setWeekDates1(currentFrom);
            try {
                graphData.setWeekDates2(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),1)));
                graphData.setWeekDates3(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),2)));
                graphData.setWeekDates4(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),3)));
                graphData.setWeekDates5(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),4)));
                graphData.setWeekDates6(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),5)));
                graphData.setWeekDates7(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),6)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            graphData.setMonthDates1(currentFrom);

            try {
                graphData.setMonthDates2(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),1)));
                graphData.setMonthDates3(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),2)));
                graphData.setMonthDates4(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),3)));
                graphData.setMonthDates5(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),4)));
                graphData.setMonthDates6(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),5)));
                graphData.setMonthDates7(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),6)));
                graphData.setMonthDates8(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),7)));
                graphData.setMonthDates9(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),8)));
                graphData.setMonthDates10(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),9)));
                graphData.setMonthDates11(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),10)));
                graphData.setMonthDates12(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),11)));
                graphData.setMonthDates13(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),12)));
                graphData.setMonthDates14(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),13)));
                graphData.setMonthDates15(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),14)));
                graphData.setMonthDates16(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),15)));
                graphData.setMonthDates17(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),16)));
                graphData.setMonthDates18(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),17)));
                graphData.setMonthDates19(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),18)));
                graphData.setMonthDates20(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),19)));
                graphData.setMonthDates21(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),20)));
                graphData.setMonthDates22(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),21)));
                graphData.setMonthDates23(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),22)));
                graphData.setMonthDates24(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),23)));
                graphData.setMonthDates25(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),24)));
                graphData.setMonthDates26(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),25)));
                graphData.setMonthDates27(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),26)));
                graphData.setMonthDates28(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),27)));
                graphData.setMonthDates29(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),28)));
                graphData.setMonthDates30(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),29)));
                if (noOfDays==31){
                    graphData.setMonthDates31(sdf.format(getFutureTime(sdf.parse(currentFrom).getTime(),30)));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }
    private void loadPicker() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat sdf1=new SimpleDateFormat("dd MMM",Locale.ENGLISH);
        try {
            weekselectiondate0.setText(sdf1.format(sdf.parse(weekoptionfrom[0])));
            weekselectiondate0.append(" - ");
            weekselectiondate0.append(sdf1.format(sdf.parse(weekoptionto[0])));

            weekselectiondate1.setText(sdf1.format(sdf.parse(weekoptionfrom[1])));
            weekselectiondate1.append(" - ");
            weekselectiondate1.append(sdf1.format(sdf.parse(weekoptionto[1])));

            weekselectiondate2.setText(sdf1.format(sdf.parse(weekoptionfrom[2])));
            weekselectiondate2.append(" - ");
            weekselectiondate2.append(sdf1.format(sdf.parse(weekoptionto[2])));

            weekselectiondate3.setText(sdf1.format(sdf.parse(weekoptionfrom[3])));
            weekselectiondate3.append(" - ");
            weekselectiondate3.append(sdf1.format(sdf.parse(weekoptionto[3])));


            monthselectiondate0.setText(sdf1.format(sdf.parse(monthoptionfrom[0])));
            monthselectiondate0.append(" - ");
            monthselectiondate0.append(sdf1.format(sdf.parse(monthoptionto[0])));
            monthselectiondate1.setText(sdf1.format(sdf.parse(monthoptionfrom[1])));
            monthselectiondate1.append(" - ");
            monthselectiondate1.append(sdf1.format(sdf.parse(monthoptionto[1])));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    LinearLayout weekgraph,weeklegendbottom;
    LinearLayout monthgraph,monthlegendbottom;

    TextView monthbardate1,monthbardate2,monthbardate3,monthbardate4,monthbardate5,monthbardate6;
    TextView monthbarday1,monthbarday2,monthbarday3,monthbarday4,monthbarday5,monthbarday6;
    TextView weekbardate1,weekbardate2,weekbardate3,weekbardate4,weekbardate5,weekbardate6,weekbardate7;
    TextView weekbarday1,weekbarday2,weekbarday3,weekbarday4,weekbarday5,weekbarday6,weekbarday7;
    TextView comodity,comodity1,comodity2,comodity3,comodity4,comodity5,comodity6;

    TextView roomType,dashboardpicker,amountdisplaygraph,amountdisplaygraphtype,maxgraphval,maxgraphvaldate,mingraphval,mingraphvaldate,graphalertcount,alertdategraph;

    Bar weekbar1,weekbar2,weekbar3,weekbar4,weekbar5,weekbar6,weekbar7;
    Bar bar1,bar2,bar3,bar4,bar5,bar6,bar7,bar8,bar9,bar10,bar11,bar12,bar13,bar14,bar15,bar16,bar17,bar18,bar19,bar20,bar21,bar22,bar23,bar24,bar25,bar26,bar27,bar28,bar29,bar30,bar31;

    Button pickerclose,weekpicker,monthpicker;
    FrameLayout back,forward;

    TextView weekselectiondate0,weekselectiondate1,weekselectiondate2,weekselectiondate3;
    LinearLayout weekselection0,weekselection1,weekselection2,weekselection3;

    TextView monthselectiondate0,monthselectiondate1;
    LinearLayout monthselection0,monthselection1,monthpickoption,weekpickoption,datepickgraph,graphmainpick;
    LinearLayout homebutton;
    Button rupeefliptext,litresfliptext;

    ImageButton flipswitchbg,flipswitchbutton;
    LinearLayout flipswitch;
    DataHelper dataHandler;
    View touchline;
    private void initView(View view) {
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_light.ttf");
        touchline=view.findViewById(R.id.touchline);
        weekgraph=(LinearLayout)view.findViewById(R.id.weekgraph);
        monthgraph=(LinearLayout)view.findViewById(R.id.monthgraphs);
        homebutton=(LinearLayout) view.findViewById(R.id.homebutton);
        weeklegendbottom=(LinearLayout)view.findViewById(R.id.weekhistorylegendbottom);
        monthlegendbottom=(LinearLayout)view.findViewById(R.id.monthhistorylegendbottom);
        rupeefliptext=(Button)view.findViewById(R.id.flipswitchrupee);
        rupeefliptext.setTypeface(type);
        litresfliptext=(Button)view.findViewById(R.id.flipswitchlitres);
        litresfliptext .setTypeface(type);
        flipswitchbg=(ImageButton)view.findViewById(R.id.flipswitchbg);
        flipswitch=(LinearLayout)view.findViewById(R.id.flipswitchbuttonview);
        spinner = (ProgressBar)view.findViewById(R.id.progressBar);
        flipswitchbutton=(ImageButton)view.findViewById(R.id.flipswitchbutton);
        rupeefliptext.setText(apartment.getCurrencyText());
        litresfliptext.setText(apartment.getUnitText());
        weekbar1=(Bar)view.findViewById(R.id.weekbar1);
        weekbar2=(Bar)view.findViewById(R.id.weekbar2);
        weekbar3=(Bar)view.findViewById(R.id.weekbar3);
        weekbar4=(Bar)view.findViewById(R.id.weekbar4);
        weekbar5=(Bar)view.findViewById(R.id.weekbar5);
        weekbar6=(Bar)view.findViewById(R.id.weekbar6);
        weekbar7=(Bar)view.findViewById(R.id.weekbar7);

        back=(FrameLayout) view.findViewById(R.id.back);

        forward=(FrameLayout)view.findViewById(R.id.forward);

        bar1=(Bar)view.findViewById(R.id.bar1);
        bar2=(Bar)view.findViewById(R.id.bar2);
        bar3=(Bar)view.findViewById(R.id.bar3);
        bar4=(Bar)view.findViewById(R.id.bar4);
        bar5=(Bar)view.findViewById(R.id.bar5);
        bar6=(Bar)view.findViewById(R.id.bar6);
        bar7=(Bar)view.findViewById(R.id.bar7);
        bar8=(Bar)view.findViewById(R.id.bar8);
        bar9=(Bar)view.findViewById(R.id.bar9);
        bar10=(Bar)view.findViewById(R.id.bar10);
        bar11=(Bar)view.findViewById(R.id.bar11);
        bar12=(Bar)view.findViewById(R.id.bar12);
        bar13=(Bar)view.findViewById(R.id.bar13);
        bar14=(Bar)view.findViewById(R.id.bar14);
        bar15=(Bar)view.findViewById(R.id.bar15);
        bar16=(Bar)view.findViewById(R.id.bar16);
        bar17=(Bar)view.findViewById(R.id.bar17);
        bar18=(Bar)view.findViewById(R.id.bar18);
        bar19=(Bar)view.findViewById(R.id.bar19);
        bar20=(Bar)view.findViewById(R.id.bar20);
        bar21=(Bar)view.findViewById(R.id.bar21);
        bar22=(Bar)view.findViewById(R.id.bar22);
        bar23=(Bar)view.findViewById(R.id.bar23);
        bar24=(Bar)view.findViewById(R.id.bar24);
        bar25=(Bar)view.findViewById(R.id.bar25);
        bar26=(Bar)view.findViewById(R.id.bar26);
        bar27=(Bar)view.findViewById(R.id.bar27);
        bar28=(Bar)view.findViewById(R.id.bar28);
        bar29=(Bar)view.findViewById(R.id.bar29);
        bar30=(Bar)view.findViewById(R.id.bar30);
        bar31 = (Bar)view.findViewById(R.id.bar31);
        pickerclose=(Button)view.findViewById(R.id.close_button_history_picker);
        pickerclose.setTypeface(type);
        weekpicker=(Button)view.findViewById(R.id.weekselect);
        weekpicker.setTypeface(type);
        monthpicker=(Button)view.findViewById(R.id.monthselect);
        monthpicker.setTypeface(type);
        datepickgraph=(LinearLayout)view.findViewById(R.id.datepickgraph);
        graphmainpick=(LinearLayout)view.findViewById(R.id.graphmainpick);
        monthselection0=(LinearLayout)view.findViewById(R.id.monthselection0);
        monthselection1=(LinearLayout)view.findViewById(R.id.monthselection1);
        monthpickoption=(LinearLayout)view.findViewById(R.id.monthpickoption);
        weekpickoption=(LinearLayout)view.findViewById(R.id.weekpickoption);
        monthselectiondate0=(TextView)view.findViewById(R.id.monthselectiondate0);
        monthselectiondate0.setTypeface(type);
        monthselectiondate1=(TextView)view.findViewById(R.id.monthselectiondate1);
        monthselectiondate1 .setTypeface(type);
        weekselection0=(LinearLayout) view.findViewById(R.id.weekselection0);
        weekselection1=(LinearLayout) view.findViewById(R.id.weekselection1);
        weekselection2=(LinearLayout) view.findViewById(R.id.weekselection2);
        weekselection3=(LinearLayout) view.findViewById(R.id.weekselection3);
        weekselectiondate0=(TextView)view.findViewById(R.id.weekselectiondate0);
        weekselectiondate0.setTypeface(type);
        weekselectiondate1=(TextView)view.findViewById(R.id.weekselectiondate1);
        weekselectiondate1.setTypeface(type);
        weekselectiondate2=(TextView)view.findViewById(R.id.weekselectiondate2);
        weekselectiondate2.setTypeface(type);
        weekselectiondate3=(TextView)view.findViewById(R.id.weekselectiondate3);
        weekselectiondate3.setTypeface(type);
        monthbardate1=(TextView)view.findViewById(R.id.monthbardate1);
        monthbardate1 .setTypeface(type);
        monthbardate2=(TextView)view.findViewById(R.id.monthbardate2);
        monthbardate2 .setTypeface(type);
        monthbardate3=(TextView)view.findViewById(R.id.monthbardate3);
        monthbardate3 .setTypeface(type);
        monthbardate4=(TextView)view.findViewById(R.id.monthbardate4);
        monthbardate4 .setTypeface(type);
        monthbardate5=(TextView)view.findViewById(R.id.monthbardate5);
        monthbardate5 .setTypeface(type);
        monthbardate6=(TextView)view.findViewById(R.id.monthbardate6);
        monthbardate6 .setTypeface(type);
        monthbarday1=(TextView)view.findViewById(R.id.monthbarday1);
        monthbarday1 .setTypeface(type);
        monthbarday2=(TextView)view.findViewById(R.id.monthbarday2);
        monthbarday2.setTypeface(type);
        monthbarday3=(TextView)view.findViewById(R.id.monthbarday3);
        monthbarday3 .setTypeface(type);
        monthbarday4=(TextView)view.findViewById(R.id.monthbarday4);
        monthbarday4.setTypeface(type);
        monthbarday5=(TextView)view.findViewById(R.id.monthbarday5);
        monthbarday5.setTypeface(type);
        monthbarday6=(TextView)view.findViewById(R.id.monthbarday6);
        monthbarday6 .setTypeface(type);

        weekbardate1=(TextView)view.findViewById(R.id.weekbardate1);
        weekbardate1 .setTypeface(type);
        weekbardate2=(TextView)view.findViewById(R.id.weekbardate2);
        weekbardate2.setTypeface(type);
        weekbardate3=(TextView)view.findViewById(R.id.weekbardate3);
        weekbardate3 .setTypeface(type);
        weekbardate4=(TextView)view.findViewById(R.id.weekbardate4);
        weekbardate4.setTypeface(type);
        weekbardate5=(TextView)view.findViewById(R.id.weekbardate5);
        weekbardate5.setTypeface(type);
        weekbardate6=(TextView)view.findViewById(R.id.weekbardate6);
        weekbardate6 .setTypeface(type);
        weekbardate7=(TextView)view.findViewById(R.id.weekbardate7);
        weekbardate7 .setTypeface(type);

        weekbarday1=(TextView)view.findViewById(R.id.weekbarday1);
        weekbarday1.setTypeface(type);
        weekbarday2=(TextView)view.findViewById(R.id.weekbarday2);
        weekbarday2.setTypeface(type);
        weekbarday3=(TextView)view.findViewById(R.id.weekbarday3);
        weekbarday3.setTypeface(type);
        weekbarday4=(TextView)view.findViewById(R.id.weekbarday4);
        weekbarday4.setTypeface(type);
        weekbarday5=(TextView)view.findViewById(R.id.weekbarday5);
        weekbarday5.setTypeface(type);
        weekbarday6=(TextView)view.findViewById(R.id.weekbarday6);
        weekbarday6.setTypeface(type);
        weekbarday7=(TextView)view.findViewById(R.id.weekbarday7);
        weekbarday7.setTypeface(type);
        comodity=(TextView)view.findViewById(R.id.comodity);
        comodity.setTypeface(type);
        comodity1=(TextView)view.findViewById(R.id.comodity1);
        comodity1 .setTypeface(type);
        comodity2=(TextView)view.findViewById(R.id.comodity2);
        comodity2 .setTypeface(type);
        comodity3=(TextView)view.findViewById(R.id.comodity3);
        comodity3 .setTypeface(type);
        comodity4=(TextView)view.findViewById(R.id.comodity4);
        comodity4.setTypeface(type);
        comodity5=(TextView)view.findViewById(R.id.comodity5);
        comodity5.setTypeface(type);
        comodity6=(TextView)view.findViewById(R.id.comodity6);
        comodity6 .setTypeface(type);
        roomType=(TextView)view.findViewById(R.id.roomType);
        roomType.setTypeface(type);

        dashboardpicker=(TextView)view.findViewById(R.id.dashboardpicker);

        amountdisplaygraph=(TextView)view.findViewById(R.id.amountdisplaygraph);
        amountdisplaygraph.setTypeface(type);
        amountdisplaygraphtype=(TextView)view.findViewById(R.id.amountdisplaygraphtype);
        amountdisplaygraphtype.setTypeface(type);
        maxgraphval=(TextView)view.findViewById(R.id.maxgraphval);
        maxgraphval.setSelected(true);
        maxgraphval.setTypeface(type);
        maxgraphvaldate=(TextView)view.findViewById(R.id.maxgraphvaldate);
        maxgraphvaldate .setTypeface(type);
        mingraphval=(TextView)view.findViewById(R.id.mingraphval);
        mingraphval.setSelected(true);
        mingraphval.setTypeface(type);
        mingraphvaldate=(TextView)view.findViewById(R.id.mingraphvaldate);
        mingraphvaldate.setTypeface(type);
        graphalertcount=(TextView)view.findViewById(R.id.graphalertcount);
        graphalertcount.setTypeface(type);
        alertdategraph=(TextView)view.findViewById(R.id.alertdategraph);
        alertdategraph.setTypeface(type);
    }

    private void initListners() {
        loadHomeButton();
        loadSpanSelector();
        loadgraphclicks();
    }

    private void loadgraphclicks() {
        bar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==1){
                    curselbar=0;
                }else {
                    curselbar=1;
                }   activatebartouch(v.getId());
            }
        });
        bar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==2){
                    curselbar=0;
                }else {
                    curselbar=2;
                } activatebartouch(v.getId());
            }
        });
        bar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==3){
                    curselbar=0;
                }else {
                    curselbar=3;
                }activatebartouch(v.getId());
            }
        });
        bar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==4){
                    curselbar=0;
                }else {
                    curselbar=4;
                } activatebartouch(v.getId());
            }
        });
        bar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==5){
                    curselbar=0;
                }else {
                    curselbar=5;
                }   activatebartouch(v.getId());
            }
        });
        bar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==6){
                    curselbar=0;
                }else {
                    curselbar=6;
                }  activatebartouch(v.getId());
            }
        });
        bar7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==7){
                    curselbar=0;
                }else {
                    curselbar=7;
                }  activatebartouch(v.getId());
            }
        });
        bar8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==8){
                    curselbar=0;
                }else {
                    curselbar=8;
                } activatebartouch(v.getId());
            }
        });
        bar9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==9){
                    curselbar=0;
                }else {
                    curselbar=9;
                }   activatebartouch(v.getId());
            }
        });
        bar10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==10){
                    curselbar=0;
                }else {
                    curselbar=10;
                }  activatebartouch(v.getId());
            }
        });
        bar11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==11){
                    curselbar=0;
                }else {
                    curselbar=11;
                }  activatebartouch(v.getId());
            }
        });
        bar12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==12){
                    curselbar=0;
                }else {
                    curselbar=12;
                }activatebartouch(v.getId());
            }
        });
        bar13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==13){
                    curselbar=0;
                }else {
                    curselbar=13;
                } activatebartouch(v.getId());
            }
        });
        bar14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==14){
                    curselbar=0;
                }else {
                    curselbar=14;
                } activatebartouch(v.getId());
            }
        });
        bar15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==15){
                    curselbar=0;
                }else {
                    curselbar=15;
                }  activatebartouch(v.getId());
            }
        });
        bar16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==16){
                    curselbar=0;
                }else {
                    curselbar=16;
                } activatebartouch(v.getId());
            }
        });
        bar17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==17){
                    curselbar=0;
                }else {
                    curselbar=17;
                } activatebartouch(v.getId());
            }
        });
        bar18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==18){
                    curselbar=0;
                }else {
                    curselbar=18;
                }  activatebartouch(v.getId());
            }
        });
        bar19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==19){
                    curselbar=0;
                }else {
                    curselbar=19;
                }activatebartouch(v.getId());
            }
        });
        bar20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==20){
                    curselbar=0;
                }else {
                    curselbar=20;
                } activatebartouch(v.getId());
            }
        });
        bar21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==21){
                    curselbar=0;
                }else {
                    curselbar=21;
                } activatebartouch(v.getId());
            }
        });
        bar22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==22){
                    curselbar=0;
                }else {
                    curselbar=22;
                }activatebartouch(v.getId());
            }
        });
        bar23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==23){
                    curselbar=0;
                }else {
                    curselbar=23;
                } activatebartouch(v.getId());
            }
        });
        bar24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==24){
                    curselbar=0;
                }else {
                    curselbar=24;
                } activatebartouch(v.getId());
            }
        });
        bar25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==25){
                    curselbar=0;
                }else {
                    curselbar=25;
                } activatebartouch(v.getId());
            }
        });
        bar26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==26){
                    curselbar=0;
                }else {
                    curselbar=26;
                } activatebartouch(v.getId());
            }
        });
        bar27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==27){
                    curselbar=0;
                }else {
                    curselbar=27;
                }activatebartouch(v.getId());
            }
        });
        bar28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==28){
                    curselbar=0;
                }else {
                    curselbar=28;
                }activatebartouch(v.getId());
            }
        });
        bar29.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==29){
                    curselbar=0;
                }else {
                    curselbar=29;
                }activatebartouch(v.getId());
            }
        });
        bar30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==30){
                    curselbar=0;
                }else {
                    curselbar=30;
                }
                activatebartouch(v.getId());
            }
        });
        bar31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselbar==31){
                    curselbar=0;
                }else {
                    curselbar=31;
                }
                activatebartouch(v.getId());
            }
        });
        weekbar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselwebar==1){
                    curselwebar=0;
                }else{
                    curselwebar=1;
                }
                activateweekbartouch(v.getId());
            }
        });
        weekbar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselwebar==2){
                    curselwebar=0;
                }else{
                    curselwebar=2;
                }
                activateweekbartouch(v.getId());
            }
        });
        weekbar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselwebar==3){
                    curselwebar=0;
                }else{
                    curselwebar=3;
                }
                activateweekbartouch(v.getId());
            }
        });
        weekbar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselwebar==4){
                    curselwebar=0;
                }else{
                    curselwebar=4;
                }
                activateweekbartouch(v.getId());
            }
        });
        weekbar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselwebar==5){
                    curselwebar=0;
                }else{
                    curselwebar=5;
                }
                activateweekbartouch(v.getId());
            }
        });
        weekbar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselwebar==6){
                    curselwebar=0;
                }else{
                    curselwebar=6;
                }
                activateweekbartouch(v.getId());
            }
        });
        weekbar7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curselwebar==7){
                    curselwebar=0;
                }else{
                    curselwebar=7;
                }
                activateweekbartouch(v.getId());
            }
        });
    }

    private void activatebartouch(int id) {
        updateDisplay();
    }

    private void activateweekbartouch(int id) {
        updateDisplay();
    }

    private void setRoomType() {
        if(weekoptionenabled){
            currentFrom=weekoptionfrom[currentseletedWeekOption];
            currentTo=weekoptionto[currentseletedWeekOption];
            ((MainActivity)getActivity()).logevent("History_time_selection",currentFrom+currentTo,"Touch Event");
        }else{
            currentFrom=monthoptionfrom[currentselectedmonthoption];
            currentTo=monthoptionto[currentselectedmonthoption];
            Log.d("Current From/To",currentFrom+"/"+currentTo);
            ((MainActivity)getActivity()).logevent("History_time_selection",currentFrom+currentTo,"Touch Event");
        }
        checkBackAndForwardButton();
    }
    private void checkBackAndForwardButton(){
        if(weekoptionenabled){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            try {
                long f=getPastTime(sdf.parse(currentFrom).getTime(),7);
                long t=getPastTime(new Date().getTime(),91);
                if(f>t){
                    back.setVisibility(View.VISIBLE);
                }else{
                    back.setVisibility(View.INVISIBLE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            try {
                long f=getPastTime(sdf.parse(currentFrom).getTime(),30);
                long t=getPastTime(new Date().getTime(),91);
                if(f>t){
                    back.setVisibility(View.VISIBLE);
                }else{
                    back.setVisibility(View.INVISIBLE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        if(weekoptionenabled){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            try {
                long f=getFutureTime(sdf.parse(currentFrom).getTime(),7);
                long t=new Date().getTime();
                if(f<t){
                    forward.setVisibility(View.VISIBLE);
                }else{
                    forward.setVisibility(View.INVISIBLE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            try {
                long f=getFutureTime(sdf.parse(currentFrom).getTime(),30);
                long t=new Date().getTime();
                if(f<t){
                    forward.setVisibility(View.VISIBLE);
                }else{
                    forward.setVisibility(View.INVISIBLE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    private void loadDefaults() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        SimpleDateFormat sdf1=new SimpleDateFormat("dd-MMM",Locale.ENGLISH);
        SimpleDateFormat sdf2=new SimpleDateFormat("dd",Locale.ENGLISH);
        SimpleDateFormat sdf3=new SimpleDateFormat("EEE",Locale.ENGLISH);
        Calendar cal = Calendar.getInstance();
        clickCount =0;
        weekoptionfrom=new String[]{"","","",""};
        weekoptionto=new String[]{"","","",""};
        monthoptionfrom=new String[]{"",""};
        monthoptionto=new String[]{"",""};
        currentFrom="";
        currentTo="";
        cal.set(Calendar.DAY_OF_MONTH,1);
        long currenttime=new Date().getTime();
        long daylength=86400000;
        weekoptionto[0]=sdf.format(new Date());
        monthoptionto[0]=sdf.format(new Date());
        weekoptionfrom[0]=sdf.format(getPastTime(currenttime,6));
        monthoptionfrom[0]=sdf.format(getPastTime(currenttime,29));


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
        int currentDay=6-today;
        Log.d("Current day", String.valueOf(currentDay));

        weekoptionfrom[1]=sdf.format(getPastTime(currenttime,today));
        weekoptionto[1]=sdf.format(getFutureTime(currenttime,currentDay));
        Log.d("Future Date", String.valueOf(weekoptionto[1]));
        weekoptionfrom[2]=sdf.format(getPastTime(getPastTime(currenttime,7),today));
        weekoptionto[2]=sdf.format(getFutureTime(getPastTime(currenttime,7),currentDay));

        try {
            weekoptionfrom[3]=getPastMonthDate(sdf.parse(weekoptionfrom[1]));
            weekoptionto[3]=sdf.format(getFutureTime(sdf.parse(weekoptionfrom[3]).getTime(),7));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int daysincurrentmonth=getDayinMonth(sdf.format(currenttime));
        monthoptionfrom[1]=sdf.format(cal.getTime());
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        monthoptionto[1]=sdf.format(cal.getTime());

//    getPastTime(currenttime,Integer.parseInt((sdf.format(new Date())).split("-")[2])-1)

    }
    public String getPastMonthDate(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        SimpleDateFormat sdf1=new SimpleDateFormat("dd-MMM",Locale.ENGLISH);
        String thismonth=sdf.format(date);
        int year=Integer.parseInt(thismonth.split("-")[0]);
        String thisdate=thismonth.split("-")[2];
        String month=sdf1.format(date).split("-")[1];

        String pastmonth="";
        switch (month){
            case "Jan":
                pastmonth="Dec";
                year-=1;
                break;
            case "Feb":
                pastmonth="Jan";
                break;
            case "Mar":
                pastmonth="Feb";
                break;
            case "Apr":
                pastmonth="Mar";
                break;
            case "May":
                pastmonth="Apr";
                break;
            case "Jun":
                pastmonth="May";
                break;
            case "Jul":
                pastmonth="Jun";
                break;
            case "Aug":
                pastmonth="Jul";
                break;
            case "Sep":
                pastmonth="Aug";
                break;
            case "Oct":
                pastmonth="Sep";
                break;
            case "Nov":
                pastmonth="Oct";
                break;
            case "Dec":
                pastmonth="Nov";
                break;
        }
        String pastdate=String.valueOf(year)+"-"+pastmonth+"-"+thisdate;
        SimpleDateFormat sdf4=new SimpleDateFormat("yyyy-MMM-dd",Locale.ENGLISH);
        try {
            pastdate=sdf.format(sdf4.parse(pastdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pastdate;
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

// Create a calendar object and set year and month
            Calendar mycal = new GregorianCalendar(iYear, iMonth-1, iDay);

// Get the number of days in that month
            daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  daysInMonth;
    }

    public long getPastTime(long time,int days){
        long daylength=86400000;
        long ans=time;
        for(int i=0;i<days;i++){
            ans-=daylength;
        }
        return ans;
    }

    public long getFutureTime(long time,int days){
        long daylength=86400000;
        long ans=time;
        for(int i=0;i<days;i++){
            ans+=daylength;
        }
        return ans;
    }
    private void loadSpanSelector() {
        roomType.setClickable(true);
        final Typeface typeface1=Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_regular.ttf");
        final Typeface typeface2=Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_light.ttf");
        roomType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepickgraph.setVisibility(View.VISIBLE);
                graphmainpick.setVisibility(View.GONE);
                if(weekoptionenabled){
                    weekpicker.setTypeface(typeface1);
                    monthpicker.setTypeface(typeface2);
                    weekpickoption.setVisibility(View.VISIBLE);
                    monthpickoption.setVisibility(View.GONE);
                }else{
                    weekpicker.setTypeface(typeface2);
                    monthpicker.setTypeface(typeface1);
                    weekpickoption.setVisibility(View.GONE);
                    monthpickoption.setVisibility(View.VISIBLE);
                }
            }
        });
        pickerclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepickgraph.setVisibility(View.GONE);
                graphmainpick.setVisibility(View.VISIBLE);
            }
        });
        weekpicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekpickoption.setVisibility(View.VISIBLE);
                monthpickoption.setVisibility(View.GONE);
                weekpicker.setTypeface(typeface1);
                monthpicker.setTypeface(typeface2);
            }
        });
        weekpicker.setTypeface(typeface1);
        monthpicker.setTypeface(typeface2);
        monthpicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekpickoption.setVisibility(View.GONE);
                monthpickoption.setVisibility(View.VISIBLE);
                weekpicker.setTypeface(typeface2);
                monthpicker.setTypeface(typeface1);
            }
        });
        monthselection0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepickgraph.setVisibility(View.GONE);
                graphmainpick.setVisibility(View.VISIBLE);
                weekoptionenabled=false;
                currentselectedmonthoption=0;
                currentseletedWeekOption=0;
                currentslot=92;
                weekpicker.setTypeface(typeface2);
                monthpicker.setTypeface(typeface1);
                monthgraph.setVisibility(View.VISIBLE);
                weekgraph.setVisibility(View.GONE);
                monthlegendbottom.setVisibility(View.VISIBLE);
                weeklegendbottom.setVisibility(View.GONE);
                roomType.setText(monthselectiondate0.getText());
                Log.d("monthselected", String.valueOf(monthselectiondate1.getText()));
                setRoomType();
                loadValues();
                displayloadedValues();
            }
        });
        monthselection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepickgraph.setVisibility(View.GONE);
                graphmainpick.setVisibility(View.VISIBLE);
                weekoptionenabled=false;
                currentselectedmonthoption=1;
                currentseletedWeekOption=0;
                currentslot=92;
                weekpicker.setTypeface(typeface2);
                monthpicker.setTypeface(typeface1);
                monthgraph.setVisibility(View.VISIBLE);
                weekgraph.setVisibility(View.GONE);
                monthlegendbottom.setVisibility(View.VISIBLE);
                weeklegendbottom.setVisibility(View.GONE);
                roomType.setText(monthselectiondate1.getText());
                Log.d("monthselected", String.valueOf(monthselectiondate1.getText()));
                clickCount =0;
                setRoomType();
                loadValues();
                displayloadedValues();
            }
        });

        weekselection0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepickgraph.setVisibility(View.GONE);
                graphmainpick.setVisibility(View.VISIBLE);
                weekoptionenabled=true;
                currentseletedWeekOption=0;
                currentselectedmonthoption=0;
                currentslot=92;
                monthgraph.setVisibility(View.GONE);
                weekgraph.setVisibility(View.VISIBLE);
                monthlegendbottom.setVisibility(View.GONE);
                weeklegendbottom.setVisibility(View.VISIBLE);
                weekpicker.setTypeface(typeface1);
                monthpicker.setTypeface(typeface2);
                roomType.setText(weekselectiondate0.getText());
                Log.d("weekselected", String.valueOf(weekselectiondate0.getText()));
                setRoomType();
                loadValues();
                displayloadedValues();
            }
        });

        weekselection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepickgraph.setVisibility(View.GONE);
                graphmainpick.setVisibility(View.VISIBLE);
                weekoptionenabled=true;
                currentseletedWeekOption=1;
                currentselectedmonthoption=0;
                currentslot=92;
                monthgraph.setVisibility(View.GONE);
                weekgraph.setVisibility(View.VISIBLE);
                monthlegendbottom.setVisibility(View.GONE);
                weeklegendbottom.setVisibility(View.VISIBLE);

                weekpicker.setTypeface(typeface1);
                monthpicker.setTypeface(typeface2);
                roomType.setText(weekselectiondate1.getText());
                Log.d("weekselected", String.valueOf(weekselectiondate1.getText()));

                setRoomType();
                loadValues();
                displayloadedValues();

            }
        });

        weekselection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepickgraph.setVisibility(View.GONE);
                graphmainpick.setVisibility(View.VISIBLE);
                weekoptionenabled=true;
                currentseletedWeekOption=2;
                currentselectedmonthoption=0;
                currentslot=92;
                monthgraph.setVisibility(View.GONE);
                weekgraph.setVisibility(View.VISIBLE);
                monthlegendbottom.setVisibility(View.GONE);
                weeklegendbottom.setVisibility(View.VISIBLE);

                weekpicker.setTypeface(typeface1);
                monthpicker.setTypeface(typeface2);
                roomType.setText(weekselectiondate2.getText());
                Log.d("weekselected", String.valueOf(weekselectiondate2.getText()));
                setRoomType();
                loadValues();
                displayloadedValues();
            }
        });

        weekselection3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepickgraph.setVisibility(View.GONE);
                graphmainpick.setVisibility(View.VISIBLE);
                weekoptionenabled=true;
                currentseletedWeekOption=3;
                currentselectedmonthoption=0;
                currentslot=92;
                monthgraph.setVisibility(View.GONE);
                weekgraph.setVisibility(View.VISIBLE);
                monthlegendbottom.setVisibility(View.GONE);
                weeklegendbottom.setVisibility(View.VISIBLE);

                weekpicker.setTypeface(typeface1);
                monthpicker.setTypeface(typeface2);
                roomType.setText(weekselectiondate3.getText());
                Log.d("weekselected", String.valueOf(weekselectiondate3.getText()));
                setRoomType();
                loadValues();
                displayloadedValues();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount--;
                if(weekoptionenabled){

                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
                    try {
                        long f=getPastTime(sdf.parse(currentFrom).getTime(),7);
                        long t=getPastTime(new Date().getTime(),91);
                        if(f>t){

                            currentFrom=sdf.format(new Date(f));
                            currentTo=sdf.format(new Date(getFutureTime(f,6)));
                            Log.d(currentFrom,currentTo);
                            ((MainActivity)getActivity()).logevent("History_time_selection",currentFrom+currentTo,"Touch Event");
                            checkBackAndForwardButton();
                            loadValues();
                            displayloadedValues();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    Calendar calendar = Calendar.getInstance();
                    Log.d("PreviousDates", String.valueOf(getPreviousDates(new Date(), clickCount)));
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    try {
                        long f=getPastTime(sdf.parse(currentFrom).getTime(),30);
                        long t=getPastTime(new Date().getTime(),91);
                        Log.d("SelectedMonthOption", String.valueOf(currentselectedmonthoption));
                        if(f>t){
                            if (currentselectedmonthoption==1){
                                currentFrom=sdf.format(getPreviousDates(new Date(),clickCount));
                                calendar.add(Calendar.MONTH,clickCount);
                                noOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                currentTo=sdf.format(calendar.getTimeInMillis());
                                currentFrom=sdf.format(getPreviousDates(new Date(),clickCount));
                                currentTo=sdf.format(new Date(getFutureTime(f,29)));
                                Log.d("FROM->TO",currentFrom+"->"+currentTo);
                            }else{
                                currentFrom=sdf.format(new Date(f));
                                currentTo=sdf.format(new Date(getFutureTime(f,29)));
                                Log.d("FROM->TO",currentFrom+"->"+currentTo);
                            }
                            ((MainActivity)getActivity()).logevent("History_time_selection",currentFrom+currentTo,"Touch Event");
                            checkBackAndForwardButton();
                            loadValues();
                            displayloadedValues();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount++;
                if(weekoptionenabled){
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
                    try {

                        long f=getFutureTime(sdf.parse(currentFrom).getTime(),7);
                        long t=new Date().getTime();
                        if(f<t){
                            currentFrom=sdf.format(new Date(f));
                            currentTo=sdf.format(new Date(getFutureTime(f,6)));
                            Log.d(currentFrom,currentTo);
                            ((MainActivity)getActivity()).logevent("History_time_selection",currentFrom+currentTo,"Touch Event");
                            checkBackAndForwardButton();
                            loadValues();
                            displayloadedValues();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    Calendar cal = Calendar.getInstance();

                    Log.d("ForwardDates", String.valueOf(cal.getActualMaximum(Calendar.DATE)));
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
                    try {
                        long f=getFutureTime(sdf.parse(currentFrom).getTime(),30);
                        long t=new Date().getTime();
                        if(f<t){
                            if (currentselectedmonthoption==1){
                                currentFrom=sdf.format(getPreviousDates(new Date(),clickCount));
                                cal.add(Calendar.MONTH,clickCount);
                                noOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                                cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                                currentTo=sdf.format(cal.getTimeInMillis());
                                Log.d("FROM->TO",currentFrom+"->"+currentTo);
                            }else{
                                currentFrom=sdf.format(new Date(f));
                                currentTo=sdf.format(new Date(getFutureTime(f,29)));
                                Log.d("FROM->TO",currentFrom+"->"+currentTo);
                            }

                            ((MainActivity)getActivity()).logevent("History_time_selection",currentFrom+currentTo,"Touch Event");
                            checkBackAndForwardButton();
                            loadValues();
                            displayloadedValues();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private Date getPreviousDates(Date nowDate, int clickCount) {
        Log.d("clickCOunt", String.valueOf(clickCount));
        Calendar c = Calendar.getInstance();
        c.setTime(nowDate);
        c.add(Calendar.MONTH, clickCount);
        c.set(Calendar.DATE, c.getMinimum(Calendar.DATE));
        noOfDays = c.getActualMaximum(Calendar.DATE);
        return c.getTime();
    }

    int currentslot=92;
    private void loadHomeButton(){
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });
    }



    private void loadFlipSwitch() {
        flipswitchbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rupeeselected=!rupeeselected;
                changeUnit();
                displayloadedValues();
            }
        });
        flipswitchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rupeeselected=!rupeeselected;
                changeUnit();
                displayloadedValues();
            }
        });
        rupeefliptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rupeeselected=true;
                changeUnit();
                displayloadedValues();
            }
        });
        litresfliptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rupeeselected=false;
                changeUnit();
                displayloadedValues();
            }
        });
    }


    private void displayloadedValues(){
        Log.d("called","displayloadedValues");
        if(rupeeselected){
            displayloadedValues1();
        }else{
            displayloadedValues2();
        }
        updateDisplay();
    }
    private void updateDisplay() {
        Log.d("called","updatedisplay");
        if(weekoptionenabled){
            loadweekAmount();
            if(curselwebar==0){
                int co=0;
                for(int iii=0;iii<alarms.size();iii++){
                    co+=alarms.get(iii).getValue();
                }
                graphalertcount.setText(String.valueOf(co));
                if(co==0){
                    alertdategraph.setText("-");
                }else if(co==1){
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
                    SimpleDateFormat sdf1=new SimpleDateFormat("dd MMM",Locale.ENGLISH);
                    String d="-";
                    for(int k=0;k<alarms.size();k++){
                        if(alarms.get(k).getValue()!=0){
                            try {
                                d=sdf1.format(sdf.parse(alarms.get(k).getDate()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    alertdategraph.setText(d);
                }else{
                    alertdategraph.setText("-");
                }
            }else{
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
                SimpleDateFormat sdf1=new SimpleDateFormat("dd MMM",Locale.ENGLISH);
                String datatodisp="-";
                HAlert a=new HAlert();
                switch (curselwebar){
                    case 1:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getWeekDates1());
                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getWeekDates1()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 2:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getWeekDates2());
                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getWeekDates2()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 3:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getWeekDates3());
                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getWeekDates3()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case  4:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getWeekDates4());
                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getWeekDates4()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 5:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getWeekDates5());
                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getWeekDates5()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 6:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getWeekDates6());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getWeekDates6()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 7:

                        a=dataHandler.getHAlert(selectedAptId,graphData.getWeekDates7());


                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getWeekDates7()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                }
            }

        }else{
            loadMonthAmount();

            if(curselbar==0){
                int co=0;
                for(int iii=0;iii<alarms.size();iii++){
                    co+=alarms.get(iii).getValue();
                }
                graphalertcount.setText(String.valueOf(co));
                if(co==0){
                    alertdategraph.setText("-");
                }else if(co==1){
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
                    SimpleDateFormat sdf1=new SimpleDateFormat("dd MMM",Locale.ENGLISH);
                    String d="-";
                    for(int k=0;k<alarms.size();k++){
                        if(alarms.get(k).getValue()!=0){
                            try {
                                d=sdf1.format(sdf.parse(alarms.get(k).getDate()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    alertdategraph.setText(d);
                }else{
                    alertdategraph.setText("-");
                }
            }else{
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
                SimpleDateFormat sdf1=new SimpleDateFormat("dd MMM",Locale.ENGLISH);
                String datatodisp="-";
                HAlert a = new HAlert();
                switch (curselbar){

                    case 1:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates1());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates1()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 2:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates2());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates2()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 3:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates3());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates3()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 4:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates4());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates4()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 5:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates5());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates5()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 6:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates6());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates6()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 7:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates7());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates7()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 8:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates8());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates8()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 9:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates9());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates9()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 10:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates10());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates10()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;

                    case 11:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates11());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates11()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 12:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates12());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates12()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 13:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates13());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates13()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 14:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates14());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates14()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 15:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates15());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates15()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 16:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates16());
                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates16()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 17:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates17());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates17()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 18:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates18());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates18()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 19:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates19());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates19()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 20:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates20());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates20()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;

                    case 21:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates21());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates21()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 22:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates22());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates22()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 23:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates23());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates23()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 24:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates24());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates24()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 25:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates25());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates25()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 26:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates26());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates26()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 27:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates27());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates27()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 28:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates28());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates28()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 29:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates29());

                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates29()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 30:
                        a=dataHandler.getHAlert(selectedAptId,graphData.getMonthDates30());
                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates30()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                    case 31:
                        a = dataHandler.getHAlert(selectedAptId,graphData.getMonthDates31());
                        datatodisp="-";
                        try {
                            datatodisp=sdf1.format(sdf.parse(graphData.getMonthDates31()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertdategraph.setText(datatodisp);
                        graphalertcount.setText(String.valueOf(a.getValue()));
                        break;
                }
            }

        }


    }
    private void displayloadedValues2() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        SimpleDateFormat sdf1=new SimpleDateFormat("dd MMM",Locale.ENGLISH);
        try {
            roomType.setText(sdf1.format(sdf.parse(currentFrom)));
            roomType.append(" - ");
            roomType.append(sdf1.format(sdf.parse(currentTo)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(weekoptionenabled){

            amountdisplaygraph.setText(String.format("%.1f",graphData.getWeektotal()));
            amountdisplaygraphtype.setText("Total");

            double maxtodis=graphData.getWeekmax();
            ccomodity1=(maxtodis/7);
            ccomodity2=(maxtodis*2)/7;
            ccomodity3=(maxtodis*3)/7;
            ccomodity4=(maxtodis*4)/7;
            ccomodity5=(maxtodis*5)/7;
            ccomodity6=(maxtodis*6)/7;

            comodity1.setText(String.format("%.1f",(ccomodity1)));
            comodity2.setText(String.format("%.1f",(ccomodity2)));
            comodity3.setText(String.format("%.1f",(ccomodity3)));
            comodity4.setText(String.format("%.1f",(ccomodity4)));
            comodity5.setText(String.format("%.1f",(ccomodity5)));
            comodity6.setText(String.format("%.1f",(ccomodity6)));

            comodity.setText("litres");
            weekbar1.init((int) maxtodis,(int)graphData.getWeekAvg(),(int)graphData.getWeekvalue1());
            weekbar2.init((int) maxtodis,(int)graphData.getWeekAvg(),(int)graphData.getWeekvalue2());
            weekbar3.init((int) maxtodis,(int)graphData.getWeekAvg(),(int)graphData.getWeekvalue3());
            weekbar4.init((int) maxtodis,(int)graphData.getWeekAvg(),(int)graphData.getWeekvalue4());
            weekbar5.init((int) maxtodis,(int)graphData.getWeekAvg(),(int)graphData.getWeekvalue5());
            weekbar6.init((int) maxtodis,(int)graphData.getWeekAvg(),(int)graphData.getWeekvalue6());
            weekbar7.init((int) maxtodis,(int)graphData.getWeekAvg(),(int)graphData.getWeekvalue7());

        }else{
            amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthtotal()));
            amountdisplaygraphtype.setText("Total");
            double maxtodis=graphData.getMonthmax();
            ccomodity1=(maxtodis/7);
            ccomodity2=(maxtodis*2)/7;
            ccomodity3=(maxtodis*3)/7;
            ccomodity4=(maxtodis*4)/7;
            ccomodity5=(maxtodis*5)/7;
            ccomodity6=(maxtodis*6)/7;
            comodity1.setText(String.format("%.1f",(ccomodity1)));
            comodity2.setText(String.format("%.1f",(ccomodity2)));
            comodity3.setText(String.format("%.1f",(ccomodity3)));
            comodity4.setText(String.format("%.1f",(ccomodity4)));
            comodity5.setText(String.format("%.1f",(ccomodity5)));
            comodity6.setText(String.format("%.1f",(ccomodity6)));
            comodity.setText("litres");
            bar1.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue1());
            bar2.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue2());
            bar3.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue3());
            bar4.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue4());
            bar5.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue5());
            bar6.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue6());
            bar7.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue7());
            bar8.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue8());
            bar9.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue9());
            bar10.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue10());

            bar11.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue11());
            bar12.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue12());
            bar13.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue13());
            bar14.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue14());
            bar15.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue15());
            bar16.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue16());
            bar17.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue17());
            bar18.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue18());
            bar19.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue19());
            bar20.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue20());

            bar21.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue21());
            bar22.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue22());
            bar23.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue23());
            bar24.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue24());
            bar25.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue25());
            bar26.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue26());
            bar27.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue27());
            bar28.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue28());
            bar29.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue29());
            bar30.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue30());
            bar31.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue31());



        }

    }

    private void displayloadedValues1() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        SimpleDateFormat sdf1=new SimpleDateFormat("dd MMM",Locale.ENGLISH);
        try {
            roomType.setText(sdf1.format(sdf.parse(currentFrom)));
            roomType.append(" - ");
            roomType.append(sdf1.format(sdf.parse(currentTo)));
            Log.d("setting",currentFrom+">>"+currentTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(weekoptionenabled){

            amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getWeektotal())));
            amountdisplaygraphtype.setText("Total");

            double maxtodis=graphData.getWeekmax();

            ccomodity1=(maxtodis/7);
            ccomodity2=(maxtodis*2)/7;
            ccomodity3=(maxtodis*3)/7;
            ccomodity4=(maxtodis*4)/7;
            ccomodity5=(maxtodis*5)/7;
            ccomodity6=(maxtodis*6)/7;
            comodity1.setText(String.format("%.2f",getRupee(ccomodity1)));
            comodity2.setText(String.format("%.2f",getRupee(ccomodity2)));
            comodity3.setText(String.format("%.2f",getRupee(ccomodity3)));
            comodity4.setText(String.format("%.2f",getRupee(ccomodity4)));
            comodity5.setText(String.format("%.2f",getRupee(ccomodity5)));
            comodity6.setText(String.format("%.2f",getRupee(ccomodity6)));
            comodity.setText("rupees");
            weekbar1.init((int) maxtodis,(int)graphData.getWeekAvg(),(int)graphData.getWeekvalue1());
            weekbar2.init((int) maxtodis,(int)graphData.getWeekAvg(),(int)graphData.getWeekvalue2());
            weekbar3.init((int) maxtodis,(int)graphData.getWeekAvg(),(int)graphData.getWeekvalue3());
            weekbar4.init((int) maxtodis,(int)graphData.getWeekAvg(),(int)graphData.getWeekvalue4());
            weekbar5.init((int) maxtodis,(int)graphData.getWeekAvg(),(int)graphData.getWeekvalue5());
            weekbar6.init((int) maxtodis,(int)graphData.getWeekAvg(),(int)graphData.getWeekvalue6());
            weekbar7.init((int) maxtodis,(int)graphData.getWeekAvg(),(int)graphData.getWeekvalue7());

        }else{
            amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthtotal())));
            amountdisplaygraphtype.setText("Total");

            int len=String.valueOf(graphData.getMonthmax()).length();
            double maxtodis=graphData.getMonthmax();
            ccomodity1=(maxtodis/7);
            ccomodity2=(maxtodis*2)/7;
            ccomodity3=(maxtodis*3)/7;
            ccomodity4=(maxtodis*4)/7;
            ccomodity5=(maxtodis*5)/7;
            ccomodity6=(maxtodis*6)/7;
            comodity1.setText(String.format("%.2f",getRupee(ccomodity1)));
            comodity2.setText(String.format("%.2f",getRupee(ccomodity2)));
            comodity3.setText(String.format("%.2f",getRupee(ccomodity3)));
            comodity4.setText(String.format("%.2f",getRupee(ccomodity4)));
            comodity5.setText(String.format("%.2f",getRupee(ccomodity5)));
            comodity6.setText(String.format("%.2f",getRupee(ccomodity6)));
            comodity.setText("rupees");
            bar1.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue1());
            bar2.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue2());
            bar3.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue3());
            bar4.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue4());
            bar5.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue5());
            bar6.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue6());
            bar7.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue7());
            bar8.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue8());
            bar9.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue9());
            bar10.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue10());

            bar11.init((int) maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue11());
            bar12.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue12());
            bar13.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue13());
            bar14.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue14());
            bar15.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue15());
            bar16.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue16());
            bar17.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue17());
            bar18.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue18());
            bar19.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue19());
            bar20.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue20());

            bar21.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue21());
            bar22.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue22());
            bar23.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue23());
            bar24.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue24());
            bar25.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue25());
            bar26.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue26());
            bar27.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue27());
            bar28.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue28());
            bar29.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue29());
            bar30.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue30());
            bar31.init((int)maxtodis,(int)graphData.getMonthAvg(),(int)graphData.getMonthvalue31());
        }
    }
    private void changeUnit() {
        Typeface typeface1=Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_regular.ttf");
        Typeface typeface2=Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_light.ttf");
        if(rupeeselected){
            ((MainActivity)getActivity()).logevent("Dashboard_Toggle",rupeefliptext.getText().toString(),"Touch Event");
            rupeefliptext.setTypeface(typeface1);
            litresfliptext.setTypeface(typeface2);
            flipswitch.setGravity(Gravity.TOP);
        }else{
            ((MainActivity)getActivity()).logevent("Dashboard_Toggle",litresfliptext.getText().toString(),"Touch Event");
            rupeefliptext.setTypeface(typeface2);
            litresfliptext.setTypeface(typeface1);
            flipswitch.setGravity(Gravity.BOTTOM);
        }
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("defaults_pref", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("currencySelected",rupeeselected).apply();
    }
    @Override
    public void onResume() {
        super.onResume();
        final String[] mobile = LoginHandler.getUserMobile(getContext());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data_status",Context.MODE_PRIVATE);
        boolean loaded = sharedPreferences.getBoolean("historyLoaded",false);
        if (!loaded){
            spinner.setVisibility(View.VISIBLE);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                HistoryHelper.getHistoryData(mobile[0],mobile[1], FirebaseInstanceId.getInstance().getToken(),HistoryFragment.this, String.valueOf(selectedAptId));
            }
        },100);

        loadValues();
        displayloadedValues();

    }
    private void loadweekAmount() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        SimpleDateFormat sdf1=new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
        SimpleDateFormat sdfe=new SimpleDateFormat("dd-EEE-yyyy",Locale.ENGLISH);
        SimpleDateFormat sdf44=new SimpleDateFormat("dd MMM",Locale.ENGLISH);
        try {
            String date1=sdfe.format(sdf.parse(graphData.getWeekDates1()));
            weekbardate1.setText(date1.split("-")[0]);
            weekbarday1.setText(date1.split("-")[1]);

            String date2=sdfe.format(sdf.parse(graphData.getWeekDates2()));
            weekbardate2.setText(date2.split("-")[0]);
            weekbarday2.setText(date2.split("-")[1]);

            String date3=sdfe.format(sdf.parse(graphData.getWeekDates3()));
            weekbardate3.setText(date3.split("-")[0]);
            weekbarday3.setText(date3.split("-")[1]);

            String date4=sdfe.format(sdf.parse(graphData.getWeekDates4()));
            weekbardate4.setText(date4.split("-")[0]);
            weekbarday4.setText(date4.split("-")[1]);

            String date5=sdfe.format(sdf.parse(graphData.getWeekDates5()));
            weekbardate5.setText(date5.split("-")[0]);
            weekbarday5.setText(date5.split("-")[1]);

            String date6=sdfe.format(sdf.parse(graphData.getWeekDates6()));
            weekbardate6.setText(date6.split("-")[0]);
            weekbarday6.setText(date6.split("-")[1]);

            String date7=sdfe.format(sdf.parse(graphData.getWeekDates7()));
            weekbardate7.setText(date7.split("-")[0]);
            weekbarday7.setText(date7.split("-")[1]);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(rupeeselected){
            maxgraphval.setText(String.valueOf((int)getRupee(graphData.getWeekmax())));
            mingraphval.setText(String.valueOf((int)getRupee(graphData.getWeekmin())));

            Log.d("max",String.valueOf((int)getRupee(graphData.getWeekmax())));
            Log.d("min",String.valueOf((int)getRupee(graphData.getWeekmin())));

        }else {
            maxgraphval.setText(String.format("%.1f",graphData.getWeekmax()));
            mingraphval.setText(String.format("%.1f",graphData.getWeekmin()));

            Log.d("max",String.format("%.2f",graphData.getWeekmax()));
            Log.d("min",String.format("%.2f",graphData.getWeekmin()));
        }
        try {
            maxgraphvaldate.setText(sdf44.format(sdf.parse(graphData.getWeekmaxdate())));
            mingraphvaldate.setText(sdf44.format(sdf.parse(graphData.getWeekmindate())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(curselwebar==0){
            if(rupeeselected){
                amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getWeektotal())));

            }else{
                amountdisplaygraph.setText(String.format("%.1f",graphData.getWeektotal()));

            }
            for(int i:weekbarids){
                ((Bar)vvv.findViewById(i)).setTouched(false);
            }
            touchline.setVisibility(View.INVISIBLE);
            amountdisplaygraphtype.setText("Total");

        }else{
            for(int i:weekbarids){
                ((Bar)vvv.findViewById(i)).setTouched(false);
            }
            touchline.setVisibility(View.VISIBLE);
            switch (curselwebar){
                case 1:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getWeekvalue1())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getWeekvalue1()));

                    }
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getWeekDates1())));
                        weekbar1.setTouched(true);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getWeekvalue2())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getWeekvalue2()));

                    }

                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getWeekDates2())));
                        weekbar2.setTouched(true);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getWeekvalue3())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getWeekvalue3()));

                    }
                    try {
                        weekbar3.setTouched(true);
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getWeekDates3())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getWeekvalue4())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getWeekvalue4()));

                    }
                    weekbar4.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getWeekDates4())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:

                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getWeekvalue5())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getWeekvalue5()));

                    }
                    weekbar5.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getWeekDates5())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getWeekvalue6())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getWeekvalue6()));

                    }
                    weekbar6.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getWeekDates6())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getWeekvalue7())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getWeekvalue7()));

                    }
                    weekbar7.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getWeekDates7())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }





    }
    private void loadMonthAmount() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        SimpleDateFormat sdf1=new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
        SimpleDateFormat sdfe=new SimpleDateFormat("dd-EEE-yyyy",Locale.ENGLISH);
        SimpleDateFormat sdf44=new SimpleDateFormat("dd MMM",Locale.ENGLISH);
        try {
            String date1=sdfe.format(sdf.parse(graphData.getMonthDates1()));
            monthbardate1.setText(date1.split("-")[0]);
            monthbarday1.setText(date1.split("-")[1]);

            String date2=sdfe.format(sdf.parse(graphData.getMonthDates6()));
            monthbardate2.setText(date2.split("-")[0]);
            monthbarday2.setText(date2.split("-")[1]);

            String date3=sdfe.format(sdf.parse(graphData.getMonthDates11()));
            monthbardate3.setText(date3.split("-")[0]);
            monthbarday3.setText(date3.split("-")[1]);

            String date4=sdfe.format(sdf.parse(graphData.getMonthDates16()));
            monthbardate4.setText(date4.split("-")[0]);
            monthbarday4.setText(date4.split("-")[1]);

            String date5=sdfe.format(sdf.parse(graphData.getMonthDates21()));
            monthbardate5.setText(date5.split("-")[0]);
            monthbarday5.setText(date5.split("-")[1]);

            String date6=sdfe.format(sdf.parse(graphData.getMonthDates26()));
            monthbardate6.setText(date6.split("-")[0]);
            monthbarday6.setText(date6.split("-")[1]);


        } catch (ParseException e) {
            e.printStackTrace();
        }



        if(rupeeselected){
            maxgraphval.setText(String.valueOf((int)getRupee(graphData.getMonthmax())));
            mingraphval.setText(String.valueOf((int)getRupee(graphData.getMonthmin())));

        }else {
            maxgraphval.setText(String.format("%.1f",graphData.getMonthmax()));
            mingraphval.setText(String.format("%.1f",graphData.getMonthmin()));
        }
        try {
            maxgraphvaldate.setText(sdf44.format(sdf.parse(graphData.getMonthMaxdate())));
            mingraphvaldate.setText(sdf44.format(sdf.parse(graphData.getMonthmindate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(curselbar==0){
            if(rupeeselected){
                amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthtotal())));

            }else{
                amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthtotal()));

            }
            for(int i:barids){
                ((Bar)vvv.findViewById(i)).setTouched(false);
            }
            touchline.setVisibility(View.INVISIBLE);


            ///////////////////////////////////////////////////////////


            amountdisplaygraphtype.setText("Total");
        }else{
            for(int i:barids){
                ((Bar)vvv.findViewById(i)).setTouched(false);
            }
            touchline.setVisibility(View.VISIBLE);
            switch (curselbar){
                case 1:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue1())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue1()));

                    }
                    bar1.setTouched(true);
                    try {

                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates1())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue2())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue2()));

                    }
                    bar2.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates2())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;

                case 3:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue3())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue3()));

                    }
                    bar3.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates3())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;

                case 4:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue4())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue4()));

                    }
                    bar4.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates4())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;

                case 5:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue5())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue5()));

                    }
                    bar5.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates5())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue6())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue6()));

                    }
                    bar6.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates6())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue7())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue7()));

                    }
                    bar7.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates7())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue8())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue8()));

                    }
                    bar8.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates8())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue9())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue9()));

                    }
                    bar9.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates9())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 10:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue10())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue10()));

                    }
                    bar10.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates10())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 11:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue11())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue11()));

                    }
                    bar11.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates11())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 12:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue12())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue12()));

                    }
                    bar12.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates12())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 13:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue13())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue13()));

                    }
                    bar13.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates13())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 14:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue14())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue14()));

                    }
                    bar14.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates14())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 15:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue15())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue15()));

                    }
                    bar15.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates15())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 16:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue16())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue16()));

                    }
                    bar16.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates16())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 17:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue17())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue17()));

                    }
                    bar17.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates17())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 18:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue18())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue18()));

                    }
                    bar18.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates18())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 19:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue19())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue19()));

                    }
                    bar19.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates19())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 20:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue20())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue20()));

                    }
                    bar20.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates20())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 21:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue21())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue21()));

                    }
                    bar21.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates21())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 22:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue22())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue22()));

                    }
                    bar22.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates22())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 23:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue23())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue23()));

                    }
                    bar23.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates23())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 24:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue24())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue24()));

                    }
                    bar24.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates24())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 25:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue25())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue25()));

                    }
                    bar25.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates25())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 26:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue26())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue26()));

                    }
                    bar26.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates26())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 27:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue27())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue27()));

                    }
                    bar27.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates27())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 28:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue28())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue28()));

                    }
                    bar28.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates28())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 29:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue29())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue29()));

                    }
                    bar29.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates29())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 30:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue30())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue30()));

                    }
                    bar30.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates30())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 31:
                    if(rupeeselected){
                        amountdisplaygraph.setText(String.valueOf((int)getRupee(graphData.getMonthvalue31())));

                    }else{
                        amountdisplaygraph.setText(String.format("%.1f",graphData.getMonthvalue31()));

                    }
                    bar31.setTouched(true);
                    try {
                        amountdisplaygraphtype.setText(sdf44.format(sdf.parse(graphData.getMonthDates31())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;


            }
        }
    }
    private void loadAptId() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("defaults_pref", Context.MODE_PRIVATE);
        selectedAptId = sharedPreferences.getInt("apartmentIdSelected",-1);
        apartment = dataHandler.getApartment(selectedAptId);
        rupeeselected = sharedPreferences.getBoolean("currencySelected",false);
    }

    @Override
    public Context getInstance() {
        return getContext();
    }

    @Override
    public void loadData(boolean latest) {
//        if(alertDialog!=null){
//            if(alertDialog.isShowing()){
//                alertDialog.dismiss();
//            }
//        }
        if(latest){
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("defaults_pref",Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("firstSelection",false).apply();
        }else{
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("defaults_pref",Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("firstSelection",true).apply();

        }
        
        isloaded = true;
        loadValues();
        displayloadedValues();
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void errorGettingData(String response, int httpResult, String url, String xmsin, String token) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
        String dateTime = sdf.format(new Date());
        String[] mobile = LoginHandler.getUserMobile(getContext());
        CrashHelper.SendCrashMailer(mobile[0], AppConstants.APPVERSION, String.valueOf(httpResult),response+"REQUEST_URL:"+url+"X_MSIN:"+xmsin+"TOKEN:"+token,dateTime,"android");
    }

    @Override
    public void RecheckData() {

    }

    List<Slabs> slabsList = new ArrayList<>();
    private void loadslabs() {
        slabsList = dataHandler.getSlabs(selectedAptId);
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
}
