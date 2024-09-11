package com.example.navigatorappandroid.retrofit;
import com.example.navigatorappandroid.retrofit.request.ChatRequest;
import com.example.navigatorappandroid.retrofit.request.DecisionRequest;
import com.example.navigatorappandroid.retrofit.request.VacancyRequest;
import com.example.navigatorappandroid.retrofit.response.AnswerToOfferResponse;
import com.example.navigatorappandroid.retrofit.response.ChatMessageResponse;
import com.example.navigatorappandroid.retrofit.response.ExtendedUserInfoResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatApi {

    @GET("api/chat/messages/{senderId}/{recipientId}/count")
    Call<ChatMessageResponse> countNewMessages(@Path("senderId") long senderId, @Path("recipientId") long recipientId);

    @GET("api/chat/messages/find")
    Call<ChatMessageResponse> findChatMessages(@Body ChatRequest chatRequest);

    @GET("api/chat/messages/{id}")
    Call<ChatMessageResponse> findMessage(@Path("id") long id);

    @POST("api/chat/message/{userId}")
    Call<ChatMessageResponse> processMessage(@Path("userId") String userId, @Body ChatRequest chatRequest);

    @GET("api/chat/room")
    Call<ChatMessageResponse> getOrCreateChatRoom(@Body ChatRequest chatRequest);

    @POST("api/chat/employer/offer/{userId}")
    Call<ResultErrorsResponse> sendOfferFromEmployer(@Path("userId") String userId, @Body VacancyRequest vacancyRequest);

    @POST("api/chat/employee/offer/{userId}")
    Call<ExtendedUserInfoResponse> sendEmployeesOffer(@Path("userId") String userId, @Body VacancyRequest vacancyRequest);

    @GET("api/chat/open")
    Call<ResultErrorsResponse> openChat(@Body ChatRequest chatRequest);

    @POST("api/chat/offer/decision")
    Call<AnswerToOfferResponse> giveDecisionToOffer(@Body DecisionRequest decision);
}
