package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.example.navigatorappandroid.handler.EmployeeStatusHandler;
import com.example.navigatorappandroid.model.User;
import com.example.navigatorappandroid.model.Vacancy;
import com.example.navigatorappandroid.retrofit.request.LocationsRequest;
import com.example.navigatorappandroid.retrofit.request.SearchRequest;
import com.example.navigatorappandroid.retrofit.response.DistanceResponse;
import com.example.navigatorappandroid.retrofit.response.IdResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.SearchResponse;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkListEmployeeActivity extends MainDisplayActivity {

    private EmployeeStatusHandler employeeStatusHandler;
    private HashMap<String, View> viewMap;
    private Button statusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_list_employee);
        setCurrentActivity(this);
        sortRequestButton = findViewById(R.id.sort_request);
        filterRequestButton = findViewById(R.id.filters_request);
        searchSettingsLayout = findViewById(R.id.work_list_employee_sort_request_layout);
        searchResultsLayout = findViewById(R.id.work_list_employee_search_results_layout);
        changeSortRequestFieldCondition();
        if (arguments != null && arguments.get("profession") != null) {
            executeSearchForVacancies(arguments.getString("profession"),
                    arguments.getString("additional_language"));
        }
        if (userInfoResponse.getEmployeeData().getStatus() == 2) {
            employeeStatusHandler = new EmployeeStatusHandler();
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
                                , arguments.getString("additional_language"));
                    }
                });
                for (int i = 0; i < 3; i++) {
                    RadioButton radioButton = new RadioButton(searchResultsLayout.getContext());
                    radioButton.setId(View.generateViewId());
                    radioButton.setBackground(ContextCompat.getDrawable(this, R.color.custom_gray_blue));
                    LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP, 50, view.getResources().getDisplayMetrics()));
                    radioButton.setLayoutParams(buttonParams);
                    switch (i) {
                        case 1:
                            viewMap.put("rating", radioButton);
                            radioButton.setText(getResources().getString(R.string.sort_by_reputation));
                            radioButton.setContentDescription("rating");
                            break;
                        case 2:
                            viewMap.put("location", radioButton);
                            radioButton.setText(getResources().getString(R.string.sort_by_location));
                            radioButton.setContentDescription("location");
                            break;
                        default:
                            viewMap.put("name", radioButton);
                            radioButton.setText(getResources().getString(R.string.sort_by_persons_name));
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
                                arguments.getString("additional_language"));
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

    public void onSettingsClick(View view) {
        addActivityToQueue(getCurrentActivity());
        Intent intent = new Intent(view.getContext(), EmployeeSettingsActivity.class);
        finish();
        startActivity(intent);
    }

    public void onSearchClick(View view) {
        addActivityToQueue(getCurrentActivity());
        Intent intent = new Intent(this, SearchVacanciesActivity.class);
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

    public void onStatusClick(View view) {
        addActivityToQueue(getCurrentActivity());
        Intent intent = new Intent(this, EmployeeStatusActivity.class);
        finish();
        startActivity(intent);
    }

    public void onMapClick(View view) {
        generalApi.changeWorkDisplay().enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                Intent intent = new Intent(view.getContext(), WorkMapEmployeeActivity.class);
                finish();
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(WorkListEmployeeActivity.this, "Error " +
                        "'changeWorkDisplay' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onTimersClick(View view) {
        addActivityToQueue(getCurrentActivity());
        Intent intent = new Intent(this, TimersListActivity.class);
        finish();
        startActivity(intent);
    }

    public void onChatsClick(View view) {
        addActivityToQueue(getCurrentActivity());
        Intent intent = new Intent(this, ChatListActivity.class);
        finish();
        startActivity(intent);
    }

    public void onAddLanguagesClick(View view) {
        addActivityToQueue(getCurrentActivity());
        Intent intent = new Intent(this, ChooseAdditionalLanguagesActivity.class);
        finish();
        startActivity(intent);
    }

    private void executeSearchForVacancies(String profession, String additionalLanguage) {
        searchResultsLayout.removeAllViews();
        SearchRequest searchRequest = new SearchRequest();
        generalApi.getProfessionIdByName(profession).enqueue(new Callback<IdResponse>() {
            @Override
            public void onResponse(Call<IdResponse> call, Response<IdResponse> response) {
                searchRequest.setProfessionId(response.body().getId());
            }
            @Override
            public void onFailure(Call<IdResponse> call, Throwable t) {
                Toast.makeText(WorkListEmployeeActivity.this, "Error " +
                        "'getProfessionIdByName' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        generalApi.getLanguageIdByName(additionalLanguage).enqueue(new Callback<IdResponse>() {
            @Override
            public void onResponse(Call<IdResponse> call, Response<IdResponse> response) {
                searchRequest.setAdditionalLanguageId(response.body().getId());
            }
            @Override
            public void onFailure(Call<IdResponse> call, Throwable t) {
                Toast.makeText(WorkListEmployeeActivity.this, "Error " +
                        "'getLanguageIdByName' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
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
        searchRequest.setAreLanguagesMatched(userInfoResponse.isAreLanguagesMatched());
        searchApi.getVacanciesByProfession(searchRequest).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                List<Vacancy> vacancies = response.body().getVacancyList();
                for (Vacancy vacancy : vacancies) {
                    User employer = vacancy.getEmployerRequests().getEmployer();
                    LocationsRequest locationsRequest = new LocationsRequest();
                    locationsRequest.setLat1(userInfoResponse.getLocation().getLatitude());
                    locationsRequest.setLong1(userInfoResponse.getLocation().getLongitude());
                    locationsRequest.setLat2(vacancy.getJobLocation().getLatitude());
                    locationsRequest.setLong2(vacancy.getJobLocation().getLongitude());
                    searchApi.getMeasuredDistance(locationsRequest).enqueue(new Callback<DistanceResponse>() {
                        @Override
                        public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {
                            addVacancyButton(employer, vacancy, response.body().getDistance());
                        }
                        @Override
                        public void onFailure(Call<DistanceResponse> call, Throwable t) {
                            Toast.makeText(WorkListEmployeeActivity.this, "Error " +
                                    "'getMeasuredDistance' method is failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(WorkListEmployeeActivity.this, "Error " +
                        "'getVacanciesByProfession' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addVacancyButton(User employer, Vacancy vacancy, double distance) {
        String employerId = employer.getId().toString();
        String vacancyId = vacancy.getId().toString();
        String name = employer.getName();
        byte rating = employer.getRanking();
        Button button = new Button(searchResultsLayout.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(layoutParams);
        button.setBackground(ContextCompat.getDrawable(this, R.drawable.rectangle_button));
        button.setTextColor(getResources().getColor(R.color.black));
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("   ");
        sb.append(rating);
        sb.append("   ");
        sb.append(distance);
        button.setText(sb);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addActivityToQueue(getCurrentActivity());
                Intent intent = new Intent(v.getContext(), EmployerExtendedInfoActivity.class);
                intent.putExtra("employer_id", employerId);
                intent.putExtra("vacancy_id", vacancyId);
                finish();
                startActivity(intent);
            }
        });
        searchResultsLayout.addView(button);
    }
}
