package com.example.sarthakghosh.freshap;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.software.shell.fab.ActionButton;

public class Enter_PickUp extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter__pick_up);

        ActionButton actionButton2 = (ActionButton) findViewById(R.id.action_button);

        actionButton2.setButtonColor(getResources().getColor(R.color.fab_material_red_500));

        actionButton2.setImageResource(R.drawable.fab_plus_icon);
        actionButton2.show();
        actionButton2.moveUp(100.0f);
    }

    protected void openPickUpFragment(View view){


    }
}
