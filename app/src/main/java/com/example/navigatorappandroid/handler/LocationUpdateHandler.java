package com.example.navigatorappandroid.handler;
import android.os.Handler;

public class LocationUpdateHandler {

    private static final int UPDATE_INTERVAL = 15000; // 15 seconds
    private Handler handler;
    private Runnable locationUpdateRunnable;

    public LocationUpdateHandler() {
        handler = new Handler();
        locationUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                updateLocation();
                handler.postDelayed(this, UPDATE_INTERVAL);
            }
        };
    }

    public void startLocationUpdates() {
        handler.postDelayed(locationUpdateRunnable, UPDATE_INTERVAL);
    }

    public void stopLocationUpdates() {
        handler.removeCallbacks(locationUpdateRunnable);
    }

    private void updateLocation() {

    }
}