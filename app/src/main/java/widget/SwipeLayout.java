package widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;


public class SwipeLayout extends FrameLayout {

    public static enum Status {
        Close, Open, Swiping
    }

    public interface OnSwipeListener {
        void onClose(SwipeLayout layout);

        void onOpen(SwipeLayout layout);

        void onStartClose(SwipeLayout layout);

        void onStartOpen(SwipeLayout layout);
    }

    private Status status = Status.Close;

    private OnSwipeListener onSwipeListener;

    public Status getStaus() {
        return status;
    }

    public OnSwipeListener getOnSwipeListener() {
        return onSwipeListener;
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

    public void setStaus(Status staus) {
        this.status = staus;
    }

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mHelper = ViewDragHelper.create(this, callback);
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {


        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        public int getViewHorizontalDragRange(View child) {
            return mRange;
        }



        public int clampViewPositionHorizontal(View child, int left, int dx) {


            if (child == mFrontView) {
                if (left < -mRange) {
                    return -mRange;
                } else if (left > 0) {

                    return 0;
                }
            } else if (child == mBackView) {
                if (left < mWidth - mRange) {
                    return mWidth - mRange;
                } else if (left > mWidth) {
                    return mWidth;
                }
            }
            return left;
        }


        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            if (changedView == mFrontView) {
                mBackView.offsetLeftAndRight(dx);
            } else if (changedView == mBackView) {

                mFrontView.offsetLeftAndRight(dx);
            }

            dispatchDragEvent();

            invalidate();
        }


        public void onViewReleased(View releasedChild, float xvel, float yvel) {

            if (xvel == 0 && mFrontView.getLeft() < -mRange * 0.5f) {
                open();
            } else if (xvel < 0) {
                open();
            } else {
                close();
            }
        }
    };
    private View mBackView;
    private View mFrontView;
    private int mRange;
    private int mWidth;
    private int mHeight;
    private ViewDragHelper mHelper;


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mHelper.shouldInterceptTouchEvent(ev);
    }


    protected void dispatchDragEvent() {

        Status lastStatus = status;


        status = updateStatus();

        if (lastStatus != status && onSwipeListener != null) {
            if (status == Status.Open) {
                onSwipeListener.onOpen(this);
            } else if (status == Status.Close) {
                onSwipeListener.onClose(this);
            } else if (status == Status.Swiping) {
                if (lastStatus == Status.Close) {
                    onSwipeListener.onStartOpen(this);
                } else if (lastStatus == Status.Open) {
                    onSwipeListener.onStartClose(this);
                }
            }
        }

    }


    private Status updateStatus() {
        int left = mFrontView.getLeft();
        if (left == 0) {
            return Status.Close;
        } else if (left == -mRange) {
            return Status.Open;
        }
        return Status.Swiping;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//
//        mRange = mBackView.getMeasuredWidth();
//        mWidth = getMeasuredWidth();
//        mHeight = getMeasuredHeight();
//
//    }


    public void close() {
        close(true);
    }

    public void close(boolean isSmooth) {
        int finalLeft = 0;
        if (isSmooth) {

            if (mHelper.smoothSlideViewTo(mFrontView, finalLeft, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            layoutContent(false);
        }
    }

    public void open() {
        open(true);
    }

    public void open(boolean isSmooth) {
        int finalLeft = -mRange;
        if (isSmooth) {

            if (mHelper.smoothSlideViewTo(mFrontView, finalLeft, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }

        } else {
            layoutContent(false);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        try {
            mHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mRange = mBackView.getMeasuredWidth();
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        layoutContent(false);

    }


    private void layoutContent(boolean isOpen) {

        Rect rect = computeFrontRect(isOpen);
        mFrontView.layout(rect.left, rect.top, rect.right, rect.bottom);

        Rect backRect = computeBackRectViaFront(rect);
        mBackView.layout(backRect.left, backRect.top, backRect.right, backRect.bottom);

        bringChildToFront(mFrontView);
    }


    private Rect computeBackRectViaFront(Rect rect) {
        int left = rect.right;
        return new Rect(left, 0, left + mRange, 0 + mHeight);
    }

    private Rect computeFrontRect(boolean isOpen) {
        int left = 0;
        if (isOpen) {
            left = -mRange;
        }
        return new Rect(left, 0, left + mWidth, 0 + mHeight);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mBackView = getChildAt(0);
        mFrontView = getChildAt(1);
    }

}
