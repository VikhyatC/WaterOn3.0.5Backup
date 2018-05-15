package com.wateron.smartrhomes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class ForgotPasswordOtpValidateFragment extends Fragment {
    String otp;
    EditText otpnumber;
    Button verify;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forgot_password_otp_validate_fragment,container,false);
        otpnumber=(EditText)view.findViewById(R.id.otpnumber);
        verify=(Button)view.findViewById(R.id.verifyOTPButton);
        otpnumber.setImeOptions(EditorInfo.IME_ACTION_DONE);
        otpnumber.requestFocus();
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        otpnumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do here your stuff f
                    verify.performClick();
                    return true;
                }
                return  false;
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp=otpnumber.getText().toString().trim();
                if(otp!=null){
                    if(!otp.isEmpty()){

//                        ((PreLoginActivity)getActivity()).logevent("Signup_OTP","OTP entry","Touch Event");
                        PreLoginActivity preLoginActivity= (PreLoginActivity) getActivity();
                        preLoginActivity.setOtp(otp);
                        preLoginActivity.verifyOTP();
                    }else{
                        otpnumber.setError("Please enter valid OTP");
                    }
                }else{
                    otpnumber.setError("Please enter valid OTP");
                }
            }
        });
        return view;
    }
}
