package com.example.navigatorappandroid;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.example.navigatorappandroid.model.Language;
import com.example.navigatorappandroid.retrofit.request.ProfileRequest;
import com.example.navigatorappandroid.retrofit.response.AvatarResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.TextListResponse;
import com.jaredrummler.materialspinner.MaterialSpinner;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeSettingsActivity extends BaseActivity {

    private Uri selectedImageUri;
    private static final int REQUEST_IMAGE_PICK = 1;
    private ImageView avatarImageView;
    private EditText nameEditText;
    private EditText phoneEditText;
    private CheckBox isPhoneHiddenCheckBox;
    private CheckBox isEmailHiddenCheckBox;
    private EditText socialNetworksLinksEditText;
    private Spinner interfaceLanguageSpinner;
    private MaterialSpinner communicationLanguagesSpinner;
    private CheckBox isDriversLicenseCheckBox;
    private CheckBox isAutoCheckBox;
    private CheckBox areLanguagesMatchedCheckBox;
    private CheckBox isMultivacancyAllowedCheckBox;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_employee);
        avatarImageView = findViewById(R.id.avatar);
        Button uploadButton = findViewById(R.id.avatar_upload);
        Button setAvatarButton = findViewById(R.id.set_avatar);
        uploadButton.setOnClickListener(view -> openGallery());
        setAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri != null) {
                    uploadAvatar(selectedImageUri);
                } else {
                    Toast.makeText(EmployeeSettingsActivity.this, "Select an image first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        String name = userInfoResponse.getName();
        nameEditText = findViewById(R.id.first_and_last_name);
        nameEditText.setText(name);
        String phone = userInfoResponse.getPhone();
        phoneEditText = findViewById(R.id.phone);
        phoneEditText.setText(phone);
        boolean isPhoneHidden = userInfoResponse.isPhoneHidden();
        isPhoneHiddenCheckBox = findViewById(R.id.is_phone_hidden);
        isPhoneHiddenCheckBox.setChecked(isPhoneHidden);
        boolean isEmailHidden = userInfoResponse.isEmailHidden();
        isEmailHiddenCheckBox = findViewById(R.id.is_email_hidden);
        isEmailHiddenCheckBox.setChecked(isEmailHidden);
        String socialNetworksLinks = userInfoResponse.getSocialNetworksLinks();
        socialNetworksLinksEditText = findViewById(R.id.social_networks_links);
        socialNetworksLinksEditText.setText(socialNetworksLinks);
        String interfaceLanguage = userInfoResponse.getEndonymInterfaceLanguage();
        interfaceLanguageSpinner = findViewById(R.id.interface_language);
        ArrayList<String> languagesList = new ArrayList<>();
        generalApi.getLanguagesList().enqueue(new Callback<TextListResponse>() {
            @Override
            public void onResponse(Call<TextListResponse> call, Response<TextListResponse> response) {
                languagesList.addAll(response.body().getList());
            }

            @Override
            public void onFailure(Call<TextListResponse> call, Throwable t) {
                Toast.makeText(EmployeeSettingsActivity.this, "error: 'getLanguagesList' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<String> languagesAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, languagesList);
        languagesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interfaceLanguageSpinner.setAdapter(languagesAdapter);
        int spinnerInterfaceLanguagePosition = languagesAdapter.getPosition(interfaceLanguage);
        interfaceLanguageSpinner.setSelection(spinnerInterfaceLanguagePosition);
        List<String> communicationLanguages = userInfoResponse.getCommunicationLanguages().stream()
                .map(Language::getLanguageEndonym).collect(Collectors.toList());
        communicationLanguagesSpinner = findViewById(R.id.communication_language_first);
        communicationLanguagesSpinner.setBackground(ContextCompat.getDrawable(this, R.drawable.spinner));
        communicationLanguagesSpinner.setAdapter(languagesAdapter);
        ArrayList<Integer> multiSpinnerPositions = new ArrayList<>();
        for (String lang : communicationLanguages) {
            multiSpinnerPositions.add(languagesAdapter.getPosition(lang));
        }
        for (Integer i : multiSpinnerPositions) {
            communicationLanguagesSpinner.setSelectedIndex(i);
        }
        boolean isDriversLicense = userInfoResponse.getEmployeeData().isDriverLicense();
        isDriversLicenseCheckBox = findViewById(R.id.drivers_license);
        isDriversLicenseCheckBox.setChecked(isDriversLicense);
        boolean isAuto = userInfoResponse.getEmployeeData().isAuto();
        isAutoCheckBox = findViewById(R.id.auto);
        isAutoCheckBox.setChecked(isAuto);
        boolean areLanguagesMatched = userInfoResponse.isAreLanguagesMatched();
        areLanguagesMatchedCheckBox = findViewById(R.id.are_languages_matched);
        areLanguagesMatchedCheckBox.setChecked(areLanguagesMatched);
        int limit = userInfoResponse.getLimitForTheSearch();
        boolean isMultivacancyAllowed = userInfoResponse.isMultivacancyAllowed();
        isMultivacancyAllowedCheckBox = findViewById(R.id.is_multivacancy_allowed);
        isMultivacancyAllowedCheckBox.setChecked(isMultivacancyAllowed);
        seekBar = findViewById(R.id.seekbar);
        seekBar.setProgress(limit);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                avatarImageView.setImageURI(selectedImageUri);
            }
        }
    }

    private void uploadAvatar(Uri imageUri) {
        String filePath = getRealPathFromURI(imageUri);
        if (filePath != null) {
            File file = new File(filePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
            generalApi.profileAvatar(body).enqueue(new Callback<AvatarResponse>() {
                @Override
                public void onResponse(Call<AvatarResponse> call, Response<AvatarResponse> response) {}

                @Override
                public void onFailure(Call<AvatarResponse> call, Throwable t) {
                    Toast.makeText(EmployeeSettingsActivity.this, "Failed to set avatar", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null) return null;
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    public void onProfessionListClick(View view) {
        Intent intent = new Intent(this, EmployeeProfessionListActivity.class);
        finish();
        startActivity(intent);
    }

    public void onChangePasswordClick(View view) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        finish();
        startActivity(intent);
    }

    public void onConfirmClick(View view) {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setName(nameEditText.getText().toString());
        profileRequest.setPhone(phoneEditText.getText().toString());
        profileRequest.setPhoneHidden(isPhoneHiddenCheckBox.isChecked());
        profileRequest.setEmailHidden(isEmailHiddenCheckBox.isChecked());
        profileRequest.setSocialNetworksLinks(socialNetworksLinksEditText.getText().toString());
        profileRequest.setInterfaceLanguage(interfaceLanguageSpinner.getSelectedItem().toString());
        profileRequest.setCommunicationLanguages(communicationLanguagesSpinner.getItems().stream().map(Object::toString).collect(Collectors.toList()));
        profileRequest.setDriverLicense(isDriversLicenseCheckBox.isChecked());
        profileRequest.setAuto(isAutoCheckBox.isChecked());
        profileRequest.setAreLanguagesMatched(areLanguagesMatchedCheckBox.isChecked());
        profileRequest.setMultivacancyAllowed(isMultivacancyAllowedCheckBox.isChecked());
        profileRequest.setLimit(seekBar.getProgress());
        generalApi.profile(profileRequest).enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                Intent intent;
                if (userInfoResponse.getCurrentWorkDisplay() == 1) {
                    intent = new Intent(view.getContext(), WorkMapEmployeeActivity.class);
                    finish();
                    startActivity(intent);
                }
                intent = new Intent(view.getContext(), WorkListEmployeeActivity.class);
                finish();
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(EmployeeSettingsActivity.this, "error: 'profile' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onLogoutClick(View view) {
        authApi.logout().enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                finish();
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(EmployeeSettingsActivity.this, "error: 'logout' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
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
