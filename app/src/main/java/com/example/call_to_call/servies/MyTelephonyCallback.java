package com.example.call_to_call.servies;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.call_to_call.MainActivity;

@RequiresApi(api = Build.VERSION_CODES.S)
public class MyTelephonyCallback extends TelephonyCallback implements  TelephonyCallback.CallStateListener {

    private long callStartTime;
    private boolean isCallOngoing = false;
   /* private Context context;

    public MyTelephonyCallback(Context context) {
        this.context = context;
    }*/
    @Override
    public void onCallStateChanged(int state) {
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                // Call ended
                if (isCallOngoing) {
                    long callEndTime = System.currentTimeMillis();
                    long callDuration = (callEndTime - callStartTime)/1000;


                    Log.d("CallDurationCallback", "Call Duration: " + callDuration + " sec");
                   /* Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);*/
                    isCallOngoing = false;
                }
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                // Call started
                if (!isCallOngoing) {
                    callStartTime = System.currentTimeMillis();
                    isCallOngoing = true;
                }
                break;

            case TelephonyManager.CALL_STATE_RINGING:
                // Phone is ringing
                break;
        }
    }
}
