package com.example.navigatorappandroid;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.navigatorappandroid.handler.EmployeeStatusHandler;
import com.example.navigatorappandroid.handler.LanguageHandler;
import com.example.navigatorappandroid.handler.LocationUpdateHandler;
import com.example.navigatorappandroid.model.Vacancy;
import com.example.navigatorappandroid.retrofit.AuthApi;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.request.LocationsRequest;
import com.example.navigatorappandroid.retrofit.request.SearchRequest;
import com.example.navigatorappandroid.retrofit.response.DistanceResponse;
import com.example.navigatorappandroid.retrofit.response.LoginResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.SearchResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkMapEmployeeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LanguageHandler languageHandler;
    private LatLng latLngMyLocation;
    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap googleMap;
    private final LatLng defaultLocation = new LatLng(0.0, 0.0);
    private static final int DEFAULT_ZOOM = 15;
    private PlacesClient placesClient;
    private boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private CameraPosition cameraPosition;
    private LocationUpdateHandler locationUpdateHandler;
    private EmployeeStatusHandler employeeStatusHandler;
    private LinearLayout linearLayout;
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
        setContentView(R.layout.activity_work_map_employee);
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.ea1ddfbd25d1e33e);
        mapFragment.getMapAsync(this);
        languageHandler = new LanguageHandler();
        linearLayout = findViewById(R.id.work_map_employee_sort_request_layout);
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        changeSortRequestFieldCondition();
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                userInfoResponse = response.body();
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(WorkMapEmployeeActivity.this, "Error: 'getUserInfo' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = new Locale(languageHandler.getLanguageCode(userInfoResponse.getEndonymInterfaceLanguage()));
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
        employeeStatusHandler = new EmployeeStatusHandler();
        locationUpdateHandler = new LocationUpdateHandler(lastKnownLocation.getLatitude(),
                lastKnownLocation.getLongitude(), userInfoResponse.getId());
        Bundle arguments = getIntent().getExtras();
        if (arguments != null && arguments.get("profession") != null) {
            executeSearchForVacancies(arguments.getString("profession"), searchApi);
        }
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
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            e.printStackTrace();
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
                            if (lastKnownLocation != null) {
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                double lat = lastKnownLocation.getLatitude();
                                double lng = lastKnownLocation.getLongitude();
                                latLngMyLocation = new LatLng(lat, lng);
                                googleMap.addMarker( new MarkerOptions()
                                        .position(latLngMyLocation)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_location_icon)));
                            }
                        } else {
                            googleMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            e.printStackTrace();
        }
    }

    public void onStatusClick(View view) {
        Intent intent = new Intent(this, EmployeeStatusActivity.class);
        startActivity(intent);
    }

    public void onSettingsClick(View view) {
        Intent intent = new Intent(view.getContext(), EmployeeSettingsActivity.class);
        /*
        intent.putExtra("avatar", userInfoResponse.getAvatar());
        intent.putExtra("name", userInfoResponse.getName());
        intent.putExtra("phone", userInfoResponse.getPhone());
        intent.putExtra("is_phone_hidden", userInfoResponse.isPhoneHidden());
        intent.putExtra("is_email_hidden", userInfoResponse.isEmailHidden());
        intent.putExtra("social_networks_links", userInfoResponse.getSocialNetworksLinks());
        intent.putExtra("interface_language", userInfoResponse.getEndonymInterfaceLanguage());
        intent.putExtra("communication_languages", userInfoResponse.getCommunicationLanguages());
        intent.putExtra("is_multivacancy_allowed", userInfoResponse.isMultivacancyAllowed());
        intent.putExtra("are_languages_matched", userInfoResponse.isAreLanguagesMatched());
        intent.putExtra("limit_in_the_search", userInfoResponse.getLimitForTheSearch());
        intent.putExtra("work_requirements", userInfoResponse.getEmployeeData().getEmployeesWorkRequirements());
        intent.putExtra("is_drivers_license", userInfoResponse.getEmployeeData().isDriverLicense());
        intent.putExtra("is_auto", userInfoResponse.getEmployeeData().isAuto());
         */
        startActivity(intent);
    }

    public void onSearchClick(View view) {
        Intent intent = new Intent(this, SearchVacanciesActivity.class);
        startActivity(intent);
    }

    public void onListClick(View view) {
        generalApi.changeWorkDisplay().enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {}

            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(WorkMapEmployeeActivity.this, "Error " +
                        "'changeWorkDisplay' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = new Intent(this, WorkListEmployeeActivity.class);
        startActivity(intent);
    }

    public void onTimersClick(View view) {
    }

    public void onChatsClick(View view) {
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

    private void executeSearchForVacancies(String profession, SearchApi searchApi) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setProfessionName(profession);
        RadioGroup radioGroup = linearLayout.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener((radiogroup, id)-> {
            RadioButton radio = findViewById(id);
            switch (radio.getText().toString()) {
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
        searchRequest.setMultivacancyAllowed(userInfoResponse.isMultivacancyAllowed());
        searchRequest.setLimit(userInfoResponse.getLimitForTheSearch());
        searchRequest.setAreLanguagesMatch(userInfoResponse.isAreLanguagesMatched());
        searchApi.getVacanciesByProfession(searchRequest).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                List<Vacancy> vacancies = response.body().getVacancyList();
                HashMap<Marker, Vacancy> map = new HashMap<>();
                for (Vacancy vacancy : vacancies) {
                    LatLng jobLocation = new LatLng(vacancy.getJobLocation().getLatitude(),
                            vacancy.getJobLocation().getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(jobLocation.latitude, jobLocation.longitude), DEFAULT_ZOOM));
                    Marker marker = googleMap.addMarker( new MarkerOptions()
                            .position(jobLocation)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.other_user_icon)));
                    map.put(marker, vacancy);
                }
                googleMap.setInfoWindowAdapter(new WorkMapEmployeeActivity.CustomInfoWindowAdapter(map));
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(WorkMapEmployeeActivity.this, "Error: 'getVacanciesByProfession' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final LinearLayout layout;
        HashMap<Marker, Vacancy> map;

        public CustomInfoWindowAdapter(HashMap<Marker, Vacancy> map) {
            this.map = map;
            this.layout = (LinearLayout) getLayoutInflater().inflate(R.layout.info_popup, null);
        }

        @Nullable
        @Override
        public View getInfoContents(@NonNull Marker marker) {
            Vacancy vacancy = map.get(marker);
            TextView name = layout.findViewById(R.id.name);
            TextView rating = layout.findViewById(R.id.rating);
            TextView distance = layout.findViewById(R.id.distance);
            name.setText(vacancy.getEmployerRequests().getEmployer().getName());
            rating.setText(vacancy.getEmployerRequests().getEmployer().getRanking());
            LocationsRequest locationsRequest = new LocationsRequest();
            locationsRequest.setLat1(userInfoResponse.getLocation().getLatitude());
            locationsRequest.setLat2(vacancy.getJobLocation().getLatitude());
            locationsRequest.setLong1(userInfoResponse.getLocation().getLongitude());
            locationsRequest.setLong2(vacancy.getJobLocation().getLongitude());
            searchApi.getMeasuredDistance(locationsRequest).enqueue(new Callback<DistanceResponse>() {
                @Override
                public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {
                    distance.setText(response.body().getDistance().toString());
                }

                @Override
                public void onFailure(Call<DistanceResponse> call, Throwable t) {
                    Toast.makeText(WorkMapEmployeeActivity.this, "Error: 'getMeasuredDistance' " +
                            "method is failure", Toast.LENGTH_SHORT).show();
                }
            });
            Button extendedInfoButton = layout.findViewById(R.id.extended_info_button);
            extendedInfoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), EmployeeExtendedInfoActivity.class);
                    /*
                    ProfessionToUserRequest professionToUserRequest = new ProfessionToUserRequest();
                    professionToUserRequest.setId(vacancy.getProfession().getId());
                    generalApi.getProfessionNameInSpecifiedLanguage(professionToUserRequest).enqueue(new Callback<StringResponse>() {
                        @Override
                        public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                            intent.putExtra("vacancy_profession", response.body().getString());
                        }

                        @Override
                        public void onFailure(Call<StringResponse> call, Throwable t) {
                            Toast.makeText(WorkMapEmployeeActivity.this, "Error: 'getProfessionNameInSpecifiedLanguage' " +
                                    "method is failure", Toast.LENGTH_SHORT).show();
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
                    intent.putExtra("name", vacancy.getEmployerRequests().getEmployer().getName());
                    intent.putExtra("rating", vacancy.getEmployerRequests().getEmployer().getRanking());
                    intent.putExtra("avatar", vacancy.getEmployerRequests().getEmployer().getAvatar());
                    intent.putExtra("firm_name", vacancy.getEmployerRequests().getFirmName());
                    StringBuilder languagesList = new StringBuilder();
                    for (Language employeeLang : vacancy.getEmployerRequests().getEmployer().getCommunicationLanguages()) {
                        languagesList.append(employeeLang.getLanguageEndonym() + ",");
                    }
                    languagesList.deleteCharAt(languagesList.length() - 1);
                    intent.putExtra("languages", languagesList.toString());
                    intent.putExtra("email", vacancy.getEmployerRequests().getEmployer().getEmail());
                    intent.putExtra("phone", vacancy.getEmployerRequests().getEmployer().getPhone());
                    intent.putExtra("social_networks_links", vacancy.getEmployerRequests().getEmployer().getSocialNetworksLinks());
                     */
                    intent.putExtra("vacancy_id", vacancy.getId());
                    intent.putExtra("employer_id", vacancy.getEmployerRequests().getEmployer().getId().toString());
                    startActivity(intent);
                }
            });

            return layout;
        }

        @Nullable
        @Override
        public View getInfoWindow(@NonNull Marker marker) {

            return null;
        }
    }
}
