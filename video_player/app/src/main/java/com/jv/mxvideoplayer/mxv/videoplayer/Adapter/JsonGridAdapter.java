package com.jv.mxvideoplayer.mxv.videoplayer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

//import com.jv.mxvideoplayer.mxv.videoplayer.MainPackage.GlideApp;
import com.bumptech.glide.Glide;
import com.jv.mxvideoplayer.mxv.videoplayer.R;
import com.jv.mxvideoplayer.mxv.videoplayer.model.Application;

/**
 * Created by jigs patel on 08-12-2019.
 */

public class JsonGridAdapter extends BaseAdapter {

        private Context mContext;
        List<Application> mAppList;

        public JsonGridAdapter(Context c, List<Application> appList) {
            mContext = c;
            mAppList = appList;

        }

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public Application getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            View v;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                //new View(this.mContext);
                v = inflater.inflate(R.layout.row_appgrid, parent, false);
            } else {
                v = (View) convertView;
            }
            ImageView imgAppIcon = (ImageView) v.findViewById(R.id.imgIcon);
            TextView txtAppName = (TextView) v.findViewById(R.id.txtAppName);

            Glide.with(mContext)
                    .load(mAppList.get(position).getIcon())
                    .centerCrop()
                    .skipMemoryCache(true)
                    .into(imgAppIcon);

            txtAppName.setText(mAppList.get(position).getAppName());


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent inMoreapp = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(mAppList.get(position).getAppUrl()));
                    mContext.startActivity(inMoreapp);
                }
            });
            return v;
        }
    }