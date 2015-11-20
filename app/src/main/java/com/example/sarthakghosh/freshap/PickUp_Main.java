package com.example.sarthakghosh.freshap;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;


public class PickUp_Main extends ActionBarActivity implements PickUpMainCom {
    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up__main);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map1)).getMap();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pick_up__main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openEnterPickUpActivity(View view) {
        // Do something in response to button click
        Intent intent = new Intent(this, Enter_PickUp.class);
        startActivity(intent);
    }

    @Override
    public void respond(List<LatLng> lines) {
        Util.setLines(lines);
       map.addPolyline(new PolylineOptions().addAll(lines).width(10).color(Color.RED));
        CameraPosition cp = new CameraPosition(lines.get(lines.size() / 2),(float)10.0,(float)0.0,(float)0.0);
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
        map.addMarker(new MarkerOptions()
                .position(lines.get(0))
                .title("Source"));
        map.addMarker(new MarkerOptions()
                .position(lines.get(lines.size()-1))
                .title("Destination"));
    }
}
