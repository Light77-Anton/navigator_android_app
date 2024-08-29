package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;

public class SearchVacanciesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vacancies);
    }

    public void onConfirm(View view) {
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.search_employees_autocomplete);
        String profession = autoCompleteTextView.getText().toString();
        Intent intent;
        if (userInfoResponse.getCurrentWorkDisplay() == 1) {
            intent = new Intent(this, WorkMapEmployeeActivity.class);
        } else {
            intent = new Intent(this, WorkListEmployeeActivity.class);
        }
        intent.putExtra("profession", profession);
        startActivity(intent);
    }

    public void onBack(View view) {
        Intent intent;
        if (userInfoResponse.getCurrentWorkDisplay() == 1) {
            intent = new Intent(this, WorkMapEmployeeActivity.class);
        } else {
            intent = new Intent(this, WorkListEmployeeActivity.class);
        }
        startActivity(intent);
    }
}
