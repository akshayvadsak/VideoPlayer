package com.jv.mxvideoplayer.mxv.videoplayer.CustomClass;

import android.os.Environment;

import java.util.ArrayList;

import com.jv.mxvideoplayer.mxv.videoplayer.model.Folder;
import com.jv.mxvideoplayer.mxv.videoplayer.model.PrivateVideo;
import com.jv.mxvideoplayer.mxv.videoplayer.model.Video;
/**
 * Created by jigs patel on 08-12-2019.
 */

public class Constants {

    public static final int APP_ID = 147;

    public final static int REQUEST_CODE_PERMISSION_WRITE = 0x101;

    public final static String INT_VIDEO_POSITION = "INT_VIDEO_POSITION";

    public static String DatabasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + ".VideoPlayer/";

    public static ArrayList<Video> MEDIA_LIST = new ArrayList<>();
    public static ArrayList<Folder> FOLDER_LIST = new ArrayList<>();
    public static ArrayList<Video> VIDEO_LIST = new ArrayList<>();
    public static ArrayList<PrivateVideo> PRIVATE_VIDEO_LIST = new ArrayList<com.jv.mxvideoplayer.mxv.videoplayer.model.PrivateVideo>();

    public static final String GOOGLE_TEST_DEVICE = "";


}
