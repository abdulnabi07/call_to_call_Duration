package com.example.call_to_call;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CallManager extends PhoneStateListener {
    private static final String TAG = "CallManager";

    private final Context context;
    private final TelephonyManager telephonyManager;
    private final SimpleDateFormat dateFormatter;
    private long callStartTime;
    private long callEndTime;
    private boolean isCallActive = false;
    private boolean isCallConnected = false;
    private CallManagerDelegate delegate;

    public CallManager(Context context) {
        this.context = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        telephonyManager.listen(this, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public void setDelegate(CallManagerDelegate delegate) {
        this.delegate = delegate;
    }

    public void startCall(String phoneNumber) {
        String uri = "tel:" + phoneNumber;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        context.startActivity(intent);
    }

    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        long currentTime = System.currentTimeMillis();

        switch (state) {
            case TelephonyManager.CALL_STATE_OFFHOOK:

                Log.d("api==>","onCallStateChanged-->CALL_STATE_OFFHOOK");
                Log.d("api==>","onCallStateChanged-->isCallActive "+isCallActive);
                if (!isCallActive) {

                    // Call is now active, but we don't start the timer until it's connected.
                    isCallActive = true;
                    Log.d("api==>","onCallStateChanged-->isCallActive -->2"+isCallActive);
                }
                break;

            case TelephonyManager.CALL_STATE_IDLE:
                Log.d("api==>","onCallStateChanged-->CALL_STATE_IDLE");
                if (isCallConnected) {
                    // Call ended after being connected
                    callEndTime = currentTime;
                    long callDuration = callEndTime - callStartTime;
                    isCallActive = false;
                    isCallConnected = false;
                    if (delegate != null) {
                        delegate.didUpdateCallEndTime(callEndTime);
                        delegate.didUpdateCallDuration(callDuration);
                    }
                    Log.d(TAG, "Call ended at: " + dateFormatter.format(new Date(callEndTime)));
                    Log.d(TAG, "Call duration: " + callDuration + " milliseconds");
                }
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                Log.d("api==>","onCallStateChanged-->CALL_STATE_RINGING");
                // Call is ringing but not yet connected
                isCallConnected = false;
                break;
        }
    }

    public void onCallConnected() {
        if (isCallActive) {
            Log.d("api==>","onCallStateChanged-->isCallActive -->3"+isCallActive);
            // Start the timer when the call is actually connected
            callStartTime = System.currentTimeMillis();
            isCallConnected = true;
            Log.d("api==>","onCallStateChanged-->delegate -->0"+delegate);
            if (delegate != null) {
                delegate.didUpdateCallStartTime(callStartTime);
            }
            Log.d(TAG, "Call connected at: " + dateFormatter.format(new Date(callStartTime)));
        }
    }
}
