package com.jv.mxvideoplayer.mxv.videoplayer.Acitivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.List;

import dmax.dialog.SpotsDialog;
import com.jv.mxvideoplayer.mxv.videoplayer.Adapter.PrivateVideoAdapter;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.AppUtils;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.Constants;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.DBHelper;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.DialogDismiss;
import com.jv.mxvideoplayer.mxv.videoplayer.R;
import com.jv.mxvideoplayer.mxv.videoplayer.model.PrivateVideo;

public class PrivateActivity extends AppCompatActivity implements View.OnClickListener, ActionMode.Callback, PrivateVideoAdapter.ClickListener {

    private Activity activity = PrivateActivity.this;
    private RecyclerView rvPrivateVideo;
    private PrivateVideoAdapter mPrivateVideoAdapter;

    private ActionMode mActionMode;
    private List<Integer> mSelectedItemList;
    LinearLayout theme;

    // Bottom Sheet
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView txtBottomSheetTitle;

    private int mSelectedPosition = 0;
    public static PrivateVideoAdapter.ClickListener clickListener;

    //SpotsDialog
    SpotsDialog removeprivateDialog;
    SpotsDialog deleteVideoDialog;
    private Dialog popupdialog;

    public void theme() {
        theme = findViewById(R.id.theme);
        SharedPreferences settings = getSharedPreferences("pref", 0);
        int id = settings.getInt("theme", 3);
        if (theme != null) {
            if (id == 1) {
                theme.setBackgroundResource(R.drawable.theme2);
            } else if (id == 2) {
                theme.setBackgroundResource(R.drawable.theme3);
            } else if (id == 3) {
                theme.setBackgroundResource(R.drawable.theme4);
            } else if (id == 4) {
                theme.setBackgroundResource(R.drawable.theme5);
            } else if (id == 5) {
                theme.setBackgroundResource(R.drawable.theme6);
            } else if (id == 6) {
                theme.setBackgroundResource(R.drawable.theme7);
            } else if (id == 7) {
                theme.setBackgroundResource(R.drawable.theme8);
            } else if (id == 8) {
                theme.setBackgroundResource(R.drawable.theme9);
            } else if (id == 9) {
                theme.setBackgroundResource(R.drawable.theme10);
            } else {
                theme.setBackgroundResource(R.drawable.theme1);

            }
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        theme();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        theme = findViewById(R.id.theme);
        theme();
        removeprivateDialog = new SpotsDialog(this, "Remove From Private...", R.style.CustomSpotDialog);
        deleteVideoDialog = new SpotsDialog(this, "Delete From Storege...", R.style.CustomSpotDialog);
        clickListener = this;

        bindToolBar();
        bindControls();
        getPrivateVideoData();
//      initBottomSheet();
        initDialog();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((TextView) toolbar.findViewById(R.id.txtToolbarTitle)).setText(getResources().getString(R.string.private_video));
    }

    private void bindControls() {
        rvPrivateVideo = findViewById(R.id.rvPrivateVideo);
        rvPrivateVideo.setLayoutManager(new LinearLayoutManager(activity));
//        rvPrivateVideo.addItemDecoration(new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL));

        mPrivateVideoAdapter = new PrivateVideoAdapter(activity);
        rvPrivateVideo.setAdapter(mPrivateVideoAdapter);
    }

    private void initDialog() {
        popupdialog = new Dialog(activity, R.style.CustomDialog);
        popupdialog.setContentView(R.layout.layout_menu_private_videos);
        txtBottomSheetTitle = popupdialog.findViewById(R.id.txtDialogTitle);

        popupdialog.findViewById(R.id.llDialogPrivate).setOnClickListener(this);
        popupdialog.findViewById(R.id.llDialogDelete).setOnClickListener(this);
    }

    private void initBottomSheet() {
        View bsView = LayoutInflater.from(PrivateActivity.this).inflate(R.layout.layout_menu_private_videos, null);
        bottomSheetDialog = new BottomSheetDialog(PrivateActivity.this);
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
    }

    private void bsShow(int position) {
        // this.videoListPosition = position;
        String filename = new File(((PrivateVideo) Constants.PRIVATE_VIDEO_LIST.get(position)).getData()).getName();
//        this.bottomSheetBehavior.setState(3);
//        this.txtBottomSheetTitle.setText(filename);
//        this.bottomSheetDialog.show();
        this.txtBottomSheetTitle.setText(filename);
        this.popupdialog.show();
    }

    private void getPrivateVideoData() {
        DBHelper dbHelper = new DBHelper(activity);
        dbHelper.getPrivateVideoData();
        dbHelper.close();
        mPrivateVideoAdapter.notifyDataSetChanged();
    }

    private void myToggleSelection(int pos) {
        mPrivateVideoAdapter.toggleSelection(pos);
        String title = mPrivateVideoAdapter.getSelectedCount() + " / " + mPrivateVideoAdapter.getItemCount();
        mActionMode.setTitle(title);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_private_video_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_unlock: {
                new RemoveFromPrivate().execute();
                break;
            }
            case R.id.menu_delete: {
                final Dialog dialog = new Dialog(activity, R.style.CustomDialog);
                dialog.setContentView(R.layout.dialog_delete_video);

                ((TextView) dialog.findViewById(R.id.txtWarning)).setText(String.format(
                        getResources().getString(R.string.confirm_delete), mPrivateVideoAdapter.getSelectedCount()));

                dialog.findViewById(R.id.txtDialogCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogDismiss.dismissWithCheck(dialog);
                    }
                });
                dialog.findViewById(R.id.txtDialogConfirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogDismiss.dismissWithCheck(dialog);
                        new DeletePrivateVideo().execute();
                    }
                });
                dialog.show();
                break;
            }
            case R.id.menu_select_all: {

                if (mPrivateVideoAdapter.getSelectedCount() == mPrivateVideoAdapter.getItemCount()) {
                    mPrivateVideoAdapter.selectNone();
                } else {
                    mPrivateVideoAdapter.selectAll();
                }
                String title = mPrivateVideoAdapter.getSelectedCount() + " / " + mPrivateVideoAdapter.getItemCount();
                mActionMode.setTitle(title);
                break;
            }
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        this.mActionMode = null;
        mPrivateVideoAdapter.clearSelection();

    }

    private void bsDissmiss() {
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        bottomSheetDialog.dismiss();
        popupdialog.dismiss();
    }

    private void fnDelete() {

        final Dialog mDialog = new Dialog(PrivateActivity.this, R.style.CustomDialog);
        mDialog.setContentView(R.layout.dialog_delete_video);

        ((TextView) mDialog.findViewById(R.id.txtWarning)).setText("Are you sure Delect this Video?");

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
                new DeletePrivateVideoDialog().execute();

            }
        });
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llDialogDelete: {
                bsDissmiss();
                fnDelete();
                break;
            }

            case R.id.llDialogPrivate: {
                bsDissmiss();
                new RemoveFromPrivateDialog().execute();
                break;
            }
        }
    }

    @Override
    public void onClick(View view, int position) {
        if (mActionMode != null) {
            myToggleSelection(position);
            return;
        }
        String s = Constants.PRIVATE_VIDEO_LIST.get(position).getNewPath();
        File file = new File(s);
        if (file.exists()) {
            Intent intent = new Intent(activity, ExoPlayerActivity.class);
            intent.setData(Uri.fromFile(file));
            startActivity(intent);
        }
    }

    @Override
    public void onClickPopMenu(View view, int position) {
        mSelectedPosition = position;
        bsShow(position);
    }

    @Override
    public void onLongClick(View view, int position) {
        if (mActionMode != null) {
            return;
        }
        mActionMode = startActionMode(PrivateActivity.this);
        mPrivateVideoAdapter.startSelection();
        myToggleSelection(position);
    }

    private class RemoveFromPrivateDialog extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            removeprivateDialog.setCancelable(false);
            removeprivateDialog.setCanceledOnTouchOutside(false);
            removeprivateDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            PrivateVideo privateVideo = Constants.PRIVATE_VIDEO_LIST.get(mSelectedPosition);

            File from = new File(privateVideo.getNewPath());
            File to = new File(privateVideo.getData());

            if (AppUtils.copyFileToOther(from.getAbsolutePath(), to.getAbsolutePath())) {
                if (to.exists()) {
                    if (AppUtils.deletePrivateFile(from.getAbsolutePath())) {
                        AppUtils.refreshMediaStore(activity, to);
                        DBHelper dbHelper = new DBHelper(activity);
                        dbHelper.removeFromPrivate(privateVideo.getId());
                        Constants.PRIVATE_VIDEO_LIST.remove(privateVideo);

                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mPrivateVideoAdapter.notifyDataSetChanged();
            DialogDismiss.dismissWithCheck(removeprivateDialog);
        }
    }

    private class DeletePrivateVideoDialog extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            deleteVideoDialog.setCanceledOnTouchOutside(false);
            deleteVideoDialog.setCancelable(false);
            deleteVideoDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            PrivateVideo privateVideo = Constants.PRIVATE_VIDEO_LIST.get(mSelectedPosition);

            File from = new File(privateVideo.getNewPath());
            if (AppUtils.deletePrivateFile(from.getAbsolutePath())) {
                DBHelper dbHelper = new DBHelper(activity);
                dbHelper.removeFromPrivate(privateVideo.getId());
                Constants.PRIVATE_VIDEO_LIST.remove(privateVideo);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mPrivateVideoAdapter.notifyDataSetChanged();
            Toast.makeText(activity, "Delete successful", Toast.LENGTH_SHORT).show();
            DialogDismiss.dismissWithCheck(deleteVideoDialog);
        }
    }

    private class RemoveFromPrivate extends AsyncTask<Void, Void, Void> {
        DBHelper dbHelper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dbHelper = new DBHelper(activity);
            mSelectedItemList = mPrivateVideoAdapter.getSelectedItems();

            removeprivateDialog.setCanceledOnTouchOutside(false);
            removeprivateDialog.setCancelable(false);
            removeprivateDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (int i = mSelectedItemList.size() - 1; i >= 0; i--) {
                    PrivateVideo privateVideo = Constants.PRIVATE_VIDEO_LIST.get(mSelectedItemList.get(i));

                    File from = new File(privateVideo.getNewPath());
                    File to = new File(privateVideo.getData());

                    if (AppUtils.copyFileToOther(from.getAbsolutePath(), to.getAbsolutePath())) {
                        if (to.exists()) {
                            if (AppUtils.deletePrivateFile(from.getAbsolutePath())) {
                                AppUtils.refreshMediaStore(activity, to);
                                dbHelper.removeFromPrivate(privateVideo.getId());
                                Constants.PRIVATE_VIDEO_LIST.remove(privateVideo);
                            }
                        }
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
            DialogDismiss.dismissWithCheck(removeprivateDialog);
            dbHelper.close();
            if (mActionMode != null) {
                mActionMode.finish();
            }
            Toast.makeText(activity, "Remove from private", Toast.LENGTH_SHORT).show();
            mPrivateVideoAdapter.notifyDataSetChanged();
        }
    }

    private class DeletePrivateVideo extends AsyncTask<Void, Void, Void> {

        DBHelper dbHelper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dbHelper = new DBHelper(activity);
            mSelectedItemList = mPrivateVideoAdapter.getSelectedItems();

            deleteVideoDialog.setCanceledOnTouchOutside(false);
            deleteVideoDialog.setCancelable(false);
            deleteVideoDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (int i = mSelectedItemList.size() - 1; i >= 0; i--) {
                    PrivateVideo privateVideo = Constants.PRIVATE_VIDEO_LIST.get(mSelectedItemList.get(i));

                    File from = new File(privateVideo.getNewPath());
                    if (AppUtils.deletePrivateFile(from.getAbsolutePath())) {
                        dbHelper.removeFromPrivate(privateVideo.getId());
                        Constants.PRIVATE_VIDEO_LIST.remove(privateVideo);
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
            DialogDismiss.dismissWithCheck(deleteVideoDialog);
            dbHelper.close();
            if (mActionMode != null) {
                mActionMode.finish();
            }
            Toast.makeText(activity, "Delete successful", Toast.LENGTH_SHORT).show();
            mPrivateVideoAdapter.notifyDataSetChanged();
        }
    }
}
