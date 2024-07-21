package com.example.chatapp;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.Chat.ApiRequest;
import com.example.chatapp.Chat.ApiResponse;
import com.example.chatapp.Chat.OpenAIApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class retroChat extends AppCompatActivity {

    EditText editeText;
    Button sendBtn;
    private String outputMessage="";
    // public static final MediaType JSON = MediaType.get("application/json");

    // OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


      //  messageView = findViewById(R.id.textViewChat35);
        editeText = findViewById(R.id.editText);
        sendBtn = findViewById(R.id.btnSendd);

    }

}

