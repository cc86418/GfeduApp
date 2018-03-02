package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 订单内容列表实体
 * Created by atom on 2017/8/30.
 */

public class OrderList {

    // 订单价格
    private float PaymentMoney;
    // 最外层列表订单
    private AllOrderBean Order;
    // 子订单
    private ArrayList<ChildOrder> ChildOrderList;

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
}
