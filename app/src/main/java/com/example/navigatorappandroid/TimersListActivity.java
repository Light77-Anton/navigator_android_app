package com.example.navigatorappandroid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.navigatorappandroid.dto.TimersDTO;
import com.example.navigatorappandroid.retrofit.response.TimersListResponse;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimersListActivity extends BaseActivity {

    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timers_list);
        setCurrentActivity(this);
        layout = findViewById(R.id.layout);
        generalApi.getTimersList().enqueue(new Callback<TimersListResponse>() {
            @Override
            public void onResponse(Call<TimersListResponse> call, Response<TimersListResponse> response) {
                if (userInfoResponse.getRole().equals("Employee")) {
                    for (TimersDTO timersDTO : response.body().getList()) {
                        Button button = getTimerButton(timersDTO);
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
                                button.setText(sb.toString());
                            }
                            @Override
                            public void onFinish() {
                                StringBuilder sb = new StringBuilder();
                                sb.append(timersDTO.getAddress());
                                sb.append("-");
                                sb.append(timersDTO.getProfession());
                                sb.append("-");
                                sb.append("00:00:00");
                                button.setText(sb.toString());
                            }
                        }.start();
                    }
                } else {
                    for (TimersDTO timersDTO : response.body().getList()) {
                        Button button = getTimerButton(timersDTO);
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

    private Button getTimerButton(TimersDTO timersDTO) {
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toChatClick(timersDTO.getId(), timersDTO.getName());
            }
        });
        layout.addView(button);

        return button;
    }

    private void toChatClick(Long userId, String userName) {
        addActivityToQueue(getCurrentActivity());
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("user_name", userName);
        finish();
        startActivity(intent);
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
