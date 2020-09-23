package com.example.healthcoach01;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

public class Calendar extends Fragment {

    MaterialCalendarView calendarView;
    View view;
    TextView Date, Data;
    CalendarDay selectedDay;
    String Key;
    DatabaseReference mDatabase;
    String[] result = {};
    int[] Step = {};
    int i=0,Count=0;
    Boolean OneTime = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.calendar, container, false);
        calendarView = (MaterialCalendarView)view.findViewById(R.id.calendarView);
        Date = view.findViewById(R.id.date);
        Data = view.findViewById(R.id.data);

        SharedPreferences preferences = getActivity().getSharedPreferences("UserInfo", 0);
        Key = preferences.getString("Key","");

        //원래코드
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Data/"+Key);



        calendarView.state().edit()
                .setFirstDayOfWeek(java.util.Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2019,12,31))
                .setMaximumDate(CalendarDay.from(2030,12,31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        calendarView.addDecorator(new OnedayDecorator());

        //점+선택날짜
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Count++;
                }

                result = new String[Count];

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(i==Count){
                        break;
                    }
                    result[i] = ds.getKey();
                    i++;
                }


                if(Count != 0){
                    //new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());
                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    ArrayList<CalendarDay> dates = new ArrayList<>();

                    if(OneTime){
                        for(int i = 0 ; i < result.length ; i ++){
                            CalendarDay day = CalendarDay.from(calendar);
                            String[] time = result[i].split("-");
                            int year = Integer.parseInt(time[0]);
                            int month = Integer.parseInt(time[1]);
                            int dayy = Integer.parseInt(time[2]);

                            dates.add(day);
                            calendar.set(year,month-1,dayy);
                        }

                        calendarView.addDecorator(new EventDecorator(Color.RED, dates,view.getContext()));

                        OneTime = false;
                    }


                }

                final DataSnapshot finDS = dataSnapshot;

                calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                        String SelectedDay;
                        for(DataSnapshot ds:finDS.getChildren()){
                            //Log.d("ds",ds.getKey());
                            if(Integer.toString(date.getDay()).length() == 1){
                                SelectedDay = date.getYear()+"-0"+(date.getMonth()+1)+"-0"+date.getDay();
                            }
                            else{
                                SelectedDay = date.getYear()+"-0"+(date.getMonth()+1)+"-"+date.getDay();
                            }

                            //Log.d("Selected day",SelectedDay);
                            if(ds.getKey().contentEquals(SelectedDay)){
                                Date.setText(SelectedDay);
                                final HashMap<String,String> LoginData = (HashMap<String, String>) ds.getValue();
                                if(LoginData.get("SQUAT") != null){
                                    Data.setText("Kcal : " + LoginData.get("Kcal") +"\nStep : " + LoginData.get("Step") + "\nSQUAT : " + LoginData.get("SQUAT") + "\nSITUPS : " + LoginData.get("SITUPS") + "\nSIDEBEND : " + LoginData.get("SIDEBEND") + "\nSIDECRUNCH : " + LoginData.get("SIDECRUNCH") + "\nPUSHUPS : " + LoginData.get("PUSHUPS"));
                                }
                                else{
                                    Data.setText("Kcal : " + LoginData.get("Kcal") +"\nStep : " + LoginData.get("Step") + "\nSQUAT : " + 0 + "\nSITUPS : " + 0 + "\nSIDEBEND : " + 0 + "\nSIDECRUNCH : " + 0 + "\nPUSHUPS : " + 0);
                                }
                                break;
                            }
                            else{
                                Date.setText(SelectedDay);
                                Data.setText("Kcal : " + 0 +"\nStep : " + 0 + "\nSQUAT : " + 0 + "\nSITUPS : " + 0 + "\nSIDEBEND : " + 0 + "\nSIDECRUNCH : " + 0 + "\nPUSHUPS : " + 0);
                            }
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            java.util.Calendar calendar = java.util.Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();


            for(int i = 0 ; i < Time_Result.length ; i ++){
                CalendarDay day = CalendarDay.from(calendar);
                //String[] time = Time_Result[i].split("-");
                //int year = Integer.parseInt(time[0]);
                //int month = Integer.parseInt(time[1]);
                //int dayy = Integer.parseInt(time[2]);

                dates.add(day);
                //calendar.set(year,month-1,dayy);
            }

            return dates;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            java.util.Calendar calendar = java.util.Calendar.getInstance();


            for(int i = 0 ; i < Time_Result.length ; i ++){
                String[] time = Time_Result[i].split("-");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                calendar.set(year,month-1,dayy);
            }

            //calendarView.addDecorator(new EventDecorator(Color.RED, dates,view.getContext()));


        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);


            calendarView.addDecorator(new EventDecorator(Color.RED, calendarDays,view.getContext()));
        }
    }


}

