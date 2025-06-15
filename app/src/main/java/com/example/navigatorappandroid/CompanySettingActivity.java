package com.example.navigatorappandroid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.navigatorappandroid.retrofit.request.StringRequest;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanySettingActivity extends BaseActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_setting);
        setCurrentActivity(this);
        editText = findViewById(R.id.company_setting);
    }

    public void onConfirmClick(View view) {
        StringRequest stringRequest = new StringRequest();
        stringRequest.setString(editText.getText().toString());
        authApi.makeRequestForCompanySetting(stringRequest).enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                Toast.makeText(CompanySettingActivity.this, getResources().getString
                        (R.string.company_setting_request_has_been_sent), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(CompanySettingActivity.this, "error: 'makeRequestForCompanySetting' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackClick(View view) {
        Activity lastActivity = getLastActivity();
        if (lastActivity != null) {
            Intent intent = new Intent(this, lastActivity.getClass());
            intent.putExtras(arguments);
            removeActivityFromQueue();
            finish();
            startActivity(intent);
        }
    }
}
