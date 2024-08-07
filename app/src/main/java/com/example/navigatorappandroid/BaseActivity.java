package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.retrofit.AuthApi;
import com.example.navigatorappandroid.retrofit.ChatApi;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.response.LoginResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseActivity extends AppCompatActivity {

    protected GeneralApi generalApi;
    protected AuthApi authApi;
    protected SearchApi searchApi;
    protected ChatApi chatApi;
    protected UserInfoResponse userInfoResponse;
    protected RetrofitService retrofitService;
    protected Bundle arguments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arguments = getIntent().getExtras();
        retrofitService = new RetrofitService();
        authApi = retrofitService.getRetrofit().create(AuthApi.class);
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        searchApi = retrofitService.getRetrofit().create(SearchApi.class);
        chatApi = retrofitService.getRetrofit().create(ChatApi.class);
        checkUserAuth();
        getUserInfo();
    }

    private void getUserInfo() {
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                userInfoResponse = response.body();
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(BaseActivity.this, "error: 'getUserInfo' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserAuth() {
        Intent intent = new Intent(this, LoginActivity.class);
        authApi.authCheck().enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (!response.body().isResult()) {
                    intent.putExtra("is_auth_error", true);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                intent.putExtra("is_auth_error", true);
                startActivity(intent);
            }
        });
    }
}
