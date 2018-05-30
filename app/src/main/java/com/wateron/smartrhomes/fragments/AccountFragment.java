package com.wateron.smartrhomes.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.wateron.smartrhomes.R;
import com.wateron.smartrhomes.activities.MainActivity;
import com.wateron.smartrhomes.component.AccountAdapter;
import com.wateron.smartrhomes.component.AppConstants;
import com.wateron.smartrhomes.models.Account;
import com.wateron.smartrhomes.util.AccountHandlerInterface;
import com.wateron.smartrhomes.util.AccountHelper;
import com.wateron.smartrhomes.util.CrashHelper;
import com.wateron.smartrhomes.util.LoginHandler;
import com.wateron.smartrhomes.util.TicketHandlerInteface;
import com.wateron.smartrhomes.util.TicketHttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Paranjay on 15-12-2017.
 */


public class AccountFragment extends Fragment implements AccountHandlerInterface,AbsListView.OnScrollListener,TicketHandlerInteface {

    private String[] mobile_MSIN;
    private int selectedAptId;
    private Activity activity;
    private Context context;
    private String member_to_delete;
    private int position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.account_fragment,container,false);
        this.activity = getActivity();
        this.context = getContext();
        getContext();
        initView(view);
        initClicks();
        loadStartData();
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return view;
    }

    private void loadStartData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("defaults_pref", MODE_PRIVATE);
        int selectedAptId = sharedPreferences.getInt("apartmentIdSelected",-1);
        String[] mobile = LoginHandler.getUserMobile(getContext());
        AccountHelper.loadFamily(mobile[0],mobile[1], FirebaseInstanceId.getInstance().getToken(),AccountFragment.this,selectedAptId);
    }
    public static boolean isClicked = false;
    private void initClicks() {
        addmemeber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isClicked){
                    isClicked =true;
                    Account account = new Account("(91) ",true);
                    accounts.add(0,account);
                    adapter.notifyDataSetChanged();
                    familylist.smoothScrollToPosition(0);
                    SharedPreferences preferences = activity.getSharedPreferences("login_details",MODE_PRIVATE);
                    preferences.edit().putBoolean("addentry",true).apply();
                    adapter.notifyDataSetChanged();
                }

//                String[] mobile = LoginHandler.getUserMobile(getContext());
//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_details", MODE_PRIVATE);
//                String authToken = sharedPreferences.getString("authToken",null);
//                Log.d("Autho",authToken);
//                AccountHelper.addFamilyMember(mobile[0],mobile[1], authToken,AccountFragment.this);
//                AccountHelper.addFamilyMember(mobile[0],mobile[1], authToken,AccountFragment.this);
                ((MainActivity)activity).logevent("Setting_Account_Add_family","Save Family","Touch Event");
            }
        });
        homemenubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)activity).onBackPressed();
            }
        });
    }
    AccountAdapter adapter;
    List<String> mobiles;
    ListView familylist;
    TextView inst_lc;
    LinearLayout homemenubutton;
    ImageButton addmemeber;
    private void initView(View view) {
        homemenubutton = view.findViewById(R.id.homemenubutton);
        familylist = view.findViewById(R.id.familyMemberList);
        inst_lc = view.findViewById(R.id.inst_lc);
        mobile_MSIN = LoginHandler.getUserMobile(getContext());
        addmemeber=view.findViewById(R.id.addmemeber);

    }


    List<Account> accounts=new ArrayList<>();

    @Override
    public void errorLoadingMembers(String response, int httpResult, String url, String xmsin, String token) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
        String dateTime = sdf.format(new Date());
        String[] mobile = LoginHandler.getUserMobile(getContext());
        CrashHelper.SendCrashMailer("("+mobile[1]+")"+mobile[0], AppConstants.APPVERSION, String.valueOf(httpResult),response+"REQUEST_URL:"+url+"X_MSIN:"+xmsin+"TOKEN:"+token,dateTime+"-"+"AccountSettingsScreen","android");
    }

    @Override
    public void loadData(List<String> numbers) {
        Log.d("Loading Data","Account Inteface");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("defaults_pref", Context.MODE_PRIVATE);
        selectedAptId = sharedPreferences.getInt("apartmentIdSelected",-1);
        TicketHttpHelper.getUserTicketDetails(mobile_MSIN[1],mobile_MSIN[0],selectedAptId, FirebaseInstanceId.getInstance().getToken(),this);
        if(mobiles==null){
            mobiles = new ArrayList<>();
        }else{
            mobiles.clear();
        }
        accounts.clear();
        for(String s:numbers){
            Account account=new Account(s,false);
            accounts.add(account);
        }
        mobiles.addAll(numbers);
        adapter = new AccountAdapter(activity,R.layout.acc_fm_num,accounts,mobiles,AccountFragment.this);
        familylist.setAdapter(adapter);
    }



    @Override
    public void loadData() {

    }

    @Override
    public void loadData(Account member_number) {
//        member_number.setNumber(member_to_delete);
        Account account = adapter.getItem(position);
        adapter.remove(account);
        Log.d("AccountToDelete :",member_number.getNumber());
        adapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Memeber Deleted Successfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public void loadAddedData(String number) {
        Log.d("Loading","Added data");
        Account account =new Account(number,false);
        adapter.insert(account,0);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void errorLoadingDeletedMembers(String response, int httpResult, String s, String s1, String s2, int member_ccode, String member_mobile, long apt_id, String pos) {
//        adapter.remove(adapter.getItem(Integer.parseInt(pos)));
        adapter.notifyDataSetChanged();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
        String dateTime = sdf.format(new Date());
        String[] mobile = LoginHandler.getUserMobile(getContext());
        CrashHelper.SendCrashMailer(mobile[0],AppConstants.APPVERSION, String.valueOf(httpResult),response+"\n"+s1+"\n"+s2+"MEMBER_COUNTRY_CODE:"+member_ccode+"MEMBER_MOBILE:"+member_mobile+"APT_ID:"+apt_id,dateTime,"android");
    }



    public void removeExisitingUser( int position) {
//        Log.d("MSIN,Prevdata,position",is+"-"+mobi+":"+prevdata+"Position"+position);
        Account Remove_before_save = adapter.getItem(position);
        adapter.remove(Remove_before_save);
    }

    public void deleteUser(String mobi, int aptID, int pos) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("login_details",MODE_PRIVATE);
        String token = sharedPreferences.getString("authToken",null);
        mobi=mobi.replace("(","");
        mobi=mobi.replace(")","-");
        position = pos;
        String mobile[] = LoginHandler.getUserMobile(getContext());
        final String member_mobile=mobi.split("-")[1];
        final String is= mobi.split("-")[0];
        if (token!= null){
            AccountHelper.deleteFamily(mobile[0],mobile[1],token,member_mobile,this,is,aptID,pos);
        }

//        Log.d("Deleting mobile and ISN",member_mobile+" "+is);
    }

    public void editUser(String mobi) {
        for(int i=0;i<accounts.size();i++){
            if(accounts.get(i).getNumber().equals(mobi)){
                toggleEditor(accounts.get(i));

                adapter.notifyDataSetChanged();
            }
        }
    }

    public void toggleEditor(Account id){
        if(id==null){
            for(Account a: accounts){
                a.setEditable(false);
            }
        }else{
            for(Account a:accounts){
                if(a.getNumber().equals(id.getNumber())){
                    a.setEditable(true);
                }else{
                    a.setEditable(false);
                }
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (SCROLL_STATE_TOUCH_SCROLL == scrollState) {

            Log.d("Clearing Focus","OnScrolled");
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus != null) {
                currentFocus.clearFocus();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void fetchData(String user_data) {
        try {
            JSONObject User_details = new JSONObject(user_data);
            String mobile = User_details.getString("resident_phone");
            inst_lc.setText(mobile);
            String[] user_mobile = LoginHandler.getUserMobile(context);
            Log.d("Resident :User ",mobile+":"+ user_mobile[1]+"-"+user_mobile[0]);
            if ((user_mobile[1]+"-"+user_mobile[0]).equals(mobile)){
                addmemeber.setVisibility(View.VISIBLE);
                SharedPreferences sharedPreferences = activity.getSharedPreferences("login_details",MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("isResident",true).apply();
                adapter.notifyDataSetChanged();
            }else{
                SharedPreferences sharedPreferences = activity.getSharedPreferences("login_details",MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("isResident",false).apply();
                adapter.notifyDataSetChanged();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
