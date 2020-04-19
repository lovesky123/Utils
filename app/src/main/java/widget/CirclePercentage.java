package widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.example.utils.R;

public class CirclePercentage extends View {

    private int mArcColor;
    private int mArcBackgroundSelectColor;
    private int mArcWidth;
    private int mCenterTextColor;
    private int mCenterTextColorSelected;
    private int mCenterTextSize;
    private int mCircleRadius;
    private Paint arcPaint;
    private Paint arcCirclePaint;
    private Paint centerTextPaint;
    private RectF arcRectF;
    private Rect textBoundRect;
    private int arcStartColor;
    private int arcEndColor;
    private Paint mCenterCirclePaint;
    private RectF mCenterCircleRect;
    private String mIndicate;//说明
    private String mPercentage;//比例
    private int mCurRate = 0;
    private float mRateOffset = 0;

    public CirclePercentage(Context context) {
        this(context, null);
    }

    public CirclePercentage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePercentage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentage, defStyleAttr, 0);
        mArcColor = typedArray.getColor(R.styleable.CirclePercentage_arcColor, 0xff0000);
        mArcBackgroundSelectColor = typedArray.getColor(R.styleable.CirclePercentage_arcSelectedBackgroundColor, 0xff0000);
        mArcWidth = typedArray.getDimensionPixelSize(R.styleable.CirclePercentage_arcWidthValue, 0);
        mCenterTextColor = typedArray.getColor(R.styleable.CirclePercentage_centerTextColor, 0x0000ff);
        mCenterTextColorSelected = typedArray.getColor(R.styleable.CirclePercentage_centerTextColorSelected, 0x0000ff);
        mCenterTextSize = typedArray.getDimensionPixelSize(R.styleable.CirclePercentage_centerTextSize, 0);
        mCircleRadius = typedArray.getDimensionPixelSize(R.styleable.CirclePercentage_circleRadius, 0);
        mIndicate = typedArray.getString(R.styleable.CirclePercentage_centerTextIndicate);
        arcStartColor = typedArray.getColor(R.styleable.CirclePercentage_arcStartColor, 0x1212F6);
        arcEndColor = typedArray.getColor(R.styleable.CirclePercentage_arcEndColor, 0xF212C6);
        typedArray.recycle();

        initPaint();

    }

    private void initPaint() {

        mCenterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterCirclePaint.setStyle(Paint.Style.FILL);
        mCenterCirclePaint.setStrokeWidth(mArcWidth);
        mCenterCirclePaint.setColor(mArcBackgroundSelectColor);

        arcCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcCirclePaint.setStyle(Paint.Style.STROKE);
        arcCirclePaint.setStrokeWidth(mArcWidth);
        arcCirclePaint.setColor(mArcBackgroundSelectColor - 0x50000000);
        arcCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(mArcWidth);
        arcPaint.setColor(arcStartColor);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);

        centerTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerTextPaint.setStyle(Paint.Style.STROKE);
        centerTextPaint.setColor(mCenterTextColor);
        centerTextPaint.setTextSize(mCenterTextSize);
        centerTextPaint.setStrokeCap(Paint.Cap.ROUND);

        //圓弧的外接矩形
        arcRectF = new RectF();

        //文字的边界矩形
        textBoundRect = new Rect();

        mCenterCircleRect = new RectF();
        mRateOffset = (float) (mArcWidth * 100 / (2 * Math.PI * mCircleRadius)) - 1;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureDimension(widthMeasureSpec), measureDimension(heightMeasureSpec));
    }

    private int measureDimension(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = mCircleRadius * 2;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (isSelected()) {
            centerTextPaint.setColor(mCenterTextColorSelected);
            centerTextPaint.setFakeBoldText(true);
        } else {
            centerTextPaint.setColor(mCenterTextColor);
            centerTextPaint.setFakeBoldText(false);
        }

        canvas.rotate(-90, getWidth() / 2, getHeight() / 2);

        arcRectF.set(getWidth() / 2 - mCircleRadius + mArcWidth / 2, getHeight() / 2 - mCircleRadius + mArcWidth / 2,
                getWidth() / 2 + mCircleRadius - mArcWidth / 2, getHeight() / 2 + mCircleRadius - mArcWidth / 2);

        canvas.drawArc(arcRectF, 0, 360, false, arcCirclePaint);

        float rate = mCurRate;
        if (rate < mRateOffset) {
            rate = mCurRate;
        } else if (rate - mRateOffset <= mRateOffset) {
            rate = mRateOffset;
        } else {
            rate = rate - mRateOffset;
        }

        canvas.drawArc(arcRectF, 0, -360 * rate / 100, false, arcPaint);
        if (isSelected()) {
            mCenterCircleRect.set(getWidth() / 2 - mCircleRadius + mArcWidth - 1, getHeight() / 2 - mCircleRadius + mArcWidth - 1,
                    getWidth() / 2 + mCircleRadius - mArcWidth + 1, getHeight() / 2 + mCircleRadius - mArcWidth + 1);

            canvas.drawArc(mCenterCircleRect, 0, 360, true, mCenterCirclePaint);
        }
        canvas.rotate(90, getWidth() / 2, getHeight() / 2);

        int offset = 0;
        if (!TextUtils.isEmpty(mPercentage)) {
            centerTextPaint.getTextBounds(mPercentage, 0, mPercentage.length(), textBoundRect);
            offset = textBoundRect.height() / 2;
            if (!TextUtils.isEmpty(mIndicate)) {
                offset = 0;
            }
            canvas.drawText(mPercentage, getWidth() / 2 - textBoundRect.width() / 2, getHeight() / 2 + offset, centerTextPaint);
        }

        if (!TextUtils.isEmpty(mIndicate)) {
            centerTextPaint.getTextBounds(mIndicate, 0, mIndicate.length(), textBoundRect);
            canvas.drawText(mIndicate, getWidth() / 2 - textBoundRect.width() / 2, getHeight() / 2 + offset + textBoundRect.height(), centerTextPaint);
        }

    }

    public void setPercentageText(String indicate, String percentage, final int rate) {
        mIndicate = indicate;
        mPercentage = percentage;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mCurRate, rate);
        valueAnimator.setDuration(Math.abs(mCurRate - rate) * 30);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Integer animatedValue = (Integer) valueAnimator.getAnimatedValue();
                if (animatedValue != null) {
                    mCurRate = animatedValue;
                }
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.start();
    }
}
