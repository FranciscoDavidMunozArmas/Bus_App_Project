package com.example.bus_app.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.example.bus_app.R;
import com.example.bus_app.databinding.main.HomeBinding;
import com.example.bus_app.viewmodel.HomeVM;
import com.example.bus_app.viewmodel.SharedVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Home extends Fragment {

    private HomeVM mViewModel;
    private SharedVM share;

    public static Home newInstance() { return new Home(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        share = ViewModelProviders.of(requireActivity()).get(SharedVM.class);

        // Inflate the layout for this fragment
        HomeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        mViewModel = new ViewModelProvider(this).get(HomeVM.class);
        binding.setHomeVM(mViewModel);
        binding.setLifecycleOwner(this);

        mViewModel.setActivity(getActivity());
        mViewModel.setContext(getContext());

        mViewModel.setId(getActivity().getIntent().getExtras().get("id").toString());

        share.setBool(mViewModel.get_hand().getValue());
        share.setUser(mViewModel.getUser());

        ((Button) binding.getRoot().findViewById(R.id.home_btn_pay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container, Payment.newInstance());
                transaction.commit();
            }
        });

        ((Button) binding.getRoot().findViewById(R.id.home_btn_charge)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container, Charge.newInstance());
                transaction.commit();
            }
        });

        mViewModel.get_floating_btn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if(bool){
                    ((RelativeLayout)binding.getRoot().findViewById(R.id.home_floating_container_box)).setVisibility(View.VISIBLE);
                } else {
                    ((RelativeLayout)binding.getRoot().findViewById(R.id.home_floating_container_box)).setVisibility(View.GONE);
                }
            }
        });

        mViewModel.get_hand().observe(this, new Observer<Boolean>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onChanged(Boolean bool) {
                share.setBool(bool);
                changeSide(((RelativeLayout)binding.getRoot().findViewById(R.id.home_floating_container_box)), bool);
                changeSide(((FloatingActionButton)binding.getRoot().findViewById(R.id.home_floating_btn_settings)), bool);
                changeSide(((RelativeLayout)binding.getRoot().findViewById(R.id.home_user_info)), !bool);
            }
        });

        mViewModel.get_logout().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if(bool){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        //return inflater.inflate(R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void changeSide(RelativeLayout relativeLayout, boolean hand){
        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
        if(!hand){
            param.addRule(RelativeLayout.ALIGN_PARENT_START);
            param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            param.removeRule(RelativeLayout.ALIGN_PARENT_END);
            param.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else {
            param.addRule(RelativeLayout.ALIGN_PARENT_END);
            param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            param.removeRule(RelativeLayout.ALIGN_PARENT_START);
            param.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }
        relativeLayout.setLayoutParams(param);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void changeSide(FloatingActionButton relativeLayout, boolean hand){
        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
        if(!hand){
            param.addRule(RelativeLayout.ALIGN_PARENT_START);
            param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            param.removeRule(RelativeLayout.ALIGN_PARENT_END);
            param.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else {
            param.addRule(RelativeLayout.ALIGN_PARENT_END);
            param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            param.removeRule(RelativeLayout.ALIGN_PARENT_START);
            param.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }
        relativeLayout.setLayoutParams(param);
    }
}