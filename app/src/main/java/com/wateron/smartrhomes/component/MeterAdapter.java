package com.wateron.smartrhomes.component;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wateron.smartrhomes.R;
import com.wateron.smartrhomes.fragments.SettingFragment;
import com.wateron.smartrhomes.models.MeterSet;


/**
 * Created by paran on 10/10/2016.
 */

public class MeterAdapter extends ArrayAdapter<MeterSet> {
    SettingFragment settingsFragment;
    public MeterAdapter(Context context, MeterSet[] resource, SettingFragment settingsFragment) {
        super(context, R.layout.metersetting_list_item,resource);
        this.settingsFragment=settingsFragment;
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Typeface type = Typeface.createFromAsset(getContext().getAssets(),"fonts/roboto_light.ttf");
        LayoutInflater customInflater=LayoutInflater.from(getContext());
        View meterView=customInflater.inflate(R.layout.metersetting_list_item,parent,false);
        final MeterSet meter=getItem(position);
        final EditText changesettings=(EditText)meterView.findViewById(R.id.changesettings);
        TextView meterLocationTag=(TextView)meterView.findViewById(R.id.inst_lc_label);
        final TextView meterLocation=(TextView)meterView.findViewById(R.id.inst_lc);
        TextView meterId=(TextView)meterView.findViewById(R.id.meter_serial);
        TextView meterIdTag=(TextView)meterView.findViewById(R.id.meter_ser_label);
        ImageButton imageButton=(ImageButton)meterView.findViewById(R.id.edit_meter_button);
        meterLocationTag.setTypeface(type);
        meterLocation.setTypeface(type);
        meterId.setTypeface(type);
        meterIdTag.setTypeface(type);
        meterId.setText(meter.getId());
        meterLocation.setText(meter.getLocation());
        changesettings.setText(meter.getLocation());
        changesettings.setImeOptions(EditorInfo.IME_ACTION_DONE);
        if(meter.isEditOn()){
            changesettings.setVisibility(View.VISIBLE);
            meterLocation.setVisibility(View.GONE);
            imageButton.setImageResource(R.drawable.save);
            changesettings.requestFocus();

        }else{
            changesettings.setVisibility(View.GONE);
            meterLocation.setVisibility(View.VISIBLE);
            imageButton.setImageResource(R.drawable.menu_edit);
        }
//        changesettings.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if(keyCode==KeyEvent.KEYCODE_BACK){
//                    settingsFragment.listupdate(position,true);
//                }
//                return false;
//            }
//        });


        changesettings.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do here your stuff f
                    if(!changesettings.getText().toString().isEmpty()){
                        if(changesettings.getText().toString().length()<=10){
                            ((InputMethodManager)settingsFragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(changesettings.getWindowToken(), 0);
                            Log.d("METER_ID on POSITION",meter.getId()+"on"+position);
                            settingsFragment.editValues(meter.getId(),changesettings.getText().toString(),position);
                        }else{
                            changesettings.setError("Maximum length is 10 characters");
                        }

                    }else{
                        Toast.makeText(settingsFragment.getActivity(),"Can't leave the name blank",Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
                return  false;
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                settingsFragment.editValues(position);
                if(meter.isEditOn()){
                    if(!changesettings.getText().toString().isEmpty()){
                        if(changesettings.getText().toString().length()<=10){
                            ((InputMethodManager)settingsFragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(changesettings.getWindowToken(), 0);
                            Log.d("METER_ID",meter.getId());
                            settingsFragment.editValues(meter.getId(),changesettings.getText().toString(),position);
                        }else{
                            changesettings.setError("Maximum length is 10 characters");
                        }

                    }else{
                        Toast.makeText(settingsFragment.getActivity(),"Can't leave the name blank",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Log.d("Updating","List");
                    settingsFragment.listupdate(position,true);
                }


            }
        });
        return meterView;
    }

//    public class Holder{
//        TextView meterLocationTag,meterLocation,meterId,meterIdTag;
//        ImageButton imageButton
//    }
}
