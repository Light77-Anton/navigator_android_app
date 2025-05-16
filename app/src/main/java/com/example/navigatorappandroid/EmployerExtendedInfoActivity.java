package com.example.navigatorappandroid;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.navigatorappandroid.retrofit.request.VacancyRequest;
import com.example.navigatorappandroid.retrofit.response.ExtendedUserInfoResponse;
import com.example.navigatorappandroid.retrofit.response.VacancyInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployerExtendedInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_info_employer);
        setCurrentActivity(this);
        TextView vacancyProfession = findViewById(R.id.profession);
        TextView vacancyJobLocation = findViewById(R.id.job_location);
        TextView vacancyStartDateTime = findViewById(R.id.start_date_time);
        TextView vacancyAvailability = findViewById(R.id.vacancy_availability);
        TextView vacancyInfo = findViewById(R.id.payment_and_additional_info);
        searchApi.getVacancyById(arguments.getString("vacancy_id")).enqueue(new Callback<VacancyInfoResponse>() {
            @Override
            public void onResponse(Call<VacancyInfoResponse> call, Response<VacancyInfoResponse> response) {
                vacancyProfession.setText(response.body().getProfessionName());
                vacancyJobLocation.setText(response.body().getJobAddress());
                vacancyStartDateTime.setText(response.body().getLocalDateTime().toString());
                vacancyAvailability.setText(response.body().getVacancyAvailability().toString());
                vacancyInfo.setText(response.body().getPaymentAndAdditionalInfo());
            }
            @Override
            public void onFailure(Call<VacancyInfoResponse> call, Throwable t) {
                Toast.makeText(EmployerExtendedInfoActivity.this, "Error " +
                        "'getVacancyById' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        TextView name = findViewById(R.id.name);
        Button rating = findViewById(R.id.rating);
        ImageView avatar = findViewById(R.id.avatar);
        TextView firmName = findViewById(R.id.firm_name);
        TextView languages = findViewById(R.id.languages);
        TextView email = findViewById(R.id.email);
        TextView phone = findViewById(R.id.phone);
        TextView socialNetworksLinks = findViewById(R.id.social_networks_links);
        searchApi.getEmployerInfo(arguments.getString("employer_id")).enqueue(new Callback<ExtendedUserInfoResponse>() {
            @Override
            public void onResponse(Call<ExtendedUserInfoResponse> call, Response<ExtendedUserInfoResponse> response) {
                ExtendedUserInfoResponse eui = response.body();
                name.setText(eui.getName());
                rating.setText(getResources().getString(R.string.rating) + eui.getRating());
                languages.setText(getResources().getString(R.string.communication_languages) + eui.getLanguages());
                if (response.body().getEmail().equals("hidden")) {
                    email.setText(getResources().getString(R.string.hidden));
                } else {
                    email.setText(response.body().getEmail());
                }
                if (response.body().getPhone().equals("hidden")) {
                    email.setText(getResources().getString(R.string.hidden));
                } else {
                    phone.setText(response.body().getPhone());
                }
                socialNetworksLinks.setText(getResources().getString(R.string.social_networks_links)
                        + eui.getSocialNetworksLinks());
                if (response.body().getFirmName() != null) {
                    firmName.setText(getResources().getString(R.string.firm) + eui.getFirmName());
                }
                if (eui.getAvatar() != null) {
                    byte[] decodedBytes = Base64.decode(eui.getAvatar(), Base64.DEFAULT);
                    avatar.setImageBitmap(BitmapFactory.decodeByteArray(decodedBytes, 0 , decodedBytes.length));
                } else {
                    avatar.setImageDrawable(getResources().getDrawable(R.drawable.default_user_avatar));
                }
            }
            @Override
            public void onFailure(Call<ExtendedUserInfoResponse> call, Throwable t) {
                Toast.makeText(EmployerExtendedInfoActivity.this, "Error " +
                        "'getEmployerInfo' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onRatingClick(View view) {
        addActivityToQueue(getCurrentActivity());
        Intent intent = new Intent(this, CommentsListActivity.class);
        intent.putExtras(arguments);
        finish();
        startActivity(intent);
    }

    public void onSendOffer(View view) {
        VacancyRequest vacancyRequest = new VacancyRequest();
        vacancyRequest.setVacancyId(Long.parseLong(arguments.getString("vacancy_id")));
        chatApi.sendEmployeesOffer(arguments.getString("employer_id"), vacancyRequest).enqueue(new Callback<ExtendedUserInfoResponse>() {
            @Override
            public void onResponse(Call<ExtendedUserInfoResponse> call, Response<ExtendedUserInfoResponse> response) {
                Toast.makeText(EmployerExtendedInfoActivity.this,
                        getResources().getString(R.string.offer_has_been_sent), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ExtendedUserInfoResponse> call, Throwable t) {
                Toast.makeText(EmployerExtendedInfoActivity.this, "Error " +
                        "'sendEmployeesOffer' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
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
