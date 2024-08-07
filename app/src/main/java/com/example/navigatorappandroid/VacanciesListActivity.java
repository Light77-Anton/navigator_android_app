package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.model.InfoAboutVacancyFromEmployer;
import com.example.navigatorappandroid.model.Language;
import com.example.navigatorappandroid.model.User;
import com.example.navigatorappandroid.model.Vacancy;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.request.ProfessionToUserRequest;
import com.example.navigatorappandroid.retrofit.request.VacancyRequest;
import com.example.navigatorappandroid.retrofit.response.StringResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VacanciesListActivity  extends AppCompatActivity {

    private RetrofitService retrofitService;
    private GeneralApi generalApi;
    private UserInfoResponse userInfoResponse;
    private View entireView;
    private LinearLayout linearLayout;
    Bundle arguments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arguments = getIntent().getExtras();
        retrofitService = new RetrofitService();
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                userInfoResponse = response.body();
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(VacanciesListActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        entireView = getLayoutInflater().inflate(R.layout.activity_vacancies_list, null);
        linearLayout = entireView.findViewById(R.id.activity_vacancies_list_layout);
        for (Vacancy vacancy : userInfoResponse.getEmployerRequests().getVacancies()) {
            ProfessionToUserRequest professionToUserRequest = new ProfessionToUserRequest();
            professionToUserRequest.setId(vacancy.getProfession().getId());
            generalApi.getProfessionNameInSpecifiedLanguage(professionToUserRequest).enqueue(new Callback<StringResponse>() {
                @Override
                public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                    Button button = new Button(linearLayout.getContext());
                    button.setText(response.body().getString() + ", " + vacancy.getJobLocation().getJobAddress()
                    + ", " + vacancy.getStartDateTime().toString());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 20, 0, 0);
                    button.setPadding(15, 15, 15, 15);
                    button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                    button.setLayoutParams(layoutParams);
                    button.setBackgroundResource(R.drawable.rectangle_button);
                    button.setTextColor(getResources().getColor(R.color.white));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            VacancyRequest vacancyRequest = new VacancyRequest();
                            vacancyRequest.setProfessionName(response.body().getString());
                            vacancyRequest.setJobAddress(vacancy.getJobLocation().getJobAddress());
                            vacancyRequest.setStartTimestamp(vacancy.getStartDateTime());
                            generalApi.getRecipient(Long.parseLong(arguments.getString(arguments.getString("id")))).enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    for (Language userLanguage : response.body().getCommunicationLanguages()) {
                                        for (InfoAboutVacancyFromEmployer info : vacancy.getPaymentAndAdditionalInfo()) {
                                            if (info.getLanguage().getLanguageEndonym().
                                                    equals(userLanguage.getLanguageEndonym())) {
                                                vacancyRequest.setPaymentAndAdditionalInfo(info.getText());
                                            }
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Toast.makeText(VacanciesListActivity.this, "fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent intent = new Intent(v.getContext(), SettingWaitingDateTimeActivity.class);
                            intent.putExtras(arguments);
                            intent.putExtra("vacancy_request", vacancyRequest);
                            startActivity(intent);
                        }
                    });
                    linearLayout.addView(button);
                }

                @Override
                public void onFailure(Call<StringResponse> call, Throwable t) {
                    Toast.makeText(VacanciesListActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
        setContentView(R.layout.activity_vacancies_list);
    }

    public void onBack(View view) {
        Intent intent = new Intent(this, OfferSendingChooseActivity.class);
        intent.putExtras(arguments);
        startActivity(intent);
    }
}
