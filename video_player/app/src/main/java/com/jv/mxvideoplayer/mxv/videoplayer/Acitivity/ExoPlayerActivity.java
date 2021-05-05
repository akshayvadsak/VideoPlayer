package com.jv.mxvideoplayer.mxv.videoplayer.Acitivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.CaptionStyleCompat;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;

//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.InterstitialAd;
import com.jv.mxvideoplayer.mxv.videoplayer.BuildConfig;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.AppUtils;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.Constants;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.DialogDismiss;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.FileUriUtils;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref;
import com.jv.mxvideoplayer.mxv.videoplayer.MainFragment;
import com.jv.mxvideoplayer.mxv.videoplayer.R;
import com.jv.mxvideoplayer.mxv.videoplayer.application.MyApp;
import com.jv.mxvideoplayer.mxv.videoplayer.exoplayer.AspectRatioFrameLayout;
import com.jv.mxvideoplayer.mxv.videoplayer.exoplayer.SubtitleView;
import com.jv.mxvideoplayer.mxv.videoplayer.exoplayer.TimeBar;
import com.jv.mxvideoplayer.mxv.videoplayer.exoplayer.TrackSelectionHelper;
import com.jv.mxvideoplayer.mxv.videoplayer.model.Video;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.Constants.VIDEO_LIST;



public class ExoPlayerActivity extends AppCompatActivity implements View.OnClickListener, AudioManager.OnAudioFocusChangeListener,
        TextRenderer.Output, SimpleExoPlayer.VideoListener, Player.EventListener, TimeBar.OnScrubListener {

    private static final ControlDispatcher DEFAULT_CONTROL_DISPATCHER = new ControlDispatcher() {

        @Override
        public boolean dispatchSetPlayWhenReady(Player player, boolean playWhenReady) {
            player.setPlayWhenReady(playWhenReady);
            return true;
        }

        @Override
        public boolean dispatchSeekTo(Player player, int windowIndex, long positionMs) {
            player.seekTo(windowIndex, positionMs);
            return true;
        }

        @Override
        public boolean dispatchSetRepeatMode(Player player, @Player.RepeatMode int repeatMode) {
            player.setRepeatMode(repeatMode);
            return true;
        }

    };

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    private Activity activity = ExoPlayerActivity.this;
    private int position;
    private File videoSourceFile;
    private AspectRatioFrameLayout contentFrame;
    private SurfaceView surfaceView;
    private LinearLayout shutterView;
    private SubtitleView subtitleView;
    private FrameLayout overlayFrameLayout;
    private TextView progressTextView;
    private ImageView imgIndicator;
    private Window mWindow;
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener afChangeListener;

    private int audioManagerResult;
    private Drawable indVolumeDrawable;
    private Drawable indBrightnessDrawable;

    private int mInitialViewWidth;
    private int mInitialViewHeight;

    private SimpleExoPlayer player;
    private boolean inErrorState;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private TrackSelectionHelper trackSelectionHelper;
    private TrackGroupArray lastSeenTrackGroupArray;

    private boolean shouldAutoPlay;
    private int resumeWindow;
    private long resumePosition;

    // Player Control

    private boolean isLock = false;
    private View unLockButton;
    private View lockButton;
    private View rotateButton;
    private View sharebutton;
    private View muteButton;
    private LinearLayout llControlRoot;
    private View backButton;
    private TextView titleView;
    private View audioTrackButton;
    private View subtitleTrackButton;
    private View previousButton;
    private View nextButton;
    private View playButton;
    private View pauseButton;
    private View cutnextButton;
    private View cutpreButton;
    private TimeBar timeBar;
    private TextView positionView, durationView;
    private boolean scrubbing;
    private ControlDispatcher controlDispatcher;

    private ImageView scaleButton;
    private TextView scaleModeTextView;
    private Drawable scaleFitButtonDrawable;
    private Drawable scaleWidthButtonDrawable;
    private Drawable scaleHeightButtonDrawable;
    private Drawable scaleFillButtonDrawable;
    private Drawable scaleZoomButtonDrawable;

    private String scaleFitContentDescription;
    private String scaleWidthContentDescription;
    private String scaleHeightContentDescription;
    private String scaleFillContentDescription;
    private String scaleZoomContentDescription;

    private StringBuilder formatBuilder;
    private Formatter formatter;
    private boolean multiWindowTimeBar;
    private Timeline.Window window;

    private long hideAtMs;
    private int showTimeoutMs = 3000;

    // Mute
    private boolean isMute = false;
    private float mCurrentVolume;

    private int seekForwardTime = 10 * 1000; // default 10 second
    private int seekBackwardTime = 10 * 1000; // default 10 second

    private Runnable updateProgressAction = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };
    private Runnable hideAction = new Runnable() {
        @Override
        public void run() {
            hideControl();
        }
    };

    public void setMute(float vol) {
        player.setVolume(vol);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shouldAutoPlay = true;
        clearResumePosition();
        mediaDataSourceFactory = buildDataSourceFactory(true);

        setContentView(R.layout.activity_exo_player);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(activity, android.R.color.black));
        }
        AppUtils.LOG("onCreate");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try {
            position = getIntent().getExtras().getInt(Constants.INT_VIDEO_POSITION, 0);
        } catch (Exception e) {
            try {
                e.printStackTrace();
                Intent intent = getIntent();
                AppUtils.LOG(intent.toString());
                String filePath = FileUriUtils.getFilePathFromUri(activity, intent.getData());
                AppUtils.LOG(filePath);
                position = 0;
                VIDEO_LIST.clear();
                VIDEO_LIST.add(new Video(filePath));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        mWindow = getWindow();
        audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);

        audioManagerResult = audioManager != null ? audioManager.requestAudioFocus(afChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN) : 0;


        WindowManager.LayoutParams layout = mWindow.getAttributes();
        layout.screenBrightness = Float.valueOf(SharedPref.getSharedPrefData(activity, SharedPref.playerBrightness, "10")) / 100;
        mWindow.setAttributes(layout);

        bindControl();
        bindPlayerControl();

        mInitialViewWidth = getResources().getDisplayMetrics().widthPixels;
        mInitialViewHeight = getResources().getDisplayMetrics().heightPixels;

        overlayFrameLayout.setOnTouchListener(new OnSwipeTouchListener() {
            float diffTime = -1, finalTime = -1;
            int startVolume;
            int maxVolume;
            int startBrightness;
            int maxBrightness;

            @Override
            public void onMove(Direction dir, float diff) {
                if (!isLock) {
                    if (dir == Direction.LEFT || dir == Direction.RIGHT) {
                        if (player.getDuration() <= 60) {
                            diffTime = (float) player.getDuration() * diff / ((float) mInitialViewWidth);
                        } else {
                            diffTime = (float) 60000 * diff / ((float) mInitialViewWidth);
                        }
                        if (dir == Direction.LEFT) {
                            diffTime *= -1;
                        }
                        finalTime = player.getCurrentPosition() + diffTime;
                        if (finalTime < 0) {
                            finalTime = 0;
                        } else if (finalTime > player.getDuration()) {
                            finalTime = player.getDuration();
                        }
                        diffTime = finalTime - player.getCurrentPosition();
                        String progressText =
                                AppUtils.getDurationString((long) finalTime, false) +
                                        " [" + (dir == Direction.LEFT ? "-" : "+") +
                                        AppUtils.getDurationString((long) Math.abs(diffTime), false) +
                                        "]";
                        progressTextView.setText(progressText);
                    } else {
                        finalTime = -1;
                        if (initialX >= mInitialViewWidth / 2 || mWindow == null) {
                            float diffVolume;
                            int finalVolume;

                            diffVolume = (float) maxVolume * diff / ((float) mInitialViewHeight / 2);
                            if (dir == Direction.DOWN) {
                                diffVolume = -diffVolume;
                            }
                            finalVolume = startVolume + (int) diffVolume;
                            if (finalVolume < 0)
                                finalVolume = 0;
                            else if (finalVolume > maxVolume)
                                finalVolume = maxVolume;

                            String progressText = "\t" + finalVolume;
                            if (imgIndicator.getDrawable() == null) {
                                imgIndicator.setImageDrawable(indVolumeDrawable);
                            }
                            progressTextView.setText(progressText);
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, finalVolume, 0);
                        } else if (initialX < mInitialViewWidth / 2) {
                            float diffBrightness;
                            int finalBrightness;

                            diffBrightness = (float) maxBrightness * diff / ((float) mInitialViewHeight / 2);
                            if (dir == Direction.DOWN) {
                                diffBrightness = -diffBrightness;
                            }
                            finalBrightness = startBrightness + (int) diffBrightness;
                            if (finalBrightness < 0)
                                finalBrightness = 0;
                            else if (finalBrightness > maxBrightness)
                                finalBrightness = maxBrightness;

                            String progressText = "\t" + finalBrightness;
                            if (imgIndicator.getDrawable() == null) {
                                imgIndicator.setImageDrawable(indBrightnessDrawable);
                            }
                            progressTextView.setText(progressText);

                            WindowManager.LayoutParams layout = mWindow.getAttributes();
                            layout.screenBrightness = (float) finalBrightness / 100;
                            mWindow.setAttributes(layout);

                            SharedPref.setSharedPrefData(activity, SharedPref.playerBrightness, String.valueOf(finalBrightness));
                        }
                    }
                }
            }

            @Override
            public void onClick() {

                if (playerControlVisible()) {
                    removeCallbacks(updateProgressAction);
                    removeCallbacks(hideAction);
                    hideControl();
                } else {
                    if (hideAtMs != C.TIME_UNSET) {
                        long delayMs = hideAtMs - SystemClock.uptimeMillis();
                        if (delayMs <= 0) {
                            hideControl();
                        } else {
                            llControlRoot.postDelayed(hideAction, delayMs);
                        }
                    }
                    updateAll();
                    showControl();
                }
            }

            @Override
            public void onAfterMove() {
                if (!isLock) {
                    if (finalTime >= 0) {
                        seekToTimeBarPosition((long) finalTime);
                    }
                    if (imgIndicator.getDrawable() != null)
                        imgIndicator.setImageDrawable(null);
                    imgIndicator.setVisibility(GONE);
                    progressTextView.setVisibility(GONE);
                }
            }

            @Override
            public void onBeforeMove(Direction dir) {
                if (!isLock) {
                    if (dir == Direction.LEFT || dir == Direction.RIGHT) {
                        progressTextView.setVisibility(View.VISIBLE);
                    } else {
                        maxBrightness = 100;
                        if (mWindow != null) {
                            startBrightness = (int) (mWindow.getAttributes().screenBrightness * 100);
                        }
                        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                        startVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        imgIndicator.setVisibility(VISIBLE);
                        progressTextView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        showControl();

    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mInitialViewWidth = getResources().getDisplayMetrics().widthPixels;
        mInitialViewHeight = getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        releasePlayer();
        shouldAutoPlay = true;
        clearResumePosition();
        setIntent(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializePlayer();
        } else {
            showToast(R.string.storage_permission_denied);
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
            AppUtils.LOG("onStart");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
            AppUtils.LOG("onResume");
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
            AppUtils.LOG("onPause");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
            AppUtils.LOG("onStop");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (activity.isTaskRoot()) {
            finish();
        } else {
            releasePlayer();
            /*Intent intent = new Intent(activity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);*/
            finish();
        }

    }


    private void bindControl() {
        contentFrame = findViewById(R.id.exo_content_frame);
        surfaceView = findViewById(R.id.exo_surfaceView);
        shutterView = findViewById(R.id.exo_shutter);
        subtitleView = findViewById(R.id.exo_subtitles);
        if (subtitleView != null) {
            subtitleView.setUserDefaultStyle();
            subtitleView.setStyle(new CaptionStyleCompat(Color.WHITE, Color.TRANSPARENT,
                    Color.TRANSPARENT, CaptionStyleCompat.EDGE_TYPE_NONE, Color.WHITE, AppUtils.getTextTypeFace(activity)));
            subtitleView.setUserDefaultTextSize();
        }
        overlayFrameLayout = findViewById(R.id.exo_overlay);
        imgIndicator = findViewById(R.id.imgIndicator);
        progressTextView = findViewById(R.id.exo_progress_text_view);

        //overlayFrameLayout.setClickable(true);

        setResizeModeRaw(contentFrame, AspectRatioFrameLayout.RESIZE_MODE_FIT);
    }

    private void bindPlayerControl() {
        llControlRoot = findViewById(R.id.llControlRoot);
        positionView = findViewById(R.id.exo_position);
        durationView = findViewById(R.id.exo_duration);

        controlDispatcher = DEFAULT_CONTROL_DISPATCHER;

        // Update progress
        window = new Timeline.Window();
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());

        // Control
        timeBar = findViewById(R.id.exo_progress);
        if (timeBar != null) {
            timeBar.setListener(this);
        }
        backButton = findViewById(R.id.exo_back);
        if (backButton != null) {
            backButton.setOnClickListener(this);
        }
        titleView = findViewById(R.id.exo_title);
        audioTrackButton = findViewById(R.id.exo_audio_track);
        if (audioTrackButton != null) {
            audioTrackButton.setOnClickListener(this);
        }
        subtitleTrackButton = findViewById(R.id.exo_subtitle_track);
        if (subtitleTrackButton != null) {
            subtitleTrackButton.setOnClickListener(this);
        }
        unLockButton = findViewById(R.id.exo_unlock);
        if (unLockButton != null) {
            unLockButton.setOnClickListener(this);
        }
        lockButton = findViewById(R.id.exo_lock);
        if (lockButton != null) {
            lockButton.setOnClickListener(this);
        }
        rotateButton = findViewById(R.id.exo_rotate);
        if (rotateButton != null) {
            rotateButton.setOnClickListener(this);
        }
        muteButton = findViewById(R.id.exo_mute);
        if (muteButton != null) {
            muteButton.setOnClickListener(this);
        }

        sharebutton=findViewById(R.id.exo_share);
        if(sharebutton!=null) {
            sharebutton.setOnClickListener(this);
        }

        playButton = findViewById(R.id.exo_play);
        if (playButton != null) {
            playButton.setOnClickListener(this);
        }

        cutpreButton = findViewById(R.id.exo_cutpre);
        if (cutpreButton != null) {
            cutpreButton.setOnClickListener(this);
        }

        cutnextButton = findViewById(R.id.exo_cutnext);
        if (cutnextButton != null) {
            cutnextButton.setOnClickListener(this);
        }

        pauseButton = findViewById(R.id.exo_pause);
        if (pauseButton != null) {
            pauseButton.setOnClickListener(this);
        }
        previousButton = findViewById(R.id.exo_prev);
        if (previousButton != null) {
            previousButton.setOnClickListener(this);
        }
        nextButton = findViewById(R.id.exo_next);
        if (nextButton != null) {
            nextButton.setOnClickListener(this);
        }
        scaleButton = findViewById(R.id.exo_scale_toggle);
        if (scaleButton != null) {
            scaleButton.setOnClickListener(this);
        }
        scaleModeTextView = findViewById(R.id.exo_scale_toggle_text);
        scaleModeTextView.setVisibility(GONE);
        Resources resources = activity.getResources();
        scaleFitButtonDrawable = resources.getDrawable(R.drawable.exo_control_scale_fit);
        scaleWidthButtonDrawable = resources.getDrawable(R.drawable.exo_control_scale_width);
        scaleHeightButtonDrawable = resources.getDrawable(R.drawable.exo_control_scale_height);
        scaleFillButtonDrawable = resources.getDrawable(R.drawable.exo_control_scale_fill);
        scaleZoomButtonDrawable = resources.getDrawable(R.drawable.exo_control_scale_zoom);

        indVolumeDrawable = resources.getDrawable(R.drawable.exo_control_volume);
        indBrightnessDrawable = resources.getDrawable(R.drawable.exo_control_brightness);

        scaleFitContentDescription = resources.getString(R.string.exo_controls_scale_fit_description);
        scaleWidthContentDescription = resources.getString(R.string.exo_controls_scale_width_description);
        scaleHeightContentDescription = resources.getString(R.string.exo_controls_scale_height_description);
        scaleFillContentDescription = resources.getString(R.string.exo_controls_scale_fill_description);
        scaleZoomContentDescription = resources.getString(R.string.exo_controls_scale_zoom_description);
    }

    private void initializePlayer() {

        if (audioManagerResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            boolean needNewPlayer = player == null;
            if (needNewPlayer) {

                TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
                trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
                trackSelectionHelper = new TrackSelectionHelper(trackSelector, adaptiveTrackSelectionFactory);
                lastSeenTrackGroupArray = null;

                player = ExoPlayerFactory.newSimpleInstance(activity, trackSelector);
                setPlayer(player);
                player.setPlayWhenReady(shouldAutoPlay);
            }

            videoSourceFile = new File(VIDEO_LIST.get(position).getData());

            if (Util.maybeRequestReadExternalStoragePermission(this, Uri.fromFile(videoSourceFile))) {
                // The player will be reinitialized if the permission is granted.
                return;
            }

            titleView.setText(videoSourceFile.getName());

            SharedPref.setSharedPrefData(activity, SharedPref.lastPlayed, videoSourceFile.getAbsolutePath());

            MediaSource mediaSource = new ExtractorMediaSource(Uri.fromFile(videoSourceFile), mediaDataSourceFactory,
                    new DefaultExtractorsFactory(), null, null);

            boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
            if (haveResumePosition) {
                player.seekTo(resumeWindow, resumePosition);
            }
            player.prepare(mediaSource, !haveResumePosition, false);
            inErrorState = false;
            updateButtonVisibilities();
        }
    }


    private void setPlayer(SimpleExoPlayer player) {
        if (player != null) {
            player.setVideoSurfaceView(surfaceView);
            player.addListener(this);
            player.addTextOutput(this);
            player.addVideoListener(this);
            updateAll();
            mCurrentVolume = player.getVolume();
        } else {
            hideControl();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.stop();
            audioManager.abandonAudioFocus(afChangeListener);
            shouldAutoPlay = player.getPlayWhenReady();
            updateResumePosition();
            player.release();
            player = null;
            trackSelector = null;
            trackSelectionHelper = null;
        }
    }

    private void updateResumePosition() {

        resumeWindow = player.getCurrentWindowIndex();
        resumePosition = Math.max(0, player.getContentPosition());
        //SharedPref.setSharedPrefData(activity, SharedPref.resumePosition, String.valueOf(resumePosition));
    }

    private void clearResumePosition() {

        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((MyApp) getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
        return ((MyApp) getApplication())
                .buildHttpDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    private void updateButtonVisibilities() {
        if (player == null) {
            return;
        }
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }
        for (int i = 0; i < mappedTrackInfo.length; i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                switch (player.getRendererType(i)) {
                    case C.TRACK_TYPE_AUDIO:
                        audioTrackButton.setVisibility(VISIBLE);
                        audioTrackButton.setTag(i);
                        break;
                    case C.TRACK_TYPE_VIDEO:
                        break;
                    case C.TRACK_TYPE_TEXT:
                        subtitleTrackButton.setVisibility(VISIBLE);
                        subtitleTrackButton.setTag(i);
                        break;
                }
            }
        }
    }

    @SuppressWarnings("ResourceType")
    private void setResizeModeRaw(AspectRatioFrameLayout aspectRatioFrame, int resizeMode) {
        aspectRatioFrame.setResizeMode(resizeMode);
    }

    private void nextMedia() {

        if (player != null) {
            if ((position + 1) < VIDEO_LIST.size()) {
                position++;
                videoSourceFile = new File(VIDEO_LIST.get(position).getData());
                titleView.setText(videoSourceFile.getName());

                SharedPref.setSharedPrefData(activity, SharedPref.lastPlayed, videoSourceFile.getAbsolutePath());

                MediaSource mediaSource = new ExtractorMediaSource(Uri.fromFile(videoSourceFile), mediaDataSourceFactory,
                        new DefaultExtractorsFactory(), null, null);

                player.stop();
                player.seekTo(0L);
                player.prepare(mediaSource);
            } else {
                if (player.getPlaybackState() == Player.STATE_ENDED)
                    finish();
            }
        }
    }

    private void nextcut(){

        if (player != null) {

            int currentPosition = (int) player.getCurrentPosition();
            if (currentPosition + seekForwardTime <= player.getDuration()) {
                player.seekTo(currentPosition + seekForwardTime);
            } else {
                player.seekTo(player.getDuration());
            }
        }

    }

    private void nextpre(){

        if (player != null) {

            int currentPosition = (int) player.getCurrentPosition();
            if (currentPosition - seekBackwardTime >= 0) {
                player.seekTo(currentPosition - seekBackwardTime);
            } else {
                player.seekTo(0);
            }
        }


    }


    private void previousMedia() {
        if (player != null) {
            if ((position - 1) >= 0) {
                position--;
                videoSourceFile = new File(VIDEO_LIST.get(position).getData());
                titleView.setText(videoSourceFile.getName());

                SharedPref.setSharedPrefData(activity, SharedPref.lastPlayed, videoSourceFile.getAbsolutePath());

                MediaSource mediaSource = new ExtractorMediaSource(Uri.fromFile(videoSourceFile), mediaDataSourceFactory,
                        new DefaultExtractorsFactory(), null, null);

                player.stop();
                player.seekTo(0L);
                player.prepare(mediaSource);
            } else {
                player.seekToDefaultPosition();
            }
        }
    }

    private void hideControl() {
        llControlRoot.setVisibility(GONE);
        removeCallbacks(updateProgressAction);
        removeCallbacks(hideAction);
        hideAtMs = C.TIME_UNSET;
        setFullScreen(true);
    }

    private void showControl() {
        if (!playerControlVisible()) {
            llControlRoot.setVisibility(VISIBLE);
            updateAll();
            requestPlayPauseFocus();
        }
        setFullScreen(false);
        hideAfterTimeout();
    }

    private void hideAfterTimeout() {
        removeCallbacks(hideAction);
        if (showTimeoutMs > 0) {
            hideAtMs = SystemClock.uptimeMillis() + showTimeoutMs;
            llControlRoot.postDelayed(hideAction, showTimeoutMs);
        } else {
            hideAtMs = C.TIME_UNSET;
        }
    }

    private void setFullScreen(boolean fullScreen) {
        int flags = !fullScreen ? 0 : View.SYSTEM_UI_FLAG_LOW_PROFILE;

        ViewCompat.setFitsSystemWindows(llControlRoot, !fullScreen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (fullScreen || isLock) {
                flags |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
        }
        contentFrame.setSystemUiVisibility(flags);
    }

    private boolean playerControlVisible() {
        return llControlRoot.getVisibility() == VISIBLE;
    }

    private void updateAll() {
        updatePlayPauseButton();
        updateNavigation();
        updateScaleMode();
        updateProgress();
    }

    private void updatePlayPauseButton() {

        if (!playerControlVisible()) {
            return;
        }
        boolean requestPlayPauseFocus = false;
        boolean playing = player != null && player.getPlayWhenReady();

        if (playButton != null) {
            requestPlayPauseFocus |= playing && playButton.isFocused();
            playButton.setVisibility(playing ? GONE : View.VISIBLE);

        }
        if (pauseButton != null) {
            requestPlayPauseFocus |= !playing && pauseButton.isFocused();
            pauseButton.setVisibility(!playing ? GONE : View.VISIBLE);

        }
        if (requestPlayPauseFocus) {
            requestPlayPauseFocus();
        }
    }

    private void updateScaleMode() {
        if (!playerControlVisible()) {
            return;
        }
        if (player == null) {
            return;
        }

        @AspectRatioFrameLayout.ResizeMode int scaleMode = contentFrame.getResizeMode();
        if (scaleMode == AspectRatioFrameLayout.RESIZE_MODE_FIT) {
            scaleButton.setImageDrawable(scaleFitButtonDrawable);
            scaleButton.setContentDescription(scaleFitContentDescription);
        } else if (scaleMode == AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH) {
            scaleButton.setImageDrawable(scaleWidthButtonDrawable);
            scaleButton.setContentDescription(scaleWidthContentDescription);
        } else if (scaleMode == AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT) {
            scaleButton.setImageDrawable(scaleHeightButtonDrawable);
            scaleButton.setContentDescription(scaleHeightContentDescription);
        } else if (scaleMode == AspectRatioFrameLayout.RESIZE_MODE_FILL) {
            scaleButton.setImageDrawable(scaleFillButtonDrawable);
            scaleButton.setContentDescription(scaleFillContentDescription);
        } else if (scaleMode == AspectRatioFrameLayout.RESIZE_MODE_ZOOM) {
            scaleButton.setImageDrawable(scaleZoomButtonDrawable);
            scaleButton.setContentDescription(scaleZoomContentDescription);
        }
    }

    private void updateNavigation() {
        if (!playerControlVisible()) {
            return;
        }
        Timeline timeline = player != null ? player.getCurrentTimeline() : null;
        boolean haveNonEmptyTimeline = timeline != null && !timeline.isEmpty();
        boolean isSeekable = false;
        boolean enablePrevious = true;
        boolean enableNext = true;
        if (haveNonEmptyTimeline) {
            int windowIndex = player.getCurrentWindowIndex();
            timeline.getWindow(windowIndex, window);
            isSeekable = window.isSeekable;
        }
        setButtonEnabled(enablePrevious, previousButton);
        setButtonEnabled(enableNext, nextButton);
        if (timeBar != null) {
            timeBar.setEnabled(isSeekable);
        }
    }


    public class ShareVideoDialog extends AsyncTask<Void, Void, Void> {


        private ArrayList shareList;

        private ShareVideoDialog() {
            this.shareList = new ArrayList();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.shareList.clear();
        }

        protected Void doInBackground(Void... voids) {
            try {
                File file = new File(((Video) VIDEO_LIST.get(position)).getData());
                try {
                    this.shareList.add(FileProvider.getUriForFile(getApplication(), BuildConfig.APPLICATION_ID + ".provider", file));
                } catch (Exception e) {
                    e.printStackTrace();
                    this.shareList.add(Uri.fromFile(file));
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (MainFragment.mActionMode != null) {
                MainFragment. mActionMode.finish();
            }
            AppUtils.shareMultipleFile(getApplicationContext(), this.shareList);
        }
    }

    private void updateProgress() {

        if (!playerControlVisible()) {
            return;
        }
        long position = 0;
        long bufferedPosition = 0;
        long duration = 0;
        if (player != null) {
            long currentWindowTimeBarOffsetUs = 0;
            long durationUs = 0;
            Timeline timeline = player.getCurrentTimeline();
            if (!timeline.isEmpty()) {
                int currentWindowIndex = player.getCurrentWindowIndex();
                int firstWindowIndex = multiWindowTimeBar ? 0 : currentWindowIndex;
                int lastWindowIndex =
                        multiWindowTimeBar ? timeline.getWindowCount() - 1 : currentWindowIndex;
                for (int i = firstWindowIndex; i <= lastWindowIndex; i++) {
                    if (i == currentWindowIndex) {
                        currentWindowTimeBarOffsetUs = durationUs;
                    }
                    timeline.getWindow(i, window);
                    if (window.durationUs == C.TIME_UNSET) {
                        Assertions.checkState(!multiWindowTimeBar);
                        break;
                    }
                    durationUs += window.durationUs;
                }
            }
            duration = C.usToMs(durationUs);
            position = C.usToMs(currentWindowTimeBarOffsetUs);
            bufferedPosition = position;

            position += player.getCurrentPosition();
            bufferedPosition += player.getBufferedPosition();
        }
        if (durationView != null) {
            durationView.setText(Util.getStringForTime(formatBuilder, formatter, duration));
        }
        if (positionView != null && !scrubbing) {
            positionView.setText(Util.getStringForTime(formatBuilder, formatter, position));
        }
        if (timeBar != null) {
            timeBar.setPosition(position);
            timeBar.setBufferedPosition(bufferedPosition);
            timeBar.setDuration(duration);
        }

        // Cancel any pending updates and schedule a new one if necessary.
        removeCallbacks(updateProgressAction);
        int playbackState = player == null ? Player.STATE_IDLE : player.getPlaybackState();
        if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
            long delayMs;
            if (player.getPlayWhenReady() && playbackState == Player.STATE_READY) {
                delayMs = 10 - (position % 10);
                if (delayMs < 2) {
                    delayMs += 10;
                }
            } else {
                delayMs = 10;
            }
            if (player.getDuration() >= 120000)
                delayMs = delayMs * 10;
            llControlRoot.postDelayed(updateProgressAction, delayMs);
        }

    }

    private void setLock(boolean lock) {
        isLock = lock;
        findViewById(R.id.llControlTop).setVisibility(lock ? GONE : VISIBLE);
        findViewById(R.id.llControlBottom).setVisibility(lock ? GONE : VISIBLE);
        rotateButton.setVisibility(lock ? GONE : VISIBLE);
        sharebutton.setVisibility(lock ? GONE:VISIBLE);
        unLockButton.setVisibility(lock ? VISIBLE : GONE);
        lockButton.setVisibility(lock? GONE:VISIBLE);
        muteButton.setVisibility(lock ? GONE : VISIBLE);
        if (lock)
            hideControl();
        else
            showControl();
    }

    private void requestPlayPauseFocus() {
        boolean playing = player != null && player.getPlayWhenReady();
        if (!playing && playButton != null) {
            playButton.requestFocus();
        } else if (playing && pauseButton != null) {
            pauseButton.requestFocus();
        }
    }

    private void setButtonEnabled(boolean enabled, View view) {
        if (view == null) {
            return;
        }
        view.setEnabled(enabled);
        view.setAlpha(enabled ? 1f : 0.3f);
        view.setVisibility(VISIBLE);

    }

    private void seekToTimeBarPosition(long positionMs) {
        int windowIndex;
        Timeline timeline = player.getCurrentTimeline();
        if (multiWindowTimeBar && !timeline.isEmpty()) {
            int windowCount = timeline.getWindowCount();
            windowIndex = 0;
            while (true) {
                long windowDurationMs = timeline.getWindow(windowIndex, window).getDurationMs();
                if (positionMs < windowDurationMs) {
                    break;
                } else if (windowIndex == windowCount - 1) {
                    // Seeking past the end of the last window should seek to the end of the timeline.
                    positionMs = windowDurationMs;
                    break;
                }
                positionMs -= windowDurationMs;
                windowIndex++;
            }
        } else {
            windowIndex = player.getCurrentWindowIndex();
        }
        seekTo(windowIndex, positionMs);
    }

    private void seekTo(int windowIndex, long positionMs) {
        boolean dispatched = controlDispatcher.dispatchSeekTo(player, windowIndex, positionMs);
        if (!dispatched) {
            // The seek wasn't dispatched. If the progress bar was dragged by the user to perform the
            // seek then it'll now be in the wrong position. Trigger a progress update to snap it back.
            updateProgress();
        }
    }

    public void removeCallbacks(Runnable action) {
        if (action != null) {
            llControlRoot.removeCallbacks(action);
        }
    }

    private void showToast(int messageId) {
        showToast(getString(messageId));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    // TextRenderer.Output implementation

    @Override
    public void onCues(List<Cue> cues) {
        if (subtitleView != null) {
            subtitleView.onCues(cues);
        }
    }

    // SimpleExoPlayer.VideoListener implementation

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        if (contentFrame != null) {
            float aspectRatio = height == 0 ? 1 : (width * pixelWidthHeightRatio) / height;
            contentFrame.setAspectRatio(aspectRatio);
        }
    }

    @Override
    public void onRenderedFirstFrame() {
        if (shutterView != null) {
            shutterView.setVisibility(GONE);
        }
    }

    // Player.EventListener implementation

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        updateNavigation();
        //updateTimeBarMode();
        updateProgress();
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        updateButtonVisibilities();
        if (trackGroups != lastSeenTrackGroupArray) {
            MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO)
                        == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                    showToast(R.string.error_unsupported_video);
                }
                if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO)
                        == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                    showToast(R.string.error_unsupported_audio);
                }
            }
            lastSeenTrackGroupArray = trackGroups;
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        updatePlayPauseButton();
        updateProgress();
        if (playbackState == Player.STATE_ENDED) {
            nextMedia();
        }
        updateButtonVisibilities();
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        String errorString = null;
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            Exception cause = e.getRendererException();
            if (cause instanceof DecoderInitializationException) {
                // Special case for decoder initialization failures.
                DecoderInitializationException decoderInitializationException =
                        (DecoderInitializationException) cause;
                if (decoderInitializationException.decoderName == null) {
                    if (decoderInitializationException.getCause() instanceof DecoderQueryException) {
                        errorString = getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType);
                    } else {
                        errorString = getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType);
                    }
                } else {
                    errorString = getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.decoderName);
                }
            }
        }
        if (errorString != null) {
            //showToast(errorString);
            final Dialog dialog = new Dialog(activity, R.style.ErrorDialog);
            dialog.setContentView(R.layout.dialog_error);

            ((TextView) dialog.findViewById(R.id.txtError)).setText(errorString);

            dialog.findViewById(R.id.txtDialogConfirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogDismiss.dismissWithCheck(dialog);
                    onBackPressed();
                }
            });
            dialog.show();
        }
        inErrorState = true;
        if (isBehindLiveWindow(e)) {
            clearResumePosition();
            initializePlayer();
        } else {
            updateResumePosition();
            updateButtonVisibilities();
            showControl();
        }
    }

    @Override
    public void onPositionDiscontinuity() {
        updateNavigation();
        updateProgress();
        if (inErrorState) {
            // This will only occur if the user has performed a seek whilst in the error state. Update the
            // resume position so that if the user then retries, playback will resume from the position to
            // which they seeked.
            updateResumePosition();
        }
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    // TimeBar.OnScrubListener implementation

    @Override
    public void onScrubStart(TimeBar timeBar, long position) {
        removeCallbacks(hideAction);
        scrubbing = true;
    }

    @Override
    public void onScrubMove(TimeBar timeBar, long position) {
        if (positionView != null) {
            positionView.setText(Util.getStringForTime(formatBuilder, formatter, position));
        }
    }

    @Override
    public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
        scrubbing = false;
        if (!canceled && player != null) {
            seekToTimeBarPosition(position);
        }
        hideAfterTimeout();
    }

    @Override
    public void onClick(View view) {

        if (player != null) {
            if (audioTrackButton == view) {
                MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    trackSelectionHelper.showSelectionDialog(this, view.getContentDescription(),
                            trackSelector.getCurrentMappedTrackInfo(), (int) view.getTag());
                }
            } else if (subtitleTrackButton == view) {
                MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    trackSelectionHelper.showSelectionDialog(this, view.getContentDescription(),
                            trackSelector.getCurrentMappedTrackInfo(), (int) view.getTag());
                }
            } else if (lockButton == view) {
                setLock(true);
            } else if (unLockButton == view) {
                setLock(false);
            } else if (rotateButton == view) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    rotateButton.setActivated(false);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    rotateButton.setActivated(true);
                }
            } else if (backButton == view) {
                onBackPressed();

            } else if (nextButton == view) {
                nextMedia();

            } else if (previousButton == view) {
                previousMedia();

            } else if (playButton == view) {
                controlDispatcher.dispatchSetPlayWhenReady(player, true);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            } else if (pauseButton == view) {
                controlDispatcher.dispatchSetPlayWhenReady(player, false);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


            } else if (scaleButton == view) {
                Assertions.checkState(contentFrame != null);
                contentFrame.setResizeMode(contentFrame.getNextResizeMode());
                updateScaleMode();
                if (scaleModeTextView.getVisibility() == GONE) {
                    scaleModeTextView.setVisibility(VISIBLE);
                }
                scaleModeTextView.setText(scaleButton.getContentDescription().toString());
                scaleModeTextView.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        scaleModeTextView.setVisibility(GONE);
                    }
                }, 3000);

            } else if (muteButton == view) {
                if (isMute) {
//                    setMute(mCurrentVolume);
                    setMute(0);
                    muteButton.setBackground(getResources().getDrawable(R.drawable.ic_v_mute));
                    isMute = false;
                } else {
                    muteButton.setBackground(getResources().getDrawable(R.drawable.ic_v_full));
//                    setMute(0);
                    setMute(mCurrentVolume);
                    isMute = true;
                }
            }

            else if(sharebutton==view) {
                new ShareVideoDialog().execute();
            }

            else if(cutnextButton==view){
                nextcut();
            }

            else if(cutpreButton==view){
                nextpre();
            }

        }

        hideAfterTimeout();
    }


    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            releasePlayer();
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
            // Pause playback
            controlDispatcher.dispatchSetPlayWhenReady(player, false);
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            // Lower the volume, keep playing
            controlDispatcher.dispatchSetPlayWhenReady(player, false);
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            // Your app has been granted audio focus again
            // Raise volume to normal, restart playback if necessary
            if (player != null) {
                controlDispatcher.dispatchSetPlayWhenReady(player, true);
                return;
            }
            initializePlayer();
        }
    }

    public interface ControlDispatcher {

        boolean dispatchSetPlayWhenReady(Player player, boolean playWhenReady);

        boolean dispatchSeekTo(Player player, int windowIndex, long positionMs);

        boolean dispatchSetRepeatMode(Player player, @Player.RepeatMode int repeatMode);

    }
}
