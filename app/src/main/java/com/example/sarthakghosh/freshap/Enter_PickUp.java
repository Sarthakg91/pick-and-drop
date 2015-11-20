package com.example.sarthakghosh.freshap;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.software.shell.fab.ActionButton;

import java.util.List;

public class Enter_PickUp extends ActionBarActivity implements Communicator {
    String name1;
    String location1;
    enterPickUpFragment frag;
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
        mapFrag.getMap().addPolyline(new PolylineOptions().addAll(lines).width(10).color(Color.RED));
        CameraPosition cp = new CameraPosition(lines.get(lines.size() / 2),(float)10.0,(float)0.0,(float)0.0);
        mapFrag.getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cp));
        mapFrag.getMap().addMarker(new MarkerOptions()
                .position(lines.get(0))
                .title("Source"));
        mapFrag.getMap().addMarker(new MarkerOptions()
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
        name1=name;
        location1=location;
        FragmentManager my_manager2=getFragmentManager();
        FragmentTransaction transaction=my_manager2.beginTransaction();
        transaction.remove(frag);
        transaction.commit();

    }
}
