package citypicker;

public abstract class OnCityItemClickListener {
    
    /**
     * 当选择省市区三级选择器时，需要覆盖此方法
     * @param province
     * @param city
     * @param district
     */
    public void onSelected(Address province, Address city, Address district) {
        
    }
    
    /**
     * 取消
     */
    public void onCancel() {
        
    }
}
