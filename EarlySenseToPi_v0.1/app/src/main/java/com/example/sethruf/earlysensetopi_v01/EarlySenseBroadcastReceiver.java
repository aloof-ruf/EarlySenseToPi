package com.example.sethruf.earlysensetopi_v01;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Seth.Ruf on 24/04/2015.
 */
public class EarlySenseBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent startServiceIntent = new Intent(context, EarlySenseBackgroundService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, startServiceIntent, 0);
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 30 * 1000, pendingIntent);
            Log.d("Service", "Alarm Manager Started");
        }
    }

}
