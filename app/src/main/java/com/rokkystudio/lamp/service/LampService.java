package com.rokkystudio.lamp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.rokkystudio.lamp.BassPlayer;
import com.rokkystudio.lamp.media.MediaManager;
import com.rokkystudio.lamp.Player;

public class LampService extends Service implements Player.PlayerListener
{
    public static final String SERVICE_COMMAND = "COMMAND";
    public static final String MEDIA_SYNC = "MEDIA_SYNC";

    private MediaManager mMediaManager;
    private Player mPlayer;
    private final IBinder mBinder = new LampBinder();

    @Override
    public void onCreate()
    {
        super.onCreate();

        mMediaManager = new MediaManager(this);
        mMediaManager.syncMedia();

        //NotificationTools notification = new NotificationTools(this);
        //notification.showNotification();

        mPlayer = new BassPlayer(this);
        //player.playTrack(media.get(11).getAbsolutePath());
        // TODO restore player state
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // TODO Save player state
        mPlayer.free();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (intent.hasExtra(SERVICE_COMMAND))
        {
            String command = intent.getStringExtra(SERVICE_COMMAND);
            if (command.equals(MEDIA_SYNC)) {
                mMediaManager.syncMedia();
                return START_STICKY;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public Player getPlayer() {
        return mPlayer;
    }

    @Override
    public void onPlayerError(String message) {

    }

    public class LampBinder extends Binder {
        LampService getService() {
            return LampService.this;
        }
    }
}
