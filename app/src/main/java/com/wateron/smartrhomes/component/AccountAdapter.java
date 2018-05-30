package com.wateron.smartrhomes.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wateron.smartrhomes.R;
import com.wateron.smartrhomes.fragments.AccountFragment;
import com.wateron.smartrhomes.models.Account;
import com.wateron.smartrhomes.util.AccountHandlerInterface;
import com.wateron.smartrhomes.util.AccountHelper;
import com.wateron.smartrhomes.util.CrashHelper;
import com.wateron.smartrhomes.util.LoginHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Paranjay on 15-12-2017.
 */

public class AccountAdapter extends ArrayAdapter<Account> implements AccountHandlerInterface {

    AccountFragment accountFragment;
    ViewHolder holder;
    List<Account> accounts = new ArrayList<>();
    private boolean isResident;

    public AccountAdapter(@NonNull Context context, int resource, @NonNull List<Account> accounts, List<String> mobiles, AccountFragment accountFragment) {
        super(context, resource, accounts);
        this.accountFragment = accountFragment;
        this.accounts = accounts;
    }

    @Override
    public void errorLoadingMembers(String response, int httpResult,String url,String xmsin,String token) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
        String dateTime = sdf.format(new Date());
        String[] mobile = LoginHandler.getUserMobile(getContext());
        CrashHelper.SendCrashMailer("("+mobile[1]+")"+mobile[0],AppConstants.APPVERSION, String.valueOf(httpResult),response+"REQUEST_URL:"+url+"XMSIN:"+xmsin+"TOKEN:"+token,dateTime+"-"+"AccountSettingsScreen","android");
    }

    @Override
    public void loadData(List<String> numbers) {
    }



    @Override
    public void loadData() {

    }

    @Override
    public void loadData(Account member_number) {

    }

    @Override
    public void loadAddedData(String number) {
        Log.d("Adding Number",number);

        Account account = new Account(number,false);
        add(account);
        notifyDataSetChanged();
        Toast.makeText(getContext(),"Member Details Saved Successfully",Toast.LENGTH_LONG).show();
    }

    @Override
    public void errorLoadingDeletedMembers(String response, int httpResult, String s, String s1, String s2, int member_ccode, String member_mobile, long apt_id, String string) {

    }

    public void ShowInterface() {
        holder.save.setVisibility(View.VISIBLE);
    }

    public void showInterface() {
        notifyDataSetChanged();
    }


    static class ViewHolder{
        public EditText mobileView,isdView;
        public TextView mobilenumDisp;
        public ImageButton edit,save,delete;
        public LinearLayout editor;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater customInflater=LayoutInflater.from(getContext());
        View rowView = customInflater.inflate(R.layout.acc_fm_num,parent,false);;

//        if(rowView == null){
            Log.d("GetFamilyList","Now");
            holder = new ViewHolder();
            holder.mobileView = rowView.findViewById(R.id.changemobile);
            holder.mobileView.requestFocus();
            holder.isdView = rowView.findViewById(R.id.changemobileisd);
            holder.mobilenumDisp = rowView.findViewById(R.id.mobilenu);
            holder.editor = rowView.findViewById(R.id.editor);
            holder.save = rowView.findViewById(R.id.save);
            holder.edit = rowView.findViewById(R.id.edit);
            holder.delete = rowView.findViewById(R.id.delete);
            holder.editor = rowView.findViewById(R.id.editor);
            rowView.setTag(holder);

//        }

//        final ViewHolder holder = (ViewHolder) rowView.getTag();
        Account account = getItem(position);
        String data = account.getNumber();

        if(data==null){
            return rowView;
        }
        holder.mobileView.setInputType(InputType.TYPE_NULL);
        holder.isdView.setInputType(InputType.TYPE_NULL);
        holder.delete.setVisibility(View.GONE);
        holder.edit.setVisibility(View.GONE);
        SharedPreferences preferences = getContext().getSharedPreferences("defaults_pref", MODE_PRIVATE);
        final int aptID = preferences.getInt("apartmentIdSelected",0);
        holder.mobilenumDisp.setText(data);
        final String finalData = data;
        data=data.replace("(","");
        Log.d("data",data);
        data=data.replace(")","-");
        Log.d("data",data);
        final String mobi=data.split("-")[1];
        final String is= data.split("-")[0];
        holder.mobileView.setText(mobi);
        holder.isdView.setText(is);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_details",MODE_PRIVATE);
        isResident = sharedPreferences.getBoolean("isResident",false);
        boolean addentry =  sharedPreferences.getBoolean("addentry",false);

        if (isResident){
            if ((addentry)||(account.isEditable())){
                Log.d("Save","VisibleNow");
                holder.save.setVisibility(View.VISIBLE);
                holder.save.setEnabled(true);
                holder.isdView.setInputType(InputType.TYPE_CLASS_PHONE);
                holder.mobileView.setInputType(InputType.TYPE_CLASS_PHONE);
                holder.isdView.setFocusable(true);
                holder.mobileView.setFocusable(true);
                SharedPreferences preferences1 = getContext().getSharedPreferences("login_details",MODE_PRIVATE);
                preferences1.edit().putBoolean("addentry",false).apply();
            }
            //            holder.save.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String member_mobile= holder.mobileView.getText().toString().trim();
                    String member_isd= holder.isdView.getText().toString().trim();
                    if(member_mobile.length()>0 && member_isd.length()>0) {
                        Log.d("Deleting Data : ", finalData);
                        accountFragment.deleteUser(finalData, aptID, position);
                        notifyDataSetChanged();
                    }
                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    accountFragment.editUser(finalData);

                }
            });
            holder.save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] mobile = LoginHandler.getUserMobile(getContext());
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_details", MODE_PRIVATE);
                    sharedPreferences.edit().putBoolean("addentry",true).apply();
                    AccountFragment.isClicked = false;
                    String authToken = sharedPreferences.getString("authToken",null);
//                Log.d("Autho",authToken);
                    String member_mobile= holder.mobileView.getText().toString().trim();
                    String member_isd= holder.isdView.getText().toString().trim();
                    Log.d("FieldsEmpty", String.valueOf(member_mobile.length()));
                    if(member_mobile.length()>0 && member_isd.length()>0){
                        Log.d("Member Details:",member_mobile+":"+member_isd);
                        AccountHelper.addFamilyMember(mobile[0],mobile[1], authToken,AccountAdapter.this,member_mobile,member_isd);
                        accountFragment.removeExisitingUser(position);
                        return;
                    }
                    AccountFragment.isClicked = false;
                    Log.d("Saving","Account Details");
                }
            });
            holder.mobileView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }


        return rowView;
    }
}
