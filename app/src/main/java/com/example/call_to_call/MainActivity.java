package com.example.call_to_call;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CallManagerDelegate {

    private static final int REQUEST_CALL_PERMISSION = 1;
    private EditText phoneNumberEditText;
    private Button callButton;
    private RecyclerView callLogsRecyclerView;
    private CallLogsAdapter callLogsAdapter;
    private List<String> callLogsList;
    private CallManager callManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        long callDuration = getIntent().getLongExtra("callDuration", 0);

        // Display the call duration
        TextView durationTextView = findViewById(R.id.durationTextView);
        durationTextView.setText("Call Duration: " + callDuration + " seconds");

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        callButton = findViewById(R.id.callButton);
        callLogsRecyclerView = findViewById(R.id.callLogsRecyclerView);

        callLogsList = new ArrayList<>();
        Log.d("api==>","callLogsList: "+callLogsList);
        callLogsAdapter = new CallLogsAdapter(callLogsList);
        callLogsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        callLogsRecyclerView.setAdapter(callLogsAdapter);

        callManager = new CallManager(this);
        callManager.setDelegate(this);

        callButton.setOnClickListener(v -> {
            String phoneNumber = phoneNumberEditText.getText().toString();
            if (phoneNumber.isEmpty()) {
                Toast.makeText(MainActivity.this, "Enter a phone number", Toast.LENGTH_SHORT).show();
            } else {
                makePhoneCall(phoneNumber);
            }
        });
    }

    private void makePhoneCall(String phoneNumber) {
       /* if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {

        }*/
        callManager.startCall(phoneNumber);
        // Here we assume a delay before the call is connected; you can adjust this based on actual behavior.
        new android.os.Handler().postDelayed(() -> callManager.onCallConnected(), 2000);
    }

    @Override
    public void didUpdateCallStartTime(long startTime) {
        Log.d("api==>", "Call started at: " + new Date(startTime).toString());
    }

    @Override
    public void didUpdateCallEndTime(long endTime) {
        Log.d("api==>", "Call ended at: " + new Date(endTime).toString());
    }

    @Override
    public void didUpdateCallDuration(long duration) {
        String log = "Call duration: " + (duration / 1000) + " seconds";
        Log.d("api==>","Callduration "+log);
        callLogsList.add(log);
        callLogsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String phoneNumber = phoneNumberEditText.getText().toString();
                makePhoneCall(phoneNumber);
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
