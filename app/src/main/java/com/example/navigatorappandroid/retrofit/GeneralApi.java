package com.example.navigatorappandroid.retrofit;
import com.example.navigatorappandroid.model.User;
import com.example.navigatorappandroid.retrofit.request.ChangePasswordRequest;
import com.example.navigatorappandroid.retrofit.request.CommentRequest;
import com.example.navigatorappandroid.retrofit.request.EmployeeInfoForEmployersRequest;
import com.example.navigatorappandroid.retrofit.request.InProgramMessageRequest;
import com.example.navigatorappandroid.retrofit.request.StatusRequest;
import com.example.navigatorappandroid.retrofit.request.VacancyRequest;
import com.example.navigatorappandroid.retrofit.request.LocationRequest;
import com.example.navigatorappandroid.retrofit.request.ModeratorDecision;
import com.example.navigatorappandroid.retrofit.request.PasswordRequest;
import com.example.navigatorappandroid.retrofit.request.ProfessionRequest;
import com.example.navigatorappandroid.retrofit.request.ProfessionToUserRequest;
import com.example.navigatorappandroid.retrofit.request.ProfileRequest;
import com.example.navigatorappandroid.retrofit.request.TextListInSpecifiedLanguageRequest;
import com.example.navigatorappandroid.retrofit.request.VoteRequest;
import com.example.navigatorappandroid.retrofit.response.AvatarResponse;
import com.example.navigatorappandroid.retrofit.response.CommentsListResponse;
import com.example.navigatorappandroid.retrofit.response.IdResponse;
import com.example.navigatorappandroid.retrofit.response.JobListResponse;
import com.example.navigatorappandroid.retrofit.response.ProfessionNamesListResponse;
import com.example.navigatorappandroid.retrofit.response.ProfessionToUserResponse;
import com.example.navigatorappandroid.retrofit.response.RelationshipStatusResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import com.example.navigatorappandroid.retrofit.response.StringResponse;
import com.example.navigatorappandroid.retrofit.response.MapTextResponse;
import com.example.navigatorappandroid.retrofit.response.TextListResponse;
import com.example.navigatorappandroid.retrofit.response.TimersListResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import com.example.navigatorappandroid.retrofit.response.VacancyListResponse;
import com.example.navigatorappandroid.retrofit.response.VoteResponse;
import java.security.Principal;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GeneralApi {

    @GET("api/templates/get")
    Call<VacancyListResponse> getTemplatesList();

    @PUT("api/moderator")
    Call<ResultErrorsResponse> setModerator();

    @POST("api/system/code/add")
    Call<ResultErrorsResponse> addMessageCodeName(@Query("code") String codeName);

    @POST("api/system/message/add")
    Call<ResultErrorsResponse> addInProgramMessage(@Body InProgramMessageRequest inProgramMessageRequest);

    @POST("api/language/add")
    Call<ResultErrorsResponse> addLanguage(@Query("language") String language);

    @PUT("api/profile/password/change")
    Call<ResultErrorsResponse> changePassword(@Body ChangePasswordRequest changePasswordRequest); // добавить на backend

    @Multipart
    @PUT("api/profile/avatar")
    Call<AvatarResponse> profileAvatar(@Part MultipartBody.Part avatar);

    @PUT("api/profile")
    Call<ResultErrorsResponse> profile(@Body ProfileRequest profileRequest);

    @PUT("api/status/check")
    Call<ResultErrorsResponse> checkEmployeeStatus();

    @POST("api/professions/name/add")
    Call<ResultErrorsResponse> addProfessionInSpecifiedLanguage(@Body ProfessionRequest professionRequest);

    @POST("api/professions/new/add")
    Call<ResultErrorsResponse> addNewProfessionId();

    @PUT("api/block")
    Call<ResultErrorsResponse> blockUser(@Body ModeratorDecision moderatorDecision);

    @PUT("api/unblock")
    Call<ResultErrorsResponse> unblockUser(@Body ModeratorDecision moderatorDecision);

    @PUT("api/location")
    Call<ResultErrorsResponse> updateLocation(@Body LocationRequest locationRequest);

    @POST("api/restore")
    Call<ResultErrorsResponse> getRecoveryCode(@Query("email") String email);

    @POST("api/password")
    Call<ResultErrorsResponse> changePassword(@Body PasswordRequest passwordRequest);

    @POST("api/like")
    Call<VoteResponse> like(@Body VoteRequest voteRequest);

    @POST("api/dislike")
    Call<VoteResponse> dislike(@Body VoteRequest voteRequest);

    @POST("api/comment")
    Call<ResultErrorsResponse> comment(@Body CommentRequest commentRequest);

    @GET("api/get/{id}/comments")
    Call<CommentsListResponse> getCommentsListByUserId(@Query("id") String id);

    @POST("api/employer/search/passive")
    Call<ResultErrorsResponse> setPassiveSearch(@Body VacancyRequest vacancyRequest);

    @POST("api/user/{id}/favorite/{decision}")
    Call<ResultErrorsResponse> favorite(@Query("id") String id, @Query("decision") String decision);

    @POST("api/user/{id}/blacklist/{decision}")
    Call<ResultErrorsResponse> blacklist(@Query("id") String id, @Query("decision") String decision);

    @GET("api/user/{id}/relationship/status")
    Call<RelationshipStatusResponse> getRelationshipStatus(@Query("id") String id);

    @GET("api/user/sender/get")
    Call<User> getSender(Principal principal);

    @GET("api/user/recipient/{id}/get")
    Call<User> getRecipient(@Path("recipientId") long recipientId);

    @GET("api/jobs")
    Call<JobListResponse> getJobList();

    @DELETE("api/delete")
    Call<ResultErrorsResponse> checkAndDeleteNotConfirmedJobs();

    @GET("api/language/get")
    Call<StringResponse> getUsersInterfaceLanguage();

    @GET("api/system/text/get")
    Call<MapTextResponse> checkAndGetTextListInSpecifiedLanguage(@Body TextListInSpecifiedLanguageRequest textList);

    @GET("api/languages/list/get")
    Call<TextListResponse> getLanguagesList();

    @GET("api/professions/names/list/get")
    Call<ProfessionNamesListResponse> getProfessionsNamesInSpecifiedLanguage();

    @GET("api/profession/get/by/name")
    Call<IdResponse> getProfessionIdByName(@Body String professionName);

    @GET("api/vacancy/info/get")
    Call<StringResponse> getAdditionalInfoAboutVacancyInSpecifiedLanguage(@Body ProfessionToUserRequest professionToUserRequest);

    @GET("api/profession/{id}/name/get")
    Call<StringResponse> getProfessionNameInSpecifiedLanguage(@Path("id") long id);

    @GET("api/info/from/employee/get")
    Call<StringResponse> getInfoFromEmployeeInEmployersLanguage(@Body ProfessionToUserRequest professionToUserRequest);

    @GET("api/professions/to/user/get")
    Call<StringResponse> getProfessionsToUserInEmployersLanguage(@Body long id);

    @GET("api/profession/to/user/get")
    Call<ProfessionToUserResponse> getProfessionToUser(@Body ProfessionToUserRequest professionToUserRequest);

    @POST("api/profession/to/user/post")
    Call<ResultErrorsResponse> postProfessionToUser(@Body ProfessionToUserRequest professionToUserRequest);

    @DELETE("api/profession/to/user/delete")
    Call<ResultErrorsResponse> deleteProfessionToUser(@Body ProfessionToUserRequest professionToUserRequest);

    @GET("api/system/message/get")
    Call<StringResponse> getMessageInSpecifiedLanguage(@Body InProgramMessageRequest inProgramMessageRequest);

    @PUT("api/auth/account/activate/{id}")
    Call<StringResponse> activateAccount(@Path("id") Long email);

    @GET("api/user/get")
    Call<UserInfoResponse> getUserInfo();// изменить получение аватара на backend!

    @GET("api/user/avatar/{path}/get")
    Call<ResponseBody> getUserAvatar(@Path("path") String path);// изменить получение аватара на backend!

    @PUT("api/user/display/change")
    Call<ResultErrorsResponse> changeWorkDisplay(); // добавить на backend!

    @PUT("api/user/employee/work/info/change")
    Call<ResultErrorsResponse> changeInfoFromEmployeeForEmployers
            (@Body EmployeeInfoForEmployersRequest employeeInfoForEmployersRequest); // добавить на backend!

    @PUT("api/status")
    Call<ResultErrorsResponse> employeeStatus(@Body StatusRequest statusRequest);

    @GET("api/user/timers/list/get")
    Call<TimersListResponse> getTimersList( );

    @GET("api/language/get/by/name")
    Call<IdResponse> getLanguageIdByName(String name);
}
