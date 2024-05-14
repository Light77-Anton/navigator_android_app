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
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.retrofit.ChatApi;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.request.VacancyRequest;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.TextListResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferSendingNewActivity extends AppCompatActivity {

    byte AUTOCOMPLETE_REQUEST_CODE = 1;
    private RetrofitService retrofitService;
    private GeneralApi generalApi;
    private SearchApi searchApi;
    private ChatApi chatApi;
    private UserInfoResponse userInfoResponse;
    private View entireView;
    VacancyRequest vacancyRequest;
    Spinner professionSpinner;
    Button jobAddressButton;
    DatePicker datePicker;
    TimePicker timePicker;
    EditText info;
    LocalDate currentDate;
    Bundle arguments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arguments = getIntent().getExtras();
        retrofitService = new RetrofitService();
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        searchApi = retrofitService.getRetrofit().create(SearchApi.class);
        entireView = getLayoutInflater().inflate(R.layout.activity_offer_sending_new, null);
        professionSpinner = entireView.findViewById(R.id.profession);
        ArrayList<String> professionNamesList = new ArrayList<>();
        generalApi.getProfessionsNamesInSpecifiedLanguage().enqueue(new Callback<TextListResponse>() {
            @Override
            public void onResponse(Call<TextListResponse> call, Response<TextListResponse> response) {
                professionNamesList.addAll(response.body().getList());
            }

            @Override
            public void onFailure(Call<TextListResponse> call, Throwable t) {
                Toast.makeText(OfferSendingNewActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<String> professionNamesAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, professionNamesList);
        professionNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professionSpinner.setAdapter(professionNamesAdapter);
        Button jobAddressButton = entireView.findViewById(R.id.job_address);
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
        info = entireView.findViewById(R.id.payment_and_additional_info);
        datePicker = entireView.findViewById(R.id.date_picker);
        long timestamp = System.currentTimeMillis();
        currentDate = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
        datePicker.init(currentDate.getYear(), currentDate.getMonthValue(), currentDate.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {}
                });
        timePicker = entireView.findViewById(R.id.vacancy_time_picker);
        setContentView(R.layout.activity_offer_sending_new);
    }

    public void onContinue(View view) {
        if (isAddressValid(jobAddressButton.getText().toString())) {
            vacancyRequest = new VacancyRequest();
            vacancyRequest.setProfessionName(professionSpinner.toString());
            vacancyRequest.setJobAddress(jobAddressButton.getText().toString());
            vacancyRequest.setPaymentAndAdditionalInfo(info.getText().toString());
            vacancyRequest.setStartTimestamp(LocalDateTime.of(datePicker.getYear(), datePicker.getMonth(),
                    datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute()));
            chatApi.sendOfferFromEmployer(arguments.getString("id"), vacancyRequest).enqueue(new Callback<ResultErrorsResponse>() {
                @Override
                public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                    Intent intent = new Intent(view.getContext(), SettingWaitingDateTimeActivity.class);
                    intent.putExtras(arguments);
                    intent.putExtra("vacancy_request", vacancyRequest);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                    Toast.makeText(OfferSendingNewActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(OfferSendingNewActivity.this, "address is not exist", Toast.LENGTH_SHORT).show();
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

    public void onBack(View view) {
        Intent intent = new Intent(this, OfferSendingChooseActivity.class);
        intent.putExtras(arguments);
        startActivity(intent);
    }
}
