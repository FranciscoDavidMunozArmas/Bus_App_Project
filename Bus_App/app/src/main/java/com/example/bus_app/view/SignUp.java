package com.example.bus_app.view;

import android.content.Intent;
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
import com.example.bus_app.databinding.login.SignupBinding;
import com.example.bus_app.ui.PagesController;
import com.example.bus_app.viewmodel.SignUpVM;

public class SignUp extends Fragment {

    private SignUpVM mViewModel;

    public static SignUp newInstance() { return new SignUp(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        SignupBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);

        mViewModel = new ViewModelProvider(this).get(SignUpVM.class);
        binding.setSignupVM(mViewModel);
        binding.setLifecycleOwner(this);

        mViewModel.setActivity(getActivity());
        mViewModel.setContext(getContext());

        ((Button) binding.getRoot().findViewById(R.id.sign_up_btn_back)).setOnClickListener(new View.OnClickListener() {
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
                if(bool){
                    StartActivity(MainActivity.class);
                }
            }
        });

        //return inflater.inflate(R.layout.fragment_sign_up, container, false);
        return binding.getRoot();
    }

    private void StartActivity(Class<?> cls){
        PagesController.startNewActivity(this, cls, "id", "" + mViewModel.getId());
    }
}