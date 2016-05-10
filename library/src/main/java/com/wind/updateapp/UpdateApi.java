package com.wind.updateapp;

import android.util.Log;

/**
 * Created by wind on 16/5/9.
 */
public class UpdateApi {
    private String upadteUrl="";

    public void update(String packageName, String versionCode, String channelName,UpdateListener listener) {
        try{
            Log.e("UpdateApi","update");
            listener.onUpdateReturned(UpdateStatus.Yes,null);
           /* URL url = new URL(getUpadteUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            //connection.setRequestProperty("Accept-Charset", "utf-8");
            //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //connection.setRequestProperty("Content-Length", String.valueOf(parameterData.length()));
            StringBuffer params = new StringBuffer();
            // 表单参数与get形式一样
            params.append("version_code").append("=").append(versionCode).append("&")
                    .append("channel_name").append("=").append(channelName).append("&").append("package_name").append("=").append(packageName);
            byte[] bytes = params.toString().getBytes();
            connection.getOutputStream().write(bytes);// 输入参数

            if (connection.getResponseCode()==200){
                InputStream ips=connection.getInputStream();

                String result=StreamTool.readInputStream(ips);
                //转成对象

            }

            connection.disconnect();*/
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getUpadteUrl() {
        return upadteUrl;
    }


    public interface UpdateListener{
        void onUpdateReturned(int updateStatus,UpdateInfo updateInfo);
    }
}
