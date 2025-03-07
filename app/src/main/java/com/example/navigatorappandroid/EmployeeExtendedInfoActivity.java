package com.example.navigatorappandroid;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.navigatorappandroid.model.Vote;
import com.example.navigatorappandroid.retrofit.ChatApi;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.request.ChatRequest;
import com.example.navigatorappandroid.retrofit.request.ProfessionToUserRequest;
import com.example.navigatorappandroid.retrofit.response.ExtendedUserInfoResponse;
import com.example.navigatorappandroid.retrofit.response.ProfessionNamesListResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.StringResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeExtendedInfoActivity extends BaseActivity {

    private List<Vote> votesToUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_info_employee);
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
        searchApi.getEmployeeInfo(arguments.getString("employee_id")).enqueue(new Callback<ExtendedUserInfoResponse>() {
            @Override
            public void onResponse(Call<ExtendedUserInfoResponse> call, Response<ExtendedUserInfoResponse> response) {
                ExtendedUserInfoResponse eui = response.body();
                name.setText(eui.getName());
                rating.setText(getResources().getString(R.string.rating) + eui.getRating());
                byte[] decodedBytes = Base64.decode(eui.getAvatar(), Base64.DEFAULT);
                avatar.setImageBitmap(BitmapFactory.decodeByteArray(decodedBytes, 0 , decodedBytes.length));
                status.setText(getResources().getString(R.string.employee_status) + eui.getStatus());
                professions.setText(getResources().getString(R.string.professions) + eui.getProfessions());
                professionsInfoFromEmployee.setText(
                        getResources().getString(R.string.professions_info_from_employee)
                                + eui.getInfoFromEmployee());
                languages.setText(getResources().getString(R.string.communication_languages) + eui.getLanguages());
                email.setText(getResources().getString(R.string.email_view) + eui.getEmail());
                phone.setText(getResources().getString(R.string.phone) + eui.getPhone());
                socialNetworksLinks.setText(getResources().getString(R.string.social_networks_links)
                        + eui.getSocialNetworksLinks());
                votesToUser.addAll(eui.getVotesToUser());
            }
            @Override
            public void onFailure(Call<ExtendedUserInfoResponse> call, Throwable t) {
                Toast.makeText(EmployeeExtendedInfoActivity.this, "Error " +
                        "'getEmployeeInfo' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onRatingClick(View view) {
        Intent intent = new Intent(this, CommentsListActivity.class);
        intent.putExtras(arguments);
        intent.putExtra("votes", votesToUser);
        finish();
        startActivity(intent);
    }

    public void onSendOffer(View view) {
        Intent intent = new Intent(this, OfferSendingChooseActivity.class);
        intent.putExtras(arguments);
        finish();
        startActivity(intent);
    }

    public void onBack(View view) {
        Intent intent;
        if (arguments.getString("activity").equals("map")) {
            intent = new Intent(this, WorkMapEmployerActivity.class);
        } else {
            intent = new Intent(this, WorkListEmployerActivity.class);
        }
        intent.putExtras(arguments);
        finish();
        startActivity(intent);
    }
}
