package com.example.healthcoach01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class Join extends AppCompatActivity {

    private EditText name,id,pw,nickname,age,height,weight;
    private String Name,Id,Pw,Nick,Gender,Age,Height,Weight;
    private boolean checked;
    DatabaseReference mDatabase;
    DatabaseReference mDatabase2;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);

        name = findViewById(R.id.name);
        nickname = findViewById(R.id.nick);
        id = findViewById(R.id.id);
        pw = findViewById(R.id.pw);
        age = findViewById(R.id.age);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);


        //firebase 정의
        mDatabase = FirebaseDatabase.getInstance().getReference().child("example");


    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.sign:
                Name = name.getText().toString();
                Id = id.getText().toString();
                Pw = pw.getText().toString();
                Nick = nickname.getText().toString();
                Age = age.getText().toString();
                Height = height.getText().toString();
                Weight = weight.getText().toString();

                if(name.length()<1 || id.length()<1 || pw.length()<1  || nickname.length()<1 || age.length()<1 || !checked || height.length()<1 || weight.length()<1)
                    Toast. makeText (getApplicationContext(), "There is a blank", Toast. LENGTH_SHORT ).show();
                else{
                    mDatabase.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            HashMap<String,String> LoginData = (HashMap<String, String>) dataSnapshot.getValue();
                            if(LoginData.get("ID").contentEquals(Id)) {
                                Toast.makeText(getApplicationContext(), "This ID already exists. Please change your ID", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //hashmap 만들기
                                HashMap result = new HashMap<>();
                                result.put("Name",Name);
                                result.put("ID", Id);
                                result.put("Password", Pw);
                                result.put("Nickname",Nick);
                                result.put("Age",Age);
                                result.put("Gender",Gender);
                                result.put("Height",Height);
                                result.put("Weight",Weight);

                                Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
                                //firebase에 저장
                                mDatabase.push().setValue(result);
                                finish();


                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                break;
        }
    }

    public void onRadioCheck(View v){
        checked = ((RadioButton)v).isChecked();

        switch(v.getId()){
            case R.id.radio_male:
                if(checked)
                    Gender = "male";
                break;
            case R.id.radio_female:
                if(checked)
                    Gender = "female";
                break;
        }

    }

}