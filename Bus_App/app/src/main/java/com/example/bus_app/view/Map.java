package com.example.bus_app.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bus_app.R;
import com.example.bus_app.viewmodel.SharedVM;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

public class Map extends Fragment implements OnMapReadyCallback {

    private SharedVM share;

    private boolean click = true;

    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private static final int REQUEST_SETTING_CODE = 1001;
    private SupportMapFragment supportMapFragment;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    //
    private JSONObject json;
    private double originLatitude, originLongitude;
    private double endLatitude, endLongitude;
    //

    public static Map newInstance() {
        return new Map();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        share = ViewModelProviders.of(requireActivity()).get(SharedVM.class);

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        fetchLastLocation();
        ((FloatingActionButton) view.findViewById(R.id.map_floating_btn_settings)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click) {
                    ((RelativeLayout) view.findViewById(R.id.map_floating_container_box)).setVisibility(View.VISIBLE);
                    click = false;
                } else {
                    ((RelativeLayout) view.findViewById(R.id.map_floating_container_box)).setVisibility(View.GONE);
                    click = true;
                }
            }
        });
        changeSide((FloatingActionButton) view.findViewById(R.id.map_floating_btn_settings), share.getBool().getValue());
        changeSide((RelativeLayout) view.findViewById(R.id.map_floating_container_box), share.getBool().getValue());

        return view;
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE);
            return;
        }

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);
        //
        locationRequest.setFastestInterval(2000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getContext()).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) exception;
                                resolvableApiException.startResolutionForResult(getActivity(), REQUEST_SETTING_CODE);
                            } catch (IntentSender.SendIntentException e) {

                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }

            }
        });

        //
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        currentLocation = location;
                        //SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
                        supportMapFragment.getMapAsync(Map.this);
                    }
                }
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE);
            return;
        }
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions marker = new MarkerOptions().position(latLng).title("I'm here").flat(true);
        CameraPosition camera = new CameraPosition.Builder().target(latLng).zoom(20).bearing(1).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        //googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        MarkerOptions stop= new MarkerOptions().position(new LatLng(-0.315407, -78.441998)).title("PARADA: Condorvall, Vingala, Expreso");
        MarkerOptions stop_2= new MarkerOptions().position(new LatLng(-0.315345, -78.442192)).title("PARADA: Condorvall, Vingala, Expreso");
        stop.icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_stop_bus));
        stop_2.icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_stop_bus));
        MarkerOptions bus_start= new MarkerOptions().position(new LatLng(-0.320921, -78.442374)).title("");
        MarkerOptions bus_end= new MarkerOptions().position(new LatLng(-0.306327, -78.449482)).title("");
        googleMap.clear();
        //googleMap.addMarker(marker);
        if(!((ToggleButton)getView().findViewById(R.id.map_floating_cb_line)).isChecked()){
            googleMap.addMarker(stop);
            googleMap.addMarker(stop_2);
        }
        if(!((ToggleButton)getView().findViewById(R.id.map_floating_cb_stop)).isChecked()){
            googleMap.addMarker(bus_start);
            googleMap.addMarker(bus_end);
        }
        googleMap.setMyLocationEnabled(true);

        /*
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    json = new JSONObject(response);
                    route(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);*/
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /*
    private void route(JSONObject json) {

    }
    */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

        case REQUEST_CODE:
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                fetchLastLocation();
            }
            break;
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_SETTING_CODE){
            switch (resultCode){
                case Activity
                        .RESULT_OK:
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    }
}