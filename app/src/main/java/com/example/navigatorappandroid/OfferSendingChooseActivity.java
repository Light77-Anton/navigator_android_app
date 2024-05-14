package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;

public class OfferSendingChooseActivity extends AppCompatActivity {

    private RetrofitService retrofitService;
    private GeneralApi generalApi;
    private SearchApi searchApi;
    private UserInfoResponse userInfoResponse;
    private RelativeLayout relativeLayout;
    Bundle arguments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arguments = getIntent().getExtras();
        setContentView(R.layout.activity_offer_sending_choose);
    }

    public void onCreateNewVacancy(View view) {
        Intent intent = new Intent(this, OfferSendingChooseActivity.class);;
        intent.putExtras(arguments);
        startActivity(intent);
    }

    public void onChooseFromList(View view) {
        Intent intent = new Intent(this, .class);;
        intent.putExtras(arguments);
        startActivity(intent);
    }

    public void onBack(View view) {
        Intent intent = new Intent(this, EmployeeExtendedInfoActivity.class);;
        intent.putExtras(arguments);
        startActivity(intent);
    }
}
