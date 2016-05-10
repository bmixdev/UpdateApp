package com.wind.updateapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;

/**
 * Created by wind on 16/5/9.
 */
public class UpdateAgent {
    private static UpdateAgent updateAgent;
    private UpdateAgent(){

    }

    public synchronized static UpdateAgent getInstance(){
        if (updateAgent==null){
            updateAgent=new UpdateAgent();
        }
        return updateAgent;
    }

    private UpdateApi.UpdateListener updateListener;
    public void setUpdateListener(UpdateApi.UpdateListener updateListener){
        this.updateListener=updateListener;
    }
    /**
     * 强制检查更新,不管用户是否忽略此更新
     * @param context
     * @param channelName
     */
    public  void forceUpdate(final FragmentActivity context, final String channelName){
        update(context,channelName,true);
    }

    /**
     * 自动检查更新(非强制)
     * @param context
     * @param channelName
     */
    public void update(final FragmentActivity context, final String channelName){
        update(context,channelName,false);
    }
    private   void update(final FragmentActivity context, final String channelName,boolean forceUpdate){
        //获取应用版本号
        final String versionCode=getAppVersion(context);
        //应用渠道
        //包名
        final String packageName=context.getPackageName();

        if (!forceUpdate){
            //是否忽略更新
            SharedPreferences sp=context.getSharedPreferences("update",FragmentActivity.MODE_PRIVATE);
            boolean isIgnoreUpadte=sp.getBoolean(UpdateDialogFragment.SP_KEY_IGNORE_UPDATE,false);
            if (isIgnoreUpadte){
                return;
            }
        }

        if (updateListener==null){
            updateListener=new UpdateApi.UpdateListener() {
                @Override
                public void onUpdateReturned(int updateStatus, UpdateInfo updateInfo) {
                    switch (updateStatus) {
                        case UpdateStatus.Yes: // has update
                            UpdateAgent.showUpdateDialog(context,
                                    updateInfo);
                            break;
                        case UpdateStatus.No: // has no update
                            //  ToastUtil.showToast(SettingActivity.this,"没有更新");
                            break;
                          /*  case UpdateStatus.NoneWifi: // none wifi
                                break;*/
                        case UpdateStatus.Timeout: // time out
                            //  ToastUtil.showToast(SettingActivity.this, "超时");
                            break;
                    }
                }
            };
        }

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                new UpdateApi().update(packageName, versionCode, channelName, updateListener);
            }
        });
        thread.start();



    }

    private static void showUpdateDialog(final FragmentActivity context, UpdateInfo updateInfo) {
        if (!context.isFinishing()){
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UpdateDialogFragment dialogFragment=new UpdateDialogFragment();
                    dialogFragment.show(context.getSupportFragmentManager(),"UpdateDialogFragment");
                }
            });

        }
    }


    public static String getAppVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionCode+"";
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
