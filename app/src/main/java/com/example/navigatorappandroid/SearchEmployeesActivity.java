package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import androidx.appcompat.app.AppCompatActivity;

public class SearchEmployeesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onConfirm(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View coreView = inflater.inflate(R.layout.activity_search_employees, null);
        AutoCompleteTextView autoCompleteTextView = coreView.findViewById(R.id.search_employees_autocomplete);
        String profession = autoCompleteTextView.getText().toString();
        Intent intent;
        Bundle arguments = getIntent().getExtras();
        if (arguments.getString("activity").equals("map")) {
            intent = new Intent(this, WorkMapEmployerActivity.class);
            intent.putExtra("profession", profession);
            startActivity(intent);
        }
        intent = new Intent(this, WorkListEmployerActivity.class);
        intent.putExtra("profession", profession);
        startActivity(intent);
    }

    public void onBack(View view) {
        Intent intent;
        Bundle arguments = getIntent().getExtras();
        if (arguments.getString("activity").equals("map")) {
            intent = new Intent(this, WorkMapEmployerActivity.class);
            startActivity(intent);
        }
        intent = new Intent(this, WorkListEmployerActivity.class);
        startActivity(intent);
    }
}
