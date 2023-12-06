package com.example.navigatorappandroid.retrofit;
import com.example.navigatorappandroid.retrofit.request.SearchRequest;
import com.example.navigatorappandroid.retrofit.response.EmployeeInfoResponse;
import com.example.navigatorappandroid.retrofit.response.SearchResponse;
import com.example.navigatorappandroid.retrofit.response.ProfessionsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SearchApi {

    @GET("api/search/professions")
    Call<ProfessionsResponse> getProfessionList();

    @GET("api/search/employees")
    Call<SearchResponse> getEmployeesOfChosenProfession(@Body SearchRequest searchRequest);

    @GET("api/search/vacancies")
    Call<SearchResponse> getVacanciesByProfession(@Body SearchRequest searchRequest);

    @GET("api/search/employee/info/{id}")
    Call<EmployeeInfoResponse> getEmployeeInfo(@Path("id") long id);
}
