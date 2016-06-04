package com.FJU.iWeather;


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Handler;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.os.Message;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Created by FJU on 2015/5/25.
 */
public class BaiduWeather {

    private List <perWeather> wlist = new ArrayList<perWeather>();
    private  static String Apikey="596aa9d11c70cece5d62c1e3e3794674";
    private  String city;
    private  String xml;
    private  Context context;
    BaiduWeather(String city,Context context)
    {
        this.context=context;
        this.city=city;
    }

    public List<perWeather> getWeather() {
        String buffstr = null;
        try {
            xml = getXmlCode(city);  //设置输入城市
            if(xml.equals("无数据"))
            {
                readXMLfromLocal();
            }
            else
            {
                saveXMLtoLocal();
            }
            readStringXml(xml);//调用xml解析函数
            //return wlist;

        } catch (Exception e) {
            e.printStackTrace();
            return wlist;
        }
        return wlist;
    }

    private String getXmlCode(String city) {
        try {
            city=URLEncoder.encode(city, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String requestUrl = "http://api.map.baidu.com/telematics/v3/weather?location="+city+"&ak="+Apikey;
        String XMLcode =null;
        // 创建HttpGet对象
        HttpGet request = new HttpGet(requestUrl);
        // 创建HttpClient对象
        HttpClient client = new DefaultHttpClient();
        HttpResponse httpResponse = null;

        try {
            httpResponse = client.execute(request);
            //如果网页访问成功
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                XMLcode = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
            }
            else
            {
                XMLcode ="无数据";
            }

        } catch (IOException e) {
            e.printStackTrace();
            XMLcode ="无数据";

        }

        return XMLcode;
    }

    private boolean readStringXml(String xml) {

        Document doc = null;
        List listdate=null;  //用来存放日期
        List listweather=null;
        List listwind=null;
        List listtem=null;

        try {
            // 读取并解析XML文档
            //下面的是通过解析xml字符串的

            doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            Element rootElt = doc.getRootElement(); // 获取根节点
            Iterator iter = rootElt.elementIterator("results"); // 获取根节点下的子节点results
            String status=rootElt.elementText("status"); //获取状态，如果等于success,表示有数据
            if(!status.equals("success"))
                return false;  //如果不存在数据，直接返回
            String date= rootElt.elementText("date");  //获取根节点下的，当天日期
            //遍历results节点
            while (iter.hasNext()) {
                Element recordEle = (Element) iter.next();
                Iterator iters = recordEle.elementIterator("weather_data"); //
                //遍历results节点下的weather_data节点
                while (iters.hasNext()) {
                    Element itemEle = (Element) iters.next();
                    listdate=itemEle.elements("date");
                    listweather=itemEle.elements("weather");
                    listwind=itemEle.elements("wind");
                    listtem=itemEle.elements("temperature");
                }

                wlist.clear();
                for(int i=0;i<listdate.size();i++){  //由于每一个list.size都相等，这里统一处理
                    Element eledate=(Element)listdate.get(i); //依次取出date
                    Element eleweather=(Element)listweather.get(i);
                    Element elewind=(Element)listwind.get(i);
                    Element eletem=(Element)listtem.get(i);
                    perWeather pw = new perWeather(eledate.getText(),eleweather.getText(),elewind.getText(),eletem.getText());
                    wlist.add(pw);
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public  void saveXMLtoLocal()//保存单个地点的天气数据
    {
        SharedPreferences share = context.getSharedPreferences("datafju", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = share.edit();
        edit.putString(city, xml);
        edit.commit();
    }
    public void readXMLfromLocal()
    {
        SharedPreferences share = context.getSharedPreferences("datafju", Activity.MODE_PRIVATE);
        String s = share.getString(city, "无数据").toString();
        xml=s;
    }


    public void setCity(String city)
    {
        this.city=city;
    }
    public String getCity()
    {
        return city;
    }



}

class perWeather
{
    String city;
    String date;
    String weather;
    String wind;
    String tem;
    perWeather(String date,String weather ,String wind,String tem)
    {

        this.date=date;
        this.weather=weather;
        this.wind= wind;
        this.tem =tem;
    }
    perWeather(String city,String date,String weather ,String wind,String tem)
    {
        this.city=city;
        this.date=date;
        this.weather=weather;
        this.wind= wind;
        this.tem =tem;
    }
}