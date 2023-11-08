package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.retrofit.AuthApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.request.LoginRequest;
import com.example.navigatorappandroid.retrofit.response.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onConfirm(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View coreView = inflater.inflate(R.layout.activity_login, null);
        EditText emailEditText = coreView.findViewById(R.id.login);
        EditText passwordEditText = coreView.findViewById(R.id.password);
        RetrofitService retrofitService = new RetrofitService();
        AuthApi authApi = retrofitService.getRetrofit().create(AuthApi.class);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(emailEditText.getText().toString());
        loginRequest.setPassword(passwordEditText.getText().toString());
        authApi.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body().getBlockMessage() != null) {
                    Toast.makeText(LoginActivity.this, response.body().getBlockMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent;
                    if (response.body().getUserRole().equals("Employee")) {
                        intent = new Intent(coreView.getContext(), WorkMapEmployeeActivity.class);
                        startActivity(intent);
                    } else if (response.body().getUserRole().equals("Employer")) {
                        intent = new Intent(coreView.getContext(), WorkMapEmployerActivity.class);
                        startActivity(intent);
                    } else {
                        intent = new Intent(coreView.getContext(), );
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBack(View view) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
