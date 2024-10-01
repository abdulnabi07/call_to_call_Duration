package com.example.call_to_call;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.call_to_call.broadcast.CallBroadcastReceiver;

public class MainActivity3 extends AppCompatActivity {


    private static final int REQUEST_CALL_PERMISSION = 1;
    private EditText editTextPhone;
    private Button buttonCall;
    private CallBroadcastReceiver callReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main3);


        editTextPhone = findViewById(R.id.editTextPhone);
        buttonCall = findViewById(R.id.buttonCall);

        callReceiver = new CallBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        registerReceiver(callReceiver, filter);

        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

    }

    private void makePhoneCall() {
        String number = editTextPhone.getText().toString();
        if (number.trim().isEmpty()) {
            Toast.makeText(MainActivity3.this, "Enter a phone number", Toast.LENGTH_SHORT).show();
        } else {
            /*if (ContextCompat.checkSelfPermission(MainActivity3.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity3.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, android.net.Uri.parse(dial)));
            }*/

            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, android.net.Uri.parse(dial)));
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(callReceiver);
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
}