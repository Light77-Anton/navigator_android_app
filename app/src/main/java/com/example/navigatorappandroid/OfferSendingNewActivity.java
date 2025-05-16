package com.example.navigatorappandroid;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.navigatorappandroid.model.ProfessionName;
import com.example.navigatorappandroid.retrofit.request.VacancyRequest;
import com.example.navigatorappandroid.retrofit.response.ProfessionNamesListResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferSendingNewActivity extends BaseActivity {

    private PlacesClient placesClient;
    byte AUTOCOMPLETE_REQUEST_CODE = 1;
    VacancyRequest vacancyRequest;
    Spinner professionSpinner;
    Button jobAddressButton;
    DatePicker startDatePicker;
    TimePicker startTimePicker;
    DatePicker waitingDatePicker;
    TimePicker waitingTimePicker;
    EditText info;
    LocalDate currentDate;
    CheckBox waitingUntilStartDateTimeCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_sending_new);
        setCurrentActivity(this);
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(this);
        professionSpinner = findViewById(R.id.profession);
        ArrayList<ProfessionName> professionNamesList = new ArrayList<>();
        generalApi.getProfessionsNamesInSpecifiedLanguage().enqueue(new Callback<ProfessionNamesListResponse>() {
            @Override
            public void onResponse(Call<ProfessionNamesListResponse> call, Response<ProfessionNamesListResponse> response) {
                professionNamesList.addAll(response.body().getList());
            }
            @Override
            public void onFailure(Call<ProfessionNamesListResponse> call, Throwable t) {
                Toast.makeText(OfferSendingNewActivity.this, "Error " +
                        "'getProfessionsNamesInSpecifiedLanguage' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<String> professionNamesAdapter = new ArrayAdapter
                (this, android.R.layout.simple_spinner_item, professionNamesList);
        professionNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professionSpinner.setAdapter(professionNamesAdapter);
        Button jobAddressButton = findViewById(R.id.job_address);
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
        info = findViewById(R.id.payment_and_additional_info);
        startDatePicker = findViewById(R.id.start_date_picker);
        long timestamp = System.currentTimeMillis();
        currentDate = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
        startDatePicker.init(currentDate.getYear(), currentDate.getMonthValue(), currentDate.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {}
                });
        startTimePicker = findViewById(R.id.start_time_picker);
        LocalTime currentTime = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalTime();
        startTimePicker.setIs24HourView(true);
        startTimePicker.setHour(currentTime.getHour());
        startTimePicker.setMinute(currentTime.getMinute());
        waitingUntilStartDateTimeCheckbox = findViewById(R.id.waiting_until_start_date_time_checkbox);
        waitingDatePicker = findViewById(R.id.waiting_date_picker);
        waitingTimePicker = findViewById(R.id.waiting_time_picker);
    }

    public void onSend(View view) {
        boolean isDataValid = true;
        if (!isAddressValid(jobAddressButton.getText().toString())) {
            isDataValid = false;
            Toast.makeText(OfferSendingNewActivity.this,
                    getResources().getString(R.string.address_is_not_exist), Toast.LENGTH_SHORT).show();
        }
        if (info.getText().toString().length() > 200) {
            isDataValid = false;
            Toast.makeText(OfferSendingNewActivity.this,
                    getResources().getString(R.string.additional_info_is_too_long), Toast.LENGTH_SHORT).show();
        }
        if (isDataValid) {
            vacancyRequest = new VacancyRequest();
            vacancyRequest.setProfessionName(professionSpinner.toString());
            vacancyRequest.setJobAddress(jobAddressButton.getText().toString());
            vacancyRequest.setPaymentAndAdditionalInfo(info.getText().toString());
            vacancyRequest.setStartTimestamp(LocalDateTime.of(startDatePicker.getYear(), startDatePicker.getMonth(),
                    startDatePicker.getDayOfMonth(), startTimePicker.getHour(), startTimePicker.getMinute()));
            if (waitingUntilStartDateTimeCheckbox.isChecked()) {
                vacancyRequest.setWaitingTimestamp(vacancyRequest.getStartTimestamp());
            } else {
                vacancyRequest.setWaitingTimestamp(LocalDateTime.of(waitingDatePicker.getYear(), waitingDatePicker.getMonth(),
                        waitingDatePicker.getDayOfMonth(), waitingTimePicker.getHour(), waitingTimePicker.getMinute()));
            }
            chatApi.sendOfferFromEmployer(arguments.getString("employee_id"), vacancyRequest).enqueue(new Callback<ResultErrorsResponse>() {
                @Override
                public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                    onBack(true);
                }
                @Override
                public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                    Toast.makeText(OfferSendingNewActivity.this, "Error " +
                            "'sendOfferFromEmployer' method is failure", Toast.LENGTH_SHORT).show();
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
                vacancyRequest.setLatitude(place.getLatLng().latitude);
                vacancyRequest.setLongitude(place.getLatLng().longitude);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Toast.makeText(this, "Error: " + Autocomplete.getStatusFromIntent(data).getStatusMessage(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
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

    public void onBack(boolean isOfferSent) {
        removeActivityFromQueue();
        Activity lastActivity = getLastActivity();
        if (lastActivity != null) {
            Intent intent = new Intent(this, lastActivity.getClass());
            intent.putExtras(arguments);
            intent.putExtra("is_offer_sent", isOfferSent);
            removeActivityFromQueue();
            finish();
            startActivity(intent);
        }
    }

    public void onBack(View view) {
        Activity lastActivity = getLastActivity();
        if (lastActivity != null) {
            Intent intent = new Intent(this, lastActivity.getClass());
            intent.putExtras(arguments);
            removeActivityFromQueue();
            finish();
            startActivity(intent);
        }
    }
}
