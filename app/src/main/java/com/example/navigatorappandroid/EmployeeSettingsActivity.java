package com.example.navigatorappandroid;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.response.TextListResponse;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        View coreView = inflater.inflate(R.layout.activity_settings_employee, null);
        RetrofitService retrofitService = new RetrofitService();
        GeneralApi generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        Bundle arguments = getIntent().getExtras();
        String avatar = arguments.get("avatar").toString();
        ImageView imageViewAvatar = coreView.findViewById(R.id.avatar);
        imageViewAvatar.setImageURI(new URI(avatar));
        String name = arguments.get("name").toString();
        EditText nameEditText = coreView.findViewById(R.id.first_and_last_name);
        nameEditText.setText(name);
        String phone = arguments.get("phone").toString();
        EditText phoneEditText = coreView.findViewById(R.id.phone);
        phoneEditText.setText(phone);
        String socialNetworksLinks = arguments.get("social_networks_links").toString();
        EditText socialNetworksLinksEditText = coreView.findViewById(R.id.social_networks_links);
        socialNetworksLinksEditText.setText(socialNetworksLinks);
        String interfaceLanguage = arguments.get("interface_language").toString();
        Spinner interfaceLanguageSpinner = coreView.findViewById(R.id.interface_language);
        ArrayList<String> languagesList = new ArrayList<>();
        generalApi.getLanguagesList().enqueue(new Callback<TextListResponse>() {
            @Override
            public void onResponse(Call<TextListResponse> call, Response<TextListResponse> response) {
                languagesList.addAll(response.body().getList());
            }

            @Override
            public void onFailure(Call<TextListResponse> call, Throwable t) {
                Toast.makeText(EmployeeSettingsActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<String> languagesAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, languagesList);
        languagesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interfaceLanguageSpinner.setAdapter(languagesAdapter);
        int spinnerInterfaceLanguagePosition = languagesAdapter.getPosition(interfaceLanguage);
        interfaceLanguageSpinner.setSelection(spinnerInterfaceLanguagePosition);
        ArrayList<String> communicationLanguages = arguments.getStringArrayList("communication_language");
        MaterialSpinner communicationLanguagesSpinner = coreView.findViewById(R.id.communication_language_first);
        communicationLanguagesSpinner.setAdapter(languagesAdapter);
        ArrayList<Integer> multiSpinnerPositions = new ArrayList<>();
        for (String lang : communicationLanguages) {
            multiSpinnerPositions.add(languagesAdapter.getPosition(lang));
        }
        for (Integer i : multiSpinnerPositions) {
            communicationLanguagesSpinner.setSelectedIndex(i);
        }
        String workRequirements = arguments.get("work_requirements").toString();
        EditText workRequirementsEditText = coreView.findViewById(R.id.work_requirements);
        workRequirementsEditText.setText(workRequirements);
        boolean isDriversLicense = arguments.getBoolean("is_drivers_license");
        CheckBox isDriversLicenseCheckBox = coreView.findViewById(R.id.drivers_license);
        isDriversLicenseCheckBox.setChecked(isDriversLicense);
        boolean isAuto = arguments.getBoolean("is_auto");
        CheckBox isAutoCheckBox = coreView.findViewById(R.id.auto);
        isAutoCheckBox.setChecked(isAuto);
        //ArrayList<String> professions = arguments.getStringArrayList("professions");

        String status = arguments.get("status").toString();
        Spinner statusSpinner = coreView.findViewById(R.id.status);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, );


        setContentView();
    }
}
