package com.wind.updateapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wind on 16/5/11.
 */
public class DownloadService extends Service {

    private static final String ACTION_CANCEL = "action_cancel";
    private static final String ACTION_PAUSE_OR_START = "action_pause_or_start";
    public static final String EXTRA_KEY_DOWNLOAD_URL = "extra_key_download_url";
    private static final int WHAT_PROGRESS = 1;
    private static final int WHAT_ERROR = 2;
    private static final int WHAT_DOWNLOAD_SUCCESS = 3;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (executorService == null || executorService.isShutdown()) {
                return;
            }
            switch (msg.what) {
                case WHAT_PROGRESS:
                    Integer progress = (Integer) msg.obj;
                    Log.e("handleMessage", progress + "");
                    mBuilder.setContentText(progress + "%");
                    mBuilder.setProgress(100, progress, false);
                    mNotification = mBuilder.build();
                    mNotification.flags = Notification.FLAG_ONGOING_EVENT;
                    mNotification.flags = Notification.FLAG_NO_CLEAR;
                    // Displays the progress bar for the first time.
                    mNotificationManager.notify(NOTIFY_ID, mNotification);
                    break;
                case WHAT_ERROR:
                    Log.e("DownloadService", "下载失败");
                    break;
                case WHAT_DOWNLOAD_SUCCESS:
                    Log.e("DownloadService", "下载成功");
                    installApk();
                    break;
            }


        }
    };

    private void installApk() {
        AutoInstall.install(getApplicationContext(), getDownloadApkPath());
    }

    private String getDownloadApkPath() {
        return getDownloadPath() + getFilenName(downloadUrl);
    }

    Notification mNotification;
    NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
    private static final int NOTIFY_ID = 0;
    private boolean stopflag = false;

    private int oldProgress;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("DownloadService", "onCreate");
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent cancelIntent = new Intent(this, DownloadService.class);
        cancelIntent.setAction(ACTION_CANCEL);
        PendingIntent piCancel = PendingIntent.getService(this, 0, cancelIntent, 0);

     /*   Intent pauseOrStartIntent = new Intent(this, DownloadService.class);
        pauseOrStartIntent.setAction(ACTION_PAUSE_OR_START);

        PendingIntent piPauseOrStart = PendingIntent.getService(this, 0, pauseOrStartIntent, 0);*/


        long when = System.currentTimeMillis();
        mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.download)//设置小标题
                .setContentTitle("正在下载:" + getAppName())
                .setContentText("0%")

                .setStyle(new NotificationCompat.BigTextStyle())
                // .addAction(R.drawable.item_checked, "暂停", piPauseOrStart)
                .addAction(R.drawable.download_cancel, "取消", piCancel)
                .setWhen(when)
                .setAutoCancel(false);

        mNotification = mBuilder.build();
        // 放置在"正在运行"栏目中
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
        mNotification.flags = Notification.FLAG_NO_CLEAR;


        mNotificationManager.notify(NOTIFY_ID, mBuilder.build());
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    String downloadUrl = "";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("DownloadService", "onStartCommand");
        //获取action
        String action = "";

        if (intent != null) {
            action = intent.getAction();

            if (TextUtils.isEmpty(action))
                downloadUrl = intent.getStringExtra(EXTRA_KEY_DOWNLOAD_URL);

            Log.e("onStartCommand", "downloadUrl" + downloadUrl);
        }

        if (TextUtils.isEmpty(action)) {
            startDownload(downloadUrl);
        } else if (ACTION_PAUSE_OR_START.equals(action)) {
            stopflag = !stopflag;
            if (!stopflag) {
                startDownload(downloadUrl);
            } else {
                executorService.shutdownNow();
            }
        } else if (ACTION_CANCEL.equals(action)) {
            if (executorService != null && !executorService.isShutdown()) {
                executorService.shutdown();
            }

            stopSelf();
        }


        return super.onStartCommand(intent, flags, startId);
    }


    ExecutorService executorService;

    private void startDownload(String downloadUrl) {
        //if (executorService == null) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new DownloadRunnable(downloadUrl));
        //}

    }

    private String getAppName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;

    }

    private class DownloadRunnable implements Runnable {
        private String downloadUrl;
        int startposition;

        DownloadRunnable(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            startposition = 0;
        }

        @Override
        public void run() {
            // 开始执行后台任务
            try {

                URL url = new URL(downloadUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
               /* conn.setRequestProperty("User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");*/
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                //String range="bytes=" + startposition + "-"+ fileLen;
                //Log.e("Range",range);
                conn.setRequestProperty("Referer", url.toString());
                //conn.setRequestProperty("Range", range);// 设置获取实体数据的范围

                conn.setRequestProperty(
                        "User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
                conn.setRequestProperty("Connection", "Keep-Alive"); // 设置为持久连接

                int code = conn.getResponseCode();
                if (code == 200) {
                    printResponseHeader(conn);
                    int fileLen = conn.getContentLength();
                    Log.e("Length", "length" + conn.getContentLength() + "-fileLen:" + fileLen);
                    File receiveFile = new File(getDownloadPath(), getFilenName(downloadUrl));
                    if (!receiveFile.exists()) {
                        boolean isCreated = receiveFile.createNewFile();
                        Log.e("File", "isCreated" + isCreated + ":eceiveFile.getPath()" + receiveFile.getPath());
                    }
                    RandomAccessFile file = new RandomAccessFile(
                            receiveFile, "rwd");
                    // 1.设置本地文件大小跟服务器的文件大小一致
                    file.setLength(fileLen);

                    // 设置 数据从文件哪个位置开始写
                    file.seek(startposition);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    // 代表当前读到的服务器数据的位置 ,同时这个值已经存储的文件的位置
                    int currentPostion = startposition;
                    // 创建一个文件对象 ,记录当前某个文件的下载位置
                    InputStream is = conn.getInputStream();
                    while ((len = is.read(buffer)) != -1) {
                        if (stopflag) {
                            System.out.println("文件下载暂停");
                            return;
                        }

                        //System.out.println("文件下载中");
                        file.write(buffer, 0, len);


                        currentPostion += len;

                        Message msg = Message.obtain();
                        int progress = (int) ((currentPostion / (float) fileLen) * 100);
                        if (oldProgress != progress) {
                            oldProgress = progress;
                            msg.what = WHAT_PROGRESS;
                            msg.obj = progress;
                            mHandler.sendMessage(msg);
                        }
                    }

                    file.close();
                    System.out.println("文件下载完毕");
                    Message msg = Message.obtain();
                    msg.what = WHAT_DOWNLOAD_SUCCESS;
                    mHandler.sendMessage(msg);

                }
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = WHAT_ERROR;
                mHandler.sendMessage(msg);
            } finally {
                if (!stopflag)
                    stopSelf();
            }


        }
    }

    private String getFilenName(String downloadUrl) {
        return downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
    }

    private String getDownloadPath() {
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String parentPath = dir + "/download" + "/.wind/";
        File parentDir = new File(parentPath);
        if (!parentDir.exists()) {
            boolean mkFlag = parentDir.mkdirs();
            Log.e("dir", "mkFlag:" + mkFlag);
        }

        return parentPath;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("DownloadService", "onDestroy");
        // stopForeground(true);
        mNotificationManager.cancel(NOTIFY_ID);
    }

    /**
     * 获取Http响应头字段
     *
     * @param http
     * @return
     */
    public static Map<String, String> getHttpResponseHeader(
            HttpURLConnection http) {
        Map<String, String> header = new LinkedHashMap<String, String>();
        for (int i = 0; ; i++) {
            String mine = http.getHeaderField(i);
            if (mine == null)
                break;
            header.put(http.getHeaderFieldKey(i), mine);
        }
        return header;
    }

    /**
     * 打印Http头字段
     *
     * @param http
     */
    public static void printResponseHeader(HttpURLConnection http) {
        Map<String, String> header = getHttpResponseHeader(http);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            String key = entry.getKey() != null ? entry.getKey() + ":" : "";
            print(key + entry.getValue());
        }
    }

    private static void print(String msg) {
        Log.i("DownloadService", msg);
    }
}
