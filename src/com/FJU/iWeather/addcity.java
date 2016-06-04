package com.FJU.iWeather;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

import android.widget.AdapterView.OnItemClickListener;
import com.FJU.iWeather.SlideCutListView.RemoveDirection;
import com.FJU.iWeather.SlideCutListView.RemoveListener;



/**
 * Created by FJU on 2015/5/27.
 */



public class addcity extends Activity implements RemoveListener{

    private Button sch,back,about;
    private EditText edt;
    private TextView tv ;
    private SlideCutListView slideCutListView ;
    private ArrayAdapter<String> adapter;
    private List<String> dataSourceList = new ArrayList<String>();
    public static String TAG = "LocTestDemo";

    private BroadcastReceiver broadcastReceiver;
    public static String LOCATION_BCR = "location_bcr";

    private Button locBtn;
    UIupdater data =null;// UI更新器 的本地数据更新方法

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.addcity);

        sch =(Button)findViewById(R.id.button2);
        edt =(EditText)findViewById(R.id.editText);
        locBtn = (Button) findViewById(R.id.button3);
        tv =(TextView)findViewById(R.id.textView12);
        about = (Button)findViewById(R.id.buttonabout);
        back =(Button)findViewById(R.id.buttonback);

        init();


    }
    private void init() {
        initialize();
        initializeListeners();
        slideCutListView = (SlideCutListView) findViewById(R.id.slideCutListView);
        slideCutListView.setRemoveListener(this);

        data = new UIupdater(this);
        dataSourceList = data.getcityList();

        adapter = new ArrayAdapter<String>(this, R.layout.item, R.id.list_item, dataSourceList);
        slideCutListView.setAdapter(adapter);

        slideCutListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(addcity.this, dataSourceList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void removeItem(RemoveDirection direction, int position) {
        adapter.remove(adapter.getItem(position));
            data.savecityList(dataSourceList);
//        switch (direction) {
//            case RIGHT:
//                //Toast.makeText(this, "向右删除  "+ position, Toast.LENGTH_SHORT).show();
//                break;
//            case LEFT:
//                //Toast.makeText(this, "向左删除  "+ position, Toast.LENGTH_SHORT).show();
//                break;
//            default:
//                break;
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
        {
            Intent intent = new Intent(addcity.this, MyActivity.class);
            startActivity(intent);
            finish();
        }


        return super.onKeyDown(keyCode, event);
    }

    class sMyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg){
            //TODOAuto-generatedmethodstub
            super.handleMessage(msg);
            if (msg.what==0x6)
            {

                tv.setText("未找到 "+edt.getText());
                edt.setText("");

            }
            else if(msg.what==0x7)
            {
                dataSourceList.add(edt.getText().toString());
                slideCutListView.setAdapter(adapter);
                data.savecityList(dataSourceList);
                tv.setText("添加城市 "+edt.getText()+" 成功");
                edt.setText("");
            }

        }
    }
    private sMyHandler mHandler = new sMyHandler();
    public void haveCity()
    {
        BaiduWeather bd = new BaiduWeather(edt.getText().toString(),addcity.this);

        new Thread()
        {
            public void run()
            {
                Message msg;
                //读取网络数据
                List<perWeather> list = bd.getWeather();//获取天气数据
                if(list.size()==0)//如果网络数据加载失败
                {
                    msg=mHandler.obtainMessage(0x6);
                }
                else
                {
                    msg=mHandler.obtainMessage(0x7);
                }

                //UPDATE是一个自己定义的整数，代表了消息ID

                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private void initialize()//注册广播
    {
        registerBroadCastReceiver();
    }


    private void initializeListeners()//初始化监听
    {
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addcity.this, about.class);
                startActivity(intent);

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addcity.this, MyActivity.class);
                startActivity(intent);
                finish();

            }
        });

        sch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt.getText().toString().equals(""))
                    return;
                haveCity();//城市有数据 就加入， 否则反馈
            }
        });
        locBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tv.setText("正在定位中...");


                myBDlocation m = new myBDlocation(addcity.this);
                m.requestLocationInfo();
            }
        });
    }

    /**
     * 注册一个广播，监听定位结果
     */
    private void registerBroadCastReceiver()
    {
        broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String address = intent.getStringExtra("address");
                if(address!=null)
                {
                    tv.setText("当前定位城市:" + address);
                    edt.setText(address);
                }
                else
                    tv.setText("定位失败,检查网络是否开启");
            }
        };
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction(LOCATION_BCR);
        registerReceiver(broadcastReceiver, intentToReceiveFilter);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}