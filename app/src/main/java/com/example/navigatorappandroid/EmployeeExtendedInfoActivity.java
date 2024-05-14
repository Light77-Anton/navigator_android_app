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
import com.example.navigatorappandroid.retrofit.ChatApi;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.request.ChatRequest;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeExtendedInfoActivity extends AppCompatActivity {

    private RetrofitService retrofitService;
    private GeneralApi generalApi;
    private SearchApi searchApi;
    private ChatApi chatApi;
    private UserInfoResponse userInfoResponse;
    private ScrollView scrollView = (ScrollView) getLayoutInflater().inflate(R.layout.activity_extended_info_employee, null);
    private RelativeLayout relativeLayout = scrollView.findViewById(R.id.employee_info_layout);
    private Bundle arguments;

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
        Button chatButton = relativeLayout.findViewById(R.id.open_chat);
        chatApi = retrofitService.getRetrofit().create(ChatApi.class);
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setSenderId(userInfoResponse.getId());
        chatRequest.setRecipientId(Long.parseLong(arguments.getString("id")));
        chatApi.openChat(chatRequest).enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                if (response.body().getErrors().isEmpty()) {
                    chatButton.setVisibility(View.VISIBLE);
                    chatButton.setEnabled(true);
                    if (!response.body().isResult()) {
                        Toast.makeText(EmployeeExtendedInfoActivity.this, "Now you can start chat with the user", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(EmployeeExtendedInfoActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        setContentView(R.layout.activity_extended_info_employee);
    }

    public void onOpenChat(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        if (arguments.getString("activity").equals("map")) {
            intent.putExtra("activity", "map");
        } else {
            intent.putExtra("activity", "list");
        }
        intent.putExtra("page_info_role", "employee");
        intent.putExtras(arguments);
        startActivity(intent);
    }

    public void onSendOffer(View view) {
        Intent intent = new Intent(this, OfferSendingChooseActivity.class);
        intent.putExtras(arguments);
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
        startActivity(intent);
    }
}
