package toast;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IntDef;

import com.example.utils.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Date: 2018/11/26
 * @Description: 使用系统Toast的问题 {@link android.widget.Toast}：当通知权限被关闭时在华为等手机上Toast不显示。因此采取自定义Toast解决.
 * 优先使用系统Toast，如果通知权限被关闭，则使用DovaToast.
 */
public class DToast {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DURATION_SHORT, DURATION_LONG})
    public @interface Duration {
    }

    public static final int DURATION_SHORT = 2000;
    public static final int DURATION_LONG = 5000;

//    public static void customToast(String msg) {
//        customToast(Utils.getContext(),msg);
//    }

    public static void customToast(Context context, String msg) {

//        if (context instanceof Activity && DovaToast.isBadChoice()) {
//            //检测到DovaToast连续多次抛出token null is not valid异常时，直接启用ActivityToast
//            return new ActivityToast(context);
//        }
//        return new DovaToast(context);

        if (context != null) {
            IToast toast = DToast.make(context);
            View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
            TextView textView = view.findViewById(R.id.custom_toast_text);
            textView.setText(msg);
            toast.setView(view);
            toast.setDuration(DToast.DURATION_LONG);
            toast.setGravity(Gravity.TOP, 0, 200);

            toast.show();
        }
    }



    public static IToast make(Context mContext) {
        if (mContext == null) return null;
        if (mContext instanceof Activity && DovaToast.isBadChoice()) {
            //检测到DovaToast连续多次抛出token null is not valid异常时，直接启用ActivityToast
            return new ActivityToast(mContext);
        }
        return new DovaToast(mContext);


//        //如果有通知权限，直接使用系统Toast
//        //MIUI系统没有通知权限时系统Toast也能正常展示
//        if (NotificationManagerCompat.from(mContext).areNotificationsEnabled() || SystemToast.isValid4HookINotificationManager() || DUtil.isMIUI()) {
//            return new SystemToast(mContext);
//        } else {//否则使用自定义Toast
//            if (mContext instanceof Activity && DovaToast.isBadChoice()) {
//                //检测到DovaToast连续多次抛出token null is not valid异常时，直接启用ActivityToast
//                return new ActivityToast(mContext);
//            }
//            return new DovaToast(mContext);
//        }
    }

    /**
     * 终止并清除所有弹窗
     */
    public static void cancel() {
        DovaToast.cancelAll();
        SystemToast.cancelAll();
    }

    /**
     * 清除与{@param mActivity}关联的ActivityToast，避免窗口泄漏
     */
    public static void cancelActivityToast(Activity mActivity) {
        DovaToast.cancelActivityToast(mActivity);
    }
}
