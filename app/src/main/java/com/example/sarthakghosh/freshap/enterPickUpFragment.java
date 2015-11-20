package com.example.sarthakghosh.freshap;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Sarthak Ghosh on 17-11-2015.
 */
public class enterPickUpFragment extends Fragment {



    Button buttonOK;
    Button buttonClose;
    EditText nameTextField;
    EditText locationTextField;
    Communicator comm;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        comm= (Communicator) getActivity();
        return inflater.inflate(R.layout.enter_pick_up_fragment_layout,container,false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buttonOK= (Button) getActivity().findViewById(R.id.button_ok);

        buttonClose= (Button) getActivity().findViewById(R.id.button_close);

        nameTextField= (EditText) getActivity().findViewById(R.id.textView_name);
        locationTextField= (EditText) getActivity().findViewById(R.id.textView_location);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                respondToOkayButton();
                Toast.makeText(getActivity(),"route set",Toast.LENGTH_SHORT).show();

            }
        });

        buttonClose.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
               respondToCloseButton();
            }
        });



    }

    private void respondToOkayButton() {
        comm.respond(nameTextField.toString(), locationTextField.toString());
    }

    private void respondToCloseButton() {
        comm.respond("","");
        Toast.makeText(getActivity(),"cancelled",Toast.LENGTH_SHORT).show();
    }


}
