package com.example.call_to_call.servies;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.call_to_call.MainActivity;

public class CallTrackingService extends Service {

    private long startTime = 0;
    private long endTime = 0;
    boolean callEnded=false;
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize TelephonyManager and listen to call state changes
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        // Call started
                        callEnded=true;
                        startTime = System.currentTimeMillis();
                        Log.d("CallTrackingService", "Call started");
                        break;

                    case TelephonyManager.CALL_STATE_IDLE:
                        // Call ended
                       /* if (startTime != 0) {
                            endTime = System.currentTimeMillis();
                            long durationInMillis = endTime - startTime;
                            long durationInSeconds = durationInMillis / 1000;
                            Log.d("CallTrackingService", "Call ended. Duration: " + durationInSeconds + " seconds");


                            // Start MainActivity
                          *//*  Intent newActivityIntent = new Intent(CallTrackingService.this, MainActivity.class);
                            newActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            newActivityIntent.putExtra("callDuration", durationInSeconds); // Pass call duration
                            startActivity(newActivityIntent);*//*
                            // Try to navigate to SecondActivity


                            navigateToSecondActivity(durationInSeconds);

                            // Stop the service
                            stopSelf();
                        }*/


                        if(callEnded)
                        {Log.d("CallTrackingService", "Call ended. Duration: if ");
                            //you will be here at **STEP 4**
                            //you should stop service again over here
                        }
                        else
                        {Log.d("CallTrackingService", "Call ended. Duration:  else" );
                            //you will be here at **STEP 1**
                            //stop your service over here,
                            //i.e. stopService (new Intent(`your_context`,`CallService.class`));
                            //NOTE: `your_context` with appropriate context and `CallService.class` with appropriate class name of your service which you want to stop.

                        }

                        break;
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void navigateToSecondActivity(long callDuration) {
        // Create an Intent to open SecondActivity
        Intent secondActivityIntent = new Intent(this, MainActivity.class);
        secondActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        secondActivityIntent.putExtra("callDuration", callDuration);  // Pass call duration

        // Try to start the activity directly (if the app is in the foreground)
        try {
            startActivity(secondActivityIntent);
        } catch (Exception e) {
            Log.e("CallTrackingService", "Unable to start activity directly: " + e.getMessage());

            // Show a notification as a fallback for Android 10 and above if activity launch is blocked
           // showNotification(callDuration);
        }
    }



}
