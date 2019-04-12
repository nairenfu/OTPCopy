package com.hylux.otpcopy;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class OtpService extends Service implements SmsReceiver.SmsListener {

    private SmsReceiver smsReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        smsReceiver = new SmsReceiver(this);
        registerReceiver(smsReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
        Log.d("OtpService", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("OtpService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("OtpService", "onDestroy");
        unregisterReceiver(smsReceiver);
        super.onDestroy();
    }

    @Override
    public void onSmsReceived(String otp) {
        Log.d("OtpService", "onSmsReceived");
        Log.d("SmsReceived", otp);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("OTP", otp);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(getApplicationContext(), "Copied " + otp + "to Clipboard", Toast.LENGTH_SHORT).show();
    }
}
