package com.example.bus_app.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bus_app.R;

public class  Charge extends Fragment {

    public static Charge newInstance() { return new Charge(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charge, container, false);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fstep1 = new ChargeStep1();
        transaction.replace(R.id.charge_container,fstep1);
        transaction.commit();

        return view;
    }

}