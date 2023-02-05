package com.example.dragdrop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

//checks if the battery isn't low
public class BatteryCheckReceiver extends BroadcastReceiver {

    //checks if the battery isn't low
    //in case of battery is lower than 15% toast is appearing on screen
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