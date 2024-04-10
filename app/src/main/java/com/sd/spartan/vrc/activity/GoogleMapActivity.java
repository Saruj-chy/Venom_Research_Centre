package com.sd.spartan.vrc.activity;

import static com.sd.spartan.vrc.controller.AppConstants.FALSE;
import static com.sd.spartan.vrc.controller.AppConstants.TRUE;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sd.spartan.vrc.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap googleMap;
    SupportMapFragment supportMapFragment;
    SearchView mSearchView;
    LatLng mLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        SimpleClass.CheckInActive(TRUE);


        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mSearchView = findViewById(R.id.sv_location);

        mLatLng = new LatLng(LocationController.latitude, LocationController.longitude);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = mSearchView.getQuery().toString();
                List<Address>  addressList = null;

                Geocoder geocoder = new Geocoder(GoogleMapActivity.this);
                try{
                    addressList = geocoder.getFromLocationName(location,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                assert addressList != null;
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                googleMap.clear();
                googleMap.addMarker(markerOptions.position(latLng).title(location));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        this.googleMap.addMarker(new MarkerOptions().position(mLatLng));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 12));
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng,10));

        this.googleMap.setOnMapClickListener(latLng -> {
            this.googleMap.clear();
            this.googleMap.addMarker(new MarkerOptions().position(latLng));
            CreateVenomActivity.placeName= getAddress(this,latLng.latitude, latLng.longitude );

            Intent data = new Intent();
            setResult(60, data);
            SimpleClass.CheckInActive(true);
            finish();
        });



    }
    public String getAddress(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            return obj.getAddressLine(0);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!SimpleClass.CheckActive){
            SimpleClass.IntentLogin(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SimpleClass.CheckInActive(FALSE);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        SimpleClass.CheckInActive(TRUE);
    }

}