package widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.utils.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class DateSeekBar extends View {
    public static final int PORTRAIT = 0; // 竖屏模式

    public static final int LANDSCAPE = 1; // 横屏模式

    private static final int TIME = 24; // 时间段24小时，后面需要设置成可变的，支持跨天的进度条

    public static final int MAX = TIME * 3600; // 进度条最大值

    private static final int NONE = 0; // NONE模式

    private static final int DRAG = 1; // 拖拽模式

    private static final int ZOOM = 2; // 缩放模式

    private static final int SEEK_DELEY = 500; // seek延时触发时间

    private int viewId;//seekbar标记

    private int mWinIndex; // 由于seek延时触发和一个进度条对应N个窗口的模式，seek结束时需要把窗口号回调出去处理

    private int mMode = NONE; // 手势模式

    private int mOrientation = LANDSCAPE; // 横竖屏模式标志

    private Date mStartDate; // 开始时间

    private Date mEnDate; // 结束时间

    private float mWidth = 0; // 控件宽度

    private float mHeight = 0; // 控件高度

    private float mSourceWidth = 0; // 控件可见范围宽度

    private float mEmptyWidth; // 进度条前后预留的控件可见范围的一半

    private float mDrawWidth = 0; // 整个控件绘制范围宽度，不包含首尾空白

    private float mDrawStart = 0; // 开始绘制X

    private float mDrawEnd = 0; // 结束绘制Y

    private float mFillHeight = 0; // 填充内容的高度

    private float mThumbX = 0; // Thumb X坐标

    private float mMidX = 0; // 缩放基准点X坐标

    private float mMidY = 0; // 缩放基准点Y坐标

    private float mStartX = 0; // 一次DRAG的开始点X值

    private float mStartY = 0; // 一次DRAG的开始点Y值

    private float mScaleTextSize = sp2px(12); // 刻度文字大小

    private int mScaleTextColor; // 刻度文字颜色

    private float mDateMarginSize = dip2px(20); // 刻度距离上部大小

    private float mDateTextSize = sp2px(14); // 日期文字(当前时间)大小

    private int mDateTextColor; // 日期文字颜色

    private boolean isHideDateTime = false; //是否隐藏日期时间

    private int mThumbColor; // Thumb颜色

    private int mBackGroundMarginColor; // 分割线上部背景颜色

    private int mBackGroundColor; // 背景颜色

    private int mFillGroundMarginColor; // 分割线上部填充颜色

    private int mFillBgColor; // 填充时间条背景颜色

    private int mFillColor; // 填充时间的颜色

    private int mMaxScale = 480; // 最大缩放倍数

    private float mCurPos = 0.f; // 当前进度

    private float mCurrentScale = 1.0f; // 当前缩放系数

    private float mCurrentStart = 0.f; // 当前开始绘制X

    private float mCurrentEnd = 0.f; // 当前结束绘制Y

    private float mZoomStartdis = 0.f; // 一次Zoom开始时两个手指间距离（down）

    private float mZoomEnddis = 0.f; // 一次Zoom结束时两个手指间距离（up/cancel)

    private float mZoomScale = 1.0f; // 一次Zoom过程中相对于Zoom开始时的缩放倍数

    private boolean mCanTouch = true; // 能否Touch

    private boolean mShowShaderMode = false;//是否显示选择线

    private float mShaderLeftScale = 1.0f / 3.0f;//左侧选择线坐标比例

    private float mShaderRightScale = 2.0f / 3.0f;//右侧侧选择线坐标比例

    private boolean mSendShaderPosEnable = false;//是否发送选择线进度

    private int mShaderFillGroundMarginColor; // 显示选择线时分割线上部填充颜色

    private ArrayList<ClipRect> mClipRects; // 填充内容

    private Timer mSeekTimer = null; // seek延时触发定时器

    private TimerTask mSeekTimerTask = null; // seek触发任务

    private OnDateSeekBarChangeListener mSeekBarListener; // seek监听器

    public interface OnDateSeekBarChangeListener {
        void onProgressChanged(DateSeekBar seekBar, float xPosition, float yPosition, int winIndex);

        void onStartTrackingTouch(DateSeekBar seekBar, float xPosition, float yPosition, int winIndex);

        void onStopTrackingTouch(DateSeekBar seekBar, long curSeconds, int winIndex);

        void onStopZoom(DateSeekBar seekBar, float curScale, int winIndex);

        void onChangeTime(DateSeekBar seekBar, String time, float pos, int winIndex);

        void onChangeShader(DateSeekBar seekBar, long curStartTime, long curEndTime, int winIndex);

        void onChangeShaderInner(DateSeekBar seekBar, float curStartScale, float curEndScale, int winIndex);
    }

    public DateSeekBar(Context context) {
        this(context, null);
    }

    public DateSeekBar(Context context, AttributeSet attrs) {
        // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.seekBarStyle);
    }

    public DateSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public void setShaderFillGroundMarginColor(int color) {
        mShaderFillGroundMarginColor = color;
    }

    /**
     * 初始化函数
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午5:23:25
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimeBar);
        mBackGroundMarginColor = typedArray.getColor(R.styleable.TimeBar_background_margin_color, Color.WHITE);
        mBackGroundColor = typedArray.getColor(R.styleable.TimeBar_background_color, Color.WHITE);
        mFillGroundMarginColor = typedArray.getColor(R.styleable.TimeBar_fill_margin_color, Color.WHITE);
        mFillBgColor = typedArray.getColor(R.styleable.TimeBar_fill_bg_color, Color.BLACK);
        mFillColor = typedArray.getColor(R.styleable.TimeBar_fill_color, Color.WHITE);
        mScaleTextSize = sp2px(typedArray.getDimension(R.styleable.TimeBar_scale_text_size, 12));
        mScaleTextColor = typedArray.getColor(R.styleable.TimeBar_scale_text_color, Color.WHITE);
        mDateTextSize = sp2px(typedArray.getDimension(R.styleable.TimeBar_date_text_size, 14));
        mDateTextColor = typedArray.getColor(R.styleable.TimeBar_date_text_color, Color.WHITE);
        mThumbColor = typedArray.getColor(R.styleable.TimeBar_thumb_color, Color.WHITE);
        mDateMarginSize = sp2px(typedArray.getDimension(R.styleable.TimeBar_date_margin_size, 20));
        typedArray.recycle();

        mCurPos = 0.f;
        Date date = new Date(System.currentTimeMillis());
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        setStartDate((Date)date.clone());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBack(canvas);
        drawFill(canvas);
        drawText(canvas);
        if(!mShowShaderMode) drawThumb(canvas);
        drawCurPos(canvas);
        if(mShowShaderMode) {
            drawShader(canvas);
            drawShaderThumb(canvas);
            drawShaderText(canvas);
        }
    }

    /**
     * 绘制背景
     * 
     * @param canvas
     */
    private void drawBack(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        paint.setColor(mBackGroundMarginColor);
        canvas.drawRect(mDrawStart - mEmptyWidth, 0, mDrawEnd + mEmptyWidth, mDateMarginSize, paint);
        paint.setColor(mBackGroundColor);
        canvas.drawRect(mDrawStart - mEmptyWidth, mDateMarginSize, mDrawEnd + mEmptyWidth, mHeight, paint);
    }

    /**
     * 绘制填充时间
     *
     * @param canvas
     */
    private void drawFill(Canvas canvas) {
        // 绘制Fill的背景
        Paint paintBg = new Paint();
        paintBg.setAntiAlias(true);
        paintBg.setStyle(Style.FILL);
        paintBg.setColor(mShowShaderMode ? mShaderFillGroundMarginColor : mFillGroundMarginColor);
        canvas.drawRect(mDrawStart - mEmptyWidth, 0, mDrawEnd + mEmptyWidth, mDateMarginSize, paintBg);
        paintBg.setColor(mFillBgColor);
        canvas.drawRect(mDrawStart - mEmptyWidth, mDateMarginSize, mDrawEnd + mEmptyWidth, mHeight, paintBg);

        // 绘制Fill
        if (mClipRects == null || mClipRects.size() <= 0) {
            return;
        }

        for (ClipRect clipRect : mClipRects) {
            float xStart = mDrawWidth * (float) clipRect.start / MAX + mDrawStart;
            float xEnd = mDrawWidth * (float) clipRect.end / MAX + mDrawStart;
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Style.FILL);
            paint.setColor(mFillColor);
            canvas.drawRect(xStart, mDateMarginSize, xEnd, mHeight, paint);
        }
    }

    /**
     * 绘制浮层阴影
     * @param canvas
     */
    private void drawShader(Canvas canvas) {
        float xStart = mShaderLeftScale * mSourceWidth;
        float xEnd = mShaderRightScale * mSourceWidth;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new LinearGradient(xEnd, mHeight * 2 / 3, xEnd, mHeight, Color.TRANSPARENT, Color.argb(80,  0, 255, 0), Shader.TileMode.CLAMP));
        canvas.drawRect(xStart, mHeight * 2 / 3, xEnd, mHeight, paint);

    }

    /**
     * 绘制阴影Thumb
     *
     * @param canvas
     */
    private void drawShaderThumb(Canvas canvas) {
        float width = dip2px(11);
        float height = dip2px(5);
        float lineHeight = dip2px(2);

        Paint paint = new Paint();
        paint.setStrokeWidth(lineHeight);
        paint.setStyle(Style.FILL);
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);

        float xStart = mShaderLeftScale * mSourceWidth;
        float xEnd = mShaderRightScale * mSourceWidth;

        // 画直线
//        paint.setStrokeWidth(0);
        canvas.drawLine(xStart, mDateMarginSize -  height, xStart, mHeight, paint);
        canvas.drawLine(xEnd, mDateMarginSize -  height, xEnd, mHeight, paint);

        // 画上横线
        canvas.drawLine(xStart, mDateMarginSize -  height, xStart + width / 2, mDateMarginSize -  height, paint);
        canvas.drawLine(xEnd - width / 2, mDateMarginSize -  height, xEnd, mDateMarginSize -  height, paint);

        // 画下横线
        canvas.drawLine(xStart, mHeight - lineHeight, xStart + width / 2, mHeight - lineHeight, paint);
        canvas.drawLine(xEnd - width / 2, mHeight - lineHeight, xEnd, mHeight - lineHeight, paint);
    }

    /**
     * 绘制阴影时间点
     *
     * @param canvas
     */
    private void drawShaderText(Canvas canvas) {
        float height = dip2px(5);

        Paint paint = new Paint();
        paint.setStrokeWidth(0);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        paint.setTextSize(mScaleTextSize);
        paint.setTextAlign(Align.LEFT);
        paint.setColor(Color.WHITE);

        float xStart = mShaderLeftScale * mSourceWidth;
        float xEnd = mShaderRightScale * mSourceWidth;

        String shaderLeftTimeStr = getTimeStr(getShaderPosByloc(xStart));
        String shaderRightTimeStr = getTimeStr(getShaderPosByloc(xEnd));

        float textStartWidth = paint.measureText(shaderLeftTimeStr); // 文字长度
        float textEndWidth = paint.measureText(shaderRightTimeStr); // 文字长度
        FontMetrics fm = paint.getFontMetrics();
        float textHeight = (float)Math.ceil(fm.descent - fm.ascent); // 文字高度

        float curTextLeftX = xStart - textStartWidth / 2;
        float curTextRightX = xEnd - textEndWidth / 2;
        if(Math.abs(curTextRightX - curTextLeftX) <= Math.max(textStartWidth, textEndWidth) + dip2px(5)) {
            float innerLength = ((Math.max(textStartWidth, textEndWidth) - Math.abs(curTextRightX - curTextLeftX) + dip2px(5)) / 2);
            curTextLeftX -= innerLength;
            curTextRightX += innerLength;
        }
        //画时间
        canvas.drawText(shaderLeftTimeStr, curTextLeftX, mDateMarginSize -  height - textHeight / 3, paint);
        canvas.drawText(shaderRightTimeStr, curTextRightX, mDateMarginSize -  height - textHeight / 3, paint);
    }

    /**
     * 绘制文字
     * 
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        paint.setColor(mScaleTextColor);
        paint.setTextAlign(Align.LEFT);
        float cellWidth = mDrawWidth / (TIME * 60);
        paint.setTextSize(mScaleTextSize);
        float lineHeight = getMeasuredHeight() / 2.0f;

        canvas.drawLine(0, mDateMarginSize, MAX, mDateMarginSize, paint); // 绘制刻度线
        for (int i = 0; i <= TIME * 60; i++) {
            float lineX = cellWidth * (i) + mDrawStart; // 每个刻度的X坐标
           
            if (mCurrentScale >= 1 && mCurrentScale < 4) {
                // 2小时的时刻
                if (i % (60 * 2) == 0) {
                    drawCell(canvas, paint, lineHeight, lineX, "%02d", timeTo24H(i / 60));
                }
            }

            if (mCurrentScale >= 4 && mCurrentScale < 8) {
                // 1小时的刻度
                if (i % 60 == 0) {
                    drawCell(canvas, paint, lineHeight, lineX, "%02d", timeTo24H(i / 60));
                }
            }

            if (mCurrentScale >= 8 && mCurrentScale < 36) {
                // 30分钟的刻度
                if (i % 30 == 0) {
                    drawCell(canvas, paint, lineHeight, lineX, "%02d:%02d", timeTo24H(i / 60), ((i / 30) % 2) * 30);
                }
            }

            if(mOrientation == PORTRAIT){
            if (mCurrentScale >= 36 && mCurrentScale < 160) {
                // 10分钟的刻度
                if (i % 10 == 0) {
                    drawCell(canvas, paint, lineHeight, lineX, "%02d:%02d", timeTo24H(i / 60), i % 60);
                }
            }

            if (mCurrentScale >= 160) {
                // 1分钟的刻度
                drawCell(canvas, paint, lineHeight, lineX, "%02d:%02d", timeTo24H(i / 60), i % 60);
            }
            }else{
            	if (mCurrentScale >= 36 && mCurrentScale < 240) {
                    // 10分钟的刻度
                    if (i % 10 == 0) {
                        drawCell(canvas, paint, lineHeight, lineX, "%02d:%02d", timeTo24H(i / 60), i % 60);
                    }
                }

                if (mCurrentScale >= 240) {
                    // 1分钟的刻度
                    drawCell(canvas, paint, lineHeight, lineX, "%02d:%02d", timeTo24H(i / 60), i % 60);
                }
            }
        }
    }

    /**
     * 绘制一个刻度Cell
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午3:17:52
     * @param canvas
     *            Canvas
     * @param paint
     *            Paint
     * @param lineHeight
     *            刻度线高度
     * @param lineX
     *            刻度线X坐标
     * @param format
     *            文字
     */
    private void drawCell(Canvas canvas, Paint paint, float lineHeight, float lineX, String format, Object... args) {
        if (!(lineX >= 0 && lineX <= mSourceWidth)) {
            // 在可见范围之外，不绘制
            return;
        }
        String text = String.format(format, args); // 显示的刻度文字
        float dateStrLen = paint.measureText(text); // 文字长度
        FontMetrics fm = paint.getFontMetrics();
        int textHeight = (int) Math.ceil(fm.descent - fm.ascent); // 文字高度
        // if (mOrientation == PORTRAIT) {
        // // 竖屏
        // canvas.drawLine(lineX, mHeight / 2 + mFillHeight / 2, lineX, mHeight / 2 + mFillHeight /
        // 2 + lineHeight,
        // paint);// 绘制刻度线
        // canvas.drawText(text, lineX - dateStrLen / 2, mHeight / 2 + mFillHeight / 2 + lineHeight
        // + textHeight,
        // paint);// 绘制时间文字
        // } else {
        // 横屏
        canvas.drawLine(lineX, mDateMarginSize, lineX, mDateMarginSize + lineHeight, paint); // 绘制刻度线
        canvas.drawText(text, lineX - dateStrLen / 2, mDateMarginSize + lineHeight + dip2px(10), paint); // 绘制时间文字
        // }
    }

    /**
     * 绘制Thumb 在PAD版本中Thumb不需要滑动
     * 
     * @param canvas
     */
    private void drawThumb(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Style.FILL);
        paint.setColor(mThumbColor);
        paint.setAntiAlias(true);

        float width = dip2px(11);
        float height = dip2px(5);

        // 画直线
        paint.setStrokeWidth(0);
        canvas.drawLine(mThumbX, height, mThumbX, mHeight, paint);

        // 画上三角
        Path path1 = new Path();
        path1.moveTo(mThumbX - width / 2, 0);
        path1.lineTo(mThumbX + width / 2, 0);
        path1.lineTo(mThumbX, height);
        path1.close();
        canvas.drawPath(path1, paint);

//        // 画下三角
//        path1.moveTo(mThumbX - width / 2, mHeight);
//        path1.lineTo(mThumbX, mHeight - height);
//        path1.lineTo(mThumbX + width / 2, mHeight);
//        path1.close();
//        canvas.drawPath(path1, paint);
    }

    /**
     * 绘制当前日期和时间
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午8:57:53
     * @param canvas
     */
    private void drawCurPos(Canvas canvas) {
        if(isHideDateTime){
            return;
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        paint.setColor(mDateTextColor);
        paint.setTextSize(mDateTextSize);

        int paddingCenter = dip2px(14); // 日期和当前时间中间的空间的一半
        String dateStr = getDateStr(mCurPos); // 日期字符串
        FontMetrics fm = paint.getFontMetrics();
        int textHeight = (int) Math.ceil(fm.descent - fm.ascent); // 字体高度

        if (mOrientation == LANDSCAPE) {
            // 横屏
            // 绘制时间
            canvas.drawText(getTimeStr(mCurPos), mThumbX + paddingCenter, textHeight, paint);

            // 绘制日期
            float dateStrLen = paint.measureText(dateStr); // 文字长度
            canvas.drawText(getDateStr(mCurPos), mThumbX - dateStrLen - paddingCenter, textHeight, paint);
        } else {
            // 竖屏
            // 绘制时间
            canvas.drawText(getTimeStr(mCurPos), mThumbX + paddingCenter, mHeight / 2 - mFillHeight / 2 - dip2px(4),
                    paint);

            // 绘制日期
            float dateStrLen = paint.measureText(dateStr); // 文字长度
            canvas.drawText(getDateStr(mCurPos), mThumbX - dateStrLen - paddingCenter, mHeight / 2 - mFillHeight / 2
                    - dip2px(4), paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mShowShaderMode) {
            onTouchShader(event);
            return true;
        }
        if (!mCanTouch) {
            return true;
        }
        float x = event.getX();
        float y = event.getY();

        if (x < 0) {
            x = 0;
        }
        if (x > mWidth) {
            x = mWidth;
        }

        float desx = x - mStartX;
        float desy = y - mStartY;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                if (mCurrentScale < 1.0f) {
                    mCurrentScale = 1.0f;
                }
                if (event.getPointerCount() <= 1) {
                    mMode = DRAG;
                } else {
                    mMode = ZOOM;
                }
                if (mMode == DRAG) {
                    if (mSeekTimerTask != null) {
                        mSeekTimerTask.cancel();
                        mSeekTimerTask = null;
                    }
                    mCurPos = (mThumbX - mDrawStart) / mWidth * MAX;
                    mStartX = event.getX();
                    mStartY = event.getY();

                    if (mSeekBarListener != null) {
                        mSeekBarListener.onStartTrackingTouch(this, x, mHeight, mWinIndex);
                        mSeekBarListener.onChangeTime(this, getTimeStr(mCurPos), mCurPos, mWinIndex);
                    }
                } else if (mMode == ZOOM) {
                    mZoomStartdis = spacing(event);
                    midPoint(event);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mMode == DRAG) // 目前拖动只按拖动进度条对待
                {
                    mCurPos = (mThumbX - mDrawStart) / mWidth * MAX;
                    setMove(desx, desy);
                    if (mSeekBarListener != null) {
                        mSeekBarListener.onProgressChanged(this, x, mHeight, mWinIndex);
                        mSeekBarListener.onChangeTime(this, getTimeStr(mCurPos), mCurPos, mWinIndex);
                    }
                } else if (mMode == ZOOM) {
                    mZoomEnddis = spacing(event);
                    float scale = mZoomEnddis / mZoomStartdis;
                    if (scale * mCurrentScale >= mMaxScale && scale >= 1.0f) {
                        break;
                    }
                    mZoomScale = scale;
                    setZoom(mZoomScale, mMidX, mMidY);
                }

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:

                if (mMode == ZOOM) {
                    mCurrentScale = mZoomScale * mCurrentScale;
                    if (mCurrentScale > mMaxScale) {
                        mCurrentScale = mMaxScale;
                    } else if (mCurrentScale < 1.0f) {
                        mCurrentScale = 1.0f;
                    }
                    if (mDrawStart > mEmptyWidth) {
                        mDrawStart = mEmptyWidth;
                    }
                    if (mDrawEnd < mSourceWidth - mEmptyWidth) {
                        mDrawEnd = mSourceWidth - mEmptyWidth;
                    }
                }
                mWidth = mDrawWidth;
                mCurrentStart = mDrawStart;
                mCurrentEnd = mDrawEnd;
                controlSide();

                // 必须放在controlSide后面计算
                mCurPos = (mThumbX - mDrawStart) / mWidth * MAX;
                invalidate();

                if (mMode == DRAG && mSeekBarListener != null) {
                    long curSeconds = (long) mCurPos + mStartDate.getTime() / 1000;
                    if (mSeekTimerTask == null) {
                        mSeekTimerTask = new StopTrackingTask(mSeekBarListener, this, curSeconds, mWinIndex);
                    } else {
                        mSeekTimerTask.cancel();
                        mSeekTimerTask = null;
                        mSeekTimerTask = new StopTrackingTask(mSeekBarListener, this, curSeconds, mWinIndex);
                    }

                    if (mSeekTimer == null) {
                        mSeekTimer = new Timer();
                    }

                    mSeekTimer.schedule(mSeekTimerTask, SEEK_DELEY);
                }

                if (mMode == ZOOM && mSeekBarListener != null) {
                    setPressed(false);
                    mSeekBarListener.onStopZoom(this, mCurrentScale, mWinIndex);
                }

                mMode = NONE;
                break;
        }
        return true;
    }

    private float preShaderClickX = 0f;
    private int isClickedType = 0;
    private void onTouchShader(MotionEvent event) {
        float x = event.getX();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                if(Math.abs(x - mShaderLeftScale * mSourceWidth) <= dip2px(11)) {
                    isClickedType = 1;
                } else if(Math.abs(x - mShaderRightScale * mSourceWidth) <= dip2px(11)) {
                    isClickedType = 2;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float desx = x - preShaderClickX;
                setShaderMove(desx, isClickedType);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                isClickedType = 0;
                break;
        }
        preShaderClickX = x;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mSourceWidth = w;

        if (mDrawWidth == 0) {
            mDrawWidth = w;
            mDrawStart = mDrawWidth / 2;
            mDrawEnd = mDrawWidth;
            mEmptyWidth = mDrawWidth / 2;
            mThumbX = mDrawWidth / 2;
        } else {
        	//若无判断，切换横屏时有可能出现宽度为0的状况使刻度线混乱
        	if(w != 0 && oldw != 0){
            mDrawWidth = mDrawWidth * w / oldw;         
            mDrawStart = mDrawStart * w / oldw;
            mDrawEnd = mDrawEnd * w / oldw;
            mEmptyWidth = mEmptyWidth * w / oldw;
            mThumbX = mThumbX * w / oldw;
        	}
        }

        mCurrentStart = mDrawStart;
        mCurrentEnd = mDrawEnd;
        mWidth = mDrawWidth;

        if (mOrientation == LANDSCAPE) {
            mFillHeight = mHeight;
        } else {
            mFillHeight = mHeight / 2.5f;
        }
        refreshShader();
    }

    /**
     * 获取缩放基准点坐标
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午7:48:06
     * @param event
     */
    private void midPoint(MotionEvent event) {
        mMidX = mThumbX;
        mMidY = event.getY(0) + event.getY(1);
        mMidY /= 2;
    }

    /**
     * 缩放
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午7:08:54
     * @param zoom
     *            要放大/缩小的倍数
     * @param x
     *            缩放基准点X
     * @param y
     *            缩放基准点Y
     */
    private void setZoom(float zoom, float x, float y) {
        if (mWidth * zoom < mSourceWidth) {
            // 缩小的比控件原始大小还要小时，做边界限制。只能缩小到控件的原始大小。防止异常绘制，进度条会异常跳动。
            float scale = mSourceWidth / mWidth;
            mDrawWidth = mSourceWidth;
            mDrawStart = x - scale * (x - mCurrentStart);
            mDrawEnd = scale * (mCurrentEnd - x) + x;
        } else {
            mDrawWidth = mWidth * zoom;
            mDrawStart = x - zoom * (x - mCurrentStart);
            mDrawEnd = zoom * (mCurrentEnd - x) + x;
        }

        controlSide();
        invalidate();
    }

    /**
     * 滑动
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午2:34:38
     * @param x
     *            X要移动到的位置
     * @param y
     *            Y要移动到的位置
     */
    private void setMove(float x, float y) {
        float tmpStart = mCurrentStart + x;
        float tmpEnd = mCurrentEnd + x;
        if (tmpStart > mEmptyWidth) {
            mDrawStart = mEmptyWidth;
            x = mDrawStart - mCurrentStart;

        } else if (tmpEnd < mSourceWidth - mEmptyWidth) {
            mDrawEnd = mSourceWidth - mEmptyWidth;
            x = mDrawEnd - mCurrentEnd;
        }

        mDrawStart = mCurrentStart + x;
        mDrawEnd = mCurrentEnd + x;
        controlSide();
        invalidate();
    }

    private void setShaderMove(float x, int lineClick) {
        float mShaderLeftX = mShaderLeftScale * mSourceWidth;
        float mShaderRightX = mShaderRightScale * mSourceWidth;
        if(lineClick == 1 && (mShaderLeftScale < mShaderRightScale || x < 0)) {
            // 开始时间不允许大于结束时间
            mShaderLeftX = mShaderLeftX + x > mShaderRightX ? mShaderRightX : mShaderLeftX + x;
//            mShaderLeftX += x;
            if(mShaderLeftX <= mDrawStart) mShaderLeftX = mDrawStart;
            mShaderLeftScale = mShaderLeftX / mSourceWidth;
            if(mSeekBarListener != null) {
                sendShaderChange();
            }
            invalidate();
        } else if(lineClick == 2 && (mShaderLeftScale < mShaderRightScale || x > 0)) {
            // 结束时间不允许小于开始时间
            mShaderRightX = mShaderRightX + x < mShaderLeftX ? mShaderLeftX : mShaderRightX + x;
//            mShaderRightX += x;
            if(mShaderRightX >= mDrawEnd) mShaderRightX = mDrawEnd;
            mShaderRightScale = mShaderRightX / mSourceWidth;
            if(mSeekBarListener != null) {
                sendShaderChange();
            }
            invalidate();
        }
    }

    /**
     * 边界限制
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午7:47:33
     */
    private void controlSide() {
    	Log.i("绘制区间的边界", mDrawWidth + "");
        if (mDrawWidth < mSourceWidth) {
            mDrawWidth = mSourceWidth;
            mDrawStart = 0;
            mDrawEnd = mDrawWidth - mDrawStart;
        }

        if (mDrawStart > mEmptyWidth) {
            mDrawStart = mEmptyWidth;
        }

        if (mDrawEnd < mSourceWidth - mEmptyWidth) {
            mDrawEnd = mSourceWidth - mEmptyWidth;
        }
    }

    /**
     * seek结束操作
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午2:32:46
     * @version V1.0
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2014年9月15日
     * @modify by reason:{方法名}:{原因}
     */
    private class StopTrackingTask extends TimerTask {
        private OnDateSeekBarChangeListener listener;

        private DateSeekBar seekBar;

        private long curSeconds;

        private int winIndex;

        public StopTrackingTask(OnDateSeekBarChangeListener listener, DateSeekBar seekBar, long curSeconds, int winIndex) {
            this.listener = listener;
            this.seekBar = seekBar;
            this.curSeconds = curSeconds;
            this.winIndex = winIndex;
        }

        @Override
        public void run() {
            if (listener != null) {
                post(new Runnable() {
                    public void run() {
                        setPressed(false);
                        listener.onStopTrackingTouch(seekBar, curSeconds, winIndex);
                    }
                });
            }
        }

    }

    // ************************一些帮助方法***************************//
    /**
     * 两点间距离
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 上午11:00:01
     * @param event
     * @return
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    /**
     * 把48小时的时间String 转换成 24小时的时间String
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午7:22:50
     * @param time
     * @return
     */
    private int timeTo24H(int time) {
        // if (time >= 24) {
        // time = time - 24;
        // }
        return time;
    }

    /**
     * 更具当前进度获取时间字符串
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午8:16:32
     * @param curPos
     * @return
     */
    private String getTimeStr(float curPos) {
        long curTime = (mStartDate.getTime() / 1000L) + (long) curPos;
        Date date = new Date(curTime * 1000);
        String str = String.format("%02d:%02d:%02d", date.getHours(), date.getMinutes(), date.getSeconds());
        return str;
    }

    /**
     * 更具当前进度获取日期字符串
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午8:21:49
     * @param curPos
     * @return
     */
    private String getDateStr(float curPos) {
        Date date;
        if (curPos < MAX) {// / 2
            // 第一天
            date = mStartDate;
        } else {
            // 后一天
            date = mEnDate;
        }

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * dip2px
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 上午10:52:35
     * @param dpValue
     * @return
     */
    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp2px
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 上午10:52:42
     * @param spValue
     * @return
     */
    public int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    // ************************Public方法**************************//

    /**
     * 获取当前时间
     */
    public void getSeekTime(){
        mSeekBarListener.onChangeTime(this, getTimeStr(mCurPos), mCurPos, mWinIndex);
    }
    /**
     * 设置seek操作监听回调
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午2:31:27
     * @param listener
     */
    public void setOnSeekBarChangeListener(OnDateSeekBarChangeListener listener) {
        mSeekBarListener = listener;
    }

    /**
     * 隐藏日期时间
     */
    public void hideDateTime(boolean isHideDateTime){
        this.isHideDateTime = isHideDateTime;
    }
    /**
     * 设置进度条开始时间，48小时的
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午8:43:06
     * @param date
     */
    public void setStartDate(Date date) {
        this.mStartDate = date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calendar.getTime();
        endDate.setHours(24);
        endDate.setMinutes(0);
        endDate.setSeconds(0);
        this.mEnDate = endDate;
    }

    /**
     * 设置刻度文字大小
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午3:48:28
     * @param size
     */
    public void setScaleTextSize(int size) {
        this.mScaleTextSize = size;
    }

    /**
     * 设置刻度文字颜色
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午3:49:26
     * @param color
     */
    public void setScaleTextColrt(int color) {
        this.mScaleTextColor = color;
    }

    /**
     * 设置日期（当前时间）文字大小
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午3:50:11
     * @param size
     */
    public void setDateTextSize(int size) {
        this.mDateTextSize = size;
    }

    /**
     * 设置填充控件背景颜色
     * <p>
     * </p>
     *
     * @author 26499 2017年1月11日 下午3:53:55
     * @param color
     */
    public void setBackGroundColor(int color) {
        this.mBackGroundColor = color;
    }

    /**
     * 设置填充控件进度条上部颜色
     * <p>
     * </p>
     *
     * @author 26499 2017年1月11日 下午3:53:55
     * @param color
     */
    public void setBackGroundMarginColor(int color) {
        this.mBackGroundMarginColor = color;
    }

    /**
     * 设置日期（当前时间）文字颜色
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午3:51:03
     * @param color
     */
    public void setDateTextColor(int color) {
        this.mDateTextColor = color;
    }

    /**
     * 设置Thumb颜色
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午3:53:15
     * @param color
     */
    public void setThumbColor(int color) {
        this.mThumbColor = color;
    }


    /**
     * 设置填充背景上部颜色
     * <p>
     * </p>
     *
     * @author 26499 2017年1月11日 下午3:53:55
     * @param color
     */
    public void setFillGroundMarginColor(int color) {
        this.mFillGroundMarginColor = color;
    }

    /**
     * 设置填充背景颜色
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午3:53:55
     * @param color
     */
    public void setFillBgColor(int color) {
        this.mFillBgColor = color;
    }

    /**
     * 设置填充颜色
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 上午10:24:44
     * @param color
     */
    public void setFillColor(int color) {
        this.mFillColor = color;
    }

    /**
     * 设置横竖屏模式
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午3:20:34
     * @param orientation
     */
    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
    }

    /**
     * 设置填充时间
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午3:47:43
     * @param clipRects
     */
    public void setClipRects(ArrayList<ClipRect> clipRects) {
        this.mClipRects = clipRects;
        invalidate();
    }

    /**
     * 获取当前进度，精确到秒
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午8:11:13
     * @return
     */
    public float getProgress() {
        return mCurPos;
    }

    /**
     * 设置Touch事件是否可用
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午3:56:22
     * @param touch
     */
    public void setCanTouch(boolean touch) {
        mCanTouch = touch;
    }

    /**
     * 显示选择线
     * @param isShow
     */
    public void setShowShader(boolean isShow) {
        mShowShaderMode = isShow;
        float mShaderLeftX = mShaderLeftScale * mSourceWidth;
        float mShaderRightX = mShaderRightScale * mSourceWidth;
        if(mShaderLeftX <= mDrawStart) mShaderLeftScale = mDrawStart / mSourceWidth;
        if(mShaderRightX >= mDrawEnd) mShaderRightScale = mDrawEnd / mSourceWidth;
        sendShaderChange();
        invalidate();
    }

    public void setSendShaderEnable(boolean enable) {
        mSendShaderPosEnable = enable;
    }

    private void sendShaderChange() {
        if(mSeekBarListener != null && mSendShaderPosEnable) {
            float mShaderLeftX = mShaderLeftScale * mSourceWidth;
            float mShaderRightX = mShaderRightScale * mSourceWidth;
            mSeekBarListener.onChangeShader(this, getSecondByPos(getShaderPosByloc(mShaderLeftX)), getSecondByPos(getShaderPosByloc(mShaderRightX)), mWinIndex);
            mSeekBarListener.onChangeShaderInner(this, mShaderLeftX / mSourceWidth, mShaderRightX / mSourceWidth, mWinIndex);
        }
    }

    private long getSecondByPos(float curPos) {
        return (long) curPos + mStartDate.getTime() / 1000;
    }

    private float getShaderPosByloc(float loc) {
        float mPos = (loc - mDrawStart) / mWidth * MAX;
        if(mPos >= MAX) mPos = MAX - 1;
        return mPos;
    }

    public void setShaderLoaction(float curStartScale, float curEndScale) {
        mShaderLeftScale = curStartScale;
        mShaderRightScale = curEndScale;
//        mShaderLeftPos = (mShaderLeftX - mDrawStart) / mWidth * MAX;
//        mShaderRightPos = (mShaderRightX - mDrawStart) / mWidth * MAX;
//        if(mShaderRightPos >= MAX) mShaderRightPos = MAX - 1;
    }

    private void refreshShader() {
//        mShaderLeftX = mShaderLeftScale * mSourceWidth;
//        mShaderRightX = mShaderRightScale * mSourceWidth;
//        if(mShaderLeftX <= mDrawStart) mShaderLeftX = mDrawStart;
//        if(mShaderRightX >= mDrawEnd) mShaderRightX = mDrawEnd;
        invalidate();
    }

    /**
     * 设置当前进度
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午1:41:47
     * @param progress
     */
    public void setProgress(float progress) {
        if (progress < 0) {
            progress = 0;
        }

        if(progress > 3600 * 24){
            progress = 3600 * 24;
        }

        mCurPos = progress;
        mDrawStart = mThumbX - progress / MAX * mWidth;
        mDrawEnd = mDrawStart + mDrawWidth;
        mCurrentStart = mDrawStart;
        mCurrentEnd = mDrawEnd;
        controlSide();
        invalidate();
    }

    /**
     * 缩放完成
     * 
     * @param scale
     */
    public void scaleEnd(float scale) {
        mCurrentScale *= scale;
        if (mCurrentScale > mMaxScale) {
            mCurrentScale = mMaxScale;
        } else if (mCurrentScale < 1.0f) {
            mCurrentScale = 1.0f;
        }
        if (mDrawStart > 0) {
            mDrawStart = 0;
        }
        if (mDrawEnd < mSourceWidth) {
            mDrawEnd = mSourceWidth;
        }
        mWidth = mDrawWidth;
        mCurrentStart = mDrawStart;
        mCurrentEnd = mDrawEnd;
        controlSide();
    }

    /**
     * 设置缩放倍率
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 上午11:17:56
     * @param scale
     */
    public void setScale(float scale) {
        if (scale < 1.0f) {
            scale = 1.0f;
        }

        if (scale > mMaxScale) {
            scale = mMaxScale;
        }

        float zoom = scale / mCurrentScale;
        setZoom(zoom, mMidX, 0);
        scaleEnd(zoom);
        invalidate();
    }

    /**
     * 获取当前缩放倍率
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 上午10:58:29
     * @return
     */
    public float getCurScale() {
        return mCurrentScale;
    }

    /**
     * 由于seek延时触发和一个进度条对应N个窗口的模式，seek结束时需要把窗口号回调出去处理
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午3:50:05
     * @param index
     */
    public void setWinIndex(int index) {
        mWinIndex = index;
    }

    public void setViewId(int id) {
        viewId = id;
    }

    public int getViewId() {
        return viewId;
    }

    /**
     * 自动缩放到合适比例（播放成功之后调用）
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午4:38:56
     */
    public void autoZoom() {
        if (mClipRects == null || mClipRects.size() == 0) {
            return;
        }

        long wholeSecond = mClipRects.get(mClipRects.size() - 1).end - mClipRects.get(0).start;
        float scale = ((float) MAX) / ((float) wholeSecond);
        if (scale < 1.0f) {
            scale = 1.0f;
        }

        if (scale > mMaxScale) {
            scale = mMaxScale;
        }

        setScale(scale);
    }

    /**
     * 进度条恢复初始状态
     * <p>
     * </p>
     * 
     * @author 26499 2017年1月11日 下午3:54:49
     */
    public void clear() {
        setScale(1);
        setProgress(0);
        setCanTouch(true);
        setClipRects(null);
        mStartX = 0.f;
        mStartY = 0.f;
        mMidX = 0.f;
        mMidY = 0.f;
        mZoomStartdis = 0.f;
        mZoomEnddis = 0.f;
        mZoomScale = 1.0f;
        mMode = NONE;
    }

    public float getCurrentScale() {
        return mCurrentScale;
    }

    public void setCurrentScale(float currentScale) {
        this.mCurrentScale = currentScale;
    }

    public static class ClipRect {
        public long start;
        public long end;

        public ClipRect(long start,long end)
        {
            this.start = start;
            this.end = end;
        }
    }
}
