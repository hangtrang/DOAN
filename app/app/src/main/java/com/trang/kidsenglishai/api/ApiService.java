package com.trang.kidsenglishai.api;

import com.trang.kidsenglishai.model.Achievement;
import com.trang.kidsenglishai.model.PracticeHistory;
import com.trang.kidsenglishai.model.QuizQuestion;
import com.trang.kidsenglishai.model.Topic;
import com.trang.kidsenglishai.model.User;
import com.trang.kidsenglishai.model.Vocabulary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("auth/login.php")
    Call<ApiResponse<User>> login(@Body LoginRequest request);

    @POST("auth/register.php")
    Call<ApiResponse<User>> register(@Body RegisterRequest request);
//
//    @GET("topics.php")
//    Call<ApiResponse<List<Topic>>> getTopics();
//
//    @GET("words.php")
//    Call<ApiResponse<List<Vocabulary>>> getWords(@Query("topic_id") int topicId);
//
//    @GET("get_achievement.php")
//    Call<ApiResponse<Achievement>> getAchievement(@Query("user_id") int userId);
//
//    @POST("save_achievement.php")
//    Call<ApiResponse<Object>> saveAchievement(@Body AchievementRequest request);
//
//    @POST("save_practice.php")
//    Call<ApiResponse<Object>> savePractice(@Body PracticeRequest request);
//
//    @GET("practice_history.php")
//    Call<ApiResponse<List<PracticeHistory>>> getPracticeHistory(@Query("user_id") int userId);
//
//    @GET("quiz_questions.php")
//    Call<ApiResponse<List<QuizQuestion>>> getQuizQuestions();
//
//    @POST("save_quiz_result.php")
//    Call<ApiResponse<Object>> saveQuizResult(@Body QuizSubmitRequest request);
//
//    @POST("auth/update_profile.php")
//    Call<ApiResponse<User>> updateProfile(@Body UpdateProfileRequest request);
//
//    @POST("auth/reset_password.php")
//    Call<ApiResponse<Object>> resetPassword(@Body ResetPasswordRequest request);









    @POST("auth/update_profile.php")
    Call<ApiResponse<User>> updateProfile(
            @Body UpdateProfileRequest request
    );

    @POST("auth/reset_password.php")
    Call<ApiResponse<Object>> resetPassword(
            @Body ResetPasswordRequest request
    );

    @GET("topics.php")
    Call<ApiResponse<List<Topic>>> getTopics();

    @GET("vocabularies.php")
    Call<ApiResponse<List<Vocabulary>>> getWords(
            @Query("topic_id") int topicId
    );

    @GET("get_achievement.php")
    Call<ApiResponse<Achievement>> getAchievement(
            @Query("user_id") int userId
    );

    @POST("save_achievement.php")
    Call<ApiResponse<Object>> saveAchievement(
            @Body AchievementRequest request
    );

    @POST("save_practice.php")
    Call<ApiResponse<Object>> savePractice(
            @Body PracticeRequest request
    );

    @GET("practice_history.php")
    Call<ApiResponse<List<PracticeHistory>>> getPracticeHistory(
            @Query("user_id") int userId
    );

    @GET("quiz_questions.php")
    Call<ApiResponse<List<QuizQuestion>>> getQuizQuestions();

    @POST("save_quiz_result.php")
    Call<ApiResponse<Object>> saveQuizResult(
            @Body QuizSubmitRequest request
    );
}
