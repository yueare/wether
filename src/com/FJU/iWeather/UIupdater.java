package com.FJU.iWeather;

import android.app.Activity;
import android.content.*;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FJU on 2015/5/28.
 */
public class UIupdater {

    private ArrayList<View> viewContainter = new ArrayList<View>();
    private ArrayList<String> titleContainer = new ArrayList<String>();
    private ArrayList<ViewAdapter> uiAdapterContainer = new ArrayList<ViewAdapter>();
    private Context context;
    private ViewPager pager = null;
    private PagerTabStrip tabStrip = null;
    private View mview=null;
    private Button button =null;
    UIupdater(Context context)
    {
        this.context=context;
        //获取布局
        LayoutInflater flater = LayoutInflater.from(context);
        mview = flater.inflate(R.layout.mview, null);

        pager = (ViewPager) mview.findViewById(R.id.viewpager);
        tabStrip = (PagerTabStrip) mview.findViewById(R.id.tabstrip);
        button=(Button)mview.findViewById(R.id.buttonSet);

        //取消tab下面的长横线
        tabStrip.setDrawFullUnderline(false);
        //设置tab的背景色
        //tabStrip.setBackgroundColor(Color.argb(255, 111, 40, 98));
        tabStrip.setBackgroundColor(Color.argb(255, 26, 209, 255));
        //设置当前tab页签的下划线颜色
        tabStrip.setTabIndicatorColor(Color.argb(255, 26, 209, 255));
        tabStrip.setTextSpacing(80);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, addcity.class);
                context.startActivity(intent);
                ((MyActivity)context).finish();

            }
        });

    }



    public View update()
    {

        getCityList();
        for(int i=0;i<titleContainer.size();i++)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.otherweatherback, null);
            ViewAdapter u = new ViewAdapter(view,context);
            viewContainter.add(view);
            uiAdapterContainer.add(u);
            uiAdapterContainer.get(i).setCity(titleContainer.get(i));
            uiAdapterContainer.get(i).updateWeather();
        }
        if(titleContainer.size()==0)
        {
            tabStrip.setBackgroundColor(Color.argb(255, 40, 46, 58));
            //设置当前tab页签的下划线颜色
            tabStrip.setTabIndicatorColor(Color.argb(255, 40, 46, 58));

            titleContainer.add("");
            View view = LayoutInflater.from(context).inflate(R.layout.welcome, null);
            viewContainter.add(view);

        }

        updatepage();
        return mview;
    }

    private void updatepage()
    {
        pager.setAdapter(new PagerAdapter() {

            //viewpager中的组件数量
            @Override
            public int getCount() {
                return viewContainter.size();
            }

            //滑动切换的时候销毁当前的组件
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                ((ViewPager) container).removeView(viewContainter.get(position));
            }

            //每次滑动的时候生成的组件
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(viewContainter.get(position));
                return viewContainter.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titleContainer.get(position);
            }
        });
    }
    private  int getCityList()
    {
        titleContainer.clear();
        SharedPreferences share = context.getSharedPreferences("citylist", Activity.MODE_PRIVATE);
        int size =share.getInt("size", 0);
        for(int i=0;i<size;i++)
        {
            titleContainer.add(share.getString("city" + i, "无"));
        }

        return titleContainer.size();
    }

    public ArrayList<String> getcityList()
    {
        getCityList();
        return titleContainer;
    }
    private void saveCityList()
    {

        SharedPreferences share = context.getSharedPreferences("citylist", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = share.edit();
        edit.putInt("size",titleContainer.size());
        for(int i=0;i<titleContainer.size();i++)
        {
            edit.putString("city" + i, titleContainer.get(i));
        }
        edit.commit();
    }
    public void savecityList(List<String> list)
    {

        SharedPreferences share = context.getSharedPreferences("citylist", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = share.edit();
        edit.putInt("size", list.size());
        for(int i=0;i<list.size();i++)
        {
            edit.putString("city" + i, list.get(i));
        }
        edit.commit();
    }

    public void addCity(String city)
    {
        int index=titleContainer.size();
        titleContainer.add(city);
        View view = LayoutInflater.from(context).inflate(R.layout.otherweatherback, null);
        ViewAdapter u = new ViewAdapter(view,context);
        viewContainter.add(view);
        uiAdapterContainer.add(u);
        uiAdapterContainer.get(index).setCity(titleContainer.get(index));
        uiAdapterContainer.get(index).updateWeather();
        updatepage();
    }





}
