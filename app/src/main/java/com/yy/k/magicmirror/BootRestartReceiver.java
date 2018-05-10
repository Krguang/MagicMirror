package com.yy.k.magicmirror;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootRestartReceiver extends BroadcastReceiver {

    public static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION))
        {
            Intent intent2 = new Intent(context, MainActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
        }
    }
}