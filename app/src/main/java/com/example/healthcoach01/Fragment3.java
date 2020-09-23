//앱 내에서 제공하는 운동프로그램

package com.example.healthcoach01;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class Fragment3 extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.app_workout, container, false);


        ListView listView2;
        ListViewAdapter_app adapter2;
        RelativeLayout shapeLayout;

        adapter2 = new ListViewAdapter_app();

        listView2 = (ListView) view.findViewById(R.id.app_workoutlist);
        listView2.setAdapter(adapter2);



        adapter2.addItem2("COURSE 1", "11.20kcal", "1:15", "1set", R.drawable.track1);
        adapter2.addItem2("COURSE 2", "23.50kcal", "2:00", "1set", R.drawable.track4);
        adapter2.addItem2("COURSE 3", "37.42kcal", "2:45", "1set", R.drawable.track5);
        adapter2.addItem2("COURSE 4", "37.42kcal", "2:45", "1set", R.drawable.track2);
        adapter2.addItem2("COURSE 5", "45.20cal", "3:30", "1set", R.drawable.track3);


        adapter2.notifyDataSetChanged();



        return view;
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
