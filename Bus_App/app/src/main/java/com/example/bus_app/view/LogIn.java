package com.example.bus_app.view;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bus_app.R;
import com.example.bus_app.databinding.login.LoginBinding;
import com.example.bus_app.ui.PagesController;
import com.example.bus_app.viewmodel.LoginVM;

public class LogIn extends Fragment {

    private LoginVM mViewModel;
    private boolean log_on_start = false;

    public static LogIn newInstance() {
        return new LogIn();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        LoginBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        mViewModel = new ViewModelProvider(this).get(LoginVM.class);
        binding.setLoginVM(mViewModel);
        binding.setLifecycleOwner(this);

        mViewModel.setActivity(getActivity());
        mViewModel.setContext(getContext());

        if(mViewModel.CheckLogin()){
            StartActivity(MainActivity.class);
        }

        ((Button) binding.getRoot().findViewById(R.id.log_in_btn_sign_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.log_in_container, SignUp.newInstance());
                transaction.commit();
            }
        });

        ((Button) binding.getRoot().findViewById(R.id.log_in_btn_fpassword)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.log_in_container, ForgetPassword.newInstance());
                transaction.commit();
            }
        });

        mViewModel.getStart().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if(bool){
                    StartActivity(MainActivity.class);
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = new ViewModelProvider(this).get(LoginVM.class);
        // TODO: Use the ViewModel

    }

    private void StartActivity(Class<?> cls){
        PagesController.startNewActivity(this, cls, "id", "" + mViewModel.getId());
    }

}