package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
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
import androidx.core.content.ContextCompat;
import com.example.navigatorappandroid.model.User;
import com.example.navigatorappandroid.retrofit.request.LocationsRequest;
import com.example.navigatorappandroid.retrofit.request.SearchRequest;
import com.example.navigatorappandroid.retrofit.response.DistanceResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.SearchResponse;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import java.util.HashMap;
import java.util.List;
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

    public void changeSearchSettingsLayoutCondition(View view) {
        searchSettingsLayout.removeAllViews();
        if (view.getContentDescription().toString().equals("sort")) {
            if (isSortRequestOpened) {
                isSortRequestOpened = false;
                sortRequestButton.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        ContextCompat.getDrawable(this, R.drawable.baseline_arrow_downward_24), null);
            } else {
                isSortRequestOpened = true;
                RadioGroup radioGroup = new RadioGroup(searchResultsLayout.getContext());
                radioGroup.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams groupParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                radioGroup.setLayoutParams(groupParams);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        executeSearchForEmployees(arguments.getString("profession")
                                , arguments.getStringArray("languages_array"));
                    }
                });
                for (int i = 0; i < 3; i++) {
                    RadioButton radioButton = new RadioButton(searchResultsLayout.getContext());
                    radioButton.setId(View.generateViewId());
                    radioButton.setText(getResources().getString(R.string.sort_by_persons_name));
                    radioButton.setBackground(ContextCompat.getDrawable(this, R.color.custom_gray_blue));
                    LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP, 50, view.getResources().getDisplayMetrics()));
                    radioButton.setLayoutParams(buttonParams);
                    switch (i) {
                        case 1:
                            radioButton.setContentDescription("rating");
                            viewMap.put("rating", radioButton);
                            break;
                        case 2:
                            radioButton.setContentDescription("location");
                            viewMap.put("location", radioButton);
                            break;
                        default:
                            radioButton.setContentDescription("name");
                            viewMap.put("name", radioButton);
                    }
                    radioGroup.addView(radioButton);
                }
                searchSettingsLayout.addView(radioGroup);
                viewMap.put("radio_group", radioGroup);
                TextView seekbarTextView = new TextView(searchResultsLayout.getContext());
                LinearLayout.LayoutParams seekbarTextParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                seekbarTextView.setLayoutParams(seekbarTextParams);
                seekbarTextView.setBackground(ContextCompat.getDrawable(this, R.color.custom_gray_blue));
                seekbarTextView.setTextColor(getResources().getColor(R.color.black));
                seekbarTextView.setText(R.string.in_radius_of);
                searchSettingsLayout.addView(seekbarTextView);
                SeekBar seekBar = new SeekBar(searchResultsLayout.getContext());
                LinearLayout.LayoutParams seekbarParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 48, view.getResources().getDisplayMetrics()));
                seekBar.setLayoutParams(seekbarParams);
                seekBar.setBackground(ContextCompat.getDrawable(this, R.color.custom_gray_blue));
                seekBar.setMin(1);
                seekBar.setMax(50);
                seekBar.setProgress(25);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        executeSearchForEmployees(arguments.getString("profession"),
                                arguments.getStringArray("languages_array"));
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });
                searchSettingsLayout.addView(seekBar);
                viewMap.put("seekbar", seekBar);
                sortRequestButton.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        ContextCompat.getDrawable(this, R.drawable.baseline_arrow_upward_24), null);
            }
        } else {
            // пока фильтров у рабочего нет
            if (isFiltersRequestOpened) {
                isFiltersRequestOpened = false;
                filterRequestButton.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        ContextCompat.getDrawable(this, R.drawable.baseline_arrow_downward_24), null);
            } else {
                isFiltersRequestOpened = true;
                for (int i = 0; i < 2; i++) {
                    CheckBox checkbox = new CheckBox(searchResultsLayout.getContext());
                    LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP, 50, view.getResources().getDisplayMetrics()));
                    checkbox.setLayoutParams(buttonParams);
                    checkbox.setBackground(ContextCompat.getDrawable(this, R.color.custom_gray_blue));
                    checkbox.setId(View.generateViewId());
                    searchSettingsLayout.addView(checkbox);
                   if (i == 0) {
                       checkbox.setText(getResources().getString(R.string.is_auto));
                       viewMap.put("is_auto", checkbox);
                   } else {
                       checkbox.setText(getResources().getString(R.string.show_temporarily_inactive_employees));
                       viewMap.put("show_temporarily_inactive_employees", checkbox);
                   }
                    searchSettingsLayout.addView(checkbox);
                }
                filterRequestButton.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        ContextCompat.getDrawable(this, R.drawable.baseline_arrow_upward_24), null);
            }
        }
    }

    private void executeSearchForEmployees(String profession, String[] additionalLanguages) {
        googleMap.clear();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setProfessionName(profession);
        searchRequest.setAdditionalLanguages(additionalLanguages);
        RadioGroup radioGroup = (RadioGroup) viewMap.get("radio_group");
        int checkedButtonId = radioGroup.getCheckedRadioButtonId();
        if (checkedButtonId != -1) {
            RadioButton checkedButton = findViewById(checkedButtonId);
            searchRequest.setSortType(checkedButton.getContentDescription().toString());
        } else {
            searchRequest.setSortType("");
        }
        SeekBar seekBar = (SeekBar) viewMap.get("seekbar");
        searchRequest.setInRadiusOf(seekBar.getProgress());
        searchRequest.setMultivacancyAllowed(userInfoResponse.getEmployeeData().isMultivacancyAllowed());
        searchRequest.setLimit(userInfoResponse.getLimitForTheSearch());
        searchRequest.setAreLanguagesMatch(userInfoResponse.isAreLanguagesMatched());
        if (viewMap.containsKey("is_auto")) {
            CheckBox isAutoCheckbox = (CheckBox) viewMap.get("is_auto");
            searchRequest.setAuto(isAutoCheckbox.isChecked());
        }
        if (viewMap.containsKey("show_temporarily_inactive_employees")) {
            CheckBox showTemporarilyInactiveEmployeescheckBox = (CheckBox) viewMap.get("is_auto");
            searchRequest.setShowTemporarilyInactiveEmployees(showTemporarilyInactiveEmployeescheckBox.isChecked());
        }
        searchApi.getEmployeesOfChosenProfession(searchRequest).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                List<User> employees = response.body().getEmployeeList();
                HashMap<Marker, User> map = new HashMap<>();
                for (User employee : employees) {
                    LatLng userLocation = new LatLng(employee.getLocation().getLatitude(),
                            employee.getLocation().getLongitude());
                    Marker marker = googleMap.addMarker( new MarkerOptions()
                            .position(userLocation)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.other_user_icon)));
                    map.put(marker, employee);
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
                   finish();
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
