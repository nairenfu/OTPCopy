package com.hylux.otpcopy;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static boolean isRunning = true;

    private static String fileName = "senders.ser";
    private static File file;

    public static HashMap<Long, String> otpSenders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.
                checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 0);
        }

        if (ContextCompat.
                checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 0);
        }

        Log.d("files", getFilesDir().toString());
        try {
            FileInputStream fis = this.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            otpSenders = (HashMap<Long, String>) ois.readObject();
            ois.close();
            fis.close();
            Log.d("otpSenders", otpSenders.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            file = new File(getFilesDir(), fileName);
            otpSenders = new HashMap<>();
            Log.d("newFile", file.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        final Intent smsServiceIntent = new Intent(this, OtpService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(smsServiceIntent);
        } else {
            startService(smsServiceIntent);
        }
        Toast.makeText(getApplicationContext(), "OTP Copy service started", Toast.LENGTH_SHORT).show();

        Log.d("isRunning", String.valueOf(isRunning));

        final Button toggleServiceButton = findViewById(R.id.toggleService);
        if (isRunning) {
            toggleServiceButton.setText(R.string.stop_service);
        } else {
            toggleServiceButton.setText(R.string.start_service);
        }
        toggleServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRunning) {
                    stopService(smsServiceIntent);
                    Toast.makeText(getApplicationContext(), "OTP Copy service stopped", Toast.LENGTH_SHORT).show();
                    isRunning = false;
                    toggleServiceButton.setText(R.string.start_service);
                    Log.d("isRunning", String.valueOf(isRunning));
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(smsServiceIntent);
                    } else {
                        startService(smsServiceIntent);
                    }
                    Toast.makeText(getApplicationContext(), "OTP Copy service started", Toast.LENGTH_SHORT).show();
                    isRunning = true;
                    toggleServiceButton.setText(R.string.stop_service);
                    Log.d("isRunning", String.valueOf(isRunning));
                }
            }
        });

        final Button addSenderButton = findViewById(R.id.addButton);
        addSenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long number = Long.parseLong(((EditText) findViewById(R.id.addNumber)).getText().toString());
                String name = ((EditText) findViewById(R.id.addName)).getText().toString();
                otpSenders.put(number, name);
                Log.d("addSender", number + ", " + name);
                Log.d("otpSenders", otpSenders.toString());

                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(otpSenders);
                    fos.flush();
                    fos.close();
                    oos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    File file = new File(getFilesDir(), fileName);
                    Log.d("newFile", file.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
