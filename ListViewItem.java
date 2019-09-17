package com.example.itchyfeet;

/**
 * Created by user on 2017-04-24.
 */

public class ListViewItem {
    private String listnumStr; //
    private String titleStr;
    private String fromdateStr;
    private String todateStr;

    public void setListnum(String listnum){
        listnumStr=listnum;
    }
    public void setTitle(String title){
        titleStr=title;
    }
    public void setFromdate(String fromdate){
        fromdateStr=fromdate;
    }
    public void setTodate(String todate){
        todateStr=todate;
    }

    public String getListnum(){
        return this.listnumStr;
    }
    public String getTitle(){
        return this.titleStr;
    }

    public String getFromdate(){
        return this.fromdateStr;
    }
    public String getTodate(){
        return this.todateStr;
    }


}