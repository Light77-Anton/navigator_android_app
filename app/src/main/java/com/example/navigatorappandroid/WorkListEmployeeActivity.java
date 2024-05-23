package com.example.navigatorappandroid;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.navigatorappandroid.handler.EmployeeStatusHandler;
import com.example.navigatorappandroid.handler.LanguageHandler;
import com.example.navigatorappandroid.handler.LocationUpdateHandler;
import com.example.navigatorappandroid.model.InfoAboutVacancyFromEmployer;
import com.example.navigatorappandroid.model.Language;
import com.example.navigatorappandroid.model.User;
import com.example.navigatorappandroid.model.Vacancy;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.request.LocationsRequest;
import com.example.navigatorappandroid.retrofit.request.ProfessionToUserRequest;
import com.example.navigatorappandroid.retrofit.request.SearchRequest;
import com.example.navigatorappandroid.retrofit.response.DistanceResponse;
import com.example.navigatorappandroid.retrofit.response.SearchResponse;
import com.example.navigatorappandroid.retrofit.response.StringResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkListEmployeeActivity extends AppCompatActivity {

    private LanguageHandler languageHandler;
    private LocationUpdateHandler locationUpdateHandler;

    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private EmployeeStatusHandler employeeStatusHandler;
    private View coreView;
    private LinearLayout linearLayout;
    private LinearLayout searchResultsLayout;
    private RetrofitService retrofitService;
    private GeneralApi generalApi;
    private SearchApi searchApi;
    private UserInfoResponse userInfoResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coreView = getLayoutInflater().inflate(R.layout.activity_work_list_employee, null);
        linearLayout = coreView.findViewById(R.id.work_list_employee_sort_request_layout);
        searchResultsLayout = coreView.findViewById(R.id.work_list_employee_search_results_layout);
        retrofitService = new RetrofitService();
        searchApi = retrofitService.getRetrofit().create(SearchApi.class);
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        changeSortRequestFieldCondition();
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                userInfoResponse = response.body();
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(WorkListEmployeeActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        Bundle arguments = getIntent().getExtras();
        if (arguments.get("profession") != null) {
            executeSearchForVacancies(arguments.getString("profession"));
        }
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = new Locale(languageHandler.getLanguageCode(userInfoResponse.getEndonymInterfaceLanguage()));
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        getLocationPermission();
        getDeviceLocation();
        locationUpdateHandler = new LocationUpdateHandler(lastKnownLocation.getLatitude(),
                lastKnownLocation.getLongitude(), userInfoResponse.getId());
        setContentView(R.layout.activity_work_list_employee);
    }

    @Override
    protected void onResume() {
        super.onResume();
        employeeStatusHandler.startStatusChecking();
        locationUpdateHandler.startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        employeeStatusHandler.stopStatusChecking();
        locationUpdateHandler.stopLocationUpdates();
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            lastKnownLocation = task.getResult();
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            e.printStackTrace();
        }
    }

    private void enableLinearLayout() {
        linearLayout.setEnabled(true);
        linearLayout.setVisibility(View.VISIBLE);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            child.setEnabled(true);
            child.setVisibility(View.VISIBLE);
        }
    }

    private void disableLinearLayout() {
        linearLayout.setEnabled(false);
        linearLayout.setVisibility(View.INVISIBLE);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            child.setEnabled(false);
            child.setVisibility(View.INVISIBLE);
        }
    }

    private void changeSortRequestFieldCondition() {
        if (linearLayout.isEnabled()) {
            disableLinearLayout();
        } else {
            enableLinearLayout();
        }
    }

    public void onSettingsClick(View view) {
        Intent intent = new Intent(view.getContext(), EmployeeSettingsActivity.class);
        intent.putExtra("avatar", userInfoResponse.getAvatar());
        intent.putExtra("name", userInfoResponse.getName());
        intent.putExtra("phone", userInfoResponse.getPhone());
        intent.putExtra("is_phone_hidden", userInfoResponse.isPhoneHidden());
        intent.putExtra("is_email_hidden", userInfoResponse.isEmailHidden());
        intent.putExtra("social_networks_links", userInfoResponse.getSocialNetworksLinks());
        intent.putExtra("interface_language", userInfoResponse.getEndonymInterfaceLanguage());
        intent.putExtra("communication_languages", userInfoResponse.getCommunicationLanguages());
        intent.putExtra("are_languages_matched", userInfoResponse.isAreLanguagesMatched());
        intent.putExtra("limit_in_the_search", userInfoResponse.getLimitForTheSearch());
        intent.putExtra("is_multivacancy_allowed", userInfoResponse.isMultivacancyAllowed());
        intent.putExtra("activity", "list");
        intent.putExtra("work_requirements", userInfoResponse.getEmployeeData().getEmployeesWorkRequirements());
        intent.putExtra("is_drivers_license", userInfoResponse.getEmployeeData().isDriverLicense());
        intent.putExtra("is_auto", userInfoResponse.getEmployeeData().isAuto());
        startActivity(intent);
    }

    public void onSearchClick(View view) {
        Intent intent = new Intent(this, SearchVacanciesActivity.class);
        intent.putExtra("activity", "list");
        startActivity(intent);
    }

    public void onStatusClick(View view) {
        Intent intent = new Intent(this, EmployeeStatusActivity.class);
        intent.putExtra("activity", "list");
        startActivity(intent);
    }

    private void executeSearchForVacancies(String profession) {
        searchResultsLayout.removeAllViews();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setProfessionName(profession);
        RadioGroup radioGroup = linearLayout.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener((radiogroup, id)-> {
            RadioButton radio = findViewById(id);
            switch (radio.getContentDescription().toString()) {
                case "name":
                    searchRequest.setSortType("name");
                    break;
                case "rating":
                    searchRequest.setSortType("rating");
                    break;
                case "location":
                    searchRequest.setSortType("location");
                    break;
                default:
                    searchRequest.setSortType("");
                    break;
            }
        });
        SeekBar seekBar = linearLayout.findViewById(R.id.seekbar);
        searchRequest.setInRadiusOf(seekBar.getProgress());
        CheckBox checkBox = linearLayout.findViewById(R.id.is_auto_checkbox);
        searchRequest.setAuto(checkBox.isChecked());
        searchRequest.setMultivacancyAllowed(userInfoResponse.getEmployeeData().isMultivacancyAllowed());
        searchRequest.setLimit(userInfoResponse.getLimitForTheSearch());
        searchRequest.setAreLanguagesMatch(userInfoResponse.isAreLanguagesMatched());
        searchApi.getVacanciesByProfession(searchRequest).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                List<Vacancy> vacancies = response.body().getVacancyList();
                for (Vacancy vacancy : vacancies) {
                    User employer = vacancy.getEmployerRequests().getEmployer();
                    LocationsRequest locationsRequest = new LocationsRequest();
                    locationsRequest.setLat1(userInfoResponse.getLocation().getLatitude());
                    locationsRequest.setLong1(userInfoResponse.getLocation().getLongitude());
                    locationsRequest.setLat2(vacancy.getJobLocation().getLatitude());
                    locationsRequest.setLong2(vacancy.getJobLocation().getLongitude());
                    searchApi.getMeasuredDistance(locationsRequest).enqueue(new Callback<DistanceResponse>() {
                        @Override
                        public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {
                            addVacancyButton(employer, vacancy, response.body().getDistance());
                        }

                        @Override
                        public void onFailure(Call<DistanceResponse> call, Throwable t) {
                            Toast.makeText(WorkListEmployeeActivity.this, "Error " +
                                    "'getMeasuredDistance' method is failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(WorkListEmployeeActivity.this, "Error " +
                        "'getVacanciesByProfession' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addVacancyButton(User employer, Vacancy vacancy, double distance) {
        long id = employer.getId();
        String name = employer.getName();
        String firmName = employer.getEmployerRequests().getFirmName();
        String avatar = employer.getAvatar();
        double rating = employer.getRanking();
        Button button = new Button(searchResultsLayout.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(layoutParams);
        button.setBackground(getResources().getDrawable(R.drawable.rectangle_button));
        button.setTextColor(getResources().getColor(R.color.black));
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EmployerExtendedInfoActivity.class);
                intent.putExtra("activity", "list");
                ProfessionToUserRequest professionToUserRequest = new ProfessionToUserRequest();
                professionToUserRequest.setId(vacancy.getProfession().getId());
                generalApi.getProfessionNameInSpecifiedLanguage(professionToUserRequest).enqueue(new Callback<StringResponse>() {
                    @Override
                    public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                        intent.putExtra("vacancy_profession", response.body().getString());
                    }

                    @Override
                    public void onFailure(Call<StringResponse> call, Throwable t) {
                        Toast.makeText(WorkListEmployeeActivity.this, "Error " +
                                "'getProfessionNameInSpecifiedLanguage' method is failure", Toast.LENGTH_SHORT).show();
                    }
                });
                intent.putExtra("vacancy_job_address", vacancy.getJobLocation().getJobAddress());
                intent.putExtra("vacancy_start_date_time", vacancy.getStartDateTime().toString());
                for (String language : userInfoResponse.getCommunicationLanguages()) {
                    for (InfoAboutVacancyFromEmployer info : vacancy.getPaymentAndAdditionalInfo()) {
                        if (info.getLanguage().getLanguageEndonym().equals(language)) {
                            intent.putExtra("vacancy_info", info.getText());
                        }
                    }
                }
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("rating", rating);
                intent.putExtra("avatar", avatar);
                intent.putExtra("firm_name", firmName);
                StringBuilder languagesList = new StringBuilder();
                for (Language employeeLang : employer.getCommunicationLanguages()) {
                    languagesList.append(employeeLang.getLanguageEndonym() + ",");
                }
                languagesList.deleteCharAt(languagesList.length() - 1);
                intent.putExtra("languages", languagesList.toString());
                intent.putExtra("email", employer.getEmail());
                intent.putExtra("phone", employer.getPhone());
                intent.putExtra("social_networks_links", employer.getSocialNetworksLinks());
                startActivity(intent);
            }
        });
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("   ");
        sb.append(rating);
        sb.append("   ");
        sb.append(distance);
        button.setText(sb);
        searchResultsLayout.addView(button);
    }
}
