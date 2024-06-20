package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.retrofit.AuthApi;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.request.ChangePasswordRequest;
import com.example.navigatorappandroid.retrofit.response.LoginResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private UserInfoResponse userInfoResponse;
    private RetrofitService retrofitService;
    private GeneralApi generalApi;
    private AuthApi authApi;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;
    private Bundle arguments;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Intent intent = new Intent(this, LoginActivity.class);
        retrofitService = new RetrofitService();
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        authApi = retrofitService.getRetrofit().create(AuthApi.class);
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
                Toast.makeText(ChangePasswordActivity.this, "error: 'getUserInfo' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        arguments = getIntent().getExtras();
    }

    public void onConfirmClick(View view) {
        passwordEditText = findViewById(R.id.password);
        repeatPasswordEditText = findViewById(R.id.repeat_password);
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setPassword(passwordEditText.getText().toString());
        changePasswordRequest.setRepeated_password(repeatPasswordEditText.getText().toString());
        generalApi.changePassword(changePasswordRequest).enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                if (response.body().isResult()) {
                    Intent intent;
                    if (userInfoResponse.getRole().equals("Employee")) {
                        intent = new Intent(view.getContext(), EmployeeSettingsActivity.class);
                    } else {
                        intent = new Intent(view.getContext(), EmployerSettingsActivity.class);
                    }
                    intent.putExtra("activity", arguments.getString("activity"));
                    startActivity(intent);
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (String error : response.body().getErrors()) {
                        sb.append(error);
                        sb.append(" ");
                    }
                    Toast.makeText(ChangePasswordActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "error: 'changePassword' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackClick(View view) {
        Intent intent;
        Bundle arguments = getIntent().getExtras();
        if (arguments.getString("role").equals("employee")) {
            if (arguments.getString("activity").equals("map")) {
                intent = new Intent(this, WorkMapEmployeeActivity.class);
                startActivity(intent);
            }
            intent = new Intent(this, WorkListEmployeeActivity.class);
            startActivity(intent);
        } else if (arguments.getString("role").equals("employer")) {
            if (arguments.getString("activity").equals("map")) {
                intent = new Intent(this, WorkMapEmployerActivity.class);
                startActivity(intent);
            }
            intent = new Intent(this, WorkListEmployerActivity.class);
            startActivity(intent);
        } else {
            if (arguments.getString("activity").equals("map")) {
                intent = new Intent(this, WorkMapEmployeeActivity.class);
                startActivity(intent);
            }
            intent = new Intent(this, WorkListEmployeeActivity.class);
            startActivity(intent);
        }
    }
}
