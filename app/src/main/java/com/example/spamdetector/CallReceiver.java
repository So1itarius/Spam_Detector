package com.example.spamdetector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.telephony.TelephonyManager;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.caller.info.R;

import java.io.IOException;
import java.util.ArrayList;


import static com.example.spamdetector.MainActivity.adapter;

public class CallReceiver extends BroadcastReceiver {
    private static boolean incomingCall = false;
    private static WindowManager windowManager;
    private static ViewGroup windowLayout;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

                String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                incomingCall = true;

                Log.debug("Show window: " + phoneNumber);
                try {
                    showWindow(context, phoneNumber);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                if (incomingCall) {
                    Log.debug("Close window.");
                    closeWindow();
                    incomingCall = false;
                }
            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                if (incomingCall) {
                    Log.debug("Close window.");
                    closeWindow();
                    incomingCall = false;
                }
            }
        }
    }

    public void showWindow(Context context, String phone) throws IOException {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP;

        windowLayout = (ViewGroup) layoutInflater.inflate(R.layout.info, null);
        //
        new MainActivity.NewThread().execute(phone);
        //
        TextView textViewNumber=(TextView) windowLayout.findViewById(R.id.textViewNumber);
        //
        MainActivity.lv = (ListView) windowLayout.findViewById(R.id.listView1);
        //
        Button buttonClose=(Button) windowLayout.findViewById(R.id.buttonClose);
        textViewNumber.setText(phone);
        //
        MainActivity.lv.setAdapter(MainActivity.adapter);
        //
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeWindow();
            }
        });

        windowManager.addView(windowLayout, params);
    }

    private void closeWindow() {
        if (windowLayout !=null){
            windowManager.removeView(windowLayout);
            windowLayout =null;
        }
    }
}