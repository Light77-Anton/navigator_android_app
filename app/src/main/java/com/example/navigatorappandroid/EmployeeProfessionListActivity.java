package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.navigatorappandroid.model.InfoFromEmployee;
import com.example.navigatorappandroid.model.Language;
import com.example.navigatorappandroid.model.ProfessionName;
import com.example.navigatorappandroid.retrofit.request.EmployeeInfoForEmployersRequest;
import com.example.navigatorappandroid.retrofit.request.ProfessionToUserRequest;
import com.example.navigatorappandroid.retrofit.response.ProfessionNamesListResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.AdapterView;
import androidx.core.content.ContextCompat;

public class EmployeeProfessionListActivity extends BaseActivity {
    private List<ProfessionName> professionNamesList;
    private List<String> professionNamesSpinner;
    private LinearLayout layout;
    private String[] languages;
    private List<Language> languagesList;
    private List<EditText> editTextList;
    private HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professions_list);
        layout = findViewById(R.id.layout);
        professionNamesList = new ArrayList<>();
        professionNamesSpinner = new ArrayList<>();
        generalApi.getProfessionsNamesInSpecifiedLanguage().enqueue(new Callback<ProfessionNamesListResponse>() {
            @Override
            public void onResponse(Call<ProfessionNamesListResponse> call, Response<ProfessionNamesListResponse> response) {
                professionNamesList.addAll(response.body().getList());
                professionNamesSpinner.addAll(response.body().getList().stream()
                        .map(ProfessionName::getProfessionName).collect(Collectors.toList()));
                professionNamesSpinner.add(getResources().getString(R.string.empty));
            }
            @Override
            public void onFailure(Call<ProfessionNamesListResponse> call, Throwable t) {
                Toast.makeText(EmployeeProfessionListActivity.this, "Error " +
                        "'getProfessionsNamesInSpecifiedLanguage' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<String> professionsAdapter = new ArrayAdapter
                (this, android.R.layout.simple_spinner_item, professionNamesSpinner);
        professionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int spinnerEmptyPosition = professionsAdapter
                .getPosition(getResources().getString(R.string.empty));
        hashMap = new HashMap<>();
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hashMap.put(parent.getContentDescription().toString(),
                        (String)parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        Spinner professionSpinner;
        for (int i = 0 ; i < 5; i++) {
            switch (i) {
                case 0:
                    professionSpinner = findViewById(R.id.first_profession);
                    break;
                case 1:
                    professionSpinner = findViewById(R.id.second_profession);
                    break;
                case 2:
                    professionSpinner = findViewById(R.id.third_profession);
                    break;
                case 3:
                    professionSpinner = findViewById(R.id.fought_profession);
                    break;
                default:
                    professionSpinner = findViewById(R.id.fifth_profession);
                    break;
            }
            professionSpinner.setAdapter(professionsAdapter);
            professionSpinner.setSelection(spinnerEmptyPosition);
            professionSpinner.setOnItemSelectedListener(itemSelectedListener);
        }
        for (int i = 0 ; i < userInfoResponse.getEmployeeData().getProfessions().size(); i++) {
            switch (i) {
                case 0:
                    professionSpinner = findViewById(R.id.first_profession);
                    break;
                case 1:
                    professionSpinner = findViewById(R.id.second_profession);
                    break;
                case 2:
                    professionSpinner = findViewById(R.id.third_profession);
                    break;
                case 3:
                    professionSpinner = findViewById(R.id.fought_profession);
                    break;
                default:
                    professionSpinner = findViewById(R.id.fifth_profession);
                    break;
            }
            for (ProfessionName professionName : userInfoResponse.getEmployeeData().getProfessions().get(i).getProfessionNames()) {
                if (userInfoResponse.getEndonymInterfaceLanguage()
                        .equals(professionName.getLanguage().getLanguageEndonym())) {
                    int spinnerProfessionPosition = professionsAdapter
                            .getPosition(professionName.getProfessionName());
                    professionSpinner.setSelection(spinnerProfessionPosition);
                }
            }
        }
        languagesList = new ArrayList<>();
        editTextList = new ArrayList<>();
        languages = getResources().getStringArray(R.array.languages);
        for (Language lang : userInfoResponse.getCommunicationLanguages()) {
            EditText editText = new EditText(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 30, 0, 0);
            editText.setPadding(15, 15, 15, 15);
            editText.setLayoutParams(params);
            editText.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_text));
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            editText.setTextColor(getResources().getColor(R.color.white));
            editText.setHintTextColor(getResources().getColor(R.color.white));
            for (String language : languages) {
                if (language.equals(lang.getLanguageEndonym())) {
                    switch (language) {
                        case "English":
                            editText.setHint(getResources().getString(R.string.info_from_employee_for_employers_hint_english));
                            break;
                        case "Русский":
                            editText.setHint(getResources().getString(R.string.info_from_employee_for_employers_hint_russian));
                            break;
                        default:
                            break;
                    }
                    editText.setContentDescription(language);
                    languagesList.add(lang);
                }
            }
            for (InfoFromEmployee infoFromEmployee : userInfoResponse.getEmployeeData().getInfoFromEmployee()) {
                if (infoFromEmployee.getLanguage().getLanguageEndonym().equals(lang.getLanguageEndonym())) {
                    editText.setText(infoFromEmployee.getText());
                }
            }
            layout.addView(editText);
            editTextList.add(editText);
        }
    }

    private void setProfessionsToEmployee() {
        for (String currentProfessionName : hashMap.values()) {
            for (ProfessionName professionName : professionNamesList) {
                if (professionName.getProfessionName().equals(currentProfessionName)) {
                    ProfessionToUserRequest professionToUserRequest = new ProfessionToUserRequest();
                    professionToUserRequest.setId(professionName.getProfession().getId());
                    generalApi.postProfessionToUser(professionToUserRequest).enqueue(new Callback<ResultErrorsResponse>() { // по идее должно сначало удалить старые связи
                        @Override
                        public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {}

                        @Override
                        public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                            Toast.makeText(EmployeeProfessionListActivity.this, "Error " +
                                    "'postProfessionToUser' method is failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }

    private void setInfoFromEmployee(View view) {
        HashMap<Language, String> map = new HashMap<>();
        for (EditText editText : editTextList) {
            for (Language lang : languagesList) {
                if (lang.getLanguageEndonym().equals(editText.getContentDescription().toString())
                        && !editText.getText().toString().isEmpty()) {
                    map.put(lang, editText.getText().toString());
                }
            }
        }
        EmployeeInfoForEmployersRequest employeeInfoForEmployersRequest = new EmployeeInfoForEmployersRequest();
        employeeInfoForEmployersRequest.setMap(map);
        generalApi.changeInfoFromEmployeeForEmployers(employeeInfoForEmployersRequest).enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                Intent intent = new Intent(view.getContext(), EmployeeSettingsActivity.class);
                finish();
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(EmployeeProfessionListActivity.this, "Error " +
                        "'changeInfoFromEmployeeForEmployers' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onConfirmClick(View view) {
        setProfessionsToEmployee();
        setInfoFromEmployee(view);
    }

    public void onBackClick(View view) {
        Intent intent = new Intent(this, EmployeeSettingsActivity.class);
        finish();
        startActivity(intent);
    }
}
