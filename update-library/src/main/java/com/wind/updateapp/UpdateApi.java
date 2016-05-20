package com.wind.updateapp;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wind on 16/5/9.
 */
public class UpdateApi {
    private String upadteUrl="http://121.40.100.36:8080/api/";

    public void update(String packageName, String versionCode, String channelName,UpdateListener listener) {
        try{
           /* Log.e("UpdateApi","update");
            UpdateInfo updateInfo=new UpdateInfo();
            updateInfo.setLatestAppUrl("http://img1.51marryyou.com/2016-03-18/a0b15b52af3267491263ca48a7d4e3a1.apk");
            listener.onUpdateReturned(UpdateStatus.Yes,updateInfo);*/
            URL url = new URL(getUpadteUrl(packageName,channelName,versionCode));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setUseCaches(false);
           // connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            //connection.setRequestProperty("Accept-Charset", "utf-8");
            //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //connection.setRequestProperty("Content-Length", String.valueOf(parameterData.length()));
           /* StringBuffer params = new StringBuffer();
            // 表单参数与get形式一样
            params.append("version_code").append("=").append(versionCode).append("&")
                    .append("channel_name").append("=").append(channelName).append("&").append("package_name").append("=").append(packageName);
            byte[] bytes = params.toString().getBytes();
            connection.getOutputStream().write(bytes);// 输入参数*/

            if (connection.getResponseCode()==200){
                InputStream ips=connection.getInputStream();

                String result=StreamTool.readInputStream(ips);
                try {
                    //转成对象
                    JSONObject jsonObject = new JSONObject(result);
                    String version = jsonObject.getString("Version");
                    String description = jsonObject.getString("Description");
                    String appUrl = jsonObject.getString("Url");
                    String appSize = jsonObject.getString("Length");

                    UpdateInfo updateInfo = new UpdateInfo();
                    updateInfo.setLatestVersion(version);
                    updateInfo.setLatestUpdateContent(description);
                    updateInfo.setLatestAppUrl(appUrl);
                    updateInfo.setLatestAppSize(appSize);
                    listener.onUpdateReturned(UpdateStatus.Yes, updateInfo);
                }catch (Exception e){
                    e.printStackTrace();
                    listener.onUpdateReturned(UpdateStatus.No, null);
                }
            }

            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getUpadteUrl(String packageName, String channelName,String versionCode) {
        return upadteUrl+packageName+"/"+channelName+"/"+versionCode;
    }


    public interface UpdateListener{
        void onUpdateReturned(int updateStatus,UpdateInfo updateInfo);
    }
}
