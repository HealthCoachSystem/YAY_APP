package com.example.healthcoach01;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Mainpage extends AppCompatActivity
{
    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Viewpage frag1;
    private WorkoutViewPage frag2;
    private Fragment2 frag3;
    private SettingTab frag4;

    String LockNumber;
    public static final String PREFS_NAME = "SETTING";
    boolean Lockornot;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healthmain);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.hometab:
                        setFrag(0);
                        break;
                    case R.id.exercisetab:
                        setFrag(1);
                        break;
                    case R.id.gametab:
                        setFrag(2);
                        break;
                    case R.id.settingtab:
                        setFrag(3);
                        break;
                }
                return true;
            }
        });

        frag1 = new Viewpage();
        frag2 = new WorkoutViewPage();
        frag3 = new Fragment2();
        frag4 = new SettingTab();
        setFrag(0); // 첫 프래그먼트 화면 지정
    }

    // 프레그먼트 교체
    private void setFrag(int n)
    {
        fm = getSupportFragmentManager();
        ft= fm.beginTransaction();
        switch (n)
        {
            case 0:
                ft.replace(R.id.bottom_layout,frag1);
                ft.commit();
                break;

            case 1:
                ft.replace(R.id.bottom_layout,frag2);
                ft.commit();
                break;

            case 2:
                ft.replace(R.id.bottom_layout,frag3);
                ft.commit();
                break;

            case 3:
                ft.replace(R.id.bottom_layout,frag4);
                ft.commit();
                break;


        }
    }


    @Override
    public void onStart(){
        super.onStart();
        Log.i("Main", "onStart()");
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        LockNumber = settings.getString("Number","");
        Lockornot = settings.getBoolean("LOCK",false);

    }

    private boolean checkDeviceLock(){
        KeyguardManager myKM = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
        return myKM.inKeyguardRestrictedInputMode();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkDeviceLock()){
            if(Lockornot){
                Intent in = new Intent(this,LockCheck.class);
                in.putExtra("LockNumber",LockNumber);
                startActivity(in);
            }
        }
        Log.i("Main", "onResume()");
    }

    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.runFinalization();
        System.exit(0);
    }
}

