package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 购物车班型列表内容实体
 * Created by atom on 2017/9/7.
 */

public class ShopSingleCart {

    // 购物车id
    private int CartId;
    // 产品类型 2：班型 5：套餐
    private int ProductModule;
    // 1：现购 2：预购
    private int BuyType;
    // 套餐
    private DoubleCart Model;
    private ArrayList<Gift> GiftList;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public ArrayList<Gift> getGiftList() {
        return GiftList;
    }

    public void setGiftList(ArrayList<Gift> giftList) {
        GiftList = giftList;
    }

    public DoubleCart getModel() {
        return Model;
    }

    public void setModel(DoubleCart model) {
        Model = model;
    }

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



}
