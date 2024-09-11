package com.example.navigatorappandroid;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.navigatorappandroid.retrofit.request.ChatRequest;
import com.example.navigatorappandroid.retrofit.request.VacancyRequest;
import com.example.navigatorappandroid.retrofit.response.ExtendedUserInfoResponse;
import com.example.navigatorappandroid.retrofit.response.VacancyInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployerExtendedInfoActivity extends BaseActivity {
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_info_employer);
        relativeLayout = findViewById(R.id.employer_info_layout);
        TextView vacancyProfession = relativeLayout.findViewById(R.id.profession);
        TextView vacancyJobLocation = relativeLayout.findViewById(R.id.job_location);
        TextView vacancyStartDateTime = relativeLayout.findViewById(R.id.start_date_time);
        TextView vacancyAvailability = relativeLayout.findViewById(R.id.vacancy_availability);
        TextView vacancyInfo = relativeLayout.findViewById(R.id.payment_and_additional_info);
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
        TextView name = relativeLayout.findViewById(R.id.name);
        Button rating = relativeLayout.findViewById(R.id.rating);
        ImageView avatar = relativeLayout.findViewById(R.id.avatar);
        TextView firmName = relativeLayout.findViewById(R.id.firm_name);
        TextView languages = relativeLayout.findViewById(R.id.languages);
        TextView email = relativeLayout.findViewById(R.id.email);
        TextView phone = relativeLayout.findViewById(R.id.phone);
        TextView socialNetworksLinks = relativeLayout.findViewById(R.id.social_networks_links);
        searchApi.getEmployerInfo(arguments.getString("employer_id")).enqueue(new Callback<ExtendedUserInfoResponse>() {
            @Override
            public void onResponse(Call<ExtendedUserInfoResponse> call, Response<ExtendedUserInfoResponse> response) {
                name.setText(response.body().getName());
                rating.setText(response.body().getRating());
                languages.setText(response.body().getLanguages());
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
                socialNetworksLinks.setText(response.body().getSocialNetworksLinks());
                if (response.body().getFirmName() != null) {
                    firmName.setText(response.body().getFirmName());
                }
                byte[] decodedBytes = Base64.decode(response.body().getAvatar(), Base64.DEFAULT);
                avatar.setImageBitmap(BitmapFactory.decodeByteArray
                        (decodedBytes, 0 , decodedBytes.length));
            }

            @Override
            public void onFailure(Call<ExtendedUserInfoResponse> call, Throwable t) {
                Toast.makeText(EmployerExtendedInfoActivity.this, "Error " +
                        "'getEmployerInfo' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setEmployeeId(userInfoResponse.getId());
        chatRequest.setEmployerId(Long.parseLong(arguments.getString("employer_id")));
        if (arguments.getString("vacancy_id") != null) {
            Button button = findViewById(R.id.give_offer);
            button.setVisibility(View.VISIBLE);
            button.setClickable(true);
        }
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
        Intent intent;
        if (userInfoResponse.getCurrentWorkDisplay() == 1) {
            intent = new Intent(this, WorkMapEmployeeActivity.class);
            startActivity(intent);
        }
        intent = new Intent(this, WorkListEmployeeActivity.class);
        startActivity(intent);
    }
}
