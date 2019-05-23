package com.rokkystudio.lamp;

import android.content.Context;

public abstract class Player
{
    public enum RepeatMode {
        REPEAT_ALL, REPEAT_ONE
    }

    public enum ShuffleMode {
        SHUFFLE_NONE, SHUFFLE_ALL
    }

    private RepeatMode  mRepeatMode  = RepeatMode.REPEAT_ALL;
    private ShuffleMode mShuffleMode = ShuffleMode.SHUFFLE_NONE;

    private final Context mContext;
    private Playlist mPlaylist;
    private PlayerListener mPlayerListener = null;

    // Текущее состояние плеера
    public static final int STATE_UNLOAD  = 0;
    public static final int STATE_READY   = 1;
    public static final int STATE_PLAYING = 2;
    public static final int STATE_PAUSED  = 3;
    public static final int STATE_STOPPED = 4;
    public static final int STATE_ERROR   = 5;

    private int mState = STATE_UNLOAD;

    public Player(Context context) {
        mContext = context;
        mPlaylist = new Playlist();
    }

    public abstract void playPause();
    public abstract void nextTrack();
    public abstract void prevTrack();
    public abstract void setTrackPosition(float position);
    public abstract void setVolume(float volume);
    public abstract void stop();

    public void excludeTrack() {
        // TODO добавить текущий трек в список исключений, запустить следующий трек
    }

    public void free() {
        mPlaylist.clear();
    }

    public void load(Playlist playlist) {
        stop();
        free();
        mPlaylist = playlist;
    }

    public void setRepeatMode(RepeatMode mode) {
        mRepeatMode = mode;
    }

    public RepeatMode getRepeatMode() {
        return mRepeatMode;
    }

    public void setShuffleMode(ShuffleMode mode) {
        mShuffleMode = mode;
    }

    public ShuffleMode getShuffleMode() {
        return mShuffleMode;
    }

    public void setPlayerListener(PlayerListener listener) {
        mPlayerListener = listener;
    }

    protected int getPlayerState() {
        return mState;
    }

    protected void setPlayerState(int state) {
        mState = state;
    }

    protected PlayerListener getPlayerListener() {
        return mPlayerListener;
    }

    protected boolean isSetPlayerListener() {
        return mPlayerListener != null;
    }

    public interface PlayerListener {
        void onPlayerError(String message);
    }
}
