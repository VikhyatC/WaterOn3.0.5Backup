package com.wateron.smartrhomes.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

import com.bumptech.glide.Glide;
import com.wateron.smartrhomes.R;
import com.wateron.smartrhomes.component.AppConstants;
import com.wateron.smartrhomes.component.TouchListner1;
import com.wateron.smartrhomes.fragments.ForgotPasswordFragment;
import com.wateron.smartrhomes.fragments.ForgotPasswordOtpValidateFragment;
import com.wateron.smartrhomes.fragments.ForgotPasswordSavePasswordFragment;
import com.wateron.smartrhomes.fragments.SignInFragment;
import com.wateron.smartrhomes.fragments.SignUpFragment;
import com.wateron.smartrhomes.fragments.SignUpOtpValidateFragment;
import com.wateron.smartrhomes.fragments.SignUpSavePasswordFragment;
import com.wateron.smartrhomes.util.CrashHelper;
import com.wateron.smartrhomes.util.LoginHandler;
import com.wateron.smartrhomes.util.SignUpHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PreLoginActivity extends AppCompatActivity {
    private static String OTPTAG = "OTPScreen";
    private static String SIGNINTAG = "SignInScreen";
    private static String SIGNUPTAG = "SignupScreen";
    private static String RESETPASSWORDTAG = "ResetPasswordScreen";

    String mobileNo="";
    String ccode="91";
    ProgressDialog progressdialog;
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo,String ccode) {
        this.mobileNo = mobileNo;
        this.ccode=ccode;
    }
    String otp;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    String pass;

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    View main;
    View main2;
    TextView title;
    TextView lable;
    ImageView dot1,dot2,dot3,dot4;
    Button signin,signup;
    String lables[]={
            "Monitor water consumption of all your inlets\ndaily, weekly or monthly",
            "Prevent water wastage and save your home\nfrom damages and money",
            "Compare yourself with others in your\nneighbourhood and set goals"
    };
    String titles[]={
            "Monitor your water\nconsumption",
            "Monitor leaks in your\nhome",
            "Motivate yourself to\nsave"

    };
    int imgs[]={
            R.drawable.intro1,
            R.drawable.intro3,
            R.drawable.intro2,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);
        View view=findViewById(R.id.pre_login);
        main=findViewById(R.id.WelcomeScreen);
        main2=findViewById(R.id.frame_holder_pre_login);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        view.setOnTouchListener(new TouchListner1(PreLoginActivity.this));
        signin=(Button)findViewById(R.id.signin);
        signup=(Button)findViewById(R.id.signUp);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDisplayedImage = 3;
                loadViewInFragment("signInHome");
                switchView(1);
//                logevent("Launch_SignIn","SignIn","Touch Event");
//                Intent intent=new Intent(PreLoginActivity.this,SignInOrRegisterActivity.class);
//
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
//                WelcomeActivity.this.finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentDisplayedImage = 3;
                loadViewInFragment("signUpHome");
                switchView(1);
//                logevent("Launch_Signup","SignUP","Touch Event");
//                Intent intent=new Intent(WelcomeActivity.this,SignUpActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
//                WelcomeActivity.this.finish();
            }
        });
        loadSwitcher();
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            int t=bundle.getInt("page",0);
            currentDisplayedImage=t;
        }
        setDots(currentDisplayedImage);
        lable.setText(lables[currentDisplayedImage]);
        title.setText(titles[currentDisplayedImage]);
    }

    private void loadSwitcher() {
        dot1=(ImageView)findViewById(R.id.dot1);
        dot2=(ImageView)findViewById(R.id.dot2);
        dot3=(ImageView)findViewById(R.id.dot3);
        dot4=(ImageView)findViewById(R.id.dot4);

        title=(TextView)findViewById(R.id.welcometitle);
        title.setText(titles[currentDisplayedImage]);
        lable=(TextView)findViewById(R.id.welcomelable);
        lable.setText(lables[currentDisplayedImage]);



    }



    boolean switchit=true;
    int currentDisplayedImage=0;


    public void setDots(int id){

        Glide.with(this).load(imgs[id]).into((ImageView) findViewById(R.id.view1));

        if(id==1){
            ((LinearLayout)findViewById(R.id.m1)).setBackgroundResource(R.drawable.black_gradient);
        }else {
            ((LinearLayout)findViewById(R.id.m1)).setBackgroundResource(R.drawable.blue_gradient);
        }
        switch (id){
            case 0:
                dot1.setImageResource(R.drawable.fulldot);
                dot2.setImageResource(R.drawable.empty_dot);
                dot3.setImageResource(R.drawable.empty_dot);
                dot4.setImageResource(R.drawable.empty_dot);

                break;
            case 1:
                dot2.setImageResource(R.drawable.fulldot);
                dot1.setImageResource(R.drawable.empty_dot);
                dot3.setImageResource(R.drawable.empty_dot);
                dot4.setImageResource(R.drawable.empty_dot);
                break;
            case 2:
                dot3.setImageResource(R.drawable.fulldot);
                dot4.setImageResource(R.drawable.empty_dot);
                dot1.setImageResource(R.drawable.empty_dot);
                dot2.setImageResource(R.drawable.empty_dot);
                break;
            case 3:
                dot4.setImageResource(R.drawable.fulldot);
                dot1.setImageResource(R.drawable.empty_dot);
                dot2.setImageResource(R.drawable.empty_dot);
                dot3.setImageResource(R.drawable.empty_dot);
                break;
        }
    }

    public void goNext() {
        if(currentDisplayedImage<2){
            currentDisplayedImage++;
            setDots(currentDisplayedImage);
            lable.setText(lables[currentDisplayedImage]);
            title.setText(titles[currentDisplayedImage]);
        }else if(currentDisplayedImage==2){
//            Intent intent=new Intent(PreLoginActivity.this,SignInOrRegisterActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
//            PreLoginActivity.this.finish();
            currentDisplayedImage++;
            loadViewInFragment("signUpHome");
            switchView(1);
        }
    }

    Fragment fragment;
    private void loadViewInFragment(String tag){
        switch (tag){
            case "signInHome":
                fragment = new SignInFragment();

//                ((SignInFragment)fragment).setMobile(mobileNo,ccode);
                break;
            case "signUpHome":
                fragment = new SignUpFragment();
//                ((SignUpFragment)fragment).setMobile(mobileNo,ccode);
                break;
            case "signUpOtpValidate":
                fragment = new SignUpOtpValidateFragment();
                break;
            case "signUpPasswordSave":
                fragment = new SignUpSavePasswordFragment();
                break;
            case "forgotPasswordHome":
                fragment = new ForgotPasswordFragment();
                break;
            case "forgotPasswordOTPValidate":
                fragment = new ForgotPasswordOtpValidateFragment();
                break;
            case "forgotPasswordSave":
                fragment = new ForgotPasswordSavePasswordFragment();
                break;
//            default:
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_top,R.anim.slide_out_bottom);
        ft.replace(R.id.frame_holder_pre_login,fragment);
        ft.commit();
    }

    private void switchView(int tag) {
        switch(tag){
            case 0: main.setVisibility(View.VISIBLE);
                main2.setVisibility(View.GONE);
                break;
            case 1:
                main.setVisibility(View.GONE);
                main2.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void goBack() {
        if(!(currentDisplayedImage > 3)){
            if(currentDisplayedImage==3){
                switchView(0);
            }
            if(currentDisplayedImage>0){
                currentDisplayedImage--;
                setDots(currentDisplayedImage);
                lable.setText(lables[currentDisplayedImage]);
                title.setText(titles[currentDisplayedImage]);
            }
        }
    }

    boolean isForgotPass = false;
    public void requestOTP(boolean fp) {
        isForgotPass = fp;
        progressdialog=new ProgressDialog(PreLoginActivity.this);
        progressdialog.setCancelable(false);
        progressdialog.setMessage("Requesting OTP");
        progressdialog.show();
        Log.d("requst opt mobile",ccode+mobileNo);
        SignUpHelper.sendOTP(PreLoginActivity.this,"("+ccode+")"+mobileNo);
    }

    public void onOTPSendFailed(final int code, String response, String s1, final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressdialog!=null){
                    if(progressdialog.isShowing()){
                        progressdialog.dismiss();
                    }
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
                String dateTime = sdf.format(new Date());
                String[] mobile = LoginHandler.getUserMobile(getApplicationContext());
                String model_name = Build.MODEL;
                String manufacturer_name = Build.MANUFACTURER;
                String product_name = Build.PRODUCT;
                String versionDetails = System.getProperty("os.version");
                Log.d("VersionDetails",versionDetails);
                CrashHelper.SendCrashMailer("("+ccode+")"+mobileNo, AppConstants.APPVERSION, String.valueOf(code),s+"\n"+model_name+"\n"+manufacturer_name+"\n"+product_name,dateTime+"-"+OTPTAG,"android");
                Toast.makeText(PreLoginActivity.this,"Invalid Mobile Number",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onOTPSendSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressdialog!=null){
                    if(progressdialog.isShowing()){
                        progressdialog.dismiss();
                    }
                }
                currentDisplayedImage = 4;
                if(isForgotPass){
                    loadViewInFragment("forgotPasswordOTPValidate");
                }else {
                    loadViewInFragment("signUpOtpValidate");
                }
                switchView(1);
            }
        });
    }

    public void verifyOTP() {
        progressdialog=new ProgressDialog(PreLoginActivity.this);
        progressdialog.setCancelable(false);
        progressdialog.setMessage("Verifying OTP");
        progressdialog.show();
        Log.d("requst opt mobile",ccode+mobileNo);
        SignUpHelper.verifyOTP(PreLoginActivity.this,"("+ccode+")"+mobileNo,otp);
    }

    public void onOTPVerificationSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressdialog!=null){
                    if(progressdialog.isShowing()){
                        progressdialog.dismiss();
                    }
                }
//                logevent("Signup_OTP_verify","OTP Verified","OTP Verified");
                currentDisplayedImage = 4;
                if(isForgotPass){
                    loadViewInFragment("forgotPasswordSave");
                }else{
                    loadViewInFragment("signUpPasswordSave");
                }
                switchView(1);
            }
        });
    }

    public void onOTPVerificationFailed(final int code, final String s, final String url, final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressdialog!=null){
                    if(progressdialog.isShowing()){
                        progressdialog.dismiss();
                    }
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
                String dateTime = sdf.format(new Date());
                String[] mobile = LoginHandler.getUserMobile(getApplicationContext());
                Log.d("Mobile number Verified:",mobileNo);
                String model_name = Build.MODEL;
                String manufacturer_name = Build.MANUFACTURER;
                String product_name = Build.PRODUCT;
                CrashHelper.SendCrashMailer("("+ccode+")"+mobileNo,AppConstants.APPVERSION, String.valueOf(code),response+"\n"+model_name+"\n"+manufacturer_name+"\n"+product_name+"\n"+url+"\n"+s,dateTime+"-"+OTPTAG,"android");
                Toast.makeText(PreLoginActivity.this,"Error verifying OTP, please try again",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void savePassword() {
        progressdialog=new ProgressDialog(PreLoginActivity.this);
        progressdialog.setCancelable(false);
        progressdialog.setMessage("Storing password");
        progressdialog.show();
        SignUpHelper.storePassword(PreLoginActivity.this,"("+ccode+")"+mobileNo,pass);
    }

    public void onStorePasswordFailed(final int httpResult, final String s, final String url, final String token, final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressdialog!=null){
                    if(progressdialog.isShowing()){
                        progressdialog.dismiss();
                    }
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
                String dateTime = sdf.format(new Date());
                String model_name = Build.MODEL;
                String manufacturer_name = Build.MANUFACTURER;
                String product_name = Build.PRODUCT;
                String[] mobile = LoginHandler.getUserMobile(getApplicationContext());
                CrashHelper.SendCrashMailer("("+ccode+")"+mobileNo,AppConstants.APPVERSION, String.valueOf(httpResult),response+"\n"+model_name+"\n"+manufacturer_name+"\n"+product_name+"\n"+url+"\n"+s+"\n"+token,dateTime+"-"+RESETPASSWORDTAG,"android");
//                logevent("Signup_password_save_error","Password Error","Error non fatal");
                Toast.makeText(PreLoginActivity.this,"Error storing password, please try again",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onStorePasswordSuccess() {
        SignUpHelper.signInUser(PreLoginActivity.this,"("+ccode+")"+mobileNo,pass);
    }

    public void onSignInFailed(final int httpResult, final String response1, final String request_url, final String token, final String authoriztaion) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressdialog!=null){
                    if(progressdialog.isShowing()){
                        progressdialog.dismiss();
                    }
                }
                showDialog(PreLoginActivity.this,
                        "Sign In Failed"+"\n"+ "Please reset your password using \"Forgot password\" to continue using the Wateron app\n" +
                                "\n" +
                                "#Tip: you can reuse your earlier password, though we suggest you use a new one");
//                logevent("Signin_Error","Signin Error","Error non fatal");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
                String dateTime = sdf.format(new Date());
//                String[] mobile = LoginHandler.getUserMobile(getApplicationContext());
                String model_name = Build.MODEL;
                String manufacturer_name = Build.MANUFACTURER;
                String product_name = Build.PRODUCT;
                CrashHelper.SendCrashMailer("("+ccode+")"+mobileNo,AppConstants.APPVERSION, String.valueOf(httpResult),response1+"\n"+model_name+"\n"+manufacturer_name+"\n"+product_name+request_url+authoriztaion+token,dateTime+"-"+SIGNINTAG,"android");
            }
        });

    }

    public void onSignInSuccesswithDemonumber() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressdialog!=null){
                    if(progressdialog.isShowing()){
                        progressdialog.dismiss();
                    }
                }
                AlertDialog.Builder builder= new AlertDialog.Builder(PreLoginActivity.this);
                builder.setCancelable(false);
                builder.setMessage("You will be viewing a demo version of the app, Click ok to continue.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(PreLoginActivity.this,SplashActivity.class);
                        LoginHandler.loginUserToApp(mobileNo,ccode,true,getApplicationContext());
                        startActivity(intent);
                        PreLoginActivity.this.finish();
                    }
                });
                builder.create().show();
            }
        });

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
                openForgotPassword();
            }
        });

        dialog.show();

    }

    public void onSignInSuccess(final String authToken) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressdialog!=null){
                    if(progressdialog.isShowing()){
                        progressdialog.dismiss();
                    }
                }
                SharedPreferences sharedPreferences = getSharedPreferences("login_details",MODE_PRIVATE);
                sharedPreferences.edit().putString("authToken",authToken).apply();
                Intent intent=new Intent(PreLoginActivity.this,SplashActivity.class);
                LoginHandler.loginUserToApp(mobileNo,ccode,true,getApplicationContext());
//            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//            RegistrationTokenUpdater.updateToken(getApplicationContext(),refreshedToken);
                startActivity(intent);
                PreLoginActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(currentDisplayedImage <= 3){
            goBack();
        }else{
            if(fragment instanceof SignUpOtpValidateFragment){
                currentDisplayedImage = 3;
                loadViewInFragment("signUpHome");
                switchView(1);
            }else if(fragment instanceof SignUpSavePasswordFragment){
                currentDisplayedImage = 4;
                loadViewInFragment("signUpOtpValidate");
                switchView(1);
            }else if(fragment instanceof ForgotPasswordOtpValidateFragment){
                currentDisplayedImage = 3;
                loadViewInFragment("forgotPasswordHome");
                switchView(1);
            }else if(fragment instanceof ForgotPasswordSavePasswordFragment){
                currentDisplayedImage = 4;
                loadViewInFragment("forgotPasswordOTPValidate");
                switchView(1);
            }
        }
    }

    public void openSignInPage() {
        currentDisplayedImage = 3;
        loadViewInFragment("signInHome");
        switchView(1);
    }

    public void openSignUpPage() {
        currentDisplayedImage = 3;
        loadViewInFragment("signUpHome");
        switchView(1);
    }

    public void loginUser() {
        progressdialog=new ProgressDialog(PreLoginActivity.this);
        progressdialog.setCancelable(false);
        progressdialog.setMessage("Signing In");
        progressdialog.show();
        SignUpHelper.signInUser(PreLoginActivity.this,"("+ccode+")"+mobileNo,pass);
    }

    public void openForgotPassword() {
        currentDisplayedImage = 3;
        loadViewInFragment("forgotPasswordHome");
        switchView(1);
    }
}
