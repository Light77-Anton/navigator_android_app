package com.example.navigatorappandroid;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.request.InProgramMessageRequest;
import com.example.navigatorappandroid.retrofit.response.StringResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationSuccessActivity extends AppCompatActivity {

    private final String REGISTRATION_SUCCESS_MESSAGE = getResources().getString
            (R.string.registration_success_message_java);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
        String language = arguments.get("language").toString();
        LayoutInflater inflater = getLayoutInflater();
        View coreView = inflater.inflate(R.layout.activity_registration_success, null);
        TextView textView = coreView.findViewById(R.id.message);
        RetrofitService retrofitService = new RetrofitService();
        GeneralApi generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        InProgramMessageRequest inProgramMessageRequest = new InProgramMessageRequest();
        inProgramMessageRequest.setLanguage(language);
        inProgramMessageRequest.setCodeName(REGISTRATION_SUCCESS_MESSAGE);
        generalApi.getMessageInSpecifiedLanguage(inProgramMessageRequest).enqueue(new Callback<StringResponse>() {
            @Override
            public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                textView.setText(response.body().getString());
                setContentView(coreView);
            }

            @Override
            public void onFailure(Call<StringResponse> call, Throwable t) {
                Toast.makeText(RegistrationSuccessActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
