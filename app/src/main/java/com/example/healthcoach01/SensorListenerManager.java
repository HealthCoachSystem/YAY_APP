package com.example.healthcoach01;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import io.esense.esenselib.ESenseConfig;
import io.esense.esenselib.ESenseEvent;
import io.esense.esenselib.ESenseSensorListener;


public class SensorListenerManager extends AppCompatActivity implements ESenseSensorListener {
    private long timeStamp;
    private double[] accel;
    private double[] gyro;
    private double acc_rms,gyro_rms, previousrms = 0, MA_rms, count_acc_rms;
    private boolean In = false;
    private int count =0, previouscount = 0;;
    private String Result="", PreviousResult="ready";
    private double threshold = 0.0;

    private String xAcc="";
    private String yAcc="";
    private String zAcc="";
    private String xRot="";
    private String yRot="";
    private String zRot="";
    private String AccRms="";
    private String RotRms="";
    private String roll="";
    private String pitch="";
    private int nDataCount=0;



    Context context;

    ESenseConfig eSenseConfig;

    public SensorListenerManager(Context context) {
        this.context = context;
        eSenseConfig = new ESenseConfig();
    }

    @Override
    public void onSensorChanged(ESenseEvent evt)  throws JSONException {
        nDataCount++; // window size 만큼 카운트
        accel = evt.convertAccToG(eSenseConfig);
        gyro = evt.convertGyroToDegPerSecond(eSenseConfig);

        // 운동 카운팅용
        acc_rms = Math.sqrt((Math.pow(accel[0], 2) + Math.pow(accel[1], 2) + Math.pow(accel[2], 2)));
        gyro_rms = Math.sqrt((Math.pow(gyro[0], 2) + Math.pow(gyro[1], 2) + Math.pow(gyro[2], 2)));

        count_acc_rms = Math.sqrt((Math.pow(accel[0], 2) + Math.pow(accel[1], 2) + Math.pow(accel[2], 2))/3);

        // 서버에 보내기 위해 ','로 분리
        xAcc+=accel[0]+",";
        yAcc+=accel[1]+",";
        zAcc+=accel[2]+",";
        xRot+=gyro[0]+",";
        yRot+=gyro[1]+",";
        zRot+=gyro[2]+",";
        AccRms+=acc_rms+",";
        RotRms+=gyro_rms+",";
        roll+=Math.atan(accel[1]/accel[2])+",";
        pitch+=Math.atan(accel[0]/accel[2])+",";

        timeStamp = evt.getTimestamp();

        if(!Result.contentEquals(PreviousResult)){
            PreviousResult = Result;
            count = 0;
        }

        switch (Result){
            case "squat":
                threshold = 0.7;
                MA_rms = 0.2*previousrms + 0.8*count_acc_rms;
                previousrms = count_acc_rms;
                break;
            case "pushup":
                threshold = 0.7;
                MA_rms = 0.2*previousrms + 0.8*count_acc_rms;
                previousrms = count_acc_rms;
                break;
            case "sidebend":
                threshold = 40;
                MA_rms = 0.2*previousrms + 0.8*gyro[1];
                previousrms = gyro[1];
                break;
            case "sidecrunch":
                threshold = 40;
                MA_rms = 0.2*previousrms + 0.8*gyro[1];
                previousrms = gyro[1];
                break;
            case "situp":
                threshold = 100;
                MA_rms = 0.2*previousrms + 0.8*gyro[2];
                previousrms = gyro[2];
                break;
        }

        if(MA_rms > threshold){
            if(In == false){
                count++ ;
                In = true;
            }
        }
        else{
            if(In == true){
                In = false;
            }
        }


        if (nDataCount==15) {// 특정개수가 쌓이면 request 보냄
            // json 만들고
            JSONObject data = new JSONObject();

            xAcc=xAcc.substring(0,xAcc.length()-1);
            yAcc=yAcc.substring(0,yAcc.length()-1);
            zAcc=zAcc.substring(0,zAcc.length()-1);
            xRot=xRot.substring(0,xRot.length()-1);
            yRot=yRot.substring(0,yRot.length()-1);
            zRot=zRot.substring(0,zRot.length()-1);
            AccRms=AccRms.substring(0,AccRms.length()-1);
            RotRms=RotRms.substring(0,RotRms.length()-1);
            roll=roll.substring(0,roll.length()-1);
            pitch=pitch.substring(0,pitch.length()-1);


            data.put("xAcc",xAcc);
            data.put("yAcc",yAcc);
            data.put("zAcc",zAcc);
            data.put("xRot",xRot);
            data.put("yRot",yRot);
            data.put("zRot",zRot);
            data.put("AccRms",AccRms);
            data.put("RotRms",RotRms);
            data.put("roll",roll);
            data.put("pitch",pitch);

            String url="http://34.67.193.96:2431";

            NetworkTask networkTask = new NetworkTask(url, data);
            networkTask.execute();

            nDataCount=0;

            xAcc="";
            yAcc="";
            zAcc="";
            xRot="";
            yRot="";
            zRot="";
            AccRms="";
            RotRms="";
            roll="";
            pitch="";
        }


        if(count != previouscount){

            Intent result = new Intent("CountResult");
            result.putExtra("Count",count);
            result.putExtra("Exercise",Result);
            context.sendBroadcast(result);

            previouscount = count;
        }
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {
        String url;
        JSONObject values;
        NetworkTask(String url, JSONObject values){
            this.url = url;
            this.values = values;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress bar를 보여주는 등등의 행위
        }
        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);
            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }
        @Override
        protected void onPostExecute(String result) {
            // 통신이 완료되면 호출됩니다.
            // 결과에 따른 UI 수정 등은 여기서 합니다.
            Result = result.substring(result.lastIndexOf(":")+1);
            Result = Result.replace("\"","");
            Result = Result.replace("}","");
            System.out.println(Result);
        }
    }

}
