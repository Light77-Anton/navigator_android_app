package com.example.navigatorappandroid;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.request.JobRequest;
import com.example.navigatorappandroid.retrofit.request.StringRequest;
import com.example.navigatorappandroid.retrofit.response.IdResponse;
import com.example.navigatorappandroid.retrofit.response.TextListResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployerVacancyEditActivity extends AppCompatActivity {

    private PlacesClient placesClient;
    UserInfoResponse userInfoResponse;
    RetrofitService retrofitService;
    GeneralApi generalApi;
    SearchApi searchApi;
    LinearLayout linearLayout;
    Spinner requiredProfessionSpinner;
    Long professionId;
    AutoCompleteTextView jobAddressAutoComplete;
    DatePicker datePicker;
    EditText paymentAndAdditionalInfoEditText;
    ArrayList<String> professionNamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacancy_edit);
        Places.initialize(getApplicationContext(), BuildConfig.GOOGLE_PLACES_API_KEY);
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
        jobAddressAutoComplete = linearLayout.findViewById(R.id.job_address);
        ArrayAdapter<AutocompletePrediction> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        jobAddressAutoComplete.setAdapter(adapter);
        jobAddressAutoComplete.setThreshold(5);
        jobAddressAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            AutocompletePrediction item = (AutocompletePrediction) parent.getItemAtPosition(position);
            if (item != null) {
                String placeId = item.getPlaceId();
            }
        });
        jobAddressAutoComplete.addTextChangedListener(new AutocompleteTextWatcher(adapter, placesClient));
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

            requiredProfessionSpinner.setSelection();
        }
    }

    public void onConfirmClick(View view) {
        JobRequest jobRequest = new JobRequest();
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
        String enteredJobAddress = jobAddressAutoComplete.getText().toString();
        isAddressValid(enteredJobAddress);
        LocalDate localDate = LocalDate.of(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        jobRequest.setTimestamp(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        String info = paymentAndAdditionalInfoEditText.getText().toString();
        jobRequest.setPaymentAndAdditionalInfo(info);

        searchApi.setVacancy();
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
                // Valid address
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Invalid or non-existent address
        return false;
    }
}
