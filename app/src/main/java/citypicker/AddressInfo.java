package citypicker;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 地址信息
 */
public class AddressInfo implements Parcelable {


    /**
     * cityId : 1
     * cityName : 伊犁
     * creator : user
     * detailAddress : 西湖区同仁精华大厦1008
     * districtId : 1
     * districtName : 伊犁
     * id : 1
     * isDefault : false
     * modifier : user
     * provinceId : 1
     * provinceName : 新疆
     * receiverName : 张三
     * receiverPhone : 13812345678
     * sortNumber : 77
     * userId : 1
     */

    private int cityId;
    private String cityName;
    private String creator;
    private String detailAddress;
    private int districtId;
    private String districtName;
    private int id;
    private boolean isDefault;
    private String modifier;
    private int provinceId;
    private String provinceName;
    private String receiverName;
    private String receiverPhone;
    private int sortNumber;
    private String userId;

    public AddressInfo(){}

    public AddressInfo(Parcel in) {
        cityId = in.readInt();
        cityName = in.readString();
        creator = in.readString();
        detailAddress = in.readString();
        districtId = in.readInt();
        districtName = in.readString();
        id = in.readInt();
        isDefault = in.readByte() != 0;
        modifier = in.readString();
        provinceId = in.readInt();
        provinceName = in.readString();
        receiverName = in.readString();
        receiverPhone = in.readString();
        sortNumber = in.readInt();
        userId = in.readString();
    }

    public static final Creator<AddressInfo> CREATOR = new Creator<AddressInfo>() {
        @Override
        public AddressInfo createFromParcel(Parcel in) {
            return new AddressInfo(in);
        }

        @Override
        public AddressInfo[] newArray(int size) {
            return new AddressInfo[size];
        }
    };

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public int getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(int sortNumber) {
        this.sortNumber = sortNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cityId);
        dest.writeString(cityName);
        dest.writeString(creator);
        dest.writeString(detailAddress);
        dest.writeInt(districtId);
        dest.writeString(districtName);
        dest.writeInt(id);
        dest.writeByte((byte) (isDefault ? 1 : 0));
        dest.writeString(modifier);
        dest.writeInt(provinceId);
        dest.writeString(provinceName);
        dest.writeString(receiverName);
        dest.writeString(receiverPhone);
        dest.writeInt(sortNumber);
        dest.writeString(userId);
    }
}
