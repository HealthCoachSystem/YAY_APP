package com.example.healthcoach01;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;


public class Hometab extends Fragment implements SensorEventListener{

    //step 센서
    private SensorManager sensorManager;    //센서값 받아오기 위한 변
    private Sensor stepCountSensor; //걸음 수 측정
    private boolean isSensorPresent = false;
    private int mCounterSteps = 0;  //리스너가 등록되고 난 후의 step count
    private int mSteps = 0; //현재 걸음 수
    private int todayStep;  //오늘 걸음 수
    private int totalStep;  //총 걸음 수

    TextView nick, Kcal, Time, Km;
    String Nickname, Key, kcal, km, Step="0";
    int time, hour, min;
    DatabaseReference mDatabase;
    DatabaseReference mDatabase2;
    Context context;


    CircleProgressView circleProgressBar;

    LineChart lineChart;
    int i = 0;
    LineDataSet dataset;
    ArrayList<String> labels;
    ArrayList<Entry> entries;
    LineData data;
    FrameLayout hometab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) this.getActivity().getSystemService(Activity.SENSOR_SERVICE);

        // circleProgressBar = (CircleProgressView) getActivity().findViewById(R.id.circlebar);


        if (sensorManager != null) {
            stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isSensorPresent = true;
        } else {
            isSensorPresent = false;
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.hometab, container, false);
        nick = (TextView)view.findViewById(R.id.nick);
        Kcal = (TextView)view.findViewById(R.id.kcal);
        Time = (TextView)view.findViewById(R.id.time);
        Km = (TextView)view.findViewById(R.id.km);
        circleProgressBar = view.findViewById(R.id.circlebar);
        lineChart = view.findViewById(R.id.chart);
        hometab = view.findViewById(R.id.backtohometab);

        SharedPreferences preferences = getActivity().getSharedPreferences("UserInfo", 0);
        Nickname = preferences.getString("Nickname","");
        Key = preferences.getString("Key","");

        nick.post(new Runnable() {
            @Override
            public void run() {
                nick.setText("Hello, " + Nickname);
            }
        });

        context = view.getContext();


        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
        // nowDate 변수에 값을 저장한다.
        final String formatDate = sdfNow.format(date);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Data/"+Key);
        circleProgressBar.setText(getPreferences(today()) +" Step");
        circleProgressBar.setTextMode(TextMode.VALUE);
        circleProgressBar.setTextMode(TextMode.TEXT);
        circleProgressBar.setValue(0);

        mDatabase.limitToLast(7).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                HashMap<String,String> LoginData = (HashMap<String, String>) dataSnapshot.getValue();
                if(dataSnapshot.getKey().contentEquals(formatDate)){
                    kcal = LoginData.get("Kcal");
                    final float kcal2 = Float.valueOf(kcal);

                    Kcal.post(new Runnable() {
                        @Override
                        public void run() {
                            Kcal.setText(String.format("%.2f", kcal2) +"Kcal");
                        }
                    });


                    km = String.valueOf((LoginData.get("Km")));

                    Km.post(new Runnable() {
                        @Override
                        public void run() {
                            Km.setText(km+"Km");
                        }
                    });

                    time = Integer.parseInt(LoginData.get("Time"));

                    final long min = time / 60;
                    final long sec = time % 60;

                    if(min < 60) {
                        Time.post(new Runnable() {
                            @Override
                            public void run() {
                                Time.setText(min + "m" + sec + "s");
                            }
                        });
                    }
                    else if(min >= 60){
                        final long hour = min / 60;
                        final long min2 = min % 60;

                        Time.post(new Runnable() {
                            @Override
                            public void run() {
                                Time.setText(hour + "h" + min2 +"m");
                            }
                        });
                    }

                    Step = LoginData.get("Step");

                    circleProgressBar.setMaxValue(10000);
                    circleProgressBar.setText(Step +" Step");
                    circleProgressBar.setTextMode(TextMode.TEXT);
                    circleProgressBar.setBarColor(context.getColor(R.color.barstart), context.getColor(R.color.barstart));
                    circleProgressBar.setValue(Float.parseFloat(Step));

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


        mDatabase.limitToLast(7).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                entries.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    String value = (String) ds.child("Kcal").getValue();
                    float SensorValue = Float.parseFloat(value);
                    entries.add(new Entry(SensorValue,i));
                    i++;
                    labels.add(ds.getKey().substring(5));
                }

                data = new LineData(labels, dataset);
                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
                lineChart.setData(data);
                lineChart.animateY(1000);
                dataset.setColors(ColorTemplate.COLORFUL_COLORS);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        entries = new ArrayList<>();
        dataset = new LineDataSet(entries, "Kcal");
        labels = new ArrayList<String>();
        data = new LineData(labels, dataset);

        XAxis XAxis = lineChart.getXAxis();
        XAxis.setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);

        lineChart.setData(data);
        lineChart.notifyDataSetChanged();
        lineChart.setDescription(null);

        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Km");
        mDatabase2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                HashMap<String,String> LoginData = (HashMap<String, String>) dataSnapshot.getValue();

                if(dataSnapshot.getKey().contentEquals(formatDate)){


                    Km.setText((LoginData.get("Km")));
                    //Km.setText(LoginData.get("Km")); //소수점 한자리수 까지 나타냄
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void onResume() {
        super.onResume();
        if (isSensorPresent) {
            //리스너 등록
            sensorManager.registerListener((SensorEventListener) this,stepCountSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

            //sensorManager.registerListener((SensorEventListener) getActivity(), stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        totalStep = (int) event.values[0];  //걸음 수 카운트 0부터 시작 //총 걸음 수
        todayStep = getPreferences(today());


        //exercisekmCal = (height * 0.37); //보폭 수 계산1
        //exercisekmCal2 = (height - 100); //보폭 수 계산2
        //avgStepKm = (((exercisekmCal + exercisekmCal2) / 2) * (getPreferences(today()))) / 100000;
        //보폭 수 계산1,2 평균 값 구해서 stepcount값과 계산

        // Km.setText((String.format("%.1f", avgStepKm))); //소수점 한자리수 까지 나타냄

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (mCounterSteps < 1) {
                mCounterSteps = totalStep;
            } else {
                mSteps = totalStep - mCounterSteps;
                savePreferences(today(), todayStep + mSteps);
                mCounterSteps = totalStep;

                //circleProgressBar.setText(Integer.toString(getPreferences(today())));

            }
        }
    }


    private void savePreferences(String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private int getPreferences(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key, 0);
    }

    public String today() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(Calendar.getInstance().getTime());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


}