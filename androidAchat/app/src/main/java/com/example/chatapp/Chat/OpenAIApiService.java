package com.example.chatapp.Chat;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface OpenAIApiService {
        @Headers({"Authorization: Bearer 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", "Content-Type: application/json"})
        @POST("/v1/completions")
        Call<ApiResponse> createResponseChat35(@Body ApiRequest body);
        @Headers({"Authorization: Bearer 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", "Content-Type: application/json"})
        @POST("/v1/chat/completions")
        Call<ApiResponse> createResponseChat4(@Body ApiRequest body);
}
