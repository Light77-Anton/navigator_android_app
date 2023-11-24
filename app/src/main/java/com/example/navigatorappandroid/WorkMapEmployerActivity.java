package com.example.navigatorappandroid;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.navigatorappandroid.model.User;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.request.RequestForEmployees;
import com.example.navigatorappandroid.retrofit.response.EmployeeInfoResponse;
import com.example.navigatorappandroid.retrofit.response.EmployeesListResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveCanceledListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WorkMapEmployerActivity extends AppCompatActivity implements OnCameraMoveCanceledListener,
        OnMapReadyCallback {

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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
        Bundle arguments = getIntent().getExtras();
        if (arguments.get("profession") != null) {
            RetrofitService retrofitService = new RetrofitService();
            SearchApi searchApi = retrofitService.getRetrofit().create(SearchApi.class);
            UserInfoResponse userInfoResponse = searchApi.getEmployeeInfo().enqueue(new Callback<EmployeeInfoResponse>() {
                @Override
                public void onResponse(Call<EmployeeInfoResponse> call, Response<EmployeeInfoResponse> response) {}

                @Override
                public void onFailure(Call<EmployeeInfoResponse> call, Throwable t) {
                    Toast.makeText(WorkMapEmployerActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            });
            RequestForEmployees requestForEmployees = new RequestForEmployees();
            requestForEmployees.setProfessionName(arguments.getString("profession"));
            requestForEmployees.setLimit(userInfoResponse.getLimitForTheSearch());
            requestForEmployees.setAuto(userInfoResponse.getEmployeeData().isAuto());
            requestForEmployees.setAreLanguagesMatch(userInfoResponse.isAreLanguagesMatched());
            requestForEmployees.setInRadiusOf(userInfoResponse.getLimitForTheSearch());
            searchApi.getEmployeesOfChosenProfession(requestForEmployees).enqueue(new Callback<EmployeesListResponse>() {
                @Override
                public void onResponse(Call<EmployeesListResponse> call, Response<EmployeesListResponse> response) {

                }

                @Override
                public void onFailure(Call<EmployeesListResponse> call, Throwable t) {

                }
            });

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

    @Override
    public void onCameraMoveCanceled() {

    }

    public void onSettingsClick(View view) {
        User user;
        RetrofitService retrofitService = new RetrofitService();
        GeneralApi generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                String role = response.body().getRole();
                if (role.equals("Employee")) {
                    Intent intent = new Intent(view.getContext(), EmployeeSettingsActivity.class);
                    fillIntentWithGeneralData(intent, response);
                    intent.putExtra("work_requirements",response.body().getEmployeeData().getEmployeesWorkRequirements());
                    intent.putExtra("is_drivers_license", response.body().getEmployeeData().isDriverLicense());
                    intent.putExtra("is_auto", response.body().getEmployeeData().isAuto());
                    intent.putExtra("professions", response.body().getEmployeeData().getProfessionToUserList());
                    intent.putExtra("status", response.body().getEmployeeData().getStatus());
                    startActivity(intent);
                } else if (role.equals("Employer")) {
                    Intent intent = new Intent(view.getContext(), EmployerSettingsActivity.class);
                    fillIntentWithGeneralData(intent, response);
                    intent.putExtra("firm_name", response.body().getEmployerRequests().getFirmName());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(view.getContext(), ModeratorSettingsActivity.class);
                    fillIntentWithGeneralData(intent, response);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(WorkMapEmployerActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillIntentWithGeneralData(Intent intent, Response<UserInfoResponse> response) {
        intent.putExtra("avatar", response.body().getAvatar());
        intent.putExtra("name", response.body().getName());
        intent.putExtra("phone", response.body().getPhone());
        intent.putExtra("social_networks_links", response.body().getSocialNetworksLinks());
        intent.putExtra("interface_language", response.body().getEndonymInterfaceLanguage());
        intent.putExtra("communication_language", response.body().getCommunicationLanguages());
    }
}
