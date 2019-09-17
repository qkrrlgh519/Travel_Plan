package com.example.itchyfeet;

/**
 * Created by 김재웅 on 2017-05-19.
 */


    public class DayListViewItem {

        private String ddaynum;
        private String hotelmoney;
        private String tourismmoney;
        private String foodmoney;

    public String getHotelmoney() {
        return hotelmoney;
    }

    public void setHotelmoney(String hotelmoney) {
        this.hotelmoney = hotelmoney;
    }

    public String getTourismmoney() {
        return tourismmoney;
    }

    public void setTourismmoney(String tourismmoney) {
        this.tourismmoney = tourismmoney;
    }

    public String getFoodmoney() {
        return foodmoney;
    }

    public void setFoodmoney(String foodmoney) {
        this.foodmoney = foodmoney;
    }

    public void setDaynum(String daynum){ddaynum=daynum;}
        public String getDaynum(){
            return this.ddaynum;
        }

    }

