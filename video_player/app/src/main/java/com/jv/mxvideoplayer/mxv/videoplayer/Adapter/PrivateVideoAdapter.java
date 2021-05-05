package com.jv.mxvideoplayer.mxv.videoplayer.Adapter;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jv.mxvideoplayer.mxv.videoplayer.Acitivity.PrivateActivity;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.AppUtils;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.Constants;
//import com.jv.mxvideoplayer.mxv.videoplayer.MainPackage.GlideApp;
import com.jv.mxvideoplayer.mxv.videoplayer.R;
import com.jv.mxvideoplayer.mxv.videoplayer.model.PrivateVideo;


/**
 * Created by jigs patel on 08-12-2019.
 */

public class PrivateVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public String getDate(long val){
        val*=1000L;
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(val));
    }
    private Activity activity;
    private Drawable placeHolder;
    private SparseBooleanArray selectedItems;
    private boolean isSelectionMode = false;

    private int POST_TYPE = 1;
    private int AD_TYPE = 2;
    public static int AD_FORM = 2;
    public static boolean mIsAdChoiceDisplayed = false;

    public ClickListener clickListener;

    public interface ClickListener {
        void onClick(View view, int i);

        void onClickPopMenu(View view, int i);

        void onLongClick(View view, int i);
    }

    public PrivateVideoAdapter(Activity activity) {
        this.activity = activity;
        if (Constants.PRIVATE_VIDEO_LIST == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        placeHolder = new ColorDrawable(activity.getResources().getColor(android.R.color.black));
        selectedItems = new SparseBooleanArray();
    }

    // Supper Class Methods
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(activity).inflate(R.layout.adapter_private_video_item, parent, false);
            return new ViewHolder(view);



    }

    @Override
    public int getItemViewType(int position) {
        /*if (position == 1) {
            return AD_TYPE;
        } else {
            return POST_TYPE;
        }*/
        return POST_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);

            int index = position;
            /*if (index != 0) {
                index--;
            }*/
            PrivateVideo video = Constants.PRIVATE_VIDEO_LIST.get(index);
            ((ViewHolder) holder).bindVideo(video, index);

    }



    @Override
    public int getItemCount() {
        /*if (Constants.PRIVATE_VIDEO_LIST.size() == 0) {
            return Constants.PRIVATE_VIDEO_LIST.size();
        } else {
            return Constants.PRIVATE_VIDEO_LIST.size() + 1;
        }*/
        return Constants.PRIVATE_VIDEO_LIST.size();
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

    // View Holder Classes

    public class AdHolder extends RecyclerView.ViewHolder {
        private ImageView mAdIcon;
        private TextView mAdTitle;
        private TextView mAdSocialContext;
        private Button mAdCallToAction;
        private LinearLayout adChoicesContainer;
//        private LinearLayout native_ad_unit;

        public AdHolder(View view) {
            super(view);

//            native_ad_unit = (LinearLayout) view.findViewById(R.id.ll_header);
            mAdTitle = (TextView) view.findViewById(R.id.native_ad_title);
            mAdSocialContext = (TextView) view.findViewById(R.id.native_ad_social_context);
            mAdCallToAction = (Button) view.findViewById(R.id.native_ad_call_to_action);
            mAdIcon = (ImageView) view.findViewById(R.id.native_ad_icon);
            adChoicesContainer = (LinearLayout) view.findViewById(R.id.ad_choices_container);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumb, imgMenu;
        TextView txtDuration, txtTitle, txtResolution, txtSize;
        CheckBox checkBoxVideo;
        ConstraintLayout mainlayout;
        CardView videothumb;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBoxVideo = itemView.findViewById(R.id.checkboxVideo);
            imgThumb = itemView.findViewById(R.id.imgThumb);
            txtDuration = itemView.findViewById(R.id.txtDuration);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtResolution = itemView.findViewById(R.id.txtResolution);
            txtSize = itemView.findViewById(R.id.txtSize);
            imgMenu = itemView.findViewById(R.id.imgMenu);
            mainlayout = (ConstraintLayout) itemView.findViewById(R.id.mainlayout);
            videothumb = (CardView) itemView.findViewById(R.id.videothumb);
        }

        public void bindVideo(PrivateVideo video,final int position) {
            try {

                Glide.with(activity)
                        .asBitmap()
                        .load(Uri.fromFile(new File(video.getNewPath())))
                        .placeholder(placeHolder)
                        .override(150, 150)
                        .into(imgThumb);

                txtDuration.setText(AppUtils.FormatTimeForDisplay(video.getDuration()));
                txtTitle.setText((new File(video.getData()).getName()));
//                txtResolution.setText(video.getResolution());
//                txtResolution.setText("Modified :"+video.getDateTaken()+"");
                txtResolution.setText("Modified :"+getDate(video.getDateTaken())+"");

                txtSize.setText(AppUtils.getFileSize(video.getSize()));

                imgMenu.setVisibility(isSelectionMode ? View.GONE : View.VISIBLE);

                checkBoxVideo.setVisibility(isSelectionMode ? View.VISIBLE : View.GONE);
                checkBoxVideo.setChecked(selectedItems.get(position, false));
                imgMenu.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        clickListener = PrivateActivity.clickListener;
                        clickListener.onClickPopMenu(v, position);
                    }
                });
                videothumb.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        clickListener = PrivateActivity.clickListener;
                        clickListener.onLongClick(view, position);
                        return false;
                    }
                });
                mainlayout.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        clickListener = PrivateActivity.clickListener;
                        clickListener.onLongClick(view, position);
                        return false;
                    }
                });
                mainlayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        clickListener = PrivateActivity.clickListener;
                        clickListener.onClick(view, position);
                    }
                });
                videothumb.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        clickListener = PrivateActivity.clickListener;
                        clickListener.onClick(view, position);
                    }
                });

                checkBoxVideo.setChecked(selectedItems.get(position, false));
                checkBoxVideo.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        clickListener = PrivateActivity.clickListener;
                        clickListener.onClick(view, position);
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
