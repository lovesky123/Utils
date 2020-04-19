package widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.utils.R;

/**
 * 自定义圆形imageView
 */
public class RoundImageView extends ImageView {

    private int mBorderThickness = 0;

    private Context mContext;

    private int defaultColor = 0xFFFFFFFF; //不透明白底

    // 自定义边框外圈颜色
    private int mBorderOutsideColor = 0;
    // 自定义边框内圈颜色
    private int mBorderInsideColor = 0;
    // 控件宽度
    private int defaultWidth = 0;
    // 控件长度
    private int defaultHeight = 0;

    public RoundImageView(Context context) {
        super(context);
        mContext = context;
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setCustomAttributes(attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setCustomAttributes(attrs);
    }

    private void setCustomAttributes(AttributeSet attrs) {
        //找到attars中的检索数组
        TypedArray a = mContext.obtainStyledAttributes(attrs,R.styleable.roundedimageview);
        //边框厚度
        mBorderThickness = a.getDimensionPixelSize(R.styleable.roundedimageview_border_thickness, 1);
        //外边框颜色
        mBorderOutsideColor = a.getColor(R.styleable.roundedimageview_border_outside_color,defaultColor);
        //内边框颜色
        mBorderInsideColor = a.getColor(R.styleable.roundedimageview_border_inside_color, defaultColor);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable() ;
        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        this.measure(0, 0);
        if (drawable.getClass() == NinePatchDrawable.class)
            return;
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        if (defaultWidth == 0) {
            defaultWidth = getWidth();
        }
        if (defaultHeight == 0) {
            defaultHeight = getHeight();
        }
        int radius = 0;
        // 定义画两个边框，分别为外圆边框和内圆边框
        if (mBorderInsideColor != defaultColor && mBorderOutsideColor != defaultColor) {
            //中间要显示的image的半径为总图形半径减去总边框厚度
            radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - 2 * mBorderThickness;
            // 画内圆
            drawCircleBorder(canvas, radius + mBorderThickness / 2,mBorderInsideColor);
            // 画外圆
            drawCircleBorder(canvas, radius + mBorderThickness + mBorderThickness / 2, mBorderOutsideColor);
            // 定义只画一个内边框
        } else if (mBorderInsideColor != defaultColor && mBorderOutsideColor == defaultColor) {
            radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - mBorderThickness;
            drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor);
            // 定义只画一个外边框
        } else if (mBorderInsideColor == defaultColor && mBorderOutsideColor != defaultColor) {
            radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - mBorderThickness;
            drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderOutsideColor);
            // 当内外边框颜色都设为白色时不画边框
        } else {
            radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2;
        }
        Bitmap roundBitmap = getCroppedRoundBitmap(bitmap, radius);
        canvas.drawBitmap(roundBitmap, defaultWidth / 2 - radius, defaultHeight / 2 - radius, null);
    }

    /**
     * 获取裁剪后的圆形图片
     * @param bmp
     * @param radius
     * @return
     */
    public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
        Bitmap scaledSrcBmp;
        int diameter = radius * 2;
        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        int squareWidth = 0, squareHeight = 0;
        int x = 0, y = 0;
        Bitmap squareBitmap;
        if (bmpHeight > bmpWidth) {// 高大于宽
            squareWidth = squareHeight = bmpWidth;
            x = 0;
            y = (bmpHeight - bmpWidth) / 2;
            // 截取正方形图片
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight);
        } else if (bmpHeight < bmpWidth) {// 宽大于高
            squareWidth = squareHeight = bmpHeight;
            x = (bmpWidth - bmpHeight) / 2;
            y = 0;
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,squareHeight);
        } else {
            squareBitmap = bmp;
        }
        if (squareBitmap.getWidth() != diameter || squareBitmap.getHeight() != diameter) {
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,diameter, true);
        } else {
            scaledSrcBmp = squareBitmap;
        }
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
                scaledSrcBmp.getHeight() / 2,
                scaledSrcBmp.getWidth() / 2,
                paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
        //额，释放对象引用，即释放内存，源码作者C++出身无疑，其实在java中这没啥关系
        bmp = null;
        squareBitmap = null;
        scaledSrcBmp = null;
        return output;
    }

    /**
     * 边缘画圆
     */
    private void drawCircleBorder(Canvas canvas, int radius, int color) {
        Paint paint = new Paint();
        //去锯齿 ，使得边缘图像清晰度提升
        paint.setAntiAlias(true);
        //对Bitmap进行滤波，清晰图像
        paint.setFilterBitmap(true);
        //设定使用图像抖动处理,会使绘制出来的图片颜色更加平滑和饱满,图像更加清晰
        paint.setDither(true);
        //设置边框颜色
        paint.setColor(color);
        //设置paint的　style　为STROKE：空心  ，绘制空心圆形
        paint.setStyle(Paint.Style.STROKE);
        //设置paint的外框宽度
        paint.setStrokeWidth(mBorderThickness);
        //x,y分别为圆心的坐标
        canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, radius, paint);
    }

    public void invalidateImage(int color){
        if(color == 0){
            mBorderInsideColor = defaultColor;
        }else{
            mBorderInsideColor = color;
        }
        this.requestLayout();
        this.invalidate();
    }
}
