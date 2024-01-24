package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.handler.LocationUpdateHandler;
import com.example.navigatorappandroid.model.Language;
import com.example.navigatorappandroid.model.User;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.request.LocationsRequest;
import com.example.navigatorappandroid.retrofit.request.ProfessionToUserRequest;
import com.example.navigatorappandroid.retrofit.request.SearchRequest;
import com.example.navigatorappandroid.retrofit.response.DistanceResponse;
import com.example.navigatorappandroid.retrofit.response.SearchResponse;
import com.example.navigatorappandroid.retrofit.response.StringResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkListEmployerActivity extends AppCompatActivity {

    private LocationUpdateHandler locationUpdateHandler;

    private LinearLayout linearLayout = findViewById(R.id.work_list_employer_sort_request_layout);
    private LinearLayout searchResultsLayout = findViewById(R.id.work_list_employer_search_results_layout);
    private RetrofitService retrofitService;
    private GeneralApi generalApi;
    private SearchApi searchApi;
    private UserInfoResponse userInfoResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_list_employer);
        changeSortRequestFieldCondition();
        retrofitService = new RetrofitService();
        searchApi = retrofitService.getRetrofit().create(SearchApi.class);
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                userInfoResponse = response.body();
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(WorkListEmployerActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        Bundle arguments = getIntent().getExtras();
        if (arguments.get("profession") != null) {
            executeSearchForEmployees(arguments.getString("profession"), searchApi);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationUpdateHandler.startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationUpdateHandler.stopLocationUpdates();
    }

    private void enableLinearLayout() {
        linearLayout.setEnabled(true);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            child.setEnabled(true);
        }
    }

    private void disableLinearLayout() {
        linearLayout.setEnabled(false);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            child.setEnabled(false);
        }
    }

    private void changeSortRequestFieldCondition() {
        if (linearLayout.isEnabled()) {
            disableLinearLayout();
        } else {
            enableLinearLayout();
        }
    }

    public void onSettingsClick(View view) {
        Intent intent = new Intent(view.getContext(), EmployerSettingsActivity.class);
        intent.putExtra("firm_name", userInfoResponse.getEmployerRequests().getFirmName());
        intent.putExtra("avatar", userInfoResponse.getAvatar());
        intent.putExtra("name", userInfoResponse.getName());
        intent.putExtra("phone", userInfoResponse.getPhone());
        intent.putExtra("social_networks_links", userInfoResponse.getSocialNetworksLinks());
        intent.putExtra("interface_language", userInfoResponse.getEndonymInterfaceLanguage());
        intent.putExtra("communication_language", userInfoResponse.getCommunicationLanguages());
        intent.putExtra("are_languages_matched", userInfoResponse.isAreLanguagesMatched());
        intent.putExtra("limit_in_the_search", userInfoResponse.getLimitForTheSearch());
        intent.putExtra("activity", "list");
        startActivity(intent);
    }

    public void onSearchClick(View view) {
        Intent intent = new Intent(this, SearchEmployeesActivity.class);
        intent.putExtra("activity", "list");
        startActivity(intent);
    }

    public void onVacanciesSettingClick(View view) {
        Intent intent = new Intent(this, EmployerVacanciesSettingActivity.class);
        intent.putExtra("activity", "list");
        startActivity(intent);
    }

    private void executeSearchForEmployees(String profession, SearchApi searchApi) {
        searchResultsLayout.removeAllViews();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setProfessionName(profession);
        RadioGroup radioGroup = linearLayout.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener((radiogroup, id)-> {
            RadioButton radio = findViewById(id);
            switch (radio.getText().toString()) {
                case "Sort by person\\'s name":
                    searchRequest.setSortType("name");
                    break;
                case "Sort by reputation":
                    searchRequest.setSortType("rating");
                    break;
                case "Sort by location":
                    searchRequest.setSortType("location");
                    break;
                default:
                    searchRequest.setSortType("");
                    break;
            }
        });
        SeekBar seekBar = linearLayout.findViewById(R.id.seekbar);
        searchRequest.setInRadiusOf(seekBar.getProgress());
        CheckBox checkBox = linearLayout.findViewById(R.id.is_auto_checkbox);
        searchRequest.setAuto(checkBox.isChecked());
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
                            Toast.makeText(WorkListEmployerActivity.this, "fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(WorkListEmployerActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addEmployeeButton(User employee, double distance) {
        String name = employee.getName();
        String avatar = employee.getAvatar();
        double rating = employee.getRanking();
        String status;
        if (employee.getEmployeeData().getStatus().equals("custom")) {
            status = "Status: will active since " + employee.getEmployeeData().getActiveStatusStartDate();
        } else {
            status = "Status: " + employee.getEmployeeData().getStatus();
        }
        ProfessionToUserRequest professionToUserRequest = new ProfessionToUserRequest();
        professionToUserRequest.setId(employee.getId());
        Button button = new Button(searchResultsLayout.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(layoutParams);
        button.setBackground(getResources().getDrawable(R.drawable.rectangle_button));
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
                Intent intent = new Intent(v.getContext(), EmployeeExtendedInfoActivity.class);
                intent.putExtra("activity", "list");
                intent.putExtra("name", name);
                intent.putExtra("rating", rating);
                intent.putExtra("avatar", avatar);
                intent.putExtra("status", status);
                StringBuilder languagesList = new StringBuilder();
                for (Language employeeLang : employee.getCommunicationLanguages()) {
                    languagesList.append(employeeLang.getLanguageEndonym() + ",");
                }
                languagesList.deleteCharAt(languagesList.length() - 1);
                intent.putExtra("languages", languagesList.toString());
                generalApi.getProfessionsToUserInEmployersLanguage(professionToUserRequest).enqueue(new Callback<StringResponse>() {
                    @Override
                    public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                        String professions = response.body().getString();
                        intent.putExtra("professions", professions);
                    }

                    @Override
                    public void onFailure(Call<StringResponse> call, Throwable t) {
                        Toast.makeText(WorkListEmployerActivity.this, "fail", Toast.LENGTH_SHORT).show();
                    }
                });
                generalApi.getInfoFromEmployeeInEmployersLanguage(professionToUserRequest).enqueue(new Callback<StringResponse>() {
                    @Override
                    public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                        String info = response.body().getString();
                        intent.putExtra("additional_info", info);
                    }

                    @Override
                    public void onFailure(Call<StringResponse> call, Throwable t) {
                        Toast.makeText(WorkListEmployerActivity.this, "fail", Toast.LENGTH_SHORT).show();
                    }
                });
                intent.putExtra("email", employee.getEmail());
                intent.putExtra("phone", employee.getPhone());
                intent.putExtra("social_networks_links", employee.getSocialNetworksLinks());
                startActivity(intent);
            }
        });
        searchResultsLayout.addView(button);
    }
}
