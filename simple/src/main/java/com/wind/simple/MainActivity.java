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
       /* UpdateAgent.getInstance().setUpdateListener(new UpdateApi.UpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateInfo updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                    *//*    UpdateAgent.showUpdateDialog(MainActivity.this,
                                updateInfo);*//*
                        Toast.makeText(MainActivity.this,"有更新",Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.No: // has no update
                        //  ToastUtil.showToast(SettingActivity.this,"没有更新");
                        break;
                          *//*  case UpdateStatus.NoneWifi: // none wifi
                                break;*//*
                    case UpdateStatus.Timeout: // time out
                        //  ToastUtil.showToast(SettingActivity.this, "超时");
                        break;
                }
            }
        });
        UpdateAgent.getInstance().forceUpdate(this,"");*/
    }
}
