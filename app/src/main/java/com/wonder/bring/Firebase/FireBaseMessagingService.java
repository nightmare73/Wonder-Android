package com.wonder.bring.Firebase;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;



public class FireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }

}
