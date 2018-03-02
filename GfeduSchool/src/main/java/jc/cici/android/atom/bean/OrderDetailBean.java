package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 *
 * Created by atom on 2017/9/4.
 */

public class OrderDetailBean {

    // 订单价格
    private float PaymentMoney;
    // 最外层列表订单
    private AllOrderBean Order;
    // 子订单
    private ArrayList<ChildOrder> ChildOrderList;
    // 支付列表
    private ArrayList<PayMent> PaymentList;

    public float getPaymentMoney() {
        return PaymentMoney;
    }

    public void setPaymentMoney(float paymentMoney) {
        PaymentMoney = paymentMoney;
    }

    public AllOrderBean getOrder() {
        return Order;
    }

    public void setOrder(AllOrderBean order) {
        Order = order;
    }

    public ArrayList<ChildOrder> getChildOrderList() {
        return ChildOrderList;
    }

    public void setChildOrderList(ArrayList<ChildOrder> childOrderList) {
        ChildOrderList = childOrderList;
    }

    public ArrayList<PayMent> getPaymentList() {
        return PaymentList;
    }

    public void setPaymentList(ArrayList<PayMent> paymentList) {
        PaymentList = paymentList;
    }
}
