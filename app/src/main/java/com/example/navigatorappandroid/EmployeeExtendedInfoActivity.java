package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeExtendedInfoActivity extends AppCompatActivity {

    private RetrofitService retrofitService;
    private GeneralApi generalApi;
    private SearchApi searchApi;
    private UserInfoResponse userInfoResponse;
    private ScrollView scrollView = (ScrollView) getLayoutInflater().inflate(R.layout.activity_extended_info_employee, null);
    private RelativeLayout relativeLayout = scrollView.findViewById(R.id.employee_info_layout);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                userInfoResponse = response.body();
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(EmployeeExtendedInfoActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        TextView name = relativeLayout.findViewById(R.id.name);
        Button rating = relativeLayout.findViewById(R.id.rating);
        ImageView avatar = relativeLayout.findViewById(R.id.avatar);
        TextView status = relativeLayout.findViewById(R.id.status);
        TextView professions = relativeLayout.findViewById(R.id.professions);
        TextView languages = relativeLayout.findViewById(R.id.languages);
        TextView email = relativeLayout.findViewById(R.id.email);
        TextView phone = relativeLayout.findViewById(R.id.phone);
        TextView socialNetworksLinks = relativeLayout.findViewById(R.id.social_networks_links);
        name.setText(arguments.getString("name"));
        rating.setText(arguments.getString("rating"));
        avatar.setImageURI();
        status.setText(arguments.getString("status"));
        professions.setText(arguments.getString("professions"));
        languages.setText(arguments.getString("languages"));
        email.setText(arguments.getString("email"));
        phone.setText(arguments.getString("phone"));
        socialNetworksLinks.setText(arguments.getString("social_networks_links"));
        setContentView(R.layout.activity_extended_info_employee);
    }

    public void onOpenChat(View view) {
        Bundle arguments = getIntent().getExtras();
        Intent intent = new Intent(EmployeeExtendedInfoActivity.this, ChatActivity.class);
        if (arguments.getString("activity").equals("map")) {
            intent.putExtra("activity", "map");
        } else {
            intent.putExtra("activity", "list");
        }
        intent.putExtra("page_info_role", "employee");
        intent.putExtra("id", arguments.getLong("id"));
        intent.putExtra("name", arguments.getString("name"));
        intent.putExtra("rating", arguments.getString("rating"));
        intent.putExtra("avatar", arguments.getString("avatar"));
        intent.putExtra("status", arguments.getString("status"));
        intent.putExtra("languages", arguments.getString("languages"));
        intent.putExtra("professions", arguments.getString("professions"));
        intent.putExtra("additional_info", arguments.getString("additional_info"));
        intent.putExtra("email", arguments.getString("email"));
        intent.putExtra("phone", arguments.getString("phone"));
        intent.putExtra("social_networks_links", arguments.getString("social_networks_links"));
        startActivity(intent);
    }

    public void onSendOffer(View view) {
        Bundle arguments = getIntent().getExtras();
        Intent intent;
        if (arguments.getString("activity").equals("map")) {
            intent = new Intent(EmployeeExtendedInfoActivity.this, WorkMapEmployerActivity.class);
        } else {
            intent = new Intent(EmployeeExtendedInfoActivity.this, WorkListEmployerActivity.class);
        }
        startActivity(intent);
    }

    public void onBack(View view) {
        Bundle arguments = getIntent().getExtras();
        Intent intent;
        if (arguments.getString("activity").equals("map")) {
            intent = new Intent(EmployeeExtendedInfoActivity.this, WorkMapEmployerActivity.class);
        } else {
            intent = new Intent(EmployeeExtendedInfoActivity.this, WorkListEmployerActivity.class);
        }
        startActivity(intent);
    }
}
