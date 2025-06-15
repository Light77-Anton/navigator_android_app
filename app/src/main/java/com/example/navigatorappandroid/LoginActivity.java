package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

    private Button restoreButton;
    private TextView recoveryLabel;
    private EditText recoveryEmail;
    private boolean isRecoveryFieldActivated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
        setContentView(R.layout.activity_login);
        recoveryLabel = findViewById(R.id.recovery_label);
        recoveryEmail = findViewById(R.id.recovery_email);
        restoreButton = findViewById(R.id.restore_password);
        if (arguments != null && arguments.getBoolean("is_auth_error")) {
            Toast.makeText(LoginActivity.this, "Authentication error: you " +
                    "have to login again", Toast.LENGTH_SHORT).show();
        }
    }

    public void onConfirm(View view) {
        EditText emailEditText = findViewById(R.id.login);
        EditText passwordEditText = findViewById(R.id.password);
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
                        if (response.body().getCurrentWorkDisplay() == 0) {
                            intent = new Intent(view.getContext(), WorkListEmployeeActivity.class);
                        } else {
                            intent = new Intent(view.getContext(), WorkMapEmployeeActivity.class);
                        }
                        startActivity(intent);
                    } else if (response.body().getUserRole().equals("Employer")) {
                        if (response.body().getCurrentWorkDisplay() == 0) {
                            intent = new Intent(view.getContext(), WorkListEmployerActivity.class);
                        } else {
                            intent = new Intent(view.getContext(), WorkMapEmployerActivity.class);
                        }
                        startActivity(intent);
                    }
                    /*
                    else {
                        intent = new Intent(view.getContext(), );
                        startActivity(intent);
                    }
                     */
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: 'login'" +
                        " method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onRestorePassword(View view) {
        if (!isRecoveryFieldActivated) {
            isRecoveryFieldActivated = true;
            recoveryLabel.setClickable(true);
            recoveryLabel.setVisibility(View.VISIBLE);
            recoveryEmail.setClickable(true);
            recoveryEmail.setVisibility(View.VISIBLE);
            restoreButton.setText("Confirm recovery email");
            restoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(LoginActivity.this, "A recovery mail has been sent", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void onBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
