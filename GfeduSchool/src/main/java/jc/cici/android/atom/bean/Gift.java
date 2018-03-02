package jc.cici.android.atom.bean;

/**
 * 购物车赠送课程
 * Created by atom on 2017/9/7.
 */

public class Gift {

    // 赠品id
    private int PKID;
    // 赠品名称
    private String Name;
    // 赠品价格
    private String Price;
    // 售价
    private String SalePrice;
    // 1：班型  3：商品 4：优惠券 5：套餐
    private int Gift_Type;

    public int getPKID() {
        return PKID;
    }

    public void setPKID(int PKID) {
        this.PKID = PKID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(String salePrice) {
        SalePrice = salePrice;
    }

    public int getGift_Type() {
        return Gift_Type;
    }

    public void setGift_Type(int gift_Type) {
        Gift_Type = gift_Type;
    }
}
