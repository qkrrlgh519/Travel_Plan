package com.example.itchyfeet;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;


/**
 * Created by 김재웅 on 2017-05-16.
 */

public class Client extends AbstractClient{
    public static ArrayList<tPlan> receivetarrayList;
    public static ArrayList<mPlan> receivemarrayList;
    public static ArrayList<plPlan> receiveparrayList;
    public static String viewuserplancommand;
    public static String viewuserplannumbercommand;
    public static ClientModel clientModel; //클라 정보
    public static tPlan tplan;
    public static ArrayList<mPlan> marrayList;
    public static ArrayList<plPlan> parrayList;
    public static ArrayList<tPlan> tarrayList;
    public static String command;

    public static String checkId;
    GoogleMap mGoogleMap;

    private Context mContext = null;

    protected void connetionClosed(){
        Log.d("coonetionCLosed","연결종료");
    }
    protected void connectionException(Exception exception){
        Log.d("coonetionCLosed","연결종료");
    }
    protected void connectionEstablished(){
        Log.d("coonetion","연결완료");
    }
    public ClientModel getProductModel(){
        return clientModel;
    }
    public ArrayList<mPlan> getmPlan(){
        return marrayList;
    }
    public ArrayList<plPlan> getplPlan() { return parrayList; }
    public ArrayList<tPlan> gettPlan(){return tarrayList;}
    public String getCommand() {return command; }

    public Client(String host,int port) throws IOException{
        super(host,port);
        openConnection();
        Log.d("connection","연결OK");
    }
    @Override
    protected void handleMessageFromServer(Object msg) {
        if(msg.toString().substring(23, 28).equals("tPlan")){
            receivetarrayList = (ArrayList<tPlan>) msg;
        }

        if(msg.toString().substring(23, 28).equals("mPlan")){
            receivemarrayList = (ArrayList<mPlan>) msg;
        }

        else if(msg.toString().substring(23, 29).equals("plPlan")){
            receiveparrayList = (ArrayList<plPlan>) msg;
        }

    }


    public void handleMessageFromClientUI(String message) {
        try {
            sendToServer(message);
        } catch (IOException e) {
            System.out.println("서버에 메세지를 보낼 수 없습니다. 클라이언트를 종료합니다..");
            quit();
        }
    }
    public void accept(){
        try {
            BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
            String message;

            while (true) {
                message = fromConsole.readLine();
                handleMessageFromClientUI(message);
            }
        } catch (Exception ex) {
            System.out.println("Unexpected error while reading from console!");
        }
    }
    public void quit(){
        try{
            closeConnection();
        } catch (IOException e){}
        //System.exit(0);
    }
}