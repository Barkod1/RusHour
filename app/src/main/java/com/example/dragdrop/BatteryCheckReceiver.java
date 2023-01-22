package com.example.dragdrop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

public class BatteryCheckReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float) scale;
        if(batteryPct<0.15){

            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "Your battery is low! make sure to charge your phone.", duration);
            toast.show();        }
    }
}