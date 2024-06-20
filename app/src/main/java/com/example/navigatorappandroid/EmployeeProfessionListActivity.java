package com.example.navigatorappandroid;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.navigatorappandroid.model.InfoFromEmployee;
import com.example.navigatorappandroid.model.Language;
import com.example.navigatorappandroid.model.ProfessionName;
import com.example.navigatorappandroid.retrofit.AuthApi;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.request.ProfessionToUserRequest;
import com.example.navigatorappandroid.retrofit.request.StringRequest;
import com.example.navigatorappandroid.retrofit.response.IdResponse;
import com.example.navigatorappandroid.retrofit.response.LoginResponse;
import com.example.navigatorappandroid.retrofit.response.ProfessionToUserResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeProfessionListActivity extends AppCompatActivity {

    private UserInfoResponse userInfoResponse;
    private RetrofitService retrofitService;
    private GeneralApi generalApi;
    private SearchApi searchApi;
    private AuthApi authApi;
    private long professionId;
    private ProfessionToUserResponse professionToUserResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, LoginActivity.class);
        retrofitService = new RetrofitService();
        searchApi = retrofitService.getRetrofit().create(SearchApi.class);
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        authApi = retrofitService.getRetrofit().create(AuthApi.class);
        authApi.authCheck().enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (!response.body().isResult()) {
                    intent.putExtra("is_auth_error", true);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                intent.putExtra("is_auth_error", true);
                startActivity(intent);
            }
        });
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
        setContentView(R.layout.activity_professions_list);
        Button professionButton;
        for (int i = 0 ; i < 5; i++) {
            switch (i) {
                case 1:
                    professionButton = findViewById(R.id.second_profession);
                    break;
                case 2:
                    professionButton = findViewById(R.id.third_profession);
                    break;
                case 3:
                    professionButton = findViewById(R.id.fought_profession);
                    break;
                case 4:
                    professionButton = findViewById(R.id.fifth_profession);
                    break;
                default:
                    professionButton = findViewById(R.id.first_profession);
                    break;
            }
            if (userInfoResponse.getEmployeeData().getProfessions().get(i) == null) {
                professionButton.setText("+");
                professionButton.setTextColor(getResources().getColor(R.color.dark_yellow));
                continue;
            }
            for (ProfessionName professionName : userInfoResponse.getEmployeeData()
                    .getProfessions().get(i).getProfessionNames()) {
                if (professionName.getLanguage().getLanguageEndonym()
                        .equals(userInfoResponse.getEndonymInterfaceLanguage())) {
                    professionButton.setText(professionName.getProfessionName());
                    professionButton.setTextColor(getResources().getColor(R.color.white));
                }
            }
        }
        for (Language lang : userInfoResponse.getCommunicationLanguages()) {
            EditText editText = new EditText(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 30, 0, 0);
            editText.setPadding(15, 15, 15, 15);
            editText.setLayoutParams(params);
            editText.setBackground(getResources().getDrawable(R.drawable.edit_text));
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            editText.setTextColor(getResources().getColor(R.color.white));
            editText.setHintTextColor(getResources().getColor(R.color.white));
            editText.setHint(getResources().getString(R.string.additional_info_for_employers_hint));
            for (InfoFromEmployee infoFromEmployee : userInfoResponse.getEmployeeData().getInfoFromEmployee()) { //
                if (infoFromEmployee.getLanguage().getLanguageEndonym().equals(lang.getLanguageEndonym())) {
                    editText.setText(infoFromEmployee.getText());
                }
            }
            LinearLayout layout = findViewById(R.id.layout);
            layout.addView(editText);
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

    public void onConfirmClick(View view) {

    }

    public void onBackClick(View view) {
        Intent intent = new Intent(this, EmployeeSettingsActivity.class);
        startActivity(intent);
    }
}
