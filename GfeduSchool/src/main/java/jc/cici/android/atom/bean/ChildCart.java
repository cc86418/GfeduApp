package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 套餐班型之子类班实体
 * Created by atom on 2017/9/7.
 */

public class ChildCart {

    // 购物车订单id
    private int CartId;

    private int ProductModule;
    private int BuyType;
    private SingleClass Model;
    private ArrayList<Gift> GiftList;

    public int getCartId() {
        return CartId;
    }

    public void setCartId(int cartId) {
        CartId = cartId;
    }

    public int getProductModule() {
        return ProductModule;
    }

    public void setProductModule(int productModule) {
        ProductModule = productModule;
    }

    public int getBuyType() {
        return BuyType;
    }

    public void setBuyType(int buyType) {
        BuyType = buyType;
    }

    public SingleClass getModel() {
        return Model;
    }

    public void setModel(SingleClass model) {
        Model = model;
    }

    public ArrayList<Gift> getGiftList() {
        return GiftList;
    }

    public void setGiftList(ArrayList<Gift> giftList) {
        GiftList = giftList;
    }
}
