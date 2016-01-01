package org.kotemaru.android.uibot;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import org.kotemaru.android.uibot.NetworkUtil;

public class MainActivity extends AppCompatActivity {
    private Handler mUiHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setChecked(R.id.sw_wifi, NetworkUtil.isWifiEnabled(this));
        setChecked(R.id.sw_mobile, NetworkUtil.isMobileDataEnabled(this));
        setChecked(R.id.sw_airplane, NetworkUtil.isAirplaneMode(this));
    }

    private void setChecked(int resId, boolean b) {
        ((Switch) findViewById(resId)).setChecked(b);
    }

    public void onClickWifi(final View view) {
        final Context context = this;
        final Switch sw = (Switch) view;
        NetworkUtil.setWifiEnabled(this, sw.isChecked());
        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean real = NetworkUtil.isWifiEnabled(context);
                if (sw.isChecked() != real) {
                    Toast.makeText(context, "Can not set wifi mode.", Toast.LENGTH_LONG).show();
                    sw.setChecked(real);
                }
            }
        }, 500);
    }

    public void onClickMobile(View view) {
        final Context context = this;
        final Switch sw = (Switch) view;
        try {
            NetworkUtil.setMobileDataEnabled(this, sw.isChecked());
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean real = NetworkUtil.isMobileDataEnabled(context);
                if (sw.isChecked() != real) {
                    Toast.makeText(context, "Can not set mobile mode", Toast.LENGTH_LONG).show();
                    sw.setChecked(real);
                }
            }
        }, 500);
    }

    public void onClickAirPlane(View view) {
        final Context context = this;
        final Switch sw = (Switch) view;
        try {
            NetworkUtil.setAirplaneMode(this, sw.isChecked());
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean real = NetworkUtil.isAirplaneMode(context);
                if (sw.isChecked() != real) {
                    Toast.makeText(context, "Can not set airplane mode", Toast.LENGTH_LONG).show();
                    sw.setChecked(real);
                }
            }
        }, 500);
    }
}
