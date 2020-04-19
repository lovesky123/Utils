package banner;

/**
 * @author wulimin
 * @function MZHolderCreator
 * @time 2018/8/15 下午12:00
 */


public interface MZHolderCreator<VH extends MZViewHolder> {
    /**
     * 创建ViewHolder
     *
     * @return
     */
    public VH createViewHolder();
}
