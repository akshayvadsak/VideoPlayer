package com.jv.mxvideoplayer.mxv.videoplayer.CustomClass;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.jv.mxvideoplayer.mxv.videoplayer.R;
import com.jv.mxvideoplayer.mxv.videoplayer.model.Folder;
import com.jv.mxvideoplayer.mxv.videoplayer.model.Video;

import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.Constants.FOLDER_LIST;
import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.Constants.MEDIA_LIST;
import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.Constants.VIDEO_LIST;


/**
 * Created by keval on 08-12-2019.
 */

public class AppUtils {

    private static int bytesRead;
    private static int bytesAvailable;
    private static int bufferSize;
    private static byte[] buffer;
    private static int maxBufferSize = 1024 * 1024;

    /* Log.i */
    public static void LOG(String message, Object... args) {
        try {
            if (args != null)
                message = String.format(message, args);
            Log.i("VideoPlayer", message);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    /* Convert dp to px */
    public static float DPtoPX(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    /* Get file size */
    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        if (digitGroups < 3) {
            return new DecimalFormat("###0").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
        } else {
            return new DecimalFormat("###0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
        }
    }

    /* Get formatted time from duration */
    public static String FormatTimeForDisplay(long ms) {

        long s = (ms / 1000) % 60;
        long m = ms / (1000 * 60) % 60;
        long h = ms / (1000 * 60 * 60);

        String sString = s < 10 ? "0" + s : "" + s;
        String mString = m < 10 ? "0" + m : "" + m;
        String hString = "" + h;

        String durationString = String.format(Locale.US, "%s:%s", mString, sString);
        if (h != 0) {
            durationString = String.format(Locale.US, "%s:%s:%s", hString, mString, sString);
        }

        return durationString;
    }

    /* Get formatted time from duration */
    public static String getDurationString(long durationMs, boolean negativePrefix) {
        long hours = TimeUnit.MILLISECONDS.toHours(durationMs);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs);
        if (hours > 0) {
            return String.format(Locale.getDefault(), "%s%02d:%02d:%02d",
                    negativePrefix ? "-" : "",
                    hours,
                    minutes - TimeUnit.HOURS.toMinutes(hours),
                    seconds - TimeUnit.MINUTES.toSeconds(minutes));
        }
        return String.format(Locale.getDefault(), "%s%02d:%02d",
                negativePrefix ? "-" : "",
                minutes,
                seconds - TimeUnit.MINUTES.toSeconds(minutes)
        );
    }

    /* Get video info */
    public static void fetchVideoData(Context context) {
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.SIZE,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.RESOLUTION,
                MediaStore.Video.VideoColumns.DATE_MODIFIED,
                MediaStore.Video.VideoColumns.BUCKET_ID,
                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME
        };

        Uri dataUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//        Uri dataUri = MediaStore.Files.getContentUri("external");
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                + " OR " + MediaStore.MediaColumns.MIME_TYPE + "='audio/x-matroska'";
        Cursor cursor=null;
        try {
            cursor = context.getContentResolver().query(dataUri, projection, null, null, null);

            if (cursor.moveToFirst()) {
                MEDIA_LIST.clear();
                File file;
                while (!cursor.isAfterLast()) {
                    file = new File(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)));
                    if (file.exists()) {
                        MEDIA_LIST.add(new Video(
                                cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)),
                                cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)),
                                cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)),
                                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)),
                                cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION)),
                                cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.RESOLUTION)),
                                cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED)),
                                cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_ID)),
                                cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME))));
                    }
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
    }

    /* Get video album*/
    public static void fetchAlbumData(Context context) {
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Video.VideoColumns.BUCKET_ID,
                MediaStore.MediaColumns.SIZE};
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//        Uri uri = MediaStore.Files.getContentUri("external");
        String selection = "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                + " OR " + MediaStore.MediaColumns.MIME_TYPE + "='audio/x-matroska')";

        FOLDER_LIST.clear();
        HashSet<String> folderHashSet = new HashSet<>();
        Cursor cursor = null;
        long size;
        for (Video video : MEDIA_LIST) {
            if (!folderHashSet.contains(video.getBucketID())) {
                size = 0L;
                try {
                    cursor = context.getContentResolver().query(uri, projection,
                            MediaStore.Video.VideoColumns.BUCKET_ID + "='" + video.getBucketID() + "'", null, null);
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        size += cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE));
                        cursor.moveToNext();
                    }
                    File file = new File(video.getData());
                    FOLDER_LIST.add(new Folder(video.getId(), video.getBucketID(), video.getBucketDisplayName(), cursor.getCount(), size, file.getParent()));
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                folderHashSet.add(video.getBucketID());
            }
        }

        Collections.sort(FOLDER_LIST, new Comparator<Folder>() {
            @Override
            public int compare(Folder videoFolder, Folder t1) {
                try{

                    return videoFolder.getBucketDisplayName().compareToIgnoreCase(t1.getBucketDisplayName());
                }catch (Exception e){
                    return 1;

                }
            }
        });
    }

    /* Delete file */
    public static boolean deleteFile(Context context, int id, String location) {
        if (location != null) {
            File file = new File(location);
            if (file.exists()) {
                context.getContentResolver().delete(MediaStore.Files.getContentUri("external"),
                        MediaStore.Files.FileColumns._ID + "=" + id, null);
                if (file.exists()) {
                    if (file.delete())
                        return true;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    /* Delete private file*/
    public static boolean deletePrivateFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.delete()) {
                return true;
            }
        }
        return false;
    }

    /* Refresh mediaStore*/
    public static void refreshMediaStore(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);

        } else {
            MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }
    }

    /* Rename file */
    public static String renameFile(Context context, Video video, String rename) {
        File from = new File(video.getData());
        File to = new File(from.getParent() + File.separator + rename);
        if (from.exists()) {
            if (!to.exists()) {
                if (from.renameTo(to)) {
                    if (to.exists()) {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Files.FileColumns.DATA, to.getAbsolutePath());
                        context.getContentResolver().update(MediaStore.Files.getContentUri("external"),
                                values, MediaStore.Files.FileColumns._ID + "=" + video.getId(), null);
                        Toast.makeText(context, "Rename successfully", Toast.LENGTH_SHORT).show();
                        return to.getAbsolutePath();
                    }
                }
            }
        }
        return null;
    }

    /* Share Multiple file */
    public static void shareMultipleFile(Context context, ArrayList arrayList) {
        if (arrayList != null) {
            Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("video/*");
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayList);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /* Copy file from-to*/
    public static boolean copyFileToOther(String from, String to) {

        try {
            File fromFile = new File(from);
            File toFile = new File(to);

            FileOutputStream fos = new FileOutputStream(toFile);
            DataOutputStream dos = new DataOutputStream(fos);
            FileInputStream fileInputStream = new FileInputStream(fromFile);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            fileInputStream.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* Get media codec info */
    public static void getCodecInfo() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                MediaCodecList codecList = new MediaCodecList(MediaCodecList.ALL_CODECS);
                MediaCodecInfo[] mediaCodecInfos = codecList.getCodecInfos();
                StringBuilder builder = new StringBuilder();
                for (MediaCodecInfo info : mediaCodecInfos) {
                    builder.append(info.getName());
                    builder.append(" - ");
                    builder.append(Arrays.toString(info.getSupportedTypes()));
                    builder.append("\n");
                }
                Log.i("MediaCodec", builder.toString());
                builder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Typeface getTextTypeFace(Context context) {
        int i = Integer.parseInt(SharedPref.getSharedPrefData(context, SharedPref.textTypeFace, "1"));
        switch (i) {
            case 0: {
                return (Typeface.DEFAULT);
            }
            case 1: {
                return (Typeface.DEFAULT_BOLD);
            }
            case 2: {
                return (Typeface.SANS_SERIF);
            }
            case 3: {
                return (Typeface.SERIF);
            }
            case 4: {
                return (Typeface.MONOSPACE);
            }
            default:
                return (Typeface.DEFAULT);
        }
    }

    public static void sortVideo(Context context) {
        String sortType = SharedPref.getSharedPrefData(context, SharedPref.sortType,
                context.getResources().getString(R.string.title));
        try {
            if (sortType.equalsIgnoreCase(context.getResources().getString(R.string.title))) {
                sortByName();
            } else if (sortType.equalsIgnoreCase(context.getResources().getString(R.string.duration))) {
                sortByDuration();
            } else if (sortType.equalsIgnoreCase(context.getResources().getString(R.string.time))) {
                sortByTime();
            } else if (sortType.equalsIgnoreCase(context.getResources().getString(R.string.size))) {
                sortBySize();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void sortByName() {
        Collections.sort(VIDEO_LIST, new Comparator<Video>() {
            @Override
            public int compare(Video o1, Video o2) {
                File file1 = new File(o1.getData());
                File file2 = new File(o2.getData());
                return file1.getName().compareToIgnoreCase(file2.getName());
            }
        });
    }

    private static void sortBySize() {
        Collections.sort(VIDEO_LIST, new Comparator<Video>() {
            @Override
            public int compare(Video o1, Video o2) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return Long.compare(o1.getSize(), o2.getSize());
                } else {
                    return ((Long) o1.getSize()).compareTo(o2.getSize());
                }
            }
        });
    }

    private static void sortByTime() {
        Collections.sort(VIDEO_LIST, new Comparator<Video>() {
            @Override
            public int compare(Video o2, Video o1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return Long.compare(o1.getDateTaken(), o2.getDateTaken());
                } else {
                    return ((Long) o1.getDateTaken()).compareTo(o2.getDateTaken());
                }
            }
        });
    }

    private static void sortByDuration() {
        Collections.sort(VIDEO_LIST, new Comparator<Video>() {
            @Override
            public int compare(Video o1, Video o2) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return Long.compare(o1.getDuration(), o2.getDuration());
                } else {
                    return ((Long) o1.getDuration()).compareTo(o2.getDuration());
                }
            }
        });
    }

    public enum SortOrder {
        ASCENDING, DESCENDING
    }
}
