/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kotemaru.android.uibot;

import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Basic sample for unbundled UiAutomator.
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class ServerTest {

    private UiDevice mDevice;
    private JsDriver mJsDriver;
    private Context mContext;

    @Before
    public void init() {
        mContext = InstrumentationRegistry.getInstrumentation().getContext();
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mJsDriver = new JsDriver(mContext, mDevice);
    }

    @Test
    public void accept() {
        try {
            new AcceptThread(9999).run();
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }

    private class AcceptThread extends Thread {
        private ServerSocket ssock;

        public AcceptThread(int port) throws IOException {
            this.ssock = new ServerSocket(port);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Socket sock = ssock.accept();
                    exec(sock);
                }
            } catch (IOException e) {
                Log.e("AcceptThread", e.getMessage());
            } finally {
                mJsDriver.exit();
            }
        }
    }

    public void exec(Socket sock) {
        try {
            Log.i("ConnectThread", "From " + sock.getRemoteSocketAddress());

            Reader reader = new InputStreamReader(sock.getInputStream(), "utf-8");
            PrintWriter out = new PrintWriter(sock.getOutputStream());
            Object result = mJsDriver.eval(reader, out);
            //reader.close();
            out.println("" + result);
            out.flush();
            out.close();
            Log.i("ConnectThread", "End");
        } catch (IOException e) {
            Log.e("ConnectThread", e.getMessage());
        } finally {
            try {
                sock.close();
            } catch (IOException e) {
                Log.e("ConnectThread", e.getMessage());
            }
        }
    }

}
