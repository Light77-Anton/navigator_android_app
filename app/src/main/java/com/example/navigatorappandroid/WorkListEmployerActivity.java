package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.example.navigatorappandroid.model.User;
import com.example.navigatorappandroid.retrofit.request.LocationsRequest;
import com.example.navigatorappandroid.retrofit.request.SearchRequest;
import com.example.navigatorappandroid.retrofit.response.DistanceResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.SearchResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkListEmployerActivity extends MainDisplayActivity {

    private HashMap<String, View> viewMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_list_employer);
        setCurrentActivity(this);
        sortRequestButton = findViewById(R.id.sort_request);
        filterRequestButton = findViewById(R.id.filters_request);
        searchSettingsLayout = findViewById(R.id.work_list_employer_sort_request_layout);
        searchResultsLayout = findViewById(R.id.work_list_employer_search_results_layout);
        changeSortRequestFieldCondition();
        if (arguments != null && arguments.get("profession") != null) {
            executeSearchForEmployees(arguments.getString("profession"),
                    arguments.getStringArray("languages_array"));
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
            CheckBox isAutoCheckbox = new CheckBox(searchResultsLayout.getContext());
            LinearLayout.LayoutParams checkboxParams = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 50, view.getResources().getDisplayMetrics()));
            isAutoCheckbox.setLayoutParams(checkboxParams);
            isAutoCheckbox.setText(R.string.is_auto);
            isAutoCheckbox.setBackground(ContextCompat.getDrawable(this, R.color.custom_gray_blue));
            isAutoCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    executeSearchForEmployees(arguments.getString("profession"),
                            arguments.getStringArray("languages_array"));
                }
            });
            viewMap.put("is_auto", isAutoCheckbox);
            searchSettingsLayout.addView(isAutoCheckbox);
            CheckBox showTemporarilyInactiveCheckbox = new CheckBox(searchResultsLayout.getContext());
            showTemporarilyInactiveCheckbox.setLayoutParams(checkboxParams);
            showTemporarilyInactiveCheckbox.setText(R.string.show_temporarily_inactive_employees);
            showTemporarilyInactiveCheckbox.setBackground(ContextCompat.getDrawable(this, R.color.custom_gray_blue));
            showTemporarilyInactiveCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    executeSearchForEmployees(arguments.getString("profession"),
                            arguments.getStringArray("languages_array"));
                }
            });
            viewMap.put("show_temporarily_inactive", showTemporarilyInactiveCheckbox);
            searchSettingsLayout.addView(showTemporarilyInactiveCheckbox);
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

    public void onSettingsClick(View view) {
        addActivityToQueue(getCurrentActivity());
        Intent intent = new Intent(view.getContext(), EmployerSettingsActivity.class);
        finish();
        startActivity(intent);
    }

    public void onSearchClick(View view) {
        addActivityToQueue(getCurrentActivity());
        Intent intent = new Intent(this, SearchEmployeesActivity.class);
        finish();
        startActivity(intent);
    }

    public void onVacanciesSettingClick(View view) {
        addActivityToQueue(getCurrentActivity());
        Intent intent = new Intent(this, EmployerVacanciesSettingActivity.class);
        finish();
        startActivity(intent);
    }

    public void onMapClick(View view) {
        generalApi.changeWorkDisplay().enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                Intent intent = new Intent(view.getContext(), WorkMapEmployerActivity.class);
                finish();
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(WorkListEmployerActivity.this, "Error " +
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

    private void executeSearchForEmployees(String profession, String[] additionalLanguages) {
        searchResultsLayout.removeAllViews();
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
        CheckBox autoCheckBox = (CheckBox) viewMap.get("is_auto");
        searchRequest.setAuto(autoCheckBox.isChecked());
        searchRequest.setMultivacancyAllowed(userInfoResponse.getEmployerRequests().isMultivacancyAllowedInSearch());
        CheckBox showTemporarilyInactiveEmployeescheckBox = (CheckBox) viewMap.get("show_temporarily_inactive");
        searchRequest.setShowTemporarilyInactiveEmployees(showTemporarilyInactiveEmployeescheckBox.isChecked());
        searchRequest.setLimit(userInfoResponse.getLimitForTheSearch());
        searchRequest.setAreLanguagesMatch(userInfoResponse.isAreLanguagesMatched());
        searchApi.getEmployeesOfChosenProfession(searchRequest).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                List<User> employees = response.body().getEmployeeList();
                for (User employee : employees) {
                    LocationsRequest locationsRequest = new LocationsRequest();
                    locationsRequest.setLat1(userInfoResponse.getLocation().getLatitude());
                    locationsRequest.setLong1(userInfoResponse.getLocation().getLongitude());
                    locationsRequest.setLat2(employee.getLocation().getLatitude());
                    locationsRequest.setLong2(employee.getLocation().getLongitude());
                    searchApi.getMeasuredDistance(locationsRequest).enqueue(new Callback<DistanceResponse>() {
                        @Override
                        public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {
                            addEmployeeButton(employee, response.body().getDistance());
                        }
                        @Override
                        public void onFailure(Call<DistanceResponse> call, Throwable t) {
                            Toast.makeText(WorkListEmployerActivity.this, "Error: " +
                                    "'getMeasuredDistance' method is failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(WorkListEmployerActivity.this, "Error " +
                        "'getEmployeesOfChosenProfession' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addEmployeeButton(User employee, double distance) {
        long employeeId = employee.getId();
        String name = employee.getName();
        byte rating = employee.getRanking();
        Button button = new Button(searchResultsLayout.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(layoutParams);
        button.setBackground(ContextCompat.getDrawable(this, R.drawable.rectangle_button));
        button.setTextColor(getResources().getColor(R.color.black));
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        String status;
        if (employee.getEmployeeData().getStatus() == 2) {
            status = getString(R.string.employee_status_custom) + Instant.ofEpochMilli
                            (employee.getEmployeeData().getActiveStatusStartDate())
                    .atZone(ZoneId.systemDefault()).toLocalDate().toString();
        } else {
            status = getString(R.string.employee_status) + employee.getEmployeeData().getStatus();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(status);
        sb.append("   ");
        sb.append(name);
        sb.append("   ");
        sb.append(rating);
        sb.append("   ");
        sb.append(distance);
        button.setText(sb);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addActivityToQueue(getCurrentActivity());
                Intent intent = new Intent(v.getContext(), EmployeeExtendedInfoActivity.class);
                intent.putExtra("employee_id", employeeId);
                finish();
                startActivity(intent);
            }
        });
        searchResultsLayout.addView(button);
    }
}
