package com.example.bus_app;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePage extends Fragment {

    private Button btn_pay;
    private boolean floating_active;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomePage() {
        floating_active = false;
    }

    public static HomePage newInstance(String param1, String param2) {
        HomePage fragment = new HomePage();
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
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        ((Button)view.findViewById(R.id.btnPay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPage pay = new PayPage();
                getFragmentManager().beginTransaction().replace(R.id.splash_frame_container, pay, pay.getTag()).commit();
            }
        });
        ((Button)view.findViewById(R.id.btnRecharge)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RechargePage recharge = new RechargePage();
                getFragmentManager().beginTransaction().replace(R.id.splash_frame_container, recharge, recharge.getTag()).commit();
            }
        });
        ((FloatingActionButton)view.findViewById(R.id.fbtn_setting_main)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!floating_active){
                    ((RelativeLayout)view.findViewById(R.id.floating_container_home)).setVisibility(View.VISIBLE);
                    floating_active = true;
                } else {
                    ((RelativeLayout)view.findViewById(R.id.floating_container_home)).setVisibility(View.GONE);
                    floating_active = false;
                }
            }
        });
        ((CheckBox)view.findViewById(R.id.cb_floating_position_home)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(((CheckBox)view.findViewById(R.id.cb_floating_position_home)).isChecked()){
                    ((CheckBox)view.findViewById(R.id.cb_floating_position_home)).setText("Left Hand");
                    RelativeLayout.LayoutParams params_floating_btn = (RelativeLayout.LayoutParams)((FloatingActionButton)view.findViewById(R.id.fbtn_setting_main)).getLayoutParams();
                    params_floating_btn.addRule(RelativeLayout.ALIGN_PARENT_START);
                    params_floating_btn.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    params_floating_btn.removeRule(RelativeLayout.ALIGN_PARENT_END);
                    params_floating_btn.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    ((FloatingActionButton)view.findViewById(R.id.fbtn_setting_main)).setLayoutParams(params_floating_btn);

                    RelativeLayout.LayoutParams params_cont_float = (RelativeLayout.LayoutParams)((RelativeLayout)view.findViewById(R.id.floating_container_home)).getLayoutParams();
                    params_cont_float.addRule(RelativeLayout.ALIGN_PARENT_START);
                    params_cont_float.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    params_cont_float.removeRule(RelativeLayout.ALIGN_PARENT_END);
                    params_cont_float.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    ((RelativeLayout)view.findViewById(R.id.floating_container_home)).setLayoutParams(params_cont_float);

                    RelativeLayout.LayoutParams params_user_info = (RelativeLayout.LayoutParams)((RelativeLayout)view.findViewById(R.id.user_info)).getLayoutParams();
                    params_user_info.addRule(RelativeLayout.ALIGN_PARENT_END);
                    params_user_info.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params_user_info.removeRule(RelativeLayout.ALIGN_PARENT_START);
                    params_user_info.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    ((RelativeLayout)view.findViewById(R.id.user_info)).setLayoutParams(params_user_info);
                } else {
                    ((CheckBox)view.findViewById(R.id.cb_floating_position_home)).setText("Right Hand");
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)((FloatingActionButton)view.findViewById(R.id.fbtn_setting_main)).getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_END);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.removeRule(RelativeLayout.ALIGN_PARENT_START);
                    params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    ((FloatingActionButton)view.findViewById(R.id.fbtn_setting_main)).setLayoutParams(params);

                    RelativeLayout.LayoutParams params_cont_float = (RelativeLayout.LayoutParams)((RelativeLayout)view.findViewById(R.id.floating_container_home)).getLayoutParams();
                    params_cont_float.addRule(RelativeLayout.ALIGN_PARENT_END);
                    params_cont_float.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params_cont_float.removeRule(RelativeLayout.ALIGN_PARENT_START);
                    params_cont_float.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    ((RelativeLayout)view.findViewById(R.id.floating_container_home)).setLayoutParams(params_cont_float);

                    RelativeLayout.LayoutParams params_user_info = (RelativeLayout.LayoutParams)((RelativeLayout)view.findViewById(R.id.user_info)).getLayoutParams();
                    params_user_info.addRule(RelativeLayout.ALIGN_PARENT_START);
                    params_user_info.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    params_user_info.removeRule(RelativeLayout.ALIGN_PARENT_END);
                    params_user_info.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    ((RelativeLayout)view.findViewById(R.id.user_info)).setLayoutParams(params_user_info);
                }
            }
        });
        return view;
    }
}