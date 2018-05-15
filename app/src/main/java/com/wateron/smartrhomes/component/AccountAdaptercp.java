package com.wateron.smartrhomes.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wateron.smartrhomes.R;
import com.wateron.smartrhomes.fragments.AccountFragment;
import com.wateron.smartrhomes.models.Account;

import java.util.List;

/**
 * Created by Paranjay on 15-12-2017.
 */

public class AccountAdaptercp extends ArrayAdapter<Account> {

    AccountFragment accountFragment;
    public AccountAdaptercp(@NonNull Context context, int resource, @NonNull List<Account> objects, AccountFragment accountFragment) {
        super(context, resource, objects);
        this.accountFragment = accountFragment;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater customInflater=LayoutInflater.from(getContext());
        if(convertView==null)
        convertView=customInflater.inflate(R.layout.acc_fm_num,parent,false);

        TextView mobilenu= convertView.findViewById(R.id.mobilenu);
        LinearLayout editor= convertView.findViewById(R.id.editor);
        final EditText changemobileisd = convertView.findViewById(R.id.changemobileisd);
        final EditText changemobile = convertView.findViewById(R.id.changemobile);
        ImageButton delete = convertView.findViewById(R.id.delete);
        ImageButton edit = convertView.findViewById(R.id.edit);
        ImageButton save = convertView.findViewById(R.id.save);
        Account account = getItem(position);
        String data = account.getNumber();
        if(account.isEditable()){
            edit.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
            editor.setVisibility(View.VISIBLE);
            mobilenu.setVisibility(View.GONE);
        }else{
            edit.setVisibility(View.VISIBLE);
            save.setVisibility(View.GONE);
            editor.setVisibility(View.GONE);
            mobilenu.setVisibility(View.VISIBLE);
        }
        if(data==null){
            return convertView;
        }
        mobilenu.setText(data);
        final String finalData = data;
        data=data.replace("(","");
        Log.d("data",data);
        data=data.replace(")","-");
        Log.d("data",data);
        final String mobi=data.split("-")[1];
        final String is= data.split("-")[0];
        changemobile.setText(mobi);
        changemobileisd.setText(is);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                accountFragment.deleteUser(finalData, aptID);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountFragment.editUser(finalData);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String m= changemobile.getText().toString();
                String i=changemobileisd.getText().toString();
                if(!m.isEmpty() || !i.isEmpty()){
                    return;
                }
//                accountFragment.saveUser(m,i,finalData, position);
            }
        });

        return convertView;
    }
}
