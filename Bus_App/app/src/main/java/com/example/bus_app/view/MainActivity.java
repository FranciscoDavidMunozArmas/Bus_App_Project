package com.example.bus_app.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.bus_app.R;
import com.example.bus_app.ui.AlertDialogHelper;
import com.example.bus_app.ui.PagesController;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((BottomNavigationView) findViewById(R.id.bottom_navigation)).setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, Home.newInstance())
                    .commitNow();
        }
    }

    private void loadFragment(Fragment fragment){
        PagesController.changeFrame(fragment, this, R.id.main_container);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_home_element:
                    loadFragment(Home.newInstance());
                    return true;
                case R.id.menu_map_element:
                    loadFragment(Map.newInstance());
                    return true;
                case R.id.menu_history_element:
                    loadFragment(History.newInstance());
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        AlertDialogHelper.ExitBack(this);
    }


}