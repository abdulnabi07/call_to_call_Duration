package com.example.call_to_call.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.call_to_call.R;
import com.example.call_to_call.servies.CallTrackingService;


public class MainActivity5 extends AppCompatActivity {

    private static final int REQUEST_CALL_PERMISSION = 1;
    private static final int REQUEST_CODE = 1;

    Button callButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        callButton = findViewById(R.id.call);

        callButton.setOnClickListener(v -> {
            String phoneNumber = "9346678276"; // Replace with the desired phone number

            if (!phoneNumber.isEmpty()) {
                checkAndMakePhoneCall(phoneNumber);
            } else {
                Toast.makeText(MainActivity5.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            }
        });

        // Check and request permissions if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.READ_PHONE_STATE
                }, REQUEST_CODE);
            }
        }
    }

    // Function to check and request CALL_PHONE permission before making the call
    private void checkAndMakePhoneCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            makePhoneCall(phoneNumber);
        }
    }

    // Function to make a phone call and start tracking the call via the service
    private void makePhoneCall(String phoneNumber) {
        String dial = "tel:" + phoneNumber;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(dial));

        // Start call and call tracking service
        startActivity(callIntent);

        // Start CallTrackingService
        Intent serviceIntent = new Intent(this, CallTrackingService.class);
        startService(serviceIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the call
                String phoneNumber = "9346678276"; // Use a stored or inputted phone number here
                makePhoneCall(phoneNumber);
            } else {
                // Permission denied
                Toast.makeText(this, "Call permission is required to make a phone call", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions for reading call logs or phone state granted
            } else {
                Toast.makeText(this, "Permission is required for tracking calls", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
