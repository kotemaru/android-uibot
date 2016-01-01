// Copyright (c) kotemaru.org  (APL/2.0)
package org.kotemaru.android.uibot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CommandReceiver extends BroadcastReceiver {
    private static final String PKG = "org.kotemaru.android.networkcontroller";
    public static final String WIFI_ACTION = PKG + ".WIFI";
    public static final String MOBILE_ACTION = PKG + ".MOBILE";
    public static final String AIRPLANE_ACTION = PKG + ".AIRPLANE";
    public static final String KEY_ENABLED = "enabled";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        boolean isEnabled = intent.getBooleanExtra(KEY_ENABLED, true);
        if (WIFI_ACTION.equals(action)) {
            NetworkUtil.setWifiEnabled(context, isEnabled);
        } else if (MOBILE_ACTION.equals(action)) {
            NetworkUtil.setMobileDataEnabled(context, isEnabled);
        } else if (AIRPLANE_ACTION.equals(action)) {
            NetworkUtil.setAirplaneMode(context, isEnabled);
        }
    }
}
