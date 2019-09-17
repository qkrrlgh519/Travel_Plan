package com.example.itchyfeet;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Text;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback {

    EditText getAdd;
    Button   getLocation;
    //MapView  map;
    //MapController controller;

    String strAddress;
    List<Address> listAddress;
    Geocoder geocoder;
    Address AddrAddress;
    LatLng location;

    private ArrayList<LatLng> arrayPoints;
    GoogleMap mGoogleMap;
    private tPlan tplan;
    private mPlan mplan;
    private plPlan plplan;
    private ArrayList<mPlan> mArrayList;
    private ArrayList<plPlan> plArrayList;
    private ArrayList<tPlan> tArrayList;
    private  String[] travelDay;
    Marker marker;
    int number;
    AlertDialog dialog, dialog3,dialog_day;
    ArrayList<String> spinnerlist;
    Spinner beginspinner;
    Spinner endspinner;


    private EditText textTitle;
    private TextView textCal1, textCal2;
    private Button btnCal1, btnCal2;
    private int tYear, tYear1;
    private int tMonth, tMonth1;
    private int tDay, tDay1;
    static final int DATE_DIALOG_ID_FROM=1;
    static final int DATE_DIALOG_ID_TO=2;

    private TextView textbTime, textaTime;
    private Button btnbTime, btnaTime;
    private int mHour,mHour1;
    private int mMinute, mMinute1;
    static final int TIME_DIALOG_ID_FROM=3;
    static final int TIME_DIALOG_ID_TO=4;
    private long result;
    private int daynumber;



    ClientService clientservice;
    GlobalApplication GlobalID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travelnote_main_map);
        Intent intent = getIntent();
        MapsInitializer.initialize(getApplicationContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button blocation = (Button) MainMapActivity.this.findViewById(R.id.bGetLocation);
        EditText editmap = (EditText) MainMapActivity.this.findViewById(R.id.etGetAddress);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        final AlertDialog.Builder builder3 = new AlertDialog.Builder(MainMapActivity.this);
        Context mcontext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater)MainMapActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout3 = inflater.inflate(R.layout.travelnote_travel_add,(ViewGroup)findViewById(R.id.linearLayout));
        final LinearLayout linear3 = (LinearLayout) layout3;

        Button btn1 = (Button)linear3.findViewById(R.id.textbtn1);
        Button btn2 = (Button)linear3.findViewById(R.id.textbtn2);
        textTitle = (EditText)linear3.findViewById(R.id.travel_add_textTitle);
        btnCal1 = (Button)linear3.findViewById(R.id.travel_add_btn1);
        btnCal2 = (Button)linear3.findViewById(R.id.travel_add_btn2);
        textCal1 = (TextView)linear3.findViewById(R.id.travel_add_text1);
        textCal2 = (TextView)linear3.findViewById(R.id.travel_add_text2);
        TextView tplantitletxt = (TextView) linear3.findViewById(R.id.travel_title);
        TextView tplandatetxt = (TextView)linear3.findViewById(R.id.travel_date);
        EditText titledit=(EditText)linear3.findViewById(R.id.travel_add_textTitle);
        TextView tplanAtime=(TextView) linear3.findViewById(R.id.travel_add_text1);
        TextView tplanBtime=(TextView) linear3.findViewById(R.id.travel_add_text2);
        TextView titletxt = (TextView) linear3.findViewById(R.id.titletext);

        btnCal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID_FROM);
            }
        });
        btnCal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID_TO);
            }
        });
        final Typeface typeFace = Typeface.createFromAsset(getAssets(), "BMJUA.ttf");
        blocation.setTypeface(typeFace);
        titletxt.setTypeface(typeFace);
        editmap.setTypeface(typeFace);
        tplantitletxt.setTypeface(typeFace);
        tplandatetxt.setTypeface(typeFace);
        btn1.setTypeface(typeFace);
        btn2.setTypeface(typeFace);
        titledit.setTypeface(typeFace);
        tplanAtime.setTypeface(typeFace);
        tplanBtime.setTypeface(typeFace);


        tplan = new tPlan();
        final Calendar c = Calendar.getInstance();
        tYear = c.get(Calendar.YEAR);
        tMonth = c.get(Calendar.MONTH);
        tDay = c.get(Calendar.DATE);
        tYear1= c.get(Calendar.YEAR);
        tMonth1= c.get(Calendar.MONTH);
        tDay1= c.get(Calendar.DATE);

        updateDate();


        builder3.setCancelable(false);
        builder3.setView(layout3);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //값 받고 저장하는 기능
                tArrayList = new ArrayList<tPlan>();
                tplan.setTitle(textTitle.getText().toString());
                tplan.setAdate(textCal1.getText().toString());
                tplan.setBdate(textCal2.getText().toString());
                tplan.setUserID(GlobalID.KakaoID);
                final String[] travelDay = new String[(int)result];
                for(int i=0;i<result;i++){
                    tplan.setDay(String.valueOf(i+1));
                    tArrayList.add(tplan);
                    tplan = new tPlan();
                    tplan.setUserID(GlobalID.KakaoID);
                    tplan.setTitle(textTitle.getText().toString());
                    tplan.setAdate(textCal1.getText().toString());
                    tplan.setBdate(textCal2.getText().toString());

                }
                try {
                    clientservice = new ClientService();
                    clientservice.SendTitle(tArrayList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dialog3.dismiss();
                final AlertDialog.Builder builder4 = new AlertDialog.Builder(MainMapActivity.this);
                Context mcontext = getApplicationContext();

                LayoutInflater inflater = (LayoutInflater)mcontext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout4 = inflater.inflate(R.layout.travelnote_add_day,(ViewGroup)findViewById(R.id.linearlayout11));
                LinearLayout linear4 = (LinearLayout) layout4;
                Typeface typeFace = Typeface.createFromAsset(getAssets(), "BMJUA.ttf");
                TextView mylist = (TextView) linear4.findViewById(R.id.titletext5);
                mylist.setTypeface(typeFace);

                CharSequence info[] = new CharSequence[(int) result];
                for(int i =0;i<(int)result;i++) {
                   info[i]=i+1+"일차";
                }

                builder4.setItems(info, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        daynumber=position+1;
                        String dayStr = String.valueOf(daynumber);
                        travelDay[position]=dayStr;
                        dialog.dismiss();
                    }

                });

                builder4.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainMapActivity.this, "등록이 완료되었습니다.",Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                MainActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(intent1); // 다음 화면으로 넘어간다
                    }
                });
                dialog_day=builder4.create();
                dialog_day.setCanceledOnTouchOutside(false);
                dialog_day.show();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        MainActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent2); // 다음 화면으로 넘어간다
            }
        });

        dialog3 = builder3.create();
        dialog3.setCanceledOnTouchOutside(false);
        dialog3.show();
    }

    //title
    private void updateDate(){
        String str = (tYear-2000)+"/"+(tMonth+1) + "/" + tDay ;
        String str1 = (tYear1-2000) + "/" + (tMonth1+1) + "/" + tDay1;
        textCal1.setText(str);
        textCal2.setText(str1);
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(tYear-2000,tMonth,tDay);
        endDate.set(tYear1-2000,tMonth1,tDay1);

        long rawResult = (endDate.getTimeInMillis()-startDate.getTimeInMillis())/1000;
        result = rawResult/(60*60*24)+1;



    }
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener(){
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth){
                    tYear=year;
                    tMonth=monthOfYear;
                    tDay=dayOfMonth;
                    updateDate();
                }
            };
    private DatePickerDialog.OnDateSetListener mDateSetListener1 =
            new DatePickerDialog.OnDateSetListener(){
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth){
                    tYear1=year;
                    tMonth1=monthOfYear;
                    tDay1=dayOfMonth;
                    updateDate();
                }
            };

    //marker
    private void updateTime(){
        String str = String.valueOf(mHour) + ":" + String.valueOf(mMinute);
        String str1 = String.valueOf(mHour1) + ":" + String.valueOf(mMinute1);
        textbTime.setText(str);
        textaTime.setText(str1);
    }


    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener(){
                public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                    mHour=hourOfDay;
                    mMinute=minute;
                    updateTime();
                }
            };
    private TimePickerDialog.OnTimeSetListener mTimeSetListener1 =
            new TimePickerDialog.OnTimeSetListener(){
                public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                    mHour1=hourOfDay;
                    mMinute1=minute;
                    updateTime();
                }
            };
    protected Dialog onCreateDialog(int id){
        switch(id){
            case DATE_DIALOG_ID_FROM:
                return new DatePickerDialog(this, mDateSetListener,tYear, tMonth, tDay);
            case DATE_DIALOG_ID_TO:
                return new DatePickerDialog(this, mDateSetListener1, tYear1, tMonth1, tDay1);
            case TIME_DIALOG_ID_FROM:
                return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,false);
            case TIME_DIALOG_ID_TO:
                return new TimePickerDialog(this, mTimeSetListener1, mHour1, mMinute1, false);
        }
        return null;
    }


    /**
     * OnMapReady 는 map이 사용가능하면 호출되는 콜백 메소드입니다
     * 여기서 marker 나 line, listener, camera 이동 등을 설정해두면 됩니다.
     * 이번 예제에서는 서울역 근처에 marker를 더하고 적절한 title과, zoom을 설정해둡니다
     * 이 시점에서, 만약 사용자 기기게 Google Play service가 설치되지 않으면
     * 설치하라고 메세지가 뜨게 되고,  설치후에 다시 이 앱으로 제어권이 넘어옵니다
     */



    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mArrayList = new ArrayList<mPlan>();
        arrayPoints = new ArrayList<LatLng>();
        plArrayList = new ArrayList<plPlan>();
        number = 0;

        getAdd = (EditText) findViewById(R.id.etGetAddress);
        getLocation = (Button) findViewById(R.id.bGetLocation);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.bGetLocation:
                        strAddress = getAdd.getText().toString();
                        geocoder = new Geocoder(MainMapActivity.this);

                        try{
                            listAddress = geocoder.getFromLocationName(strAddress, 5);

                            if(listAddress.size() > 0){
                                AddrAddress = listAddress.get(0);
                                location = new LatLng(AddrAddress.getLatitude(), AddrAddress.getLongitude());

                                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                                mGoogleMap.getUiSettings().setCompassEnabled(true);
                            }
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(
                new LatLng(37.555744, 126.970431)   // camera 좌표를 서울역 근처로 옮겨 봅니다.
        ));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mGoogleMap.animateCamera(zoom);

        //add markers
        findViewById(R.id.btn1).setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v) {
                        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
                            public void onMapClick(LatLng latLng) {
                                final LatLng markerlatLng = latLng;
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                mplan = new mPlan();
                                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                marker =  mGoogleMap.addMarker(markerOptions); //<------마커 삭제 가능함.

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainMapActivity.this);
                                Context mcontext = getApplicationContext();
                                LayoutInflater inflater =(LayoutInflater)MainMapActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                                View layout = inflater.inflate(R.layout.travelnote_dialog_marker,(ViewGroup)findViewById(R.id.linearLayout2));
                                final LinearLayout linear = (LinearLayout) layout;

                                final EditText editLocation = (EditText)linear.findViewById(R.id.dialog_location);
                                final Spinner editThema = (Spinner) linear.findViewById(R.id.dialog_thema);
                                final EditText editPrice = (EditText)linear.findViewById(R.id.dialog_price);
                                final EditText editNote = (EditText)linear.findViewById(R.id.dialog_note);
                                final Button btnYES1 = (Button)linear.findViewById(R.id.buttonYES);
                                final Button btnNO1 = (Button)linear.findViewById(R.id.buttonNO);

                                textbTime = (TextView) linear.findViewById(R.id.editText3);
                                btnbTime=(Button) linear.findViewById(R.id.button3);
                                textaTime = (TextView) linear.findViewById(R.id.editText33);
                                btnaTime=(Button) linear.findViewById(R.id.button4);


                                TextView titletxt1 = (TextView)linear.findViewById(R.id.titletext1);
                                TextView locatxt = (TextView)linear.findViewById(R.id.textView1);
                                TextView thematxt = (TextView)linear.findViewById(R.id.textView2);
                                TextView memotxt = (TextView)linear.findViewById(R.id.textView5);
                                TextView timetxt = (TextView)linear.findViewById(R.id.textview3);
                                TextView pricetxt =(TextView)linear.findViewById(R.id.textView4);
                                TextView atime = (TextView)linear.findViewById(R.id.editText3);
                                TextView btime =(TextView)linear.findViewById(R.id.editText33);
                                final Typeface typeFace = Typeface.createFromAsset(getAssets(), "BMJUA.ttf");
                                btnYES1.setTypeface(typeFace);
                                btnNO1.setTypeface(typeFace);
                                atime.setTypeface(typeFace);
                                btime.setTypeface(typeFace);
                                editLocation.setTypeface(typeFace);
                                editPrice.setTypeface(typeFace);
                                editNote.setTypeface(typeFace);
                                timetxt.setTypeface(typeFace);
                                titletxt1.setTypeface(typeFace);
                                locatxt.setTypeface(typeFace);
                                pricetxt.setTypeface(typeFace);
                                memotxt.setTypeface(typeFace);
                                thematxt.setTypeface(typeFace);


                                btnbTime.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showDialog(TIME_DIALOG_ID_FROM);
                                    }
                                });
                                btnaTime.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showDialog(TIME_DIALOG_ID_TO);
                                    }
                                });

                                final Calendar t = Calendar.getInstance();
                                mHour = t.get(Calendar.HOUR_OF_DAY);
                                mMinute = t.get(Calendar.MINUTE);
                                mHour1 = t.get(Calendar.HOUR_OF_DAY);
                                mMinute1 = t.get(Calendar.MINUTE);

                                updateTime();

                                btnYES1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        mplan.setMarkerId(marker.getId());
                                        mplan.setLat(markerlatLng.latitude);
                                        mplan.setLng(markerlatLng.longitude);
                                        mplan.setUserID(GlobalID.KakaoID);
                                        mplan.setLocation(editLocation.getText().toString());
                                        mplan.setTheme(editThema.getSelectedItem().toString());
                                        mplan.setbTime(textbTime.getText().toString());
                                        mplan.setaTime(textaTime.getText().toString());
                                        mplan.setNote(editNote.getText().toString());
                                        mplan.setDay(String.valueOf(daynumber));
                                        mplan.setTitle(textTitle.getText().toString());
                                        mplan.setAdate(textCal1.getText().toString());
                                        mplan.setBdate(textCal2.getText().toString());

                                        if(editLocation.getText().toString().equals("")){
                                            Toast.makeText(MainMapActivity.this, "위치는 필수 입력 정보입니다.",Toast.LENGTH_LONG).show();
                                            if(!(editPrice.getText().toString().equals(""))){
                                                mplan.setPrice(Integer.parseInt(editPrice.getText().toString()));
                                                //mArrayList.get(number).setPrice(Integer.parseInt(editPrice.getText().toString()));
                                            }
                                            else{
                                                mplan.setPrice(0);
                                            }
                                        }
                                        else{
                                            if(!(editPrice.getText().toString().equals(""))){
                                                mplan.setPrice(Integer.parseInt(editPrice.getText().toString()));
                                                //mArrayList.get(number).setPrice(Integer.parseInt(editPrice.getText().toString()));
                                            }
                                            else{
                                                mplan.setPrice(0);
                                            }
                                            dialog.dismiss();
                                        }
                                        mArrayList.add(mplan);
                                        Toast.makeText(MainMapActivity.this, "LatLng:"+new LatLng(mplan.getLat(), mplan.getLng())+"위치:"
                                                +mplan.getLocation()+"테마:"+mplan.getTheme()+"b시간:"+mplan.getbTime()+"a시간"+mplan.getaTime()
                                                +"비용"+mplan.getPrice()+"메모"+mplan.getNote(),Toast.LENGTH_LONG).show();
                                    }
                                });

                                btnNO1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        dialog.dismiss();
                                        //취소 버튼 눌렀을 때 구현될 기능 -> 포인트 지우기
                                    }
                                });


                                builder.setView(layout);
                                dialog= builder.create();
                                dialog.show();
                                dialog.setCanceledOnTouchOutside(false);
                                mGoogleMap.setOnMapClickListener(null);
                            }
                        });
                        closeOptionsMenu();
                    }
                });
        //add markers listener end



        //add polyline
        findViewById(R.id.btn2).setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v) {
                        if(mArrayList.size()>=2) {
                            plplan = new plPlan();
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainMapActivity.this);
                            LayoutInflater inflater2 = (LayoutInflater) MainMapActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                            View layout2 = inflater2.inflate(R.layout.travelnote_dialog_line,(ViewGroup)findViewById(R.id.linearLayout3));
                            final LinearLayout linear2 = (LinearLayout) layout2;
                            final Typeface typeFace = Typeface.createFromAsset(getAssets(), "BMJUA.ttf");
                            TextView linetitletxt = (TextView)linear2.findViewById(R.id.titletext6);
                            TextView txt1 = (TextView) linear2.findViewById(R.id.textView1);
                            TextView txt2 = (TextView) linear2.findViewById(R.id.textVehicle);
                            TextView txt3 = (TextView) linear2.findViewById(R.id.textView3);
                            TextView txt4 = (TextView) linear2.findViewById(R.id.textview3);
                            TextView txt5 = (TextView) linear2.findViewById(R.id.textview33);
                            TextView txt6 = (TextView) linear2.findViewById(R.id.textView4);
                            TextView txt7 = (TextView) linear2.findViewById(R.id.textView5) ;


                            linetitletxt.setTypeface(typeFace);
                            builder1.setView(layout2);

                            beginspinner = (Spinner)layout2.findViewById(R.id.dialog_beginlocation);
                            endspinner = (Spinner)layout2.findViewById(R.id.dialog_endlocation);
                            final Spinner editVehicle = (Spinner) linear2.findViewById(R.id.dialog_Vehicle);
                            final TextView editPrice = (TextView) linear2.findViewById(R.id.dialog_price2);
                            final TextView editNote = (TextView) linear2.findViewById(R.id.dialog_note2);

                            final ArrayList<String> spinnerlist1 = new ArrayList<String>();
                            final ArrayList<String> spinnerlist2 = new ArrayList<String>();

                            for(int i = 0;i<mArrayList.size();i++){
                                spinnerlist1.add(mArrayList.get(i).getLocation());
                            }
                            for(int i = 0;i<mArrayList.size();i++){
                                spinnerlist2.add(mArrayList.get(i).getLocation());
                            }
                            final Button btnYES2 = (Button) linear2.findViewById(R.id.buttonYES2);
                            final Button btnNO2 = (Button) linear2.findViewById(R.id.buttonNO2);

                            ArrayAdapter<String> beginadapter = new ArrayAdapter<String>(MainMapActivity.this,R.layout.support_simple_spinner_dropdown_item,spinnerlist1);
                            ArrayAdapter<String> endadapter = new ArrayAdapter<String>(MainMapActivity.this,R.layout.support_simple_spinner_dropdown_item,spinnerlist2);

                            beginspinner.setAdapter(beginadapter);
                            endspinner.setAdapter(endadapter);

                            textbTime = (TextView) linear2.findViewById(R.id.textview3);
                            btnbTime=(Button) linear2.findViewById(R.id.button3);
                            textaTime = (TextView) linear2.findViewById(R.id.textview33);
                            btnaTime=(Button) linear2.findViewById(R.id.button4);

                            btnbTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog(TIME_DIALOG_ID_FROM);
                                }
                            });
                            btnaTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog(TIME_DIALOG_ID_TO);
                                }
                            });

                            final Calendar t = Calendar.getInstance();
                            mHour = t.get(Calendar.HOUR_OF_DAY);
                            mMinute = t.get(Calendar.MINUTE);
                            mHour1 = t.get(Calendar.HOUR_OF_DAY);
                            mMinute1 = t.get(Calendar.MINUTE);

                            updateTime();
                            txt1.setTypeface(typeFace);
                            txt2.setTypeface(typeFace);
                            txt3.setTypeface(typeFace);
                            txt4.setTypeface(typeFace);
                            txt5.setTypeface(typeFace);
                            txt6.setTypeface(typeFace);
                            txt7.setTypeface(typeFace);
                            btnYES2.setTypeface(typeFace);
                            btnNO2.setTypeface(typeFace);
                            textaTime.setTypeface(typeFace);
                            textbTime.setTypeface(typeFace);
                            editPrice.setTypeface(typeFace);
                            editNote.setTypeface(typeFace);

                            btnYES2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PolylineOptions polylineOptions = new PolylineOptions();
                                    polylineOptions.color(Color.RED);
                                    polylineOptions.width(5);
                                    String starts = beginspinner.getSelectedItem().toString();
                                    String ends = endspinner.getSelectedItem().toString();
                                    for(int i = 0;i<mArrayList.size();i++) {
                                        if (starts.equals(mArrayList.get(i).getLocation())){
                                            arrayPoints.add(new LatLng(mArrayList.get(i).getLat(),mArrayList.get(i).getLng()));
                                        }
                                        if(ends.equals(mArrayList.get(i).getLocation())){
                                            arrayPoints.add(new LatLng(mArrayList.get(i).getLat(),mArrayList.get(i).getLng()));
                                        }
                                    }

                                    plplan.setbP2P(starts);
                                    plplan.setaP2P(ends);
                                    plplan.setUserID(GlobalID.KakaoID);
                                    plplan.setDay(String.valueOf(daynumber));
                                    plplan.setVehicle(editVehicle.getSelectedItem().toString());
                                    plplan.setbTime(textbTime.getText().toString());
                                    plplan.setaTime(textaTime.getText().toString());
                                    plplan.setNote(editNote.getText().toString());
                                    plplan.setTitle(textTitle.getText().toString());
                                    plplan.setAdate(textCal1.getText().toString());
                                    plplan.setBdate(textCal2.getText().toString());

                                    if(!(editPrice.getText().toString().equals(""))){
                                        plplan.setPrice(Integer.parseInt(editPrice.getText().toString()));
                                        //mArrayList.get(number).setPrice(Integer.parseInt(editPrice.getText().toString()));
                                    }
                                    else{
                                        plplan.setPrice(0);
                                    }

                                    polylineOptions.addAll(arrayPoints);
                                    mGoogleMap.addPolyline(polylineOptions);
                                    arrayPoints.clear();
                                    Toast.makeText(MainMapActivity.this, "bP2P:"+plplan.getbP2P()+"aP2P:"+plplan.getaP2P()
                                            +"교통수단:"+plplan.getVehicle()+"b시간:"+plplan.getbTime()
                                            +"a시간"+plplan.getaTime()+"비용"+plplan.getPrice()
                                            +"메모"+plplan.getNote(),Toast.LENGTH_LONG).show();
                                    plArrayList.add(plplan);
                                    dialog.dismiss();
                                }
                            });

                            btnNO2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //취소 클릭시 입력해 놓은 정보 삭제 기능
                                    dialog.dismiss();
                                }
                            });
                            dialog = builder1.create();
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                            mGoogleMap.setOnMapClickListener(null);
                        }
                        else{
                            Toast.makeText(MainMapActivity.this, "마커정보를 입력해주세요.", Toast.LENGTH_LONG).show();
                        }

                    }
                });




        //save button
        findViewById(R.id.btn3).setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v) {

                        try {


                            clientservice = new ClientService();
                            clientservice.SendMarker(mArrayList);
                            clientservice.SendPolyline(plArrayList);
                            final AlertDialog.Builder builder4 = new AlertDialog.Builder(MainMapActivity.this);
                            Context mcontext = getApplicationContext();
                            LayoutInflater inflater = (LayoutInflater)mcontext.getSystemService(LAYOUT_INFLATER_SERVICE);
                            View layout4 = inflater.inflate(R.layout.travelnote_add_day,(ViewGroup)findViewById(R.id.linearlayout11));
                            LinearLayout linear4 = (LinearLayout) layout4;
                            Typeface typeFace = Typeface.createFromAsset(getAssets(), "BMJUA.ttf");

                            TextView mylist = (TextView) linear4.findViewById(R.id.titletext5);

                            mylist.setTypeface(typeFace);

                            CharSequence info[] = new CharSequence[(int) result];
                            for(int i =0;i<(int)result;i++) {
                                info[i]=i+1+"일차";
                            }

                            builder4.setItems(info, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int position) {
                                    daynumber=position+1;
                                    String dayStr = String.valueOf(daynumber);

                                        dialog.dismiss();

                                }

                            });
                            builder4.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MainMapActivity.this, "등록이 완료되었습니다.",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(
                                            getApplicationContext(), // 현재 화면의 제어권자
                                            MainActivity.class); // 다음 넘어갈 클래스 지정
                                    startActivity(intent); // 다음 화면으로 넘어간다
                                }
                            });

                            dialog_day=builder4.create();
                            dialog_day.setCanceledOnTouchOutside(false);
                            dialog_day.show();
                            mGoogleMap.clear();
                            mArrayList = new ArrayList<mPlan>();
                            arrayPoints = new ArrayList<LatLng>();
                            plArrayList = new ArrayList<plPlan>();
                            number = 0;

                            //if 다시 다이얼로그 띄우기
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        //마커 정보 수정 코드
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker clickmarker) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainMapActivity.this);
                Context mcontext = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater)mcontext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.travelnote_dialog_marker,(ViewGroup)findViewById(R.id.linearLayout2));
                final LinearLayout linear = (LinearLayout) layout;

                final EditText editLocation = (EditText)linear.findViewById(R.id.dialog_location);
                final EditText editPrice = (EditText)linear.findViewById(R.id.dialog_price);
                //final EditText editThema = (EditText)linear.findViewById(R.id.dialog_thema);
                final EditText editNote = (EditText)linear.findViewById(R.id.dialog_note);
                final Button btnYES = (Button)linear.findViewById(R.id.buttonYES);
                final Button btnNO = (Button)linear.findViewById(R.id.buttonNO);

                for(int i = 0 ; i< mArrayList.size();i++) {
                    if (clickmarker.getId().equals(mArrayList.get(i).getMarkerId())) {
                        builder.setTitle("Input Information!");
                        builder.setView(layout);
                        number = i;
                    }
                }
                //
                btnYES.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {

                    }

                });
                btnNO.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog = builder.create();
                dialog.show();

                return true;
            }
        });
        mGoogleMap.animateCamera(zoom);

        //폴리라인 정보 수정 코드
        mGoogleMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainMapActivity.this);
                Context mcontext = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater)mcontext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.travelnote_dialog_line,(ViewGroup)findViewById(R.id.linearLayout3));
                final LinearLayout linear = (LinearLayout) layout;

                builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}






