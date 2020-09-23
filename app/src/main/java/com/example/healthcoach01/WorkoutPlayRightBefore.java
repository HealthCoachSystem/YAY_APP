package com.example.healthcoach01;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WorkoutPlayRightBefore extends AppCompatActivity {
    TextView workoutdetail;
    Button workoutstartbtn;
    ImageView startimageview;
    TextView workoutcount;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_rightbefore);

        ListView listView3;
        ListViewAdapter_before adapter_before;


        startimageview = (ImageView) findViewById(R.id.startimageview);
        workoutdetail = (TextView) findViewById(R.id.workoutdetail);
        workoutstartbtn = (Button) findViewById(R.id.workoutstartbtn);
        workoutcount = (TextView) findViewById(R.id.workoutcount);


        adapter_before = new ListViewAdapter_before();
        listView3 = (ListView) findViewById(R.id.workoutstartlist);
        listView3.setAdapter(adapter_before);

        Intent intent = getIntent();
        final int pos = intent.getIntExtra("pos", 0);
        switch(pos){
            case 0 :
                adapter_before.addItem2("SQUATS", "10", R.drawable.squats);
                adapter_before.addItem2("BREAK TIME", "15s", R.drawable.breaktime);
                adapter_before.addItem2("SIDEBEND", "10", R.drawable.sidebend);
                break;
            case 1 :
                adapter_before.addItem2("SQUATS", "10", R.drawable.squats);
                adapter_before.addItem2("BREAK TIME", "15s", R.drawable.breaktime);
                adapter_before.addItem2("SIDEBEND", "10", R.drawable.sidebend);
                adapter_before.addItem2("BREAK TIME", "15s", R.drawable.breaktime);
                adapter_before.addItem2("SITUPS", "10", R.drawable.situps);
                break;
            case 2 :
                adapter_before.addItem2("SQUATS", "10", R.drawable.squats);
                adapter_before.addItem2("BREAK TIME", "15s", R.drawable.breaktime);
                adapter_before.addItem2("SIDEBEND", "10", R.drawable.sidebend);
                adapter_before.addItem2("BREAK TIME", "15s", R.drawable.breaktime);
                adapter_before.addItem2("SITUPS", "10", R.drawable.situps);
                adapter_before.addItem2("BREAK TIME", "15s", R.drawable.breaktime);
                adapter_before.addItem2("SIDECRUNCH", "10", R.drawable.sidecrunch);
                break;
            case 3 :
                adapter_before.addItem2("SITUPS", "10", R.drawable.situps);
                adapter_before.addItem2("BREAK TIME", "15s", R.drawable.breaktime);
                adapter_before.addItem2("SIDECRUNCH", "10", R.drawable.sidecrunch);
                adapter_before.addItem2("BREAK TIME", "15s", R.drawable.breaktime);
                adapter_before.addItem2("SQUATS", "10", R.drawable.squats);
                adapter_before.addItem2("BREAK TIME", "15s", R.drawable.breaktime);
                adapter_before.addItem2("SIDEBEND", "10", R.drawable.sidebend);
                break;
            case 4 :
                adapter_before.addItem2("SIDEBEND", "10", R.drawable.sidebend);
                adapter_before.addItem2("BREAK TIME", "15s", R.drawable.breaktime);
                adapter_before.addItem2("SQUATS", "10", R.drawable.squats);
                adapter_before.addItem2("BREAK TIME", "15s", R.drawable.breaktime);
                adapter_before.addItem2("SIDECRUNCH", "10", R.drawable.sidecrunch);
                adapter_before.addItem2("BREAK TIME", "15s", R.drawable.breaktime);
                adapter_before.addItem2("SITUPS", "10", R.drawable.situps);
                adapter_before.addItem2("BREAK TIME", "15s", R.drawable.breaktime);
                adapter_before.addItem2("PUSHUPS", "10", R.drawable.pushups);
                break;
        }

        workoutstartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WorkoutPlay.class);
                intent.putExtra("pos", pos);
                getApplicationContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });

        //adapter_before.addItem2(squats);
        //adapter_before.addItem2(sidebend);
        //adapter_before.addItem2("Sidebend");

        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }
}