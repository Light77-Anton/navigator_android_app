package com.example.navigatorappandroid.handler;
import android.os.Handler;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeStatusHandler {

    private static final int UPDATE_INTERVAL = 15000; // 15 seconds
    private Handler handler;
    private Runnable statusUpdateRunnable;
    private boolean result;

    public EmployeeStatusHandler() {
        handler = new Handler();
        statusUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                RetrofitService retrofitService = new RetrofitService();
                GeneralApi generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
                generalApi.checkEmployeeStatus().enqueue(new Callback<ResultErrorsResponse>() {
                    @Override
                    public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                        result = false;
                        if (response.body().isResult()) {
                           result = true;
                        }
                    }
                    @Override
                    public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                        result = false;
                    }
                });
                if (!result) {
                    handler.postDelayed(this, UPDATE_INTERVAL);
                }
            }
        };
    }

    public void startStatusChecking() {
        handler.postDelayed(statusUpdateRunnable, UPDATE_INTERVAL);
    }

    public void stopStatusChecking() {
        handler.removeCallbacks(statusUpdateRunnable);
    }
}
