package com.example.chatapp.Compilor;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CompilorApiService {
    @POST("/v1/execute")
    Call<ApiResponseCompilor> createCompiler(@Body ApiRequestCompilor body);
}
