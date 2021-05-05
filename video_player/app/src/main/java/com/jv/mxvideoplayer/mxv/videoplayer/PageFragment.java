package com.jv.mxvideoplayer.mxv.videoplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class PageFragment extends Fragment {

    private TextView description;
    private ImageView image,imageunder;

    public static PageFragment getInstance(String titles, int postion) {
        PageFragment f = new PageFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        args.putString("page_title", titles);
        args.putInt("position", postion);

        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout resource that'll be returned
        View rootView = inflater.inflate(R.layout.fragment_page, container,
                false);

        description = (TextView)rootView.findViewById(R.id.textView);
        image = (ImageView) rootView.findViewById(R.id.image);
        imageunder = (ImageView) rootView.findViewById(R.id.imageunder);

        description.setText(getArguments().getString("page_title"));
        setImage();

        return rootView;
    }

    private void setImage() {
        int postion = getArguments().getInt("position");
        if (postion == 0) {
            image.setImageResource(R.drawable.theme1);
            imageunder.setImageResource(R.drawable.ic_demoscreen_1);
        } else if (postion == 1) {
            image.setImageResource(R.drawable.theme2);
            imageunder.setImageResource(R.drawable.ic_demoscreen_2);

        } else if (postion == 2) {
            image.setImageResource(R.drawable.theme3);
            imageunder.setImageResource(R.drawable.ic_demoscreen_3);

        } else if (postion == 3) {
            image.setImageResource(R.drawable.theme4);
            imageunder.setImageResource(R.drawable.ic_demoscreen_4);

        } else if (postion == 4) {
            image.setImageResource(R.drawable.theme5);
            imageunder.setImageResource(R.drawable.ic_demoscreen_5);

        } else if (postion == 5) {
            image.setImageResource(R.drawable.theme6);
            imageunder.setImageResource(R.drawable.ic_demoscreen_6);

        } else if (postion == 6) {
            image.setImageResource(R.drawable.theme7);
            imageunder.setImageResource(R.drawable.ic_demoscreen_7);

        } else if (postion == 7) {

            image.setImageResource(R.drawable.theme8);
            imageunder.setImageResource(R.drawable.ic_demoscreen_8);

        }else if (postion == 8) {
            image.setImageResource(R.drawable.theme9);
            imageunder.setImageResource(R.drawable.ic_demoscreen_9);

        } else if (postion == 9) {
            image.setImageResource(R.drawable.theme10);
            imageunder.setImageResource(R.drawable.ic_demoscreen_10);
        }
    }
}