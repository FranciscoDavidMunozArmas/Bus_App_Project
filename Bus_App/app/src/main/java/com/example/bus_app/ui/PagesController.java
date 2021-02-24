package com.example.bus_app.ui;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class PagesController {

    public static void changeFrame(Fragment fragment, AppCompatActivity activity, int host_container){
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(host_container, fragment);
        transaction.commit();
    }

    public static void startNewActivity(Fragment fragment, Class<?> cls){
        Intent intent = new Intent(fragment.getActivity(), cls);
        fragment.startActivity(intent);
        fragment.getActivity().finish();
    }

    public static void startNewActivity(Fragment fragment, Class<?> cls, String name, String id){
        Intent intent = new Intent(fragment.getActivity(), cls);
        intent.putExtra(name, id);
        fragment.startActivity(intent);
        fragment.getActivity().finish();
    }

}