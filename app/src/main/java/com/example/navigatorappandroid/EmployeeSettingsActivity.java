package com.example.navigatorappandroid;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.response.TextListResponse;
import com.jaredrummler.materialspinner.MaterialSpinner;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_settings_employee, null);
        RetrofitService retrofitService = new RetrofitService();
        GeneralApi generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        Bundle arguments = getIntent().getExtras();
        if (arguments.get("avatar") != null && !arguments.get("avatar").equals("")) {
            String avatar = arguments.get("avatar").toString();
            ImageView imageViewAvatar = layout.findViewById(R.id.avatar);
            imageViewAvatar.setImageURI(Uri.parse(avatar));
        }
        String name = arguments.get("name").toString();
        EditText nameEditText = layout.findViewById(R.id.first_and_last_name);
        nameEditText.setText(name);
        String phone = arguments.get("phone").toString();
        EditText phoneEditText = layout.findViewById(R.id.phone);
        phoneEditText.setText(phone);
        boolean isPhoneHidden = arguments.getBoolean("is_phone_hidden");
        CheckBox isPhoneHiddenCheckBox = layout.findViewById(R.id.is_phone_hidden);
        isPhoneHiddenCheckBox.setChecked(isPhoneHidden);
        boolean isEmailHidden = arguments.getBoolean("is_email_hidden");
        CheckBox isEmailHiddenCheckBox = layout.findViewById(R.id.is_email_hidden);
        isEmailHiddenCheckBox.setChecked(isEmailHidden);
        String socialNetworksLinks = arguments.get("social_networks_links").toString();
        EditText socialNetworksLinksEditText = layout.findViewById(R.id.social_networks_links);
        socialNetworksLinksEditText.setText(socialNetworksLinks);
        String interfaceLanguage = arguments.get("interface_language").toString();
        Spinner interfaceLanguageSpinner = layout.findViewById(R.id.interface_language);
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
        MaterialSpinner communicationLanguagesSpinner = layout.findViewById(R.id.communication_language_first);
        communicationLanguagesSpinner.setAdapter(languagesAdapter);
        ArrayList<Integer> multiSpinnerPositions = new ArrayList<>();
        for (String lang : communicationLanguages) {
            multiSpinnerPositions.add(languagesAdapter.getPosition(lang));
        }
        for (Integer i : multiSpinnerPositions) {
            communicationLanguagesSpinner.setSelectedIndex(i);
        }
        String workRequirements = arguments.get("work_requirements").toString();
        EditText workRequirementsEditText = layout.findViewById(R.id.work_requirements);
        workRequirementsEditText.setText(workRequirements);
        boolean isDriversLicense = arguments.getBoolean("is_drivers_license");
        CheckBox isDriversLicenseCheckBox = layout.findViewById(R.id.drivers_license);
        isDriversLicenseCheckBox.setChecked(isDriversLicense);
        boolean isAuto = arguments.getBoolean("is_auto");
        CheckBox isAutoCheckBox = layout.findViewById(R.id.auto);
        isAutoCheckBox.setChecked(isAuto);
        boolean areLanguagesMatched = arguments.getBoolean("are_languages_matched");
        CheckBox areLanguagesMatchedCheckBox = layout.findViewById(R.id.are_languages_matched);
        areLanguagesMatchedCheckBox.setChecked(areLanguagesMatched);
        int limit = arguments.getInt("limit_in_the_search");
        SeekBar seekBar = layout.findViewById(R.id.seekbar);
        seekBar.setProgress(limit);
        setContentView(layout);
    }

    public void onProfessionListClick() {
        Intent intent = new Intent(this, EmployeeProfessionListActivity.class);
        startActivity(intent);
    }


}
