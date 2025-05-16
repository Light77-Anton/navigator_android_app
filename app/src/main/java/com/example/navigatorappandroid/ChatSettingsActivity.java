package com.example.navigatorappandroid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.navigatorappandroid.retrofit.response.RelationshipStatusResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatSettingsActivity extends BaseActivity {

    private String userId;
    private String userName;
    private Button favoriteButton;
    private Button blackListButton;
    private Button extendedInfoButton;
    private String favoriteOptionalDecision;
    private String blackListOptionalDecision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_settings);
        setCurrentActivity(this);
        favoriteButton = findViewById(R.id.favorite_button);
        blackListButton = findViewById(R.id.black_list_button);
        extendedInfoButton = findViewById(R.id.extended_info_button);
        userId = arguments.getString("user_id");
        userName = arguments.getString("user_name");
        extendedInfoButton.setText(R.string.show_extended_info);
        generalApi.getRelationshipStatus(userId).enqueue(new Callback<RelationshipStatusResponse>() {
            @Override
            public void onResponse(Call<RelationshipStatusResponse> call, Response<RelationshipStatusResponse> response) {
                if (response.body().isFavorite()) {
                    favoriteButton.setText(R.string.remove_from_favorites);
                    favoriteOptionalDecision = "remove";
                } else {
                    favoriteButton.setText(R.string.add_to_favorites);
                    favoriteOptionalDecision = "add";
                }
                if (response.body().isInBlackList()) {
                    blackListButton.setText(R.string.remove_from_black_list);
                    blackListOptionalDecision = "remove";
                } else {
                    blackListButton.setText(R.string.add_to_black_list);
                    blackListOptionalDecision = "add";
                }
            }
            @Override
            public void onFailure(Call<RelationshipStatusResponse> call, Throwable t) {
                Toast.makeText(ChatSettingsActivity.this, "error: 'getRelationshipStatus' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
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

    public void onFavorite(View view) {
        generalApi.favorite(userId, favoriteOptionalDecision).enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                if (favoriteOptionalDecision.equals("add")) {
                    Toast.makeText(ChatSettingsActivity.this, userName + getResources()
                            .getString(R.string.user_was_added_to_favorites), Toast.LENGTH_SHORT).show();
                    favoriteButton.setText(R.string.remove_from_favorites);
                } else {
                    Toast.makeText(ChatSettingsActivity.this, userName + getResources()
                            .getString(R.string.user_was_removed_from_favorites), Toast.LENGTH_SHORT).show();
                    favoriteButton.setText(R.string.add_to_favorites);
                }
            }
            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(ChatSettingsActivity.this, "error: 'favorite' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBlackList(View view) {
        generalApi.blacklist(userId, blackListOptionalDecision).enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                if (blackListOptionalDecision.equals("add")) {
                    Toast.makeText(ChatSettingsActivity.this, userName + getResources()
                            .getString(R.string.user_was_added_to_black_list), Toast.LENGTH_SHORT).show();
                    blackListButton.setText(R.string.remove_from_black_list);
                } else {
                    Toast.makeText(ChatSettingsActivity.this, userName + getResources()
                            .getString(R.string.user_was_removed_from_black_list), Toast.LENGTH_SHORT).show();
                    blackListButton.setText(R.string.add_to_black_list);
                }
            }
            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(ChatSettingsActivity.this, "error: 'onBlackList' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onExtendedInfo(View view) {
        addActivityToQueue(getCurrentActivity());
        Intent intent;
        if (userInfoResponse.getRole().equals("Employer")) {
            intent = new Intent(this, EmployeeExtendedInfoActivity.class);
            intent.putExtra("employee_id", userId);
        } else {
            intent = new Intent(this, EmployerExtendedInfoActivity.class);
            intent.putExtra("employer_id", userId);
        }
        finish();
        startActivity(intent);
    }
}
