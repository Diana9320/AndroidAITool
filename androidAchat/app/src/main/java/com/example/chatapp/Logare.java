package com.example.chatapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Logare extends AppCompatActivity {
   Button btnLogare;
   EditText Email ;
   EditText Pass;
   TextView Test;
   @Override
    protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.sample_logare);
      btnLogare = findViewById(R.id.btnSendd);
        Email = findViewById(R.id.txtEmail);
        Pass = findViewById(R.id.txtPass);
       Test = findViewById(R.id.lbltest);
      // lstView = findViewById(R.id.lstview);

       btnLogare.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              // Intent intent= new Intent(Logare.this,MainActivity.class);
               //startActivity(intent);
               }
       });
   }


}