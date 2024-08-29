package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

public class NotificationActivity extends BaseActivity {

    private LinearLayout layout;
    private Intent intent;
    private boolean isTemplateSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        layout = findViewById(R.id.inside_layout);
        if (userInfoResponse.getRole().equals("Employer")) {
            switch (activityTag) {
                case "Change password activity":
                    intent = new Intent(this, ChangePasswordActivity.class);
                    break;
                case "Chat activity":
                    intent = new Intent(this, ChatActivity.class);
                    break;
                case "Chat list activity":
                    intent = new Intent(this, ChatListActivity.class);
                    break;
                case "Chat settings activity":
                    intent = new Intent(this, ChatSettingsActivity.class);
                    break;
                case "Choose additional languages activity":
                    intent = new Intent(this, ChooseAdditionalLanguagesActivity.class);
                    break;
                case "Comments list activity":
                    intent = new Intent(this, );
                    break;
                case "Extended info employee activity":
                    intent = new Intent(this, EmployeeExtendedInfoActivity.class);
                    break;
                case "Offer sending choose activity":
                    intent = new Intent(this, OfferSendingChooseActivity.class);
                    break;
                case "Offer sending new activity":
                    intent = new Intent(this, OfferSendingNewActivity.class);
                    break;
                case "Search employees activity":
                    intent = new Intent(this, SearchEmployeesActivity.class);
                    break;
                case "Settings employer activity":
                    intent = new Intent(this, EmployerSettingsActivity.class);
                    break;
                case "Timers list activity":
                    intent = new Intent(this, TimersListActivity.class);
                    break;
                case "Vacancies list activity":
                    intent = new Intent(this, VacanciesListActivity.class);
                    break;
                case "Vacancies setting activity":
                    intent = new Intent(this, EmployerVacanciesSettingActivity.class);
                    break;
                case "Edit vacancy activity":
                    intent = new Intent(this, EmployerVacancyEditActivity.class);
                    break;
                case "Waiting date time setting activity":
                    intent = new Intent(this, SettingWaitingDateTimeActivity.class);
                    break;
                case "Employer work list activity":
                    intent = new Intent(this, WorkListEmployerActivity.class);
                    break;
                case "Employer work map activity":
                    intent = new Intent(this, WorkMapEmployerActivity.class);
                    break;
                default:
                    intent = new Intent(this, LoginActivity.class);
            }
        } else {
            switch (activityTag) {
                case "Change password activity":
                    intent = new Intent(this, ChangePasswordActivity.class);
                    break;
                case "Chat activity":
                    intent = new Intent(this, ChatActivity.class);
                    break;
                case "Chat list activity":
                    intent = new Intent(this, ChatListActivity.class);
                    break;
                case "Chat settings activity":
                    intent = new Intent(this, ChatSettingsActivity.class);
                    break;
                case "Comments list activity":
                    intent = new Intent(this, );
                    break;
                case "Extended info employer activity":
                    intent = new Intent(this, EmployerExtendedInfoActivity.class);
                    break;
                case "Timers list activity":
                    intent = new Intent(this, TimersListActivity.class);
                    break;
                case "Settings employee activity":
                    intent = new Intent(this, EmployeeSettingsActivity.class);
                    break;
                case "Employee work list activity":
                    intent = new Intent(this, WorkListEmployeeActivity.class);
                    break;
                case "Employee work map activity":
                    intent = new Intent(this, WorkMapEmployeeActivity.class);
                    break;
                case "Search vacancies activity":
                    intent = new Intent(this, SearchVacanciesActivity.class);
                    break;
                case "Employee status activity":
                    intent = new Intent(this, EmployeeStatusActivity.class);
                    break;
                case "Professions list activity":
                    intent = new Intent(this, EmployeeProfessionListActivity.class);
                    break;
                default:
                    intent = new Intent(this, LoginActivity.class);
            }
        }
        if (isQueueLocked && activeNotificationData != null) {
            TextView nameTextView = new TextView(layout.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            nameTextView.setLayoutParams(params);
            nameTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            nameTextView.setPadding(20, 0, 0, 0);
            nameTextView.setTextColor(getResources().getColor(R.color.black));
            nameTextView.setTextSize(30, TypedValue.COMPLEX_UNIT_SP);
            nameTextView.setText(activeNotificationData.getName());
            layout.addView(nameTextView);
            TextView professionTextView = new TextView(layout.getContext());
            professionTextView.setLayoutParams(params);
            professionTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            professionTextView.setPadding(20, 0, 0, 0);
            professionTextView.setTextColor(getResources().getColor(R.color.black));
            professionTextView.setTextSize(30, TypedValue.COMPLEX_UNIT_SP);
            professionTextView.setText(activeNotificationData.getProfession());
            layout.addView(professionTextView);
            TextView addressTextView = new TextView(layout.getContext());
            addressTextView.setLayoutParams(params);
            addressTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            addressTextView.setPadding(20, 0, 0, 0);
            addressTextView.setTextColor(getResources().getColor(R.color.black));
            addressTextView.setTextSize(30, TypedValue.COMPLEX_UNIT_SP);
            addressTextView.setText(activeNotificationData.getAddress());
            layout.addView(addressTextView);
            if (userInfoResponse.getRole().equals("Employer")) {
                CheckBox checkBox = new CheckBox(this);
                LinearLayout.LayoutParams checkboxParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 72, layout.getResources().getDisplayMetrics()));
                checkboxParams.setMargins(30, 40, 0, 0);
                checkBox.setLayoutParams(checkboxParams);
                checkBox.setTextColor(getResources().getColor(R.color.black));
                checkBox.setTextSize(20, TypedValue.COMPLEX_UNIT_SP);
                checkBox.setText(getResources().getString(R.string.save_vacancy_as_template));
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            isTemplateSaved = true;
                        } else {
                            isTemplateSaved = false;
                        }
                        intent.putExtra("is_template_saved", isTemplateSaved);
                    }
                });
                layout.addView(checkBox);
            }
            TextView buttonsDescTextView = new TextView(layout.getContext());
            buttonsDescTextView.setLayoutParams(params);
            buttonsDescTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            buttonsDescTextView.setPadding(20, 0, 0, 0);
            buttonsDescTextView.setTextColor(getResources().getColor(R.color.black));
            buttonsDescTextView.setTextSize(30, TypedValue.COMPLEX_UNIT_SP);
            buttonsDescTextView.setText(getResources().getString(R.string.rate_user_now_or_later));
            layout.addView(buttonsDescTextView);
            Button goToRateButton = new Button(layout.getContext());
            params.setMargins(0, 30, 0, 0);
            goToRateButton.setLayoutParams(params);
            goToRateButton.setBackground(ContextCompat.getDrawable(this, R.drawable.notification_background));
            goToRateButton.setTextColor(getResources().getColor(R.color.black));
            goToRateButton.setTextSize(20, TypedValue.COMPLEX_UNIT_SP);
            goToRateButton.setText(getResources().getString(R.string.go_to_rate_user));
            goToRateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(this, );
                    startActivity(intent);
                }
            });
            layout.addView(goToRateButton);
            Button skipButton = new Button(layout.getContext());
            skipButton.setLayoutParams(params);
            skipButton.setBackground(ContextCompat.getDrawable(this, R.drawable.notification_background));
            skipButton.setTextColor(getResources().getColor(R.color.black));
            skipButton.setTextSize(20, TypedValue.COMPLEX_UNIT_SP);
            skipButton.setText(getResources().getString(R.string.skip));
            skipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(intent);
                }
            });
            layout.addView(skipButton);
        }
    }
}
