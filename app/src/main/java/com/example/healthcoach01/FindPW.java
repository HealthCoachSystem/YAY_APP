package com.example.healthcoach01;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class FindPW extends AppCompatActivity {

    private EditText name,id;
    private String Name,Id;
    DatabaseReference mDatabase;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.findpw);

        name = findViewById(R.id.name);
        id = findViewById(R.id.id);

        //firebase 정의
        mDatabase = FirebaseDatabase.getInstance().getReference().child("example");

    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.send:
                Name = name.getText().toString();
                Id = id.getText().toString();

                mDatabase.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        HashMap<String,String> LoginData = (HashMap<String, String>) dataSnapshot.getValue();
                        if(LoginData.get("Name").contentEquals(Name)) {
                            if (LoginData.get("ID").contentEquals(Id)) {
                                Toast.makeText(getApplicationContext(), "your password is " + LoginData.get("Password"), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
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
                break;
        }
    }

}