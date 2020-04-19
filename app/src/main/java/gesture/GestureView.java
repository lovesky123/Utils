package gesture;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.utils.R;
import com.example.utils.SizeUtil;

import java.util.ArrayList;
import java.util.List;

import widget.RoundImageView;

/**
 * 密码图案view，主要是作为9个点的容器
 * Created by hook on 2015/1/5.
 */
public class GestureView extends ViewGroup {

    private Context ctx;
    private int size;                          // 该view的大小
    private int padding;                       // 边缘点(左坐标)与边缘之间的距离
    private int space;                         // 两点之间距离(圆心)
    private List<GesturePoint> gesturePoints;  // 九个点的集合
    private LocusLine locusLine;               // 划过点的线

    /**
     * @param ctx    上下文环境
     *
     * @param weight 该view在屏幕中占据的重量为1，总重量
     */
    public GestureView(Context ctx, double weight, Listener l) {
        super(ctx);
        this.ctx = ctx;
        size = (int) (SizeUtil.getScreenWidth() / weight);
        padding = size / 18;
        space = size / 3;
        gesturePoints = new ArrayList<>();
        addPoints();
        locusLine = new LocusLine(ctx, gesturePoints, l);
    }

    private void addPoints() {
        for (int i = 0; i < 9; i++) {
            /**
             * 将点的图片添加到该容器
             */
            RoundImageView img = new RoundImageView(ctx);
            img.setImageResource(R.mipmap.point);
            this.addView(img);

            /**
             * 创建GesturePoint并添加到gesturePoints
             */
            int row = i / 3;
            int col = i % 3;

            int leftX = col * space + padding;
            int topY = row * space + padding;
            int rightX = col * space + space - padding;
            int bottomY = row * space + space - padding;

            GesturePoint point = new GesturePoint(
                    leftX, rightX, topY, bottomY, img, i + 1);
            gesturePoints.add(point);
        }
    }

    public void setParentView(ViewGroup parent) {
        // 得到屏幕的宽度
        LayoutParams layoutParams = new LayoutParams(size, size);
        this.setLayoutParams(layoutParams);
        locusLine.setLayoutParams(layoutParams);
        parent.addView(this);
        parent.addView(locusLine);
    }

    public void setGestureThumbnailDefaultBackground() {
        for (GesturePoint point : gesturePoints) {
            point.setBackground(R.mipmap.point);
        }
    }

    public void setGestureThumbnailBackground(int resourceId, String gesture) {
        for (int i = 0; i < 9; i++) {
            if (gesture.contains(gesturePoints.get(i).getNum() + "")) {
                gesturePoints.get(i).setBackground(resourceId);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < 9; i++) {
            int row = i / 3;
            int col = i % 3;
            int leftX = col * space + padding;
            int topY = row * space + padding;
            int rightX = col * space + space - padding;
            int bottomY = row * space + space - padding;
            View v = getChildAt(i);
            v.layout(leftX, topY, rightX, bottomY);
        }
    }

    public interface Listener {
        void handleGesture(String gesture);
    }
}