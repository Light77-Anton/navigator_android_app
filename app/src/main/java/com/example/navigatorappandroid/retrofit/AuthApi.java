package com.example.navigatorappandroid.retrofit;
import com.example.navigatorappandroid.retrofit.request.LoginRequest;
import com.example.navigatorappandroid.retrofit.request.RegistrationRequest;
import com.example.navigatorappandroid.retrofit.response.CaptchaResponse;
import com.example.navigatorappandroid.retrofit.response.DeleteAccountResponse;
import com.example.navigatorappandroid.retrofit.response.LoginResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthApi {

    @GET("api/auth/check")
    Call<LoginResponse> authCheck();

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("api/auth/logout")
    Call<ResultErrorsResponse> logout();

    @GET("api/auth/captcha")
    Call<CaptchaResponse> captcha();

    @POST("api/auth/registration")
    Call<ResultErrorsResponse> registration(@Body RegistrationRequest registrationRequest);

    @DELETE("api/auth/delete")
    Call<DeleteAccountResponse> deleteAccount();

}
