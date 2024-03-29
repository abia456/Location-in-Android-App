package com.example.location_module;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class GPS_Service extends Service{


    //int count=0;
    private LocationListener listener;
    private LocationManager locationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {


        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        listener = new LocationListener() {

            List<Address> addresses;
            @Override
            public void onLocationChanged(Location location) {


              try {
                  addresses=
                  geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
              }
              catch (IOException e)
              {
                  e.printStackTrace();
              }

                Intent i = new Intent("location_update");
                i.putExtra("longitude", location.getLongitude());
                i.putExtra("latitude", location.getLatitude());
                //i.putExtra("Address", addresses.get(0).toString());
                i.putExtra("Accuracy",location.getAccuracy());

               // float distance= A.distanceTo(B);

                    sendBroadcast(i);


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,0,listener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //count=0;
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }








}


