package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.navigatorappandroid.retrofit.request.ChangePasswordRequest;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        passwordEditText = findViewById(R.id.password);
        repeatPasswordEditText = findViewById(R.id.repeat_password);
    }

    public void onConfirmClick(View view) {
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
                    finish();
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
        if (userInfoResponse.getRole().equals("Employee")) {
            intent = new Intent(view.getContext(), EmployeeSettingsActivity.class);
        } else {
            intent = new Intent(view.getContext(), EmployerSettingsActivity.class);
        }
        finish();
        startActivity(intent);
    }
}
