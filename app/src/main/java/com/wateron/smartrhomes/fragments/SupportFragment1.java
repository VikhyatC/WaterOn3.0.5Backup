package com.wateron.smartrhomes.fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wateron.smartrhomes.R;

import org.w3c.dom.Text;

/**
 * Created by Vikhyat Chandra on 2/16/2018.
 */

public class SupportFragment1 extends android.support.v4.app.Fragment {
    private Button button;
    private TextView text_email;
    private TextView text_email1;
    private TextView text_call;
    private TextView text_call1;
    private TextView text_call2;
    private TextView text_call3;
    private TextView text_call4;
    private TextView OR1;
    private TextView OR2;
    private Typeface type;
    private Typeface type1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.support_fragment_1,container,false);
        type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_regular.ttf");
        type1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_light.ttf");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        text_call = (TextView)view.findViewById(R.id.call_support1);
        text_email = (TextView) view.findViewById(R.id.email_text1);
        text_email1 = (TextView)view.findViewById(R.id.email_text2);
        text_call2 = (TextView)view.findViewById(R.id.call_support2);
        text_call3 = (TextView)view.findViewById(R.id.call_support3);
        text_call4 = (TextView)view.findViewById(R.id.call_support4);
        OR1 = (TextView)view.findViewById(R.id.OR1);
        OR2 = (TextView)view.findViewById(R.id.OR2);
        button = (Button)view.findViewById(R.id.create_ticket);
        button.setTypeface(type);
        OR1.setTypeface(type);
        OR2.setTypeface(type);
        text_call4.setTypeface(type1);
        text_call3.setTypeface(type1);
        text_call2.setTypeface(type1);
        text_call.setTypeface(type1);
        text_email.setTypeface(type1);
        text_email1.setTypeface(type1);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.Fragment fragment = new SupportFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentview, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
