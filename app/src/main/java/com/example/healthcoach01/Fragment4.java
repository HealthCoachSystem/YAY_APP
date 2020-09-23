//운동 선택화면 + 블루투스 페어링 요청 / 블루투스 페어링 된 디바이스 목록 alertdialog

package com.example.healthcoach01;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class Fragment4 extends Fragment {
    final ArrayList<String> items = new ArrayList<String>() ;
    //final ArrayAdapter<String> adapters = new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_list_item_single_choice, items);
    //private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    String result = "";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            final View view= (View) inflater.inflate(R.layout.my_workout, container, false);
        final ListView listView;

        final CheckBox cb1, cb2, cb3, cb4, cb5;
        Button confirm;
        Button next;
        final TextView result2;


            cb1 = (CheckBox)view.findViewById(R.id.checkbox);
            cb2 = (CheckBox)view.findViewById(R.id.checkbox2);
            cb3 = (CheckBox)view.findViewById(R.id.checkbox3);
            cb4 = (CheckBox)view.findViewById(R.id.checkbox4);
            cb5 = (CheckBox)view.findViewById(R.id.checkbox5);
            result2 = (TextView)view.findViewById(R.id.result);


            //result2.setText(" ");

            confirm = (Button)view.findViewById(R.id.confirm);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    result = "";

                    if(cb1.isChecked() == true) {
                        result += "SITUPS" + "\n";
                    } else {
                        result.replace("SITUPS", "");
                    }
                    if(cb2.isChecked() == true){
                        result += "SQUATS"+ "\n";
                    }else {
                        result.replace("SQUATS", "");
                    }
                    if(cb3.isChecked() == true) {
                        result += "PUSHUPS"+ "\n";
                    } else{
                        result.replace("PUSHUPS", "");
                    }
                   if(cb4.isChecked() == true) {
                        result += "SIDEBEND"+ "\n";
                    } else {
                        result.replace("SIDEBEND", "");
                    } if(cb5.isChecked() == true) {
                        result += "SIDECRUNCH"+ "\n";
                    }else {
                        result.replace("SIDECRUNCH", "");
                    }

                   result2.setText(result);
                }
            });



            next = (Button)view.findViewById(R.id.next);
            next.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String list = (String) result2.getText();
                    Intent intent = new Intent(v.getContext(), WorkoutPlayRightBefore_myworkout.class);
                    intent.putExtra("list", list);
                    startActivity(intent);
                }
            });


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
