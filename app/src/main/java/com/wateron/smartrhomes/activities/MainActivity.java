package com.wateron.smartrhomes.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wateron.smartrhomes.R;
import com.wateron.smartrhomes.application.App;
import com.wateron.smartrhomes.component.AppConstants;
import com.wateron.smartrhomes.fragments.AccountFragment;
import com.wateron.smartrhomes.fragments.AlertFragment;
import com.wateron.smartrhomes.fragments.DashboardFragment;
import com.wateron.smartrhomes.fragments.HistoryFragment;

import com.wateron.smartrhomes.fragments.SettingFragment;
import com.wateron.smartrhomes.fragments.SupportFragment1;
import com.wateron.smartrhomes.fragments.ValveFragment;
import com.wateron.smartrhomes.util.CrashHelper;
import com.wateron.smartrhomes.util.DashboardHandlerInterface;
import com.wateron.smartrhomes.util.DataHelper;
import com.wateron.smartrhomes.util.HistoryHandlerInterface;
import com.wateron.smartrhomes.util.HistoryHelper;
import com.wateron.smartrhomes.util.LoginHandler;
import com.wateron.smartrhomes.util.RefreshHandlerInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,SwipeRefreshLayout.OnRefreshListener,DashboardHandlerInterface,HistoryHandlerInterface,RefreshHandlerInterface {
    public long timecreated=0;
    DrawerLayout drawer;
    NavigationView navigationView;
    Fragment fragment;
    Spinner spinner;

    @SuppressLint("StaticFieldLeak")
    public static  SwipeRefreshLayout refreshLayout;

    private String response;
    private ProgressBar progressBar;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            finish();
            return true;
        }
        return super.onTouchEvent(event);
    }
    int selectedAptId;
    String[] mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("OnCreate","MainActivity");
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        aptids = new ArrayList<>();
        aptnames = new ArrayList<>();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.swp_lout);
        refreshLayout.setColorSchemeResources(R.color.c1,R.color.c2,R.color.c3,R.color.c4);
        refreshLayout.setOnRefreshListener(this);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        refreshLayout.setEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setBackgroundColor(getResources().getColor(R.color.menucolor));
        navigationView.getMenu().getItem(0).setChecked(true);
        View header=navigationView.getHeaderView(0);
        ImageView img=(ImageView)header.findViewById(R.id.ddown1);
        spinner=(Spinner)header.findViewById(R.id.spinner_nav);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });

        drawer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(fragment instanceof DashboardFragment){
                    setMenu(0,0);
                }else if(fragment instanceof HistoryFragment){
                    setMenu(0,1);
                }else if(fragment instanceof AlertFragment){
                    setMenu(0,2);
                }else if(fragment instanceof ValveFragment){
                    setMenu(0,3);
                }else if(fragment instanceof AccountFragment){
                    setMenu(1,0);
                }else if(fragment instanceof SettingFragment){
                    setMenu(1,1);
                }else if(fragment instanceof SupportFragment1){
                    setMenu(0,5);
                }
            }
        });

        if(getIntent()!=null){

            if(getIntent().getBooleanExtra("loadValve",false)){
                SharedPreferences sharedPreferences1=getSharedPreferences("valve_manager",MODE_PRIVATE);
                sharedPreferences1.edit().putBoolean("loadvalve",true).apply();
                Log.d("Intent","showValve");
                showValve();
            }else if(getIntent().getBooleanExtra("loadAlert",false)){
                int screen = getSharedPreferences("alert_manager",Context.MODE_PRIVATE).getInt("apartment_index",-1);
                Log.d("SelectedScreen", String.valueOf(screen));

//                if (screen==0){
//                    spinner.setSelection(1,false);
//                }else{
//                    spinner.setSelection(0,false);
//                }
//                spinner.setOnItemClickListener();
                spinner.setSelection(screen);
                loadSpinner();
                loadAlert();
            }else{
                Log.d("Intent","NotNull");

                loadHome(11);

            }
        }else{
            Log.d("Intent","null");
            loadHome(11);
        }
    }

    private void showValve() {
        fragment = new ValveFragment();
        Log.d("Menu","valve_control click");
        setMenu(0,3);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentview,fragment);
        ft.commit();
        sendScreen("Valve Screen");
    }

    int initplace=0;
    List<String> aptnames;
    List<Integer> aptids;
    public void loadSpinnerView(List<String> apts,int select,List<Integer> aptsid){
        aptnames.clear();
        aptids.clear();
        aptids.addAll(aptsid);
        aptnames.addAll(apts) ;
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,aptnames);
        dataAdapter.setDropDownViewResource(R.layout.spinner_text);
        spinner.setAdapter(dataAdapter);
        if(aptnames.size()!=0){
            initplace = select;
            spinner.setSelection(initplace);
            loadSpinner();
        }
    }

    boolean reloading = false;
    private Boolean apartment1Load =null;
    private Boolean apartment2Load = null;
    private void loadSpinner(){

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSharedPreferences("defaults_pref",Context.MODE_PRIVATE).edit()
                        .putInt("apartmentIdSelected",aptids.get(position))
                        .putInt("apartmentPosition",position)
                        .apply();
                Log.d("Item selected", String.valueOf(position));
//                if (position==1){
//                    if (apartment2Load == null){
////                        String[] mobile = LoginHandler.getUserMobile(getApplicationContext());
////                        SharedPreferences preferences = getSharedPreferences("login_details",MODE_PRIVATE);
////                        String auth = preferences.getString("authToken",null);
////                        DashboardHelper.RefreshDashboardData(mobile[1],mobile[0],selectedAptId,auth,MainActivity.this);
//                        Log.d("Online","Load1");
//                        SharedPreferences sharedPreferences = getSharedPreferences("data_status",Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean("historyLoaded",false);
//                        editor.apply();
//                        SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                        apartment2Load = Preferences.getBoolean("Apartment1firstTime",true);
//                        HistoryHelper.dataLatest = false;
//                        if (apartment2Load){
//                            Log.d("Loading Mode", String.valueOf(apartment2Load));
//                            Preferences.edit().putBoolean("Apartment1firstTime",false).commit();
//                        }
//                    }else {
//                        if (apartment1Load==null){
////                            String[] mobile = LoginHandler.getUserMobile(getApplicationContext());
////                            SharedPreferences preferences = getSharedPreferences("login_details",MODE_PRIVATE);
////                            String auth = preferences.getString("authToken",null);
////                            DashboardHelper.RefreshDashboardData(mobile[1],mobile[0],selectedAptId,auth,MainActivity.this);
//                            Log.d("Online","Load2");
//                            SharedPreferences sharedPreferences = getSharedPreferences("data_status",Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putBoolean("historyLoaded",false);
//
//                            editor.apply();
//                            SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                            apartment1Load = Preferences.getBoolean("Apartment2firstTime",true);
//                            HistoryHelper.dataLatest = false;
//                            if (apartment1Load){
//                                Log.d("Loading Mode", String.valueOf(apartment2Load));
//                                Preferences.edit().putBoolean("Apartment2firstTime",false).commit();
//                            }
//                        }
//                    }
//
//                }
                logevent("Menu Click","Apartment Number","Touch Event");
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("defaults_pref", Context.MODE_PRIVATE);
                selectedAptId = sharedPreferences.getInt("apartmentIdSelected", -1);
                mobile = LoginHandler.getUserMobile(getApplicationContext());
                Handler handler =new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        drawer.closeDrawer(GravityCompat.START);
                    }
                },500);

                Log.d("Reloading?", String.valueOf(reloading));
                if(reloading){
                    reloading = false;

                }else{
                    Log.d("LoadSpinnerReloaded","itemSelected"+position);
                    reloading = true;
                    reload();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void reload() {
        if(fragment instanceof DashboardFragment){
            Log.d("Reloading","Dashboard");
            Bundle bundle = new Bundle();
            bundle.putString("isRefreshed", response);
            fragment=new DashboardFragment();
            fragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction().addToBackStack(null);
            ft.replace(R.id.fragmentview,fragment);
            ft.commitAllowingStateLoss();
            getSupportFragmentManager().executePendingTransactions();
        }else if(fragment instanceof HistoryFragment){
            fragment=new HistoryFragment();
            Log.d("History Fragment","Detected");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentview,fragment);
            ft.commit();
            getSupportFragmentManager().executePendingTransactions();
        }else if(fragment instanceof AlertFragment){
            fragment=new AlertFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentview,fragment);
            ft.commit();
            getSupportFragmentManager().executePendingTransactions();
        }else if(fragment instanceof ValveFragment){
            fragment=new ValveFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentview,fragment);
            ft.commit();
            getSupportFragmentManager().executePendingTransactions();
        }else if(fragment instanceof SettingFragment){
            fragment=new SettingFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentview,fragment);
            ft.commit();
            getSupportFragmentManager().executePendingTransactions();
        }else if(fragment instanceof AccountFragment){
            fragment=new AccountFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentview,fragment);
            ft.commit();
            getSupportFragmentManager().executePendingTransactions();
        }else if(fragment instanceof SupportFragment1){
            fragment=new SupportFragment1();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.fragmentview,fragment);
            ft.commit();
            getSupportFragmentManager().executePendingTransactions();
        }

    }

    @Override
    public void onBackPressed() {
        refreshLayout.setEnabled(true);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            Log.d("Drawer is","Started");
            if(menu==1){
                if(fragment instanceof DashboardFragment){
                    setMenu(0,0);
                }else if(fragment instanceof HistoryFragment){
                    setMenu(0,1);
                }else if(fragment instanceof AlertFragment){
                    setMenu(0,2);
                }else if(fragment instanceof ValveFragment){
                    setMenu(0,3);
                }else if(fragment instanceof AccountFragment){
                    setMenu(0,-1);
                }else if(fragment instanceof SettingFragment){
                    setMenu(0,-1);
                }else if(fragment instanceof SupportFragment1){
                    setMenu(0,5);
                }

            }else{
                if(fragment instanceof DashboardFragment){
                    setMenu(0,0);
                }else if(fragment instanceof HistoryFragment){
                    setMenu(0,1);
                }else if(fragment instanceof AlertFragment){
                    setMenu(0,2);
                }else if(fragment instanceof ValveFragment){
                    setMenu(0,3);
                }else if(fragment instanceof AccountFragment){
                    setMenu(1,0);
                }else if(fragment instanceof SettingFragment){
                    setMenu(1,1);
                }else if(fragment instanceof SupportFragment1){
                    setMenu(0,5);
                }
                drawer.closeDrawer(GravityCompat.START);
            }
        } else {

            if(fragment instanceof DashboardFragment){
                finish();
                Log.d("Drawer is","Dashboard");
            }else if(fragment instanceof HistoryFragment){
                Log.d("Drawer is","History");
                loadHome(3);
            }else if(fragment instanceof AlertFragment){
                Log.d("Drawer is","Alert");
                loadHome(3);
            }else if(fragment instanceof ValveFragment){
                Log.d("Drawer is","Valve");
                loadHome(3);
            }else if(fragment instanceof AccountFragment){
                Log.d("Drawer is","Account");
                loadHome(3);
            }else if(fragment instanceof SettingFragment){
                Log.d("Drawer is","Settings");
                loadHome(3);
            }else if(fragment instanceof SupportFragment1){
                Log.d("Drawer is","Support");
                loadHome(3);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dashboard) {
            Bundle bundle = new Bundle();
            bundle.putString("data", response);
            fragment=new DashboardFragment();
            fragment.setArguments(bundle);
            refreshLayout.setEnabled(true);
            Log.d("Menu","dashboard click");
            setMenu(0,0);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentview,fragment);
            ft.commit();
            getSupportFragmentManager().executePendingTransactions();
            sendScreen("Dashboard Screen");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.history) {
            if ((apartment2Load == null)&&(getSharedPreferences("defaults_pref",Context.MODE_PRIVATE).getInt("apartmentPosition",-1)==0)){
//                        String[] mobile = LoginHandler.getUserMobile(getApplicationContext());
//                        SharedPreferences preferences = getSharedPreferences("login_details",MODE_PRIVATE);
//                        String auth = preferences.getString("authToken",null);
//                        DashboardHelper.RefreshDashboardData(mobile[1],mobile[0],selectedAptId,auth,MainActivity.this);
                Log.d("Online","Load1");
                SharedPreferences sharedPreferences = getSharedPreferences("data_status",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("historyLoaded",false);
                editor.apply();
                SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                apartment2Load = Preferences.getBoolean("Apartment1firstTime",true);
                HistoryHelper.dataLatest = false;
                if (apartment2Load){
                    Log.d("Loading Mode", String.valueOf(apartment2Load));
                    Preferences.edit().putBoolean("Apartment1firstTime",false).commit();
                }
            }else{
                if ((apartment1Load==null)&&(getSharedPreferences("defaults_pref",Context.MODE_PRIVATE).getInt("apartmentPosition",-1)==1)){
//                            String[] mobile = LoginHandler.getUserMobile(getApplicationContext());
//                            SharedPreferences preferences = getSharedPreferences("login_details",MODE_PRIVATE);
//                            String auth = preferences.getString("authToken",null);
//                            DashboardHelper.RefreshDashboardData(mobile[1],mobile[0],selectedAptId,auth,MainActivity.this);
                    Log.d("Online","Load2");
                    SharedPreferences sharedPreferences = getSharedPreferences("data_status",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("historyLoaded",false);

                    editor.apply();
                    SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    apartment1Load = Preferences.getBoolean("Apartment2firstTime",true);
                    HistoryHelper.dataLatest = false;
                    if (apartment1Load){
                        Log.d("Loading Mode", String.valueOf(apartment2Load));
                        Preferences.edit().putBoolean("Apartment2firstTime",false).commit();
                    }
                }
            }
            fragment=new HistoryFragment();
            Log.d("Menu","history click");
            setMenu(0,1);
            refreshLayout.setEnabled(false);
            drawer.closeDrawer(GravityCompat.START);
            drawer.addDrawerListener(new DrawerLayout.DrawerListener() {

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                }

                @Override
                public void onDrawerOpened(View drawerView) {

                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    if (fragment != null) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragmentview,fragment);
                        ft.commit();
                        getSupportFragmentManager().executePendingTransactions();

                    }

                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }

            });

            sendScreen("History Screen");
        }else if(id == R.id.alerts) {
            fragment = new AlertFragment();
            Log.d("Menu","alerts click");
            setMenu(0,2);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentview,fragment);
            ft.commit();
            refreshLayout.setEnabled(false);
            getSupportFragmentManager().executePendingTransactions();
            sendScreen("Alert Screen");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }else if(id == R.id.valve_control){
            fragment = new ValveFragment();
            Log.d("Menu","valve_control click");
            setMenu(0,3);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentview,fragment);
            refreshLayout.setEnabled(false);
            ft.commit();
            getSupportFragmentManager().executePendingTransactions();
            sendScreen("Valve Screen");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }else if(id == R.id.settings){
            Log.d("Menu","settings click");
            setMenu(1,-1);
            refreshLayout.setEnabled(false);
        }else if(id == R.id.support){
            fragment = new SupportFragment1();
            Log.d("Menu","support click");
            setMenu(0,5);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            refreshLayout.setEnabled(false);
            ft.replace(R.id.fragmentview,fragment);
            ft.commit();
            getSupportFragmentManager().executePendingTransactions();
            sendScreen("Support Screen");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }else if(id == R.id.signOut){
            Log.d("Menu","about click");
            deleteEverything();
            logevent("Sign Out","Sign Out","Touch Event");
            Intent intent=new Intent(MainActivity.this,SplashActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }else if(id == R.id.account){
            fragment = new AccountFragment();
            Log.d("Menu","account click");
            setMenu(1,0);
            refreshLayout.setEnabled(false);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentview,fragment);
            ft.commit();
            getSupportFragmentManager().executePendingTransactions();
            sendScreen("Account Setting Screen");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }else if(id == R.id.meter){
            fragment = new SettingFragment();
            Log.d("Menu","meter click");
            setMenu(1,1);
            refreshLayout.setEnabled(false);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentview,fragment);
            ft.commit();
            getSupportFragmentManager().executePendingTransactions();
            sendScreen("Meter Setting  Screen");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        if(!refreshLayout.isRefreshing()){
            System.out.println("Swipe refresh Toggled");

        }
        return true;
    }

    private void deleteEverything() {
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


    private void setMenu(int i,int j) {

        if(i==0){
            selectmenu1=j;
        }else {
            selectmenu2=j;
        }
        if(menu!=i){
            menu = i;
            reloadNavigation();
        }else {
            if(menu==0){
                if(selectmenu1!=-1){
                    navigationView.getMenu().getItem(selectmenu1).setChecked(true);
                }
            }else{
                if(selectmenu2!=-1){
                    navigationView.getMenu().getItem(selectmenu2).setChecked(true);
                }
            }
        }
    }

    public void logevent(String s, String s1, String cat) {
        App application = (App) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory(cat)
                        .setAction(s)
                        .setLabel(s1).build()
        );
        Log.i("GA","logged");
    }

    int menu = 0;
    int selectmenu1=0;
    int selectmenu2=0;
    private void reloadNavigation(){
        navigationView.getMenu().clear();
        if(menu==0){
            navigationView.inflateMenu(R.menu.activity_main_drawer);
            if(selectmenu1!=-1){
                navigationView.getMenu().getItem(selectmenu1).setChecked(true);
            }
        }else{
            navigationView.inflateMenu(R.menu.home);
            if(selectmenu2!=-1){
                navigationView.getMenu().getItem(selectmenu2).setChecked(true);
            }

        }

    }

    public void opendr() {
        logevent("Dashboard_Menu", "Menu Click","Touch Event");
        drawer.openDrawer(Gravity.LEFT);
    }

    public void loadHistory() {
        if ((apartment2Load == null) && (getSharedPreferences("defaults_pref", Context.MODE_PRIVATE).getInt("apartmentPosition", -1) == 0)) {
//                        String[] mobile = LoginHandler.getUserMobile(getApplicationContext());
//                        SharedPreferences preferences = getSharedPreferences("login_details",MODE_PRIVATE);
//                        String auth = preferences.getString("authToken",null);
//                        DashboardHelper.RefreshDashboardData(mobile[1],mobile[0],selectedAptId,auth,MainActivity.this);
            Log.d("Online", "Load1");
            SharedPreferences sharedPreferences = getSharedPreferences("data_status", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("historyLoaded", false);
            editor.apply();
            SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            apartment2Load = Preferences.getBoolean("Apartment1firstTime", true);
            HistoryHelper.dataLatest = false;
            if (apartment2Load) {
                Log.d("Loading Mode", String.valueOf(apartment2Load));
                Preferences.edit().putBoolean("Apartment1firstTime", false).commit();
            }
        } else {
            if ((apartment1Load == null) && (getSharedPreferences("defaults_pref", Context.MODE_PRIVATE).getInt("apartmentPosition", -1) == 1)) {
//                            String[] mobile = LoginHandler.getUserMobile(getApplicationContext());
//                            SharedPreferences preferences = getSharedPreferences("login_details",MODE_PRIVATE);
//                            String auth = preferences.getString("authToken",null);
//                            DashboardHelper.RefreshDashboardData(mobile[1],mobile[0],selectedAptId,auth,MainActivity.this);
                Log.d("Online", "Load2");
                SharedPreferences sharedPreferences = getSharedPreferences("data_status", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("historyLoaded", false);

                editor.apply();
                SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                apartment1Load = Preferences.getBoolean("Apartment2firstTime", true);
                HistoryHelper.dataLatest = false;
                if (apartment1Load) {
                    Log.d("Loading Mode", String.valueOf(apartment2Load));
                    Preferences.edit().putBoolean("Apartment2firstTime", false).commit();
                }
            }

            fragment = new HistoryFragment();
            setMenu(0, 1);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentview, fragment);
            ft.commit();

        }
    }


    public void loadAlert() {
        fragment=new AlertFragment();
        setMenu(0,2);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentview,fragment);
        ft.commit();
    }

    public void loadHome(int note) {
        Log.d("Load","Home");
        setMenu(0,0);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isRefreshed", false);
        fragment=new DashboardFragment();
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentview,fragment);
        ft.commit();
        getSupportFragmentManager().executePendingTransactions();
        if(note==0){
            Toast.makeText(MainActivity.this,"No alerts to display",Toast.LENGTH_LONG).show();
        }
        if(note==1){
            Toast.makeText(MainActivity.this,"This apartment meter does not have Valve. Please switch to apartment which has valve before opening",Toast.LENGTH_LONG).show();
        }
    }

    private void toggleSwipeToRefresh(){

        if(fragment instanceof DashboardFragment){
            Log.d("DASHBOARD","Refresh");
            refreshLayout.setEnabled(true);
        }else{
            Log.d("Others","Norefresh");
            refreshLayout.setEnabled(false);
        }
    }

    public void sendScreen(String s){
        // Get tracker.
        Tracker t = ((App) getApplication()).getDefaultTracker();
        t.setScreenName(s);
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(getApplicationContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    @Override
    public void onRefresh() {
        System.out.println("Refreshing Now");
        final Handler handler = new Handler();
        String mobile[] = LoginHandler.getUserMobile(getApplicationContext());
//        DashboardHelper.getDashboardData(mobile[0],mobile[1], FirebaseInstanceId.getInstance().getToken(),this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if (!isOnline()){
                    final AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
                    refreshLayout.setRefreshing(false);
                    builder.setTitle("No internet Connection");
                    builder.setMessage("Please turn on internet connection to continue");
                    builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction().addToBackStack(null);
                    Bundle bundle = new Bundle();
                    spinner.setFocusable(false);
                    refreshLayout.setRefreshing(false);
                    bundle.putBoolean("isRefreshed",true);
                    fragment=new DashboardFragment();
                    fragment.setArguments(bundle);
                    toggleSwipeToRefresh();
                    ft.replace(R.id.fragmentview,fragment);
                    ft.commit();
                }

            }
        }, 2000);

    }

    @Override
    public Context getInstance() {
        return getApplicationContext();
    }

    @Override
    public void loadData(boolean latest) {


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        },500);

    }

    @Override
    public void errorGettingData(String response, int httpResult, String url, String xmsin, String token) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
        String dateTime = sdf.format(new Date());
        String[] mobile = LoginHandler.getUserMobile(getApplicationContext());
        String versionDetails = System.getProperty("os.version");
        Log.d("VersionDetails",versionDetails);
        CrashHelper.SendCrashMailer("("+mobile[1]+")"+mobile[0], AppConstants.APPVERSION, String.valueOf(httpResult),response+"\n"+"REQUEST_URL:"+url+"\n"+"X_MSIN:"+xmsin+"\n"+"TOKEN:"+token,dateTime,"android");
        Toast.makeText(getApplicationContext(),"Refresh failed Please try again",Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateDailyConsumption(Double yesterday, Double today, HashMap<Integer, ArrayList<Double>> meter_values) {

    }


    @Override
    public void RecheckData() {

    }

    @Override
    public void stopRefreshing() {

    }
}
