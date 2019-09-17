package com.example.itchyfeet;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.kakaolink.internal.AppActionInfo;
import com.kakao.util.KakaoParameterException;

import static android.R.layout.simple_list_item_1;

public class ShareDayListViewActivity extends Activity {

    String UserID;
    String title;
    String day;
    String h = null,f =null ,s = null;
    ClientService clientservice;
    String ViewUserPlanNumberCommand = "xgetUserPlanNumberxxxxxxxxxxxxxxx";
    public static Client client;
    GlobalApplication GlobalID;
    AlertDialog dialog1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.travelnote_share_daylistview);

        Intent intent = getIntent();
        title = intent.getExtras().getString("name");


        UserID = GlobalID.KakaoID;
        ViewUserPlanNumberCommand = ViewUserPlanNumberCommand +"@"+ UserID +"@" + title;
        final ArrayList<tPlan> receivetPlan;

        try {
            clientservice = new ClientService();
            clientservice.ViewUserPlanNumber(ViewUserPlanNumberCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }

        receivetPlan = client.receivetarrayList;

        ListView list = (ListView) findViewById(R.id.daylistview);
        DayListAdapter adapters = new DayListAdapter();
        final ArrayList<String> arrayList = new ArrayList<String>();

        for (int index2 = 1; index2 <= Integer.parseInt(receivetPlan.get(0).getAdate()
        ); index2++) {
            for (int index = 0; index < receivetPlan.size(); index++) {
                if (receivetPlan.get(index).getTitle().equals(String.valueOf(index2))) {

                    if (receivetPlan.get(index).getBdate().equals("숙박")) {
                        h = receivetPlan.get(index).getDay();
                    } else if (receivetPlan.get(index).getBdate().equals("맛집")) {
                        f = receivetPlan.get(index).getDay();
                    } else {
                        s = receivetPlan.get(index).getDay();
                    }

                }
            }
            adapters.addItem(String.valueOf(index2)+"일차",h+"원",s+"원",f+"원");
            arrayList.add(String.valueOf(index2)+"일차");
            list.setAdapter(adapters);
            ArrayAdapter<String> nadapter = new ArrayAdapter<String>(this, simple_list_item_1, arrayList);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    day = arrayList.get(position).toString();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ShareDayListViewActivity.this);
                    Context mcontext1 = getApplicationContext();
                    LayoutInflater inflater = (LayoutInflater) mcontext1.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.travelnote_dialog_askshare, (ViewGroup) findViewById(R.id.linearLayout6));
                    LinearLayout linear = (LinearLayout) layout;

                    Button OkSharebtn = (Button) linear.findViewById(R.id.btnOkShare);
                    Button NoSharebtn = (Button) linear.findViewById(R.id.btnNoShare);

                    OkSharebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                            shareKakao();
                            finish();
                        }
                    });

                    NoSharebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                    dialog1 = builder1.create();
                    dialog1.setTitle("공유확인");
                    dialog1.setView(layout);
                    dialog1.show();
                }
            });
        }
    }

    public void shareKakao() {

//            /*이미지 가로/세로 사이즈는 80px 보다 커야하며, 이미지 용량은 500kb 이하로 제한된다.*/
//            String url = "http://upload2.inven.co.kr/upload/2015/09/27/bbs/i12820605286.jpg";
//            kakaoBuilder.addImage(url, 1080, 1920);
//
//            /*앱 실행버튼 추가*/
//            kakaoBuilder.addAppButton("앱 실행");
//
//            /*메시지 발송*/
//            kakaoLink.sendMessage(kakaoBuilder, this);

        try {

            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
            final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
            AppActionInfo aai = new AppActionInfo(AppActionInfo.ACTION_INFO_OS.ANDROID, AppActionBuilder.DEVICE_TYPE.PHONE,
                    "params=" + GlobalID.KakaoID + "=" + title + "=" + day, "referrer=kakaotalklink");
            AppActionBuilder aab = new AppActionBuilder().addActionInfo(aai);
            kakaoTalkLinkMessageBuilder.addAppButton("공유 정보 도착", aab.build());

            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, this);
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }

    }
}