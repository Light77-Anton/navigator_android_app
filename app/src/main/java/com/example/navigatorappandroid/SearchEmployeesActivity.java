package com.example.navigatorappandroid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;

public class SearchEmployeesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_employees);
        setCurrentActivity(this);
    }

    public void onConfirm(View view) {
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.search_employees_autocomplete);
        String profession = autoCompleteTextView.getText().toString();
        onBack(profession);
    }

    public void onBack(String profession) {
        Activity lastActivity = getLastActivity();
        if (lastActivity != null) {
            Intent intent = new Intent(this, lastActivity.getClass());
            intent.putExtras(arguments);
            intent.putExtra("profession", profession);
            removeActivityFromQueue();
            finish();
            startActivity(intent);
        }
    }

    public void onBack(View view) {
        Activity lastActivity = getLastActivity();
        if (lastActivity != null) {
            Intent intent = new Intent(this, lastActivity.getClass());
            intent.putExtras(arguments);
            removeActivityFromQueue();
            finish();
            startActivity(intent);
        }
    }
}
