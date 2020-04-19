package com.example.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * activity管理
 */

public class ActivityManagerUtils {
    private static ActivityManagerUtils mActivityManagerUtils;

    private ActivityManagerUtils() {
        //这里面写一些需要执行初始化的工作
    }

    public static ActivityManagerUtils getInstance() {
        if (mActivityManagerUtils == null) {
            synchronized (ActivityManagerUtils.class) {
                if (mActivityManagerUtils == null) {
                    mActivityManagerUtils = new ActivityManagerUtils();
                }
            }
        }
        return mActivityManagerUtils;
    }

    /**
     * 打开的activity
     **/
    private static Stack<Activity> activityStack;
    private static Stack<Activity> activityStack_sign;

    /**
     * 新建了一个activity
     *
     * @param activity 传入
     */

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishLastActivity() {
        Activity activity = activityStack.lastElement();
        removeActivity(activity);
    }

    /**
     * 移除指定的Activity
     *
     * @param activity 传入
     */

    public void removeActivity(Activity activity) {
        if (activity != null && activityStack != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 清除不释放指定的Activity
     *
     * @param activity 传入
     */
    public void clearThisActivity(Activity activity) {
        if (activity != null && activityStack != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 移出activity不结束
     */
    public void removeActivitys() {
        if (activityStack == null) {
            return;
        }
        activityStack.clear();
    }

    /**
     * 应用退出，清楚所有的activity
     */
    public void clearActivitys() {
        if (activityStack == null) {
            return;
        }
        activityStack.clear();
    }
    /**
     * 结束指定类名的Activity
     */
    public void finishThisActivityclass(Class<?> cls) {
        if (cls == null || activityStack==null) return;
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                removeActivity(activity);
            }
        }
    }

    /**
     * 应用退出，结束所有的activity
     */
    public void finishActivitys() {
        if (activityStack == null) {
            return;
        }
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public void addActivity_sign(Activity activity) {
        if (activityStack_sign == null) {
            activityStack_sign = new Stack<Activity>();
        }
        activityStack_sign.add(activity);
    }

    /**
     * 移除指定的Activity
     *
     * @param activity
     */
    public void removeActivity_sign(Activity activity) {
        if (activity != null) {
            activityStack_sign.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 移出activity不结束
     */
    public void removeActivitys_sign() {
        if (activityStack_sign == null) {
            return;
        }
        activityStack_sign.clear();
    }

    public void finishActivitys_sign() {
        if (activityStack_sign == null) {
            return;
        }
        for (int i = 0, size = activityStack_sign.size(); i < size; i++) {
            if (null != activityStack_sign.get(i)) {
                activityStack_sign.get(i).finish();
            }
        }
        activityStack_sign.clear();
    }

    public void clearActivitys_sign() {
        if (activityStack_sign == null) {
            return;
        }
        activityStack_sign.clear();
    }
}
