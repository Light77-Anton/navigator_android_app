package com.example.navigatorappandroid;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.example.navigatorappandroid.handler.LanguageHandler;
import com.example.navigatorappandroid.handler.LocationUpdateHandler;
import com.example.navigatorappandroid.model.Language;
import com.example.navigatorappandroid.model.User;
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
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkMapEmployerActivity extends AppCompatActivity implements OnMapReadyCallback {

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
    private LinearLayout linearLayout;
    private RetrofitService retrofitService;
    private View coreView;
    private GeneralApi generalApi;
    private SearchApi searchApi;
    private UserInfoResponse userInfoResponse;
    private HashSet<Marker> currentMarkers = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languageHandler = new LanguageHandler();
        coreView = getLayoutInflater().inflate(R.layout.activity_work_map_employer, null);
        linearLayout = coreView.findViewById(R.id.work_map_employer_sort_request_layout);
        retrofitService = new RetrofitService();
        searchApi = retrofitService.getRetrofit().create(SearchApi.class);
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.ea1ddfbd25d1e33e);
        mapFragment.getMapAsync(this);
        changeSortRequestFieldCondition();
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                userInfoResponse = response.body();
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(WorkMapEmployerActivity.this, "Error: 'getUserInfo' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = new Locale(languageHandler.getLanguageCode(userInfoResponse.getEndonymInterfaceLanguage()));
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        setContentView(R.layout.activity_work_map_employer);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
        locationUpdateHandler = new LocationUpdateHandler(lastKnownLocation.getLatitude(),
                lastKnownLocation.getLongitude(), userInfoResponse.getId());
        Bundle arguments = getIntent().getExtras();
        if (arguments.get("profession") != null) {
            executeSearchForEmployees(arguments.getString("profession"));
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

    public void onSettingsClick(View view) {
        Intent intent = new Intent(view.getContext(), EmployerSettingsActivity.class);
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
        intent.putExtra("activity", "map");
        startActivity(intent);
    }

    public void onSearchClick(View view) {
        Intent intent = new Intent(this, SearchEmployeesActivity.class);
        intent.putExtra("activity", "map");
        startActivity(intent);
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

    public void onVacanciesSettingClick(View view) {
        Intent intent = new Intent(this, EmployerVacanciesSettingActivity.class);
        intent.putExtra("activity", "map");
        startActivity(intent);
    }

    private void executeSearchForEmployees(String profession) {
        if (!currentMarkers.isEmpty()) {
            for (Marker marker : currentMarkers) {
                marker.remove();
            }
        }
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
        searchRequest.setMultivacancyAllowed(userInfoResponse.getEmployerRequests().isMultivacancyAllowedInSearch());
        searchRequest.setLimit(userInfoResponse.getLimitForTheSearch());
        searchRequest.setAreLanguagesMatch(userInfoResponse.isAreLanguagesMatched());
        searchApi.getEmployeesOfChosenProfession(searchRequest).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                List<User> employees = response.body().getEmployeeList();
                HashMap<Marker, User> map = new HashMap<>();
                for (User employee : employees) {
                    LatLng userLocation = new LatLng(employee.getLocation().getLatitude(),
                            employee.getLocation().getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(userLocation.latitude, userLocation.longitude), DEFAULT_ZOOM));
                    Marker marker = googleMap.addMarker( new MarkerOptions()
                            .position(userLocation)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.other_user_icon)));
                    map.put(marker, employee);
                    currentMarkers.add(marker);
                }
                googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(map));
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(WorkMapEmployerActivity.this, "Error: 'getEmployeesOfChosenProfession' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final LinearLayout layout;
        HashMap<Marker, User> map;

        public CustomInfoWindowAdapter(HashMap<Marker, User> map) {
            this.map = map;
            this.layout = (LinearLayout) getLayoutInflater().inflate(R.layout.info_popup, null);
        }

        @Nullable
        @Override
        public View getInfoContents(@NonNull Marker marker) {
            User employee = map.get(marker);
            TextView name = layout.findViewById(R.id.name);
            TextView rating = layout.findViewById(R.id.rating);
            TextView distance = layout.findViewById(R.id.distance);
            name.setText(employee.getName());
            rating.setText(employee.getRanking().toString());
            LocationsRequest locationsRequest = new LocationsRequest();
            locationsRequest.setLat1(userInfoResponse.getLocation().getLatitude());
            locationsRequest.setLat2(employee.getLocation().getLatitude());
            locationsRequest.setLong1(userInfoResponse.getLocation().getLongitude());
            locationsRequest.setLong2(employee.getLocation().getLongitude());
            searchApi.getMeasuredDistance(locationsRequest).enqueue(new Callback<DistanceResponse>() {
                @Override
                public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {
                    distance.setText(response.body().getDistance().toString());
                }

                @Override
                public void onFailure(Call<DistanceResponse> call, Throwable t) {
                    Toast.makeText(WorkMapEmployerActivity.this, "Error: 'getMeasuredDistance' " +
                            "method is failure", Toast.LENGTH_SHORT).show();
                }
            });
            Button extendedInfoButton = layout.findViewById(R.id.extended_info_button);
            extendedInfoButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(v.getContext(), EmployeeExtendedInfoActivity.class);
                   intent.putExtra("activity", "map");
                   intent.putExtra("id", employee.getId().toString());
                   intent.putExtra("name", employee.getName());
                   intent.putExtra("rating", employee.getRanking());
                   intent.putExtra("avatar", employee.getAvatar());
                   String status;
                   if (employee.getEmployeeData().getStatus() == 2) {
                       status = getString(R.string.employee_status_custom) + Instant.ofEpochMilli
                                       (employee.getEmployeeData().getActiveStatusStartDate())
                               .atZone(ZoneId.systemDefault()).toLocalDate().toString();
                   } else {
                       status = getString(R.string.employee_status) + employee.getEmployeeData().getStatus();
                   }
                   intent.putExtra("status", status);
                   StringBuilder languagesList = new StringBuilder();
                   for (Language employeeLang : employee.getCommunicationLanguages()) {
                       languagesList.append(employeeLang.getLanguageEndonym() + ",");
                   }
                   languagesList.deleteCharAt(languagesList.length() - 1);
                   intent.putExtra("languages", languagesList.toString());
                   ProfessionToUserRequest professionToUserRequest = new ProfessionToUserRequest();
                   professionToUserRequest.setId(employee.getId());
                   generalApi.getProfessionsToUserInEmployersLanguage(professionToUserRequest).enqueue(new Callback<StringResponse>() {
                       @Override
                       public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                           String professions = response.body().getString();
                           intent.putExtra("professions", professions);
                       }

                       @Override
                       public void onFailure(Call<StringResponse> call, Throwable t) {
                           Toast.makeText(WorkMapEmployerActivity.this, "Error: 'getProfessionsToUserInEmployersLanguage' " +
                                   "method is failure", Toast.LENGTH_SHORT).show();
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
                           Toast.makeText(WorkMapEmployerActivity.this, "Error: 'getInfoFromEmployeeInEmployersLanguage' " +
                                   "method is failure", Toast.LENGTH_SHORT).show();
                       }
                   });
                   intent.putExtra("email", employee.getEmail());
                   intent.putExtra("phone", employee.getPhone());
                   intent.putExtra("social_networks_links", employee.getSocialNetworksLinks());
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
