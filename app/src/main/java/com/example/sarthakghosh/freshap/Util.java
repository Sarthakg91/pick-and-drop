package com.example.sarthakghosh.freshap;

import android.net.http.AndroidHttpClient;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by satyajeet on 11/20/2015.
 */
public class Util {

    public static String API_KEY = "AIzaSyDtKNFAF1A2THvsUTASd4eP__Odj6KhV8g";
    private static List<LatLng> lines;
    private static String source;
    private static String dest;


    public static void setLines(List<LatLng> lines) {
        Util.lines = lines;
    }

    public static List<LatLng> getPolyLines() {
        return lines;
    }

    public static String getHttpResponse(String url) throws IOException {
        HttpResponse response;
        HttpGet request;
        AndroidHttpClient client = AndroidHttpClient.newInstance("pickanddrop");

        request = new HttpGet(url);
        response = client.execute(request);

        InputStream source = response.getEntity().getContent();
        BufferedReader r = new BufferedReader(new InputStreamReader(source));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }

    public static List<LatLng> getLines(String httpResponse) throws JSONException {
        JSONObject result = new JSONObject(httpResponse);
        JSONArray routes = result.getJSONArray("routes");

        long distanceForSegment = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getInt("value");

        JSONArray steps = routes.getJSONObject(0).getJSONArray("legs")
                .getJSONObject(0).getJSONArray("steps");

        List<LatLng> lines = new ArrayList<LatLng>();

        for(int i=0; i < steps.length(); i++) {
            String polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points");

            for(LatLng p : decodePolyline(polyline)) {
                lines.add(p);
            }
        }
        return lines;
    }


    private static List<LatLng> decodePolyline(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();

        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(p);
        }

        return poly;
    }

    public static void setSource(String source) {
        Util.source = source;
    }

    public static void setDest(String dest) {
        Util.dest = dest;
    }

    public static String getSource() {
        return source;
    }

    public static String getDest() {
        return dest;
    }
}
