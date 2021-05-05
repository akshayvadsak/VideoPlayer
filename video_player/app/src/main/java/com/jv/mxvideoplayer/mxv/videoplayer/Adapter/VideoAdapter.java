package com.jv.mxvideoplayer.mxv.videoplayer.Adapter;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdListener;
//import com.facebook.ads.AdView;
import com.bumptech.glide.Glide;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.AppUtils;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.Constants;
import com.jv.mxvideoplayer.mxv.videoplayer.MainFragment;
//import com.jv.mxvideoplayer.mxv.videoplayer.MainPackage.GlideApp;
import com.jv.mxvideoplayer.mxv.videoplayer.R;
import com.jv.mxvideoplayer.mxv.videoplayer.model.Video;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private Drawable placeHolder;
    private SparseBooleanArray selectedItems;
    private boolean isSelectionMode = false;
    public ClickListener clickListener;


    public interface ClickListener {
        void onClick(View view, int i);

        void onClickPopMenu(View view, int i);

        void onLongClick(View view, int i);
    }

    // Constructor

    public VideoAdapter(Activity activity) {
        this.activity = activity;

        if (Constants.VIDEO_LIST == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        placeHolder = new ColorDrawable(activity.getResources().getColor(R.color.colorAccent));
        selectedItems = new SparseBooleanArray();
    }

    // Super Class Methods

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity)
                .inflate(R.layout.adapter_video_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        if (position > 0 && position % 4 == 0 || position == 0) {
            int index = getRealPosition(position);
//
            ((ViewHolder) holder).bindVideoad(index);

        } else {

            int index = getRealPosition(position);
//            if (index != 0) {
//                index--;
//            }
            Video video = Constants.VIDEO_LIST.get(index);
            ((ViewHolder) holder).bindVideo(video, index);


        }

    }

    private int getRealPosition(int position) {
        if (position == 0) {
            return position;
        } else {
            return (position - position / 4)-1;
        }
    }

    @Override
    public int getItemCount() {
        int additionalContent = 0;
        if (Constants.VIDEO_LIST.size() > 0 && 4 > 0 && Constants.VIDEO_LIST.size() >= 4) {
            additionalContent = Constants.VIDEO_LIST.size() / 4;
        }


//        Log.println(Log.ASSERT,"aaa",Constants.VIDEO_LIST.size() + additionalContent+"");
        return Constants.VIDEO_LIST.size() + additionalContent+1;
    }

//    @Override
//    public int getItemCount() {
//
//        return Constants.VIDEO_LIST.size();
//
//    }

    // User Define Methods

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(activity, view);
        popup.getMenuInflater().inflate(R.menu.menu_main_setting, popup.getMenu());
        popup.show();
    }

    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    public void selectAll() {
        for (int i = 0; i < getItemCount(); i++) {
            if (!selectedItems.get(i, false)) {
                selectedItems.put(i, true);
            }
        }
        notifyDataSetChanged();
    }

    public void selectNone() {
        for (int i = 0; i < getItemCount(); i++) {
            if (selectedItems.get(i, false)) {
                selectedItems.delete(i);
            }
        }
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return selectedItems.size();
    }

    public void startSelection() {
        this.isSelectionMode = true;
        notifyDataSetChanged();
    }

    public void clearSelection() {
        selectedItems.clear();
        this.isSelectionMode = false;
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        AppUtils.LOG(String.valueOf(items.size()));
        return items;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumb, imgMenu;
        TextView txtDuration, txtTitle, txtResolution, txtSize;
        CheckBox checkBoxVideo;
        ConstraintLayout mainlayout;
        CardView videothumb;
        LinearLayout akhu;

        LinearLayout ll_ad_container;
        LinearLayout adContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            akhu = itemView.findViewById(R.id.aakhu);
            ll_ad_container = itemView.findViewById(R.id.ll_ad_container);

            checkBoxVideo = itemView.findViewById(R.id.checkboxVideo);
            imgThumb = itemView.findViewById(R.id.imgThumb);
            txtDuration = itemView.findViewById(R.id.txtDuration);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtResolution = itemView.findViewById(R.id.txtResolution);
            txtSize = itemView.findViewById(R.id.txtSize);
            imgMenu = itemView.findViewById(R.id.imgMenu);
            mainlayout = (ConstraintLayout) itemView.findViewById(R.id.mainlayout);
            videothumb = (CardView) itemView.findViewById(R.id.videothumb);
            adContainer = (LinearLayout) itemView.findViewById(R.id.ll_ad_container1);

        }


        public void bindVideoad(int position) {

            akhu.setVisibility(View.GONE);
            ll_ad_container.setVisibility(View.VISIBLE);
//            AdView adView = new AdView(activity, "190108885922911_190110939256039", com.facebook.ads.AdSize.BANNER_HEIGHT_50);
//            adContainer.addView(adView);
//            AdListener adListener = new AdListener() {
//                @Override
//                public void onError(Ad ad, AdError adError) {
//
//                    Log.e("Erro", "Error" + adError.getErrorMessage());
//                }
//
//                @Override
//                public void onAdLoaded(Ad ad) {
//                    Log.e("onAdLoaded", "onAdLoaded");
//                }
//
//                @Override
//                public void onAdClicked(Ad ad) {
//                    Log.e("onAdClicked", "onAdClicked");
//                }
//
//                @Override
//                public void onLoggingImpression(Ad ad) {
//                    Log.e("onLogging", "onLogging");
//                }
//            };
//            adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());


//            if(position==0 || position ==-1){
//                com.google.android.gms.ads.AdView adView1 = new com.google.android.gms.ads.AdView(activity);
//                adView1.setAdSize(AdSize.LARGE_BANNER);
//                adView1.setAdUnitId(Utilitie.admobbanner1);
//                AdRequest adRequest1 = new AdRequest.Builder().build();
//                adView1.loadAd(adRequest1);
//                if (ll_ad_container != null) {
//                    ll_ad_container.removeAllViews();
//                }
//                ll_ad_container.addView(adView1);
//            }else {
//                com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(activity);
//                adView.setAdSize(AdSize.BANNER);
//                adView.setAdUnitId(Utilitie.admobbanner);
//                AdRequest adRequest = new AdRequest.Builder().build();
//                adView.loadAd(adRequest);
//                if (ll_ad_container != null) {
//                     ll_ad_container.removeAllViews();
//                }
//                ll_ad_container.addView(adView);

//            }
        }

        public void bindVideo(Video video, final int position) {
            ll_ad_container.setVisibility(View.GONE);
            akhu.setVisibility(View.VISIBLE);
            try {
                Glide.with(activity)
                        .asBitmap()
                        .load(Uri.fromFile(new File(video.getData())))
                        .placeholder(placeHolder)
                        .override(150, 150)
                        .into(imgThumb);

                txtDuration.setText(AppUtils.FormatTimeForDisplay(video.getDuration()));
                txtTitle.setText((new File(video.getData()).getName()));
                txtResolution.setText("Modified :" + getDate(video.getDateTaken()) + "");
                txtSize.setText(AppUtils.getFileSize(video.getSize()));

                imgMenu.setVisibility(isSelectionMode ? View.GONE : View.VISIBLE);
                imgMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupMenu(v);
                    }

                });

                checkBoxVideo.setVisibility(isSelectionMode ? View.VISIBLE : View.GONE);
                checkBoxVideo.setChecked(selectedItems.get(position, false));

                imgMenu.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        VideoAdapter.this.clickListener = MainFragment.clickListener;
                        VideoAdapter.this.clickListener.onClickPopMenu(v, position);
                    }
                });
                videothumb.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        VideoAdapter.this.clickListener = MainFragment.clickListener;
                        VideoAdapter.this.clickListener.onLongClick(view, position);
                        return false;
                    }
                });
                mainlayout.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        VideoAdapter.this.clickListener = MainFragment.clickListener;
                        VideoAdapter.this.clickListener.onLongClick(view, position);
                        return false;
                    }
                });
                mainlayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        VideoAdapter.this.clickListener = MainFragment.clickListener;
                        VideoAdapter.this.clickListener.onClick(view, position);
                    }
                });
                videothumb.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        VideoAdapter.this.clickListener = MainFragment.clickListener;
                        VideoAdapter.this.clickListener.onClick(view, position);
                    }
                });

                checkBoxVideo.setChecked(selectedItems.get(position, false));
                checkBoxVideo.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        VideoAdapter.this.clickListener = MainFragment.clickListener;
                        VideoAdapter.this.clickListener.onClick(view, position);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public String getDate(long val) {
        val *= 1000L;
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(val));
    }
}