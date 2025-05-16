package com.example.navigatorappandroid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OfferSendingChooseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_sending_choose);
        setCurrentActivity(this);
    }

    public void onCreateNewVacancy(View view) {
        addActivityToQueue(getCurrentActivity());
        Intent intent = new Intent(this, OfferSendingNewActivity.class);
        intent.putExtras(arguments);
        finish();
        startActivity(intent);
    }

    public void onChooseFromList(View view) {
        addActivityToQueue(getCurrentActivity());
        Intent intent = new Intent(this, VacanciesListActivity.class);
        intent.putExtras(arguments);
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
