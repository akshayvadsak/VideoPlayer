package com.jv.mxvideoplayer.mxv.videoplayer.Adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.AppUtils;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.Constants;
import com.jv.mxvideoplayer.mxv.videoplayer.MainFragment;
import com.jv.mxvideoplayer.mxv.videoplayer.R;

/**
 * Created by jigs patel on 08-12-2019.
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private Context context;
    private SparseBooleanArray selectedItems;
    private boolean isSelectionMode = false;
    public FolderClickListener folderClickListener;

    public interface FolderClickListener {
        void onClickFolder(View view, int i);

        void onClickPopMenuFolder(View view, int i);

        void onLongClickFolder(View view, int i);
    }


    public FolderAdapter(Context context) {
        this.context = context;
        if (Constants.FOLDER_LIST == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.adapter_folder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

//        if (position > 0 && position % 4 == 0  || position == 0) {
//
//
//            holder.akhu.setVisibility(View.GONE);
//            holder.ll_ad_container.setVisibility(View.VISIBLE);
//
//            if(position==0){
////                com.google.android.gms.ads.AdView adView1 = new com.google.android.gms.ads.AdView(context);
////                adView1.setAdSize(AdSize.LARGE_BANNER);
////                adView1.setAdUnitId(Utilitie.admobbanner1);
////                AdRequest adRequest1 = new AdRequest.Builder().build();
////                adView1.loadAd(adRequest1);
////                if (holder.ll_ad_container != null) {
////                    holder. ll_ad_container.removeAllViews();
////                }
////                holder.ll_ad_container.addView(adView1);
//            }else {
////                com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(context);
////                adView.setAdSize(AdSize.BANNER);
////                adView.setAdUnitId(Utilitie.admobbanner);
////                AdRequest adRequest = new AdRequest.Builder().build();
////                adView.loadAd(adRequest);
////                if (holder.ll_ad_container != null) {
////                    holder. ll_ad_container.removeAllViews();
////                }
////                holder.ll_ad_container.addView(adView);
//
//            }
//
//
//        } else {
            holder.ll_ad_container.setVisibility(View.GONE);
            holder.akhu.setVisibility(View.VISIBLE);


            try {
                holder.txtFolderName.setText(Constants.FOLDER_LIST.get(position).getBucketDisplayName());
                holder.txtVideoCount.setText(String.valueOf(Constants.FOLDER_LIST.get(position).getCount()).concat(" videos"));
                holder.txtFolderSize.setText(AppUtils.getFileSize(Constants.FOLDER_LIST.get(position).getSize()));

                holder.imgMenu.setVisibility(isSelectionMode ? View.GONE : View.VISIBLE);
                holder.imgMenu.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        FolderAdapter.this.folderClickListener = MainFragment.folderClickListener;
                        FolderAdapter.this.folderClickListener.onClickPopMenuFolder(v, position);
                    }
                });
                holder.mainlayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        FolderAdapter.this.folderClickListener = MainFragment.folderClickListener;
                        FolderAdapter.this.folderClickListener.onClickFolder(view, position);
                    }
                });
                holder.mainlayout.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        FolderAdapter.this.folderClickListener = MainFragment.folderClickListener;
                        FolderAdapter.this.folderClickListener.onLongClickFolder(view, position);
                        return false;
                    }
                });
                holder.imgFolder.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        FolderAdapter.this.folderClickListener = MainFragment.folderClickListener;
                        FolderAdapter.this.folderClickListener.onClickFolder(view, position);
                    }
                });
                holder.imgFolder.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        FolderAdapter.this.folderClickListener = MainFragment.folderClickListener;
                        FolderAdapter.this.folderClickListener.onLongClickFolder(view, position);
                        return false;
                    }
                });


                holder.checkBoxFolder.setVisibility(isSelectionMode ? View.VISIBLE : View.GONE);

                holder.checkBoxFolder.setChecked(selectedItems.get(position, false));
                holder.checkBoxFolder.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        FolderAdapter.this.folderClickListener = MainFragment.folderClickListener;
                        FolderAdapter.this.folderClickListener.onClickFolder(view, position);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
//        }
    }
//
//    private int getRealPosition(int position) {
//        if (4 == 0) {
//            return position-1;
//        } else {
//            return (position - position / 4)-1;
//        }
//    }

    @Override
    public int getItemCount() {
//        int additionalContent = 0;
//        if (Constants.FOLDER_LIST.size() > 0 && 4 > 0 && Constants.FOLDER_LIST.size() > 4) {
//            additionalContent = Constants.FOLDER_LIST.size() / 4;
//        }


//        Log.println(Log.ASSERT,"aaa",Constants.FOLDER_LIST.size() + additionalContent+"");
        return Constants.FOLDER_LIST.size() ;
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
        return items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_ad_container;
        LinearLayout akhu;

        TextView txtFolderName, txtVideoCount, txtFolderSize;
        CheckBox checkBoxFolder;
        ImageView imgMenu;
        LinearLayout mainlayout;
        ImageView imgFolder;

        public ViewHolder(View itemView) {
            super(itemView);
            akhu = itemView.findViewById(R.id.aakhu);
            ll_ad_container = itemView.findViewById(R.id.ll_ad_container);
            mainlayout = itemView.findViewById(R.id.mainlayout);
            imgFolder = itemView.findViewById(R.id.imgFolder);
            txtFolderName = itemView.findViewById(R.id.txtFolderName);
            txtVideoCount = itemView.findViewById(R.id.txtVideoCount);
            txtFolderSize = itemView.findViewById(R.id.txtFolderSize);
            checkBoxFolder = itemView.findViewById(R.id.checkboxFolder);
            imgMenu = itemView.findViewById(R.id.imgMenu);
        }
    }
}
