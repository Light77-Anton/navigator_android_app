package com.example.navigatorappandroid;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.retrofit.AuthApi;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.request.RegistrationRequest;
import com.example.navigatorappandroid.retrofit.response.CaptchaResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.TextListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private RetrofitService retrofitService;
    private GeneralApi generalApi;
    private AuthApi authApi;
    Spinner languagesSpinner;
    RegistrationRequest registrationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        retrofitService = new RetrofitService();
        authApi = retrofitService.getRetrofit().create(AuthApi.class);
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        registrationRequest = new RegistrationRequest();
        generalApi.getLanguagesList().enqueue(new Callback<TextListResponse>() {
            @Override
            public void onResponse(Call<TextListResponse> call, Response<TextListResponse> response) {
                languagesSpinner = findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>
                        (((View) findViewById(android.R.id.content)).getContext(), android.R.layout.simple_spinner_item,
                                response.body().getList());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                languagesSpinner.setAdapter(adapter);
                AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String language_endonym = (String)parent.getItemAtPosition(position);
                        registrationRequest.setInterfaceLanguage(language_endonym);
                        registrationRequest.setCommunicationLanguage(language_endonym);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                };
                languagesSpinner.setOnItemSelectedListener(itemSelectedListener);
                setCaptcha();
            }
            @Override
            public void onFailure(Call<TextListResponse> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this,
                        "Error: 'getLanguagesList' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void OnConfirm(View view) {
        EditText firstAndLastNameEditText = findViewById(R.id.first_edit_text);
        String name = firstAndLastNameEditText.getText().toString();
        EditText emailEditText = findViewById(R.id.second_edit_text);
        String email = emailEditText.getText().toString();
        EditText phoneEditText = findViewById(R.id.third_edit_text);
        String phone = phoneEditText.getText().toString();
        EditText socialNetworksLinksEditText = findViewById(R.id.fourth_edit_text);
        String socialNetworksLinks = socialNetworksLinksEditText.getText().toString();
        EditText passwordEditText = findViewById(R.id.fifth_edit_text);
        String password = passwordEditText.getText().toString();
        EditText repeatPasswordEditText = findViewById(R.id.sixth_edit_text);
        String repeatPassword = repeatPasswordEditText.getText().toString();
        EditText textFromThePictureEditText = findViewById(R.id.seventh_edit_text);
        String textFromThePicture = textFromThePictureEditText.getText().toString();
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || socialNetworksLinks.isEmpty() ||
                password.isEmpty() || repeatPassword.isEmpty() || textFromThePicture.isEmpty()) {
            Toast.makeText(RegistrationActivity.this,
                    "You have to fill all fields marked with *", Toast.LENGTH_SHORT).show();
        } else {
            registrationRequest.setName(name);
            registrationRequest.setEmail(email);
            registrationRequest.setPhone(phone);
            registrationRequest.setSocialNetworksLinks(socialNetworksLinks);
            registrationRequest.setPassword(password);
            registrationRequest.setRepeatedPassword(repeatPassword);
            String role;
            CheckBox employeeCheckBox = findViewById(R.id.first_checkbox);
            if (employeeCheckBox.isChecked()) {
                role = "Employee";
            } else {
                role = "Employer";
            }
            registrationRequest.setRole(role);
            registrationRequest.setCode(textFromThePicture);
            authApi.registration(registrationRequest).enqueue(new Callback<ResultErrorsResponse>() {
                @Override
                public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                    if (response.body().isResult()) {
                        Intent intent = new Intent(view.getContext(), RegistrationSuccessActivity.class);
                        intent.putExtra("language", registrationRequest.getInterfaceLanguage());
                        startActivity(intent);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        response.body().getErrors().stream().map(e -> sb.append(e).append("\n"));
                        Toast.makeText(RegistrationActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                    Toast.makeText(RegistrationActivity.this, "Error: 'registration'" +
                            " method is failure", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setCaptcha() {
        ImageView imageView = findViewById(R.id.captcha);
        authApi.captcha().enqueue(new Callback<CaptchaResponse>() {
            @Override
            public void onResponse(Call<CaptchaResponse> call, Response<CaptchaResponse> response) {
                byte[] decodedBytes = Base64.decode(response.body().getImage(), Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                imageView.setImageBitmap(decodedBitmap);
            }
            @Override
            public void onFailure(Call<CaptchaResponse> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "Error: 'captcha'" +
                        " method is failure", Toast.LENGTH_SHORT).show();
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
}
