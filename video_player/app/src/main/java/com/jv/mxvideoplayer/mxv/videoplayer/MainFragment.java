package com.jv.mxvideoplayer.mxv.videoplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdOptionsView;
//import com.facebook.ads.NativeAd;
//import com.facebook.ads.NativeAdLayout;
//import com.facebook.ads.NativeAdListener;
//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdLoader;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.InterstitialAd;
//import com.google.android.gms.ads.VideoController;
//import com.google.android.gms.ads.VideoOptions;
//import com.google.android.gms.ads.formats.MediaView;
//import com.google.android.gms.ads.formats.NativeAdOptions;
//import com.google.android.gms.ads.formats.UnifiedNativeAd;
//import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jv.mxvideoplayer.mxv.videoplayer.Acitivity.ExoPlayerActivity;
import com.jv.mxvideoplayer.mxv.videoplayer.Adapter.FolderAdapter;
import com.jv.mxvideoplayer.mxv.videoplayer.Adapter.VideoAdapter;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.AnimationUtil;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.AppUtils;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.Constants;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.DBHelper;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.DialogDismiss;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.RandomStringGenerator;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref;
//import com.jv.mxvideoplayer.mxv.videoplayer.MainPackage.GlideApp;
import com.jv.mxvideoplayer.mxv.videoplayer.model.Folder;
import com.jv.mxvideoplayer.mxv.videoplayer.model.Video;
import com.jv.mxvideoplayer.mxv.videoplayer.widget.FastScrollRecyclerView.FastScrollRecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.Constants.FOLDER_LIST;
import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.Constants.MEDIA_LIST;
import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.Constants.VIDEO_LIST;
//import static com.jv.mxvideoplayer.mxv.videoplayer.MainActivity.ShowInterstitial;
//import static com.jv.mxvideoplayer.mxv.videoplayer.MainActivity.loadFacebookInterstitial;



public class MainFragment extends Fragment implements View.OnClickListener, ActionMode.Callback,
        SwipeRefreshLayout.OnRefreshListener, FolderAdapter.FolderClickListener, VideoAdapter.ClickListener, MainActivity.HomePressListener {

    int check=0;
    int pos=0;
    private Toolbar toolbar;
    private TextView txtToolbarTitle;
    public static ActionMode mActionMode;

    ImageView imgThumb;
    TextView txtDuration, txtTitle, txtResolution, txtSize;

    private SwipeRefreshLayout srlMedia;
    public static FastScrollRecyclerView rvFolder, rvVideo;
    private FolderAdapter mFolderAdapter;
    public static VideoAdapter mVideoAdapter;
    private boolean isVideo = false;
    private boolean isVideoList = false;

    private String bucketID;
    private int folderListPosition;

    public static List<Integer> mSelectedItemList = new ArrayList<>();
    private LinearLayout llVideoControl, llDeleteVideo, llShareVideo, llRenameVideo, llProperties;
    private Dialog mDialog;

    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView txtBottomSheetTitle;

    public static int videoListPosition;
    public static FolderAdapter.FolderClickListener folderClickListener;
    public static VideoAdapter.ClickListener clickListener;
    public static MainActivity.HomePressListener homePressListener;

    View view = null;
    private int currentFolderPosition = 0;

    SpotsDialog dialogAds;
    SpotsDialog dialogPrivate;
    private Dialog poupdialog;
//    private InterstitialAd mInterstitialAd;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main1, container, false);

//        loadFacebookInterstitial(getActivity());
        dialogAds = new SpotsDialog(getActivity(), "Showing ADS...", R.style.CustomSpotDialog);
        dialogPrivate = new SpotsDialog(getActivity(), "Please Wait...", R.style.CustomSpotDialog);
        folderClickListener = this;
        clickListener = this;
        homePressListener = this;

        imgThumb = view.findViewById(R.id.imgThumb);
        txtDuration = view.findViewById(R.id.txtDuration);
        txtTitle = view.findViewById(R.id.txtTitle);
        txtResolution = view.findViewById(R.id.txtResolution);
        txtSize = view.findViewById(R.id.txtSize);

        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        txtToolbarTitle = toolbar.findViewById(R.id.txtToolbarTitle);

        bindToolbar();
        bindControls();

        srlMedia.setRefreshing(true);
        onRefresh();
//        initBottomSheet();
        initdialog();
        return view;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (isVideoList) {
            menu.findItem(R.id.menu_sort).setVisible(true);
        } else {
            menu.findItem(R.id.menu_sort).setVisible(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        getActivity().getMenuInflater().inflate(R.menu.menu_main_setting, menu);
        super.onCreateOptionsMenu(menu, inflater);

//        if(rvFolder.getVisibility() == View.VISIBLE)
//        {
//            menu.findItem(R.id.menu_sort).setVisible(false);
//        }
//        else {
//            menu.clear();
//
//            menu.findItem(R.id.menu_sort).setVisible(true);
//        }
    }


    @Override
    public void handleHomeButtonPress() {
        if (rvVideo.getVisibility() == View.VISIBLE) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
            AnimationUtil.crossFadeViews(srlMedia, rvVideo, AnimationUtil.ANIMATION_DURATION_SHORT);
            isVideoList = false;
            getActivity().invalidateOptionsMenu();
        } else {
            //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.btn_back);
            //AnimationUtil.crossFadeViews(this.rvVideo, this.srlMedia, AnimationUtil.ANIMATION_DURATION_SHORT);
            //isVideoList = true;
            MainActivity.mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void handleBackButtonPress() {
        if (rvVideo.getVisibility() == View.VISIBLE) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
            AnimationUtil.crossFadeViews(srlMedia, rvVideo, AnimationUtil.ANIMATION_DURATION_SHORT);
            isVideoList = false;
            getActivity().invalidateOptionsMenu();
            bindToolbar();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.customview1, viewGroup, false);
            builder.setView(dialogView);
//            facebookNativ(getActivity(),dialogView);

            AlertDialog alertDialog = builder.create();
            TextView yes = dialogView.findViewById(R.id.yes);
            TextView no = dialogView.findViewById(R.id.no);
            TextView rate = dialogView.findViewById(R.id.rate);

//            loadNativeAd(dialogView);

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();

                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertDialog.dismiss();

                }
            });

            rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                    }
                }
            });


            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.show();

        }


    }
//    private NativeAdLayout nativeAdLayout;
//    private LinearLayout adView;
//    private NativeAd nativeAd;
//
//    private void loadNativeAd(View dialogView) {
//        // Instantiate a NativeAd object.
//        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
//        // now, while you are testing and replace it later when you have signed up.
//        // While you are using this temporary code you will only get test ads and if you release
//        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
//        nativeAd = new NativeAd(getContext(), "190108885922911_190111569255976");
//
//        NativeAdListener nativeAdListener = new NativeAdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                if (nativeAd == null || nativeAd != ad) {
//                    return;
//                }
//                // Inflate Native Ad into Container
//                inflateAd(nativeAd,dialogView);
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//
//            @Override
//            public void onMediaDownloaded(Ad ad) {
//
//            }
////            @Override
////            public void onAdLoaded(Ad ad) {
////
////            }
//
//        };
//
//        // Request an ad
//        nativeAd.loadAd(
//                nativeAd.buildLoadAdConfig()
//                        .withAdListener(nativeAdListener)
//                        .build());
//    }
//
//    private void inflateAd(NativeAd nativeAd, View dialogView) {
//
//        nativeAd.unregisterView();
//
//        // Add the Ad view into the ad container.
//        nativeAdLayout = dialogView.findViewById(R.id.native_ad_container);
//        LayoutInflater inflater = LayoutInflater.from(getContext());
//        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
//        adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdLayout, false);
//        nativeAdLayout.addView(adView);
//
//        // Add the AdOptionsView
//        LinearLayout adChoicesContainer =dialogView.findViewById(R.id.ad_choices_container);
//        AdOptionsView adOptionsView = new AdOptionsView(getContext(), nativeAd, nativeAdLayout);
//        adChoicesContainer.removeAllViews();
//        adChoicesContainer.addView(adOptionsView, 0);
//
//        // Create native UI using the ad metadata.
//        com.facebook.ads.MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
//        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
//        com.facebook.ads.MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
//        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
//        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
//        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
//        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);
//
//        // Set the Text.
//        nativeAdTitle.setText(nativeAd.getAdvertiserName());
//        nativeAdBody.setText(nativeAd.getAdBodyText());
//        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
//        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
//        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
//        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());
//
//        // Create a list of clickable views
//        List<View> clickableViews = new ArrayList<>();
//        clickableViews.add(nativeAdTitle);
//        clickableViews.add(nativeAdCallToAction);
//
//        // Register the Title and CTA button to listen for clicks.
//        nativeAd.registerViewForInteraction(
//                adView, nativeAdMedia, nativeAdIcon, clickableViews);
//    }

//    public static  String TAG = "NativeAdActivity";
//    private NativeAd nativeAd;
//
//    public void facebookNativ(Activity activity,View view) {
//        nativeAd = new NativeAd(activity, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");
//
//        NativeAdListener nativeAdListener = new NativeAdListener() {
//            @Override
//            public void onMediaDownloaded(Ad ad) {
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
////                if (Constant.ADS_TYPE.equals("facebook")) {
////
////                    refreshAd();
////                }
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                if (nativeAd == null || nativeAd != ad) {
//                    return;
//                }
//                inflateAd(nativeAd,activity,view);
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//            }
//        };
//
//
//        nativeAd.loadAd(
//                nativeAd.buildLoadAdConfig()
//                        .withAdListener(nativeAdListener)
//                        .build());
//    }


//    private void loadNativeAd(Activity activity,View view) {
//        // Instantiate a NativeAd object.
//        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
//        // now, while you are testing and replace it later when you have signed up.
//        // While you are using this temporary code you will only get test ads and if you release
//        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
//        nativeAd = new NativeAd(activity, "190108885922911_190111569255976");
//
//        NativeAdListener nativeAdListener = new NativeAdListener() {
//            @Override
//            public void onMediaDownloaded(Ad ad) {
//                // Native ad finished downloading all assets
//                Log.e(TAG, "Native ad finished downloading all assets.");
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                // Native ad failed to load
//                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                // Native ad is loaded and ready to be displayed
//                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
//                if (nativeAd == null || nativeAd != ad) {
//                    return;
//                }
//                // Inflate Native Ad into Container
//                inflateAd(nativeAd,activity,view);
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//                // Native ad clicked
//                Log.d(TAG, "Native ad clicked!");
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//                // Native ad impression
//                Log.d(TAG, "Native ad impression logged!");
//            }
//        };
//
//        // Request an ad
//        nativeAd.loadAd(
//                nativeAd.buildLoadAdConfig()
//                        .withAdListener(nativeAdListener)
//                        .build());
//

//    }
//    private void inflateAd(NativeAd nativeAd,Activity activity,View nview) {
//
//        nativeAd.unregisterView();
//
//        // Add the Ad view into the ad container.
//        nativeAdLayout = nview.findViewById(R.id.native_ad_container);
//        LayoutInflater inflater = LayoutInflater.from(activity);
//        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
//        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdLayout, false);
//        nativeAdLayout.addView(adView);
//
//        // Add the AdOptionsView
//        LinearLayout adChoicesContainer = view.findViewById(R.id.ad_choices_container);
//        AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, (NativeAdLayout) nativeAdLayout);
//        adChoicesContainer.removeAllViews();
//        adChoicesContainer.addView(adOptionsView, 0);
//
//        // Create native UI using the ad metadata.
//        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
//        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
//        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
//        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
//        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
//        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
//        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);
//
//        // Set the Text.
//        nativeAdTitle.setText(nativeAd.getAdvertiserName());
//        nativeAdBody.setText(nativeAd.getAdBodyText());
//        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
//        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
//        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
//        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());
//
//        // Create a list of clickable views
//        List<View> clickableViews = new ArrayList<>();
//        clickableViews.add(nativeAdTitle);
//        clickableViews.add(nativeAdCallToAction);
//
//        // Register the Title and CTA button to listen for clicks.
//        /*nativeAd.registerViewForInteraction(
//                adView, nativeAdMedia, nativeAdIcon, clickableViews);*/
//    }



    @Override
    public void onResume() {
        super.onResume();


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case android.R.id.home: {
                onBackPress();
                break;
            }*/
            case R.id.menu_them: {
                Intent i = new Intent(getContext(), PageActivity.class);
                startActivity(i);

                break;
            }
            case R.id.menu_refresh: {

                Log.e("refresh","ref");
//                ShowInterstitial(getActivity());
                srlMedia.setRefreshing(true);
                onRefresh();

                break;
            }
            case R.id.menu_setting: {
//                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            }
            case R.id.menu_search: {
//                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            }
            case R.id.menu_sort: {
                mDialog = new Dialog(getActivity(), R.style.CustomDialog);
                mDialog.setContentView(R.layout.dialog_sort_video);
                mDialog.show();

                final RadioGroup rgSortType = mDialog.findViewById(R.id.rgSortType);

                String sortType = SharedPref.getSharedPrefData(getActivity(), SharedPref.sortType, getResources().getString(R.string.title));

                if (sortType.equalsIgnoreCase(getResources().getString(R.string.title)))
                    rgSortType.check(R.id.rbTitle);
                else if (sortType.equalsIgnoreCase(getResources().getString(R.string.duration)))
                    rgSortType.check(R.id.rbDuration);
                else if (sortType.equalsIgnoreCase(getResources().getString(R.string.time)))
                    rgSortType.check(R.id.rbTime);
                else if (sortType.equalsIgnoreCase(getResources().getString(R.string.size)))
                    rgSortType.check(R.id.rbSize);

                (mDialog.findViewById(R.id.txtDialogCancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogDismiss.dismissWithCheck(mDialog);
                    }
                });

                (mDialog.findViewById(R.id.txtDialogConfirm)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RadioButton rbType = rgSortType.findViewById(rgSortType.getCheckedRadioButtonId());
                        SharedPref.setSharedPrefData(getActivity(), SharedPref.sortType, String.valueOf(rbType.getText()));
                        DialogDismiss.dismissWithCheck(mDialog);

                        if (mVideoAdapter != null) {
                            AppUtils.sortVideo(getActivity());
                            mVideoAdapter.notifyDataSetChanged();
                        }
                    }
                });
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void initdialog() {
        poupdialog = new Dialog(getActivity(), R.style.CustomDialog);
        poupdialog.setContentView(R.layout.layout_menu_video);
        txtBottomSheetTitle = poupdialog.findViewById(R.id.txtDialogTitle);



        poupdialog.findViewById(R.id.llDialogPrivate).setOnClickListener(this);
        poupdialog.findViewById(R.id.llDialogDelete).setOnClickListener(this);
        poupdialog.findViewById(R.id.llDialogShare).setOnClickListener(this);
        poupdialog.findViewById(R.id.llDialogRename).setOnClickListener(this);
        poupdialog.findViewById(R.id.llDialogProperties).setOnClickListener(this);
    }


    private void initBottomSheet() {
        View bsView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_menu_video, null);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(bsView);

        bottomSheetBehavior = BottomSheetBehavior.from((View) bsView.getParent());

        BottomSheetBehavior.BottomSheetCallback callback = new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        bottomSheetDialog.dismiss();
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    default:
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        };

        bottomSheetBehavior.setBottomSheetCallback(callback);

        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                bottomSheetDialog.findViewById(R.id.llDialogRename).setVisibility(isVideo ? View.VISIBLE : View.GONE);
            }
        });

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        txtBottomSheetTitle = bottomSheetDialog.findViewById(R.id.txtDialogTitle);
        bottomSheetDialog.findViewById(R.id.llDialogPrivate).setOnClickListener(this);
        bottomSheetDialog.findViewById(R.id.llDialogDelete).setOnClickListener(this);
        bottomSheetDialog.findViewById(R.id.llDialogShare).setOnClickListener(this);
        bottomSheetDialog.findViewById(R.id.llDialogRename).setOnClickListener(this);
        bottomSheetDialog.findViewById(R.id.llDialogProperties).setOnClickListener(this);
    }

    private void bindToolbar() {
        //((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        txtToolbarTitle.setText(getResources().getString(R.string.app_name));
    }

    private void bindNameToolbar() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void bindControls() {
        llVideoControl = view.findViewById(R.id.llControlVideo);
        llVideoControl.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            llVideoControl.setElevation(AppUtils.DPtoPX(getActivity(), 40));
        } else {
            ViewCompat.setElevation(llVideoControl, AppUtils.DPtoPX(getActivity(), 40));
        }

        llDeleteVideo = view.findViewById(R.id.llDeleteVideo);
        llDeleteVideo.setOnClickListener(this);

        llShareVideo = view.findViewById(R.id.llShareVideo);
        llShareVideo.setOnClickListener(this);

        llRenameVideo = view.findViewById(R.id.llRenameVideo);
        llRenameVideo.setOnClickListener(this);

        llProperties = view.findViewById(R.id.llProperties);
        llProperties.setOnClickListener(this);

        view.findViewById(R.id.flDummy).setOnClickListener(this);

        rvFolder = view.findViewById(R.id.rvFolder);
        rvFolder.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        rvFolder.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        rvVideo = view.findViewById(R.id.rvVideo);
        rvVideo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        rvVideo.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        mFolderAdapter = new FolderAdapter(getActivity());
        rvFolder.setAdapter(mFolderAdapter);
        mVideoAdapter = new VideoAdapter(getActivity());

        rvVideo.setAdapter(mVideoAdapter);

        srlMedia = view.findViewById(R.id.srlFolder);
        srlMedia.setOnRefreshListener(this);
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    private void fnRenameDialog() {
        final EditText txtRename;
        if (this.isVideo) {
            final Video video = (Video) VIDEO_LIST.get(this.videoListPosition);
            this.mDialog = new Dialog(getActivity(), R.style.CustomDialog);
            this.mDialog.setContentView(R.layout.dialog_rename);
            txtRename = (EditText) this.mDialog.findViewById(R.id.txtRename);
            try {
                final File file = new File(video.getData());
                String s = file.getName().substring(0, file.getName().lastIndexOf("."));
                txtRename.setText(s);
                txtRename.setSelection(s.length());
                this.mDialog.findViewById(R.id.txtDialogCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogDismiss.dismissWithCheck(mDialog);
                    }
                });
                this.mDialog.findViewById(R.id.txtDialogConfirm).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String rename = txtRename.getText().toString();
                        if (rename.isEmpty()) {
                            txtRename.requestFocus();
                            txtRename.setError("Video name can not be empty!");
                            return;
                        }

                        String response = AppUtils.renameFile(getActivity(), video, rename + file.getName().substring(file.getName().lastIndexOf("."), file.getName().length()));
                        if (response != null) {
                            ((Video) VIDEO_LIST.get(videoListPosition)).setData(response);
                            mVideoAdapter.notifyDataSetChanged();
                            DialogDismiss.dismissWithCheck(mDialog);
                            return;
                        }
                        Toast.makeText(getActivity(), "Try different name", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mDialog.show();
        } else {
            Folder folder = (Folder) FOLDER_LIST.get(this.folderListPosition);
            this.mDialog = new Dialog(getActivity(), R.style.CustomDialog);
            this.mDialog.setContentView(R.layout.dialog_rename);
            txtRename = (EditText) this.mDialog.findViewById(R.id.txtRename);
            final String s = folder.getBucketDisplayName();
            try {

                txtRename.setText(s);
                txtRename.setSelection(s.length());
                this.mDialog.findViewById(R.id.txtDialogCancel).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        DialogDismiss.dismissWithCheck(mDialog);
                    }
                });
                this.mDialog.findViewById(R.id.txtDialogConfirm).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String rename = txtRename.getText().toString();
                        if (rename.isEmpty()) {
                            txtRename.requestFocus();
                            txtRename.setError("Folder name can not be empty!");
                            return;
                        }
                        File oldFolder = new File(Environment.getExternalStorageDirectory(), s);
                        File newFolder = new File(Environment.getExternalStorageDirectory(), rename);
                        if (oldFolder.renameTo(newFolder)) {
                            AppUtils.refreshMediaStore(getActivity(), newFolder);
                            ((Folder) FOLDER_LIST.get(folderListPosition)).setBucketDisplayName(rename);
                            mFolderAdapter.notifyDataSetChanged();
                            DialogDismiss.dismissWithCheck(mDialog);
                            Toast.makeText(getActivity(), "Folder Rename Successfully", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Toast.makeText(getActivity(), "Try different name", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        this.mDialog.show();
    }


    private void fnRename() {
        if (isVideo) {
            mSelectedItemList = mVideoAdapter.getSelectedItems();
            final Video video = VIDEO_LIST.get(mSelectedItemList.get(0));

            mDialog = new Dialog(getActivity(), R.style.CustomDialog);
            mDialog.setContentView(R.layout.dialog_rename);

            final EditText txtRename = mDialog.findViewById(R.id.txtRename);

            try {
                final File file = new File(video.getData());
                String s = file.getName().substring(0, file.getName().lastIndexOf("."));
                txtRename.setText(s);
                txtRename.setSelection(s.length());

                mDialog.findViewById(R.id.txtDialogCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogDismiss.dismissWithCheck(mDialog);
                    }
                });

                mDialog.findViewById(R.id.txtDialogConfirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String rename = txtRename.getText().toString();
                        if (!rename.isEmpty()) {
                            rename = rename + file.getName().substring(file.getName().
                                    lastIndexOf("."), file.getName().length());
                            String response = AppUtils.renameFile(getActivity(), video, rename);
                            if (response != null) {
                                VIDEO_LIST.get(mSelectedItemList.get(0)).setData(response);
                                mVideoAdapter.notifyDataSetChanged();
                                mActionMode.finish();
                                DialogDismiss.dismissWithCheck(mDialog);
                            } else
                                Toast.makeText(getActivity(), "Try different name", Toast.LENGTH_SHORT).show();
                        } else {
                            txtRename.requestFocus();
                            txtRename.setError("Video name can not be empty!");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            mDialog.show();
        }
    }


    private void fnPropertiesDialog() {
        if (this.isVideo) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss a", Locale.getDefault());
            Video video = (Video) VIDEO_LIST.get(this.videoListPosition);
            this.mDialog = new Dialog(getActivity(), R.style.CustomDialog);
            this.mDialog.setContentView(R.layout.dialog_video_detail);
            ((TextView) this.mDialog.findViewById(R.id.txtTitle)).setText(new File(video.getData()).getName());
            ((TextView) this.mDialog.findViewById(R.id.txtSize)).setText(AppUtils.getFileSize(video.getSize()));
            ((TextView) this.mDialog.findViewById(R.id.txtDuration)).setText(AppUtils.getDurationString(video.getDuration(), false));
            ((TextView) this.mDialog.findViewById(R.id.txtResolution)).setText(video.getResolution());
            ((TextView) this.mDialog.findViewById(R.id.txtType)).setText(video.getMimeType());
            ((TextView) this.mDialog.findViewById(R.id.txtDate)).setText(dateFormat.format(new Date(video.getDateTaken())));
            ((TextView) this.mDialog.findViewById(R.id.txtLocation)).setText(video.getData());

            this.mDialog.findViewById(R.id.txtDialogClose).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    DialogDismiss.dismissWithCheck(mDialog);
                }
            });
            this.mDialog.show();
            return;
        }

        this.mSelectedItemList = this.mFolderAdapter.getSelectedItems();
        if (this.mSelectedItemList.size() > 1) {
            long videoSize = 0;
            int videoCount = 0;

            for (int i = this.mSelectedItemList.size() - 1; i >= 0; i--) {
                videoSize += ((Folder) FOLDER_LIST.get(((Integer) this.mSelectedItemList.get(i)).intValue())).getSize();
                videoCount += ((Folder) FOLDER_LIST.get(((Integer) this.mSelectedItemList.get(i)).intValue())).getCount();
            }
            this.mDialog = new Dialog(getActivity(), R.style.CustomDialog);
            this.mDialog.setContentView(R.layout.dialog_multi_video_detail);
            ((TextView) this.mDialog.findViewById(R.id.txtContains)).setText(videoCount + " videos");
            ((TextView) this.mDialog.findViewById(R.id.txtSize)).setText(AppUtils.getFileSize(videoSize));
            this.mDialog.findViewById(R.id.txtDialogClose).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    DialogDismiss.dismissWithCheck(mDialog);
                }
            });
            this.mDialog.show();
            return;
        }

        Folder folder = (Folder) FOLDER_LIST.get(this.folderListPosition);
        this.mDialog = new Dialog(getActivity(), R.style.CustomDialog);
        this.mDialog.setContentView(R.layout.dialog_folder_detail);
        ((TextView) this.mDialog.findViewById(R.id.txtDialogTitle)).setText(folder.getBucketDisplayName());
        ((TextView) this.mDialog.findViewById(R.id.txtSize)).setText(AppUtils.getFileSize(folder.getSize()));
        ((TextView) this.mDialog.findViewById(R.id.txtLocation)).setText(folder.getLocation());

        this.mDialog.findViewById(R.id.txtDialogClose).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogDismiss.dismissWithCheck(mDialog);
            }
        });
        this.mDialog.show();
    }

    private void fnProperties() {
        if (isVideo) {
            mSelectedItemList = mVideoAdapter.getSelectedItems();

            if (mSelectedItemList.size() > 1) {
                long videoSize = 0L;
                for (int i = mSelectedItemList.size() - 1; i >= 0; i--) {
                    videoSize += VIDEO_LIST.get(mSelectedItemList.get(i)).getSize();
                }
                mDialog = new Dialog(getActivity(), R.style.CustomDialog);
                mDialog.setContentView(R.layout.dialog_multi_video_detail);

                String temp = mSelectedItemList.size() + "videos";
                ((TextView) mDialog.findViewById(R.id.txtContains)).setText(temp);
                ((TextView) mDialog.findViewById(R.id.txtSize)).setText(AppUtils.getFileSize(videoSize));

                mDialog.findViewById(R.id.txtDialogClose).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogDismiss.dismissWithCheck(mDialog);
                    }
                });
                mDialog.show();
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss a", Locale.getDefault());
                Video video = VIDEO_LIST.get(mSelectedItemList.get(0));
                mDialog = new Dialog(getActivity(), R.style.CustomDialog);
                mDialog.setContentView(R.layout.dialog_video_detail);

                File file = new File(video.getData());

                ((TextView) mDialog.findViewById(R.id.txtTitle)).setText(file.getName());
                ((TextView) mDialog.findViewById(R.id.txtSize)).setText(AppUtils.getFileSize(video.getSize()));
                ((TextView) mDialog.findViewById(R.id.txtDuration)).setText(AppUtils.getDurationString(video.getDuration(), false));
                ((TextView) mDialog.findViewById(R.id.txtResolution)).setText(video.getResolution());
                ((TextView) mDialog.findViewById(R.id.txtType)).setText(video.getMimeType());
                ((TextView) mDialog.findViewById(R.id.txtDate)).setText(dateFormat.format(new Date(video.getDateTaken())));
                ((TextView) mDialog.findViewById(R.id.txtLocation)).setText(video.getData());

                mDialog.findViewById(R.id.txtDialogClose).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogDismiss.dismissWithCheck(mDialog);
                    }
                });
                mDialog.show();
            }
            mActionMode.finish();
        } else {
            mSelectedItemList = mFolderAdapter.getSelectedItems();
            if (mSelectedItemList.size() > 1) {
                long videoSize = 0L;
                int videoCount = 0;
                for (int i = mSelectedItemList.size() - 1; i >= 0; i--) {
                    videoSize += FOLDER_LIST.get(mSelectedItemList.get(i)).getSize();
                    videoCount += FOLDER_LIST.get(mSelectedItemList.get(i)).getCount();
                }
                mDialog = new Dialog(getActivity(), R.style.CustomDialog);
                mDialog.setContentView(R.layout.dialog_multi_video_detail);

                String temp = videoCount + " videos";
                ((TextView) mDialog.findViewById(R.id.txtContains)).setText(temp);
                ((TextView) mDialog.findViewById(R.id.txtSize)).setText(AppUtils.getFileSize(videoSize));

                mDialog.findViewById(R.id.txtDialogClose).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogDismiss.dismissWithCheck(mDialog);
                    }
                });
                mDialog.show();
            } else {
                Folder folder = FOLDER_LIST.get(folderListPosition);
                mDialog = new Dialog(getActivity(), R.style.CustomDialog);
                mDialog.setContentView(R.layout.dialog_folder_detail);

                ((TextView) mDialog.findViewById(R.id.txtDialogTitle)).setText(folder.getBucketDisplayName());
                ((TextView) mDialog.findViewById(R.id.txtSize)).setText(AppUtils.getFileSize(folder.getSize()));
                ((TextView) mDialog.findViewById(R.id.txtLocation)).setText(folder.getLocation());

                mDialog.findViewById(R.id.txtDialogClose).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogDismiss.dismissWithCheck(mDialog);
                    }
                });
                mDialog.show();
            }
            mActionMode.finish();
        }
    }


    private void fnDeleteDialog() {
        if (this.isVideo) {
            this.mDialog = new Dialog(getActivity(), R.style.CustomDialog);
            this.mDialog.setContentView(R.layout.dialog_delete_video);
            ((TextView) this.mDialog.findViewById(R.id.txtWarning)).setText(String.format(getResources().getString(R.string.confirm_delete_dialog), new Object[0]) + new File(((Video) VIDEO_LIST.get(this.videoListPosition)).getData()).getName());
            this.mDialog.findViewById(R.id.txtDialogCancel).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    DialogDismiss.dismissWithCheck(mDialog);
                }
            });
            this.mDialog.findViewById(R.id.txtDialogConfirm).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    DialogDismiss.dismissWithCheck(mDialog);
                    new DeleteVideoDialog().execute();
                }
            });

            this.mDialog.show();
            return;
        }

        this.mDialog = new Dialog(getActivity(), R.style.CustomDialog);
        this.mDialog.setContentView(R.layout.dialog_delete_folder);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(((Folder) FOLDER_LIST.get(this.folderListPosition)).getBucketDisplayName());
        ((TextView) this.mDialog.findViewById(R.id.txtFolders)).setText(stringBuilder.toString());

        this.mDialog.findViewById(R.id.txtDialogCancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                DialogDismiss.dismissWithCheck(mDialog);
            }
        });

        this.mDialog.findViewById(R.id.txtDialogConfirm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogDismiss.dismissWithCheck(mDialog);
                new DeleteFolderDialog().execute(new Void[0]);
            }
        });
        this.mDialog.show();
    }

    private void fnDelete() {
        if (isVideo) {
            mDialog = new Dialog(getActivity(), R.style.CustomDialog);
            mDialog.setContentView(R.layout.dialog_delete_video);

            ((TextView) mDialog.findViewById(R.id.txtWarning)).setText(String.format(getResources().getString(R.string.confirm_delete), mVideoAdapter.getSelectedCount()));

            mDialog.findViewById(R.id.txtDialogCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogDismiss.dismissWithCheck(mDialog);
                }
            });

            mDialog.findViewById(R.id.txtDialogConfirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogDismiss.dismissWithCheck(mDialog);
                    new DeleteVideo().execute();
                }
            });
            mDialog.show();

        } else {
            mDialog = new Dialog(getActivity(), R.style.CustomDialog);
            mDialog.setContentView(R.layout.dialog_delete_folder);

            List<Integer> tempList = mFolderAdapter.getSelectedItems();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(((Folder) FOLDER_LIST.get(((Integer) tempList.get(0)).intValue())).getBucketDisplayName());

            //stringBuilder.append(FOLDER_LIST.get(tempList.get(0)).getBucketDisplayName());
            for (int i = 1; i < tempList.size(); i++) {
                stringBuilder.append(", ");
                stringBuilder.append(FOLDER_LIST.get(tempList.get(i)).getBucketDisplayName());
            }

            ((TextView) mDialog.findViewById(R.id.txtFolders)).setText(stringBuilder.toString());

            mDialog.findViewById(R.id.txtDialogCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogDismiss.dismissWithCheck(mDialog);
                }
            });

            mDialog.findViewById(R.id.txtDialogConfirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogDismiss.dismissWithCheck(mDialog);
                    new DeleteFolder().execute();
                }
            });
            mDialog.show();
        }
    }

    private void bsShow(int position) {
        this.videoListPosition = position;
        String filename = new File(((Video) VIDEO_LIST.get(this.videoListPosition)).getData()).getName();
//        this.bottomSheetBehavior.setState(3);
//        this.txtBottomSheetTitle.setText(filename);
//        this.bottomSheetDialog.show();
    }

    private void bsShow() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.show();
    }

    private void bsDissmiss() {
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        bottomSheetDialog.dismiss();
        poupdialog.dismiss();
    }

    private void bsShowFolder(int position) {
        this.folderListPosition = position;
        String filename = ((Folder) FOLDER_LIST.get(position)).getBucketDisplayName();
//        this.bottomSheetBehavior.setState(3);
//        this.txtBottomSheetTitle.setText(filename);
//        this.bottomSheetDialog.show();
    }

    // Implements On Click Listener
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llProperties: {
                fnProperties();
                break;
            }

            case R.id.llDeleteVideo: {
                fnDelete();
                break;
            }
            case R.id.llShareVideo: {
                new ShareVideo().execute();
                break;
            }
            case R.id.llRenameVideo: {
                fnRename();
                break;
            }
            case R.id.llDialogPrivate: {
                bsDissmiss();
                if (isVideo) {
                    new AddVideoToPrivateDialog().execute();
                } else {
                    new AddFolderToPrivateDialog().execute();
                }
                break;
            }
            case R.id.llDialogDelete: {
                bsDissmiss();
                fnDeleteDialog();
                break;
            }
            case R.id.llDialogShare: {
                bsDissmiss();
                new ShareVideoDialog().execute();
//                showAdmobInter();
                break;
            }
            case R.id.llDialogRename: {
                bsDissmiss();
                fnRenameDialog();
                break;
            }
            case R.id.llDialogProperties: {
                bsDissmiss();
                fnPropertiesDialog();
                break;
            }
        }
    }


    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_video_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        menu.findItem(R.id.menu_lock).setVisible(isVideoList);
        llVideoControl.setVisibility(View.VISIBLE);

        if (isVideo) {
            llRenameVideo.setVisibility(View.VISIBLE);
            llShareVideo.setVisibility(View.VISIBLE);
        } else {
            llRenameVideo.setVisibility(View.GONE);
            llShareVideo.setVisibility(View.GONE);
        }
        view.findViewById(R.id.imgRecentPlayed).setVisibility(View.GONE);
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_select_all: {
                String title;
                if (isVideo) {
                    if (mVideoAdapter.getSelectedCount() == mVideoAdapter.getItemCount()) {
                        mVideoAdapter.selectNone();
                        title = mVideoAdapter.getSelectedCount() + " / " + mVideoAdapter.getItemCount();
                        mActionMode.setTitle(title);
                        mActionMode.finish();
                    } else {
                        mVideoAdapter.selectAll();
                        title = mVideoAdapter.getSelectedCount() + " / " + mVideoAdapter.getItemCount();
                        mActionMode.setTitle(title);
                    }
                } else {
                    if (mFolderAdapter.getSelectedCount() == mFolderAdapter.getItemCount()) {
                        mFolderAdapter.selectNone();
                        title = mFolderAdapter.getSelectedCount() + " / " + mFolderAdapter.getItemCount();
                        mActionMode.setTitle(title);
                        mActionMode.finish();
                    } else {
                        mFolderAdapter.selectAll();
                        title = mFolderAdapter.getSelectedCount() + " / " + mFolderAdapter.getItemCount();
                        mActionMode.setTitle(title);
                    }
                }
                setControlButton();
                return true;
            }
            case R.id.menu_lock: {
                if (isVideo) {
                    new AddVideoToPrivate().execute();
                } else {
                    new AddFolderToPrivate().execute();
                }
                return true;
            }
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
        if (isVideo) {
            mVideoAdapter.clearSelection();
        } else {
            mFolderAdapter.clearSelection();
        }
        llVideoControl.setVisibility(View.GONE);

        String s = SharedPref.getSharedPrefData(getContext(), SharedPref.lastPlayed);
        File file = new File(s);
        if (file.exists()) {
            Drawable placeHolder;
            placeHolder = new ColorDrawable(getActivity().getResources().getColor(android.R.color.black));

            String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                    + " OR " + MediaStore.MediaColumns.MIME_TYPE + "='audio/x-matroska'";

            int column_index = -1;
            String[] projection = {MediaStore.Video.Media.DATA,
                    MediaStore.MediaColumns.SIZE,
                    MediaStore.Video.VideoColumns.DURATION,
                    MediaStore.Video.VideoColumns.RESOLUTION
            };
            Uri uri = Uri.fromFile(new File(s));
            Uri dataUri = MediaStore.Files.getContentUri("external");

            Cursor cursor = getActivity().getContentResolver().query(dataUri, projection, MediaStore.Images.Media.DATA + "=? ",
                    new String[]{file.getAbsolutePath()}, null, null);

//            Cursor cursor = getActivity().getContentResolver().query(uri, projection, selection, null, null);
            if (cursor != null) {
                // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
                // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
                column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                cursor.moveToFirst();
            }
//            try {

            Glide.with(getContext())
                    .asBitmap()
                    .load(Uri.fromFile(new File(cursor.getString(column_index))))
                    .placeholder(placeHolder)
                    .override(150, 150)
                    .into(imgThumb);

            txtDuration.setText(AppUtils.FormatTimeForDisplay(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION)));
            txtTitle.setText((new File(cursor.getString(column_index)).getName()));
            txtResolution.setText(cursor.getColumnIndex(MediaStore.Video.VideoColumns.RESOLUTION));
            txtSize.setText(AppUtils.getFileSize(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)));


            view.findViewById(R.id.imgRecentPlayed).setVisibility(View.VISIBLE);
//            } catch (Exception e) {
//
//            }

        }
    }

    // Implements On Refresh Listener
    @Override
    public void onRefresh() {
        view.findViewById(R.id.flDummy).setVisibility(View.VISIBLE);
        refreshMedia();
    }

    // Implements On Folder Click Listener
    @Override
    public void onClickFolder(View view, int position) {
        if (this.mActionMode != null) {
            myToggleSelection(position);
            setControlButton();
            return;
        }
        this.bucketID = ((Folder) FOLDER_LIST.get(position)).getBucketID();
        this.folderListPosition = position;
        bindNameToolbar();
        this.txtToolbarTitle.setText(((Folder) FOLDER_LIST.get(position)).getBucketDisplayName());
        showAdmobInter();
        new FetchVideoData().execute();
        this.isVideoList = true;
        this.isVideo = true;
        getActivity().invalidateOptionsMenu();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        AnimationUtil.crossFadeViews(this.rvVideo, this.srlMedia, AnimationUtil.ANIMATION_DURATION_SHORT);
//            MenuItem menuItem = custommenu.findItem(R.id.menu_sort);
//            menuItem.setVisible(true);
        getActivity().invalidateOptionsMenu();
    }

    public void showAdmobInter() {

            if(check==1) {
                Intent intent = new Intent(getActivity(), ExoPlayerActivity.class);
                intent.putExtra(Constants.INT_VIDEO_POSITION, pos);
                startActivity(intent);
                check = 0;
            }

    }
    @Override
    public void onClickPopMenuFolder(View view, int position) {
        bsShowFolder(position);
        this.folderListPosition = position;
        String filename = ((Folder) FOLDER_LIST.get(position)).getBucketDisplayName();
        txtBottomSheetTitle.setText(filename);
//        if(isVideo){
//            poupdialog.findViewById(R.id.llDialogPrivate).setVisibility(View.VISIBLE);
//        }else {
        poupdialog.findViewById(R.id.llDialogPrivate).setVisibility(View.GONE);

//        }
        poupdialog.show();
    }

    @Override
    public void onLongClickFolder(View view, int position) {
        if (this.mActionMode == null) {
//            this.folderListPosition = position;
            this.isVideo = false;
            this.mActionMode = getActivity().startActionMode(this);
            this.mFolderAdapter.startSelection();
            myToggleSelection(position);
            setControlButton();
        }
    }

    // Implements On Video Click Listener
    @Override
    public void onClick(View view, int position) {
        if (this.mActionMode != null) {
            myToggleSelection(position);
            setControlButton();
            return;
        }
        check=1;
        pos=position;
        showAdmobInter();
    }

    @Override
    public void onClickPopMenu(View view, int position) {
        bsShow(position);
        String filename = new File(((Video) VIDEO_LIST.get(this.videoListPosition)).getData()).getName();
        txtBottomSheetTitle = poupdialog.findViewById(R.id.txtDialogTitle);
        txtBottomSheetTitle.setText(filename);
        if(isVideo){
            poupdialog.findViewById(R.id.llDialogPrivate).setVisibility(View.VISIBLE);

        }else {
            poupdialog.findViewById(R.id.llDialogPrivate).setVisibility(View.GONE);

        }
        this.poupdialog.show();
    }

    @Override
    public void onLongClick(View view, int position) {
        if (this.mActionMode == null) {
            this.isVideo = true;
            this.mActionMode = getActivity().startActionMode(this);
            this.mVideoAdapter.startSelection();
            myToggleSelection(position);
            setControlButton();
        }
    }

    // User Define Functions
    private void myToggleSelection(int pos) {
        if (isVideo) {
            mVideoAdapter.toggleSelection(pos);
            int count = mVideoAdapter.getSelectedCount();
            if (count == 0) {
                mActionMode.finish();
            } else {
                String title = mVideoAdapter.getSelectedCount() + " / " + mVideoAdapter.getItemCount();
                mActionMode.setTitle(title);
                mActionMode.invalidate();
            }

        } else {
            mFolderAdapter.toggleSelection(pos);
            int count = mFolderAdapter.getSelectedCount();

            if (count == 0) {
                mActionMode.finish();
            } else {
                String title = mFolderAdapter.getSelectedCount() + " / " + mFolderAdapter.getItemCount();
                mActionMode.setTitle(title);
                mActionMode.invalidate();
            }
        }
    }


    private void setControlButton() {
        if (isVideo) {
            if (mVideoAdapter.getSelectedCount() == 0) {
                setButtonEnabled(llRenameVideo, false);
                setButtonEnabled(llProperties, false);
                setButtonEnabled(llDeleteVideo, false);
                setButtonEnabled(llShareVideo, false);
            } else if (mVideoAdapter.getSelectedCount() == 1) {
                setButtonEnabled(llRenameVideo, true);
                setButtonEnabled(llProperties, true);
                setButtonEnabled(llDeleteVideo, true);
                setButtonEnabled(llShareVideo, true);
            } else if (mVideoAdapter.getSelectedCount() > 1) {
                setButtonEnabled(llDeleteVideo, true);
                setButtonEnabled(llProperties, true);
                setButtonEnabled(llShareVideo, true);
                setButtonEnabled(llRenameVideo, false);
            }
        } else {
            if (mFolderAdapter.getSelectedCount() == 0) {
                setButtonEnabled(llRenameVideo, false);
                setButtonEnabled(llProperties, false);
                setButtonEnabled(llDeleteVideo, false);
            } else if (mFolderAdapter.getSelectedCount() == 1) {
                setButtonEnabled(llRenameVideo, true);
                setButtonEnabled(llProperties, true);
                setButtonEnabled(llDeleteVideo, true);

            } else if (mFolderAdapter.getSelectedCount() > 1) {
                setButtonEnabled(llDeleteVideo, true);
                setButtonEnabled(llProperties, true);
                setButtonEnabled(llRenameVideo, false);
            }
        }
    }

    private void setButtonEnabled(View view, boolean enable) {
        view.setEnabled(enable);
        view.setAlpha(enable ? 1.0f : 0.3f);
    }

    private void refreshMedia() {
        String s = SharedPref.getSharedPrefData(getContext(), SharedPref.lastPlayed);
        File file = new File(s);
        new FetchFolderData().execute();
        if (file.exists()) {
            Drawable placeHolder;
            placeHolder = new ColorDrawable(getActivity().getResources().getColor(android.R.color.black));
            String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                    + " OR " + MediaStore.MediaColumns.MIME_TYPE + "='audio/x-matroska'";

            Uri uri = Uri.fromFile(new File(s));
//            Log.println(Log.ASSERT,"sss",s+"");
            int column_index = -1;
            String[] projection = {MediaStore.Video.Media.DATA,
                    MediaStore.Video.VideoColumns.DURATION,
                    MediaStore.Video.VideoColumns.RESOLUTION,
                    MediaStore.MediaColumns.SIZE
            };
          //  Uri dataUri = MediaStore.Files.getContentUri("external");
            Uri dataUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

                    ;

         //   Cursor cursor = getActivity().getContentResolver().query(dataUri, projection, MediaStore.Images.Media.DATA + "=? ",
          ////          new String[]{file.getAbsolutePath()}, null, null);


         Cursor   cursor = getContext().getContentResolver().query(dataUri,  projection, null, null, null);
            //int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //cursor.moveToFirst();


            if (cursor != null) {
                Log.println(Log.ASSERT, "000", "000");
                // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
                // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA

                 column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                //  column_index = cursor
                  //      .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                //cursor.moveToFirst();
            }
            Log.println(Log.ASSERT, "111", String.valueOf(uri));


//            try {


            Glide.with(getContext())
                    .asBitmap()
                    .load(Uri.fromFile(new File(cursor.getString(column_index))))
                    .placeholder(placeHolder)
                    .override(150, 150)
                    .into(imgThumb);

            txtDuration.setText(AppUtils.FormatTimeForDisplay(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION))));
            txtTitle.setText((new File(cursor.getString(column_index)).getName()));
            txtResolution.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.RESOLUTION)));
            txtSize.setText(AppUtils.getFileSize(cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE))));


            view.findViewById(R.id.imgRecentPlayed).setVisibility(View.VISIBLE);
//            } catch (Exception e) {
//
//            }

        }
    }


    private class DeleteFolderDialog extends AsyncTask<Void, Void, Void> {
        Folder folder;

        private DeleteFolderDialog() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... voids) {
            this.folder = (Folder) FOLDER_LIST.get(folderListPosition);
            Iterator it = MEDIA_LIST.iterator();
            try {
                while (it.hasNext()) {
                    Video video = (Video) it.next();
                    if (video.getBucketID().equals(this.folder.getBucketID()) && AppUtils.deleteFile(getActivity(), video.getId(), video.getData())) {
                        MEDIA_LIST.remove(video);
                    }
                }
            }catch (Exception e) {
                return null;

            }

            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            FOLDER_LIST.remove(this.folder);
            Toast.makeText(getActivity(), "Folder Delete successful", Toast.LENGTH_LONG).show();
            mFolderAdapter.notifyDataSetChanged();
        }
    }

    private class DeleteVideoDialog extends AsyncTask<Void, Void, Void> {

        private DeleteVideoDialog() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... voids) {
            try {
                int count = ((Folder) FOLDER_LIST.get(folderListPosition)).getCount();
                long size = ((Folder) FOLDER_LIST.get(folderListPosition)).getSize();
                int position = videoListPosition;
                Video video = (Video) VIDEO_LIST.get(position);
                if (AppUtils.deleteFile(getActivity(), video.getId(), video.getData())) {
                    size -= video.getSize();
                    VIDEO_LIST.remove(position);
                    MEDIA_LIST.remove(video);
                    count--;
                }
                ((Folder) FOLDER_LIST.get(folderListPosition)).setCount(count);
                ((Folder) FOLDER_LIST.get(folderListPosition)).setSize(size);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mActionMode != null) {
                mActionMode.finish();
            }
            Toast.makeText(getActivity(), "Delete successful", Toast.LENGTH_LONG).show();
            mVideoAdapter.notifyDataSetChanged();
            if (mFolderAdapter != null) {
                mFolderAdapter.notifyDataSetChanged();
            }
        }
    }

    private class FetchFolderData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MEDIA_LIST.clear();
            FOLDER_LIST.clear();
            VIDEO_LIST.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                AppUtils.fetchVideoData(getContext());
                AppUtils.fetchAlbumData(getContext());
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            AppUtils.LOG("size\tM - " + MEDIA_LIST.size() + "\tF - " + FOLDER_LIST.size() + "\tV - " + VIDEO_LIST.size());
            if (FOLDER_LIST.size() < 1) {
                view.findViewById(R.id.llNoMedia).setVisibility(View.VISIBLE);
            }
            if (srlMedia != null) {
                srlMedia.setRefreshing(false);
            }
            if (mFolderAdapter == null) {
                mFolderAdapter = new FolderAdapter(getActivity());
                rvFolder.setAdapter(mFolderAdapter);
            } else {
                mFolderAdapter.notifyDataSetChanged();
            }
            view.findViewById(R.id.flDummy).setVisibility(View.GONE);
        }
    }

    private class FetchVideoData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            VIDEO_LIST.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (Video video : MEDIA_LIST) {
                if (bucketID.equals(video.getBucketID())) {
                    VIDEO_LIST.add(video);
                }
            }
            AppUtils.sortVideo(getActivity());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mVideoAdapter == null) {
                mVideoAdapter = new VideoAdapter(getActivity());
                rvVideo.setAdapter(mVideoAdapter);
            } else {
                mVideoAdapter.notifyDataSetChanged();
            }
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
                File file = new File(((Video) VIDEO_LIST.get(MainFragment.this.videoListPosition)).getData());
                try {
                    this.shareList.add(FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", file));
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
            if (mActionMode != null) {
                mActionMode.finish();
            }
            AppUtils.shareMultipleFile(getActivity(), this.shareList);
        }
    }

    private class ShareVideo extends AsyncTask<Void, Void, Void> {
        private ArrayList shareList = new ArrayList();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            shareList.clear();
            mSelectedItemList = mVideoAdapter.getSelectedItems();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (int i = 0; i < mSelectedItemList.size(); i++) {
                    File file = new File(VIDEO_LIST.get(mSelectedItemList.get(i)).getData());
                    try {
                        shareList.add(FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", file));
                    } catch (Exception e) {
                        e.printStackTrace();
                        shareList.add(Uri.fromFile(file));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mActionMode != null) {
                mActionMode.finish();
            }
            AppUtils.shareMultipleFile(getActivity(), shareList);
        }
    }

    private class DeleteVideo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSelectedItemList = mVideoAdapter.getSelectedItems();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                int count = FOLDER_LIST.get(folderListPosition).getCount();
                long size = FOLDER_LIST.get(folderListPosition).getSize();
                int position;
                for (int i = mSelectedItemList.size() - 1; i >= 0; i--) {
                    position = mSelectedItemList.get(i);
                    Video video = VIDEO_LIST.get(position);
                    if (AppUtils.deleteFile(getActivity(), video.getId(), video.getData())) {
                        size -= video.getSize();
                        VIDEO_LIST.remove(position);
                        MEDIA_LIST.remove(video);
                        count--;
                    }
                }
                FOLDER_LIST.get(folderListPosition).setCount(count);
                FOLDER_LIST.get(folderListPosition).setSize(size);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mActionMode != null) {
                mActionMode.finish();
            }
            Toast.makeText(getActivity(), "Delete successful", Toast.LENGTH_SHORT).show();
            mVideoAdapter.notifyDataSetChanged();
            if (mFolderAdapter != null) {
                mFolderAdapter.notifyDataSetChanged();
            }
        }
    }

    private class DeleteFolder extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSelectedItemList = mFolderAdapter.getSelectedItems();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = mSelectedItemList.size() - 1; i >= 0; i--) {
                Folder folder = FOLDER_LIST.get(mSelectedItemList.get(i));
                for (Video video : MEDIA_LIST) {
                    if (video.getBucketID().equals(folder.getBucketID())) {
                        if (AppUtils.deleteFile(getActivity(), video.getId(), video.getData())) {
                            MEDIA_LIST.remove(video);
                        }
                    }
                }
                FOLDER_LIST.remove(folder);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mActionMode != null) {
                mActionMode.finish();
            }
            Toast.makeText(getActivity(), "Delete successful", Toast.LENGTH_SHORT).show();
            mFolderAdapter.notifyDataSetChanged();
        }
    }

    private class AddVideoToPrivateDialog extends AsyncTask<Void, Void, Void> {
        Dialog dialog;
        DBHelper dbHelper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dbHelper = new DBHelper(getActivity());
            mSelectedItemList = mVideoAdapter.getSelectedItems();

            dialogPrivate.setCanceledOnTouchOutside(false);
            dialogPrivate.setCancelable(false);
            dialogPrivate.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                int count = FOLDER_LIST.get(folderListPosition).getCount();
                long size = FOLDER_LIST.get(folderListPosition).getSize();
                int position = videoListPosition;
                Video video = (Video) VIDEO_LIST.get(position);
                File from = new File(video.getData());
                File to = new File(from.getParent(), new RandomStringGenerator(30).nextString());
                if (AppUtils.copyFileToOther(from.getAbsolutePath(), to.getAbsolutePath()) && to.exists() && AppUtils.deleteFile(getActivity(), video.getId(), video.getData())) {
                    this.dbHelper.addToPrivate(video, to.getAbsolutePath());
                    size -= video.getSize();
                    VIDEO_LIST.remove(position);
                    MEDIA_LIST.remove(video);
                    count--;
                }
                ((Folder) FOLDER_LIST.get(folderListPosition)).setCount(count);
                ((Folder) FOLDER_LIST.get(folderListPosition)).setSize(size);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

            /*try {
                for (int i = mSelectedItemList.size() - 1; i >= 0; i--) {
                    Video video= Constants.VIDEO_LIST.get(mSelectedItemList.get(i));

                    File from = new File(video.getData());
                    File to = new File(from.getParent(), new RandomStringGenerator(30).nextString());
                    if (AppUtils.copyFileToOther(from.getAbsolutePath(), to.getAbsolutePath()) && to.exists() && AppUtils.deleteFile(getActivity(), video.getId(), video.getData())) {
                        this.dbHelper.addToPrivate(video, to.getAbsolutePath());
                        Constants.VIDEO_LIST.remove(video);
                        Constants.MEDIA_LIST.remove(video);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;*/
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DialogDismiss.dismissWithCheck(dialogPrivate);
            dbHelper.close();
            if (mActionMode != null) {
                mActionMode.finish();
            }
            Toast.makeText(getActivity(), "Added to private", Toast.LENGTH_SHORT).show();
            mVideoAdapter.notifyDataSetChanged();
        }
    }

    private class AddFolderToPrivateDialog extends AsyncTask<Void, Void, Void> {
        DBHelper dbHelper;
        Dialog dialog;

        private AddFolderToPrivateDialog() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.dbHelper = new DBHelper(getActivity());
            dialogPrivate.setCanceledOnTouchOutside(false);
            dialogPrivate.setCancelable(false);
            dialogPrivate.show();
        }

        protected Void doInBackground(Void... voids) {
            try {
                Folder folder = (Folder) FOLDER_LIST.get(folderListPosition);
                Iterator it = MEDIA_LIST.iterator();
                while (it.hasNext()) {
                    Video video = (Video) it.next();
                    if (video.getBucketID().equals(folder.getBucketID())) {
                        File from = new File(video.getData());
                        File to = new File(from.getParent(), new RandomStringGenerator(30).nextString());
                        if (AppUtils.copyFileToOther(from.getAbsolutePath(), to.getAbsolutePath()) && to.exists() && AppUtils.deleteFile(getActivity(), video.getId(), video.getData())) {
                            this.dbHelper.addToPrivate(video, to.getAbsolutePath());
                            MEDIA_LIST.remove(video);
                        }
                    }
                }
                FOLDER_LIST.remove(folder);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DialogDismiss.dismissWithCheck(dialogPrivate);
            this.dbHelper.close();
            Toast.makeText(getActivity(), "Folder Added to private", Toast.LENGTH_LONG).show();
            mVideoAdapter.notifyDataSetChanged();
            mFolderAdapter.notifyDataSetChanged();
        }
    }

    private class AddVideoToPrivate extends AsyncTask<Void, Void, Void> {
        Dialog dialog;
        DBHelper dbHelper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dbHelper = new DBHelper(getActivity());
            mSelectedItemList = mVideoAdapter.getSelectedItems();

            dialogPrivate.setCanceledOnTouchOutside(false);
            dialogPrivate.setCancelable(false);
            dialogPrivate.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (int i = mSelectedItemList.size() - 1; i >= 0; i--) {
                    Video video = VIDEO_LIST.get(mSelectedItemList.get(i));

                    File from = new File(video.getData());
                    File to = new File(from.getParent(), new RandomStringGenerator(30).nextString());
                    if (AppUtils.copyFileToOther(from.getAbsolutePath(), to.getAbsolutePath()) && to.exists() && AppUtils.deleteFile(getActivity(), video.getId(), video.getData())) {
                        this.dbHelper.addToPrivate(video, to.getAbsolutePath());
                        VIDEO_LIST.remove(video);
                        MEDIA_LIST.remove(video);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DialogDismiss.dismissWithCheck(dialogPrivate);
            dbHelper.close();
            if (mActionMode != null) {
                mActionMode.finish();
            }
            Toast.makeText(getActivity(), "Added to private", Toast.LENGTH_SHORT).show();
            mVideoAdapter.notifyDataSetChanged();
        }
    }

    private class AddFolderToPrivate extends AsyncTask<Void, Void, Void> {
        Dialog dialog;
        DBHelper dbHelper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dbHelper = new DBHelper(getActivity());

            mSelectedItemList = mFolderAdapter.getSelectedItems();

            dialogPrivate.setCanceledOnTouchOutside(false);
            dialogPrivate.setCancelable(false);
            dialogPrivate.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (int i = mSelectedItemList.size() - 1; i >= 0; i--) {
                    Folder folder = FOLDER_LIST.get(mSelectedItemList.get(i));
                    for (Video video : MEDIA_LIST) {
                        if (video.getBucketID().equals(folder.getBucketID())) {
                            File from = new File(video.getData());
                            File to = new File(from.getParent(), (new RandomStringGenerator(30)).nextString());

                            if (AppUtils.copyFileToOther(from.getAbsolutePath(), to.getAbsolutePath())) {
                                if (to.exists()) {
                                    if (AppUtils.deleteFile(getActivity(), video.getId(), video.getData())) {
                                        dbHelper.addToPrivate(video, to.getAbsolutePath());
                                        MEDIA_LIST.remove(video);
                                    }
                                }
                            }
                        }
                    }
                    FOLDER_LIST.remove(folder);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DialogDismiss.dismissWithCheck(dialogPrivate);
            dbHelper.close();
            if (mActionMode != null) {
                mActionMode.finish();
            }
            Toast.makeText(getActivity(), "Added to private", Toast.LENGTH_SHORT).show();
            mVideoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
//        if (nativeAd != null) {
//            nativeAd.destroy();
//        }
        super.onDestroy();
    }

}
