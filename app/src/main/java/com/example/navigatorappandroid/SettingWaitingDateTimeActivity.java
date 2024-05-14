package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.retrofit.ChatApi;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.request.VacancyRequest;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import java.time.LocalDateTime;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingWaitingDateTimeActivity extends AppCompatActivity {

    private RetrofitService retrofitService;
    private GeneralApi generalApi;
    private ChatApi chatApi;
    private UserInfoResponse userInfoResponse;
    private View entireView;
    VacancyRequest vacancyRequest;
    DatePicker datePicker;
    TimePicker timePicker;
    CheckBox waitingUntilStartDateTimeCheckbox;
    Bundle arguments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arguments = getIntent().getExtras();
        entireView = getLayoutInflater().inflate(R.layout.activity_waiting_date_time_setting, null);
        retrofitService = new RetrofitService();
        chatApi = retrofitService.getRetrofit().create(ChatApi.class);
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                userInfoResponse = response.body();
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(SettingWaitingDateTimeActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        setContentView(R.layout.activity_waiting_date_time_setting);
    }

    public void onSend(View view) {
        waitingUntilStartDateTimeCheckbox = entireView.findViewById(R.id.waiting_until_start_date_time_checkbox);
        datePicker = entireView.findViewById(R.id.date_picker);
        timePicker = entireView.findViewById(R.id.time_picker);
        vacancyRequest = (VacancyRequest) arguments.getSerializable("vacancy_request");
        if (waitingUntilStartDateTimeCheckbox.isChecked()) {
            vacancyRequest.setWaitingTimestamp(vacancyRequest.getStartTimestamp());
        } else {
            vacancyRequest.setWaitingTimestamp(LocalDateTime.of(datePicker.getYear(), datePicker.getMonth(),
                    datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute()));
        }
        chatApi.sendOfferFromEmployer(arguments.getString("id"), vacancyRequest).enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                if (response.body().isResult()) {
                    if (userInfoResponse.getEmployerRequests() != null && userInfoResponse.getEmployeeData() == null) {
                        Intent intent = new Intent(view.getContext(), EmployeeExtendedInfoActivity.class);
                        intent.putExtra();
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(view.getContext(), EmployerExtendedInfoActivity.class); // ?
                        intent.putExtra();
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(SettingWaitingDateTimeActivity.this, response.body().getErrors().get(0), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(SettingWaitingDateTimeActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
