package com.example.itchyfeet;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.sql.Time;

/**
 * Created by giho on 2017-04-19.
 */

public class mPlan implements Serializable {

    private static final long serialVersionUID = 7240642008944068767L;
    String markerId;
    Double lat, lng;
    String location;
    String theme;
    String btime, atime;
    String day;
    int price;
    String note;
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

    public void setMarkerId(String markerId) {this.markerId = markerId;}
    public String getMarkerId() {return markerId;}

    public void setDay(String day) {this.day = day;}
    public String getDay(){return day;}

    public void setLat(Double lat) {this.lat = lat;}
    public Double getLat() {return lat;}

    public void setLng(Double lng) {this.lng = lng;}
    public Double getLng() {return lng;}

    public void setLocation(String location) {this.location = location;}
    public String getLocation() {return location;}

    public void setTheme(String theme) {this.theme = theme;}
    public String getTheme() {return theme;}

    public void setbTime(String btime) {this.btime = btime;}
    public String getbTime() {return btime;}

    public void setaTime(String atime) {this.atime = atime;}
    public String getaTime() {return atime;}

    public void setPrice(int price) {this.price = price;}
    public int getPrice() {return price;}

    public void setNote(String note) {this.note = note;}
    public String getNote() {return note;}
}