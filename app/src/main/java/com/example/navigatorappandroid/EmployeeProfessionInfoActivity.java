package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.request.ProfessionToUserRequest;
import com.example.navigatorappandroid.retrofit.request.StringRequest;
import com.example.navigatorappandroid.retrofit.response.IdResponse;
import com.example.navigatorappandroid.retrofit.response.ProfessionToUserResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.TextListResponse;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeProfessionInfoActivity extends AppCompatActivity {
    private RetrofitService retrofitService;
    private  GeneralApi generalApi;
    private LinearLayout layout;
    private long professionId;
    ProfessionToUserRequest professionToUserRequest;
    ProfessionToUserResponse professionToUserResponse;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profession_info);
        layout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_profession_info, null);
        RetrofitService retrofitService = new RetrofitService();
        GeneralApi generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        Bundle arguments = getIntent().getExtras();
        String professionName = arguments.get("profession").toString();
        Spinner professionSpinner = layout.findViewById(R.id.profession);
        ArrayList<String> professionNamesList = new ArrayList<>();
        String language = arguments.get("language").toString();
        StringRequest stringRequest = new StringRequest();
        stringRequest.setString(language);
        generalApi.getProfessionsNamesInSpecifiedLanguage(stringRequest).enqueue(new Callback<TextListResponse>() {
            @Override
            public void onResponse(Call<TextListResponse> call, Response<TextListResponse> response) {
                professionNamesList.addAll(response.body().getList());
            }

            @Override
            public void onFailure(Call<TextListResponse> call, Throwable t) {
                Toast.makeText(EmployeeProfessionInfoActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<String> professionsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, professionNamesList);
        professionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professionSpinner.setAdapter(professionsAdapter);
        int spinnerProfessionPosition = professionsAdapter.getPosition(professionName);
        professionSpinner.setSelection(spinnerProfessionPosition);
        stringRequest.setString(professionName);
        generalApi.getProfessionIdByName(stringRequest).enqueue(new Callback<IdResponse>() {
            @Override
            public void onResponse(Call<IdResponse> call, Response<IdResponse> response) {
                professionId = response.body().getId();
            }
            @Override
            public void onFailure(Call<IdResponse> call, Throwable t) {
                Toast.makeText(EmployeeProfessionInfoActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        professionToUserRequest = new ProfessionToUserRequest();
        professionToUserRequest.setProfessionId(professionId);

        generalApi.getProfessionToUser(professionToUserRequest).enqueue(new Callback<ProfessionToUserResponse>() {
            @Override
            public void onResponse(Call<ProfessionToUserResponse> call, Response<ProfessionToUserResponse> response) {
                professionToUserResponse = response.body();
                if (response.body().getExtendedInfoFromEmployee() != null) {
                    EditText editText = layout.findViewById(R.id.profession_info);
                    editText.setText(response.body().getExtendedInfoFromEmployee());
                }
            }

            @Override
            public void onFailure(Call<ProfessionToUserResponse> call, Throwable t) {
                Toast.makeText(EmployeeProfessionInfoActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onConfirm(View view) {
        professionToUserRequest = new ProfessionToUserRequest();
        professionToUserRequest.setProfessionId(professionToUserResponse.getProfession().getId());
        professionToUserRequest.setAdditionalInfo(professionToUserResponse.getExtendedInfoFromEmployee());
        generalApi.postProfessionToUser(professionToUserRequest).enqueue(new Callback<ResultErrorsResponse>() {
            @Override
            public void onResponse(Call<ResultErrorsResponse> call, Response<ResultErrorsResponse> response) {}

            @Override
            public void onFailure(Call<ResultErrorsResponse> call, Throwable t) {
                Toast.makeText(EmployeeProfessionInfoActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = new Intent(this, EmployeeProfessionListActivity.class);
        startActivity(intent);
    }

    public void onBack(View view) {
        Intent intent = new Intent(this, EmployeeProfessionListActivity.class);
        startActivity(intent);
    }
}
