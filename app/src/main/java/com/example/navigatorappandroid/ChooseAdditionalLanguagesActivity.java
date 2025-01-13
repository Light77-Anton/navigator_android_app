package com.example.navigatorappandroid;
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
        Intent intent;
        if (userInfoResponse.getCurrentWorkDisplay() == 1) {
            intent = new Intent(this, WorkMapEmployeeActivity.class);
        } else {
            intent = new Intent(this, WorkListEmployeeActivity.class);
        }
        intent.putExtra("languages_array", languagesArray);
        finish();
        startActivity(intent);
    }

    public void onBackClick(View view) {
        Intent intent;
        if (userInfoResponse.getCurrentWorkDisplay() == 1) {
            intent = new Intent(this, WorkMapEmployeeActivity.class);
            finish();
            startActivity(intent);
        }
        intent = new Intent(this, WorkListEmployeeActivity.class);
        finish();
        startActivity(intent);
    }
}
