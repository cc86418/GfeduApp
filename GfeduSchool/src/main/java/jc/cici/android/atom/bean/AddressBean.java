package jc.cici.android.atom.bean;

/**
 * 地址内容实体
 * Created by atom on 2017/9/6.
 */

public class AddressBean {

    // 地址id
    private int AddressPKID;
    // 收货人姓名
    private String AddressName;
    // 收货人手机号
    private String AddressMobile;
    // 邮编
    private String AddressPostcode;
    // 省
    private String AddressProvince;
    // 市
    private String AddressCity;
    // 详细地址
    private String AddressDetail;
    // 用户id
    private int AddressStudentID;
    // 是否默认(1:默认，2:非默认)
    private int AddressIsDefault;
    // 邮箱
    private String AddressEmail;

    public int getAddressPKID() {
        return AddressPKID;
    }

    public void setAddressPKID(int addressPKID) {
        AddressPKID = addressPKID;
    }

    public String getAddressName() {
        return AddressName;
    }

    public void setAddressName(String addressName) {
        AddressName = addressName;
    }

    public String getAddressMobile() {
        return AddressMobile;
    }

    public void setAddressMobile(String addressMobile) {
        AddressMobile = addressMobile;
    }

    public String getAddressPostcode() {
        return AddressPostcode;
    }

    public void setAddressPostcode(String addressPostcode) {
        AddressPostcode = addressPostcode;
    }

    public String getAddressProvince() {
        return AddressProvince;
    }

    public void setAddressProvince(String addressProvince) {
        AddressProvince = addressProvince;
    }

    public String getAddressCity() {
        return AddressCity;
    }

    public void setAddressCity(String addressCity) {
        AddressCity = addressCity;
    }

    public String getAddressDetail() {
        return AddressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        AddressDetail = addressDetail;
    }

    public int getAddressStudentID() {
        return AddressStudentID;
    }

    public void setAddressStudentID(int addressStudentID) {
        AddressStudentID = addressStudentID;
    }

    public int getAddressIsDefault() {
        return AddressIsDefault;
    }

    public void setAddressIsDefault(int addressIsDefault) {
        AddressIsDefault = addressIsDefault;
    }

    public String getAddressEmail() {
        return AddressEmail;
    }

    public void setAddressEmail(String addressEmail) {
        AddressEmail = addressEmail;
    }
}
