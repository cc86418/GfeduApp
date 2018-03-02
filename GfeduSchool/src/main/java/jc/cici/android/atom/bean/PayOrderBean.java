package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 支付订单实体
 * Created by atom on 2017/9/14.
 */

public class PayOrderBean {

    // 订单id
    private int Order_PKID;
    // 订单编号
    private String Order_Code;
    // 订单总价
    private float Order_Money;
    // 欠费金额
    private float Order_Arrearage;
    // 订单状态(-1:已取消 0:未支付 1:欠费 2:已完成)
    private int Order_State;
    // 下单时间
    private String Order_CreateTime;
    // 是否允许多笔支付
    private int Order_IsMPayment;
    // 最小支付金额
    private float Order_MinPay;
    private ArrayList<String> ProductNames;

    public ArrayList<String> getProductNames() {
        return ProductNames;
    }

    public void setProductNames(ArrayList<String> productNames) {
        ProductNames = productNames;
    }

    public float getOrder_MinPay() {
        return Order_MinPay;
    }

    public void setOrder_MinPay(float order_MinPay) {
        Order_MinPay = order_MinPay;
    }

    public int getOrder_PKID() {
        return Order_PKID;
    }

    public void setOrder_PKID(int order_PKID) {
        Order_PKID = order_PKID;
    }

    public String getOrder_Code() {
        return Order_Code;
    }

    public void setOrder_Code(String order_Code) {
        Order_Code = order_Code;
    }

    public float getOrder_Money() {
        return Order_Money;
    }

    public void setOrder_Money(float order_Money) {
        Order_Money = order_Money;
    }

    public float getOrder_Arrearage() {
        return Order_Arrearage;
    }

    public void setOrder_Arrearage(float order_Arrearage) {
        Order_Arrearage = order_Arrearage;
    }

    public int getOrder_State() {
        return Order_State;
    }

    public void setOrder_State(int order_State) {
        Order_State = order_State;
    }

    public String getOrder_CreateTime() {
        return Order_CreateTime;
    }

    public void setOrder_CreateTime(String order_CreateTime) {
        Order_CreateTime = order_CreateTime;
    }

    public int getOrder_IsMPayment() {
        return Order_IsMPayment;
    }

    public void setOrder_IsMPayment(int order_IsMPayment) {
        Order_IsMPayment = order_IsMPayment;
    }
}
