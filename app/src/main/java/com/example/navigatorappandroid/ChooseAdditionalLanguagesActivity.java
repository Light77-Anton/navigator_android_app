package com.example.navigatorappandroid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.example.navigatorappandroid.retrofit.response.TextListResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseAdditionalLanguagesActivity extends BaseActivity {

    private Spinner additionalLanguagesSpinner;
    private String languageEndonym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_additional_languages);
        setCurrentActivity(this);
        additionalLanguagesSpinner = findViewById(R.id.additional_languages_spinner);
        generalApi.getLanguagesList().enqueue(new Callback<TextListResponse>() {
            @Override
            public void onResponse(Call<TextListResponse> call, Response<TextListResponse> response) {
                ArrayAdapter<String> languagesAdapter = new ArrayAdapter
                        (((View) findViewById(android.R.id.content)).getContext(),
                                android.R.layout.simple_spinner_item, response.body().getList());
                languagesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                additionalLanguagesSpinner.setAdapter(languagesAdapter);
                AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        languageEndonym = (String)parent.getItemAtPosition(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                };
                additionalLanguagesSpinner.setOnItemSelectedListener(itemSelectedListener);
            }
            @Override
            public void onFailure(Call<TextListResponse> call, Throwable t) {
                Toast.makeText(ChooseAdditionalLanguagesActivity.this, "error: 'getLanguagesList' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onConfirmClick(View view) {
        if (languageEndonym != null) {
            onBackClick(languageEndonym);
        }
    }

    public void onBackClick(String languageEndonym) {
        Activity lastActivity = getLastActivity();
        if (lastActivity != null) {
            Intent intent = new Intent(this, lastActivity.getClass());
            intent.putExtra("additional_language", languageEndonym);
            removeActivityFromQueue();
            finish();
            startActivity(intent);
        }
    }

    public void onBackClick(View view) {
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
