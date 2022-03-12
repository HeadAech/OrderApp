package com.example.orderapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class Confirmation {
    private Context context;
    public Confirmation(Context context){
        this.context = context;
    }

    public void sendSMS(String phoneNumber, Double orderTotal){
        SmsManager smsManager = SmsManager.getDefault();
        String msg = context.getResources().getString(R.string.ordered_sms);
        smsManager.sendTextMessage(phoneNumber, null, msg +  " " + orderTotal + " PLN.", null, null);
//        Toast.makeText(context.getApplicationContext(), "SMS sent.",
//                Toast.LENGTH_LONG).show();
    }
}
