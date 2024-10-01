package com.example.call_to_call.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.Date;

public class CallBroadcastReceiver extends BroadcastReceiver {

    //private static Date callStartTime;
    //private static boolean isCallActive = false;


    private static Date callStartTime;
    private static boolean isCallConnected = false;

    @Override
    public void onReceive(Context context, Intent intent) {

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

      /*  if (state != null) {
            if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                // Call started
                callStartTime = new Date();
                isCallActive = true;
                Toast.makeText(context, "Call started", Toast.LENGTH_SHORT).show();
            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE) && isCallActive) {
                // Call ended
                Date callEndTime = new Date();
                long duration = (callEndTime.getTime() - callStartTime.getTime()) / 1000; // in seconds
                isCallActive = false;
                Toast.makeText(context, "Call ended. Duration: " + duration + " seconds", Toast.LENGTH_LONG).show();
            }
        }*/

        if (state != null) {
            // Detect when the call is connected
            if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK) && !isCallConnected) {
                callStartTime = new Date();
                isCallConnected = true;
                Toast.makeText(context, "Call connected", Toast.LENGTH_SHORT).show();
            }
            // Detect when the call ends
            else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE) && isCallConnected) {
                Date callEndTime = new Date();
                long duration = (callEndTime.getTime() - callStartTime.getTime()) / 1000; // Call duration in seconds
                isCallConnected = false;
                Toast.makeText(context, "Call ended. Duration: " + duration + " seconds", Toast.LENGTH_LONG).show();
            }
        }


    }
}
