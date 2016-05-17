package com.wind.updateapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class AutoInstall {

    /** 
     * 安装 
     *  
     * @param context 
     *            接收外部传进来的context
     * @param apkPath
     */  
    public static void install(Context context,String apkPath) {

        // 核心是下面几句代码  
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }  
}  