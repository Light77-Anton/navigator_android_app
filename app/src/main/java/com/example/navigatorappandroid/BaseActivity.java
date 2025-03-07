package com.example.navigatorappandroid;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.navigatorappandroid.dto.TimersDTO;
import com.example.navigatorappandroid.handler.LanguageHandler;
import com.example.navigatorappandroid.retrofit.AuthApi;
import com.example.navigatorappandroid.retrofit.ChatApi;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.response.LoginResponse;
import com.example.navigatorappandroid.retrofit.response.TimersListResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseActivity extends AppCompatActivity {

    private List<Activity> enteredActivitiesQueue;
    private LanguageHandler languageHandler;
    protected static final int REQUEST_IMAGE_PICK = 1;
    protected GeneralApi generalApi;
    protected AuthApi authApi;
    protected SearchApi searchApi;
    protected ChatApi chatApi;
    protected UserInfoResponse userInfoResponse;
    protected RetrofitService retrofitService;
    protected Bundle arguments;
    protected View rootView;
    protected static TimersDTO activeNotificationData;
    protected static boolean isQueueLocked;
    //protected String activityTag;
    private static final String CHANNEL_ID = "your_channel_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arguments = getIntent().getExtras();
        retrofitService = new RetrofitService();
        authApi = retrofitService.getRetrofit().create(AuthApi.class);
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        searchApi = retrofitService.getRetrofit().create(SearchApi.class);
        chatApi = retrofitService.getRetrofit().create(ChatApi.class);
        checkUserAuth();
        getUserInfo();
        startTimers();
        languageHandler = new LanguageHandler();
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = new Locale(languageHandler.getLanguageCode(userInfoResponse.getEndonymInterfaceLanguage()));
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    private void startTimers() {
        for (TimersDTO timersDTO : getTimers()) {
            new CountDownTimer(timersDTO.getMillisInFuture(), 1000) {
                @Override
                public void onTick(long l) {}
                @Override
                public void onFinish() {
                    if (!isQueueLocked) {
                        isQueueLocked = true;
                        activeNotificationData = timersDTO;
                        rootView = findViewById(android.R.id.content).getRootView();
                        //activityTag = (String) rootView.getTag();
                        showNotification(rootView.getContext(), getResources().getString(R.string.notification)
                                , getResources().getString(R.string.agreed_time_has_come),
                                timersDTO.getContactedPersonId(), timersDTO.getName());
                    }
                }
            }.start();
        }
    }

    private static void showNotification(Context context, String title, String message,
                                         Long userId, String name) {
        createNotificationChannel(context);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("user_name", name);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private List<TimersDTO> getTimers() {
        List<TimersDTO> list = new ArrayList<>();
        generalApi.getTimersList().enqueue(new Callback<TimersListResponse>() {
            @Override
            public void onResponse(Call<TimersListResponse> call, Response<TimersListResponse> response) {
                list.addAll(response.body().getList());
            }
            @Override
            public void onFailure(Call<TimersListResponse> call, Throwable t) {
                Toast.makeText(BaseActivity.this, "error: 'startTimers' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });

        return list;
    }

    private void getUserInfo() {
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                userInfoResponse = response.body();
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(BaseActivity.this, "error: 'getUserInfo' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserAuth() {
        Intent intent = new Intent(this, LoginActivity.class);
        authApi.authCheck().enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (!response.body().isResult()) {
                    finish();
                    intent.putExtra("is_auth_error", true);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                finish();
                intent.putExtra("is_auth_error", true);
                startActivity(intent);
            }
        });
    }

    public Activity getLastActivity() {
        if (!enteredActivitiesQueue.isEmpty()) {
           return enteredActivitiesQueue.get(enteredActivitiesQueue.size() - 1);
        }

        return null;
    }

    public void addActivityToQueue(Activity activity) {
        enteredActivitiesQueue.add(activity);
    }

    public void removeActivityFromQueue() {
        if (!enteredActivitiesQueue.isEmpty()) {
            enteredActivitiesQueue.remove(enteredActivitiesQueue.size() - 1);
        }
    }
}
