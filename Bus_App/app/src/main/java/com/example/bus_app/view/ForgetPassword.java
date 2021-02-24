package com.example.bus_app.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bus_app.R;
import com.example.bus_app.databinding.login.ForgetpasswordBinding;
import com.example.bus_app.viewmodel.ForgetPasswordVM;

public class ForgetPassword extends Fragment {

    private ForgetPasswordVM mViewModel;

    public static ForgetPassword newInstance() { return new ForgetPassword(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ForgetpasswordBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forget_password, container, false);
        mViewModel = new ViewModelProvider(this).get(ForgetPasswordVM.class);
        binding.setForgetVM(mViewModel);
        binding.setLifecycleOwner(this);

        mViewModel.setActivity(getActivity());
        mViewModel.setContext(getContext());

        ((Button) binding.getRoot().findViewById(R.id.forget_password_btn_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.log_in_container, LogIn.newInstance());
                transaction.commit();
            }
        });

        mViewModel.getStart().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if (bool) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.log_in_container, LogIn.newInstance());
                    transaction.commit();
                }
            }
        });

        return binding.getRoot();
    }
}