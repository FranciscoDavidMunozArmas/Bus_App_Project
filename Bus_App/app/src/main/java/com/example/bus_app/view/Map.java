package com.example.bus_app.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bus_app.R;
import com.example.bus_app.viewmodel.SharedVM;

public class Map extends Fragment {

    private SharedVM share;

    public static Map newInstance() { return new Map(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        share = ViewModelProviders.of(requireActivity()).get(SharedVM.class);

        View v = inflater.inflate(R.layout.fragment_map, container, false);
        ((TextView) v.findViewById(R.id.sample_text)).setText(share.getStr().getValue().toString());

        // Inflate the layout for this fragment
        return v;
    }
}