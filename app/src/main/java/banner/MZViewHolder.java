package banner;

import android.content.Context;
import android.view.View;

/**
* @function MZViewHolder
* @author wulimin
* @time 2018/8/15 下午12:00
*/


public interface MZViewHolder<T> {
    /**
     *  创建View
     * @param context
     * @return
     */
    View createView(Context context);

    /**
     * 绑定数据
     * @param context
     * @param position
     * @param data
     */
    void onBind(Context context, int position, T data);
}
