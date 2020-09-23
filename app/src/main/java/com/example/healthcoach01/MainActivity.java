package com.example.healthcoach01;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private EditText id;
    private EditText pw;
    private String ID,PW;
    DatabaseReference mDatabase;
    String Nickname;
    String sheight;
    float height;

    String Key;
    private double exercisekmCal, exercisekmCal2; //운동거리 계산
    private double avgStepKm; //평균 운동거리
    private boolean mLive = true;

    DatabaseReference mDatabase2;

    boolean LoginStatus = false;

    private class ExData {

        int value1, value2, value3;
        String key;

        ExData(String key, int a, int b, int c) {
            this.key = key;
            this.value1 = a;
            this.value2 = b;
            this.value3 = c;
        }
    }

    final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            final SharedPreferences preferences = getSharedPreferences("UserInfo", 0);
            SharedPreferences sharedPreferences;
            Key = preferences.getString("Key", "");

            if (Intent.ACTION_DATE_CHANGED.equals(action)){

                sp = getSharedPreferences("sp", MODE_PRIVATE);
                editor = sp.edit();
                editor.putInt("times", 0);
                editor.putString("kcals", String.valueOf(0.00));
                editor.putString("SQUAT", "0");
                editor.putString("SIDEBEND", "0");
                editor.putString("SITUPS", "0");
                editor.putString("SIDECRUNCH", "0");
                editor.putString("PUSHUPS", "0");

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                editor = sharedPreferences.edit();
                String key = null;
                editor.putString(key, "0");
                editor.commit();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = findViewById(R.id.id);
        pw = findViewById(R.id.pw);
        //firebase 정의
        mDatabase = FirebaseDatabase.getInstance().getReference().child("example");
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Rank");
        SharedPreferences preferences = this.getSharedPreferences("UserInfo", 0);
        Nickname = preferences.getString("Nickname","");
        LoginStatus = preferences.getBoolean("KeepLogin",false);



        if(mLive){
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_DATE_CHANGED);
            getApplicationContext().registerReceiver(mIntentReceiver, filter);
        }

        if(LoginStatus){
            mDatabase.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    SharedPreferences settings = getSharedPreferences("UserInfo", MODE_PRIVATE);
                    String key = settings.getString("Key","");
                    if(key.contentEquals(dataSnapshot.getKey())){
                        HashMap data = new HashMap<>();

                        long now = System.currentTimeMillis();
                        //// 현재시간을 date 변수에 저장한다.
                        Date date = new Date(now);
                        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
                        // nowDate 변수에 값을 저장한다.
                        final String formatDate = sdfNow.format(date);

                        data.put("SQUAT", sqtvalue(today()));
                        data.put("SIDEBEND",sbvalue(today()));
                        data.put("SITUPS", situpsvalue(today()));
                        data.put("SIDECRUNCH", scvalue(today()));
                        data.put("PUSHUPS",pusvalue(today()));


                        data.put("Kcal", String.format("%.2f", kcalvalue(today())));
                        data.put("Time", Integer.toString(timevalue(today())));
                        data.put("Step", Integer.toString(getPreferences(today())));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            data.put("Timestamp", Timestamp.valueOf(formatDate + " 0:0:0.0").toInstant().getEpochSecond());
                            FirebaseDatabase.getInstance().getReference().child("Data/" + dataSnapshot.getKey() + "/" + formatDate).setValue(data);}



                        ////firebase에 저장
                        Intent in = new Intent(MainActivity.this, Mainpage.class);
                        startActivity(in);
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

            //닉네임이랑 스텝 수 매핑
            mDatabase2.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    HashMap data = new HashMap<>();

                    long now = System.currentTimeMillis();
                    // 현재시간을 date 변수에 저장한다.
                    Date date = new Date(now);
                    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
                    // nowDate 변수에 값을 저장한다.
                    final String formatDate = sdfNow.format(date);

                    //data.put("Step",Integer.toString(10000));
                    data.put("Step", Integer.toString(getPreferences(today())));
                    data.put("Nickname", Nickname);

                    ////firebase에 저장
                    FirebaseDatabase.getInstance().getReference().child("Rank/" + Nickname + "/" + formatDate).setValue(data);

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

            //final SharedPreferences preferences = this.getSharedPreferences("UserInfo", 0);
            Nickname = preferences.getString("Nickname", "");
            Key = preferences.getString("Key", "");
            //km 계산
            mDatabase = FirebaseDatabase.getInstance().getReference().child("example");
            mDatabase.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    HashMap<String, String> LoginData2 = (HashMap<String, String>) dataSnapshot.getValue();
                    if (dataSnapshot.getKey().contentEquals(Key)) {
                        sheight = LoginData2.get("Height");
                        height = Float.valueOf(sheight);

                        exercisekmCal = (height * 0.37); //보폭 수 계산1
                        exercisekmCal2 = (height - 100); //보폭 수 계산2
                        avgStepKm = Double.parseDouble(String.format("%.1f", (((exercisekmCal + exercisekmCal2) / 2) * (getPreferences(today()))) / 100000));
                        //보폭 수 계산1,2 평균 값 구해서 stepcount값과 계산

                        HashMap data = new HashMap<>();

                        long now = System.currentTimeMillis();
                        // 현재시간을 date 변수에 저장한다.
                        Date date = new Date(now);
                        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
                        // nowDate 변수에 값을 저장한다.
                        final String formatDate = sdfNow.format(date);

                        data.put("Km", avgStepKm);
                        ////firebase에 저장
                        FirebaseDatabase.getInstance().getReference().child("Km/" + Nickname + "/" + formatDate).setValue(data);
                        FirebaseDatabase.getInstance().getReference().child("Data/" + dataSnapshot.getKey() + "/" + formatDate).updateChildren(data);
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
        }


    }


    public void onClick(View v){
        switch(v.getId()) {
            case R.id.logbt:
                ID = id.getText().toString();
                PW = pw.getText().toString(); //id,pw 받아서 string

                mDatabase.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        HashMap<String, String> LoginData = (HashMap<String, String>) dataSnapshot.getValue();
                        if (LoginData.get("ID").contentEquals(ID))
                            if (LoginData.get("Password").contentEquals(PW)) {
                                SharedPreferences settings = getSharedPreferences("UserInfo", MODE_PRIVATE);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("Nickname", LoginData.get("Nickname"));
                                editor.putString("Key", dataSnapshot.getKey());
                                editor.putBoolean("KeepLogin",true);
                                editor.apply();

                                HashMap data = new HashMap<>();

                                long now = System.currentTimeMillis();
                                //// 현재시간을 date 변수에 저장한다.
                                Date date = new Date(now);
                                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
                                // nowDate 변수에 값을 저장한다.
                                final String formatDate = sdfNow.format(date);

                                data.put("SQUAT", sqtvalue(today()));
                                data.put("SIDEBEND",sbvalue(today()));
                                data.put("SITUPS", situpsvalue(today()));
                                data.put("SIDECRUNCH", scvalue(today()));
                                data.put("PUSHUPS",pusvalue(today()));


                                data.put("Kcal", String.format("%.2f", kcalvalue(today())));
                                data.put("Time", Integer.toString(timevalue(today())));
                                data.put("Step", Integer.toString(getPreferences(today())));
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    data.put("Timestamp", Timestamp.valueOf(formatDate + " 0:0:0.0").toInstant().getEpochSecond());
                                    FirebaseDatabase.getInstance().getReference().child("Data/" + dataSnapshot.getKey() + "/" + formatDate).setValue(data);}

                                ////firebase에 저장

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

                //닉네임이랑 스텝 수 매핑
                mDatabase2.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        HashMap data = new HashMap<>();

                        long now = System.currentTimeMillis();
                        // 현재시간을 date 변수에 저장한다.
                        Date date = new Date(now);
                        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
                        // nowDate 변수에 값을 저장한다.
                        final String formatDate = sdfNow.format(date);

                        //data.put("Step",Integer.toString(10000));
                        data.put("Step", Integer.toString(getPreferences(today())));
                        data.put("Nickname", Nickname);

                        ////firebase에 저장
                        FirebaseDatabase.getInstance().getReference().child("Rank/" + Nickname + "/" + formatDate).setValue(data);

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

                final SharedPreferences preferences = this.getSharedPreferences("UserInfo", 0);
                Nickname = preferences.getString("Nickname", "");
                Key = preferences.getString("Key", "");
                //km 계산
                mDatabase = FirebaseDatabase.getInstance().getReference().child("example");
                mDatabase.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        HashMap<String, String> LoginData2 = (HashMap<String, String>) dataSnapshot.getValue();
                        if (dataSnapshot.getKey().contentEquals(Key)) {
                            sheight = LoginData2.get("Height");
                            height = Float.valueOf(sheight);

                            exercisekmCal = (height * 0.37); //보폭 수 계산1
                            exercisekmCal2 = (height - 100); //보폭 수 계산2
                            avgStepKm = Double.parseDouble(String.format("%.1f", (((exercisekmCal + exercisekmCal2) / 2) * (getPreferences(today()))) / 100000));
                            //보폭 수 계산1,2 평균 값 구해서 stepcount값과 계산

                            HashMap data = new HashMap<>();

                            long now = System.currentTimeMillis();
                            // 현재시간을 date 변수에 저장한다.
                            Date date = new Date(now);
                            // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
                            // nowDate 변수에 값을 저장한다.
                            final String formatDate = sdfNow.format(date);

                            data.put("Km", avgStepKm);
                            ////firebase에 저장
                            FirebaseDatabase.getInstance().getReference().child("Km/" + Nickname + "/" + formatDate).setValue(data);
                            FirebaseDatabase.getInstance().getReference().child("Data/" + dataSnapshot.getKey() + "/" + formatDate).updateChildren(data);
                            //setValue(data);

                            Intent in = new Intent(MainActivity.this, Mainpage.class);
                            startActivity(in);
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

                break;
            case R.id.signbt:
                Intent intent = new Intent(this, Join.class);
                startActivity(intent);
                break;
            case R.id.forgotid:
                Intent In = new Intent(this,FindID.class);
                startActivity(In);
                break;
            case R.id.forgotpw:
                Intent Int = new Intent(this,FindPW.class);
                startActivity(Int);
                break;
        }
    }

    private int getPreferences(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getInt(key, 0);
    }

    public String today() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(Calendar.getInstance().getTime());
    }

    public int timevalue(String today) {
        sp = getSharedPreferences("sp", MODE_PRIVATE);
        int timesp = sp.getInt("times", 0);
        return timesp;
    }

    public Double kcalvalue(String today){
        sp = getSharedPreferences("sp", MODE_PRIVATE);
        String kcalsp = sp.getString("kcals", String.valueOf(0.00));
        Double kcalsp2 = Double.parseDouble(kcalsp);
        return kcalsp2;
    }

    public String sqtvalue(String today){
        sp = getSharedPreferences("sp", MODE_PRIVATE);
        String sqt = sp.getString("SQUAT", "0");
        return sqt;
    }

    public String sbvalue(String today){
        sp = getSharedPreferences("sp", MODE_PRIVATE);
        String sb = sp.getString("SIDEBEND", "0");
        return sb;
    }

    public String situpsvalue(String today){
        sp = getSharedPreferences("sp", MODE_PRIVATE);
        String sit = sp.getString("SITUPS", "0");
        return sit;
    }

    public String scvalue(String today){
        sp = getSharedPreferences("sp", MODE_PRIVATE);
        String sc = sp.getString("SIDECRUNCH", "0");
        return sc;
    }

    public String pusvalue(String today){
        sp = getSharedPreferences("sp", MODE_PRIVATE);
        String pus = sp.getString("PUSHUPS", "0");
        return pus;
    }

}