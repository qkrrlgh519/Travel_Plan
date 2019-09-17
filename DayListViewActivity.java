package com.example.itchyfeet;

import java.io.IOException;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import static android.R.layout.simple_list_item_1;

public class DayListViewActivity extends Activity {

    String UserID;
    String h = null,f =null ,s = null;
    String title;
    ClientService clientservice;
    String ViewUserPlanNumberCommand = "xgetUserPlanNumberxxxxxxxxxxxxxxx";
    public static Client client;

    GlobalApplication GlobalID;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.travelnote_travel_daylistview);

        Intent intent = getIntent();
        title = intent.getExtras().getString("name");



        UserID = GlobalID.KakaoID;
        ViewUserPlanNumberCommand = ViewUserPlanNumberCommand +"@"+ UserID +"@" + title;
        final ArrayList<tPlan> receivetPlan;

        try {
            clientservice = new ClientService();
            clientservice.ViewUserPlanNumber(ViewUserPlanNumberCommand);
        }catch(IOException e){
            e.printStackTrace();
        }

        receivetPlan = client.receivetarrayList;

        ListView list = (ListView)findViewById(R.id.daylistview);
        DayListAdapter adapters=new DayListAdapter();
        final ArrayList<String> arrayList = new ArrayList<String>();

        for(int index2 = 1; index2 <= Integer.parseInt(receivetPlan.get(0).getAdate()
        ); index2++){
            for(int index = 0; index < receivetPlan.size(); index++){
                if(receivetPlan.get(index).getTitle().equals(String.valueOf(index2))){

                    if(receivetPlan.get(index).getBdate().equals("숙박")){
                        h = receivetPlan.get(index).getDay();
                    }
                    else if(receivetPlan.get(index).getBdate().equals("맛집")){
                        f = receivetPlan.get(index).getDay();
                    }
                    else{
                        s = receivetPlan.get(index).getDay();
                    }

                }
            }
            adapters.addItem(String.valueOf(index2)+"일차",h+"원",s+"원",f+"원");
            arrayList.add(String.valueOf(index2)+"일차");

            /*if(title.equals(receivetPlan.get(index).getTitle())){
                for(int index2 = 0; index2 < Integer.parseInt(receivetPlan.get(index).getDay()); index2++){
                    adapters.addItem(String.valueOf(index2 + 1)+"일차"); //<머니들 가져와서 넣기
                    arrayList.add(String.valueOf(index2 + 1)+"일차");
                }
            }*/
        }

        list.setAdapter(adapters);
        ArrayAdapter<String> nadapter=new ArrayAdapter<String>(this,simple_list_item_1,arrayList);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String dayStr = arrayList.get(position).toString();
                String titleStr = title;
                Intent nextIntent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        DayInfoActivity.class); // 다음 넘어갈 클래스 지정
                nextIntent.putExtra("day",dayStr);
                nextIntent.putExtra("title",titleStr);
                startActivity(nextIntent);
            }
        });
    }
}

