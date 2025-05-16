package com.example.navigatorappandroid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.navigatorappandroid.model.Vacancy;
import com.example.navigatorappandroid.retrofit.request.VacancyRequest;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.StringResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VacanciesListActivity extends BaseActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacancies_list);
        setCurrentActivity(this);
        linearLayout = findViewById(R.id.activity_vacancies_list_layout);
        for (Vacancy vacancy : userInfoResponse.getEmployerRequests().getVacancies()) {
            generalApi.getProfessionNameInSpecifiedLanguage(vacancy.getProfession().getId()).enqueue(new Callback<StringResponse>() {
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
                            vacancyRequest.setPaymentAndAdditionalInfo(vacancy.getPaymentAndAdditionalInfo());
                            vacancyRequest.setWaitingTimestamp(vacancy.getWaitingDateTime());
                            chatApi.sendOfferFromEmployer(arguments.getString("employee_id"), vacancyRequest).enqueue(new Callback<ResultErrorsResponse>() {
                                @Override
                                public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                                    if (response.body().isResult()) {
                                        onBack(true);
                                    } else {
                                        Toast.makeText(VacanciesListActivity.this,
                                                response.body().getErrors().get(0), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                                    Toast.makeText(VacanciesListActivity.this, "Error " +
                                            "'sendOfferFromEmployer' method is failure", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    linearLayout.addView(button);
                }
                @Override
                public void onFailure(Call<StringResponse> call, Throwable t) {
                    Toast.makeText(VacanciesListActivity.this, "Error " +
                            "'getProfessionNameInSpecifiedLanguage' method is failure", Toast.LENGTH_SHORT).show();
                }
            });
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
}
