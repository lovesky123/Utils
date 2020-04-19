package crumbview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.utils.R;

import java.util.Vector;

public class CrumbViewEx extends HorizontalScrollView implements ISkinConfig {

    protected final static int MSG_DATA_CHANGE = 10001;
//    protected final static String s_colRoot = "#89817B";
//    protected final static String s_colItem = "#3996fe";

    private int m_iCode = 10000;                                //编码自增值
    private Vector<CrumbItem> vecItems = new Vector<>();        //面包屑数据列表
    private CrumbItemListener m_pItemClickListener;             //面包屑点击事件监听
    private LinearLayout mContainerLayout;            //布局容器
    private int density;                                        //适配不同分辨率的手机
    protected Handler mHandler;
    private Context mContext;

    private boolean mNeedTransSkin;

    public CrumbViewEx(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public CrumbViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public CrumbViewEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initView();
    }

    //初始化界面
    private void initView() {
        mContainerLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mContainerLayout.setLayoutParams(params);
        density = (int) getResources().getDisplayMetrics().density;
        addView(mContainerLayout);
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case MSG_DATA_CHANGE:
                            handleDataChange();
                            break;
                        default:
                            break;
                    }
                }
            };
        }
        setHorizontalScrollBarEnabled(false);
    }

    //产生新的code
    private String IncreaseCode() {
        m_iCode++;
        return String.format("%d", m_iCode);
    }

    //更新界面
    @SuppressWarnings("deprecation")
    private void handleDataChange() {
        int iItemNum = vecItems.size();
        int iViewNum = mContainerLayout.getChildCount();

        int iLayoutWidth = 0;
        for (int i = 0; i < iItemNum; i++) {
            CrumbItem pItem = vecItems.get(i);
            //增加view
            if (i >= iViewNum) {
                final BreadView itemView = new BreadView(getContext());
                itemView.setEnd(false);
                itemView.setRoot(false);
                String charSet = pItem.mName;
                if (i == 0) {
                    itemView.setRoot(true);
                }

                if (i == iItemNum - 1 && i != 0) {
                    itemView.setEnd(true);
                }
                itemView.setText(charSet);//设置面包屑名字
                itemView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.text_crumb_size));
                itemView.setPadding(density * 30, 0, density * 30, 0);
                itemView.resetColor(isNeedTransSkin());
                TextPaint textPaint = itemView.getPaint();
                int with = (int) Layout.getDesiredWidth(itemView.getText().toString(), 0, itemView.getText().length(), textPaint);

                if (i == 0) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    itemView.setLayoutParams(params);
                    itemView.setId(i);//设置控件的ID
                } else {
                    BreadView child = (BreadView) mContainerLayout.getChildAt(i - 1);
                    RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    params1.addRule(RelativeLayout.ALIGN_LEFT, child.getId());
                    params1.leftMargin = density * 5;
                    itemView.setLayoutParams(params1);
                    itemView.setId(i);//设置控件的ID
                }

                iLayoutWidth = with + 2 * density * 30;

                itemView.setTag(pItem);
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (m_pItemClickListener != null) {
                            itemView.setEnd(true);
                            itemView.setRoot(false);
                            itemView.resetColor(isNeedTransSkin());
//                            Castable<CrumbItem> cast = new Castable<>();
                            CrumbItem item = (CrumbItem) v.getTag();
                            if (item != null) {
                                boolean bNeedUpdate = false;
                                for (int i = vecItems.size() - 1; i >= 0; i--) {
                                    CrumbItem delItem = vecItems.get(i);
                                    if (delItem.mCode.compareTo(item.mCode) == 0) {
                                        if (bNeedUpdate) {
                                            notifyDataChange();
                                        }
                                        break;
                                    }
                                    bNeedUpdate = true;
                                    vecItems.remove(delItem);
                                }
                                if (bNeedUpdate) {
                                    m_pItemClickListener.onCrumbItemClick(item);
                                }
                            }

                        }
                    }
                });
                mContainerLayout.addView(itemView);
            } else {
                BreadView child = (BreadView) mContainerLayout.getChildAt(i);
                child.setEnd(false);
                child.setRoot(false);
                if (i == 0) {
                    child.setRoot(true);
                }
                if (i == iItemNum - 1 && i != 0) {
                    child.setEnd(true);
                }
                child.resetColor(isNeedTransSkin());
            }
        }
        //移除多余的view
        iViewNum = mContainerLayout.getChildCount();
        while (iViewNum > iItemNum) {
            mContainerLayout.removeViewAt(iViewNum - 1);
            iViewNum--;
        }

        // 滑动到最后一个
        post(new Runnable() {
            @Override
            public void run() {
                fullScroll(ScrollView.FOCUS_RIGHT);
            }
        });
    }

    public void setClickListener(CrumbItemListener listener) {
        m_pItemClickListener = listener;
    }

    /**
     * 更新名称
     *
     * @param strCode 唯一编码, addItem时产生
     * @param strName 名称
     */
    public void updateName(String strCode, String strName) {
        for (CrumbItem item : vecItems) {
            if (item.mCode.compareTo(strCode) == 0) {
                item.mName = strName;
                item.mIsChange = true;
                notifyDataChange();
                break;
            }
        }
    }

    /**
     * 更新名称
     *
     * @param strCode 节点编码
     * @param strName 名称
     */
    public void updateNameByNodeCode(String strCode, String strName) {
        for (CrumbItem item : vecItems) {
            if (item.mNodeCode.compareTo(strCode) == 0) {
                item.mName = strName;
                item.mIsChange = true;
                notifyDataChange();
                break;
            }
        }
    }

    /**
     * 添加面包屑
     *
     * @param strNodeCode 节点编码，回调给上层用
     * @param strName     名称
     * @return 唯一编码
     */
    public String addItem(String strNodeCode, String strName) {
        String strCode = IncreaseCode();
        CrumbItem item = new CrumbItem();
        item.mCode = strCode;
        item.mName = strName;
        item.mNodeCode = strNodeCode;
        vecItems.add(item);
        notifyDataChange();
        return strCode;
    }

    /**
     * 清空数据
     */
    public void clear() {
        vecItems.clear();
    }

    /**
     * 更新界面
     */
    public void notifyDataChange() {
        Message message = mHandler.obtainMessage(MSG_DATA_CHANGE);
        message.what = MSG_DATA_CHANGE;
        message.arg1 = 0;
        message.arg2 = 0;
        mHandler.sendMessage(message);
    }

    @Override
    public boolean isNeedTransSkin() {
        return mNeedTransSkin;
    }

    @Override
    public void setNeedTransSkin(boolean needTransSkin) {
        this.mNeedTransSkin = needTransSkin;
    }

    public class CrumbItem {
        public String mCode = "";
        public String mName = "";
        public String mNodeCode = "";
        public boolean mIsChange = false;
    }

    public interface CrumbItemListener {
        public void onCrumbItemClick(CrumbItem item);
    }
}
