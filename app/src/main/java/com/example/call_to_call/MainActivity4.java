package com.example.call_to_call;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.call_to_call.adapters.CallLogAdapter;
import com.example.call_to_call.adapters.CallRecordAdapter;
import com.example.call_to_call.model.CallLogItem;
import com.example.call_to_call.servies.CallLogService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity4 extends AppCompatActivity {

    private static final int REQUEST_CODE_CALL_PERMISSION = 1;
    private RecyclerView recyclerView,recyclerView2;
    private CallLogAdapter adapter;
    private CallRecordAdapter adapter2;
    private List<CallLogItem> dialedNumbersList;
    private EditText phoneNumberEditText;
    private Button callButton;
    private TelephonyManager telephonyManager;
    private PhoneStateListener phoneStateListener;
    private boolean isOutgoingCall;

    private long callStartTime;
    private long callEndTime;
    private MediaRecorder recorder;
    private File audioFile;
    private List<String> recordingList = new ArrayList<>();
    private boolean isRecording = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView2 = findViewById(R.id.recyclerView2);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        callButton = findViewById(R.id.callButton);
        dialedNumbersList = new ArrayList<>();
        adapter = new CallLogAdapter(dialedNumbersList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        adapter2 = new CallRecordAdapter(this,recordingList);
        recyclerView2.setAdapter(adapter2);


        // Set up the telephony manager and phone state listener
     telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        startRecording();
                        callStartTime = System.currentTimeMillis();
                        isOutgoingCall = true;
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:

                        if (isOutgoingCall) {
                            // Fetch the last call details when the call ends
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    fetchLastCallDetails();
                                    stopRecording();
                                }
                            }, 1000);


                            isOutgoingCall = false;
                        }
                        break;
                }
            }
        };
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        // Start the background service to listen for call logs
        Intent serviceIntent = new Intent(this, CallLogService.class);
        startService(serviceIntent);

        // Call button click event
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEditText.getText().toString();
                if (!TextUtils.isEmpty(phoneNumber)) {
                    makeCall(phoneNumber);
                } else {
                    Toast.makeText(MainActivity4.this, "Enter a phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void startRecording() {
        try {
            File audioDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC);  // Use app-specific directory
            if (audioDir != null && !audioDir.exists()) {
                audioDir.mkdirs();  // Create directory if it doesn't exist
            }
            String outputPath = audioDir.getAbsolutePath() + "/call_recording_" + System.currentTimeMillis() + ".3gp";
            audioFile = new File(outputPath);

            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION); // Use appropriate audio source
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // Use 3GP format
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // Use AMR_NB for voice
            recorder.setOutputFile(outputPath);

            recorder.prepare();
            recorder.start();
            isRecording = true;
            Toast.makeText(this, "Recording Started", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error starting recording", Toast.LENGTH_SHORT).show();
            isRecording = false;
        }
    }



    private void stopRecording() {
        if (isRecording && recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                isRecording = false;  // Reset the flag after stopping recording.

                recordingList.add(audioFile.getAbsolutePath());
                Log.d("api==>","recordnglist==> "+recordingList.size());
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Recording Stopped", Toast.LENGTH_SHORT).show();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error stopping recording", Toast.LENGTH_SHORT).show();
            } finally {
                recorder = null;  // Clean up MediaRecorder object.
            }
        }
    }

    // Fetch the last dialed call details after the call ends
    private void fetchLastCallDetails() {
        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
            @SuppressLint("Range") String callType = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));


            String callTypeStr;
            switch (Integer.parseInt(callType)) {
                case CallLog.Calls.OUTGOING_TYPE:
                    callTypeStr = "Outgoing";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    callTypeStr = "Incoming";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    callTypeStr = "Missed";
                    break;
                default:
                    callTypeStr = "Unknown";
            }
            Toast.makeText(this, "duration"+duration, Toast.LENGTH_SHORT).show();

            dialedNumbersList.add(new CallLogItem(number, "Last Call", callTypeStr, duration + " sec"));
            adapter.notifyDataSetChanged();
            cursor.close();
        }
    }

    // Make a phone call from the app
    private void makeCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PERMISSION);
            return;
        }
        startActivity(callIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the phone state listener
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String phoneNumber = phoneNumberEditText.getText().toString();
                if (!TextUtils.isEmpty(phoneNumber)) {
                    makeCall(phoneNumber);
                }
            } else {
                Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
