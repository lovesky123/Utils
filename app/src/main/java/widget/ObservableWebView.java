package widget;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 功能说明：自定义WebView滑动监听
 */
public class ObservableWebView extends WebView {

    private OnScrollChangedCallBack mOnScrollChangedCallBack;

    public ObservableWebView(Context context) {
        super(context);
    }

    public ObservableWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallBack != null) {
            mOnScrollChangedCallBack.onScroll(l - oldl, t - oldt);
        }
    }

    public OnScrollChangedCallBack getOnScrollChangedCallBack() {
        return mOnScrollChangedCallBack;
    }

    public void setOnScrollChangedCallBack(OnScrollChangedCallBack onScrollChangedCallBack) {
        this.mOnScrollChangedCallBack = onScrollChangedCallBack;
    }

    /**
     * WebView滑动监听接口
     */
    public interface OnScrollChangedCallBack {
        void onScroll(int dx, int dy);
    }
}
