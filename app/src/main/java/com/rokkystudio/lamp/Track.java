package com.rokkystudio.lamp;

import android.graphics.Bitmap;

import com.un4seen.bass.BASS;

import java.util.Locale;

import static com.un4seen.bass.BASS.BASS_POS_BYTE;

public class Track
{
    private String mArtist;
    private String mTitle;
    private byte[] mCover;
    private int mDuration;
    private boolean mHidden;
    private int mListened;
    private String mHash;
    private String mPath;

    public Track(String artist, String title, byte[] cover, int duration, boolean hidden, int listened, String hash, String path) {
        mArtist = artist; mTitle = title; mCover = cover; mDuration = duration; mHidden = hidden; mListened = listened; mHash = hash; mPath = path;
    }

    public String getArtist() {
        return mArtist;
    }

    public String getTitle() {
        return mTitle;
    }

    public byte[] getCover() {
        return mCover;
    }

    public int getDuration() { return mDuration; }

    public boolean isHidden() { return mHidden; }

    /**
     *  Общее время прослушивания данного трека
     */
    public int getListened() { return mListened; }

    public String getHash() {
        return mHash;
    }

    public String getPath() {
        return mPath;
    }

    public Bitmap getPreview()
    {
        /*
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.
        int channel = BASS.BASS_StreamCreateFile(mPath, 0, 0, 0);
        BASS.BASS_ChannelGetData(channel, buffer, BASS_DATA_FFT2048);
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.getPaint().setColor(0xff74AC23);
        drawable.draw();

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        Canvas canvas = new Canvas();
        canvas.drawLines(buffer.array(), paint);

        buffer.array()
        */
        // TODO Возвращать только готовое изображение.
        // TODO Загрузку изображения делать в отдельном классе загрузчике
        return null;
    }

    /*
    public String getTime()
    {
        if (!mTimeLength.isEmpty()) {
            return mTimeLength;
        }

        int channel = BASS.BASS_StreamCreateFile(mPath, 0, 0, 0);
        long bytesLength = BASS.BASS_ChannelGetLength(channel, BASS_POS_BYTE);
        int timeLength = (int) BASS.BASS_ChannelBytes2Seconds(channel, bytesLength);
        BASS.BASS_StreamFree(channel);

        int hours = timeLength / 3600;
        int minutes = (timeLength % 3600) / 60;
        int seconds = timeLength % 60;

        if (hours > 0) {
            mTimeLength = String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            mTimeLength = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        }
        return mTimeLength;
    }
    */
}
