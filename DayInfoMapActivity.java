package com.example.itchyfeet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.io.IOException;
import java.util.ArrayList;

public class DayInfoMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    String UserID;
    ClientService clientservice;
    String command = "vPlan";
    String day, title;
    public static Client client;
    GlobalApplication GlobalID;

    private ArrayList<LatLng> arrayPoints;
    LatLng receiveLatLng;
    AlertDialog dialog, dialog2, dialog_day;
    TextView textLocation, textTheme, textbTime, textaTime, textPrice, textNote;
    TextView textaP2P, textbP2P, textVehicle, textbTimeLine, textaTimeLine, textPriceLine, textNoteLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travelnote_travel_day_info_map);

        Intent intent = getIntent();
        day = intent.getExtras().getString("day");
        title = intent.getExtras().getString("title");
        UserID = GlobalID.KakaoID;
        command = command + UserID + "@" + title + "#" + day + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        mGoogleMap = map;
        arrayPoints = new ArrayList<LatLng>();

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        //tPlan receivetPlan;
        final ArrayList<mPlan> receivemPlan;
        final ArrayList<plPlan> receiveplPlan;

        MarkerOptions markerOptions = new MarkerOptions();
        PolylineOptions polylineOptions = new PolylineOptions();

        try {
            clientservice = new ClientService();
            clientservice.SendViewCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

        receivemPlan = client.receivemarrayList;
        receiveplPlan = client.receiveparrayList;

        if(!receivemPlan.get(0).equals(null)){
            LatLng START = new LatLng(receivemPlan.get(0).getLat(),receivemPlan.get(0).getLng());
            map.moveCamera(CameraUpdateFactory.newLatLng(START));
            map.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
        else{
            LatLng START = new LatLng(37.56, 126.97);
            map.moveCamera(CameraUpdateFactory.newLatLng(START));
            map.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

        //서버로부터 받아온 값으로 marker 찍기
        for(int index = 0; index < receivemPlan.size(); index++){
            receiveLatLng = new LatLng(receivemPlan.get(index).getLat(),receivemPlan.get(index).getLng());
            markerOptions.position(receiveLatLng);
            mGoogleMap.addMarker(markerOptions);
        }

        //서버로부터 받아온 값으로 polyline 찍기
        for(int index=0; index < receiveplPlan.size(); index++){
            for(int index2 = 0; index2 < receivemPlan.size(); index2++){
                if(receivemPlan.get(index2).getLocation().equals(receiveplPlan.get(index).getbP2P())){
                    arrayPoints.add(new LatLng(receivemPlan.get(index2).getLat(),receivemPlan.get(index2).getLng()));
                }
                if(receivemPlan.get(index2).getLocation().equals(receiveplPlan.get(index).getaP2P())){
                    arrayPoints.add(new LatLng(receivemPlan.get(index2).getLat(),receivemPlan.get(index2).getLng()));
                }
                polylineOptions.color(Color.RED);
                polylineOptions.width(5);
                polylineOptions.addAll(arrayPoints);
                polylineOptions.clickable(true);
                mGoogleMap.addPolyline(polylineOptions);
                arrayPoints.clear();
            }
        }

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker clickmarker) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DayInfoMapActivity.this);
                Context mcontext = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater)mcontext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.travelnote_dialog_marker_view,(ViewGroup)findViewById(R.id.linearLayoutView1));
                LinearLayout linear = (LinearLayout) layout;
                Typeface typeFace = Typeface.createFromAsset(getAssets(), "BMJUA.ttf");
                textLocation = (TextView) linear.findViewById(R.id.textlocation);
                textTheme = (TextView) linear.findViewById(R.id.texttheme);
                textbTime = (TextView) linear.findViewById(R.id.textbtime);
                textaTime = (TextView) linear.findViewById(R.id.textatime);
                textPrice = (TextView) linear.findViewById(R.id.textprice);
                textNote = (TextView) linear.findViewById(R.id.textnote);

                TextView txt1 =  (TextView)linear.findViewById(R.id.txt1);
                TextView txt2 = (TextView)linear.findViewById(R.id.txt2);
                TextView txt3 = (TextView)linear.findViewById(R.id.txt3);
                TextView txt4 = (TextView)linear.findViewById(R.id.txt4);
                TextView txt5 = (TextView)linear.findViewById(R.id.txt5);
                TextView txt6 = (TextView)linear.findViewById(R.id.titletext8);
                txt1.setTypeface(typeFace);
                txt2.setTypeface(typeFace);
                txt3.setTypeface(typeFace);
                txt4.setTypeface(typeFace);
                txt5.setTypeface(typeFace);
                txt6.setTypeface(typeFace);
                textLocation.setTypeface(typeFace);
                textTheme.setTypeface(typeFace);
                textaTime.setTypeface(typeFace);
                textbTime.setTypeface(typeFace);
                textPrice.setTypeface(typeFace);
                textNote.setTypeface(typeFace);
                final Button btnYES = (Button)linear.findViewById(R.id.btnYES);
                final Button btnNO = (Button)linear.findViewById(R.id.btnNO);
                btnYES.setTypeface(typeFace);
                btnNO.setTypeface(typeFace);
                for(int index = 0; index < receivemPlan.size(); index++) {
                    LatLng receivemarkerLatLng = new LatLng(receivemPlan.get(index).getLat(), receivemPlan.get(index).getLng());
                    if(receivemarkerLatLng.equals(clickmarker.getPosition())) {
                        mPlan mplan = receivemPlan.get(index);


                        textLocation.setText(mplan.getLocation());
                        textTheme.setText(mplan.getTheme());
                        textbTime.setText(mplan.getbTime());
                        textaTime.setText(mplan.getaTime());
                        textPrice.setText(String.valueOf(mplan.getPrice()));
                        textNote.setText(mplan.getNote());
                    }
                }

                btnYES.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {
                        dialog.dismiss();
                    }

                });
                btnNO.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                builder.setView(layout);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(DayInfoMapActivity.this);
                Context mcontext = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater)mcontext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.travelnote_dialog_line_view,(ViewGroup)findViewById(R.id.linearLayoutView2));
                LinearLayout linear = (LinearLayout) layout;
                Typeface typeFace = Typeface.createFromAsset(getAssets(), "BMJUA.ttf");
                textbP2P = (TextView) linear.findViewById(R.id.textbp2p);
                textaP2P = (TextView) linear.findViewById(R.id.textap2p);
                textVehicle = (TextView) linear.findViewById(R.id.textvehicle);
                textbTimeLine = (TextView) linear.findViewById(R.id.textbtime);
                textaTimeLine = (TextView) linear.findViewById(R.id.textatime);
                textPriceLine = (TextView) linear.findViewById(R.id.textprice);
                textNoteLine = (TextView) linear.findViewById(R.id.textnote);
                final Button btnYES = (Button)linear.findViewById(R.id.btnYES);
                final Button btnNO = (Button)linear.findViewById(R.id.btnNO);
                btnYES.setTypeface(typeFace);
                btnNO.setTypeface(typeFace);
                TextView txt1 =  (TextView)linear.findViewById(R.id.txt1);
                TextView txt2 = (TextView)linear.findViewById(R.id.txt2);
                TextView txt3 = (TextView)linear.findViewById(R.id.txt3);
                TextView txt4 = (TextView)linear.findViewById(R.id.txt4);
                TextView txt5 = (TextView)linear.findViewById(R.id.txt5);
                TextView txt6 = (TextView)linear.findViewById(R.id.titletext9);
                textbP2P.setTypeface(typeFace);
                textaP2P.setTypeface(typeFace);
                textVehicle.setTypeface(typeFace);
                textbTimeLine.setTypeface(typeFace);
                textaTimeLine.setTypeface(typeFace);
                textPriceLine.setTypeface(typeFace);
                textNoteLine.setTypeface(typeFace);

                txt1.setTypeface(typeFace);
                txt2.setTypeface(typeFace);
                txt3.setTypeface(typeFace);
                txt4.setTypeface(typeFace);
                txt5.setTypeface(typeFace);
                txt6.setTypeface(typeFace);


                LatLng latlngbP2P = polyline.getPoints().get(0);
                LatLng latlngaP2P = polyline.getPoints().get(1);
                String strbP2P = null;
                String straP2P = null;

                for(int index = 0; index < receivemPlan.size(); index++){
                    LatLng receivemarkerLatLng = new LatLng(receivemPlan.get(index).getLat(), receivemPlan.get(index).getLng());
                    if(latlngbP2P.equals(receivemarkerLatLng)){
                        strbP2P = receivemPlan.get(index).getLocation();
                    }
                    if(latlngaP2P.equals(receivemarkerLatLng)){
                        straP2P = receivemPlan.get(index).getLocation();
                    }
                }



                for(int index = 0; index < receiveplPlan.size(); index++){
                    if(receiveplPlan.get(index).getbP2P().equals(strbP2P) && receiveplPlan.get(index).getaP2P().equals(straP2P)){
                        plPlan plplan = receiveplPlan.get(index);
                        textbP2P.setText(plplan.getbP2P());
                        textaP2P.setText(plplan.getaP2P());
                        textVehicle.setText(plplan.getVehicle());
                        textbTimeLine.setText(plplan.getbTime());
                        textaTimeLine.setText(plplan.getaTime());
                        textPriceLine.setText(String.valueOf(plplan.getPrice()));
                        textNoteLine.setText(plplan.getNote());

                    }
                }

                btnYES.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }

                });
                btnNO.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });

                builder.setView(layout);

                dialog2 = builder.create();
                dialog2.show();

            }
        });
    }
}
