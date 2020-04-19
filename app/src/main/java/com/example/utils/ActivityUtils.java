package com.example.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.AnimRes;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import java.util.List;

public final class ActivityUtils {

	private ActivityUtils() {
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	/**
	 * 判断Activity是否存在
	 *
	 * @param packageName 包名
	 * @param className   activity全路径类名
	 * @return {@code true}: 是<br>{@code false}: 否
	 */
	public static boolean isActivityExists( final String packageName,
										    final String className) {
		Intent intent = new Intent();
		intent.setClassName(packageName, className);
		return !(MyApplication.getInstance().getPackageManager().resolveActivity(intent, 0) == null ||
				intent.resolveActivity(MyApplication.getInstance().getPackageManager()) == null ||
				MyApplication.getInstance().getPackageManager().queryIntentActivities(intent, 0).size() == 0);
	}

	/**
	 * 启动Activity
	 *
	 * @param clz activity类
	 */
	public static void startActivity( final Class<?> clz) {
		Context context = getActivityOrApp();
		startActivity(context, null, context.getPackageName(), clz.getName(), null);
	}

	/**
	 * 启动Activity
	 *
	 * @param clz     activity类
	 * @param options 跳转动画
	 */
	public static void startActivity( final Class<?> clz,
									  final Bundle options) {
		Context context = getActivityOrApp();
		startActivity(context, null, context.getPackageName(), clz.getName(), options);
	}

	/**
	 * 启动Activity
	 *
	 * @param clz       activity类
	 * @param enterAnim 入场动画
	 * @param exitAnim  出场动画
	 */
	public static void startActivity( final Class<?> clz,
									 @AnimRes final int enterAnim,
									 @AnimRes final int exitAnim) {
		Context context = getActivityOrApp();
		startActivity(context, null, context.getPackageName(), clz.getName(),
				getOptionsBundle(context, enterAnim, exitAnim));
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN && context instanceof Activity) {
			((Activity) context).overridePendingTransition(enterAnim, exitAnim);
		}
	}

	/**
	 * 启动Activity
	 *
	 * @param activity activity
	 * @param clz      activity类
	 */
	public static void startActivity( final Activity activity,
									  final Class<?> clz) {
		startActivity(activity, null, activity.getPackageName(), clz.getName(), null);
	}

	/**
	 * 启动Activity
	 *
	 * @param activity activity
	 * @param clz      activity类
	 * @param options  跳转动画
	 */
	public static void startActivity( final Activity activity,
									  final Class<?> clz,
									  final Bundle options) {
		startActivity(activity, null, activity.getPackageName(), clz.getName(), options);
	}

	/**
	 * 启动Activity
	 *
	 * @param activity       activity
	 * @param clz            activity类
	 * @param sharedElements 共享元素
	 */
	public static void startActivity( final Activity activity,
									  final Class<?> clz,
									  final View... sharedElements) {
		startActivity(activity, null, activity.getPackageName(), clz.getName(),
				getOptionsBundle(activity, sharedElements));
	}

	/**
	 * 启动Activity
	 *
	 * @param activity  activity
	 * @param clz       activity类
	 * @param enterAnim 入场动画
	 * @param exitAnim  出场动画
	 */
	public static void startActivity( final Activity activity,
									  final Class<?> clz,
									 @AnimRes final int enterAnim,
									 @AnimRes final int exitAnim) {

		startActivity(activity, null, activity.getPackageName(), clz.getName(),
				getOptionsBundle(activity, enterAnim, exitAnim));
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			activity.overridePendingTransition(enterAnim, exitAnim);
		}
	}

	/**
	 * 启动Activity
	 *
	 * @param extras extras
	 * @param clz    activity类
	 */
	public static void startActivity( final Bundle extras,
									  final Class<?> clz) {
		Context context = getActivityOrApp();
		startActivity(context, extras, context.getPackageName(), clz.getName(), null);
	}

	/**
	 * 启动Activity
	 *
	 * @param extras  extras
	 * @param clz     activity类
	 * @param options 跳转动画
	 */
	public static void startActivity( final Bundle extras,
									  final Class<?> clz,
									  final Bundle options) {
		Context context = getActivityOrApp();
		startActivity(context, extras, context.getPackageName(), clz.getName(), options);
	}

	/**
	 * 启动Activity
	 *
	 * @param extras    extras
	 * @param clz       activity类
	 * @param enterAnim 入场动画
	 * @param exitAnim  出场动画
	 */
	public static void startActivity( final Bundle extras,
									  final Class<?> clz,
									 @AnimRes final int enterAnim,
									 @AnimRes final int exitAnim) {
		Context context = getActivityOrApp();
		startActivity(context, extras, context.getPackageName(), clz.getName(),
				getOptionsBundle(context, enterAnim, exitAnim));
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN && context instanceof Activity) {
			((Activity) context).overridePendingTransition(enterAnim, exitAnim);
		}
	}

	/**
	 * 启动Activity
	 *
	 * @param extras   extras
	 * @param activity activity
	 * @param clz      activity类
	 */
	public static void startActivity( final Bundle extras,
									  final Activity activity,
									  final Class<?> clz) {
		startActivity(activity, extras, activity.getPackageName(), clz.getName(), null);
	}

	/**
	 * 启动Activity
	 *
	 * @param extras   extras
	 * @param activity activity
	 * @param clz      activity类
	 * @param options  跳转动画
	 */
	public static void startActivity( final Bundle extras,
									  final Activity activity,
									  final Class<?> clz,
									  final Bundle options) {
		startActivity(activity, extras, activity.getPackageName(), clz.getName(), options);
	}

	/**
	 * 启动Activity
	 *
	 * @param extras         extras
	 * @param activity       activity
	 * @param clz            activity类
	 * @param sharedElements 共享元素
	 */
	public static void startActivity( final Bundle extras,
									  final Activity activity,
									  final Class<?> clz,
									  final View... sharedElements) {
		startActivity(activity, extras, activity.getPackageName(), clz.getName(),
				getOptionsBundle(activity, sharedElements));
	}

	/**
	 * 启动Activity
	 *
	 * @param extras    extras
	 * @param activity  activity
	 * @param clz       activity类
	 * @param enterAnim 入场动画
	 * @param exitAnim  出场动画
	 */
	public static void startActivity( final Bundle extras,
									  final Activity activity,
									  final Class<?> clz,
									 @AnimRes final int enterAnim,
									 @AnimRes final int exitAnim) {
		startActivity(activity, extras, activity.getPackageName(), clz.getName(),
				getOptionsBundle(activity, enterAnim, exitAnim));
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			activity.overridePendingTransition(enterAnim, exitAnim);
		}
	}

	/**
	 * 启动Activity
	 *
	 * @param pkg 包名
	 * @param cls 全类名
	 */
	public static void startActivity( final String pkg,
									  final String cls) {
		startActivity(getActivityOrApp(), null, pkg, cls, null);
	}

	/**
	 * 启动Activity
	 *
	 * @param pkg     包名
	 * @param cls     全类名
	 * @param options 动画
	 */
	public static void startActivity( final String pkg,
									  final String cls,
									  final Bundle options) {
		startActivity(getActivityOrApp(), null, pkg, cls, options);
	}

	/**
	 * 启动Activity
	 *
	 * @param pkg       包名
	 * @param cls       全类名
	 * @param enterAnim 入场动画
	 * @param exitAnim  出场动画
	 */
	public static void startActivity( final String pkg,
									  final String cls,
									 @AnimRes final int enterAnim,
									 @AnimRes final int exitAnim) {
		Context context = getActivityOrApp();
		startActivity(context, null, pkg, cls, getOptionsBundle(context, enterAnim, exitAnim));
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN && context instanceof Activity) {
			((Activity) context).overridePendingTransition(enterAnim, exitAnim);
		}
	}

	/**
	 * 启动Activity
	 *
	 * @param activity activity
	 * @param pkg      包名
	 * @param cls      全类名
	 */
	public static void startActivity( final Activity activity,
									  final String pkg,
									  final String cls) {
		startActivity(activity, null, pkg, cls, null);
	}

	/**
	 * 启动Activity
	 *
	 * @param activity activity
	 * @param pkg      包名
	 * @param cls      全类名
	 * @param options  动画
	 */
	public static void startActivity( final Activity activity,
									  final String pkg,
									  final String cls,
									  final Bundle options) {
		startActivity(activity, null, pkg, cls, options);
	}

	/**
	 * 启动Activity
	 *
	 * @param activity       activity
	 * @param pkg            包名
	 * @param cls            全类名
	 * @param sharedElements 共享元素
	 */
	public static void startActivity( final Activity activity,
									  final String pkg,
									  final String cls,
									  final View... sharedElements) {
		startActivity(activity, null, pkg, cls, getOptionsBundle(activity, sharedElements));
	}

	/**
	 * 启动Activity
	 *
	 * @param activity  activity
	 * @param pkg       包名
	 * @param cls       全类名
	 * @param enterAnim 入场动画
	 * @param exitAnim  出场动画
	 */
	public static void startActivity( final Activity activity,
									  final String pkg,
									  final String cls,
									 @AnimRes final int enterAnim,
									 @AnimRes final int exitAnim) {
		startActivity(activity, null, pkg, cls, getOptionsBundle(activity, enterAnim, exitAnim));
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			activity.overridePendingTransition(enterAnim, exitAnim);
		}
	}

	/**
	 * 启动Activity
	 *
	 * @param extras extras
	 * @param pkg    包名
	 * @param cls    全类名
	 */
	public static void startActivity( final Bundle extras,
									  final String pkg,
									  final String cls) {
		startActivity(getActivityOrApp(), extras, pkg, cls, null);
	}

	/**
	 * 启动Activity
	 *
	 * @param extras  extras
	 * @param pkg     包名
	 * @param cls     全类名
	 * @param options 动画
	 */
	public static void startActivity( final Bundle extras,
									  final String pkg,
									  final String cls,
									  final Bundle options) {
		startActivity(getActivityOrApp(), extras, pkg, cls, options);
	}

	/**
	 * 启动Activity
	 *
	 * @param extras    extras
	 * @param pkg       包名
	 * @param cls       全类名
	 * @param enterAnim 入场动画
	 * @param exitAnim  出场动画
	 */
	public static void startActivity( final Bundle extras,
									  final String pkg,
									  final String cls,
									 @AnimRes final int enterAnim,
									 @AnimRes final int exitAnim) {
		Context context = getActivityOrApp();
		startActivity(context, extras, pkg, cls, getOptionsBundle(context, enterAnim, exitAnim));
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN && context instanceof Activity) {
			((Activity) context).overridePendingTransition(enterAnim, exitAnim);
		}
	}

	/**
	 * 启动Activity
	 *
	 * @param activity activity
	 * @param extras   extras
	 * @param pkg      包名
	 * @param cls      全类名
	 */
	public static void startActivity( final Bundle extras,
									  final Activity activity,
									  final String pkg,
									  final String cls) {
		startActivity(activity, extras, pkg, cls, null);
	}

	/**
	 * 启动Activity
	 *
	 * @param extras   extras
	 * @param activity activity
	 * @param pkg      包名
	 * @param cls      全类名
	 * @param options  动画
	 */
	public static void startActivity( final Bundle extras,
									  final Activity activity,
									  final String pkg,
									  final String cls,
									  final Bundle options) {
		startActivity(activity, extras, pkg, cls, options);
	}

	/**
	 * 启动Activity
	 *
	 * @param extras         extras
	 * @param activity       activity
	 * @param pkg            包名
	 * @param cls            全类名
	 * @param sharedElements 共享元素
	 */
	public static void startActivity( final Bundle extras,
									  final Activity activity,
									  final String pkg,
									  final String cls,
									  final View... sharedElements) {
		startActivity(activity, extras, pkg, cls, getOptionsBundle(activity, sharedElements));
	}

	/**
	 * 启动Activity
	 *
	 * @param extras    extras
	 * @param pkg       包名
	 * @param cls       全类名
	 * @param enterAnim 入场动画
	 * @param exitAnim  出场动画
	 */
	public static void startActivity( final Bundle extras,
									  final Activity activity,
									  final String pkg,
									  final String cls,
									 @AnimRes final int enterAnim,
									 @AnimRes final int exitAnim) {
		startActivity(activity, extras, pkg, cls, getOptionsBundle(activity, enterAnim, exitAnim));
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			activity.overridePendingTransition(enterAnim, exitAnim);
		}
	}

	/**
	 * 启动Activity
	 *
	 * @param intent 意图
	 */
	public static void startActivity( final Intent intent) {
		startActivity(intent, getActivityOrApp(), null);
	}

	/**
	 * 启动Activity
	 *
	 * @param intent  意图
	 * @param options 跳转动画
	 */
	public static void startActivity( final Intent intent,
									  final Bundle options) {
		startActivity(intent, getActivityOrApp(), options);
	}

	/**
	 * 启动Activity
	 *
	 * @param intent    意图
	 * @param enterAnim 入场动画
	 * @param exitAnim  出场动画
	 */
	public static void startActivity( final Intent intent,
									 @AnimRes final int enterAnim,
									 @AnimRes final int exitAnim) {
		Context context = getActivityOrApp();
		startActivity(intent, context, getOptionsBundle(context, enterAnim, exitAnim));
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN && context instanceof Activity) {
			((Activity) context).overridePendingTransition(enterAnim, exitAnim);
		}
	}

	/**
	 * 启动Activity
	 *
	 * @param activity activity
	 * @param intent   意图
	 */
	public static void startActivity( final Activity activity,
									  final Intent intent) {
		startActivity(intent, activity, null);
	}

	/**
	 * 启动Activity
	 *
	 * @param activity activity
	 * @param intent   意图
	 * @param options  跳转动画
	 */
	public static void startActivity( final Activity activity,
									  final Intent intent,
									  final Bundle options) {
		startActivity(intent, activity, options);
	}

	/**
	 * 启动Activity
	 *
	 * @param activity       activity
	 * @param intent         意图
	 * @param sharedElements 共享元素
	 */
	public static void startActivity( final Activity activity,
									  final Intent intent,
									  final View... sharedElements) {
		startActivity(intent, activity, getOptionsBundle(activity, sharedElements));
	}

	/**
	 * 启动Activity
	 *
	 * @param activity  activity
	 * @param intent    意图
	 * @param enterAnim 入场动画
	 * @param exitAnim  出场动画
	 */
	public static void startActivity( final Activity activity,
									  final Intent intent,
									 @AnimRes final int enterAnim,
									 @AnimRes final int exitAnim) {
		startActivity(intent, activity, getOptionsBundle(activity, enterAnim, exitAnim));
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			activity.overridePendingTransition(enterAnim, exitAnim);
		}
	}

	/**
	 * 启动多个Activity
	 *
	 * @param intents 意图
	 */
	public static void startActivities( final Intent[] intents) {
		startActivities(intents, getActivityOrApp(), null);
	}

	/**
	 * 启动多个Activity
	 *
	 * @param intents 意图
	 * @param options 跳转动画
	 */
	public static void startActivities( final Intent[] intents,
									    final Bundle options) {
		startActivities(intents, getActivityOrApp(), options);
	}

	/**
	 * 启动多个Activity
	 *
	 * @param intents   意图
	 * @param enterAnim 入场动画
	 * @param exitAnim  出场动画
	 */
	public static void startActivities( final Intent[] intents,
									   @AnimRes final int enterAnim,
									   @AnimRes final int exitAnim) {
		Context context = getActivityOrApp();
		startActivities(intents, context, getOptionsBundle(context, enterAnim, exitAnim));
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN && context instanceof Activity) {
			((Activity) context).overridePendingTransition(enterAnim, exitAnim);
		}
	}

	/**
	 * 启动多个Activity
	 *
	 * @param activity activity
	 * @param intents  意图
	 */
	public static void startActivities( final Activity activity,
									    final Intent[] intents) {
		startActivities(intents, activity, null);
	}

	/**
	 * 启动多个Activity
	 *
	 * @param activity activity
	 * @param intents  意图
	 * @param options  跳转动画
	 */
	public static void startActivities( final Activity activity,
									    final Intent[] intents,
									    final Bundle options) {
		startActivities(intents, activity, options);
	}

	/**
	 * 启动多个Activity
	 *
	 * @param activity  activity
	 * @param intents   意图
	 * @param enterAnim 入场动画
	 * @param exitAnim  出场动画
	 */
	public static void startActivities( final Activity activity,
									    final Intent[] intents,
									   @AnimRes final int enterAnim,
									   @AnimRes final int exitAnim) {
		startActivities(intents, activity, getOptionsBundle(activity, enterAnim, exitAnim));
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			activity.overridePendingTransition(enterAnim, exitAnim);
		}
	}

	/**
	 * 回到桌面
	 */
	public static void startHomeActivity() {
		Intent homeIntent = new Intent(Intent.ACTION_MAIN);
		homeIntent.addCategory(Intent.CATEGORY_HOME);
		startActivity(homeIntent);
	}

	/**
	 * 获取Activity栈链表
	 *
	 * @return Activity栈链表
	 */
	public static List<Activity> getActivityList() {
		return Utils.sActivityList;
	}

	/**
	 * 获取启动项Activity
	 *
	 * @return 启动项Activity
	 */
	public static String getLauncherActivity() {
		return getLauncherActivity(MyApplication.getInstance().getPackageName());
	}

	/**
	 * 获取启动项Activity
	 *
	 * @param packageName 包名
	 * @return 启动项Activity
	 */
	public static String getLauncherActivity( final String packageName) {
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PackageManager pm = MyApplication.getInstance().getPackageManager();
		List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
		for (ResolveInfo aInfo : info) {
			if (aInfo.activityInfo.packageName.equals(packageName)) {
				return aInfo.activityInfo.name;
			}
		}
		return "no " + packageName;
	}

	/**
	 * 获取栈顶Activity
	 *
	 * @return 栈顶Activity
	 */
	public static Activity getTopActivity() {
		if (Utils.sTopActivityWeakRef != null) {
			Activity activity = Utils.sTopActivityWeakRef.get();
			if (activity != null) {
				return activity;
			}
		}
		List<Activity> activities = Utils.sActivityList;
		int size = activities.size();
		return size > 0 ? activities.get(size - 1) : null;
	}

	/**
	 * 判断Activity是否存在栈中
	 *
	 * @param activity activity
	 * @return {@code true}: 存在<br>{@code false}: 不存在
	 */
	public static boolean isActivityExistsInStack( final Activity activity) {
		List<Activity> activities = Utils.sActivityList;
		for (Activity aActivity : activities) {
			if (aActivity.equals(activity)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断Activity是否存在栈中
	 *
	 * @param clz activity类
	 * @return {@code true}: 存在<br>{@code false}: 不存在
	 */
	public static boolean isActivityExistsInStack( final Class<?> clz) {
		List<Activity> activities = Utils.sActivityList;
		for (Activity aActivity : activities) {
			if (aActivity.getClass().equals(clz)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 结束Activity
	 *
	 * @param activity activity
	 */
	public static void finishActivity( final Activity activity) {
		finishActivity(activity, false);
	}

	/**
	 * 结束Activity
	 *
	 * @param activity   activity
	 * @param isLoadAnim 是否启动动画
	 */
	public static void finishActivity( final Activity activity, final boolean isLoadAnim) {
		activity.finish();
		if (!isLoadAnim) {
			activity.overridePendingTransition(0, 0);
		}
	}

	/**
	 * 结束Activity
	 *
	 * @param activity  activity
	 * @param enterAnim 入场动画
	 * @param exitAnim  出场动画
	 */
	public static void finishActivity( final Activity activity,
									  @AnimRes final int enterAnim,
									  @AnimRes final int exitAnim) {
		activity.finish();
		activity.overridePendingTransition(enterAnim, exitAnim);
	}

	/**
	 * 结束Activity
	 *
	 * @param clz activity类
	 */
	public static void finishActivity( final Class<?> clz) {
		finishActivity(clz, false);
	}

	/**
	 * 结束Activity
	 *
	 * @param clz        activity类
	 * @param isLoadAnim 是否启动动画
	 */
	public static void finishActivity( final Class<?> clz, final boolean isLoadAnim) {
		List<Activity> activities = Utils.sActivityList;
		for (Activity activity : activities) {
			if (activity.getClass().equals(clz)) {
				activity.finish();
				if (!isLoadAnim) {
					activity.overridePendingTransition(0, 0);
				}
			}
		}
	}

	/**
	 * 结束Activity
	 *
	 * @param clz       activity类
	 * @param enterAnim 入场动画
	 * @param exitAnim  出场动画
	 */
	public static void finishActivity( final Class<?> clz,
									  @AnimRes final int enterAnim,
									  @AnimRes final int exitAnim) {
		List<Activity> activities = Utils.sActivityList;
		for (Activity activity : activities) {
			if (activity.getClass().equals(clz)) {
				activity.finish();
				activity.overridePendingTransition(enterAnim, exitAnim);
			}
		}
	}

	/**
	 * 结束到指定Activity
	 *
	 * @param activity      activity
	 * @param isIncludeSelf 是否结束该activity自己
	 */
	public static boolean finishToActivity( final Activity activity,
										   final boolean isIncludeSelf) {
		return finishToActivity(activity, isIncludeSelf, false);
	}

	/**
	 * 结束到指定Activity
	 *
	 * @param activity      activity
	 * @param isIncludeSelf 是否结束该activity自己
	 * @param isLoadAnim    是否启动动画
	 */
	public static boolean finishToActivity( final Activity activity,
										   final boolean isIncludeSelf,
										   final boolean isLoadAnim) {
		List<Activity> activities = Utils.sActivityList;
		for (int i = activities.size() - 1; i >= 0; --i) {
			Activity aActivity = activities.get(i);
			if (aActivity.equals(activity)) {
				if (isIncludeSelf) {
					finishActivity(aActivity, isLoadAnim);
				}
				return true;
			}
			finishActivity(aActivity, isLoadAnim);
		}
		return false;
	}

	/**
	 * 结束到指定Activity
	 *
	 * @param activity      activity
	 * @param isIncludeSelf 是否结束该activity自己
	 * @param enterAnim     入场动画
	 * @param exitAnim      出场动画
	 */
	public static boolean finishToActivity( final Activity activity,
										   final boolean isIncludeSelf,
										   @AnimRes final int enterAnim,
										   @AnimRes final int exitAnim) {
		List<Activity> activities = Utils.sActivityList;
		for (int i = activities.size() - 1; i >= 0; --i) {
			Activity aActivity = activities.get(i);
			if (aActivity.equals(activity)) {
				if (isIncludeSelf) {
					finishActivity(aActivity, enterAnim, exitAnim);
				}
				return true;
			}
			finishActivity(aActivity, enterAnim, exitAnim);
		}
		return false;
	}

	/**
	 * 结束到指定Activity
	 *
	 * @param clz           activity类
	 * @param isIncludeSelf 是否结束该activity自己
	 */
	public static boolean finishToActivity( final Class<?> clz,
										   final boolean isIncludeSelf) {
		return finishToActivity(clz, isIncludeSelf, false);
	}

	/**
	 * 结束到指定Activity
	 *
	 * @param clz           activity类
	 * @param isIncludeSelf 是否结束该activity自己
	 * @param isLoadAnim    是否启动动画
	 */
	public static boolean finishToActivity( final Class<?> clz,
										   final boolean isIncludeSelf,
										   final boolean isLoadAnim) {
		List<Activity> activities = Utils.sActivityList;
		for (int i = activities.size() - 1; i >= 0; --i) {
			Activity aActivity = activities.get(i);
			if (aActivity.getClass().equals(clz)) {
				if (isIncludeSelf) {
					finishActivity(aActivity, isLoadAnim);
				}
				return true;
			}
			finishActivity(aActivity, isLoadAnim);
		}
		return false;
	}

	/**
	 * 结束到指定Activity
	 *
	 * @param clz           activity类
	 * @param isIncludeSelf 是否结束该activity自己
	 * @param enterAnim     入场动画
	 * @param exitAnim      出场动画
	 */
	public static boolean finishToActivity( final Class<?> clz,
										   final boolean isIncludeSelf,
										   @AnimRes final int enterAnim,
										   @AnimRes final int exitAnim) {
		List<Activity> activities = Utils.sActivityList;
		for (int i = activities.size() - 1; i >= 0; --i) {
			Activity aActivity = activities.get(i);
			if (aActivity.getClass().equals(clz)) {
				if (isIncludeSelf) {
					finishActivity(aActivity, enterAnim, exitAnim);
				}
				return true;
			}
			finishActivity(aActivity, enterAnim, exitAnim);
		}
		return false;
	}

	/**
	 * 结束除最新之外的同类型Activity
	 * <p>也就是让栈中最多只剩下一种类型的Activity</p>
	 *
	 * @param clz activity类
	 */
	public static void finishOtherActivitiesExceptNewest( final Class<?> clz) {
		finishOtherActivitiesExceptNewest(clz, false);
	}

	/**
	 * 结束除最新之外的同类型Activity
	 * <p>也就是让栈中最多只剩下一种类型的Activity</p>
	 *
	 * @param clz        activity类
	 * @param isLoadAnim 是否启动动画
	 */
	public static void finishOtherActivitiesExceptNewest( final Class<?> clz,
														 final boolean isLoadAnim) {
		List<Activity> activities = Utils.sActivityList;
		boolean flag = false;
		for (int i = activities.size() - 1; i >= 0; i--) {
			Activity activity = activities.get(i);
			if (activity.getClass().equals(clz)) {
				if (flag) {
					finishActivity(activity, isLoadAnim);
				} else {
					flag = true;
				}
			}
		}
	}

	/**
	 * 结束除最新之外的同类型Activity
	 * <p>也就是让栈中最多只剩下一种类型的Activity</p>
	 *
	 * @param clz       activity类
	 * @param enterAnim 入场动画
	 * @param exitAnim  出场动画
	 */
	public static void finishOtherActivitiesExceptNewest( final Class<?> clz,
														 @AnimRes final int enterAnim,
														 @AnimRes final int exitAnim) {
		List<Activity> activities = Utils.sActivityList;
		boolean flag = false;
		for (int i = activities.size() - 1; i >= 0; i--) {
			Activity activity = activities.get(i);
			if (activity.getClass().equals(clz)) {
				if (flag) {
					finishActivity(activity, enterAnim, exitAnim);
				} else {
					flag = true;
				}
			}
		}
	}

	/**
	 * 结束所有activity
	 */
	public static void finishAllActivities() {
		finishAllActivities(false);
	}

	/**
	 * 结束所有activity
	 *
	 * @param isLoadAnim 是否启动动画
	 */
	public static void finishAllActivities(final boolean isLoadAnim) {
		List<Activity> activityList = Utils.sActivityList;
		for (int i = activityList.size() - 1; i >= 0; --i) {// 从栈顶开始移除
			Activity activity = activityList.get(i);
			activity.finish();// 在onActivityDestroyed发生remove
			if (!isLoadAnim) {
				activity.overridePendingTransition(0, 0);
			}
		}
	}

	/**
	 * 结束所有activity
	 *
	 * @param enterAnim 入场动画
	 * @param exitAnim  出场动画
	 */
	public static void finishAllActivities(@AnimRes final int enterAnim, @AnimRes final int exitAnim) {
		List<Activity> activityList = Utils.sActivityList;
		for (int i = activityList.size() - 1; i >= 0; --i) {// 从栈顶开始移除
			Activity activity = activityList.get(i);
			activity.finish();// 在onActivityDestroyed发生remove
			activity.overridePendingTransition(enterAnim, exitAnim);
		}
	}

	private static Context getActivityOrApp() {
		Activity topActivity = getTopActivity();
		return topActivity == null ? MyApplication.getInstance() : topActivity;
	}

	private static void startActivity(final Context context,
									  final Bundle extras,
									  final String pkg,
									  final String cls,
									  final Bundle options) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		if (extras != null) intent.putExtras(extras);
		intent.setComponent(new ComponentName(pkg, cls));
		startActivity(intent, context, options);
	}

	private static void startActivity(final Intent intent,
									  final Context context,
									  final Bundle options) {
		if (!(context instanceof Activity)) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		if (options != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			context.startActivity(intent, options);
		} else {
			context.startActivity(intent);
		}
	}

	private static void startActivities(final Intent[] intents,
										final Context context,
										final Bundle options) {
		if (!(context instanceof Activity)) {
			for (Intent intent : intents) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
		}
		if (options != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			context.startActivities(intents, options);
		} else {
			context.startActivities(intents);
		}
	}

	private static Bundle getOptionsBundle(final Context context,
                                           final int enterAnim,
                                           final int exitAnim) {
		return ActivityOptionsCompat.makeCustomAnimation(context, enterAnim, exitAnim).toBundle();
	}

	private static Bundle getOptionsBundle(final Activity activity,
                                           final View[] sharedElements) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			int len = sharedElements.length;
			androidx.core.util.Pair<View, String>[] pairs = new androidx.core.util.Pair[len];
			for (int i = 0; i < len; i++) {
				pairs[i] = Pair.create(sharedElements[i], sharedElements[i].getTransitionName());
			}
			return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs).toBundle();
		}
		return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, null, null).toBundle();
	}
}