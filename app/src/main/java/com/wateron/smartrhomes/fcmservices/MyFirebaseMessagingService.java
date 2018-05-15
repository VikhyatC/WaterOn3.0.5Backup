package com.wateron.smartrhomes.fcmservices;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wateron.smartrhomes.R;
import com.wateron.smartrhomes.activities.MainActivity;
import com.wateron.smartrhomes.models.Alert;
import com.wateron.smartrhomes.models.Apartment;
import com.wateron.smartrhomes.models.Meter;
import com.wateron.smartrhomes.util.DataHelper;

import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * Created by paran on 9/24/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String TAG ="FCM Update";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "FR"+remoteMessage.getData().toString());
        try{
            if(remoteMessage.getData().size() > 0){
                int type = Integer.parseInt(remoteMessage.getData().get("type"));
                int screen = Integer.parseInt(remoteMessage.getData().get("screen"));
                String title = remoteMessage.getData().get("title").toString();
                String message = remoteMessage.getData().get("message").toString();

                DataHelper helper = new DataHelper(getApplicationContext());
                if(type==0){
                    if(screen==0){
                        String alertlocation=remoteMessage.getData().get("alertlocation");
                        String alertquantity=remoteMessage.getData().get("alertquantity");
                        String alerttime=remoteMessage.getData().get("alerttime");
                        Log.d("AlertMessagingDetails:",alertlocation+" "+alertquantity+" "+alerttime);
                        Meter meter = helper.getMeter(alertlocation);
                        message+=" :"+meter.getLocationUser();
                        Alert alarm = new Alert();
                        alarm.setTime(alerttime);
                        alarm.setQty(Double.parseDouble(alertquantity));
                        alarm.setMeterId(meter.getId());
                        alarm.setAptId(meter.getAptId());
                        helper.updateAlert(alarm);

                        SharedPreferences sharedPreferences=getSharedPreferences("alert_manager",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putBoolean("loadNotificationAlert",true);
                        editor.putString("alertid","1");
                        editor.putInt("apartment_index",meter.getApartment_index());
                        editor.putInt("selectedApartment", meter.getAptId());
                        editor.apply();
                        showNotificationForAlert(title,message);
                    }
                    if(screen==1){
                        String valve=remoteMessage.getData().get("valve");
                        String status=remoteMessage.getData().get("status");
                        String lastoperated=remoteMessage.getData().get("lastoperated");
                        Log.d("valve",valve);
                        Meter meter = helper.getMeter(valve);
                        Log.d("MeterRead", String.valueOf(meter));
                        String st="";
                        if(status.equals("Opened")){
                            meter.setValveCurrentStatus("Open");
                            st=" Opened";
                        }else if(status.equals("Closed")){
                            meter.setValveCurrentStatus("Close");
                            st=" Closed";
                        }else {
                            meter.setValveCurrentStatus("NA");
                            st= " NA";
                        }
                        meter.setValveLastOperated(lastoperated);
//                        Log.d("LASTOPERATED",meter.getValveLastOperated());
                        helper.updateMeter(meter);
                        message = meter.getLocationUser()+": "+message+st+".";
                        Log.d("LastOperated",lastoperated);
                        SharedPreferences sharedPreferences1=getSharedPreferences("valve_manager",MODE_PRIVATE);
                        sharedPreferences1.edit().putString("lastOperated",meter.getValveLastOperated()).putBoolean("loadvalve",true).putString("apartment", String.valueOf(meter.getAptId())).putString("meterid", String.valueOf(meter.getId())).apply();
                        showNotificationForValve(title,message);
//                        SharedPreferences sharedPreferences=getSharedPreferences("toast_state",MODE_PRIVATE);
//                        sharedPreferences.edit().putBoolean(meter.getId(),false).apply();
                    }
                    if(screen==99){
                        showNotificationForHomeScreen(title,message);
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private int mNoteId=0;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showNotificationForHomeScreen(String title, String mainContent){
        Intent intent= new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("loadAlert",true);
        @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        NotificationManager noteMgr = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification note = new Notification.Builder(getApplication()).setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(mainContent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        note.flags |= Notification.FLAG_AUTO_CANCEL;
        noteMgr.notify(mNoteId, note);
        mNoteId++;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showNotificationForValve(String title, String messge) {
        Intent intent= new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("loadValve",true);
        @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        NotificationManager noteMgr = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification note = new Notification.Builder(getApplication()).setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(messge)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        note.flags |= Notification.FLAG_AUTO_CANCEL;
        noteMgr.notify(mNoteId, note);
        mNoteId++;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showNotificationForAlert(String title, String message) {
        Intent intent= new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("loadAlert",true);
        @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        NotificationManager noteMgr = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification note = new Notification.Builder(getApplication()).setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        note.flags |= Notification.FLAG_AUTO_CANCEL;
        noteMgr.notify(mNoteId, note);
        mNoteId++;
    }


}
