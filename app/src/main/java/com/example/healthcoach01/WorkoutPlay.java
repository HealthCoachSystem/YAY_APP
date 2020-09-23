package com.example.healthcoach01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import io.esense.esenselib.ESenseConnectionListener;
import io.esense.esenselib.ESenseManager;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class WorkoutPlay extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    Button exit;
    Button connect;
    SeekBar progressBar;
    VideoView workoutImage;
    TextView count;
    TextView workoutTimer;
    DatabaseReference mDatabase;
    String Key;
    int timedata;

    ESenseConnectionListener eSenseConnectionListener;
    ESenseManager manager;
    SensorListenerManager sensorListenerManager;


    boolean connected = false,received = false;;
    private int timeout = 30000;
    AlertDialog alertDialog;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private String TAG = "Esense";
    int cnt, pos;
    String exercise="";
    boolean start = false;
    int point = 0;

    int[] breakpoint = {0,30000,75000,120000,165000};

    MediaPlayer mp = null;

    BroadcastReceiver CountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            received = true;
            cnt = intent.getIntExtra("Count",0);
            exercise = intent.getStringExtra("Exercise");
            if(cnt==1 && start == false){
                if(pos == 0 || pos == 1 || pos ==2){
                    if(exercise.contentEquals("squat")) {
                        start = true;
                        workoutImage.start();
                        progressBar.setMax(workoutImage.getDuration());
                        progressBar.postDelayed(onEverySecond, 0);
                    }
                }
                else{
                    if(exercise.contentEquals("situp")) {
                        start = true;
                        workoutImage.start();
                        progressBar.setMax(workoutImage.getDuration());
                        progressBar.postDelayed(onEverySecond, 0);
                    }
                }
            }
            Log.d("Main",cnt+"");
            if(start){
                switch (cnt){
                    case 1:
                        mp = MediaPlayer.create(context,R.raw.num1);
                        mp.start();
                        break;
                    case 2:
                        mp = MediaPlayer.create(context,R.raw.num2);
                        mp.start();
                        break;
                    case 3:
                        mp = MediaPlayer.create(context,R.raw.num3);
                        mp.start();
                        break;
                    case 4:
                        mp = MediaPlayer.create(context,R.raw.num4);
                        mp.start();
                        break;
                    case 5:
                        mp = MediaPlayer.create(context,R.raw.num5);
                        mp.start();
                        break;
                    case 6:
                        mp = MediaPlayer.create(context,R.raw.num6);
                        mp.start();
                        break;
                    case 7:
                        mp = MediaPlayer.create(context,R.raw.num7);
                        mp.start();
                        break;
                    case 8:
                        mp = MediaPlayer.create(context,R.raw.num8);
                        mp.start();
                        break;
                    case 9:
                        mp = MediaPlayer.create(context,R.raw.num9);
                        mp.start();
                        break;
                    case 10:
                        mp = MediaPlayer.create(context,R.raw.num10);
                        mp.start();
                        workoutImage.pause();
                        workoutImage.post(new Runnable() {
                            @Override
                            public void run() {
                                workoutImage.seekTo(breakpoint[point]);
                            }
                        });
                        workoutImage.start();
                        point+=1;
                        break;
                }
                count.post(new Runnable() {
                    @Override
                    public void run() {
                        if(cnt <=10)
                            count.setText(cnt+"");
                    }
                });
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_main);

        connect = (Button) findViewById(R.id.connect);
        progressBar = (SeekBar) findViewById(R.id.progress);
        workoutImage = (VideoView) findViewById(R.id.workoutImage);
        workoutTimer = (TextView) findViewById(R.id.workouttimer);
        exit = (Button) findViewById(R.id.exit);
        count = (TextView) findViewById(R.id.count);

        IntentFilter filter = new IntentFilter();
        filter.addAction("CountResult"); //BroadcastReceiver의 Intentfilter 설정 후 TrackingService에서 받은 Action 추가
        registerReceiver(CountReceiver, filter); //BroadcastReceiver 등록


        manager = new ESenseManager("eSense-0869", WorkoutPlay.this.getApplicationContext(), eSenseConnectionListener);
        sensorListenerManager = new SensorListenerManager(this);
        eSenseConnectionListener = new ESenseConnectionListener() {
            @Override
            public void onDeviceFound(ESenseManager manager) {
                Log.d("TAG", "Found");
            }

            @Override
            public void onDeviceNotFound(ESenseManager manager) {
                Log.d("TAG", "Not Found");
            }

            @Override
            public void onConnected(ESenseManager manager) {
                Log.d("TAG", "Connected");


            }

            @Override
            public void onDisconnected(ESenseManager manager) {
                Log.d("TAG", "Not Connected");
            }
        };



        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", 0);
        switch(pos){
            case 0 :
                String course1 = "android.resource://" + getPackageName() + "/" + R.raw.course1;
                workoutImage.setVideoURI(Uri.parse(course1));
                workoutImage.requestFocus();
                break;
            case 1 :
                String course2 = "android.resource://" + getPackageName() + "/" + R.raw.course2;
                workoutImage.setVideoURI(Uri.parse(course2));
                workoutImage.requestFocus();
                break;
            case 2 :
                String course3 = "android.resource://" + getPackageName() + "/" + R.raw.course3;
                workoutImage.setVideoURI(Uri.parse(course3));
                workoutImage.requestFocus();
                break;
            case 3 :
                String course4 = "android.resource://" + getPackageName() + "/" + R.raw.course4;
                workoutImage.setVideoURI(Uri.parse(course4));
                workoutImage.requestFocus();
                break;
            case 4 :
                String course5 = "android.resource://" + getPackageName() + "/" + R.raw.course5;
                workoutImage.setVideoURI(Uri.parse(course5));
                workoutImage.requestFocus();
                break;
        }

        //for saving Kcal, Time
        SharedPreferences preferences = this.getSharedPreferences("UserInfo", 0);
        Key = preferences.getString("Key","");

        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
        // nowDate 변수에 값을 저장한다.
        final String formatDate = sdfNow.format(date);



        mDatabase = FirebaseDatabase.getInstance().getReference().child("Data/" + Key);

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                HashMap<String,String> LoginData = (HashMap<String, String>) dataSnapshot.getValue();
                if(dataSnapshot.getKey().contentEquals(formatDate)){
                    HashMap data = new HashMap<>();

                    String timedatas = LoginData.get("Time");
                    timedata = Integer.parseInt(timedatas);

                    String Kcaldatas = (LoginData.get("Kcal"));
                    double kcaldata = Double.parseDouble(Kcaldatas);


                    int timetest = 0;
                    double kcaltest = 0;
                    int sqt_1 = 0;
                    int sb_1 = 0;
                    int sit_1 = 0;
                    int sc_1 = 0;
                    int pus_1 = 0;


                    int sqt_count = Integer.parseInt(LoginData.get("SQUAT"));
                    int sb_count = Integer.parseInt( LoginData.get("SIDEBEND"));
                    int sit_count = Integer.parseInt(LoginData.get("SITUPS"));
                    int sc_count = Integer.parseInt(LoginData.get("SIDECRUNCH"));
                    int pus_count = Integer.parseInt(LoginData.get("PUSHUPS"));

                    switch (pos){
                        case 0 :
                            timetest = timedata + 75;
                            kcaltest = kcaldata + 11.20;
                            sqt_1 = sqt_count + 10;
                            sb_1 = sb_count + 10;
                            data.put("SQUAT", Integer.toString(sqt_1));
                            data.put("SIDEBEND",Integer.toString(sb_1));
                            break;
                        case 1 :
                            timetest = timedata + 120;
                            kcaltest = kcaldata + 23.50;
                            sqt_1 = sqt_count + 10;
                            sb_1 = sb_count + 10;
                            sit_1 = sit_count + 10;
                            data.put("SQUAT", Integer.toString(sqt_1));
                            data.put("SIDEBEND",Integer.toString(sb_1));
                            data.put("SITUPS", Integer.toString(sit_1));
                            break;

                        case 2 :
                            timetest = timedata + 165;
                            kcaltest = kcaldata + 37.42;
                            sqt_1 = sqt_count + 10;
                            sb_1 = sb_count + 10;
                            sit_1 = sit_count + 10;
                            sc_1 = sc_count + 10;
                            data.put("SQUAT", Integer.toString(sqt_1));
                            data.put("SIDEBEND",Integer.toString(sb_1));
                            data.put("SITUPS", Integer.toString(sit_1));
                            data.put("SIDECRUNCH", Integer.toString(sc_1));
                            break;

                        case 3 :
                            timetest = timedata + 165;
                            kcaltest = kcaldata + 37.42;
                            sqt_1 = sqt_count + 10;
                            sb_1 = sb_count + 10;
                            sit_1 = sit_count + 10;
                            sc_1 = sc_count + 10;
                            data.put("SQUAT", Integer.toString(sqt_1));
                            data.put("SIDEBEND",Integer.toString(sb_1));
                            data.put("SITUPS", Integer.toString(sit_1));
                            data.put("SIDECRUNCH", Integer.toString(sc_1));
                            break;

                        case 4 :
                            timetest = timedata + 210;
                            kcaltest = kcaldata + 45.20;
                            sqt_1 = sqt_count + 10;
                            sb_1 = sb_count + 10;
                            sit_1 = sit_count + 10;
                            sc_1 = sc_count + 10;
                            pus_1 = pus_count + 10;
                            data.put("SQUAT", Integer.toString(sqt_1));
                            data.put("SIDEBEND",Integer.toString(sb_1));
                            data.put("SITUPS", Integer.toString(sit_1));
                            data.put("SIDECRUNCH", Integer.toString(sc_1));
                            data.put("PUSHUPS",Integer.toString(pus_1));
                            break;
                    }

                    sp = getSharedPreferences("sp", MODE_PRIVATE);
                    editor = sp.edit();
                    editor.putInt("times", timetest);
                    editor.putString("kcals", String.valueOf(kcaltest));

                    editor.putString("SQUAT", Integer.toString(sqt_1));
                    editor.putString("SIDEBEND", Integer.toString(sb_1));
                    editor.putString("SITUPS", Integer.toString(sit_1));
                    editor.putString("SIDECRUNCH", Integer.toString(sc_1));
                    editor.putString("PUSHUPS", Integer.toString(pus_1));
                    editor.commit();


                    data.put("Time", Integer.toString(timetest));
                    data.put("Kcal", Double.toString(kcaltest));
                    data.put("Step", Integer.toString(getPreferences(today())));



                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        data.put("Timestamp", Timestamp.valueOf(formatDate + " 0:0:0.0").toInstant().getEpochSecond());
                        //FirebaseDatabase.getInstance().getReference().child("Data/" + dataSnapshot.getKey() + "/" + formatDate).updateChildren(data);
                    }

                    FirebaseDatabase.getInstance().getReference().child("Data/" + Key +"/"+  "/" + formatDate).updateChildren(data);
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



        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        //수정 해상도
        final MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                workoutImage.setLayoutParams(lp);
                ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(workoutImage.getLayoutParams());
                margin.setMargins(0, 350, 0, 0);
                workoutImage.setLayoutParams(new FrameLayout.LayoutParams(margin));
            }
        };

        final MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //프로그레스바 진행

                progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        long times = progress / 1000;
                        long sec = times % 60;
                        long min = times / 60;

                        workoutTimer.setText(String.format("%02d:%02d", min, sec));


                        if(fromUser){
                            // this is when actually seekbar has been seeked to a new position
                            workoutImage.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });


                mp.setOnVideoSizeChangedListener(onVideoSizeChangedListener);

                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                workoutImage.setLayoutParams(lp);
                ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(workoutImage.getLayoutParams());
                margin.setMargins(0, 350, 0, 0);
                workoutImage.setLayoutParams(new FrameLayout.LayoutParams(margin));
                //workoutImage.postDelayed(onEverySecond, 1000);

                workoutImage.postDelayed(onEverySecond, 500);

                connect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MediaPlayer mediaPlayer = null;
                        try{
                            for(int i = 3; i > 0; i--){
                                Toast.makeText(getApplicationContext(),""+i,Toast.LENGTH_SHORT).show();
                                Log.d("Main",i+"");
                                Thread.sleep(1000);
                            }
                        }catch(InterruptedException e){
                            System.out.println(e.getMessage());
                        }
                        Log.d("TAG", "Start");
                        connected = true;
                        if(manager.isConnected()){
                            mediaPlayer = MediaPlayer.create(WorkoutPlay.this,R.raw.connecteffect);
                            mediaPlayer.start();
                        }
                        manager.registerSensorListener(sensorListenerManager, 10);
                    }
                });

            }
        };

        workoutImage.setOnPreparedListener(onPreparedListener);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Esense Bluetooth Connect").setMessage("연결 버튼을 눌러 Esense를 연결해주세요");
        builder.setPositiveButton("연결", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                manager.connect(timeout);
                Toast.makeText(getApplicationContext(), "trying to connect...", Toast.LENGTH_LONG).show();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();

        if (!checkPermission()) {
            requestPermission();
        } else {
            Log.d(TAG, "Permission already granted..");
        }

        workoutImage.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                manager.unregisterSensorListener();
                finish();
            }
        });


    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if(connected){
            manager.disconnect();
        }

        if(received)
            unregisterReceiver(CountReceiver);

        if(mp!=null)
            mp.stop();
    }

    private boolean checkPermission() {
        int recordResult = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int locationResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int writeResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return locationResult == PackageManager.PERMISSION_GRANTED &&
                writeResult == PackageManager.PERMISSION_GRANTED && recordResult == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION,
                WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, PERMISSION_REQUEST_CODE);

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean recordAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && storageAccepted && recordAccepted){
                        Log.d(TAG, "Permission granted");
                    } else {
                        Log.d(TAG, "Permission denied");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to all permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION,
                                                            WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(WorkoutPlay.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private Runnable onEverySecond = new Runnable() {
        @Override
        public void run() {
            if(progressBar != null) {
                progressBar.setProgress(workoutImage.getCurrentPosition());
            }
            if(workoutImage.isPlaying()) {
                progressBar.postDelayed(onEverySecond, 1000);
            }
        }
    };

    private int getPreferences(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getInt(key, 0);
    }

    public String today() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(Calendar.getInstance().getTime());
    }

}