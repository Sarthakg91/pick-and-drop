package com.example.sarthakghosh.freshap;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by satyajeet on 11/20/2015.
 */
public class Util {


    private static List<LatLng> lines;




    public static void setLines(List<LatLng> lines) {
        Util.lines = lines;
    }

    public static List<LatLng> getPolyLines() {
        return lines;
    }
}
