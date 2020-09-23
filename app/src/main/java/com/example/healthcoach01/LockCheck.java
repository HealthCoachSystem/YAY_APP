package com.example.healthcoach01;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LockCheck extends AppCompatActivity {

    int pwcount = 0;
    boolean isdeleted = false;
    String[] passwds = new String[4];
    TextView passview1;
    TextView passview2;
    TextView passview3;
    TextView passview4;
    String PASSWORD ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Lock", "onCreate()");
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lock);
        passview1= findViewById(R.id.pw1);
        passview2= findViewById(R.id.pw2);
        passview3= findViewById(R.id.pw3);
        passview4= findViewById(R.id.pw4);
        Intent in = getIntent();
        PASSWORD = in.getStringExtra("LockNumber");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.num1:
                passwds[pwcount] = "1";
                //pwcount += 1;
                break;
            case R.id.num2:
                passwds[pwcount] = "2";
                //pwcount += 1;
                break;
            case R.id.num3:
                passwds[pwcount] = "3";
                //pwcount += 1;
                break;
            case R.id.num4:
                passwds[pwcount] = "4";
                //pwcount += 1;
                break;
            case R.id.num5:
                passwds[pwcount] = "5";
                //pwcount += 1;
                break;
            case R.id.num6:
                passwds[pwcount] = "6";
                //pwcount += 1;
                break;
            case R.id.num7:
                passwds[pwcount] = "7";
                // pwcount += 1;
                break;
            case R.id.num8:
                passwds[pwcount] = "8";
                // pwcount += 1;
                break;
            case R.id.num9:
                passwds[pwcount] = "9";
                //pwcount += 1;
                break;
            case R.id.num0:
                passwds[pwcount] = "0";
                //pwcount += 1;
                break;
            case R.id.delete:
                //pwcount -=1;
                isdeleted = true;
                break;
        }

        if(pwcount == 0){
            if(!isdeleted){
                passview1.setText(passwds[pwcount]);
                pwcount+=1;
                isdeleted = false;
                return ;
            }
            else{
                isdeleted = false;
                return ;
            }
        }
        else if(pwcount == 1){
            if(!isdeleted){
                passview2.setText(passwds[pwcount]);
                pwcount+=1;
                isdeleted = false;
                return ;
            }
            else{
                pwcount-=1;
                passview1.setText("_");
                isdeleted = false;
                return ;
            }
        }
        else if(pwcount == 2){
            if(!isdeleted){
                passview3.setText(passwds[pwcount]);
                pwcount+=1;
                isdeleted = false;
                return ;
            }
            else{
                pwcount-=1;
                passview2.setText("_");
                isdeleted = false;
                return ;
            }
        }
        else if(pwcount == 3){
            if(!isdeleted){
                passview4.setText(passwds[pwcount]);
                isdeleted = false; //성공
                String LockNum = passwds[0] + passwds[1] + passwds[2] + passwds[3];
                if(LockNum.equals(PASSWORD)){
                    finish();
                }
                else{
                    passwds = new String[4];
                    pwcount = 0;
                    passview1.setText("_");
                    passview2.setText("_");
                    passview3.setText("_");
                    passview4.setText("_");
                    return;
                }
            }
            else{
                pwcount-=1;
                passview3.setText("_");
                isdeleted = false;
                return ;
            }
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
        Log.i("Lock", "onDestroy");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Lock", "onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Lock", "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Lock", "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
        Log.i("Lock", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
        Log.i("Lock", "onStop()");
    }



}
