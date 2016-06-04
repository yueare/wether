package com.FJU.iWeather;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by FJU on 2015/5/25. 针对一个View 进行UI刷新
 */
public class ViewAdapter {

    //UI控件组
    private ImageView imageView[];
    private TextView textView [];
    private LinearLayout main;
    private Button refresh;
    private Context context;
    private RefreshableView refreshableView;
    //动画
    private RotateAnimation rotateAnimation = new RotateAnimation(0.0f,+720.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

    //天气数据
    private String city="嘉善";
    private BaiduWeather bd;
    List<perWeather> list;

    ViewAdapter(View view, Context context)
    {
        imageView = new ImageView[4];
        textView = new TextView[11];
        refresh=(Button)view.findViewById(R.id.button);
        main =(LinearLayout)view.findViewById(R.id.main);
        imageView[0] =(ImageView)view.findViewById(R.id.imageView);
        imageView[1] =(ImageView)view.findViewById(R.id.imageView2);
        imageView[2] =(ImageView)view.findViewById(R.id.imageView3);
        imageView[3] =(ImageView)view.findViewById(R.id.imageView4);
        textView[0] =(TextView)view.findViewById(R.id.textView);
        textView[1] =(TextView)view.findViewById(R.id.textView2);
        textView[2] =(TextView)view.findViewById(R.id.textView3);
        textView[3] =(TextView)view.findViewById(R.id.textView4);
        textView[4] =(TextView)view.findViewById(R.id.textView5);
        textView[5] =(TextView)view.findViewById(R.id.textView6);
        textView[6] =(TextView)view.findViewById(R.id.textView7);
        textView[7] =(TextView)view.findViewById(R.id.textView8);
        textView[8] =(TextView)view.findViewById(R.id.textView9);
        textView[9] =(TextView)view.findViewById(R.id.textView10);
        textView[10] =(TextView)view.findViewById(R.id.textView19);
        refreshableView = (RefreshableView)view.findViewById(R.id.refreshable_view);


        this.context=context;
        bd = new BaiduWeather(city,context);
//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateWeather();
//            }
//        });
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    updateWeather();
                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshableView.finishRefreshing();
            }
        }, 1);

    }

    public void setCity(String city)
    {
        this.city=city;
        bd.setCity(city);
    }
    public String getCity()
    {
        return city;
    }

    private void updateUI()
    {
        if(list.size()>0)
        {
            for(int i=0;i<=2;i++)
            {
                textView[i].setText(list.get(i+1).date);
            }
            for(int i=0;i<=2;i++)
            {
                textView[i+3].setText(list.get(i+1).tem.replace(" ~","° /").replace("℃","°"));
            }
            String tmpnow;
            String tmp ="今天 " + list.get(0).date.replace("实时","当前").replace("℃", "°").replace("(","").replace(")","");
            tmpnow = tmp.substring(tmp.indexOf("当前")+3,tmp.indexOf("°"));
            tmp = tmp.substring(0,tmp.indexOf("当前")-1);

            textView[6].setText(tmp);
            textView[7].setText(list.get(0).weather);
            textView[8].setText(list.get(0).wind);
            textView[9].setText(" "+bd.getCity() + "  " + list.get(0).tem.replace(" ~","° /").replace("℃","°"));
            textView[10].setText(tmpnow);
            //--------------------pic process

            for(int i=0;i<imageView.length;i++)
            {
                imageView[i].setImageResource(R.drawable.w100);
                imageView[i].setImageResource(getWeatherImg(list.get(i).weather));
            }

        }



    }
    class sMyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg){
            //TODOAuto-generatedmethodstub
            super.handleMessage(msg);
            if (msg.what==0x1)//刷新网络数据 更新界面
            {
//                rotateAnimation.setDuration(1200);
//                refresh.startAnimation(rotateAnimation);
                updateUI();
            }
            else if (msg.what==0x2)//提取本地数据 更新界面
            {

                updateUI();
            }
        }
    }
    private sMyHandler mHandler = new sMyHandler();
    public void updateWeather()
    {
        new Thread()
        {
            public void run()
            {
                Message msg;
                //预先读取本地数据



                //读取网络数据
                list = bd.getWeather();//获取天气数据
                if(list.size()==0)//如果网络数据加载失败
                {
                    //bd.readXMLfromLocal();
                    //list = bd.getWeather();//利用本地数据再次提取信息
                    return;
                }
                else
                {
                    //bd.saveXMLtoLocal();//刷新本地数据为最新网络数据
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //UPDATE是一个自己定义的整数，代表了消息ID
                msg=mHandler.obtainMessage(0x1);
                mHandler.sendMessage(msg);
            }
        }.start();
    }


    private int getWeatherImg(String weather) {
        int img = 0;
        if (weather.contains("转")) {
            weather = weather.substring(0, weather.indexOf("转"));
        }
        if (weather.contains("晴")) {
            img = R.drawable.weather5;
        } else if (weather.contains("多云")) {
            img = R.drawable.weather3;
        }
        else if (weather.contains("阴")) {
            img = R.drawable.weather2;
        } else if (weather.contains("雷")) {
            img = R.drawable.weather21;
        }
        else if(weather.contains("阵雨")) {
            img = R.drawable.weather0;
        }else if (weather.contains("小雨")) {
            img = R.drawable.weather12;
        } else if (weather.contains("中雨")) {
            img = R.drawable.weather12;
        } else if (weather.contains("大雨")) {
            img = R.drawable.weather23;
        } else if (weather.contains("暴雨")) {
            img = R.drawable.weather23;
        } else if (weather.contains("雨夹雪")) {
            img = R.drawable.weather18;
        } else if (weather.contains("冻雨")) {
            img = R.drawable.weather18;
        } else if (weather.contains("小雪")) {
            img = R.drawable.weather19;
        } else if (weather.contains("中雪")) {
            img = R.drawable.weather19;
        } else if (weather.contains("大雪")) {
            img = R.drawable.weather33;
        } else if (weather.contains("暴雪")) {
            img = R.drawable.weather33;
        } else if (weather.contains("冰雹")) {
            img = R.drawable.weather33;
        } else if (weather.contains("雾") || weather.contains("霾")) {
            img = R.drawable.weather11;
        } else if (weather.contains("沙尘暴") || weather.contains("浮尘")
                || weather.contains("扬沙")) {
            img = R.drawable.weather28;
        } else {
            img = R.drawable.weather5;
        }
        return img;
    }

}
