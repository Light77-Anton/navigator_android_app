package com.example.navigatorappandroid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigatorappandroid.model.Comment;
import com.example.navigatorappandroid.retrofit.request.CommentRequest;
import com.example.navigatorappandroid.retrofit.response.CommentsListResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReplyActivity extends BaseActivity {

    private EditText reply;
    private TextView title;
    private Comment initialComment;
    private long repliedCommentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        setCurrentActivity(this);
        reply = findViewById(R.id.reply);
        title = findViewById(R.id.title);
        StringBuilder sb = new StringBuilder();
        sb.append(arguments.getString("sender_name"));
        sb.append(getResources().getString(R.string.contacted_person_leave_public_comment_about_you));
        sb.append("'");
        sb.append(arguments.getString("comment_text"));
        sb.append("'");
        title.setText(sb.toString());
        chatApi.getCommentById(arguments.getLong("comment_id")).enqueue(new Callback<CommentsListResponse>() {
            @Override
            public void onResponse(Call<CommentsListResponse> call, Response<CommentsListResponse> response) {
                Comment c = response.body().getList().get(0);
                if (c.isInitialComment()) {
                    initialComment = c;
                } else {
                    initialComment = c.getInitialComment();
                }
                repliedCommentId = c.getId();
            }
            @Override
            public void onFailure(Call<CommentsListResponse> call, Throwable t) {
                Toast.makeText(ReplyActivity.this,
                        "Error 'getInitialCommentById' method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onConfirmClick(View view) {
        if (reply.getText().toString().isEmpty() || reply.getText().toString().isBlank()) {
            onBack(view);
        } else {
            CommentRequest commentRequest = new CommentRequest();
            commentRequest.setRepliedCommentId(repliedCommentId);
            commentRequest.setContent(reply.getText().toString());
            generalApi.reply(initialComment.getId(), commentRequest).enqueue(new Callback<ResultErrorsResponse>() {
                @Override
                public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                    onBack(view);
                }
                @Override
                public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                    Toast.makeText(ReplyActivity.this,
                            "Error 'reply' method is failure", Toast.LENGTH_SHORT).show();
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
