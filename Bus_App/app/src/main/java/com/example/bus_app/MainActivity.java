package com.example.bus_app;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity{

    private HomePage home;
    private MapPage map;
    private PayPage pay;
    private HistoryPage history;
    private RechargePage recharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hide_status();
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(navItemSelected);
        init();
        loadFragment(new LoginPage(nav));
    }

    private void hide_status(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
    }

    private void loadFragment(Fragment fragment){
        ControllerLoad.loadFragment(fragment, this);
    }

    private void init(){
        home = new HomePage();
        map = new MapPage();
        pay = new PayPage();
        history = new HistoryPage();
        recharge = new RechargePage();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navItemSelected =  new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.home_element_menu:
                    loadFragment(home);
                    return true;
                case R.id.map_element_menu:
                    return true;
                case R.id.history_element_menu:
                    loadFragment(history);
                    return true;
            }
            return false;
        }
    };
}