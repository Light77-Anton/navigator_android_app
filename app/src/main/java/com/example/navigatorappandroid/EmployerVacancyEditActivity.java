package com.example.navigatorappandroid;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.navigatorappandroid.model.ProfessionName;
import com.example.navigatorappandroid.model.Vacancy;
import com.example.navigatorappandroid.retrofit.request.VacancyRequest;
import com.example.navigatorappandroid.retrofit.response.ProfessionNamesListResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.VacancyInfoResponse;
import com.example.navigatorappandroid.retrofit.response.VacancyListResponse;
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
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployerVacancyEditActivity extends BaseActivity {

    private PlacesClient placesClient;
    byte AUTOCOMPLETE_REQUEST_CODE = 1;
    LinearLayout linearLayout;
    Spinner templatesSpinner;
    Spinner requiredProfessionSpinner;
    EditText quotasNumberEditText;
    Button jobAddressButton;
    Double jobAddressLatitude;
    Double jobAddressLongitude;
    DatePicker datePicker;
    DatePicker acceptableDateForWaiting;
    TimePicker timePicker;
    TimePicker acceptableTimeForWaiting;
    CheckBox waitUntilStartDateTimeCheckbox;
    EditText paymentAndAdditionalInfoEditText;
    CheckBox isRequiredToCloseAllQuotasCheckbox;
    CheckBox saveTemplateCheckbox;
    EditText templateNameEditText;
    List<ProfessionName> professionNamesList;
    List<Vacancy> templatesList;
    VacancyInfoResponse vacancyInfoResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacancy_edit);
        setCurrentActivity(this);
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(this);
        linearLayout = findViewById(R.id.activity_vacancy_edit_layout);
        templatesSpinner = linearLayout.findViewById(R.id.templates);
        requiredProfessionSpinner = linearLayout.findViewById(R.id.required_profession);
        quotasNumberEditText = linearLayout.findViewById(R.id.quotas_number);
        professionNamesList = new ArrayList<>();
        generalApi.getProfessionsNamesInSpecifiedLanguage().enqueue(new Callback<ProfessionNamesListResponse>() {
            @Override
            public void onResponse(Call<ProfessionNamesListResponse> call, Response<ProfessionNamesListResponse> response) {
                professionNamesList.addAll(response.body().getList());
            }
            @Override
            public void onFailure(Call<ProfessionNamesListResponse> call, Throwable t) {
                Toast.makeText(EmployerVacancyEditActivity.this, "Error " +
                        "'getProfessionsNamesInSpecifiedLanguage' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<String> professionNamesAdapter = new ArrayAdapter
                (this, android.R.layout.simple_spinner_item, professionNamesList);
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
        datePicker = linearLayout.findViewById(R.id.start_date_picker);
        long timestamp = System.currentTimeMillis();
        LocalDate currentDate = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
        datePicker.init(currentDate.getYear(), currentDate.getMonthValue(), currentDate.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {}
                });
        timePicker = linearLayout.findViewById(R.id.start_time_picker);
        LocalTime currentTime = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalTime();
        timePicker.setIs24HourView(true);
        timePicker.setHour(currentTime.getHour());
        timePicker.setMinute(currentTime.getMinute());
        acceptableDateForWaiting = linearLayout.findViewById(R.id.waiting_date_picker);
        acceptableTimeForWaiting = linearLayout.findViewById(R.id.waiting_time_picker);
        paymentAndAdditionalInfoEditText = linearLayout.findViewById(R.id.payment_and_additional_info);
        waitUntilStartDateTimeCheckbox = linearLayout.findViewById(R.id.waiting_until_start_date_time_checkbox);
        isRequiredToCloseAllQuotasCheckbox = linearLayout.findViewById(R.id.is_required_to_close_all_quotas_checkbox);
        saveTemplateCheckbox = linearLayout.findViewById(R.id.save_template_checkbox);
        templateNameEditText = linearLayout.findViewById(R.id.template_name);
        if (arguments.getString("vacancy_id") != null) {
            String vacancyId = arguments.getString("vacancy_id");
            searchApi.getVacancyById(vacancyId).enqueue(new Callback<VacancyInfoResponse>() {
                @Override
                public void onResponse(Call<VacancyInfoResponse> call, Response<VacancyInfoResponse> response) {
                    vacancyInfoResponse = response.body();
                    int positionInSpinner = professionNamesAdapter.getPosition(vacancyInfoResponse.getProfessionName());
                    requiredProfessionSpinner.setSelection(positionInSpinner);
                    quotasNumberEditText.setText(vacancyInfoResponse.getQuotasNumber());
                    jobAddressButton.setText(vacancyInfoResponse.getJobAddress());
                    jobAddressLatitude = vacancyInfoResponse.getJobAddressLatitude();
                    jobAddressLongitude = vacancyInfoResponse.getJobAddressLongitude();
                    datePicker.init(vacancyInfoResponse.getLocalDateTime().getYear(),
                            vacancyInfoResponse.getLocalDateTime().getMonthValue(),
                            vacancyInfoResponse.getLocalDateTime().getDayOfMonth(),
                            new DatePicker.OnDateChangedListener() {
                                @Override
                                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {}
                            });
                    timePicker.setHour(vacancyInfoResponse.getLocalDateTime().getHour());
                    timePicker.setMinute(vacancyInfoResponse.getLocalDateTime().getMinute());
                    paymentAndAdditionalInfoEditText.setText(vacancyInfoResponse.getPaymentAndAdditionalInfo());
                    acceptableDateForWaiting.init(vacancyInfoResponse.getVacancyAvailability().getYear(),
                            vacancyInfoResponse.getVacancyAvailability().getMonthValue(),
                            vacancyInfoResponse.getVacancyAvailability().getDayOfMonth(),
                            new DatePicker.OnDateChangedListener() {
                                @Override
                                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {}
                            });
                    acceptableTimeForWaiting.setHour(vacancyInfoResponse.getVacancyAvailability().getHour());
                    acceptableTimeForWaiting.setMinute(vacancyInfoResponse.getVacancyAvailability().getMinute());
                }
                @Override
                public void onFailure(Call<VacancyInfoResponse> call, Throwable t) {
                    Toast.makeText(EmployerVacancyEditActivity.this, "Error " +
                            "'getVacancyById' method is failure", Toast.LENGTH_SHORT).show();
                }
            });
        }
        templatesList = new ArrayList<>();
        generalApi.getTemplatesList().enqueue(new Callback<VacancyListResponse>() {
            @Override
            public void onResponse(Call<VacancyListResponse> call, Response<VacancyListResponse> response) {
                templatesList.addAll(response.body().getList());
            }
            @Override
            public void onFailure(Call<VacancyListResponse> call, Throwable t) {
                Toast.makeText(EmployerVacancyEditActivity.this, "Error " +
                        "'getTemplatesList' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        List<String> templatesNames = templatesList.stream().map(Vacancy::getTemplateName).collect(Collectors.toList());
        templatesNames.add(getResources().getString(R.string.no_template));
        ArrayAdapter<String> templatesAdapter = new ArrayAdapter
                (this,android.R.layout.simple_spinner_item, templatesNames);
        templatesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        templatesSpinner.setAdapter(templatesAdapter);
        templatesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                for (Vacancy template : templatesList) {
                    if (template.getTemplateName().equals(selectedItem)) {
                        for (ProfessionName name : template.getProfession().getProfessionNames()) {
                            if (name.getLanguage().getLanguageEndonym()
                                    .equals(userInfoResponse.getEndonymInterfaceLanguage())) {
                                int position = templatesAdapter.getPosition(name.getProfessionName());
                                requiredProfessionSpinner.setSelection(position);
                            }
                        }
                        quotasNumberEditText.setText(template.getQuotasNumber());
                        jobAddressButton.setText(template.getJobLocation().getJobAddress());
                        jobAddressLatitude = template.getJobLocation().getLatitude();
                        jobAddressLongitude = template.getJobLocation().getLongitude();
                        paymentAndAdditionalInfoEditText.setText(template.getPaymentAndAdditionalInfo());
                        isRequiredToCloseAllQuotasCheckbox.setChecked(template.isNecessaryToCloseAllQuotas());
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
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
        if (arguments.getString("vacancy_id") != null) {
            searchApi.deleteVacancyById(arguments.getString("vacancy_id")).enqueue(new Callback<ResultErrorsResponse>() {
                @Override
                public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                    onBack(view);
                }
                @Override
                public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                    Toast.makeText(EmployerVacancyEditActivity.this, "Error " +
                            "'deleteVacancyById' method is failure", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onConfirmClick(View view) {
        VacancyRequest vacancyRequest = new VacancyRequest();
        String enteredJobAddress = jobAddressButton.getText().toString();
        if (isAddressValid(enteredJobAddress)) {
            if (arguments.getString("vacancy_id") != null) {
                vacancyRequest.setVacancyId(Long.parseLong(arguments.getString("vacancy_id")));
            }
            if (requiredProfessionSpinner.getSelectedItem() != null) {
                vacancyRequest.setProfessionName(requiredProfessionSpinner.getSelectedItem().toString());
            }
            LocalDateTime startDateTime = LocalDateTime.of(datePicker.getYear(), datePicker.getMonth(),
                    datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute());
            vacancyRequest.setStartTimestamp(startDateTime);
            if (waitUntilStartDateTimeCheckbox.isChecked()) {
                vacancyRequest.setWaitingTimestamp(startDateTime);
            } else {
                LocalDateTime acceptableWaitingTime = LocalDateTime.of(
                        acceptableDateForWaiting.getYear(),
                        acceptableDateForWaiting.getMonth(),
                        acceptableDateForWaiting.getDayOfMonth(),
                        acceptableTimeForWaiting.getHour(),
                        acceptableTimeForWaiting.getMinute());
                vacancyRequest.setWaitingTimestamp(acceptableWaitingTime);
            }
            String info = paymentAndAdditionalInfoEditText.getText().toString();
            vacancyRequest.setPaymentAndAdditionalInfo(info);
            vacancyRequest.setQuotasNumber(Integer.parseInt(quotasNumberEditText.getText().toString()));
            vacancyRequest.setJobAddress(enteredJobAddress);
            vacancyRequest.setLatitude(jobAddressLatitude);
            vacancyRequest.setLongitude(jobAddressLongitude);
            vacancyRequest.setEmployerId(userInfoResponse.getId());
            vacancyRequest.setEmployerName(userInfoResponse.getName());
            vacancyRequest.setRequiredToCloseAllQuotas(isRequiredToCloseAllQuotasCheckbox.isChecked());
            vacancyRequest.setSaveTemplate(saveTemplateCheckbox.isChecked());
            vacancyRequest.setTemplateName(templateNameEditText.getText().toString());
            searchApi.setVacancy(vacancyRequest).enqueue(new Callback<ResultErrorsResponse>() {
                @Override
                public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                    if (response.body().isResult()) {
                        onBack(view);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (String error : response.body().getErrors()) {
                            sb.append(error).append(" ");
                        }
                        Toast.makeText(EmployerVacancyEditActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                    Toast.makeText(EmployerVacancyEditActivity.this, "Error " +
                            "'setVacancy' method is failure", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(EmployerVacancyEditActivity.this,
                    getResources().getString(R.string.address_is_not_exist), Toast.LENGTH_SHORT).show();
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
