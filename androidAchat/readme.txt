//interogare Chat care merge cu biblioteca retrofit

//--clasa ApiRequest
package com.example.chatapp.Chat;

public class ApiRequest {
    private String model;
    private String prompt;
    private int max_tokens;
    private double temperature;

    public ApiRequest() {
    }

//    public ApiRequest(String model, String prompt, int max_tokens, double temperature) {
//        this.model = model;
//        this.prompt = prompt;
//        this.max_tokens = max_tokens;
//        this.temperature = temperature;
//    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getMax_tokens() {
        return max_tokens;
    }

    public void setMax_tokens(int max_tokens) {
        this.max_tokens = max_tokens;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}



--clasa ApiResponse
package com.example.chatapp.Chat;

import java.util.List;

public class ApiResponse {
    private List<Choice> choices;

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public static class Choice {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

    }
}


//--interfata OpenAIApiService
package com.example.chatapp.Chat;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface OpenAIApiService {
        @Headers({"Authorization: Bearer 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", "Content-Type: application/json"})
        @POST("/v1/completions")
        Call<ApiResponse> createCompletion(@Body ApiRequest body);

}


//-- functie implemetare main

 private void fetchResponseFromOpenAI(String prompt) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenAIApiService service = retrofit.create(OpenAIApiService.class);

        ApiRequest request = new ApiRequest();
        request.setModel("gpt-3.5-turbo-instruct");
        request.setPrompt(prompt);
        request.setMax_tokens(7);
        request.setTemperature(0.0);

        Call<ApiResponse> call = service.createCompletion(request);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                if( response.isSuccessful() && response.body() !=null){
                    String text = response.body().getChoices().get(0).getText().trim();
                    runOnUiThread(()->messageView.setText(text));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API Error","nu s-a putut obtine rasp ",t);
            }
        });

    }
