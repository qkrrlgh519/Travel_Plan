package com.example.itchyfeet;

/**
 * Created by giho on 2017-05-21.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;

import java.io.IOException;
import java.util.ArrayList;

public class DayInfoActivity extends Activity {

    String UserID;
    ClientService clientservice;
    String command = "vPlan";
    String day, title;
    public static Client client;
    GlobalApplication GlobalID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travelnote_travel_day_info);
        final Typeface typeFace = Typeface.createFromAsset(getAssets(),"BMJUA.ttf");
        int Tourism, Accommodation, Restaurant, Polyline;
        Intent intent = getIntent();
        day = intent.getExtras().getString("day");
        title = intent.getExtras().getString("title");
        UserID = GlobalID.KakaoID;
        command = command + UserID + "@" + title + "#" + day.substring(0,day.indexOf("일")) + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

        final ArrayList<mPlan> receivemPlan;
        final ArrayList<plPlan> receiveplPlan;

        try {
            clientservice = new ClientService();
            clientservice.SendViewCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

        receivemPlan = client.receivemarrayList;
        receiveplPlan = client.receiveparrayList;
        Tourism = R.drawable.tourism;
        Accommodation = R.drawable.hotel;
        Restaurant = R.drawable.food;
        Polyline = R.drawable.black_line;

        Button viewmapbtn = (Button)findViewById(R.id.btnviewmap);
        TextView titletxt = (TextView)findViewById(R.id.titletext11);
        titletxt.setTypeface(typeFace);
        ListView listview ;
        DayInfoAdapter adapter;

        // Adapter 생성
        adapter = new DayInfoAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.dayinfolistview);

        for(int index = 0; index < receivemPlan.size(); index++){
            mPlan mplan = new mPlan();
            mplan.setTheme(receivemPlan.get(index).getTheme());
            mplan.setLocation(receivemPlan.get(index).getLocation());
            mplan.setbTime(receivemPlan.get(index).getbTime());
            mplan.setaTime(receivemPlan.get(index).getaTime());

            if(mplan.getTheme().equals("관광")){
                adapter.addItem(ContextCompat.getDrawable(this,Tourism),mplan.getLocation(),
                        mplan.getbTime()+"~"+mplan.getaTime());
            }
            else if(mplan.getTheme().equals("숙박")){
                adapter.addItem(ContextCompat.getDrawable(this,Accommodation),mplan.getLocation(),
                        mplan.getbTime()+"~"+mplan.getaTime());
            }
            else if(mplan.getTheme().equals("맛집")){
                adapter.addItem(ContextCompat.getDrawable(this,Restaurant),mplan.getLocation(),
                        mplan.getbTime()+"~"+mplan.getaTime());
            }

            for(int index2 = 0; index2 < receiveplPlan.size(); index2++){
                plPlan plplan = new plPlan();
                plplan.setbP2P(receiveplPlan.get(index2).getbP2P());
                plplan.setaP2P(receiveplPlan.get(index2).getaP2P());
                plplan.setbTime(receiveplPlan.get(index2).getbTime());
                plplan.setaTime(receiveplPlan.get(index2).getaTime());
                if(mplan.getLocation().equals(plplan.getbP2P())){
                    adapter.addItem(ContextCompat.getDrawable(this,Polyline),
                            plplan.getbP2P()+"-"+plplan.getaP2P(),
                            plplan.getbTime()+"~"+plplan.getaTime());
                }
            }
        }

        viewmapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dayStr = day;
                String titleStr = title;
                Intent nextIntent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        DayInfoMapActivity.class); // 다음 넘어갈 클래스 지정
                nextIntent.putExtra("day",dayStr);
                nextIntent.putExtra("title",titleStr);
                startActivity(nextIntent);
            }
        });

        listview.setAdapter(adapter);
    }
}