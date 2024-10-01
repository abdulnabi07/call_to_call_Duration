package com.example.call_to_call.broadcast;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.call_to_call.MainActivity;

public class CallReceiver  extends BroadcastReceiver {
    private long startTime;
    private long endTime ;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(action)) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            Log.d("CallDurationReceiver", "state:  "+state);
            if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
                // Call started, capture the start time
                startTime = System.currentTimeMillis();
                Log.d("CallDurationReceiver", "Call started"+startTime);
            }

            if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
                // Call ended, capture the end time
                endTime = System.currentTimeMillis();
                Log.d("CallDurationReceiver", "Call ended. Duration: " + " seconds");
                long durationInMillis = endTime - startTime;
                long durationInSeconds = durationInMillis / 1000;
                Log.d("CallDurationReceiver", "Call ended. Duration: " + durationInSeconds + " seconds");
                Intent newActivityIntent = new Intent(context, MainActivity.class);
                newActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                newActivityIntent.putExtra("callDuration", durationInSeconds); // Pass call duration
                context.startActivity(newActivityIntent);
                if (startTime != 0) {
                    // Calculate the call duration

                }
            }
        }
    }


/*    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        // Check for outgoing call
        if (Intent.ACTION_NEW_OUTGOING_CALL.equals(action)) {
            String outgoingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d("CallReceiver", "Outgoing call to: " + outgoingNumber);
        }

        // Check for phone state change (incoming call)
        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(action)) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d("CallReceiver", "Incoming call from: " + incomingNumber);
            }

            if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
                // Phone is idle, meaning the call has ended
                Log.d("CallReceiver", "Call ended. Fetching call log...");
                fetchCallLog(context);
            }
        }
    }

    // Fetch call history from the CallLog content provider
    private void fetchCallLog(Context context) {
        Uri callLogUri = CallLog.Calls.CONTENT_URI;
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(callLogUri, null, null, null, CallLog.Calls.DATE + " DESC");
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));

                Log.d("CallReceiver", "Last call - Number: " + number + ", Type: " + type + ", Date: " + date + ", Duration: " + duration);
            }
        } catch (SecurityException e) {
            Log.e("CallReceiver", "Permission denied to read call log.", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }*/
}
