package com.example.navigatorappandroid.retrofit;
import com.example.navigatorappandroid.retrofit.request.ChatRequest;
import com.example.navigatorappandroid.retrofit.response.ChatMessageResponse;
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

    @POST("api/chat/message")
    Call<ChatMessageResponse> processMessage(@Body ChatRequest chatRequest);

    @GET("api/chat/room")
    Call<ChatMessageResponse> getOrCreateChatRoom(@Body ChatRequest chatRequest);
}
