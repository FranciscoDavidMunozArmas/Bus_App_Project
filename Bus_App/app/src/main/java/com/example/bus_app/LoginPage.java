package com.example.bus_app;

import android.content.ClipData;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

        ((Button)view.findViewById(R.id.btn_forget_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeRelativeLayout(((RelativeLayout)view.findViewById(R.id.forget_password)), ((RelativeLayout)view.findViewById(R.id.sign_up)), ((RelativeLayout)view.findViewById(R.id.login)));
            }
        });

        ((Button)view.findViewById(R.id.btn_sign_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeRelativeLayout(((RelativeLayout)view.findViewById(R.id.sign_up)), ((RelativeLayout)view.findViewById(R.id.login)), ((RelativeLayout)view.findViewById(R.id.forget_password)));
            }
        });

        ((Button)view.findViewById(R.id.btn_back_sign_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeRelativeLayout(((RelativeLayout)view.findViewById(R.id.login)), ((RelativeLayout)view.findViewById(R.id.forget_password)), ((RelativeLayout)view.findViewById(R.id.sign_up)));
            }
        });

        ((Button)view.findViewById(R.id.btn_back_forget_pwd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeRelativeLayout(((RelativeLayout)view.findViewById(R.id.login)), ((RelativeLayout)view.findViewById(R.id.forget_password)), ((RelativeLayout)view.findViewById(R.id.sign_up)));
            }
        });

        ((Button)view.findViewById(R.id.btn_change_pwd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView)view.findViewById(R.id.forget_pwd_username_error)).setVisibility(View.GONE);
                ((TextView)view.findViewById(R.id.forget_pwd_pwd_error)).setVisibility(View.GONE);
                ((TextView)view.findViewById(R.id.forget_pwd_confirm_pwd_error)).setVisibility(View.GONE);
                if(!checkInput(((EditText)view.findViewById(R.id.forget_pwd_username_input)).getText().toString()) &&
                        !checkInput(((EditText)view.findViewById(R.id.forget_pwd_pwd_input)).getText().toString()) &&
                        !checkInput(((EditText)view.findViewById(R.id.forget_pwd_confirm_pwd_input)).getText().toString()) &&
                        ((EditText)view.findViewById(R.id.forget_pwd_pwd_input)).getText().toString().equals(((EditText)view.findViewById(R.id.forget_pwd_confirm_pwd_input)).getText().toString())){
                    ChangeRelativeLayout(((RelativeLayout)view.findViewById(R.id.login)), ((RelativeLayout)view.findViewById(R.id.forget_password)), ((RelativeLayout)view.findViewById(R.id.sign_up)));
                }

                if(checkInput(((EditText)view.findViewById(R.id.forget_pwd_username_input)).getText().toString())){
                    ((TextView)view.findViewById(R.id.forget_pwd_username_error)).setVisibility(View.VISIBLE);
                }
                if(checkInput(((EditText)view.findViewById(R.id.forget_pwd_pwd_input)).getText().toString())){
                    ((TextView)view.findViewById(R.id.forget_pwd_pwd_error)).setVisibility(View.VISIBLE);
                }
                if(checkInput(((EditText)view.findViewById(R.id.forget_pwd_confirm_pwd_input)).getText().toString()) ||
                        !((EditText)view.findViewById(R.id.forget_pwd_pwd_input)).getText().toString().equals(((EditText)view.findViewById(R.id.forget_pwd_confirm_pwd_input)).getText().toString())){
                    ((TextView)view.findViewById(R.id.forget_pwd_confirm_pwd_error)).setVisibility(View.VISIBLE);
                }
            }
        });

        ((Button)view.findViewById(R.id.btn_accept_sign_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView)view.findViewById(R.id.sign_up_txt_username_error)).setVisibility(View.GONE);
                ((TextView)view.findViewById(R.id.sign_up_txt_pwd_error)).setVisibility(View.GONE);
                ((TextView)view.findViewById(R.id.sign_up_txt_confirm_pwd_error)).setVisibility(View.GONE);
                ((TextView)view.findViewById(R.id.sign_up_cb_error)).setVisibility(View.GONE);
                if(!checkInput(((EditText)view.findViewById(R.id.sign_up_username_input)).getText().toString()) &&
                        !checkInput(((EditText)view.findViewById(R.id.sign_up_pwd_input)).getText().toString()) &&
                        !checkInput(((EditText)view.findViewById(R.id.sign_up_confirm_pwd_input)).getText().toString()) &&
                        ((EditText)view.findViewById(R.id.sign_up_confirm_pwd_input)).getText().toString().equals(((EditText)view.findViewById(R.id.sign_up_pwd_input)).getText().toString()) &&
                        ((CheckBox)view.findViewById(R.id.sign_up_cb)).isChecked()){
                    HomePage home = new HomePage();
                    getFragmentManager().beginTransaction().replace(R.id.splash_frame_container, home, home.getTag()).commit();
                    nav.setVisibility(View.VISIBLE);
                }

                if(checkInput(((EditText)view.findViewById(R.id.sign_up_username_input)).getText().toString())){
                    ((TextView)view.findViewById(R.id.sign_up_txt_username_error)).setVisibility(View.VISIBLE);
                }
                if(checkInput(((EditText)view.findViewById(R.id.sign_up_pwd_input)).getText().toString())){
                    ((TextView)view.findViewById(R.id.sign_up_txt_pwd_error)).setVisibility(View.VISIBLE);
                }
                if(checkInput(((EditText)view.findViewById(R.id.sign_up_confirm_pwd_input)).getText().toString()) ||
                        !((EditText)view.findViewById(R.id.sign_up_confirm_pwd_input)).getText().toString().equals(((EditText)view.findViewById(R.id.sign_up_pwd_input)).getText().toString())){
                    ((TextView)view.findViewById(R.id.sign_up_txt_confirm_pwd_error)).setVisibility(View.VISIBLE);
                }
                if(!((CheckBox)view.findViewById(R.id.sign_up_cb)).isChecked()){
                    ((TextView)view.findViewById(R.id.sign_up_cb_error)).setVisibility(View.VISIBLE);
                }
            }
        });

        ((Button)view.findViewById(R.id.btn_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView)view.findViewById(R.id.login_txt_username_error)).setVisibility(View.GONE);
                ((TextView)view.findViewById(R.id.login_txt_password_error)).setVisibility(View.GONE);
                if(!checkInput(((EditText)view.findViewById(R.id.login_pwd_input)).getText().toString()) &&
                        !checkInput(((EditText)view.findViewById(R.id.login_username_input)).getText().toString())){
                    HomePage home = new HomePage();
                    getFragmentManager().beginTransaction().replace(R.id.splash_frame_container, home, home.getTag()).commit();
                    nav.setVisibility(View.VISIBLE);
                }

                if(checkInput(((EditText)view.findViewById(R.id.login_username_input)).getText().toString())){
                    ((TextView)view.findViewById(R.id.login_txt_username_error)).setVisibility(View.VISIBLE);
                }
                if(checkInput(((EditText)view.findViewById(R.id.login_pwd_input)).getText().toString())){
                    ((TextView)view.findViewById(R.id.login_txt_password_error)).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void ChangeRelativeLayout(RelativeLayout visible, RelativeLayout gone_1, RelativeLayout gone_2){
        visible.setVisibility(View.VISIBLE);
        gone_1.setVisibility(View.GONE);
        gone_2.setVisibility(View.GONE);
    }

    private boolean hasBlank(String str){
        for (int i = 0; i < str.length(); i++){
            if(str.charAt(i) == ' '){
                return true;
            }
        }
        return false;
    }
    private boolean isBlank(String str){
        int blank = 0;
        for (int i = 0; i < str.length(); i++){
            if(str.charAt(i) == ' '){
                blank++;
            }
        }
        return (blank == str.length())?true : false;
    }

    private boolean checkInput(String str){
        return (str.isEmpty() || hasBlank(str) || isBlank(str));
    }
}