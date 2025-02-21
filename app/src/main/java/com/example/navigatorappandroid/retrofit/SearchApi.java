package com.example.navigatorappandroid.retrofit;
import com.example.navigatorappandroid.retrofit.request.VacancyRequest;
import com.example.navigatorappandroid.retrofit.request.LocationsRequest;
import com.example.navigatorappandroid.retrofit.request.SearchRequest;
import com.example.navigatorappandroid.retrofit.request.StringRequest;
import com.example.navigatorappandroid.retrofit.response.DistanceResponse;
import com.example.navigatorappandroid.retrofit.response.ExtendedUserInfoResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.SearchResponse;
import com.example.navigatorappandroid.retrofit.response.ProfessionsResponse;
import com.example.navigatorappandroid.retrofit.response.VacancyInfoResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SearchApi {

    @GET("api/search/distance")
    Call<DistanceResponse> getMeasuredDistance(@Body LocationsRequest locationsRequest);

    @GET("api/search/professions")
    Call<ProfessionsResponse> getProfessionList();

    @POST("api/search/employer/vacancy/set")
    Call<ResultErrorsResponse> setVacancy(@Body VacancyRequest vacancyRequest);

    @GET("api/search/vacancy/{id}/get")
    Call<VacancyInfoResponse> getVacancyById(@Path("id") String id);

    @DELETE("api/search/vacancy/{id}/delete")
    Call<ResultErrorsResponse> deleteVacancyById(@Body String id);

    @GET("api/search/employees")
    Call<SearchResponse> getEmployeesOfChosenProfession(@Body SearchRequest searchRequest);

    @GET("api/search/vacancies")
    Call<SearchResponse> getVacanciesByProfession(@Body SearchRequest searchRequest);

    @GET("api/search/employee/{id}/info")
    Call<ExtendedUserInfoResponse> getEmployeeInfo(@Path("id") String id);

    @GET("api/search/employer/{id}/info")
    Call<ExtendedUserInfoResponse> getEmployerInfo(@Path("id") String id);
}
