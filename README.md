# UpdateApp
用于 android app自动更新

用法
------

Android Studio

使用前，对于Android Studio的用户，可以选择添加:

compile project(':library')



自动更新
------
   在要自动更新的Activity的onCreate方法中加入
   UpdateAgent.getInstance().update(this,"这里填入android 渠道名称");

点击检查按钮更新
------
 UpdateAgent.getInstance().forceUpdate(this,"这里填入android 渠道名称");
 
 如果要显示loading等待，请自行设置UpdateListener
 以下代码需要写在forceUpdate方法之前。
  UpdateAgent.getInstance().setUpdateListener(new UpdateApi.UpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateInfo updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                    //可以在这里取消对话框
                    
                        UpdateAgent.showUpdateDialog(MainActivity.this,
                                updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        //  ToastUtil.showToast(SettingActivity.this,"没有更新");
                        break;
                    
                    case UpdateStatus.Timeout: // time out
                        //  ToastUtil.showToast(SettingActivity.this, "超时");
                        break;
                }
            }
        });
