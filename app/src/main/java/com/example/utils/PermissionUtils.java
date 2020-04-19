package com.example.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/04/16
 *     desc  : 权限相关工具类
 * </pre>
 */
public final class PermissionUtils {

	private static int mRequestCode = -1;

	private static OnPermissionListener mOnPermissionListener;

	public interface OnPermissionListener {

		void onPermissionGranted();

		void onPermissionDenied(String[] deniedPermissions);
	}

	public abstract static class RationaleHandler {
		private Context context;
		private int requestCode;
		private String[] permissions;

		protected abstract void showRationale();

		void showRationale(Context context, int requestCode, String[] permissions) {
			this.context = context;
			this.requestCode = requestCode;
			this.permissions = permissions;
			showRationale();
		}

		@TargetApi(Build.VERSION_CODES.M)
		public void requestPermissionsAgain() {
			((Activity) context).requestPermissions(permissions, requestCode);
		}
	}

	@TargetApi(Build.VERSION_CODES.M)
	public static void requestPermissions(Context context, int requestCode
			, String[] permissions, OnPermissionListener listener) {
		requestPermissions(context, requestCode, permissions, listener, null);
	}

	@TargetApi(Build.VERSION_CODES.M)
	public static void requestPermissions(Context context, int requestCode
			, String[] permissions, OnPermissionListener listener, RationaleHandler handler) {
		if (context instanceof Activity) {
			mRequestCode = requestCode;
			mOnPermissionListener = listener;
			String[] deniedPermissions = getDeniedPermissions(context, permissions);
			if (deniedPermissions.length > 0) {
				boolean rationale = shouldShowRequestPermissionRationale(context, deniedPermissions);
				if (rationale && handler != null) {
					handler.showRationale(context, requestCode, deniedPermissions);
				} else {
					((Activity) context).requestPermissions(deniedPermissions, requestCode);
				}
			} else {
				if (mOnPermissionListener != null)
					mOnPermissionListener.onPermissionGranted();
			}
		} else {
			throw new RuntimeException("Context must be an Activity");
		}
	}

	/**
	 * 请求权限结果，对应Activity中onRequestPermissionsResult()方法。
	 */
	public static void onRequestPermissionsResult(Activity context, int requestCode, String[] permissions, int[]
			grantResults) {
		if (mRequestCode != -1 && requestCode == mRequestCode) {
			if (mOnPermissionListener != null) {
				String[] deniedPermissions = getDeniedPermissions(context, permissions);
				if (deniedPermissions.length > 0) {
					mOnPermissionListener.onPermissionDenied(deniedPermissions);
				} else {
					mOnPermissionListener.onPermissionGranted();
				}
			}
		}
	}

	/**
	 * 获取请求权限中需要授权的权限
	 */
	private static String[] getDeniedPermissions(final Context context, final String[] permissions) {
		List<String> deniedPermissions = new ArrayList<>();
		for (String permission : permissions) {
			if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
				deniedPermissions.add(permission);
			}
		}
		return deniedPermissions.toArray(new String[deniedPermissions.size()]);
	}

	/**
	 * 是否彻底拒绝了某项权限
	 */
	public static boolean hasAlwaysDeniedPermission(final Context context, final String... deniedPermissions) {
		for (String deniedPermission : deniedPermissions) {
			if (!shouldShowRequestPermissionRationale(context, deniedPermission)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否有权限需要说明提示
	 */
	private static boolean shouldShowRequestPermissionRationale(final Context context, final String... deniedPermissions) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;
		boolean rationale;
		for (String permission : deniedPermissions) {
			rationale = ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission);
			if (rationale) return true;
		}
		return false;
	}

	/**
	 * 通知权限
	 */
	@SuppressLint("NewApi")
	private static boolean isNotificationEnabled(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			return NotificationManagerCompat.from(context).getImportance() != NotificationManager.IMPORTANCE_NONE;
		}
		return NotificationManagerCompat.from(context).areNotificationsEnabled();
	}

	public static void openNotificationPermissionSetting(Context context) {
		if (!isNotificationEnabled(context)) {
			try {
				Intent localIntent = new Intent();
				localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//直接跳转到应用通知设置的代码：
				if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					localIntent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
					localIntent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
					context.startActivity(localIntent);
					return;
				}
				if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
					localIntent.putExtra("app_package", context.getPackageName());
					localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
					context.startActivity(localIntent);
					return;
				}
				if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
					localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
					localIntent.addCategory(Intent.CATEGORY_DEFAULT);
					localIntent.setData(Uri.parse("package:" + context.getPackageName()));
					context.startActivity(localIntent);
					return;
				}
				localIntent.setAction(Intent.ACTION_VIEW);
				localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
				localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(" cxx   pushPermission 有问题");
			}
		} else {

		}
	}

}
