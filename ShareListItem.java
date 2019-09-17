package com.example.itchyfeet;

import android.graphics.drawable.Drawable;

/**
 * Created by giho on 2017-05-24.
 */

public class ShareListItem {
    private Drawable iconDrawable ;
    private String contentStr ;
    private String timeStr ;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setContent(String content) {
        contentStr = content ;
    }
    public void setTime(String time) {
        timeStr = time ;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getContent() {
        return this.contentStr ;
    }
    public String getTime() {
        return this.timeStr ;
    }
}
