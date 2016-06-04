package com.FJU.iWeather;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import com.FJU.iWeather.R;

/**
 * Created by FJU on 2015/5/29.
 */
public class about extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.about);
    }
}