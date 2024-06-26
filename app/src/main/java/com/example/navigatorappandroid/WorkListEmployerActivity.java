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
import com.example.navigatorappandroid.handler.LanguageHandler;
import com.example.navigatorappandroid.handler.LocationUpdateHandler;
import com.example.navigatorappandroid.model.User;
import com.example.navigatorappandroid.retrofit.AuthApi;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.request.LocationsRequest;
import com.example.navigatorappandroid.retrofit.request.ProfessionToUserRequest;
import com.example.navigatorappandroid.retrofit.request.SearchRequest;
import com.example.navigatorappandroid.retrofit.response.DistanceResponse;
import com.example.navigatorappandroid.retrofit.response.LoginResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.SearchResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkListEmployerActivity extends AppCompatActivity {
    private LanguageHandler languageHandler;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private LocationUpdateHandler locationUpdateHandler;
    private Location lastKnownLocation;
    private LinearLayout linearLayout;
    private LinearLayout searchResultsLayout;
    private RetrofitService retrofitService;
    private GeneralApi generalApi;
    private SearchApi searchApi;
    private AuthApi authApi;
    private UserInfoResponse userInfoResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, LoginActivity.class);
        retrofitService = new RetrofitService();
        searchApi = retrofitService.getRetrofit().create(SearchApi.class);
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        authApi = retrofitService.getRetrofit().create(AuthApi.class);
        authApi.authCheck().enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (!response.body().isResult()) {
                    intent.putExtra("is_auth_error", true);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                intent.putExtra("is_auth_error", true);
                startActivity(intent);
            }
        });
        setContentView(R.layout.activity_work_list_employer);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        languageHandler = new LanguageHandler();
        linearLayout = findViewById(R.id.work_list_employer_sort_request_layout);
        searchResultsLayout = findViewById(R.id.work_list_employer_search_results_layout);
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
                Toast.makeText(WorkListEmployerActivity.this, "error: 'getUserInfo' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        Bundle arguments = getIntent().getExtras();
        if (arguments != null && arguments.get("profession") != null) {
            executeSearchForEmployees(arguments.getString("profession"));
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationUpdateHandler.startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    private void enableLinearLayout() {
        linearLayout.setEnabled(true);
        linearLayout.setVisibility(View.VISIBLE);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            child.setEnabled(true);
            child.setVisibility(View.VISIBLE);
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
        Intent intent = new Intent(view.getContext(), EmployerSettingsActivity.class);
        /*
        intent.putExtra("firm_name", userInfoResponse.getEmployerRequests().getFirmName());
        intent.putExtra("avatar", userInfoResponse.getAvatar());
        intent.putExtra("name", userInfoResponse.getName());
        intent.putExtra("phone", userInfoResponse.getPhone());
        intent.putExtra("social_networks_links", userInfoResponse.getSocialNetworksLinks());
        intent.putExtra("interface_language", userInfoResponse.getEndonymInterfaceLanguage());
        intent.putExtra("communication_languages", userInfoResponse.getCommunicationLanguages());
        intent.putExtra("are_languages_matched", userInfoResponse.isAreLanguagesMatched());
        intent.putExtra("limit_in_the_search", userInfoResponse.getLimitForTheSearch());
        intent.putExtra("is_multivacancy_allowed", userInfoResponse.isMultivacancyAllowed());
         */
        startActivity(intent);
    }

    public void onSearchClick(View view) {
        Intent intent = new Intent(this, SearchEmployeesActivity.class);
        startActivity(intent);
    }

    public void onVacanciesSettingClick(View view) {
        Intent intent = new Intent(this, EmployerVacanciesSettingActivity.class);
        startActivity(intent);
    }

    public void onMapClick(View view) {
        generalApi.changeWorkDisplay().enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {}

            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(WorkListEmployerActivity.this, "Error " +
                        "'changeWorkDisplay' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = new Intent(this, WorkMapEmployerActivity.class);
        startActivity(intent);
    }

    public void onTimersClick(View view) {
    }

    public void onChatsClick(View view) {
    }

    private void executeSearchForEmployees(String profession) {
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
        CheckBox autoCheckBox = linearLayout.findViewById(R.id.is_auto_checkbox);
        searchRequest.setAuto(autoCheckBox.isChecked());
        searchRequest.setMultivacancyAllowed(userInfoResponse.getEmployerRequests().isMultivacancyAllowedInSearch());
        CheckBox showTemporarilyInactiveEmployeescheckBox = linearLayout.findViewById(R.id.show_temporarily_inactive_employees);
        searchRequest.setShowTemporarilyInactiveEmployees(showTemporarilyInactiveEmployeescheckBox.isChecked());
        searchRequest.setLimit(userInfoResponse.getLimitForTheSearch());
        searchRequest.setAreLanguagesMatch(userInfoResponse.isAreLanguagesMatched());
        searchApi.getEmployeesOfChosenProfession(searchRequest).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                List<User> employees = response.body().getEmployeeList();
                for (User employee : employees) {
                    LocationsRequest locationsRequest = new LocationsRequest();
                    locationsRequest.setLat1(userInfoResponse.getLocation().getLatitude());
                    locationsRequest.setLong1(userInfoResponse.getLocation().getLongitude());
                    locationsRequest.setLat2(employee.getLocation().getLatitude());
                    locationsRequest.setLong2(employee.getLocation().getLongitude());
                    searchApi.getMeasuredDistance(locationsRequest).enqueue(new Callback<DistanceResponse>() {
                        @Override
                        public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {
                            addEmployeeButton(employee, response.body().getDistance());
                        }

                        @Override
                        public void onFailure(Call<DistanceResponse> call, Throwable t) {
                            Toast.makeText(WorkListEmployerActivity.this, "Error: " +
                                    "'getMeasuredDistance' method is failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(WorkListEmployerActivity.this, "Error " +
                        "'getEmployeesOfChosenProfession' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addEmployeeButton(User employee, double distance) {
        long employeeId = employee.getId();
        String name = employee.getName();
        byte rating = employee.getRanking();
        ProfessionToUserRequest professionToUserRequest = new ProfessionToUserRequest();
        professionToUserRequest.setId(employee.getId());
        Button button = new Button(searchResultsLayout.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(layoutParams);
        button.setBackground(getResources().getDrawable(R.drawable.rectangle_button));
        button.setTextColor(getResources().getColor(R.color.black));
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        String status;
        if (employee.getEmployeeData().getStatus() == 2) {
            status = getString(R.string.employee_status_custom) + Instant.ofEpochMilli
                            (employee.getEmployeeData().getActiveStatusStartDate())
                    .atZone(ZoneId.systemDefault()).toLocalDate().toString();
        } else {
            status = getString(R.string.employee_status) + employee.getEmployeeData().getStatus();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(status);
        sb.append("   ");
        sb.append(name);
        sb.append("   ");
        sb.append(rating);
        sb.append("   ");
        sb.append(distance);
        button.setText(sb);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EmployeeExtendedInfoActivity.class);
                /*
                intent.putExtra("name", name);
                intent.putExtra("rating", rating);
                intent.putExtra("avatar", avatar);
                intent.putExtra("status", status);
                StringBuilder languagesList = new StringBuilder();
                for (Language employeeLang : employee.getCommunicationLanguages()) {
                    languagesList.append(employeeLang.getLanguageEndonym() + ",");
                }
                languagesList.deleteCharAt(languagesList.length() - 1);
                intent.putExtra("languages", languagesList.toString());
                generalApi.getProfessionsToUserInEmployersLanguage(professionToUserRequest).enqueue(new Callback<StringResponse>() {
                    @Override
                    public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                        String professions = response.body().getString();
                        intent.putExtra("professions", professions);
                    }

                    @Override
                    public void onFailure(Call<StringResponse> call, Throwable t) {
                        Toast.makeText(WorkListEmployerActivity.this, "Error " +
                                "'getProfessionsToUserInEmployersLanguage' method is failure", Toast.LENGTH_SHORT).show();
                    }
                });
                generalApi.getInfoFromEmployeeInEmployersLanguage(professionToUserRequest).enqueue(new Callback<StringResponse>() {
                    @Override
                    public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                        String info = response.body().getString();
                        intent.putExtra("additional_info", info);
                    }

                    @Override
                    public void onFailure(Call<StringResponse> call, Throwable t) {
                        Toast.makeText(WorkListEmployerActivity.this, "Error: 'getInfoFromEmployeeInEmployersLanguage'" +
                                " method is failure", Toast.LENGTH_SHORT).show();
                    }
                });
                intent.putExtra("email", employee.getEmail());
                intent.putExtra("phone", employee.getPhone());
                intent.putExtra("social_networks_links", employee.getSocialNetworksLinks());
                 */
                intent.putExtra("employee_id", employeeId);
                startActivity(intent);
            }
        });
        searchResultsLayout.addView(button);
    }
}
