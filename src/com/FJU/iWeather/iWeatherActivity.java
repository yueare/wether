package com.FJU.iWeather;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
/**
 * Created by FJU on 2015/5/27.
 */
public class iWeatherActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new UIupdater(this).update());
    }


}
