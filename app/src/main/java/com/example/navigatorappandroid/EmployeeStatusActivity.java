package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.request.StatusRequest;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeStatusActivity extends AppCompatActivity {

    RetrofitService retrofitService;
    GeneralApi generalApi;
    StatusRequest statusRequest;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        relativeLayout = findViewById(R.id.activity_employee_status_layout);
        setContentView(relativeLayout);
        retrofitService = new RetrofitService();
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        statusRequest = new StatusRequest();
        RadioGroup radios = findViewById(R.id.radios);
        radios.setOnCheckedChangeListener((radiogroup, id)-> {
            RadioButton radio = findViewById(id);
            switch (radio.getText().toString()) {
                case "Active":
                    statusRequest.setStatus("active");
                    break;
                case "Inactive":
                    statusRequest.setStatus("inactive");
                    break;
                default:
                    statusRequest.setStatus("custom");
                    long timestamp = System.currentTimeMillis();
                    LocalDate currentDate = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
                    statusRequest.setTimestamp(currentDate.atStartOfDay(ZoneId.systemDefault())
                            .toInstant().toEpochMilli());
                    DatePicker datePicker = findViewById(R.id.datePicker);
                    datePicker.setClickable(true);
                    datePicker.setVisibility(View.VISIBLE);
                    datePicker.init(currentDate.getYear(), currentDate.getMonthValue(), currentDate.getDayOfMonth(),
                            new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            LocalDate localDate = LocalDate.of(year, monthOfYear, dayOfMonth);
                            statusRequest.setTimestamp(localDate.atStartOfDay(ZoneId.systemDefault())
                                    .toInstant().toEpochMilli());
                        }
                    });
                    break;
            }
        });
        TextView helperText = relativeLayout.findViewById(R.id.helper_text);
        Button activeHelpButton = findViewById(R.id.active_help_button);
        activeHelpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                helperText.setText(R.string.active_status_description);
                helperText.setVisibility(View.VISIBLE);
            }
        });
        Button inactiveHelpButton = findViewById(R.id.inactive_help_button);
        inactiveHelpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                helperText.setText(R.string.inactive_status_description);
                helperText.setVisibility(View.VISIBLE);
            }
        });
        Button customHelpButton = findViewById(R.id.custom_help_button);
        customHelpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                helperText.setText(R.string.temporarily_inactive_status_description);
                helperText.setVisibility(View.VISIBLE);
            }
        });
    }
    public void onBackClick(View view) {
        if (statusRequest.getStatus() != null) {
            generalApi.employeeStatus(statusRequest).enqueue(new Callback<ResultErrorsResponse>() {
                @Override
                public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {}

                @Override
                public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                    Toast.makeText(EmployeeStatusActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
        Intent intent;
        Bundle arguments = getIntent().getExtras();
        if (arguments.getString("activity").equals("map")) {
            intent = new Intent(this, WorkMapEmployeeActivity.class);
            startActivity(intent);
        }
        intent = new Intent(this, WorkListEmployeeActivity.class);
        startActivity(intent);
    }
}
