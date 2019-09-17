package com.example.itchyfeet;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.IOException;
import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;

public class ShareListViewActivity extends AppCompatActivity {

    String UserID;
    ClientService clientservice;
    String ViewUserPlanCommand = "getUserPlanxxxxxxxxxxxxxxxxxxxxxx";
    public static Client client;
    GlobalApplication GlobalID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.travelnote_list_view);



        UserID = GlobalID.KakaoID;
        ViewUserPlanCommand = ViewUserPlanCommand + UserID;
        final ArrayList<tPlan> receivetPlan;

        try {
            clientservice = new ClientService();
            clientservice.ViewUserPlan(ViewUserPlanCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }

        receivetPlan = client.receivetarrayList;

        final ArrayList<String> arrayList = new ArrayList<String>();

        ListView listview;
        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);

        for (int index = 0; index < receivetPlan.size(); index++) {
            adapter.addItem(String.valueOf(index + 1), receivetPlan.get(index).getTitle(),
                      receivetPlan.get(index).getBdate(),   receivetPlan.get(index).getAdate());
            arrayList.add(receivetPlan.get(index).getTitle());
        }

        listview.setAdapter(adapter);
        ArrayAdapter<String> nadapte = new ArrayAdapter<String>(this, simple_list_item_1, arrayList);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String nextStr = arrayList.get(position).toString();
                Intent nextIntent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        ShareDayListViewActivity.class); // 다음 넘어갈 클래스 지정
                nextIntent.putExtra("name", nextStr);
                startActivity(nextIntent);
                finish();
            }
        });


    }
}