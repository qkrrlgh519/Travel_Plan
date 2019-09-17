package com.example.itchyfeet;

/**
 * Created by giho on 2017-05-16.
 */


import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.itchyfeet.Client.clientModel;
import static com.example.itchyfeet.Client.tplan;


public class ClientService {
    String host = "192.168.43.184";
    int port = 9999;
    CompareThread cthread;
    LoginThread lthread;
    TitleThread tthread;
    MarkerThread sthread;
    PolylineThread pthread;
    UserPlanThread upthread;
    UserPlanNumberThread upnthread;
    ViewCommandThread vcthread;
    ViewShareThread vsthread;
    ShareThread shthread;
    ShareOkThread shothread;

    public static Client client;

    private static ClientService newInstance;

    public static ClientService getInstance() {
        if (newInstance == null) {
            newInstance = new ClientService();
        }
        return newInstance;
    }

    public void CompareName(String kakaoID) throws IOException {
        String kaid = kakaoID;
        if (client == null) {
            try {
                client = new Client(host, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cthread = new CompareThread(kaid);
        cthread.start();
        try {
            cthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private class CompareThread extends Thread {
        private String kakaoID;

        public CompareThread(String kakaoID) {
            this.kakaoID = kakaoID;
        }

        public void run() {
            try {
                if (client == null) {
                    client = new Client(host, port);
                }
                client.sendToServer("@Compare@" + kakaoID + "@333333333333333333333333333");
            } catch (Exception e) {

            }

        }
    }

    //로그인 Kakao id랑 NickName
    public void LoginConnect(String KakaoID, String NickName) throws IOException {
        String kakaoID = KakaoID;
        String nickname = NickName;
        if (client == null) {
            try {
                client = new Client(host, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        lthread = new LoginThread(kakaoID, nickname);
        lthread.start();

        try {
            lthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class LoginThread extends Thread {
        private String kaid;
        private String nick;

        public LoginThread(String kid, String nickid) {
            this.kaid = kid;
            this.nick = nickid;
        }

        public void run() {
            try {
                if (client == null) {
                    client = new Client(host, port);
                }
                client.sendToServer("@Login" + "@" + kaid + "@" + nick + "@3333333333333333333333333333");
            } catch (Exception e) {

            }
        }
    }

    public ArrayList<tPlan> SendTitle(ArrayList<tPlan> inputarrayList) throws IOException {
        ArrayList<tPlan> arrayList;
        arrayList = inputarrayList;
        if (client == null) {
            try {
                client = new Client(host, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tthread = new TitleThread((Object) arrayList);
        tthread.start();

        try {
            tthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1500);
            return arrayList;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


    private class TitleThread extends Thread {
        private Object tplanlist;

        public TitleThread(Object o) {
            this.tplanlist = o;
        }

        public void run() {
            try {
                if (client == null) {
                    client = new Client(host, port);
                }
                client.sendToServer(tplanlist);
                //client.quit();
            } catch (Exception e) {

            }
        }
    }

    public ArrayList<mPlan> SendMarker(ArrayList<mPlan> inputarrayList) throws IOException {
        ArrayList<mPlan> arrayList;
        arrayList = inputarrayList;
        if (client == null) {
            try {
                client = new Client(host, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sthread = new MarkerThread((Object) arrayList);
        sthread.start();

        try {
            sthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1500);
            arrayList = client.marrayList;
            return arrayList;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


    private class MarkerThread extends Thread {
        private Object markerlist;

        public MarkerThread(Object o) {
            this.markerlist = o;
        }

        public void run() {
            try {
                if (client == null) {
                    client = new Client(host, port);
                }
                client.sendToServer(markerlist);
                //client.quit();
            } catch (Exception e) {

            }
        }
    }

    public ArrayList<plPlan> SendPolyline(ArrayList<plPlan> inputarrayList) throws IOException {
        ArrayList<plPlan> arrayList;
        arrayList = inputarrayList;
        if (client == null) {
            try {
                client = new Client(host, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pthread = new PolylineThread((Object) arrayList);
        pthread.start();

        try {
            pthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1500);
            arrayList = client.parrayList;
            return arrayList;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


    private class PolylineThread extends Thread {
        private Object polylinelist;

        public PolylineThread(Object o) {
            this.polylinelist = o;
        }

        public void run() {
            try {
                if (client == null) {
                    client = new Client(host, port);
                }
                client.sendToServer(polylinelist);
            } catch (Exception e) {

            }
        }
    }

    private class ViewShareThread extends Thread {
        private String kaid;

        public ViewShareThread(String kaid) {
            this.kaid = kaid;
        }

        public void run() {
            try {
                if (client == null) {
                    client = new Client(host, port);
                }
                client.sendToServer("@ShareView" + "@" + kaid + "@333333333333333333333333333333333");
            } catch (Exception e) {

            }
        }
    }

    public void ViewShare(String inputid) throws IOException {
        String ViewShareID = inputid;
        if (client == null) {
            try {
                client = new Client(host, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        vsthread = new ViewShareThread(ViewShareID);
        vsthread.start();

        try {
            vsthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    //사용자의 계획 보기 - 여행 계획 다 가져오기
    public String ViewUserPlan(String inputViewUserPlanCommand) throws IOException {
        String ViewUserPlancommand = inputViewUserPlanCommand;
        if (client == null) {
            try {
                client = new Client(host,port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        upthread = new UserPlanThread((Object)ViewUserPlancommand);
        upthread.start();

        try {
            upthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1500);
            ViewUserPlancommand = client.viewuserplancommand;
            return ViewUserPlancommand;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


    private class UserPlanThread extends Thread{
        private Object viewuserplancommand;
        public UserPlanThread(Object o){
            this.viewuserplancommand = o;
        }
        public void run(){
            try {
                if (client == null) {
                    client = new Client(host, port);
                }
                client.sendToServer(viewuserplancommand);
            } catch (Exception e) {

            }
        }
    }
    public String SendViewCommand(String inputCommand) throws IOException {
        String command = inputCommand;
        if (client == null) {
            try {
                client = new Client(host,port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        vcthread = new ViewCommandThread((Object)command);
        vcthread.start();

        try {
            vcthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1500);
            command = client.command;
            return command;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String ViewUserPlanNumber(String inputViewUserPlanNumberCommand) throws IOException {
        String ViewUserPlanNumbercommand = inputViewUserPlanNumberCommand;
        if (client == null) {
            try {
                client = new Client(host,port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        upnthread = new UserPlanNumberThread((Object)ViewUserPlanNumbercommand);
        upnthread.start();

        try {
            upnthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1500);
            ViewUserPlanNumbercommand = client.viewuserplannumbercommand;
            return ViewUserPlanNumbercommand;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


    private class UserPlanNumberThread extends Thread{
        private Object viewuserplannumbercommand;
        public UserPlanNumberThread(Object o){
            this.viewuserplannumbercommand = o;
        }
        public void run(){
            try {
                if (client == null) {
                    client = new Client(host, port);
                }
                client.sendToServer(viewuserplannumbercommand);
            } catch (Exception e) {

            }
        }
    }


    private class ViewCommandThread extends Thread{
        private Object command;
        public ViewCommandThread(Object o){
            this.command = o;
        }
        public void run(){
            try {
                if (client == null) {
                    client = new Client(host, port);
                }
                client.sendToServer(command);
            } catch (Exception e) {

            }
        }
    }

    //사용자의 계획 보기 - 여행 계획 다 가져오기
    public String SharePlan(String inputShareCommand) throws IOException {
        String ShareCommand = inputShareCommand;
        if (client == null) {
            try {
                client = new Client(host, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        shthread = new ShareThread((Object) ShareCommand);
        shthread.start();

        try {
            shthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1500);
            return ShareCommand;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


    private class ShareThread extends Thread {
        private Object sharecommand;

        public ShareThread(Object o) {
            this.sharecommand = o;
        }

        public void run() {
            try {
                if (client == null) {
                    client = new Client(host, port);
                }
                client.sendToServer(sharecommand);
            } catch (Exception e) {

            }
        }
    }


    public String ShareOkPlan(String inputShareOkCommand) throws IOException {
        String ShareOkCommand = inputShareOkCommand;
        if (client == null) {
            try {
                client = new Client(host, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        shothread = new ShareOkThread((Object) ShareOkCommand);
        shothread.start();

        try {
            shothread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1500);
            return ShareOkCommand;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


    private class ShareOkThread extends Thread {
        private Object shareokcommand;

        public ShareOkThread(Object o) {
            this.shareokcommand = o;
        }

        public void run() {
            try {
                if (client == null) {
                    client = new Client(host, port);
                }
                client.sendToServer(shareokcommand);
            } catch (Exception e) {

            }
        }
    }
}


