package com.wateron.smartrhomes.fcmservices;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.wateron.smartrhomes.util.LoginHandler;

/**
 * Created by paran on 9/24/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {



    /*Call to sendRegistrationToServer method has been commented cause we have moved to a system
    which uploads the FCM token to server so we do not need to do it here*/
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM Update", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
      //  sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        if(LoginHandler.isUserLoggedIn(getApplicationContext())){
            RegistrationTokenUpdater.updateToken(getApplicationContext(),refreshedToken);
        }else{
            Log.d("FCM token","Not updated, user not logged in");
        }
    }
}
