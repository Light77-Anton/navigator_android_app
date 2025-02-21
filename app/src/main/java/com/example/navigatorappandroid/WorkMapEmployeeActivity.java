package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
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
import androidx.core.content.ContextCompat;
import com.example.navigatorappandroid.handler.EmployeeStatusHandler;
import com.example.navigatorappandroid.model.Vacancy;
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

public class WorkMapEmployeeActivity extends MainDisplayActivity implements OnMapReadyCallback {

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
    private EmployeeStatusHandler employeeStatusHandler;
    private LinearLayout searchSettingsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_map_employee);
        sortRequestButton = findViewById(R.id.sort_request);
        filterRequestButton = findViewById(R.id.filters_request);
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.ea1ddfbd25d1e33e);
        mapFragment.getMapAsync(this);
        searchSettingsLayout = findViewById(R.id.work_map_employee_sort_request_layout);
        changeSortRequestFieldCondition();
        if (userInfoResponse.getEmployeeData().getStatus() == 2) {
            employeeStatusHandler = new EmployeeStatusHandler();
        }
        if (savedInstanceState != null) {
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setCurrentStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userInfoResponse.getEmployeeData().getStatus() == 2) {
            employeeStatusHandler.startStatusChecking();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (userInfoResponse.getEmployeeData().getStatus() == 2) {
            employeeStatusHandler.stopStatusChecking();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
        if (arguments != null && arguments.get("profession") != null) {
            executeSearchForVacancies(arguments.getString("profession"),
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

    public void onStatusClick(View view) {
        Intent intent = new Intent(this, EmployeeStatusActivity.class);
        finish();
        startActivity(intent);
    }

    public void onSettingsClick(View view) {
        Intent intent = new Intent(view.getContext(), EmployeeSettingsActivity.class);
        finish();
        startActivity(intent);
    }

    public void onSearchClick(View view) {
        Intent intent = new Intent(this, SearchVacanciesActivity.class);
        finish();
        startActivity(intent);
    }

    public void onListClick(View view) {
        generalApi.changeWorkDisplay().enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                Intent intent = new Intent(view.getContext(), WorkListEmployeeActivity.class);
                finish();
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(WorkMapEmployeeActivity.this, "Error " +
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
                        executeSearchForVacancies(arguments.getString("profession")
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
                            viewMap.put("rating", radioButton);
                            radioButton.setContentDescription("rating");
                            break;
                        case 2:
                            viewMap.put("location", radioButton);
                            radioButton.setContentDescription("location");
                            break;
                        default:
                            viewMap.put("name", radioButton);
                            radioButton.setContentDescription("name");
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
                        executeSearchForVacancies(arguments.getString("profession"),
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
                filterRequestButton.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        ContextCompat.getDrawable(this, R.drawable.baseline_arrow_upward_24), null);
            }
        }
    }

    private void executeSearchForVacancies(String profession, String[] additionalLanguages) {
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
        searchApi.getVacanciesByProfession(searchRequest).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                List<Vacancy> vacancies = response.body().getVacancyList();
                HashMap<Marker, Vacancy> map = new HashMap<>();
                for (Vacancy vacancy : vacancies) {
                    LatLng jobLocation = new LatLng(vacancy.getJobLocation().getLatitude(),
                            vacancy.getJobLocation().getLongitude());
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

    public void onAddLanguagesClick(View view) {
        Intent intent = new Intent(this, ChooseAdditionalLanguagesActivity.class);
        finish();
        startActivity(intent);
    }

    private void setCurrentStatus() {
        byte currentStatus = userInfoResponse.getEmployeeData().getStatus();
        switch (currentStatus) {
            case 0:
                statusButton.setCompoundDrawablesWithIntrinsicBounds(
                        null, null,
                        ContextCompat.getDrawable(this, R.drawable.inactive), null);
                break;
            case 1:
                statusButton.setCompoundDrawablesWithIntrinsicBounds(
                        null, null,
                        ContextCompat.getDrawable(this, R.drawable.active), null);
                break;
            default:
                statusButton.setCompoundDrawablesWithIntrinsicBounds(
                        null, null,
                        ContextCompat.getDrawable(this, R.drawable.temporarily_inactive), null);
        }
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
                    intent.putExtra("vacancy_id", vacancy.getId());
                    intent.putExtra("employer_id", vacancy.getEmployerRequests().getEmployer().getId().toString());
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
