package com.example.navigatorappandroid.handler;
import android.os.Handler;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.request.LocationRequest;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationUpdateHandler {

    private static final int UPDATE_INTERVAL = 15000; // 15 seconds
    private Handler handler;
    private Runnable locationUpdateRunnable;

    public LocationUpdateHandler(double latitude, double longitude, long id) {
        handler = new Handler();
        locationUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                updateLocation(latitude, longitude, id);
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

    private void updateLocation(double latitude, double longitude, long id) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setId(id);
        locationRequest.setLatitude(latitude);
        locationRequest.setLongitude(longitude);
        RetrofitService retrofitService = new RetrofitService();
        GeneralApi generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        generalApi.updateLocation(locationRequest).enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {}
            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {}
        });
    }
}