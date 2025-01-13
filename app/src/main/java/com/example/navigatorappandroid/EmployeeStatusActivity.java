package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.navigatorappandroid.retrofit.request.StatusRequest;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeStatusActivity extends BaseActivity {

    private StatusRequest statusRequest;
    private byte currentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_status);
        DatePicker datePicker = findViewById(R.id.datePicker);
        statusRequest = new StatusRequest();
        RadioGroup radios = findViewById(R.id.radios);
        radios.setOnCheckedChangeListener((radiogroup, id)-> {
            RadioButton radio = findViewById(id);
            switch (radio.getContentDescription().toString()) {
                case "active":
                    statusRequest.setStatus("active");
                    datePicker.setClickable(false);
                    datePicker.setVisibility(View.INVISIBLE);
                    break;
                case "inactive":
                    statusRequest.setStatus("inactive");
                    datePicker.setClickable(false);
                    datePicker.setVisibility(View.INVISIBLE);
                    break;
                default:
                    statusRequest.setStatus("custom");
                    long timestamp = System.currentTimeMillis();
                    LocalDate currentDate = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
                    statusRequest.setTimestamp(currentDate.atStartOfDay(ZoneId.systemDefault())
                            .toInstant().toEpochMilli());
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
            }
        });
        TextView helperText = findViewById(R.id.helper_text);
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
        currentStatus = userInfoResponse.getEmployeeData().getStatus();
        RadioButton button;
        switch (currentStatus) {
            case 0:
                button = findViewById(R.id.inactive);
                break;
            case 1:
                button = findViewById(R.id.active);
                break;
            default:
                button = findViewById(R.id.custom);
        }
        button.setChecked(true);
    }

    public void onBackClick(View view) {
        if (statusRequest.getStatus() != null) {
            generalApi.employeeStatus(statusRequest).enqueue(new Callback<ResultErrorsResponse>() {
                @Override
                public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                    Intent intent;
                    if (userInfoResponse.getCurrentWorkDisplay() == 1) {
                        intent = new Intent(view.getContext(), WorkMapEmployeeActivity.class);
                        finish();
                        startActivity(intent);
                    }
                    intent = new Intent(view.getContext(), WorkListEmployeeActivity.class);
                    finish();
                    startActivity(intent);
                }
                @Override
                public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                    Toast.makeText(EmployeeStatusActivity.this, "error: 'employeeStatus' " +
                            "method is failure", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
