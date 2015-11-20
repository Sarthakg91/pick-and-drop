package com.example.sarthakghosh.freshap;

import android.app.Fragment;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
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
public class enterStartAndEndFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private View inflate;

    private EditText sourceTxt;
    private EditText destTxt;
    private GoogleApiClient mGoogleApiClient;
    private String url;
    private List<LatLng> lines;
    private PickUpMainCom comm;


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
        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .addApi(Places.GEO_DATA_API)
                .build();
       return inflate;
    }

    private void generateRoute() {

        Editable source  = sourceTxt.getText();
        Util.setSource(source.toString());
        Editable dest = destTxt.getText();
        Util.setDest(dest.toString());
         url="https://maps.googleapis.com/maps/api/directions/json?origin="+
                source.toString()+"&destination="+dest.toString()+"&key="+Util.API_KEY;
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
            String httpResponse = Util.getHttpResponse(url);
            lines = Util.getLines(httpResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this.getActivity(),
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }
}
