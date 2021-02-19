package com.example.bus_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoginPage extends Fragment {

    RelativeLayout rlayout;
    ImageView splashIcon;
    Handler handler = new Handler();
    BottomNavigationView nav;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rlayout.setVisibility(View.VISIBLE);
            splashIcon.setVisibility(View.GONE);
        }
    };

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public LoginPage() {
    }
    public LoginPage(BottomNavigationView nav) {
        this.nav = nav;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginPage.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginPage newInstance(String param1, String param2) {
        LoginPage fragment = new LoginPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_page, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rlayout = (RelativeLayout) view.findViewById(R.id.start_login);

        splashIcon = (ImageView) view.findViewById(R.id.splash_icon);
        handler.postDelayed(runnable, 2000);

        ((Button)view.findViewById(R.id.btn_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePage home = new HomePage();
                getFragmentManager().beginTransaction().replace(R.id.splash_frame_container, home, home.getTag()).commit();
                nav.setVisibility(View.VISIBLE);
            }
        });
    }
}