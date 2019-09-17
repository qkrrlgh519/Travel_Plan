package com.example.itchyfeet;

import java.io.Serializable;

/**
 * Created by giho on 2017-04-19.
 */

public class plPlan implements Serializable {
    private static final long serialVersionUID = 2287322447272450807L;
    String bP2P, aP2P;
    String vehicle;
    String btime, atime;
    int price;
    String note;
    String day;
    String title;
    String UserID;
    String bdate, adate;

    public void setBdate(String bdate) {this.bdate = bdate;}
    public String getBdate() {return bdate;}

    public void setAdate(String adate) {this.adate = adate;}
    public String getAdate() {return adate;}


    public void setUserID(String UserID){this.UserID=UserID;}
    public String getUserID(){return UserID;}


    public void setTitle(String title) {this.title = title;}
    public String getTitle() {return title;}

    public void setDay(String day) {this.day = day;}
    public String getDay(){return day;}

    public void setbP2P(String bP2P) {this.bP2P = bP2P;}
    public String getbP2P() {return bP2P;}

    public void setaP2P(String aP2P) {this.aP2P = aP2P;}
    public String getaP2P() {return aP2P;}

    public void setVehicle(String vehicle) {this.vehicle = vehicle;}
    public String getVehicle() {return vehicle;}

    public void setbTime(String btime) {this.btime = btime;}
    public String getbTime() {return btime;}

    public void setaTime(String atime) {this.atime = atime;}
    public String getaTime() {return atime;}

    public void setPrice(int price) {this.price = price;}
    public int getPrice() {return price;}

    public void setNote(String note) {this.note = note;}
    public String getNote() {return note;}
}