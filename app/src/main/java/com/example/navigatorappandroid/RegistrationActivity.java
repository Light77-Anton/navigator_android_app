package com.example.navigatorappandroid;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.retrofit.AuthApi;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.request.RegistrationRequest;
import com.example.navigatorappandroid.retrofit.request.TextListInSpecifiedLanguageRequest;
import com.example.navigatorappandroid.retrofit.response.CaptchaResponse;
import com.example.navigatorappandroid.retrofit.response.MapTextResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.StringResponse;
import com.example.navigatorappandroid.retrofit.response.TextListResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    private final String ENTER_YOUR_DATA_FOR_REGISTRATION = getResources().getString
            (R.string.enter_your_data_for_registration_java);
    private final String FIRST_AND_LAST_NAME = getResources().getString
            (R.string.first_and_last_name_java);
    private final String EMAIL = getResources().getString
            (R.string.email_java);
    private final String CHOOSE_YOUR_INTERFACE_LANGUAGE = getResources().getString
            (R.string.choose_your_interface_language_java);
    private final String PHONE = getResources().getString
            (R.string.phone_java);
    private final String SOCIAL_NETWORKS_LINKS = getResources().getString
            (R.string.social_networks_links_java);
    private final String PASSWORD = getResources().getString
            (R.string.password_java);
    private final String REPEAT_PASSWORD = getResources().getString
            (R.string.repeat_password_java);
    private final String CHOOSE_YOUR_ROLE = getResources().getString
            (R.string.choose_your_role_java);
    private final String I_AM_LOOKING_FOR_A_JOB = getResources().getString
            (R.string.i_am_looking_for_a_job_java);
    private final String I_AM_LOOKING_FOR_EMPLOYEES = getResources().getString
            (R.string.i_am_looking_for_employees_java);
    private final String ENTER_TEXT_FROM_THE_PICTURE = getResources().getString
            (R.string.enter_text_from_the_picture_java);
    private final String TEXT_FROM_THE_PICTURE = getResources().getString
            (R.string.text_from_the_picture_java);
    private final String NOTE_ABOUT_SETTINGS = getResources().getString
            (R.string.note_about_settings_java);
    private final String CONFIRM = getResources().getString
            (R.string.confirm_java);
    private final String BACK = getResources().getString
            (R.string.back_java);

    public void OnConfirm(View view) {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        EditText firstAndLastNameEditText = findViewById(R.id.first_edit_text);
        registrationRequest.setName(firstAndLastNameEditText.getText().toString());
        EditText emailEditText = findViewById(R.id.second_edit_text);
        registrationRequest.setEmail(emailEditText.getText().toString());
        EditText phoneEditText = findViewById(R.id.third_edit_text);
        registrationRequest.setPhone(phoneEditText.getText().toString());
        EditText socialNetworksLinksEditText = findViewById(R.id.fourth_edit_text);
        registrationRequest.setSocialNetworksLinks(socialNetworksLinksEditText.getText().toString());
        EditText passwordEditText = findViewById(R.id.fifth_edit_text);
        registrationRequest.setPassword(passwordEditText.getText().toString());
        EditText repeatPasswordEditText = findViewById(R.id.sixth_edit_text);
        registrationRequest.setRepeatedPassword(repeatPasswordEditText.getText().toString());
        String role;
        CheckBox employeeCheckBox = findViewById(R.id.first_checkbox);
        if (employeeCheckBox.isChecked()) {
            role = "Employee";
        } else {
            role = "Employer";
        }
        registrationRequest.setRole(role);
        EditText textFromThePictureEditText = findViewById(R.id.seventh_edit_text);
        registrationRequest.setCode(textFromThePictureEditText.getText().toString());
        Spinner languagesSpinner = findViewById(R.id.spinner);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String language_endonym = (String)parent.getItemAtPosition(position);
                registrationRequest.setInterfaceLanguage(language_endonym);
                registrationRequest.setCommunicationLanguage(language_endonym);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        languagesSpinner.setOnItemSelectedListener(itemSelectedListener);

        RetrofitService retrofitService = new RetrofitService();
        AuthApi authApi = retrofitService.getRetrofit().create(AuthApi.class);
        GeneralApi generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        authApi.registration(registrationRequest).enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                if (response.body().isResult()) {
                    generalApi.getUsersInterfaceLanguage().enqueue(new Callback<StringResponse>() {
                        @Override
                        public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                            Intent intent = new Intent(view.getContext(), RegistrationSuccessActivity.class);
                            intent.putExtra("language", response.body().getString());
                            startActivity(intent);
                        }
                        @Override
                        public void onFailure(Call<StringResponse> call, Throwable t) {
                            Toast.makeText(RegistrationActivity.this, "fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    StringBuilder sb = new StringBuilder();
                    response.body().getErrors().stream().map(e -> sb.append(e).append("\n"));
                    Toast.makeText(RegistrationActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCaptcha() {
        ImageView imageView = findViewById(R.id.captcha);
        RetrofitService retrofitService = new RetrofitService();
        AuthApi authApi = retrofitService.getRetrofit().create(AuthApi.class);
        authApi.captcha().enqueue(new Callback<CaptchaResponse>() {
            @Override
            public void onResponse(Call<CaptchaResponse> call, Response<CaptchaResponse> response) {
                imageView.setImageURI(Uri.parse(response.body().getImage()));
            }

            @Override
            public void onFailure(Call<CaptchaResponse> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkRole(View view) {
        CheckBox employeeCheckbox = findViewById(R.id.first_checkbox);
        CheckBox employerCheckbox = findViewById(R.id.second_checkbox);
        if (employeeCheckbox.isChecked()) {
            employerCheckbox.setChecked(false);
        } else {
            employeeCheckbox.setChecked(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
        String language = arguments.get("language").toString();
        LayoutInflater inflater = getLayoutInflater();
        RetrofitService retrofitService = new RetrofitService();
        GeneralApi generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        TextListInSpecifiedLanguageRequest textList = new TextListInSpecifiedLanguageRequest();
        textList.setLanguage(language);
        List<String> codeNameList = new ArrayList<>();
        codeNameList.add(ENTER_YOUR_DATA_FOR_REGISTRATION);
        codeNameList.add(FIRST_AND_LAST_NAME);
        codeNameList.add(EMAIL);
        codeNameList.add(CHOOSE_YOUR_INTERFACE_LANGUAGE);
        codeNameList.add(PHONE);
        codeNameList.add(SOCIAL_NETWORKS_LINKS);
        codeNameList.add(PASSWORD);
        codeNameList.add(REPEAT_PASSWORD);
        codeNameList.add(CHOOSE_YOUR_ROLE);
        codeNameList.add(I_AM_LOOKING_FOR_A_JOB);
        codeNameList.add(I_AM_LOOKING_FOR_EMPLOYEES);
        codeNameList.add(ENTER_TEXT_FROM_THE_PICTURE);
        codeNameList.add(TEXT_FROM_THE_PICTURE);
        codeNameList.add(NOTE_ABOUT_SETTINGS);
        codeNameList.add(CONFIRM);
        codeNameList.add(BACK);
        textList.setCodeNameList(codeNameList);
        View coreView = inflater.inflate(R.layout.activity_registration, null);
        generalApi.getLanguagesList().enqueue(new Callback<TextListResponse>() {
            @Override
            public void onResponse(Call<TextListResponse> call, Response<TextListResponse> response) {
                Spinner languagesSpinner = coreView.findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>
                        (coreView.getContext(), android.R.layout.simple_spinner_item,
                                response.body().getList());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                languagesSpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<TextListResponse> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        generalApi.checkAndGetTextListInSpecifiedLanguage(textList).enqueue(new Callback<MapTextResponse>() {
            @Override
            public void onResponse(Call<MapTextResponse> call, Response<MapTextResponse> response) {
                TextView firstTextView = coreView.findViewById(R.id.first_text_view);
                firstTextView.setText(response.body().getMap().get(ENTER_YOUR_DATA_FOR_REGISTRATION));
                EditText firstEditText = coreView.findViewById(R.id.first_edit_text);
                firstEditText.setHint(response.body().getMap().get(FIRST_AND_LAST_NAME));
                EditText secondEditText = coreView.findViewById(R.id.second_edit_text);
                secondEditText.setHint(response.body().getMap().get(EMAIL));
                TextView secondTextView = coreView.findViewById(R.id.second_text_view);
                secondTextView.setText(response.body().getMap().get(CHOOSE_YOUR_INTERFACE_LANGUAGE));
                EditText thirdEditText = coreView.findViewById(R.id.third_edit_text);
                thirdEditText.setHint(response.body().getMap().get(PHONE));
                EditText fourthEditText = coreView.findViewById(R.id.fourth_edit_text);
                fourthEditText.setHint(response.body().getMap().get(SOCIAL_NETWORKS_LINKS));
                EditText fifthEditText = coreView.findViewById(R.id.fifth_edit_text);
                fifthEditText.setHint(response.body().getMap().get(PASSWORD));
                EditText sixthEditText = coreView.findViewById(R.id.sixth_edit_text);
                sixthEditText.setHint(response.body().getMap().get(REPEAT_PASSWORD));
                TextView thirdTextView = coreView.findViewById(R.id.third_text_view);
                thirdTextView.setText(response.body().getMap().get(CHOOSE_YOUR_ROLE));
                CheckBox firstCheckBox = coreView.findViewById(R.id.first_checkbox);
                firstCheckBox.setText(response.body().getMap().get(I_AM_LOOKING_FOR_A_JOB));
                CheckBox secondCheckBox = coreView.findViewById(R.id.second_checkbox);
                secondCheckBox.setText(response.body().getMap().get(I_AM_LOOKING_FOR_EMPLOYEES));
                TextView fourthTextView = coreView.findViewById(R.id.fourth_text_view);
                fourthTextView.setText(response.body().getMap().get(ENTER_TEXT_FROM_THE_PICTURE));
                EditText seventhEditText = coreView.findViewById(R.id.seventh_edit_text);
                seventhEditText.setHint(response.body().getMap().get(TEXT_FROM_THE_PICTURE));
                TextView fifthTextView = coreView.findViewById(R.id.fifth_text_view);
                fifthTextView.setText(response.body().getMap().get(NOTE_ABOUT_SETTINGS));
                Button firstButton = coreView.findViewById(R.id.first_button);
                firstButton.setText(response.body().getMap().get(CONFIRM));
                Button secondButton = coreView.findViewById(R.id.second_button);
                secondButton.setText(response.body().getMap().get(BACK));
                setCaptcha();

                setContentView(coreView);
            }

            @Override
            public void onFailure(Call<MapTextResponse> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
