package com.example.healthcoach01;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListViewAdapter_myworkout extends BaseAdapter {


    private TextView workoutnumber;
    private TextView workoutdetail;
    private TextView workoutcount;

    private ArrayList<ListViewItem_myworkout> listViewItemList4 = new ArrayList<ListViewItem_myworkout>();

    public ListViewAdapter_myworkout(){

    }

    //adapter에 사용되는 데이터 개수를 리턴
    @Override
    public int getCount(){
        return listViewItemList4.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_workout_item, parent, false);
        }
        //화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        ListViewItem_myworkout listViewItem4 = listViewItemList4.get(position);

        workoutnumber = (TextView) convertView.findViewById(R.id.workoutnumber);
        workoutdetail = (TextView) convertView.findViewById(R.id.workoutdetail);
        workoutcount = (TextView) convertView.findViewById(R.id.workoutcount);

        workoutdetail.setText(listViewItem4.getWorkoutdetail());
        workoutcount.setText(listViewItem4.getWorkoutcount());
        workoutnumber.setText(listViewItem4.getWorkoutnumber());

/*
        Bundle bundle = ((Activity) context).getIntent().getExtras();
        String ss = bundle.getString("posname");
        System.out.print("바보얌 " + ss);
        System.out.println();


        switch (ss){
            case "SITUPS" :
                workoutdetail.setText("SITUPS");
                //arr[0] == "1. SITUPS";
                //adapter2.addItem3("1.", "SITUPS", "10");

            case "SQUATS":
                workoutdetail.setText("SQUATS");
                //adapter2.addItem3("2.","SQUATS", "10");

            case "PUSHUPS" :
                workoutdetail.setText("PUSHUPS");
                //adapter2.addItem3("3.","PUSHUPS", "10");

            case "SIDEBEND" :
                workoutdetail.setText("SIDEBEND");
                //adapter2.addItem3("4.","SIDEBEND", "10");

            case "SIDECRUNCH" :
                workoutdetail.setText("SIDECRUNCH");
                //adapter2.addItem3("5.","SIDECRUNCH", "10");

        }*/


        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return listViewItemList4.get(position);
    }

    //아이템 데이터 추가를 위한 함수.
    public void addItem3(String workoutNumber, String workoutDetail, String workoutCount){
        ListViewItem_myworkout item3 = new ListViewItem_myworkout();

        item3.setWorkoutdetail(workoutDetail);
        item3.setWorkoutcount(workoutCount);
        item3.setWorkoutnumber(workoutNumber);

        listViewItemList4.add(item3);
    }
}

