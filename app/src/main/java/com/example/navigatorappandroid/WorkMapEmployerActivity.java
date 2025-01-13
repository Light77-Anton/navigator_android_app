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

public class WorkMapEmployerActivity extends MainDisplayActivity implements OnMapReadyCallback {

    private LatLng latLngMyLocation;
    private GoogleMap googleMap;
    private final LatLng defaultLocation = new LatLng(0.0, 0.0);
    private static final int DEFAULT_ZOOM = 15;
    private PlacesClient placesClient;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private CameraPosition cameraPosition;
    private boolean isSortRequestOpened = false;
    private boolean isFiltersRequestOpened = false;
    private Button sortRequestButton;
    private Button filterRequestButton;
    private HashMap<String, View> viewMap;
    private Button toChatsButton;
    private Button statusButton;
    private LinearLayout searchSettingsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_map_employer);
        sortRequestButton = findViewById(R.id.sort_request);
        filterRequestButton = findViewById(R.id.filters_request);
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.ea1ddfbd25d1e33e);
        mapFragment.getMapAsync(this);
        searchSettingsLayout = findViewById(R.id.work_map_employer_sort_request_layout);
        changeSortRequestFieldCondition();
        if (savedInstanceState != null) {
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
        if (arguments != null && arguments.get("profession") != null) {
            executeSearchForEmployees(arguments.getString("profession"),
                    arguments.getStringArray("languages_array"));
        }
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

    public void onSettingsClick(View view) {
        Intent intent = new Intent(this, EmployerSettingsActivity.class);
        finish();
        startActivity(intent);
    }

    public void onSearchClick(View view) {
        Intent intent = new Intent(this, SearchEmployeesActivity.class);
        finish();
        startActivity(intent);
    }

    public void onListClick(View view) {
        generalApi.changeWorkDisplay().enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                Intent intent = new Intent(view.getContext(), WorkListEmployerActivity.class);
                finish();
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(WorkMapEmployerActivity.this, "Error " +
                        "'changeWorkDisplay' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onTimersClick(View view) {
        Intent intent = new Intent(this, TimersListActivity.class);
        finish();
        startActivity(intent);
    }

    public void onChatsClick(View view) {
        Intent intent = new Intent(this, ChatListActivity.class);
        finish();
        startActivity(intent);
    }

    private void enableLinearLayout() {
        searchSettingsLayout.setEnabled(true);
        searchSettingsLayout.setVisibility(View.VISIBLE);
        for (int i = 0; i < searchSettingsLayout.getChildCount(); i++) {
            View child = searchSettingsLayout.getChildAt(i);
            child.setEnabled(true);
            child.setVisibility(View.VISIBLE);
        }
    }

    private void disableLinearLayout() {
        searchSettingsLayout.setEnabled(false);
        searchSettingsLayout.setVisibility(View.INVISIBLE);
        for (int i = 0; i < searchSettingsLayout.getChildCount(); i++) {
            View child = searchSettingsLayout.getChildAt(i);
            child.setEnabled(false);
            child.setVisibility(View.INVISIBLE);
        }
    }

    private void changeSortRequestFieldCondition() {
        if (searchSettingsLayout.isEnabled()) {
            disableLinearLayout();
        } else {
            enableLinearLayout();
        }
    }

    public void onVacanciesSettingClick(View view) {
        Intent intent = new Intent(this, EmployerVacanciesSettingActivity.class);
        finish();
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
        CheckBox showTemporarilyInactiveEmployeescheckBox = linearLayout.findViewById(R.id.show_temporarily_inactive_employees);
        searchRequest.setShowTemporarilyInactiveEmployees(showTemporarilyInactiveEmployeescheckBox.isChecked());
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
            rating.setText(employee.getRanking());
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
                   intent.putExtra("employee_id", employee.getId().toString());
                   /*
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
                    */
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
