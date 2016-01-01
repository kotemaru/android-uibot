// Copyright (c) kotemaru.org  (APL/2.0)
package org.kotemaru.android.uibot;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NetworkUtil {
    private static final String TAG = NetworkUtil.class.getSimpleName();

    public static void setWifiEnabled(Context context, boolean isEnabled) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(isEnabled);
    }

    public static boolean isWifiEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    public static void setAirplaneMode(Context context, boolean isEnabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Log.e(TAG, "setAirplaneMode() not work on over JELLY_BEAN_MR1.");
            throw new RuntimeException("setAirplaneMode() not work on over JELLY_BEAN_MR1.");
        }

        Settings.System.putInt(
                context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON,
                isEnabled ? 1 : 0);
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", isEnabled);
        context.sendBroadcast(intent);
    }

    @SuppressWarnings("deprecation")
    public static boolean isAirplaneMode(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    public static void setMobileDataEnabled(Context context, boolean isEnabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.e(TAG, "setMobileDataEnabled() not work on over LOLLIPOP.");
            throw new RuntimeException("setMobileDataEnabled() not work on over LOLLIPOP.");
        }

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Class clazz = Class.forName(cm.getClass().getName());
            Field serviceField = clazz.getDeclaredField("mService");
            serviceField.setAccessible(true);
            Object service = serviceField.get(cm);
            Class clazz2 = Class.forName(service.getClass().getName());
            Method setMobileDataEnabledMethod = clazz2.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(service, isEnabled);
        } catch (Exception e) {
            Log.e(TAG, "setMobileDataEnabled failed.", e);
        }
    }

    @SuppressWarnings("deprecation")
    public static boolean isMobileDataEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            return Settings.Global.getInt(context.getContentResolver(), "mobile_data", 0) != 0;
        } else {
            return Settings.System.getInt(context.getContentResolver(), "mobile_data", 0) != 0;
        }
    }

}
