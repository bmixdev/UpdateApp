package com.wind.simple;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.wind.updateapp.UpdateAgent;

/**
 * Created by wind on 16/5/10.
 */
public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UpdateAgent.getInstance().update(this,"");
    }
}
