package com.example.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	private static long lastClickTime;




	/**
	 * <p>
	 * 检测是否重复点击事件
	 * </p>
	 *
	 * @return
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 800) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	//过滤设备/通道名称字符
	public static String strFilter(String str){
		String strEx="[\\:*?\"<>|/]";
		Pattern pattern=Pattern.compile(strEx);
		Matcher matcher=pattern.matcher(str);
		String mStr=matcher.replaceAll("");
		return mStr.replace("\\", "");
	}

	//过滤设备名称 只包含字母数字中文-_@
	public static String devNameFilter(String str){
		String strEx="[^a-zA-Z0-9-_@.!\u4E00-\u9FA5]";
		Pattern pattern=Pattern.compile(strEx);
		Matcher matcher=pattern.matcher(str);
		String mStr=matcher.replaceAll("");
		return mStr.replace("\\", "");
	}

	public static String encode(String uri) {

		if (TextUtils.isEmpty(uri)) {
			return uri;
		}
		String chinese = "[\u0391-\uFFE5\\$]";
		for (int i = 0; i < uri.length(); i++) {
			String temp = uri.substring(i, i + 1);
			if (temp.matches(chinese)) {
				uri = uri.replace(temp, Uri.encode(temp));
			}
		}
		uri = uri.replace(" ", "%20");
		return uri;
	}

	/**格式化浏览次数*/
	public static String encodeBrowswCount(int counts){
		if(counts < 10000){
			return counts +"次观看";
		}else{
			float d = counts;
			return String.format("%.1f",d/10000) + "万次观看";
		}
	}

	/**
	 * 字符过滤，用于密码
	 * @param str
	 * @return
	 */
	public static String strPwdFilter(String str) {

		if (TextUtils.isEmpty(str)) {
			return str;
		}

		String strEx = "^[a-zA-Z0-9\\-\\_\\@]+";

		for (int i = 0; i < str.length(); i++) {
			String temp = str.substring(i, i + 1);
			if (!temp.matches(strEx)) {
				str = str.replace(temp, "");
				return strPwdFilter(str);
			}
		}

		return str;
	}

	public static float stringToInt(String sb) {
		double temp = Double.valueOf(sb);
		return (float) Math.rint(temp);
	}

	public static String timeToDate(long time,String format){
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		String formatDate = dateformat.format(new Date(time));
		return formatDate;
	}

	public static long dateToTime(String date,String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		long time = 0;
		try {
			time =  dateFormat.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}
	/**
	 * 有内容返回true
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNullBool(String str) {
		if (!TextUtils.isEmpty(str) && !"null".equals(str)) {
			return true;
		} else {
			return false;
		}
	}


	@SuppressLint("StaticFieldLeak")
	private static Application sApplication;

	static WeakReference<Activity> sTopActivityWeakRef;
	static List<Activity> sActivityList = new LinkedList<>();

	private static Application.ActivityLifecycleCallbacks mCallbacks = new Application.ActivityLifecycleCallbacks() {
		@Override
		public void onActivityCreated(Activity activity, Bundle bundle) {
			sActivityList.add(activity);
			setTopActivityWeakRef(activity);
		}

		@Override
		public void onActivityStarted(Activity activity) {
			setTopActivityWeakRef(activity);
		}

		@Override
		public void onActivityResumed(Activity activity) {
			setTopActivityWeakRef(activity);
		}

		@Override
		public void onActivityPaused(Activity activity) {

		}

		@Override
		public void onActivityStopped(Activity activity) {

		}

		@Override
		public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

		}

		@Override
		public void onActivityDestroyed(Activity activity) {
			sActivityList.remove(activity);
		}
	};

	private Utils() {
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	/**
	 * 初始化工具类
	 *
	 * @param app 应用
	 */
	public static void init( final Application app) {
		Utils.sApplication = app;
		app.registerActivityLifecycleCallbacks(mCallbacks);
	}

	/**
	 * 获取Application
	 *
	 * @return Application
	 */
	public static Application getApp() {
		if (sApplication != null) return sApplication;
		throw new NullPointerException("u should init first");
	}

	private static void setTopActivityWeakRef(Activity activity) {
		if (sTopActivityWeakRef == null || !activity.equals(sTopActivityWeakRef.get())) {
			sTopActivityWeakRef = new WeakReference<>(activity);
		}
	}















	
}

