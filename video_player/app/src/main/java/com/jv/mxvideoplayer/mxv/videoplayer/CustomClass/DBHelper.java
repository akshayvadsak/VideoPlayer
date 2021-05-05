package com.jv.mxvideoplayer.mxv.videoplayer.CustomClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;
import java.io.File;
import com.jv.mxvideoplayer.mxv.videoplayer.model.PrivateVideo;
import com.jv.mxvideoplayer.mxv.videoplayer.model.Video;

/**
 * Created by jigs patel on 30-12-2019.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String KEY_ID = MediaStore.Files.FileColumns._ID;
    public static final String MEDIA_ID = "media" + MediaStore.Files.FileColumns._ID;
    public static final String MEDIA_SIZE = "media" + MediaStore.MediaColumns.SIZE;
    public static final String MEDIA_DURATION = MediaStore.Video.VideoColumns.DURATION;
    public static final String MEDIA_RESOLUTION = MediaStore.Video.VideoColumns.RESOLUTION;
    public static final String MEDIA_DATE_TAKEN = MediaStore.Video.VideoColumns.DATE_TAKEN;
    public static final String MEDIA_BUCKET_ID = MediaStore.Video.VideoColumns.BUCKET_ID;
    public static final String MEDIA_BUCKET_DISPLAY_NAME = MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME;
    public static final String MEDIA_PATH = MediaStore.Files.FileColumns.DATA;
    public static final String MEDIA_NEW_PATH = "new" + MediaStore.Files.FileColumns.DATA;

    private static final String DB_NAME = "VideoPlayer";
    private static final int DB_VERSION = 1;
    private static final String TABLE_PRIVATE = "privateMedia";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_PRIVATE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MEDIA_ID + " INTEGER UNIQUE,"
            + MEDIA_SIZE + " INTEGER,"
            + MEDIA_DURATION + " INTEGER,"
            + MEDIA_RESOLUTION + " TEXT,"
            + MEDIA_DATE_TAKEN + " TEXT,"
            + MEDIA_BUCKET_ID + " TEXT,"
            + MEDIA_BUCKET_DISPLAY_NAME + " TEXT,"
            + MEDIA_PATH + " TEXT,"
            + MEDIA_NEW_PATH + " TEXT)";

    public DBHelper(Context context) {
        super(context, Constants.DatabasePath + DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addToPrivate(Video video, String newPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MEDIA_ID, video.getId());
        values.put(MEDIA_SIZE, video.getSize());
        values.put(MEDIA_DURATION, video.getDuration());
        values.put(MEDIA_RESOLUTION, video.getResolution());
        values.put(MEDIA_DATE_TAKEN, video.getDateTaken());
        values.put(MEDIA_BUCKET_ID, video.getBucketID());
        values.put(MEDIA_BUCKET_DISPLAY_NAME, video.getBucketDisplayName());
        values.put(MEDIA_PATH, video.getData());
        values.put(MEDIA_NEW_PATH, newPath);

        db.insert(TABLE_PRIVATE, null, values);
        db.close();
    }

    public void removeFromPrivate(int mediaID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRIVATE, MEDIA_ID + "=" + mediaID, null);
        db.close();
    }

    public void getPrivateVideoData() {
        Constants.PRIVATE_VIDEO_LIST.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_PRIVATE, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    PrivateVideo video = new PrivateVideo();

                    video.setId(cursor.getInt(cursor.getColumnIndex(MEDIA_ID)));
                    video.setSize(cursor.getLong(cursor.getColumnIndex(MEDIA_SIZE)));
                    video.setDuration(cursor.getLong(cursor.getColumnIndex(MEDIA_DURATION)));
                    video.setResolution(cursor.getString(cursor.getColumnIndex(MEDIA_RESOLUTION)));
                    video.setDateTaken(cursor.getLong(cursor.getColumnIndex(MEDIA_DATE_TAKEN)));
                    video.setBucketID(cursor.getString(cursor.getColumnIndex(MEDIA_BUCKET_ID)));
                    video.setBucketDisplayName(cursor.getString(cursor.getColumnIndex(MEDIA_BUCKET_DISPLAY_NAME)));
                    video.setData(cursor.getString(cursor.getColumnIndex(MEDIA_PATH)));
                    video.setNewPath(cursor.getString(cursor.getColumnIndex(MEDIA_NEW_PATH)));

                    File file = new File(video.getNewPath());
                    if (file.exists()) {
                        Constants.PRIVATE_VIDEO_LIST.add(video);
                    }
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }

    }
}
