package com.example.sarthakghosh.freshap;

import android.app.Fragment;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.api.GoogleApiClient;
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
 * Created by Sarthak Ghosh on 15-11-2015.
 */
public class enterStartAndEndFragment extends Fragment {

    private View inflate;
    private String API_KEY = "AIzaSyDtKNFAF1A2THvsUTASd4eP__Odj6KhV8g";
    private EditText sourceTxt;
    private EditText destTxt;
    private GoogleApiClient mGoogleApiClient;
    private String url;
    private List<LatLng> lines;
    private PickUpMainCom comm;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.start_and_end_fragment_layout, container, false);

        sourceTxt = (EditText) inflate.findViewById(R.id.source);
        destTxt = (EditText) inflate.findViewById(R.id.dest);
        comm = (PickUpMainCom)getActivity();
        Button okayButton = (Button)inflate.findViewById(R.id.button2);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateRoute();
            }
        });

       return inflate;
    }

    private void generateRoute() {

        Editable source  = sourceTxt.getText();
        Editable dest = destTxt.getText();
         url="https://maps.googleapis.com/maps/api/directions/json?origin="+
                source.toString()+"&destination="+dest.toString()+"&key="+API_KEY;
        url = url.replaceAll(" ","%20");



        Thread query = new Thread(new Runnable() {
            @Override
            public void run() {

                runQuery();

            }
        });
        query.start();
        while (query.isAlive());
        if(lines!=null && lines!=null){
            comm.respond(lines);
        }
    }

    private void runQuery() {
        try {
            String httpResponse = getHttpResponse(url);
            lines = getLines(httpResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getHttpResponse(String url) throws IOException {
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

    private List<LatLng> decodePolyline(String encoded) {

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


    private List<LatLng> getLines(String jsonRes) throws JSONException {
        JSONObject result = new JSONObject(jsonRes);
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
}
