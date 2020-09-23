//catchme if you can

package com.example.healthcoach01;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


public class Fragment2 extends Fragment {


    //프로필 사진
    CircleImageView rank1;
    CircleImageView rank2;
    CircleImageView rank3;
    CircleImageView rank4;

    //1,2,3,4 위
    ImageView r1;
    ImageView r2;
    ImageView r3;
    ImageView r4;

    TextView here1;
    TextView here2;
    TextView here3;
    TextView here4;

    //프로필 네임 (닉네임)
    TextView name1;
    TextView name2;
    TextView name3;
    TextView name4;

    //걸음 수
    TextView step1;
    TextView step2;
    TextView step3;
    TextView step4;


    String step_1;
    String step_2;
    String step_3;
    String step_4;

    Integer stp1;
    Integer stp2;
    Integer stp3;
    Integer stp4;

    String Nickname;
    String[] nickarr;

    String step_count;

    //불러올 닉네임
    String nickname;
    String nickname1;
    String nickname2;
    String nickname3;
    String nickname4;

    private String ID,PW, Step, Key;
    Integer stepdial;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment2, container, false);


        rank1 = (CircleImageView) view.findViewById(R.id.rank1);
        rank2 = (CircleImageView) view.findViewById(R.id.rank2);
        rank3 = (CircleImageView) view.findViewById(R.id.rank3);
        rank4 = (CircleImageView) view.findViewById(R.id.rank4);

        r1 = (ImageView) view.findViewById(R.id.r1);
        r2 = (ImageView) view.findViewById(R.id.r2);
        r3 = (ImageView) view.findViewById(R.id.r3);
        r4 = (ImageView) view.findViewById(R.id.r4);

        step1 = (TextView) view.findViewById(R.id.step1);
        step2 = (TextView) view.findViewById(R.id.step2);
        step3 = (TextView) view.findViewById(R.id.step3);
        step4 = (TextView) view.findViewById(R.id.step4);

        name1 = (TextView) view.findViewById(R.id.name1);
        name2 = (TextView) view.findViewById(R.id.name2);
        name3 = (TextView) view.findViewById(R.id.name3);
        name4 = (TextView) view.findViewById(R.id.name4);

        here1 = (TextView) view.findViewById(R.id.here1);
        here2 = (TextView) view.findViewById(R.id.here2);
        here3 = (TextView) view.findViewById(R.id.here3);
        here4 = (TextView) view.findViewById(R.id.here4);

        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
        // nowDate 변수에 값을 저장한다.
        final String formatDate = sdfNow.format(date);


        SharedPreferences preferences = getActivity().getSharedPreferences("UserInfo", 0);
        Nickname = preferences.getString("Nickname","");


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference stepRef = database.getReference().child("Rank");


        stepRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Integer> formapping = new HashMap<>();

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    nickname = dataSnapshot1.getKey();
                    nickarr = nickname.split(" ");

                    step_count = (String) dataSnapshot1.child(formatDate).child("Step").getValue();
                    //steparr = step_count.split(" ");

                    //해시맵 함수에 닉네임이랑 스텝수 mapping해서 넣음
                    for(int i = 0; i < nickarr.length; i++) {
                        try {
                            formapping.put(nickname, Integer.valueOf(step_count));
                        }catch(NumberFormatException e){
                            //NumberformatException발생 시
                        }
                    }for(int i = 1; i<5; i++){
                        formapping.put("None" + i, -1);
                    }

                }
                //value 내림차순으로 정렬하고, value가 같으면 key 오름차순으로 정렬함
                List<Map.Entry<String,Integer>> list_entries = new LinkedList<>(formapping.entrySet());
                Collections.sort(list_entries, new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                        int comparison = ((o1.getValue()) - (o2.getValue())) * - 1;
                        return comparison == 0 ? o1.getKey().compareTo(o2.getKey()) : comparison;
                        //o2.getValue().compareTo(o1.getValue());
                    }
                });

                //순서 유지를 위해 LinkedHshMap을 사용
                Map<String, Integer> sortedMap = new LinkedHashMap<>();
                for(Iterator<Map.Entry<String, Integer>> iter = list_entries.iterator(); iter.hasNext();){
                    Map.Entry<String, Integer> entry = iter.next();
                    sortedMap.put(entry.getKey(), entry.getValue());
                }


                System.out.print("순차점수 : " + sortedMap);
                System.out.println();


                stp1 = sortedMap.get(sortedMap.keySet().toArray()[0]);
                stp2 = sortedMap.get(sortedMap.keySet().toArray()[1]);
                stp3 = sortedMap.get(sortedMap.keySet().toArray()[2]);
                stp4 = sortedMap.get(sortedMap.keySet().toArray()[3]);

                step_1 = Integer.toString(stp1);
                step_2 = Integer.toString(stp2);
                step_3 = Integer.toString(stp3);
                step_4 = Integer.toString(stp4);

                step1.setText(step_1);
                /*
                if(step_1==null){
                    step1.setText("0");
                }*/
                step2.setText(step_2);
                /*
                if(step_2==null){
                    step2.setText("0");
                }*/
                step3.setText(step_3);
                /*
                if(step_3==null){
                    step3.setText("0");
                }*/
                step4.setText(step_4);
                /*
                if(step_4==null){
                    step4.setText("0");
                }*/

                nickname1 = (String) sortedMap.keySet().toArray()[0];
                nickname2 = (String) sortedMap.keySet().toArray()[1];
                nickname3 = (String) sortedMap.keySet().toArray()[2];
                nickname4 = (String) sortedMap.keySet().toArray()[3];

                name1.setText(nickname1);
                if(nickname1 == null){
                    name1.setText("Friend");
                }
                name2.setText(nickname2);
                if(nickname2 == null){
                    name2.setText("Friend");
                }
                name3.setText(nickname3);
                if(nickname3 == null){
                    name3.setText("Friend");
                }
                name4.setText(nickname4);
                if(nickname4 == null){
                    name4.setText("Friend");
                }

                here1.setText(nickname1);
                if(nickname1 == null){
                    here1.setText("Friend");
                }
                here2.setText(nickname2);
                if(nickname2 == null){
                    here2.setText("Friend");
                }
                here3.setText(nickname3);
                if(nickname3 == null){
                    here3.setText("Friend");
                }
                here4.setText(nickname4);
                if(nickname4 == null){
                    here4.setText("Friend");
                }


                //user1 좌표

                if(stp1 == -1 || stp1 == 0){
                    here1.setX(85);
                    here1.setY(1135);
                }
                if(stp1 > 0 && 500 > stp1) {
                    here1.setX(85);
                    here1.setY(1050);
                    here1.invalidate();
                }
                else if(stp1 >= 500 && stp1 < 1000){
                    here1.setX(100);
                    here1.setY(1000);
                }
                else if(stp1 >=1000 && stp1 < 1500){
                    here1.setX(138);
                    here1.setY(950);
                }
                else if(stp1 >= 1500 && stp1 < 2000){
                    here1.setX(150);
                    here1.setY(920);
                }
                else if(stp1 >= 2000 && stp1 < 2500){
                    here1.setX(170);
                    here1.setY(900);
                }
                else if(stp1 >= 2500 && stp1 < 3000){
                    here1.setX(210);
                    here1.setY(845);
                }
                else if(stp1 >= 3000 && stp1 < 3500){
                    here1.setX(260);
                    here1.setY(820);
                }
                else if(stp1 >= 3500 && stp1 < 4000){
                    here1.setX(310);
                    here1.setY(800);
                }
                else if(stp1 >= 4000 && stp1 < 4500){
                    here1.setX(360);
                    here1.setY(820);
                }
                else if(stp1 >= 4500 && stp1 < 5000){
                    here1.setX(410);
                    here1.setY(830);
                }
                else if(stp1 >= 5000 && stp1 < 5500){
                    here1.setX(460);
                    here1.setY(855);
                }
                else if(stp1 >= 5500 && stp1 < 6000){
                    here1.setX(510);
                    here1.setY(865);
                }
                else if(stp1 >= 6000 && stp1 < 6500){
                    here1.setX(580);
                    here1.setY(905);
                }
                else if(stp1 >= 6500 && stp1 < 7000){
                    here1.setX(620);
                    here1.setY(945);
                }
                else if(stp1 >= 7000 && stp1 < 7500){
                    here1.setX(670);
                    here1.setY(990);
                }
                else if(stp1 >= 7500 && stp1 < 8000){
                    here1.setX(710);
                    here1.setY(1025);
                }
                else if(stp1 >= 8000 && stp1 < 8500){
                    here1.setX(760);
                    here1.setY(1090);
                }
                else if(stp1 >= 8500 && stp1 < 9000){
                    here1.setX(770);
                    here1.setY(1120);
                }
                else if(stp1 >= 9000 && stp1 < 9500){
                    here1.setX(785);
                    here1.setY(1140);
                }
                else if(stp1 >= 9500 && stp1 < 10000){
                    here1.setX(785);
                    here1.setY(1145);
                }
                else if(stp1 >= 10000){
                    here1.setX(810);
                    here1.setY(1210);
                }


                //user2 좌표
                if(stp2 == -1 || stp2 == 0){
                    here2.setX(85);
                    here2.setY(1135);
                }
                if(stp2 > 0 && 500 > stp2) {
                    here2.setX(85);
                    here2.setY(1050);
                    here2.invalidate();
                }
                else if(stp2 >= 500 && stp2 < 1000){
                    here2.setX(100);
                    here2.setY(1000);
                }
                else if(stp2 >=1000 && stp2 < 1500){
                    here2.setX(138);
                    here2.setY(950);
                }
                else if(stp2 >= 1500 && stp2 < 2000){
                    here2.setX(150);
                    here2.setY(920);
                }
                else if(stp2 >= 2000 && stp2 < 2500){
                    here2.setX(170);
                    here2.setY(900);
                }
                else if(stp2 >= 2500 && stp2 < 3000){
                    here2.setX(210);
                    here2.setY(845);
                }
                else if(stp2 >= 3000 && stp2 < 3500){
                    here2.setX(260);
                    here2.setY(820);
                }
                else if(stp2 >= 3500 && stp2 < 4000){
                    here2.setX(310);
                    here2.setY(800);
                }
                else if(stp2 >= 4000 && stp2 < 4500){
                    here2.setX(360);
                    here2.setY(820);
                }
                else if(stp2 >= 4500 && stp2 < 5000){
                    here2.setX(410);
                    here2.setY(830);
                }
                else if(stp2 >= 5000 && stp2 < 5500){
                    here2.setX(460);
                    here2.setY(855);
                }
                else if(stp2 >= 5500 && stp2 < 6000){
                    here2.setX(510);
                    here2.setY(865);
                }
                else if(stp2 >= 6000 && stp2 < 6500){
                    here2.setX(580);
                    here2.setY(905);
                }
                else if(stp2 >= 6500 && stp2 < 7000){
                    here2.setX(620);
                    here2.setY(945);
                }
                else if(stp2 >= 7000 && stp2 < 7500){
                    here2.setX(670);
                    here2.setY(990);
                }
                else if(stp2 >= 7500 && stp2 < 8000){
                    here2.setX(710);
                    here2.setY(1025);
                }
                else if(stp2 >= 8000 && stp2 < 8500){
                    here2.setX(760);
                    here2.setY(1090);
                }
                else if(stp2 >= 8500 && stp2 < 9000){
                    here2.setX(770);
                    here2.setY(1120);
                }
                else if(stp2 >= 9000 && stp2 < 9500){
                    here2.setX(785);
                    here2.setY(1140);
                }
                else if(stp2 >= 9500 && stp2 < 10000){
                    here2.setX(785);
                    here2.setY(1145);
                }
                else if(stp2 >= 10000){
                    here2.setX(810);
                    here2.setY(1210);
                }


                //user3 좌표
                if(stp3 == -1 || stp3 == 0){
                    here3.setX(85);
                    here3.setY(1135);
                }
                if(stp3 > 0 && 500 > stp3) {
                    here3.setX(85);
                    here3.setY(1050);
                    here3.invalidate();
                }
                else if(stp3 >= 500 && stp3 < 1000){
                    here3.setX(100);
                    here3.setY(1000);
                }
                else if(stp3 >=1000 && stp3 < 1500){
                    here3.setX(138);
                    here3.setY(950);
                }
                else if(stp3 >= 2000 && stp3 < 2500){
                    here3.setX(170);
                    here3.setY(900);
                }
                else if(stp3 >= 2500 && stp3 < 3000){
                    here3.setX(210);
                    here3.setY(845);
                }
                else if(stp3 >= 3000 && stp3 < 3500){
                    here3.setX(260);
                    here3.setY(820);
                }
                else if(stp3 >= 3500 && stp3 < 4000){
                    here3.setX(310);
                    here3.setY(800);
                }
                else if(stp3 >= 4000 && stp3 < 4500){
                    here3.setX(360);
                    here3.setY(820);
                }
                else if(stp3 >= 4500 && stp3 < 5000){
                    here3.setX(410);
                    here3.setY(830);
                }
                else if(stp3 >= 5000 && stp3 < 5500){
                    here3.setX(460);
                    here3.setY(855);
                }
                else if(stp3 >= 5500 && stp3 < 6000){
                    here3.setX(510);
                    here3.setY(865);
                }
                else if(stp3 >= 6000 && stp3 < 6500){
                    here3.setX(580);
                    here3.setY(905);
                }
                else if(stp3 >= 6500 && stp3 < 7000){
                    here3.setX(620);
                    here3.setY(945);
                }
                else if(stp3 >= 7000 && stp3 < 7500){
                    here3.setX(670);
                    here3.setY(990);
                }
                else if(stp3 >= 7500 && stp3 < 8000){
                    here3.setX(710);
                    here3.setY(1025);
                }
                else if(stp3 >= 8000 && stp3 < 8500){
                    here3.setX(760);
                    here3.setY(1090);
                }
                else if(stp3 >= 8500 && stp3 < 9000){
                    here3.setX(770);
                    here3.setY(1120);
                }
                else if(stp3 >= 9000 && stp3 < 9500){
                    here3.setX(785);
                    here3.setY(1140);
                }
                else if(stp3 >= 9500 && stp3 < 10000){
                    here3.setX(785);
                    here3.setY(1145);
                }
                else if(stp3 >= 10000){
                    here3.setX(810);
                    here3.setY(1210);
                }


                //user4 좌표
                if(stp4 == -1 || stp4 == 0){
                    here4.setX(85);
                    here4.setY(1135);
                }
                if(stp4 > 0 && 500 > stp4) {
                    here4.setX(85);
                    here4.setY(1050);
                    here4.invalidate();
                }
                else if(stp4 >= 500 && stp4 < 1000){
                    here4.setX(100);
                    here4.setY(1000);
                }
                else if(stp4 >=1000 && stp4 < 1500){
                    here4.setX(138);
                    here4.setY(950);
                }
                else if(stp4 >= 1500 && stp4 < 2000){
                    here4.setX(150);
                    here4.setY(920);
                }
                else if(stp4 >= 2000 && stp4 < 2500){
                    here4.setX(170);
                    here4.setY(900);
                }
                else if(stp4 >= 2500 && stp4 < 3000){
                    here4.setX(210);
                    here4.setY(845);
                }
                else if(stp4 >= 3000 && stp4 < 3500){
                    here4.setX(260);
                    here4.setY(820);
                }
                else if(stp4 >= 3500 && stp4 < 4000){
                    here4.setX(310);
                    here4.setY(800);
                }
                else if(stp4 >= 4000 && stp4 < 4500){
                    here4.setX(360);
                    here4.setY(820);
                }
                else if(stp4 >= 4500 && stp4 < 5000){
                    here4.setX(410);
                    here4.setY(830);
                }
                else if(stp4 >= 5000 && stp4 < 5500){
                    here4.setX(460);
                    here4.setY(855);
                }
                else if(stp4 >= 5500 && stp4 < 6000){
                    here4.setX(510);
                    here4.setY(865);
                }
                else if(stp4 >= 6000 && stp4 < 6500){
                    here4.setX(580);
                    here4.setY(905);
                }
                else if(stp4 >= 6500 && stp4 < 7000){
                    here4.setX(620);
                    here4.setY(945);
                }
                else if(stp4 >= 7000 && stp4 < 7500){
                    here4.setX(670);
                    here4.setY(990);
                }
                else if(stp4 >= 7500 && stp4 < 8000){
                    here4.setX(710);
                    here4.setY(1025);
                }
                else if(stp4 >= 8000 && stp4 < 8500){
                    here4.setX(760);
                    here4.setY(1090);
                }
                else if(stp4 >= 8500 && stp4 < 9000){
                    here4.setX(770);
                    here4.setY(1120);
                }
                else if(stp4 >= 9000 && stp4 < 9500){
                    here4.setX(785);
                    here4.setY(1140);
                }
                else if(stp4 >= 9500 && stp4 < 10000){
                    here4.setX(785);
                    here4.setY(1145);
                }
                else if(stp4 >= 10000){
                    here4.setX(810);
                    here4.setY(1210);
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //사용자 별 dialog 띄워주기
        SharedPreferences user = getActivity().getSharedPreferences("UserInfo", 0);
        Key = user.getString("Key","");


        DatabaseReference dialogRef = FirebaseDatabase.getInstance().getReference().child("Data/"+Key);
        dialogRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                HashMap<String, String> LoginData = (HashMap<String, String>) dataSnapshot.getValue();
                if(dataSnapshot.getKey().contentEquals(formatDate)) {

                    Step = LoginData.get("Step");
                    stepdial = Integer.parseInt(Step);


                    if(stepdial == 10000){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Day Challenge").setMessage("Congratulations! :) " + "\n" + "You crushed the Goal Day Challenge with 10,000 steps");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getContext(), "OK CLICK", Toast.LENGTH_SHORT).show();
                            }
                        });
                        /*
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Toast.makeText(getContext(), "Cancel Click", Toast.LENGTH_SHORT).show();
                            }
                        });
*/
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Congratulations!").setMessage("선택하세요");

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