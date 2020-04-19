package citypicker;

public class Address {


    /**
     * address : 湖北省
     * addressLevel : 1
     * id : 1
     * parentAddressId : 1
     * postCode : 100032
     */

    private String address;
    private int addressLevel;
    private int id;
    private int parentAddressId;
    private int postCode;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAddressLevel() {
        return addressLevel;
    }

    public void setAddressLevel(int addressLevel) {
        this.addressLevel = addressLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentAddressId() {
        return parentAddressId;
    }

    public void setParentAddressId(int parentAddressId) {
        this.parentAddressId = parentAddressId;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

}
