package com.example.itchyfeet;

/**
 * Created by giho on 2017-05-21.
 */


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DayListAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<DayListViewItem> daylistViewItemList = new ArrayList<DayListViewItem>() ;

    // ListViewAdapter의 생성자
    public DayListAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return daylistViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        final Typeface typeFace = Typeface.createFromAsset(context.getAssets(),"BMJUA.ttf");
        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.travelnote_travel_daylistview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득, title, fromdate, todate, personnum, cost
        TextView daynumTextView = (TextView) convertView.findViewById(R.id.daynum);
        TextView hotelTextView = (TextView) convertView.findViewById(R.id.hotelTextView);
        TextView foodTextView =(TextView) convertView.findViewById(R.id.foodTextView);
        TextView sightTextView = (TextView)convertView.findViewById(R.id.sightTextView);
        daynumTextView.setTypeface(typeFace);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        DayListViewItem daylistViewItem = daylistViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        daynumTextView.setText(daylistViewItem.getDaynum());
        hotelTextView.setText(daylistViewItem.getHotelmoney());
        foodTextView.setText(daylistViewItem.getFoodmoney());
        sightTextView.setText(daylistViewItem.getTourismmoney());
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return daylistViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String daynum,String hMoney,String tMoney,String fMoney) {
        DayListViewItem item = new DayListViewItem();
        item.setDaynum(daynum);
        item.setHotelmoney(hMoney);
        item.setTourismmoney(tMoney);
        item.setFoodmoney(fMoney);
        daylistViewItemList.add(item);
    }
}

