package com.example.navigatorappandroid;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.retrofit.AuthApi;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.request.ProfileRequest;
import com.example.navigatorappandroid.retrofit.response.AvatarResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.TextListResponse;
import com.jaredrummler.materialspinner.MaterialSpinner;
import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeSettingsActivity extends AppCompatActivity {

    private Uri selectedImageUri;
    private static final int REQUEST_IMAGE_PICK = 1;
    ImageView avatarImageView;
    RetrofitService retrofitService;
    GeneralApi generalApi;
    EditText nameEditText;
    EditText phoneEditText;
    CheckBox isPhoneHiddenCheckBox;
    CheckBox isEmailHiddenCheckBox;
    EditText socialNetworksLinksEditText;
    Spinner interfaceLanguageSpinner;
    MaterialSpinner communicationLanguagesSpinner;
    EditText workRequirementsEditText;
    CheckBox isDriversLicenseCheckBox;
    CheckBox isAutoCheckBox;
    CheckBox areLanguagesMatchedCheckBox;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_settings_employee, null);
        retrofitService = new RetrofitService();
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        Bundle arguments = getIntent().getExtras();
        avatarImageView = layout.findViewById(R.id.avatar);
        Button uploadButton = layout.findViewById(R.id.avatar_upload);
        Button setAvatarButton = layout.findViewById(R.id.set_avatar);
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
        String name = arguments.get("name").toString();
        nameEditText = layout.findViewById(R.id.first_and_last_name);
        nameEditText.setText(name);
        String phone = arguments.get("phone").toString();
        phoneEditText = layout.findViewById(R.id.phone);
        phoneEditText.setText(phone);
        boolean isPhoneHidden = arguments.getBoolean("is_phone_hidden");
        isPhoneHiddenCheckBox = layout.findViewById(R.id.is_phone_hidden);
        isPhoneHiddenCheckBox.setChecked(isPhoneHidden);
        boolean isEmailHidden = arguments.getBoolean("is_email_hidden");
        isEmailHiddenCheckBox = layout.findViewById(R.id.is_email_hidden);
        isEmailHiddenCheckBox.setChecked(isEmailHidden);
        String socialNetworksLinks = arguments.get("social_networks_links").toString();
        socialNetworksLinksEditText = layout.findViewById(R.id.social_networks_links);
        socialNetworksLinksEditText.setText(socialNetworksLinks);
        String interfaceLanguage = arguments.get("interface_language").toString();
        interfaceLanguageSpinner = layout.findViewById(R.id.interface_language);
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
        communicationLanguagesSpinner = layout.findViewById(R.id.communication_language_first);
        communicationLanguagesSpinner.setAdapter(languagesAdapter);
        ArrayList<Integer> multiSpinnerPositions = new ArrayList<>();
        for (String lang : communicationLanguages) {
            multiSpinnerPositions.add(languagesAdapter.getPosition(lang));
        }
        for (Integer i : multiSpinnerPositions) {
            communicationLanguagesSpinner.setSelectedIndex(i);
        }
        String workRequirements = arguments.get("work_requirements").toString();
        workRequirementsEditText = layout.findViewById(R.id.work_requirements);
        workRequirementsEditText.setText(workRequirements);
        boolean isDriversLicense = arguments.getBoolean("is_drivers_license");
        isDriversLicenseCheckBox = layout.findViewById(R.id.drivers_license);
        isDriversLicenseCheckBox.setChecked(isDriversLicense);
        boolean isAuto = arguments.getBoolean("is_auto");
        isAutoCheckBox = layout.findViewById(R.id.auto);
        isAutoCheckBox.setChecked(isAuto);
        boolean areLanguagesMatched = arguments.getBoolean("are_languages_matched");
        areLanguagesMatchedCheckBox = layout.findViewById(R.id.are_languages_matched);
        areLanguagesMatchedCheckBox.setChecked(areLanguagesMatched);
        int limit = arguments.getInt("limit_in_the_search");
        seekBar = layout.findViewById(R.id.seekbar);
        seekBar.setProgress(limit);
        setContentView(layout);
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
        profileRequest.setEmployeesWorkRequirements(workRequirementsEditText.getText().toString());
        profileRequest.setDriverLicense(isDriversLicenseCheckBox.isChecked());
        profileRequest.setAuto(isAutoCheckBox.isChecked());
        profileRequest.setAreLanguagesMatched(areLanguagesMatchedCheckBox.isChecked());
        profileRequest.setLimit(seekBar.getProgress());
        generalApi.profile(profileRequest).enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                Intent intent;
                Bundle arguments = getIntent().getExtras();
                if (arguments.getString("activity").equals("map")) {
                    intent = new Intent(view.getContext(), WorkMapEmployeeActivity.class);
                    startActivity(intent);
                }
                intent = new Intent(this, WorkListEmployeeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(EmployeeSettingsActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onLogoutClick(View view) {
        AuthApi authApi = retrofitService.getRetrofit().create(AuthApi.class);
        authApi.logout().enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(EmployeeSettingsActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackClick(View view) {
        Intent intent;
        Bundle arguments = getIntent().getExtras();
        if (arguments.getString("activity").equals("map")) {
            intent = new Intent(this, WorkMapEmployeeActivity.class);
            startActivity(intent);
        }
        intent = new Intent(this, WorkListEmployeeActivity.class);
        startActivity(intent);
    }
}
