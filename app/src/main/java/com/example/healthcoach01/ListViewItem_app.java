package com.example.healthcoach01;

public class ListViewItem_app {

    private int workout_picture;
    private String workout_name;
    private String workout_kcal;
    private String workout_time;
    private String workout_set;

    public void setName(String name){
        workout_name = name;
    }

    public void setKcal(String kcal){
        workout_kcal = kcal;
    }

    public void setTime(String time){
        workout_time = time;
    }

    public void setSet(String set){
        workout_set = set;
    }

    public void setPic(int pic){
        workout_picture = pic;
    }


    public int getPic(){
        return this.workout_picture;
    }

    public String getName(){
        return this.workout_name;
    }

    public String getKcal(){
        return this.workout_kcal;
    }

    public String getTime(){
        return this.workout_time;
    }

    public String getSet(){
        return this.workout_set;
    }
}
