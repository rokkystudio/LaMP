package com.rokkystudio.lamp.media;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rokkystudio.lamp.Track;

public class MediaDatabase extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION  =  2;
    private static final String DATABASE_NAME  =  "LAMP.db";

    public static final String TABLE_FILE      =  "file";
    public static final String FILE_ID         =  "id";
    public static final String FILE_PATH       =  "path";
    public static final String FILE_TRACK      =  "track";

    public static final String TABLE_TRACK     =  "track";
    public static final String TRACK_ID        =  "id";
    public static final String TRACK_HASH      =  "hash";
    public static final String TRACK_ARTIST    =  "artist";
    public static final String TRACK_TITLE     =  "title";
    public static final String TRACK_COVER     =  "cover";
    public static final String TRACK_DURATION  =  "duration";
    public static final String TRACK_LISTENED  =  "listened";
    public static final String TRACK_HIDDEN    =  "hidden";


    public MediaDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(
            "CREATE TABLE " + TABLE_FILE + "(" +
            FILE_ID + " INTEGER PRIMARY KEY, " +
            FILE_PATH + " TEXT, " +
            FILE_TRACK + " INTEGER," +
            "FOREIGN KEY(" + FILE_TRACK + ") REFERENCES " + TABLE_TRACK + "(" + TRACK_ID + ") )"
        );

        db.execSQL(
            "CREATE TABLE " + TABLE_TRACK + "(" +
            TRACK_ID + " INTEGER PRIMARY KEY, " +
            TRACK_ARTIST + " TEXT, " +
            TRACK_TITLE + " TEXT, " +
            TRACK_COVER + " BLOB, " +
            TRACK_DURATION + " INTEGER, " +
            TRACK_LISTENED + " INTEGER, " +
            TRACK_HIDDEN + " BOOLEAN, " +
            TRACK_HASH + " TEXT )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACK);
        onCreate(db);
    }

    public void syncTrack(Track track)
    {
        SQLiteDatabase db = getWritableDatabase();
        //Cursor cursor = db.query(TABLE_TRACK, null, TRACK_PATH + " == " + track.getPath(), null, null, null, null, "1");
        //cursor.close();
    }

    public boolean isTrackExist(Track track) {
        SQLiteDatabase db = getReadableDatabase();
        //Cursor cursor = db.query(TABLE_TRACK, null, TRACK_PATH + " == " + track.getPath(), null, null, null, null, "1");
        //boolean result = cursor.getCount() > 0;
        //cursor.close();
        return false;
    }

    public Track getTrackByHash(String hash) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_TRACK, null, TRACK_HASH + " == " + hash, null, null, null, null, "1");
        cursor.moveToFirst();
        //cursor.getString()
        //Track track = new Track()
        cursor.close();
        return null; // TODO
    }

    private Track getTrackFromCursor(Cursor cursor)
    {
        int columnId       = cursor.getColumnIndex(TRACK_ID);
        int columnArtist   = cursor.getColumnIndex(TRACK_ARTIST);
        int columnTitle    = cursor.getColumnIndex(TRACK_TITLE);
        int columnCover    = cursor.getColumnIndex(TRACK_COVER);
        int columnDuration = cursor.getColumnIndex(TRACK_DURATION);
        int columnHidden   = cursor.getColumnIndex(TRACK_HIDDEN);
        int columnListened = cursor.getColumnIndex(TRACK_LISTENED);
        int columnHash     = cursor.getColumnIndex(TRACK_HASH);

        int id = cursor.getInt(columnId);
        String artist = cursor.getString(columnArtist);
        String title = cursor.getString(columnTitle);
        byte[] cover = cursor.getBlob(columnCover);
        int duration = cursor.getInt(columnDuration);
        boolean hidden = cursor.getInt(columnHidden) > 0;
        int listened = cursor.getInt(columnListened);
        String hash = cursor.getString(columnHash);

        return new Track(artist, title, cover, duration, hidden, listened, hash, null);
    }
}