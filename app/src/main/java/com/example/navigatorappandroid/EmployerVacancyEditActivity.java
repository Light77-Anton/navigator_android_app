package com.example.navigatorappandroid;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.request.JobRequest;
import com.example.navigatorappandroid.retrofit.request.StringRequest;
import com.example.navigatorappandroid.retrofit.response.IdResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.TextListResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import com.example.navigatorappandroid.retrofit.response.VacancyInfoResponse;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployerVacancyEditActivity extends AppCompatActivity {

    private PlacesClient placesClient;
    byte AUTOCOMPLETE_REQUEST_CODE = 1;
    UserInfoResponse userInfoResponse;
    RetrofitService retrofitService;
    GeneralApi generalApi;
    SearchApi searchApi;
    LinearLayout linearLayout;
    Spinner requiredProfessionSpinner;
    Long professionId;
    Button jobAddressButton;
    Double jobAddressLatitude;
    Double jobAddressLongitude;
    DatePicker datePicker;
    EditText paymentAndAdditionalInfoEditText;
    ArrayList<String> professionNamesList;
    VacancyInfoResponse vacancyInfoResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacancy_edit);
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(this);
        ScrollView scrollView = (ScrollView) getLayoutInflater().inflate(R.layout.activity_vacancy_edit, null);
        linearLayout = scrollView.findViewById(R.id.activity_vacancy_edit_layout);
        Bundle arguments = getIntent().getExtras();
        retrofitService = new RetrofitService();
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        searchApi = retrofitService.getRetrofit().create(SearchApi.class);
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                userInfoResponse = response.body();
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(EmployerVacancyEditActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        requiredProfessionSpinner = linearLayout.findViewById(R.id.required_profession);
        professionNamesList = new ArrayList<>();
        generalApi.getProfessionsNamesInSpecifiedLanguage().enqueue(new Callback<TextListResponse>() {
            @Override
            public void onResponse(Call<TextListResponse> call, Response<TextListResponse> response) {
                professionNamesList.addAll(response.body().getList());
            }

            @Override
            public void onFailure(Call<TextListResponse> call, Throwable t) {

            }
        });
        ArrayAdapter<String> professionNamesAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, professionNamesList);
        professionNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        requiredProfessionSpinner.setAdapter(professionNamesAdapter);
        jobAddressButton = linearLayout.findViewById(R.id.job_address);
        jobAddressButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);
                try {
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                                    .build(v.getContext());
                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), "Error launching autocomplete intent", Toast.LENGTH_SHORT).show();
                }
            }
        });
        datePicker = linearLayout.findViewById(R.id.start_date);
        long timestamp = System.currentTimeMillis();
        LocalDate currentDate = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
        datePicker.init(currentDate.getYear(), currentDate.getMonthValue(), currentDate.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {}
                });
        paymentAndAdditionalInfoEditText = linearLayout.findViewById(R.id.payment_and_additional_info);
        if (arguments.getString("vacancy_id") != null) {
            String vacancyId = arguments.getString("vacancy_id");
            StringRequest stringRequest = new StringRequest();
            stringRequest.setString(vacancyId);
            searchApi.getVacancyById(stringRequest).enqueue(new Callback<VacancyInfoResponse>() {
                @Override
                public void onResponse(Call<VacancyInfoResponse> call, Response<VacancyInfoResponse> response) {
                    vacancyInfoResponse.setProfessionName(response.body().getProfessionName());
                    vacancyInfoResponse.setJobAddress(response.body().getJobAddress());
                    vacancyInfoResponse.setPaymentAndAdditionalInfo(response.body().getPaymentAndAdditionalInfo());
                    vacancyInfoResponse.setLocalDate(response.body().getLocalDate());
                    int positionInSpinner = professionNamesAdapter.getPosition(vacancyInfoResponse.getProfessionName());
                    requiredProfessionSpinner.setSelection(positionInSpinner);
                    jobAddressButton.setText(vacancyInfoResponse.getJobAddress());
                    datePicker.init(vacancyInfoResponse.getLocalDate().getYear(),
                            vacancyInfoResponse.getLocalDate().getMonthValue(),
                            vacancyInfoResponse.getLocalDate().getDayOfMonth(),
                            new DatePicker.OnDateChangedListener() {
                                @Override
                                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {}
                            });
                    paymentAndAdditionalInfoEditText.setText(vacancyInfoResponse.getPaymentAndAdditionalInfo());
                }

                @Override
                public void onFailure(Call<VacancyInfoResponse> call, Throwable t) {
                    Toast.makeText(EmployerVacancyEditActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                jobAddressLatitude = place.getLatLng().latitude;
                jobAddressLongitude = place.getLatLng().longitude;
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Toast.makeText(this, "Error: " + Autocomplete.getStatusFromIntent(data).getStatusMessage(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    public void onDeleteClick(View view) {
        Bundle arguments = getIntent().getExtras();
        if (arguments.getString("vacancy_id") != null) {
            StringRequest stringRequest = new StringRequest();
            stringRequest.setString(arguments.getString("vacancy_id"));
            searchApi.deleteVacancyById(stringRequest).enqueue(new Callback<ResultErrorsResponse>() {
                @Override
                public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {}

                @Override
                public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                    Toast.makeText(EmployerVacancyEditActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
        Intent intent = new Intent(this, EmployerVacanciesSettingActivity.class);
        startActivity(intent);
    }

    public void onConfirmClick(View view) {
        JobRequest jobRequest = new JobRequest();
        String enteredJobAddress = jobAddressButton.getText().toString();
        if (isAddressValid(enteredJobAddress)) {
            String professionName = requiredProfessionSpinner.toString();
            StringRequest stringRequest = new StringRequest();
            stringRequest.setString(professionName);
            generalApi.getProfessionIdByName(stringRequest).enqueue(new Callback<IdResponse>() {
                @Override
                public void onResponse(Call<IdResponse> call, Response<IdResponse> response) {
                    jobRequest.setProfessionId(response.body().getId());
                }

                @Override
                public void onFailure(Call<IdResponse> call, Throwable t) {
                    Toast.makeText(EmployerVacancyEditActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            });
            LocalDate localDate = LocalDate.of(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            jobRequest.setTimestamp(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
            String info = paymentAndAdditionalInfoEditText.getText().toString();
            jobRequest.setPaymentAndAdditionalInfo(info);
            jobRequest.setJobAddress(enteredJobAddress);
            jobRequest.setLatitude(jobAddressLatitude);
            jobRequest.setLongitude(jobAddressLongitude);
            searchApi.setVacancy(jobRequest).enqueue(new Callback<ResultErrorsResponse>() {
                @Override
                public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {}

                @Override
                public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                    Toast.makeText(EmployerVacancyEditActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(this, EmployerVacanciesSettingActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(EmployerVacancyEditActivity.this, "Job address not exists", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackClick(View view) {
        Intent intent = new Intent(this, EmployerVacanciesSettingActivity.class);
        startActivity(intent);
    }

    private boolean isAddressValid(String enteredAddress) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(enteredAddress, 1);
            if (addresses != null && addresses.size() > 0) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
