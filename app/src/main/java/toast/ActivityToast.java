package toast;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.WindowManager;


/**
 * @Date: 2018/11/20
 * @Description: 只展示在当前Activity的弹窗，不具有跨页面功能, 销毁Activity时需要主动清除相应的ActivityToast, 否则会造成窗口泄漏：/has leaked window/。
 * <p>
 * 注意：在Activity.onCreate()时展示ActivityToast依然会抛出Unable to add window -- token null is not valid; is your activity running，因为此时Activity尚未进入running状态
 */
public class ActivityToast extends DovaToast {

    //context非Activity时会抛出异常:Unable to add window -- token null is not valid; is your activity running?
    public ActivityToast(Context mContext) {
        super(mContext);
    }

    @Override
    public WindowManager.LayoutParams getWMParams() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.format = PixelFormat.TRANSLUCENT;

//        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;//此方案是否更优？
//            mParams.y = mToast.getYOffset() + getNavBarHeight();

        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.windowAnimations = android.R.style.Animation_Toast;
        // TODO: 2018/11/20 考虑是否需要引入windowToken
//        lp.token=((Activity)getContext()).getWindow().getDecorView().getWindowToken();
        lp.gravity = getGravity();
        lp.x = getXOffset();
        lp.y = getYOffset();
        return lp;
    }

    @Override
    public WindowManager getWMManager() {
        //context非Activity时会抛出异常:Unable to add window -- token null is not valid; is your activity running?
        if (mContext instanceof Activity) {
            return ((Activity) mContext).getWindowManager();
        } else {
            return null;
        }
    }
}