package com.example.chatapp;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.chatapp.Chat.ApiRequest;
import com.example.chatapp.Chat.ApiResponse;
import com.example.chatapp.Chat.OpenAIApiService;
import com.example.chatapp.Compilor.ApiRequestCompilor;
import com.example.chatapp.Compilor.ApiResponseCompilor;
import com.example.chatapp.Compilor.CompilorApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    String opt="Selecteaza Limbaj";
    TextView messageViewCompilor;
    String txt="";
    ConstraintLayout layout;
    RadioButton radioChat35;
    RadioButton radioChat4;
    EditText editeText,chatResp;
    Button sendBtn,imgBtn,btnCompile, btnLbj;
    private Spinner dropBoxLbj;
    String textDeCompilat;
    private String altLimbajSelected="";
    String [] lbj={opt,"c","cpp","java","python3","csharp","sql","php","fsharp","kotlin",
    "go","rust","swift"};
    private String outputMessage = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        layout =findViewById(R.id.main);
        dropBoxLbj = findViewById(R.id.spinner);
        messageViewCompilor = findViewById(R.id.txtCompilor);
        editeText = findViewById(R.id.editText);
        sendBtn = findViewById(R.id.btnSendd);
        btnCompile = findViewById(R.id.btnCompile);
        imgBtn = findViewById(R.id.btnAiImg);
        chatResp = findViewById(R.id.chatTextResponse);
        radioChat4 = findViewById(R.id.radioChat4);
        radioChat35 = findViewById(R.id.radioChat3);
        btnLbj = findViewById(R.id.btnDiffLbj);

        ArrayAdapter adapter= new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, lbj);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropBoxLbj.setAdapter(adapter);
        editeText.setText("");
        //buton pentru a generare cod
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder str= new StringBuilder("Genereaza cod compilabil fara comentarii ");
                String l=dropBoxLbj.getSelectedItem().toString();
                if (!dropBoxLbj.getSelectedItem().toString().equals(opt)) {
                    str.append(" in ");
                    str.append(l);
                    str.append(", fara explicatii suplimentare si necesitatea introducerii de valori de la tastatura, pentru: ");
                }else {
                    str.append(", fara explicatii suplimentare si necesitatea introducerii de valori de la tastatura, pentru: ");
                }
                String promptText1 = editeText.getText().toString();
                str.append(promptText1);
                String promptText=str.toString();
                if(!editeText.getText().toString().trim().isEmpty()) {
                    if (radioChat35.isChecked())
                        fetchResponseFromOpenAIV35(promptText);
                    else if (radioChat4.isChecked())
                        fetchResponseFromOpenAIV4(promptText);
                }
                else
                    chatResp.setText("Câmp MESAJ necompletat, nu s-a putut genera cod!");
                editeText.setText("");
                str.setLength(0);
            }
        });

        //buton compilare
        btnCompile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dropBoxLbj.getSelectedItem().equals(opt))
                {
                    fetchResponseFromCompilor(chatResp.getText().toString(),dropBoxLbj.getSelectedItem().toString());
                }else
                    createPopupWindow("Selecteaza limbaj pentru compilare" );

            }
        });
        //buton schimbare limbaj
        btnLbj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageScrollBar();
            }
        });
    }

    private void fetchResponseFromOpenAIV35(String prompt) {
        chatResp.setText("");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OpenAIApiService service = retrofit.create(OpenAIApiService.class);
        ApiRequest request = new ApiRequest();
        request.setModel("gpt-3.5-turbo-instruct");
        request.setPrompt(prompt);
        request.setMax_tokens(3000);
        request.setTemperature(0.0);

        Call<ApiResponse> call = service.createResponseChat35(request);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    if (!apiResponse.getChoices().isEmpty()) {
                        String text = apiResponse.getChoices().get(0).getText().trim();
                        runOnUiThread(() -> chatResp.setText(text));
                    } else {
                        runOnUiThread(() -> chatResp.setText("No text available"));
                    }
                } else {
                    runOnUiThread(() -> chatResp.setText("Response not successful"));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API Error", "nu s-a putut obtine raspuns ", t);
                Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchResponseFromOpenAIV4(String prompt) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OpenAIApiService service = retrofit.create(OpenAIApiService.class);

        List<ApiRequest.Message> messages = new ArrayList<>();
        messages.add(new ApiRequest.Message("user", prompt));

        ApiRequest request = new ApiRequest();
        request.setModel("gpt-4-turbo");
        request.setMessages(messages);
        request.setMax_tokens(3000);
        request.setTemperature(0.0);

        Call<ApiResponse> call = service.createResponseChat4(request);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    List<ApiResponse.Choice> choices = apiResponse.getChoices();
                    if (choices != null && !choices.isEmpty()) {
                        StringBuilder allTexts = new StringBuilder();
                        for (ApiResponse.Choice choice : choices) {

                            ApiResponse.Choice.Message messages = choice.getMessage();
                            if (messages != null) {

                                    allTexts.append(messages.getContent());
                                    allTexts.append("\n");
                            }
                        }
                        runOnUiThread(() -> chatResp.setText(allTexts.toString()));
                    } else {
                        runOnUiThread(() -> chatResp.setText("No messages available"));
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show());
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API Error", "nu s-a putut obtine rasp ", t);

                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Error connect server", Toast.LENGTH_SHORT).show();
                    chatResp.append("\non Failure to connect");
                });

            }
        });
    }

    private void fetchResponseFromCompilor(String script,String lbj){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.jdoodle.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CompilorApiService service = retrofit.create(CompilorApiService.class);

        ApiRequestCompilor request = new ApiRequestCompilor();
        request.setClientId("4c147c54cb3c8ac13a75337ed85b1e20");
        request.setClientSecret("9339ae4501ce321f41cd979f6198d6f16a93e502dda641ea1a433bd708984dba");
        request.setLanguage(lbj);
        request.setScript(script);
      //  request.setVersionIndex("3");

        Call<ApiResponseCompilor> call = service.createCompiler(request);
        call.enqueue(new Callback<ApiResponseCompilor>() {
            @Override
            public void onResponse(Call<ApiResponseCompilor> call, retrofit2.Response<ApiResponseCompilor> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponseCompilor apiResponseCompilor = response.body();
                    if (!apiResponseCompilor.getOutput().isEmpty()) {
                        String text = apiResponseCompilor.getOutput().toString();
                        runOnUiThread(() -> messageViewCompilor.setText(text+"\ntimp executie "+apiResponseCompilor.getCpuTime() ));
                        createPopupWindow(text+"\ntimp proces "+apiResponseCompilor.getCpuTime() );
                    } else {
                        runOnUiThread(() -> messageViewCompilor.setText("No text available"));
                    }
                } else {
                    runOnUiThread(() -> messageViewCompilor.setText("Response not successful"));
                }
            }
            @Override
            public void onFailure(Call<ApiResponseCompilor> call, Throwable t) {
                Log.e("API Error", "nu s-a putut obtine raspuns ", t);
                Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                runOnUiThread(() -> messageViewCompilor.setText("nu merge"));
            }
        });
    }

    private String fetchResponseFromCompilorReturn(String script,String lbj){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.jdoodle.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CompilorApiService service = retrofit.create(CompilorApiService.class);

        ApiRequestCompilor request = new ApiRequestCompilor();
        request.setClientId("4c147c54cb3c8ac13a75337ed85b1e20");
        request.setClientSecret("9339ae4501ce321f41cd979f6198d6f16a93e502dda641ea1a433bd708984dba");
        request.setLanguage(lbj);
        request.setScript(script);
        //  request.setVersionIndex("3");
        StringBuilder stringBuilder=new StringBuilder("");
        Call<ApiResponseCompilor> call = service.createCompiler(request);
        call.enqueue(new Callback<ApiResponseCompilor>() {
            @Override
            public void onResponse(Call<ApiResponseCompilor> call, retrofit2.Response<ApiResponseCompilor> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponseCompilor apiResponseCompilor = response.body();
                    if (!apiResponseCompilor.getOutput().isEmpty()) {
                        String text = apiResponseCompilor.getOutput().toString();
                        stringBuilder.append(text+"\ntimp executie "+apiResponseCompilor.getCpuTime() );
                        txt=stringBuilder.toString();
                    } else {
//                        runOnUiThread(() -> messageViewCompilor.setText("No text available"));
                        txt="No test available";
                    }
                } else {
                    runOnUiThread(() -> messageViewCompilor.setText("Response not successful"));
                    txt="No response";
                }
            }
            @Override
            public void onFailure(Call<ApiResponseCompilor> call, Throwable t) {
                Log.e("API Error", "nu s-a putut obtine raspuns ", t);
                Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                runOnUiThread(() -> messageViewCompilor.setText("nu merge"));
            }
        });
       // Toast.makeText(MainActivity.this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
        return txt;
    }
    private void createPopupWindow(String text){
        LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popup =inflater.inflate(R.layout.popup_window, null);

       TextView textViewPopup = popup.findViewById(R.id.textViewPopup);
       textViewPopup.setText(text);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height =ViewGroup.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;

        PopupWindow popupWindow=new PopupWindow(popup,width,height,focusable)  ;
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.blue(44)));
        layout.post(new Runnable(){
            @Override
            public void run(){
                popupWindow.showAtLocation(layout, Gravity.BOTTOM,0,0);
            }
        });
    }

    private void languageScrollBar(){
        StringBuilder txt= new StringBuilder("Rescrie codul urmator fara comentarii ");
        txt.append(chatResp.getText());
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Limbajele de programare accesibile.");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, lbj);
        builder.setAdapter(arrayAdapter,(dialog, which)->{
           altLimbajSelected=lbj[which];
            dropBoxLbj.setSelection(which);
        });
        builder.setOnDismissListener(dialog -> {
            if (altLimbajSelected.equals("")) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Nu ai selectat limbaj", Toast.LENGTH_SHORT).show());
            } else {
               // chatResp.setText("Limbajul selectat este " + altLimbajSelected);
                txt.append(" in limbajul " + altLimbajSelected.toString() + " fara explicatii suplimentare si compilabil");
                    if (radioChat35.isChecked())
                        fetchResponseFromOpenAIV35(txt.toString());
                    else if (radioChat4.isChecked())
                        fetchResponseFromOpenAIV4(txt.toString());
            }
        });
        AlertDialog dialog =builder.create();
        dialog.show();
    }
}
