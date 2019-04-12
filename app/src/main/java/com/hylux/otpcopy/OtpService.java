package com.hylux.otpcopy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class OtpService extends Service implements SmsReceiver.SmsListener {

    private SmsReceiver smsReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "channel_otp";
            String CHANNEL_NAME = "Channel OTP";

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
            channel.setSound(null, null);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID).setSound(null).setVibrate(new long[]{0});
            notification.setChannelId(CHANNEL_ID);

            startForeground(1, notification.build());
        }

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

        Toast.makeText(getApplicationContext(), "Copied " + otp + " to Clipboard", Toast.LENGTH_SHORT).show();
    }
}
