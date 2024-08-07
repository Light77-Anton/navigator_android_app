package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.dto.TimersDTO;
import com.example.navigatorappandroid.retrofit.AuthApi;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.response.LoginResponse;
import com.example.navigatorappandroid.retrofit.response.TimersListResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimersListActivity extends AppCompatActivity {

    private RetrofitService retrofitService;
    private GeneralApi generalApi;
    private AuthApi authApi;
    private LinearLayout layout;
    private UserInfoResponse userInfoResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, LoginActivity.class);
        retrofitService = new RetrofitService();
        authApi = retrofitService.getRetrofit().create(AuthApi.class);
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        authApi.authCheck().enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (!response.body().isResult()) {
                    intent.putExtra("is_auth_error", true);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                intent.putExtra("is_auth_error", true);
                startActivity(intent);
            }
        });
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                userInfoResponse = response.body();
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(TimersListActivity.this, "error: 'getUserInfo' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        setContentView(R.layout.activity_timers_list);
        View rootView = findViewById(android.R.id.content);
        layout = rootView.findViewById(R.id.layout);
        generalApi.getTimersList().enqueue(new Callback<TimersListResponse>() {
            @Override
            public void onResponse(Call<TimersListResponse> call, Response<TimersListResponse> response) {
                if (userInfoResponse.getRole().equals("Employee")) {
                    for (TimersDTO timersDTO : response.body().getList()) {
                        TextView textView = new TextView(layout.getContext());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 30, 0, 0);
                        textView.setLayoutParams(params);
                        textView.setPadding(5, 5, 5, 5);
                        textView.setBackground(getResources().getDrawable(R.drawable.gray_blue_rectangle));
                        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                        textView.setTextColor(getResources().getColor(R.color.red));
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        layout.addView(textView);
                        new CountDownTimer(timersDTO.getMillisInFuture(), 1000) {
                            @Override
                            public void onTick(long l) {
                                NumberFormat f = new DecimalFormat("00");
                                long hour = (l / 3600000) % 24;
                                long min = (l / 60000) % 60;
                                long sec = (l / 1000) % 60;
                                StringBuilder sb = new StringBuilder();
                                sb.append(timersDTO.getAddress());
                                sb.append("-");
                                sb.append(timersDTO.getProfession());
                                sb.append("-");
                                sb.append(f.format(hour));
                                sb.append(":");
                                sb.append(f.format(min));
                                sb.append(":");
                                sb.append(f.format(sec));
                                textView.setText(sb.toString());
                            }
                            @Override
                            public void onFinish() {
                                StringBuilder sb = new StringBuilder();
                                sb.append(timersDTO.getAddress());
                                sb.append("-");
                                sb.append(timersDTO.getProfession());
                                sb.append("-");
                                sb.append("00:00:00");
                                textView.setText(sb.toString());
                            }
                        }.start();
                    }
                } else {
                    for (TimersDTO timersDTO : response.body().getList()) {
                        Button button = new Button(layout.getContext());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 30, 0, 0);
                        button.setLayoutParams(params);
                        button.setPadding(5, 5, 5, 5);
                        button.setBackground(getResources().getDrawable(R.drawable.gray_blue_rectangle));
                        button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                        button.setTextColor(getResources().getColor(R.color.red));
                        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        button.setContentDescription(timersDTO.getId().toString());
                        layout.addView(button);
                        new CountDownTimer(timersDTO.getMillisInFuture(), 1000) {
                            @Override
                            public void onTick(long l) {
                                NumberFormat f = new DecimalFormat("00");
                                long hour = (l / 3600000) % 24;
                                long min = (l / 60000) % 60;
                                long sec = (l / 1000) % 60;
                                StringBuilder sb = new StringBuilder();
                                sb.append(timersDTO.getName());
                                sb.append("-");
                                sb.append(timersDTO.getProfession());
                                sb.append("-");
                                sb.append(f.format(hour));
                                sb.append(":");
                                sb.append(f.format(min));
                                sb.append(":");
                                sb.append(f.format(sec));
                                button.setText(sb.toString());
                            }
                            @Override
                            public void onFinish() {
                                StringBuilder sb = new StringBuilder();
                                sb.append(timersDTO.getName());
                                sb.append("-");
                                sb.append(timersDTO.getProfession());
                                sb.append("-");
                                sb.append("00:00:00");
                                button.setText(sb.toString());
                            }
                        }.start();
                    }
                }
            }

            @Override
            public void onFailure(Call<TimersListResponse> call, Throwable t) {
                Toast.makeText(TimersListActivity.this, "error: 'getTimersList' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void toChatClick(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("id", view.getContentDescription().toString());
        startActivity(intent);
    }
}
