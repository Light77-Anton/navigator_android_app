package com.example.navigatorappandroid.retrofit;
import com.example.navigatorappandroid.model.User;
import com.example.navigatorappandroid.retrofit.request.RequestForEmployees;
import com.example.navigatorappandroid.retrofit.response.EmployeeInfoResponse;
import com.example.navigatorappandroid.retrofit.response.EmployeesListResponse;
import com.example.navigatorappandroid.retrofit.response.ProfessionsResponse;
import java.security.Principal;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SearchApi {

    @GET("api/search/professions")
    Call<ProfessionsResponse> getProfessionList();

    @GET("api/search/employees")
    Call<EmployeesListResponse> getEmployeesOfChosenProfession(
            @Body RequestForEmployees requestForEmployees);

    @GET("api/search/employees/nearest")
    Call<EmployeesListResponse> getTheNearestEmployeeOfChosenProfession(
            @Body RequestForEmployees requestForEmployees);

    @GET("api/search/employees/best")
    Call<EmployeesListResponse> getTheBestEmployees(
            @Body RequestForEmployees requestForEmployees);

    @GET("api/search/employee/info/{id}")
    Call<EmployeeInfoResponse> getEmployeeInfo(@Path("id") long id);
}
