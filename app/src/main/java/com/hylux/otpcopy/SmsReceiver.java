package com.hylux.otpcopy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Scanner;

public class SmsReceiver extends BroadcastReceiver {

    //TODO Toggle between restricted and unrestricted mode
    //TODO Lookout for keywords in SMS

    private SmsListener smsListener;

    public SmsReceiver(SmsListener smsListener) {
        this.smsListener = smsListener;
    }

    public String strip(String body) {
        int result = new Scanner(body).useDelimiter("\\D+").nextInt();
        return String.valueOf(result);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SmsReceiver", "onReceive");

        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            Log.d("onReceive", "isSms");

            String sender = "",
                    body = "";

            for (SmsMessage message : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                sender = message.getOriginatingAddress();
                body = body.concat(message.getMessageBody());
            }

            Log.d("smsSender", sender);
            Log.d("smsBody", body);

            //TODO get rid of symbols, spaces, brackets...
            if (MainActivity.otpSenders.containsKey(Long.parseLong(sender))) {
                smsListener.onSmsReceived(strip(body));
            }
        }
    }

    interface SmsListener {
        void onSmsReceived(String otp);
    }
}
