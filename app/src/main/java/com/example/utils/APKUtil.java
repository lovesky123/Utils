
package com.example.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class APKUtil {
    private static String TAG = "APK_UTILS";
    /**
     * 获取Application下的Meta-Data数据
     */
    public static String getApplicationMetaData(String key) {
        Context context = MyApplication.getInstance();
        String medaData = "";
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            medaData = info.metaData.getString(key);
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return medaData;
    }

    /**
     * 是否安装了某个应用
     *
     * @param packageName
     * @return
     */
    public static boolean isAInstallPackage(String packageName) {
        final PackageManager packageManager = MyApplication.getInstance().getPackageManager();// 获取packagemanager
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        List<String> packages = new ArrayList<String>();// 用于存储所有已安装程序的包名
        if (packageInfos != null) {
            for (PackageInfo packageInfo : packageInfos) {
                packages.add(packageInfo.packageName);
            }
        }
        return packages.contains(packageName);// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

    /**
     * 获得本应用版本号
     *
     * @param //context
     * @return 返回版本号
     */
    public static int getMYVersionCode() {
        int versionCode = 0;
        try {
            PackageInfo pi = getPackageInfoByPackage(MyApplication.getInstance().getPackageName());
            versionCode = pi.versionCode;
        } catch (Exception e) {
        }
        return versionCode;
    }

    /**
     * 获得应用版本名称
     *
     * @param //context
     * @return
     */
    public static String getMYVersionName() {
        String versionName = "";
        try {
            PackageInfo pi = getPackageInfoByPackage(MyApplication.getInstance().getPackageName());
            versionName = pi.versionName;
        } catch (Exception e) {
        }
        return versionName;
    }


    /**
     * 按已安装程序报名获取包信息
     *
     * @param //context
     * @param packageName 已安装程序的包名
     * @return
     */
    public static PackageInfo getPackageInfoByPackage(String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = MyApplication.getInstance().getPackageManager().getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }


    /**
     * 打开安卓应用市场
     *
     * @param context
     */
    public static void openAppMarket(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }


    /**
     * 获取渠道名称
     *
     * @return
     */
    public static String getChannelName() {
        return getApplicationMetaData("TD_CHANNEL_ID");
    }

    /**
     * 判断是否service 正在工作
     *
     * @param className
     * @return
     */
    public static boolean isServiceWorking(String className) {
        ActivityManager myManager = (ActivityManager) MyApplication.getInstance()
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName()
                    .equals(className)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取当前本地apk的版本 versionCode
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @return
     */
    public static String getVersionName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

}
