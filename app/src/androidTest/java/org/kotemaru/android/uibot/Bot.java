// Copyright (c) kotemaru.org  (APL/2.0)
package org.kotemaru.android.uibot;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.util.Log;

import java.io.PrintWriter;

public class Bot {
    private static final String TAG = "Bot";
    private final UiDevice mDevice;
    private final android.content.Context mAndroidContext;
    private PrintWriter mPrintWriter;

    public Bot(android.content.Context androidContext, UiDevice uiDevice) {
        mDevice = uiDevice;
        mAndroidContext = androidContext;
    }

    public UiObject2 find(String key) {
        UiObject2 obj = findRes(key);
        if (obj != null) return obj;
        return findText(key);
    }

    public UiObject2 findText(String text) {
        return mDevice.findObject(By.text(text));
    }

    public UiObject2 findRes(String resId) {
        return mDevice.findObject(By.res(resId));
    }

    public UiObject2 click(String key) {
        UiObject2 obj = find(key);
        //Log.d(TAG, "click:" + obj);
        if (obj != null) obj.click();
        return obj;
    }

    public UiObject2 clickText(String text) {
        UiObject2 obj = findText(text);
        if (obj != null) obj.click();
        return obj;
    }

    public UiObject2 clickRes(String resId) {
        UiObject2 obj = findRes(resId);
        if (obj != null) obj.click();
        return obj;
    }

    public UiObject2 waitText(String text, long timeout) {
        UiObject2 obj = mDevice.wait(Until.findObject(By.text(text)), timeout);
        return obj;
    }

    public UiObject2 waitRes(String resId, long timeout) {
        UiObject2 obj = mDevice.wait(Until.findObject(By.res(resId)), timeout);
        return obj;
    }

    public boolean setWifiEnabled(boolean b) {
        NetworkUtil.setWifiEnabled(mAndroidContext, b);
        return NetworkUtil.isWifiEnabled(mAndroidContext);
    }
    public boolean isWifiEnabled() {
        return NetworkUtil.isWifiEnabled(mAndroidContext);
    }
    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void log(String msg) {
        Log.d(TAG, msg);
        mPrintWriter.println(msg);
    }

    public void setPrintWriter(PrintWriter printWriter) {
        mPrintWriter = printWriter;
    }
}
