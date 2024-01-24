package com.example.navigatorappandroid;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeExtendedInfoActivity extends AppCompatActivity {

    private RetrofitService retrofitService;
    private GeneralApi generalApi;
    private SearchApi searchApi;
    private UserInfoResponse userInfoResponse;
    private ScrollView scrollView = (ScrollView) getLayoutInflater().inflate(R.layout.activity_extended_info_employee, null);
    private RelativeLayout relativeLayout = scrollView.findViewById(R.id.employee_info_layout);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_info_employee);
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                userInfoResponse = response.body();
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(EmployeeExtendedInfoActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
