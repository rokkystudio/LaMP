package com.rokkystudio.lamp;

import android.content.Context;

import com.un4seen.bass.BASS;

import static com.un4seen.bass.BASS.BASS_DEVICE_LATENCY;

public class BassPlayer extends Player
{
    private static final int DEFAULT_DEVICE = -1;
    private static final int DEFAULT_FREQUENCY = 44100;

    // Канал воспроизводимого трека
    public static final int CHANNEL_UNKNOWN = -1;
    public static final int CHANNEL_ERROR = 0;
    private int mChannel = CHANNEL_UNKNOWN;

    // Код последней обнаруженной ошибки
    private int mLastErrorCode = BASS.BASS_OK;

    public BassPlayer(Context context) {
        super(context);
        BASS.BASS_Init(DEFAULT_DEVICE, DEFAULT_FREQUENCY, BASS_DEVICE_LATENCY);
        checkError();
    }

    public void load(String path)
    {
        // TODO Использовать эту функцию для проигрывания одного медиафайла из текущего плейлиста или по запросу из бю
        free();
        mChannel = BASS.BASS_StreamCreateFile(path, 0, 0, 0);

        if (mChannel == CHANNEL_ERROR) {
            setPlayerState(STATE_ERROR);
            checkError();
        } else {
            setPlayerState(STATE_READY);
        }
    }

    public void free()
    {
        // TODO использовать Free для высвобождения ресурсов когда происходит Destroy службы
        if (getPlayerState() == STATE_PLAYING ||
            getPlayerState() == STATE_PAUSED) {
            stop();
        }

        if (mChannel != CHANNEL_ERROR && mChannel != CHANNEL_UNKNOWN) {
            BASS.BASS_StreamFree(mChannel);
            setPlayerState(STATE_UNLOAD);
            checkError();
        }
    }

    @Override
    public void playPause()
    {
        // TODO разобраться с паузой и доделать
        if (getPlayerState() != STATE_PLAYING) {
            BASS.BASS_ChannelPlay(mChannel, true);
            setPlayerState(STATE_PLAYING);
            checkError();
        } else if (getPlayerState() == STATE_PLAYING) {
            BASS.BASS_ChannelPause(mChannel);
            setPlayerState(STATE_PAUSED);
            checkError();
        }
    }

    @Override
    public void nextTrack() {

    }

    @Override
    public void prevTrack() {

    }

    @Override
    public void setTrackPosition(float position) {

    }

    @Override
    public void setVolume(float volume) {

    }


    @Override
    public void stop() {
        if (getPlayerState() == STATE_PLAYING ||
            getPlayerState() == STATE_PAUSED) {
            BASS.BASS_ChannelStop(mChannel);
            setPlayerState(STATE_STOPPED);
            checkError();
        }
    }

    private void checkError() {
        mLastErrorCode = BASS.BASS_ErrorGetCode();
        if (mLastErrorCode == BASS.BASS_OK) return;

        setPlayerState(STATE_ERROR);
        if (isSetPlayerListener()) {
            String errorMessage = getErrorMessage(mLastErrorCode);
            getPlayerListener().onPlayerError(errorMessage);
        }
    }

    private String getErrorMessage(int errorCode)
    {
        String result = "BASS_ERROR_UNKNOWN";
        switch (errorCode)
        {
            // case BASS.BASS_OK:
            //    result = "BASS_OK";
            case BASS.BASS_ERROR_MEM:
                result = "BASS_ERROR_MEM";
            case BASS.BASS_ERROR_FILEOPEN:
                result = "BASS_ERROR_FILEOPEN";
            case BASS.BASS_ERROR_DRIVER:
                result = "BASS_ERROR_DRIVER";
            case BASS.BASS_ERROR_BUFLOST:
                result = "BASS_ERROR_BUFLOST";
            case BASS.BASS_ERROR_HANDLE:
                result = "BASS_ERROR_HANDLE";
            case BASS.BASS_ERROR_FORMAT:
                result = "BASS_ERROR_FORMAT";
            case BASS.BASS_ERROR_POSITION:
                result = "BASS_ERROR_POSITION";
            case BASS.BASS_ERROR_INIT:
                result = "BASS_ERROR_INIT";
            case BASS.BASS_ERROR_START:
                result = "BASS_ERROR_START";
            case BASS.BASS_ERROR_SSL:
                result = "BASS_ERROR_SSL";
            case BASS.BASS_ERROR_ALREADY:
                result = "BASS_ERROR_ALREADY";
            case BASS.BASS_ERROR_NOCHAN:
                result = "BASS_ERROR_NOCHAN";
            case BASS.BASS_ERROR_ILLTYPE:
                result = "BASS_ERROR_ILLTYPE";
            case BASS.BASS_ERROR_ILLPARAM:
                result = "BASS_ERROR_ILLPARAM";
            case BASS.BASS_ERROR_NO3D:
                result = "BASS_ERROR_NO3D";
            case BASS.BASS_ERROR_NOEAX:
                result = "BASS_ERROR_NOEAX";
            case BASS.BASS_ERROR_DEVICE:
                result = "BASS_ERROR_DEVICE";
            case BASS.BASS_ERROR_NOPLAY:
                result = "BASS_ERROR_NOPLAY";
            case BASS.BASS_ERROR_FREQ:
                result = "BASS_ERROR_FREQ";
            case BASS.BASS_ERROR_NOTFILE:
                result = "BASS_ERROR_NOTFILE";
            case BASS.BASS_ERROR_NOHW:
                result = "BASS_ERROR_NOHW";
            case BASS.BASS_ERROR_EMPTY:
                result = "BASS_ERROR_EMPTY";
            case BASS.BASS_ERROR_NONET:
                result = "BASS_ERROR_NONET";
            case BASS.BASS_ERROR_CREATE:
                result = "BASS_ERROR_CREATE";
            case BASS.BASS_ERROR_NOFX:
                result = "BASS_ERROR_NOFX";
            case BASS.BASS_ERROR_NOTAVAIL:
                result = "BASS_ERROR_NOTAVAIL";
            case BASS.BASS_ERROR_DECODE:
                result = "BASS_ERROR_DECODE";
            case BASS.BASS_ERROR_DX:
                result = "BASS_ERROR_DX";
            case BASS.BASS_ERROR_TIMEOUT:
                result = "BASS_ERROR_TIMEOUT";
            case BASS.BASS_ERROR_FILEFORM:
                result = "BASS_ERROR_FILEFORM";
            case BASS.BASS_ERROR_SPEAKER:
                result = "BASS_ERROR_SPEAKER";
            case BASS.BASS_ERROR_VERSION:
                result = "BASS_ERROR_VERSION";
            case BASS.BASS_ERROR_CODEC:
                result = "BASS_ERROR_CODEC";
            case BASS.BASS_ERROR_ENDED:
                result = "BASS_ERROR_ENDED";
            case BASS.BASS_ERROR_BUSY:
                result = "BASS_ERROR_BUSY";
        }
        return result;
    }
}
