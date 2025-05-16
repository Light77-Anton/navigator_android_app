package com.example.navigatorappandroid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.example.navigatorappandroid.retrofit.response.TextListResponse;
import com.jaredrummler.materialspinner.MaterialSpinner;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseAdditionalLanguagesActivity extends BaseActivity {

    private MaterialSpinner additionalLanguagesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_additional_languages);
        setCurrentActivity(this);
        ArrayList<String> languagesList = new ArrayList<>();
        generalApi.getLanguagesList().enqueue(new Callback<TextListResponse>() {
            @Override
            public void onResponse(Call<TextListResponse> call, Response<TextListResponse> response) {
                languagesList.addAll(response.body().getList());
            }
            @Override
            public void onFailure(Call<TextListResponse> call, Throwable t) {
                Toast.makeText(ChooseAdditionalLanguagesActivity.this, "error: 'getLanguagesList' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<String> languagesAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, languagesList);
        languagesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        additionalLanguagesSpinner = findViewById(R.id.additional_languages_spinner);
        additionalLanguagesSpinner.setAdapter(languagesAdapter);
        additionalLanguagesSpinner.setBackground(ContextCompat.getDrawable(this, R.drawable.spinner));
    }

    public void onConfirmClick(View view) {
        List<String> list = additionalLanguagesSpinner.getItems().stream().map(Object::toString).collect(Collectors.toList());
        int size = additionalLanguagesSpinner.getItems().size();
        String[] languagesArray = new String[size];
        for (int i = 0; i < size; i++) {
            languagesArray[i] = list.get(i);
        }
        onBackClick(languagesArray);
    }

    public void onBackClick(String[] languagesArray) {
        Activity lastActivity = getLastActivity();
        if (lastActivity != null) {
            Intent intent = new Intent(this, lastActivity.getClass());
            intent.putExtra("languages_array", languagesArray);
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
