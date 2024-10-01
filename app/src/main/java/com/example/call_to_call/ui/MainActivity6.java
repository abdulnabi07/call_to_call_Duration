package com.example.call_to_call.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.call_to_call.MainActivity;
import com.example.call_to_call.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity6 extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private Button callButton;
    private TelephonyManager telephonyManager;
    private MyCallStateListener callStateListener;
    private long callStartTime;
    private long callEndTime;
    private ExecutorService executorService;

    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main6);

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        callButton = findViewById(R.id.callButton);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        callStateListener = new MyCallStateListener();

        executorService = Executors.newSingleThreadExecutor();

        // Request the CALL_PHONE permission if needed
        callButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE);
            } else {
                makePhoneCall();
            }
        });

        // Register the callback for Android 12 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            telephonyManager.registerTelephonyCallback(executorService, callStateListener);
        }
    }

    private void makePhoneCall() {
        // Remove hardcoded phone number and use EditText
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        if (!phoneNumber.isEmpty()) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                callStartTime = System.currentTimeMillis(); // Record the start time of the call
                startActivity(callIntent);
            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    makePhoneCall();
                } else {
                    Toast.makeText(MainActivity6.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            telephonyManager.unregisterTelephonyCallback(callStateListener);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private class MyCallStateListener extends TelephonyCallback implements TelephonyCallback.CallStateListener {

        @Override
        public void onCallStateChanged(int state) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d("api==>", "CALL_STATE_IDLE: ");
                    if (callStartTime != 0) {
                        callEndTime = System.currentTimeMillis(); // Record the end time of the call
                        long callDuration = (callEndTime - callStartTime) / 1000; // Calculate the duration in seconds
                        Log.d("api==>", "callDuration: " + callDuration);
                         Intent intent = new Intent(MainActivity6.this, MainActivity.class);
                       startActivity(intent);
                        //navigateToCallCompleted(callDuration);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d("api==>", "CALL_STATE_OFFHOOK: ");
                    // Call has started
                    callStartTime = System.currentTimeMillis(); // Record the start time of the call
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    // Incoming call ringing
                    break;
            }
        }
    }

    private void navigateToCallCompleted(long callDuration) {
        Intent intent = new Intent(MainActivity6.this, MainActivity5.class);
        intent.putExtra("callDuration", callDuration);
        startActivity(intent);
    }
}
