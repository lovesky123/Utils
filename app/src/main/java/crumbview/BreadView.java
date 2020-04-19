package crumbview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.Gravity;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.utils.R;

/**
 * 面包屑item背景
 * <p/>
 * Created by 29149 on 2017/3/22.
 */
public class BreadView extends AppCompatTextView {

    private boolean isRoot = false;
    private boolean isEnd = false;

    private Paint mPaintFill = new Paint();
    private Path mPath = new Path();

    private int mBackgroundColor;

    public BreadView(Context context) {
        super(context);
        init();
    }

    public BreadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BreadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER);
        setBackgroundResource(0);
        resetColor(false);
    }

    @SuppressWarnings("deprecation")
    public void resetColor(boolean isTrans) {
        int mTextColor;
        if (isTrans) {
            if (isEnd && !isRoot) {
                mBackgroundColor = getContext().getResources().getColor(R.color.common_bread_view_end_fill_trans);
                mTextColor = getContext().getResources().getColor(R.color.common_bread_text_end);
            } else {
                mBackgroundColor = getContext().getResources().getColor(R.color.common_bread_view_node_fill_trans);
                mTextColor = getContext().getResources().getColor(R.color.common_bread_text_node);
            }
        } else {
            if (isEnd && !isRoot) {
                mBackgroundColor = getContext().getResources().getColor(R.color.common_bread_view_end_fill_normal);
                mTextColor = getContext().getResources().getColor(R.color.common_bread_text_end);
            } else {
                mBackgroundColor = getContext().getResources().getColor(R.color.common_bread_view_node_fill_normal);
                mTextColor = getContext().getResources().getColor(R.color.common_bread_text_node);
            }
        }
        setTextColor(mTextColor);
        invalidate();
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawView(canvas);
        super.onDraw(canvas);
    }

    private void drawView(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();

        mPaintFill.setAntiAlias(true);
        mPaintFill.setStyle(Paint.Style.FILL);

        mPath.moveTo(0, 0);
        mPath.lineTo(width - height / 4, 0);
        mPath.lineTo(width, height / 2);
        mPath.lineTo(width - height / 4, height);
        mPath.lineTo(0, height);

        if (isRoot) {
            mPaintFill.setColor(mBackgroundColor);
        } else {
            mPaintFill.setColor(mBackgroundColor);
            mPath.lineTo(height / 4f, height / 2f);
        }
        mPath.close();

        canvas.drawPath(mPath, mPaintFill);
    }
}
