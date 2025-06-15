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
import com.example.navigatorappandroid.retrofit.response.ExtendedUserInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeExtendedInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_info_employee);
        setCurrentActivity(this);
        if (arguments.getBoolean("is_offer_sent")) {
            Toast.makeText(EmployeeExtendedInfoActivity.this,
                    getResources().getString(R.string.offer_has_been_sent), Toast.LENGTH_SHORT).show();
        }
        TextView name = findViewById(R.id.name);
        Button rating = findViewById(R.id.rating);
        ImageView avatar = findViewById(R.id.avatar);
        TextView status = findViewById(R.id.status);
        TextView professions = findViewById(R.id.professions);
        TextView professionsInfoFromEmployee = findViewById(R.id.professions_info_from_employee);
        TextView languages = findViewById(R.id.languages);
        TextView email = findViewById(R.id.email);
        TextView phone = findViewById(R.id.phone);
        TextView socialNetworksLinks = findViewById(R.id.social_networks_links);
        searchApi.getEmployeeInfo(Long.parseLong(arguments.getString("employee_id"))).enqueue(new Callback<ExtendedUserInfoResponse>() {
            @Override
            public void onResponse(Call<ExtendedUserInfoResponse> call, Response<ExtendedUserInfoResponse> response) {
                ExtendedUserInfoResponse eui = response.body();
                name.setText(eui.getName());
                rating.setText(getResources().getString(R.string.rating) + eui.getRating());
                if (eui.getAvatar() != null) {
                    byte[] decodedBytes = Base64.decode(eui.getAvatar(), Base64.DEFAULT);
                    avatar.setImageBitmap(BitmapFactory.decodeByteArray(decodedBytes, 0 , decodedBytes.length));
                } else {
                    avatar.setImageDrawable(getResources().getDrawable(R.drawable.default_user_avatar));
                }
                status.setText(getResources().getString(R.string.employee_status) + eui.getStatus());
                professions.setText(getResources().getString(R.string.professions) + eui.getProfessions());
                professionsInfoFromEmployee.setText(
                        getResources().getString(R.string.professions_info_from_employee)
                                + eui.getInfoFromEmployee());
                languages.setText(getResources().getString(R.string.communication_languages) + eui.getLanguages());
                if (response.body().getEmail().equals("hidden")) {
                    email.setText(getResources().getString(R.string.email_view) + getResources().getString(R.string.hidden));
                } else {
                    email.setText(getResources().getString(R.string.email_view) + eui.getEmail());
                }
                if (response.body().getPhone().equals("hidden")) {
                    phone.setText(getResources().getString(R.string.phone) + getResources().getString(R.string.hidden));
                } else {
                    phone.setText(getResources().getString(R.string.phone) + eui.getPhone());
                }
                socialNetworksLinks.setText(getResources().getString(R.string.social_networks_links)
                        + eui.getSocialNetworksLinks());
            }
            @Override
            public void onFailure(Call<ExtendedUserInfoResponse> call, Throwable t) {
                Toast.makeText(EmployeeExtendedInfoActivity.this, "Error " +
                        "'getEmployeeInfo' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onRatingClick(View view) {
        addActivityToQueue(getCurrentActivity());
        Intent intent = new Intent(this, CommentsListActivity.class);
        intent.putExtras(arguments);
        intent.putExtra("is_offer_sent", false);
        finish();
        startActivity(intent);
    }

    public void onSendOffer(View view) {
        addActivityToQueue(getCurrentActivity());
        Intent intent = new Intent(this, OfferSendingChooseActivity.class);
        intent.putExtras(arguments);
        intent.putExtra("is_offer_sent", false);
        finish();
        startActivity(intent);
    }

    public void onBack(View view) {
        Activity lastActivity = getLastActivity();
        if (lastActivity != null) {
            Intent intent = new Intent(this, lastActivity.getClass());
            intent.putExtras(arguments);
            intent.putExtra("is_offer_sent", false);
            removeActivityFromQueue();
            finish();
            startActivity(intent);
        }
    }
}
