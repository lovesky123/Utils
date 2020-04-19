package widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.utils.R;
import com.example.utils.Utils;

public class TitleBar extends FrameLayout implements View.OnClickListener {

    public static final int RIGHTCLICK = 868;
    public static final int LEFTCLICK = 610;
    public static final int CENTER = 741;

    private boolean mIsShowContainsLayout;//是否展示中间容器布局   当titlebar 中间展示的不为文字时 ，在容器内添加展示的界面
    private String mTextLeftStr;//左边文字
    private String mTextRightStr;//右边文字
    private String mTextCenterStr;//中间文字
    private int mImgLeft;//左边图片
    private int mImgRight;//右边图片
    private float mItemSize;//左右两边文字大小
    //    private int mItemTextColor;//左右两边文字颜色
    private float mTextCenterSize;//中间文字大小
    private int mTextLeftColor;//左边文字颜色
    private int mTextRightColor;//右边文字颜色
    private int mTextCenterColor;//中间文字颜色
    private int mBaseBackGround;//base背景颜色


    private RelativeLayout rly_titlebar;//base
    private TextView title_bar_leftTxt;
    private ImageView title_bar_leftImg;
    private ImageView title_bar_rightImg;
    private TextView title_bar_rightTxt;
    private TextView title_bar_centerTxt;
    private FrameLayout fly_title_contains;


    private OnClickListener mOnClickListener;

    Context mContext;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.currency_title_bar_layout, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar, defStyleAttr, 0);
        mIsShowContainsLayout = typedArray.getBoolean(R.styleable.TitleBar_containsLayout, false);
        mTextLeftStr = typedArray.getString(R.styleable.TitleBar_textLeftStr);
        mTextRightStr = typedArray.getString(R.styleable.TitleBar_textRightStr);
        mTextCenterStr = typedArray.getString(R.styleable.TitleBar_textCenterStr);
        mImgLeft = typedArray.getResourceId(R.styleable.TitleBar_imgLeft, -1);
        mImgRight = typedArray.getResourceId(R.styleable.TitleBar_imgRight, -1);
        mItemSize = typedArray.getDimension(R.styleable.TitleBar_itemSize, 14);
        //        mItemTextColor = typedArray.getColor(R.styleable.TitleBar_itemColor, ContextCompat.getColor(context, R.color.txt_normal));
        mTextCenterSize = typedArray.getDimension(R.styleable.TitleBar_textCenterSizeT, 18);
        mTextLeftColor = typedArray.getColor(R.styleable.TitleBar_textLeftColor, ContextCompat.getColor(context, R.color.text_color_8a000000));
        mTextRightColor = typedArray.getColor(R.styleable.TitleBar_textRightColor, ContextCompat.getColor(context, R.color.text_color_8a000000));
        mTextCenterColor = typedArray.getColor(R.styleable.TitleBar_textCenterColorT, ContextCompat.getColor(context, R.color.text_color_8a000000));
        mBaseBackGround = typedArray.getColor(R.styleable.TitleBar_baseBackGround, Color.WHITE);

        initView();
        initData();
        initListener();

        typedArray.recycle();
    }


    public View addContains() {
        return null;
    }

    private void initListener() {
        title_bar_leftTxt.setOnClickListener(this);
        title_bar_rightTxt.setOnClickListener(this);
        title_bar_leftImg.setOnClickListener(this);
        title_bar_rightImg.setOnClickListener(this);
    }

    private void initData() {
        //中间容器
        if (mIsShowContainsLayout) {
            fly_title_contains.setVisibility(VISIBLE);
            title_bar_centerTxt.setVisibility(GONE);
            fly_title_contains.addView(addContains());
        } else {
            title_bar_centerTxt.setVisibility(VISIBLE);
            fly_title_contains.setVisibility(GONE);
        }

        //左侧文字
        if (Utils.isNullBool(mTextLeftStr)) {
            title_bar_leftTxt.setText(mTextLeftStr);
        } else {
            title_bar_leftTxt.setVisibility(GONE);
        }

        //右侧文字
        if (Utils.isNullBool(mTextRightStr)) {
            title_bar_rightTxt.setText(mTextRightStr);
        } else {
            title_bar_rightTxt.setVisibility(GONE);
        }

        //中间文字
        if (Utils.isNullBool(mTextCenterStr)) {
            title_bar_centerTxt.setText(mTextCenterStr);
        } else {
            title_bar_centerTxt.setVisibility(GONE);
        }

        //左侧图片
        if (mImgLeft != -1) {
            title_bar_leftImg.setImageResource(mImgLeft);
        } else {
            title_bar_leftImg.setVisibility(GONE);
        }

        //右侧图片
        if (mImgRight != -1) {
            title_bar_rightImg.setImageResource(mImgRight);
        } else {
            title_bar_rightImg.setVisibility(GONE);
        }

        //设置左右文字大小
        if (mItemSize != 14) {
            title_bar_leftTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mItemSize);
            title_bar_rightTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mItemSize);
        }


        //中间文字大小
        if (mTextCenterSize != 18) {
            title_bar_centerTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextCenterSize);
        }


        //        //两侧文字统一颜色
        //        if (mItemTextColor != R.color.txt_normal) {
        //            title_bar_leftTxt.setTextColor(mItemTextColor);
        //            title_bar_rightTxt.setTextColor(mItemTextColor);
        //        }

        //左侧文字的颜色
        if (title_bar_leftTxt.getVisibility() == VISIBLE) {
            title_bar_leftTxt.setTextColor(mTextLeftColor);
        }

        //右侧文字的颜色
        if (title_bar_rightTxt.getVisibility() == VISIBLE) {
            title_bar_rightTxt.setTextColor(mTextRightColor);
        }

        //中间文字的颜色
        if (title_bar_centerTxt.getVisibility() == VISIBLE) {
            title_bar_centerTxt.setTextColor(mTextCenterColor);
        }

        //title背景颜色
        if (rly_titlebar != null && rly_titlebar.getVisibility() == VISIBLE) {
            rly_titlebar.setBackgroundColor(mBaseBackGround);
        }
    }


    public TitleBar setmTextCenterStr(String mTextCenterStr) {
        if (title_bar_centerTxt != null) {
            title_bar_centerTxt.setVisibility(VISIBLE);
            title_bar_centerTxt.setText(mTextCenterStr);
        }
        return this;
    }

    public TitleBar setmTextRightStr(String mTextRightStr) {
        if (title_bar_rightTxt != null) {
            title_bar_rightTxt.setText(mTextRightStr);
            if (title_bar_rightImg != null) {
                title_bar_rightImg.setVisibility(GONE);
            }
        }
        return this;
    }

    @SuppressLint("NewApi")
    public void setmTextRightColor(int mTextRightColor) {
        if (title_bar_rightTxt != null) {
            title_bar_rightTxt.setTextColor(mContext.getResources().getColor(mTextRightColor));
        }
    }

    public TitleBar setmTextLeftStr(String mTextLeftStr) {
        if (title_bar_leftTxt != null) {
            title_bar_leftTxt.setText(mTextRightStr);
            title_bar_leftTxt.setVisibility(VISIBLE);
            if (title_bar_leftImg != null) {
                title_bar_leftImg.setVisibility(GONE);
            }
        }
        return this;
    }


    public TextView getTitle_bar_leftTxt() {
        if (title_bar_leftTxt != null) {
            return title_bar_leftTxt;
        } else {
            return null;
        }
    }

    public ImageView getTitle_bar_leftImg() {
        if (title_bar_leftImg != null) {
            title_bar_leftImg.setVisibility(VISIBLE);
            return title_bar_leftImg;
        } else {
            return null;
        }
    }

    public ImageView getTitle_bar_rightImg() {
        if (title_bar_rightImg != null) {
            return title_bar_rightImg;
        } else {
            return null;
        }
    }

    public TextView getTitle_bar_rightTxt() {
        if (title_bar_rightTxt != null) {
            return title_bar_rightTxt;
        } else {
            return null;
        }
    }

    public TextView getTitle_bar_centerTxt() {
        if (title_bar_centerTxt != null) {
            return title_bar_centerTxt;
        } else {
            return null;
        }
    }


    private void initView() {
        rly_titlebar = (RelativeLayout) findViewById(R.id.rly_titlebar);
        title_bar_leftTxt = (TextView) findViewById(R.id.title_bar_leftTxt);
        title_bar_leftImg = (ImageView) findViewById(R.id.title_bar_leftImg);
        title_bar_rightImg = (ImageView) findViewById(R.id.title_bar_rightImg);
        title_bar_rightTxt = (TextView) findViewById(R.id.title_bar_rightTxt);
        title_bar_centerTxt = (TextView) findViewById(R.id.title_bar_centerTxt);
        fly_title_contains = (FrameLayout) findViewById(R.id.fly_title_contains);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (R.id.title_bar_leftTxt == vId || R.id.title_bar_leftImg == vId) {
            if (mOnClickListener != null) {
                mOnClickListener.titleBarClick(LEFTCLICK);
            }
        } else if (R.id.title_bar_rightTxt == vId || R.id.title_bar_rightImg == vId) {
            if (mOnClickListener != null) {
                mOnClickListener.titleBarClick(RIGHTCLICK);
            }
        }
    }

    public interface OnClickListener {
        void titleBarClick(int vId);
    }
}
