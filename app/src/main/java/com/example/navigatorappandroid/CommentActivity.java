package com.example.navigatorappandroid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.navigatorappandroid.retrofit.request.VoteRequest;
import com.example.navigatorappandroid.retrofit.response.IdResponse;
import com.example.navigatorappandroid.retrofit.response.VoteResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends BaseActivity {

    private TextView votesCountView;
    private RadioGroup radioGroup;
    private TextView currentVoteView;
    private EditText commentView;
    private String userId;
    private Byte value;
    private Long availableVotesCount;
    private long employeeId;
    private long employerId;
    private long senderId;
    private long recipientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        setCurrentActivity(this);
        votesCountView = findViewById(R.id.votes_count);
        if (userInfoResponse.getRole().equals("Employee")) {
            employerId = Long.parseLong(arguments.getString("employer_id"));
            recipientId = Long.parseLong(arguments.getString("employer_id"));
            employeeId = userInfoResponse.getId();
        } else {
            employeeId = Long.parseLong(arguments.getString("employee_id"));
            recipientId = Long.parseLong(arguments.getString("employee_id"));
            employerId = userInfoResponse.getId();
        }
        senderId = userInfoResponse.getId();
        generalApi.getAvailableVotesCount(employeeId, employerId).enqueue(new Callback<IdResponse>() {
            @Override
            public void onResponse(Call<IdResponse> call, Response<IdResponse> response) {
                availableVotesCount = response.body().getId();
                votesCountView.setText(availableVotesCount.toString());
            }
            @Override
            public void onFailure(Call<IdResponse> call, Throwable t) {
                Toast.makeText(CommentActivity.this, "error: 'getAvailableVotesCount' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        radioGroup = findViewById(R.id.radios);
        radioGroup.setOnCheckedChangeListener((radiogroup, id)-> {
            RadioButton radio = findViewById(id);
            switch (radio.getText().toString()) {
                case "1":
                    value = 1;
                    break;
                case "2":
                    value = 2;
                    break;
                case "3":
                    value = 3;
                    break;
                case "4":
                    value = 4;
                    break;
                case "5":
                    value = 5;
                    break;
                case "6":
                    value = 6;
                    break;
                case "7":
                    value = 7;
                    break;
                case "8":
                    value = 8;
                    break;
                case "9":
                    value = 9;
                    break;
                case "10":
                    value = 10;
                    break;
                default:
                    break;
            }
        });
        currentVoteView = findViewById((R.id.your_current_vote));
        generalApi.getAverageVoteFromSenderToRecipient(senderId, recipientId).enqueue(new Callback<IdResponse>() {
            @Override
            public void onResponse(Call<IdResponse> call, Response<IdResponse> response) {
                StringBuilder sb = new StringBuilder();
                sb.append(response.body().getId());
                sb.append("/10");
                currentVoteView.setText(sb.toString());
            }
            @Override
            public void onFailure(Call<IdResponse> call, Throwable t) {
                Toast.makeText(CommentActivity.this, "error: 'getAverageVoteFromSenderToRecipient' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        commentView = findViewById(R.id.comment);
        if (userInfoResponse.getRole().equals("Employee")) {
            userId = arguments.getString("employee_id");
        } else {
            userId = arguments.getString("employer_id");
        }
    }

    public void onVoteClick(View view) {
        if (availableVotesCount == 0) {
            Toast.makeText(CommentActivity.this, getResources().getString(R.string.you_can_vote_no_more),
                    Toast.LENGTH_SHORT).show();
        } else {
            availableVotesCount = availableVotesCount -1;
            VoteRequest voteRequest = new VoteRequest();
            voteRequest.setCommentContent(commentView.getText().toString());
            voteRequest.setValue(value);
            voteRequest.setUserId(Long.parseLong(userId));
            generalApi.vote(voteRequest).enqueue(new Callback<VoteResponse>() {
                @Override
                public void onResponse(Call<VoteResponse> call, Response<VoteResponse> response) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(response.body().getAverageValue());
                    sb.append("/10");
                    currentVoteView.setText(sb.toString());
                }
                @Override
                public void onFailure(Call<VoteResponse> call, Throwable t) {
                    Toast.makeText(CommentActivity.this, "error: 'vote' " +
                            "method is failure", Toast.LENGTH_SHORT).show();
                }
            });

        }
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
