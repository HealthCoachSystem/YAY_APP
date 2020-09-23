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
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.MediaController;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import io.esense.esenselib.ESenseConnectionListener;
import io.esense.esenselib.ESenseManager;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class WorkoutPlay_myworkout extends AppCompatActivity{

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    Button exit;
    TextView connect;
    SeekBar progressBar;
    VideoView workoutImage;
    TextView count;
    TextView workoutTimer;
    private ArrayList<String> array = new ArrayList<String>();
    private int array_count;
    DatabaseReference mDatabase;
    String Key;
    int timedata;

    ESenseConnectionListener eSenseConnectionListener;
    ESenseManager manager;
    SensorListenerManager sensorListenerManager;


    boolean connected = false ,received = false;
    private int timeout = 30000;
    AlertDialog alertDialog;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private String TAG = "Esense";
    int cnt;

    MediaPlayer mp = null;

    BroadcastReceiver CountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            received = true;
            cnt = intent.getIntExtra("Count",0);
            if(cnt==1){
                workoutImage.start();
                progressBar.setMax(workoutImage.getDuration());
                progressBar.postDelayed(onEverySecond, 0);
            }
            Log.d("Main",cnt+"");
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
                    break;
            }
            count.post(new Runnable() {
                @Override
                public void run() {
                    count.setText(cnt+"");
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_main);

        connect = (TextView) findViewById(R.id.connect);
        progressBar = (SeekBar) findViewById(R.id.progress);
        workoutImage = (VideoView) findViewById(R.id.workoutImage);
        workoutTimer = (TextView) findViewById(R.id.workouttimer);
        exit = (Button) findViewById(R.id.exit);
        count = (TextView) findViewById(R.id.count);
        array_count = 0;

        IntentFilter filter = new IntentFilter();
        filter.addAction("CountResult"); //BroadcastReceiver의 Intentfilter 설정 후 TrackingService에서 받은 Action 추가
        registerReceiver(CountReceiver, filter); //BroadcastReceiver 등록


        manager = new ESenseManager("eSense-0869", WorkoutPlay_myworkout.this.getApplicationContext(), eSenseConnectionListener);
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
        final String list = intent.getStringExtra("list");


        final String squats = "SQUATS";
        final String situps = "SITUPS";
        final String pushups = "PUSHUPS";
        final String sidebend = "SIDEBEND";
        final String sidecrunch = "SIDECRUNCH";

        if (list.contains(situps)) {
            array.add("android.resource://" + getPackageName() + "/" + R.raw.situps);
        }
        if (list.contains(squats)) {
            array.add("android.resource://" + getPackageName() + "/" + R.raw.squats);
        }
        if (list.contains(sidebend)) {
            array.add("android.resource://" + getPackageName() + "/" + R.raw.sidebend);
        }
        if (list.contains(sidecrunch)) {
            array.add("android.resource://" + getPackageName() + "/" + R.raw.sidecrunch);
        }
        if (list.contains(pushups)) {
            array.add("android.resource://" + getPackageName() + "/" + R.raw.pushups);
        }


        //for saving Kcal, Time
        SharedPreferences preferences = this.getSharedPreferences("UserInfo", 0);
        Key = preferences.getString("Key", "");

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
                HashMap<String, String> LoginData = (HashMap<String, String>) dataSnapshot.getValue();
                if (dataSnapshot.getKey().contentEquals(formatDate)) {
                    HashMap data = new HashMap<>();

                    String timedatas = LoginData.get("Time");

                    timedata = Integer.parseInt(timedatas);

                    int timetest = 0;

                    if (array.size() == 0) {
                        timetest = timedata + 0;
                    } else if (array.size() == 1) {
                        timetest = timedata + 30;
                    } else if (array.size() == 2) {
                        timetest = timedata + 60;
                    } else if (array.size() == 3) {
                        timetest = timedata + 90;
                    } else if (array.size() == 4) {
                        timetest = timedata + 120;
                    } else if (array.size() == 5) {
                        timetest = timedata + 150;
                    }

                    data.put("Time", Integer.toString(timetest));

                    String kcaldatas = LoginData.get("Kcal");
                    double kcaldata = Double.parseDouble(kcaldatas);

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


                    if (list.contains(situps)) {
                        kcaltest = kcaldata + 9.00;
                        sit_1 = sit_count + 10;
                        data.put("SITUPS", Integer.toString(sit_1));
                    }
                    if (list.contains(squats)) {
                        kcaltest += 4.40;
                        sqt_1 = sqt_count + 10;
                        data.put("SQUAT", Integer.toString(sqt_1));
                        if (!list.contains(situps)) {
                            kcaltest = kcaldata + 4.40;
                        }
                    }
                    if (list.contains(pushups)) {
                        kcaltest += 7.00;
                        pus_1 = pus_count + 10;
                        data.put("PUSHUPS",Integer.toString(pus_1));
                        if (!list.contains(situps) && !list.contains(squats)) {
                            kcaltest = kcaldata + 7.00;
                        }
                    }
                    if (list.contains(sidebend)) {
                        kcaltest += 5.00;
                        sb_1 = sb_count + 10;
                        data.put("SIDEBEND",Integer.toString(sb_1));
                        if (!list.contains(situps) && !list.contains(squats) && !list.contains(pushups)) {
                            kcaltest = kcaldata + 5.00;
                        }
                    }
                    if (list.contains(sidecrunch)) {
                        kcaltest += 15.00;
                        sc_1 = sc_count + 10;
                        data.put("SIDECRUNCH", Integer.toString(sc_1));
                        if (!list.contains(situps) && !list.contains(squats) && !list.contains(pushups) && !list.contains(sidebend)) {
                            kcaltest = kcaldata + 15.00;
                        }
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

                    data.put("Kcal", Double.toString(kcaltest));
                    data.put("Step", Integer.toString(getPreferences(today())));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        data.put("Timestamp", Timestamp.valueOf(formatDate + " 0:0:0.0").toInstant().getEpochSecond());
                    }

                    FirebaseDatabase.getInstance().getReference().child("Data/" + Key + "/" + "/" + formatDate).updateChildren(data);
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
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
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
                progressBar.setMax(workoutImage.getDuration());
                progressBar.postDelayed(onEverySecond, 0);

                progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        long times = progress / 1000;
                        long sec = times % 60;
                        long min = times / 60;

                        workoutTimer.setText(String.format("%02d:%02d", min, sec));

                        if (fromUser) {
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

                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                workoutImage.setLayoutParams(lp);
                ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(workoutImage.getLayoutParams());
                margin.setMargins(0, 350, 0, 0);
                workoutImage.setLayoutParams(new FrameLayout.LayoutParams(margin));
                //workoutImage.postDelayed(onEverySecond, 1000);

                workoutImage.postDelayed(onEverySecond, 500);
                connect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            for (int i = 3; i > 0; i--) {
                                Thread.sleep(1000);
                                Toast.makeText(getApplicationContext(), "" + i, Toast.LENGTH_SHORT).show();
                            }
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                        Log.d("TAG", "Start");
                        connected = true;
                        if(manager.isConnected()){
                            MediaPlayer mp = MediaPlayer.create(WorkoutPlay_myworkout.this,R.raw.connecteffect);
                            mp.start();
                        }
                        manager.registerSensorListener(sensorListenerManager, 10);
                        workoutImage.start();
                        progressBar.setMax(workoutImage.getDuration());
                        progressBar.postDelayed(onEverySecond, 0);
                    }
                });
            }
        };
        workoutImage.setOnPreparedListener(onPreparedListener);

        String video = "android.resource://" + getPackageName() + "/" + R.raw.countplay;

        //Uri video = Uri.parse(path + "/DCIM/Camera/countplay.mp4");
        //workoutImage.setMediaController(mediaController);

        workoutImage.setVideoURI(Uri.parse(video));
        workoutImage.requestFocus();


        //하나 재생 끝나면 연속 재생
        MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if (array.size() > array_count) {
                    Uri video1 = Uri.parse(array.get(array_count));
                    array_count++;

                    workoutImage.setVideoURI(video1);
                    workoutImage.start();

                    mp.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
                    workoutImage.setOnPreparedListener(onPreparedListener);
                }
                else {
                    manager.unregisterSensorListener();
                    finish();
                }
            }
        };


        workoutImage.setOnCompletionListener(onCompletionListener);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Esense Bluetooth Connect").setMessage("연결 버튼을 눌러 Esense를 연결해주세요");
        builder.setPositiveButton("연결", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                manager.connect(timeout);
            }
        });

        alertDialog = builder.create();
        alertDialog.show();

        if (!checkPermission()) {
            requestPermission();
        } else {
            Log.d(TAG, "Permission already granted..");
        }
    }



    @Override
    public void onDestroy(){
        super.onDestroy();
       if(connected)
           manager.unregisterSensorListener();
       if(received)
           unregisterReceiver(CountReceiver);
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
        new AlertDialog.Builder(WorkoutPlay_myworkout.this)
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