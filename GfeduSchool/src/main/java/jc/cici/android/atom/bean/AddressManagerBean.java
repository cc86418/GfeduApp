package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 地址管理实体类
 * Created by atom on 2017/8/23.
 */

public class AddressManagerBean {

    // 地址数量
   private int AddressListCount;
    private ArrayList<AddressBean> AddressList;

    public int getAddressListCount() {
        return AddressListCount;
    }

    public void setAddressListCount(int addressListCount) {
        AddressListCount = addressListCount;
    }

    public ArrayList<AddressBean> getAddressList() {
        return AddressList;
    }

    public void setAddressList(ArrayList<AddressBean> addressList) {
        AddressList = addressList;
    }
}
