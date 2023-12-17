package com.example.navigatorappandroid.retrofit;
import com.example.navigatorappandroid.retrofit.request.JobRequest;
import com.example.navigatorappandroid.retrofit.request.LocationsRequest;
import com.example.navigatorappandroid.retrofit.request.SearchRequest;
import com.example.navigatorappandroid.retrofit.request.StringRequest;
import com.example.navigatorappandroid.retrofit.response.DistanceResponse;
import com.example.navigatorappandroid.retrofit.response.EmployeeInfoResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.SearchResponse;
import com.example.navigatorappandroid.retrofit.response.ProfessionsResponse;
import com.example.navigatorappandroid.retrofit.response.VacancyInfoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SearchApi {

    @GET("api/search/distance")
    Call<DistanceResponse> getMeasuredDistance(@Body LocationsRequest locationsRequest);

    @GET("api/search/professions")
    Call<ProfessionsResponse> getProfessionList();

    @POST("api/search/employer/vacancy/set")
    Call<ResultErrorsResponse> setVacancy(@Body JobRequest jobRequest);

    @GET("api/search/employer/vacancy/get")
    Call<VacancyInfoResponse> getVacancyById(@Body StringRequest stringRequest);

    @GET("api/search/employer/vacancy/delete")
    Call<ResultErrorsResponse> deleteVacancyById(@Body StringRequest stringRequest);

    @GET("api/search/employees")
    Call<SearchResponse> getEmployeesOfChosenProfession(@Body SearchRequest searchRequest);

    @GET("api/search/vacancies")
    Call<SearchResponse> getVacanciesByProfession(@Body SearchRequest searchRequest);

    @GET("api/search/employee/info/{id}")
    Call<EmployeeInfoResponse> getEmployeeInfo(@Path("id") long id);
}
