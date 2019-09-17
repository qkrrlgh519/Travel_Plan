package com.example.itchyfeet;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by giho on 2017-05-14.
 */

public class tPlan implements Serializable{
    private static final long serialVersionUID = -3703047716855451794L;
    String title;
    String bdate, adate;
    String day;
    String UserID;

    public void setUserID(String UserID){this.UserID=UserID;}
    public String getUserID(){return UserID;}

    public void setDay(String day) {this.day = day;}
    public String getDay(){return day;}

    public void setTitle(String title) {this.title = title;}
    public String getTitle() {return title;}

    public void setBdate(String bdate) {this.bdate = bdate;}
    public String getBdate() {return bdate;}

    public void setAdate(String adate) {this.adate = adate;}
    public String getAdate() {return adate;}
}