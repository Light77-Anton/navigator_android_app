package com.example.navigatorappandroid;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.example.navigatorappandroid.model.Vacancy;
import java.util.List;

public class EmployerVacanciesSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacancies_setting);
        LinearLayout linearLayout = findViewById(R.id.activity_vacancies_setting_layout);
        List<Vacancy> vacancyList = userInfoResponse.getEmployerRequests().getVacancies();
        for (Vacancy vacancy : vacancyList) {
            Button button = new Button(this);
            button.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
            button.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.rectangle_button));
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
                    finish();
                    startActivity(intent);
                }
            });
            linearLayout.addView(button);
        }
    }

    public void onAddVacancy(View view) {
        Intent intent = new Intent(this, EmployerVacancyEditActivity.class);
        finish();
        startActivity(intent);
    }

    public void onBackClick(View view) {
        Intent intent;
        if (userInfoResponse.getCurrentWorkDisplay() == 1) {
            intent = new Intent(this, WorkMapEmployerActivity.class);
        } else {
            intent = new Intent(this, WorkListEmployerActivity.class);
        }
        finish();
        startActivity(intent);
    }
}
