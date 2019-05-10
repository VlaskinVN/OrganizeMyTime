package com.example.nicol.organizemytime.alarm;

import android.content.Context;
import android.os.PowerManager;

public class WakeLocker {
    private static PowerManager.WakeLock wakeLock;

    public static void acquire(Context context){
        if (wakeLock != null)
            wakeLock.release();

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, WakeLocker.class.getName());
        wakeLock.acquire();
    }

    public static void release(){
        if (wakeLock != null)
            wakeLock.release();
        wakeLock = null;
    }
}
