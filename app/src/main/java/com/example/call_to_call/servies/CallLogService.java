package com.example.call_to_call.servies;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.CallLog;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.call_to_call.model.CallLogItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CallLogService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<CallLogItem> callLogs = getCallLogs();
                Log.d("CallLogService", "Call Logs: " + callLogs.toString());
            }
        }).start();

        return START_STICKY;
    }
    private List<CallLogItem> getCallLogs() {
        List<CallLogItem> callRecords = new ArrayList<>();
        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);
            int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
            int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);

            do {
                String number = cursor.getString(numberIndex);
                String callType = cursor.getString(typeIndex);
                String callDate = cursor.getString(dateIndex);
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = cursor.getString(durationIndex);
                String dir = null;

                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }

                CallLogItem record = new CallLogItem(number, dir, callDayTime.toString(), callDuration);
                callRecords.add(record);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return callRecords;
    }








    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
