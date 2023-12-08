package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.model.ProfessionName;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.request.ProfessionToUserRequest;
import com.example.navigatorappandroid.retrofit.request.StringRequest;
import com.example.navigatorappandroid.retrofit.response.IdResponse;
import com.example.navigatorappandroid.retrofit.response.ProfessionToUserResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeProfessionListActivity extends AppCompatActivity {

    private UserInfoResponse userInfoResponse;

    RetrofitService retrofitService;
    GeneralApi generalApi;
    SearchApi searchApi;
    RelativeLayout relativeLayout;
    private long professionId;
    ProfessionToUserResponse professionToUserResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professions_list);
        retrofitService = new RetrofitService();
        searchApi = retrofitService.getRetrofit().create(SearchApi.class);
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                userInfoResponse = response.body();
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(EmployeeProfessionListActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        relativeLayout = (RelativeLayout) getLayoutInflater().inflate( R.layout.activity_professions_list, null);
        Button professionButton;
        for (int i = 0 ; i < 5; i++) {
            switch (i) {
                case 1:
                    professionButton = relativeLayout.findViewById(R.id.second_profession);
                    break;
                case 2:
                    professionButton = relativeLayout.findViewById(R.id.third_profession);
                    break;
                case 3:
                    professionButton = relativeLayout.findViewById(R.id.fought_profession);
                    break;
                case 4:
                    professionButton = relativeLayout.findViewById(R.id.fifth_profession);
                    break;
                default:
                    professionButton = relativeLayout.findViewById(R.id.first_profession);
                    break;
            }
            if (userInfoResponse.getEmployeeData().getProfessionToUserList().get(i) == null) {
                professionButton.setText("+");
                professionButton.setTextColor(getResources().getColor(R.color.dark_yellow));
                break;
            }
            for (ProfessionName professionName : userInfoResponse.getEmployeeData()
                    .getProfessionToUserList().get(i).getProfession().getProfessionNames()) {
                if (professionName.getLanguage().getLanguageEndonym()
                        .equals(userInfoResponse.getEndonymInterfaceLanguage())) {
                    professionButton.setText(professionName.getProfessionName());
                    professionButton.setTextColor(getResources().getColor(R.color.white));
                }
            }
        }
    }

    public void onProfessionClick(View view) {
        Intent intent = new Intent(this, EmployeeProfessionInfoActivity.class);
        Button button;
        switch (view.getId()) {
            case R.id.second_profession:
                button = relativeLayout.findViewById(R.id.second_profession);
                break;
            case R.id.third_profession:
                button = relativeLayout.findViewById(R.id.third_profession);
                break;
            case R.id.fought_profession:
                button = relativeLayout.findViewById(R.id.fought_profession);
                break;
            case R.id.fifth_profession:
                button = relativeLayout.findViewById(R.id.fifth_profession);
                break;
            default:
                button = relativeLayout.findViewById(R.id.first_profession);
                break;
        }
        intent.putExtra("profession", button.getText().toString());
        intent.putExtra("language", userInfoResponse.getEndonymInterfaceLanguage());
        startActivity(intent);
    }

    public void onRemoveClick(View view) {
        Button button;
        switch (view.getId()) {
            case R.id.second_remove_button:
                button = relativeLayout.findViewById(R.id.second_profession);
                break;
            case R.id.third_remove_button:
                button = relativeLayout.findViewById(R.id.third_profession);
                break;
            case R.id.fought_remove_button:
                button = relativeLayout.findViewById(R.id.fought_profession);
                break;
            case R.id.fifth_remove_button:
                button = relativeLayout.findViewById(R.id.fifth_profession);
                break;
            default:
                button = relativeLayout.findViewById(R.id.first_profession);
                break;
        }
        StringRequest stringRequest = new StringRequest();
        stringRequest.setString(button.getText().toString());
        generalApi.getProfessionIdByName(stringRequest).enqueue(new Callback<IdResponse>() {
            @Override
            public void onResponse(Call<IdResponse> call, Response<IdResponse> response) {
                professionId = response.body().getId();
            }
            @Override
            public void onFailure(Call<IdResponse> call, Throwable t) {
                Toast.makeText(EmployeeProfessionListActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        ProfessionToUserRequest professionToUserRequest = new ProfessionToUserRequest();
        professionToUserRequest.setProfessionId(professionId);
        generalApi.deleteProfessionToUser(professionToUserRequest).enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {
                button.setText("+");
                button.setTextColor(getResources().getColor(R.color.dark_yellow));
            }

            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(EmployeeProfessionListActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
