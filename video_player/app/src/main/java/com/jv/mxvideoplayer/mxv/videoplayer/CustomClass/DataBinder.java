package com.jv.mxvideoplayer.mxv.videoplayer.CustomClass;

import java.util.ArrayList;

/**
 * Created by jigs patel on 28-12-2019.
 */

public class DataBinder {

    public static ArrayList<String> fetchQue() {
        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("What was your childhood nickname?");
        arrayList.add("What is your favorite color?");
        arrayList.add("In which city were you born?");
        arrayList.add("What is your dream job?");
        arrayList.add("What is your favorite movie?");

        return arrayList;
    }
}
