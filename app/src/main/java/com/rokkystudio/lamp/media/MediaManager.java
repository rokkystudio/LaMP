package com.rokkystudio.lamp.media;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.rokkystudio.lamp.Playlist;
import com.un4seen.bass.BASS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.un4seen.bass.BASS.BASS_DEVICE_LATENCY;

public class MediaManager
{
    // По умолчанию имя плейлиста берется из имени папки
    private static final int SORTING_NORMAL  = 0;
    private static final int SORTING_FOLDER  = 1;
    private static final int SORTING_POPULAR = 2;
    private static final int SORTING_ARTIST  = 3;
    private static final int SORTING_CUSTOM  = 4;

    private Context mContext;
    private MediaDatabase mMediaDatabase;
    private MediaManagerListener mMediaManagerListener = null;

    public MediaManager(Context context) {
        BASS.BASS_Init(-1, 44100, BASS_DEVICE_LATENCY);
        mMediaDatabase = new MediaDatabase(context);
        mContext = context;
    }

    /**
     *  Синхронизация базы данных с файловой системой
     */
    public void syncMedia() {
        new SyncMediaTask(mMediaDatabase).execute();
    }

    public List<Playlist> getPlaylists(int sorting) {
        // TODO Фильтровать файлы меньше 500кб и короче 30 сек
        return null;
    }

    public void setMediaManagerListener(MediaManagerListener listener) {
        mMediaManagerListener = listener;
    }

    public interface MediaManagerListener {
        void onMediaSyncStart();
        void onMediaSyncFinish();
        void onMediaLocationChanged();
    }

    /*****************************************************************************************/
    /*****************************************************************************************/
    /*****************************************************************************************/

    private static class SyncMediaTask extends AsyncTask<Void, Void, Void>
    {
        private SQLiteDatabase mSQLiteDatabase;

        SyncMediaTask(MediaDatabase mediaDatabase) {
            mSQLiteDatabase = mediaDatabase.getWritableDatabase();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            // Сканируем все
            HashSet<String> mediaFiles = new HashSet<>();
            for (String storagePath : getStorageList())
            {
                Log.d("PATH", storagePath);
                List<String> list = scanMediaPath(storagePath);
                mediaFiles.addAll(list);
            }

            for (String path : mediaFiles) {
                syncTrack(path);
            }
            return null;
        }

        private void syncTrack(String path)
        {
            Log.d("TRACK", path);
            try {
                String hash = getSHA1(path);
                Cursor cursor = mSQLiteDatabase.query(MediaDatabase.TABLE_TRACK, null,
                    MediaDatabase.TRACK_HASH + " = ?", new String[] { hash }, null, null, null, "1");

                if (cursor.getCount() > 0) {
                    // Update existing track in database
                    cursor.moveToFirst();
                    //cursor
                } else {
                    // Add new track to database
                    //cursor.getDouble();
                    cursor.close();
                }

                cursor.close();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     *  Возвращает список всех найденных медиафайлов
     *  в указанной и вложенных директориях
     */
    private static List<String> scanMediaPath(String path)
    {
        List<String> result = new ArrayList<>();
        File objects[] = new File(path).listFiles();
        if (objects == null || objects.length == 0) return result;

        for (File object : objects)
        {
            // Пропускаем скрытые объекты и ( . | .. )
            if (object.getName().startsWith(".")) continue;

            // Сканируем вложенные директории рекурсивно
            if (object.isDirectory()) {
                result.addAll(scanMediaPath(object.getAbsolutePath()));
            }

            if (isMediaFile(object)) {
                result.add(object.getAbsolutePath());
            }
        }

        return result;
    }

    /**
     *  Является ли указанный файл медиафайлом.
     *  На этом этапе нельзя фильтровать список.
     *  Фильтры работают во время запроса плейлиста.
     */
    private static boolean isMediaFile(File object)
    {
        if (!object.isFile()) return false;

        String filename = object.getName().toLowerCase();
        if (filename.endsWith(".aiff")) return true;
        if (filename.endsWith(".mp2"))  return true;
        if (filename.endsWith(".mp3"))  return true;
        if (filename.endsWith(".ogg"))  return true;
        if (filename.endsWith(".flac")) return true;
        if (filename.endsWith(".ape"))  return true;
        if (filename.endsWith(".m4a"))  return true;
        if (filename.endsWith(".mp4"))  return true;
        if (filename.endsWith(".m4p"))  return true;
        if (filename.endsWith(".aac"))  return true;
        if (filename.endsWith(".ac3"))  return true;
        if (filename.endsWith(".dts"))  return true;
        if (filename.endsWith(".mpc"))  return true;
        if (filename.endsWith(".wma"))  return true;
        if (filename.endsWith(".wv"))   return true;

        return false;
    }

    /**
     *  Возвращает список всех монтированных устройств в системе
     */
    private static List<String> getStorageList()
    {
        List<String> result = new ArrayList<>();
        try {
            File mounts = new File("/proc/mounts");
            BufferedReader reader = new BufferedReader(new FileReader(mounts));

            String line;
            while ((line = reader.readLine()) != null)
            {
                String path = line.split("\\s+")[1];
                if (path == null) continue;
                if (path.equals("/")) continue;
                if (path.startsWith("/acct")) continue;
                if (path.startsWith("/cust")) continue;
                if (path.startsWith("/dev")) continue;
                if (path.startsWith("/proc")) continue;
                if (path.startsWith("/sys")) continue;
                if (path.startsWith("/config")) continue;

                if (path.startsWith("/")) {
                    result.add(path); // mount_point
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String getSHA1(String filename) throws IOException, NoSuchAlgorithmException
    {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        try (InputStream input = new FileInputStream(new File(filename)))
        {
            byte[] buffer = new byte[8192];
            int length = input.read(buffer);

            while (length != -1) {
                sha1.update(buffer, 0, length);
                length = input.read(buffer);
            }
        }

        StringBuilder builder = new StringBuilder();
        for (byte b : sha1.digest()) {
            builder.append(String.format("%02X", b));
        }
        return builder.toString();
    }
}
