package com.rokkystudio.lamp.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import static android.content.Context.BIND_AUTO_CREATE;

public class LampClient
{
    private boolean mServiceBound = false;
    private LampService mLampService = null;
    private final Context mContext;

    private ServiceConnection mServiceConnection = new ServiceConnection()
    {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mLampService = ((LampService.LampBinder) binder).getService();
            mServiceBound = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
            mLampService = null;
        }
    };

    public LampClient(Context context) {
        mContext = context;
        Intent intent = new Intent(context, LampService.class);
        mContext.startService(intent);
        mContext.bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void unbindService() {
        if (mServiceBound) {
            mContext.unbindService(mServiceConnection);
            mLampService = null;
        }
    }

    public boolean isServiceBound() {
        return mServiceBound;
    }

    public LampService getService() {
        return mLampService;
    }
}
