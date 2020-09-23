package com.example.healthcoach01;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import at.grabner.circleprogress.TextMode;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class SettingTab extends Fragment {
    Switch Lock, Alarm;
    ImageView LockImage, AlarmImage;
    TextView Nick, Id, Info, Logout, Notice;
    String Nickname, Key, LockNumber;
    DatabaseReference mDatabase;
    boolean Lockornot = false, Alarmornot;
    String sheight;
    float height;
    float bmi;
    String sweight;
    float weight;
    TextView bmitxt;

    public static final String PREFS_NAME = "SETTING";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.settingtab, container, false);
        Notice = view.findViewById(R.id.notice);
        Notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"We are currently working on this page.",Toast.LENGTH_SHORT).show();
            }
        });
        Nick = view.findViewById(R.id.nick);
        bmitxt = view.findViewById(R.id.bmi);
        Id = view.findViewById(R.id.id);
        Info = view.findViewById(R.id.info);
        Logout = view.findViewById(R.id.logout);
        Logout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                SharedPreferences settings = getActivity().getSharedPreferences("UserInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("KeepLogin",false);
                editor.apply();

                Lockornot = false;
                Alarmornot = false;

                getActivity().finish();

            }
        });

        SharedPreferences preferences = getActivity().getSharedPreferences("UserInfo", 0);
        Nickname = preferences.getString("Nickname","");
        Key = preferences.getString("Key","");

        Nick.post(new Runnable() {
            @Override
            public void run() {
                Nick.setText("I'm, " + Nickname);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("example");

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final HashMap<String,String> LoginData = (HashMap<String, String>) dataSnapshot.getValue();
                if(dataSnapshot.getKey().contentEquals(Key)){
                    Id.post(new Runnable() {
                        @Override
                        public void run() {
                            Id.setText("ID : " + LoginData.get("ID"));
                        }
                    });

                    Info.post(new Runnable() {
                        @Override
                        public void run() {
                            Info.setText("Gender : " + LoginData.get("Gender") +" / Age : "+LoginData.get("Age"));
                        }
                    });

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



        Lock = view.findViewById(R.id.lockSwitch);
        LockImage = view.findViewById(R.id.lockImage);
        Lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    LockImage.setImageResource(R.drawable.lock_on);
                    Lockornot = true;
                }
                else {
                    LockImage.setImageResource(R.drawable.lock_off);
                    Lockornot = false;
                }
            }
        }); //암호 잠금
        Lock.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(Lockornot == true){
                    Intent in = new Intent(getActivity(),SetLock.class);
                    startActivityForResult(in,1234);
                }
                else {
                    LockImage.setImageResource(R.drawable.lock_off);
                }
            }
        });

        Alarm = view.findViewById(R.id.alarmSwitch);
        AlarmImage = view.findViewById(R.id.alarmImage);
        Alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    AlarmImage.setImageResource(R.drawable.alert_on);
                    Alarmornot = true;
                }
                else {
                    AlarmImage.setImageResource(R.drawable.alert_off);
                    Alarmornot = false;
                }
            }
        });
        Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Alarmornot == true){
                    Intent in = new Intent(getActivity(),SetAlarm.class);
                    startActivityForResult(in,123);
                }
                else {
                    AlarmImage.setImageResource(R.drawable.alert_off);
                }
            }
        });




        mDatabase = FirebaseDatabase.getInstance().getReference().child("example");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                HashMap<String, String> LoginData2 = (HashMap<String, String>) dataSnapshot.getValue();
                if (dataSnapshot.getKey().contentEquals(Key)) {
                    sheight = LoginData2.get("Height");
                    height = Float.valueOf(sheight);
                    sweight = LoginData2.get("Weight");
                    weight = Float.valueOf(sweight);


                    HashMap data = new HashMap<>();

                    long now = System.currentTimeMillis();
                    // 현재시간을 date 변수에 저장한다.
                    Date date = new Date(now);
                    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
                    // nowDate 변수에 값을 저장한다.
                    final String formatDate = sdfNow.format(date);

                    bmi = weight / ((height/100) * (height/100));
                    String bmistr = String.format("%.2f", (bmi));

                    data.put("BMI", bmistr);
                    ////firebase에 저장

                    bmitxt.setText("BMI : " + bmistr);
                    FirebaseDatabase.getInstance().getReference().child("example/" + dataSnapshot.getKey()).updateChildren(data);
                    //setValue(data);

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


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 123) {
            if(resultCode == RESULT_OK) {
                AlarmImage.post(new Runnable() {
                    @Override
                    public void run() {
                        AlarmImage.setImageResource(R.drawable.alert_on);
                    }
                });
            } else if (resultCode == RESULT_CANCELED){
                Alarm.post(new Runnable() {
                    @Override
                    public void run() {
                        Alarm.setChecked(false);
                    }
                });
                AlarmImage.post(new Runnable() {
                    @Override
                    public void run() {
                        AlarmImage.setImageResource(R.drawable.alert_off);
                    }
                });
                Alarmornot = false;
            }
        }
        else if(requestCode==1234){
            if(resultCode == RESULT_OK) {

                LockNumber = data.getStringExtra("LockNum");
                SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Number",LockNumber);
                editor.apply();

                Log.d("LockNumber",LockNumber);

                LockImage.post(new Runnable() {
                    @Override
                    public void run() {
                        LockImage.setImageResource(R.drawable.lock_on);
                    }
                });
            } else if (resultCode == RESULT_CANCELED){
                Lock.post(new Runnable() {
                    @Override
                    public void run() {
                        Lock.setChecked(false);
                    }
                });
                LockImage.post(new Runnable() {
                    @Override
                    public void run() {
                        LockImage.setImageResource(R.drawable.lock_off);
                    }
                });
                Lockornot = false;
            }
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.i("Main", "onStop()");
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean("LOCK",Lockornot);
        editor.putBoolean("ALARM",Alarmornot);
        editor.putString("Number",LockNumber);

        editor.apply();
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.i("Main", "onStart()");

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME,MODE_PRIVATE);

        Lockornot = settings.getBoolean("LOCK",false);
        Alarmornot = settings.getBoolean("ALARM",false);
        LockNumber = settings.getString("Number","");
        Log.d("Lockornot",Lockornot+"");
        Log.d("Alarmornot",Alarmornot+"");



        if(Lockornot == false)
            Lock.setChecked(false);
        else
            Lock.setChecked(true);

        if(Alarmornot == false)
            Alarm.setChecked(false);
        else
            Alarm.setChecked(true);

    }

}