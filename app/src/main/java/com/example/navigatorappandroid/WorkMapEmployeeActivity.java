package com.example.navigatorappandroid;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
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
import com.example.navigatorappandroid.handler.LocationUpdateHandler;
import com.example.navigatorappandroid.model.Vacancy;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.request.SearchRequest;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkMapEmployeeActivity extends AppCompatActivity implements OnMapReadyCallback {

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
    LinearLayout linearLayout = findViewById(R.id.work_map_employee_sort_request_layout);
    RetrofitService retrofitService;
    GeneralApi generalApi;
    SearchApi searchApi;
    UserInfoResponse userInfoResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.activity_work_map_employee);
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.ea1ddfbd25d1e33e);
        mapFragment.getMapAsync(this);
        changeSortRequestFieldCondition();
        retrofitService = new RetrofitService();
        searchApi = retrofitService.getRetrofit().create(SearchApi.class);
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                userInfoResponse = response.body();
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(WorkMapEmployeeActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
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
        if (arguments.get("profession") != null) {
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
        intent.putExtra("activity", "map");
        startActivity(intent);
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
        intent.putExtra("communication_language", userInfoResponse.getCommunicationLanguages());
        intent.putExtra("are_languages_matched", userInfoResponse.isAreLanguagesMatched());
        intent.putExtra("limit_in_the_search", userInfoResponse.getLimitForTheSearch());
        intent.putExtra("activity", "map");
        intent.putExtra("work_requirements", userInfoResponse.getEmployeeData().getEmployeesWorkRequirements());
        intent.putExtra("is_drivers_license", userInfoResponse.getEmployeeData().isDriverLicense());
        intent.putExtra("is_auto", userInfoResponse.getEmployeeData().isAuto());
        startActivity(intent);
    }

    public void onSearchClick(View view) {
        Intent intent = new Intent(this, SearchVacanciesActivity.class);
        intent.putExtra("activity", "map");
        startActivity(intent);
    }

    private void enableLinearLayout() {
        linearLayout.setEnabled(true);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            child.setEnabled(true);
        }
    }

    private void disableLinearLayout() {
        linearLayout.setEnabled(false);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            child.setEnabled(false);
        }
    }

    private void changeSortRequestFieldCondition() {
        if (linearLayout.isEnabled()) {
            disableLinearLayout();
        } else {
            enableLinearLayout();
        }
    }

    private void executeSearchForVacancies(String profession, SearchApi searchApi){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setProfessionName(profession);
        RadioGroup radioGroup = linearLayout.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener((radiogroup, id)-> {
            RadioButton radio = findViewById(id);
            switch (radio.getText().toString()) {
                case "Sort by person\\'s name":
                    searchRequest.setSortType("name");
                    break;
                case "Sort by reputation":
                    searchRequest.setSortType("rating");
                    break;
                case "Sort by location":
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
                Toast.makeText(WorkMapEmployeeActivity.this, "fail", Toast.LENGTH_SHORT).show();
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
            TextView startDate = layout.findViewById(R.id.status_or_start_date);
            name.setText(vacancy.getEmployerRequests().getEmployer().getName());
            rating.setText(vacancy.getEmployerRequests().getEmployer().getRanking().toString());
            startDate.setText(vacancy.getStartDateTime().toString());

            return layout;
        }

        @Nullable
        @Override
        public View getInfoWindow(@NonNull Marker marker) {

            return null;
        }
    }
}
