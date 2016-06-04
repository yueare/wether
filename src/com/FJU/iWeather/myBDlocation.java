package com.FJU.iWeather;

import android.content.Context;
import android.content.Intent;
import com.baidu.location.*;

public class myBDlocation
{
	public LocationClient mLocationClient = null;
	public GeofenceClient mGeofenceClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	public static String TAG = "myBDlocation";
	Context context;
	public myBDlocation(Context context)
	{
		this.context=context;
		
		mLocationClient = new LocationClient(context);

		/**
		 * 项目的key
		 */

		mLocationClient.setAK("mrkFduw900voCwAXWP2G5NOj");
		mLocationClient.registerLocationListener(myListener);
		mGeofenceClient = new GeofenceClient(context);
	}
	

	/**
	 * 停止定位
	 */
	public void stopLocationClient()
	{
		if (mLocationClient != null && mLocationClient.isStarted())
		{
			mLocationClient.stop();
		} 
	}

	/**
	 * 发起定位
	 */
	public void requestLocationInfo()
	{
		setLocationOption();
		
		if (mLocationClient != null && !mLocationClient.isStarted())
		{
			mLocationClient.start();
		}

		if (mLocationClient != null && mLocationClient.isStarted())
		{
			mLocationClient.requestLocation();
		} 
	}
	
	/**
	 *  设置相关参数
	 */
	private void setLocationOption()
	{
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开GPS
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setServiceName("com.baidu.location.service_v2.9");//调用百度地图定位服务
		option.setPoiExtraInfo(true);
		option.setAddrType("all");
		option.setPoiNumber(10);
		option.disableCache(true);
		mLocationClient.setLocOption(option);
	}

	/**
	 * 监听函数，有更新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener
	{
		@Override
		public void onReceiveLocation(BDLocation location)
		{
			if (location == null)
			{
				sendBroadCast("定位失败!");
				return;
			}
			sendBroadCast(location.getCity());
		}

		public void onReceivePoi(BDLocation poiLocation)
		{
			if (poiLocation == null)
			{
				sendBroadCast("定位失败!");
				return;
			}
			sendBroadCast(poiLocation.getCity());

		}
		
	}
	
	/**
	 * 得到发送广播
	 * @param address
	 */
	public void sendBroadCast(String address)
	{
		stopLocationClient();
		
		Intent intent = new Intent(addcity.LOCATION_BCR);
		intent.putExtra("address", address);
		context.sendBroadcast(intent);
	}
}
