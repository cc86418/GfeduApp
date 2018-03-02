package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 购物车实体类
 * Created by atom on 2017/9/7.
 */

public class ShopcartBean {

    // 购物车数量
    private int CartCount;
    // 总原价
    private String TotalPrice;
    // 总售价
    private String TotalSalePrice;
    // 总减额度
    private String TotalMinus;
    // 购物车列表
    private ArrayList<ShopSingleCart> CartList;

    public int getCartCount() {
        return CartCount;
    }

    public void setCartCount(int cartCount) {
        CartCount = cartCount;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getTotalSalePrice() {
        return TotalSalePrice;
    }

    public void setTotalSalePrice(String totalSalePrice) {
        TotalSalePrice = totalSalePrice;
    }

    public String getTotalMinus() {
        return TotalMinus;
    }

    public void setTotalMinus(String totalMinus) {
        TotalMinus = totalMinus;
    }

    public ArrayList<ShopSingleCart> getCartList() {
        return CartList;
    }

    public void setCartList(ArrayList<ShopSingleCart> cartList) {
        CartList = cartList;
    }
}
