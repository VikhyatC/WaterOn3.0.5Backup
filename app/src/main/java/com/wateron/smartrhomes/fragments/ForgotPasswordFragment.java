package com.wateron.smartrhomes.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wateron.smartrhomes.R;
import com.wateron.smartrhomes.activities.PreLoginActivity;

/**
 * Created by Paranjay on 04-12-2017.
 */

public class ForgotPasswordFragment extends Fragment {

    EditText mobileforVerify,cc;
    String mobileNo,ccode;
    Button signup,signin;
    LinearLayout whyLogout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forgot_pass,container,false);
        mobileforVerify=(EditText)view.findViewById(R.id.mobileforVerify);
        cc=(EditText)view.findViewById(R.id.ccode);
        signin=(Button)view.findViewById(R.id.signin);
        signup=(Button)view.findViewById(R.id.signUp);
        mobileforVerify.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mobileforVerify.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

//                    ((SignUpActivity)getActivity()).logevent("Signup_Mobile","Enter Mobile","Touch Event");
                    signup.performClick();
                    return true;
                }
                return  false;
            }
        });
        whyLogout=(LinearLayout) view.findViewById(R.id.whyIamOut);

        whyLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(getActivity(),"We have recently upgraded the crypto standard, to give you a more secure environment while using our app. Please reset your password to continue using the Wateron app.\n" +
                        "\n" +
                        "#Tip: you can reuse your earlier password, though we suggest you use a new one");
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileNo=mobileforVerify.getText().toString();
                ccode=cc.getText().toString();
                if(mobileNo!=null && ccode!=null){
                    if(!mobileNo.isEmpty() || !ccode.isEmpty()){
                        ((PreLoginActivity)getActivity()).setMobileNo(mobileNo, ccode);
                    }
                }
                ((PreLoginActivity)getActivity()).openSignInPage();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileNo=mobileforVerify.getText().toString();
                ccode=cc.getText().toString();
                if(cc!=null){
                    if(cc.getText().toString().trim().isEmpty()){
                        cc.setError("Please enter valid country code");
                    }else {
                        if(mobileNo!=null){
                            if(!mobileNo.isEmpty()){
//                                ((SignUpActivity)getActivity()).logevent("Signup_signup","Signup click","Touch Event");
                                ((PreLoginActivity)getActivity()).setMobileNo(mobileNo,ccode);
                                ((PreLoginActivity)getActivity()).requestOTP(true);
                            }else {
                                mobileforVerify.setError("Please enter valid mobile number");
                            }
                        }else{
                            mobileforVerify.setError("Please enter valid mobile number");
                        }
                    }
                }
            }
        });
        return view;
    }
    public void showDialog(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
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
            }
        });

        dialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        mobileNo = ((PreLoginActivity)getActivity()).getMobileNo();
        ccode = ((PreLoginActivity)getActivity()).getCcode();
        mobileforVerify.setText(mobileNo);
        cc.setText(ccode);
    }

//    public void setMobile(String mobileNo, String ccode) {
//        if(mobileNo!=null && ccode != null){
//            if(!mobileNo.isEmpty()){
//                mobileforVerify.setText(mobileNo);
//            }
//            if(!ccode.isEmpty()){
//                cc.setText(ccode);
//            }
//        }
//    }
}
