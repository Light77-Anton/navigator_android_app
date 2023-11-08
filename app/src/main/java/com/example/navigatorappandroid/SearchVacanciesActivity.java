package com.example.navigatorappandroid;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
    }

    public void onBack(View view) {

    }
}
