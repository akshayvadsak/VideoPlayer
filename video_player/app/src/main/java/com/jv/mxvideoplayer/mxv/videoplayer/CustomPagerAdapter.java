package com.jv.mxvideoplayer.mxv.videoplayer;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;


import com.viewpagerindicator.IconPagerAdapter;

public class CustomPagerAdapter extends FragmentPagerAdapter implements
        IconPagerAdapter {

    private ArrayList listIcon;
    private ArrayList pageContents;

    public CustomPagerAdapter(FragmentManager fm, ArrayList list,
                              ArrayList content) {
        super(fm);
        this.listIcon = list;
        this.pageContents = content;
    }


    @Override
    public Fragment getItem(int position) {

        return PageFragment.getInstance((String) pageContents.get(position), position);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "";
        } else if (position == 1) {
            return "";
        } else if (position == 2) {
            return "";
        }else if (position == 3) {
            return "";
        }else if (position == 4) {
            return "";
        }else if (position == 5) {
            return "";
        }else if (position == 6) {
            return "";
        }else if (position == 7) {
            return "";
        }else if (position == 8) {
            return "";
        } else
            return "";
    }

    @Override
    public int getIconResId(int index) {
        return (int) listIcon.get(index);
    }

}