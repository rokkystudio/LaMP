package com.rokkystudio.lamp;

import android.util.Log;

import com.un4seen.bass.BASS;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Playlist extends ArrayList<Track>
{
    public Playlist() {

    }

    public Playlist(List<File> files)
    {
        BASS.BASS_SAMPLE info = new BASS.BASS_SAMPLE();

        for (File file : files) {
            int stream = BASS.BASS_StreamCreateFile(file.getAbsolutePath(), 0, 0, 0);
            BASS.BASS_SampleGetInfo(stream, info);
            Log.d("", info.toString());
        }
    }
}
