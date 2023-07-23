package com.example.navigatorappandroid.retrofit;
import com.example.navigatorappandroid.model.User;
import com.example.navigatorappandroid.retrofit.request.CommentRequest;
import com.example.navigatorappandroid.retrofit.request.InProgramMessageRequest;
import com.example.navigatorappandroid.retrofit.request.JobRequest;
import com.example.navigatorappandroid.retrofit.request.LocationRequest;
import com.example.navigatorappandroid.retrofit.request.ModeratorDecision;
import com.example.navigatorappandroid.retrofit.request.PasswordRequest;
import com.example.navigatorappandroid.retrofit.request.ProfessionRequest;
import com.example.navigatorappandroid.retrofit.request.ProfileRequest;
import com.example.navigatorappandroid.retrofit.request.VoteRequest;
import com.example.navigatorappandroid.retrofit.response.AvatarResponse;
import com.example.navigatorappandroid.retrofit.response.JobListResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.StringResponse;
import com.example.navigatorappandroid.retrofit.response.VoteResponse;
import java.security.Principal;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GeneralApi {

    @PUT("api/moderator")
    Call<ResultErrorsResponse> setModerator(Principal principal);

    @POST("api/system/code/add")
    Call<ResultErrorsResponse> addMessageCodeName(@Query("code") String codeName, Principal principal);

    @POST("api/system/message/add")
    Call<ResultErrorsResponse> addInProgramMessage(@Body InProgramMessageRequest inProgramMessageRequest, Principal principal);

    @POST("api/language/add")
    Call<ResultErrorsResponse> addLanguage(@Query("language") String language, Principal principal);

    @PUT("api/profile/avatar")
    Call<AvatarResponse> profileAvatar(@Query("avatar") Multipart avatar, Principal principal);

    @PUT("api/profile")
    Call<ResultErrorsResponse> profile(@Body ProfileRequest profileRequest, Principal principal);

    @POST("api/professions/name/add")
    Call<ResultErrorsResponse> addProfessionInSpecifiedLanguage(@Body ProfessionRequest professionRequest, Principal principal);

    @POST("api/professions/new/add")
    Call<ResultErrorsResponse> addNewProfessionId();

    @PUT("api/block")
    Call<ResultErrorsResponse> blockUser(@Body ModeratorDecision moderatorDecision);

    @PUT("api/unblock")
    Call<ResultErrorsResponse> unblockUser(@Body ModeratorDecision moderatorDecision);

    @PUT("api/location/{id}")
    Call<ResultErrorsResponse> updateLocation(@Path("id") long id, @Body LocationRequest locationRequest);

    @POST("api/restore")
    Call<ResultErrorsResponse> getRecoveryCode(@Query("email") String email);

    @POST("api/password")
    Call<ResultErrorsResponse> changePassword(@Body PasswordRequest passwordRequest);

    @POST("api/like")
    Call<VoteResponse> like(@Body VoteRequest voteRequest);

    @POST("api/dislike")
    Call<VoteResponse> dislike(@Body VoteRequest voteRequest);

    @POST("api/comment")
    Call<ResultErrorsResponse> comment(@Body CommentRequest commentRequest, Principal principal);

    @POST("api/employer/search/passive")
    Call<ResultErrorsResponse> setPassiveSearch(@Body JobRequest jobRequest, Principal principal);

    @POST("api/user/{id}/blacklist/{decision}")
    Call<ResultErrorsResponse> blacklist(@Query("favoriteId") String favoriteId,
                                        @Query("decision") String decision, Principal principal);

    @GET("api/user/sender/get")
    Call<User> getSender(Principal principal);

    @GET("api/user/recipient/{id}/get")
    Call<User> getRecipient(@Path("recipientId") long recipientId);

    @GET("api/jobs")
    Call<JobListResponse> getJobList(Principal principal);

    @DELETE("api/delete")
    Call<ResultErrorsResponse> checkAndDeleteNotConfirmedJobs();

    @GET("api/language/get")
    Call<StringResponse> getUsersInterfaceLanguage();
}
