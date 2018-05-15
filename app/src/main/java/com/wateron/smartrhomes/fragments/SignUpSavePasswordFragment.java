package com.wateron.smartrhomes.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wateron.smartrhomes.R;
import com.wateron.smartrhomes.activities.PreLoginActivity;

/**
 * Created by Paranjay on 04-12-2017.
 */

public class SignUpSavePasswordFragment extends Fragment {
    EditText new_passsword,cnf_passowrd;
    String np,cp;
    Button save_password_button;
    TextView showpss,notice;
    boolean showstate=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_password_save_fragment,container,false);
        showpss=(TextView)view.findViewById(R.id.showpss);
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        showpss.setClickable(true);
        showpss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showstate= !showstate;
                if(showstate){
                    Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_light.ttf");
                    showpss.setBackgroundResource(R.drawable.eyeopen);
                    new_passsword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD|InputType.TYPE_CLASS_TEXT);
                    cnf_passowrd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD|InputType.TYPE_CLASS_TEXT);
                    new_passsword.setTypeface(type);
                    cnf_passowrd.setTypeface(type);
                }else{
                    Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_light.ttf");
                    showpss.setBackgroundResource(R.drawable.eyehint);
                    new_passsword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
                    cnf_passowrd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
                    new_passsword.setTypeface(type);
                    cnf_passowrd.setTypeface(type);
                }
            }
        });

        new_passsword=(EditText)view.findViewById(R.id.new_passsword);
        new_passsword.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        new_passsword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    //do here your stuff f

                    cnf_passowrd.requestFocus();
                    return true;
                }
                return  false;
            }
        });
        cnf_passowrd=(EditText)view.findViewById(R.id.cnf_passowrd);
        cnf_passowrd.setFocusable(true);
        cnf_passowrd.setImeOptions(EditorInfo.IME_ACTION_DONE);
        cnf_passowrd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do here your stuff f
                    save_password_button.performClick();
                    return true;
                }
                return  false;
            }
        });
        notice=(TextView) view.findViewById(R.id.notice);
        new_passsword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new_passsword.getText().toString().isEmpty()){
                    if(notice.getVisibility()==View.GONE){
                        notice.setVisibility(View.VISIBLE);
                        Handler hh=new Handler();
                        hh.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                notice.setVisibility(View.GONE);
                            }
                        },2000);
                    }
                }
            }
        });
        cnf_passowrd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!new_passsword.getText().toString().isEmpty()){
                    if(new_passsword.getText().toString().equals(cnf_passowrd.getText().toString())){
                        cnf_passowrd.setError(null);
                    }else{
                        cnf_passowrd.setError("Both passwords are not matching");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        save_password_button=(Button)view.findViewById(R.id.save_password_button);

        save_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                np=new_passsword.getText().toString();
                cp=cnf_passowrd.getText().toString();
                if(!np.isEmpty()){
                    if(np.equals(cp)){
                        if(np.length()>=8){
                            if(np.matches("[a-zA-Z0-9.? ]*")){
                                new_passsword.setError("At least one special character is must");
                            }else{

//                                ((PreLoginActivity)getActivity()).logevent("Signup_password_save","Password save button","Touch Event");
                                if (np.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")){
                                    PreLoginActivity signUpActivity=((PreLoginActivity)getActivity());
                                    signUpActivity.setPass(np);
                                    signUpActivity.savePassword();
                                }else{
                                    new_passsword.setError("Minimum length for password is 8 character with minimum 1 special character and 1 number atleast");
                                }

                            }
                        }else {
                            new_passsword.setError("Minimum length for password is 8 character with minimum 1 special character and 1 number atleast");
                        }

                    }else{
                        cnf_passowrd.setError("Password is not matching, try again");
                    }
                }else {
                    new_passsword.setError("Minimum length for password is 8 character with minimum 1 special character and 1 number atleast");
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        notice.setVisibility(View.VISIBLE);
        Handler handler=new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                notice.setVisibility(View.GONE);
////                Toast.makeText(getActivity(),"Minimum length for password is 8 character with minimum 1 special character",Toast.LENGTH_LONG).show();
//            }
//        },2500);
    }
}
