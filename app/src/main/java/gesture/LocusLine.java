package gesture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import com.example.utils.SizeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 轨迹线，显示绘制密码图案的轨迹
 * Created by hook on 2015/1/7.
 */
public class LocusLine extends View {

    /**
     * 声明画笔，画布，位图
     */
    private Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;

    /**
     * 点和线
     */
    private GesturePoint currentPoint;
    private List<GesturePoint> points;
    private List<Pair<GesturePoint, GesturePoint>> lines;

    /**
     * 结果处理
     */
    private StringBuilder gesture;
    private GestureView.Listener listener;

    public LocusLine(Context ctx) {
        super(ctx);
    }

    public LocusLine(Context ctx, List<GesturePoint> points, GestureView.Listener l) {
        super(ctx);

        /**
         * 初始化画笔画布
         */
        paint = new Paint(Paint.DITHER_FLAG);
        bitmap = Bitmap.createBitmap(SizeUtil.getScreenWidth(),
                SizeUtil.getScreenHeight(),
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas();
        canvas.setBitmap(bitmap);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(Color.rgb(0xFD, 0x53, 0x53));
        paint.setAntiAlias(true);

        this.points = points;
        this.listener = l;
        lines = new ArrayList<>();
        gesture = new StringBuilder();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 声明起点坐标
                int movX = (int) event.getX();
                int movY = (int) event.getY();
                currentPoint = getPointAt(movX, movY);
                if (currentPoint != null) {
                    currentPoint.setHeightLighted(true);
                    gesture.append(currentPoint.getNum());
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                clearScreenAndDrawLines();
                int x = (int) event.getX();
                int y = (int) event.getY();
                GesturePoint point = getPointAt(x, y);
                if (currentPoint == null) {
                    currentPoint = point;
                    if (currentPoint != null) {
                        currentPoint.setHeightLighted(true);
                        gesture.append(currentPoint.getNum());
                    }
                } else {
                    canvas.drawLine(currentPoint.getCentreX(),
                            currentPoint.getCentreY(), x, y, paint);
                    if (point != null && !point.isHeightLighted()) {
                        lines.add(new Pair<>(currentPoint, point));
                        currentPoint = point;
                        currentPoint.setHeightLighted(true);
                        gesture.append(currentPoint.getNum());
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (listener != null &&
                        !TextUtils.isEmpty(gesture.toString())) {
                    listener.handleGesture(gesture.toString());
                }
                gesture.setLength(0);
                lines.clear();
                clearScreenAndDrawLines();
                for (GesturePoint p : points) {
                    p.setHeightLighted(false);
                }
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    private GesturePoint getPointAt(int x, int y) {
        for (GesturePoint point : points) {
            if (x >= point.getLeftX() && x < point.getRightX() &&
                    y >= point.getTopY() && y < point.getBottomY()) {
                return point;
            }
        }
        return null;
    }

    private void clearScreenAndDrawLines() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (Pair<GesturePoint, GesturePoint> pair : lines) {
            canvas.drawLine(pair.first.getCentreX(), pair.first.getCentreY(),
                    pair.second.getCentreX(), pair.second.getCentreY(), paint);
        }
    }
}