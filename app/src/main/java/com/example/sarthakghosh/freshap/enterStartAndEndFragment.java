package com.example.sarthakghosh.freshap;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sarthak Ghosh on 15-11-2015.
 */
public class enterStartAndEndFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.start_and_end_fragment_layout,container,false);
    }
}
