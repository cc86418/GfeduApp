package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 套餐内容实体
 * Created by atom on 2017/8/30.
 */

public class PackageProduct {

    private int OrderProduct_PKID;
    private String OrderProduct_Code;
    private int Product_Module;
    private int Product_ID;
    private String Product_Name;
    private int Product_Count;
    private float Product_Money;
    private float Product_PayMoney;
    private float Product_OriginalPrice;
    private int OrderProduct_State;
    private int Order_BuyType;
    private String Image;
    private String OpearteDesc;
    private ArrayList<GiftBeam> GiftList;
    private PackageClassType ClassType;
    private PackageExpress Express;

    public int getOrderProduct_PKID() {
        return OrderProduct_PKID;
    }

    public void setOrderProduct_PKID(int orderProduct_PKID) {
        OrderProduct_PKID = orderProduct_PKID;
    }

    public String getOrderProduct_Code() {
        return OrderProduct_Code;
    }

    public void setOrderProduct_Code(String orderProduct_Code) {
        OrderProduct_Code = orderProduct_Code;
    }

    public int getProduct_Module() {
        return Product_Module;
    }

    public void setProduct_Module(int product_Module) {
        Product_Module = product_Module;
    }

    public int getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(int product_ID) {
        Product_ID = product_ID;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public int getProduct_Count() {
        return Product_Count;
    }

    public void setProduct_Count(int product_Count) {
        Product_Count = product_Count;
    }

    public float getProduct_Money() {
        return Product_Money;
    }

    public void setProduct_Money(float product_Money) {
        Product_Money = product_Money;
    }

    public float getProduct_PayMoney() {
        return Product_PayMoney;
    }

    public void setProduct_PayMoney(float product_PayMoney) {
        Product_PayMoney = product_PayMoney;
    }

    public float getProduct_OriginalPrice() {
        return Product_OriginalPrice;
    }

    public void setProduct_OriginalPrice(float product_OriginalPrice) {
        Product_OriginalPrice = product_OriginalPrice;
    }

    public int getOrderProduct_State() {
        return OrderProduct_State;
    }

    public void setOrderProduct_State(int orderProduct_State) {
        OrderProduct_State = orderProduct_State;
    }

    public int getOrder_BuyType() {
        return Order_BuyType;
    }

    public void setOrder_BuyType(int order_BuyType) {
        Order_BuyType = order_BuyType;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getOpearteDesc() {
        return OpearteDesc;
    }

    public void setOpearteDesc(String opearteDesc) {
        OpearteDesc = opearteDesc;
    }

    public ArrayList<GiftBeam> getGiftList() {
        return GiftList;
    }

    public void setGiftList(ArrayList<GiftBeam> giftList) {
        GiftList = giftList;
    }

    public PackageClassType getClassType() {
        return ClassType;
    }

    public void setClassType(PackageClassType classType) {
        ClassType = classType;
    }

    public PackageExpress getExpress() {
        return Express;
    }

    public void setExpress(PackageExpress express) {
        Express = express;
    }
}
