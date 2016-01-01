// Copyright (c) kotemaru.org  (APL/2.0)
package org.kotemaru.android.uibot;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

public class JsDriver {
    private static final String TAG = "JsDriver";
    private final Context mContext;
    private final Scriptable mGlobalScope;
    private final UiDevice mUiDevice;
    private final Bot mBot;

    public JsDriver(android.content.Context androidContext, UiDevice uiDevice) {
        mUiDevice = uiDevice;
        mBot = new Bot(androidContext, uiDevice);
        mContext = Context.enter();
        mContext.setOptimizationLevel(-1); // use interpreter mode
        mGlobalScope = mContext.initStandardObjects();
        mGlobalScope.put("device", mGlobalScope, uiDevice);
        mGlobalScope.put("bot", mGlobalScope, mBot);
        mGlobalScope.put("By", mGlobalScope, By.class);
    }

    public void exit() {
        Context.exit();
    }

    public Object eval(Reader reader, PrintWriter out) throws IOException {
        try {
            mBot.setPrintWriter(out);
            Object result = mContext.evaluateReader(mGlobalScope, reader, "source", 1, null);
            return result;
            /*
            StringBuilder sbuf = new StringBuilder();
            int ch;
            while ((ch=reader.read()) >= 0) {
                //if (ch == 26) break;
                sbuf.append((char)ch);
            }
            //reader.close();
            String script = sbuf.toString();
            Log.d(TAG, "script="+script);
            Object result = mContext.evaluateString(mGlobalScope, script, "source", 1, null);
            return result;
*/

        } catch (RhinoException e) {
            Log.e(TAG, e.getMessage(), e);
            return e.toString();
        }
    }

    public Object eval(String script) throws IOException {
        try {
            Object result = mContext.evaluateString(mGlobalScope, script, "source", 1, null);
            return result;
        } catch (RhinoException e) {
            Log.e(TAG, e.getMessage(), e);
            return e.toString();
        }
    }
}
