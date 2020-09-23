package com.example.healthcoach01;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

public class WorkoutPlayRightBefore_myworkout extends AppCompatActivity {

    Button workoutstartbtn;
    ImageView startimageview;
    TextView workoutcount;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_rightbefore);

        ListView listView1; //담은 목록
        ListViewAdapter_myworkout adapter2;

        startimageview = (ImageView) findViewById(R.id.startimageview);
        workoutstartbtn = (Button) findViewById(R.id.workoutstartbtn);
        workoutcount = (TextView) findViewById(R.id.workoutcount);

        adapter2 = new ListViewAdapter_myworkout();
        listView1 = (ListView) findViewById(R.id.workoutstartlist);
        listView1.setAdapter(adapter2);


        Intent intent = getIntent();
        final String list = intent.getStringExtra("list");

        if (list.contains("SITUPS"))
            adapter2.addItem3("- ", "SITUPS", "10");
            //adapter2.notifyDataSetChanged();
        if (list.contains("SQUATS"))
            adapter2.addItem3("- ", "SQUATS", "10");
            //adapter2.notifyDataSetChanged();
        if (list.contains("PUSHUPS"))
            adapter2.addItem3("- ", "PUSHUPS", "10");
            //adapter2.notifyDataSetChanged();
        if (list.contains("SIDEBEND"))
            adapter2.addItem3("- ", "SIDEBEND", "10");
            //adapter2.notifyDataSetChanged();
        if (list.contains("SIDECRUNCH"))
            adapter2.addItem3("- ", "SIDECRUNCH", "10");
            //adapter2.notifyDataSetChanged();

        adapter2.notifyDataSetChanged();

        workoutstartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WorkoutPlay_myworkout.class);
                intent.putExtra("list", list);
                getApplicationContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });

        //adapter_before.addItem2(squats);
        //adapter_before.addItem2(sidebend);
        //adapter_before.addItem2("Sidebend");

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }
}
