package com.example.navigatorappandroid;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.model.Vacancy;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployerVacanciesSettingActivity extends AppCompatActivity {

    UserInfoResponse userInfoResponse;
    RetrofitService retrofitService;
    GeneralApi generalApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacancies_setting);
        ScrollView scrollView = (ScrollView) getLayoutInflater().inflate(R.layout.activity_vacancies_setting, null);
        LinearLayout linearLayout = scrollView.findViewById(R.id.activity_vacancies_setting_layout);
        retrofitService = new RetrofitService();
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                userInfoResponse = response.body();
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(EmployerVacanciesSettingActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        List<Vacancy> vacancyList = userInfoResponse.getEmployerRequests().getVacancies();
        for (Vacancy vacancy : vacancyList) {
            Button button = new Button(this);
            button.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
            button.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
            button.setBackground(getResources().getDrawable(R.drawable.rectangle_button));
            button.setTextColor(getResources().getColor(R.color.white));
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30.0f);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 30, 0, 0);
            button.setLayoutParams(layoutParams);
            button.setText(vacancy.getId().toString());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), EmployerVacancyEditActivity.class);
                    intent.putExtra("vacancy_id", button.getText().toString());
                    startActivity(intent);
                }
            });
            linearLayout.addView(button);
        }
    }

    public void onAddVacancy(View view) {
        Intent intent = new Intent(this, EmployerVacancyEditActivity.class);
        startActivity(intent);
    }

    public void onBackClick(View view) {
        Intent intent;
        Bundle arguments = getIntent().getExtras();
        if (arguments.getString("activity").equals("map")) {
            intent = new Intent(this, WorkMapEmployeeActivity.class);
            startActivity(intent);
        }
        intent = new Intent(this, WorkListEmployerActivity.class);
        startActivity(intent);
    }
}
