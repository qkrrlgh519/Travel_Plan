package com.example.itchyfeet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_list_item_1;


public class ShareEntireActivity extends AppCompatActivity {

    String UserID;
    ClientService clientservice;
    public static Client client;
    GlobalApplication GlobalID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int Send,Receive;
        final ArrayList<mPlan> receivemPlan;
        final Typeface typeFace = Typeface.createFromAsset(getAssets(),"BMJUA.ttf");
        clientservice = new ClientService();

        try {
            clientservice.ViewShare(GlobalID.KakaoID);
        } catch (IOException e) {
            e.printStackTrace();
        }


        receivemPlan = client.receivemarrayList;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.travelnote_share_entirelist_view);
        Button sharebtn = (Button) findViewById(R.id.sharebutton);
        TextView tit = (TextView) ShareEntireActivity.this.findViewById(R.id.titletext201);
        tit.setTypeface(typeFace);
        sharebtn.setTypeface(typeFace);

        // 공유하기 버튼 클릭시
        sharebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        ShareListViewActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
                finish();
            }
        });
        Send =R.drawable.send_icon_1;
        Receive=R.drawable.receive_icon_1;
        ArrayList<String> arrayList = new ArrayList<String>();
        // Adapter 생성
        ShareListAdapter adapter = new ShareListAdapter();
        ListView listview ;
        // 리스트뷰 참조 및 Adapter달기

        if(receivemPlan.get(0).getAdate().equals("섹스")){

        }
        else {
            for (int index = 0; index < receivemPlan.size(); index++) {
                mPlan mplan = new mPlan();
                mplan.setAdate(receivemPlan.get(index).getAdate());
                mplan.setBdate(receivemPlan.get(index).getBdate());
                mplan.setTitle(receivemPlan.get(index).getTitle());
                mplan.setDay(receivemPlan.get(index).getDay());
                mplan.setNote(receivemPlan.get(index).getNote());
                //sender
                if (mplan.adate.equals(GlobalID.KakaoID)) {
                    adapter.addItem(ContextCompat.getDrawable(this, Send), "  "+receivemPlan.get(index).getTitle()+"의 "+receivemPlan.get(index).getDay() +"일차",
                            "   "+receivemPlan.get(index).atime +"->"+ receivemPlan.get(index).btime +"   "+receivemPlan.get(index).getNote());

                }
                //receive
                if (mplan.bdate.equals(GlobalID.KakaoID)) {
                    adapter.addItem(ContextCompat.getDrawable(this, Receive), "  "+receivemPlan.get(index).getTitle()+"의 "+receivemPlan.get(index).getDay() +"일차",
                            "   "+receivemPlan.get(index).atime+ "->"+receivemPlan.get(index).btime + "   "+receivemPlan.get(index).getNote());
                }
            }
            listview = (ListView) findViewById(R.id.sharelistview);
            listview.setAdapter(adapter);
        }
    }
}