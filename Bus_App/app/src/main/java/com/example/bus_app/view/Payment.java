package com.example.bus_app.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;import android.widget.Button;
import android.widget.TextView;
import com.example.bus_app.R;
import com.google.zxing.integration.android.IntentIntegrator;

public class Payment extends Fragment {

    public static Payment newInstance() {
        return new Payment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setPrompt("Enfoca el codigo QR dentro del rectangulo");

        integrator.setOrientationLocked(false);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.initiateScan();

        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

}
