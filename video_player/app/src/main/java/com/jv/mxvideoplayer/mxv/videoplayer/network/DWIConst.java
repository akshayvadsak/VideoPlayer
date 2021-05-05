package com.jv.mxvideoplayer.mxv.videoplayer.network;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;


import java.util.ArrayList;
import java.util.List;

import com.jv.mxvideoplayer.mxv.videoplayer.model.Application;



public class DWIConst {

    public static String PRFS_INSTALL_COUNT = "installCount";
    public static String PRFS_NAME = "MyAppPrfs";
    public static String PRFS_INSTALLED = "installed";
    public static String SUCEESS = "1";

    public static List<Application> mAppList = new ArrayList<>();

    public static boolean setListViewHeightBasedOnItemsGrid(GridView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount() / 3 + (listAdapter.getCount() % 3);

            // Get total height of all items.
            int padding = listView.getPaddingBottom() + listView.getPaddingTop();

            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = 10 * ((numberOfItems - 1));/*listView.getDividerHeight() *
                    (numberOfItems - 1);*/

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + padding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;

        } else {
            return false;
        }

    }

}
