package com.example.navigatorappandroid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

public class CommentsListActivity extends BaseActivity {

    private GridLayout commentsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_list);
        commentsLayout = findViewById(R.id.comments_layout);
        if (userInfoResponse.getRole().equals("Employer")) {

        } else {

        }
    }

    public void onBack(View view) {
        Activity lastActivity = getLastActivity();
        Intent intent;
        if (lastActivity != null) {
            intent = new Intent(this, lastActivity.getClass());
            intent.putExtras(arguments);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        removeActivityFromQueue();
        finish();
        startActivity(intent);
    }
}
