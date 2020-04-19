package gesture;

import com.example.utils.R;
import widget.RoundImageView;

/**
 * 手势密码的点，包括小点缩略图
 * 不是三点，是三个三点
 */
public class GesturePoint {
    private int leftX, rightX, topY, bottomY;  // 左右、上下的坐标，用来固定点的位置和大小
    private int centreX, centreY;              // 圆心坐标，用来定位两点之间的连线
    private RoundImageView img;               // 点对应的ImageView控件
    private boolean heightLighted;             // 是否高亮(划过的时候高亮)
    private int num;                           // 点所代表的数字

    public GesturePoint(int leftX, int rightX, int topY, int bottomY, RoundImageView img, int num) {
        this.leftX = leftX;
        this.rightX = rightX;
        centreX = (leftX + rightX) / 2;
        this.topY = topY;
        this.bottomY = bottomY;
        centreY = (topY + bottomY) / 2;
        this.img = img;
        this.num = num;
    }

    public int getLeftX() {
        return leftX;
    }

    public void setLeftX(int leftX) {
        this.leftX = leftX;
    }

    public int getRightX() {
        return rightX;
    }

    public void setRightX(int rightX) {
        this.rightX = rightX;
    }

    public int getTopY() {
        return topY;
    }

    public void setTopY(int topY) {
        this.topY = topY;
    }

    public int getBottomY() {
        return bottomY;
    }

    public void setBottomY(int bottomY) {
        this.bottomY = bottomY;
    }

    public int getCentreX() {
        return centreX;
    }

    public void setCentreX(int centreX) {
        this.centreX = centreX;
    }

    public int getCentreY() {
        return centreY;
    }

    public void setCentreY(int centreY) {
        this.centreY = centreY;
    }

    public RoundImageView getImg() {
        return img;
    }

    public void setImg(RoundImageView img) {
        this.img = img;
    }

    public boolean isHeightLighted() {
        return heightLighted;
    }

    public void setHeightLighted(boolean heightLighted) {
        this.heightLighted = heightLighted;
        if (heightLighted) {
            img.setImageResource(R.mipmap.point_on);
        } else {
            img.setImageResource(R.mipmap.point);
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setBackground(int resourceId) {
        img.setImageResource(resourceId);
    }

    @Override
    public String toString() {
        return "GesturePoint{" +
                "leftX=" + leftX +
                ", rightX=" + rightX +
                ", topY=" + topY +
                ", bottomY=" + bottomY +
                ", centreX=" + centreX +
                ", centreY=" + centreY +
                ", img=" + img +
                ", heightLighted=" + heightLighted +
                ", num=" + num +
                '}';
    }
}