package com.wateron.smartrhomes.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.os.ConfigurationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.wateron.smartrhomes.R;
import com.wateron.smartrhomes.component.AppConstants;
import com.wateron.smartrhomes.models.Apartment;
import com.wateron.smartrhomes.models.Meter;
import com.wateron.smartrhomes.models.Slabs;
import com.wateron.smartrhomes.util.CrashHelper;
import com.wateron.smartrhomes.util.DashboardHandlerInterface;
import com.wateron.smartrhomes.util.DashboardHelper;
import com.wateron.smartrhomes.util.DataHelper;
import com.wateron.smartrhomes.util.LoginHandler;
import com.wateron.smartrhomes.util.TokenSessionHandler;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity implements DashboardHandlerInterface,TokenSessionHandler {
    LinearLayout loading_bar ;
    ImageView whitebar;
    public boolean isLoogedin = false;
    private int selectedAptId;
    private List<Apartment> apartments;
    List<Meter> meterList;
    List<Slabs> SlabsList;
    private boolean currencySelected;
    private boolean previous_state;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String locale = getApplicationContext().getResources().getConfiguration().locale.getCountry();
        String current = getApplicationContext().getResources().getConfiguration().locale.getDisplayCountry();
        Log.d("DeviceLocale:",current);
//        SharedPreferences shp = getSharedPreferences("toast_state",MODE_PRIVATE);
        loading_bar = (LinearLayout)findViewById(R.id.id111);
        progressBarCircle=(ImageView)findViewById(R.id.progressBarCircle);
        whitebar = (ImageView)findViewById(R.id.whitebar);
        Log.d("OnCreate","activity created");
        SharedPreferences shp = getSharedPreferences("APP_STARTUP",MODE_PRIVATE);
//        String last_mobile = shp.getString("selectedmobile","");
        previous_state = shp.getBoolean("APP_LOGGED_IN",false);

//        Toast.makeText(getApplicationContext(),"saved User mobile and state"+userm+" "+previous_state,Toast.LENGTH_LONG).show();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        if(LoginHandler.isUserLoggedIn(getApplicationContext())){

//                if(isLoogedin){
            if (!isOnline()){
                final AlertDialog.Builder builder =new AlertDialog.Builder(this);
                builder.setTitle("No internet Connection");
                builder.setMessage("Please turn on internet connection to continue");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // To test, Add a Toast, with a context of getBaseContext() and message such as
                        // "I can't believe you pressed me! You better sleep with one eye open tonight. Just sayin'."

                        builder.setCancelable(true);
                    }

                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }else{
                fetchData();
            }

        }else {
            if(previous_state ){
                if (!isOnline()){
                    final AlertDialog.Builder builder =new AlertDialog.Builder(this);
                    builder.setTitle("No internet Connection");
                    builder.setMessage("Please turn on internet connection to continue");

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // To test, Add a Toast, with a context of getBaseContext() and message such as
                            // "I can't believe you pressed me! You better sleep with one eye open tonight. Just sayin'."
                            builder.setCancelable(true);
                        }

                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{

                    String userm = getSharedPreferences("userdaetails",MODE_PRIVATE).getString("mobile","");
                    Log.d("UserMobile",userm);
                    String mobile = userm.substring(userm.length()-10);
                    String isd = userm.substring(0,2);
                    LoginHandler.loginUserToApp(mobile,isd,true,getApplicationContext());
                    DashboardHelper.getPreviouslyUsedToken(isd,mobile,this);
                    loading_bar.setVisibility(View.VISIBLE);
                    pg=0;
                    setBarProgress(pg);
                    handler.post(runnable);
                    DashboardHelper.PreloadDashboardData(mobile,isd, FirebaseInstanceId.getInstance().getToken(),this);
                }


            }else{
                loadWelcomePage();
            }
        }
    }

    private boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(getApplicationContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d("OnPostResume","activity started");

    }

    @Override
    protected void onStart() {
        Log.d("OnSTart","activity started");
        super.onStart();
    }

    private void fetchData() {
        SharedPreferences sharedPreferences=getSharedPreferences("toast_state",Context.MODE_PRIVATE);
        long lasttimeloadedat=sharedPreferences.getLong("lastdate",0);
        System.out.println("Last time loaded :"+lasttimeloadedat);
//        if(lasttimeloadedat==0){
        //DataManager.getFullDataFromServer(SplashActivity.this,true);
        Log.d("OnCreate","activity created");
        loading_bar.setVisibility(View.VISIBLE);
        pg=0;
        setBarProgress(pg);

        String[] mobile = LoginHandler.getUserMobile(getApplicationContext());
        DashboardHelper.PreloadDashboardData(mobile[0],mobile[1], FirebaseInstanceId.getInstance().getToken(),this);
        handler.post(runnable);
        /*DashboardExecutor dashboardExecutor = new DashboardExecutor();
        dashboardExecutor.execute();
        Log.d("Executing Handler","Fetcging data");
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void fetchDataAndGoToHome() {
    }

    int pg =0;
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        public void run() {
            System.out.println("===========Executing Runnable=============");
            pg+=1;
            //loadHomePage();
            if(pg<25){
                //pg+=10;
                Log.d("BarProgress", String.valueOf(pg));
                setBarProgress(pg);
                handler.post(runnable);
            }else if((pg >25)&&(pg<=70)) {
                Log.d("BarProgress", String.valueOf(pg));
                //pg=100;
                setBarProgress(pg);
                handler.post(runnable);
            }else if((pg>70)&&(pg<=98)) {
                Log.d("BarProgress>85", String.valueOf(pg));
                pg +=1;
                setBarProgress(pg);
                handler.post(runnable);
//                loadHomePage();
            }else if (pg>98 && pg<101){
                loadHomePage();
            }

        }
    };
    /*Runnable loadingThread = new Runnable() {
        @Override
        public void run() {
            TimerTask timer = null;
            while (pg<100){

                timer = new TimerTask() {
                    @Override
                    public void run() {
                        ++pg;
                    }

                };
//                    timer.scheduledExecutionTime();
            }

            Timer ticks = new Timer(true);
            ticks.scheduleAtFixedRate(timer,1000,5000);
            Log.d("BarProgress", String.valueOf(pg));
            setBarProgress(pg);
        }
    };*/

    /*private void fetchDataAndGoToHome() {

        String userm = getSharedPreferences("userdaetails",MODE_PRIVATE).getString("mobile","");
        Log.d("Mobile Foundddddddddd:",userm);
        if(lasttimeloadedat==0){
            //DataManager.getFullDataFromServer(SplashActivity.this,true);
            loading_bar.setVisibility(View.VISIBLE);
            pg=2;
            setBarProgress(pg);
            handler.postDelayed(runnable,150);
        }else{
            long currenttime=new Date().getTime();
            if(currenttime>(lasttimeloadedat+7200000)){
                //DataManager.getFullDataFromServer(SplashActivity.this,true);


                loading_bar.setVisibility(View.VISIBLE);
                pg=2;
                setBarProgress(pg);
                handler.postDelayed(runnable,150);
            }else{
                handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //goToDashBoard();
                    }
                },200);
            }
        }
    }*/
    ImageView progressBarCircle;
    private void setBarProgress(int p) {
        addMarginsInDp1(whitebar,((154*p)/100)+8,10);
        addMarginsInDp(progressBarCircle,(154*p)/100,0,0,0);
    }
    private void addMarginsInDp1(View view, int leftInDp, int topInDp) {
        DisplayMetrics dm = view.getResources().getDisplayMetrics();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(convertDpToPx(leftInDp,dm),convertDpToPx(topInDp,dm));
        view.setLayoutParams(lp);
    }
    private void addMarginsInDp(View view, int leftInDp, int topInDp, int rightInDp, int bottomInDp) {
        DisplayMetrics dm = view.getResources().getDisplayMetrics();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(convertDpToPx(16,dm),convertDpToPx(16,dm));

        lp.setMargins(convertDpToPx(leftInDp, dm), convertDpToPx(topInDp, dm), convertDpToPx(rightInDp, dm), convertDpToPx(bottomInDp, dm));
        view.setLayoutParams(lp);
    }
    private int convertDpToPx(int dp, DisplayMetrics displayMetrics) {
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
        return Math.round(pixels);
    }

    private void loadRealTimeHome() {
        Intent intent = new Intent(SplashActivity.this,RealTimeActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    private void loadWelcomePage() {
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,PreLoginActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        },1000);

    }

    private void loadHomePage() {
//        SharedPreferences sharedPreferences=getSharedPreferences("toast_state",Context.MODE_PRIVATE);
//        long lasttimeloadedat=sharedPreferences.getLong("lastdate",0);
//        Log.d("Last time modified :", String.valueOf(lasttimeloadedat));
        //fetchData();
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        intent.putExtra("isRefreshed",true);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    @Override
    public Context getInstance() {
        return getApplicationContext();
    }

    @Override
    public void loadData(boolean latest) {
        System.out.println("LoadingData"+latest);
        handler.post(runnable);

//        Handler delay = new Handler();
//        delay.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                loadHomePage();
//            }
//        },1000);
    }

    @Override
    public void errorGettingData(String response, int httpResult, String url, String xmsin, String token) {
            Log.d("ResponseError:Code",response+":"+httpResult);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
            String dateTime = sdf.format(new Date());
            String[] mobile = LoginHandler.getUserMobile(getApplicationContext());
            CrashHelper.SendCrashMailer("("+mobile[1]+")"+mobile[0], AppConstants.APPVERSION, String.valueOf(httpResult),response+"REQUEST_URL:"+url+"X_MSIN:"+xmsin+"TOKEN:"+token,dateTime,"android");
            Toast.makeText(getApplicationContext(),"Error Fetching Apartment data, please signin again",Toast.LENGTH_LONG).show();
            DataHelper dataHandler=new DataHelper(getApplicationContext());
            dataHandler.deletedata();
            String prefs[]=new String []{
                    "defaults_pref","login_details","toast_state","userdaetails","data_status","alert_manager","APP_STARTUP"
            };
            for(String s:prefs){
                SharedPreferences sharedPreferences=getSharedPreferences(s,MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
            }

    }

    @Override
    public void updateDailyConsumption(Double yesterday, Double today, HashMap<Integer, ArrayList<Double>> meter_values) {

    }

    @Override
    public void autoRefresher(JSONArray consumptiontoday) {

    }

    @Override
    public void storeToken(String auth) {
        Log.d("authToken",auth);
        SharedPreferences sharedPreferences = getSharedPreferences("login_details",MODE_PRIVATE);
        sharedPreferences.edit().putString("authToken",auth).apply();
    }

    /*@SuppressLint("StaticFieldLeak")
    private class DashboardExecutor extends AsyncTask<String,Void,Void> implements DashboardHandlerInterface {

        //private DashDataDates dashDataDates;
        private int id;

        @Override
        protected Void doInBackground(String... strings) {
            Log.d("Executing","SplashPreloading");

            return null;
        }

        @Override
        public Context getInstance() {
            return getApplicationContext();
        }
        boolean isLoaded = true;
        @Override
        public void loadData(boolean latest) {

        }

        @Override
        public void errorGettingData() {

        }
    }*/
}