package com.example.healthcoach01;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListViewAdapter_app extends BaseAdapter {

    private ImageView workout_picture;
    private TextView workout_name;
    private TextView workout_kcal;
    private TextView workout_time;
    private TextView workout_set;

    private ArrayList<ListViewItem_app> listViewItemList2 = new ArrayList<ListViewItem_app>();

    public ListViewAdapter_app(){

    }

    //adapter에 사용되는 데이터 개수를 리턴
    @Override
    public int getCount(){
        return listViewItemList2.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.app_workoutlist_item, parent, false);
        }
        //화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        workout_name = (TextView)convertView.findViewById(R.id.workoutname);
        workout_kcal = (TextView)convertView.findViewById(R.id.calories);
        workout_time = (TextView)convertView.findViewById(R.id.workouttime);
        workout_set = (TextView)convertView.findViewById(R.id.set);
        workout_picture = (ImageView)convertView.findViewById(R.id.workoutpic);

        final ListViewItem_app listViewItem2 = listViewItemList2.get(position);

        workout_name.setText(listViewItem2.getName());
        workout_kcal.setText(listViewItem2.getKcal());
        workout_time.setText(listViewItem2.getTime());
        workout_set.setText(listViewItem2.getSet());
        workout_picture.setImageResource(listViewItem2.getPic());


        LinearLayout cmdArea = (LinearLayout)convertView.findViewById(R.id.cmdArea);
        cmdArea.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //해당 리스트뷰 클릭 시
                Intent intent = new Intent(v.getContext(), WorkoutPlayRightBefore.class);
                intent.putExtra("pos", pos);
                context.startActivity(intent);

                //Toast.makeText(v.getContext(), listViewItemList2.get(pos).getName(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(v.getContext(),listViewItemList2.get(pos).getName(), Toast.LENGTH_SHORT).show();
            }

        });

        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return listViewItemList2.get(position);
    }

    //아이템 데이터 추가를 위한 함수.
    public void addItem2(String workout_name, String workout_kcal, String workout_time, String workout_set, int workout_picture){
        ListViewItem_app item2 = new ListViewItem_app();

        item2.setName(workout_name);
        item2.setKcal(workout_kcal);
        item2.setSet(workout_set);
        item2.setTime(workout_time);
        item2.setPic(workout_picture);

        listViewItemList2.add(item2);
    }
}
