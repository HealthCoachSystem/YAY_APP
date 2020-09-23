package com.example.healthcoach01;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListViewAdapter_before extends BaseAdapter {


        private TextView workoutdetail;
        private TextView workoutcount;
        private ImageView workoutpic2;

        private ArrayList<ListViewItem_before> listViewItemList3 = new ArrayList<ListViewItem_before>();

        public ListViewAdapter_before(){

        }

        //adapter에 사용되는 데이터 개수를 리턴
        @Override
        public int getCount(){
            return listViewItemList3.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            final int pos = position;
            final Context context = parent.getContext();

            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.workout_rightbefore_item, parent, false);
            }
            //화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

            final ListViewItem_before listViewItem3 = listViewItemList3.get(position);

            workoutdetail = (TextView) convertView.findViewById(R.id.workoutdetail);
            workoutcount = (TextView) convertView.findViewById(R.id.workoutcount);
            workoutpic2 = (ImageView) convertView.findViewById(R.id.workoutpic2);

            workoutdetail.setText(listViewItem3.getWorkoutdetail());
            workoutcount.setText(listViewItem3.getWorkoutcount());
            workoutpic2.setImageResource(listViewItem3.getWokroutpic2());

/*
            LinearLayout cmdArea = (LinearLayout)convertView.findViewById(R.id.cmdArea);
            cmdArea.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //해당 리스트뷰 클릭 시
                    Intent intent = new Intent(v.getContext(), WorkoutPlay.class);
                    context.startActivity(intent);
                    Toast.makeText(v.getContext(),listViewItemList3.get(pos).getWorkoutdetail(), Toast.LENGTH_SHORT).show();
                }

            });*/

            return convertView;
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public Object getItem(int position){
            return listViewItemList3.get(position);
        }

        //아이템 데이터 추가를 위한 함수.
        public void addItem2(String workoutDetail, String workoutCount, int workoutPic2){
            ListViewItem_before item3 = new ListViewItem_before();

            item3.setWorkoutdetail(workoutDetail);
            item3.setWorkoutcount(workoutCount);
            item3.setWokroutpic2(workoutPic2);

            listViewItemList3.add(item3);
        }
    }

