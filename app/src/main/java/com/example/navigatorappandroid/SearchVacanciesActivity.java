package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class SearchVacanciesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onConfirm(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View coreView = inflater.inflate(R.layout.activity_search_vacancies, null);
        Spinner spinner = coreView.findViewById(R.id.profession_choice);
        String profession = spinner.getSelectedItem().toString();
        Intent intent = new Intent(this, WorkMapEmployeeActivity.class);
        intent.putExtra("profession", profession);
        startActivity(intent);
    }

    public void onBack(View view) {
        Intent intent = new Intent(this, WorkMapEmployeeActivity.class);
        startActivity(intent);
    }
}
