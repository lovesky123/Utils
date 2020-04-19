package widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.utils.R;

public class CommonItemView extends LinearLayout {

    private TextView tx_title;
    private TextView tx_content;

    public CommonItemView(Context context) {
        this(context, null, 0);
    }

    public CommonItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonItemView);
        String title = typedArray.getString(R.styleable.CommonItemView_tx_title);
        int contentSize = typedArray.getDimensionPixelSize(R.styleable.CommonItemView_tx_content_size, 16);
        int contentColor = typedArray.getColor(R.styleable.CommonItemView_tx_content_color, Color.BLACK);
        String hint = typedArray.getString(R.styleable.CommonItemView_tx_content_hint);
        typedArray.recycle();
        LayoutInflater.from(context).inflate(R.layout.layout_common_itemview,this);
        initView();
        setLeftText(title);
        setRightTextSize(contentSize);
        setRightTextColor(contentColor);
        tx_content.setHint(hint);
    }

    private void initView() {
        tx_title = (TextView) findViewById(R.id.tx_title);
        tx_content = (TextView) findViewById(R.id.tx_content);
    }

    public void setLeftText(String string) {
        tx_title.setText(string);
    }

    public void setRightText(String string) {
        tx_content.setText(string);
    }

    public void setRightTextSize(float size) {
        tx_content.setTextSize(size);
    }

    private void setContentHint(String hintStr){
        tx_content.setHint(hintStr);
    }

    public void setRightTextColor(int color) {
        tx_content.setTextColor(color);
    }

    public void hideDrawableRight() {
        tx_content.setCompoundDrawables(null,null,null,null);
    }

    public void showDrawableRight() {
        tx_content.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getContext(),R.drawable.icon_arrow_right),null);
    }

    public String getRightText() {
        return tx_content.getText().toString();
    }

    public String getLeftText() {
        return tx_title.getText().toString();
    }
}
