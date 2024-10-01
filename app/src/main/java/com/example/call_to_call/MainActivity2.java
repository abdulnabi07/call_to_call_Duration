package com.example.call_to_call;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;

public class MainActivity2 extends AppCompatActivity {


    private EditText phoneNumberEditText;
    private Button callButton;
    private TextView callDurationTextView;
    private TelephonyManager telephonyManager;
    private long callStartTime;
    private long callEndTime;
    private long callCurrentTime;
    private long callRealTime;
    private boolean isCallOngoing;
    private boolean isSpokenTimeActive = false;  // Only track the spoken time (after call is answered)
    private boolean isCallAnswered = false;  // To track whether the call is answered
    private boolean isCallActive = false; // This will be used to track whether the call is really in progress
    private boolean isIncomingCall = false; // To track whether the call is incoming
    private boolean isOutgoingCall = false; // To track whether the call is outgoing

    private static final int REQUEST_CALL_PERMISSION = 1;

    private long ringingStartTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);


        phoneNumberEditText = findViewById(R.id.phone_number);
        callButton = findViewById(R.id.call_button);
        callDurationTextView = findViewById(R.id.call_duration);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
        setupPhoneStateListener();
     /*   telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);*/

    }


    private void makePhoneCall() {
        String phoneNumber = phoneNumberEditText.getText().toString();
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
            return;
        }

       /* if (ContextCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {

        }*/

        startPhoneCall(phoneNumber);
    }


    private void startPhoneCall(String phoneNumber) {
        String dial = "tel:" + phoneNumber;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(dial));
        startActivity(callIntent);
    }
   /* private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if (isCallOngoing) {
                        callEndTime = System.currentTimeMillis();
                        long callDuration = (callEndTime - callStartTime) / 1000; // in seconds
                        callDurationTextView.setText("Call duration: " + callDuration + " seconds");
                        callDurationTextView.setVisibility(View.VISIBLE);
                        isCallOngoing = false;
                    }
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    callStartTime = System.currentTimeMillis();
                    isCallOngoing = true;
                    callDurationTextView.setVisibility(View.GONE);
                    break;

                case TelephonyManager.CALL_STATE_RINGING:
                    // Call is ringing, no action needed here
                    break;
            }
        }
    };*/

  /*  private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    // When the call is over and it was active (spoken time):
                    if (isCallAnswered) {
                        Log.d("api==>","isCallAnswered:1 "+isCallAnswered);
                        callEndTime = System.currentTimeMillis();
                        long callDuration = (callEndTime - callStartTime) / 1000;  // duration in seconds
                        callDurationTextView.setText("Call duration: " + callDuration + " seconds");
                        callDurationTextView.setVisibility(View.VISIBLE);
                        isCallAnswered = false;  // Reset flag
                    }
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // When the call is answered and spoken time begins:
                    Log.d("api==>","isCallAnswered:2 "+isCallAnswered);
                    if (isCallAnswered) {
                        Log.d("api==>","isCallAnswered:3 "+isCallAnswered);
                        callStartTime = System.currentTimeMillis();
                        isCallAnswered = true;  // Start tracking spoken time
                        callDurationTextView.setVisibility(View.GONE);  // Hide text view during the call
                    }
                    break;




                case TelephonyManager.CALL_STATE_RINGING:
                    // Call is ringing, do nothing special
                    break;
            }
        }
    };
*/

   /* private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    // When the call ends
                    Log.d("api==>","isCallActive:0 "+isCallActive);
                    Log.d("api==>","iCALL_STATE_IDLE ");
                    if (isCallActive) {
                        Log.d("api==>","isCallActive:1 "+isCallActive);
                        callEndTime = System.currentTimeMillis();
                        callStartTime = System.currentTimeMillis();
                        callRealTime =(callCurrentTime-callStartTime)/1000;

                        callDurationTextView.setText("callRealTime: time: " + callRealTime + " seconds");

                        Log.d("api==>","callRealTime:2 "+callRealTime);
                        Log.d("api==>","callStartTime:1 "+callStartTime);

                 *//*       long callDuration = (callEndTime - callStartTime) / 1000;  // Duration in seconds
                        callDurationTextView.setText("Spoken time: " + callDuration + " seconds");*//*
                        callDurationTextView.setVisibility(View.VISIBLE);
                        isCallActive = false; // Reset after the call ends
                        isOutgoingCall = false;
                        isIncomingCall = false;
                    }
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d("api==>","CALL_STATE_OFFHOOK ");
                    // When the call is connected (either outgoing or incoming)
                    Log.d("api==>","isCallActive:2 "+isCallActive+" "+isOutgoingCall);


                    if (!isCallActive && !isOutgoingCall) {
                        // Check if it's an outgoing call and the call is not active yet
                        callStartTime = System.currentTimeMillis();

                        callRealTime =(callCurrentTime-callStartTime)/1000;
                        callDurationTextView.setText("callRealTime: time: " + callRealTime + " seconds");
                        Log.d("api==>","callRealTime:1 "+callRealTime);
                        Log.d("api==>","callStartTime:0 "+callStartTime);
                        isCallActive = true;  // Mark the call as active
                        callDurationTextView.setVisibility(View.GONE);
                        Log.d("api==>","isCallActive:3 "+isCallActive+" "+isOutgoingCall);

                    }
                    break;

                case TelephonyManager.CALL_STATE_RINGING:
                    // Phone is ringing (incoming call)
                    Log.d("api==>","CALL_STATE_RINGING ");
                    callCurrentTime=System.currentTimeMillis();
                    isIncomingCall = true; // Track incoming call
                    Log.d("api==>","callCurrentTime:0 "+callCurrentTime);
                    Log.d("api==>","isIncomingCall:0 "+isIncomingCall);

                    break;
            }
        }
    };*/


/*    private void setupPhoneStateListener() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);

                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        // Phone is ringing
                        ringingStartTime = System.currentTimeMillis();
                        Log.d("PhoneState", "Phone started ringing at: " + new Date(ringingStartTime));
                        break;

                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_IDLE:
                        // Call has been answered or ended
                        if (ringingStartTime != 0) {
                            long ringingDuration = System.currentTimeMillis() - ringingStartTime;
                            callDurationTextView.setText("callRealTime: time: " + ringingDuration + " seconds");
                            Log.d("PhoneState", "Phone rang for " + (ringingDuration / 1000) + " seconds.");
                            ringingStartTime = 0; // Reset the start time
                        }
                        break;
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }*/

    private void setupPhoneStateListener() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);

                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        // Phone is ringing
                        ringingStartTime = System.currentTimeMillis();
                        Log.d("PhoneState", "Phone started ringing at: " + new Date(ringingStartTime));
                        break;

                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        // Either an outgoing call is in progress or an incoming call has been answered
                        if (ringingStartTime != 0) {
                            // This is an incoming call that has been answered
                            long ringingDuration = System.currentTimeMillis() - ringingStartTime;
                            callDurationTextView.setText("callRealTime: time: " + ringingDuration / 1000 + " seconds");
                            Log.d("PhoneState", "Phone rang for " + (ringingDuration / 1000) + " seconds.");
                            ringingStartTime = 0; // Reset the start time
                        } else {
                            // This could be an outgoing call
                            Log.d("PhoneState", "Outgoing call started");
                        }
                        break;

                    case TelephonyManager.CALL_STATE_IDLE:
                        // Call has ended
                        if (ringingStartTime != 0) {
                            long ringingDuration = System.currentTimeMillis() - ringingStartTime;
                            callDurationTextView.setText("callRealTime: time: " + ringingDuration / 1000 + " seconds");
                            Log.d("PhoneState", "Phone rang for " + (ringingDuration / 1000) + " seconds.");
                            ringingStartTime = 0; // Reset the start time
                        }
                        break;
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
    }
}