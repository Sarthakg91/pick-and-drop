package com.example.sarthakghosh.freshap;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.software.shell.fab.ActionButton;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class Enter_PickUp extends ActionBarActivity implements Communicator {
    String name1;
    String location1;
    enterPickUpFragment frag;
    GoogleMap map;
    private List<LatLng> lines;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter__pick_up);

        ActionButton actionButton2 = (ActionButton) findViewById(R.id.action_button);

        actionButton2.setButtonColor(getResources().getColor(R.color.fab_material_red_500));

        actionButton2.setImageResource(R.drawable.fab_plus_icon);
        actionButton2.show();
        actionButton2.moveUp(100.0f);


        actionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // button click action goes here
                openPickUpFragment(v);
            }
        });

        MapFragment mapFrag = (MapFragment)getFragmentManager().findFragmentById(R.id.map1);
        List<LatLng> lines = Util.getPolyLines();
        map =  mapFrag.getMap();
        map.addPolyline(new PolylineOptions().addAll(lines).width(10).color(Color.RED));
        CameraPosition cp = new CameraPosition(lines.get(lines.size() / 2),(float)10.0,(float)0.0,(float)0.0);
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
        map.addMarker(new MarkerOptions()
                .position(lines.get(0))
                .title("Source"));
        map.addMarker(new MarkerOptions()
                .position(lines.get(lines.size() - 1))
                .title("Destination"));
    }

    protected void openPickUpFragment(View view){
        frag=new enterPickUpFragment();
        FragmentManager my_manager=getFragmentManager();
        FragmentTransaction transaction=my_manager.beginTransaction();
        transaction.add(R.id.pickUpMainScreen, frag, "pop_frag");
        transaction.commit();
    }

    public void okClicked(View view)
    {

    }

    @Override
    public void respond(String name, String location) {
        map.clear();
        name1=name;
        location1=location;
        FragmentManager my_manager2=getFragmentManager();
        FragmentTransaction transaction=my_manager2.beginTransaction();
        transaction.remove(frag);
        transaction.commit();
        url="https://maps.googleapis.com/maps/api/directions/json?origin="+
                Util.getSource().toString()+"&destination="+location+"&key="+Util.API_KEY;
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
            map.addPolyline(new PolylineOptions().addAll(lines).width(10).color(Color.RED));
            CameraPosition cp = new CameraPosition(lines.get(lines.size() / 2),(float)10.0,(float)0.0,(float)0.0);
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
            map.addMarker(new MarkerOptions()
                    .position(lines.get(0))
                    .title("Source"));
            map.addMarker(new MarkerOptions()
                    .position(lines.get(lines.size() - 1))
                    .title(name));
        }

        url="https://maps.googleapis.com/maps/api/directions/json?origin="+
                location+"&destination="+Util.getDest()+"&key="+Util.API_KEY;
        url = url.replaceAll(" ","%20");


        lines = null;
        query = new Thread(new Runnable() {
            @Override
            public void run() {

                runQuery();

            }
        });
        query.start();
        while (query.isAlive());
        if(lines!=null && lines!=null){
            map.addPolyline(new PolylineOptions().addAll(lines).width(10).color(Color.RED));
            CameraPosition cp = new CameraPosition(lines.get(lines.size() / 2),(float)10.0,(float)0.0,(float)0.0);
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
            map.addMarker(new MarkerOptions()
                    .position(lines.get(lines.size() - 1))
                    .title("Destination"));
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

}
