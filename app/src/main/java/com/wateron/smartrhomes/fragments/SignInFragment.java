package com.wateron.smartrhomes.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wateron.smartrhomes.R;
import com.wateron.smartrhomes.activities.PreLoginActivity;

import java.util.Objects;

/**
 * Created by Paranjay on 04-12-2017.
 */

public class SignInFragment extends Fragment {
    Button signin,signup;
    int errorcode=0;
    TextView showpss,forgotPasswordButton;
    SharedPreferences sp;
    int errorcode1=0;
    String mobileno="";
    String code="";
    LinearLayout whyLogout;
    EditText mobile,name,ccode;
    boolean showstate=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in_fragment,container,false);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_light.ttf");
        showpss=(TextView)view.findViewById(R.id.showpss);
        whyLogout=(LinearLayout) view.findViewById(R.id.whyIamOut);

        whyLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("Please reset your password using \"Forgot Password\" to continue using the Wateron app.\n" +
                        "\n" +
                        "#Tip: you can reuse your earlier password, though we suggest you use a new one");
            }
        });
        forgotPasswordButton=(TextView)view.findViewById(R.id.forgot_password_button);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileno=mobile.getText().toString().trim();
                code=ccode.getText().toString().trim();
                if(mobileno!=null && code!=null){
                    if(!mobileno.isEmpty() || !code.isEmpty()){
                        ((PreLoginActivity)getActivity()).setMobileNo(mobileno, code);
                    }
                }
                ((PreLoginActivity)getActivity()).openForgotPassword();
            }
        });

        showpss.setClickable(true);
        showpss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showstate= !showstate;
                if(showstate){
//                    logevent("Signin_eye","Signin EYE","Touch Event");
                    Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_light.ttf");
                    showpss.setBackgroundResource(R.drawable.eyeopen);
                    name.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD|InputType.TYPE_CLASS_TEXT);
                    name.setTypeface(type);
                }else{
                    Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/roboto_light.ttf");
                    showpss.setBackgroundResource(R.drawable.eyehint);
                    name.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
                    name.setTypeface(type);
                }
            }
        });
        mobile=(EditText)view.findViewById(R.id.mobile);
        ccode=(EditText)view.findViewById(R.id.ccode);
        ccode.setTypeface(type);
        mobile.setTypeface(type);
        name=(EditText)view.findViewById(R.id.name);
        name.setTypeface(type);

        signin=(Button)view.findViewById(R.id.signin);
        signin.setTypeface(type);
        signup=(Button)view.findViewById(R.id.signUp);
        signup.setTypeface(type);
        name.setImeOptions(EditorInfo.IME_ACTION_DONE);
        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do here your stuff f
                    signin.performClick();
                    return true;
                }
                return  false;
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorcode=0;
                errorcode1=0;
                if(validate()){
                    mobileno=mobile.getText().toString().trim();
                    code=ccode.getText().toString().trim();
                    ((PreLoginActivity)getActivity()).setMobileNo(mobileno,code);
                    (((PreLoginActivity)getActivity())).setPass(name.getText().toString());
                    ((PreLoginActivity)getActivity()).loginUser();
                }else{
                    if(errorcode==1){
                        mobile.setError("Please enter valid mobile number with country code");
                    }
                    if(errorcode1==2){
                        name.setError("Please enter valid password");
                    }
                    if ((errorcode ==3)&&(errorcode1==3)){
                        mobile.setError("Please enter a valid 10 digit mobile number");
                    }
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileno=mobile.getText().toString().trim();
                code=ccode.getText().toString().trim();
                if(mobileno!=null && code!=null){
                    if(!mobileno.isEmpty() || !code.isEmpty()){
                        ((PreLoginActivity)getActivity()).setMobileNo(mobileno, code);
                    }
                }
                ((PreLoginActivity)getActivity()).openSignUpPage();

            }
        });
        return view;
    }
    public void showDialog(String msg){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mobileno=mobile.getText().toString().trim();
                code=ccode.getText().toString().trim();
                if(mobileno!=null && code!=null){
                    if(!mobileno.isEmpty() || !code.isEmpty()){
                        ((PreLoginActivity)getActivity()).setMobileNo(mobileno, code);
                    }
                }
                ((PreLoginActivity)getActivity()).openForgotPassword();
            }
        });

        dialog.show();

    }

    private boolean validate() {
        Log.d("Validate Details", String.valueOf(code));
        if(mobile.getText().toString().trim().isEmpty() || ccode.getText().toString().trim().isEmpty()){
            errorcode=1;

        }
        if(code.equals("91")){
            Log.d("Detected India :", String.valueOf(mobile.getText().toString().length()));
            if (mobile.getText().toString().length()!=10){

//                Toast.makeText(getContext(),"Please enter a valid 10 digit mobile number",Toast.LENGTH_LONG).show();
                errorcode =3;
                errorcode1 =3;
            }
        }

        if(name.getText().toString().trim().isEmpty() ){
            errorcode1=2;

        }
        if(errorcode==0 && errorcode1==0){
            return true;
        }else{
            return false;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mobileno = ((PreLoginActivity)getActivity()).getMobileNo();
        code = ((PreLoginActivity)getActivity()).getCcode();
        mobile.setText(mobileno);
        ccode.setText(code);
    }
}
