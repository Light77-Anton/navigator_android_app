package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.retrofit.AuthApi;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.response.LoginResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchVacanciesActivity extends AppCompatActivity {

    private UserInfoResponse userInfoResponse;
    private GeneralApi generalApi;
    private AuthApi authApi;
    private RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vacancies);
        Intent intent = new Intent(this, LoginActivity.class);
        retrofitService = new RetrofitService();
        authApi = retrofitService.getRetrofit().create(AuthApi.class);
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
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
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                userInfoResponse = response.body();
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(SearchVacanciesActivity.this, "error: 'getUserInfo' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onConfirm(View view) {
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.search_employees_autocomplete);
        String profession = autoCompleteTextView.getText().toString();
        Intent intent;
        if (userInfoResponse.getCurrentWorkDisplay() == 1) {
            intent = new Intent(this, WorkMapEmployeeActivity.class);
        } else {
            intent = new Intent(this, WorkListEmployeeActivity.class);
        }
        intent.putExtra("profession", profession);
        startActivity(intent);
    }

    public void onBack(View view) {
        Intent intent;
        if (userInfoResponse.getCurrentWorkDisplay() == 1) {
            intent = new Intent(this, WorkMapEmployeeActivity.class);
        } else {
            intent = new Intent(this, WorkListEmployeeActivity.class);
        }
        startActivity(intent);
    }
}
