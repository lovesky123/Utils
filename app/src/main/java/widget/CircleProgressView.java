package widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.example.utils.ConvertUtils;
import com.example.utils.R;

public class CircleProgressView extends View {
    private int mProgressWidth;
    private int mMax;
    private int mProgress;

    private Paint mProgressPaint;
    private Paint mProgressDefaultPaint;
    private RectF mRectF;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //读取xml文件中的信息
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
        int mProgressColor = attributes.getColor(R.styleable.CircleProgressView_cpvProgressColor, Color.BLACK);
        int mProgressDefaultColor = attributes.getColor(R.styleable.CircleProgressView_cpvProgressDefaultColor, Color.TRANSPARENT);
        mProgressWidth = attributes.getDimensionPixelSize(R.styleable.CircleProgressView_cpvProgressWidth, ConvertUtils.dp2px(2));
        mMax = attributes.getInteger(R.styleable.CircleProgressView_cpvMax, 100);
        mProgress = attributes.getInteger(R.styleable.CircleProgressView_cpvProgress, 0);
        attributes.recycle();

        mProgressPaint = new Paint();
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(mProgressWidth);
        mProgressPaint.setAntiAlias(true);

        mProgressDefaultPaint = new Paint();
        mProgressDefaultPaint.setColor(mProgressDefaultColor);
        mProgressDefaultPaint.setStyle(Paint.Style.STROKE);
        mProgressDefaultPaint.setStrokeWidth(mProgressWidth);
        mProgressDefaultPaint.setAntiAlias(true);

        mRectF = new RectF(0, 0, 0, 0);
    }

    public void setMax(int max){
        this.mMax = max;
        invalidate();
    }

    public void setProgress(int progress){
        this.mProgress = progress;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int range = getMeasuredWidth() > getMeasuredHeight() ? getMeasuredHeight()-mProgressWidth : getMeasuredWidth()-mProgressWidth;
        mRectF.set(mProgressWidth, mProgressWidth, range, range);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(mRectF, -90, 360, false, mProgressDefaultPaint);

        int angle = (int) ((double)mProgress/mMax*360);
        canvas.drawArc(mRectF, -90, angle, false, mProgressPaint);
    }
}
