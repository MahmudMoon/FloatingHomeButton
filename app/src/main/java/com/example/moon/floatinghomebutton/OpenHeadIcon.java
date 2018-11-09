package com.example.moon.floatinghomebutton;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class OpenHeadIcon extends BroadcastReceiver {

    Context mcontext;
    @Override
    public void onReceive(Context context, Intent intent) {
        mcontext = context;
        if(!isMyServiceRunning("com.example.moon.floatinghomebutton.FloatingViewService")) {
            Log.i("SERVICE STARTED", "onReceive: SERVICE STARTED");
            Toast.makeText(context,"SERVICE STARTED",Toast.LENGTH_SHORT).show();
            context.startService(new Intent(context, FloatingViewService.class));
        }else{
            Log.i("SERVICE STARTED", "onReceive: Service Running");
            Toast.makeText(context,"Service Running",Toast.LENGTH_SHORT).show();
        }


    }

    private boolean isMyServiceRunning(String serviceClass) {
        ActivityManager manager = (ActivityManager)mcontext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
